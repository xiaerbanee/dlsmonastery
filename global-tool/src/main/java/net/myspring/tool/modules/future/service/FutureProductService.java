package net.myspring.tool.modules.future.service;

import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.modules.future.dto.ProductDto;
import net.myspring.tool.modules.future.repository.FutureProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FutureDataSource
@Transactional(readOnly = true)
public class FutureProductService {
    @Autowired
    private FutureProductRepository futureProductRepository;

    public List<ProductDto> findHasImeProduct(){
        return futureProductRepository.findHasImeProduct();
    }
}
