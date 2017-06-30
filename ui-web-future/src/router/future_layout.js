const adApplyList = r => require.ensure([], () => r(require('pages/future/layout/adApplyList.vue')));
const adApplyForm = r => require.ensure([], () => r(require('pages/future/layout/adApplyForm.vue')));
const adApplyGoods = r => require.ensure([], () => r(require('pages/future/layout/adApplyGoods.vue')));
const adApplyBillForm = r => require.ensure([], () => r(require('pages/future/layout/adApplyBillForm.vue')));
const adGoodsOrderList = r => require.ensure([], () => r(require('pages/future/layout/adGoodsOrderList.vue')));
const adGoodsOrderDetailList = r => require.ensure([], () => r(require('pages/future/layout/adGoodsOrderDetailList.vue')));
const adGoodsOrderForm= r => require.ensure([], () => r(require('pages/future/layout/adGoodsOrderForm.vue')));
const adGoodsOrderDetail= r => require.ensure([], () => r(require('pages/future/layout/adGoodsOrderDetail.vue')));
const adGoodsOrderBill= r => require.ensure([], () => r(require('pages/future/layout/adGoodsOrderBill.vue')));
const adGoodsOrderShip= r => require.ensure([], () => r(require('pages/future/layout/adGoodsOrderShip.vue')));
const adGoodsOrderSign= r => require.ensure([], () => r(require('pages/future/layout/adGoodsOrderSign.vue')));
const adGoodsOrderPrint= r => require.ensure([], () => r(require('pages/future/layout/adGoodsOrderPrint.vue')));
const adPricesystemChangeList  = r => require.ensure([], () => r(require('pages/future/layout/adPricesystemChangeList.vue')));
const adPricesystemChangeForm = r => require.ensure([], () => r(require('pages/future/layout/adPricesystemChangeForm.vue')));
const shopAdList= r => require.ensure([], () => r(require('pages/future/layout/shopAdList.vue')));
const shopAdForm= r => require.ensure([], () => r(require('pages/future/layout/shopAdForm.vue')));
const shopAdDetail= r => require.ensure([], () => r(require('pages/future/layout/shopAdDetail.vue')));
const shopAllotList = r => require.ensure([], () => r(require('pages/future/layout/shopAllotList.vue')));
const shopAllotShipPrint = r => require.ensure([], () => r(require('pages/future/layout/shopAllotShipPrint.vue')));
const shopAllotForm = r => require.ensure([], () => r(require('pages/future/layout/shopAllotForm.vue')));
const shopAllotDetail = r => require.ensure([], () => r(require('pages/future/layout/shopAllotDetail.vue')));
const shopBuildList  = r => require.ensure([], () => r(require('pages/future/layout/shopBuildList.vue')));
const shopBuildForm  = r => require.ensure([], () => r(require('pages/future/layout/shopBuildForm.vue')));
const shopBuildDetail  = r => require.ensure([], () => r(require('pages/future/layout/shopBuildDetail.vue')));
const shopDepositList= r => require.ensure([], () => r(require('pages/future/layout/shopDepositList.vue')));
const shopDepositForm= r => require.ensure([], () => r(require('pages/future/layout/shopDepositForm.vue')));
const shopDepositBatchForm= r => require.ensure([], () => r(require('pages/future/layout/shopDepositBatchForm.vue')));
const shopGoodsDepositList = r => require.ensure([], () => r(require('pages/future/layout/shopGoodsDepositList.vue')));
const shopGoodsDepositForm = r => require.ensure([], () => r(require('pages/future/layout/shopGoodsDepositForm.vue')));
const shopImageList= r => require.ensure([], () => r(require('pages/future/layout/shopImageList.vue')));
const shopImageForm= r => require.ensure([], () => r(require('pages/future/layout/shopImageForm.vue')));
const shopPrintList= r => require.ensure([], () => r(require('pages/future/layout/shopPrintList.vue')));
const shopPrintForm= r => require.ensure([], () => r(require('pages/future/layout/shopPrintForm.vue')));
const shopPrintDetail = r => require.ensure([], () => r(require('pages/future/layout/shopPrintDetail.vue')));
const shopPromotionList= r => require.ensure([], () => r(require('pages/future/layout/shopPromotionList.vue')));
const shopPromotionForm= r => require.ensure([], () => r(require('pages/future/layout/shopPromotionForm.vue')));








