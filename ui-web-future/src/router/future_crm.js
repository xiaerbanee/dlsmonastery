const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const afterSaleAreaEdit= r => require.ensure([], () => r(require('../pages/crm/afterSaleAreaEdit.vue')));
const afterSaleEditForm = r => require.ensure([], () => r(require('../pages/crm/afterSaleEditForm.vue')));
const afterSaleForm = r => require.ensure([], () => r(require('../pages/crm/afterSaleForm.vue')));
const afterSaleFromCompany = r => require.ensure([], () => r(require('../pages/crm/afterSaleFromCompany.vue')));
const afterSaleList = r => require.ensure([], () => r(require('../pages/crm/afterSaleList.vue')));
const afterSaleProductAllotList = r => require.ensure([], () => r(require('../pages/crm/afterSaleProductAllotList.vue')));

const afterSaleStoreAllotList = r => require.ensure([], () => r(require('../pages/crm/afterSaleStoreAllotList.vue')));
const afterSaleToCompany = r => require.ensure([], () => r(require('../pages/crm/afterSaleToCompany.vue')));

const bankInList = r => require.ensure([], () => r(require('../pages/crm/bankInList.vue')));
const bankInForm = r => require.ensure([], () => r(require('../pages/crm/bankInForm.vue')));
const bankInBatchForm = r => require.ensure([], () => r(require('../pages/crm/bankInBatchForm.vue')));
const bankInDetail = r => require.ensure([], () => r(require('../pages/crm/bankInDetail.vue')));
const depotAccountList = r => require.ensure([], () => r(require('../pages/crm/depotAccountList.vue')));
const receivableList = r => require.ensure([], () => r(require('../pages/crm/receivableList.vue')));
const depotAccountDetail = r => require.ensure([], () => r(require('../pages/crm/depotAccountDetail.vue')));
const depotDetailList  = r => require.ensure([], () => r(require('../pages/crm/depotDetailList.vue')));
const depotInventoryReport =r =>require([], () => r(require('../pages/crm/depotInventoryReport.vue')))
const demoPhoneList= r => require.ensure([], () => r(require('../pages/crm/demoPhoneList.vue')));
const demoPhoneForm= r => require.ensure([], () => r(require('../pages/crm/demoPhoneForm.vue')));
const demoPhoneTypeOfficeList= r => require.ensure([], () => r(require('../pages/crm/demoPhoneTypeOfficeList.vue')));
const demoPhoneTypeList= r => require.ensure([], () => r(require('../pages/crm/demoPhoneTypeList.vue')));
const demoPhoneTypeForm= r => require.ensure([], () => r(require('../pages/crm/demoPhoneTypeForm.vue')));
const expressList  = r => require.ensure([], () => r(require('../pages/crm/expressList.vue')));
const expressForm = r => require.ensure([], () => r(require('../pages/crm/expressForm.vue')));
const expressOrderList  = r => require.ensure([], () => r(require('../pages/crm/expressOrderList.vue')));
const expressOrderForm = r => require.ensure([], () => r(require('../pages/crm/expressOrderForm.vue')));


