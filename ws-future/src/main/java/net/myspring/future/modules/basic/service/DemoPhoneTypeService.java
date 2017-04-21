package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.DepotTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto;
import net.myspring.future.modules.basic.dto.OfficeBasicDto;
import net.myspring.future.modules.basic.manager.DemoPhoneTypeManager;
import net.myspring.future.modules.basic.mapper.DemoPhoneTypeMapper;
import net.myspring.future.modules.basic.mapper.DemoPhoneTypeOfficeMapper;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.basic.web.Query.DemoPhoneTypeQuery;
import net.myspring.future.modules.basic.web.form.DemoPhoneTypeDetailForm;
import net.myspring.future.modules.basic.web.form.DemoPhoneTypeForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.future.common.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DemoPhoneTypeService {

    @Autowired
    private DemoPhoneTypeMapper demoPhoneTypeMapper;
    @Autowired
    private DemoPhoneTypeManager demoPhoneTypeManager;
    @Autowired
    private DemoPhoneTypeOfficeMapper demoPhoneTypeOfficeMapper;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public DemoPhoneType findOne(String id) {
        DemoPhoneType demoPhoneType = demoPhoneTypeManager.findOne(id);
        return demoPhoneType;
    }

    public DemoPhoneTypeForm findForm(DemoPhoneTypeForm demoPhoneTypeForm){
        List<OfficeBasicDto> areaList = officeClient.findByType(Const.OFFICE_TYPE_AREA);
        List<DemoPhoneTypeOffice> demoPhoneTypeOfficeList = demoPhoneTypeOfficeMapper.findByDemoPhoneTypeId(demoPhoneTypeForm.getId());
        CollectionUtil.joinChild(demoPhoneTypeOfficeList, areaList);
        Map<String, DemoPhoneTypeOffice> demoPhoneTypeOfficeMap = CollectionUtil.extractToMap(demoPhoneTypeOfficeList, "officeId");
        for (OfficeBasicDto area : areaList) {
            if (area.getTaskPoint()!=null&&Const.MIN_AREA_POINT.compareTo(area.getTaskPoint()) < 0) {
                DemoPhoneTypeOffice demoPhoneTypeOffice = demoPhoneTypeOfficeMap.get(area.getId());
                if (demoPhoneTypeOffice == null) {
                    demoPhoneTypeOffice = new DemoPhoneTypeOffice();
                    demoPhoneTypeOffice.setOfficeId(area.getId());
                    demoPhoneTypeOffice.setQty(0);
                    demoPhoneTypeOfficeList.add(demoPhoneTypeOffice);
                }
            }
        }
        demoPhoneTypeForm.setDemoPhoneTypeOfficeList(demoPhoneTypeOfficeList);
        return demoPhoneTypeForm;
    }

    public DemoPhoneTypeDetailForm findDetailForm(DemoPhoneTypeDetailForm demoPhoneTypeDetailForm){
        if(!demoPhoneTypeDetailForm.isCreate()){
            DemoPhoneType demoPhoneType=demoPhoneTypeManager.findOne(demoPhoneTypeDetailForm.getId());
            demoPhoneTypeDetailForm=BeanUtil.map(demoPhoneType,DemoPhoneTypeDetailForm.class);
            cacheUtils.initCacheInput(demoPhoneTypeDetailForm);
        }
        return demoPhoneTypeDetailForm;
    }

    public List<DemoPhoneType> findAllByApplyEndDate(LocalDate applyEndDate) {
        List<DemoPhoneType> demoPhoneTypeList = demoPhoneTypeMapper.findAllByApplyEndDate(applyEndDate);
        return demoPhoneTypeList;
    }

    public Page<DemoPhoneTypeDto> findPage(Pageable pageable, DemoPhoneTypeQuery demoPhoneTypeQuery) {
        Page<DemoPhoneTypeDto> page = demoPhoneTypeMapper.findPage(pageable, demoPhoneTypeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(DemoPhoneTypeForm demoPhoneTypeForm) {
        demoPhoneTypeMapper.logicDeleteOne(demoPhoneTypeForm.getId());
    }

    public DemoPhoneType save(DemoPhoneTypeForm demoPhoneTypeForm) {
        DemoPhoneType demoPhoneType;
        if (demoPhoneTypeForm.isCreate()) {
            demoPhoneType= BeanUtil.map(demoPhoneTypeForm, DemoPhoneType.class);
            demoPhoneType=demoPhoneTypeManager.save(demoPhoneType);
        } else {
            productTypeMapper.updateDemoPhoneTypeToNull(demoPhoneTypeForm.getId());
            demoPhoneType=demoPhoneTypeManager.updateForm(demoPhoneTypeForm);
        }
        if (CollectionUtil.isNotEmpty(demoPhoneTypeForm.getProductTypeIdList())) {
            productTypeMapper.updateDemoPhoneType(demoPhoneType.getId(), demoPhoneTypeForm.getProductTypeIdList());
        }
        List<DemoPhoneTypeOffice> demoPhoneTypeOffices = Lists.newArrayList();
        for (DemoPhoneTypeOffice demoPhoneTypeOffice : demoPhoneTypeForm.getDemoPhoneTypeOfficeList()) {
            if (StringUtils.isBlank(demoPhoneTypeOffice.getId())) {
                demoPhoneTypeOffice.setDemoPhoneTypeId(demoPhoneType.getId());
                demoPhoneTypeOffices.add(demoPhoneTypeOffice);
            } else {
                demoPhoneTypeOfficeMapper.update(demoPhoneTypeOffice);
            }
        }
        if (CollectionUtil.isNotEmpty(demoPhoneTypeOffices)) {
            demoPhoneTypeOfficeMapper.batchSave(demoPhoneTypeOffices);
        }
        return demoPhoneType;
    }
}
