package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.enums.ExpressOrderTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.web.form.PrintConfigForm;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.dto.ExpressOrderPrintDto;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PrintService {
    @Value("${app.print.version}")
    private String version;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;

    public Map<String,String> checkConfig(PrintConfigForm printConfigForm) {
        Map<String,String> map = Maps.newHashMap();
        //检查配置
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isBlank(printConfigForm.getOrderType())) {
            sb.append("请填写单据类型\n");
        } else {
            if(!ExpressOrderTypeEnum.getList().contains(printConfigForm.getOrderType())) {
                sb.append("单据类型：");
                sb.append(printConfigForm.getOrderType());
                sb.append(" 在系统中不存在\n");
            }
        }
        if(StringUtils.isBlank(printConfigForm.getStoreNames())) {
            sb.append("请填写仓库名称\n");
        } else {
            List<String> storeNameList = StringUtils.getSplitList(printConfigForm.getStoreNames(), CharConstant.COMMA);
            for(String storeName:storeNameList) {
                if(depotRepository.findByName(storeName)==null) {
                    sb.append("仓库：").append(storeName).append(" 在系统中不存在\n");
                }
            }
        }
        if(!version.equals(printConfigForm.getVersion())) {
            sb.append("最新打印软件版本为：").append(version).append("，当前使用版本为").append(printConfigForm.getVersion());
        }
        String message = sb.toString();
        if(StringUtils.isBlank(message)) {
            map.put("success","true");
        } else {
            map.put("success","false");
            map.put("message",message);
        }
        return map;
    }

    public void print(String expressOrderId) {
        ExpressOrder expressOrder = expressOrderRepository.findOne(expressOrderId);
        expressOrder.setOutPrintDate(LocalDateTime.now());
        expressOrder.setLastModifiedBy(RequestUtils.getAccountId());
        expressOrderRepository.save(expressOrder);
    }


    public List<ExpressOrderPrintDto> findOrderList(PrintConfigForm printConfigForm) {
        List<ExpressOrder> list = Lists.newArrayList();
        List<String> storeNameList = StringUtils.getSplitList(printConfigForm.getStoreNames(),CharConstant.COMMA);
        List<Depot> depots = depotRepository.findByNameList(storeNameList);
        final List<Long> depotIds = CollectionUtil.extractToList(depots, "id");
        if(StringUtils.isNotBlank(printConfigForm.getOrderIds())) {
            List<String> orderIds = StringUtils.getSplitList(printConfigForm.getOrderIds(),CharConstant.COMMA);
            List<String> businessIds = Lists.newArrayList();
            for(String orderId:orderIds) {
                if(StringUtils.isNotBlank(orderId)) {
                    businessIds.add(IdUtils.getId(orderId));
                }
            }
            if(ExpressOrderTypeEnum.物料订单.name().equals(printConfigForm.getOrderType())) {
                list = expressOrderRepository.findByTypeAndExtendIds(printConfigForm.getOrderType(), businessIds);
            }else{
                list = expressOrderRepository.findByTypeAndBusinessIds(printConfigForm.getOrderType(), businessIds);
            }
        } else {
            final LocalDate printDate=printConfigForm.getPrintDate()!=null? LocalDateUtils.parse(printConfigForm.getPrintDate()):LocalDate.now();
            Specification<ExpressOrder> spec = new Specification<ExpressOrder>() {
                public Predicate toPredicate(Root<ExpressOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                    List<Predicate> predicates = Lists.newArrayList();
                    predicates.add(builder.equal(root.get("printDate").as(LocalDate.class), printDate));
                    predicates.add(builder.equal(root.get("enabled").as(Boolean.class), true));
                    predicates.add(builder.isNull(root.get("outPrintDate")));
                    if(PrintConfigForm.PrintPickUpType.自提.name().equals(printConfigForm.getPickUpType())) {
                        predicates.add(root.get("shipType").as(String.class).in(Arrays.asList(ShipTypeEnum.总部自提.name(),ShipTypeEnum.地区自提.name())));
                    } else if (PrintConfigForm.PrintPickUpType.非自提.name().equals(printConfigForm.getPickUpType())) {
                        predicates.add(root.get("shipType").as(String.class).in(Arrays.asList(ShipTypeEnum.总部发货.name(),ShipTypeEnum.地区发货.name())));
                    }
                    predicates.add(root.get("fromDepot").get("id").in(depotIds));
                    predicates.add(builder.isFalse(root.get("locked").as(Boolean.class)));
                    predicates.add(builder.equal(root.get("extendType").as(String.class), printConfigForm.getOrderType()));
                    predicates.add(builder.isNotNull(root.get("outCode")));
                    return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };

        }
        return null;
    }

}
