package net.myspring.util.excel;

import net.myspring.util.text.Encodes;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ExcelView extends AbstractView {

	private Boolean office2003=false;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleExcelBook simpleExcelBook = (SimpleExcelBook) model.get("simpleExcelBook");
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(simpleExcelBook.getName()));
		simpleExcelBook.getWorkbook().write(response.getOutputStream());
	}
}
