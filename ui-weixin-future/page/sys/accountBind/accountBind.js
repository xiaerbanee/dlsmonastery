//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    userInfo: [],
    companyNames: null,
    companyIndex: null,
    disabled: false
  },
  onLoad: function () {
    var that = this
    app.getUserInfo(function (userInfo) {
      that.setData({
        userInfo: userInfo
      })
    });

    //获取所有公司
    wx.request({
      url: $util.getUrl("login/companyNames"),
      header: {
        'x-auth-token': app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ companyNames: res.data });
      }
    })
  },
  bindCompanyChange: function (e) {
    this.setData({
      companyIndex: e.detail.value
    })
  },
  formSubmit: function (e) {
    var that = this;
    if(e.detail.value.companyName == "" || e.detail.value.loginName == "" || e.detail.value.password=="") {
        wx.showModal({
          content: "请输入公司，用户名及密码",
          showCancel: false
        });
        return;
    }
    that.setData({ disabled: true });
    wx.request({
      url: $util.getUrl("login/accountBind"),
      data: {companyName:e.detail.value.companyName,
             loginName: e.detail.value.loginName,
             password: e.detail.value.password,
             code: app.globalData.weixinCode},
      header: {
        'x-auth-token': app.globalData.sessionId
      },
      success: function (res) {
        if(res.data.success) {
         wx.navigateBack({delta: 10})
        } else {
          wx.showModal({
            title:"绑定失败",
            content: res.data.message,
            showCancel: false,
            success: function (res) {
              if (res.confirm) {
                that.setData({ disabled: false });
              }
            }
          });
        }
      }
    })
  }
})