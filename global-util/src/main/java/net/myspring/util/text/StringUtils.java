package net.myspring.util.text;

import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

public class StringUtils extends org.apache.commons.lang3.StringUtils{

	public static String getReplaced(String str, List<String> beforeList, String after) {
		String result = trim(str);
		if (StringUtils.isNotBlank(result)) {
			for (String before : beforeList) {
				result = StringUtils.replace(result, before, after);
			}
		}
		return result;
	}

	public static String getNumberStr(String str) {
		if(isBlank(str)) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<str.length();i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					sb.append(str.charAt(i));
				}
			}
			return sb.toString();
		}
	}

	public static String getNextBusinessId(String maxBusinessId) {
		if(maxBusinessId==null) {
			String businessId = LocalDateUtils.format(LocalDate.now(),"yyMMdd") + "00000";
			maxBusinessId = String.valueOf(businessId);
		}
		return String.valueOf(Long.valueOf(maxBusinessId)+1);
	}
}
