package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.IdUtils;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.PricesystemDetailDto;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.manager.PricesystemDetailManager;
import net.myspring.future.modules.basic.manager.PricesystemManager;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.PricesystemMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.basic.web.Query.PricesystemQuery;
import net.myspring.future.modules.basic.web.form.AdPricesystemDetailForm;
import net.myspring.future.modules.basic.web.form.PricesystemDetailForm;
import net.myspring.future.modules.basic.web.form.PricesystemForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PricesystemService {

    @Autowired
    private PricesystemMapper pricesystemMapper;
    @Autowired
    private PricesystemDetailMapper pricesystemDetailMapper;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private PricesystemManager pricesystemManager;
    @Autowired
    private PricesystemDetailManager pricesystemDetailManager;



    public Page<PricesystemDto> findPage(Pageable pageable, PricesystemQuery pricesystemQuery) {
        Page<PricesystemDto> page = pricesystemMapper.findPage(pageable, pricesystemQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<Pricesystem> findAll(){
        return pricesystemMapper.findAll();
    }

    public void logicDeleteOne(PricesystemForm pricesystemForm) {
        pricesystemForm.setName(pricesystemForm.getName()+"(废弃时间"+new Date()+")");
        pricesystemForm.setEnabled(false);
        pricesystemMapper.updateForm(pricesystemForm);
    }

    public Pricesystem save(PricesystemForm pricesystemForm) {
        Pricesystem pricesystem;
        if(pricesystemForm.isCreate()) {
            List<PricesystemDetailForm> pricesystemDetails = Lists.newArrayList();
            pricesystem= BeanUtil.map(pricesystemForm,Pricesystem.class);
            pricesystem=pricesystemManager.save(pricesystem);
            for (int i = 0; i < pricesystemForm.getPricesystemDetailList().size(); i++) {
                PricesystemDetailForm pricesystemDetailForm = pricesystemForm.getPricesystemDetailList().get(i);
                if (pricesystemDetailForm.getPrice() != null && pricesystemDetailForm.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                    pricesystemDetailForm.setPricesystemId(pricesystem.getId());
                    pricesystemDetailForm.setProductId(pricesystemDetailForm.getProduct().getId());
                    pricesystemDetails.add(pricesystemDetailForm);
                }
            }
            if (CollectionUtil.isNotEmpty(pricesystemDetails)) {
                Collections.sort(pricesystemDetails,new Comparator<PricesystemDetailForm>(){
                    public int compare(PricesystemDetailForm p1, PricesystemDetailForm p2) {
                        return p1.getProduct().getName().compareTo(p2.getProduct().getName());
                    }
                });
                String expressProductId = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.EXPRESS_PRODUCT_ID.getCode());
                for(int i = 0;i<pricesystemDetails.size();i++) {
                    if(StringUtils.isNotBlank(expressProductId) && expressProductId.equals(pricesystemDetails.get(i).getProductId())) {
                        pricesystemDetails.get(i).setSort(0);
                    } else {
                        pricesystemDetails.get(i).setSort((i+1)*10);
                    }
                }
                for(int i = 0;i<pricesystemDetails.size();i++) {
                    pricesystemDetailManager.save(BeanUtil.map(pricesystemDetails.get(i),PricesystemDetail.class));
                }
            }
        }else {
            pricesystem=pricesystemManager.updateForm(pricesystemForm);
        }
        return pricesystem;
    }

    public Pricesystem findOne(String id) {
        Pricesystem pricesystem = pricesystemMapper.findOne(id);
        return pricesystem;
    }

    public void initPricesystemDetail(PricesystemForm pricesystemForm){
        String value = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.PRODUCT_GOODS_GROUP_IDS.getCode());
        List<String> outGroupIds = IdUtils.getIdList(value);
        List<Product> productList = productMapper.findByOutGroupIds(outGroupIds);
        List<PricesystemDetailForm> pricesystemDetailList=Lists.newArrayList();
        for(Product product:productList){
            PricesystemDetailForm pricesystemDetailForm=new PricesystemDetailForm();
            pricesystemDetailForm.setProduct(product);
            pricesystemDetailList.add(pricesystemDetailForm);
        }
        pricesystemForm.setPricesystemDetailList(pricesystemDetailList);
    }

}
