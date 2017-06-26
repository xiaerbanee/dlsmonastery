package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.CnJournalEntityForBankDto;
import net.myspring.cloud.modules.input.dto.CnJournalForBankDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopGoodsDepositStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto;
import net.myspring.future.modules.layout.dto.ShopGoodsDepositSumDto;
import net.myspring.future.modules.layout.repository.ShopGoodsDepositRepository;
import net.myspring.future.modules.layout.web.form.ShopGoodsDepositForm;
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ShopGoodsDepositService {

    @Autowired
    private ShopGoodsDepositRepository shopGoodsDepositRepository;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private CloudClient cloudClient;

    public Page<ShopGoodsDepositDto> findPage(Pageable pageable, ShopGoodsDepositQuery shopGoodsDepositQuery) {

        Page<ShopGoodsDepositDto> page = shopGoodsDepositRepository.findPage(pageable, shopGoodsDepositQuery);

        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void save(ShopGoodsDepositForm shopGoodsDepositForm) {

        if(shopGoodsDepositForm.isCreate()){
            ShopGoodsDeposit shopGoodsDeposit = new ShopGoodsDeposit();
            ReflectionUtil.copyProperties(shopGoodsDepositForm, shopGoodsDeposit);
            shopGoodsDeposit.setStatus(ShopGoodsDepositStatusEnum.省公司审核.name());
            shopGoodsDeposit.setOutBillType(OutBillTypeEnum.手工日记账.name());
            shopGoodsDepositRepository.save(shopGoodsDeposit);
        }else{
            ShopGoodsDeposit shopGoodsDeposit = shopGoodsDepositRepository.findOne(shopGoodsDepositForm.getId());
            shopGoodsDeposit.setBankId(shopGoodsDepositForm.getBankId());
            shopGoodsDeposit.setRemarks(shopGoodsDepositForm.getRemarks());
            shopGoodsDeposit.setAmount(shopGoodsDepositForm.getAmount());
            shopGoodsDepositRepository.save(shopGoodsDeposit);
        }
    }

    public void delete(String id) {
        shopGoodsDepositRepository.logicDelete(id);
    }

    public String export(ShopGoodsDepositQuery shopGoodsDepositQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> shopGoodsDepositColumnList = Lists.newArrayList();
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "formatId", "编号"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "departMent", "部门"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "amount", "收款金额"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "outCode", "外部编号"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "outBillType", "单据类型"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedBy", "修改人"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "修改时间"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "status", "状态"));
        shopGoodsDepositColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        List<ShopGoodsDepositDto> shopGoodsDepositDtoList = findPage(new PageRequest(0,10000), shopGoodsDepositQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("订金详细", shopGoodsDepositDtoList, shopGoodsDepositColumnList));

        List<SimpleExcelColumn> shopGoodsDepositSumColumnList = Lists.newArrayList();
        shopGoodsDepositSumColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        shopGoodsDepositSumColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        shopGoodsDepositSumColumnList.add(new SimpleExcelColumn(workbook, "totalAmount", "剩余订金"));

        List<ShopGoodsDepositSumDto> shopGoodsDepositSumDtoList = findShopGoodsDepositSumDtoList(RequestUtils.getCompanyId());
        simpleExcelSheetList.add(new SimpleExcelSheet("订金汇总", shopGoodsDepositSumDtoList, shopGoodsDepositSumColumnList));

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"订金列表"+ LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
                return null;
    }

    private List<ShopGoodsDepositSumDto> findShopGoodsDepositSumDtoList(String companyId) {
        List<ShopGoodsDepositSumDto> result = shopGoodsDepositRepository.findShopGoodsDepositSumDtoList(companyId);
        cacheUtils.initCacheInput(result);
        return result;
    }

    public ShopGoodsDepositDto findDto(String id) {

        ShopGoodsDepositDto shopGoodsDepositDto = shopGoodsDepositRepository.findDto(id);
        cacheUtils.initCacheInput(shopGoodsDepositDto);
        return shopGoodsDepositDto;
    }

    public void auditPass(String id) {

        ShopGoodsDepositDto  shopGoodsDepositDto = findDto(id);

        if(StringUtils.isBlank(shopGoodsDepositDto.getShopClientOutId())){
            throw new ServiceException(String.format("审核失败，%s 没有绑定财务门店；", shopGoodsDepositDto.getShopName()));
        }
        if(!ShopGoodsDepositStatusEnum.省公司审核.name().equals(shopGoodsDepositDto.getStatus())){
            throw new ServiceException(String.format("订金（门店：%s，金额：%.2f）状态不为 %s ；", shopGoodsDepositDto.getShopName(), shopGoodsDepositDto.getAmount(), ShopGoodsDepositStatusEnum.省公司审核.name())) ;
        }
        if( StringUtils.isNotBlank(shopGoodsDepositDto.getOutCode())){
            throw new ServiceException(String.format("订金（门店：%s，金额：%.2f）已经同步金蝶，outCode为 %s ；", shopGoodsDepositDto.getShopName(), shopGoodsDepositDto.getAmount(), shopGoodsDepositDto.getOutCode()) );
        }

        //TODO 同步金蝶
        ShopGoodsDeposit  shopGoodsDeposit = shopGoodsDepositRepository.findOne(id);
        shopGoodsDeposit.setStatus(ShopGoodsDepositStatusEnum.已通过.name());
        shopGoodsDeposit.setLocked(true);
        shopGoodsDeposit.setBillDate(LocalDateTime.now());

        shopGoodsDepositRepository.save(shopGoodsDeposit);
    }

    private List<KingdeeSynReturnDto> batchSynForCloud(){
        List<CnJournalForBankDto> cnJournalForBankDtoList = Lists.newArrayList();
            CnJournalForBankDto cnJournalForBankDto = new CnJournalForBankDto();
            cnJournalForBankDto.setExtendId("2");
            cnJournalForBankDto.setExtendType(ExtendTypeEnum.导购用机.name());
            List<CnJournalEntityForBankDto> cnJournalEntityForBankDtoList = Lists.newArrayList();

            CnJournalEntityForBankDto entityForBankDto = new CnJournalEntityForBankDto();
            entityForBankDto.setDebitAmount(BigDecimal.ZERO);
            entityForBankDto.setCreditAmount(BigDecimal.TEN.multiply(new BigDecimal(-1)));
            entityForBankDto.setDepartmentNumber("4000");
            entityForBankDto.setBankAccountNumber("1002.00011");
            entityForBankDto.setComment("模拟测试");
            cnJournalEntityForBankDtoList.add(entityForBankDto);
            cnJournalForBankDto.setEntityForBankDtoList(cnJournalEntityForBankDtoList);
            cnJournalForBankDtoList.add(cnJournalForBankDto);
        return cloudClient.synJournalBankForEmployeePhoneDeposit(cnJournalForBankDtoList);
    }

}
