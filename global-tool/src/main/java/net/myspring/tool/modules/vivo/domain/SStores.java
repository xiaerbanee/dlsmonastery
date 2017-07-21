package net.myspring.tool.modules.vivo.domain;

import net.myspring.util.cahe.annotation.CacheInput;

public class SStores {
    private String storeID;
    private String storeName;
    private String remark;
    private String shortCut;
    @CacheInput(inputKey = "offices",inputInstance = "storeID" ,outputInstance = "agentCode")
    private String agentCode;
    @CacheInput(inputKey = "offices",inputInstance = "storeID" ,outputInstance = "jointLevel")
    private String jointLevel;

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getJointLevel() {
        return jointLevel;
    }

    public void setJointLevel(String jointLevel) {
        this.jointLevel = jointLevel;
    }
}
