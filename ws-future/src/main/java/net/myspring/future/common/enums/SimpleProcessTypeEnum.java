package net.myspring.future.common.enums;

import net.myspring.common.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

public enum SimpleProcessTypeEnum {
    销售收款(BankInProcessStepEnum.values());

    private Enum[] processStepEnums;
    private List<String> allProcessStatuses;

    SimpleProcessTypeEnum(Enum... processStepEnums){
        if(processStepEnums == null || processStepEnums.length == 0){
            throw new ServiceException("非法的流程实例");
        }
        this.processStepEnums = processStepEnums;

        allProcessStatuses = new ArrayList<>();
        for(Enum each : processStepEnums){
            allProcessStatuses.add(each.name());
        }
        allProcessStatuses.add(SimpleProcessEndsEnum.已通过.name());
        allProcessStatuses.add(SimpleProcessEndsEnum.未通过.name());

    }

    public List<String> getAllProcessStatuses(){
       return allProcessStatuses;
    }

    public String getNextProcessStatus(String currentProcessStatus, boolean pass){
        if(!pass){
            return SimpleProcessEndsEnum.未通过.name();
        }

        for(int i = 0; i< processStepEnums.length; i++){
            if(processStepEnums[i].name().equals(currentProcessStatus)){
                if(i == processStepEnums.length-1){
                    return SimpleProcessEndsEnum.已通过.name();
                }else{
                    return processStepEnums[i+1].name();
                }
            }
        }
        throw new ServiceException("无法找到下一步流程状态");
    }

    public String getFirstProcessStatus(){
        return processStepEnums[0].name();
    }

    public static String getNextProcessStatus(String type, String currentProcessStatus, boolean pass){
        for(SimpleProcessTypeEnum each : SimpleProcessTypeEnum.values()){
            if(each.name().equals(type)){
                return each.getNextProcessStatus(currentProcessStatus, pass);
            }
        }
        throw new ServiceException("无法找到下一步流程状态");
    }
}
