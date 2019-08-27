package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbLotteryMapper;
import com.nxlh.manager.model.dbo.TbLottery;
import com.nxlh.manager.model.dto.LotteryDTO;
import tk.mybatis.mapper.entity.Example;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface LotteryService extends BaseService<LotteryDTO, TbLotteryMapper, TbLottery> {

    /**
     * 活动开启或关闭
     *
     * @param lotteryDTO
     * @return
     */
    boolean editStatus(LotteryDTO lotteryDTO);

    /**
     * 活动预览开启或关闭
     *
     * @param lotteryDTO
     * @return
     */
    boolean editShow(LotteryDTO lotteryDTO);

    /**
     * 根据id查询活动管理和关联表
     *
     * @param id
     * @return
     */
    LotteryDTO getLotteryById(String id);

    /**
     * 更新插入活动管理表和关联表
     *
     * @param lotteryDTO
     * @return
     */
    boolean addOrUpdateLottery(LotteryDTO lotteryDTO);


    /**
     * 获取正在进行中的抽奖活动,
     * 每个用户可能显示的不一样，抽奖活动存在：面向特定人群、会员
     * @return
     */
    List<LotteryDTO> getActivedByUser(String userId) ;


    /**
     * 获取抽奖商品
     *
     * @param id
     * @return
     */
    MyResult getAwardListById(String id, String uid) throws Exception;


    /**
     * 获取抽中商品
     *
     * @param id
     * @return
     */
    MyResult getAwardById(String id, String uid);


    /**
     * 指定用户中奖，其他人谢谢惠顾
     * @param id
     * @return
     */
    MyResult getAward_Temp(String id,String userId) throws Exception;


    /**
     * 重写分页，用于用户参与活动的统计页
     *
     * @param var1
     * @param parameters
     * @param orderby
     * @param isAsc
     * @return
     */
    Pagination<LotteryDTO> wxUserLotterypage(PageParameter var1, Map<String, Object> parameters, String[] orderby, Integer isAsc);



    /**
     * 重写分页，用于用户参与活动的统计页
     *
     * @param page
     * @param example
     * @return
     */
    Pagination<LotteryDTO> wxUserLotterypage(PageParameter page, Example example);


    /**
     * 关闭活动
     * @param id
     * @return
     */
    boolean closeActivity(String id);
}
