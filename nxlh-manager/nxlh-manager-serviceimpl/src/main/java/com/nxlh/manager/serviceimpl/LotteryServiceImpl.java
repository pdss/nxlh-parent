package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.*;
import com.nxlh.manager.mapper.TbLotteryItemMapper;
import com.nxlh.manager.mapper.TbLotteryMapper;
import com.nxlh.manager.model.dbo.TbLottery;
import com.nxlh.manager.model.dbo.TbLotteryItem;
import com.nxlh.manager.model.dbo.TbLotteryWxuser;
import com.nxlh.manager.model.dto.*;
import com.nxlh.manager.service.LotteryService;
import com.nxlh.manager.service.LotteryWxUserService;
import com.nxlh.manager.service.WxUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.nxlh.common.model.MyResult.ok;
import static com.nxlh.manager.amqp.queue.LotteryQueue.LOTTERYQueue;
import static com.nxlh.manager.amqp.queue.LotteryQueue.LOTTERY_MASTER_EX;

@Service(interfaceClass = LotteryService.class)
public class LotteryServiceImpl extends BaseDbCURDSServiceImpl<TbLotteryMapper, TbLottery, LotteryDTO> implements LotteryService {

    @Autowired
    private TbLotteryItemMapper lotteryItemMapper;

    @Autowired
    private LotteryWxUserService lotteryWxUserService;

    @Autowired
    private WxUserService wxUserService;


    private final String CACHE_PREFIX = "LOTTERY_%s";

    //指定中奖用户的redis key,
    private final String CACHE_LOTTERY_LUCK_PREFIX = "LOTTERY_LUCK_%s";

    private final long LOCK_TIME = 5;

    private static Object locker = new Object();

    private static JSONObject LotteryJSONObj;

    private static String CACHE_LUCK_USERS = "CACHE_LUCK_USERS";


//    @Autowired
//    @Lazy
//    private RedisLock redisLock;


    /**
     * 活动开启或关闭
     *
     * @param lotteryDTO
     * @return
     */
    @Override
    public boolean editStatus(LotteryDTO lotteryDTO) {

        TbLottery tbLottery = this.baseMapper.selectByPrimaryKey(lotteryDTO.getId());
        tbLottery.setStatus(lotteryDTO.getStatus());
        this.baseMapper.updateByPrimaryKey(tbLottery);
        if (lotteryDTO.getStatus() == 0) {
            var cacheKey = String.format(CACHE_PREFIX, lotteryDTO.getId());
            this.redisService.remove(cacheKey);
            this.redisService.remove(String.format(CACHE_PREFIX + "_temp", lotteryDTO.getId()));
            this.redisService.remove(String.format(CACHE_PREFIX + "_temp_users", lotteryDTO.getId()));
        }
        return true;
    }

    /**
     * 预览开启或关闭
     *
     * @param lotteryDTO
     * @return
     */
    @Override
    public boolean editShow(LotteryDTO lotteryDTO) {

        TbLottery tbLottery = this.baseMapper.selectByPrimaryKey(lotteryDTO.getId());
        tbLottery.setIsshow(lotteryDTO.getIsshow());
        this.baseMapper.updateByPrimaryKey(tbLottery);
        return true;
    }

    /**
     * 根据id查询活动管理和关联表
     *
     * @param id
     * @return
     */
    @Override
    public LotteryDTO getLotteryById(String id) {
        TbLottery tbLottery = this.baseMapper.queryInfoById(id);
        var lottery = this.beanMapper.map(tbLottery, this.currentDTOClass());
        if (tbLottery.getUsertype() == 100) {
            var joinUsers = this.lotteryWxUserService.getLotteryWithJoinUsers(id);
            List<Map<String, Object>> maps = joinUsers.stream().map(e -> new HashMap<String, Object>() {{
                put("nickname", e.getJoinuser().getNickname());
                put("id", e.getJoinuser().getId());
                put("phone", e.getJoinuser().getPhone());
            }}).collect(Collectors.toList());
            lottery.setJoinusers(maps);

        }
        return lottery;
    }

