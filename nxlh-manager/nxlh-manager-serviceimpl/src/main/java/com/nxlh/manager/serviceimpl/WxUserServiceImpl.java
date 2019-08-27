package com.nxlh.manager.serviceimpl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MessageModel;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.JsonUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbWxuserMapper;
import com.nxlh.manager.model.dbo.TbWxuser;
import com.nxlh.manager.model.dto.VipLowerDTO;
import com.nxlh.manager.model.dto.WebOrderDTO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.dto.WxUserOtherRecordDTO;
import com.nxlh.manager.model.enums.VIPEnums;
import com.nxlh.manager.model.to.UserTO;
import com.nxlh.manager.model.vo.VipConfigJsonModel;
import com.nxlh.manager.service.*;
import com.nxlh.manager.utils.SendPost;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.catalina.User;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.nxlh.manager.rediskey.Keys.SUMMARY_MONTH_NEWWXUSER_COUNT_KEY;
import static com.nxlh.manager.rediskey.Keys.SUMMARY_TODAY_NEWWXUSER_COUNT_KEY;

@Service(interfaceClass = WxUserService.class)
@Slf4j
public class WxUserServiceImpl extends BaseDbCURDSServiceImpl<TbWxuserMapper, TbWxuser, WxUserDTO> implements WxUserService {

    @Autowired
    private WxMaService wxService;

    @Autowired
    @Lazy
    private WxUserOtherRecordService wxUserOtherRecordService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private VipLowerService vipLowerService;

    @Autowired
    private WebOrderService webOrderService;


    @Value("${nxlh.userRedisExprie}")
    private long userRedisExprie = 20L;

    private  static Object locker = new Object();

    @Value("${nxlh.editUserType}")
    private String editUserTypeUrl;

    @Value("${nxlh.checkVipIsInvalidation}")
    private String checkVipIsInvalidationUrl;

    @Override
    public MyResult login(String code) {
        try {

            WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(code);
            var openid = session.getOpenid();
            var sessionkey = session.getSessionKey();
            //加密生成token
            var token = String.format("WXUSER_%s", IDUtils.genUUID());

            var map = new HashMap<String, Object>();
            map.put("openid", openid);
            var wxUser = this.getOne(map);
            //更新或添加用户
            wxUser = this.registerNewUser(wxUser, openid, token);
            wxUser.setSessionKey(sessionkey);

            var nickname = wxUser.getNickname();
            //fix emoji
            wxUser.setNickname(this.emojiConverter.toAlias(wxUser.getNickname()));

            //把当期用户写入缓存,有效期20天
            this.redisService.set(token, wxUser, userRedisExprie, TimeUnit.DAYS);

            var unionIdToken = String.format("WXUSER_UNIOINID_%s", session.getUnionid());
            this.redisService.set(unionIdToken,wxUser);

            wxUser.setSessionKey(null);
            wxUser.setNickname(nickname);
            wxUser.setOpenid(null);
            wxUser.setId(null);
            wxUser.setUnionid(null);
            return MyResult.ok(wxUser);

        } catch (WxErrorException e) {
            log.info("获取微信信息失败:{}", e.getMessage());

        }
        return MyResult.error("系统异常");
    }

