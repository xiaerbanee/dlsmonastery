package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.layout.domain.ShopBuild;
import net.myspring.future.modules.layout.dto.ShopBuildDto;
import net.myspring.future.modules.layout.mapper.ShopBuildMapper;
import net.myspring.future.modules.layout.mapper.ShopDepositMapper;
import net.myspring.future.modules.layout.web.form.ShopBuildDetailOrAuditForm;
import net.myspring.future.modules.layout.web.form.ShopBuildForm;
import net.myspring.future.modules.layout.web.query.ShopBuildQuery;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    @Autowired
    private GridFsTemplate tempGridFsTemplate;


    public Page<ShopBuildDto> findPage(Pageable pageable, ShopBuildQuery shopBuildQuery) {
        Page<ShopBuildDto> page = shopBuildMapper.findPage(pageable, shopBuildQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopBuildDto findOne(String id){
        ShopBuildDto shopBuildDto = new ShopBuildDto();
        if(StringUtils.isNotBlank(id)){
            ShopBuild shopBuild = shopBuildMapper.findOne(id);
            shopBuildDto = BeanUtil.map(shopBuild,ShopBuildDto.class);
            cacheUtils.initCacheInput(shopBuildDto);
        }
        return shopBuildDto;
    }

    public ShopBuildDto detail(ShopBuildDetailOrAuditForm shopBuildDetailOrAuditForm){
        ShopBuildDto shopBuildDto = new ShopBuildDto();
        if(!shopBuildDetailOrAuditForm.isCreate()){
            ShopBuild shopBuild = shopBuildMapper.findOne(shopBuildDetailOrAuditForm.getId());
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
            //Start Process
            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("门店建设",shopBuild.getId(),ShopBuild.class.getSimpleName(),shopBuildForm.getContent()));
            if(activitiStartDto!=null){
                shopBuild.setProcessStatus(activitiStartDto.getProcessStatus());
                shopBuild.setProcessPositionId(activitiStartDto.getPositionId());
                shopBuild.setProcessFlowId(activitiStartDto.getProcessFlowId());
                shopBuild.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
                shopBuild.setProcessTypeId(activitiStartDto.getProcessTypeId());
                shopBuildMapper.update(shopBuild);
            }
        }else{
            shopBuild = shopBuildMapper.findOne(shopBuildForm.getId());
            ReflectionUtil.copyProperties(shopBuildForm,shopBuild);
            shopBuildMapper.update(shopBuild);
        }
        return shopBuild;
    }

    public void notify(ShopBuild shopBuild) {
    }

    public void audit(ShopBuildDetailOrAuditForm shopBuildDetailOrAuditForm) {
        ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();
        ShopBuild shopBuild;
        if(!shopBuildDetailOrAuditForm.isCreate()){
            shopBuild = shopBuildMapper.findOne(shopBuildDetailOrAuditForm.getId());
            activitiCompleteForm.setProcessInstanceId(shopBuild.getProcessInstanceId());
            activitiCompleteForm.setProcessTypeId(shopBuild.getProcessTypeId());
            activitiCompleteForm.setPass(shopBuildDetailOrAuditForm.getPass().equalsIgnoreCase("1")?true:false);
            if(shopBuildDetailOrAuditForm.getPassRemarks()!=null){
                activitiCompleteForm.setComment(shopBuildDetailOrAuditForm.getPassRemarks());
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

    public void batchAudit(String[] ids,String pass){
        if(ids ==null){
            return;
        }
        //List<String> idList = Arrays.asList(ids);
        ShopBuildDetailOrAuditForm shopBuildDetailOrAuditForm = new ShopBuildDetailOrAuditForm();
        for(String id:ids){
            shopBuildDetailOrAuditForm.setId(id);
            shopBuildDetailOrAuditForm.setPass(pass);
            shopBuildDetailOrAuditForm.setPassRemarks("批量操作");
            audit(shopBuildDetailOrAuditForm);
        }

    }

    public void logicDeleteOne(String id) {
        shopBuildMapper.logicDeleteOne(id);
    }

    public String findSimpleExcelSheets(Workbook workbook, ShopBuildQuery shopBuildQuery) {

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "fixtureType", "装修类别"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "content", "装修规格说明"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "oldContents", "原始尺寸"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "newContents", "最新尺寸"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "processStatus", "状态"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedByName", "最后修改人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "最后修改时间"));

        List<ShopBuildDto> shopBuildList = shopBuildMapper.findByFilter(shopBuildQuery);
        cacheUtils.initCacheInput(shopBuildList);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("门店建设", shopBuildList, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"门店建设"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }

}
