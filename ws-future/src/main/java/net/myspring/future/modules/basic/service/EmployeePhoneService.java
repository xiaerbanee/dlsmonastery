package net.myspring.future.modules.basic.service;


import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.EmployeePhone;
import net.myspring.future.modules.basic.dto.EmployeePhoneDto;
import net.myspring.future.modules.basic.repository.EmployeePhoneRepository;
import net.myspring.future.modules.basic.web.form.EmployeePhoneForm;
import net.myspring.future.modules.basic.web.query.EmployeePhoneQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EmployeePhoneService {

    @Autowired
    private EmployeePhoneRepository employeePhoneRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public EmployeePhoneDto findOne(EmployeePhoneDto employeePhoneDto) {
        if(!employeePhoneDto.isCreate()){
            EmployeePhone employeePhone = employeePhoneRepository.findOne(employeePhoneDto.getId());
            employeePhoneDto= BeanUtil.map(employeePhone,EmployeePhoneDto.class);
            cacheUtils.initCacheInput(employeePhoneDto);
        }
        return employeePhoneDto;
    }

    public EmployeePhone save(EmployeePhoneForm employeePhoneForm) {
        EmployeePhone employeePhone;
        if(employeePhoneForm.isCreate()) {
            employeePhone = BeanUtil.map(employeePhoneForm, EmployeePhone.class);
            employeePhoneRepository.save(employeePhone);
        } else {
            employeePhone = employeePhoneRepository.findOne(employeePhoneForm.getId());
            ReflectionUtil.copyProperties(employeePhone,employeePhone);
            employeePhone =  employeePhoneRepository.save(employeePhone);
        }
        return employeePhone;
    }

    public void logicDeleteOne(EmployeePhoneForm employeePhoneForm) {
        employeePhoneRepository.logicDelete(employeePhoneForm.getId());
    }

    public Page<EmployeePhoneDto> findPage(Pageable pageable, EmployeePhoneQuery employeePhoneQuery) {
        Page<EmployeePhoneDto> page = employeePhoneRepository.findPage(pageable, employeePhoneQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }
}