const goodsOrderList = r => require.ensure([], () => r(require('../pages/crm/goodsOrderList.vue')));
const goodsOrderImeList = r => require.ensure([], () => r(require('../pages/crm/goodsOrderImeList.vue')));
const goodsOrderShipList = r => require.ensure([], () => r(require('../pages/crm/goodsOrderShipList.vue')));
const goodsOrderDetailList = r => require.ensure([], () => r(require('../pages/crm/goodsOrderDetailList.vue')));
const goodsOrderForm = r => require.ensure([], () => r(require('../pages/crm/goodsOrderForm.vue')));
const goodsOrderBatchAdd = r => require.ensure([], () => r(require('../pages/crm/goodsOrderBatchAdd.vue')));
const goodsOrderMallForm = r => require.ensure([], () => r(require('../pages/crm/goodsOrderMallForm.vue')));
const goodsOrderDetail = r => require.ensure([], () => r(require('../pages/crm/goodsOrderDetail.vue')));
const goodsOrderBill= r => require.ensure([], () => r(require('../pages/crm/goodsOrderBill.vue')));
const goodsOrderShip= r => require.ensure([], () => r(require('../pages/crm/goodsOrderShip.vue')));
const goodsOrderSign= r => require.ensure([], () => r(require('../pages/crm/goodsOrderSign.vue')));
const goodsOrderSreturn= r => require.ensure([], () => r(require('../pages/crm/goodsOrderSreturn.vue')));
const goodsOrderPrint= r => require.ensure([], () => r(require('../pages/crm/goodsOrderPrint.vue')));
const goodsOrderShipPrint= r => require.ensure([], () => r(require('../pages/crm/goodsOrderShipPrint.vue')));
const imeAllotList= r => require.ensure([], () => r(require('../pages/crm/imeAllotList.vue')));
const imeAllotForm= r => require.ensure([], () => r(require('../pages/crm/imeAllotForm.vue')));
const imeAllotBatchForm= r => require.ensure([], () => r(require('../pages/crm/imeAllotBatchForm.vue')));
const priceChangeList  = r => require.ensure([], () => r(require('../pages/crm/priceChangeList.vue')));
const priceChangeForm  = r => require.ensure([], () => r(require('../pages/crm/priceChangeForm.vue')));
const priceChangeDetail  = r => require.ensure([], () => r(require('../pages/crm/priceChangeDetail.vue')));
const priceChangeImeList  = r => require.ensure([], () => r(require('../pages/crm/priceChangeImeList.vue')));
const priceChangeImeImageUpload  = r => require.ensure([], () => r(require('../pages/crm/priceChangeImeImageUpload.vue')));
const priceChangeImeForm  = r => require.ensure([], () => r(require('../pages/crm/priceChangeImeForm.vue')));
const priceChangeImeDetail  = r => require.ensure([], () => r(require('../pages/crm/priceChangeImeDetail.vue')));
const pricesystemChangeList= r => require.ensure([], () => r(require('../pages/crm/pricesystemChangeList.vue')));
const pricesystemChangeForm= r => require.ensure([], () => r(require('../pages/crm/pricesystemChangeForm.vue')));
const productImeList = r => require.ensure([], () => r(require('../pages/crm/productImeList.vue')));
const productImeBatchQuery = r => require.ensure([], () => r(require('../pages/crm/productImeBatchQuery.vue')));
const productImeCreate = r => require.ensure([], () => r(require('../pages/crm/productImeCreate.vue')));
const productImeChange = r => require.ensure([], () => r(require('../pages/crm/productImeChange.vue')));
const productImeDetail = r => require.ensure([], () => r(require('../pages/crm/productImeDetail.vue')));
const productImeSaleList= r => require.ensure([], () => r(require('../pages/crm/productImeSaleList.vue')));
const productImeSaleForm= r => require.ensure([], () => r(require('../pages/crm/productImeSaleForm.vue')));
const productImeSaleBackForm= r => require.ensure([], () => r(require('../pages/crm/productImeSaleBackForm.vue')));
const productImeUploadList  = r => require.ensure([], () => r(require('../pages/crm/productImeUploadList.vue')));
const productImeUploadForm = r => require.ensure([], () => r(require('../pages/crm/productImeUploadForm.vue')));
const productImeBatchUploadForm = r => require.ensure([], () => r(require('../pages/crm/productImeBatchUploadForm.vue')));
const productImeUploadBackForm = r => require.ensure([], () => r(require('../pages/crm/productImeUploadBackForm.vue')));
const productMonthPriceList = r => require.ensure([], () => r(require('../pages/crm/productMonthPriceList.vue')));
const productMonthPriceForm = r => require.ensure([], () => r(require('../pages/crm/productMonthPriceForm.vue')));
const reportScoreList = r => require.ensure([], () => r(require('../pages/crm/reportScoreList.vue')));
const reportScoreForm = r => require.ensure([], () => r(require('../pages/crm/reportScoreForm.vue')));
const reportScoreOfficeList = r => require.ensure([], () => r(require('../pages/crm/reportScoreOfficeList.vue')));
const reportScoreAreaList = r => require.ensure([], () => r(require('../pages/crm/reportScoreAreaList.vue')));
const productImeSaleReport = r => require.ensure([], () => r(require('../pages/crm/productImeSaleReport.vue')));
const productImeSaleReportGrid = r => require.ensure([], () => r(require('../pages/crm/productImeSaleReportGrid.vue')));
const productImeStockReport = r => require.ensure([], () => r(require('../pages/crm/productImeStockReport.vue')));
const productImeStockReportGrid = r => require.ensure([], () => r(require('../pages/crm/productImeStockReportGrid.vue')));
const productMonthPriceSum = r => require.ensure([], () => r(require('../pages/crm/productMonthPriceSum.vue')));
const dataReportList = r => require.ensure([], () => r(require('../pages/crm/dataReportList.vue')));
const storeAllotList = r => require.ensure([], () => r(require('../pages/crm/storeAllotList.vue')));
const storeAllotForm = r => require.ensure([], () => r(require('../pages/crm/storeAllotForm.vue')));
const storeAllotShip= r => require.ensure([], () => r(require('../pages/crm/storeAllotShip.vue')));
const storeAllotPrint= r => require.ensure([], () => r(require('../pages/crm/storeAllotPrint.vue')));
const storeAllotShipPrint= r => require.ensure([], () => r(require('../pages/crm/storeAllotShipPrint.vue')));
const storeAllotDetail = r => require.ensure([], () => r(require('../pages/crm/storeAllotDetail.vue')));
const storeInventoryReport = r => require.ensure([], () => r(require('../pages/crm/storeInventoryReport.vue')));


