//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        productImeDetail: {},
        options:null
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
        var options=that.data.options;
        wx.request({
            url: $util.getUrl("crm/productIme/detail"),
            header: { Cookie: "JSESSIONID=" + app.globalData.sessionId},
            data: { productImeId: options.productImeId },
            success: function (res) {
                that.setData({ productImeDetail: res.data });
            }
        });
    },
});