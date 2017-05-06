package net.myspring.cloud.modules.remote.client;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by lihx on 2017/4/25.
 */
@FeignClient("summer-basic")
public interface AccountClient {

}
