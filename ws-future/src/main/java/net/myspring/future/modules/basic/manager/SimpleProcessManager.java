package net.myspring.future.modules.basic.manager;

import net.myspring.future.common.enums.SimpleProcessTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.SimpleProcess;
import net.myspring.future.modules.basic.domain.SimpleProcessDetail;
import net.myspring.future.modules.basic.dto.SimpleProcessDetailDto;
import net.myspring.future.modules.basic.repository.SimpleProcessDetailRepository;
import net.myspring.future.modules.basic.repository.SimpleProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleProcessManager {

    @Autowired
    private SimpleProcessDetailRepository simpleProcessDetailRepository;
    @Autowired
    private SimpleProcessRepository simpleProcessRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public SimpleProcess go(String simpleProcessId, boolean pass, String remarks) {
        SimpleProcess simpleProcess = simpleProcessRepository.findOne(simpleProcessId);

        SimpleProcessDetail simpleProcessDetail = new SimpleProcessDetail();
        simpleProcessDetail.setSimpleProcessId(simpleProcessId);
        simpleProcessDetail.setProcessStatus(simpleProcess.getCurrentProcessStatus());
        simpleProcessDetail.setOpinion(pass?"同意":"不同意");
        simpleProcessDetail.setRemarks(remarks);
        simpleProcessDetailRepository.save(simpleProcessDetail);

        simpleProcess.setCurrentProcessStatus(SimpleProcessTypeEnum.getNextProcessStatus(simpleProcess.getType(), simpleProcess.getCurrentProcessStatus(), pass));
        simpleProcessRepository.save(simpleProcess);

        return simpleProcess;
    }

    public SimpleProcess start(SimpleProcessTypeEnum simpleProcessTypeEnum) {
        SimpleProcess simpleProcess = new SimpleProcess();
        simpleProcess.setType(simpleProcessTypeEnum.name());
        simpleProcess.setCurrentProcessStatus(simpleProcessTypeEnum.getFirstProcessStatus());
        simpleProcess.setRemarks("");
        simpleProcessRepository.save(simpleProcess);

        return simpleProcess;
    }

    public List<SimpleProcessDetailDto> findDetailDtoList(String simpleProcessId){
        List<SimpleProcessDetailDto> result = simpleProcessDetailRepository.findDtoList(simpleProcessId);
        cacheUtils.initCacheInput(result);
        return result;
    }

}
