const batchBillForm  = r => require.ensure([], () => r(require('pages/cloud/input/batchBillForm.vue')));
const batchDeliveryForm  = r => require.ensure([], () => r(require('pages/cloud/input/batchDeliveryForm.vue')));
const batchMaterialForm  = r => require.ensure([], () => r(require('pages/cloud/input/batchMaterialForm.vue')));

let routes = [
  {path: '/cloud/input/batchBillForm',component: batchBillForm,name: 'batchBillForm'},
  {path: '/cloud/input/batchDeliveryForm',component: batchDeliveryForm,name: 'batchDeliveryForm'},
  {path: '/cloud/input/batchMaterialForm',component: batchMaterialForm,name: 'batchMaterialForm'},
];

export default routes;
