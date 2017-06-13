package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.repository.ProductTypeRepository;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.ReportScore;
import net.myspring.future.modules.crm.domain.ReportScoreArea;
import net.myspring.future.modules.crm.domain.ReportScoreOffice;
import net.myspring.future.modules.crm.dto.ImeAllotDto;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ReportScoreDto;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.repository.ReportScoreAreaRepository;
import net.myspring.future.modules.crm.repository.ReportScoreOfficeRepository;
import net.myspring.future.modules.crm.repository.ReportScoreRepository;
import net.myspring.future.modules.crm.web.form.ReportScoreForm;
import net.myspring.future.modules.crm.web.query.ReportScoreQuery;
import net.myspring.future.modules.third.domain.OppoPlantAgentProductSel;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import org.elasticsearch.xpack.notification.email.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class ReportScoreService {


    @Autowired
    private ReportScoreRepository reportScoreRepository;
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private ReportScoreOfficeRepository reportScoreOfficeRepository;
    @Autowired
    private ReportScoreAreaRepository reportScoreAreaRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<ReportScoreDto> findPage(Pageable pageable, ReportScoreQuery reportScoreQuery) {
        Page<ReportScoreDto> page = reportScoreRepository.findPage(pageable,reportScoreQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(String id) {
        reportScoreRepository.logicDelete(id);
    }

    public ReportScoreDto findDto(String id) {
        ReportScoreDto reportScoreDto = reportScoreRepository.findDto(id);
        cacheUtils.initCacheInput(reportScoreDto);
        return reportScoreDto;
    }

    public String getProductTypeNames() {
        List<ProductType> productTypes = productTypeRepository.findByScoreType(true);

         return StringUtils.join(CollectionUtil.extractToList(productTypes, "name"), CharConstant.COMMA_FULL);

    }

    public String getNotScores() {
        List<String> notScores = findNotScoreItemNumbers(LocalDateTime.now());
        if(CollectionUtil.isEmpty(notScores)) {
            return "";
        }
        return StringUtils.join(notScores,CharConstant.COMMA_FULL);

    }

    //查找当月不参与打分但产生电子保卡的数据
    private List<String> findNotScoreItemNumbers(LocalDateTime localDateTime) {
        List<String> result = Lists.newArrayList();
        LocalDate firstDayOfMonth = LocalDateTimeUtils.getFirstDayOfMonth(localDateTime).toLocalDate();
        LocalDate dateEnd = localDateTime.toLocalDate().plusDays(1);
        //TODO 需要继续实现findNotScoreItemNumbers
//
//        List<NameQty> monthList = null;
//        //物料编码-颜色编码对应表
//        Map<String,String> itemNumberMap = Maps.newHashMap();
//        Map<String,ProductType> map = Maps.newHashMap();
//        if(CompanyBrand.OPPO.name().equals(account.getCompany().getBrand())) {
//            monthList = oppoPlantProductItemelectronSelRepository.findNameQtyList(firstDayOfMonth, dateEnd);
//            List<OppoPlantAgentProductSel> oppoPlantAgentProductSels = oppoPlantAgentProductSelRepository.findAll();
//            itemNumberMap = CollectionsUtil.extractToMap(oppoPlantAgentProductSels, "itemNumber","colorId");
//            for(OppoPlantAgentProductSel item:oppoPlantAgentProductSels) {
//                if(item.getProduct() != null && item.getProduct().getProductType() != null) {
//                    ProductType productType = item.getProduct().getProductType();
//                    if(productType !=null) {
//                        map.put(item.getColorId(), productType);
//                    }
//                }
//            }
//        } else if (CompanyBrand.vivo.name().equals(account.getCompany().getBrand())) {
//            monthList = vivoPlantElectronicsnRepository.findNameQtyList(firstDayOfMonth, dateEnd);
//            List<VivoPlantProducts> vivoPlantProducts = vivoPlantProductsRepository.findAll();
//            itemNumberMap = CollectionsUtil.extractToMap(vivoPlantProducts, "itemNumber","colorId");
//            for(VivoPlantProducts item:vivoPlantProducts) {
//                if(item.getProduct() != null && item.getProduct().getProductType() != null) {
//                    ProductType productType = item.getProduct().getProductType();
//                    if(productType !=null) {
//                        map.put(item.getColorId(), productType);
//                    }
//                }
//            }
//        }
//        if(CollectionsUtil.isNotEmpty(monthList)){
//            for(NameQty nameQty:monthList) {
//                String colorId = itemNumberMap.get(nameQty.getName());
//                if(!map.containsKey(colorId) && org.apache.commons.lang.StringUtils.isNotBlank(nameQty.getName())) {
//                    result.add(nameQty.getName());
//                }
//            }
//        }
        return result;
    }

    public ReportScore save(ReportScoreForm reportScoreForm) {
        LocalDate date = reportScoreForm.getScoreDate();
        LocalDate dateStart = date;
        LocalDate dateEnd = date.plusDays(1);
        LocalDate firstDayOfMonth = LocalDateUtils.getFirstDayOfThisMonth(date);
        //最近一个月
        LocalDate lastDayOfMonth = LocalDateUtils.getLastDayOfThisMonth(date).plusDays(1);
        //当日电子保卡
        List<ProductIme> dateProductImes = productImeRepository.findByRetailDate(dateStart, dateEnd);
        if(CollectionUtil.isEmpty(dateProductImes)) {
            throw new ServiceException("exception_report_score_not_in_system" );
        }
        //当月电子保卡
        List<ProductIme> monthProductImes = productImeRepository.findByRetailDate(firstDayOfMonth, dateEnd);
        //最近一个月累计电子保卡
        List<ProductIme> recentMonthProductImes = productImeRepository.findByRetailDate(lastDayOfMonth, dateEnd);
        //当前需要统计型号
        List<ProductType> productTypes = productTypeRepository.findByScoreType(true);
        Map<String,ProductType> productTypeMap = CollectionUtil.extractToMap(productTypes,"id");
        Map<String,ReportScoreOffice> reportScoreOfficeMap = Maps.newHashMap();
        Map<String,ReportScoreArea> reportScoreAreaMap = Maps.newHashMap();
        return null;
    }

}
