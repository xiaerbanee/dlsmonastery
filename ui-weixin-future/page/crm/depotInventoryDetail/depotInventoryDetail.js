// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    options: null
  },
  onLoad: function (options) {
    var that = this;
    that.data.options = options;
    app.autoLogin(function () {
      that.initPage()
    })
  },
  initPage: function () {
    var that = this;
    var options = that.data.options;
    wx.request({
      url: $util.getUrl("crm/depot/inventoryDetail"),
      data: { id: options.id, dateStart: options.dateStart, dateEnd: options.dateEnd },
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ formData: res.data })
      }
    })
  }
})