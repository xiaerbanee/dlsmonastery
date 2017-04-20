package net.myspring.future.modules.crm.service;

import net.myspring.future.modules.crm.domain.ImeAllot;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.mapper.ImeAllotMapper;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ImeAllotService {

    @Autowired
    private ImeAllotMapper imeAllotMapper;
    @Autowired
    private ProductImeMapper productImeMapper;


    @Transactional
    public void save(ImeAllot imeAllot) {
        List<String> imeList = StringUtils.getSplitList(imeAllot.getImeStr(), "");
        List<ProductIme> productImeList = productImeMapper.findByImeList(imeList);
        for(ProductIme productIme:productImeList) {
            if(!productIme.getDepotId().equals(imeAllot.getToDepotId())) {
                ImeAllot allot = new ImeAllot();
                allot.setProductImeId(productIme.getId());
                allot.setFromDepotId(productIme.getDepotId());
                allot.setToDepotId(imeAllot.getToDepotId());
                allot.setRemarks(imeAllot.getRemarks());
                allot.setCrossArea(crossArea(imeAllot.getToDepotId(),productIme.getDepotId()));
                imeAllotMapper.save(allot);
                productIme.setDepotId(imeAllot.getToDepotId());
                productImeMapper.update(productIme);
            }
        }
    }

    public void logicDeleteOne(String id) {
        imeAllotMapper.logicDeleteOne(id);
    }

    public ImeAllot findOne(String id) {
        ImeAllot imeAllot = imeAllotMapper.findOne(id);
        return imeAllot;
    }


    public List<ImeAllot> findByProductImeId(String productImeId){
        return imeAllotMapper.findByProductImeId(productImeId);
    }

    public Page<ImeAllot> findPage(Pageable pageable, Map<String, Object> map) {
        Page<ImeAllot> page = imeAllotMapper.findPage(pageable,map);
        return page;
    }

    private boolean crossArea(String toDepotId,String productImeDepotId){
        return true;
    }

}
