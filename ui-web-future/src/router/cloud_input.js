const batchBillForm  = r => require.ensure([], () => r(require('pages/cloud/input/batchBillForm.vue')));

let routes = [
  {path: '/cloud/input/batchBillForm',component: batchBillForm,name: 'batchBillForm'},
];

export default routes;
