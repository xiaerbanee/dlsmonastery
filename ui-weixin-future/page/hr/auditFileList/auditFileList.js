//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    page: {},
    formData: {},
    searchHidden: true,
    activeItem: null,
    auditList: ['待批(需要我审核)', '全部']
  },
  onLoad: function (option) {
    var that = this;
    that.setData({ height: $util.getWindowHeight() })
  },
  onShow: function () {
    let that = this;
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function (res) {
        app.autoLogin(function () {
          that.initPage()
        });
      }
    })
  },
  initPage: function () {
    var that = this;
    wx.request({
      url: $util.getUrl("basic/hr/auditFile/getQuery"),
      data: {},
      method: 'GET',
      header: {
        Cookie: "JSESSIONID=" + app.globalData.sessionId
      },
      success: function (res) {
        that.setData({ formData: res.data });
        that.setData({
          "formData.auditType": '待批(需要我审核)',
          "formData.createdDateStart": $util.formatLocalDate($util.getFirstDayOfMonth(new Date())),
          "formData.createdDateEnd": $util.formatLocalDate(new Date),
        })
        that.pageRequest();
      }
    })
  },
  pageRequest: function () {
    var that = this;
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function (res) {
        wx.request({
          url: $util.getUrl("basic/hr/auditFile"),
          header: { Cookie: "JSESSIONID=" + app.globalData.sessionId },
          data: $util.deleteExtra(that.data.formData),
          success: function (res) {
            console.log(res.data)
            let deleted = wx.getStorageSync("authorityList").includes("crm:depot:delete");
            for (let item in res.data.content) {
              let actionList = new Array();
              actionList.push("详细","审核");
              if (deleted) { actionList.push("删除"); }
              res.data.content[item].actionList = actionList;
            }
            that.setData({ page: res.data });
            wx.hideToast();
          }
        })
      }
    })
  },
  toFirstPage: function () {
    var that = this;
    that.setData({ "formData.page": 0 });
    that.pageRequest();
  },
  toPreviousPage: function () {
    var that = this;
    that.setData({ "formData.page": $util.getPreviousPageNumber(that.data.formData.page) });
    that.pageRequest();
  },
  toNextPage: function () {
    var that = this;
    that.setData({ "formData.page": $util.getNextPageNumber(that.data.formData.page, that.data.page.totalPages) });
    that.pageRequest();
  },
  toLastPage: function () {
    var that = this;
    that.setData({ "formData.page": that.data.page.totalPages - 1 });
    that.pageRequest();
  },
  toPage: function () {
    var that = this;
    var itemList = $util.getPageList(that.data.formData.page, that.data.page.totalPages);
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          that.setData({ "formData.page": itemList[res.tapIndex] - 1 });
          that.pageRequest();
        }
      }
    });
  },
  add: function () {
    wx.navigateTo({
      url: '/page/hr/auditFileForm/auditFileForm'
    })
  },
  search: function () {
    var that = this;
    that.setData({
      searchHidden: !that.data.searchHidden
    })
  },
  bindAuditType: function (e) {
    var that = this;
    that.setData({ 'formData.auditType': that.data.auditList[e.detail.value] })
  },
  itemActive: function (e) {
    var that = this;
    var id = e.currentTarget.dataset.id;
    for (var index in that.data.page.content) {
      var item = that.data.page.content[index];
      if (item.id == id) {
        that.data.activeItem = item;
      }
      if (item.id == id && item.hasOwnProperty('actionList')) {
        item.active = true;
      } else {
        item.active = false;
      }
    }
    that.setData({ page: that.data.page });
  },
  showActionSheet: function (e) {
    var that = this;
    var id = e.currentTarget.dataset.id;
    var itemList = that.data.activeItem.actionList;
    if (itemList.length==0) {
      return;
    }
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          if (itemList[res.tapIndex] == "审核") {
            wx.navigateTo({
              url: '/page/hr/auditFileDetail/auditFileDetail?action=audit&id=' + id
            })
          } else if (itemList[res.tapIndex] == "详细") {
            wx.navigateTo({
              url: '/page/hr/auditFileDetail/auditFileDetail?action=detail&id=' + id
            })
          } else if (itemList[res.tapIndex] == "删除") {
            wx.request({
              url: $util.getUrl("basic/hr/auditFile/delete"),
              data: { id: id },
              header: {
                Cookie: "JSESSIONID=" + app.globalData.sessionId
              },
              success: function (res) {
                that.pageRequest();
              }
            })
          }
        }
      }
    })
  },
  bindDateChange: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'createdDateStart') {
      that.setData({
        "formData.createdDateStart": e.detail.value
      });
    } else {
      that.setData({
        "formData.createdDateEnd": e.detail.value
      });
    }
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ searchHidden: !that.data.searchHidden, formData: that.data.formData, "formData.page": 0 });
    that.pageRequest();
  }
})