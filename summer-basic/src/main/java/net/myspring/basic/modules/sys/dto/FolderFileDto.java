package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.modules.sys.domain.District;
import net.myspring.basic.modules.sys.domain.FolderFile;
import net.myspring.util.text.StringUtils;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderFileDto extends DataDto<FolderFile> {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtendType() {
        if(StringUtils.isNotBlank(getName())){
            return getName().substring(getName().lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    public Boolean isImage() {
        String extend = getExtendType();
        return "jpg".equals(extend) || "jpeg".equals(extend) || "gif".equals(extend) || "png".equals(extend) || "bmp".equals(extend);
    }
}