    @Override
    public MyResult bindWxUser(Map<String, Object> params) {
        var wxUser = this.getById(params.get("userid").toString());
        if (wxUser == null) {
            return MyResult.error("无效用户");
        }
        //解密获取unionid
        var decode = this.decryWxData(params.get("sessionkey").toString(), params.get("encrypteddata").toString(), params.get("iv").toString());
        wxUser.setNickname(params.get("nickname").toString());
        wxUser.setProvince(params.get("province").toString());
        wxUser.setCity(params.get("city").toString());
        wxUser.setCountry(params.get("country").toString());
        wxUser.setAvatarurl(params.get("avatarurl").toString());
        wxUser.setGender(Integer.parseInt(params.get("gender").toString()));
        wxUser.setUnionid(decode.getUnionId());
        this.updateById(wxUser);

        var unionIdToken = String.format("WXUSER_UNIOINID_%s", decode.getUnionId());
        this.redisService.set(unionIdToken,wxUser);

        //查找当前Unionid的用户，可能有2个
        var unIdUsers = this.list(new HashMap<String, Object>() {{
            put("unionid", decode.getUnionId());
        }}, null, null);

        //只保留小程序的微信用户，网站创建的用户删除
        if (!CollectionUtils.isEmpty(unIdUsers)) {
            //一个是网站用户，一个是小程序
            if (unIdUsers.size() == 2) {
                //删除网站的用户
                unIdUsers.stream().forEach(e -> {
                    if (e.getOpenid() == null || !e.getOpenid().equalsIgnoreCase(wxUser.getOpenid())) {
                        var mpUser = unIdUsers.stream().filter(f->!f.getOpenid().equalsIgnoreCase(e.getOpenid())).findFirst().get();
                       //合并积分、经验
                        e.setVscore(e.getVscore().add(mpUser.getVscore()));
                        e.setExp(e.getExp().add(mpUser.getExp()));
                       this.transactionUtils.transact((a)->{
                           this.updateById(e);
                           this.removeById(e.getId());
                       });
                    }
                });

            } else {
                //网站用户没有openid
                if (unIdUsers.get(0).getOpenid()==null || !unIdUsers.get(0).getOpenid().equalsIgnoreCase(wxUser.getOpenid())) {
                    //删除网站创建的用户
                    this.removeById(unIdUsers.get(0).getId());
                }
            }
        }

        return MyResult.ok();
    }

    /**
     * 网站用户注册，同步
     *
     * @param params
     * @return
     */
    @Override
    public MessageModel syncUser(Map<String, Object> params) {

        synchronized (locker) {
            var token = String.format("WXUSER_UNIOINID_%s", params.get("unionid"));
            var user = (WxUserDTO) this.redisService.get(token);
            if (user == null)
                //内存中没，找数据库
                user = this.getOne(new HashMap<String, Object>() {{
                    put("unionid", params.get("unionid").toString());
                }});
            else {
                return MessageModel.ok(user,"exists");
            }
            //两种情况：1.新用户  2.老用户，没有在小程序中更新unionid
            if (user == null) {
                //数据库没有，新建用户
                user = new WxUserDTO();
                user.setUnionid(params.get("unionid").toString());
                user.setNickname(params.get("nickname").toString());
                user.setProvince(params.get("province").toString());
                user.setCity(params.get("city").toString());
                user.setCountry(params.get("country").toString());
                user.setAvatarurl(params.get("headimgurl").toString());
                user.setGender(Integer.parseInt(params.get("sex").toString()));
                user.setVipid(0);
                user.setVscore(BigDecimal.ZERO);
                this.add(user);
                //基于unioinid存数据
                this.redisService.set(token, user);
                return MessageModel.ok(user,"created");
            } else {
                this.redisService.set(token, user);
                return MessageModel.ok(user,"exists");
            }
        }
    }


    /**
     * 每天凌晨1点减会员经验
     *
     * @return
     */
    @Override
    public boolean checkVipIsInvalidation() throws Exception {

        var vipJson = this.getClass().getClassLoader().getResourceAsStream("props/vip.json");
        var list = JsonUtils.fastJsonToObjArray(new String(vipJson.readAllBytes()), VipConfigJsonModel.class);

        List<VipLowerDTO> modelList = new ArrayList<>();
        //region 用户每天降低经验值
        //获取所有会员微信用户
        var wxUsers = this.baseMapper.getWxuserByVipList(100);
        wxUsers.stream().forEach(e -> {
            var congfig = list.stream().filter(m -> m.getLevel().equals(e.getVipid())).findFirst().get();

            e.setExp(e.getExp().subtract(congfig.getDowngrade()));
            //经验值小于等于0降级
            if (e.getExp().compareTo(congfig.getLower()) < 1) {
                //记录降级数据
                VipLowerDTO vipLowerDTO = new VipLowerDTO();
                vipLowerDTO.setUserid(e.getId());
                vipLowerDTO.setBeforelevel(e.getVipid());
                e.setVipid(congfig.getLevel() - 1);
                e.setExp(congfig.getLower());
                vipLowerDTO.setAfterlevel(e.getVipid());
                modelList.add(vipLowerDTO);
            }
        });
        //endregion
        List<WxUserDTO> wxUserDTOS = this.beanListMap(wxUsers, this.currentDTOClass());
        if (wxUsers != null && wxUsers.size() > 0) {
            boolean result = this.transactionUtils.transact((a) -> {
                this.updateBatchById(wxUserDTOS);
                this.vipLowerService.addBatch(modelList);
            });

            //region 传递数据
            if (result) {
                this.sendWebUser(wxUserDTOS, "days");
            }
            //endregion


            return result;
        }
        return true;
    }

