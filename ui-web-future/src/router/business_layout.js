const adApplyList = r => require.ensure([], () => r(require('pages/business/layout/adApplyList.vue')));
const adApplyForm = r => require.ensure([], () => r(require('pages/business/layout/adApplyForm.vue')));
const adApplyGoods = r => require.ensure([], () => r(require('pages/business/layout/adApplyGoods.vue')));
const adApplyBillForm = r => require.ensure([], () => r(require('pages/business/layout/adApplyBillForm.vue')));
const adGoodsOrderList = r => require.ensure([], () => r(require('pages/business/layout/adGoodsOrderList.vue')));
const adGoodsOrderForm= r => require.ensure([], () => r(require('pages/business/layout/adGoodsOrderForm.vue')));
const adGoodsOrderDetail= r => require.ensure([], () => r(require('pages/business/layout/adGoodsOrderDetail.vue')));
const adGoodsOrderBill= r => require.ensure([], () => r(require('pages/business/layout/adGoodsOrderBill.vue')));
const adGoodsOrderShip= r => require.ensure([], () => r(require('pages/business/layout/adGoodsOrderShip.vue')));
const adGoodsOrderSign= r => require.ensure([], () => r(require('pages/business/layout/adGoodsOrderSign.vue')));
const adPricesystemChangeList  = r => require.ensure([], () => r(require('pages/business/layout/adPricesystemChangeList.vue')));
const adPricesystemChangeForm = r => require.ensure([], () => r(require('pages/business/layout/adPricesystemChangeForm.vue')));
const shopAdList= r => require.ensure([], () => r(require('pages/business/layout/shopAdList.vue')));
const shopAdForm= r => require.ensure([], () => r(require('pages/business/layout/shopAdForm.vue')));
const shopAdDetail= r => require.ensure([], () => r(require('pages/business/layout/shopAdDetail.vue')));
const shopAllotList = r => require.ensure([], () => r(require('pages/business/layout/shopAllotList.vue')));
const shopAllotForm = r => require.ensure([], () => r(require('pages/business/layout/shopAllotForm.vue')));
const shopAllotDetail = r => require.ensure([], () => r(require('pages/business/layout/shopAllotDetail.vue')));
const shopBuildList  = r => require.ensure([], () => r(require('pages/business/layout/shopBuildList.vue')));
const shopBuildForm  = r => require.ensure([], () => r(require('pages/business/layout/shopBuildForm.vue')));
const shopBuildDetail  = r => require.ensure([], () => r(require('pages/business/layout/shopBuildDetail.vue')));
const shopDepositList= r => require.ensure([], () => r(require('pages/business/layout/shopDepositList.vue')));
const shopDepositForm= r => require.ensure([], () => r(require('pages/business/layout/shopDepositForm.vue')));
const shopGoodsDepositList = r => require.ensure([], () => r(require('pages/business/layout/shopGoodsDepositList.vue')));
const shopGoodsDepositForm = r => require.ensure([], () => r(require('pages/business/layout/shopGoodsDepositForm.vue')));
const shopImageList= r => require.ensure([], () => r(require('pages/business/layout/shopImageList.vue')));
const shopImageForm= r => require.ensure([], () => r(require('pages/business/layout/shopImageForm.vue')));
const shopPrintList= r => require.ensure([], () => r(require('pages/business/layout/shopPrintList.vue')));
const shopPrintForm= r => require.ensure([], () => r(require('pages/business/layout/shopPrintForm.vue')));
const shopPrintDetail = r => require.ensure([], () => r(require('pages/business/layout/shopPrintDetail.vue')));
const shopPromotionList= r => require.ensure([], () => r(require('pages/business/layout/shopPromotionList.vue')));
const shopPromotionForm= r => require.ensure([], () => r(require('pages/business/layout/shopPromotionForm.vue')));








