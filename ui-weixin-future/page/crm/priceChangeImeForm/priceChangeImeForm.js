var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    formProperty:{},
    response:{},
    submitDisabled: false,
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
    if (options.action == "uploadImage") {
      wx.request({
        url: $util.getUrl("crm/priceChangeIme/detail"),
        data: { id: options.id },
        method: 'GET',
        header: { 'x-auth-token': app.globalData.sessionId },
        success: function (res) {
          that.setData({ formData: res.data })
        },
      })
    }
  },
  addImage: function (e) {
    var that = this;
    var images = that.data.formProperty.images;
    if (!images) {
      images = new Array();
    }
    wx.chooseImage({
      count: 9,
      sizeType: ['compressed','original'],
      sourceType: ['camera','album'],
      success: function (res) {
        var tempFilePaths = res.tempFilePaths
        wx.uploadFile({
          url: $util.getUrl('sys/folderFile/upload'),
          header: { 'x-auth-token': app.globalData.sessionId },
          filePath: tempFilePaths[0],
          name: 'file',
          formData: { uploadPath: 'priceChangeIme' },
          success: function (res) {
            var folderFile = JSON.parse(res.data)[0];
            images.push({
              id: folderFile.id,
              preview: $util.getUrl('sys/folderFile/preview?x-auth-token=' + app.globalData.sessionId +  '&id=' + folderFile.id),
              view: $util.getUrl('sys/folderFile/view?x-auth-token=' + app.globalData.sessionId + '&id=' + folderFile.id)
            })
            that.setData({ 'formProperty.images': images })
          }
        })
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
    e.detail.value.image = $util.getImageStr(that.data.formProperty.images);
    wx.request({
      url: $util.getUrl("crm/priceChangeIme/imageUpload"),
      data: e.detail.value,
      header: {  'x-auth-token': app.globalData.sessionId  },
      success: function (res) {
        console.log(res.data)
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ 'response.data': res.data, submitDisabled: false });
        }
      }
    })
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
})