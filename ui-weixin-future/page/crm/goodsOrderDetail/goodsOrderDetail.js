//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        goodsOrder: {},
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
            url: $util.getUrl("crm/goodsOrder/detail"),
            header: { 'x-auth-token': app.globalData.sessionId },
            data: { id: options.id },
            success: function (res) {
                that.setData({ goodsOrder: res.data });
            }
        });
    }

});