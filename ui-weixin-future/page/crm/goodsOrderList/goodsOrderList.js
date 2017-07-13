//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    page: {},
    formData: {},
    formProperty:{},
    searchHidden: true,
    activeItem: null,
    scrollTop:null,
  },
  onLoad: function (option) {
    let that = this;
      wx.request({
          url: $util.getUrl("ws/future/crm/goodsOrder/getQuery"),
          data: {},
          method: 'GET',
          header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
          success: function (res) {
              that.setData({
                  'formProperty.storeList': res.data.extra.stores, 
                  'formProperty.statusList': res.data.extra.statusList,
                  'formProperty.shipList': res.data.extra.shipTypeList,
                  'formProperty.netList': res.data.extra.netTypeList, 
                  'formProperty.areaList': res.data.extra.areaList,
                  formData:res.data
              })
          }
      })
    this.setData({ height: $util.getWindowHeight() })
  },
  onShow: function () {
    let that = this;
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
    this.pageRequest();
  },
  pageRequest: function () {
    var that = this
        wx.request({
          url: $util.getUrl("ws/future/crm/goodsOrder"),
          header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
          data: $util.deleteExtra(that.data.formData),
          success: function (res) {    
            for(var item in res.data.content){
              let actionList = new Array();
              actionList.push("详细");
              res.data.content[item].actionList=actionList
            }
            that.setData({ page: res.data});
            wx.hideToast();
            that.setData({ scrollTop: $util.toUpper() });
          }
        })
  },
  add: function () {
    wx.navigateTo({
      url: '/page/crm/goodsOrderForm/goodsOrderForm'
    })
  },
  search: function () {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden })
  },
  bindArea: function (e) {
    var that = this;
    that.setData({
      'formData.areaId': that.data.formProperty.areaList[e.detail.value].id,
      'formData.areaName': that.data.formProperty.areaList[e.detail.value].name
    })
  },
  bindStore: function (e) {
    var that = this;
    that.setData({
      'formData.storeId': that.data.formProperty.storeList[e.detail.value].id,
      'formData.storeName': that.data.formProperty.storeList[e.detail.value].name
    })
  },
  bindStatus: function (e) {
    var that = this;
    that.setData({ 'formData.status': that.data.formProperty.statusList[e.detail.value] })
  },
  bindShipType: function (e) {
    var that = this;
    that.setData({ 'formData.shipType': that.data.formProperty.shipList[e.detail.value] })
  },
  bindNetType: function (e) {
    var that = this;
    that.setData({ 'formData.netType': that.data.formProperty.netList[e.detail.value] })
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
          if (itemList[res.tapIndex] == "详细") {
            wx.navigateTo({
              url: '/page/crm/goodsOrderDetail/goodsOrderDetail?id=' + id
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
  }
})