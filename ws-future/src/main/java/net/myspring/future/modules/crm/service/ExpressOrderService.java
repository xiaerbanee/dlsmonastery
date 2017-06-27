package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.GoodsOrderRepository;
import net.myspring.future.modules.crm.web.form.ExpressOrderForm;
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.*;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class ExpressOrderService {


    @Autowired
    private ExpressOrderRepository expressOrderRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CacheUtils cacheUtils;

    public ExpressOrderDto findDto(String id){
        ExpressOrderDto expressOrderDto = expressOrderRepository.findDto(id);
        cacheUtils.initCacheInput(expressOrderDto);
        return expressOrderDto;
    }

    public Page<ExpressOrderDto> findPage(Pageable pageable, ExpressOrderQuery expressOrderQuery) {
        Page<ExpressOrderDto> page = expressOrderRepository.findPage(pageable, expressOrderQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
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

    public SimpleExcelBook exportEMS(ExpressOrderQuery expressOrderQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);
        List<List<SimpleExcelColumn>> excelColumnList=Lists.newArrayList();
        Map<String, CellStyle> cellStyleMap=ExcelUtils.getCellStyleMap(workbook);
        CellStyle headCellStyle = cellStyleMap.get(ExcelCellStyle.HEADER.name());
        CellStyle dataCellStyle = cellStyleMap.get(ExcelCellStyle.DATA.name());

        List<SimpleExcelColumn> headColumnList=Lists.newArrayList();
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"邮件号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"*配货单号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"客户订单号"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"*寄件人姓名"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"*寄件人联系方式"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"寄件人联系方式（2）"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"*寄件人地址"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"寄件人公司"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"寄件省"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"寄件市"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"寄件县"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"寄件人邮编"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"*收件人姓名"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"*收件人联系方式"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"收件人联系方式（2）"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"*收件人地址"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"收件人公司"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"到件省/直辖市"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"到件城市"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"到件县/区"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"收件人邮编"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"物品重量"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"物品长度"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"物品宽度"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"物品高度"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"打单时间"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"备注"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"业务类型"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"代收货款"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"收件人付费"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"应收货款/邮资"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"应收货款/邮资（大写）"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"内件性质"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"内件数"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"内件信息"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"留白一"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"留白二"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"付费类型"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"所负责任"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"保价金额"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"保险金额"));
        headColumnList.add(new SimpleExcelColumn(headCellStyle,"其他费"));
        excelColumnList.add(headColumnList);

        List<ExpressOrderDto> expressOrderDtoList=findPage(new PageRequest(0,10000), expressOrderQuery).getContent();
        List<String>  extendBusinessIdList= CollectionUtil.extractToList(expressOrderDtoList,"extendBusinessId");
        List<String>  lxMallOrderBusinessIdList=goodsOrderRepository.findLxMallOrderBybusinessIdList(extendBusinessIdList);


        for(ExpressOrderDto expressOrderDto : expressOrderDtoList){
            int printQty = (expressOrderDto.getExpressPrintQty()== null ? 0 : expressOrderDto.getExpressPrintQty());
            for(int i = 0; i< printQty;i++){
                List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"11"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"沈丽萍"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"0791-88513567"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,expressOrderDto.getFormatId()));
                boolean lxMallOrder=false;
                if(StringUtils.isNotBlank(expressOrderDto.getExtendBusinessId())&&lxMallOrderBusinessIdList.contains(expressOrderDto.getExtendBusinessId())){
                    lxMallOrder=true;
                }
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"南昌市洪都北大道636号西格玛商务大厦16楼("+expressOrderDto.getFromDepotName()+(lxMallOrder?"  天翼购订货":"")+")"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"南昌市欧珀电子有限公司"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"江西省"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"南昌市"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,expressOrderDto.getContator()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,expressOrderDto.getMobilePhone()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                if(printQty==1) {
                    simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle, expressOrderDto.getAddress()));   //收件人地址
                } else {
                    simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"(" + (i + 1) + ")" + expressOrderDto.getAddress())); //收件人地址
                }
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"江西省"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,expressOrderDto.getToDepotDistrictCity()));//TODO 這樣獲取名稱是否正確
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,""));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,""));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," 标准快递"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,""));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,""));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," 物品"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,""));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,expressOrderDto.getFormatId() + CharConstant.SPACE_FULL + RequestUtils.getCompanyName()));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,"如在投递过程中遇到问题请拨打客服0791-86775186.86735571"));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle,""));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                simpleExcelColumnList.add(new SimpleExcelColumn(dataCellStyle," "));
                excelColumnList.add(simpleExcelColumnList);
            }
        }

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("邮政快递打印列表",excelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"邮政快递打印列表"+StringUtils.trimToEmpty(expressOrderQuery.getExtendBusinessIdStart())+".xlsx",simpleExcelSheet);
    }

    public SimpleExcelBook export(ExpressOrderQuery expressOrderQuery) {
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

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("快递打印列表", expressOrderDtoList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"快递打印列表"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheet);
    }

    public ExpressOrderDto findByGoodsOrderId(String goodsOrderId) {
        GoodsOrder goodsOrder = goodsOrderRepository.findOne(goodsOrderId);
        if(StringUtils.isNotBlank(goodsOrder.getExpressOrderId())){
            return findDto(goodsOrder.getExpressOrderId());
        }
       return null;
    }
}
