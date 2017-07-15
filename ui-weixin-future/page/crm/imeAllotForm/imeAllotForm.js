var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        hidden: false,
        formData: { imeStr: '' },
        response: {},
        productImeList: {},
    },
    onLoad: function (options) {
        var that = this;
        app.autoLogin(function () {
            that.initPage()
        })
    },

    initPage: function () { },

    imeScan: function () {
        var that = this;
        wx.scanCode({
            success: function (res) {
                if (that.data.formData.imeStr.indexOf(res.result) < 0) {
                    var imeStr = $util.trim(that.data.formData.imeStr + '\n' + res.result);
                    that.setData({ 'formData.imeStr': imeStr, 'formData.result': res.result });
                    that.imeSearch();
                }
            }
        })
    },
    imeSearch: function () {
        var that = this;
        if ($util.isBlank(that.data.formData.imeStr)) {
            that.setData({ productImeList: {} })
        } else {
            wx.request({
                url: $util.getUrl("ws/future/crm/productIme/search"),
                data: {
                    imeStr: that.data.formData.imeStr
                },
                header: {  Cookie: "JSESSIONID=" + app.globalData.sessionId },
                success: function (res) {
                    that.setData({ productImeList: res.data.productImeList, productImeSearchResult: res.data });
                }
            })
        }
    },
    bindToDepot: function () {
        wx.navigateTo({
            url: '/page/crm/depotSearch/depotSearch?category=SHOP'
        })
    },
    imeStrChange: function (e) {
        var that = this;
        that.setData({ 'formData.result': e.detail.value })
        that.imeSearch();
    },
    formSubmit: function (e) {
        var that = this;
        that.setData({ submitDisabled: true })
        wx.request({
            url: $util.getUrl( "ws/future/crm/imeAllot/save"),
            data: e.detail.value,
            header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
            success: function (res) {
                if (res.data.success) {
                    wx.navigateBack();
                } else {
                    that.setData({ 'response.data': res.data.extra.errors});
                }
                that.setData({ submitDisabled: false})
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