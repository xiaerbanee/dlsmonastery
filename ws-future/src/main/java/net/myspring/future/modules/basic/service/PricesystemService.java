package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.IdUtils;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.domain.PricesystemDetail;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.mapper.PricesystemDetailMapper;
import net.myspring.future.modules.basic.mapper.PricesystemMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.basic.web.query.PricesystemQuery;
import net.myspring.future.modules.basic.web.form.PricesystemDetailForm;
import net.myspring.future.modules.basic.web.form.PricesystemForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
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

    public Page<PricesystemDto> findPage(Pageable pageable, PricesystemQuery pricesystemQuery) {
        Page<PricesystemDto> page = pricesystemMapper.findPage(pageable, pricesystemQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<Pricesystem> findAll(){
        return pricesystemMapper.findAll();
    }

    public void logicDeleteOne(PricesystemForm pricesystemForm) {
        Pricesystem pricesystem = pricesystemMapper.findOne(pricesystemForm.getId());
        pricesystemForm  = BeanUtil.map(pricesystem,PricesystemForm.class);
        pricesystemForm.setName(pricesystemForm.getName()+"(废弃时间"+new Date()+")");
        pricesystemForm.setEnabled(false);
        ReflectionUtil.copyProperties(pricesystemForm,pricesystem);
        pricesystemMapper.update(pricesystem);
    }

    public Pricesystem save(PricesystemForm pricesystemForm) {
        Pricesystem pricesystem;
        if(pricesystemForm.isCreate()) {
            List<PricesystemDetailForm> pricesystemDetails = Lists.newArrayList();
            pricesystem= BeanUtil.map(pricesystemForm,Pricesystem.class);
            pricesystemMapper.save(pricesystem);
            for (int i = 0; i < pricesystemForm.getPricesystemDetailList().size(); i++) {
                PricesystemDetailForm pricesystemDetailForm = pricesystemForm.getPricesystemDetailList().get(i);
                if (pricesystemDetailForm.getPrice() != null && pricesystemDetailForm.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                    pricesystemDetailForm.setPricesystemId(pricesystem.getId());
                    pricesystemDetailForm.setProductId(pricesystemDetailForm.getProductId());
                    pricesystemDetails.add(pricesystemDetailForm);
                }
            }
            if (CollectionUtil.isNotEmpty(pricesystemDetails)) {
                Collections.sort(pricesystemDetails,new Comparator<PricesystemDetailForm>(){
                    public int compare(PricesystemDetailForm p1, PricesystemDetailForm p2) {
                        return p1.getProductName().compareTo(p2.getProductName());
                    }
                });
                String expressProductId =companyConfigClient.getValueByCode(CompanyConfigCodeEnum.EXPRESS_PRODUCT_ID.getCode());
                for(int i = 0;i<pricesystemDetails.size();i++) {
                    if(StringUtils.isNotBlank(expressProductId) && expressProductId.equals(pricesystemDetails.get(i).getProductId())) {
                        pricesystemDetails.get(i).setSort(0);
                    } else {
                        pricesystemDetails.get(i).setSort((i+1)*10);
                    }
                }
                for(int i = 0;i<pricesystemDetails.size();i++) {
                    pricesystemDetailMapper.save(BeanUtil.map(pricesystemDetails.get(i),PricesystemDetail.class));
                }
            }
        }else {
            pricesystem=pricesystemMapper.findOne(pricesystemForm.getId());
            ReflectionUtil.copyProperties(pricesystemForm,pricesystem);
            pricesystemMapper.update(pricesystem);
        }
        return pricesystem;
    }

    public Pricesystem findOne(String id) {
        Pricesystem pricesystem = pricesystemMapper.findOne(id);
        return pricesystem;
    }

    public void initPricesystemDetail(PricesystemForm pricesystemForm){
        String value = "371148";
        List<String> outGroupIds = IdUtils.getIdList(value);
        List<Product> productList = productMapper.findByOutGroupIds(outGroupIds);
        List<PricesystemDetailForm> pricesystemDetailList=Lists.newArrayList();
        for(Product product:productList){
            PricesystemDetailForm pricesystemDetailForm=new PricesystemDetailForm();
            if(pricesystemForm.getId() != null){
                PricesystemDetail pricesystemDetail=pricesystemDetailMapper.findByPricesystemIdAndProductId(pricesystemForm.getId(),product.getId());
                if(pricesystemDetail != null) {
                    pricesystemDetailForm = BeanUtil.map(pricesystemDetail, PricesystemDetailForm.class);
                    pricesystemDetailForm.setProductName(product.getName());
                }else{
                    continue;
                }
            }else{
                pricesystemDetailForm.setProductName(product.getName());
                pricesystemDetailForm.setProductId(product.getId());

            }
            pricesystemDetailList.add(pricesystemDetailForm);

        }
        pricesystemForm.setPricesystemDetailList(pricesystemDetailList);
    }

}
