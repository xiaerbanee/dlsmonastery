import elementId from 'element-ui/lib/locale/lang/id'
import basicHrId from"./basic_hr_id";
import basicSysId from "./basic_sys_id"
import businessBasicId from "./future_basic_id"
import businessCrmId from "./future_crm_id"
import businessLayoutId from "./future_layout_id"
import commonId from "./common_id"
import futureEnumId from "./future_enum_id"
import basicEnumId from "./basic_enum_id"
import menuId from "./menu_id"

import elementZhCn from 'element-ui/lib/locale/lang/zh-CN'
import basicHrZhCn from"./basic_hr_zh-CN";
import basicSysZhCn from "./basic_sys_zh-CN"
import businessBasicZhCn from "./future_basic_zh-CN"
import businessCrmZhCn from "./future_crm_zh-CN"
import businessLayoutZhCn from "./future_layout_zh-CN"
import commonZhCn from "./common_zh-CN"
import futureEnumZhCn from "./future_enum_zh-CN"
import basicEnumZhCn from "./basic_enum_zh-CN"
import menuZhCn from "./menu_zh-CN"

var idArray = [elementId,basicHrId,basicSysId,businessBasicId,businessCrmId,businessLayoutId,commonId,futureEnumId,basicEnumId,menuId];
var zhCnArray = [elementZhCn,basicHrZhCn,basicSysZhCn,businessBasicZhCn,businessCrmZhCn,businessLayoutZhCn,commonZhCn,futureEnumZhCn,basicEnumZhCn,menuZhCn];

var id = merge(idArray);
var zhCn = merge(zhCnArray);

function  merge(jsonArray) {
  var resultJsonObject = {};
  for (var index in jsonArray) {
    for (var attr in jsonArray[index]) {
      resultJsonObject[attr] = jsonArray[index][attr];
    }
  }
  return resultJsonObject;
}

export default {
  id:id,
  zhCn:zhCn
}
