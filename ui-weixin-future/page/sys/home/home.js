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
        if (that.data.weixinAccountsHidden) {
            app.autoLogin(function () {
                that.initPage();
            })
        }
    }, initPage: function () {
        var that = this;
        console.log(app.globalData.weixinAccounts)
        if (app.globalData.weixinAccounts.length > 1 && app.globalData.weixinAccount == null) {
            that.setData({ weixinAccountsHidden: false, weixinAccounts: app.globalData.weixinAccounts })
        } else {
            that.setData({ weixinAccountsHidden: true })
            that.setData({ menuList: app.globalData.menuList })
        }
    }, login: function (event) {
        var that = this;
        var index = event.currentTarget.dataset.index;
        app.globalData.weixinAccount = that.data.weixinAccounts[index];
        console.log(app.globalData.weixinAccount)
        that.setData({ weixinAccountsHidden: true })
        app.login(function () {
            that.initPage();
        })
    },
    switchAccount: function (e) {
        var that = this;
        wx.request({
            url: $util.getUaaUrl('/user/logout'),
            method: 'POST',
            header: {
                Cookie: "JSESSIONID=" + app.globalData.sessionId
            },
            success: function () {
                wx.request({
                    url: $util.getUaaUrl('/logout'),
                    method: 'POST',
                    header: {
                        Cookie: "JSESSIONID=" + app.globalData.sessionId
                    },
                    success: function (res) {
                        app.globalData.menuList = null
                        app.globalData.weixinAccount = null
                        that.setData({ weixinAccountsHidden: false })
                    },
                })
            }
        })
    },
})