import elementId from 'element-ui/lib/locale/lang/id'
import businessBasicId from "./future_basic_id"
import businessCrmId from "./future_crm_id"
import tableDataId from "./table_data_id"
import commonId from "./common_id"
import elementZhCn from 'element-ui/lib/locale/lang/zh-CN'
import businessBasicZhCn from "./future_basic_zh-CN"
import businessCrmZhCn from "./future_crm_zh-CN"
import tableDataZhCn from "./table_data_zh-CN"
import commonZhCn  from "./common_zh-CN"

var idArray = [elementId,businessBasicId,businessCrmId,tableDataId,commonId];
var zhCnArray = [elementZhCn,businessBasicZhCn,businessCrmZhCn,tableDataZhCn,commonZhCn];

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
