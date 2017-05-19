package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.future.modules.crm.dto.PriceChangeImeDto;
import net.myspring.future.modules.crm.mapper.PriceChangeImeMapper;
import net.myspring.future.modules.crm.mapper.PriceChangeMapper;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.future.modules.crm.web.form.PriceChangeImeForm;
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PriceChangeImeService {

    @Autowired
    private PriceChangeImeMapper priceChangeImeMapper;
    @Autowired
    private ProductImeMapper productImeMapper;
    @Autowired
    private PriceChangeMapper priceChangeMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public PriceChangeIme findOne(String id){
        PriceChangeIme priceChangeIme=priceChangeImeMapper.findOne(id);
        return priceChangeIme;
    }

    public Page<PriceChangeImeDto> findPage(Pageable pageable, PriceChangeImeQuery priceChangeImeQuery){
        Page<PriceChangeImeDto> page=priceChangeImeMapper.findPage(pageable,priceChangeImeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public PriceChangeImeForm getFormProperty(PriceChangeImeForm priceChangeImeForm){
        if(!priceChangeImeForm.isCreate()){
            PriceChangeIme priceChangeIme = priceChangeImeMapper.findOne(priceChangeImeForm.getId());
            priceChangeImeForm = BeanUtil.map(priceChangeIme,PriceChangeImeForm.class);
            if(priceChangeImeForm.getPriceChangeId()!=null){
                priceChangeImeForm.setPriceChangeName(priceChangeMapper.findOne(priceChangeImeForm.getPriceChangeId()).getName());
            }
            if(priceChangeImeForm.getProductImeId()!=null){
                priceChangeImeForm.setProductId(productImeMapper.findOne(priceChangeImeForm.getProductImeId()).getProductId());
                priceChangeImeForm.setIme(productImeMapper.findOne(priceChangeImeForm.getProductImeId()).getIme());
            }
        }else {
            List<PriceChange> priceChange = priceChangeMapper.findAllEnabled();
            priceChangeImeForm.setPriceChangeDtos(BeanUtil.map(priceChange, PriceChangeDto.class));
        }
        return priceChangeImeForm;
    }

    public void save(PriceChangeImeForm priceChangeImeForm){

    }

    @Transactional
    public void imageUpload(PriceChangeImeForm priceChangeImeForm){
        PriceChangeIme priceChangeIme = priceChangeImeMapper.findOne(priceChangeImeForm.getId());
        priceChangeIme.setImage(priceChangeImeForm.getImage());
        priceChangeIme.setStatus("上报中");
        priceChangeImeMapper.update(priceChangeIme);
    }

    @Transactional
    public void audit(PriceChangeImeForm priceChangeImeForm){
        if(priceChangeImeForm.getPass()!=null) {
            PriceChangeIme priceChangeIme = priceChangeImeMapper.findOne(priceChangeImeForm.getId());
            if (priceChangeImeForm.getPass().equalsIgnoreCase("1")) {
                priceChangeIme.setStatus("已通过");
            } else {
                priceChangeIme.setStatus("未通过");
            }
            priceChangeIme.setAuditRemarks(priceChangeImeForm.getAuditRemarks());
            priceChangeIme.setAuditDate(LocalDateTime.now());
            priceChangeIme.setAuditBy(RequestUtils.getAccountId());
            priceChangeImeMapper.update(priceChangeIme);
        }
    }
}
