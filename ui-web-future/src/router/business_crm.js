const afterSaleList = r => require.ensure([], () => r(require('pages/business/crm/afterSaleList.vue')));
const afterSaleForm= r => require.ensure([], () => r(require('pages/business/crm/afterSaleForm.vue')));
const afterSaleEditForm= r => require.ensure([], () => r(require('pages/business/crm/afterSaleEditForm.vue')));
const afterSaleImeAllotList = r => require.ensure([], () => r(require('pages/business/crm/afterSaleImeAllotList.vue')));
const afterSaleProductAllotList = r => require.ensure([], () => r(require('pages/business/crm/afterSaleProductAllotList.vue')));
const afterSaleStoreAllotList = r => require.ensure([], () => r(require('pages/business/crm/afterSaleStoreAllotList.vue')));
const bankInList = r => require.ensure([], () => r(require('pages/business/crm/bankInList.vue')));
const bankInForm = r => require.ensure([], () => r(require('pages/business/crm/bankInForm.vue')));
const bankInDetail = r => require.ensure([], () => r(require('pages/business/crm/bankInDetail.vue')));
const depotDetailList  = r => require.ensure([], () => r(require('pages/business/crm/depotDetailList.vue')));
const demoPhoneList= r => require.ensure([], () => r(require('pages/business/crm/demoPhoneList.vue')));
const demoPhoneForm= r => require.ensure([], () => r(require('pages/business/crm/demoPhoneForm.vue')));
const demoPhoneTypeList= r => require.ensure([], () => r(require('pages/business/crm/demoPhoneTypeList.vue')));
const demoPhoneTypeForm= r => require.ensure([], () => r(require('pages/business/crm/demoPhoneTypeForm.vue')));
const expressList  = r => require.ensure([], () => r(require('pages/business/crm/expressList.vue')));
const expressForm = r => require.ensure([], () => r(require('pages/business/crm/expressForm.vue')));
const expressOrderList  = r => require.ensure([], () => r(require('pages/business/crm/expressOrderList.vue')));
const expressOrderForm = r => require.ensure([], () => r(require('pages/business/crm/expressOrderForm.vue')));
const goodsOrderList = r => require.ensure([], () => r(require('pages/business/crm/goodsOrderList.vue')));
const goodsOrderForm = r => require.ensure([], () => r(require('pages/business/crm/goodsOrderForm.vue')));
const goodsOrderDetail = r => require.ensure([], () => r(require('pages/business/crm/goodsOrderDetail.vue')));
const goodsOrderBill= r => require.ensure([], () => r(require('pages/business/crm/goodsOrderBill.vue')));
const goodsOrderShip= r => require.ensure([], () => r(require('pages/business/crm/goodsOrderShip.vue')));
const goodsOrderSign= r => require.ensure([], () => r(require('pages/business/crm/goodsOrderSign.vue')));
const goodsOrderSreturn= r => require.ensure([], () => r(require('pages/business/crm/goodsOrderSreturn.vue')));
const goodsOrderPrint= r => require.ensure([], () => r(require('pages/business/crm/goodsOrderPrint.vue')));
const goodsOrderShipPrint= r => require.ensure([], () => r(require('pages/business/crm/goodsOrderShipPrint.vue')));
const imeAllotList= r => require.ensure([], () => r(require('pages/business/crm/imeAllotList.vue')));
const imeAllotForm= r => require.ensure([], () => r(require('pages/business/crm/imeAllotForm.vue')));
const priceChangeList  = r => require.ensure([], () => r(require('pages/business/crm/priceChangeList.vue')));
const priceChangeForm  = r => require.ensure([], () => r(require('pages/business/crm/priceChangeForm.vue')));
const priceChangeDetail  = r => require.ensure([], () => r(require('pages/business/crm/priceChangeDetail.vue')));
const priceChangeImeList  = r => require.ensure([], () => r(require('pages/business/crm/priceChangeImeList.vue')));
const priceChangeImeForm  = r => require.ensure([], () => r(require('pages/business/crm/priceChangeImeForm.vue')));
const priceChangeImeDetail  = r => require.ensure([], () => r(require('pages/business/crm/priceChangeImeDetail.vue')));
const pricesystemChangeList= r => require.ensure([], () => r(require('pages/business/crm/pricesystemChangeList.vue')));
const pricesystemChangeForm= r => require.ensure([], () => r(require('pages/business/crm/pricesystemChangeForm.vue')));
const productAdEdit = r  => require.ensure([], () => r(require('pages/business/crm/productAdEdit.vue')));
const productImeList = r => require.ensure([], () => r(require('pages/business/crm/productImeList.vue')));
const productImeSaleList= r => require.ensure([], () => r(require('pages/business/crm/productImeSaleList.vue')));
const productImeSaleForm= r => require.ensure([], () => r(require('pages/business/crm/productImeSaleForm.vue')));
const productImeUploadList  = r => require.ensure([], () => r(require('pages/business/crm/productImeUploadList.vue')));
const productImeUploadForm = r => require.ensure([], () => r(require('pages/business/crm/productImeUploadForm.vue')));
const productMonthPriceList = r => require.ensure([], () => r(require('pages/business/crm/productMonthPriceList.vue')));
const productMonthPriceForm = r => require.ensure([], () => r(require('pages/business/crm/productMonthPriceForm.vue')));
const reportScoreList = r => require.ensure([], () => r(require('pages/business/crm/reportScoreList.vue')));
const reportScoreForm = r => require.ensure([], () => r(require('pages/business/crm/reportScoreForm.vue')));
const reportScoreOfficeList = r => require.ensure([], () => r(require('pages/business/crm/reportScoreOfficeList.vue')));
const reportScoreAreaList = r => require.ensure([], () => r(require('pages/business/crm/reportScoreAreaList.vue')));
const storeAllotList = r => require.ensure([], () => r(require('pages/business/crm/storeAllotList.vue')));
const storeAllotForm = r => require.ensure([], () => r(require('pages/business/crm/storeAllotForm.vue')));
const storeAllotShip= r => require.ensure([], () => r(require('pages/business/crm/storeAllotShip.vue')));
const storeAllotDetail = r => require.ensure([], () => r(require('pages/business/crm/storeAllotDetail.vue')));



