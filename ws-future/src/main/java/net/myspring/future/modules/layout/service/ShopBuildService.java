package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.layout.domain.ShopBuild;
import net.myspring.future.modules.layout.dto.ShopBuildDto;
import net.myspring.future.modules.layout.repository.ShopBuildRepository;
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
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShopBuildService {

    @Autowired
    private ShopBuildRepository shopBuildRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private DepotManager depotManager;


    public Page<ShopBuildDto> findPage(Pageable pageable, ShopBuildQuery shopBuildQuery) {
        shopBuildQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<ShopBuildDto> page = shopBuildRepository.findPage(pageable, shopBuildQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopBuildDto findOne(String id){
        ShopBuildDto shopBuildDto = new ShopBuildDto();
        if(StringUtils.isNotBlank(id)){
            shopBuildDto = shopBuildRepository.findShopBuildDto(id);
            cacheUtils.initCacheInput(shopBuildDto);
            cacheUtils.initCacheInput(shopBuildDto);
        }
        return shopBuildDto;
    }

    public ShopBuildDto detail(ShopBuildDetailOrAuditForm shopBuildDetailOrAuditForm){
        ShopBuildDto shopBuildDto = new ShopBuildDto();
        if(!shopBuildDetailOrAuditForm.isCreate()){
            ShopBuild shopBuild = shopBuildRepository.findOne(shopBuildDetailOrAuditForm.getId());
            shopBuildDto = BeanUtil.map(shopBuild,ShopBuildDto.class);
            cacheUtils.initCacheInput(shopBuildDto);
        }
        return shopBuildDto;
    }

    @Transactional
    public ShopBuild save(ShopBuildForm shopBuildForm) {
        ShopBuild shopBuild;
        if(shopBuildForm.isCreate()){
            shopBuild = BeanUtil.map(shopBuildForm,ShopBuild.class);
            shopBuildRepository.save(shopBuild);
            //Start Process
            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("门店建设",shopBuild.getId(),ShopBuild.class.getSimpleName(),shopBuildForm.getContent()));
            if(activitiStartDto!=null){
                shopBuild.setProcessStatus(activitiStartDto.getProcessStatus());
                shopBuild.setProcessPositionId(activitiStartDto.getPositionId());
                shopBuild.setProcessFlowId(activitiStartDto.getProcessFlowId());
                shopBuild.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
                shopBuild.setProcessTypeId(activitiStartDto.getProcessTypeId());
                shopBuildRepository.save(shopBuild);
            }
        }else{
            shopBuild = shopBuildRepository.findOne(shopBuildForm.getId());
            ReflectionUtil.copyProperties(shopBuildForm,shopBuild);
            shopBuildRepository.save(shopBuild);
        }
        return shopBuild;
    }

    @Transactional
    public String audit(ShopBuildDetailOrAuditForm shopBuildDetailOrAuditForm) {
        ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();
        ShopBuild shopBuild;
        if(!shopBuildDetailOrAuditForm.isCreate()){
            shopBuild = shopBuildRepository.findOne(shopBuildDetailOrAuditForm.getId());
            activitiCompleteForm.setProcessInstanceId(shopBuild.getProcessInstanceId());
            activitiCompleteForm.setProcessTypeId(shopBuild.getProcessTypeId());
            activitiCompleteForm.setPass(shopBuildDetailOrAuditForm.getPass());
            if(shopBuildDetailOrAuditForm.getPassRemarks()!=null){
                activitiCompleteForm.setComment(shopBuildDetailOrAuditForm.getPassRemarks());
            }
            try {
                ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(activitiCompleteForm);
                if(activitiCompleteDto!=null){
                    shopBuild.setLocked(true);
                    shopBuild.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
                    shopBuild.setProcessPositionId(activitiCompleteDto.getPositionId());
                    shopBuild.setProcessStatus(activitiCompleteDto.getProcessStatus());
                    shopBuildRepository.save(shopBuild);
                }
            } catch (Exception e) {
                return e.getMessage();
            }
        }
        return null;

    }

    @Transactional
    public String batchAudit(String[] ids,Boolean pass){
        if(ids ==null){
            return "未选中任何记录";
        }
        List<String> idList = Arrays.asList(ids);
        ShopBuildDetailOrAuditForm shopBuildDetailOrAuditForm = new ShopBuildDetailOrAuditForm();
        shopBuildDetailOrAuditForm.setPass(pass);
        shopBuildDetailOrAuditForm.setPassRemarks("批量操作");
        String message = null;
        for(String id:idList){
            shopBuildDetailOrAuditForm.setId(id);
            String messageDetail = audit(shopBuildDetailOrAuditForm);
            if(messageDetail != null){
                message += StringUtils.join(messageDetail, CharConstant.COMMA);
            }
        }
        return message;
    }

    @Transactional
    public void logicDelete(String id) {
        shopBuildRepository.logicDelete(id);
    }

    public SimpleExcelBook export(ShopBuildQuery shopBuildQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officeName", "地区"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopName", "店名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "areaType", "区域类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "fixtureType", "装修类别"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "oldContents", "原始尺寸"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "newContents", "最新尺寸"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "content", "装修规格说明"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "更新日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "accountNameAndAccountPhone", "项目对接人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "formatId", "编号"));

        List<ShopBuildDto> shopBuildList = findPage(new PageRequest(0,10000),shopBuildQuery).getContent();
        cacheUtils.initCacheInput(shopBuildList);
        cacheUtils.initCacheInput(shopBuildList);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("门店建设", shopBuildList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"门店建设列表"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheet);

    }

}
