package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbTagMapper;
import com.nxlh.manager.model.dbo.TbTag;
import com.nxlh.manager.model.dto.TagDTO;

import java.util.List;

public interface TagService extends BaseService<TagDTO, TbTagMapper, TbTag> {


    //根据名字查询
    List<TagDTO> getByName(String name);

}
