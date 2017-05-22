package net.myspring.future.modules.layout.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.layout.domain.ShopPrint;
import net.myspring.util.cahe.annotation.CacheInput;

/**
 * Created by zhangyf on 2017/5/5.
 */
public class ShopPrintDto extends DataDto<ShopPrint>{

    private String officeId;
    @CacheInput(inputKey = "offices", inputInstance = "officeId", outputInstance = "name")
    private String officeName;
    private String printType;
    private Integer qty;
    private String contator;
    private String mobilePhone;
    private String address;
    private String content;
    private String attachment;
    private String processStatus;
    private String processPositionId;
    private String processInstanceId;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getContator() {
        return contator;
    }

    public void setContator(String contator) {
        this.contator = contator;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }


    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getProcessPositionId() {
        return processPositionId;
    }

    public void setProcessPositionId(String processPositionId) {
        this.processPositionId = processPositionId;
    }

    public Boolean getIsAuditable(){
        if(RequestUtils.getRequestEntity().getPositionId().equals(getProcessPositionId())|| RequestUtils.getAccountId().equalsIgnoreCase("1")){
            return true;
        }else{
            return false;
        }
    }

    public Boolean getIsEditable(){
        if(RequestUtils.getAccountId().equals(getCreatedBy())|| RequestUtils.getAccountId().equalsIgnoreCase("1")){
            return true;
        }else {
            return false;
        }
    }
}
