package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_log")
public class TbLog extends BaseDBO {

    /**
     * 日志等级
     */
    private Integer level;

    /**
     * 日志内容
     */
    private String log;

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
     * 获取日志等级
     *
     * @return level - 日志等级
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 设置日志等级
     *
     * @param level 日志等级
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 获取日志内容
     *
     * @return log - 日志内容
     */
    public String getLog() {
        return log;
    }

    /**
     * 设置日志内容
     *
     * @param log 日志内容
     */
    public void setLog(String log) {
        this.log = log == null ? null : log.trim();
    }
}