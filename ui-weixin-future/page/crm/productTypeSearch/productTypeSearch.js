var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    productTypeList: {},
    name: ''
  },
  onLoad: function (options) {
    this.productTypeSearch();
  },
  productTypeSelect: function (e) {
    var that = this;
    var value = e.detail.value;
    var indexOf = value.indexOf(",");
    var productTypeId = value.substring(0, indexOf);
    var productTypeName = value.substring(indexOf + 1);
    var currentPages = getCurrentPages();
    var previousPage = currentPages[currentPages.length - 2];
    previousPage.setData({ 'formData.productTypeIdList': productTypeId, 'formData.productTypeName': productTypeName });
    wx.navigateBack({
      delta: 1
    })
  },
  productTypeSearch: function () {
    var that = this;
    if (!that.data.name) { return; }
    wx.request({
      url: $util.getUrl('ws/future/basic/productType/search'),
      data: { name: that.data.name },
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ productTypeList: res.data })
      }
    })
  },
  inputName: function (e) {
    var that = this;
    var name = e.detail.value;
    if (name.length < 2) {
      return;
    }
    that.setData({ name: name })
    that.productTypeSearch();
  }
});