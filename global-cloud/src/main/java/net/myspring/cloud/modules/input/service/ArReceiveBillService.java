package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.modules.input.dto.ArReceiveBillDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收款单
 * Created by lihx on 2017/6/24.
 */
@Service
@KingdeeDataSource
public class ArReceiveBillService {
    @Autowired
    private KingdeeManager kingdeeManager;

    private KingdeeSynDto save(ArReceiveBillDto arReceiveBillDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                arReceiveBillDto.getExtendId(),
                arReceiveBillDto.getExtendType(),
                KingdeeFormIdEnum.AR_ReceiveBill.name(),
                arReceiveBillDto.getJson(),
                kingdeeBook) {
        };
        kingdeeManager.save(kingdeeSynDto);
        return kingdeeSynDto;
    }

    public List<KingdeeSynDto> save(List<ArReceiveBillDto> arReceiveBillDtoList, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        List<KingdeeSynDto> kingdeeSynDtoList = Lists.newArrayList();
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            if (CollectionUtil.isNotEmpty(arReceiveBillDtoList)) {
                for (ArReceiveBillDto arReceiveBillDto : arReceiveBillDtoList) {
                    KingdeeSynDto kingdeeSynDto = save(arReceiveBillDto,kingdeeBook);
                    kingdeeSynDtoList.add(kingdeeSynDto);
                }
            }
        }else{
            kingdeeSynDtoList.add(new KingdeeSynDto(false,"未登入金蝶系统"));
        }
        return kingdeeSynDtoList;
    }
}
