const salOutStock  = r => require.ensure([], () => r(require('pages/cloud/input/salOutStock.vue')));
const salReturnStock  = r => require.ensure([], () => r(require('pages/cloud/input/salReturnStock.vue')));
const stkMisDelivery  = r => require.ensure([], () => r(require('pages/cloud/input/stkMisDelivery.vue')));
const cnJournalForCash  = r => require.ensure([], () => r(require('pages/cloud/input/cnJournalForCash.vue')));
const cnJournalForBank  = r => require.ensure([], () => r(require('pages/cloud/input/cnJournalForBank.vue')));
const purMrb  = r => require.ensure([], () => r(require('pages/cloud/input/purMrb.vue')));
const stkInStock  = r => require.ensure([], () => r(require('pages/cloud/input/stkInStock.vue')));
const arOtherRecAble  = r => require.ensure([], () => r(require('pages/cloud/input/arOtherRecAble.vue')));

let routes = [
  {path: '/cloud/input/salOutStock',component: salOutStock,name: 'salOutStock'},
  {path: '/cloud/input/salReturnStock',component: salReturnStock,name: 'salReturnStock'},
  {path: '/cloud/input/stkMisDelivery',component: stkMisDelivery,name: 'stkMisDelivery'},
  {path: '/cloud/input/cnJournalForCash',component: cnJournalForCash,name: 'cnJournalForCash'},
  {path: '/cloud/input/cnJournalForBank',component: cnJournalForBank,name: 'cnJournalForBank'},
  {path: '/cloud/input/purMrb',component: purMrb,name: 'purMrb'},
  {path: '/cloud/input/stkInStock',component: stkInStock,name: 'stkInStock'},
  {path: '/cloud/input/arOtherRecAble',component: arOtherRecAble,name: 'arOtherRecAble'},
];

export default routes;
