package net.myspring.cloud.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.utils.HandsontableUtils;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.sys.domain.Voucher;
import net.myspring.cloud.modules.sys.dto.VoucherDto;
import net.myspring.cloud.modules.sys.repository.VoucherRepository;
import net.myspring.cloud.modules.sys.web.form.VoucherForm;
import net.myspring.cloud.modules.sys.web.query.VoucherQuery;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 凭证
 * Created by lihx on 2017/4/5.
 */
@Service
@LocalDataSource
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public Page<VoucherDto> findPage(Pageable pageable, VoucherQuery voucherQuery) {
        Page<VoucherDto> page = voucherRepository.findPage(pageable, voucherQuery);
        return page;
    }

    public Map<String, Object> getForm(Map<String, Object> map) {
        return map;
    }

    public VoucherDto findOne(String id) {
        VoucherDto voucherDto;
        if (StringUtils.isBlank(id)) {
            voucherDto = new VoucherDto();
        } else {
            Voucher voucher = voucherRepository.findOne(id);
            voucherDto = BeanUtil.map(voucher,VoucherDto.class);
        }
        return voucherDto;
    }

    public RestResponse save(VoucherForm voucherForm){
        LocalDate date = voucherForm.getBillDate();
        String json = HtmlUtils.htmlUnescape(voucherForm.getJson());
        List<List<Object>> data = ObjectMapperUtils.readValue(json, ArrayList.class);
        RestResponse message = check(data);
        if (StringUtils.isNotBlank(message.getMessage())) {
            return message;
        }else{
//            k3cloudGlVoucher.setFdate(LocalDate.parse(billDate, DateTimeFormatter.ofPattern(DateFormat.DATE.getValue())));
//            k3cloudGlVoucherService.save(datas,k3cloudGlVoucher,getGlVoucher());
             message.setMessage("凭证保存成功");
            return message;
        }
    }

    public RestResponse check(List<List<Object>> datas) {
        RestResponse message;
        return null;
    }
}
