//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        page: {},
        formData: {
            pageNumber: 0,
            pageSize: 10
        },
        activeItem: null,
        searchHidden: true
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
        that.pageRequest();
    },
    pageRequest: function () {
        var that = this;
        wx.showToast({
            title: '加载中',
            icon: 'loading',
            duration: 10000,
            success: function (res) {
                wx.request({
                    url: $util.getUrl("basic/hr/accountTask"),
                    header: { 'x-auth-token': app.globalData.sessionId,
                              'authorization': "Bearer" + wx.getStorageSync('token').access_token
                     },
                    data: that.data.formData,
                    success: function (res) {
                        console.log(res)
                        that.setData({ page: res.data });
                        wx.hideToast();
                    }
                })
            }
        })
    },
    search: function () {
        var that = this;
        that.setData({  searchHidden: !that.data.searchHidden })
    },
    itemActive: function (e) {
        var that = this;
        var id = e.currentTarget.dataset.id;
        for (var index in that.data.page.content) {
            var item = that.data.page.content[index];
            if (item.extendId == id) {
                that.data.activeItem = item;
            }
            if (item.extendId == id && item.hasOwnProperty('actionList')) {
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
        var name = that.data.activeItem.name;
        var itemList = that.data.activeItem.actionList;
        if (!itemList) { return; }
        wx.showActionSheet({
            itemList: itemList,
            success: function (res) {
                if (!res.cancel) {
                    if (itemList[res.tapIndex] == "审核") {
                        if (name == '广告申请') {
                            wx.navigateTo({
                                url: '/page/crm/shopAdForm/shopAdForm?id=' + id + '&action=audit',
                            })
                        } else if (name == '文件审批') {
                            wx.navigateTo({
                                url: '/page/hr/auditFileDetail/auditFileDetail?id=' + id + '&action=audit',
                            })
                        }
                    }
                }
            }
        })
    },
    formSubmit: function (e) {
        var that = this;
        that.setData({ searchHidden: !that.data.searchHidden, formData: e.detail.value, "formData.pageNumber": 0 });
        that.pageRequest();
    },
    toFirstPage: function () {
        var that = this;
        that.setData({ "formData.pageNumber": 0 });
        that.pageRequest();
    },
    toPreviousPage: function () {
        var that = this;
        that.setData({ "formData.pageNumber": $util.getPreviousPageNumber(that.data.formData.pageNumber) });
        that.pageRequest();
    },
    toNextPage: function () {
        var that = this;
        that.setData({ "formData.pageNumber": $util.getNextPageNumber(that.data.formData.pageNumber, that.data.page.totalPages) });
        that.pageRequest();
    },
    toLastPage: function () {
        var that = this;
        that.setData({ "formData.pageNumber": that.data.page.totalPages - 1 });
        that.pageRequest();
    },
    toPage: function () {
        var that = this;
        var itemList = $util.getPageList(that.data.formData.pageNumber, that.data.page.totalPages);
        wx.showActionSheet({
            itemList: itemList,
            success: function (res) {
                if (!res.cancel) {
                    that.setData({ "formData.pageNumber": itemList[res.tapIndex] - 1 });
                    that.pageRequest();
                }
            }
        });
    }
})