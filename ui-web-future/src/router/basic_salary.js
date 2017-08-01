const salaryList  = r => require.ensure([], () => r(require('pages/basic/salary/salaryList.vue')));
const salaryForm = r => require.ensure([], () => r(require('pages/basic/salary/salaryForm.vue')));
const employeeSalaryBasicList = r => require.ensure([], () => r(require('pages/basic/salary/employeeSalaryBasicList.vue')));
const employeeSalaryBasicForm = r => require.ensure([], () => r(require('pages/basic/salary/employeeSalaryBasicForm.vue')));
const employeeSalaryList = r => require.ensure([], () => r(require('pages/basic/salary/employeeSalaryList.vue')));
const employeeSalaryForm = r => require.ensure([], () => r(require('pages/basic/salary/employeeSalaryForm.vue')));
const salaryTemplateList = r => require.ensure([], () => r(require('pages/basic/salary/salaryTemplateList.vue')));
const salaryTemplateForm = r => require.ensure([], () => r(require('pages/basic/salary/salaryTemplateForm.vue')));

let routes=[
  {path:'/basic/salary/salaryList',component:salaryList,name:'salaryList'},
  {path:'/basic/salary/salaryForm',component:salaryForm,name:'salaryForm'},
  {path:'/basic/salary/employeeSalaryBasicList',component:employeeSalaryBasicList,name:'employeeSalaryBasicList'},
  {path:'/basic/salary/employeeSalaryBasicForm',component:employeeSalaryBasicForm,name:'employeeSalaryBasicForm',meta: {menu:"employeeSalaryBasicList",keepAlive:true}},
  {path:'/basic/salary/employeeSalaryList',component:employeeSalaryList,name:'employeeSalaryList'},
  {path:'/basic/salary/employeeSalaryForm',component:employeeSalaryForm,name:'employeeSalaryForm',meta: {menu:"employeeSalaryList",keepAlive:true}},
  {path:'/basic/salary/salaryTemplateList',component:salaryTemplateList,name:'salaryTemplateList'},
  {path:'/basic/salary/salaryTemplateForm',component:salaryTemplateForm,name:'salaryTemplateForm',meta: {menu:"salaryTemplateList",keepAlive:true}},

]

export default routes;
