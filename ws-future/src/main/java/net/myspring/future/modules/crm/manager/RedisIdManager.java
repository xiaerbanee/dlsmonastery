package net.myspring.future.modules.crm.manager;

import net.myspring.common.exception.ServiceException;
import net.myspring.future.modules.crm.repository.AfterSaleRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderRepository;
import net.myspring.future.modules.crm.repository.StoreAllotRepository;
import net.myspring.future.modules.layout.repository.AdGoodsOrderRepository;
import net.myspring.future.modules.layout.repository.ShopAllotRepository;
import net.myspring.future.modules.layout.repository.ShopPromotionRepository;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.function.Supplier;

@Component
public class RedisIdManager {

    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private AdGoodsOrderRepository adGoodsOrderRepository;
    @Autowired
    private ShopAllotRepository shopAllotRepository;
    @Autowired
    private StoreAllotRepository storeAllotRepository;
    @Autowired
    private ShopPromotionRepository shopPromotionRepository;
    @Autowired
    private AfterSaleRepository afterSaleRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String BUSINESS_ID_KEY_PREFIX_GOODS_ORDER = "businessIdGoodsOrder:";
    private static final String BUSINESS_ID_KEY_PREFIX_AD_GOODS_ORDER = "businessIdAdGoodsOrder:";
    private static final String BUSINESS_ID_KEY_PREFIX_SHOP_ALLOT = "businessIdShopAllot:";
    private static final String BUSINESS_ID_KEY_PREFIX_STORE_ALLOT = "businessIdStoreAllot:";
    private static final String BUSINESS_ID_KEY_PREFIX_SHOP_PROMOTION = "businessIdShopPromotion:";
    private static final String BUSINESS_ID_KEY_PREFIX_AFTER_SALE = "businessIdAfterSale:";

    public synchronized String getNextGoodsOrderBusinessId(LocalDate billDate) {
        return getNextBusinessId(BUSINESS_ID_KEY_PREFIX_GOODS_ORDER, billDate, ()->goodsOrderRepository.findMaxBusinessId(billDate));
    }

    public synchronized String getNextAdGoodsOrderBusinessId(LocalDate billDate) {
        return getNextBusinessId(BUSINESS_ID_KEY_PREFIX_AD_GOODS_ORDER, billDate, ()->adGoodsOrderRepository.findMaxBusinessId(billDate));
    }

    public synchronized String getNextShopAllotBusinessId(LocalDate date) {
        return getNextBusinessId(BUSINESS_ID_KEY_PREFIX_SHOP_ALLOT, date, ()->shopAllotRepository.findMaxBusinessId(date.atStartOfDay()));
    }

    public synchronized String getNextStoreAllotBusinessId(LocalDate date) {
        return getNextBusinessId(BUSINESS_ID_KEY_PREFIX_STORE_ALLOT, date, ()->storeAllotRepository.findMaxBusinessId(date.atStartOfDay()));
    }

    public synchronized String getNextShopPromotionBusinessId(LocalDate date) {
        return getNextBusinessId(BUSINESS_ID_KEY_PREFIX_SHOP_PROMOTION, date, ()->shopPromotionRepository.findMaxBusinessId(date.atStartOfDay()));
    }

    public synchronized String getNextAfterSaleBusinessId(LocalDate date) {
        return getNextBusinessId(BUSINESS_ID_KEY_PREFIX_AFTER_SALE, date, ()->afterSaleRepository.findMaxBusinessId(date.atStartOfDay()));
    }

    private String getNextBusinessId(String keyPrefix, LocalDate date, Supplier<String> dbMaxBusinessIdSupplier) {
        try{
            String key = keyPrefix + LocalDateUtils.format(date);
            String currBusinessId = getCurrentBusinessId(key, dbMaxBusinessIdSupplier, date);
            String nextBusinessId = String.valueOf(Long.valueOf(currBusinessId)+1);
            redisTemplate.getConnectionFactory().getConnection().set(key.getBytes("UTF-8"), nextBusinessId.getBytes("UTF-8"));
            return nextBusinessId;
        }catch(Exception e){
            throw new ServiceException(e);
        }
    }

    private String getCurrentBusinessId(String key, Supplier<String> dbMaxBusinessIdSupplier, LocalDate date){
        try{
            byte[] redisValue = redisTemplate.getConnectionFactory().getConnection().get(key.getBytes("UTF-8"));
            if(redisValue!=null){
                return new String(redisValue,"UTF-8");
            }

            String dbMaxBusinessId = dbMaxBusinessIdSupplier.get();
            return StringUtils.isBlank(dbMaxBusinessId) ? getInitialBusinessId(date) : dbMaxBusinessId;
        }catch(Exception e){
            throw new ServiceException(e);
        }
    }

    private String getInitialBusinessId(LocalDate date) {
        return LocalDateUtils.formatLocalDate(date,"yyMMdd") + "000000";
    }

}
