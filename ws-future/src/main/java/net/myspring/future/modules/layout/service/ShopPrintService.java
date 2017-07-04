package net.myspring.future.modules.layout.service;

import net.myspring.future.common.enums.OfficeRuleEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.layout.domain.ShopPrint;
import net.myspring.future.modules.layout.dto.ShopPrintDto;
import net.myspring.future.modules.layout.repository.ShopPrintRepository;
import net.myspring.future.modules.layout.web.form.ShopPrintAuditForm;
import net.myspring.future.modules.layout.web.form.ShopPrintForm;
import net.myspring.future.modules.layout.web.query.ShopPrintQuery;
import net.myspring.general.modules.sys.dto.ActivitiCompleteDto;
import net.myspring.general.modules.sys.dto.ActivitiStartDto;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.general.modules.sys.form.ActivitiStartForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ShopPrintService {

    @Autowired
    private ShopPrintRepository shopPrintRepository;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;


    public Page<ShopPrintDto> findPage(Pageable pageable, ShopPrintQuery shopPrintQuery) {
        Page<ShopPrintDto> page = shopPrintRepository.findPage(pageable, shopPrintQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopPrintForm getForm(ShopPrintForm  shopPrintForm){
        shopPrintForm.getExtra().put("areaList", officeClient.findByOfficeRuleName(OfficeRuleEnum.办事处.name()));
        return shopPrintForm;
    }

    public ShopPrintDto findOne(String id){
        ShopPrintDto shopPrintDto = new ShopPrintDto();
        if(StringUtils.isNotBlank(id)){
            ShopPrint shopPrint = shopPrintRepository.findOne(id);
            shopPrintDto = BeanUtil.map(shopPrint,ShopPrintDto.class);
            cacheUtils.initCacheInput(shopPrintDto);
        }
        return shopPrintDto;
    }

    @Transactional
    public ShopPrint save(ShopPrintForm shopPrintForm) {
        ShopPrint shopPrint;
        if(shopPrintForm.isCreate()){
            shopPrint = BeanUtil.map(shopPrintForm,ShopPrint.class);
            shopPrintRepository.save(shopPrint);

            //开始流程
            ActivitiStartDto activitiStartDto = activitiClient.start(new ActivitiStartForm("广告印刷",shopPrint.getId(),ShopPrint.class.getSimpleName(),shopPrint.getPrintType()));
            if(activitiStartDto!=null){
                shopPrint.setProcessFlowId(activitiStartDto.getProcessFlowId());
                shopPrint.setProcessInstanceId(activitiStartDto.getProcessInstanceId());
                shopPrint.setProcessPositionId(activitiStartDto.getPositionId());
                shopPrint.setProcessStatus(activitiStartDto.getProcessStatus());
                shopPrint.setProcessTypeId(activitiStartDto.getProcessTypeId());
                shopPrintRepository.save(shopPrint);
            }
        }else{
            shopPrint = shopPrintRepository.findOne(shopPrintForm.getId());
            ReflectionUtil.copyProperties(shopPrintForm,shopPrint);
            shopPrintRepository.save(shopPrint);
        }
        return shopPrint;
    }

    @Transactional
    public void logicDelete(String id){
        shopPrintRepository.logicDelete(id);
    }

    @Transactional
    public void audit(ShopPrintAuditForm shopPrintAuditForm) {
        ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();

        activitiCompleteForm.setPass(shopPrintAuditForm.getPass());
        activitiCompleteForm.setComment(shopPrintAuditForm.getPassRemarks());

        ShopPrint shopPrint = shopPrintRepository.findOne(shopPrintAuditForm.getId());
        activitiCompleteForm.setProcessInstanceId(shopPrint.getProcessInstanceId());
        activitiCompleteForm.setProcessTypeId(shopPrint.getProcessTypeId());
        ActivitiCompleteDto activitiCompleteDto = activitiClient.complete(activitiCompleteForm);
        if(activitiCompleteDto!=null){
            shopPrint.setLocked(true);
            shopPrint.setProcessStatus(activitiCompleteDto.getProcessStatus());
            shopPrint.setProcessPositionId(activitiCompleteDto.getPositionId());
            shopPrint.setProcessFlowId(activitiCompleteDto.getProcessFlowId());
            shopPrintRepository.save(shopPrint);
        }
    }

    public ShopPrintQuery findQuery(ShopPrintQuery shopPrintQuery){
        return shopPrintQuery;
    }

    public void notify(ShopPrint shopPrint) {
    }
}
