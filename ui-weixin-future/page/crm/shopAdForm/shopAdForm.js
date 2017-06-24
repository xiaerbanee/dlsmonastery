// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    formProperty: {},
    response: {},
    submitDisabled: false,
    submitHidden: false,
    auditDisabled: false,
    auditHidden: false,
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
    var options = that.data.options;
    wx.request({
      url: $util.getUrl("ws/future/layout/shopAd/getForm"),
      data: {},
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ 'formProperty.shopAdTypeList': res.data.extra.shopAdTypeFormList })
      }
    })
    if (options.action == "update") {
      that.detail();
    } else if (options.action == "detail") {
      that.detail();
      that.setData({ submitHidden: true })
    } else if (options.action == "audit") {
      that.detail();
      that.setData({ auditHidden: true, submitHidden: true })
    }
  },
  bindAdType: function (e) {
    var that = this;
    that.setData({
      'formData.shopAdTypeId': that.data.formProperty.shopAdTypeList[e.detail.value].id,
      'formData.shopAdTypeName': that.data.formProperty.shopAdTypeList[e.detail.value].name
    })
  },
  bindShop: function (e) {
    wx.navigateTo({
      url: '/page/crm/depotSearch/depotSearch'
    })
  },
  switchChange: function (e) {
    var that = this;
    that.setData({ 'formData.specialArea': e.detail.value })
  },
  formSubmit: function (e) {
    var that = this;
    var event = e.detail.target.dataset.event;
    if (event == "submit") {
      that.setData({ submitDisabled: true });
      wx.request({
        url: $util.getUrl("ws/future/layout/shopAd/save"),
        data: e.detail.value,
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        success: function (res) {
          console.log(res.data)
          if (res.data.success) {
            wx.navigateBack();
          } else {
            that.setData({ 'response.data': res.data.extra.errors, submitDisabled: false });
          }
        }
      })
    } else if (event == "audit") {
      that.setData({ auditDisabled: true });
      wx.request({
        url: $util.getUrl("ws/future/layout/shopAd/audit"),
        data: e.detail.value,
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        success: function (res) {
          if (res.data.success) {
            wx.navigateBack();
          } else {
            that.setData({ auditDisabled: false })
          }
        }
      })
    }
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
  detail: function (e) {
    var that = this;
    wx.request({
      url: $util.getUrl("ws/future/layout/shopAd/findOne"),
      data: { id: that.data.options.id },
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({
          formData: res.data
        })
      }
    })
  }
})