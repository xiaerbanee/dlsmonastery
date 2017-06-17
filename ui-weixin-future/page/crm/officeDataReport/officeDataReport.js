var app = getApp();
var $util = require("../../../util/util.js");
var sliderWidth = 96;
Page({
  data: {
    page: {},
    formData: {},
    parentIdArr: null,
    hideButton: false,
    searchHidden: true,
    tabs: ["按区域", "按产品"],
    activeIndex: "0",
    sliderLeft: 0,
  },
  onLoad: function (options) {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          sliderLeft: (res.windowWidth / that.data.tabs.length - sliderWidth) / 2
        });
      }
    });
    if (options.productName) {
      that.setData({
        "parentIdArr[0]": '',
        "formData.reportDateStart": options.reportDateStart,
        "formData.reportDateEnd": options.reportDateEnd,
        "formData.productName": options.productName
      })
    } else {
      that.setData({
        "parentIdArr[0]": '',
        "formData.reportDateStart": $util.formatLocalDate($util.getFirstDayOfMonth(new Date())),
        "formData.reportDateEnd": $util.formatLocalDate(new Date),
        "formData.productName": ''
      })
    }
  },
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
    var formData = that.data.formData;
    var parentIdArr = that.data.parentIdArr;
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function () {
        wx.request({
          url: $util.getUrl("ws/future/crm/imeSaleReport/officeInventory"),
          data: {
            parentOfficeId: parentIdArr[parentIdArr.length - 1],
            reportDateStart: formData.reportDateStart,
            reportDateEnd: formData.reportDateEnd,
            productName: formData.productName
          },
          method: 'GET',
          header: { 'x-auth-token': app.globalData.sessionId,
          'authorization': "Bearer" + wx.getStorageSync('token').access_token },
          success: function (res) {
            if (that.data.parentIdArr.length > 1) {
              that.setData({ hideButton: true })
            } else {
              that.setData({ hideButton: false })
            }
            for(var index in res.data.inventoryDetailModelList) {
              res.data.inventoryDetailModelList[index].formatName = $util.getFormatOfficeName(res.data.inventoryDetailModelList[index].name)
            }
            that.setData({ page: res.data })
            wx.hideToast()
          }
        })
      }
    })
  },
  search: function () {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden });
  },
  tabClick: function (e) {
    if (e.currentTarget.dataset.id == 1) {
      wx.redirectTo({
        url: '/page/crm/productDataReport/productDataReport'
      })
    }
  },
  bindDateChange: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'reportDateStart') {
      that.setData({ "formData.reportDateStart": e.detail.value });
    } else {
      that.setData({ "formData.reportDateEnd": e.detail.value });
    }
  },
  back: function (e) {
    var that = this;
    var parentIdArr = that.data.parentIdArr;
    that.setData({
      parentIdArr: parentIdArr.slice(0, parentIdArr.length - 1)
    })
    that.pageRequest();
  },
  nextRequest: function (e) {
    var that = this;
    if (that.data.page.hasNext) {
      var parentIdArr = that.data.parentIdArr;
      parentIdArr.push(e.currentTarget.dataset.id)
      that.pageRequest();
    }
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden, formData: e.detail.value });
    that.pageRequest();
  },
})