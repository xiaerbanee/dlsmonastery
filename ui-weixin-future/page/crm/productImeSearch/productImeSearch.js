//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    imeStr: "",
    productImeSearchResult:{}
  },
  onLoad: function (option) {
  },
  onShow: function () {
    var that = this;
    app.autoLogin(function(){
      that.initPage()
    });
  },
  initPage:function() {
    var that = this;
  },
  imeScan: function () {
    var that = this;
    wx.scanCode({
      success: function (res) {
        var imeStr = $util.trim(that.data.imeStr + '\n' + res.result);
        that.setData({ imeStr: imeStr });
        that.imeSearch();
      }
    })
  },
  imeChange: function (e) {
    var that = this;
    that.setData({ imeStr: e.detail.value })
    that.imeSearch();
  },
  imeSearch: function () {
    var that = this;
    if ($util.isBlank(that.data.imeStr)) {
      that.setData({ productImeSearchResult: {} })
    } else {
      wx.request({
        url: $util.getUrl("crm/productIme/search"),
        data: { imeStr: that.data.imeStr },
        header: { 'x-auth-token': app.globalData.sessionId },
        success: function (res) {
          that.setData({ productImeSearchResult: res.data });
        }
      })
    }
  }
})