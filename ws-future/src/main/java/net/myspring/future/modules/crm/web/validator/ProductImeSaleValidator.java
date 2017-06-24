package net.myspring.future.modules.crm.web.validator;

import net.myspring.common.constant.CharConstant;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.dto.ProductImeSearchResultDto;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.form.ProductImeSaleForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;


/**
 * Created by admin on 2016/12/27.
 */
@Component
public class ProductImeSaleValidator implements Validator {

    @Autowired
    private ProductImeService productImeService;
    @Autowired
    private DepotManager depotManager;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductImeSaleForm productImeSaleForm = (ProductImeSaleForm)target;
        StringBuilder sb = new StringBuilder();
        if(StringUtils.isBlank(productImeSaleForm.getImeStr())) {
            sb.append("请输入串码.");
        }else{
            List<String> imeList = StringUtils.getSplitList(productImeSaleForm.getImeStr(), CharConstant.ENTER);
            ProductImeSearchResultDto productImeSearchResultDto = productImeService.findProductImeSearchResult(imeList);
            if(CollectionUtil.isNotEmpty(productImeSearchResultDto.getNotExists())) {
                for(String ime:productImeSearchResultDto.getNotExists()) {
                    sb.append(ime + "不存在.\n");
                }
            }
            if(CollectionUtil.isNotEmpty(productImeSearchResultDto.getProductImeList())) {
                for(ProductIme productIme:productImeSearchResultDto.getProductImeList()){
                    if(productIme.getProductImeSaleId()!=null) {
                        sb.append("串码"+productIme.getIme()+"已核销.");
                    } else if(!depotManager.isAccess(productIme.getDepotId(),true, RequestUtils.getAccountId(),RequestUtils.getOfficeId())) {
                        sb.append("您没有串码"+productIme.getIme()+"所在门店的核销权限.");
                    }else if(productIme.getProductImeUploadId() != null) {
                        sb.append("串码"+productIme.getIme()+"已上报.");
                    }
                }
            }
        }
        if(StringUtils.isNotBlank(sb.toString())) {
            errors.rejectValue("ime", "error.ime", sb.toString());
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shopId", "error.shopId", "请选择门店");

    }
}
