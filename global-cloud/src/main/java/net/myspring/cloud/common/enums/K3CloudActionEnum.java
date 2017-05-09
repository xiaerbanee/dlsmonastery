package net.myspring.cloud.common.enums;

/**
 * Created by liuj on 2017/5/8.
 */
public enum K3CloudActionEnum {
        SAVE("Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc"),
        VIEW("Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc"),
        SUBMIT("Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Submit.common.kdsvc"),
        AUDIT("Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Audit.common.kdsvc"),
        UN_AUDIT("Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.UnAudit.common.kdsvc"),
        STATUS_CONVERT("Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.StatusConvert.common.kdsvc"),
        VALIDATE_USER("Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc");

        private String value;

        K3CloudActionEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
}
