package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.future.modules.crm.mapper.PriceChangeImeMapper;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class PriceChangeImeService {

    @Autowired
    private PriceChangeImeMapper priceChangeImeMapper;
    @Autowired
    private ProductImeMapper productImeMapper;

    public PriceChangeIme findOne(String id){
        PriceChangeIme priceChangeIme=priceChangeImeMapper.findOne(id);
        return priceChangeIme;
    }

    public Page<PriceChangeIme> findPage(Pageable pageable, Map<String,Object> map){
        Page<PriceChangeIme> page=priceChangeImeMapper.findPage(pageable,map);
        return page;
    }

    @Transactional
    public void imageUpload(PriceChangeIme priceChangeIme){
        priceChangeImeMapper.update(priceChangeIme);
    }

    @Transactional
    public void audit(PriceChangeIme priceChangeIme){
        priceChangeIme.setAuditDate(LocalDateTime.now());
        priceChangeImeMapper.update(priceChangeIme);

    }
}
