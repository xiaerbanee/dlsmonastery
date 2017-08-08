var app = getApp();
var $util = require("../../../util/util.js");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    page: [],
    formData: {},
    formProperty: {},
    sum: '',
    officeIds: [],
    officeId: null,
    depotId: null,
    isDepot: false,
    nextIsShop: false,
    types: "销售报表",
    searchHidden: true,
    productTypeDetail: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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
      url: $util.getUrl('ws/future/crm/productIme/getReportQuery'),
      data: {},
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        console.log(res.data)
        that.setData({ formData: res.data, 'formData.dateEnd': $util.formatLocalDate(new Date()), formProperty: res.data.extra });
        that.pageRequest();
      }
    })
  },
  pageRequest: function () {
    var that = this;
    that.setData({ 'formData.dateRange': that.data.formData.dateStart + " - " + that.data.formData.dateEnd })
    that.setData({ 'formData.type': that.data.types, 'formData.isDetail': false });
    if (!that.data.nextIsShop) {
      that.setData({ 'formData.depotId': '' });
      wx.request({
        url: $util.getUrl('ws/future/crm/productIme/productImeReport'),
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        data: $util.deleteExtra(that.data.formData),
        success: function (res) {
          console.log(res.data)
          that.setData({ page: res.data.list, sum: res.data.sum });
          wx.hideToast();
        }
      })

    } else {
      wx.request({
        url: $util.getUrl('ws/future/basic/depotShop/depotReportDate'),
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        data: $util.deleteExtra(that.data.formData),
        success: function (res) {
          that.setData({ page: res.data.list, isDepot: true });
          wx.hideToast();
        }
      })
    }
  },
  nextLevel: function (e) {
    let that = this;
    let officeId = e.currentTarget.dataset.officeId
    let depotId = e.currentTarget.dataset.depotId;
    if (!that.data.nextIsShop) {
      wx.request({
        url: $util.getUrl('basic/sys/office/checkLastLevel'),
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        data: { officeId },
        success: function (res) {
          that.data.officeIds.push(officeId);
          that.setData({ officeId: officeId, 'formData.officeId': that.data.officeIds[that.data.officeIds.length - 1], nextIsShop: res.data })
          wx.showToast({
            title: '加载中',
            icon: 'loading',
            duration: 10000,
            success: function (res) {
              that.pageRequest();
            }
          })
        }
      })
    } else {
      that.setData({ 'formData.isDetail': true, 'formData.depotId': depotId })
      wx.request({
        url: $util.getUrl('ws/future/basic/depotShop/depotReportDetail'),
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        data: $util.deleteExtra(that.data.formData),
        success: function (res) {
          let productQtyMap = res.data.productQtyMap;
          let productTypeDetail = [];
          if (productQtyMap) {
            for (let key in productQtyMap) {
              productTypeDetail.push({ productName: key, qty: productQtyMap[key] })
            }
            that.setData({ productTypeDetail: productTypeDetail });
          }
        }
      })
    }
  },
  preLevel() {
    let that = this;
    if (that.data.formData.isDetail) {
      that.setData({ nextIsShop: true })
    } else {
      that.data.officeIds.pop();
      that.setData({ nextIsShop: false, })
    }
    that.setData({
      isDepot: false, 'formData.isDetail': false, productTypeDetail: null, 'formData.depotId': null,
      'formData.officeId': that.data.officeIds[that.data.officeIds.length - 1]
    })

    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function (res) {
        that.pageRequest();
      }
    })
  },
  bindDateChange: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'dateStart') {
      that.setData({ "formData.dateStart": e.detail.value });
    } else {
      that.setData({ "formData.dateEnd": e.detail.value });
    }
  },
  bindSumType: function (e) {
    var that = this;
    that.setData({ 'formData.sumType': that.data.formProperty.sumTypeList[e.detail.value] })
  },
  bindOutType: function (e) {
    var that = this;
    that.setData({ 'formData.outType': that.data.formProperty.outTypeList[e.detail.value] })
  },
  switchChange: function (e) {
    console.log("eeee", e.detail.value)
    this.setData({ 'formData.scoreType': e.detail.value })
  },
  search: function () {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden })
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden, formData: e.detail.value, "formData.page": 0 });
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function (res) {
        that.pageRequest();
      }
    })
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