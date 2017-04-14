package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.DemoPhoneType;
import net.myspring.future.modules.crm.mapper.DemoPhoneTypeMapper;
import net.myspring.future.modules.crm.mapper.DemoPhoneTypeOfficeMapper;
import net.myspring.future.modules.crm.mapper.ProductTypeMapper;
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
    private DemoPhoneTypeOfficeMapper demoPhoneTypeOfficeMapper;
    @Autowired
    private ProductTypeMapper productTypeMapper;

    public DemoPhoneType findOne(String id) {
        return null;
    }

    public DemoPhoneType initDemoPhoneTypeDetails(DemoPhoneType demoPhoneType){
        return demoPhoneType;
    }

    public List<DemoPhoneType> findAllByApplyEndDate(LocalDate applyEndDate) {
        List<DemoPhoneType> demoPhoneTypeList = demoPhoneTypeMapper.findAllByApplyEndDate(applyEndDate);
        return demoPhoneTypeList;
    }

    public Page<DemoPhoneType> findPage(Pageable pageable, Map<String, Object> map) {
        Page<DemoPhoneType> page = demoPhoneTypeMapper.findPage(pageable, map);
        return page;
    }

    public void delete(DemoPhoneType demoPhoneType) {
        demoPhoneTypeMapper.logicDeleteOne(demoPhoneType.getId());
    }

    public DemoPhoneType save(DemoPhoneType demoPhoneType) {
        return demoPhoneType;
    }
}
