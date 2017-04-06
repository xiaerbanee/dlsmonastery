//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
var sliderWidth = 96;
Page({
  data: {
    page: {},
    formData: {
      pageNumber: 0,
      pageSize: 10,
      order: "month_rank: DESC"
    },
    fromProperty: {},
    searchHidden: true,
    tabs: ["考核区域", "办事处"],
    activeIndex: "1",
    sliderLeft: 0,
    radioChecked: {
      dateChecked: false,
      monthChecked: true
    },
  },
  onLoad: function (options) {
    var that = this;
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
          sliderLeft: res.windowWidth - (res.windowWidth / that.data.tabs.length - sliderWidth) / 2 - sliderWidth
        });
      }
    });
    wx.request({
      url: $util.getUrl("crm/reportScoreArea/getListProperty"),
      data: {},
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
        that.setData({ 'fromProperty.areaList': res.data.areas })
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
          url: $util.getUrl("crm/reportScoreArea"),
          header: { 'x-auth-token': app.globalData.sessionId },
          data: that.data.formData,
          success: function (res) {
            for (var index in res.data.content) {
              res.data.content[index].formatName = $util.getFormatOfficeName(res.data.content[index].office.name)
            }
            that.setData({ page: res.data });
            wx.hideToast();
          }
        })
      }
    })
  },
  search: function (e) {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden })
  },
  tabClick: function (e) {
    if (e.currentTarget.dataset.id == 0) {
      wx.redirectTo({
        url: '/page/crm/reportScoreOfficeList/reportScoreOfficeList'
      })
    }
  },
  bindDateChange: function (e) {
    this.setData({ "formData.scoreDate": e.detail.value });
  },
  bindArea: function (e) {
    var that = this;
    that.setData({
      'formData.area.id': that.data.fromProperty.areaList[e.detail.value].id,
      'formData.area.name': that.data.fromProperty.areaList[e.detail.value].name
    })
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
  },
  toScoreOffice: function (e) {
    var that = this;
    var officeId = e.currentTarget.dataset.officeId
    var officeName = e.currentTarget.dataset.officeName;
    wx.navigateTo({
      url: '/page/crm/reportScoreOfficeList/reportScoreOfficeList?areaId=' + officeId + '&areaName=' + officeName + '&scoreDate=' + that.data.formData.scoreDate
    })
  }
})