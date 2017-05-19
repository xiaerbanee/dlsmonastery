package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.OfficeChange;
import net.myspring.basic.modules.hr.mapper.OfficeChangeMapper;
import net.myspring.basic.modules.sys.mapper.OfficeMapper;
import net.myspring.basic.modules.hr.web.form.OfficeChangeForm;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
public class OfficeChangeService {

    @Autowired
    private OfficeChangeMapper officeChangeMapper;
    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public OfficeChange findOne(String id){
        OfficeChange officeChange=officeChangeMapper.findOne(id);
        return officeChange;
    }

    public OfficeChangeForm getForm(OfficeChangeForm officeChangeForm){
        if(!officeChangeForm.isCreate()){
            OfficeChange officeChange=findOne(officeChangeForm.getId());
            officeChangeForm= BeanUtil.map(officeChange,OfficeChangeForm.class);
            cacheUtils.initCacheInput(officeChangeForm);
        }
        return officeChangeForm;
    }
}
