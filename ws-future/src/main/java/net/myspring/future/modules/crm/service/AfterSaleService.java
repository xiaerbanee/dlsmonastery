package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.mapper.ProductMapper;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.mapper.*;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class AfterSaleService {

    @Autowired
    private AfterSaleMapper afterSaleMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private ProductImeMapper productImeMapper;


    public List<ProductIme> findFormData(List<String> imeList){
        List<ProductIme> productImeList=productImeMapper.findByImeList(imeList);
        return productImeList;
    }

    public Map<String,AfterSale> findAfterSale(List<String> imeList) {
        List<AfterSale> afterSales = afterSaleMapper.findByBadProductImeIn(imeList);
        Map<String,AfterSale> map = Maps.newHashMap();

        return map;
    }

    //单据录入
    @Transactional
    public void save(List<List<String>> datas,LocalDate toStoreDate) {
    }


    //单据修改
    @Transactional
    public void update(List<List<String>> datas) {
    }


    //坏机返厂
    @Transactional
    public void toCompany(List<String> badImes,LocalDate toCompanyDate,String toCompanyRemarks) {

    }


    //工厂返回
    @Transactional
    public void fromCompany(List<List<String>> datas,LocalDate fromCompanyDate) {

    }


    @Transactional
    public void delete(AfterSale afterSale) {
    }


    @Transactional
    public void synToFinance(){
    }

    public Page findPage(Pageable pageable, AfterSaleQuery afterSaleQuery){
        Page<AfterSale> page=afterSaleMapper.findPage(pageable,afterSaleQuery);
        return page;
    }

    public List<AfterSale> findFilter(Map<String,Object> map){
        List<AfterSale> page=afterSaleMapper.findFilter(map);
        return page;
    }

    public void logicDelete(String id){
        afterSaleMapper.logicDeleteOne(id);
    }

    public AfterSale findOne(String id){
        AfterSale afterSale=afterSaleMapper.findOne(id);
        return afterSale;
    }

}
