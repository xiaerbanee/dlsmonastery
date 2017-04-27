package net.myspring.basic.modules.hr.client;

import net.myspring.general.modules.sys.dto.ProcessTypeDto;
import net.myspring.general.modules.sys.form.ExcelExportForm;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by wangzm on 2017/4/27.
 */
@FeignClient("summer-general")
public interface ExcelExportClient {

    @RequestMapping(value = "/sys/folderFile/uploadToMongoDb",method = RequestMethod.POST)
    String uploadToMongoDb(@RequestBody ExcelExportForm excelExportForm);
}
