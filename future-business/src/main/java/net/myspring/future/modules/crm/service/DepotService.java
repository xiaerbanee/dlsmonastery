package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.domain.ShopDeposit;
import net.myspring.future.modules.crm.mapper.*;
import net.myspring.future.modules.crm.model.DepotInventoryModel;
import net.myspring.future.modules.crm.model.ReceivableReportForDetail;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DepotService {

    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ShopAttributeMapper shopAttributeMapper;
    @Autowired
    private PricesystemMapper pricesystemMapper;
    @Autowired
    private ChainMapper chainMapper;
    @Autowired
    private AdPricesystemMapper adPricesystemMapper;
    @Autowired
    private ExpressCompanyMapper expressCompanyMapper;
    @Autowired
    private ShopDepositMapper shopDepositMapper;

    public Depot findOne(String id) {
        Depot depot = depotMapper.findOne(id);
        return depot;
    }

    public List<Depot> findByOffice(String officeId) {
        List<Depot> depotList = depotMapper.findByOfficeId(officeId);
        return depotList;
    }

    public List<Depot> findByIds(List<String> ids) {
        List<Depot> list = Lists.newArrayList();
        return list;
    }

    public List<Depot> findByTypes(List<Integer> types) {
        List<Depot> depotList = depotMapper.findByTypes(types);
        return depotList;
    }

    public List<Depot> findByOutGroupId(String outGroupId) {
        List<Depot> depotList = depotMapper.findByOutGroupId(outGroupId);
        return depotList;
    }

    public List<Depot> findProxyShop(Map<String,Object> map){
        return null;
    }

    public  List<Depot> findAdShopBsc(String name){
        return null;
    }

    public Page<Depot> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Depot> page = depotMapper.findPage(pageable, map);
        Map<String, ShopDeposit> scbzjMap = Maps.newHashMap();
        Map<String, ShopDeposit> xxbzjMap = Maps.newHashMap();
        return page;
    }

    public List<Depot> setInventory(List<Depot> depots, Map<String, Object> map) {
        List<DepotInventoryModel> depotInventoryModels = depotMapper.findInventoryData(map);
        Map<String, List<DepotInventoryModel>> depotInventoryModelMap = Maps.newHashMap();
        for (DepotInventoryModel depotInventoryModel : depotInventoryModels) {
            if (!depotInventoryModelMap.containsKey(depotInventoryModel.getShopId())) {
                depotInventoryModelMap.put(depotInventoryModel.getShopId(), Lists.newArrayList());
            }
            depotInventoryModelMap.get(depotInventoryModel.getShopId()).add(depotInventoryModel);
        }
        for (Depot depot : depots) {
            DepotInventoryModel depotInventoryModel = new DepotInventoryModel();
            List<DepotInventoryModel> depotInventoryModelList = depotInventoryModelMap.get(depot.getId());
            if (CollectionUtil.isNotEmpty(depotInventoryModelList)) {
                for (DepotInventoryModel depotInventoryModelData : depotInventoryModelList) {
                    switch (depotInventoryModelData.getType()) {
                        case "核销库存":
                            depotInventoryModel.addSaleStockQty(depotInventoryModelData.getQty());
                            break;
                        case "电子保卡库存":
                            depotInventoryModel.addBaoKaStockQty(depotInventoryModelData.getQty());
                            break;
                        case "核销销量":
                            depotInventoryModel.addSaleSalesQty(depotInventoryModelData.getQty());
                            break;
                        case "电子保卡销量":
                            depotInventoryModel.addBaoKaSalesQty(depotInventoryModelData.getQty());
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return depots;
    }

    public Depot setInventoryDetail(Depot depot, Map<String, Object> map) {
        List<DepotInventoryModel> depotInventoryModels = depotMapper.findInventoryData(map);
        Map<String, List<DepotInventoryModel>> depotInventoryModelMap = Maps.newHashMap();
        for (DepotInventoryModel depotInventoryModelData : depotInventoryModels) {
            if (!depotInventoryModelMap.containsKey(depotInventoryModelData.getProductTypeName())) {
                depotInventoryModelMap.put(depotInventoryModelData.getProductTypeName(), Lists.newArrayList());
            }
            depotInventoryModelMap.get(depotInventoryModelData.getProductTypeName()).add(depotInventoryModelData);
        }
        List<DepotInventoryModel> depotInventoryModelList = Lists.newArrayList();
        for (Map.Entry<String, List<DepotInventoryModel>> entry : depotInventoryModelMap.entrySet()) {
            DepotInventoryModel depotInventoryModel = new DepotInventoryModel();
            depotInventoryModel.setProductTypeName(entry.getKey());
            for (DepotInventoryModel depotInventoryModelData : entry.getValue()) {
                switch (depotInventoryModelData.getType()) {
                    case "核销库存":
                        depotInventoryModel.addSaleStockQty(depotInventoryModelData.getQty());
                        break;
                    case "电子保卡库存":
                        depotInventoryModel.addBaoKaStockQty(depotInventoryModelData.getQty());
                        break;
                    case "核销销量":
                        depotInventoryModel.addSaleSalesQty(depotInventoryModelData.getQty());
                        break;
                    case "电子保卡销量":
                        depotInventoryModel.addBaoKaSalesQty(depotInventoryModelData.getQty());
                        break;
                    default:
                        break;
                }
            }
            depotInventoryModelList.add(depotInventoryModel);
        }
        return depot;
    }

    public List<Depot> findStores() {
        return null;
    }

    public List<Depot> findByFilter(Map<String, Object> map) {
        List<Depot> depotList = depotMapper.findByFilter(map);
        return depotList;
    }

    public Depot save(Depot depot) {
        return null;
    }

    @Transactional
    public void synStore(){
    }

    @Transactional
    public void synShop(){
    }

    public Map<String, Object> getFormProperty() {
        return null;
    }

    public Map<String, Object> getListProperty() {
        return null;
    }

    public List<Depot> findAdDepot(String adShopName, String billType) {
        return null;
    }


    public Page<Depot> findShopAccount(Pageable pageable, Map<String, Object> map, String dateStartStr, String dateEndStr) {
        return null;
    }

    public List<ReceivableReportForDetail> findShopAccountDetail(String customerId, String dateRange) {
        return null;
    }

    public  List<Depot> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map, String dateStart, String dateEnd) {
        return null;
    }

    public SimpleExcelBook accountExport(Workbook workbook, Map<String, Object> map, String dateStart, String dateEnd) {
        String dateRange=dateStart+"~"+dateEnd;
        LocalDate dateEndStr= LocalDateUtils.parse(dateEnd).plusDays(1);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "office.name", "机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "area.name", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "extendMap.qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "extendMap.qmys", "期末应收"));

        List<SimpleExcelColumn> simpleExcelSheetColumnList = Lists.newArrayList();
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "billType", "业务类型"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "billNo", "单据编号"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "date", "记账日期"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "materialName", "名称"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "quantity", "数量"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "price", "单价"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "amount", "金额"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "payableAmount", "预收"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "actualPayAmount", "应收"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "endAmount", "期末"));
        simpleExcelSheetColumnList.add(new SimpleExcelColumn(workbook, "note", "备注"));

        List<Depot> depotList=findSimpleExcelSheets(workbook,map,dateStart,LocalDateUtils.format(dateEndStr));
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("应收报表~所有门店", depotList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"应收报表.xlsx",simpleExcelSheetList);

        SimpleExcelSheet simpleExcelSheetDetail=null;
        for(Depot depot:depotList){
            List<ReceivableReportForDetail> depotDetail=findShopAccountDetail(depot.getOutId(),dateRange);
            simpleExcelSheetDetail = new SimpleExcelSheet(depot.getName(),depotDetail,simpleExcelSheetColumnList);
            simpleExcelBook.getSimpleExcelSheets().add(simpleExcelSheetDetail);
        }
        return simpleExcelBook;
    }
}
