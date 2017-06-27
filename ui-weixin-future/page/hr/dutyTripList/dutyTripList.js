//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        page: {},
        formData: {},
        searchHidden: true,
        activeItem: null,
        scrollTop: null,
        height: null
    },
    onLoad: function (option) {
        var that = this;
        that.setData({ height: $util.getWindowHeight() })
    },
    onShow: function () {
        var that = this;
        app.autoLogin(function () {
            that.initPage()
        });
    },
    initPage: function () {
        var that = this;
        wx.request({
            url: $util.getUrl("basic/hr/dutyTrip/getQuery"),
            data: {},
            method: 'GET',
            header: {
                Cookie: "JSESSIONID=" + app.globalData.sessionId
            },
            success: function (res) {
                that.setData({ formData: res.data })
                that.pageRequest();
            }
        })
    },
    pageRequest: function () {
        var that = this;
        wx.showToast({
            title: '加载中',
            icon: 'loading',
            duration: 10000,
            success: function (res) {
                wx.request({
                    url: $util.getUrl("basic/hr/dutyTrip"),
                    header: {
                        Cookie: "JSESSIONID=" + app.globalData.sessionId
                    },
                    data: $util.deleteExtra(that.data.formData),
                    success: function (res) {
                        for (var item in res.data.content) {
                            var actionList = new Array();
                            if (res.data.content[item].deleted) {
                                actionList.push("删除");
                            }
                            res.data.content[item].actionList = actionList;
                        }
                        that.setData({ page: res.data });
                        wx.hideToast();
                        that.setData({ scrollTop: $util.toUpper() });
                    }
                })
            }
        })
    },
    add: function () {
        wx.navigateTo({
            url: '/page/hr/dutyTripForm/dutyTripForm'
        })
    },
    search: function () {
        var that = this;
        that.setData({
            searchHidden: !that.data.searchHidden
        })
    },
    bindDateChange: function (e) {
        var that = this;
        var name = e.currentTarget.dataset.name;
        if (name == 'dutyDateStart') {
            that.setData({ "formData.dutyDateStart": e.detail.value });
        } else {
            that.setData({ "formData.dutyDateEnd": e.detail.value });
        }
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
    showActionSheet: function (e) {
        var that = this;
        var id = e.currentTarget.dataset.id;
        var itemList = that.data.activeItem.actionList;
        if (itemList.length == 0) {
            return;
        }
        wx.showActionSheet({
            itemList: itemList,
            success: function (res) {
                if (!res.cancel) {
                    if (itemList[res.tapIndex] == "删除") {
                        wx.request({
                            url: $util.getUrl("basic/hr/dutyTrip/delete"),
                            data: { id: id },
                            header: {
                                Cookie: "JSESSIONID=" + app.globalData.sessionId
                            },
                            success: function (res) {
                                that.pageRequest();
                            }
                        })
                    }
                }
            }
        })
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
    },
})