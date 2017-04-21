package net.myspring.future.common.enums;

/**
 * Created by liuj on 2016/12/31.
 */
public enum CompanyConfigCodeEnum {
        AD_DEFAULT_STORE_ID("广告发货仓库"),
        DEFAULT_STORE_ID("默认发货仓库"),
        GOOD_STORE_ID("好机库"),
        DEPOSIT_STORE_ID("寄存机库"),
        DISUSE_STORE_ID("淘汰机库"),
        BAD_STORE_ID("坏机库"),
        EXPRESS_PRODUCT_ID("运费货品"),
        DEFAULT_EXPRESS_COMPANY_ID("默认快递公司"),
        DEFAULT_AD_EXPRESS_COMPANY_ID("默认物料快递公司"),
        EXPRESS_SHOULD_GET_RULE("运费应收规则"),
        EXPRESS_SHOULD_PAY_RULE("运费应付规则"),
        GOODS_ORDER_REBATE_RULE("让利规则"),
        PRODUCT_GOODS_GROUP_IDS("成品货品分组"),
        PRODUCT_GOODS_POP_GROUP_IDS("成品POP分组"),
        PRODUCT_POP_GROUP_IDS("物料POP分组"),
        PRODUCT_COUNTER_GROUP_IDS("物料柜台分组"),
        SHOP_DELEGATE_GROUP_IDS("寄售客户分组"),
        STORE_DELEGATE_GROUP_IDS("寄售大库分组"),
        STORE_AD_GROUP_IDS("物料大库分组"),
        AD_SHIP_PRICE_RULE("物料运费"),
        BASE_URL("域名"),
        SHOP_IMAGE_TYPE("形象更换类型"),
        FACTORY_AGENT_CODES("工厂用户名"),
        FACTORY_AGENT_NAMES("工厂客户名称"),
        FACTORY_AGENT_PASSWORDS("工厂密码"),
        LX_FACTORY_AGENT_CODES("联信工厂用户名"),
        LX_DEFAULT_STORE_ID("联信默认发货仓库"),
        CARRIER_LOCK_OFFICE("商城订单锁定办事处"),
        DEFALULT_CARRIAR_STORE_ID("移动默认仓库"),
        DEFAULT_PRICESYSTEM_ID("批发价格体系"),
        PURCHASE_OUT_PRODUCT_ID("采购退货货品"),
        IS_NOT_DEPOSIT_STORE("非订金发货仓库"),
        STK_INSTOCK_IDS("采购入库价格"),
        HONGBAO_RULE("抽奖规则"),
        AD_CHARGE_DEPOTS("物料原价出库门店"),
        BRAND("品牌"),
        CLOUD_DB_ID("金蝶ID"),
        CLOUD_DB_URL("金蝶URL"),
        COMPANY_CODE("公司code"),
        MERGE_STORE_IDS("昌东仓库"),
        CLOUD_DB_NAME("金蝶名称");
        private String name;
        CompanyConfigCodeEnum(String name) {
                this.name = name;
        }
        public String getName() {
                return name;
        }
        public void setName(String name) {
                this.name = name;
        }
        public String getCode() {
                return this.toString();
        }
}
