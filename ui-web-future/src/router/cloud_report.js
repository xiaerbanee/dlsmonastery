const payableReport  = r => require.ensure([], () => r(require('pages/cloud/report/payableReport.vue')));
const customerReceive  = r => require.ensure([], () => r(require('pages/cloud/report/customerReceive.vue')));
const consignmentReport  = r => require.ensure([], () => r(require('pages/cloud/report/consignmentReport.vue')));
const retailReport  = r => require.ensure([], () => r(require('pages/cloud/report/retailReport.vue')));

let routes = [
  {path: '/cloud/report/payableReport',component: payableReport,name: 'payableReport'},
  {path: '/cloud/report/customerReceive',component: customerReceive,name: 'customerReceive'},
  {path: '/cloud/report/consignmentReport',component: consignmentReport,name: 'consignmentReport'},
  {path: '/cloud/report/retailReport',component: retailReport,name: 'retailReport'},
];

export default routes;
