package net.myspring.future.modules.crm.manager;

import net.myspring.future.modules.basic.client.SequenceClient;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RedisIdManager {

    @Autowired
    private SequenceClient sequenceClient;

    private static final String BUSINESS_ID_SEQ_NAME_PREFIX_GOODS_ORDER = "businessIdGoodsOrder:";
    private static final String BUSINESS_ID_SEQ_NAME_PREFIX_AD_GOODS_ORDER = "businessIdAdGoodsOrder:";
    private static final String BUSINESS_ID_SEQ_NAME_PREFIX_SHOP_ALLOT = "businessIdShopAllot:";
    private static final String BUSINESS_ID_SEQ_NAME_PREFIX_STORE_ALLOT = "businessIdStoreAllot:";
    private static final String BUSINESS_ID_SEQ_NAME_PREFIX_SHOP_PROMOTION = "businessIdShopPromotion:";
    private static final String BUSINESS_ID_SEQ_NAME_PREFIX_AFTER_SALE = "businessIdAfterSale:";

    public String getNextGoodsOrderBusinessId(LocalDate billDate) {
        return getNextBusinessId(BUSINESS_ID_SEQ_NAME_PREFIX_GOODS_ORDER, billDate);
    }

    public String getNextAdGoodsOrderBusinessId(LocalDate billDate) {
        return getNextBusinessId(BUSINESS_ID_SEQ_NAME_PREFIX_AD_GOODS_ORDER, billDate);
    }

    public String getNextShopAllotBusinessId(LocalDate date) {
        return getNextBusinessId(BUSINESS_ID_SEQ_NAME_PREFIX_SHOP_ALLOT, date);
    }

    public String getNextStoreAllotBusinessId(LocalDate date) {
        return getNextBusinessId(BUSINESS_ID_SEQ_NAME_PREFIX_STORE_ALLOT, date);
    }

    public String getNextShopPromotionBusinessId(LocalDate date) {
        return getNextBusinessId(BUSINESS_ID_SEQ_NAME_PREFIX_SHOP_PROMOTION, date);
    }

    public String getNextAfterSaleBusinessId(LocalDate date) {
        return getNextBusinessId(BUSINESS_ID_SEQ_NAME_PREFIX_AFTER_SALE, date);
    }

    private String getNextBusinessId(String seqNamePrefix, LocalDate date) {

        String formatDateStr = LocalDateUtils.formatLocalDate(date,"yyMMdd");

        Long nextVal = sequenceClient.nextVal(seqNamePrefix + formatDateStr);
        String nextBusinessIdSuffix = "000000" + String.valueOf(nextVal);
        nextBusinessIdSuffix = nextBusinessIdSuffix.substring(nextBusinessIdSuffix.length() - 6);
        return formatDateStr + nextBusinessIdSuffix;

    }

}
