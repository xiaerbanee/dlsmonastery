package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.repository.ExpressOrderRepository;
import net.myspring.future.modules.crm.repository.ExpressRepository;
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
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


@Service
@Transactional
public class ExpressOrderService {


    @Autowired
    private ExpressOrderRepository expressOrderRepository;
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
        page.getContent().stream().filter(each-> (each.getWeight()!=null && each.getTotalQty()!=null&&each.getTotalQty()>0)).forEach(each -> each.setAverageWeight(each.getWeight().divide(new BigDecimal(each.getTotalQty()),2, BigDecimal.ROUND_HALF_UP)));

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

    public String exportEMS(ExpressOrderQuery expressOrderQuery) {
        return null;
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

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("快递打印列表", expressOrderDtoList, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"快递打印列表"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream=ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        return null;

    }

    public ExpressOrderDto findByGoodsOrderId(String goodsOrderId) {
        ExpressOrderDto expressOrderDto = expressOrderRepository.findDtoByGoodsOrderId(goodsOrderId);
        if(expressOrderDto != null){
            cacheUtils.initCacheInput(expressOrderDto);
        }

        return expressOrderDto;

    }
}
