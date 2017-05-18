package net.myspring.util.text;

import com.google.common.collect.Lists;
import net.myspring.util.time.LocalDateUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springside.modules.utils.text.EncodeUtil;

import java.time.LocalDate;
import java.util.List;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static <E> List<E> getSplitList(String str, String splitter) {
		List<E> list = Lists.newArrayList();
		if (isNotBlank(str)) {
			String[] arr = str.split(splitter);
			for (String item : arr) {
				if (isNotBlank(item) && !list.contains(item.trim())) {
					E value = (E) item.trim();
					list.add(value);
				}
			}
		}
		return list;
	}

	public static String getReplaced(String str) {
		List<String> beforeList = Lists.newArrayList();
		beforeList.add("，");
		beforeList.add(" ");
		beforeList.add("　");
		beforeList.add("\n");
		beforeList.add("	");
		return getReplaced(str, beforeList, ",");
	}

	public static String getReplaced(String str, List<String> beforeList, String after) {
		String result = trim(str);
		if (StringUtils.isNotBlank(result)) {
			for (String before : beforeList) {
				result = StringUtils.replace(result, before, after);
			}
		}
		return result;
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

	public static String getEncryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(8);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, 1024);
		return EncodeUtil.encodeHex(salt) + EncodeUtil.encodeHex(hashPassword);
	}

	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = EncodeUtil.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, 1024);
        String pwd = EncodeUtil.encodeHex(salt) + EncodeUtil.encodeHex(hashPassword);
        return password.equalsIgnoreCase(pwd);
	}

	public static  String toString(Object object) {
		if(object == null) {
			return "";
		} else {
			return String.valueOf(object);
		}
	}

	public static String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	public static String getNextBusinessId(String maxBusinessId) {
		if(maxBusinessId==null) {
			String businessId = LocalDateUtils.formatLocalDate(LocalDate.now(),"yyMMdd") + "00000";
			maxBusinessId = String.valueOf(businessId);
		}
		return String.valueOf(Long.valueOf(maxBusinessId)+1);
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

}
