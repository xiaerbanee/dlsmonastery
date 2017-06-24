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
  },
  bindDutyDate: function (e) {
    this.setData({
      "formData.dutyDate": e.detail.value
    })
  },
  bindTimeChange: function (e) {
    var that = this;
    var name = e.currentTarget.dataset.name;
    if (name == 'timeStart') {
      that.setData({ "formData.timeStart": e.detail.value });
      that.getHour();
    } else {
      that.setData({ "formData.timeEnd": e.detail.value });
      that.getHour();
    }
  },
  formSubmit: function (e) {
    var that = this;
    that.setData({ disabled: true });
    wx.request({
      url: $util.getUrl("basic/hr/dutyOvertime/save"),
      data: e.detail.value,
      header: {
Cookie:"JSESSIONID="+app.globalData.sessionId
      },
      success: function (res) {
        console.log(res)
        if (res.data.success) {
          wx.navigateBack();
        } else {
          that.setData({ 'response.data': res.data.extra.errors, disabled: false });
        }
      }
    })
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
  getHour: function (e) {
    var that = this;
    var hour = 0.0;
    var timeStart = that.data.formData.timeStart;
    var timeEnd = that.data.formData.timeEnd;
    var dutyDate = that.data.formData.dutyDate;
    if ($util.isNotBlank(timeStart) && $util.isNotBlank(timeEnd)) {
      var dateTimeStart = new Date((dutyDate + " " + timeStart)).getTime();
      var dateTimeEnd = new Date((dutyDate + " " + timeEnd)).getTime();
      hour = (dateTimeEnd - dateTimeStart) * 1.0 / (3600 * 1000);
      if (hour - Math.floor(hour) < 0.5) {
        hour = Math.floor(hour);
        that.setData({ 'formData.hour': Math.floor(hour) })
      } else {
        that.setData({ 'formData.hour': Math.floor(hour) + 0.5 })
      }
    }
  },
})