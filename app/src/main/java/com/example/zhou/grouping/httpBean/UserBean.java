package com.example.zhou.grouping.httpBean;

/**
 * Created by Zhou on 2017/4/14.
 */

public class UserBean {

    /**
     * cPasswd : 123456
     * cName : 周璨珥
     * cSex : 0
     * cClass : 计算1304
     */

    private String cPasswd;
    private String cName;
    private String cSex;
    private String cClass;
    private String email;
    private String cInfo;

    public String getcInfo() {
        return cInfo;
    }

    public void setcInfo(String cInfo) {
        this.cInfo = cInfo;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCPasswd() {
        return cPasswd;
    }

    public void setCPasswd(String cPasswd) {
        this.cPasswd = cPasswd;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getCSex() {
        return cSex;
    }

    public void setCSex(String cSex) {
        this.cSex = cSex;
    }

    public String getCClass() {
        return cClass;
    }

    public void setCClass(String cClass) {
        this.cClass = cClass;
    }
}
