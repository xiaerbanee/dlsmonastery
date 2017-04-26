package net.myspring.future.modules.basic.web.Query;

import java.util.List;

public class ExpressCompanyQuery {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpressType() {
        return expressType;
    }

    public void setExpressType(String expressType) {
        this.expressType = expressType;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }



    public String getReachPlace() {
        return reachPlace;
    }

    public void setReachPlace(String reachPlace) {
        this.reachPlace = reachPlace;
    }


    public List<String> getExpressTypeList() {
        return expressTypeList;
    }

    public void setExpressTypeList(List<String> expressTypeList) {
        this.expressTypeList = expressTypeList;
    }


    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    private String name;
    private String expressType;
    private String reachPlace;
    private String mobilePhone;
    private String contator;
    private List<String> expressTypeList;
}
