package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.nxlh.manager.mapper.TbTagMapper;
import com.nxlh.manager.model.dbo.TbTag;
import com.nxlh.manager.model.dto.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import com.nxlh.manager.service.TagService;

@Service(interfaceClass = TagService.class)
public class TagServiceImp extends BaseDbCURDSServiceImpl<TbTagMapper, TbTag, TagDTO> implements TagService {

    @Autowired
    private TbTagMapper tagMapper;

    //根据名字查询
    @Override
    public List<TagDTO> getByName(String name) {
        if (!("".equals(name.trim())) && name != null) {
            Example example = new Example(TbTag.class);
            example.createCriteria().andLike("tagname", "%" + name + "%").andEqualTo("isdelete", 0);
            PageHelper.startPage(0, 5);
            List<TbTag> tbTags = tagMapper.selectByExample(example);
            return this.beanListMap(tbTags, this.currentDTOClass());
        }
        return null;
    }
}
