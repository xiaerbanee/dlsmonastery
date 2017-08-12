//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    productImeSearchResult: [],
    formData: { imeStr: '' },
    formProperty: {},
    response: {},
    shops: [],
    saleShopName: null,
    submitDisabled: false,
  },
  onLoad: function (option) {
    var that = this;
    app.autoLogin(function () {
      that.initPage()
    })
  },
  initPage: function () {
    var that = this;
    wx.request({
      url: $util.getUrl("ws/future/crm/productImeSale/getSaleForm"),
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ formProperty: res.data })
      }
    })
  },
  imeScan: function () {
    var that = this;
    wx.scanCode({
      success: function (res) {
        that.setData({ "formData.imeStr": res.result });
        that.imeSearch();
      }
    })
  },
  imeChange: function (e) {
    var that = this;
    that.setData({ "formData.imeStr": e.detail.value })
    that.imeSearch();
  },
  imeSearch: function () {
    var that = this;
    if ($util.isBlank(that.data.formData.imeStr)) {
      that.setData({ productImeSearchResult: {} })
    } else {
      wx.request({
        url: $util.getUrl("ws/future/crm/productImeSale/checkForSale"),
        data: that.data.formData,
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        success: function (res) {
          if (res.errMsg) {
            that.setData({ "response.error": res.data });
          }
          wx.request({
            url: $util.getUrl("ws/future/crm/productImeSale/findProductImeForSaleDto"),
            data: that.data.formData,
            header: {
              Cookie: "JSESSIONID=" + app.globalData.sessionId
            },
            success: function (res) {
              console.log(res.data)
              that.setData({ productImeSearchResult: res.data });
              wx.request({
                url: $util.getUrl('ws/future/basic/depot/shop'),
                data: {},
                method: 'GET',
                header: {
                  Cookie: "JSESSIONID=" + app.globalData.sessionId
                },
                success: function (res) {
                  console.log(res.data)
                  if(res.data.length==1){
                    that.setData({'formData.saleShopId': res.data[0].id,'formData.saleFormName':res.data[0].name})
                  }
                }
              })
            }
          })
        }
      });
    }
  },
  bindShop: function (e) {
    wx.navigateTo({
      url: '/page/crm/depotSearch/depotSearch?category=shop'
    })
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
      wx.request({
        url: $util.getUrl("ws/future/crm/productImeSale/saleIme"),
        data: e.detail.value,
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId,
        },
        success: function (res) {
          console.log(res)
          if (res.data.success) {
            wx.navigateBack();
          } else if (res.data.message) {
            that.setData({ submitDisabled:false,"response.error": res.data.message })
          } else {
            that.setData({ submitDisabled: false, "response.error": res.data });
          }
        }
      })
  },
  showError: function (e) {
    var that = this;
    var key = e.currentTarget.dataset.key;
    var responseData = that.data.response.data;
    if (responseData && responseData[key] != null) {
      that.setData({ "response.error": responseData[key].message });
      delete responseData[key];
      that.setData({ "response.data": responseData })
    } else {
      that.setData({ "response.error": '' })
    }
  }
})