var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    response:{},
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
      that.setData({"formData.dateStart": e.detail.value});
    } else {
      that.setData({"formData.dateEnd": e.detail.value});
    }
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
    wx.request({
      url: $util.getUrl("basic/hr/dutyTrip/save"),
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
    if(responseData && responseData.errors && responseData.errors[key] != null) {
      that.setData({ "response.error": responseData.errors[key].message });
      delete responseData.errors[key];
      that.setData({ "response.data": responseData })
    } else {
      that.setData({ "response.error": '' })
    }
  },
})