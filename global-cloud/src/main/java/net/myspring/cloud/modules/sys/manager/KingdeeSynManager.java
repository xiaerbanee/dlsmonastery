package net.myspring.cloud.modules.sys.manager;


import net.myspring.cloud.modules.sys.domain.KingdeeSyn;
import net.myspring.cloud.modules.sys.repository.KingdeeSynRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by lihx on 2017/6/21.
 */
@Component
public class KingdeeSynManager {
    @Autowired
    private KingdeeSynRepository kingdeeSynRepository;

    public KingdeeSyn save(KingdeeSyn kingdeeSyn){
       return kingdeeSynRepository.save(kingdeeSyn);
    }

}
