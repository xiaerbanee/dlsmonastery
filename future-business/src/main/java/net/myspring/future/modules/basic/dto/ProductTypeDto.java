package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.crm.domain.PriceChangeCommission;
import net.myspring.future.modules.crm.domain.PriceChangeProduct;
import net.myspring.future.modules.crm.domain.ProductImeUpload;
import net.myspring.future.modules.crm.domain.ProductMonthPriceDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class ProductTypeDto extends DataDto<ProductType> {
    private String name;
    private String reportName;
    private String code;
    private Integer version = 0;
    private BigDecimal baokaPrice;
    private BigDecimal price1;
    private BigDecimal price2;
    private BigDecimal price3;
    private Boolean scoreType;
    private List<PriceChangeCommission> priceChangeCommissionList = Lists.newArrayList();
    private List<String> priceChangeCommissionIdList = Lists.newArrayList();
    private List<PriceChangeProduct> priceChangeProductList = Lists.newArrayList();
    private List<String> priceChangeProductIdList = Lists.newArrayList();
    private List<Product> productList = Lists.newArrayList();
    private List<String> productIdList = Lists.newArrayList();
    private List<ProductImeUpload> productImeUploadList = Lists.newArrayList();
    private List<String> productImeUploadIdList = Lists.newArrayList();
    private List<ProductMonthPriceDetail> productMonthPriceDetailList = Lists.newArrayList();
    private List<String> productMonthPriceDetailIdList = Lists.newArrayList();
    private DemoPhoneType demoPhoneType;
    private String demoPhoneTypeId;
    private List<String> commissionAreaIdList = Lists.newArrayList();
    private List<String> commissionBasicIdList = Lists.newArrayList();
    private List<String> taskProductTypeDetailIdList = Lists.newArrayList();
}
