const payableReport  = r => require.ensure([], () => r(require('pages/cloud/report/payableReport.vue')));

let routes = [
  {path: '/cloud/report/payableReport',component: payableReport,name: 'payableReport'},
];

export default routes;