    /**
     * 更新插入活动管理表和关联表
     *
     * @param lotteryDTO
     * @return
     */
    @Override
    public boolean addOrUpdateLottery(LotteryDTO lotteryDTO) {

        var prepare = true;
        final boolean[] isAdd = {true};
        if (StringUtils.isEmpty(lotteryDTO.getId())) {
            lotteryDTO.setId(IDUtils.genUUID());
            lotteryDTO.setIsdelete(0);
            lotteryDTO.setAddtime(new Date());

        } else {
            isAdd[0] = false;
            //修改就删除原关联的奖品
            Example example = Example.builder(TbLotteryItem.class).where(Sqls.custom().andEqualTo("lotteryid", lotteryDTO.getId())).build();


            //删除指定参与用户
            var example2 = Example.builder(TbLotteryWxuser.class).where(Sqls.custom().andEqualTo("lotteryid", lotteryDTO.getId())).build();

            prepare = this.transactionUtils.transact((a) -> {
                this.lotteryItemMapper.deleteByExample(example);
                this.lotteryWxUserService.remove(example2);
            });
        }

        if (!prepare) {
            return false;
        }

        //重新添加关联奖品
        List<LotteryItemDTO> lotteryItemList = lotteryDTO.getLotteryItemList();
        lotteryItemList.stream().forEach(e -> e.setLotteryid(lotteryDTO.getId()));


        //特殊值100,手动指定参与用户
        var lottery_wxusers = new ArrayList<LotteryWxUserDTO>();
        if (lotteryDTO.getUsertype() == 100) {
            lotteryDTO.getJoinuserids().forEach(e -> {
                var lottery_wxuser = new LotteryWxUserDTO();
                lottery_wxuser.setId(IDUtils.genUUID());
                lottery_wxuser.setAddtime(DateUtils.now());
                lottery_wxuser.setLotteryid(lotteryDTO.getId());
                lottery_wxuser.setWxuserid(e);
                lottery_wxusers.add(lottery_wxuser);
            });
        }

        var result = this.transactionUtils.transact((a) -> {
            if (isAdd[0]) {
                this.add(lotteryDTO);
            } else {
                this.updateById(lotteryDTO);
            }
            this.lotteryWxUserService.addBatch(lottery_wxusers);
            lotteryItemMapper.insetLotteryItemList(lotteryItemList);

        });


        return result;
    }

    @Override
    public MyResult getAwardListById(String id, String uid) throws Exception {
        if (id.equalsIgnoreCase("1111")) {
            var result = this.check1111(id, uid);
            if (result.getStatus() != 200) {
                return result;
            }
        }
        var cacheKey = String.format(CACHE_PREFIX, id);
        var cache = redisService.get(cacheKey);
        LotteryDTO lottery = null;
        if (cache == null) {
            lottery = this.getLotteryById(id);
            //可参与用户
            var joinUsers = this.lotteryWxUserService.getLotteryWithJoinUsers(id);
            //指定了参与用户
            if (!CollectionUtils.isEmpty(joinUsers)) {
                List<Map<String, Object>> joinUserMaps = joinUsers.stream().map(e -> new HashMap<String, Object>() {{
                    put("id", e.getWxuserid());

                }}).collect(Collectors.toList());
                lottery.setJoinusers(joinUserMaps);
                //按用户等级
            }


        } else {
            lottery = (LotteryDTO) cache;
        }


        if (CollectionUtils.isEmpty(lottery.getUserLotteries())) {
            var init = new ArrayList<WxuserLotteryDTO>();
            lottery.setUserLotteries(init);
        }
        if (CollectionUtils.isEmpty(lottery.getLotteryItemList())) {
            lottery.setLotteryItemList(new ArrayList<>());
        }

        var wxuser = this.wxUserService.getById(uid);
        var checkResult = this.checkCanJoin(lottery, wxuser);
        if (checkResult.getStatus() != 200) {
            return checkResult;
        }
        redisService.set(cacheKey, lottery);
        lottery.setUserLotteries(null);
        lottery.setJoinusers(null);
        lottery.setJoinuserids(null);
        return MyResult.ok(lottery);

    }