let routes = [
  {path:'/future/layout/adApplyList',component:adApplyList,name:'adApplyList'},
  {path:'/future/layout/adApplyForm',component:adApplyForm,name:'adApplyForm',meta: {menu:"adApplyList",keepAlive:true}},
  {path:'/future/layout/adApplyGoods',component:adApplyGoods,name:'adApplyGoods',meta: {menu:"adApplyList"}},
  {path:'/future/layout/adApplyBillForm',component:adApplyBillForm,name:'adApplyBillForm',meta: {menu:"adApplyList",keepAlive:true}},
  {path:'/future/layout/adGoodsOrderList',component:adGoodsOrderList,name:'adGoodsOrderList'},
  {path:'/future/layout/adGoodsOrderDetailList',component:adGoodsOrderDetailList,name:'adGoodsOrderDetailList'},
  {path:'/future/layout/adGoodsOrderForm',component:adGoodsOrderForm,name:'adGoodsOrderForm',meta: {menu:"adGoodsOrderList",keepAlive:true}},
  {path:'/future/layout/adGoodsOrderDetail',component:adGoodsOrderDetail,name:'adGoodsOrderDetail',meta: {menu:"adGoodsOrderList"}},
  {path:'/future/layout/adGoodsOrderBill',component:adGoodsOrderBill,name:'adGoodsOrderBill',meta: {menu:"adGoodsOrderList"}},
  {path:'/future/layout/adGoodsOrderShip',component:adGoodsOrderShip,name:'adGoodsOrderShip',meta: {menu:"adGoodsOrderList"}},
  {path:'/future/layout/adGoodsOrderSign',component:adGoodsOrderSign,name:'adGoodsOrderSign',meta: {menu:"adGoodsOrderList"}},
  {path:'/future/layout/adGoodsOrderPrint',component:adGoodsOrderPrint,name:'adGoodsOrderPrint',meta: {hidden: true}},
  {path:'/future/layout/adPricesystemChangeList',component:adPricesystemChangeList,name:'adPricesystemChangeList'},
  {path:'/future/layout/adPricesystemChangeForm',component:adPricesystemChangeForm,name:'adPricesystemChangeForm',meta: {menu:"adPricesystemChangeList",keepAlive:true}},
  {path:'/future/layout/shopAdList',component:shopAdList,name:'shopAdList'},
  {path:'/future/layout/shopAdForm',component:shopAdForm,name:'shopAdForm',meta: {menu:"shopAdList",keepAlive:true}},
  {path:'/future/layout/shopAdDetail',component:shopAdDetail,name:'shopAdDetail',meta: {menu:"shopAdList"}},
  {path:'/future/layout/shopAllotList',component:shopAllotList,name:'shopAllotList'},
  {path:'/future/layout/shopAllotShipPrint',component:shopAllotShipPrint,name:'shopAllotShipPrint',meta: {hidden: true}},
  {path:'/future/layout/shopAllotForm',component:shopAllotForm,name:'shopAllotForm',meta: {menu:"shopAllotList",keepAlive:true}},
  {path:'/future/layout/shopAllotDetail',component:shopAllotDetail,name:'shopAllotDetail',meta: {menu:"shopAllotList"}},
  {path:'/future/layout/shopBuildList',component:shopBuildList,name:'shopBuildList'},
  {path:'/future/layout/shopBuildForm',component:shopBuildForm,name:'shopBuildForm',meta: {menu:"shopBuildList",keepAlive:true}},
  {path:'/future/layout/shopBuildDetail',component:shopBuildDetail,name:'shopBuildDetail',meta: {menu:"shopBuildList"}},
  {path:'/future/layout/shopDepositList',component:shopDepositList,name:'shopDepositList'},
  {path:'/future/layout/shopDepositForm',component:shopDepositForm,name:'shopDepositForm',meta: {menu:"shopDepositList",keepAlive:true}},
  {path:'/future/layout/shopDepositBatchForm',component:shopDepositBatchForm,name:'shopDepositBatchForm',meta: {menu:"shopDepositList",keepAlive:true}},
  {path:'/future/layout/shopGoodsDepositList',component:shopGoodsDepositList,name:'shopGoodsDepositList'},
  {path:'/future/layout/shopGoodsDepositForm',component:shopGoodsDepositForm,name:'shopGoodsDepositForm',meta: {menu:"shopGoodsDepositList",keepAlive:true}},
  {path:'/future/layout/shopImageList',component:shopImageList,name:'shopImageList'},
  {path:'/future/layout/shopImageForm',component:shopImageForm,name:'shopImageForm',meta: {menu:"shopImageList",keepAlive:true}},
  {path:'/future/layout/shopPrintList',component:shopPrintList,name:'shopPrintList'},
  {path:'/future/layout/shopPrintForm',component:shopPrintForm,name:'shopPrintForm',meta: {menu:"shopPrintList",keepAlive:true}},
  {path:'/future/layout/shopPrintDetail',component:shopPrintDetail,name:'shopPrintDetail',meta: {menu:"shopPrintList"}},
  {path:'/future/layout/shopPromotionList',component:shopPromotionList,name:'shopPromotionList'},
  {path:'/future/layout/shopPromotionForm',component:shopPromotionForm,name:'shopPromotionForm',meta: {menu:"shopPromotionList",keepAlive:true}},

];

export default routes;
