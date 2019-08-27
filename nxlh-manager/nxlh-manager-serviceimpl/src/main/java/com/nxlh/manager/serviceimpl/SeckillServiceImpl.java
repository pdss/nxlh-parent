package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbSeckillMapper;
import com.nxlh.manager.model.dbo.TbSeckill;
import com.nxlh.manager.model.dto.SeckillDTO;
import com.nxlh.manager.service.SeckillService;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

@Service(interfaceClass = SeckillService.class)
public class SeckillServiceImpl extends BaseDbCURDSServiceImpl<TbSeckillMapper, TbSeckill, SeckillDTO> implements SeckillService {

    //秒杀活动key
    private final String MS_SECKILL_KEY = "MS_SECKILL_%s";


    @Override
    public List<SeckillDTO> getAll() {
        List<TbSeckill> tbSeckills = this.baseMapper.selectAll();
        return this.beanListMap(tbSeckills, this.currentDTOClass());
    }

//    @Override
////    public boolean updateById(SeckillDTO entity) {
////        super.updateById(entity);
////        this.redisService.set(String.format(MS_SECKILL_KEY, entity.getId()), entity);
////        return true;
////    }

    @Override
    public boolean editStatus(SeckillDTO seckillDTO) {
        TbSeckill tbSeckill = this.baseMapper.selectByPrimaryKey(seckillDTO.getId());
        tbSeckill.setStatus(seckillDTO.getStatus());
        int i = this.baseMapper.updateByPrimaryKey(tbSeckill);
        if (seckillDTO.getStatus() == 1) {
            this.redisService.set(String.format(MS_SECKILL_KEY, seckillDTO.getId()), seckillDTO);
        } else {
            this.redisService.remove(String.format(MS_SECKILL_KEY, seckillDTO.getId()));
        }
        return i > 0 ? true : false;
    }

    @Override
    public boolean checkStatus(String id) {
        return this.redisService.exists(String.format(MS_SECKILL_KEY, id));
    }
}
