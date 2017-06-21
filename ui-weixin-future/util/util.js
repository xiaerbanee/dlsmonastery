function getUrl(url) {
    var baseUrl =  "http://localhost:1200/api/";
    var result = baseUrl + url;
    if(url.indexOf("?") > 0) {
        result = result + "&requestClient=weixin";
    } else {
        result = result + "?requestClient=weixin";
    }
    return result;
}

function formatDate(date, format) {
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

function addMonth(date, qty) {
  var newDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
  newDate.setMonth(date.getMonth() + qty);
  return newDate;
}

function addDay(date, qty) {
  var newDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
  newDate.setDate(date.getDate() + qty);
  return newDate;
}


function formatLocalDate(date) {
  return formatDate(date, "yyyy-MM-dd");
}

function formatLocalDateTime(date) {
  return formatDate(date, "yyyy-MM-dd hh:mm");
}

function trim(value) {
  if(!value) {
    return "";
  }
  return value.replace(/(^\s*)|(\s*$)/g, "");
}

function getCheckboxItems(selects, selecteds) {
  for (var i in selects) {
    var value = selects[i].value;
    if (selecteds != null) {
      selects[i].checked = selecteds.indexOf(value) > -1;
    }
  }
  return selects;
}

function isBlank(value) {
  if (!value) {
    return true;
  } else {
    if (trim(value) == "") {
      return true;
    } else {
      return false;
    }
  }
}

function isNotBlank(value) {
  return !isBlank(value);
}

function getPreviousPageNumber(currentPageNumber) {
  currentPageNumber = parseInt(currentPageNumber);
  if (currentPageNumber <= 0) {
    return 0;
  } else {
    return currentPageNumber - 1;
  }
}

function getNextPageNumber(currentPageNumber, totalPages) {
  currentPageNumber = parseInt(currentPageNumber);
  totalPages = parseInt(totalPages);
  if (currentPageNumber >= totalPages - 1) {
    return currentPageNumber;
  } else {
    return currentPageNumber + 1;
  }
}

function getPageList(currentPageNumber, totalPages) {
  currentPageNumber = parseInt(currentPageNumber);
  totalPages = parseInt(totalPages);
  var list = new Array();
  var pageNumberStart = currentPageNumber - 2;
  if (pageNumberStart < 0) {
    pageNumberStart = 0;
  }
  var pageNumberEnd = currentPageNumber + 2;
  if (pageNumberEnd > totalPages - 1) {
    pageNumberEnd = totalPages - 1;
  }
  for (var i = pageNumberStart; i <= pageNumberEnd; i++) {
    list.push((i + 1).toString());
  }
  return list;
}

function getFirstDayOfMonth(date) {
  date.setDate(1);
  return date;
}

function getImages(imageStr,sessionId) {
  var that = this;
  var images = new Array();
  if (imageStr && imageStr != "") {
    var imageArray = imageStr.split(",");
    for (var i in imageArray) {
      images.push({
        id: imageArray[i],
        preview: getUrl('sys/folderFile/preview?x-auth-token=' + sessionId + '&id=' + imageArray[i]),
        view: getUrl('sys/folderFile/view?x-auth-token=' + sessionId + '&id=' + imageArray[i])
      })
    }
  }
  return images;
}
function getImageStr(images) {
  var imageStr = "";
  var imageArray = new Array();
  if (images && images.length > 0) {
    for (var i in images) {
      imageArray.push(images[i].id)
    }
    imageStr = imageArray.join(",")
  }
  return imageStr
}

function getFormatOfficeName(name) {
  if(!name) {
    return name;
  } else {
    return name.replace("办事处","");
  }
}

function deleteExtra(json) {
    var newJson = JSON.parse(JSON.stringify(json));
    delete newJson.extra;
    for(var index in newJson){
        if(!newJson[index]){
            delete newJson[index];
        }
    }
    return newJson;
}

module.exports = {
  getCheckboxItems: getCheckboxItems,
  trim: trim,
  isBlank: isBlank,
  isNotBlank: isNotBlank,
  formatDate: formatDate,
  formatLocalDate: formatLocalDate,
  formatLocalDateTime: formatLocalDateTime,
  addMonth: addMonth,
  addDay: addDay,
  getPreviousPageNumber: getPreviousPageNumber,
  getNextPageNumber: getNextPageNumber,
  getPageList: getPageList,
  getFirstDayOfMonth: getFirstDayOfMonth,
  getImages:getImages,
  getImageStr:getImageStr,
  getUrl:getUrl,
  getFormatOfficeName:getFormatOfficeName,
  deleteExtra:deleteExtra
}