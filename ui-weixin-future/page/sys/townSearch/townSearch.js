var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        townList: {},
        name: ''
    },
    onLoad: function (options) {
        var that = this;
        that.townSearch();
    },
    townSelect: function (e) {
        var that = this;
        var value = e.detail.value;
        var indexOf = value.indexOf(",");
        var id = value.substring(0, indexOf);
        var name = value.substring(indexOf + 1);
        var currentPages = getCurrentPages();
        var previousPage = currentPages[currentPages.length - 2];
        previousPage.setData({ 'formData.townId': id, 'formData.townName': name });
        wx.navigateBack({
            delta: 1
        })
    },
    townSearch: function () {
        var that = this;
        wx.request({
            url: $util.getUrl('general/sys/town/search'),
            data: { name: that.data.name },
            method: 'GET',
            header: {
                'x-auth-token': app.globalData.sessionId,
                'authorization': "Bearer" + wx.getStorageSync('token').access_token

            },
            success: function (res) {
                var data = res.data;
                var town = [];
                for (var i in data) {
                    town.push({ id: data[i].id, name: data[i].provinceName + data[i].cityName + data[i].countyName + data[i].townName });
                }
                that.setData({ townList: town })
                console.log(that.data.townList)
            }
        })
    },
    inputName: function (e) {
        var that = this;
        var name = e.detail.value;
        if (name.length < 2) {
            return;
        }
        that.setData({ name: name });
        that.townSearch();
    }
});