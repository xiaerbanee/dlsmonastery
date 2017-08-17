const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const salaryList  = r => require.ensure([], () => r(require('../pages/salary/salaryList.vue')));
const salaryForm = r => require.ensure([], () => r(require('../pages/salary/salaryForm.vue')));
const employeeSalaryBasicList = r => require.ensure([], () => r(require('../pages/salary/employeeSalaryBasicList.vue')));
const employeeSalaryBasicForm = r => require.ensure([], () => r(require('../pages/salary/employeeSalaryBasicForm.vue')));
const employeeSalaryList = r => require.ensure([], () => r(require('../pages/salary/employeeSalaryList.vue')));
const employeeSalaryForm = r => require.ensure([], () => r(require('../pages/salary/employeeSalaryForm.vue')));
const salaryTemplateList = r => require.ensure([], () => r(require('../pages/salary/salaryTemplateList.vue')));
const salaryTemplateForm = r => require.ensure([], () => r(require('../pages/salary/salaryTemplateForm.vue')));

let routes = [
    {
        path: '/',
        component: home,
        children: [
            {path:'/salary/salaryList',component:salaryList,name:'salaryList'},
            {path:'/salary/salaryForm',component:salaryForm,name:'salaryForm'},
            {path:'/salary/employeeSalaryBasicList',component:employeeSalaryBasicList,name:'employeeSalaryBasicList'},
            {path:'/salary/employeeSalaryBasicForm',component:employeeSalaryBasicForm,name:'employeeSalaryBasicForm',meta: {menu:"employeeSalaryBasicList",keepAlive:true}},
            {path:'/salary/employeeSalaryList',component:employeeSalaryList,name:'employeeSalaryList'},
            {path:'/salary/employeeSalaryForm',component:employeeSalaryForm,name:'employeeSalaryForm',meta: {menu:"employeeSalaryList",keepAlive:true}},
            {path:'/salary/salaryTemplateList',component:salaryTemplateList,name:'salaryTemplateList'},
            {path:'/salary/salaryTemplateForm',component:salaryTemplateForm,name:'salaryTemplateForm',meta: {menu:"salaryTemplateList",keepAlive:true}}
        ]
    }

];

export default routes;
