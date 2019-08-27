package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbWxuserRecyclesMapper;
import com.nxlh.manager.model.dbo.TbWxuser;
import com.nxlh.manager.model.dbo.TbWxuserRecycles;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.dto.WxUserRecycleDTO;
import com.nxlh.manager.model.enums.AccoutEnums;
import com.nxlh.manager.model.enums.RecycleEnums;
import com.nxlh.manager.service.WxUserRecycleService;
import org.apache.commons.lang3.ArrayUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = WxUserRecycleService.class)
public class WxUserRecyclesServiceImpl extends BaseDbCURDSServiceImpl<TbWxuserRecyclesMapper, TbWxuserRecycles, WxUserRecycleDTO> implements WxUserRecycleService {


    @Override
    public Pagination<WxUserRecycleDTO> listByPage(PageParameter page, String type, String userid) {
        var example = this.sqlBuilder().where(Sqls.custom().andEqualTo("userid", userid));
        if (type.equalsIgnoreCase("continue")) {
            List<Integer> status = new ArrayList<>() {{
                add(0);
                add(1);
                add(2);
            }};
            example = example.andWhere(Sqls.custom().andIn("status", status));
        } else if (type.equalsIgnoreCase("complete")) {
            List<Integer> status = new ArrayList<>() {{
                add(3);
                add(4);
            }};
            example = example.andWhere(Sqls.custom().andIn("status", status));
        }


        return this.page(page, example.orderByDesc("addtime").build());
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
    public Pagination<WxUserRecycleDTO> page(PageParameter page, Map<String, Object> parameters, String[] orderby, Integer isAsc) {
        var example = Example.builder(TbWxuserRecycles.class).andWhere(Sqls.custom().andEqualTo("isdelete", 0));
        if (parameters != null && parameters.size() > 0) {
            if ((!StringUtils.isEmpty(parameters.get("filter")))) {
                var searchFilter = parameters.get("filter").toString();
                example = example.andWhere(Sqls.custom()
                        .orLike("orderno", "%" + searchFilter + "%")
                        .orLike("payaccount", "%" + searchFilter + "%")
                        .orLike("productname", "%" + searchFilter + "%")
                        .orLike("expressno", "%" + searchFilter + "%"));
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
    public boolean cancel(String id) {
        TbWxuserRecycles tbWxuserRecycles = this.baseMapper.selectByPrimaryKey(id);
        tbWxuserRecycles.setStatus(RecycleEnums.Close.getValue());
        int i = this.baseMapper.updateByPrimaryKey(tbWxuserRecycles);
        return i > 0;
    }

    @Override
    public boolean close(String id) {
        TbWxuserRecycles tbWxuserRecycles = this.baseMapper.selectByPrimaryKey(id);
        tbWxuserRecycles.setStatus(RecycleEnums.Success.getValue());
        int i = this.baseMapper.updateByPrimaryKey(tbWxuserRecycles);
        return i > 0;
    }

    @Override
    public boolean checkPrice(WxUserRecycleDTO model) {
        TbWxuserRecycles tbWxuserRecycles = this.baseMapper.selectByPrimaryKey(model.getId());
        if (tbWxuserRecycles.getStatus() == RecycleEnums.Talk.getValue())
            tbWxuserRecycles.setStatus(RecycleEnums.Send.getValue());
        tbWxuserRecycles.setFinalprice(model.getFinalprice());
        int i = this.baseMapper.updateByPrimaryKey(tbWxuserRecycles);
        return i > 0;
    }


    @Override
    public boolean send(String orderId, String express, String expressno, String account, AccoutEnums.AccoutTypeEnums type) {
        var order = this.getById(orderId);
        order.setExpress(express);
        order.setExpressno(expressno);
        order.setAccounttype(type.getValue());
        order.setAccount(account);
        order.setStatus(RecycleEnums.Audit.getValue());
        return this.updateById(order);

    }
}
