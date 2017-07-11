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
        that.setData({ weixinAccountsHidden: true })
        app.login(function () {
            that.initPage();
        })
    },
    switchAccount: function (e) {
        var that = this;
        console.log(">>>>>>>>>>>>>>")
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
                        that.setData({ menuList: [] })
                        app.globalData.menuList = null
                        app.globalData.weixinAccount = null
                        that.setData({ weixinAccountsHidden: false })
                        wx.request({
                            url: $util.getUaaUrl('user/getWeixinAccounts?weixinCode=' + app.globalData.weixinCode),
                            data: {},
                            method: 'POST',
                            success: function (res) {
                                app.globalData.weixinAccounts = res.data;
                                that.setData({ weixinAccounts: res.data })
                            },
                        })
                    },
                })
            }
        })
    },
})