package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.model.AdApplyModel;
import net.myspring.future.modules.layout.domain.AdApply;
import net.myspring.future.modules.layout.domain.AdGoodsOrder;
import net.myspring.future.modules.layout.mapper.AdApplyMapper;
import net.myspring.future.modules.layout.web.query.AdApplyQuery;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdApplyService {

    @Autowired
    private AdApplyMapper adApplyMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ProductMapper productMapper;

    public Page<AdApply> findPage(Pageable pageable, AdApplyQuery adApplyQuery) {
        Page<AdApply> page = adApplyMapper.findPage(pageable, adApplyQuery);
        return page;
    }

    public AdApply findOne(String id){
        AdApply adApply = adApplyMapper.findOne(id);
        return adApply;
    }

    public List<AdApply> findAdApplyList(String billType, String shopId){
        return null;
    }

    public List<AdApply> findAdApplyGoodsList(){
        Map<String,Object> filter = Maps.newHashMap();
        filter.put("adShop",true);
        List<String> adApplyIdList = adApplyMapper.findAllId();
        List<AdApply> adApplys = Lists.newArrayList();

        return adApplys;
    }

    public Map<String,Object> findBillAdApplyMap(String billType){
        return null;
    }

    public List<AdGoodsOrder> bill(AdApplyModel adApplyModel){
       List<AdGoodsOrder> adGoodsOrderList=Lists.newArrayList();
       return adGoodsOrderList;
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<AdApply> adApplyList = adApplyMapper.findByFilter(map);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "id", "编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.name", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "product.code", "物料编码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "product.name", "物料名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "applyQty", "申请数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "confirmQty", "确认数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "leftQty", "待开单数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "created.loginName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "expiryDateRemarks", "截止日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("征订明细", adApplyList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }
}
