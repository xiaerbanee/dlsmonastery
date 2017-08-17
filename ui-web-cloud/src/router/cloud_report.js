const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const supplierPayable  = r => require.ensure([], () => r(require('../pages/report/supplierPayable.vue')));
const supplierPayableZMD  = r => require.ensure([], () => r(require('../pages/report/supplierPayableZMD.vue')));
const customerReceive  = r => require.ensure([], () => r(require('../pages/report/customerReceive.vue')));
const consignmentReport  = r => require.ensure([], () => r(require('../pages/report/consignmentReport.vue')));
const retailAccount  = r => require.ensure([], () => r(require('../pages/report/retailAccount.vue')));
const salProxyReceive  = r => require.ensure([], () => r(require('../pages/report/salProxyReceive.vue')));

let routes = [
    {
        path: '/',
        component: home,
        children: [
            {path: '/report/supplierPayable',component: supplierPayable,name: 'supplierPayable'},
            {path: '/report/supplierPayableZMD',component: supplierPayableZMD,name: 'supplierPayableZMD'},
            {path: '/report/customerReceive',component: customerReceive,name: 'customerReceive'},
            {path: '/report/consignmentReport',component: consignmentReport,name: 'consignmentReport'},
            {path: '/report/retailAccount',component: retailAccount,name: 'retailAccount'},
            {path: '/cloud/report/salProxyReceive',component: salProxyReceive,name: 'salProxyReceive'},
        ]
    }
];

export default routes;
