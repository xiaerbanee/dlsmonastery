package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.manager.ProductImeManager;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by liuj on 2017/4/2.
 */
@Service
public class ProductImeService {

    @Autowired
    private ProductImeManager productImeManager;

    public Page<ProductIme> findPage(Pageable pageable)  {
        TransportClient client = null;
        try {
            client = new PreBuiltXPackTransportClient(Settings.builder().put("xpack.security.user", "elastic:changeme")
                    .build()).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        GetResponse response = client.prepareGet("megacorp", "employee", "1").execute().actionGet();
        System.out.println(response.getSourceAsString());
        return null;
    }
}
