package net.myspring.report.modules.future.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.report.common.utils.CacheUtils;
import net.myspring.report.common.utils.RequestUtils;
import net.myspring.report.modules.future.dto.DepotDto;
import net.myspring.report.modules.future.dto.DepotReportDetailDto;
import net.myspring.report.modules.future.dto.DepotReportDto;
import net.myspring.report.modules.future.enums.OutTypeEnum;
import net.myspring.report.modules.future.repository.DepotRepository;
import net.myspring.report.modules.future.repository.DepotShopRepository;
import net.myspring.report.modules.future.web.query.DepotQuery;
import net.myspring.report.modules.future.web.query.ReportQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepotShopService {

    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Map<String,Object> setReportData(ReportQuery reportQuery) {
        String accountId= RequestUtils.getAccountId();
        Map<String,Object> map= Maps.newHashMap();
        reportQuery.setDepotIdList(depotRepository.findByAccountId(accountId).stream().map(net.myspring.report.modules.future.dto.DepotDto::getId).collect(Collectors.toList()));
        DepotQuery depotQuery = BeanUtil.map(reportQuery, DepotQuery.class);
        List<DepotDto> depotList = depotRepository.findFilter(depotQuery);
        List<DepotReportDto> depotReportList = getProductImeReportList(reportQuery);
        Map<String,DepotReportDto> depotReportMap= CollectionUtil.extractToMap(depotReportList,"depotId");
        for(DepotDto depot:depotList){
            if(!depotReportMap.containsKey(depot.getId())){
                DepotReportDto depotReport = new DepotReportDto();
                depotReport.setDepotId(depot.getId());
                depotReport.setQty(0);
                depotReport.setDepotName(depot.getName());
                depotReportList.add(depotReport);
            }
        }
        map.put("list",depotReportList);
        map.put("sum",setPercentage(depotReportList));
        return map;
    }

    public DepotReportDetailDto getReportDataDetail(ReportQuery reportQuery) {
        DepotReportDetailDto depotReportDetail = new DepotReportDetailDto();
        List<DepotReportDto> depotReportList = getProductImeReportList(reportQuery);
        depotReportDetail.setSum(depotReportList.size());
        depotReportDetail.setDepotReportList(depotReportList);
        Map<String, Integer> map = Maps.newHashMap();
        for (DepotReportDto depotReportDto : depotReportList) {
            if (!map.containsKey(depotReportDto.getProductName())) {
                map.put(depotReportDto.getProductName(), 1);
            } else {
                map.put(depotReportDto.getProductName(), map.get(depotReportDto.getProductName()) + 1);
            }
        }
        depotReportDetail.setProductQtyMap(map);
        cacheUtils.initCacheInput(depotReportList);
        return depotReportDetail;
    }

    public List<DepotReportDto> getProductImeReportList(ReportQuery reportQuery) {
        List<DepotReportDto> depotReportList = Lists.newArrayList();
        if (OutTypeEnum.电子保卡.name().equals(reportQuery.getOutType())) {
            if ("销售报表".equals(reportQuery.getType())) {
                depotReportList = depotShopRepository.findBaokaSaleReport(reportQuery);
            } else if ("库存报表".equals(reportQuery.getType())) {
                depotReportList = depotShopRepository.findBaokaStoreReport(reportQuery);
            }
        } else {
            if ("销售报表".equals(reportQuery.getType())) {
                depotReportList = depotShopRepository.findSaleReport(reportQuery);
            } else if ("库存报表".equals(reportQuery.getType())) {
                depotReportList = depotShopRepository.findStoreReport(reportQuery);
            }
        }
        return depotReportList;
    }

    private Integer setPercentage(List<DepotReportDto> depotReportList) {
        Integer sum = 0;
        for (DepotReportDto depotReport : depotReportList) {
            sum = sum + depotReport.getQty();
        }
        for (DepotReportDto depotReport : depotReportList) {
            depotReport.setPercent(StringUtils.division(sum, depotReport.getQty()));
        }
        return sum;
    }
}
