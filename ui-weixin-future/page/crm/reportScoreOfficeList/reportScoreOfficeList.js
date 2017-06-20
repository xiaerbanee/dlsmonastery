//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
var sliderWidth = 96;
Page({
  data: {
    page: {},
    formData: {
      page: 0,
      size: 10,
      order: "month_rank: DESC"
    },
    radioChecked: {
      dateChecked: false,
      monthChecked: true
    },
    fromProperty: {},
    searchHidden: true,
    tabs: ["考核区域", "办事处"],
    activeIndex: "0",
    sliderLeft: 0,
    options: null
  },
  onLoad: function (options) {
    var that = this;
    that.setData({ options: options })
  },
  onShow: function () {
    var that = this;
    app.autoLogin(function () {
      that.initPage()
    });
  },
  initPage: function () {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          sliderLeft: (res.windowWidth / that.data.tabs.length - sliderWidth) / 2
        });
      }
    });
    if (that.data.options.areaId) {
      that.setData({ "formData.areaId": that.data.options.areaId });
      that.setData({ "formData.areaName": that.data.options.areaName });
    }
    if (that.data.options.scoreDate) {
      that.setData({ "formData.scoreDate": that.data.options.scoreDate });
    }
    wx.request({
      url: $util.getUrl("ws/future/crm/reportScoreOffice/getQuery"),
      data: {},
      method: 'GET',
      header: {
        'x-auth-token': app.globalData.sessionId,
        'authorization': "Bearer" + wx.getStorageSync('token').access_token
      },
      success: function (res) {
        that.setData({ 'fromProperty.areaList': res.data.extra.areaList })
      }
    })
    if ($util.trim(that.data.formData.scoreDate) == "") {
      that.setData({ "formData.scoreDate": $util.formatLocalDate($util.addDay(new Date(), -1)) });
    }
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
          url: $util.getUrl("ws/future/crm/reportScoreOffice"),
          header: {
            'x-auth-token': app.globalData.sessionId,
            'authorization': "Bearer" + wx.getStorageSync('token').access_token
          },
          data: that.data.formData,
          success: function (res) {
            console.log(res.data)
            that.setData({ page: res.data });
            wx.hideToast();
          }
        });
      }
    })
  },
  search: function (e) {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden })
  },
  tabClick: function (e) {
    if (e.currentTarget.dataset.id == 1) {
      wx.redirectTo({
        url: '/page/crm/reportScoreAreaList/reportScoreAreaList'
      })
    }
  },
  bindRadioChange: function (e) {
    var that = this;
    if ("month_rank: DESC" == e.detail.value) {
      that.setData({ "radioChecked.monthChecked": true });
      that.setData({ "radioChecked.dateChecked": false });
    } else {
      that.setData({ "radioChecked.monthChecked": false });
      that.setData({ "radioChecked.dateChecked": true });
    }
    that.setData({ "formData.order": e.detail.value });
  },
  bindDateChange: function (e) {
    this.setData({ "formData.scoreDate": e.detail.value });
  },
  bindArea: function (e) {
    var that = this;
    that.setData({
      'formData.areaId': that.data.fromProperty.areaList[e.detail.value].id,
      'formData.areaName': that.data.fromProperty.areaList[e.detail.value].name
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