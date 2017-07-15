var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    formProperty: { images: [] },
    response: {},
    submitDisabled: false,
    options: null
  },
  onLoad: function (options) {
    var that = this;
    that.setData({options:options});
    app.autoLogin(function () {
      that.initPage()
    })
  },
  initPage: function () {
    var that = this;
    var options = that.data.options;
    that.detail()
  },
  addImage: function (e) {
    var that = this;
    var images = that.data.formProperty.images;
    wx.chooseImage({
      count: 9,
      sizeType: ['compressed', 'original'],
      sourceType: ['camera', 'album'],
      success: function (res) {
        var tempFilePaths = res.tempFilePaths
        for (let i in tempFilePaths) {
          wx.uploadFile({
            url: $util.getUrl('general/sys/folderFile/upload'),
            header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
            filePath: tempFilePaths[i],
            name: 'file',
            formData: { uploadPath: 'priceChangeIme' },
            success: function (res) {
              var folderFile = JSON.parse(res.data)[0];
              $util.downloadFile(images, folderFile.id, app.globalData.sessionId, 9, function () {
                that.setData({ "formProperty.images": images });
              });
            }
          })
        }
      }
    })
  },
  showImageActionSheet: function (e) {
    var that = this;
    var index = e.target.dataset.index;
    var itemList = ['预览', '删除'];
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          if (itemList[res.tapIndex] == '预览') {
            wx.previewImage({
              current: that.data.formProperty.images[index].view, // 当前显示图片的http链接
              urls: [that.data.formProperty.images[0].view]
            })
          } else {
            that.data.formProperty.images.splice(index, 1);
            that.setData({ 'formProperty.images': that.data.formProperty.images });
          }
        }
      }
    });
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ submitDisabled: true });
    let action = e.detail.target.dataset.action;
    e.detail.value.image = $util.getImageStr(that.data.formProperty.images);
    if (action=="audit"){
      wx.request({
        url: $util.getUrl("ws/future/crm/priceChangeIme/audit"),
        data: e.detail.value,
        header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
        success: function (res) {
          if (res.data.success) {
            wx.navigateBack();
          } else {
            that.setData({ 'response.data': res.data.extra.errors, submitDisabled: false });
          }
        }
      })
    } else if (action =="upload"){
      wx.request({
        url: $util.getUrl("ws/future/crm/priceChangeIme/imageUpload"),
        data: e.detail.value,
        header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
        success: function (res) {
          if (res.data.success) {
            wx.navigateBack();
          } else {
            that.setData({ 'response.data': res.data.extra.errors, submitDisabled: false });
          }
        }
      })
    }
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
  },
  detail(){
    var that=this;
    let options=that.data.options;
    wx.request({
      url: $util.getUrl("ws/future/crm/priceChangeIme/findOne"),
      data: { id: options.id },
      method: 'GET',
      header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
      success: function (res) {
        var images = new Array();
        that.setData({ formData: res.data })
        $util.downloadFile(images, res.data.image, app.globalData.sessionId, 9, function () {
          that.setData({ "formProperty.images": images });
        });
      }
    })
  }
})