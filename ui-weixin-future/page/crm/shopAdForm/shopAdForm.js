// page/hr/dutyFreeForm/dutyFreeForm.js
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    formData: {},
    formProperty: { images: [] },
    response: {},
    submitDisabled: false,
    submitHidden: false,
    auditDisabled: false,
    auditHidden: false,
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
      url: $util.getUrl("ws/future/layout/shopAd/getForm"),
      data: {},
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ 'formProperty.shopAdTypeList': res.data.extra.shopAdTypeFormList })
      }
    })
    if (options.action == "update") {
      that.detail();
    } else if (options.action == "detail") {
      that.detail();
      that.setData({ submitHidden: true })
    } else if (options.action == "audit") {
      that.detail();
      that.setData({ auditHidden: true, submitHidden: true })
    }
  },
  bindAdType: function (e) {
    var that = this;
    that.setData({
      'formData.shopAdTypeId': that.data.formProperty.shopAdTypeList[e.detail.value].id,
      'formData.shopAdTypeName': that.data.formProperty.shopAdTypeList[e.detail.value].name
    })
  },
  bindShop: function (e) {
    wx.navigateTo({
      url: '/page/crm/depotSearch/depotSearch'
    })
  },
  switchChange: function (e) {
    var that = this;
    that.setData({ 'formData.specialArea': e.detail.value })
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
              urls: [that.data.formProperty.images[index].view],
            })
          } else {
            that.data.formProperty.images.splice(index, 1);
            that.setData({ "formProperty.images": that.data.formProperty.images });
          }
        }
      }
    });
  },
  addImage: function (e) {
    var that = this;
    var images = that.data.formProperty.images;
    wx.chooseImage({
      sizeType: ['compressed'],
      sourceType: ['camera'],
      success: function (res) {
        var tempFilePaths = res.tempFilePaths;
        for (var i in tempFilePaths) {
          wx.uploadFile({
            url: $util.getUrl('general/sys/folderFile/upload'),
            header: {
              Cookie: "JSESSIONID=" + app.globalData.sessionId
            },
            filePath: tempFilePaths[i],
            name: 'file',
            formData: {
              uploadPath: 'dutySign'
            },
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
  formSubmit: function (e) {
    var that = this;
    var event = e.detail.target.dataset.event;
    if (event == "submit") {
      e.detail.value.attachment = $util.getImageStr(that.data.formProperty.images, app.globalData.sessionId);
      that.setData({ submitDisabled: true });
      wx.request({
        url: $util.getUrl("ws/future/layout/shopAd/save"),
        data: e.detail.value,
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        success: function (res) {
          console.log(res.data)
          if (res.data.success) {
            wx.navigateBack();
          } else {
            that.setData({ "response.error": res.data.message })
            that.setData({ 'response.data': res.data.extra.errors, submitDisabled: false });
          }
        }
      })
    } else if (event == "audit") {
      that.setData({ auditDisabled: true });
      wx.request({
        url: $util.getUrl("ws/future/layout/shopAd/audit"),
        data: e.detail.value,
        header: {
          Cookie: "JSESSIONID=" + app.globalData.sessionId
        },
        success: function (res) {
          if (res.data.success) {
            wx.navigateBack();
          } else {
            that.setData({ auditDisabled: false })
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
  detail: function (e) {
    var that = this;
    wx.request({
      url: $util.getUrl("ws/future/layout/shopAd/findOne"),
      data: { id: that.data.options.id },
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ formData: res.data })
        console.log(">>>>>>", res.data.attachment)
        wx.request({
          url: $util.getUrl('general/sys/folderFile/findByIds?ids=' + res.data.attachment),
          data: {},
          header: {
            Cookie: "JSESSIONID=" + app.globalData.sessionId
          },
          method: 'GET',
          success: function (res) {
            var images = new Array();
            var folderFile = res.data;
            for (let i in folderFile) {
                console.log('i:',i);
                console.log('id:', folderFile[i].id);
                $util.downloadFile(images, folderFile[i].id, app.globalData.sessionId, 9, function () {
                  console.log(images)
                  that.setData({ "formProperty.images": images });
                });
            }
          }
        })
      }
    })
  }
})