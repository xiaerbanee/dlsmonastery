const salOutStock  = r => require.ensure([], () => r(require('pages/cloud/input/salOutStock.vue')));
const salReturnStock  = r => require.ensure([], () => r(require('pages/cloud/input/salReturnStock.vue')));

let routes = [
  {path: '/cloud/input/salOutStock',component: salOutStock,name: 'salOutStock'},
  {path: '/cloud/input/salReturnStock',component: salReturnStock,name: 'salReturnStock'},
];

export default routes;
