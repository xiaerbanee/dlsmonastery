var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
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
  },

  bindDateChange: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'dateStart') {
      that.setData({ "formData.dateStart": e.detail.value });
    } else {
      that.setData({ "formData.dateEnd": e.detail.value });
    }
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
    wx.request({
      url: $util.getUrl("basic/hr/dutyTrip/save"),
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
    if (responseData && responseData[key] != null) {
      that.setData({ "response.error": responseData[key].message });
      delete responseData[key];
      that.setData({ "response.data": responseData })
    } else {
      that.setData({ "response.error": '' })
    }
  },
})