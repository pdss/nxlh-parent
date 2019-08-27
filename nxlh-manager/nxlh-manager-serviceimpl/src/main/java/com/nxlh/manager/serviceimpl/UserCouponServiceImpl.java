package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.mapper.TbIntervalSendcouponMapper;
import com.nxlh.manager.mapper.TbUserCouponMapper;
import com.nxlh.manager.model.dbo.TbUserCoupon;
import com.nxlh.manager.model.dto.*;
import com.nxlh.manager.model.enums.CouponEnums;
import com.nxlh.manager.model.enums.OrderEnums;
import com.nxlh.manager.service.CouponsService;
import com.nxlh.manager.service.UserCouponService;
import com.nxlh.manager.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.nxlh.manager.rediskey.Keys.ORDER_KEY;
import static java.util.stream.Collectors.toList;

@Service(interfaceClass = UserCouponService.class)
public class UserCouponServiceImpl extends BaseDbCURDSServiceImpl<TbUserCouponMapper, TbUserCoupon, UserCouponDTO> implements UserCouponService {

    @Autowired
    private CouponsService couponsService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private TbIntervalSendcouponMapper intervalSendcouponMapper;

    //优惠卷检查是否过期
    @Override
    public boolean checkOverdueCoupon() {
        boolean result = this.transactionUtils.transact((a) -> {
            Example example = Example.builder(TbUserCoupon.class).where(Sqls.custom().andEqualTo("status", 0)).build();
            //取出所有未使用的优惠价
            List<TbUserCoupon> tbUserCoupons = this.baseMapper.selectByExample(example);
            //比较时间，抽出过期的优惠卷
            List<TbUserCoupon> collect = tbUserCoupons.stream().filter(e -> e.getOverdate().getTime() > new Date().getTime()).collect(Collectors.toList());
            List<String> list = collect.stream().map(e -> e.getId()).collect(Collectors.toList());
            if (list != null && list.size() > 0) {
                this.baseMapper.batchOverdue(list);
            }
        });
        return result;
    }

    @Override
    public List<UserCouponDTO> getCouponsByUserId(String userid) {
        var dbos = this.baseMapper.getCouponsByUserId(userid);
        return this.beanListMap(dbos, this.currentDTOClass());
    }

