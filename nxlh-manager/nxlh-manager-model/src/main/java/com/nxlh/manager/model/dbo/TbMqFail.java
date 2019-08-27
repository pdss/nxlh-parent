package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_mq_fail")
public class TbMqFail extends BaseDBO {

    private String queue;

    private String exchange;

    private String messagedata;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return addtime
     */
    public Date getAddtime() {
        return addtime;
    }

    /**
     * @param addtime
     */
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    /**
     * @return isdelete
     */
    public Integer getIsdelete() {
        return isdelete;
    }

    /**
     * @param isdelete
     */
    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    /**
     * @return queue
     */
    public String getQueue() {
        return queue;
    }

    /**
     * @param queue
     */
    public void setQueue(String queue) {
        this.queue = queue == null ? null : queue.trim();
    }

    /**
     * @return exchange
     */
    public String getExchange() {
        return exchange;
    }

    /**
     * @param exchange
     */
    public void setExchange(String exchange) {
        this.exchange = exchange == null ? null : exchange.trim();
    }

    /**
     * @return messagedata
     */
    public String getMessagedata() {
        return messagedata;
    }

    /**
     * @param messagedata
     */
    public void setMessagedata(String messagedata) {
        this.messagedata = messagedata == null ? null : messagedata.trim();
    }
}