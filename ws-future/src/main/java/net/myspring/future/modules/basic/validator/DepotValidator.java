package net.myspring.future.modules.basic.validator;

import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.layout.domain.ShopAttribute;
import net.myspring.util.text.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */
@Component
public class DepotValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepotForm depot = (DepotForm) target;
        StringBuffer sb = new StringBuffer();
        List<ShopAttribute> shopAttributeList = depot.getShopAttributeList();
        for(ShopAttribute shopAttribute:shopAttributeList){
            String typeName = shopAttribute.getTypeName();
            Double typeValue = shopAttribute.getTypeValue();
            if(typeValue<0){
               sb.append(typeName+"值不能为负数. \n");
            }
        }
        if(StringUtils.isNotBlank(sb.toString())) {
            errors.rejectValue("shopAttributeList", "error.shopAttributeList",  sb.toString());
        }
    }
}
