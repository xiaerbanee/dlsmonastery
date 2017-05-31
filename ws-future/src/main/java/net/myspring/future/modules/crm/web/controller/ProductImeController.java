package net.myspring.future.modules.crm.web.controller;


import net.myspring.future.common.enums.InputTypeEnum;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeHistoryDto;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.query.ProductImeQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "crm/productIme")
public class ProductImeController {

    @Autowired
    private ProductImeService productImeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page list(Pageable pageable,ProductImeQuery productImeQuery){
        Page<ProductImeDto> page = productImeService.findPage(pageable,productImeQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public ProductImeQuery getQuery(ProductImeQuery productImeQuery) {
        productImeQuery.setInputTypeList(InputTypeEnum.getList());
        return productImeQuery;
    }


    @RequestMapping(value="getProductImeDetail")
    public ProductImeDto getProductImeDetail(String id) {
        return productImeService.getProductImeDetail(id);
    }

    @RequestMapping(value="getProductImeHistoryList")
    public List<ProductImeHistoryDto> getProductImeHistoryList(String id) {
        return productImeService.getProductImeHistoryList(id);
    }

    @RequestMapping(value="findDtoListByImes")
    public List<ProductImeDto> findDtoListByImes(String imeStr) {
        return productImeService.findDtoListByImes(imeStr);
    }

    @RequestMapping(value="export")
    public String export(ProductImeQuery productImeQuery) {

        return productImeService.export(productImeQuery);
    }

    @RequestMapping(value = "search")
    public List<ProductImeDto> search(String productIme,String shopId){
        String imeReverse = StringUtils.reverse(productIme);
        List<ProductIme> productImeList =productImeService.findByImeLike(imeReverse,shopId);
        List<ProductImeDto> productImeDtoList = BeanUtil.map(productImeList,ProductImeDto.class);
        return productImeDtoList;
    }

}
