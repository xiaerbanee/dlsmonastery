package net.myspring.future.modules.layout.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.client.ActivitiClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.layout.domain.ShopPrint;
import net.myspring.future.modules.layout.dto.ShopPrintDto;
import net.myspring.future.modules.layout.mapper.ShopPrintMapper;
import net.myspring.future.modules.layout.web.form.ShopPrintForm;
import net.myspring.future.modules.layout.web.query.ShopPrintQuery;
import net.myspring.general.modules.sys.form.ActivitiCompleteForm;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class ShopPrintService {

    @Autowired
    private ShopPrintMapper shopPrintMapper;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ActivitiClient activitiClient;


    public Page<ShopPrintDto> findPage(Pageable pageable, ShopPrintQuery shopPrintQuery) {
        Page<ShopPrintDto> page = shopPrintMapper.findPage(pageable, shopPrintQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public ShopPrintForm findForm(ShopPrintForm  shopPrintForm){
        if(!shopPrintForm.isCreate()){
            ShopPrint shopPrint = shopPrintMapper.findOne(shopPrintForm.getId());
            shopPrintForm = BeanUtil.map(shopPrint,ShopPrintForm.class);
            cacheUtils.initCacheInput(shopPrintForm);
        }
        return shopPrintForm;
    }

    public ShopPrint save(ShopPrintForm shopPrintForm) {
        ShopPrint shopPrint;
        if(shopPrintForm.isCreate()){
            shopPrint = BeanUtil.map(shopPrintForm,ShopPrint.class);
            shopPrintMapper.save(shopPrint);
        }else{
            shopPrint = shopPrintMapper.findOne(shopPrintForm.getId());
            ReflectionUtil.copyProperties(shopPrintForm,shopPrint);
            shopPrintMapper.update(shopPrint);
        }
        return shopPrint;
    }

    public void logicDelete(String id){
        shopPrintMapper.logicDeleteOne(id);
    }

    public void audit(ShopPrintForm shopPrintForm) {
        ActivitiCompleteForm activitiCompleteForm = new ActivitiCompleteForm();

        activitiCompleteForm.setPass(shopPrintForm.getPass()=="1"?true:false);
        activitiCompleteForm.setComment(shopPrintForm.getPassRemarks());

        ShopPrint shopPrint = shopPrintMapper.findOne(shopPrintForm.getId());
        activitiCompleteForm.setProcessInstanceId(shopPrint.getProcessInstanceId());
        activitiCompleteForm.setProcessTypeId(shopPrint.getProcessTypeId());
        activitiClient.complete(activitiCompleteForm);
    }

    public ShopPrintQuery findQuery(ShopPrintQuery shopPrintQuery){
        //shopPrintQuery.setPrintTypeList(dictMapClient.findDictMapByCategory(DictMapCategoryEnum.门店_广告印刷.name()));
        //shopPrintQuery.setOfficeIdList(officeClient.findSortList());
        return shopPrintQuery;
    }

    public void notify(ShopPrint shopPrint) {
    }
}
