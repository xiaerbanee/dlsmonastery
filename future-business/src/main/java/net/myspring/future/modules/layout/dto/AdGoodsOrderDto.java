package net.myspring.future.modules.layout.dto;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.domain.AdGoodsOrderDetail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by lihx on 2017/4/15.
 */
public class AdGoodsOrderDto {
    private String storeId;
    private String outShopId;
    private String shopId;
    private BigDecimal amount;
    private String outCode;
    private String billType;
    private LocalDate billDate;
    private String billRemarks;
    private Integer smallQty;
    private Integer mediumQty;
    private Integer largeQty;
    private Integer version = 0;
    private String processInstanceId;
    private String processStatus;
    private String businessId;
    private Boolean splitBill;
    private Boolean isUrgent;
    private ExpressOrder expressOrder;
    private String expressOrderId;
    private String employeeId;
    private String processTypeId;
    private String processFlowId;
    private String cloudSynId;
    private List<AdGoodsOrderDetail> adGoodsOrderDetailList = Lists.newArrayList();
    private List<String> adGoodsOrderDetailIdList = Lists.newArrayList();
    private Depot store;
    private Depot shop;
    private Depot outShop;
    private String parentId;
    private AdGoodsOrder parent;


}
