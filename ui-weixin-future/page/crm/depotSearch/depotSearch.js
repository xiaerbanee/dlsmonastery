var app = getApp();
var $util = require("../../../util/util.js");
Page({
    data: {
        depotList: {},
        category:'',
        name:''
    },
    onLoad:function(options){
        var that=this; 
        if(options.category){
            that.setData({category:options.category})
        }
        that.depotSearch();
    },
    depotSelect: function (e) {
        var that=this;
        var value = e.detail.value;
        var indexOf = value.indexOf(",");
        var depotId=value.substring(0,indexOf);
        var depotName = value.substring(indexOf+1);
        var currentPages = getCurrentPages();
        var previousPage = currentPages[currentPages.length-2];
        previousPage.setData({'formData.shop.id':depotId,'formData.shop.name':depotName});
        wx.navigateBack({
          delta: 1
        })
    },
    depotSearch:function(){
        var that = this;
        wx.request({
          url:  $util.getUrl( 'crm/depot/search'),
          data: {category:that.data.category,name:that.data.name},
          method: 'GET', 
          header: {'x-auth-token': app.globalData.sessionId},
          success: function(res){
            that.setData({depotList:res.data})
          }
        })
    },
    inputName: function (e) {
        var that=this;
        var name = e.detail.value;
        if(name.length<2) {
            return;
        }
        that.setData({name:name})
        that.depotSearch();
    }
});