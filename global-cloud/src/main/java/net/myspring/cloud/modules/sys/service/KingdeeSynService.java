package net.myspring.cloud.modules.sys.service;

import com.sun.org.apache.regexp.internal.RE;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeSyn;
import net.myspring.cloud.modules.sys.repository.AccountKingdeeBookRepository;
import net.myspring.cloud.modules.sys.repository.KingdeeBookRepository;
import net.myspring.cloud.modules.sys.repository.KingdeeSynRepository;
import net.myspring.cloud.modules.sys.web.query.KingdeeSynQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 金蝶同步
 * Created by lihx on 2017/6/21.
 */
@Service
@LocalDataSource
@Transactional
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
        KingdeeBook kingdeeBook = kingdeeBookRepository.findByCompanyId(RequestUtils.getCompanyId());
        kingdeeSynQuery.setKingdeeBookId(kingdeeBook.getId());
        Page<KingdeeSyn> page = kingdeeSynRepository.findPage(pageable,kingdeeSynQuery);
        return page;
    }

    public void logicDelete(String id) {
        kingdeeSynRepository.logicDelete(id);
    }

    public KingdeeSyn findOne(String id){
        return kingdeeSynRepository.findOne(id);
    }

    public KingdeeSynDto syn(String id){
        KingdeeSynDto kingdeeSynDto;
        KingdeeSyn kingdeeSyn = kingdeeSynRepository.findOne(id);
        kingdeeSynDto = BeanUtil.map(kingdeeSyn,KingdeeSynDto.class);
        if (kingdeeSynDto != null) {
            KingdeeBook kingdeeBook = kingdeeBookRepository.findByCompanyId(RequestUtils.getCompanyId());
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
