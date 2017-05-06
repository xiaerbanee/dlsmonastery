package net.myspring.future.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.text.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class IdUtils {

	private static Map<String,Long> businessIdMap = Maps.newConcurrentMap();

	private static Integer DEFAULT_PREIX_LENGTH = 2;

	public static List<List<String>> splitList(List<String> lists,int limit){
		int size=lists.size();
		List<List<String>> list= Lists.newArrayList();
		if(limit>size){
			list.add(lists);
			return list;
		}
		int result=0;
		for(int i=0;i<size;i=i+limit){
			result=i+limit;
			if(result>size){
				result=size;
			}
			list.add(lists.subList(i, result));
		}
		return list;
	}

	public static String getFormatId(Long id, String prefix) {
		return prefix+id;
	}

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

	public static String getNextBusinessId(Long maxBusinessId) {
		if(maxBusinessId==null) {
			String businessId = LocalDateUtils.format(LocalDate.now()) + "00000";
			maxBusinessId = Long.valueOf(businessId);
		}
		Long nextBusinessId = maxBusinessId+1;
		return nextBusinessId.toString();
	}


}
