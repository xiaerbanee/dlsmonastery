package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum ShopGoodsDepositStatusEnum {
	省公司审核, 已通过, 未通过;

	private static List<String> list= Lists.newArrayList();

	public static List<String> getList(){
		if(CollectionUtil.isEmpty(list)){
			for(ShopGoodsDepositStatusEnum each : ShopGoodsDepositStatusEnum.values()){
				list.add(each.name());
			}
		}
		return list;
	}
}