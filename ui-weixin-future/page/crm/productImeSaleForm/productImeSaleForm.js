//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    productImeSearchResult: {},
    formData: {imeStr:''},
    formProperty: {},
    response: {},
    submitDisabled: false
  },
  onLoad: function (option) {
    var that = this;
    app.autoLogin(function () {
      that.initPage()
    })
  },
  initPage: function () {
    var that = this;
    wx.request({
      url: $util.getUrl( "crm/productImeSale/getFormProperty"),
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
        that.setData({ formProperty: res.data })
        if(res.data.shops.length==0){
          that.setData({ "response.error": "该账户未绑定门店,请先绑定门店",submitDisabled:false});
        }
      }
    })
  },
  imeScan: function () {
    var that = this;
    wx.scanCode({
      success: function (res) {
        var ime = $util.trim(that.data.formData.imeStr + '\n' + res.result);
        that.setData({ "formData.imeStr": ime });
        that.imeSearch();
      }
    })
  },
  imeChange: function (e) {
    var that = this;
    that.setData({ "formData.imeStr": e.detail.value })
    that.imeSearch();
  },
  imeSearch: function () {
    var that = this;
    if ($util.isBlank(that.data.formData.imeStr)) {
      that.setData({ productImeSearchResult: {} })
    } else {
      wx.request({
        url: $util.getUrl( "crm/productIme/search"),
        data: that.data.formData,
        header: { 'x-auth-token': app.globalData.sessionId },
        success: function (res) {
          that.setData({ productImeSearchResult: res.data });
        }
      })
    }
  },
  bindSaleShop: function (e) {
    wx.navigateTo({
      url: '/page/crm/depotSearch/depotSearch'
    })
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
    wx.request({
      url: $util.getUrl("crm/productImeSale/save"),
      data: e.detail.value,
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
        if (res.data.success) {
          that.setData({ "response.data": res.data });
          wx.navigateBack();
        } else {
          that.setData({ "response.data": res.data, submitDisabled: false });
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