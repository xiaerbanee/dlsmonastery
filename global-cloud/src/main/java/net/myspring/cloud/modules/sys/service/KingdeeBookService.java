package net.myspring.cloud.modules.sys.service;

import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
public class KingdeeBookService {
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;

    public List<KingdeeBook> findAll(){
        return kingdeeBookMapper.findAll();
    }


}
