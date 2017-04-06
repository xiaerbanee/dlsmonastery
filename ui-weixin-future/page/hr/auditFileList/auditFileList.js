//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    page: {},
    formData: {
      pageNumber: 0,
      pageSize: 10
    },
    searchHidden: true,
    activeItem: null,
    auditList: [{ id: '0', name: '待批(需要我审核)' }, { id: '1', name: '全部' }]
  },
  onLoad: function (option) {
    var that = this
    that.setData({
      "formData.auditTypeName": '待批(需要我审核)',
      "formData.needAuditByMe": '0',
       "formData.createdDateStart": $util.formatLocalDate($util.getFirstDayOfMonth(new Date())),
      "formData.createdDateEnd": $util.formatLocalDate(new Date),
    })
  },
  onShow: function () {
    var that = this;
    app.autoLogin(function () {
      that.initPage()
    });
  },
  initPage: function () {
    var that = this;
    that.pageRequest();
  },
  pageRequest: function () {
    var that = this;
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function (res) {
        wx.request({
          url: $util.getUrl("hr/auditFile"),
          header: { 'x-auth-token': app.globalData.sessionId },
          data: that.data.formData,
          success: function (res) {
            console.log(res.data);
            for (var item in res.data.content) {
              if(res.data.content[item].content){
                res.data.content[item].content = res.data.content[item].content.replace(/\[([^\]]+)\]/g)
              }
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
    that.setData({ "formData.pageNumber": 0 });
    that.pageRequest();
  },
  toPreviousPage: function () {
    var that = this;
    that.setData({ "formData.pageNumber": $util.getPreviousPageNumber(that.data.formData.pageNumber) });
    that.pageRequest();
  },
  toNextPage: function () {
    var that = this;
    that.setData({ "formData.pageNumber": $util.getNextPageNumber(that.data.formData.pageNumber, that.data.page.totalPages) });
    that.pageRequest();
  },
  toLastPage: function () {
    var that = this;
    that.setData({ "formData.pageNumber": that.data.page.totalPages - 1 });
    that.pageRequest();
  },
  toPage: function () {
    var that = this;
    var itemList = $util.getPageList(that.data.formData.pageNumber, that.data.page.totalPages);
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          that.setData({ "formData.pageNumber": itemList[res.tapIndex] - 1 });
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
    that.setData({ 'formData.needAuditByMe': that.data.auditList[e.detail.value].id, 'formData.auditTypeName': that.data.auditList[e.detail.value].name })
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
    if (!itemList) {
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
              url: $util.getUrl("hr/auditFile/delete"),
              data: { id: id },
              header: { 'x-auth-token': app.globalData.sessionId },
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
    that.setData({ searchHidden: !that.data.searchHidden, formData: that.data.formData, "formData.pageNumber": 0 });
    console.log(that.data.formData.auditTypeName)
    that.pageRequest();
  }
})