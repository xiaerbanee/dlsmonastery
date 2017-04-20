package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.DepotChange;
import net.myspring.future.modules.crm.mapper.DepotChangeMapper;
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
public class DepotChangeService {

    @Autowired
    private DepotChangeMapper depotChangeMapper;

    public DepotChange findOne(String id) {
        DepotChange depotChange = depotChangeMapper.findOne(id);
        return depotChange;
    }

    public Page<DepotChange> findPage(Pageable pageable, Map<String, Object> map) {
        Page<DepotChange> page = depotChangeMapper.findPage(pageable, map);
        return page;
    }

    public DepotChange save(DepotChange depotChange) {
        return depotChange;
    }

    public void logicDeleteOne(String id) {
        depotChangeMapper.logicDeleteOne(id);
    }

    @Transactional
    public void audit(DepotChange depotChange, boolean pass, String comment) {
    }


    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depot.name", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "type", "调整项"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "newValue", "最新数据"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModified.loginName", "修改人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "修改时间"));

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<DepotChange> depotChangeList = depotChangeMapper.findByFilter(map);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("仓库调整", depotChangeList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }

}
