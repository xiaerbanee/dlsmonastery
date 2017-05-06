package net.myspring.future.modules.crm.service;

import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.mapper.BankMapper;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.crm.dto.BankInDto;
import net.myspring.future.modules.crm.mapper.BankInMapper;
import net.myspring.future.modules.crm.web.query.BankInQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankInService {

    @Autowired
    private BankInMapper bankInMapper;
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private BankMapper bankMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<BankInDto> findPage(Pageable pageable, BankInQuery bankInQuery) {
        Page<BankInDto> page = bankInMapper.findPage(pageable, bankInQuery);
        if(page!=null && page.getContent()!=null){
            for(BankInDto each : page.getContent()){
                if(each.getShopParentId()!=null){
                    each.setRealShopName(depotMapper.findOne(each.getShopParentId()).getName());
                }else {
                    each.setRealShopName(each.getShopName());
                }
            }
        }
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public BankIn findOne(String id){
        BankIn bankIn=bankInMapper.findOne(id);
        return bankIn;
    }

    public void delete(BankIn bankIn) {
        bankIn.setEnabled(false);
        bankInMapper.update(bankIn);
    }

    @Transactional
    public void save(BankIn bankIn){
    }

    @Transactional
    public void audit(BankIn bankIn){
    }

}
