package net.myspring.future.modules.crm.web.controller;


import com.google.common.collect.Sets;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.BoolEnum;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.*;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.dto.DepotReportDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.DepotShopService;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeHistoryDto;
import net.myspring.future.modules.crm.dto.ProductImeReportDto;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.form.ProductImeBatchChangeForm;
import net.myspring.future.modules.crm.web.form.ProductImeBatchCreateForm;
import net.myspring.future.modules.crm.web.form.ProductImeChangeForm;
import net.myspring.future.modules.crm.web.form.ProductImeCreateForm;
import net.myspring.future.modules.crm.web.query.ProductImeQuery;
import net.myspring.future.modules.crm.web.query.ReportQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.file.TempFileView;
import net.myspring.util.text.StringUtils;
import org.apache.poi.util.TempFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "crm/productIme")
public class ProductImeController {

    @Autowired
    private ProductImeService productImeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DepotShopService depotShopService;
    @Autowired
    private OfficeClient officeClient;

    @RequestMapping(method = RequestMethod.GET)
    public Page list(Pageable pageable,ProductImeQuery productImeQuery){
        return productImeService.findPage(pageable,productImeQuery);
    }

    @RequestMapping(value="getQuery")
    public ProductImeQuery getQuery(ProductImeQuery productImeQuery) {
        productImeQuery.getExtra().put("inputTypeList",InputTypeEnum.getList());
        return productImeQuery;
    }

    @RequestMapping(value="getProductImeDetail")
    public ProductImeDto getProductImeDetail(String id) {
        return productImeService.getProductImeDetail(id);
    }

    @RequestMapping(value="getProductImeHistoryList")
    public List<ProductImeHistoryDto> getProductImeHistoryList(String id) {
        return productImeService.getProductImeHistoryList(id);
    }

    @RequestMapping(value="findDtoListByImes")
    public List<ProductImeDto> findDtoListByImes(String imeStr) {
        return productImeService.findDtoListByImes(imeStr);
    }

