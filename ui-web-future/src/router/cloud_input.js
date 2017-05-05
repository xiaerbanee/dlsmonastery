const batchBillForm  = r => require.ensure([], () => r(require('pages/cloud/input/batchBillForm.vue')));
const batchMaterialForm  = r => require.ensure([], () => r(require('pages/cloud/input/batchMaterialForm.vue')));

let routes = [
  {path: '/cloud/input/batchBillForm',component: batchBillForm,name: 'batchBillForm'},
  {path: '/cloud/input/batchMaterialForm',component: batchMaterialForm,name: 'batchMaterialForm'},
];

export default routes;
