package com.nxlh.manager.service;

import com.nxlh.common.model.MessageModel;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.mapper.TbWxuserMapper;
import com.nxlh.manager.model.dbo.TbWxuser;
import com.nxlh.manager.model.dto.WxUserDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import java.util.List;

public interface WxUserService extends BaseService<WxUserDTO, TbWxuserMapper, TbWxuser> {

    /**
     * 客户端登录，Code换取Sessionkey
     *
     * @param code
     * @return
     */
    MyResult login(String code);


    /**
     * 绑定微信用户
     *
     * @param params
     * @return
     */
    MyResult bindWxUser(Map<String, Object> params);

    /**
     * 网站用户注册，同步
     * @param params
     * @return
     */
    MessageModel syncUser(Map<String, Object> params);

    /**
     * 每天降低会员的经验值
     *
     * @return
     */
    boolean checkVipIsInvalidation() throws FileNotFoundException, Exception;


    /**
     * 每月刷，三个月无消费记录降级
     *
     * @return
     */
    boolean monthCheckVipIsInvalidation() throws FileNotFoundException, Exception;

    /**
     * 绑定微信手机
     *
     * @return
     */
    MyResult bindWxPhone(String sessionKey, String enctyData, String iv, String wxUserId);


    /**
     * 根据vipid查询用户id
     *
     * @param vipid
     * @return
     */
    List<String> getWxuserByVip(Integer vipid);


    /**
     * 更新用户积分
     *
     * @param wxUserId 微信用户id
     * @param money    其他平台金额
     * @return
     */
    MyResult updateUserScore(String wxUserId, BigDecimal money);


    /**
     * 根据手机号码搜索用户
     *
     * @param phone
     * @param count 限制查询数量 -1不限制
     * @return
     */
    MyResult searchByPhone(String phone, int count);

    /**
     * 刷新Redis中的用户信息
     */
    void refreshUserRedis();


    /**
     * 修改会员的等级
     *
     * @param wxUserDTO
     * @return
     */
    MyResult editUserType(WxUserDTO wxUserDTO) throws IOException;


    List<WxUserDTO> queryAllVIP();


    /**
     * 根据unionid查找用户的数据
     *
     * @param id
     * @return
     */
    WxUserDTO getByUnionId(String id);


    /**
     * 对比会员前3个月消费数据不合格则降低经验
     *
     * @return
     * @throws IOException
     */
//    boolean compareVipBuyRecord() throws IOException;


    /**
     * 从redis获取失败请求，并重新发送
     */
    void ResendEditUserType();
    void ResendEditWxUser();

}
