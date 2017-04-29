const payableReport  = r => require.ensure([], () => r(require('pages/cloud/report/payableReport.vue')));
const receivableReport  = r => require.ensure([], () => r(require('pages/cloud/report/receivableReport.vue')));

let routes = [
  {path: '/cloud/report/payableReport',component: payableReport,name: 'payableReport'},
  {path: '/cloud/report/receivableReport',component: receivableReport,name: 'receivableReport'},
];

export default routes;