    /**
     * 检查会员3个月内（90天）是否有消费记录，无，则会员自动降1级
     *
     * @return
     */
    @Override
    public boolean monthCheckVipIsInvalidation() throws Exception {

        var vipJson = this.getClass().getClassLoader().getResourceAsStream("props/vip.json");
        var list = JsonUtils.fastJsonToObjArray(new String(vipJson.readAllBytes()), VipConfigJsonModel.class);

        List<VipLowerDTO> modelList = new ArrayList<>();

        //region 三个月无消费记录自动降级
        var startDate = DateUtils.getDayStart(DateUtils.addDay(new Date(), -90));
        List<VipLowerDTO> byUserIds = this.vipLowerService.getByUserIds(startDate);
        List<String> ids = new ArrayList<>();
        if (byUserIds != null && byUserIds.size() > 0) {
            ids = byUserIds.stream().map(e -> e.getUserid()).collect(Collectors.toList());
        } else {
            ids = null;
        }
        var wxUsers = this.baseMapper.getNoOrderRecordsIn3Months(ids, startDate);
        List<WebOrderDTO> overOrderByDate = webOrderService.getOverOrderByDate(startDate);
        List<WxUserDTO> userDTOList = overOrderByDate.stream().map(e -> e.getWxuser()).collect(Collectors.toList());
        wxUsers.removeAll(userDTOList);
        wxUsers.stream().forEach(e -> {
            VipLowerDTO vipLowerDTO = new VipLowerDTO();
            vipLowerDTO.setUserid(e.getId());
            vipLowerDTO.setBeforelevel(e.getVipid());
            e.setVipid(e.getVipid() - 1);
            var config = list.stream().filter(m -> m.getLevel() == e.getVipid()).findFirst().get();
            e.setExp(config.getExp());
            vipLowerDTO.setAfterlevel(e.getVipid());
            modelList.add(vipLowerDTO);
        });
        //endregion
        List<WxUserDTO> wxUserDTOS = this.beanListMap(wxUsers, this.currentDTOClass());
        if (wxUsers != null && wxUsers.size() > 0) {
            boolean result = this.transactionUtils.transact((a) -> {
                this.updateBatchById(wxUserDTOS);
                this.vipLowerService.addBatch(modelList);
            });
            if (result) {
                sendWebUser(wxUserDTOS, "3month");
            }
            return result;
        }
        return true;
    }

