package com.nxlh.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.dozer.Mapping;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageParameter implements Serializable {

    public PageParameter(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Mapping("pageNum")
    private int pageIndex;
    private int pageSize = 20;





}
