package com.example.zhou.grouping.httpBean;

import java.io.Serializable;

/**
 * Created by Zhou on 2017/4/15.
 */

public class LoadGroups implements Serializable {

    /**
     * gID : 20160414183920
     * gName : test
     * gPasswd : 1
     * gMin : 5
     * gMax : 5
     */

    private String gID;
    private String gName;
    private String gPasswd;
    private String gMin;
    private String gMax;

    public String getGID() {
        return gID;
    }

    public void setGID(String gID) {
        this.gID = gID;
    }

    public String getGName() {
        return gName;
    }

    public void setGName(String gName) {
        this.gName = gName;
    }

    public String getGPasswd() {
        return gPasswd;
    }

    public void setGPasswd(String gPasswd) {
        this.gPasswd = gPasswd;
    }

    public String getGMin() {
        return gMin;
    }

    public void setGMin(String gMin) {
        this.gMin = gMin;
    }

    public String getGMax() {
        return gMax;
    }

    public void setGMax(String gMax) {
        this.gMax = gMax;
    }
}
