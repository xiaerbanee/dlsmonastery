const login  = r => require.ensure([], () => r(require('../pages/login.vue')));
const notFound  = r => require.ensure([], () => r(require('../pages/404.vue')));
const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const index  = r => require.ensure([], () => r(require('../pages/index.vue')));

const kingdeeBookList  = r => require.ensure([], () => r(require('../pages/sys/kingdeeBookList.vue')));
const kingdeeBookForm  = r => require.ensure([], () => r(require('../pages/sys/kingdeeBookForm.vue')));
const accountKingdeeBookList  = r => require.ensure([], () => r(require('../pages/sys/accountKingdeeBookList.vue')));
const accountKingdeeBookForm  = r => require.ensure([], () => r(require('../pages/sys/accountKingdeeBookForm.vue')));
const currentKingdeeBookForm  = r => require.ensure([], () => r(require('../pages/sys/currentKingdeeBookForm.vue')));
const materialPriceManager  = r => require.ensure([], () => r(require('../pages/sys/materialPriceManager.vue')));
const voucherList  = r => require.ensure([], () => r(require('../pages/sys/voucherList.vue')));
const voucherForm  = r => require.ensure([], () => r(require('../pages/sys/voucherForm.vue')));
const voucherDetail  = r => require.ensure([], () => r(require('../pages/sys/voucherDetail.vue')));
const kingdeeSynList  = r => require.ensure([], () => r(require('../pages/sys/kingdeeSynList.vue')));
const kingdeeSynDetail  = r => require.ensure([], () => r(require('../pages/sys/kingdeeSynDetail.vue')));
const kingdeeSynForNoPushList  = r => require.ensure([], () => r(require('../pages/sys/kingdeeSynForNoPushList.vue')));

let routes = [
    {path: '/login',component: login,name: 'login',meta: {requiresAuth: false}},
    {path: '/404',component: notFound,name: 'notFound'},
    {
        path: '/',
        component: home,
        children: [
            {path: '/index',component: index,name: 'index'},
            {path: '/sys/kingdeeBookList',component: kingdeeBookList,name: 'kingdeeBookList'},
            {path: '/sys/kingdeeBookForm',component: kingdeeBookForm,name: 'kingdeeBookForm',meta:{menu:"kingdeeBookList",keepAlive:true}},
            {path: '/sys/accountKingdeeBookList',component: accountKingdeeBookList,name: 'accountKingdeeBookList'},
            {path: '/sys/accountKingdeeBookForm',component: accountKingdeeBookForm,name: 'accountKingdeeBookForm',meta:{menu:"accountKingdeeBookList"}},
            {path: '/sys/currentKingdeeBookForm',component: currentKingdeeBookForm,name: 'currentKingdeeBookForm'},
            {path: '/sys/materialPriceManager',component: materialPriceManager,name: 'materialPriceManager'},
            {path: '/sys/voucherList',component: voucherList,name: 'voucherList'},
            {path: '/sys/voucherForm',component: voucherForm,name: 'voucherForm',meta:{menu:"voucherList"}},
            {path: '/sys/voucherDetail',component: voucherDetail,name: 'voucherDetail',meta:{menu:"voucherList"}},
            {path: '/sys/kingdeeSynList',component: kingdeeSynList,name: 'kingdeeSynList'},
            {path: '/sys/kingdeeSynDetail',component: kingdeeSynDetail,name: 'kingdeeSynDetail',meta:{menu:"kingdeeSynList"}},
            {path: '/sys/kingdeeSynForNoPushList',component: kingdeeSynForNoPushList,name: 'kingdeeSynForNoPushList'}
        ]
    }

];

export default routes;
