const supplierPayable  = r => require.ensure([], () => r(require('pages/cloud/report/supplierPayable.vue')));
const supplierPayableZMD  = r => require.ensure([], () => r(require('pages/cloud/report/supplierPayableZMD.vue')));
const customerReceive  = r => require.ensure([], () => r(require('pages/cloud/report/customerReceive.vue')));
const consignmentReport  = r => require.ensure([], () => r(require('pages/cloud/report/consignmentReport.vue')));
const retailAccount  = r => require.ensure([], () => r(require('pages/cloud/report/retailAccount.vue')));

let routes = [
  {path: '/cloud/report/supplierPayable',component: supplierPayable,name: 'supplierPayable'},
  {path: '/cloud/report/supplierPayable',component: supplierPayableZMD,name: 'supplierPayableZMD'},
  {path: '/cloud/report/customerReceive',component: customerReceive,name: 'customerReceive'},
  {path: '/cloud/report/consignmentReport',component: consignmentReport,name: 'consignmentReport'},
  {path: '/cloud/report/retailAccount',component: retailAccount,name: 'retailAccount'},
];

export default routes;
