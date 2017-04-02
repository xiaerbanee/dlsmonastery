package net.myspring.future.modules.crm.manager;

import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * Created by liuj on 2017/4/2.
 */
@Component
public class ProductImeManager {
    @Autowired
    private ProductImeMapper productImeMapper;

    public Page<ProductIme> findPage(Pageable pageable) {
        return productImeMapper.findPage(pageable);
    }

}
