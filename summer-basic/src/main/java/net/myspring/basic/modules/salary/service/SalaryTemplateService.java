package net.myspring.basic.modules.salary.service;

import net.myspring.basic.modules.hr.repository.SalaryTemplateRepository;
import net.myspring.basic.modules.salary.domain.SalaryTemplate;
import net.myspring.basic.modules.salary.dto.SalaryTemplateDto;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryTemplateService {

    @Autowired
    private SalaryTemplateRepository salaryTemplateRepository;

    public List<SalaryTemplateDto> findAll(){
        List<SalaryTemplate> salaryTemplateList=salaryTemplateRepository.findAll();
        List<SalaryTemplateDto> salaryTemplateDtoList= BeanUtil.map(salaryTemplateList,SalaryTemplateDto.class);
        return salaryTemplateDtoList;
    }
}
