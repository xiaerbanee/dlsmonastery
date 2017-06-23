// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {
      imeStr: '',
      boxImeStr: '',
      expressCodes: ''
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
    var that = this;
  },
  numberScan: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == "formatId") {
      wx.scanCode({
        success: function (res) {
          that.setData({ 'formData.formatId': res.result });
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
      url: $util.getUrl("crm/goodsOrder/findByFormatId"),
      data: { formatId: that.data.formData.formatId },
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        console.log(res)
        that.setData({
          'formData.store': res.data.store, 'formData.shop': res.data.shop, 'formData.store': res.data.store,
          'formData.shipRemarks': res.data.shipRemarks, 'formData.remarks': res.data.remarks
        });
      }
    })
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true })
    wx.request({
      url: $util.getUrl( "crm/goodsOrder/ship"),
      data: e.detail.value,
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ 'response.data': res.data,submitDisabled: false });
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
  }
})