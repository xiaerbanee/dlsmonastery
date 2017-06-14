package net.myspring.future.modules.basic.service;

import net.myspring.future.common.enums.EmployeePhoneDepositStatusEnum;
import net.myspring.future.common.enums.EmployeePhoneStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.basic.domain.EmployeePhone;
import net.myspring.future.modules.basic.domain.EmployeePhoneDeposit;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.EmployeePhoneDepositDto;
import net.myspring.future.modules.basic.repository.BankRepository;
import net.myspring.future.modules.basic.repository.EmployeePhoneDepositRepository;
import net.myspring.future.modules.basic.repository.EmployeePhoneRepository;
import net.myspring.future.modules.basic.web.form.EmployeePhoneDepositForm;
import net.myspring.future.modules.basic.web.query.EmployeePhoneDepositQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by admin on 2017/2/17.
 */
@Service
@Transactional
public class EmployeePhoneDepositService {

    @Autowired
    private EmployeePhoneDepositRepository employeePhoneDepositRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private EmployeePhoneRepository employeePhoneRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public EmployeePhoneDepositDto findOne(EmployeePhoneDepositDto employeePhoneDepositDto) {
        if(!employeePhoneDepositDto.isCreate()){
            EmployeePhoneDeposit employeePhoneDeposit = employeePhoneDepositRepository.findOne(employeePhoneDepositDto.getId());
            employeePhoneDepositDto= BeanUtil.map(employeePhoneDeposit,EmployeePhoneDepositDto.class);
            cacheUtils.initCacheInput(employeePhoneDeposit);
        }
        return employeePhoneDepositDto;
    }

    public List<EmployeePhoneDepositDto> findByIds(List<String> ids) {
        List<EmployeePhoneDeposit> employeePhoneDepositList = employeePhoneDepositRepository.findAll(ids);
        List<EmployeePhoneDepositDto> employeePhoneDepositDtoList=BeanUtil.map(employeePhoneDepositList,EmployeePhoneDepositDto.class);
        cacheUtils.initCacheInput(employeePhoneDepositList);
        return employeePhoneDepositDtoList;
    }

    public EmployeePhoneDeposit save(EmployeePhoneDepositForm employeePhoneDepositForm) {
        EmployeePhoneDeposit employeePhoneDeposit;
        if (employeePhoneDepositForm.isCreate()) {
            String bankName = "GC邮（备用金）";
            if ("JXvivo".equalsIgnoreCase(RequestUtils.getRequestEntity().getCompanyName())) {
                bankName = "ZBL邮（备用金）";
            }
            employeePhoneDeposit=BeanUtil.map(employeePhoneDepositForm,EmployeePhoneDeposit.class);
            Bank bank = bankRepository.findByName(bankName);
            employeePhoneDeposit.setBankId(bank.getId());
            employeePhoneDeposit.setOutBillType("手工日记账");
            employeePhoneDeposit.setLocked(false);
            employeePhoneDeposit.setStatus(EmployeePhoneDepositStatusEnum.省公司审核.name());
        }else {
            employeePhoneDeposit=employeePhoneDepositRepository.findOne(employeePhoneDepositForm.getId());
            ReflectionUtil.copyProperties(employeePhoneDepositForm,employeePhoneDeposit);
        }
        employeePhoneDepositRepository.save(employeePhoneDeposit);
        return employeePhoneDeposit;
    }

    public void logicDeleteOne(EmployeePhoneDepositForm employeePhoneDepositForm) {
        employeePhoneDepositRepository.logicDelete(employeePhoneDepositForm.getId());
    }

    public Page<EmployeePhoneDepositDto> findPage(Pageable pageable, EmployeePhoneDepositQuery employeePhoneDepositQuery) {
        Page<EmployeePhoneDepositDto> page = employeePhoneDepositRepository.findPage(pageable, employeePhoneDepositQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void batchAudit(List<String> ids, boolean pass) {
        List<EmployeePhoneDeposit> employeePhoneDepositList=employeePhoneDepositRepository.findAll(ids);
        if (!pass) {
            for(EmployeePhoneDeposit employeePhoneDeposit:employeePhoneDepositList){
                employeePhoneDeposit.setStatus(EmployeePhoneDepositStatusEnum.未通过.name());
            }
            employeePhoneDepositRepository.save(employeePhoneDepositList);
        } else {
            for (EmployeePhoneDeposit employeePhoneDeposit : employeePhoneDepositList) {
                String otherTypes = "其他应付款-导购业务机押金";
                if (EmployeePhoneDepositStatusEnum.省公司审核.name().equals(employeePhoneDeposit.getStatus()) && StringUtils.isBlank(employeePhoneDeposit.getOutCode())) {
                    if (StringUtils.isNotBlank(employeePhoneDeposit.getOutBillType()) && "手工日记账".equals(employeePhoneDeposit.getOutBillType())) {
                        employeePhoneDeposit.setStatus(EmployeePhoneDepositStatusEnum.已通过.name());
                        employeePhoneDeposit.setLocked(true);
                        employeePhoneDeposit.setBillDate(LocalDateTime.now());
                        employeePhoneDepositRepository.save(employeePhoneDeposit);
                        if (employeePhoneDeposit.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                            EmployeePhone employeePhone = new EmployeePhone();
                            Product product = employeePhoneDeposit.getProduct();
                            employeePhone.setProductId(employeePhoneDeposit.getProductId());
                            employeePhone.setJobPrice(product.getPrice2());
                            employeePhone.setRetailPrice(product.getRetailPrice());
                            employeePhone.setDepotId(employeePhoneDeposit.getDepotId());
                            employeePhone.setEmployeeId(employeePhoneDeposit.getEmployeeId());
                            employeePhone.setUploadTime(employeePhoneDeposit.getCreatedDate());
                            employeePhone.setStatus(EmployeePhoneStatusEnum.已交.name());
                            employeePhoneRepository.save(employeePhone);
                        }
                    }
                }
            }
        }
    }
}