    /**
     * 往网站发送数据
     *
     * @param wxUserDTOS
     */
    private void sendWebUser(List<WxUserDTO> wxUserDTOS, String keys) {
        List<UserTO> userDTOS = new ArrayList<>();
        for (WxUserDTO e : wxUserDTOS) {
            UserTO userTO = new UserTO();
            userTO.setExp(e.getExp());
            userTO.setSumpay(e.getSumpay());
            userTO.setUnionId(e.getUnionid());
            userTO.setVipType(e.getVipid());
            userDTOS.add(userTO);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = simpleDateFormat.format(new Date());
        Date date = null;
        try {
            date = simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
            log.debug("Get Current Date Exception [by ninja.hzw]" + e);
        }
        int indexMap = 0;
        Map<Integer, List<UserTO>> map = new HashMap<>();
        int size = (userDTOS.size()) % 100 == 0 ? ((userDTOS.size()) / 100) : ((userDTOS.size()) / 100) + 1;
        if (size == 0) size++;
        for (int i = 1; i <= size; i++) {
            var index = i * 100;
            indexMap++;
            if (i == size) {
                List<UserTO> dto = userDTOS.subList(index - 100, userDTOS.size() - 1);
                map.put(indexMap, userDTOS.subList(index - 100, userDTOS.size() - 1));
                String key = "$\"NXLH.WX.ADMIN:sendWebUser_" + date + "_" + keys + "_" + indexMap + "\"";
                redisService.set(key, dto, 86400l);
            } else {
                List<UserTO> dto = userDTOS.subList(index - 100, index - 1);
                map.put(indexMap, userDTOS.subList(index - 100, index - 1));
                String key = "$\"NXLH.WX.ADMIN:sendWebUser_" + date + "_" + keys + "_" + indexMap + "\"";
                redisService.set(key, dto, 86400l);
            }
        }
        for (Map.Entry<Integer, List<UserTO>> entry : map.entrySet()) {
            String key = "$\"NXLH.WX.ADMIN:sendWebUser_" + date + "_" + keys + "_" + entry.getKey() + "\"";
            String data = JsonUtils.objectToJson(entry.getValue());
            var res = SendPost.sendPostMsg(checkVipIsInvalidationUrl, data);
            if (res) {
                redisService.remove(key);
            }
        }
    }

    /**
     * 检查会员3个月内（90天）是否有消费记录，无，则会员自动降1级
     *
     * @return
     */
    //region 原来检查会员的机制 3个月
//    @OverrideH
//    public boolean checkVipIsInvalidation() throws Exception {
//        //按自然整月算
//        var startMonth = DateUtils.addMonths(new Date(), -3);
//        //3个月中的当前月的第一天
//        var startDate = DateUtils.getFirstDateOfMonth(startMonth);
//
////        var vipJson = new FileInputStream(ResourceUtils.getFile("classpath:props/vip.json"));
//        var vipJson = this.getClass().getClassLoader().getResourceAsStream("props/vip.json");
//        List<String> ids = new ArrayList<>();
//        List<VipLowerDTO> byUserIds = this.vipLowerService.getByUserIds(startDate);
//        if (byUserIds != null && byUserIds.size() > 0) {
//            ids = byUserIds.stream().map(e -> e.getUserid()).collect(Collectors.toList());
//        } else {
//            ids = null;
//        }
//        var list = JsonUtils.fastJsonToObjArray(new String(vipJson.readAllBytes()), VipConfigJsonModel.class);
//        var wxUsers = this.baseMapper.getNoOrderRecordsIn3Months(ids, startDate);
//        List<VipLowerDTO> modelList = new ArrayList<>();
//        wxUsers.stream().forEach(e -> {
//            VipLowerDTO vipLowerDTO = new VipLowerDTO();
//            vipLowerDTO.setUserid(e.getId());
//            vipLowerDTO.setBeforelevel(e.getVipid());
//            e.setVipid(e.getVipid() - 1);
//            var config = list.stream().filter(m -> m.getLevel() == e.getVipid()).findFirst().get();
//            e.setExp(config.getExp());
//            vipLowerDTO.setAfterlevel(e.getVipid());
//            modelList.add(vipLowerDTO);
//        });
//
//
//        if (wxUsers != null && wxUsers.size() > 0) {
//            boolean result = this.transactionUtils.transact((a) -> {
//                this.updateBatchById(wxUsers);
//                this.vipLowerService.addBatch(modelList);
//            });
//            return result;
//        }
//        return true;
//    }
//endregion
    @Override
    public MyResult bindWxPhone(String sessionKey, String enctyData, String iv, String wxUserId) {
        WxMaPhoneNumberInfo phoneNoInfo = this.wxService.getUserService().getPhoneNoInfo(sessionKey, enctyData, iv);
        var user = this.getById(wxUserId);
        user.setPhone(phoneNoInfo.getPhoneNumber());
        this.updateById(user);
        //把当期用户写入缓存,有效期30天
        this.redisService.set(user.getSessiontoken(), user, userRedisExprie, TimeUnit.DAYS);
        return MyResult.ok(user.getPhone());
    }

    public WxMaUserInfo decryWxData(String sessionKey, String enctyData, String iv) {
        return this.wxService.getUserService().getUserInfo(sessionKey, enctyData, iv);

    }


    @Override
    public List<String> getWxuserByVip(Integer vipid) {
        return this.baseMapper.getWxuserByVip(vipid);
    }


    //region 用户添加积分
    @Override
    public MyResult updateUserScore(String wxUserId, BigDecimal money) {
        var wxUser = this.getById(wxUserId);
        if (money.compareTo(BigDecimal.ZERO) != 0) {

            var score = this.moneyToScoreByVip(wxUser.getVipid(), money);
            var record = new WxUserOtherRecordDTO();
            record.setId(IDUtils.genUUID());
            record.setAddtime(new Date());
            record.setScore(score);
            record.setWxuserid(wxUserId);
            record.setMoney(money);
            score = wxUser.getVscore().add(score);
            if (score.compareTo(BigDecimal.ZERO) == -1) {
                score = BigDecimal.ZERO;
            }
            wxUser.setVscore(score);
            wxUser.setSumpay(wxUser.getSumpay().add(money));

            var result = this.transactionUtils.transact((a) -> {
                this.wxUserOtherRecordService.add(record);
                this.updateById(wxUser);
            });
            return result ? MyResult.ok() : MyResult.error("系统异常");
        }

        return MyResult.ok();
    }

    @Override
    public MyResult searchByPhone(String phone, int count) {
        var example = this.sqlBuilder().where(Sqls.custom().andLike("phone", phone + "%")).orderBy("phone").build();
        if (count > 0) {
            PageHelper.startPage(1, count);
        }
        var users = this.list(example);
        var maps = new ArrayList<Map<String, Object>>();
        if (!CollectionUtils.isEmpty(users)) {
            users.stream().forEachOrdered(e -> {
                var map = new HashMap<String, Object>();
                map.put("id", e.getId());
                map.put("nickname", e.getNickname());
                map.put("phone", e.getPhone());
                maps.add(map);
            });
        }
        return MyResult.ok(maps);
    }
    //endregion


    private WxUserDTO registerNewUser(WxUserDTO wxuser, String openid,  String token) {


        if (wxuser == null) {
            wxuser = new WxUserDTO();
            wxuser.setOpenid(openid);
            wxuser.setVipid(VIPEnums.VIPTypeEnum.None.getValue());
            wxuser.setVscore(BigDecimal.ZERO);
            wxuser.setSumpay(BigDecimal.ZERO);
            wxuser.setExp(BigDecimal.ZERO);
            wxuser.setSessiontoken(token);
            wxuser.setNickname("");
            this.add(wxuser);

        } else {
            wxuser.setSessiontoken(token);
            this.updateById(wxuser);
        }
        return wxuser;
    }


    /**
     * 金额转为会员积分
     *
     * @param vipid
     * @param payPrice
     * @return
     */
    private BigDecimal moneyToScoreByVip(Integer vipid, BigDecimal payPrice) {
        var score = BigDecimal.ZERO;
        switch (vipid) {
            case 1:
                score = payPrice.divide(BigDecimal.valueOf(50));
                break;
            case 2:
                score = payPrice.divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(1.5));
                break;
            case 3:
                score = payPrice.divide(BigDecimal.valueOf(100));
                break;
            default:
                break;
        }
        return score;
    }


