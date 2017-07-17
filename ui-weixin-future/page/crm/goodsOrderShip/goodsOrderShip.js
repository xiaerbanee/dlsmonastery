// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {

    },
    response: {},
    submitDisabled: false,

  },
  onShow: function () {
    var that = this;
    app.autoLogin(function () {
      that.initPage()
    });
  },
  initPage: function () {
  },
  numberScan: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == "businessId") {
      wx.scanCode({
        success: function (res) {
          that.setData({ 'formData.businessId': res.result });
          that.numberSearch();
        }
      })
    } else if (name == "imeStr") {
      wx.scanCode({
        success: function (res) {
          if (that.data.formData.imeStr.indexOf(res.result) < 0) {
            var imeStr = $util.trim(that.data.formData.imeStr + '\n' + res.result);
            that.setData({ 'formData.imeStr': imeStr });
          }
        }
      })
    } else if (name == "boxImeStr") {
      wx.scanCode({
        success: function (res) {
          if (that.data.formData.boxImeStr.indexOf(res.result) < 0) {
            var boxImeStr = $util.trim(that.data.formData.boxImeStr + '\n' + res.result);
            that.setData({ 'formData.boxImeStr': boxImeStr });
          }
        }
      })
    } else if (name == "expressCodes") {
      wx.scanCode({
        success: function (res) {
          if (that.data.formData.expressCodes.indexOf(res.result) < 0) {
            var expressCodes = $util.trim(that.data.formData.expressCodes + '\n' + res.result);
            that.setData({ 'formData.expressCodes': expressCodes });
          }
        }
      })
    }
  },
  numberSearch: function () {
    var that = this;
    wx.request({
      url: $util.getUrl("ws/future/crm/goodsOrderShip/getShipByBusinessId"),
      data: { businessId: that.data.formData.businessId },
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        console.log(res.data)
        that.setData({ formData: res.data, 'formData.imeStr': '', 'formData.boxImeStr': '', 'formData.expressCodes': '' });
      }
    })
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true })
    wx.request({
      url: $util.getUrl("ws/future/crm/goodsOrderShip/ship"),
      data: e.detail.value,
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        console.log(res.data)
        if (res.data.success) {
          wx.navigateBack();
        } else if (res.data.hasOwnProperty("extra")) {
          that.setData({ 'response.data': res.data.extra.errors, submitDisabled: false });
        } else {
          that.setData({ "response.error": res.data, submitDisabled: false })
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
  }
})