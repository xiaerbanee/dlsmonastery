import store from '../store/'

var util = {};

util.pickerDateOption = {
  shortcuts: [{
    text: '今天',
    onClick(picker) {
      const end = new Date();
      const start = new Date();
      picker.$emit('pick', [start, end]);
    }
  }, {
    text: '昨天',
    onClick(picker) {
      const start = new Date();
      const end = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24);
      end.setTime(end.getTime() - 3600 * 1000 * 24);
      picker.$emit('pick', [start, end]);
    }
  }, {
    text: '最近一周',
    onClick(picker) {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      end.setTime(end.getTime() - 3600 * 1000 * 24);
      picker.$emit('pick', [start, end]);
    }
  }, {
    text: '最近一个月',
    onClick(picker) {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      end.setTime(end.getTime() - 3600 * 1000 * 24);
      picker.$emit('pick', [start, end]);
    }
  }]
},
util.getFirstDayOfMonth = function (date) {
  date.setDate(1);
  return date;
}
util.currentDate = function () {
  var myDate = new Date();
  return myDate.toLocaleDateString();
},
util.getSort = function (column) {
  let sort = '';
  if (column.prop == null) {
    sort = "id,DESC";
  } else {
    var order = "DESC";
    if(column.order == "ascending") {
      order = "ASC"
    }
    if (column.column.columnKey != null) {
      sort = column.column.columnKey + "," + order;
    } else {
      sort = column.prop + "," + order;
    }
  }
  return sort
}

util.copyValue = function (from, to) {
  if (from != null) {
    for (var key in to) {
      if (from[key] != null) {
        to[key] = from[key];
      }
    }
  }
}

util.cloneAndCopy = function (from, to) {
  let result = Object.assign({}, to);
  if (from != null) {
    for (var key in result) {
      if (from[key] != null) {
        result[key] = from[key];
      }
    }
  }
  return result;
}

util.confirmBeforeDelRecord = function(vueObj){
  return vueObj.$confirm(vueObj.$t('util.waringMsgBeforeDelRecord'), vueObj.$t('util.typeOfWarningBeforeDelRecord'), {
    confirmButtonText: vueObj.$t('util.confirmButton'),
    cancelButtonText:  vueObj.$t('util.cancelButton'),

    type: 'warning'
  });
}
util.confirmBeforeBatchPass = function(vueObj){
  return vueObj.$confirm(vueObj.$t('util.waringMsgBeforeBatchPass'), vueObj.$t('util.typeOfWarningBeforeBatchPass'), {
    confirmButtonText: vueObj.$t('util.confirmButton'),
    cancelButtonText:  vueObj.$t('util.cancelButton'),

    type: 'warning'
  });
}
util.confirmBeforeAction = function(vueObj, action){
  return vueObj.$confirm(action, vueObj.$t('util.notify'), {
    confirmButtonText: vueObj.$t('util.confirmButton'),
    cancelButtonText:  vueObj.$t('util.cancelButton'),

    type: 'warning'
  });
}

util.confirmBeforeExportData = function(vueObj){
  return vueObj.$confirm(vueObj.$t('util.waringMsgBeforeExportData'), vueObj.$t('util.typeOfWarningBeforeExportData'), {
    confirmButtonText: vueObj.$t('util.confirmButton'),
    cancelButtonText:  vueObj.$t('util.cancelButton'),

    type: 'warning'
  });
}




util.getQuery = function (routerName) {
  var query = {};
  if (routerName != "home") {
    var tabs = store.state.global.tabs;
    query = tabs.get(routerName);
  }
  return query;
}

util.isPermit = function (permissionName) {
  var authorityList = store.state.global.authorityList;
  if (authorityList && authorityList.indexOf(permissionName) != -1) {
    return true;
  }
  return false;
}

util.setQuery = function (routerName, query) {
  if (routerName != "home") {
    var params = {routerName: routerName, query: query};
    store.dispatch("setQuery", params);
  }
}

util.formatDate = function (date, format) {
  var o = {
    "M+": date.getMonth() + 1, //month
    "d+": date.getDate(), //day
    "h+": date.getHours(), //hour
    "m+": date.getMinutes(), //minute
    "s+": date.getSeconds(), //second
    "q+": Math.floor((date.getMonth() + 3) / 3), //quarter
    "S": date.getMilliseconds() //millisecond
  }
  if (/(y+)/.test(format)) {
    format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));

  }
  for (var k in o) {
    if (new RegExp("(" + k + ")").test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    }
  }
  return format;
}

util.formatDateRange = function (dateRange) {
  if (dateRange != null && dateRange[0] != null && dateRange[1] != null) {
    return this.formatDate(new Date(dateRange[0]), "yyyy-MM-dd") + " - " + this.formatDate(new Date(dateRange[1]), "yyyy-MM-dd");
  } else {
    return "";
  }
}

util.formatLocalDate = function (date) {
  if (date != null && date != "") {
    return this.formatDate(new Date(date), "yyyy-MM-dd");
  } else {
    return "";
  }
}

util.formatLocalDateTime = function (date) {
  if (date != null && date != "") {
    return this.formatDate(new Date(date), "yyyy-MM-dd hh:mm:ss");
  } else {
    return "";
  }
}

util.formatLocalMonth = function (date) {
  if (date != null && date != "") {
    return this.formatDate(new Date(date), "yyyy-MM");
  } else {
    return "";
  }
}

util.bool2str = function (value) {
  if (value == "true" || value == true || value == "1" || value == 1) {
    return "是";
  } else {
    return "否";
  }
};

util.getIdList = function (array) {
  var idList = new Array();
  if (array != null && array.length > 0) {
    for (var i = 0; i < array.length; i++) {
      idList.push(array[i].id);
    }
  }
  return idList;
}

util.getLabel = function (array, id, labelColumn) {
  if (labelColumn == null) {
    labelColumn = "name";
  }
  var labelObj = null;
  for (var index in array) {
    var obj = array[index];
    if (obj.id == id) {
      labelObj = obj;
    }
  }
  if (labelObj == null) {
    return null;
  } else {
    return labelObj[labelColumn];
  }
}

util.getLabelList = function (array, labelColumn) {
  if (labelColumn == null) {
    labelColumn = "name";
  }
  let list=new Array();
  if (array != null && array.length > 0) {
    for (var index in array) {
      var obj = array[index];
      list.push(obj[labelColumn])
    }
  }
  return list;
}

util.getFolderFileIdStr = function (array) {
  var idList = new Array();
  if (array != null && array.length > 0) {
    for (var i = 0; i < array.length; i++) {
      if (array[i].id != null) {
        idList.push(array[i].id);
      } else {
        if (array[i].response != null && array[i].response.length > 0 && array[i].response[0].id != null) {
          idList.push(array[i].response[0].id);
        }
      }
    }
  }
  return idList.join();
}

util.getMongoId = function (array) {
  var idList = new Array();
  if (array != null && array.length > 0) {
    for (var i = 0; i < array.length; i++) {
      if (array[i].id != null) {
        idList.push(array[i].id);
      } else {
        if (array[i].response != null && array[i].response.length > 0 && array[i].response[0].mongoId != null) {
          idList.push(array[i].response[0].mongoId);
        }
      }
    }
  }
  return idList.join();
}

util.contains = function (str, subStr) {
  if (subStr == null || subStr.length == 0) {
    return true;
  }
  var reg = eval("/" + subStr + "/ig");
  return reg.test(str);
}

util.isBlank = function (val) {
  return _.trim(val) == "";
}

util.isNotBlank = function (val) {
  return _.trim(val) != "";
}

export default util;
