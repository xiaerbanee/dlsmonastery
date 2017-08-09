package net.myspring.general.modules.sys.service;

import net.myspring.general.common.utils.RequestUtils;
import net.myspring.general.modules.sys.domain.Sequence;
import net.myspring.general.modules.sys.repository.SequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class SequenceService {

    @Autowired
    private SequenceRepository sequenceRepository;

    private final ConcurrentHashMap<String, Object> sequenceLockMap = new ConcurrentHashMap<>();

    public long nextVal(String seqName) {
        //该方法针对不同的公司不同的seqName进行synchronized，这样可以确保不同公司，不同类型的sequence的获取互不影响
        String lockKey = RequestUtils.getCompanyName()+":"+seqName;

        if(!sequenceLockMap.containsKey(lockKey)){
            synchronized (this){
                sequenceLockMap.putIfAbsent(lockKey, new Object());
            }
        }

        synchronized (sequenceLockMap.get(lockKey)){
            Sequence sequence = sequenceRepository.findBySeqName(seqName);
            if(sequence!=null){
                sequence.setCurrentVal(sequence.getCurrentVal()+1);
                sequenceRepository.save(sequence);
                return sequence.getCurrentVal();
            }else{
                sequence = new Sequence();
                sequence.setSeqName(seqName);
                sequence.setCurrentVal(1L);
                sequenceRepository.save(sequence);

                return sequence.getCurrentVal();
            }
        }

    }
}
