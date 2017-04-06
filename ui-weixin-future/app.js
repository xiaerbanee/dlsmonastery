//app.js
var $util = require("/util/util.js");
App({
    onLaunch: function () {
        console.log("lauch");
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
        wx.request({
            url: $util.getUrl("login/isLogin"),
            header: {
                'x-auth-token': that.globalData.sessionId
            },
            success: function (res) {
                if (res.data) {
                    typeof cb == "function" && cb()
                } else {
                    var loginName = that.globalData.weixinAccount.id + "_" + that.globalData.weixinCode;
                    wx.request({
                        url: $util.getUrl('login?loginName=' + loginName),
                        method: "POST",
                        header: {
                            'x-auth-token': that.globalData.sessionId
                        },
                        success: function (loginRes) {
                            that.globalData.sessionId = loginRes.data;
                            typeof cb == "function" && cb()
                        }
                    })
                }
            }
        })
    }
})