    @RequestMapping(value="export",method = RequestMethod.GET)
    public ModelAndView export(ProductImeQuery productImeQuery) {
        SimpleExcelBook simpleExcelBook = productImeService.export(productImeQuery);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView,"simpleExcelBook",simpleExcelBook);
    }

    @RequestMapping(value = "search")
    public List<ProductImeDto> search(String productIme){
        String imeReverse = StringUtils.reverse(productIme);
        return productImeService.findByImeLike(imeReverse);
    }

    @RequestMapping(value = "productImeReport")
    public Map<String,Object> productImeReport(ReportQuery productImeSaleReportQuery){
        return productImeService.productImeReport(productImeSaleReportQuery);
    }

    @RequestMapping(value = "getReportQuery")
    public ReportQuery getReportQuery(ReportQuery reportQuery){
        reportQuery.getExtra().put("sumTypeList",SumTypeEnum.getList());
        reportQuery.getExtra().put("areaTypeList",AreaTypeEnum.getList());
        reportQuery.getExtra().put("townTypeList",TownTypeEnum.getList());
        reportQuery.getExtra().put("outTypeList",ProductImeStockReportOutTypeEnum.getList());
        reportQuery.getExtra().put("boolMap",BoolEnum.getMap());
        reportQuery.setSumType(ProductImeStockReportSumTypeEnum.区域.name());
        CompanyConfigCacheDto  companyConfigCacheDto = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.PRODUCT_NAME.name());
        if(companyConfigCacheDto != null && "WZOPPO".equals(companyConfigCacheDto.getValue())) {
            reportQuery.setOutType(ProductImeStockReportOutTypeEnum.核销.name());
        }else{
            reportQuery.setOutType(ProductImeStockReportOutTypeEnum.电子保卡.name());
        }
        reportQuery.setScoreType(true);
        return reportQuery;
    }

    @RequestMapping(value = "exportReport",method = RequestMethod.GET)
    public ModelAndView exportReport(ReportQuery reportQuery) throws IOException{
        reportQuery.setDepotIdList(depotService.filterDepotIds());
        if("按串码".equals(reportQuery.getExportType())){
            reportQuery.setIsDetail(true);
        }
        reportQuery.setOfficeIds(officeClient.getChildOfficeIds(reportQuery.getOfficeId()));
        reportQuery.setOfficeId(null);
        List<DepotReportDto> depotReportList=depotShopService.getProductImeReportList(reportQuery);
        SimpleExcelBook simpleExcelBook = productImeService.getFolderFileId(depotReportList,reportQuery);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView,"simpleExcelBook",simpleExcelBook);
    }

    @RequestMapping(value = "getBatchCreateForm")
    public ProductImeBatchCreateForm getBatchCreateForm(ProductImeBatchCreateForm productImeBatchCreateForm){

        productImeBatchCreateForm.getExtra().put("productNameList" , productService.findNameList());
        productImeBatchCreateForm.getExtra().put("storeNameList" , CollectionUtil.extractToList(depotService.findStoreList(new DepotQuery()),"name"));
        return productImeBatchCreateForm;

    }

    @RequestMapping(value = "batchCreate")
    public RestResponse batchCreate(ProductImeBatchCreateForm productImeBatchCreateForm){
        if(CollectionUtil.isEmpty(productImeBatchCreateForm.getProductImeCreateFormList())){
            throw new ServiceException("请录入需要添加的串码信息");
        }

        for(ProductImeCreateForm productImeCreateForm : productImeBatchCreateForm.getProductImeCreateFormList()){
            if(StringUtils.isBlank(productImeCreateForm.getIme())){
                throw new ServiceException("串码不可以为空");
            }
            if(StringUtils.isBlank(productImeCreateForm.getProductName())){
                throw new ServiceException("货品不可以为空");
            }
            if(StringUtils.isBlank(productImeCreateForm.getStoreName())){
                throw new ServiceException("仓库不可以为空");
            }
            if(StringUtils.isBlank(productImeCreateForm.getCreatedTimeStr())){
                throw new ServiceException("工厂录入时间不可以为空");
            }
        }

        productImeService.batchCreate(productImeBatchCreateForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getBatchChangeForm")
    public ProductImeBatchChangeForm getBatchChangeForm(ProductImeBatchChangeForm  productImeBatchChangeForm){

        productImeBatchChangeForm.getExtra().put("productNameList" , productService.findNameList());
        return productImeBatchChangeForm;

    }

    @RequestMapping(value = "batchChange")
    public RestResponse batchChange(ProductImeBatchChangeForm  productImeBatchChangeForm){
        if(CollectionUtil.isEmpty(productImeBatchChangeForm.getProductImeChangeFormList())){
            throw new ServiceException("请录入需要调整的串码");
        }
        for(ProductImeChangeForm productImeChangeForm : productImeBatchChangeForm.getProductImeChangeFormList()){
            if(StringUtils.isBlank(productImeChangeForm.getIme())){
                throw new ServiceException("串码不可以为空");
            }
            if(StringUtils.isBlank(productImeChangeForm.getProductName())){
                throw new ServiceException("调整后型号不可以为空");
            }
        }

        productImeService.batchChange(productImeBatchChangeForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "batchQuery")
    public  Map<String, Object> batchQuery(String allImeStr){
        Map<String, Object> result = new HashMap<>();

        List<String> allImeList = StringUtils.getSplitList(allImeStr, CharConstant.ENTER);
        if(allImeList.size() == 0){
            result.put("errMsg", "请输入串码、串码2、Meid码、箱号");
            result.put("productImeList",  new ArrayList<>());
            return result;
        }
        List<ProductImeDto> productImeDtoList = productImeService.batchQuery(allImeList);

        Set<String> searchedKeys= Sets.newHashSet();
        for(ProductImeDto productImeDto:productImeDtoList){
            searchedKeys.add(productImeDto.getIme());
            if(StringUtils.isNotBlank(productImeDto.getIme2())){
                searchedKeys.add(productImeDto.getIme2());
            }
            if(StringUtils.isNotBlank(productImeDto.getMeid())){
                searchedKeys.add(productImeDto.getMeid());
            }
            if(StringUtils.isNotBlank(productImeDto.getBoxIme())){
                searchedKeys.add(productImeDto.getBoxIme());
            }
        }

        StringBuilder sb  = new StringBuilder();
        for(String each : allImeList){
            if(!searchedKeys.contains(each)){
                sb.append(String.format("串码：%s 不存在或者不在管辖区", each));
            }
        }

        result.put("errMsg", sb.toString());
        result.put("productImeList", productImeDtoList);
        return result;
    }

    @RequestMapping(value="batchExport")
    public String batchExport(String allImeStr) throws IOException {
        List<String> allImeList = StringUtils.getSplitList(allImeStr, CharConstant.ENTER);
        SimpleExcelBook simpleExcelBook = productImeService.export(productImeService.batchQuery(allImeList));
        File tempFile = TempFile.createTempFile("串码批量查询",".xlsx");
        try(FileOutputStream os = new FileOutputStream(tempFile)){
            simpleExcelBook.getWorkbook().write(os);
            os.flush();
        }catch(Exception e){
            throw new ServiceException(e);
        }
        return tempFile.getName();
    }

    @RequestMapping(value="download")
    public ModelAndView download(String tempFileName) throws IOException {

        return new ModelAndView(new TempFileView(), "tempFileName", tempFileName);
    }

}
