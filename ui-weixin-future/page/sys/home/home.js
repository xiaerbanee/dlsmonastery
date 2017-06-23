//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        menuList: null,
        weixinAccounts: null,
        weixinAccountsHidden: true
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
        if (that.data.menuList == null) {
            wx.request({
                url: $util.getUrl('basic/sys/menu/getMobileMenus'),
                header: {
                    Cookie:"JSESSIONID="+app.globalData.sessionId
                },
                success: function (res) {
                    that.setData({ menuList: res.data });
                }
            });

        }
    },
    switchAccount: function (e) {
        var that = this;
        wx.request({
            url: $util.getUrl('logout'),
            data: {},
            method: 'POST',
            header: {
                'x-auth-token': app.globalData.sessionId
            },
            success: function () {
                that.data.menuList = null
                that.setData({ weixinAccountsHidden: false })
            }
        })
    },
})