let routes = [
  {path:'/business/crm/afterSaleList',component:afterSaleList,name:'afterSaleList'},
  {path:'/business/crm/afterSaleForm',component:afterSaleForm,name:'afterSaleForm',meta: {menu:"afterSaleList"}},
  {path:'/business/crm/afterSaleEditForm',component:afterSaleEditForm,name:'afterSaleEditForm',meta: {menu:"afterSaleList"}},
  {path:'/business/crm/afterSaleImeAllotList',component:afterSaleImeAllotList,name:'afterSaleImeAllotList'},
  {path:'/business/crm/afterSaleProductAllotList',component:afterSaleProductAllotList,name:'afterSaleProductAllotList'},
  {path:'/business/crm/afterSaleStoreAllotList',component:afterSaleStoreAllotList,name:'afterSaleStoreAllotList'},
  {path:'/business/crm/bankInList',component:bankInList,name:'bankInList'},
  {path:'/business/crm/bankInForm',component:bankInForm,name:'bankInForm',meta: {menu:"bankInList"}},
  {path:'/business/crm/bankInDetail',component:bankInDetail,name:'bankInDetail',meta: {menu:"bankInList"}},
  {path:'/business/crm/depotDetailList',component:depotDetailList,name:'depotDetailList'},
  {path:'/business/crm/demoPhoneList',component:demoPhoneList,name:'demoPhoneList'},
  {path:'/business/crm/demoPhoneForm',component:demoPhoneForm,name:'demoPhoneForm',meta: {menu:"demoPhoneList"}},
  {path:'/business/crm/demoPhoneTypeList',component:demoPhoneTypeList,name:'demoPhoneTypeList'},
  {path:'/business/crm/demoPhoneTypeForm',component:demoPhoneTypeForm,name:'demoPhoneTypeForm',meta: {menu:"demoPhoneTypeList"}},
  {path:'/business/crm/expressList',component:expressList,name:'expressList'},
  {path:'/business/crm/expressForm',component:expressForm,name:'expressForm',meta: {menu:"expressList"}},
  {path:'/business/crm/expressOrderList',component:expressOrderList,name:'expressOrderList'},
  {path:'/business/crm/expressOrderForm',component:expressOrderForm,name:'expressOrderForm',meta: {menu:"expressOrderList"}},
  {path: '/business/crm/goodsOrderList',component: goodsOrderList,name:"goodsOrderList"},
  {path:'/business/crm/goodsOrderForm',component:goodsOrderForm,name:'goodsOrderForm',meta: {menu:"goodsOrderList"}},
  {path:'/business/crm/goodsOrderDetail',component:goodsOrderDetail,name:'goodsOrderDetail',meta: {menu:"goodsOrderList"}},
  {path:'/business/crm/goodsOrderBill',component:goodsOrderBill,name:'goodsOrderBill',meta: {menu:"goodsOrderList"}},
  {path:'/business/crm/goodsOrderShip',component:goodsOrderShip,name:'goodsOrderShip',meta: {menu:"goodsOrderList"}},
  {path:'/business/crm/goodsOrderSign',component:goodsOrderSign,name:'goodsOrderSign',meta: {menu:"goodsOrderList"}},
  {path:'/business/crm/goodsOrderSreturn',component:goodsOrderSreturn,name:'goodsOrderSreturn',meta: {menu:"goodsOrderList"}},
  {path:'/business/crm/goodsOrderPrint',component:goodsOrderPrint,name:'goodsOrderPrint',meta: {hidden: true}},
  {path:'/business/crm/goodsOrderShipPrint',component:goodsOrderShipPrint,name:'goodsOrderShipPrint',meta: {hidden: true}},
  {path:'/business/crm/imeAllotList',component:imeAllotList,name:'imeAllotList'},
  {path:'/business/crm/imeAllotForm',component:imeAllotForm,name:'imeAllotForm',meta: {menu:"imeAllotList"}},
  {path:'/business/crm/priceChangeList',component:priceChangeList,name:'priceChangeList'},
  {path:'/business/crm/priceChangeForm',component:priceChangeForm,name:'priceChangeForm',meta: {menu:"priceChangeList"}},
  {path:'/business/crm/priceChangeDetail',component:priceChangeDetail,name:'priceChangeDetail',meta: {menu:"priceChangeList"}},
  {path:'/business/crm/priceChangeImeList',component:priceChangeImeList,name:'priceChangeImeList'},
  {path:'/business/crm/priceChangeImeForm',component:priceChangeImeForm,name:'priceChangeImeForm',meta: {menu:"priceChangeImeList"}},
  {path:'/business/crm/priceChangeImeDetail',component:priceChangeImeDetail,name:'priceChangeImeDetail',meta: {menu:"priceChangeImeList"}},
  {path:'/business/crm/pricesystemChangeList',component:pricesystemChangeList,name:'pricesystemChangeList'},
  {path:'/business/crm/pricesystemChangeForm',component:pricesystemChangeForm,name:'pricesystemChangeForm',meta: {menu:"pricesystemChangeList"}},
  {path:'/business/crm/productAdEdit',component:productAdEdit,name:'productAdEdit',meta: {menu:"productList"}},
  {path:'/business/crm/productImeList',component:productImeList,name:'productImeList'},
  {path:'/business/crm/productImeSaleList',component:productImeSaleList,name:'productImeSaleList'},
  {path:'/business/crm/productImeSaleForm',component:productImeSaleForm,name:'productImeSaleForm',meta: {menu:"productImeSaleList"}},
  {path:'/business/crm/productImeUploadList',component:productImeUploadList,name:'productImeUploadList'},
  {path:'/business/crm/productImeUploadForm',component:productImeUploadForm,name:'productImeUploadForm',meta: {menu:"productImeUploadList"}},
  {path:'/business/crm/productMonthPriceList',component:productMonthPriceList,name:'productMonthPriceList' },
  {path:'/business/crm/productMonthPriceForm',component:productMonthPriceForm,name:'productMonthPriceForm',meta: {menu:"productMonthPriceList"}},
  {path:'/business/crm/reportScoreList',component:reportScoreList,name:'reportScoreList' },
  {path:'/business/crm/reportScoreForm',component:reportScoreForm,name:'reportScoreForm',meta: {menu:"reportScoreList"}},
  {path:'/business/crm/reportScoreOfficeList',component:reportScoreOfficeList,name:'reportScoreOfficeList' },
  {path:'/business/crm/reportScoreAreaList',component:reportScoreAreaList,name:'reportScoreAreaList',meta: {menu:"reportScoreOfficeList"}},
  {path:'/business/crm/storeAllotList',component:storeAllotList,name:'storeAllotList'},
  {path:'/business/crm/storeAllotForm',component:storeAllotForm,name:'storeAllotForm',meta: {menu:"storeAllotList"}},
  {path:'/business/crm/storeAllotShip',component:storeAllotShip,name:'storeAllotShip',meta: {menu:"storeAllotList"}},
  {path:'/business/crm/storeAllotDetail',component:storeAllotDetail,name:'storeAllotDetail',meta: {menu:"storeAllotList"}},

];

export default routes;
