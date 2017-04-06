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
    formProperty: {},
    searchHidden: true,
    activeItem: null
  },
  onLoad: function (option) {
    var that = this;
    wx.request({
      url: $util.getUrl("crm/shopAd/getListProperty"),
      data: {},
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
        that.setData({ 'formProperty.shopAdTypeList': res.data.shopAdTypes, 'formProperty.processList': res.data.processFlows })
      }
    })
  },
  onShow: function () {
    var that = this;
    app.autoLogin(function(){
      that.initPage()
    });
  },
  initPage:function() {
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
          url: $util.getUrl("crm/shopAd"),
          header: { 'x-auth-token': app.globalData.sessionId },
          data: that.data.formData,
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
      url: '/page/crm/shopAdForm/shopAdForm'
    })
  },
  search: function () {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden })
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden, formData: e.detail.value, "formData.pageNumber": 0 });
    that.pageRequest();
  },
  bindAdType: function (e) {
    var that = this;
    that.setData({
      'formData.shopAdType.id': that.data.formProperty.shopAdTypeList[e.detail.value].id,
      'formData.shopAdType.name': that.data.formProperty.shopAdTypeList[e.detail.value].name
    })
  },
  bindStatus: function (e) {
    var that = this;
    that.setData({ 'formData.processStatus': that.data.formProperty.processList[e.detail.value].name })
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
  itemActive: function (e) {
    var that = this;
    var id = e.currentTarget.dataset.id;
    for (var index in that.data.page.content) {
      var item = that.data.page.content[index];
      if (item.id == id) {
        that.data.activeItem = item;
      }
      if (item.id == id && item.hasOwnProperty('actionList')) {
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
    if (!itemList) {
      return;
    }
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          if (itemList[res.tapIndex] == "修改") {
            wx.navigateTo({
              url: '/page/crm/shopAdForm/shopAdForm?action=update&id=' + id
            })
          } else if (itemList[res.tapIndex] == "详细") {
            wx.navigateTo({
              url: '/page/crm/shopAdForm/shopAdForm?action=detail&id=' + id
            })
          }
          else if (itemList[res.tapIndex] == "删除") {
            wx.request({
              url: $util.getUrl("crm/shopAd/delete"),
              data: { id: id },
              header: { 'x-auth-token': app.globalData.sessionId },
              success: function (res) {
                that.pageRequest();
              }
            })
          }else if(itemList[res.tapIndex] == "审核"){
            wx.navigateTo({
              url: '/page/crm/shopAdForm/shopAdForm?action=audit&id=' + id
            })
          }
        }
      }
    })
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
  },
})