package com.example.zhou.grouping.httpBean;

/**
 * Created by Zhou on 2017/4/22.
 */

public class SelectSgIDAndIfOpen {

    /**
     * sgid : 1
     * ifOpen : 0
     * ifToMax:0
     */

    private String sgid;
    private String ifOpen;
    private String ifToMax;

    public String getIfToMax() {
        return ifToMax;
    }

    public void setIfToMax(String ifToMax) {
        this.ifToMax = ifToMax;
    }

    public String getSgid() {
        return sgid;
    }

    public void setSgid(String sgid) {
        this.sgid = sgid;
    }

    public String getIfOpen() {
        return ifOpen;
    }

    public void setIfOpen(String ifOpen) {
        this.ifOpen = ifOpen;
    }
}
