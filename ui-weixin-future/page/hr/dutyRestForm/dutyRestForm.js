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
      header: { 'x-auth-token': app.globalData.sessionId,
                'authorization': "Bearer" + wx.getStorageSync('token').access_token
       },
      success: function (res) {
        console.log(res);
        that.setData({ 'formProperty.restList': res.data.extra.restList, 'formProperty.dateList': res.data.extra.dateList, 'fromProperty': res.data})
      }
    })
  },
  bindRestType: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'restType') {
      that.setData({
        'formData.restType': that.data.formProperty.restList[e.detail.value],
        'formData.hour': '',
        'formData.dateType': '',
        'formData.timeStart': '',
        'formData.timeEnd': ''
      });
    } else {
      that.setData({
        'formData.dateType': that.data.formProperty.dateList[e.detail.value],
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
      header: { 'x-auth-token': app.globalData.sessionId,
                'authorization': "Bearer" + wx.getStorageSync('token').access_token
       },
      success: function (res) {
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ 'response.data': res.data, submitDisabled: false });
        }
      }
    })
  },
  showError: function (e) {
    var that = this;
    var key = e.currentTarget.dataset.key;
    var responseData = that.data.response.data;
    if (responseData && responseData.errors && responseData.errors[key] != null) {
      that.setData({ "response.error": responseData.errors[key].message });
      delete responseData.errors[key];
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
        var dateTimeStart = new Date((dutyDate + " " + timeStart)).getTime();
        var dateTimeEnd = new Date((dutyDate + " " + timeEnd)).getTime();
        hour = (dateTimeEnd - dateTimeStart) * 1.0 / (3600 * 1000);
        if (hour == Math.floor(hour)) {
          that.setData({ 'formData.hour': Math.floor(hour) })
        } else {
          var tempTotalrestHour = Math.floor(hour) + 0.5;
          if (hour > tempTotalrestHour) {
            hour = Math.floor(hour) + 1.0;
            that.setData({ 'formData.hour': hour})
          } else {
            hour = tempTotalrestHour;
            that.setData({ 'formData.hour': hour })
          }
        }
      }
      if (restType == '年假调休' && $util.isNotBlank(dateType)) {
        hour = dateType == '全天' ? 8.0 : 4.0;
        that.setData({ 'formData.hour': hour })
      }
    }
  },
})