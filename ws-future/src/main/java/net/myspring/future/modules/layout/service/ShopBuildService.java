package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.layout.domain.ShopBuild;
import net.myspring.future.modules.layout.dto.ShopBuildDto;
import net.myspring.future.modules.layout.mapper.ShopBuildMapper;
import net.myspring.future.modules.layout.mapper.ShopDepositMapper;
import net.myspring.future.modules.layout.web.form.ShopBuildForm;
import net.myspring.future.modules.layout.web.query.ShopBuildQuery;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
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
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ActivitiClient activitiClient;


    public Page<ShopBuildDto> findPage(Pageable pageable, ShopBuildQuery shopBuildQuery) {
        Page<ShopBuildDto> page = shopBuildMapper.findPage(pageable, shopBuildQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopBuildForm findForm(ShopBuildForm shopBuildForm){
        if(!shopBuildForm.isCreate()){
            ShopBuild shopBuild=shopBuildMapper.findOne(shopBuildForm.getId());
            shopBuildForm = BeanUtil.map(shopBuild,ShopBuildForm.class);
            cacheUtils.initCacheInput(shopBuildForm);
        }

        return shopBuildForm;
    }

    public ShopBuildDto detail(ShopBuildForm shopBuildForm){
        ShopBuildDto shopBuildDto = new ShopBuildDto();
        if(!shopBuildForm.isCreate()){
            ShopBuild shopBuild = shopBuildMapper.findOne(shopBuildForm.getId());
            shopBuildDto = BeanUtil.map(shopBuild,ShopBuildDto.class);
            cacheUtils.initCacheInput(shopBuildDto);
        }
        return shopBuildDto;
    }

    public ShopBuild save(ShopBuildForm shopBuildForm) {
        ShopBuild shopBuild;
        if(shopBuildForm.isCreate()){
            shopBuild = BeanUtil.map(shopBuildForm,ShopBuild.class);
            shopBuildMapper.save(shopBuild);
        }else{
            shopBuild = shopBuildMapper.findOne(shopBuildForm.getId());
            ReflectionUtil.copyProperties(shopBuildForm,shopBuild);
            shopBuildMapper.update(shopBuild);
        }
        return shopBuild;
    }

    public void notify(ShopBuild shopBuild) {
    }

    public void audit(ShopBuildForm shopBuildForm) {
        ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();
        ShopBuild shopBuild;
        if(!shopBuildForm.isCreate()){
            shopBuild = shopBuildMapper.findOne(shopBuildForm.getId());
            activitiCompleteForm.setProcessInstanceId(shopBuild.getProcessInstanceId());
            activitiCompleteForm.setProcessTypeId(shopBuild.getProcessTypeId());
            activitiCompleteForm.setPass(shopBuildForm.getPass());
            if(shopBuildForm.getPassRemarks()!=null){
                activitiCompleteForm.setComment(shopBuildForm.getPassRemarks());
            }
            ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(activitiCompleteForm);
            if(activitiCompleteDto!=null){
                shopBuild.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
                shopBuild.setProcessPositionId(activitiCompleteDto.getPositionId());
                shopBuild.setProcessStatus(activitiCompleteDto.getProcessStatus());
                shopBuildMapper.update(shopBuild);
            }
        }
    }

    public void batchAudit(ShopBuildForm shopBuildForm){
        List<ShopBuild> shopBuilds = shopBuildMapper.findByIds(shopBuildForm.getIds());
        for (ShopBuild shopBuild:shopBuilds){
            shopBuildForm = BeanUtil.map(shopBuild,ShopBuildForm.class);
            audit(shopBuildForm);
        }
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
