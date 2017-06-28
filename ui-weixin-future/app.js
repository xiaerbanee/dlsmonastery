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
        menuList:[],
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
            that.login(cb);
        } else {
            typeof cb == "function" && cb();
        }
    }, login: function (cb) {
        var that = this;
        wx.request({
            url: $util.getUaaUrl('user/login?weixinCode=' + that.globalData.weixinCode),
            data: {},
            method: 'POST',
            success: function (res) {
                if (res.statusCode == 401) {
                    wx.navigateTo({
                        url: '/page/sys/accountBind/accountBind'
                    })
                } else {
                    that.globalData.sessionId = res.data.JSESSIONID
                    wx.request({
                        url: $util.getUrl('basic/hr/account/getAccountInfo'),
                        header: {
                            Cookie: "JSESSIONID=" + res.data.JSESSIONID
                        },
                         data: {"isMobile":true},
                        success: function (res) {
                            that.globalData.menuList=res.data.menus;
                            that.globalData.weixinAccount=res.data.account;
                             typeof cb == "function" && cb();
                        }
                    });
                }
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