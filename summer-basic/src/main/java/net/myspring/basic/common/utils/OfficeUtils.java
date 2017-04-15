package net.myspring.basic.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.SummerBasicApplication;
import net.myspring.basic.common.enums.DictMapCategoryEnum;
import net.myspring.basic.modules.hr.domain.Office;
import net.myspring.basic.modules.hr.manager.OfficeManager;
import net.myspring.basic.modules.hr.mapper.OfficeMapper;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.mapper.DictMapMapper;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/1/8.
 */
public class OfficeUtils {

    //根据officeId获取某个级别的Office
    public static String getOfficeIdByOfficeType(String officeId, String officeType) {
        OfficeManager officeManager= SummerBasicApplication.getBean(OfficeManager.class);
        Office office = officeManager.findOne(officeId);
        if(office!=null){
            if (officeType.equals(office.getType()) || office.getType().equals("0")) {
                return officeId;
            } else {
                Office parent = officeManager.findOne(office.getParentId());
                for (int i = 1; i < 10; i++) {
                    if (parent != null) {
                        if (officeType.equals(parent.getType())) {
                            return parent.getId();
                        } else {
                            parent = officeManager.findOne(parent.getParentId());
                        }
                    }
                }
            }
            return office.getParentId() == null ? officeId : office.getParentId();
        }
        return null;
    }

    //获取当前officeId和顶级office的级别差，用于拼接机构报表sql
    public static Integer getOfficeLevelDiff(String parentOfficeId, List<String> officeIds) {
        OfficeManager officeManager= SummerBasicApplication.getBean(OfficeManager.class);
        Integer maxOfficeType = 0;
        Integer minOfficeType = 0;
        int diff = 0;
        if(CollectionUtil.isEmpty(officeIds)){
            Map<Integer,String> dictMap=getOfficeDictMap();
            List<Integer> keyList = Lists.newArrayList(dictMap.keySet());
            if (StringUtils.isBlank(parentOfficeId)) {
                minOfficeType = keyList.get(0);
            } else {
                minOfficeType = Integer.valueOf(officeManager.findOne(parentOfficeId).getType());
            }
            maxOfficeType=keyList.get(keyList.size()-1);
            Integer maxIndex = keyList.indexOf(maxOfficeType);
            Integer minIndex = keyList.indexOf(minOfficeType);
            diff = maxIndex - minIndex;
        }else {
            Map<Integer, List<String>> typeMap = getOfficeLevelMap(officeIds);
            if (CollectionUtil.isNotEmpty(typeMap)) {
                List<Integer> keyList = Lists.newArrayList(typeMap.keySet());
                if (StringUtils.isBlank(parentOfficeId)) {
                    minOfficeType = keyList.get(0);
                } else {
                    minOfficeType = Integer.valueOf(officeManager.findOne(parentOfficeId).getType());
                }
                maxOfficeType = keyList.get(keyList.size() - 1);
                Integer maxIndex = keyList.indexOf(maxOfficeType);
                Integer minIndex = keyList.indexOf(minOfficeType);
                diff = maxIndex - minIndex;
            }
        }
        return diff;
    }

    //获取下一个officeType
    public static String getNextOfficeType(String parentOfficeId) {
        OfficeManager officeManager= SummerBasicApplication.getBean(OfficeManager.class);
        Map<Integer, String> officeTypeMap = getOfficeDictMap();
        Integer currentType = 0;
        if (StringUtils.isNotBlank(parentOfficeId)) {
            currentType = Integer.valueOf(officeManager.findOne(parentOfficeId).getType());
        }
        for (Integer type : officeTypeMap.keySet()) {
            if (type > currentType) {
                return StringUtils.toString(type);
            }
        }
        return null;
    }

    //获取最顶层officeId列表
    public static List<String> getFirstLevelOfficeIds(List<String> officeIds) {
        Map<Integer, List<String>> typeMap = getOfficeLevelMap(officeIds);
        if (CollectionUtil.isEmpty(typeMap)) {
            return Lists.newArrayList();
        } else {
            List<Integer> keyList = Lists.newArrayList(typeMap.keySet());
            return typeMap.get(keyList.get(0));
        }
    }

    //获取最低层officeId列表
    public static List<String> getLastLevelOfficeIds(List<String> officeIds) {
        Map<Integer, List<String>> typeMap = getOfficeLevelMap(officeIds);
        if (CollectionUtil.isEmpty(typeMap)) {
            return Lists.newArrayList();
        } else {
            List<Integer> keyList = Lists.newArrayList(typeMap.keySet());
            return typeMap.get(keyList.get(keyList.size()-1));
        }
    }


    //根据级别排序officeIds
    public static Map<Integer, List<String>> getOfficeLevelMap(List<String> officeIds) {
        Map<Integer, List<String>> typeMap = Maps.newTreeMap();
        if (CollectionUtil.isNotEmpty(officeIds)) {
            OfficeMapper officeMapper= SummerBasicApplication.getBean(OfficeMapper.class);
            //机构分类
            List<Office> officeList = officeMapper.findByIds(officeIds);
            for (Office office : officeList) {
                Integer key = Integer.valueOf(office.getType());
                if (!typeMap.containsKey(key)) {
                    typeMap.put(key, Lists.newArrayList());
                }
                typeMap.get(key).add(office.getId());
            }
        }
        return typeMap;
    }

    //获取最底层officeType
    public static String getLastOfficeType(List<String> officeIds){
        Map<Integer, List<String>> typeMap = getOfficeLevelMap(officeIds);
        if(CollectionUtil.isEmpty(typeMap)){
            Map<Integer, String> map=getOfficeDictMap();
            return Lists.newArrayList(map.keySet()).get(map.size()-1).toString();
        }else {
            return Lists.newArrayList(typeMap.keySet()).get(typeMap.size()-1).toString();
        }
    }

    //获取机构分类字典
    public static Map<Integer, String> getOfficeDictMap(){
        DictMapMapper dictMapMapper = SummerBasicApplication.getApplicationContext().getBean(DictMapMapper.class);
        //机构分类
        List<DictMap> dictMaps = dictMapMapper.findByCategory(DictMapCategoryEnum.机构分类.name());
        Map<Integer, String> map = Maps.newTreeMap();
        for (DictMap dictMap : dictMaps) {
            map.put(Integer.valueOf(dictMap.getValue()), dictMap.getName());
        }
        return map;
    }
}
