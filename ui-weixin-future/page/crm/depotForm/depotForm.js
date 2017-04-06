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
        var options = that.data.options;
        if (options.action == 'edit') {
            wx.request({
                url: $util.getUrl("crm/depot/detail"),
                data: { id: options.id },
                header: { 'x-auth-token': app.globalData.sessionId },
                success: function (res) {
                    that.setData({ formData: res.data });
                    if (res.data.townId != null) {
                        that.setData({ "formData.town.name": res.data.town.provinceName + res.data.town.cityName + res.data.town.countyName+ res.data.town.townName })
                    }
                    wx.request({
                        url: $util.getUrl("crm/depot/getFormProperty"),
                        header: { 'x-auth-token': app.globalData.sessionId },
                        success: function (res) {
                            that.setData({ formProperty: res.data });
                            console.log(that.data.formProperty)
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
        that.setData({ "formData.areaType": that.data.formProperty.areaTypes[e.detail.value].name });
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
        that.setData({ "formData.bussinessCenterName": that.data.formProperty.businessCenterTypes[e.detail.value].name });
    },
    bindChannelTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.channelType":that.data.formProperty.channelTypes[e.detail.value].name});
    },
    bindSalePointTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.salePointType": that.data.formProperty.salePointTypes[e.detail.value].name });
    },
    bindSpecialityStoreTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.specialityStoreType": that.data.formProperty.specialityStoreTypes[e.detail.value] });
    },
    bindChainTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.chainType": that.data.formProperty.chainTypes[e.detail.value].name });
    },
    bindCarrierTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.carrierType": that.data.formProperty.carrierTypes[e.detail.value].name });
    },
    bindBusinessTypeChange: function (e) {
        var that = this;
        that.setData({ "formData.businessType": that.data.formProperty.businessTypes[e.detail.value].name });
    },
    bindAreaSizeChange: function (e) {
        var that = this;
        that.setData({ "formData.shopArea": that.data.formProperty.shopAreaTypes[e.detail.value].name });
    },
    bindShopMonthTotalChange: function (e) {
        var that = this;
        that.setData({ "shopAttribute.shopMonthTotal": that.data.shopAttribute.shopMonthTotalList[e.detail.value] });
    },
    formSubmit: function (e) {
        var that = this;
        that.setData({ submitDisabled: true });
        wx.request({
            url: $util.getUrl("crm/depot/save"),
            data: e.detail.value,
            header: { 'x-auth-token': app.globalData.sessionId },
            success: function (res) {
                if (res.data.success) {
                    wx.navigateBack();
                } else {
                    that.setData({ 'response.data': res.data, submitDisabled: false });
                    if(res.data.errors.shopAttributeList!=null ||res.data.errors.shopAttributeList!=''){
                        that.setData({ 'response.error': res.data.errors.shopAttributeList.message});
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