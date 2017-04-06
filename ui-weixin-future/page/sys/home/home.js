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
        //如果没有登陆
        if (app.globalData.weixinAccount == null) {
            console.log(app.globalData.weixinAccount)
            that.initLogin();
        } else {
            console.log("autoLogin")
            app.autoLogin(function () {
                that.initPage();
            })
        }
    },
    initLogin: function () {
        var that = this;
        wx.login({
            success: function (loginRes) {
                console.log(loginRes)
                if (loginRes.code) {
                    app.globalData.weixinCode = loginRes.code;
                    wx.request({
                        url: $util.getUrl( 'login/weixinAccounts?code=' + loginRes.code),
                        success: function (weixinAccountRes) {
                            console.log(weixinAccountRes)
                            var weixinAccounts = weixinAccountRes.data.weixinAccounts;
                            that.setData({ weixinAccounts: weixinAccounts });
                            //判断公司个数
                            if (weixinAccounts.length == 0) {
                                wx.navigateTo({
                                    url: '/page/sys/accountBind/accountBind'
                                })
                            } else if (weixinAccounts.length == 1) {
                                app.globalData.weixinAccount = weixinAccounts[0];
                                app.autoLogin(function () {
                                    that.initPage();
                                })
                            } else {
                                that.setData({ weixinAccountsHidden: false })
                            }
                        }
                    });
                }
            },
            fail: function (res) {
                console.log("wx login fail")
            },
        });
    },
    initPage: function () {
        var that = this;
        that.setData({ weixinAccountsHidden: true })
        if (that.data.menuList == null) {
            wx.request({
                url: $util.getUrl('sys/menu/getMenus?isMobile=true'),
                header: {
                    'x-auth-token': app.globalData.sessionId
                },
                success: function (res) {
                    console.log(res)
                    that.setData({ menuList: res.data });
                }
            });

        }
    },
    login: function (event) {
        var that = this;
        var index = event.currentTarget.dataset.index;
        app.globalData.weixinAccount = that.data.weixinAccounts[index];
        that.setData({ weixinAccountsHidden: true })
        app.autoLogin(function () {
            that.initPage();
        })
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
                that.data.menuList=null
                app.globalData.weixinAccount = null
                that.setData({ weixinAccountsHidden: false })
            }
        })
    }
})