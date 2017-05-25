package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.repository.DepotChangeRepository;
import net.myspring.future.modules.crm.domain.DepotChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepotChangeService {

    @Autowired
    private DepotChangeRepository depotChangeRepository;


    public void logicDeleteOne(String id) {
        depotChangeRepository.logicDeleteOne(id);
    }


    public void audit(DepotChange depotChange, boolean pass, String comment) {
    }


//    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
//        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depot.name", "门店"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "type", "调整项"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "newValue", "最新数据"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModified.loginName", "修改人"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "修改时间"));
//
//        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
//        List<DepotChange> depotChangeList = depotChangeMapper.findByFilter(map);
//        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("仓库调整", depotChangeList, simpleExcelColumnList);
//        simpleExcelSheetList.add(simpleExcelSheet);
//        return simpleExcelSheetList;
//    }

}
