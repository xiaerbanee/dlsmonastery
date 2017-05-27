package net.myspring.future.modules.crm.service;

import net.myspring.future.common.enums.PriceChangeStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.repository.PriceChangeRepository;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.future.modules.crm.dto.PriceChangeImeDto;
import net.myspring.future.modules.crm.repository.PriceChangeImeRepository;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.web.form.PriceChangeImeForm;
import net.myspring.future.modules.crm.web.form.PriceChangeImeUploadForm;
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PriceChangeImeService {

    @Autowired
    private PriceChangeImeRepository priceChangeImeRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private PriceChangeRepository priceChangeRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public PriceChangeImeDto findOne(String id){
        PriceChangeImeDto priceChangeImeDto = new PriceChangeImeDto();
        if(StringUtils.isNotBlank(id)){
            PriceChangeIme priceChangeIme=priceChangeImeRepository.findOne(id);
            priceChangeImeDto = BeanUtil.map(priceChangeIme,PriceChangeImeDto.class);
            cacheUtils.initCacheInput(priceChangeImeDto);
        }

        return priceChangeImeDto;
    }

    public Page<PriceChangeImeDto> findPage(Pageable pageable, PriceChangeImeQuery priceChangeImeQuery){
        Page<PriceChangeImeDto> page=priceChangeImeRepository.findPage(pageable,priceChangeImeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public PriceChangeImeForm getForm(PriceChangeImeForm priceChangeImeForm){
        if(!priceChangeImeForm.isCreate()){
            PriceChangeIme priceChangeIme = priceChangeImeRepository.findOne(priceChangeImeForm.getId());
            priceChangeImeForm = BeanUtil.map(priceChangeIme,PriceChangeImeForm.class);
            if(priceChangeImeForm.getPriceChangeId()!=null){
                priceChangeImeForm.setPriceChangeName(priceChangeRepository.findOne(priceChangeImeForm.getPriceChangeId()).getName());
            }
            if(priceChangeImeForm.getProductImeId()!=null){
                priceChangeImeForm.setProductId(productImeRepository.findOne(priceChangeImeForm.getProductImeId()).getProductId());
                priceChangeImeForm.setIme(productImeRepository.findOne(priceChangeImeForm.getProductImeId()).getIme());
            }
        }else {
            List<PriceChange> priceChange = priceChangeRepository.findByEnabledIsTrueOrderByIdDesc();
            priceChangeImeForm.setPriceChangeDtos(BeanUtil.map(priceChange, PriceChangeDto.class));
        }
        return priceChangeImeForm;
    }

    public void save(PriceChangeImeUploadForm priceChangeImeUploadForm){
        PriceChange priceChange = priceChangeRepository.findOne(priceChangeImeUploadForm.getPriceChangeId());
        List<List<String>> imeUploadList = priceChangeImeUploadForm.getImeUploadList();
        checkUploadIme(priceChange,imeUploadList);
    }

    public void imageUpload(PriceChangeImeForm priceChangeImeForm){
        PriceChangeIme priceChangeIme = priceChangeImeRepository.findOne(priceChangeImeForm.getId());
        priceChangeIme.setImage(priceChangeImeForm.getImage());
        priceChangeIme.setStatus(PriceChangeStatusEnum.上报中.name());
        priceChangeImeRepository.save(priceChangeIme);
    }

    public void audit(PriceChangeImeForm priceChangeImeForm){
        if(priceChangeImeForm.getPass()!=null) {
            PriceChangeIme priceChangeIme = priceChangeImeRepository.findOne(priceChangeImeForm.getId());
            if (priceChangeImeForm.getPass().equalsIgnoreCase("1")) {
                priceChangeIme.setStatus(PriceChangeStatusEnum.已通过.name());
            } else {
                priceChangeIme.setStatus(PriceChangeStatusEnum.未通过.name());
            }
            priceChangeIme.setAuditRemarks(priceChangeImeForm.getAuditRemarks());
            priceChangeIme.setAuditDate(LocalDateTime.now());
            priceChangeIme.setAuditBy(RequestUtils.getAccountId());
            priceChangeImeRepository.save(priceChangeIme);
        }
    }

    public String checkUploadIme(PriceChange priceChange,List<List<String>> imeUploadList){
        if(CollectionUtil.isEmpty(imeUploadList)){
            return null;
        }
        List<String> shopNameList = new ArrayList<>();
        List<String> imeList = new ArrayList<>();
        for(List<String> row:imeUploadList){
            String shopName = StringUtils.toString(row.get(0)).trim();
            String ime = StringUtils.toString(row.get(1)).trim();
            shopNameList.add(shopName);
            imeList.add(ime);
        }
        if(shopNameList == null||imeList == null){
            return null;
        }
        if(shopNameList.size()!=imeList.size()){
            return null;
        }

        //检查门店，串码在系统中是否存在
        List<Depot> depots = depotRepository.findByEnabledIsTrueAndNameIn(shopNameList);
        List<ProductIme> productImes = productImeRepository.findByImeList(imeList);
        return null;
    }
}
