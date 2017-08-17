import elementId from 'element-ui/lib/locale/lang/id'
import tableDataId from "./table_data_id"

import elementZhCn from 'element-ui/lib/locale/lang/zh-CN'
import tableDataZhCn from "./table_data_zh-CN"

var idArray = [elementId,tableDataId];
var zhCnArray = [elementZhCn,tableDataZhCn];

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
