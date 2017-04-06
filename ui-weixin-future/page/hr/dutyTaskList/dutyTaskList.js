//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    page: {},
    activeItem: null
  },
  onLoad: function (option) {
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
          url: $util.getUrl("hr/duty"),
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
  passAll: function (e) {
    var that = this;
    wx.showModal({
      title: '提示',
      content: '确认全部通过',
      success: function (res) {
        if (res.confirm) {
          wx.request({
            url: $util.getUrl("hr/duty/passAll"),
            data: {},
            method: 'GET',
            header: { 'x-auth-token': app.globalData.sessionId },
            success: function (res) {
              that.pageRequest();
            }
          })
        }
      }
    })
  },
  itemActive: function (e) {
    var that = this;
    var id = e.currentTarget.dataset.id;
    for (var index in that.data.page) {
      var item = that.data.page[index];
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
    var dutyType = e.currentTarget.dataset.dutyType;
    var itemList = that.data.activeItem.actionList;
    if (!itemList) { return; }
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          if (itemList[res.tapIndex] == "审核") {
            wx.navigateTo({
              url: '/page/hr/dutyTaskForm/dutyTaskForm?action=audit&id=' + id + '&dutyType=' + dutyType
            })
          }
        }
      }
    })
  }
})