package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.modules.sys.domain.FolderFile;
import net.myspring.util.text.StringUtils;

/**
 * Created by admin on 2017/4/5.
 */
public class FolderFileDto extends FolderFile {

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
