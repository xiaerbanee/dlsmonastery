//获取应用实例
var app = getApp();
var $util = require("../../../util/util.js");
Page({
  data: {
    page: {},
    formData: {},
    searchHidden: true,
    activeItem: null,
    auditList: ['待批(需要我审核)', '全部'],
    ifReload:false,
    auditType:'待批(需要我审核)'
  },
  onLoad: function (option) {
    var that = this;
    that.loadPage();
    that.setData({ height: $util.getWindowHeight() })
  },
  onShow: function () {
    var that = this;
    if(that.data.ifReload){
      that.loadPage();
    }
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
          "formData.auditType": that.data.auditType ? that.data.auditType : that.data.auditList[0],
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
            let deleted = wx.getStorageSync("authorityList").includes("hr:auditFile:delete");
            let content=res.data.content;
            for (let item in content) {
              let actionList = new Array();
              actionList.push("详细");
              if (content[item].auditable) { actionList.push("审核")}
              if (deleted && content[item].editable) { actionList.push("删除"); }
              res.data.content[item].actionList = actionList;
            }
            that.setData({ page: res.data });
            wx.hideToast();
            that.setData({ scrollTop: $util.toUpper() });
          }
        })
      }
    })
  },
  add: function () {
    this.setData({ifReload:true,auditType:this.data.formData.auditType})
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
    that.setData({ 'formData.auditType': that.data.auditList[e.detail.value]})
  },
  itemActive: function (e) {
    var that = this;
    var id = e.currentTarget.dataset.id;
    for (var index in that.data.page.content) {
      var item = that.data.page.content[index];
      if (item.id == id) {
        that.data.activeItem = item;
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
    if (itemList.length == 0) {
      return;
    }
    wx.showActionSheet({
      itemList: itemList,
      success: function (res) {
        if (!res.cancel) {
          if (itemList[res.tapIndex] == "审核") {
            that.setData({ ifReload: true, auditType: that.data.formData.auditType })
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
    wx.showToast({
      title: '加载中',
      icon: 'loading',
      duration: 10000,
      success: function (res) {
        that.pageRequest();
      }
    })  },
  loadPage(){
    var that=this;
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
})