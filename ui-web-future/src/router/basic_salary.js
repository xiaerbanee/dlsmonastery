const salaryList  = r => require.ensure([], () => r(require('pages/basic/salary/salaryList.vue')));
const salaryForm = r => require.ensure([], () => r(require('pages/basic/salary/salaryForm.vue')));

let routes=[
  {path:'/basic/salary/salaryList',component:salaryList,name:'salaryList'},
  {path:'/basic/salary/salaryForm',component:salaryForm,name:'salaryForm'},

]

export default routes;