let routes = [
  {path:'/business/layout/adApplyList',component:adApplyList,name:'adApplyList'},
  {path:'/business/layout/adApplyForm',component:adApplyForm,name:'adApplyForm',meta: {menu:"adApplyList"}},
  {path:'/business/layout/adApplyGoods',component:adApplyGoods,name:'adApplyGoods',meta: {menu:"adApplyList"}},
  {path:'/business/layout/adApplyBillForm',component:adApplyBillForm,name:'adApplyBillForm',meta: {menu:"adApplyList"}},
  {path:'/business/layout/adGoodsOrderList',component:adGoodsOrderList,name:'adGoodsOrderList'},
  {path:'/business/layout/adGoodsOrderForm',component:adGoodsOrderForm,name:'adGoodsOrderForm',meta: {menu:"adGoodsOrderList"}},
  {path:'/business/layout/adGoodsOrderDetail',component:adGoodsOrderDetail,name:'adGoodsOrderDetail',meta: {menu:"adGoodsOrderList"}},
  {path:'/business/layout/adGoodsOrderBill',component:adGoodsOrderBill,name:'adGoodsOrderBill',meta: {menu:"adGoodsOrderList"}},
  {path:'/business/layout/adGoodsOrderShip',component:adGoodsOrderShip,name:'adGoodsOrderShip',meta: {menu:"adGoodsOrderList"}},
  {path:'/business/layout/adGoodsOrderSign',component:adGoodsOrderSign,name:'adGoodsOrderSign',meta: {menu:"adGoodsOrderList"}},
  {path:'/business/layout/adPricesystemChangeList',component:adPricesystemChangeList,name:'adPricesystemChangeList'},
  {path:'/business/layout/adPricesystemChangeForm',component:adPricesystemChangeForm,name:'v',meta: {menu:"adPricesystemChangeList"}},
  {path:'/business/layout/shopAdList',component:shopAdList,name:'shopAdList'},
  {path:'/business/layout/shopAdForm',component:shopAdForm,name:'shopAdForm',meta: {menu:"shopAdList"}},
  {path:'/business/layout/shopAdDetail',component:shopAdDetail,name:'shopAdDetail',meta: {menu:"shopAdList"}},
  {path:'/business/layout/shopAllotList',component:shopAllotList,name:'shopAllotList'},
  {path:'/business/layout/shopAllotForm',component:shopAllotForm,name:'shopAllotForm',meta: {menu:"shopAllotList"}},
  {path:'/business/layout/shopAllotDetail',component:shopAllotDetail,name:'shopAllotDetail',meta: {menu:"shopAllotList"}},
  {path:'/business/layout/shopBuildList',component:shopAllotList,name:'shopBuildList'},
  {path:'/business/layout/shopBuildForm',component:shopBuildForm,name:'shopBuildForm',meta: {menu:"shopBuildList"}},
  {path:'/business/layout/shopBuildDetail',component:shopBuildDetail,name:'shopBuildDetail',meta: {menu:"shopBuildList"}},
  {path:'/business/layout/shopDepositList',component:shopDepositList,name:'shopDepositList'},
  {path:'/business/layout/shopDepositForm',component:shopDepositForm,name:'shopDepositForm',meta: {menu:"shopDepositList"}},
  {path:'/business/layout/shopGoodsDepositList',component:shopGoodsDepositList,name:'shopGoodsDepositList'},
  {path:'/business/layout/shopGoodsDepositForm',component:shopGoodsDepositForm,name:'shopGoodsDepositForm',meta: {menu:"shopGoodsDepositList"}},
  {path:'/business/layout/shopImageList',component:shopImageList,name:'shopImageList'},
  {path:'/business/layout/shopImageForm',component:shopImageForm,name:'shopImageForm',meta: {menu:"shopImageList"}},
  {path:'/business/layout/shopPrintList',component:shopPrintList,name:'shopPrintList'},
  {path:'/business/layout/shopPrintForm',component:shopPrintForm,name:'shopPrintForm',meta: {menu:"shopPrintList"}},
  {path:'/business/layout/shopPrintDetail',component:shopPrintDetail,name:'shopPrintDetail',meta: {menu:"shopPrintList"}},
  {path:'/business/layout/shopPromotionList',component:shopPromotionList,name:'shopPromotionList'},
  {path:'/business/layout/shopPromotionForm',component:shopPromotionForm,name:'shopPromotionForm',meta: {menu:"shopPromotionList"}},

];

export default routes;
