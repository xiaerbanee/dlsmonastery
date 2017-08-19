package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.report.dto.CustomerReceiveDto;
import net.myspring.cloud.modules.report.web.query.CustomerReceiveQuery;
import net.myspring.future.common.constant.ServiceConstant;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Client;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.dto.ReceivableDto;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotShopRepository;
import net.myspring.future.modules.basic.web.form.ClientForm;
import net.myspring.future.modules.basic.web.query.ClientQuery;
import net.myspring.future.modules.basic.web.query.ReceivableQuery;
import net.myspring.util.collection.CollectionUtil;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ClientService {

    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private OfficeClient officeClient;

    public Page<ClientDto> findPage(Pageable pageable, ClientQuery clientQuery) {
        Page<ClientDto> page = clientRepository.findPage(pageable, clientQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public Client save(ClientForm clientForm) {
        Client client;
        if(clientForm.isCreate()) {
            client=BeanUtil.map(clientForm,Client.class);
            clientRepository.save(client);
            //保存depot
            Depot depot = new Depot();
            depot.setClientId(client.getId());
            depot.setName(client.getName());
            depot.setNamePinyin(StringUtils.getFirstSpell(client.getName()));
            depotRepository.save(depot);
            //保存depotShop
            DepotShop depotShop = new DepotShop();
            depotShop.setDepotId(depot.getId());
            depotShopRepository.save(depotShop);
            depot.setDepotShopId(depotShop.getId());
            depotRepository.save(depot);
        } else {
            client =clientRepository.findOne(clientForm.getId());
            ReflectionUtil.copyProperties(clientForm,client);
            clientRepository.save(client);
        }
        return client;
    }

    public ClientDto findOne(String id){
        ClientDto result = new ClientDto();
        if(StringUtils.isNotBlank(id)){
            Client client=clientRepository.findOne(id);
            result = BeanUtil.map(client,ClientDto.class);
            cacheUtils.initCacheInput(result);
       }
       return result;
    }

    @Transactional
    public void delete(String id){
        clientRepository.logicDelete(id);
    }

    public String getClientName(String depotId) {
        Client clientDto = clientRepository.findByDepotId(depotId);
        if(clientDto == null) {
            return null;
        }
        return clientDto.getName();
    }

    public List<ClientDto> findByNameContaining(String name){
        List<ClientDto> clientDtoList= Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            List<Client> clientList=clientRepository.findByNameContaining(name);
            clientDtoList=BeanUtil.map(clientList,ClientDto.class);
        }
        return clientDtoList;
    }

    @Transactional
    public void syn(){
        LocalDateTime outDate=clientRepository.findMaxOutDate();
        List<BdCustomer> bdCustomers=cloudClient.findCustomerByMaxModifyDate(outDate.minusHours(1).toString());
        if(CollectionUtil.isNotEmpty(bdCustomers)){
            List<Client> clientList=clientRepository.findByOutIdIn(CollectionUtil.extractToList(bdCustomers,"FCustId"));
            Map<String,Client> outIdClientMap=CollectionUtil.extractToMap(clientList,"outId");
            for(BdCustomer bdCustomer:bdCustomers){
                Client outClient=outIdClientMap.get(bdCustomer.getFCustId());
                if(outClient==null){
                    outClient=new Client();
                }
                outClient.setName(bdCustomer.getFName().trim());
                outClient.setOutId(bdCustomer.getFCustId());
                outClient.setOutGroupId(bdCustomer.getFPrimaryGroup());
                outClient.setOutGroupName(bdCustomer.getFPrimaryGroupName());
                outClient.setOutCode(bdCustomer.getFNumber());
                outClient.setOutDate(bdCustomer.getFModifyDate());
                outClient.setEnabled(true);
                clientRepository.save(outClient);
            }
        }
    }

    public Page<ReceivableDto> findReceivableList(Pageable pageable, ReceivableQuery receivableQuery, boolean queryDetail) {

        Page<ReceivableDto> page = clientRepository.findReceivableList(pageable, receivableQuery);
        cacheUtils.initCacheInput(page.getContent());

        List<OfficeDto> officeDtoList = officeClient.findAll();
        Map<String, OfficeDto> officeMap = CollectionUtil.extractToMap(officeDtoList, "id");
        for(ReceivableDto receivableDto : page.getContent()){
            receivableDto.setDepotOfficeNames(getOfficeNames(receivableDto.getDepotOfficeIds(), officeMap));
            receivableDto.setDepotAreaNames(getOfficeNames(receivableDto.getDepotAreaIds(), officeMap));
        }

        CustomerReceiveQuery customerReceiveQuery = new CustomerReceiveQuery();
        customerReceiveQuery.setQueryDetail(queryDetail);
        if (StringUtils.isNotBlank(receivableQuery.getDutyDateRange())) {
            String[] tempParamValues = receivableQuery.getDutyDateRange().split(" - ");
            customerReceiveQuery.setDateStart(LocalDate.parse(tempParamValues[0]));
            customerReceiveQuery.setDateEnd(LocalDate.parse(tempParamValues[1]));
        }
        customerReceiveQuery.setCustomerIdList(CollectionUtil.extractToList(page.getContent(), "outId"));
        List<CustomerReceiveDto> customerReceiveDtoList = cloudClient.getCustomerReceiveList(customerReceiveQuery);
        Map<String, CustomerReceiveDto> customerReceiveDtoMap = CollectionUtil.extractToMap(customerReceiveDtoList, "customerId");

        for (ReceivableDto receivableDto : page.getContent()) {
            CustomerReceiveDto customerReceiveDto = customerReceiveDtoMap.get(receivableDto.getOutId());
            if (customerReceiveDto != null) {
                receivableDto.setCustomerReceiveDetailDtoList(customerReceiveDto.getCustomerReceiveDetailDtoList());
                receivableDto.setQcys(customerReceiveDto.getBeginShouldGet());
                receivableDto.setQmys(customerReceiveDto.getEndShouldGet());
            } else {
                receivableDto.setCustomerReceiveDetailDtoList(null);
                receivableDto.setQcys(BigDecimal.ZERO);
                receivableDto.setQmys(BigDecimal.ZERO);
            }

        }

        return page;
    }

    private String getOfficeNames(String officeIds, Map<String, OfficeDto> officeMap) {
        if(StringUtils.isBlank(officeIds)){
            return null;
        }
        List<String> officeNameList = new ArrayList<>();
        String[] officeIdList = officeIds.split(",");
        for(String officeId : officeIdList){
            officeNameList.add(officeMap.get(officeId).getName());
        }
        return StringUtils.join(officeNameList, ",");
    }

    public SimpleExcelBook receivableExportDetail(ReceivableQuery receivableQuery) {

        List<SimpleExcelSheet> sheets = new ArrayList<>();
        Workbook workbook = new SXSSFWorkbook(ServiceConstant.EXPORT_MAX_ROW_NUM);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depotOfficeNames", "所属机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "客户名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qmys", "期末应收"));

        List<ReceivableDto> receivableDtoList = findReceivableList(new PageRequest(0,ServiceConstant.EXPORT_MAX_ROW_NUM),  receivableQuery, true).getContent();
        SimpleExcelSheet sheet = new SimpleExcelSheet("应收报表", receivableDtoList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, sheet);
        sheets.add(sheet);

        Set<String> clientOutIds = Sets.newHashSet();
        for (ReceivableDto receivableDto : receivableDtoList) {

            if (StringUtils.isBlank(receivableDto.getOutId()) || clientOutIds.contains(receivableDto.getOutId())) {
                continue;
            }
            clientOutIds.add(receivableDto.getOutId());

            List<SimpleExcelColumn> receivableColumnList = Lists.newArrayList();

            receivableColumnList.add(new SimpleExcelColumn(workbook, "billType", "业务类型"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "billNo", "单据编号"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "billDate", "单据日期"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "materialName", "名称"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "qty", "数量"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "price", "单价"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "totalAmount", "金额"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "realGet", "预收"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "shouldGet", "应收"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "endShouldGet", "期末"));
            receivableColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

            SimpleExcelSheet tmpSheet = new SimpleExcelSheet(receivableDto.getName(), receivableDto.getCustomerReceiveDetailDtoList(), receivableColumnList);
            ExcelUtils.doWrite(workbook, tmpSheet);
            sheets.add(tmpSheet);
        }
        return new SimpleExcelBook(workbook, "客户应收" + LocalDateUtils.format(receivableQuery.getDutyDateStart(), "yyyyMMdd") + "~" + LocalDateUtils.format(receivableQuery.getDutyDateEnd().minusDays(1), "yyyyMMdd") + ".xlsx", sheets);
    }

    public SimpleExcelBook receivableExportAllDepots(ReceivableQuery receivableQuery) {

        Workbook workbook = new SXSSFWorkbook(ServiceConstant.EXPORT_MAX_ROW_NUM);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depotAreaNames", "所属办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depotOfficeNames", "所属机构"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "name", "客户名称"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qcys", "期初应收"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qmys", "期末应收"));

        List<ReceivableDto> receivableDtoList = findReceivableList(new PageRequest(0, ServiceConstant.EXPORT_MAX_ROW_NUM),  receivableQuery, false).getContent();
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("应收列表", receivableDtoList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook, "应收列表" + LocalDateUtils.format(LocalDate.now()) + ".xlsx", simpleExcelSheet);
    }

}
