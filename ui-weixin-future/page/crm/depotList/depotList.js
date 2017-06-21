//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        page: {},
        formData: {},
        searchHidden: true,
        activeItem: null
    },
    onLoad: function (options) { },
    onShow: function () {
        var that = this;
        app.autoLogin(function () {
            that.initPage()
        });
    },
    initPage: function () {
        var that = this;
        wx.request({
            url: $util.getUrl("ws/future/basic/depotShop/getQuery"),
            data: {},
            method: 'GET',
            header: {
                'x-auth-token': app.globalData.sessionId,
                'authorization': "Bearer" + wx.getStorageSync('token').access_token
            },
            success: function (res) {
                that.setData({ formData: res.data });
                that.pageRequest();
            }
        });
    },
    pageRequest: function () {
        var that = this;
        wx.showToast({
            title: '加载中',
            icon: 'loading',
            duration: 10000,
            success: function () {
                wx.request({
                    url: $util.getUrl("ws/future/basic/depotShop"),
                    header: {
                        'x-auth-token': app.globalData.sessionId,
                        'authorization': "Bearer" + wx.getStorageSync('token').access_token
                    },
                    method: 'GET',
                    data: $util.deleteExtra(that.data.formData),
                    success: function (res) {
                        that.setData({ page: res.data });
                        wx.hideToast();
                    }
                })
            }
        })
    },
    search: function () {
        var that = this;
        that.setData({ searchHidden: !that.data.searchHidden })
    },
    itemActive: function (e) {
        var that = this;
        var id = e.currentTarget.dataset.id;
        for (var index in that.data.page.content) {
            var item = that.data.page.content[index];
            if (item.id == id) {
                that.data.activeItem = item;
                item.active = true;
            } else {
                item.active = false;
            }
        }
        that.setData({ page: that.data.page });
    },
    showItemActionSheet: function (e) {
        var that = this;
        var id = e.currentTarget.dataset.id;
        wx.showActionSheet({
            itemList: ["修改"],
            success: function (res) {
                if (!res.cancel) {
                    if (res.tapIndex == 0) {
                        wx.navigateTo({
                            url: '/page/crm/depotForm/depotForm?action=edit&id=' + id
                        })
                    }
                }
            }
        });
    },
    formSubmit: function (e) {
        var that = this;
        that.setData({ searchHidden: !that.data.searchHidden, formData: e.detail.value, "formData.page": 0 });
        that.pageRequest();
    },
    toFirstPage: function () {
        var that = this;
        that.setData({ "formData.page": 0 });
        that.pageRequest();
    },
    toPreviousPage: function () {
        var that = this;
        that.setData({ "formData.page": $util.getPreviousPageNumber(that.data.formData.page) });
        that.pageRequest();
    },
    toNextPage: function () {
        var that = this;
        that.setData({ "formData.page": $util.getNextPageNumber(that.data.formData.page, that.data.page.totalPages) });
        that.pageRequest();
    },
    toLastPage: function () {
        var that = this;
        that.setData({ "formData.page": that.data.page.totalPages - 1 });
        that.pageRequest();
    },
    toPage: function () {
        var that = this;
        var itemList = $util.getPageList(that.data.formData.page, that.data.page.totalPages);
        wx.showActionSheet({
            itemList: itemList,
            success: function (res) {
                if (!res.cancel) {
                    that.setData({ "formData.page": itemList[res.tapIndex] - 1 });
                    that.pageRequest();
                }
            }
        });
    }
});