package net.myspring.cloud.modules.input.service;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.KingdeeFormIdEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import net.myspring.cloud.modules.input.dto.KingdeeSynExtendDto;
import net.myspring.cloud.modules.input.dto.SalOutStockDto;
import net.myspring.cloud.modules.input.manager.KingdeeManager;
import net.myspring.cloud.modules.input.web.form.BatchBillForm;
import net.myspring.cloud.modules.kingdee.mapper.ArReceivableMapper;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import net.myspring.cloud.modules.sys.mapper.KingdeeBookMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by liuj on 2017/5/11.
 */
public class SalOutStockService {
    @Autowired
    private KingdeeManager kingdeeManager;
    @Autowired
    private ArReceivableMapper arReceivableMapper;
    @Autowired
    private KingdeeBookMapper kingdeeBookMapper;

    public KingdeeSynExtendDto save(SalOutStockDto salOutStockDto) {
        KingdeeBook kingdeeBook = kingdeeBookMapper.findByCompanyId(RequestUtils.getCompanyId());
        KingdeeSynExtendDto kingdeeSynExtendDto = new KingdeeSynExtendDto(
                KingdeeFormIdEnum.SAL_OUTSTOCK.name(),
                salOutStockDto.getJson(),
                kingdeeBook,
                KingdeeFormIdEnum.AR_receivable.name()) {
            @Override
            public String getNextBillNo() {
                return arReceivableMapper.findBySourceBillNo(getBillNo()).get(0).getFBILLNO();
            }
        };
        kingdeeManager.save(kingdeeSynExtendDto);
        return kingdeeSynExtendDto;
    }


    public List<KingdeeSynExtendDto> save(BatchBillForm batchBillForm) {
            List<SalOutStockDto> salOutStockDtoList = Lists.newArrayList();
            return null;
    }





}
