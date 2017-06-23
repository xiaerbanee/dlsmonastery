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
        weixinAccount: null
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
        var token = wx.getStorageSync('token')
        var distance = that.getDistance();
        var reflushTokenDustance = wx.getStorageSync('token').expires_in;
        //如果没有登陆
        if (token == "" || $util.isNotBlank(token.access_token) || distance < 60 * 1000) {
            that.getToken(cb);
        } else if (distance < (reflushTokenDustance / 2) * 1000) {
            that.getRefreshToken();
        }
    },
    getRefreshToken: function () {
        var that = this;
        var token = wx.getStorageSync('token');
        var pages = getCurrentPages();
        var currentPages = pages[pages.length - 1]
        wx.request({
            url: $util.getUrl('uaa/oauth/token?username=' + that.globalData.weixinCode + '&password=' + that.globalData.weixinCode + '&weixinCode=' + that.globalData.weixinCode + '&grant_type=refresh_token&refresh_token=' + token.refresh_token),
            data: {},
            method: 'POST',
            success: function (res) {
                if ($util.isNotBlank(res.data.access_token)) {
                    res.data.exp = new Date().getTime();
                    wx.setStorageSync('token', res.data);
                    wx.navigateTo({
                        url: '/' + currentPages.__route__
                    });
                }
            }
        })
    },
    getDistance: function () {
        var that = this;
        var token = wx.getStorageSync('token');
        var distance;
        var expDate = new Date(token.exp + token.expires_in * 1000);
        return distance = expDate.getTime() - new Date().getTime();
    }, getToken: function (cb) {
        var that = this;
        wx.request({
            url: $util.getUrl('uaa/oauth/token?username=' + that.globalData.weixinCode + '&password=' + that.globalData.weixinCode + '&weixinCode=' + that.globalData.weixinCode + '&grant_type=password'),
            data: {},
            method: 'POST',
            success: function (res) {
                if (res.statusCode == 401) {
                    wx.navigateTo({
                        url: '/page/sys/accountBind/accountBind'
                    })
                } else if ($util.isNotBlank(res.data.access_token)) {
                    res.data.exp = new Date().getTime();
                    wx.setStorageSync('token', res.data);
                    typeof cb == "function" && cb();
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