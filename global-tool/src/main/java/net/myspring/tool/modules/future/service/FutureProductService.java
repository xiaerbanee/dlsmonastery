package net.myspring.tool.modules.future.service;

import net.myspring.tool.common.dataSource.annotation.FutureDataSource;
import net.myspring.tool.modules.future.dto.ProductDto;
import net.myspring.tool.modules.future.repository.FutureProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@FutureDataSource
@Service
public class FutureProductService {
    @Autowired
    private FutureProductRepository futureProductRepository;

    public List<ProductDto> findHasImeProduct(){
        return futureProductRepository.findHasImeProduct();
    }
}
