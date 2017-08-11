package net.myspring.cloud.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.CacheUtils;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.service.SalOutStockService;
import net.myspring.cloud.modules.input.service.SalReturnStockService;
import net.myspring.cloud.modules.kingdee.domain.ArReceivable;
import net.myspring.cloud.modules.kingdee.service.ArReceivableService;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeSyn;
import net.myspring.cloud.modules.sys.repository.AccountKingdeeBookRepository;
import net.myspring.cloud.modules.sys.repository.KingdeeBookRepository;
import net.myspring.cloud.modules.sys.repository.KingdeeSynRepository;
import net.myspring.cloud.modules.sys.web.query.KingdeeSynQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 金蝶同步
 * Created by lihx on 2017/6/21.
 */
@Service
@LocalDataSource
@Transactional(readOnly = true)
public class KingdeeSynService {
    @Autowired
    private KingdeeSynRepository kingdeeSynRepository;
    @Autowired
    private KingdeeBookRepository kingdeeBookRepository;
    @Autowired
    private AccountKingdeeBookRepository accountKingdeeBookRepository;
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private ArReceivableService arReceivableService;
    @Autowired
    private SalOutStockService salOutStockService;
    @Autowired
    private SalReturnStockService salReturnStockService;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<KingdeeSynDto> findPage(Pageable pageable, KingdeeSynQuery kingdeeSynQuery){
        KingdeeBook kingdeeBook = kingdeeBookRepository.findByCompanyName(RequestUtils.getCompanyName());
        kingdeeSynQuery.setKingdeeBookId(kingdeeBook.getId());
        Page<KingdeeSynDto> page = kingdeeSynRepository.findPage(pageable,kingdeeSynQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void logicDelete(String id) {
        kingdeeSynRepository.logicDelete(id);
    }

    public KingdeeSyn findOne(String id){
        return kingdeeSynRepository.findOne(id);
    }

    public List<KingdeeSyn> findByExtendIdAndExtendType(String extendId, String extendType){
        return kingdeeSynRepository.findByExtendIdAndExtendType(extendId,extendType);
    }

    @Transactional
    public KingdeeSyn save(KingdeeSyn kingdeeSyn) {
        if(kingdeeSyn != null) {
            return kingdeeSynRepository.save(kingdeeSyn);
        }
        return null;
    }

    @Transactional
    public List<KingdeeSyn> save(List<KingdeeSyn> kingdeeSynList) {
        if(CollectionUtil.isNotEmpty(kingdeeSynList)) {
            return kingdeeSynRepository.save(kingdeeSynList);
        }
        return null;
    }

    @Transactional
    public KingdeeSynDto syn(String id){
        KingdeeSynDto kingdeeSynDto;
        KingdeeSyn kingdeeSyn = kingdeeSynRepository.findOne(id);
        kingdeeSynDto = BeanUtil.map(kingdeeSyn,KingdeeSynDto.class);
        if (kingdeeSynDto != null) {
            AccountKingdeeBook accountKingdeeBook = accountKingdeeBookRepository.findByAccountIdAndCompanyName(RequestUtils.getAccountId(),RequestUtils.getCompanyName());
            KingdeeBook kingdeeBook = kingdeeBookRepository.findOne(accountKingdeeBook.getKingdeeBookId());
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                kingdeeSynDto.setKingdeeBook(kingdeeBook);
                kingdeeSynDto = kingdeeManager.save(kingdeeSynDto);
            }
        }
        return kingdeeSynDto;
    }

    @Transactional
    public int flush(){
        int count = 0;
        //未下推的单据（销售出库+销售退货）
        List<String> billNoList = kingdeeSynRepository.findByBillNoLikeAndNextBillNoIsNull("XS").stream().map(KingdeeSyn::getBillNo).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(billNoList)) {
            List<ArReceivable> arReceivableList = arReceivableService.findBySourceBillNoList(billNoList);
            if (CollectionUtil.isNotEmpty(arReceivableList)) {
                List<String> sourceBillNoList = arReceivableList.stream().map(ArReceivable::getFSourceBillNo).collect(Collectors.toList());
                List<KingdeeSyn> kingdeeSynList = kingdeeSynRepository.findByBillNoList(sourceBillNoList);
                for (ArReceivable arReceivable : arReceivableList) {
                    for (KingdeeSyn kingdeeSyn : kingdeeSynList) {
                        if (arReceivable.getFSourceBillNo().equals(kingdeeSyn.getBillNo())) {
                            kingdeeSyn.setNextBillNo(arReceivable.getFBillNo());
                            kingdeeSynRepository.save(kingdeeSyn);
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public List<KingdeeSyn> findNoPushDown(){
        List<String> list = Lists.newArrayList();
        list.addAll(salOutStockService.findNoPushDown());
        list.addAll(salReturnStockService.findNoPushDown());
        if (list.size() > 0){
            return kingdeeSynRepository.findByBillNoList(list);
        }
        return null;
    }
}
