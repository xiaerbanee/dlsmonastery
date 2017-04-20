package net.myspring.future.modules.layout.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.layout.domain.ShopAd;
import net.myspring.future.modules.layout.mapper.ShopAdMapper;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShopAdService {

    @Autowired
    private ShopAdMapper shopAdMapper;
    @Autowired
    private DepotMapper depotMapper;
    public Page<ShopAd> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ShopAd> page = shopAdMapper.findPage(pageable, map);
        return page;
    }

    public ShopAd save(ShopAd shopAd) {
        return null;
    }

    public void notify(ShopAd shopAd) {
    }


    public void audit(ShopAd shopAd, boolean pass, String comment) {
    }

    public ShopAd findOne(String id) {
        ShopAd shopAd = shopAdMapper.findOne(id);
        return shopAd;
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
