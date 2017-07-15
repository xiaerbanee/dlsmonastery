package net.myspring.util.file;

import net.myspring.common.exception.ServiceException;
import net.myspring.util.text.Encodes;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public class TempFileView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File tempFileDir = new File(System.getProperty("java.io.tmpdir"), "poifiles");

		File tempFile = new File(tempFileDir.getAbsolutePath(), (String) model.get("tempFileName"));
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(tempFile.getName()));

		try (FileInputStream in = new FileInputStream(tempFile)){
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len = in.read(buffer)) > 0) {
				response.getOutputStream().write(buffer,0,len);
			}
		}catch(Exception e) {
			throw new ServiceException(e);
		}finally {
            tempFile.delete();
        }
    }
}
