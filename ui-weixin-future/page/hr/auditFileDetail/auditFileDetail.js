var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    submitHidden: false,
    submitDisabled: false,
    options: null
  },
  onLoad: function (options) {
    var that = this;
    that.data.options = options
    app.autoLogin(function(){
      that.initPage();
    })
  },

  initPage: function () {
    var that = this;
    var options=that.data.options;
    wx.request({
      url: $util.getUrl("basic/hr/auditFile/detail"),
      data: { id: options.id },
      method: 'GET',
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId},
      success: function (res) {
        that.setData({ formData: res.data.auditFile })
        var contentImage = $util.getUrl("basic/hr/auditFile/view?x-auth-token=" + app.globalData.sessionId + "&id=" + options.id+"&authorization=Bearer" + wx.getStorageSync('token').access_token);
        that.setData({ "formData.contentImage": contentImage })
      }
    })
    if (options.action == "detail") {
      that.setData({ submitHidden: true })
    } else if (options.action == "audit") {
      that.setData({ submitHidden: false })
    }
  },
  showImageActionSheet: function (e) {
    var that = this;
    var index = e.target.dataset.index;
    var itemList = ['预览'];
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          if (itemList[res.tapIndex] == '预览') {
            wx.previewImage({
              current: that.data.formData.contentImage, // 当前显示图片的http链接
              urls: [that.data.formData.contentImage]
            })
          }
        }
      }
    });
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true })
    wx.request({
      url: $util.getUrl("basic/hr/auditFile/audit"),
      data: e.detail.value,
      header: { 
        Cookie: "JSESSIONID=" + app.globalData.sessionId
       },
      success: function (res) {
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ restResponse: res.data, submitDisabled: false });
        }
      }
    })
  }
})