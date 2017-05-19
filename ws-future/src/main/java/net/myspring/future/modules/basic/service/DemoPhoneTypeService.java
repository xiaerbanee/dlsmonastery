package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeOfficeDto;
import net.myspring.future.modules.basic.mapper.DemoPhoneTypeMapper;
import net.myspring.future.modules.basic.mapper.DemoPhoneTypeOfficeMapper;
import net.myspring.future.modules.basic.mapper.ProductTypeMapper;
import net.myspring.future.modules.basic.web.query.DemoPhoneTypeQuery;
import net.myspring.future.modules.basic.web.form.DemoPhoneTypeDetailForm;
import net.myspring.future.modules.basic.web.form.DemoPhoneTypeForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class DemoPhoneTypeService {

    @Autowired
    private DemoPhoneTypeMapper demoPhoneTypeMapper;
    @Autowired
    private DemoPhoneTypeOfficeMapper demoPhoneTypeOfficeMapper;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private ProductTypeMapper productTypeMapper;
    @Autowired
    private CacheUtils cacheUtils;

    private static BigDecimal MIN_AREA_POINT = new BigDecimal("0.01");

    public DemoPhoneType findOne(String id) {
        DemoPhoneType demoPhoneType = demoPhoneTypeMapper.findOne(id);
        return demoPhoneType;
    }

    public DemoPhoneTypeForm getForm(DemoPhoneTypeForm demoPhoneTypeForm){
        List<OfficeDto> areaList = officeClient.findByOfficeRuleName("办事处");
        List<DemoPhoneTypeOffice> demoPhoneTypeOfficeList = demoPhoneTypeOfficeMapper.findByDemoPhoneTypeId(demoPhoneTypeForm.getId());
        List<DemoPhoneTypeOfficeDto> demoPhoneTypeOfficeDtos= BeanUtil.map(demoPhoneTypeOfficeList,DemoPhoneTypeOfficeDto.class);
        Map<String, DemoPhoneTypeOfficeDto> DemoPhoneTypeOfficeDtoMap = CollectionUtil.extractToMap(demoPhoneTypeOfficeDtos,"officeId");
        for (OfficeDto area : areaList) {
            if (area.getTaskPoint()!=null&&MIN_AREA_POINT.compareTo(area.getTaskPoint()) < 0) {
                DemoPhoneTypeOfficeDto demoPhoneTypeOfficeDto = DemoPhoneTypeOfficeDtoMap.get(area.getId());
                if (demoPhoneTypeOfficeDto == null) {
                    demoPhoneTypeOfficeDto = new DemoPhoneTypeOfficeDto();
                    demoPhoneTypeOfficeDto.setOfficeId(area.getId());
                    demoPhoneTypeOfficeDto.setQty(0);
                    demoPhoneTypeOfficeDto.setDemoPhoneTypeId(demoPhoneTypeForm.getId());
                    demoPhoneTypeOfficeDtos.add(demoPhoneTypeOfficeDto);
                }
            }
            cacheUtils.initCacheInput(demoPhoneTypeOfficeDtos);
        }
        DemoPhoneType demoPhoneType = demoPhoneTypeMapper.findOne(demoPhoneTypeForm.getId());
        demoPhoneTypeForm = BeanUtil.map(demoPhoneType,DemoPhoneTypeForm.class);
        demoPhoneTypeForm.setDemoPhoneTypeOfficeList(demoPhoneTypeOfficeDtos);
        demoPhoneTypeForm.setProductTypeList(productTypeMapper.findByDemoPhoneTypeId(demoPhoneTypeForm.getId()));
        return demoPhoneTypeForm;
    }

    public DemoPhoneTypeDetailForm findDetailForm(DemoPhoneTypeDetailForm demoPhoneTypeDetailForm){
        if(!demoPhoneTypeDetailForm.isCreate()){
            DemoPhoneType demoPhoneType=demoPhoneTypeMapper.findOne(demoPhoneTypeDetailForm.getId());
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
            demoPhoneTypeMapper.save(demoPhoneType);
        } else {
            productTypeMapper.updateDemoPhoneTypeToNull(demoPhoneTypeForm.getId());
            demoPhoneType= demoPhoneTypeMapper.findOne(demoPhoneTypeForm.getId());
            ReflectionUtil.copyProperties(demoPhoneTypeForm,demoPhoneType);
            demoPhoneTypeMapper.update(demoPhoneType);
        }
        if (CollectionUtil.isNotEmpty(demoPhoneTypeForm.getProductTypeIdList())) {
            productTypeMapper.updateDemoPhoneType(demoPhoneType.getId(), demoPhoneTypeForm.getProductTypeIdList());
        }
        List<DemoPhoneTypeOfficeDto> demoPhoneTypeOfficeDtoList = Lists.newArrayList();
        for (DemoPhoneTypeOfficeDto demoPhoneTypeOfficeDto : demoPhoneTypeForm.getDemoPhoneTypeOfficeList()) {
            if (StringUtils.isBlank(demoPhoneTypeOfficeDto.getId())) {
                demoPhoneTypeOfficeDto.setDemoPhoneTypeId(demoPhoneType.getId());
                demoPhoneTypeOfficeDtoList.add(demoPhoneTypeOfficeDto);
            } else {
                DemoPhoneTypeOffice demoPhoneTypeOffice = BeanUtil.map(demoPhoneTypeOfficeDto,DemoPhoneTypeOffice.class);
                demoPhoneTypeOfficeMapper.update(demoPhoneTypeOffice);
            }
        }
        if (CollectionUtil.isNotEmpty(demoPhoneTypeOfficeDtoList)) {
            List<DemoPhoneTypeOffice> demoPhoneTypeOfficeList = BeanUtil.map(demoPhoneTypeOfficeDtoList,DemoPhoneTypeOffice.class);
            demoPhoneTypeOfficeMapper.batchSave(demoPhoneTypeOfficeList);
        }
        return demoPhoneType;
    }
}
