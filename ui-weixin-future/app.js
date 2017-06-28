//app.js
var $util = require("/util/util.js");
App({
    onLaunch: function () {
    },
    getUserInfo: function (cb) {
        var that = this
        if (that.globalData.userInfo) {
            typeof cb == "function" && cb(that.globalData.userInfo)
        } else {
            //调用登录接口
            wx.login({
                success: function (res) {
                    wx.getUserInfo({
                        success: function (res) {
                            that.globalData.userInfo = res.userInfo
                            typeof cb == "function" && cb(that.globalData.userInfo)
                        }
                    })
                }
            })
        }
    },
    globalData: {
        sessionId: "",
        userInfo: null,
        weixinCode: null,
        weixinAccount: null,
        weixinAccounts: [],
        menuList: [],
    },
    //检查用户是否登陆，如果未登陆，自动登陆
    autoLogin: function (cb) {
        var that = this;
        if (!that.globalData.weixinCode) {
            that.getCode(function () {
                that.checkLogin(cb);
            })
        } else {
            that.checkLogin(cb);
        }
    }, checkLogin(cb) {
        var that = this;
        var weixinAccount = that.globalData.weixinAccount
        //如果没有登陆
        if (weixinAccount == null || !$util.isNotBlank(weixinAccount.id)) {
            wx.request({
                url: $util.getUaaUrl('user/getWeixinAccounts?weixinCode=' + that.globalData.weixinCode),
                data: {},
                method: 'POST',
                success: function (res) {
                    var weixinAccounts = res.data;
                    that.globalData.weixinAccounts = weixinAccounts;
                    //判断公司个数
                    if (weixinAccounts.length == 0) {
                        wx.navigateTo({
                            url: '/page/sys/accountBind/accountBind'
                        })
                    } else if (weixinAccounts.length == 1) {
                        weixinAccount = weixinAccounts[0];
                        that.login(cb)
                    } else {
                        typeof cb == "function" && cb();
                    }
                },
            })
        } else {
            typeof cb == "function" && cb();
        }
    }, login: function (cb) {
        var that = this;
        wx.request({
            url: $util.getUaaUrl('user/login'),
            data: { weixinCode: that.globalData.weixinCode, accountId: that.globalData.weixinAccount.id },
            method: 'POST',
            success: function (res) {
                that.globalData.sessionId = res.data.JSESSIONID
                wx.request({
                    url: $util.getUrl('basic/hr/account/getAccountInfo'),
                    header: {
                        Cookie: "JSESSIONID=" + res.data.JSESSIONID
                    },
                    data: { "isMobile": true },
                    success: function (res) {
                        console.log(res.data.account)
                        that.globalData.menuList = res.data.menus;
                        that.globalData.weixinAccount = res.data.account;
                        wx.setStorageSync('authorityList', res.data.authorityList);
                        typeof cb == "function" && cb();
                    }
                });
            }
        })
    }, getCode(cb) {
        var that = this;
        if (!that.globalData.weixinCode) {
            wx.login({
                success: function (loginRes) {
                    if (loginRes.code) {
                        that.globalData.weixinCode = loginRes.code;
                        typeof cb == "function" && cb();
                    }
                }
            });
        }
    }
})