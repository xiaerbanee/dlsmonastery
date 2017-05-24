package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.gridfs.GridFSFile;
import ma.glasnost.orika.impl.Specifications;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotAccountDto;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.web.query.DepotAccountQuery;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class DepotService {
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private OfficeClient officeClient;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private GridFsTemplate tempGridFsTemplate;


    public List<DepotDto> findShopList(DepotQuery depotQuery) {
        List<Depot> depotList = depotMapper.findByAccountId(RequestUtils.getAccountId());
        depotQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        if(CollectionUtil.isNotEmpty(depotList)) {
            depotQuery.setDepotIdList(CollectionUtil.extractToList(depotList,"id"));
        }
        return depotMapper.findShopList(depotQuery);
    }

    public List<DepotDto> findStoreList(DepotQuery depotQuery) {
        List<Depot> depotList = depotMapper.findByAccountId(RequestUtils.getAccountId());
        depotQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        if(CollectionUtil.isNotEmpty(depotList)) {
            depotQuery.setDepotIdList(CollectionUtil.extractToList(depotList,"id"));
        }
        return depotMapper.findStoreList(depotQuery);
    }



    public List<DepotDto> findStoreList(String shipType) {
        //TODO  需要完成根據shipType選擇倉庫
        return findStoreList(new DepotQuery());

    }



    public List<DepotDto> findByIds(List<String> ids){
        List<Depot> depotList=depotMapper.findByIds(ids);
        List<DepotDto> depotDtoList= BeanUtil.map(depotList,DepotDto.class);
        return depotDtoList;
    }

    public DepotDto findById(String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        List<DepotDto> depotList=findByIds(ids);
        if(depotList!=null && depotList.size() >=1){
            return depotList.get(0);
        }else{
            return null;
        }
    }

    public Page<DepotAccountDto> findDepotAccountList(Pageable pageable, DepotAccountQuery depotAccountQuery) {

        if(depotAccountQuery.getDutyDateRange() == null || depotAccountQuery.getDutyDateStart()==null || LocalDate.now().minusDays(70).isAfter(depotAccountQuery.getDutyDateStart()) ){
            throw new ServiceException("查询条件请选择为70天以内");
        }

        Page<DepotAccountDto> depotAccountDtoList = depotMapper.findDepotAccountList(pageable, depotAccountQuery);
        //TODO 设置应收项
        cacheUtils.initCacheInput(depotAccountDtoList.getContent());

        return depotAccountDtoList;

    }

    public String depotAccountExportDetail(DepotAccountQuery depotAccountQuery) {


        List<SimpleExcelSheet> sheets = new ArrayList<>();
        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officeName", "所属机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "客户名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qmys", "期末应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "scbzj", "市场保证金"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "xxbzj", "形象押金"));
        List<DepotAccountDto> depotAccountList = findDepotAccountList(new PageRequest(0,10000),  depotAccountQuery).getContent();

        sheets.add(new SimpleExcelSheet("应收报表", depotAccountList, simpleExcelColumnList));

        Set<String> sheetNames = Sets.newHashSet();
        for(DepotAccountDto depotAccountDto : depotAccountList){
            //TODO 增加去重判断
//            if (StringUtils.isBlank(depotAccountDto.getOutId()) || sheetNames.contains(depotAccountDto.getOutId())) {
//                continue;
//            }
                sheetNames.add(depotAccountDto.getName());

                List<SimpleExcelColumn> depotAccountColumnList = Lists.newArrayList();

                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "billType", "业务类型"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "billNo", "单据编号"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "date", "记账日期"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "materialName", "名称"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "qty", "数量"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "price", "单价"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "totalAmount", "金额"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "receiveAmount", "预收"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "shouldGet", "应收"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "endShouldGet", "期末"));
                depotAccountColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

                List<String> customerAccounts = new ArrayList<>();// k3cloudService.findCustomerAccount(AccountUtils.getCompany().getOutDbname(),depot.getOutId(), depot.getName(), dateStart, dateEnd);

                sheets.add(new SimpleExcelSheet(depotAccountDto.getName(), customerAccounts, depotAccountColumnList));

        }

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"客户应收" + LocalDateUtils.format(depotAccountQuery.getDutyDateStart(), "yyyyMMdd") + "~" + LocalDateUtils.format(depotAccountQuery.getDutyDateEnd(), "yyyyMMdd")+".xlsx", sheets);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }

    public String depotAccountExportConfirmation(DepotAccountQuery depotAccountQuery) {
        //TODO 写确认数需要增强excel的打印能力

        return null;
    }

    public String depotAccountExportAllDepots(DepotAccountQuery depotAccountQuery) {



        Workbook workbook = new SXSSFWorkbook(10000);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "areaName", "所属办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officeName", "所属机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "客户名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qmys", "期末应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "billDate", "开单日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "inputDate", "到账日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "outCode", "外部编号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "processStatus", "状态"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        PageRequest pageRequest = new PageRequest(0,10000);
        List<BankInDto> bankInDtoList = bankInMapper.findPage(pageRequest, bankInQuery).getContent();
        cacheUtils.initCacheInput(bankInDtoList);

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("销售收款列表", bankInDtoList, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"销售收款列表"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());




// 最多两个月+10天
        if (DateUtils.getDateDiff(dateStart, new Date()) > 70) {
            dateStart = DateUtils.addDays(new Date(), -70);
        }
        if(dateEnd.before(dateStart)) {
            dateEnd= DateUtils.addDays(new Date(), 30);
        }
        Map<String, Object> searchParams = getSearchParams(request, this.getClass().getName());
        Specification<Depot> spec = FilterUtils.getSpecification(searchParams, Depot.class);
        Specification<Depot> filter = FilterUtils.getDepotScope(Depot.class);
        spec = filter==null?spec:Specifications.where(spec).and(filter);
        List<Depot> depots = depotService.findShopAccountData(spec,dateStart,dateEnd);

        ExcelView excelView = new ExcelView();
        List<List<Object>> datas = Lists.newArrayList();
        List<Object> headers = Lists.newArrayList();
        headers.add("所属办事处");
        headers.add("所属机构");
        headers.add("客户名称");
        headers.add("期初应收");
        headers.add("期末应收");
        datas.add(headers);
        for(Depot depot:depots){
            List<Object> row=Lists.newArrayList();
            if (depot.getDepositMap().get("qcys")!=null || depot.getDepositMap().get("qmys")!=null) {
                row.add(depot.getAreaName());
                row.add(depot.getOfficeName());
                row.add(depot.getName());
                row.add(depot.getDepositMap().get("qcys"));
                row.add(depot.getDepositMap().get("qmys"));
                datas.add(row);
            }
        }
        ExcelBook excelBook = ExcelUtils.getExcelBook("应收列表"+DateUtils.formatDate(new Date()) +".xlsx", datas);
        return new ModelAndView(excelView, "excelBook", excelBook);
    }

}
