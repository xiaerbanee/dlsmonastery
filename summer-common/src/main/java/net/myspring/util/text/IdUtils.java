package net.myspring.util.text;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;

import java.util.List;

public class IdUtils {
	private static Integer DEFAULT_PREIX_LENGTH = 2;

	public static String getId(String formatId) {
		return getId(formatId, DEFAULT_PREIX_LENGTH);
	}

	public static String getId(String formatId, Integer prefixLength) {
		if (StringUtils.isBlank(formatId)) {
			return null;
		} else {
			if (formatId.matches("\\d*")) {
				return formatId;
			} else {
				if (formatId.length() > prefixLength) {
					String temp = formatId.substring(prefixLength);
					if (temp.matches("\\d*")) {
						return temp;
					}
				}
			}
		}
		return null;
	}

	public static List<String> getIdList(String idStr) {
		return getIdList(idStr, DEFAULT_PREIX_LENGTH);
	}

	public static List<String> getIdList(String idStr, Integer prefixLength) {
		List<String> idList = Lists.newArrayList();
		if (StringUtils.isNotBlank(idStr)) {
			String ids = StringUtils.getReplaced(idStr);
			String[] arrId = ids.split(CharConstant.COMMA);
			for (String id : arrId) {
				if (getId(id, prefixLength) != null) {
					idList.add(getId(id, prefixLength));
				}
			}
		}
		return idList;
	}

	public static String getFormatId(String id, String prefix) {
		return getFormatId(id, prefix, "000000000000");
	}

	public static String getFormatId(String id, String prefix, String format) {
		if (id == null) {
			return "";
		} else {
			java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat(format);
			return prefix + decimalFormat.format(Long.valueOf(id));
		}
	}

}
