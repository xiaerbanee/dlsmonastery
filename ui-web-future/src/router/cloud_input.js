const salOutStock  = r => require.ensure([], () => r(require('pages/cloud/input/salOutStock.vue')));
const batchDeliveryForm  = r => require.ensure([], () => r(require('pages/cloud/input/batchDeliveryForm.vue')));
const batchMaterialForm  = r => require.ensure([], () => r(require('pages/cloud/input/batchMaterialForm.vue')));

let routes = [
  {path: '/cloud/input/salOutStock',component: salOutStock,name: 'salOutStock'},
  {path: '/cloud/input/batchDeliveryForm',component: batchDeliveryForm,name: 'batchDeliveryForm'},
  {path: '/cloud/input/batchMaterialForm',component: batchMaterialForm,name: 'batchMaterialForm'},
];

export default routes;
