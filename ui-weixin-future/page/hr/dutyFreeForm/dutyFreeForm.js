// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    formProperty: {},
    response: {},
    submitDisabled: false,
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
      url: $util.getUrl("basic/hr/dutyFree/getForm"),
      data: {},
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId,
                'authorization': "Bearer" + wx.getStorageSync('token').access_token
       },
      success: function (res) {
        that.setData({ 'formProperty.dateList': res.data.dateList })
      }
    })
  },

  bindFreeDate: function (e) {
    var that = this;
    that.setData({
      'formData.freeDate': e.detail.value
    });
  },
  bindDateType: function (e) {
    var that = this;
    that.setData({
      'formData.dateType': that.data.formProperty.dateList[e.detail.value]
    });
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
    wx.request({
      url: $util.getUrl("basic/hr/dutyFree/save"),
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
})