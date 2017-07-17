var app = getApp();
var $util = require("../../../util/util.js");
var WxParse = require('../../../util/wxParse/wxParse.js');
Page({
  data: {
    formData: {},
    submitHidden: false,
    submitDisabled: false,
    response:'',
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
      url: $util.getUrl("basic/hr/auditFile/findOne"),
      data: { id: options.id },
      method: 'GET',
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId},
      success: function (res) {
        console.log(res.data)
        that.setData({ formData: res.data });
        if(res.data.content!=null){
          WxParse.wxParse('content', 'html', res.data.content, that, 5);
        }
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
        console.log(res)
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ 'response.error': res.data, submitDisabled: false });
        }
      }
    })
  }
})