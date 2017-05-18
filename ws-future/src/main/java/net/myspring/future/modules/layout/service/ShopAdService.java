package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.future.common.enums.TotalPriceTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.basic.mapper.ShopAdTypeMapper;
import net.myspring.future.modules.layout.domain.ShopAd;
import net.myspring.future.modules.layout.dto.ShopAdDto;
import net.myspring.future.modules.layout.mapper.ShopAdMapper;
import net.myspring.future.modules.layout.web.form.ShopAdForm;
import net.myspring.future.modules.layout.web.query.ShopAdQuery;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ShopAdService {

    @Autowired
    private ShopAdMapper shopAdMapper;
    @Autowired
    private ShopAdTypeMapper shopAdTypeMapper;
    @Autowired
    private ActivitiClient activitiClient;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ShopAdDto> findPage(Pageable pageable, ShopAdQuery shopAdQuery) {
        Page<ShopAdDto> page = shopAdMapper.findPage(pageable, shopAdQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopAd save(ShopAdForm shopAdForm) {
        ShopAd shopAd;
        ShopAdType shopAdType = shopAdTypeMapper.findOne(shopAdForm.getShopAdTypeId());
        BigDecimal price=BigDecimal.ZERO;
        if(TotalPriceTypeEnum.按数量.toString().equals(shopAdType.getTotalPriceType())){
            price=shopAdType.getPrice().multiply(new BigDecimal(shopAdForm.getQty()));
        }
        if(TotalPriceTypeEnum.按面积.toString().equals(shopAdType.getTotalPriceType())){
            price=shopAdForm.getLength().multiply(shopAdForm.getWidth()).multiply(shopAdType.getPrice()).multiply(new BigDecimal(shopAdForm.getQty()));
        }
        price = price.setScale(0,BigDecimal.ROUND_HALF_UP);
        shopAdForm.setPrice(price);

        if(shopAdForm.isCreate()){
            shopAd = BeanUtil.map(shopAdForm,ShopAd.class);
            shopAdMapper.save(shopAd);
            //启动流程
            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("广告申请",shopAd.getId(),ShopAd.class.getSimpleName(),shopAdForm.getPrice().toString()));
            if(activitiStartDto!=null){
                shopAd.setProcessPositionId(activitiStartDto.getPositionId());
                shopAd.setProcessStatus(activitiStartDto.getProcessStatus());
                shopAd.setProcessFlowId(activitiStartDto.getProcessFlowId());
                shopAd.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
                shopAd.setProcessTypeId(activitiStartDto.getProcessTypeId());
                shopAdMapper.update(shopAd);
            }
        }else {
            shopAd = shopAdMapper.findOne(shopAdForm.getId());
            ReflectionUtil.copyProperties(shopAdForm,shopAd);
            shopAdMapper.update(shopAd);
        }
        return shopAd;
    }

    public ShopAdQuery getQuery(ShopAdQuery shopAdQuery) {
        shopAdQuery.setShopAdTypes(shopAdTypeMapper.findAllByEnabled());
        return shopAdQuery;
    }


    public void audit(ShopAdForm shopAdForm) {
        ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();
        ShopAd shopAd;
        if(!shopAdForm.isCreate()){
            shopAd = shopAdMapper.findOne(shopAdForm.getId());
            activitiCompleteForm.setProcessInstanceId(shopAd.getProcessInstanceId());
            activitiCompleteForm.setProcessTypeId(shopAd.getProcessTypeId());
            if(shopAdForm.getPassRemarks()!=null){
                activitiCompleteForm.setComment(shopAdForm.getPassRemarks());
            }
            activitiCompleteForm.setPass(shopAdForm.getPass());
            ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(activitiCompleteForm);
            if(activitiCompleteDto!=null){
                shopAd.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
                shopAd.setProcessStatus(activitiCompleteDto.getProcessStatus());
                shopAd.setProcessPositionId(activitiCompleteDto.getPositionId());
                shopAdMapper.update(shopAd);
            }
        }
    }

    public void batchAudit(ShopAdForm shopAdForm){
        List<ShopAd> shopAds = shopAdMapper.findByIds(shopAdForm.getIds());
        for (ShopAd shopAd:shopAds){
            shopAdForm = BeanUtil.map(shopAd,ShopAdForm.class);
            audit(shopAdForm);
        }
    }

    public ShopAd findOne(String id) {
        ShopAd shopAd = shopAdMapper.findOne(id);
        return shopAd;
    }

    public ShopAdForm findForm(ShopAdForm shopAdForm){
        if(!shopAdForm.isCreate()){
            ShopAd shopAd = shopAdMapper.findOne(shopAdForm.getId());
            shopAdForm = BeanUtil.map(shopAd,ShopAdForm.class);
            cacheUtils.initCacheInput(shopAdForm);
        }
        shopAdForm.setShopAdTypeFormList(shopAdTypeMapper.findAllByEnabled());
        return shopAdForm;
    }

    public Boolean getAuditable(ShopAd shopAd) {
        return true;
    }

    public void logicDelete(String id) {
        shopAdMapper.logicDeleteOne(id);
    }

    public String findSimpleExcelSheets(Workbook workbook, ShopAdQuery shopAdQuery) throws IOException{

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopAdTypeName", "广告品种"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "length", "长度"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "width", "宽度"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qty", "数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "specialArea", "是否专区"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "content", "内容说明"));
        List<ShopAdDto> shopAdDtoList = shopAdMapper.findByFilter(shopAdQuery);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("广告申请", shopAdDtoList, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"广告申请"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());
    }
}
