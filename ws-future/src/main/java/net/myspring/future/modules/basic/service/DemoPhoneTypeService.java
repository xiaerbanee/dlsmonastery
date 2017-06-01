package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.DemoPhoneType;
import net.myspring.future.modules.basic.domain.DemoPhoneTypeOffice;
import net.myspring.future.modules.basic.domain.ProductType;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeDto;
import net.myspring.future.modules.basic.dto.DemoPhoneTypeOfficeDto;
import net.myspring.future.modules.basic.repository.DemoPhoneTypeRepository;
import net.myspring.future.modules.basic.repository.DemoPhoneTypeOfficeRepository;
import net.myspring.future.modules.basic.repository.ProductTypeRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DemoPhoneTypeService {

    @Autowired
    private DemoPhoneTypeRepository demoPhoneTypeRepository;
    @Autowired
    private DemoPhoneTypeOfficeRepository demoPhoneTypeOfficeRepository;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    private static BigDecimal MIN_POINT = new BigDecimal("0.01");

    public DemoPhoneTypeDto findOne(String id) {
        DemoPhoneTypeDto demoPhoneTypeDto = new DemoPhoneTypeDto();
        if(StringUtils.isNotBlank(id)){
            demoPhoneTypeDto = BeanUtil.map(demoPhoneTypeRepository.findOne(id),DemoPhoneTypeDto.class);
        }
        return demoPhoneTypeDto;
    }

    public DemoPhoneTypeForm getForm(DemoPhoneTypeForm demoPhoneTypeForm){
        List<OfficeDto> areaList = officeClient.findByOfficeRuleName("办事处");
        List<DemoPhoneTypeOffice> demoPhoneTypeOfficeList = demoPhoneTypeOfficeRepository.findByDemoPhoneTypeId(demoPhoneTypeForm.getId());
        List<DemoPhoneTypeOfficeDto> demoPhoneTypeOfficeDtos= BeanUtil.map(demoPhoneTypeOfficeList,DemoPhoneTypeOfficeDto.class);
        Map<String, DemoPhoneTypeOfficeDto> DemoPhoneTypeOfficeDtoMap = CollectionUtil.extractToMap(demoPhoneTypeOfficeDtos,"officeId");
        for (OfficeDto area : areaList) {
            if (area.getTaskPoint()!=null&&MIN_POINT.compareTo(area.getTaskPoint()) < 0) {
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
        if(!demoPhoneTypeForm.isCreate()){
            DemoPhoneType demoPhoneType = demoPhoneTypeRepository.findOne(demoPhoneTypeForm.getId());
            demoPhoneTypeForm = BeanUtil.map(demoPhoneType,DemoPhoneTypeForm.class);
            demoPhoneTypeForm.setProductTypeList(productTypeRepository.findByDemoPhoneTypeId(demoPhoneTypeForm.getId()));
        }
        demoPhoneTypeForm.setDemoPhoneTypeOfficeList(demoPhoneTypeOfficeDtos);
        return demoPhoneTypeForm;
    }

    public DemoPhoneTypeDetailForm findDetailForm(DemoPhoneTypeDetailForm demoPhoneTypeDetailForm){
        if(!demoPhoneTypeDetailForm.isCreate()){
            DemoPhoneType demoPhoneType=demoPhoneTypeRepository.findOne(demoPhoneTypeDetailForm.getId());
            demoPhoneTypeDetailForm=BeanUtil.map(demoPhoneType,DemoPhoneTypeDetailForm.class);
            cacheUtils.initCacheInput(demoPhoneTypeDetailForm);
        }
        return demoPhoneTypeDetailForm;
    }

    public List<DemoPhoneType> findAllByApplyEndDate(LocalDate applyEndDate) {
        List<DemoPhoneType> demoPhoneTypeList = demoPhoneTypeRepository.findAllByApplyEndDate(applyEndDate);
        return demoPhoneTypeList;
    }

    public Page<DemoPhoneTypeDto> findPage(Pageable pageable, DemoPhoneTypeQuery demoPhoneTypeQuery) {
        Page<DemoPhoneTypeDto> page = demoPhoneTypeRepository.findPage(pageable, demoPhoneTypeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(DemoPhoneTypeForm demoPhoneTypeForm) {
        demoPhoneTypeRepository.logicDelete(demoPhoneTypeForm.getId());
    }

    public DemoPhoneType save(DemoPhoneTypeForm demoPhoneTypeForm) {
        DemoPhoneType demoPhoneType;
        if (demoPhoneTypeForm.isCreate()) {
            demoPhoneType= BeanUtil.map(demoPhoneTypeForm, DemoPhoneType.class);
            demoPhoneTypeRepository.save(demoPhoneType);
        } else {
            demoPhoneType= demoPhoneTypeRepository.findOne(demoPhoneTypeForm.getId());
            ReflectionUtil.copyProperties(demoPhoneTypeForm,demoPhoneType);
            demoPhoneTypeRepository.save(demoPhoneType);
            //edit>>清空原有的demotypeId
            List<ProductType> productTypes = productTypeRepository.findByDemoPhoneTypeId(demoPhoneTypeForm.getId());
            if(CollectionUtil.isNotEmpty(productTypes)){
                for(ProductType productType:productTypes){
                    productType.setDemoPhoneTypeId(null);
                    productTypeRepository.save(productType);
                }
            }
        }
        //添加productTypeId
        if (CollectionUtil.isNotEmpty(demoPhoneTypeForm.getProductTypeIdList())) {
            for(String productTypeId:demoPhoneTypeForm.getProductTypeIdList()){
                ProductType productType = productTypeRepository.findOne(productTypeId);
                productType.setDemoPhoneTypeId(demoPhoneType.getId());
                productTypeRepository.save(productType);
            }
        }

        List<DemoPhoneTypeOfficeDto> demoPhoneTypeOfficeDtoList = Lists.newArrayList();
        for (DemoPhoneTypeOfficeDto demoPhoneTypeOfficeDto : demoPhoneTypeForm.getDemoPhoneTypeOfficeList()) {
            if (StringUtils.isBlank(demoPhoneTypeOfficeDto.getId())) {
                demoPhoneTypeOfficeDto.setDemoPhoneTypeId(demoPhoneType.getId());
                demoPhoneTypeOfficeDtoList.add(demoPhoneTypeOfficeDto);
            } else {
                DemoPhoneTypeOffice demoPhoneTypeOffice = BeanUtil.map(demoPhoneTypeOfficeDto,DemoPhoneTypeOffice.class);
                demoPhoneTypeOfficeRepository.save(demoPhoneTypeOffice);
            }
        }
        if (CollectionUtil.isNotEmpty(demoPhoneTypeOfficeDtoList)) {
            List<DemoPhoneTypeOffice> demoPhoneTypeOfficeList = BeanUtil.map(demoPhoneTypeOfficeDtoList,DemoPhoneTypeOffice.class);
            demoPhoneTypeOfficeRepository.save(demoPhoneTypeOfficeList);
        }
        return demoPhoneType;
    }

    public List<DemoPhoneTypeDto> findNameLike(String name){
        List<DemoPhoneTypeDto> demoPhoneTypeDtos  = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)){
            demoPhoneTypeDtos = BeanUtil.map(demoPhoneTypeRepository.findByNameLike(name),DemoPhoneTypeDto.class);
        }
        return demoPhoneTypeDtos;
    }
}
