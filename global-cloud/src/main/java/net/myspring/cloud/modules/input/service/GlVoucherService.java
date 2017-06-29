package net.myspring.cloud.modules.input.service;

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.modules.input.dto.GlVoucherDto;
import net.myspring.cloud.modules.input.dto.KingdeeSynDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.sys.domain.AccountKingdeeBook;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 凭证录入
 * Created by lihx on 2017/6/26.
 */
@Service
@KingdeeDataSource
@Transactional
public class GlVoucherService {
    @Autowired
    private KingdeeManager kingdeeManager;

    private KingdeeSynDto save(GlVoucherDto glVoucherDto, KingdeeBook kingdeeBook){
        KingdeeSynDto kingdeeSynDto = new KingdeeSynDto(
                glVoucherDto.getExtendId(),
                glVoucherDto.getExtendType(),
                KingdeeFormIdEnum.GL_VOUCHER.name(),
                glVoucherDto.getJson(),
                kingdeeBook) {
        };
        kingdeeManager.save(kingdeeSynDto);
        if (!kingdeeSynDto.getSuccess()){
            throw new ServiceException("凭证录入失败："+kingdeeSynDto.getResult());
        }
        return kingdeeSynDto;
    }

    public KingdeeSynDto save(GlVoucherDto glVoucherDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        KingdeeSynDto kingdeeSynDto;
        Boolean isLogin = kingdeeManager.login(kingdeeBook.getKingdeePostUrl(),kingdeeBook.getKingdeeDbid(),accountKingdeeBook.getUsername(),accountKingdeeBook.getPassword());
        if(isLogin) {
            kingdeeSynDto = save(glVoucherDto, kingdeeBook);
        }else{
            kingdeeSynDto = new KingdeeSynDto(false,"未登入金蝶系统");
        }
        return kingdeeSynDto;
    }

    public KingdeeSynDto saveFor(GlVoucherDto glVoucherDto, KingdeeBook kingdeeBook, AccountKingdeeBook accountKingdeeBook){
        if (glVoucherDto != null) {
            glVoucherDto.setCreator(accountKingdeeBook.getUsername());

        }
        return save(glVoucherDto,kingdeeBook,accountKingdeeBook);
    }


}
