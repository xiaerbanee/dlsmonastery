package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.OfficeUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.repository.ProductTypeRepository;
import net.myspring.future.modules.crm.domain.ReportScore;
import net.myspring.future.modules.crm.domain.ReportScoreArea;
import net.myspring.future.modules.crm.domain.ReportScoreOffice;
import net.myspring.future.modules.crm.dto.ReportScoreDataDto;
import net.myspring.future.modules.crm.dto.ReportScoreDto;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.repository.ReportScoreAreaRepository;
import net.myspring.future.modules.crm.repository.ReportScoreOfficeRepository;
import net.myspring.future.modules.crm.repository.ReportScoreRepository;
import net.myspring.future.modules.crm.web.form.ReportScoreForm;
import net.myspring.future.modules.crm.web.query.ReportScoreQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private RedisTemplate redisTemplate;


    public static BigDecimal getScore(BigDecimal saleMoney, BigDecimal totalSaleMoney, BigDecimal point, BigDecimal companyScore) {
        DecimalFormat df = new DecimalFormat("0.00");
        //（区域销售额*实际打分）/(区域点位*全省销售额)
        BigDecimal score = saleMoney.multiply(new BigDecimal("100")).multiply(companyScore).divide(point.multiply(totalSaleMoney), 2, BigDecimal.ROUND_HALF_DOWN);
        return new BigDecimal(df.format(score));
    }

    public Page<ReportScoreDto> findPage(Pageable pageable, ReportScoreQuery reportScoreQuery) {
        Page<ReportScoreDto> page = reportScoreRepository.findPage(pageable, reportScoreQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public String getProductTypeNames() {
        List<ProductType> productTypes = productTypeRepository.findByScoreType(true);
        return StringUtils.join(CollectionUtil.extractToList(productTypes, "name"), CharConstant.COMMA_FULL);

    }

    public String getNotScores() {
        List<String> notScores = findNotScoreItemNumbers(LocalDateTime.now());
        if (CollectionUtil.isEmpty(notScores)) {
            return "";
        }
        return StringUtils.join(notScores, CharConstant.COMMA_FULL);

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

    public ReportScoreDto findOne(ReportScoreDto reportScoreDto) {
        if (!reportScoreDto.isCreate()) {
            ReportScore reportScore = reportScoreRepository.getOne(reportScoreDto.getId());
            reportScoreDto = BeanUtil.map(reportScore, ReportScoreDto.class);
            cacheUtils.initCacheInput(reportScoreDto);
        }
        return reportScoreDto;
    }

    public ReportScore save(ReportScoreForm reportScoreForm) {
        ReportScore reportScore = BeanUtil.map(reportScoreForm, ReportScore.class);
        LocalDate date = reportScoreForm.getScoreDate();
        LocalDate dateStart = date;
        LocalDate dateEnd = date.plusDays(1);
        LocalDate firstDayOfMonth = LocalDateUtils.getFirstDayOfThisMonth(date);
        LocalDate dayOfLastMonth = date.minusMonths(1);
        List<ReportScoreDataDto> recentMonthReportScoreDataList = reportScoreRepository.findDataByRetailDate(dayOfLastMonth, dateEnd);
        Map<String, List<String>> areaMap = officeClient.getLastRuleMapByOfficeRuleName("办事处");
        Map<String, List<String>> officeMap = officeClient.getLastRuleMapByOfficeRuleName("考核区域");
        Map<String, ReportScoreArea> reportScoreAreaMap = Maps.newHashMap();
        Map<String, ReportScoreOffice> reportScoreOfficeMap = Maps.newHashMap();
        //每个区域打分对应哪个办事处打分
        Map<String, ReportScoreArea> map = Maps.newHashMap();
        //统计最近一个月的金额和数量
        for (ReportScoreDataDto reportScoreDataDto : recentMonthReportScoreDataList) {
            String areaId = getOfficeId(areaMap, reportScoreDataDto.getOfficeId());
            String officeId = getOfficeId(officeMap, reportScoreDataDto.getOfficeId());
            if (StringUtils.isNotBlank(areaId) && StringUtils.isNotBlank(officeId)) {
                if (!reportScoreAreaMap.containsKey(areaId)) {
                    ReportScoreArea reportScoreArea = new ReportScoreArea();
                    reportScoreArea.setOfficeId(areaId);
                    reportScoreArea.setScoreDate(date);
                    reportScoreAreaMap.put(areaId, reportScoreArea);
                }
                ReportScoreArea reportScoreArea = reportScoreAreaMap.get(areaId);
                reportScoreArea.setRecentMonthSaleQty(reportScoreArea.getRecentMonthSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScoreArea.setRecentMonthSaleMoney(reportScoreArea.getRecentMonthSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
                if (!reportScoreOfficeMap.containsKey(officeId)) {
                    ReportScoreOffice reportScoreOffice = new ReportScoreOffice();
                    reportScoreOffice.setOfficeId(officeId);
                    reportScoreOffice.setScoreDate(date);
                    reportScoreOfficeMap.put(officeId, reportScoreOffice);
                    map.put(officeId, reportScoreArea);
                }
                ReportScoreOffice reportScoreOffice = reportScoreOfficeMap.get(officeId);
                reportScoreOffice.setRecentMonthSaleQty(reportScoreOffice.getRecentMonthSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScoreOffice.setRecentMonthSaleMoney(reportScoreOffice.getRecentMonthSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
                reportScore.setRecentMonthSaleQty(reportScore.getRecentMonthSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScore.setRecentMonthSaleMoney(reportScore.getRecentMonthSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
            }
        }
        //统计本月数据
        List<ReportScoreDataDto> monthReportScoreDataList = reportScoreRepository.findDataByRetailDate(firstDayOfMonth, dateEnd);
        for (ReportScoreDataDto reportScoreDataDto : monthReportScoreDataList) {
            String areaId = getOfficeId(areaMap, reportScoreDataDto.getOfficeId());
            String officeId = getOfficeId(officeMap, reportScoreDataDto.getOfficeId());
            if (StringUtils.isNotBlank(areaId) && StringUtils.isNotBlank(officeId)) {
                ReportScoreArea reportScoreArea = reportScoreAreaMap.get(areaId);
                reportScoreArea.setMonthSaleQty(reportScoreArea.getMonthSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScoreArea.setMonthSaleMoney(reportScoreArea.getMonthSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
                ReportScoreOffice reportScoreOffice = reportScoreOfficeMap.get(officeId);
                reportScoreOffice.setMonthSaleQty(reportScoreOffice.getMonthSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScoreOffice.setMonthSaleMoney(reportScoreOffice.getMonthSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
                reportScore.setMonthSaleQty(reportScore.getMonthSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScore.setMonthSaleMoney(reportScore.getMonthSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
            }
        }
        //统计当日数据
        List<ReportScoreDataDto> reportScoreDataList = reportScoreRepository.findDataByRetailDate(dateStart, dateEnd);
        for (ReportScoreDataDto reportScoreDataDto : reportScoreDataList) {
            String areaId = getOfficeId(areaMap, reportScoreDataDto.getOfficeId());
            String officeId = getOfficeId(officeMap, reportScoreDataDto.getOfficeId());
            if (StringUtils.isNotBlank(areaId) && StringUtils.isNotBlank(officeId)) {
                ReportScoreArea reportScoreArea = reportScoreAreaMap.get(areaId);
                reportScoreArea.setSaleQty(reportScoreArea.getSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScoreArea.setSaleMoney(reportScoreArea.getSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
                ReportScoreOffice reportScoreOffice = reportScoreOfficeMap.get(officeId);
                reportScoreOffice.setSaleQty(reportScoreOffice.getSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScoreOffice.setSaleMoney(reportScoreOffice.getSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
                reportScore.setSaleQty(reportScore.getSaleQty() + reportScoreDataDto.getTotalSaleQty());
                reportScore.setSaleMoney(reportScore.getSaleMoney().add(reportScoreDataDto.getTotalSaleMoney()));
            }
        }
        reportScore.setScore(reportScoreForm.getCompanyScore());
        reportScore.setMonthScore(reportScoreForm.getCompanyMonthScore());

        List<ReportScoreArea> reportScoreAreas = Lists.newArrayList();
        List<ReportScoreOffice> reportScoreOffices = Lists.newArrayList();
        for (ReportScoreArea reportScoreArea : reportScoreAreaMap.values()) {
            BigDecimal point = OfficeUtil.findOne(redisTemplate, reportScoreArea.getOfficeId()).getPoint();
            reportScoreArea.setScore(getScore(reportScoreArea.getSaleMoney(), reportScore.getSaleMoney(), point, reportScore.getScore()));
            reportScoreArea.setMonthScore(getScore(reportScoreArea.getMonthSaleMoney(), reportScore.getMonthSaleMoney(), point, reportScore.getMonthScore()));
            reportScoreAreas.add(reportScoreArea);
        }
        for (ReportScoreOffice reportScoreOffice : reportScoreOfficeMap.values()) {
            BigDecimal point = OfficeUtil.findOne(redisTemplate, reportScoreOffice.getOfficeId()).getPoint();
            reportScoreOffice.setScore(getScore(reportScoreOffice.getSaleMoney(), reportScore.getSaleMoney(), point, reportScore.getScore()));
            reportScoreOffice.setMonthScore(getScore(reportScoreOffice.getMonthSaleMoney(), reportScore.getMonthSaleMoney(), point, reportScore.getMonthScore()));
            reportScoreOffices.add(reportScoreOffice);
        }
        //办事处排名
        Collections.sort(reportScoreAreas, new Comparator<ReportScoreArea>() {
            public int compare(ReportScoreArea r1, ReportScoreArea r2) {
                return r2.getScore().compareTo(r1.getScore());
            }
        });
        for (int i = 0; i < reportScoreAreas.size(); i++) {
            reportScoreAreas.get(i).setDateRank(i + 1);
        }
        Collections.sort(reportScoreAreas, new Comparator<ReportScoreArea>() {
            public int compare(ReportScoreArea r1, ReportScoreArea r2) {
                return r2.getMonthScore().compareTo(r1.getMonthScore());
            }
        });
        for (int i = 0; i < reportScoreAreas.size(); i++) {
            reportScoreAreas.get(i).setMonthRank(i + 1);
        }
        //考核区域排名
        Collections.sort(reportScoreOffices, new Comparator<ReportScoreOffice>() {
            public int compare(ReportScoreOffice r1, ReportScoreOffice r2) {
                return r2.getScore().compareTo(r1.getScore());
            }
        });
        int rank = 1;
        for (int i = 0; i < reportScoreOffices.size(); i++) {
            ReportScoreOffice reportScoreOffice = reportScoreOffices.get(i);
            reportScoreOffice.setDateRank(rank);
            rank = rank + 1;
        }
        Collections.sort(reportScoreOffices, new Comparator<ReportScoreOffice>() {
            public int compare(ReportScoreOffice r1, ReportScoreOffice r2) {
                return r2.getMonthScore().compareTo(r1.getMonthScore());
            }
        });
        rank = 1;
        for (int i = 0; i < reportScoreOffices.size(); i++) {
            ReportScoreOffice reportScoreOffice = reportScoreOffices.get(i);
            reportScoreOffice.setMonthRank(rank);
            rank = rank + 1;
        }
        reportScoreRepository.save(reportScore);
        for (ReportScoreArea reportScoreArea : reportScoreAreas) {
            reportScoreArea.setReportScoreId(reportScore.getId());
        }
        reportScoreAreaRepository.save(reportScoreAreas);
        for (ReportScoreOffice reportScoreOffice : reportScoreOffices) {
            ReportScoreArea reportScoreArea = map.get(reportScoreOffice.getOfficeId());
            reportScoreOffice.setReportScoreAreaId(reportScoreArea.getId());
        }
        reportScoreOfficeRepository.save(reportScoreOffices);
        return reportScore;
    }

    public void delete(ReportScoreForm reportScoreForm) {
        reportScoreRepository.logicDelete(reportScoreForm.getId());
    }

    private String getOfficeId(Map<String, List<String>> map, String unitId) {
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getValue().contains(unitId)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
