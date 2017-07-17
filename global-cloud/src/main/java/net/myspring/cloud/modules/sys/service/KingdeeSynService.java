package net.myspring.cloud.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
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
import java.util.Map;

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

    public Page<KingdeeSyn> findPage(Pageable pageable, KingdeeSynQuery kingdeeSynQuery){
        KingdeeBook kingdeeBook = kingdeeBookRepository.findByCompanyName(RequestUtils.getCompanyName());
        kingdeeSynQuery.setKingdeeBookId(kingdeeBook.getId());
        Page<KingdeeSyn> page = kingdeeSynRepository.findPage(pageable,kingdeeSynQuery);
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
    public void save(KingdeeSyn kingdeeSyn) {
        if(kingdeeSyn != null) {
            kingdeeSynRepository.save(kingdeeSyn);
        }
    }

    @Transactional
    public void save(List<KingdeeSyn> kingdeeSynList) {
        if(CollectionUtil.isNotEmpty(kingdeeSynList)) {
            kingdeeSynRepository.save(kingdeeSynList);
        }
    }

    @Transactional
    public KingdeeSynDto syn(String id){
        KingdeeSynDto kingdeeSynDto;
        KingdeeSyn kingdeeSyn = kingdeeSynRepository.findOne(id);
        kingdeeSynDto = BeanUtil.map(kingdeeSyn,KingdeeSynDto.class);
        if (kingdeeSynDto != null) {
            KingdeeBook kingdeeBook = kingdeeBookRepository.findByCompanyName(RequestUtils.getCompanyName());
            AccountKingdeeBook accountKingdeeBook = accountKingdeeBookRepository.findByAccountId(RequestUtils.getAccountId());
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                kingdeeSynDto.setKingdeeBook(kingdeeBook);
                kingdeeSynDto = kingdeeManager.save(kingdeeSynDto);
            }
        }
        return kingdeeSynDto;
    }
}