    @Override
    public Pagination<UserCouponDTO> getCouponsByStatus(PageParameter page, String userid, CouponEnums.CouponStatusEnum status) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        var dbos = this.baseMapper.getCouponsByStatus(userid, status.getValue());
        return this.mapPageInfo(new PageInfo(dbos), this.currentDTOClass());
    }

    @Override
    public UserCouponDTO getDetailsById(String user_coupon_id) {
        var dbo = this.baseMapper.getUserCouponInfoById(user_coupon_id);
        return this.beanMap(dbo, this.currentDTOClass());
    }

    @Override
    public MyResult getValidCouponsByOrder(String userId, String orderno) {
        var cacheKey = String.format(ORDER_KEY, orderno);
        var cache = this.redisService.get(cacheKey);
        if (cache != null) {
            var order = (OrderDTO) cache;
            //普通消费 || 预售活动 可用券
            if (order.getOrdertype() == OrderEnums.OrderTypeEnum.sale.getValue()|| order.getOrdertype() == OrderEnums.OrderTypeEnum.prebuy.getValue()) {
                var proIds = order.getOrderItems().stream().map(e -> e.getProductid()).collect(toList());
                //获取用户有效券
                var userCoupons = this.getCouponsByStatus(new PageParameter(1, 100), userId, CouponEnums.CouponStatusEnum.Valid);
                List<UserCouponDTO> valids = new ArrayList<>();
                if (userCoupons.getList() != null && userCoupons.getList().size() > 0) {
                    //过滤出能用的券
                    valids = userCoupons.getList()
                            .stream()
                            //符合满减条件
                            .filter(e ->
                                    e.getCouponInfo().getType() == CouponEnums.CouponTypeEnum.Limit.getValue() &&
                                            e.getCouponInfo().getLimitmoney().compareTo(order.getOrderprice()) < 1 && (e.getCouponInfo().getShopscope().equalsIgnoreCase("all") || proIds.contains(e.getCouponInfo().getShopscope()))

                            ).collect(toList());
                }

                return MyResult.ok(valids);
            } else {
                return MyResult.ok(null);
            }

        }
        return MyResult.error("订单无效");
    }

    @Override
    public boolean insertWxuserCoupons(WxUserDTO wxUserDTO) {
        CouponsDTO byId = couponsService.getById(wxUserDTO.getCouponsId());
        Date expireDate = new Date();
        //1、当月   2、指定日期   3、指定天数
        if (byId.getVaildtype() == CouponEnums.CouponValidTypeEnum.month.getValue()) {
            expireDate = DateUtils.getNextMonthFirstDay();
        } else if (byId.getVaildtype() == CouponEnums.CouponValidTypeEnum.date.getValue()) {
            expireDate = byId.getVailddate();
        } else if (byId.getVaildtype() == CouponEnums.CouponValidTypeEnum.days.getValue()) {
            expireDate = DateUtils.addDay(new Date(), byId.getVailddays());
        }
        TbUserCoupon tbUserCoupon = new TbUserCoupon();
        tbUserCoupon.setAddtime(new Date());
        tbUserCoupon.setCouponid(byId.getId());
        tbUserCoupon.setOverdate(expireDate);
        tbUserCoupon.setUserid(wxUserDTO.getId());
        tbUserCoupon.setGettype(2);
        tbUserCoupon.setStatus(0);
        boolean result = this.transactionUtils.transact((a) -> {
            for (int i = 0; i < wxUserDTO.getCouponCount(); i++) {
                tbUserCoupon.setId(IDUtils.genUUID());
                this.baseMapper.insert(tbUserCoupon);
            }
        });
        return result;
    }

    @Override
    public MyResult getCanReceiveCoupons(String wxUserId) {
        var example = this.sqlBuilder().where(Sqls
                .custom()
                .andEqualTo("isdelete", 0)
                .andEqualTo("gettype", CouponEnums.CouponGetTypeEnum.Manual.getValue())).build();
        //只拿手动的券
        var allCoupons = this.couponsService.list(example);
        //目前用户已有的券
        var userCoupons = this.getCouponsByUserId(wxUserId);

        //如果用户没有券，那么所有券都可以领取
        if (userCoupons != null && userCoupons.size() > 0) {
            //已领取过的券，这些券不可再领取
            allCoupons
                    .stream()
                    .forEach(e ->
                    {
                        var exists = userCoupons.stream().filter(p -> p.getCouponid().equalsIgnoreCase(e.getId())).count() > 0;
                        e.setCanget(exists ? 0 : 1);
                    });

        } else {
            allCoupons.forEach(e -> e.setCanget(1));
        }


        return MyResult.ok(allCoupons);
    }

    @Override
    public MyResult receive(String wxUserId, String couponId) {
        var exists = this.baseMapper.userHasCouponById(couponId, wxUserId);
        if (exists == 1) {
            return MyResult.error("您已领取过该券!");
        }

        var couponInfo = this.couponsService.getById(couponId);

        var userCoupon = new UserCouponDTO();
        userCoupon.setId(IDUtils.genUUID());
        userCoupon.setAddtime(new Date());
        userCoupon.setStatus(CouponEnums.CouponStatusEnum.Valid.getValue());
        userCoupon.setCouponid(couponId);
        userCoupon.setGettype(CouponEnums.CouponGetTypeEnum.Manual.getValue());
        userCoupon.setUserid(wxUserId);

        //当月
        if (couponInfo.getVaildtype() == CouponEnums.CouponValidTypeEnum.month.getValue()) {
            //下个月的第一天的0点
            var overdate = DateUtils.getNextMonthFirstDay();
            userCoupon.setOverdate(overdate);
            var validDays = DateUtils.daysBetween(new Date(), overdate);
            userCoupon.setOverdays((int) validDays);
            //指定日期
        } else if (couponInfo.getVaildtype() == CouponEnums.CouponValidTypeEnum.date.getValue()) {
            var overdate = DateUtils.addDay(DateUtils.getMinTime(couponInfo.getVailddate()), 1);
            userCoupon.setOverdate(overdate);
            var validDays = DateUtils.daysBetween(new Date(), overdate);
            userCoupon.setOverdays((int) validDays);
            //指定天数
        } else if (couponInfo.getVaildtype() == CouponEnums.CouponValidTypeEnum.days.getValue()) {
            var validDays = couponInfo.getVailddays() + 1;
            var overdate = DateUtils.addDay(DateUtils.getMinTime(DateUtils.now()), validDays);
            userCoupon.setOverdate(overdate);
            userCoupon.setOverdays(validDays);
        }

        this.add(userCoupon);
        return MyResult.ok("领取成功");
    }

    @Override
    public boolean insertCouponByVip(CouponsDTO couponsDTO) {
        Date expireDate = new Date();
        //1、当月   2、指定日期   3、指定天数
        if (couponsDTO.getVaildtype() == 1) {
            expireDate = DateUtils.getNextMonthFirstDay();
        } else if (couponsDTO.getVaildtype() == 2) {
            expireDate = couponsDTO.getVailddate();
        } else if (couponsDTO.getVaildtype() == 3) {
            expireDate = DateUtils.addDay(new Date(), couponsDTO.getVailddays());
        }
        final Date exp_date = expireDate;
        List<String> wxuserIds = null;
        if (couponsDTO.getVipid() == -1) {
            wxuserIds = this.wxUserService.getWxuserByVip(null);
        } else {
            wxuserIds = this.wxUserService.getWxuserByVip(couponsDTO.getVipid());
        }
        final List<String> userIds = wxuserIds;

        boolean result = this.transactionUtils.transact((a) -> {

            for (int i = 1; i <= couponsDTO.getCount(); i++) {
                for (String wxuserid : userIds) {
                    this.baseMapper.insertByVip(wxuserid, couponsDTO.getId(), exp_date);
                }
            }
        });
        return result;
    }

    @Override
    public List<ShopDTO> getTagProductInfoByCouponId(String couponId, List<String> productIds) {
        return this.baseMapper.getTagProductIdByCouponId(couponId, productIds);
    }

    @Override
    public boolean sendFixedCoupon() {
        List<IntervalSendcouponDTO> intervalSendcouponDTOS = this.intervalSendcouponMapper.selectBypage();
        intervalSendcouponDTOS.stream().forEach(e -> {
            if (e.getCount() != null && e.getCount() > 0) {
                e.getCoupon().setCount(e.getCount());
            } else {
                e.getCoupon().setCount(0);
            }
            e.getCoupon().setVipid(e.getVipid());
        });
        List<CouponsDTO> collect = intervalSendcouponDTOS.stream().map(e -> e.getCoupon()).collect(toList());

        boolean result = this.transactionUtils.transact((a) -> {

            for (CouponsDTO coupon : collect) {
                if (coupon.getVipid() != null) {
                    this.insertCouponByVip(coupon);
                }
            }
        });
        return result;
    }

    @Override
    public List<UserCouponDTO> checkCouponValid(List<UserCouponDTO> userCouponDTOS) {
        var now = DateUtils.now();
        //只过滤未使用且未过期
        return userCouponDTOS.stream().filter(e -> e.getOverdate().compareTo(now) == 1 && e.getStatus() == CouponEnums.CouponStatusEnum.Valid.getValue()).collect(Collectors.toList());
    }
}
