const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const salOutStock  = r => require.ensure([], () => r(require('../pages/input/salOutStock.vue')));
const salReturnStock  = r => require.ensure([], () => r(require('../pages/input/salReturnStock.vue')));
const stkMisDelivery  = r => require.ensure([], () => r(require('../pages/input/stkMisDelivery.vue')));
const cnJournalForCash  = r => require.ensure([], () => r(require('../pages/input/cnJournalForCash.vue')));
const cnJournalForBank  = r => require.ensure([], () => r(require('../pages/input/cnJournalForBank.vue')));
const purMrb  = r => require.ensure([], () => r(require('../pages/input/purMrb.vue')));
const stkInStock  = r => require.ensure([], () => r(require('../pages/input/stkInStock.vue')));
const arOtherRecAble  = r => require.ensure([], () => r(require('../pages/input/arOtherRecAble.vue')));
const apPayBill  = r => require.ensure([], () => r(require('../pages/input/apPayBill.vue')));
const arRefundBill  = r => require.ensure([], () => r(require('../pages/input/arRefundBill.vue')));
const arReceiveBill  = r => require.ensure([], () => r(require('../pages/input/arReceiveBill.vue')));
const batchMaterial  = r => require.ensure([], () => r(require('../pages/input/batchMaterial.vue')));
const hsAdjustmentBill  = r => require.ensure([], () => r(require('../pages/input/hsAdjustmentBill.vue')));

let routes = [
    {
        path: '/',
        component: home,
        children: [
            {path: '/input/salOutStock',component: salOutStock,name: 'salOutStock'},
            {path: '/input/salReturnStock',component: salReturnStock,name: 'salReturnStock'},
            {path: '/input/stkMisDelivery',component: stkMisDelivery,name: 'stkMisDelivery'},
            {path: '/input/cnJournalForCash',component: cnJournalForCash,name: 'cnJournalForCash'},
            {path: '/input/cnJournalForBank',component: cnJournalForBank,name: 'cnJournalForBank'},
            {path: '/input/purMrb',component: purMrb,name: 'purMrb'},
            {path: '/input/stkInStock',component: stkInStock,name: 'stkInStock'},
            {path: '/input/arOtherRecAble',component: arOtherRecAble,name: 'arOtherRecAble'},
            {path: '/input/apPayBill',component: apPayBill,name: 'apPayBill'},
            {path: '/input/arRefundBill',component: arRefundBill,name: 'arRefundBill'},
            {path: '/input/arReceiveBill',component: arReceiveBill,name: 'arReceiveBill'},
            {path: '/input/batchMaterial',component: batchMaterial,name: 'batchMaterial'},
            {path: '/cloud/input/hsAdjustmentBill',component: hsAdjustmentBill,name: 'hsAdjustmentBill'},
        ]
    }

];

export default routes;
