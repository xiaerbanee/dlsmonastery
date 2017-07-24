//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    weixinAccounts: null,
    weixinAccountsHidden: true,
    companyHidden: false,
    menuList: null,
    tip: "",
    companyNames: [],
  },
  onLoad: function (options) {
    let that = this;
    wx.request({
      url: $util.getUaaUrl('/user/getCompanyNames'),
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ companyNames: res.data})
      }
    })
  },
  onShow: function (options) {
    var that = this;
    if ($util.isNotBlank(app.globalData.companyName)) {
      app.autoLogin(function () {
        that.initPage();
      })
    }
  }, initPage: function () {
    var that = this;
    if (app.globalData.weixinAccounts.length > 1 && app.globalData.weixinAccount == null) {
      that.setData({ weixinAccountsHidden: false, weixinAccounts: app.globalData.weixinAccounts })
    } else {
      that.setData({ tip: "当前帐号：" + app.globalData.weixinAccount.loginName })
      that.setData({ weixinAccountsHidden: true })
      that.setData({ menuList: app.globalData.menuList })
    }
  }, login: function (event) {
    var that = this;
    var index = event.currentTarget.dataset.index;
    app.globalData.weixinAccount = that.data.weixinAccounts[index];
    that.setData({ weixinAccountsHidden: true })
    app.login(function () {
      that.initPage();
    });
    if (app.globalData.weixinAccount.accountName === 'xcxtest') {
      that.setData({ tip: "当前为测试帐号xcxtest，请绑定并登陆自己的真实帐号" })
    } else {
      that.setData({ tip: "当前帐号：" + app.globalData.weixinAccount.accountName })
    }
  },
  switchAccount: function (e) {
    var that = this;
    wx.request({
      url: $util.getUaaUrl('/user/logout'),
      method: 'POST',
      data: { companyName: app.globalData.companyName },
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function () {
        wx.request({
          url: $util.getUaaUrl('/logout'),
          data: { companyName: app.globalData.companyName },
          method: 'POST',
          header: {
            Cookie: "JSESSIONID=" + app.globalData.sessionId
          },
          success: function (res) {
            that.setData({ menuList: [] })
            app.globalData.menuList = null
            app.globalData.weixinAccount = null
            that.setData({ weixinAccountsHidden: false })
            wx.request({
              url: $util.getUaaUrl('user/getWeixinAccounts?weixinCode=' + app.globalData.weixinCode + "&companyName=" + app.globalData.companyName),
              method: 'POST',
              success: function (res) {
                app.globalData.weixinAccounts = res.data;
                that.setData({ weixinAccounts: res.data })
              },
            });
          },
        })
      }
    })
  },
  chooseCompany: function (e) {
    app.globalData.companyName = e.currentTarget.dataset.name;
    var that = this;
    if ($util.isNotBlank(app.globalData.companyName)) {
      that.setData({ companyHidden: true })
      app.autoLogin(function () {
        that.initPage();
      })
    }
  },
  logoutCompany:function(e){
    app.globalData.companyName=null;
    this.setData({ companyHidden: false, weixinAccountsHidden: true,tip:''});
  }
})