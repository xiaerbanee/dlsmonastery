const payableReport  = r => require.ensure([], () => r(require('pages/cloud/report/payableReport.vue')));
const receivableReport  = r => require.ensure([], () => r(require('pages/cloud/report/receivableReport.vue')));
const consignmentReport  = r => require.ensure([], () => r(require('pages/cloud/report/consignmentReport.vue')));

let routes = [
  {path: '/cloud/report/payableReport',component: payableReport,name: 'payableReport'},
  {path: '/cloud/report/receivableReport',component: receivableReport,name: 'receivableReport'},
  {path: '/cloud/report/consignmentReport',component: consignmentReport,name: 'consignmentReport'},
];

export default routes;