let routes = [
    {
        path: '/',
        component: home,
        children: [
            {path:'/crm/afterSaleAreaEdit',component:afterSaleAreaEdit,name:'afterSaleAreaEdit'},
            {path:'/crm/afterSaleEditForm',component:afterSaleEditForm,name:'afterSaleEditForm'},
            {path:'/crm/afterSaleForm',component:afterSaleForm,name:'afterSaleForm'},
            {path:'/crm/afterSaleFromCompany',component:afterSaleFromCompany,name:'afterSaleFromCompany'},
            {path:'/crm/afterSaleList',component:afterSaleList,name:'afterSaleList'},
            {path:'/crm/afterSaleProductAllotList',component:afterSaleProductAllotList,name:'afterSaleProductAllotList'},
            {path:'/crm/afterSaleStoreAllotList',component:afterSaleStoreAllotList,name:'afterSaleStoreAllotList'},
            {path:'/crm/afterSaleToCompany',component:afterSaleToCompany,name:'afterSaleToCompany'},

            {path:'/crm/bankInList',component:bankInList,name:'bankInList'},
            {path:'/crm/bankInForm',component:bankInForm,name:'bankInForm',meta: {menu:"bankInList",keepAlive:true}},
            {path:'/crm/bankInBatchForm',component:bankInBatchForm,name:'bankInBatchForm',meta: {menu:"bankInList",keepAlive:true}},
            {path:'/crm/bankInDetail',component:bankInDetail,name:'bankInDetail',meta: {menu:"bankInList"}},
            {path:'/crm/depotDetailList',component:depotDetailList,name:'depotDetailList'},
            {path:'/crm/depotInventoryReport',component:depotInventoryReport,name:'depotInventoryReport'},
            {path:'/crm/demoPhoneList',component:demoPhoneList,name:'demoPhoneList'},
            {path:'/crm/demoPhoneForm',component:demoPhoneForm,name:'demoPhoneForm',meta: {menu:"demoPhoneList",keepAlive:true}},
            {path:'/crm/demoPhoneTypeOfficeList',component:demoPhoneTypeOfficeList,name:'demoPhoneTypeOfficeList'},
            {path:'/crm/demoPhoneTypeList',component:demoPhoneTypeList,name:'demoPhoneTypeList'},
            {path:'/crm/demoPhoneTypeForm',component:demoPhoneTypeForm,name:'demoPhoneTypeForm',meta: {menu:"demoPhoneTypeList",keepAlive:true}},
            {path:'/crm/expressList',component:expressList,name:'expressList'},
            {path:'/crm/expressForm',component:expressForm,name:'expressForm',meta: {menu:"expressList",keepAlive:true}},
            {path:'/crm/expressOrderList',component:expressOrderList,name:'expressOrderList'},
            {path:'/crm/expressOrderForm',component:expressOrderForm,name:'expressOrderForm',meta: {menu:"expressOrderList",keepAlive:true}},
            {path:'/crm/depotAccountList',component:depotAccountList,name:'depotAccountList',meta: {menu:"depotAccountList"}},
            {path:'/crm/receivableList',component:receivableList,name:'receivableList',meta: {menu:"receivableList"}},
            {path:'/crm/depotAccountDetail',component:depotAccountDetail,name:'depotAccountDetail',meta: {menu:"depotAccountDetail"}},



            {path: '/crm/goodsOrderList',component: goodsOrderList,name:"goodsOrderList"},
            {path: '/crm/goodsOrderImeList',component: goodsOrderImeList,name:"goodsOrderImeList"},
            {path: '/crm/goodsOrderShipList',component: goodsOrderShipList,name:"goodsOrderShipList"},
            {path: '/crm/goodsOrderDetailList',component: goodsOrderDetailList,name:"goodsOrderDetailList"},
            {path:'/crm/goodsOrderForm',component:goodsOrderForm,name:'goodsOrderForm',meta: {menu:"goodsOrderList",keepAlive:true}},
            {path:'/crm/goodsOrderBatchAdd',component:goodsOrderBatchAdd,name:'goodsOrderBatchAdd',meta: {menu:"goodsOrderList",keepAlive:true}},
            {path:'/crm/goodsOrderMallForm',component:goodsOrderMallForm,name:'goodsOrderMallForm',meta: {menu:"goodsOrderList",keepAlive:true}},
            {path:'/crm/goodsOrderDetail',component:goodsOrderDetail,name:'goodsOrderDetail',meta: {menu:"goodsOrderList"}},
            {path:'/crm/goodsOrderBill',component:goodsOrderBill,name:'goodsOrderBill',meta: {menu:"goodsOrderList"}},
            {path:'/crm/goodsOrderShip',component:goodsOrderShip,name:'goodsOrderShip',meta: {menu:"goodsOrderList"}},
            {path:'/crm/goodsOrderSign',component:goodsOrderSign,name:'goodsOrderSign',meta: {menu:"goodsOrderList"}},
            {path:'/crm/goodsOrderSreturn',component:goodsOrderSreturn,name:'goodsOrderSreturn',meta: {menu:"goodsOrderList"}},
            {path:'/crm/goodsOrderPrint',component:goodsOrderPrint,name:'goodsOrderPrint',meta: {hidden: true}},
            {path:'/crm/goodsOrderShipPrint',component:goodsOrderShipPrint,name:'goodsOrderShipPrint',meta: {hidden: true}},
            {path:'/crm/imeAllotList',component:imeAllotList,name:'imeAllotList'},
            {path:'/crm/imeAllotForm',component:imeAllotForm,name:'imeAllotForm',meta: {menu:"imeAllotList",keepAlive:true}},
            {path:'/crm/imeAllotBatchForm',component:imeAllotBatchForm,name:'imeAllotBatchForm',meta: {menu:"imeAllotList",keepAlive:true}},
            {path:'/crm/priceChangeList',component:priceChangeList,name:'priceChangeList'},
            {path:'/crm/priceChangeForm',component:priceChangeForm,name:'priceChangeForm',meta: {menu:"priceChangeList",keepAlive:true}},
            {path:'/crm/priceChangeDetail',component:priceChangeDetail,name:'priceChangeDetail',meta: {menu:"priceChangeList"}},
            {path:'/crm/priceChangeImeList',component:priceChangeImeList,name:'priceChangeImeList'},
            {path:'/crm/priceChangeImeImageUpload',component:priceChangeImeImageUpload,name:'priceChangeImeImageUpload',meta: {menu:"priceChangeImeList"}},
            {path:'/crm/priceChangeImeForm',component:priceChangeImeForm,name:'priceChangeImeForm',meta: {menu:"priceChangeImeList",keepAlive:true}},
            {path:'/crm/priceChangeImeDetail',component:priceChangeImeDetail,name:'priceChangeImeDetail',meta: {menu:"priceChangeImeList"}},
            {path:'/crm/pricesystemChangeList',component:pricesystemChangeList,name:'pricesystemChangeList'},
            {path:'/crm/pricesystemChangeForm',component:pricesystemChangeForm,name:'pricesystemChangeForm',meta: {menu:"pricesystemChangeList",keepAlive:true}},
            {path:'/crm/productImeList',component:productImeList,name:'productImeList'},
            {path:'/crm/productImeChange',component:productImeChange,name:'productImeChange', meta: {menu:"productImeList",keepAlive:true}},
            {path:'/crm/productImeCreate',component:productImeCreate,name:'productImeCreate',meta: {menu:"productImeList",keepAlive:true}},
            {path:'/crm/productImeBatchQuery',component:productImeBatchQuery,name:'productImeBatchQuery',meta: {menu:"productImeList",keepAlive:true}},
            {path:'/crm/productImeDetail',component:productImeDetail,name:'productImeDetail'},
            {path:'/crm/productImeSaleList',component:productImeSaleList,name:'productImeSaleList'},
            {path:'/crm/productImeSaleForm',component:productImeSaleForm,name:'productImeSaleForm',meta: {menu:"productImeSaleList",keepAlive:true}},
            {path:'/crm/productImeSaleBackForm',component:productImeSaleBackForm,name:'productImeSaleBackForm',meta: {menu:"productImeSaleList",keepAlive:true}},
            {path:'/crm/productImeUploadList',component:productImeUploadList,name:'productImeUploadList'},
            {path:'/crm/productImeUploadForm',component:productImeUploadForm,name:'productImeUploadForm',meta: {menu:"productImeUploadList",keepAlive:true}},
            {path:'/crm/productImeBatchUploadForm',component:productImeBatchUploadForm,name:'productImeBatchUploadForm',meta: {menu:"productImeUploadList",keepAlive:true}},
            {path:'/crm/productImeUploadBackForm',component:productImeUploadBackForm,name:'productImeUploadBackForm',meta: {menu:"productImeUploadList",keepAlive:true}},
            {path:'/crm/productMonthPriceList',component:productMonthPriceList,name:'productMonthPriceList' },
            {path:'/crm/productMonthPriceForm',component:productMonthPriceForm,name:'productMonthPriceForm',meta: {menu:"productMonthPriceList",keepAlive:true}},
            {path:'/crm/reportScoreList',component:reportScoreList,name:'reportScoreList' },
            {path:'/crm/reportScoreForm',component:reportScoreForm,name:'reportScoreForm',meta: {menu:"reportScoreList",keepAlive:true}},
            {path:'/crm/reportScoreOfficeList',component:reportScoreOfficeList,name:'reportScoreOfficeList' },
            {path:'/crm/reportScoreAreaList',component:reportScoreAreaList,name:'reportScoreAreaList',meta: {menu:"reportScoreOfficeList"}},
            {path:'/crm/productImeSaleReport',component:productImeSaleReport,name:'productImeSaleReport'},
            {path:'/crm/productImeSaleReportGrid',component:productImeSaleReportGrid,name:'productImeSaleReportGrid',meta: {menu:"productImeSaleReport"}},
            {path:'/crm/productImeStockReport',component:productImeStockReport,name:'productImeStockReport'},
            {path:'/crm/productImeStockReportGrid',component:productImeStockReportGrid,name:'productImeStockReportGrid',meta: {menu:"productImeStockReport"}},
            {path:'/crm/productMonthPriceSum',component:productMonthPriceSum,name:'productMonthPriceSum'},
            {path:'/crm/dataReportList',component:dataReportList,name:'dataReportList'},
            {path:'/crm/storeAllotList',component:storeAllotList,name:'storeAllotList'},
            {path:'/crm/storeAllotForm',component:storeAllotForm,name:'storeAllotForm',meta: {menu:"storeAllotList",keepAlive:true}},
            {path:'/crm/storeAllotShip',component:storeAllotShip,name:'storeAllotShip',meta: {menu:"storeAllotList"}},
            {path:'/crm/storeAllotPrint',component:storeAllotPrint,name:'storeAllotPrint',meta: {hidden: true}},
            {path:'/crm/storeAllotShipPrint',component:storeAllotShipPrint,name:'storeAllotShipPrint',meta: {hidden: true}},
            {path:'/crm/storeAllotDetail',component:storeAllotDetail,name:'storeAllotDetail',meta: {menu:"storeAllotList"}},
            {path:'/crm/storeInventoryReport',component:storeInventoryReport,name:'storeInventoryReport'},

        ]
    }
];

export default routes;
