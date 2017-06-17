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
    activeIndex: "1",
    sliderLeft: 0
  },
  onLoad: function (options) {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          sliderLeft: res.windowWidth - (res.windowWidth / that.data.tabs.length - sliderWidth) / 2 - sliderWidth
        });
      }
    });
    that.setData({
      "parentIdArr[0]": '',
      "formData.reportDateStart": $util.formatLocalDate($util.getFirstDayOfMonth(new Date())),
      "formData.reportDateEnd": $util.formatLocalDate(new Date),
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
    var formData = that.data.formData;
    var parentIdArr = that.data.parentIdArr;
    console.log(parentIdArr)
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function () {
        wx.request({
          url: $util.getUrl("ws/future/crm/imeSaleReport/productInventory"),
          data: {
            productTypeId: parentIdArr[parentIdArr.length - 1],
            reportDateStart: formData.reportDateStart,
            reportDateEnd: formData.reportDateEnd
          },
          method: 'GET',
          header: { 'x-auth-token': app.globalData.sessionId,
          'authorization': "Bearer" + wx.getStorageSync('token').access_token},
          success: function (res) {
            if (that.data.parentIdArr.length > 1) {
              that.setData({ hideButton: true })
            } else {
              that.setData({ hideButton: false })
            }
            that.setData({ page: res.data });
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
    if(e.currentTarget.dataset.id==0){
          wx.redirectTo({
      url: '/page/crm/officeDataReport/officeDataReport'
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
    var parentIdArr = that.data.parentIdArr;
    var id=e.currentTarget.dataset.id;
    var name=e.currentTarget.dataset.name;
    if(parentIdArr.length == 1){
      that.data.parentIdArr.push(id);
      that.pageRequest();
    }else{
      wx.navigateTo({
        url: '/page/crm/officeDataReport/officeDataReport?productName='+name+'&reportDateStart='+that.data.formData.reportDateStart+'&reportDateEnd='+that.data.formData.reportDateEnd
      })
    }
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden, formData: e.detail.value });
    that.pageRequest();
  },
})