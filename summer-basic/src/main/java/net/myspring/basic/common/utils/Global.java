package net.myspring.basic.common.utils;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.SummerBasicApplication;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.manager.DictEnumManager;
import net.myspring.basic.modules.sys.mapper.DictEnumMapper;
import net.myspring.basic.modules.sys.mapper.DictMapMapper;
import net.myspring.basic.modules.sys.service.DistrictService;
import net.myspring.util.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Global {

	private static Logger logger = LoggerFactory.getLogger(Global.class);
	private static DictEnumMapper dictEnumMapper = SummerBasicApplication.getBean(DictEnumMapper.class);
	private static DictMapMapper dictMapMapper = SummerBasicApplication.getBean(DictMapMapper.class);

	private static Map<String,List<DictEnum>> dictEnumMap=Maps.newHashMap();
    private static Map<String,List<DictMap>> dictMap=Maps.newHashMap();

    static {
        dictEnumMap=getDictEnumMap();
        dictMap=getDictMap();
    }

	public static List<String> getDictEnumValueList(String category) {
		List<DictEnum> dictEnumList=dictEnumMap.get(category);
		return CollectionUtil.extractToList(dictEnumList,"value");
	}

	public static Map<String,List<DictEnum>> getDictEnumMap() {
		if(CollectionUtil.isEmpty(dictEnumMap)){
			List<DictEnum> dictEnumList=dictEnumMapper.findAllEnabled();
            dictEnumMap = CollectionUtil.extractToMapList(dictEnumList, "category");
        }
		return dictEnumMap;
	}

	public static HashBiMap<String,String> getDictMapList(String category) {
		List<DictMap> dictMaps = dictMap.get(category);
		HashBiMap<String,String> map = HashBiMap.create();
		for (DictMap dictMap : dictMaps) {
			map.put(dictMap.getValue(),dictMap.getName());
		}
		return map;
	}
	public static Map<String,List<DictMap>> getDictMap() {
        if(CollectionUtil.isEmpty(dictMap)){
            List<DictMap> dictMapList=dictMapMapper.findAllEnabled();
            dictMap = CollectionUtil.extractToMapList(dictMapList, "category");
        }
		return dictMap;
	}
}
