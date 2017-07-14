// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    formProperty: {},
    response: {},
    submitDisabled: false
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
    var options = that.data.options;
    wx.request({
      url: $util.getUrl("basic/hr/auditFile/getForm"),
      data: {},
      method: 'GET',
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        wx.request({
          url: $util.getUrl("/general/sys/processType/findByCreatePositionId"),
          data: {},
          method: 'GET',
          header: {
            Cookie: "JSESSIONID=" + app.globalData.sessionId
          },
          success: function (res) {
            that.setData({ 'formProperty.processList': res.data })
          }
        })
      }
    })
    if (options.action == "update") {
      wx.request({
        url: $util.getUrl("basic/hr/auditFile/findOne"),
        data: { id: options.id },
        method: 'GET',
        header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
        success: function (res) {
          that.setData({ formData: res.data })
        }
      })
    }
  },
  bindProcessType: function (e) {
    var that = this;
    that.setData({
      'formData.name': that.data.formProperty.processList[e.detail.value].name
    })
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
    wx.request({
      url: $util.getUrl("basic/hr/auditFile/save"),
      data: e.detail.value,
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ "response.error": res.data.message, submitDisabled: false })
          that.setData({ 'response.data': res.data.extra.errors, submitDisabled: false });
        }
      }
    })
  },
  showError: function (e) {
    var that = this;
    var key = e.currentTarget.dataset.key;
    var responseData = that.data.response.data;
    if (responseData && responseData[key] != null) {
      that.setData({ "response.error": responseData[key].message });
      delete responseData[key];
      that.setData({ "response.data": responseData })
    } else {
      that.setData({ "response.error": '' })
    }
  },
})