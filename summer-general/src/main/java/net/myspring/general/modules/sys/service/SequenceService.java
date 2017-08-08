package net.myspring.general.modules.sys.service;

import net.myspring.general.modules.sys.domain.Sequence;
import net.myspring.general.modules.sys.repository.SequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SequenceService {

    @Autowired
    private SequenceRepository sequenceRepository;

    public synchronized Long nextVal(String key) {
        Sequence sequence = sequenceRepository.findByKey(key);
        if(sequence!=null){
            sequence.setCurrentVal(sequence.getCurrentVal()+1);
            sequenceRepository.save(sequence);
            return sequence.getCurrentVal();
        }else{
            sequence = new Sequence();
            sequence.setKey(key);
            sequence.setCurrentVal(1L);
            sequenceRepository.save(sequence);

            return sequence.getCurrentVal();
        }
    }
}
