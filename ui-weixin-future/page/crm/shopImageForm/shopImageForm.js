// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {
    },
    formProperty:{},
    response:{},
    submitDisabled: false,
    submitHidden: false,
    options: null
  },
  onLoad: function (options) {
    var that = this;
    that.data.options = options
    app.autoLogin(function () {
      that.initPage()
    })
  },
  initPage:function(){
    var that = this;
    var options=that.data.options;
    wx.request({
      url: $util.getUrl("crm/shopImage/getFormProperty"),
      data: {},
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
        console.log(res);
        that.setData({ 'formProperty.imageTypeList': res.data.shopImageTypes })
      }
    })
    if (options.action == "update") {
      that.detail();
    } else if (options.action == "detail") {
      that.detail();
      that.setData({ submitHidden: !that.data.submitHidden });
    }
  },
  bindShop: function (e) {
    wx.navigateTo({
      url: '/page/crm/depotSearch/depotSearch?category=SHOP'
    })
  },
  bindImageType: function (e) {
    var that = this;
    that.setData({ 'formData.imageType': that.data.formProperty.imageTypeList[e.detail.value] })
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
        for (var i in tempFilePaths) {
          wx.uploadFile({
            url: $util.getUrl('sys/folderFile/upload'),
            header: {
              'x-auth-token': app.globalData.sessionId
            },
            filePath: tempFilePaths[i],
            name: 'file',
            formData: {
              uploadPath: 'shopImage'
            },
            success: function (res) {
              var folderFile = JSON.parse(res.data);
              images.push({
                id: folderFile[0].id,
                preview: $util.getUrl('sys/folderFile/preview?x-auth-token=' + app.globalData.sessionId +  '&id=' + folderFile[0].id),
                view: $util.getUrl('sys/folderFile/view?x-auth-token=' + app.globalData.sessionId + '&id=' + folderFile[0].id)
              })
              that.setData({ 'formProperty.images': images })
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
              urls: [that.data.formProperty.images[index].view]
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
     e.detail.value.image = $util.getImageStr(that.data.formProperty.images,app.globalData.sessionId);
    wx.request({
      url: $util.getUrl("crm/shopImage/save"),
      data: e.detail.value,
      header: {'x-auth-token': app.globalData.sessionId},
      success: function (res) {
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ 'response.data': res.data , submitDisabled: false});
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
  detail: function () {
    var that = this;
    wx.request({
      url: $util.getUrl("crm/shopImage/detail"),
      data: { id: that.data.options.id },
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
         that.setData({ formData: res.data })
         that.setData({ 'formProperty.images': $util.getImages(res.data.images) })
      }
    })
  }
})