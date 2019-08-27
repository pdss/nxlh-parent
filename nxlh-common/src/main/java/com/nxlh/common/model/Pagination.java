package com.nxlh.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.dozer.Mapping;

import java.util.List;

@Data
@ToString

@NoArgsConstructor
public class Pagination<T> extends PageParameter {

    public Pagination(int pageIndex, int pageSize, int total) {
        var pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        this.totalPages = pages;
        this.total = total;
        this.setPageIndex(pageIndex);
        this.setPageSize(pageSize);
    }

    public Pagination(Pagination pagination, List targetList) {
        this.totalPages = pagination.totalPages;
        this.total= pagination.getTotal();
        this.setPageIndex(pagination.getPageIndex());
        this.setPageSize(pagination.getPageSize());
        if (targetList != null) {
            this.setList(targetList);
        } else {
            this.setList(pagination.getList());
        }
    }

    @Mapping("pages")
    private int totalPages;

    @Mapping("total")
    private int total;

    private List<T> list;


}
