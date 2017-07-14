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
        console.log(">>>>>>>>>",res)
        if (res.data.success) {
          wx.navigateBack();
        } else if (res.data.message){
          that.setData({ "response.error": res.data.message, disabled: false })
        } else if (res.data.extra){
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
      timeStart = timeStart.split(':');
      timeEnd = timeEnd.split(':');
      var timeStartLong = timeStart[0] * 60 + 1.0 *timeStart[1];
      var timeEndLong = timeEnd[0] * 60+1.0*timeEnd[1];
      if(timeEndLong<timeStartLong){
        hour=0;
      }else{
        hour = parseInt((timeEndLong - timeStartLong) / 60)
        if ((timeEndLong - timeStartLong)%60>=30){
          hour=hour+0.5
        }
      }
      console.log(isNaN(hour))
      that.setData({
        'formData.hour':hour
      });
    
    }
  },
})