const home  = r => require.ensure([], () => r(require('../pages/home.vue')));

const productImeSaleReport = r => require.ensure([], () => r(require('../pages/crm/productImeSaleReport.vue')));
const productImeSaleReportGrid = r => require.ensure([], () => r(require('../pages/crm/productImeSaleReportGrid.vue')));
const productImeStockReport = r => require.ensure([], () => r(require('../pages/crm/productImeStockReport.vue')));
const productImeStockReportGrid = r => require.ensure([], () => r(require('../pages/crm/productImeStockReportGrid.vue')));
const storeInventoryReport = r => require.ensure([], () => r(require('../pages/crm/storeInventoryReport.vue')));
const reportScoreAreaList = r => require.ensure([], () => r(require('../pages/crm/reportScoreAreaList.vue')));
const reportScoreList = r => require.ensure([], () => r(require('../pages/crm/reportScoreList.vue')));

const productMonthPriceSum = r => require.ensure([], () => r(require('../pages/crm/productMonthPriceSum.vue')));
const reportScoreOfficeList = r => require.ensure([], () => r(require('../pages/crm/reportScoreOfficeList.vue')));

let routes = [
    {
        path: '/',
        component: home,
        children: [
            {path:'/crm/reportScoreList',component:reportScoreList,name:'reportScoreList' },
            {path:'/crm/reportScoreOfficeList',component:reportScoreOfficeList,name:'reportScoreOfficeList' },
            {path:'/crm/reportScoreAreaList',component:reportScoreAreaList,name:'reportScoreAreaList',meta: {menu:"reportScoreOfficeList"}},
            {path:'/crm/productMonthPriceSum',component:productMonthPriceSum,name:'productMonthPriceSum'},
            {path:'/crm/storeInventoryReport',component:storeInventoryReport,name:'storeInventoryReport'},
            {path:'/crm/productImeStockReport',component:productImeStockReport,name:'productImeStockReport'},
            {path:'/crm/productImeStockReportGrid',component:productImeStockReportGrid,name:'productImeStockReportGrid',meta: {menu:"productImeStockReport"}},
            {path:'/crm/productImeSaleReport',component:productImeSaleReport,name:'productImeSaleReport'},
            {path:'/crm/productImeSaleReportGrid',component:productImeSaleReportGrid,name:'productImeSaleReportGrid',meta: {menu:"productImeSaleReport"}},
        ]
    }
];

export default routes;
