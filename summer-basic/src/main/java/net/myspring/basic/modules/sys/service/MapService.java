package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.amap.AmapUtils;
import net.myspring.basic.common.amap.Coordtransform;
import net.myspring.basic.common.amap.bo.RegeoBo;
import net.myspring.basic.common.amap.bo.RegeoPoiBo;
import net.myspring.basic.common.gmap.GmapUtils;
import net.myspring.basic.common.gmap.bo.Address_component;
import net.myspring.basic.common.gmap.bo.GeocodeResult;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by liuj on 2017-03-02.
 */
@Service
@Transactional
public class MapService {

    @Value("${weixin.app.name}")
    private String weixinAppName;

    @Value("${map.key}")
    private String mapKey;

    @Value("${amap.key}")
    private String amapKey;

    public List<String> getAddressList(String longitude, String latitude) {
        if("mktsp".equals(weixinAppName)) {
            return getAmapAddressList(longitude,latitude);
        } else {
            return getGoogleAddressList(longitude,latitude);
        }
    }

    public List<String> getAmapAddressList(String longitude, String latitude) {
        Double[] gcj02 = Coordtransform.WGS84ToGCJ02(Double.valueOf(longitude),Double.valueOf(latitude));
        RegeoBo regeoBo = AmapUtils.getRegeoBo(gcj02[0].toString(),gcj02[1].toString(), amapKey,200,"all");
        List<String> addressList= Lists.newArrayList();
        if(regeoBo != null && regeoBo.getRegeocodeBo()!= null) {
            if(CollectionUtil.isEmpty(regeoBo.getRegeocodeBo().getRegeoPoiBoList())) {
                addressList.add(regeoBo.getRegeocodeBo().getFormatted_address());
            } else {
                for(RegeoPoiBo regeoPoiBo:regeoBo.getRegeocodeBo().getRegeoPoiBoList()) {
                    addressList.add(regeoPoiBo.getName() + regeoPoiBo.getAddress());
                }
            }
        }
        return addressList;
    }

    public List<String> getGoogleAddressList(String longitude, String latitude) {
        String languge = "en";
        if("IDVIVO".equals(RequestUtils.getCompanyName())) {
            languge = "id";
        }
        GeocodeResult geocodeResult = GmapUtils.getGeocodeResult(longitude,latitude,mapKey,languge);
        List<String> addressList = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(geocodeResult.getAddress_components())) {
            for(Address_component address_component:geocodeResult.getAddress_components()) {
                if(StringUtils.isNotBlank(address_component.getFormatted_address())) {
                    addressList.add(address_component.getFormatted_address());
                }
            }
        }
        return addressList;
    }

}
