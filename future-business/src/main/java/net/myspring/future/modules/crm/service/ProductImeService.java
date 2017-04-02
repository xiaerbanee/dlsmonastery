package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.manager.ProductImeManager;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by liuj on 2017/4/2.
 */
@Service
public class ProductImeService {

        @Autowired
        private ProductImeManager productImeManager;

        public Page<ProductIme> findPage(Pageable pageable) {
            return productImeManager.findPage(pageable);
        }
}
