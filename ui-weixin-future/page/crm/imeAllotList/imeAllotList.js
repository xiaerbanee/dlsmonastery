var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    page: {},
    formData: { },
    formProperty:{},
    response:{},
    searchHidden: true,
    activeItem: null
  },
  onLoad: function (options) {
    var that = this;
    wx.request({
      url: $util.getUrl("crm/imeAllot/getQuery"),
      data: {},
      method: 'GET',
      header: {  Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        that.setData({'formProperty.crossAreaList':res.data.crossArea,'formProperty.statusList':res.data.status})
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
  pageRequest: function (cb) {
    var that = this
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function () {
        wx.request({
          url: $util.getUrl("crm/imeAllot"),
          header: {  Cookie: "JSESSIONID=" + app.globalData.sessionId},
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
      url: '/page/crm/imeAllotForm/imeAllotForm'
    })
  },
  search: function () {
    var that = this;
    that.setData({
      searchHidden: !that.data.searchHidden
    })
  },
  changeCrossArea: function (e) {
    var that=this;
    that.setData({
      'formData.crossArea.name': that.data.formProperty.crossAreaList[e.detail.value].name,
       'formData.crossArea.value': that.data.formProperty.crossAreaList[e.detail.value].value })
  },
  changeStatus: function (e) {
    var that=this;
    that.setData({ 'formData.status': that.data.formProperty.statusList[e.detail.value] })
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
    if (!itemList) { return; }
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          if (itemList[res.tapIndex] == '删除') {
            wx.request({
              url: $util.getUrl("crm/imeAllot/delete"),
              data: { id: id },
              header: {  Cookie: "JSESSIONID=" + app.globalData.sessionId },
              success: function (res) {
                that.pageRequest();
              }
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
  },
})