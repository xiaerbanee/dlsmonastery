package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.dto.DepotChangeDto;
import net.myspring.future.modules.crm.repository.DepotChangeRepository;
import net.myspring.future.modules.crm.domain.DepotChange;
import net.myspring.future.modules.crm.web.query.DepotChangeQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepotChangeService {

    @Autowired
    private DepotChangeRepository depotChangeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<DepotChangeDto> findPage(Pageable pageable, DepotChangeQuery depotChangeQuery){
        Page<DepotChangeDto> page = depotChangeRepository.findPage(pageable,depotChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotChangeDto findOne(String id){
        DepotChangeDto depotChangeDto;
        if(StringUtils.isBlank(id)){
            depotChangeDto = new DepotChangeDto();
        }else{
            depotChangeDto = BeanUtil.map(depotChangeRepository.findOne(id),DepotChangeDto.class);
            cacheUtils.initCacheInput(depotChangeDto);
        }
        return depotChangeDto;
    }


    public void logicDelete(String id) {
        depotChangeRepository.logicDelete(id);
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
//        List<DepotChange> depotChangeList = depotChangeRepository.findByFilter(map);
//        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("仓库调整", depotChangeList, simpleExcelColumnList);
//        simpleExcelSheetList.add(simpleExcelSheet);
//        return simpleExcelSheetList;
//    }

}
