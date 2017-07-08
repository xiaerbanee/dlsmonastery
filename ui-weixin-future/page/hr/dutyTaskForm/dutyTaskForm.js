var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    submitDisabled: false,
    response: {},
    options: null
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
      url: $util.getUrl("basic/hr/duty/getForm"),
      data: { id: options.id, dutyType: options.dutyType },
      method: 'GET',
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        console.log(res.data.item)
        that.setData({
          dutyType: res.data.dutyType,
          formData: res.data.item
        })
        if (that.data.dutyType == "签到") {
          var images = new Array();
          $util.downloadFile(images, res.data.attachment, app.globalData.sessionId, 9, function () {
            that.setData({ "formProperty.images": images });
          });
        }
      }
    })
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
              current: that.data.formProperty.images[index].view, // 当前显示图片的http链接
              urls: [that.data.formProperty.images[0].view]
            })
          }
        }
      }
    });
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
    wx.request({
      url: $util.getUrl("hr/duty/audit"),
      data: e.detail.value,
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ 'response.data': res.data, submitDisabled: false });
        }
      }
    })
  }
})