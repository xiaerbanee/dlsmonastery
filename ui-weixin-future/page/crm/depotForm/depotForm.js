var $util = require("../../../util/util.js");
var app = getApp();
Page({
    data: {
        formData: {},
        formProperty: {},
        response: {},
        submitDisabled: false,
        options: null
    },
    onLoad: function (options) {
        var that = this;
        that.data.options = options;
        app.autoLogin(function () {
            that.initPage()
        })
    },
    initPage: function () {
        var that = this;
        that.setData({ shopAttributeShow: false });
        var options = that.data.options;
        if (options.action == 'edit') {
            wx.request({
                url: $util.getUrl("ws/future/basic/depotShop/findOne"),
                data: { id: options.id },
                header: {
                    Cookie: "JSESSIONID=" + app.globalData.sessionId
                },
                success: function (res) {
                    console.log(res.data)
                    that.setData({ formData: res.data });
                    wx.request({
                        url: $util.getUrl("ws/future/basic/depotShop/getForm"),
                        header: {
                            Cookie: "JSESSIONID=" + app.globalData.sessionId
                        },
                        success: function (res) {
                            that.setData({ formProperty: res.data });
                        }
                    })
                }
            })
        }
    },
    bindTown: function (e) {
        wx.navigateTo({
            url: '/page/sys/townSearch/townSearch'
        })
    },
    bindAreaTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.areaType": that.data.formProperty.extra.areaList[e.detail.value].name });
    },
    bindBussinessCenterChange: function (e) {
        var that = this;
        that.setData({ "formData.bussinessCenter": e.detail.value });
        if (!that.data.formData.bussinessCenter) {
            that.setData({ "formData.bussinessCenterName": '' });
        }
    },
    bussinessCenterNameChange: function (e) {
        var that = this;
        that.setData({ "formData.bussinessCenterName": that.data.formProperty.extra.businessCenterList[e.detail.value].name });
    },
    bindChannelTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.channelType": that.data.formProperty.extra.channelList[e.detail.value].name });
    },
    bindSalePointTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.salePointType": that.data.formProperty.extra.salePointList[e.detail.value].name });
    },
    bindSpecialityStoreTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.specialityStoreType": that.data.formProperty.extra.specialityStoreList[e.detail.value] });
    },
    bindTurnoverTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.turnoverType": that.data.formProperty.extra.turnoverList[e.detail.value].name });
    },
    bindCarrierTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.carrierType": that.data.formProperty.extra.carrierList[e.detail.value].name });
    },
    bindBusinessTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.businessType": that.data.formProperty.extra.businessList[e.detail.value].name });
    },
    bindAreaSizeChange: function (e) {
        var that = this;
        that.setData({ "formData.shopArea": that.data.formProperty.extra.shopAreaList[e.detail.value].name });
    },
    bindShopMonthTotalChange: function (e) {
        var that = this;
        that.setData({ "shopAttribute.shopMonthTotal": that.data.formProperty.extra.shopMonthTotalList[e.detail.value] });
    },
    formSubmit: function (e) {
        var that = this;
        that.setData({ submitDisabled: true });
        wx.request({
            url: $util.getUrl("ws/future/basic/depotShop/save"),
            data: e.detail.value,
            header: {
                Cookie: "JSESSIONID=" + app.globalData.sessionId
            },
            success: function (res) {
                if (res.data.success) {
                    wx.navigateBack();
                } else {
                    that.setData({ 'response.data': res.data, submitDisabled: false });
                    if (res.data.errors.shopAttributeList != null || res.data.errors.shopAttributeList != '') {
                        that.setData({ 'response.error': res.data.errors.shopAttributeList.message });
                    }
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
})