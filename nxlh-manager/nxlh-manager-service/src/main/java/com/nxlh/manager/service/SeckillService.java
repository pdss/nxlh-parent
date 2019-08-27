package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbSeckillMapper;
import com.nxlh.manager.model.dbo.TbSeckill;
import com.nxlh.manager.model.dto.SeckillDTO;

import java.util.List;

public interface SeckillService extends BaseService<SeckillDTO, TbSeckillMapper, TbSeckill> {

    List<SeckillDTO> getAll();

    /**
     * 上下架
     *
     * @param seckillDTO
     * @return
     */
    boolean editStatus(SeckillDTO seckillDTO);


    /**
     * 检查活动是否开启
     * @param id
     * @return
     */
    boolean checkStatus(String id);

}
