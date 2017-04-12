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
            that.initLogin();
        } else {
            app.autoLogin(function () {
                that.initPage();
            })
        }
    },
    initLogin: function () {
        var that = this;
        wx.login({
            success: function (loginRes) {
                if (loginRes.code) {
                    app.globalData.weixinCode = loginRes.code;
                    that.getToken();
                }
            },
            fail: function (res) {
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
                that.data.menuList = null
                app.globalData.weixinAccount = null
                that.setData({ weixinAccountsHidden: false })
            }
        })
    },
    getToken: function () {
        wx.request({
            url: $util.getUrl('uaa/oauth/token?weixinCode=' + app.globalData.weixinCode + "&grant_type=password"),
            data: {},
            method: 'POST',
            success: function (res) {
                if (res.statusCode == 401) {
                    wx.navigateTo({
                        url: '/page/sys/accountBind/accountBind'
                    })
                } else if ($util.isNotBlank(res.data)) {
                    wx.navigateTo({
                        url: '/page/sys/home/home'
                    })
                }
            },
            fail: function () {
                // fail
            },
            complete: function () {
                // complete
            }
        })
    }
})