    /**
     * 检查是否能参与抽奖
     *
     * @param lottery
     * @return
     */
    private MyResult checkCanJoin(LotteryDTO lottery, WxUserDTO wxUserDTO) {

        //商品都抽完了
        if (lottery.getIsover()) {
            return MyResult.build(0, "活动已结束");
        }


        //已删除、不显示、未开启一律不处理
        if (lottery.getIsdelete() == 1 || lottery.getIsshow() == 0 || lottery.getStatus() == 0) {
            return MyResult.build(1, "无效活动");
        }

        //参与人数已满
        if (lottery.getUserlimit() != -1 && lottery.getUserLotteries().size() == lottery.getUserlimit()) {
            return MyResult.build(0, "活动已结束");
        }

        //当前用户是否已参与过
        var isJoin = lottery.getUserLotteries().stream().filter(e -> e.getWxuserid().equalsIgnoreCase(wxUserDTO.getId())).findFirst();
        if (isJoin.isPresent()) {
            //已经抽过，返回抽奖结果
            var resultMap = new HashMap<String, Object>() {{
                put("productimage", isJoin.get().getLotteryItem().getProductimage());
                put("productname", isJoin.get().getLotteryItem().getProductname());
                put("activecode", isJoin.get().getActivecode());
                put("id", isJoin.get().getLotteryitemid());
                put("lotteryItemList", lottery.getLotteryItemList());
                put("result", !isJoin.get().getShopid().equalsIgnoreCase(IDUtils.empty()));
            }};
            return MyResult.build(2, "您已参与过此次抽奖", resultMap);
        }

        //用户是否有参与资格(针对指定用户)
        if (!CollectionUtils.isEmpty(lottery.getJoinusers())) {
            var canJoin = lottery.getJoinusers().stream().filter(e -> e.containsValue(wxUserDTO.getId())).count() > 0;
            if (!canJoin) {
                return MyResult.build(0, "谢谢参与");
            }
            //用户等级
        } else {
            var utype = lottery.getUsertype();
            //用户满足指定等级
            if (utype != 0 && wxUserDTO.getVipid() < utype) {
                return MyResult.build(0, "谢谢参与");
            }
        }

        return MyResult.ok();
    }


    private MyResult check1111(String id, String userId) {
        var cacheKey_temp = String.format(CACHE_PREFIX + "_temp", id);
        if (this.redisService.exists(cacheKey_temp)) {
            return MyResult.build(0, "活动已结束");
        }
        var cacheUsersKey = String.format(CACHE_PREFIX + "_temp_users", id);
        var users = (List<String>) this.redisService.get(cacheUsersKey);
        if (CollectionUtils.isEmpty(users)) {
            users = new ArrayList<>();
        }
        if (users.indexOf(userId) > -1) {
            return MyResult.build(0, "谢谢参与");
        }
        return MyResult.ok();
    }

