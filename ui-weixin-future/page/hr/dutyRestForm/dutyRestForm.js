var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    formProperty: {},
    response: {},
    options: null
  },
  onLoad: function (options) {
    var that = this;
    that.data.options = options
    app.autoLogin(function () {
      that.initPage()
    })
  },
  initPage: function () {
    var that = this;
    wx.request({
      url: $util.getUrl("basic/hr/dutyRest/getForm"),
      data: {},
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ formProperty: res.data })
      }
    })
  },
  bindRestType: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'restType') {
      that.setData({
        'formData.restType': that.data.formProperty.extra.restList[e.detail.value],
        'formData.hour': '',
        'formData.dateType': '',
        'formData.timeStart': '',
        'formData.timeEnd': ''
      });
    } else {
      that.setData({
        'formData.dateType': that.data.formProperty.extra.dateList[e.detail.value],
      })
      that.getHour();
    }
  },
  bindChange: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'timeStart') {
      that.setData({ "formData.timeStart": e.detail.value });
      that.getHour();
    } else if (name == 'timeEnd') {
      that.setData({ "formData.timeEnd": e.detail.value });
      that.getHour();
    } else {
      that.setData({ 'formData.dutyDate': e.detail.value });
    }
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
    wx.request({
      url: $util.getUrl("basic/hr/dutyRest/save"),
      data: e.detail.value,
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({"response.error":res.data.message})
          that.setData({ 'response.data': res.data.extra.errors, submitDisabled: false });
        }
      }
    })
  },
  showError: function (e) {
    var that = this;
    var key = e.currentTarget.dataset.key;
    var responseData = that.data.response.data;
    if (responseData && responseData && responseData[key] != null) {
      that.setData({ "response.error": responseData[key].message });
      delete responseData[key];
      that.setData({ "response.data": responseData })
    } else {
      that.setData({ "response.error": '' })
    }
  },
  getHour: function (e) {
    var that = this;
    var hour = 0.0;
    var restType = that.data.formData.restType;
    var timeStart = that.data.formData.timeStart;
    var timeEnd = that.data.formData.timeEnd;
    var dutyDate = that.data.formData.dutyDate;
    var dateType = that.data.formData.dateType;
    if ($util.isNotBlank(restType) && $util.isNotBlank(dutyDate)) {
      if ($util.isNotBlank(timeStart) && $util.isNotBlank(timeEnd)) {
        timeStart = timeStart.split(':');
        timeEnd = timeEnd.split(':');
        var timeStartLong = timeStart[0] * 60 + 1.0 * timeStart[1];
        var timeEndLong = timeEnd[0] * 60 + 1.0 * timeEnd[1];
        if (timeEndLong < timeStartLong) {
          hour = 0;
        } else {
          hour = parseInt((timeEndLong - timeStartLong) / 60)
          console.log(hour)
          if ((timeEndLong - timeStartLong) % 60 >0&&(timeEndLong - timeStartLong) % 60 <= 30) {
            hour = hour + 0.5
          } else if ((timeEndLong - timeStartLong) % 60 > 30){
            hour = hour +1
          }
        }
        that.setData({
          'formData.hour': hour
        });
      }
      if (restType == '年假调休' && $util.isNotBlank(dateType)) {
        hour = dateType == '全天' ? 8.0 : 4.0;
        that.setData({ 'formData.hour': hour })
      }
    }
  },
})