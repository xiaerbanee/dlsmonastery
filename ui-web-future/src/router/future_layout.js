const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const adApplyList = r => require.ensure([], () => r(require('../pages/layout/adApplyList.vue')));
const adApplyForm = r => require.ensure([], () => r(require('../pages/layout/adApplyForm.vue')));
const adApplyGoods = r => require.ensure([], () => r(require('../pages/layout/adApplyGoods.vue')));
const adApplyBillForm = r => require.ensure([], () => r(require('../pages/layout/adApplyBillForm.vue')));
const adApplyEditForm = r => require.ensure([], () => r(require('../pages/layout/adApplyEditForm.vue')));
const adGoodsOrderList = r => require.ensure([], () => r(require('../pages/layout/adGoodsOrderList.vue')));
const adGoodsOrderDetailList = r => require.ensure([], () => r(require('../pages/layout/adGoodsOrderDetailList.vue')));
const adGoodsOrderForm= r => require.ensure([], () => r(require('../pages/layout/adGoodsOrderForm.vue')));
const adGoodsOrderDetail= r => require.ensure([], () => r(require('../pages/layout/adGoodsOrderDetail.vue')));
const adGoodsOrderBill= r => require.ensure([], () => r(require('../pages/layout/adGoodsOrderBill.vue')));
const adGoodsOrderShip= r => require.ensure([], () => r(require('../pages/layout/adGoodsOrderShip.vue')));
const adGoodsOrderSign= r => require.ensure([], () => r(require('../pages/layout/adGoodsOrderSign.vue')));
const adGoodsOrderPrint= r => require.ensure([], () => r(require('../pages/layout/adGoodsOrderPrint.vue')));
const adPricesystemChangeList  = r => require.ensure([], () => r(require('../pages/layout/adPricesystemChangeList.vue')));
const adPricesystemChangeForm = r => require.ensure([], () => r(require('../pages/layout/adPricesystemChangeForm.vue')));
const shopAdDoorList= r => require.ensure([], () => r(require('../pages/layout/shopAdDoorList.vue')));
const shopAdDoorForm= r => require.ensure([], () => r(require('../pages/layout/shopAdDoorForm.vue')));
const shopAdDoorDetail= r => require.ensure([], () => r(require('../pages/layout/shopAdDoorDetail.vue')));
const shopAdList= r => require.ensure([], () => r(require('../pages/layout/shopAdList.vue')));
const shopAdForm= r => require.ensure([], () => r(require('../pages/layout/shopAdForm.vue')));
const shopAdDetail= r => require.ensure([], () => r(require('../pages/layout/shopAdDetail.vue')));
const shopAllotList = r => require.ensure([], () => r(require('../pages/layout/shopAllotList.vue')));
const shopAllotShipPrint = r => require.ensure([], () => r(require('../pages/layout/shopAllotShipPrint.vue')));
const shopAllotForm = r => require.ensure([], () => r(require('../pages/layout/shopAllotForm.vue')));
const shopAllotDetail = r => require.ensure([], () => r(require('../pages/layout/shopAllotDetail.vue')));
const shopBuildList  = r => require.ensure([], () => r(require('../pages/layout/shopBuildList.vue')));
const shopBuildForm  = r => require.ensure([], () => r(require('../pages/layout/shopBuildForm.vue')));
const shopBuildDetail  = r => require.ensure([], () => r(require('../pages/layout/shopBuildDetail.vue')));
const shopDepositList= r => require.ensure([], () => r(require('../pages/layout/shopDepositList.vue')));
const shopDepositForm= r => require.ensure([], () => r(require('../pages/layout/shopDepositForm.vue')));
const shopDepositBatchForm= r => require.ensure([], () => r(require('../pages/layout/shopDepositBatchForm.vue')));
const shopGoodsDepositList = r => require.ensure([], () => r(require('../pages/layout/shopGoodsDepositList.vue')));
const shopGoodsDepositForm = r => require.ensure([], () => r(require('../pages/layout/shopGoodsDepositForm.vue')));
const shopGoodsDepositBatchForm = r => require.ensure([], () => r(require('../pages/layout/shopGoodsDepositBatchForm.vue')));
const shopImageList= r => require.ensure([], () => r(require('../pages/layout/shopImageList.vue')));
const shopImageForm= r => require.ensure([], () => r(require('../pages/layout/shopImageForm.vue')));
const shopPrintList= r => require.ensure([], () => r(require('../pages/layout/shopPrintList.vue')));
const shopPrintForm= r => require.ensure([], () => r(require('../pages/layout/shopPrintForm.vue')));
const shopPrintDetail = r => require.ensure([], () => r(require('../pages/layout/shopPrintDetail.vue')));
const shopPromotionList= r => require.ensure([], () => r(require('../pages/layout/shopPromotionList.vue')));
const shopPromotionForm= r => require.ensure([], () => r(require('../pages/layout/shopPromotionForm.vue')));


