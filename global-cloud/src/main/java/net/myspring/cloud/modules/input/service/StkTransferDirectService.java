package net.myspring.cloud.modules.input.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.dto.StkTransferDirectDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 直接调拨单
 * Created by lihx on 2017/6/23.
 */
@Service
@KingdeeDataSource
@Transactional(readOnly = true)
public class StkTransferDirectService {
    @Autowired
    private KingdeeManager kingdeeManager;

    private KingdeeSynDto save(StkTransferDirectDto stkTransferDirectDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                stkTransferDirectDto.getExtendId(),
                stkTransferDirectDto.getExtendType(),
                KingdeeFormIdEnum.STK_TransferDirect.name(),
                stkTransferDirectDto.getJson(),
                kingdeeBook);
        kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("直接调拨单失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    @Transactional
    public KingdeeSynDto save(StkTransferDirectDto stkTransferDirectDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto = null;
        if (stkTransferDirectDto != null) {
            Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
            if(isLogin) {
                kingdeeSynDto = save(stkTransferDirectDto, kingdeeBook);
            }else{
                kingdeeSynDto = new KingdeeSynDto(false,"未登入金蝶系统");
            }
        }
        return kingdeeSynDto;
    }

    @Transactional
    public KingdeeSynDto saveForWS(StkTransferDirectDto stkTransferDirectDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        stkTransferDirectDto.setCreator(accountKingdeeBook.getUsername());
        return save(stkTransferDirectDto,kingdeeBook,accountKingdeeBook);
    }
}
