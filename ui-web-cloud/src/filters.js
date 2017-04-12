import Vue from 'vue'

Vue.filter("arr2str", (value, separator=",") => value.join(separator) )

Vue.filter("bool2str", function(value) {
  return util.bool2str(value);
});

Vue.filter("joinColumn",function (value,columnName) {
  if(value==null || value.length==0) {
    return "";
  } else {
    var result = new Array();
    for(var index in value) {
      var obj = value[index];

      if(obj !=null && obj[columnName] != null) {
        result.push(obj[columnName]);
      }
    }
    if(result.length==0) {
      return "";
    } else {
      return result.join();
    }
  }
})

Vue.filter("formatLocalDateTime", function(value) {
  return util.formatLocalDateTime(value);
});

Vue.filter("getEnumLabel",function (value,enumName) {
    if(value==null || value=="" || enumName==null || enumName=="") {
      return "";
    } else {
      return enumMap[enumName][value];
    }
})

Vue.filter("toString", function(value) {
  return value==null?"":""+value;
});

Vue.filter("toInteger", function(value) {
  return value==null?null:Number(value);
});
