const salOutStock  = r => require.ensure([], () => r(require('pages/cloud/input/salOutStock.vue')));
const salReturnStock  = r => require.ensure([], () => r(require('pages/cloud/input/salReturnStock.vue')));
const stkMisDelivery  = r => require.ensure([], () => r(require('pages/cloud/input/stkMisDelivery.vue')));


let routes = [
  {path: '/cloud/input/salOutStock',component: salOutStock,name: 'salOutStock'},
  {path: '/cloud/input/salReturnStock',component: salReturnStock,name: 'salReturnStock'},
  {path: '/cloud/input/stkMisDelivery',component: stkMisDelivery,name: 'stkMisDelivery'},
];

export default routes;
