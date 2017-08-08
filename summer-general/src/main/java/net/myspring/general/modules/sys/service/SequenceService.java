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

    public synchronized long nextVal(String seqName) {
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
