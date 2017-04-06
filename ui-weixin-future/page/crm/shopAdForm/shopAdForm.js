// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {
      specialArea:false,
    },
    formProperty:{},
    response:{},
    submitDisabled:false,
    submitHidden:false,
    auditDisabled:false,
    auditHidden:false,
    options:null
  },
  onLoad: function (options) {
    var that = this;
    that.data.options = options
    app.autoLogin(function () {
      that.initPage()
    })
  },
  initPage: function () {
    var that = this;
    var options = that.data.options;
    wx.request({
      url: $util.getUrl("crm/shopAd/getFormProperty"),
      data: {},
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function(res){
        that.setData({'formProperty.shopAdTypeList':res.data.shopAdTypes})
      }
    })
    if (options.action == "update") {
      that.detail();
    }else if(options.action=="detail"){
      that.detail();
      that.setData({submitHidden:true})
    }else if(options.action =="audit"){
      that.detail();
      that.setData({auditHidden:true,submitHidden:true})
    }
  },
  bindAdType: function (e) {
    var that = this;
    that.setData({
      'formData.shopAdType.id': that.data.formProperty.shopAdTypeList[e.detail.value].id,
      'formData.shopAdType.name': that.data.formProperty.shopAdTypeList[e.detail.value].name
    })
  },
  bindShop:function(e){
    wx.navigateTo({
      url: '/page/crm/depotSearch/depotSearch'
    })
  },
  switchChange: function(e) {
    var that=this;
    that.setData({'formData.specialArea':e.detail.value})
  },
  formSubmit: function (e) {
    var that = this;
    var event=e.detail.target.dataset.event;
    if(event=="submit"){
      that.setData({ submitDisabled: true });
      wx.request({
      url: $util.getUrl("crm/shopAd/save"),
      data: e.detail.value,
      header: {'x-auth-token': app.globalData.sessionId},
      success: function (res) {
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ 'response.data': res.data ,submitDisabled: false });
        }
      }
    })
    }else if(event=="audit"){
      that.setData({ auditDisabled: true });
      wx.request({
        url: $util.getUrl("crm/shopAd/audit"),
        data: e.detail.value,
        header: {'x-auth-token': app.globalData.sessionId},
        success: function(res){
         if (res.data.success) {
           wx.navigateBack();
          }else{
            that.setData({auditDisabled:false})
          }
        }
      })
    }

  },
  showError: function (e) {
    var that = this;
    var key = e.currentTarget.dataset.key;
    var responseData = that.data.response.data;
    if(responseData && responseData.errors && responseData.errors[key] != null) {
      that.setData({ "response.error": responseData.errors[key].message });
      delete responseData.errors[key];
      that.setData({ "response.data": responseData })
    } else {
      that.setData({ "response.error": '' })
    }
  },
  detail:function(e){
    var that=this;
    wx.request({
        url: $util.getUrl("crm/shopAd/detail"),
        data: { id: that.data.options.id },
        method: 'GET',
        header: { 'x-auth-token': app.globalData.sessionId },
        success: function (res) {
          that.setData({
             formData:res.data.shopAd,
            'formData.shop.name': res.data.shopAd.shop.name,
            'formData.shopAdType.name':res.data.shopAd.shopAdType.name
          })
        }
      })
  }
})