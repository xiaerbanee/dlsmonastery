package net.myspring.future.modules.basic.web.validator;

import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotShopForm;
import net.myspring.future.modules.layout.domain.ShopAttribute;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Created by admin on 2016/12/26.
 */
@Component
public class DepotShopValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        DepotShopForm depot = (DepotShopForm) target;
    }
}
