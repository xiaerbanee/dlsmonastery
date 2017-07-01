package net.myspring.tool.common.client;

import net.myspring.tool.common.domain.ProductEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by guolm on 2017/6/11.
 */

@FeignClient("ws-future")
public interface ProductClient {

    @RequestMapping(method = RequestMethod.GET, value = "basic/product/findHasImeProduct")
    List<ProductEntity> findHasImeProduct();
}