    @Override
    public MyResult getAwardById(String id, String uid) {
        var cacheKey = String.format(CACHE_PREFIX, id);
        if (!this.redisService.exists(cacheKey)) {
            return MyResult.forb("无效请求");
        }

        var check = check1111(id, uid);
        if (check.getStatus() != 200) {
            return check;
        }

        synchronized (locker) {

            var user = this.wxUserService.getById(uid);
            var luck_users = (List<String>) this.redisService.get(CACHE_LUCK_USERS);
            if (CollectionUtils.isEmpty(luck_users)) {
                luck_users = new ArrayList<>();
            }
            //非金牌不中,已中奖不中
            if ((id.equalsIgnoreCase("2222") || id.equalsIgnoreCase("3333")) && user.getVipid() != 3 || luck_users.indexOf(uid) > -1) {
                var awardMap = new HashMap<String, Object>() {{
                    put("productimage", "");
                    put("productname", "谢谢惠顾");
                    put("activecode", "");
                    put("id", "");
                    put("result", false);
                }};
                return MyResult.ok(awardMap);
            }
            var lottery = (LotteryDTO) redisService.get(cacheKey);

            if (CollectionUtils.isEmpty(lottery.getUserLotteries())) {
                var init = new ArrayList<WxuserLotteryDTO>();
                lottery.setUserLotteries(new ArrayList<>());
            }
            if (CollectionUtils.isEmpty(lottery.getLotteryItemList())) {
                lottery.setLotteryItemList(new ArrayList<>());
            }

            var checkResult = this.checkCanJoin(lottery, user);
            if (checkResult.getStatus() != 200) {
                return checkResult;
            }

            //抽奖结果

            //如果当前活动没有指定中奖人，则视为：随机抽奖
            LotteryItemDTO award = this.award(lottery.getLotteryItemList());
            if (award != null) {
                var count = award.getCount() - 1;
                award.setCount(count < 0 ? 0 : count);


                //抽奖记录
                var record = new WxuserLotteryDTO();
                record.setId(IDUtils.genUUID());
                record.setAddtime(DateUtils.now());
                record.setActivecode(IDUtils.genUUID());
                record.setStatus(0);
                record.setLotteryid(lottery.getId());
                record.setShopid(award.getProductid());
                record.setWxuserid(uid);
                record.setLotteryItem(award);
                record.setLotteryitemid(award.getId());
                lottery.getUserLotteries().add(record);

                //谢谢惠顾，没有激活码
                if (award.getProductid().equalsIgnoreCase(IDUtils.empty())) {
                    record.setActivecode("");
                } else {
                    //中奖用户id
                    // var luck_users = (List<String>) this.redisService.get(CACHE_LUCK_USERS);
                    luck_users.add(uid);
                    //记中奖用户
                    this.redisService.set(CACHE_LUCK_USERS, luck_users);

                    //剩余奖品数
                    var giftCount = lottery.getLotteryItemList()
                            .stream()
                            .filter(e -> !e.getProductid().equalsIgnoreCase(IDUtils.empty()))
                            .map(e -> e.getCount()).reduce(0, Integer::sum);
                    if (giftCount == 0) {
                        lottery.setIsover(true);
                    }

                }
                //写入缓存
                this.redisService.set(cacheKey, lottery);
                //消息队列写库
                this.rabbitTemplate.convertAndSend(LOTTERY_MASTER_EX, LOTTERYQueue, record, new CorrelationData(IDUtils.genUUID()));

//            this.redisLock.unlock(lockName, time + "");
                var awardMap = new HashMap<String, Object>() {{
                    put("productimage", award.getProductimage());
                    put("productname", award.getProductname());
                    put("activecode", record.getActivecode());
                    put("id", record.getLotteryitemid());
                    put("result", !award.getProductid().equalsIgnoreCase(IDUtils.empty()));
                }};
                return MyResult.ok(awardMap);
            } else {
                lottery.setIsover(true);
                this.redisService.set(cacheKey, lottery);
                return MyResult.build(0, "活动已结束");
            }
        }
    }

    /**
     * 指定用户中奖，其他人谢谢惠顾
     *
     * @param id
     * @return
     */
    @Override
    public MyResult getAward_Temp(String id, String userId) throws Exception {
        if (LotteryJSONObj == null) {
            var json = this.getClass().getClassLoader().getResourceAsStream("props/lottery.json").readAllBytes();
            LotteryJSONObj = JsonUtils.fastJsonToObj(new String(json));
        }

        var cacheKey = String.format(CACHE_PREFIX + "_temp", id);
        var cacheUsersKey = String.format(CACHE_PREFIX + "_temp_users", id);
        var users = (List<String>) this.redisService.get(cacheUsersKey);
        if (CollectionUtils.isEmpty(users)) {
            users = new ArrayList<>();
        }
        synchronized (locker) {
            if (this.redisService.exists(cacheKey)) {
                return MyResult.build(0, "活动已结束");
            }
            if (LotteryJSONObj.getString("userid").equalsIgnoreCase(userId)) {
                this.redisService.set(cacheKey, 1);
                var awardMap = new HashMap<String, Object>() {{
                    put("productimage", "http://nxlh-resources.oss-cn-zhangjiakou.aliyuncs.com/nxlh-resources/lottery-1.jpg");
                    put("productname", "HORI便携式显示器");
                    put("activecode", IDUtils.genUUID());
                    put("id", "");
                    put("result", true);
                }};
                return MyResult.ok(awardMap);
            } else {
                users.add(userId);
                var awardMap = new HashMap<String, Object>() {{
                    put("productimage", "http://nxlh-resources.oss-cn-zhangjiakou.aliyuncs.com/nxlh-resources/018f42f9ecd3481db470377c91afa6e8.jpg");
                    put("productname", "谢谢惠顾");
                    put("activecode", "");
                    put("id", "");
                    put("result", false);
                }};
                this.redisService.set(cacheUsersKey, users);
                return MyResult.ok(awardMap);
            }
        }
    }