let routes = [
    {
        path: '/',
        component: home,
        children: [
            {path:'/layout/adApplyList',component:adApplyList,name:'adApplyList'},
            {path:'/layout/adApplyForm',component:adApplyForm,name:'adApplyForm',meta: {menu:"adApplyList",keepAlive:true}},
            {path:'/layout/adApplyGoods',component:adApplyGoods,name:'adApplyGoods',meta: {menu:"adApplyList"}},
            {path:'/layout/adApplyBillForm',component:adApplyBillForm,name:'adApplyBillForm',meta: {menu:"adApplyList",keepAlive:true}},
            {path:'/layout/adApplyEditForm',component:adApplyEditForm,name:'adApplyEditForm',meta: {menu:"adApplyList",keepAlive:true}},
            {path:'/layout/adGoodsOrderList',component:adGoodsOrderList,name:'adGoodsOrderList'},
            {path:'/layout/adGoodsOrderDetailList',component:adGoodsOrderDetailList,name:'adGoodsOrderDetailList'},
            {path:'/layout/adGoodsOrderForm',component:adGoodsOrderForm,name:'adGoodsOrderForm',meta: {menu:"adGoodsOrderList",keepAlive:true}},
            {path:'/layout/adGoodsOrderDetail',component:adGoodsOrderDetail,name:'adGoodsOrderDetail',meta: {menu:"adGoodsOrderList"}},
            {path:'/layout/adGoodsOrderBill',component:adGoodsOrderBill,name:'adGoodsOrderBill',meta: {menu:"adGoodsOrderList"}},
            {path:'/layout/adGoodsOrderShip',component:adGoodsOrderShip,name:'adGoodsOrderShip',meta: {menu:"adGoodsOrderList"}},
            {path:'/layout/adGoodsOrderSign',component:adGoodsOrderSign,name:'adGoodsOrderSign',meta: {menu:"adGoodsOrderList"}},
            {path:'/layout/adGoodsOrderPrint',component:adGoodsOrderPrint,name:'adGoodsOrderPrint',meta: {hidden: true}},
            {path:'/layout/adPricesystemChangeList',component:adPricesystemChangeList,name:'adPricesystemChangeList'},
            {path:'/layout/adPricesystemChangeForm',component:adPricesystemChangeForm,name:'adPricesystemChangeForm',meta: {menu:"adPricesystemChangeList",keepAlive:true}},
            {path:'/layout/shopAdDoorList',component:shopAdDoorList,name:'shopAdDoorList'},
            {path:'/layout/shopAdDoorForm',component:shopAdDoorForm,name:'shopAdDoorForm',meta: {menu:"shopAdDoorList",keepAlive:true}},
            {path:'/layout/shopAdDoorDetail',component:shopAdDoorDetail,name:'shopAdDoorDetail',meta: {menu:"shopAdDoorList",keepAlive:true}},
            {path:'/layout/shopAdList',component:shopAdList,name:'shopAdList'},
            {path:'/layout/shopAdForm',component:shopAdForm,name:'shopAdForm',meta: {menu:"shopAdList",keepAlive:true}},
            {path:'/layout/shopAdDetail',component:shopAdDetail,name:'shopAdDetail',meta: {menu:"shopAdList"}},
            {path:'/layout/shopAllotList',component:shopAllotList,name:'shopAllotList'},
            {path:'/layout/shopAllotShipPrint',component:shopAllotShipPrint,name:'shopAllotShipPrint',meta: {hidden: true}},
            {path:'/layout/shopAllotForm',component:shopAllotForm,name:'shopAllotForm',meta: {menu:"shopAllotList",keepAlive:true}},
            {path:'/layout/shopAllotDetail',component:shopAllotDetail,name:'shopAllotDetail',meta: {menu:"shopAllotList"}},
            {path:'/layout/shopBuildList',component:shopBuildList,name:'shopBuildList'},
            {path:'/layout/shopBuildForm',component:shopBuildForm,name:'shopBuildForm',meta: {menu:"shopBuildList",keepAlive:true}},
            {path:'/layout/shopBuildDetail',component:shopBuildDetail,name:'shopBuildDetail',meta: {menu:"shopBuildList"}},
            {path:'/layout/shopDepositList',component:shopDepositList,name:'shopDepositList'},
            {path:'/layout/shopDepositForm',component:shopDepositForm,name:'shopDepositForm',meta: {menu:"shopDepositList",keepAlive:true}},
            {path:'/layout/shopDepositBatchForm',component:shopDepositBatchForm,name:'shopDepositBatchForm',meta: {menu:"shopDepositList",keepAlive:true}},
            {path:'/layout/shopGoodsDepositList',component:shopGoodsDepositList,name:'shopGoodsDepositList'},
            {path:'/layout/shopGoodsDepositForm',component:shopGoodsDepositForm,name:'shopGoodsDepositForm',meta: {menu:"shopGoodsDepositList",keepAlive:true}},
            {path:'/layout/shopGoodsDepositBatchForm',component:shopGoodsDepositBatchForm,name:'shopGoodsDepositBatchForm',meta: {menu:"shopGoodsDepositList",keepAlive:true}},
            {path:'/layout/shopImageList',component:shopImageList,name:'shopImageList'},
            {path:'/layout/shopImageForm',component:shopImageForm,name:'shopImageForm',meta: {menu:"shopImageList",keepAlive:true}},
            {path:'/layout/shopPrintList',component:shopPrintList,name:'shopPrintList'},
            {path:'/layout/shopPrintForm',component:shopPrintForm,name:'shopPrintForm',meta: {menu:"shopPrintList",keepAlive:true}},
            {path:'/layout/shopPrintDetail',component:shopPrintDetail,name:'shopPrintDetail',meta: {menu:"shopPrintList"}},
            {path:'/layout/shopPromotionList',component:shopPromotionList,name:'shopPromotionList'},
            {path:'/layout/shopPromotionForm',component:shopPromotionForm,name:'shopPromotionForm',meta: {menu:"shopPromotionList",keepAlive:true}},
        ]
    }
];
export default routes;
