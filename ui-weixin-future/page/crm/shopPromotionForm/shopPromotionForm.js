var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    formProperty: {},
    response: {},
    submitDisabled: false,
    submitHidden: false,
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
      url: $util.getUrl("crm/shopPromotion/getForm"),
      data: {},
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
        console.log(res)
        that.setData({ 'formProperty.activityList': res.data.activityType })
      }
    })
    if (options.action == "update") {
      that.detail()
    } else if (options.action == "detail") {
      that.detail();
      that.setData({ submitHidden: !that.data.submitHidden })
    }
  },
  bindShop: function (e) {
    wx.navigateTo({
      url: '/page/crm/depotSearch/depotSearch?category=SHOP'
    })
  },
  bindActivityDate: function (e) {
    var that = this;
    that.setData({ "formData.activityDate": e.detail.value })
  },
  bindActivityType: function (e) {
    var that = this;
    that.setData({ 'formData.activityType': that.data.formProperty.activityList[e.detail.value] })
  },
  addImage: function (e) {
    var that = this;
    var activityImage = e.target.dataset.name;
    var images = that.data.formProperty[activityImage];
    if (!images) {
      images = new Array();
    }
    wx.chooseImage({
      count: 9,
      sizeType: ['original', 'compressed'],
      sourceType: ['camera', 'album'],
      success: function (res) {
        var tempFilePaths = res.tempFilePaths;
        for (var i in tempFilePaths) {
          wx.uploadFile({
            url: $util.getUrl('sys/folderFile/upload'),
            header: { 'x-auth-token': app.globalData.sessionId },
            filePath: tempFilePaths[i],
            name: 'file',
            formData: { uploadPath: 'shopPromotion' },
            success: function (res) {
              var folderFile = JSON.parse(res.data);
              images.push({
                id: folderFile[0].id,
                preview: $util.getUrl('sys/folderFile/preview?x-auth-token=' + app.globalData.sessionId + '&id=' + folderFile[0].id),
                view: $util.getUrl('sys/folderFile/view?x-auth-token=' + app.globalData.sessionId + '&id=' + folderFile[0].id)
              });
              that.data.formProperty[activityImage] = images;
              that.setData({ 'formProperty': that.data.formProperty })
            }
          })
        }
      }
    })
  },
  showImageActionSheet: function (e) {
    var that = this;
    var index = e.target.dataset.index;
    var activityImage = e.target.dataset.name;
    var itemList = ['预览', '删除'];
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          if (itemList[res.tapIndex] == '预览') {
            var images = that.data.formProperty[activityImage];
            wx.previewImage({
              current: images[index].view, // 当前显示图片的http链接
              urls: [images[index].view]
            })
          } else {
            that.data.formProperty[activityImage].splice(index, 1);
            that.setData({ 'formProperty': that.data.formProperty })
          }
        }
      }
    });
  },
  formSubmit: function (e) {
    var that = this;
    console.log(e.detail.value)
    that.setData({ submitDisabled: true });
    e.detail.value.activityImage1 = $util.getImageStr(that.data.formProperty.activityImage1,app.globalData.sessionId);
    e.detail.value.activityImage2 = $util.getImageStr(that.data.formProperty.activityImage2,app.globalData.sessionId);
    e.detail.value.activityImage3 = $util.getImageStr(that.data.formProperty.activityImage3,app.globalData.sessionId);
    wx.request({
      url: $util.getUrl("crm/shopPromotion/save"),
      data: e.detail.value,
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
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
    if (responseData && responseData.errors && responseData.errors[key] != null) {
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
      url: $util.getUrl("crm/shopPromotion/detail"),
      data: { id: that.data.options.id },
      method: 'GET',
      header: { 'x-auth-token': app.globalData.sessionId },
      success: function (res) {
        that.setData({ formData: res.data })
        that.setData({ 'formProperty.activityImage1': $util.getImages(res.data.activityImage1) })
        that.setData({ 'formProperty.activityImage2': $util.getImages(res.data.activityImage2) })
        that.setData({ 'formProperty.activityImage3': $util.getImages(res.data.activityImage3) })
      }
    })
  }
})