    /*
      抽奖算法
     */
    public static LotteryItemDTO award(List<LotteryItemDTO> awards) {
        //总的概率区间
        float totalPro = 0f;
        //存储每个奖品新的概率区间
        List<Float> proSection = new ArrayList<Float>();
        proSection.add(0f);
        //遍历每个奖品，设置概率区间，总的概率区间为每个概率区间的总和
        for (LotteryItemDTO award : awards) {
            //每个概率区间为奖品概率乘以1000（把三位小数转换为整）再乘以剩余奖品数量
            totalPro += award.getPercent().floatValue() * 10 * award.getCount();
            proSection.add(totalPro);
        }
        //获取总的概率区间中的随机数
        Random random = new Random();
        if (totalPro == 0) {
            return null;
        }
        float randomPro = (float) random.nextInt((int) totalPro);
        //判断取到的随机数在哪个奖品的概率区间中
        for (int i = 0, size = proSection.size(); i < size; i++) {
            if (randomPro >= proSection.get(i)
                    && randomPro < proSection.get(i + 1)) {
                return awards.get(i);
            }
        }
        return null;
    }

    @Override
    public Pagination<LotteryDTO> wxUserLotterypage(PageParameter page, Example example) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        var list = this.baseMapper.queryWxuserLotteryCount();
        PageInfo<LotteryDTO> pageInfo = new PageInfo<LotteryDTO>(list);

        return mapPageInfo(pageInfo, this.currentDTOClass());
    }

    @Override
    public boolean closeActivity(String id) {
        TbLottery tbLottery = this.baseMapper.selectByPrimaryKey(id);
        tbLottery.setIsdelete(1);
        int i = this.baseMapper.updateByPrimaryKey(tbLottery);
        var cacheKey = String.format(CACHE_PREFIX, id);
        redisService.remove(cacheKey);
        return i > 0 ? true : false;
    }

    @Override
    public Pagination<LotteryDTO> wxUserLotterypage(PageParameter page, Map<String, Object> parameters, String[] orderby, Integer isAsc) {
        var example = this.columnsMapToSqlBuilder(parameters);
        if (ArrayUtils.isNotEmpty(orderby)) {
            if (isAsc == 1) {
                example.orderBy(orderby);
            } else {
                example.orderByDesc(orderby);
            }
        }
        return wxUserLotterypage(page, example.build());
    }


    @Override
    public List<LotteryDTO> getActivedByUser(String userId) {
        var wxuser = this.wxUserService.getById(userId);
        //获取面向所有人或当前用户所属的vip的活动
        var sqlWhere = this.sqlBuilder().where(Sqls.custom()
                .andEqualTo("status", 1)
                .andEqualTo("isdelete", 0)
//                .andEqualTo("usertype", List.of(-1))
//                .andLessThanOrEqualTo("usertype", wxuser.getVipid())
                .andEqualTo("isshow", 1)).build();
        var actives = this.list(sqlWhere);

        var user_lotterys = this.baseMapper.getLotteriesByJoinUserId(userId);
        if (CollectionUtils.isEmpty((user_lotterys))) {
            actives.forEach(e -> e.setHasTicket(false));
        } else {
            user_lotterys.forEach(a ->
                    actives.forEach(b -> {
                        b.setHasTicket(a.getId().equalsIgnoreCase(b.getId()));
                    })
            );

        }

        return actives;

    }


}