    /**
     * 重写查询 模糊查询
     *
     * @param page
     * @param parameters
     * @param orderby
     * @param isAsc
     * @return
     */
    @Override
    public Pagination<WxUserDTO> page(PageParameter page, Map<String, Object> parameters, String[] orderby, Integer isAsc) {
        var example = Example.builder(TbWxuser.class).andWhere(Sqls.custom().andEqualTo("isdelete", 0));
        if (parameters != null && parameters.size() > 0) {
            if ((!StringUtils.isEmpty(parameters.get("nickname"))) && (!StringUtils.isEmpty(parameters.get("phone")))) {
                example = example.andWhere(Sqls.custom()
                        .orLike("nickname", "%" + parameters.get("nickname").toString() + "%")
                        .orLike("phone", "%" + parameters.get("phone").toString() + "%"));
            }
            if (!StringUtils.isEmpty(parameters.get("viptype"))) {
                List<Integer> viptype = (List<Integer>) parameters.get("viptype");
                example = example.andWhere(Sqls.custom().orIn("vipid", viptype));
            }


        }

        if (ArrayUtils.isNotEmpty(orderby)) {
            if (isAsc == 1) {
                example.orderBy(orderby);
            } else {
                example.orderByDesc(orderby);
            }
        }
        return page(page, example.build());
    }


    @Override
    public void refreshUserRedis() {
        var example = this.sqlBuilder().where(Sqls.custom().andNotEqualTo("sessiontoken", "")).build();
        var allUsers = this.list(example);
        allUsers.forEach(e -> {
            if (this.redisService.exists(e.getSessiontoken())) {
                //fix 昵称中的emoji
                e.setNickname(this.emojiConverter.toAlias(e.getNickname()));
                this.redisService.set(e.getSessiontoken(), e, userRedisExprie, TimeUnit.DAYS);
            }
        });
    }

