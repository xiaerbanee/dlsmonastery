package net.myspring.future.modules.basic.manager;

import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.SimpleProcessEndsEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.SimpleProcess;
import net.myspring.future.modules.basic.domain.SimpleProcessDetail;
import net.myspring.future.modules.basic.domain.SimpleProcessStep;
import net.myspring.future.modules.basic.domain.SimpleProcessType;
import net.myspring.future.modules.basic.dto.SimpleProcessDetailDto;
import net.myspring.future.modules.basic.repository.SimpleProcessDetailRepository;
import net.myspring.future.modules.basic.repository.SimpleProcessRepository;
import net.myspring.future.modules.basic.repository.SimpleProcessStepRepository;
import net.myspring.future.modules.basic.repository.SimpleProcessTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SimpleProcessManager {

    @Autowired
    private SimpleProcessDetailRepository simpleProcessDetailRepository;
    @Autowired
    private SimpleProcessRepository simpleProcessRepository;
    @Autowired
    private SimpleProcessStepRepository simpleProcessStepRepository;
    @Autowired
    private SimpleProcessTypeRepository simpleProcessTypeRepository;
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

        Map<String, String> nextProcessStatusInfo = getNextProcessStatusInfo(simpleProcess.getSimpleProcessTypeId(), simpleProcess.getCurrentProcessStatus(), pass);
        simpleProcess.setCurrentProcessStatus(nextProcessStatusInfo.get("nextProcessStatus"));
        simpleProcess.setCurrentPositionId(nextProcessStatusInfo.get("nextPositionId"));
        simpleProcessRepository.save(simpleProcess);

        return simpleProcess;
    }

    private Map<String, String> getNextProcessStatusInfo(String simpleProcessTypeId, String currentProcessStatus, boolean pass){
        Map<String, String> result = new HashMap<>();
        if(!pass){
            result.put("nextProcessStatus", SimpleProcessEndsEnum.未通过.name());
            result.put("nextPositionId", null);
            return result;
        }

        List<SimpleProcessStep> simpleProcessStepList = simpleProcessStepRepository.findByEnabledIsTrueAndSimpleProcessTypeIdOrderBySortAsc(simpleProcessTypeId);
        for(int i = 0; i< simpleProcessStepList.size(); i++){
            if(simpleProcessStepList.get(i).getStep().equals(currentProcessStatus)){
                if(i == simpleProcessStepList.size()-1){
                    result.put("nextProcessStatus", SimpleProcessEndsEnum.已通过.name());
                    result.put("nextPositionId", null);
                    return result;
                }else{
                    result.put("nextProcessStatus",  simpleProcessStepList.get(i+1).getStep());
                    result.put("nextPositionId",  simpleProcessStepList.get(i+1).getPositionId());
                    return result;
                }
            }
        }
        throw new ServiceException("无法找到下一步流程状态");
    }

    public SimpleProcess start(String simpleProcessTypeName) {
        SimpleProcess simpleProcess = new SimpleProcess();
        SimpleProcessType simpleProcessType = simpleProcessTypeRepository.findByEnabledIsTrueAndName(simpleProcessTypeName);
        simpleProcess.setSimpleProcessTypeId(simpleProcessType.getId());

        SimpleProcessStep firstSimpleProcessStep = simpleProcessStepRepository.findTopByEnabledIsTrueAndSimpleProcessTypeIdOrderBySortAsc(simpleProcessType.getId());
        simpleProcess.setCurrentProcessStatus(firstSimpleProcessStep.getStep());
        simpleProcess.setCurrentPositionId(firstSimpleProcessStep.getPositionId());
        simpleProcess.setRemarks("");
        simpleProcessRepository.save(simpleProcess);

        return simpleProcess;
    }

    public List<SimpleProcessDetailDto> findDetailDtoList(String simpleProcessId){
        List<SimpleProcessDetailDto> result = simpleProcessDetailRepository.findDtoList(simpleProcessId);
        cacheUtils.initCacheInput(result);
        return result;
    }

    public List<String> getAllProcessStatuses(String simpleProcessTypeName){
        SimpleProcessType simpleProcessType = simpleProcessTypeRepository.findByEnabledIsTrueAndName(simpleProcessTypeName);

        List<SimpleProcessStep> simpleProcessStepList = simpleProcessStepRepository.findByEnabledIsTrueAndSimpleProcessTypeIdOrderBySortAsc(simpleProcessType.getId());

        List<String> allProcessStatuses = new ArrayList<>();
        for(SimpleProcessStep each : simpleProcessStepList){
            allProcessStatuses.add(each.getStep());
        }
        allProcessStatuses.add(SimpleProcessEndsEnum.已通过.name());
        allProcessStatuses.add(SimpleProcessEndsEnum.未通过.name());

        return allProcessStatuses;
    }
}
