package com.nxlh.manager.rediskey;

public class Keys {

    public static final String ORDER_KEY = "PREVIEWORDER_%s";

    public static final String SHOPPINGCAR_KEY = "%s_SHOPPINGCAR";

    //今日下单数
    public static final String SUMMARY_TODAY_ORDER_COUNT_KEY = "SUMMARY_TODAY_ORDER_COUNT_KEY";

    //今日新微信用户
    public static final String SUMMARY_TODAY_NEWWXUSER_COUNT_KEY = "SUMMARY_TODAY_NEWWXUSER_COUNT_KEY";

    //今日微信用户访问量
    public static final String SUMMARY_TODAY_WXUSER_REQUEST_COUNT_KEY = "SUMMARY_TODAY_WXUSER_REQUEST_COUNT_KEY";

    //今日更新商品数（上新+更新）
    public static final String SUMMARY_TODAY_UPDATE_SHOP_COUNT_KEY = "SUMMARY_TODAY_UPDATE_SHOP_COUNT_KEY";

    //本月下单总数
    public static final String SUMMARY_MONTH_ORDER_COUNT_KEY = "SUMMARY_MONTH_ORDER_COUNT_KEY";

    /**
     * 本月新微信用户
     */
    public static final String SUMMARY_MONTH_NEWWXUSER_COUNT_KEY = "SUMMARY_MONTH_NEWWXUSER_COUNT_KEY";

    //本月微信用户的访问量
    public static final String SUMMARY_MONTH_WXUSER_REQUEST_COUNT_KEY = "SUMMARY_MONTH_WXUSER_REQUEST_COUNT_KEY";


    //本月更新商品数
    public static final String SUMMARY_MONTH_UPDATE_SHOP_COUNT_KEY = "SUMMARY_MONTH_UPDATE_SHOP_COUNT_KEY";


    //今日退款商品数
    public static final String SUMMARY_TODAY_ORDERREFUND_COUNT_KEY = "SUMMARY_TODAY_ORDERREFUND_COUNT_KEY";

    //本月退款商品数
    public static final String SUMMARY_MONTH_ORDERREFUND_COUNT_KEY = "SUMMARY_MONTH_ORDERREFUND_COUNT_KEY";


}
