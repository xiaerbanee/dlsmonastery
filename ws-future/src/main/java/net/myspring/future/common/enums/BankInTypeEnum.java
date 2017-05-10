package net.myspring.future.common.enums;

import com.google.common.collect.Lists;
import net.myspring.util.collection.CollectionUtil;

import java.util.List;

public enum BankInTypeEnum {
    销售收款,
		定金收款;
	/*SALES_RECEIPT,
	DEPOSIT_RECEIPT;*/

	private static List<String> list= Lists.newArrayList();

	public static List<String> getList(){
		if(CollectionUtil.isEmpty(list)){
			for(BankInTypeEnum each : BankInTypeEnum.values()){
				list.add(each.name());
			}
		}
		return list;
	}
	}