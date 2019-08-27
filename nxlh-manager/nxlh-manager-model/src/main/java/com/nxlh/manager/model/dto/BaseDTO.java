package com.nxlh.manager.model.dto;


import java.io.Serializable;
import java.util.Date;

public interface BaseDTO extends Serializable {

    String getId();

    void setId(String id);

    Date getAddtime();


    void setAddtime(Date addtime);

    void setIsdelete(Integer isdelete);

    Integer getIsdelete();


}
