package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.TotalPriceTypeEnum;
import net.myspring.future.modules.basic.domain.ShopAdType;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ShopAdTypeMapper;
import net.myspring.future.modules.basic.web.form.ShopAdTypeForm;
import net.myspring.future.modules.layout.domain.ShopAd;
import net.myspring.future.modules.layout.dto.ShopAdDto;
import net.myspring.future.modules.layout.mapper.ShopAdMapper;
import net.myspring.future.modules.layout.web.form.ShopAdForm;
import net.myspring.future.modules.layout.web.query.ShopAdQuery;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShopAdService {

    @Autowired
    private ShopAdMapper shopAdMapper;
    @Autowired
    private ShopAdTypeMapper shopAdTypeMapper;
    public Page<ShopAdDto> findPage(Pageable pageable, ShopAdQuery shopAdQuery) {
        Page<ShopAdDto> page = shopAdMapper.findPage(pageable, shopAdQuery);
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
        }else {
            shopAd = shopAdMapper.findOne(shopAdForm.getId());
            ReflectionUtil.copyProperties(shopAdForm,shopAd);
            shopAdMapper.update(shopAd);
        }
        return shopAd;
    }

    public void notify(ShopAd shopAd) {
    }


    public void audit(ShopAd shopAd, boolean pass, String comment) {
    }

    public ShopAd findOne(String id) {
        ShopAd shopAd = shopAdMapper.findOne(id);
        return shopAd;
    }

    public ShopAdForm findForm(ShopAdForm shopAdForm){
        if(!shopAdForm.isCreate()){
            ShopAd shopAd = shopAdMapper.findOne(shopAdForm.getId());
            shopAdForm = BeanUtil.map(shopAd,ShopAdForm.class);
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

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.name", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopAdType.name", "广告品种"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "length", "长度"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "width", "宽度"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "qty", "数量"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "specialArea", "是否专区"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "content", "内容说明"));

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<ShopAd> shopAdList = shopAdMapper.findByFilter(map);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("广告申请", shopAdList, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }
}
