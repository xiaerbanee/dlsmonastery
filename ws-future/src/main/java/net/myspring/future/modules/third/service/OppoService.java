package net.myspring.future.modules.third.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.third.client.OppoClient;
import net.myspring.future.modules.third.domain.*;
import net.myspring.future.modules.third.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by guolm on 2017/5/23.
 */
@Service
@Transactional(readOnly = false)
public class OppoService {
    @Autowired
    private OppoClient oppoClient;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private OppoCustomerAllotRepository oppoCustomerAllotRepository;
    @Autowired
    private OppoCustomerStockRepository oppoCustomerStockRepository;
    @Autowired
    private OppoCustomerImeiStockRepository oppoCustomerImeiStockRepository;
    @Autowired
    private OppoCustomerSaleRepository oppoCustomerSaleRepository;
    @Autowired
    OppoCustomerSaleImeiRepository oppoCustomerSaleImeiRepository;
    @Autowired
    private OppoCustomerSaleCountRepository oppoCustomerSaleCountRepository;
    @Autowired
    private OppoCustomerAfterSaleImeiRepository oppoCustomerAfterSaleImeiRepository;
    @Autowired
    private OppoCustomerDemoPhoneRepository oppoCustomerDemoPhoneRepository;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public List<OppoCustomerAllot> findOppoCustomerAllots(LocalDate dateStart, LocalDate dateEnd){
        List<OppoCustomerAllot> oppoCustomerAllots=oppoCustomerAllotRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd));
        return oppoCustomerAllots;
    }

    public List<OppoCustomerStock> findOppoCustomerStocks(LocalDate dateStart, LocalDate dateEnd){
        List<OppoCustomerStock> oppoCustomerStocks=oppoCustomerStockRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd));
        return oppoCustomerStocks;
    }

    public List<OppoCustomerImeiStock> findOppoCustomerImeiStocks(LocalDate dateStart, LocalDate dateEnd){
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoCustomerImeiStockRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd));
        return oppoCustomerImeiStocks;
    }

    public List<OppoCustomerSale> findOppoCustomerSales(LocalDate dateStart, LocalDate dateEnd){
        List<OppoCustomerSale> oppoCustomerSales=oppoCustomerSaleRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd));
        return oppoCustomerSales;
    }

    public List<OppoCustomerSaleImei> findOppoCustomerSaleImeis(LocalDate dateStart, LocalDate dateEnd){
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoCustomerSaleImeiRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd));
        return oppoCustomerSaleImeis;
    }

    public List<OppoCustomerSaleCount>  findOppoCustomerSaleCounts(LocalDate dateStart, LocalDate dateEnd){
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoCustomerSaleCountRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd));
        return oppoCustomerSaleCounts;
    }

    public List<OppoCustomerAfterSaleImei> findOppoCustomerAfterSaleImeis(LocalDate dateStart, LocalDate dateEnd){
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoCustomerAfterSaleImeiRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd));
        return oppoCustomerAfterSaleImeis;
    }

    public List<OppoCustomerDemoPhone> findOppoCustomerDemoPhones(LocalDate dateStart, LocalDate dateEnd){
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoCustomerDemoPhoneRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd));
        return oppoCustomerDemoPhones;
    }
}
