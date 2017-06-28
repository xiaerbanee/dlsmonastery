//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    userInfo: [],
    disabled: false,
    code: "",
  },
  onLoad: function () {
    var that = this
    app.getUserInfo(function (userInfo) {
      that.setData({
        userInfo: userInfo
      })
    });
    wx.login({
      success: function (res) {
        that.setData({
          code: res.code
        })
      }
    })
  },
  formSubmit: function (e) {
    var that = this;
    if (e.detail.value.loginName == "" || e.detail.value.password == "") {
      wx.showModal({
        content: "请输入用户名及密码",
        showCancel: false
      });
      return;
    }
    that.setData({ disabled: true });
    wx.request({
      url: "http://localhost:1200/user/bind",
      data: {
        loginName: e.detail.value.loginName,
        password: e.detail.value.password,
        code: that.data.code
      },
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        if (res.data.success) {
          wx.showModal({
            title: "小提示",
            content: res.data.message,
            showCancel: false,
            success: function (res) {
              that.setData({ disabled: false });
              wx.switchTab({ url: '/page/sys/home/home' })
            }
          })
        } else {
          wx.showModal({
            title: "小提示",
            content: res.data.message,
            showCancel: false,
            success: function (res) {
              that.setData({ disabled: false });
            }
          });
        }
      }
    })
  }
})