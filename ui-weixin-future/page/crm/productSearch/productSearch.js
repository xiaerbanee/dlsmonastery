var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        productList: null,
        name: '',
        checked: false,
        ids: null,
        names: null
    },
    onLoad: function (options) {
        var that = this;
        wx.showToast({
            title: '加载中',
            icon: 'loading',
            duration: 10000,
            success: function (res) {
                that.productSearch();
            }
        })
    },
    productSelect: function (e) {
        var that = this;
        var currentPages = getCurrentPages();
        var previousPage = currentPages[currentPages.length - 2];
        previousPage.setData({ 'formData.productIds': that.data.ids, 'formData.productNames': that.data.names });
        wx.navigateBack({
            delta: 1
        })
    },
    productChange: function (e) {
        var that = this;
        var productList = that.data.productList;
        var values = e.detail.value;
        var idArr = [];
        var nameArr = [];
        for (var i = 0, lenI = productList.length; i < lenI; ++i) {
            productList[i].checked = false;
            for (var j = 0, lenJ = values.length; j < lenJ; ++j) {
                if (productList[i].id == values[j].split(",")[0]) {
                    idArr.push((values[j].split(",")[0]));
                    nameArr.push(values[j].split(",")[1]);
                    that.setData({ ids: idArr, names: nameArr })
                    productList[i].checked = true;
                    break;
                }
            }
        }
        that.setData({ productList: productList })
    },
    productSearch: function () {
        var that = this;
        wx.request({
            url: $util.getUrl('crm/product/findHasImeProduct'),
            method: 'GET',
            header: { 'x-auth-token': app.globalData.sessionId },
            success: function (res) {
                wx.hideToast();
            }
        })
    }
});