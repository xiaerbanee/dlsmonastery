package net.myspring.future.modules.basic.manager;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.enums.ExtendTypeEnum;
import net.myspring.cloud.modules.input.dto.ArOtherRecAbleDto;
import net.myspring.cloud.modules.input.dto.ArOtherRecAbleFEntityDto;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.future.modules.basic.client.CloudClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.ClientDto;
import net.myspring.future.modules.basic.repository.ClientRepository;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 其他应收单
 * Created by lihx on 2017/6/26.
 */
@Component
public class ArOtherRecAbleManager {
    @Autowired
    private CloudClient cloudClient;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ClientRepository clientRepository;

    public KingdeeSynReturnDto synForGoodOrder(ShopGoodsDeposit shopGoodsDeposit){
        Depot depot = depotRepository.findOne(shopGoodsDeposit.getId());
        ClientDto client = clientRepository.findByDepotId(shopGoodsDeposit.getShopId());
        ArOtherRecAbleDto otherRecAbleDto = new ArOtherRecAbleDto();
        otherRecAbleDto.setExtendType(ExtendTypeEnum.货品订货.name());
        otherRecAbleDto.setExtendId(shopGoodsDeposit.getId());
        if (shopGoodsDeposit.getBillDate() != null){
            otherRecAbleDto.setDate(shopGoodsDeposit.getBillDate().toLocalDate());
        }else {
            otherRecAbleDto.setDate(LocalDate.now());
        }
        otherRecAbleDto.setCustomerNumber(client.getOutCode());
        otherRecAbleDto.setAmount(shopGoodsDeposit.getAmount());
        List<ArOtherRecAbleFEntityDto> entityDtoList = Lists.newArrayList();

        ArOtherRecAbleFEntityDto entityDto = new ArOtherRecAbleFEntityDto();
        entityDto.setAccountNumber("2241");//其他应付款
        entityDto.setCustomerForNumber(null);
        entityDto.setEmpInfoNumber("0001");//员工
        entityDto.setOtherTypeNumber("2241.00028");//其他应付款-订货会订金
        entityDto.setAmount(shopGoodsDeposit.getAmount());
        entityDto.setExpenseTypeNumber("6602.000");//无
        entityDto.setComment(depot.getName()+"-"+shopGoodsDeposit.getRemarks());
        entityDtoList.add(entityDto);
        otherRecAbleDto.setArOtherRecAbleFEntityDtoList(entityDtoList);
        return cloudClient.synOtherRecAble(otherRecAbleDto);
    }

    public KingdeeSynReturnDto synForShopGoodsDeposit(ShopGoodsDeposit shopGoodsDeposit){
        Depot depot = depotRepository.findOne(shopGoodsDeposit.getId());
        ClientDto client = clientRepository.findByDepotId(shopGoodsDeposit.getShopId());
        ArOtherRecAbleDto otherRecAbleDto = new ArOtherRecAbleDto();
        otherRecAbleDto.setExtendType(ExtendTypeEnum.定金收款.name());
        otherRecAbleDto.setExtendId(shopGoodsDeposit.getId());
        if (shopGoodsDeposit.getBillDate() != null){
            otherRecAbleDto.setDate(shopGoodsDeposit.getBillDate().toLocalDate());
        }else {
            otherRecAbleDto.setDate(LocalDate.now());
        }
        otherRecAbleDto.setCustomerNumber(client.getOutCode());
        otherRecAbleDto.setAmount(shopGoodsDeposit.getAmount());
        List<ArOtherRecAbleFEntityDto> entityDtoList = Lists.newArrayList();

        ArOtherRecAbleFEntityDto entityDto = new ArOtherRecAbleFEntityDto();
        entityDto.setAccountNumber("2241");//其他应付款
        entityDto.setCustomerForNumber(null);
        entityDto.setEmpInfoNumber("0001");//员工
        entityDto.setOtherTypeNumber("2241.00028");//其他应付款-订货会订金
        entityDto.setAmount(shopGoodsDeposit.getAmount());
        entityDto.setExpenseTypeNumber("6602.000");//无
        entityDto.setComment(depot.getName()+"-"+shopGoodsDeposit.getRemarks());
        entityDtoList.add(entityDto);
        otherRecAbleDto.setArOtherRecAbleFEntityDtoList(entityDtoList);
        return cloudClient.synOtherRecAble(otherRecAbleDto);
    }

    public KingdeeSynReturnDto synForShopDeposit(ShopDeposit shopDeposit){
        Depot depot = depotRepository.findOne(shopDeposit.getId());
        ClientDto client = clientRepository.findByDepotId(shopDeposit.getShopId());
        ArOtherRecAbleDto otherRecAbleDto = new ArOtherRecAbleDto();
        otherRecAbleDto.setDate(shopDeposit.getBillDate());
        otherRecAbleDto.setCustomerNumber(client.getOutCode());
        otherRecAbleDto.setExtendType(ExtendTypeEnum.押金列表.name());
        otherRecAbleDto.setExtendId(shopDeposit.getId());
        otherRecAbleDto.setAmount(shopDeposit.getAmount());
        List<ArOtherRecAbleFEntityDto> entityDtoList = Lists.newArrayList();

        ArOtherRecAbleFEntityDto entityDto = new ArOtherRecAbleFEntityDto();
        entityDto.setAccountNumber("2241");//其他应付款
        entityDto.setCustomerForNumber(null);
        entityDto.setEmpInfoNumber("0001");//员工
        entityDto.setOtherTypeNumber("2241.00002B");//其他应付款-客户押金（批发）-市场保证金
        entityDto.setExpenseTypeNumber("6602.000");//无
        entityDto.setAmount(shopDeposit.getAmount());
        entityDto.setComment(depot.getName()+""+shopDeposit.getRemarks());
        entityDtoList.add(entityDto);
        otherRecAbleDto.setArOtherRecAbleFEntityDtoList(entityDtoList);
        return cloudClient.synOtherRecAble(otherRecAbleDto);
    }


}
