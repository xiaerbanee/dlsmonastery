package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.ExpressRepository;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.mapper.ExpressOrderMapper;
import net.myspring.future.modules.crm.web.form.ExpressOrderForm;
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@Transactional
public class ExpressOrderService {

    @Autowired
    private ExpressOrderMapper expressOrderMapper;
    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private ExpressRepository expressRepository;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CacheUtils cacheUtils;

    public ExpressOrderDto findDto(String id){
        return expressOrderRepository.findDto(id);
    }

    public Page<ExpressOrderDto> findPage(Pageable pageable, ExpressOrderQuery expressOrderQuery) {
        Page<ExpressOrderDto> page = expressOrderMapper.findPage(pageable, expressOrderQuery);
        page.getContent().stream().filter(each-> (each.getWeight()!=null && each.getTotalQty()!=null&&each.getTotalQty()>0)).forEach(each -> each.setAverageWeight(each.getWeight().divide(new BigDecimal(each.getTotalQty()),2, BigDecimal.ROUND_HALF_UP)));

        cacheUtils.initCacheInput(page.getContent());
        return page;
    }


    public void update(ExpressOrder expressOrder){
        expressOrderRepository.save(expressOrder);
    }

    public void save(ExpressOrder expressOrder){
        expressOrderRepository.save(expressOrder);
    }

    public void save(String extendType,String extendId,String expressCodes,String expressCompanyId) {
        ExpressOrder expressOrder = expressOrderRepository.findByExtendIdAndExtendType(extendId, extendType);
        expressOrder.setExpressCompanyId(expressCompanyId);
        expressOrder.setExpressCodes(expressCodes);

        List<String> expressCodeList = StringUtils.getSplitList(expressCodes, "");
        List<Express> expresses = expressRepository.findByExpressOrderId(expressOrder.getId());
        if(CollectionUtil.isNotEmpty(expresses)) {
            for(int i = expresses.size()-1;i>=0;i--) {
                Express express = expresses.get(i);
                if(!expressCodeList.contains(express.getCode())) {
                    express.setEnabled(false);
                    expressRepository.save(express);
                    expresses.remove(i);
                }
            }
        }
        Map<String,Express> expressMap = CollectionUtil.extractToMap(expresses, "code");
        for(String code:expressCodeList) {
            if(!expressMap.containsKey(code)){
                Express express = new Express();
                express.setExpressOrderId(expressOrder.getId());
                express.setCode(code);
                expressRepository.save(express);
            }
        }
        expressOrderRepository.save(expressOrder);
    }

    public ExpressOrderForm getForm(ExpressOrderForm expressOrderForm) {

        if(expressOrderForm.isCreate()){
            throw new ServiceException("expressOrderCantNew");
        }

        ExpressOrderDto expressOrderDto = expressOrderRepository.findDto(expressOrderForm.getId());
        cacheUtils.initCacheInput(expressOrderDto);
        return BeanUtil.map(expressOrderDto, ExpressOrderForm.class);


    }

    public void save(ExpressOrderForm expressOrderForm) {
        if(expressOrderForm.isCreate()){
            throw new ServiceException("expressOrderCantNew");
        }
        ExpressOrder eo = expressOrderRepository.findOne(expressOrderForm.getId());
        ReflectionUtil.copyProperties(expressOrderForm, eo);
        expressOrderRepository.save(eo);
    }

    public void resetPrintStatus(String id) {
        ExpressOrder eo = expressOrderRepository.findOne(id);

        eo.setExpressPrintDate(null);
        eo.setOutPrintDate(null);
        expressOrderRepository.save(eo);
    }

    public String exportEMS(ExpressOrderQuery expressOrderQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);


        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)->" ",  "邮件号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)->"11","配货单号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)->" ","客户订单号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)->"沈丽萍","寄件人姓名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)->"0791-88513567","寄件人联系方式"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"formatId","寄件人联系方式（2）"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (ExpressOrderDto row, Integer i)->"南昌市洪都北大道636号西格玛商务大厦16楼("+row.getFromDepotName()+")","寄件人地址"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)->"南昌市欧珀电子有限公司", "寄件人公司"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "江西省", "寄件省"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "南昌市", "寄件市"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)->" ", "寄件县"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "寄件人邮编"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"contator","收件人姓名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"mobilePhone","收件人联系方式"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ",  "收件人联系方式（2）"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,(ExpressOrderDto row, Integer i)-> {
            if (row.getExpressPrintQty()!=null && row.getExpressPrintQty() > 1) {
               return  "(" + (i + 1) + ")" + row.getAddress();
            }else {
               return  row.getAddress();
            }
        },"收件人地址"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ","收件人公司"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "江西省","到件省/直辖市"));
        //TODO 獲取depotCItyName
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,(row, i)-> "depotCItyName","到件城市"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "到件县/区"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "收件人邮编"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "物品重量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "物品长度"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "物品宽度"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "物品高度"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "", "打单时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "", "备注"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " 标准快递", "业务类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "代收货款"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ", "收件人付费"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "", "应收货款/邮资"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "", "应收货款/邮资（大写）"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " 物品", "内件性质"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "", "内件数"));
        final String companyName = "companyName";//TODO 獲取companyName
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,(ExpressOrderDto row, Integer i)-> (row.getFormatId() + CharConstant.SPACE_FULL + companyName),"内件信息"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "如在投递过程中遇到问题请拨打客服0791-86775186.86735571","留白一"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> "","留白二"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ","付费类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ","所负责任"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ","保价金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ","保险金额"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, (row, i)-> " ","其他费"));


        List<ExpressOrderDto> expressOrderDtoList=findPage(new PageRequest(0,10000), expressOrderQuery).getContent();
        cacheUtils.initCacheInput(expressOrderDtoList);

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("快递打印列表", expressOrderDtoList, simpleExcelColumnList, (data)->{
            List<ExpressOrderDto> result = new ArrayList<>();
            ExpressOrderDto  expressOrderDto = (ExpressOrderDto) data;
            if(expressOrderDto.getExpressPrintQty()!=null && expressOrderDto.getExpressPrintQty()>=1){
                for(int i=0; i< expressOrderDto.getExpressPrintQty(); i++){
                    result.add(expressOrderDto);
                }
            }
            return result;
        });
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"快递打印列表"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream=ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());

        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }

    public String export(ExpressOrderQuery expressOrderQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"extendBusinessId","订单号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"extendType","订单类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"expressCompanyName","快递公司"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"fromDepotName","发货方"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toDepotName","收货方"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"shouldGet","应收运费"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"shouldPay","应付运费"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"realPay","实付运费"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"contator","联系人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"mobilePhone","联系电话"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"address","地址"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"expressPrintDate","快递打印时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"outPrintDate","出库单打印时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdDate","创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"averageWeight","单机重量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"remarks","备注"));

        List<ExpressOrderDto> expressOrderDtoList=findPage(new PageRequest(0,10000), expressOrderQuery).getContent();
        cacheUtils.initCacheInput(expressOrderDtoList);

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("快递打印列表", expressOrderDtoList, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"快递打印列表"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream=ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }

    public ExpressOrderDto findByGoodsOrderId(String goodsOrderId) {
        ExpressOrderDto expressOrderDto = expressOrderRepository.findDtoByGoodsOrderId(goodsOrderId);
        if(expressOrderDto != null){
            cacheUtils.initCacheInput(expressOrderDto);
        }

        return expressOrderDto;

    }
}
