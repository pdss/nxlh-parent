package com.nxlh.manager.model.dbo;


import com.alibaba.fastjson.annotation.JSONField;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.model.dto.BaseDTO;
import lombok.Setter;
import lombok.ToString;
import org.apache.solr.client.solrj.beans.Field;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Setter
@ToString
public abstract class BaseDBO implements BaseDTO {

    public BaseDBO() {
//        this.id = IDUtils.genUUID();
//        this.addtime = new Date();
        this.isdelete = 0;

    }

    @Id
    @Field
    protected String id;
    protected Date addtime;
    @JSONField(serialize = false)
    protected Integer isdelete;


    public Integer getIsdelete() {
        return isdelete;
    }

    public String getId() {
        return id;
    }

    public Date getAddtime() {
        return addtime;
    }
}
