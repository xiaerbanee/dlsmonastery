//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    page: {},
    formData: {},
    formProperty: {},
    searchHidden: true,
    activeItem: null,
    scrollTop: null,
    height: null
  },
  onLoad: function (option) {
    this.setData({ height: $util.getWindowHeight() })
  },
  onShow: function () {
    var that = this;
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function (res) {
        app.autoLogin(function () {
          that.initPage()
        });
      }
    })
  },
  initPage: function () {
    var that = this;
    wx.request({
      url: $util.getUrl("ws/future/layout/shopImage/getQuery"),
      data: {},
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        console.log(res.data)
        that.setData({ 'formProperty.areaList': res.data.extra.areaList,formData:res.data });
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
          url: $util.getUrl("ws/future/layout/shopImage"),
          header: {
            Cookie: "JSESSIONID=" + app.globalData.sessionId
          },
          data: $util.deleteExtra(that.data.formData),
          success: function (res) {
            that.setData({ page: res.data });
            wx.hideToast();
          }
        })
      }
    })
  },
  add: function () {
    wx.navigateTo({
      url: '/page/crm/shopImageForm/shopImageForm'
    })
  },
  search: function () {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden })
  },
  bindDateChange: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'createdDateStart') {
      that.setData({ "formData.createdDateStart": e.detail.value });
    } else {
      that.setData({ "formData.createdDateEnd": e.detail.value });
    }
  },
  bindOffice: function (e) {
    var that = this;
    that.setData({
      'formData.areaId': that.data.formProperty.areaList[e.detail.value].id,
      'formData.areaName': that.data.formProperty.areaList[e.detail.value].name
    })
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
    wx.showActionSheet({
      itemList: ["修改", "详细", "删除"],
      success: function (res) {
        if (!res.cancel) {
          if (res.tapIndex == 0) {
            wx.navigateTo({
              url: '/page/crm/shopImageForm/shopImageForm?action=update&id=' + id
            })
          } else if (res.tapIndex == 1) {
            wx.navigateTo({
              url: '/page/crm/shopImageForm/shopImageForm?action=detail&id=' + id
            })
          }
          else if (res.tapIndex == 2) {
            wx.request({
              url: $util.getUrl("ws/future/layout/shopImage/delete"),
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