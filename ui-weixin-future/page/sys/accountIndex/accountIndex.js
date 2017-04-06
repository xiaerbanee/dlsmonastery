var app = getApp();
Page({
  data: {
    userInfo: {}
  },
  onLoad: function () {
    var that = this
    //调用应用实例的方法获取全局数据
    app.getUserInfo(function (userInfo) {
      that.setData({
        userInfo: userInfo
      })
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
  },
  accountBind: function () {
    wx.navigateTo({
      url: '/page/sys/accountBind/accountBind'
    })
  }
})