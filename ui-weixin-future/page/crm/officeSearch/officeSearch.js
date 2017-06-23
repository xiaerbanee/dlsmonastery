var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        officeList: {},
        type: '',
        name: ''
    },
    onLoad: function (options) {
        var that = this;
        that.setData({ type: options.type })
        that.officeSearch();
    },
    officeSelect: function (e) {
        var that = this;
        var value = e.detail.value;
        var indexOf = value.indexOf(",");
        var officeId = value.substring(0, indexOf);
        var officeName = value.substring(indexOf + 1);
        var currentPages = getCurrentPages();
        var previousPage = currentPages[currentPages.length - 2];
        previousPage.setData({ 'formData.officeId': officeId, 'formData.officeName': officeName });
        wx.navigateBack({
            delta: 1
        })
    },
    officeSearch: function () {
        var that = this;
        wx.request({
            url: $util.getUrl('/basic/hr/office/search'),
            data: { name: that.data.name, type: that.data.type },
            method: 'GET',
            header: {
                 Cookie: "JSESSIONID=" + app.globalData.sessionId
            },
            success: function (res) {
                that.setData({ officeList: res.data })
            }
        })
    },
    inputName: function (e) {
        var that = this;
        var name = e.detail.value;
        if (name.length < 2) {
            return;
        }
        that.setData({ name: name })
        that.officeSearch();
    }
});