    @Override
    public MyResult editUserType(WxUserDTO wxUserDTO) throws IOException {


        var vipJson = this.getClass().getClassLoader().getResourceAsStream("props/vip.json");
        var list = JsonUtils.fastJsonToObjArray(new String(vipJson.readAllBytes()), VipConfigJsonModel.class);
        BigDecimal exp = list.get(wxUserDTO.getVipid()).getExp();
        TbWxuser tbWxuser = this.baseMapper.selectByPrimaryKey(wxUserDTO.getId());
        if (wxUserDTO.getVipid() != null && tbWxuser != null) {
            tbWxuser.setVipid(wxUserDTO.getVipid());
            tbWxuser.setExp(exp);
            String key = "$\"NXLH.WX.ADMIN:editUserType_" + new Date() + "\"";
            int i = this.baseMapper.updateByPrimaryKey(tbWxuser);
            if (i > 0) {
                this.refreshUserRedis();
                redisService.set(key, "{UnionId:" + tbWxuser.getUnionid() + ",VipType:" + tbWxuser.getVipid() + "}");
                var res = SendPost.sendPostMsg(editUserTypeUrl, "{UnionId:" + tbWxuser.getUnionid() + ",VipType:" + tbWxuser.getVipid() + "}");
                if (res) {
                    redisService.remove(key);
                }
                return MyResult.ok();
            }
        }
        return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "参数异常");
    }


    @Override
    public List<WxUserDTO> queryAllVIP() {
        var example = this.sqlBuilder().where(Sqls.custom().andGreaterThan("vipid", 0));
        List<TbWxuser> tbWxusers = this.baseMapper.selectByExample(example.build());
        return this.beanListMap(tbWxusers, this.currentDTOClass());
    }

    @Override
    public WxUserDTO getByUnionId(String id) {
        var example = this.sqlBuilder().where(Sqls.custom().andEqualTo("unionid", id));
        TbWxuser tbWxuser = this.baseMapper.selectOneByExample(example.build());
        return this.beanMap(tbWxuser, this.currentDTOClass());
    }

    @Override
    public void ResendEditUserType() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = simpleDateFormat.format(new Date());
        Date date = null;
        try {
            date = simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
            log.debug("Get Current Date Exception [by ninja.hzw]" + e);
        }
        String key = "$\"NXLH.WX.ADMIN:sendWebUser_" + date + "_*";
        Set set = redisService.fuzzyQueries(key);
        if (set.size() > 0) {
            set.forEach(e -> {
                var values = redisService.get(e).toString();
                Boolean res = SendPost.sendPostMsg(editUserTypeUrl, values.toString());
                if (res) {
                    redisService.remove(e);
                }
            });
        }

    }

    @Override
    public void ResendEditWxUser() {
        String key = "$\"NXLH.WX.ADMIN:editUserType_*\"";
        Set set = redisService.fuzzyQueries(key);
        if (set.size() > 0) {
            set.forEach(e -> {
                var values = redisService.get(e).toString();
                Boolean res = SendPost.sendPostMsg(editUserTypeUrl, values.toString());
                if (res) {
                    redisService.remove(e);
                }
            });
        }

    }


    public boolean compareVipBuyRecord() throws IOException {
        //按自然整月算
        var startMonth = DateUtils.addMonths(new Date(), -3);
        //3个月中的当前月的第一天
        var startDate = DateUtils.getFirstDateOfMonth(startMonth);
        var vipJson = new FileInputStream(ResourceUtils.getFile("classpath:props/vip.json"));
        var list = JsonUtils.fastJsonToObjArray(new String(vipJson.readAllBytes()), VipConfigJsonModel.class);
        List<TbWxuser> wxuserByVipList = this.baseMapper.getWxuserByVipList(list.get(list.size()).getLevel());
        boolean result = this.transactionUtils.transact((a) -> {
            for (TbWxuser tbWxuser : wxuserByVipList) {
                BigDecimal sumPriceInTime = this.orderService.getSumPriceInTime(tbWxuser.getId(), startDate, new Date());
//            double v = sumPriceInTime.doubleValue();
                BigDecimal subtract = sumPriceInTime.subtract(new BigDecimal(300.0));
                if (subtract.doubleValue() < 0.0) {
                    tbWxuser.setExp(tbWxuser.getExp().add(subtract));
                    WxUserDTO map = this.beanMapper.map(tbWxuser, this.currentDTOClass());
                    this.updateById(map);
                }
            }
        });
        return result;
    }
}
