package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.ShopBuild;
import net.myspring.future.modules.crm.mapper.ShopBuildMapper;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShopBuildService {

    @Autowired
    private ShopBuildMapper shopBuildMapper;

    public Page<ShopBuild> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ShopBuild> page = shopBuildMapper.findPage(pageable, map);
        return page;
    }

    public ShopBuild findOne(String id){
        ShopBuild shopBuild=shopBuildMapper.findOne(id);
        return shopBuild;
    }

    public ShopBuild save(ShopBuild shopBuild) {
        return null;
    }

    public void notify(ShopBuild shopBuild) {
    }

    public void audit(ShopBuild shopBuild, boolean pass, String comment) {
    }

    public void logicDeleteOne(String id) {
        shopBuildMapper.logicDeleteOne(id);
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "fixtureType", "装修类别"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "content", "装修规格说明"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "oldContents", "原始尺寸"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "newContents", "最新尺寸"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "processStatus", "状态"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "created.loginName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "created.loginName", "最后修改人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "最后修改时间"));
        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<ShopBuild> shopBuildList = shopBuildMapper.findByFilter(map);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("门店建设", shopBuildList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }

}
