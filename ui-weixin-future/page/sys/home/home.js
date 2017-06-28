//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        weixinAccounts: null,
        weixinAccountsHidden: true,
        menuList: null
    },
    onLoad: function (options) {
    },
    onShow: function (options) {
        var that = this;
        app.autoLogin(function () {
            that.initPage();
        })
    }, initPage: function () {
        var that = this;
        that.setData({ weixinAccountsHidden: true })
        that.setData({ menuList: app.globalData.menuList })
        if (that.data.menuList == null || that.data.menuList.length == 0) {
            wx.request({
                url: $util.getUrl('basic/sys/menu/getMobileMenus'),
                header: {
                    Cookie: "JSESSIONID=" + app.globalData.sessionId
                },
                success: function (res) {
                    app.globalData.menuList = res.data
                    that.setData({ menuList: res.data })
                }
            });

        }
    },
    switchAccount: function (e) {
        var that = this;
        wx.request({
            url: $util.getUaaUrl('/user/logout'),
            data: {},
            method: 'POST',
            header: {
                Cookie: "JSESSIONID=" + app.globalData.sessionId
            },
            success: function () {
                app.globalData.menuList = null
                app.globalData.weixinAccount = null
                that.setData({ weixinAccountsHidden: false })
            }
        })
    },
})