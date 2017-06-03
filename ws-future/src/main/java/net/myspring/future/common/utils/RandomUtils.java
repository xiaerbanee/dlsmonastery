package net.myspring.future.common.utils;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

/**
 * Created by zhangyf on 2017/6/2.
 */
public class RandomUtils {

    /**
     * 获取区间内不重复的多个随机数
     * @param fromIndex 最小数（包含）
     * @param toIndex 最大数（不包含）
     * @param cnt 个数
     * @return
     */
    public static List<Integer> getRandomList(Integer fromIndex, Integer toIndex, Integer cnt){
        List<Integer> result = Lists.newArrayList();
        List<Integer> list = Lists.newArrayList();
        for(Integer i=fromIndex;i<toIndex;i++) {
            list.add(i);
        }
        Random random = new Random();
        while(result.size()<cnt) {
            int x=random.nextInt(list.size());
            result.add(list.get(x));
            list.remove(x);
        }
        return result;
    }
}
