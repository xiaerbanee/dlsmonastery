var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        formData: {},
        options: null
    },
    onLoad: function (options) {
        var that = this;
        that.data.options = options
        app.autoLogin(function () {
            that.initPage()
        })
    },
    initPage: function () {
        var that = this;
        var options = that.data.options;
        wx.request({
            url: $util.getUrl("basic/hr/employee/saleDetail"),
            data: { id: options.id, dateStart: options.dateStart, dateEnd: options.dateEnd },
            method: 'GET',
            header: { 'x-auth-token': app.globalData.sessionId,
                      'authorization': "Bearer" + wx.getStorageSync('token').access_token
             },
            success: function (res) {
                that.setData({ formData: res.data })
            }
        })
    },
})