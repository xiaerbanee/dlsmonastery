package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeSyn;
import net.myspring.cloud.modules.sys.repository.KingdeeBookRepository;
import net.myspring.cloud.modules.sys.repository.KingdeeSynRepository;
import net.myspring.cloud.modules.sys.web.query.KingdeeSynQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 金蝶同步
 * Created by lihx on 2017/6/21.
 */
@Service
@LocalDataSource
public class KingdeeSynService {
    @Autowired
    private KingdeeSynRepository kingdeeSynRepository;
    @Autowired
    private KingdeeBookRepository kingdeeBookRepository;

    public Page<KingdeeSyn> findPage(Pageable pageable, KingdeeSynQuery kingdeeSynQuery){
        KingdeeBook kingdeeBook = kingdeeBookRepository.findByCompanyId(kingdeeSynQuery.getCompanyId());
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
}
