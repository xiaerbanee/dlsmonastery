const login  = r => require.ensure([], () => r(require('../pages/login.vue')));
const notFound  = r => require.ensure([], () => r(require('../pages/404.vue')));
const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const index  = r => require.ensure([], () => r(require('../pages/index.vue')));

const dictEnumList  = r => require.ensure([], () => r(require('../pages/sys/dictEnumList.vue')));
const dictEnumForm  = r => require.ensure([], () => r(require('../pages/sys/dictEnumForm.vue')));
const dictMapList  = r => require.ensure([], () => r(require('../pages/sys/dictMapList.vue')));
const dictMapForm  = r => require.ensure([], () => r(require('../pages/sys/dictMapForm.vue')));
const companyConfigList  = r => require.ensure([], () => r(require('../pages/sys/companyConfigList.vue')));
const companyConfigForm  = r => require.ensure([], () => r(require('../pages/sys/companyConfigForm.vue')));
const menuCategoryList  = r => require.ensure([], () => r(require('../pages/sys/menuCategoryList.vue')));
const menuCategoryForm  = r => require.ensure([], () => r(require('../pages/sys/menuCategoryForm.vue')));
const menuList  = r => require.ensure([], () => r(require('../pages/sys/menuList.vue')));
const menuForm  = r => require.ensure([], () => r(require('../pages/sys/menuForm.vue')));
const folderList  = r => require.ensure([], () => r(require('../pages/sys/folderList.vue')));
const folderForm  = r => require.ensure([], () => r(require('../pages/sys/folderForm.vue')));
const folderFileList  = r => require.ensure([], () => r(require('../pages/sys/folderFileList.vue')));
const processTypeList  = r => require.ensure([], () => r(require('../pages/sys/processTypeList.vue')));
const processTypeForm  = r => require.ensure([], () => r(require('../pages/sys/processTypeForm.vue')));
const permissionList  = r => require.ensure([], () => r(require('../pages/sys/permissionList.vue')));
const permissionForm  = r => require.ensure([], () => r(require('../pages/sys/permissionForm.vue')));
const processList= r => require.ensure([], () => r(require('../pages/sys/processList.vue')));
const backendList  = r => require.ensure([], () => r(require('../pages/sys/backendList.vue')));
const backendForm= r => require.ensure([], () => r(require('../pages/sys/backendForm.vue')));
const backendModuleList  = r => require.ensure([], () => r(require('../pages/sys/backendModuleList.vue')));
const backendModuleForm= r => require.ensure([], () => r(require('../pages/sys/backendModuleForm.vue')));
const officeRuleList  = r => require.ensure([], () => r(require('../pages/sys/officeRuleList.vue')));
const officeRuleForm= r => require.ensure([], () => r(require('../pages/sys/officeRuleForm.vue')));
const officeList = r => require.ensure([],() => r(require('../pages/sys/officeList.vue')));
const officeForm = r => require.ensure([],() => r(require('../pages/sys/officeForm.vue')));
const officeBusinessForm = r => require.ensure([],() => r(require('../pages/sys/officeBusinessForm.vue')));
const officeChangeList = r => require.ensure([],() => r(require('../pages/sys/officeChangeList.vue')));
const officeChangeForm = r => require.ensure([],() => r(require('../pages/sys/officeChangeForm.vue')));
const roleList = r => require.ensure([],() => r(require('../pages/sys/roleList.vue')));
const roleForm = r => require.ensure([],() => r(require('../pages/sys/roleForm.vue')));
const roleAuthorityForm = r => require.ensure([],() => r(require('../pages/sys/roleAuthorityForm.vue')));

let routes = [
    {path: '/login',component: login,name: 'login',meta: {requiresAuth: false}},
    {path: '/404',component: notFound,name: 'notFound'},
    {
        path: '/',
        component: home,
        children: [
            {path: '/index',component: index,name: 'index'},
            {path:'/sys/processList',component:processList,name:'processList'},
            {path:'/sys/dictEnumList',component:dictEnumList,name:'dictEnumList',meta: {menu:"dictEnumList"}},
            {path:'/sys/dictEnumForm',component:dictEnumForm,name:'dictEnumForm',meta: {menu:"dictEnumList",keepAlive:true}},
            {path:'/sys/dictMapList',component:dictMapList,name:'dictMapList'},
            {path:'/sys/dictMapForm',component:dictMapForm,name:'dictMapForm',meta: {menu:"dictMapList",keepAlive:true}},
            {path:'/sys/companyConfigList',component:companyConfigList,name:'companyConfigList'},
            {path:'/sys/companyConfigForm',component:companyConfigForm,name:'companyConfigForm',meta: {menu:"companyConfigList",keepAlive:true}},
            {path:'/sys/menuCategoryList',component:menuCategoryList,name:'menuCategoryList'},
            {path:'/sys/menuCategoryForm',component:menuCategoryForm,name:'menuCategoryForm',meta: {menu:"menuCategoryList",keepAlive:true}},
            {path:'/sys/menuList',component:menuList,name:'menuList'},
            {path:'/sys/menuForm',component:menuForm,name:'menuForm',meta: {menu:"menuList",keepAlive:true}},
            {path:'/sys/folderList',component:folderList,name:'folderList'},
            {path:'/sys/folderForm',component:folderForm,name:'folderForm',meta: {menu:"folderList",keepAlive:true}},
            {path:'/sys/folderFileList',component:folderFileList,name:'folderFileList'},
            {path:'/sys/processTypeList',component:processTypeList,name:'processTypeList'},
            {path:'/sys/processTypeForm',component:processTypeForm,name:'processTypeForm',meta: {menu:"processTypeList",keepAlive:true}},
            {path:'/sys/permissionList',component:permissionList,name:'permissionList'},
            {path:'/sys/permissionForm',component:permissionForm,name:'permissionForm',meta: {menu:"permissionList",keepAlive:true}},
            {path:'/sys/backendList',component:backendList,name:'backendList'},
            {path:'/sys/backendForm',component:backendForm,name:'backendForm',meta: {menu:"backendList",keepAlive:true}},
            {path:'/sys/backendModuleList',component:backendModuleList,name:'backendModuleList'},
            {path:'/sys/backendModuleForm',component:backendModuleForm,name:'backendModuleForm',meta: {menu:"backendModuleList",keepAlive:true}},
            {path:'/sys/officeRuleList',component:officeRuleList,name:'officeRuleList'},
            {path:'/sys/officeRuleForm',component:officeRuleForm,name:'officeRuleForm',meta: {menu:"officeRuleList",keepAlive:true}},
            {path:'/sys/officeList',component:officeList,name:'officeList'},
            {path:'/sys/officeForm',component:officeForm,name:'officeForm',meta: {menu:"officeList",keepAlive:true}},
            {path:'/sys/officeBusinessForm',component:officeBusinessForm,name:'officeBusinessForm',meta: {menu:"officeList",keepAlive:true}},
            {path:'/sys/officeChangeList',component:officeChangeList,name:'officeChangeList'},
            {path:'/sys/officeChangeForm',component:officeChangeForm,name:'officeChangeForm',meta: {menu:"officeList",keepAlive:true}},
            {path:'/sys/roleList',component:roleList,name:'roleList'},
            {path:'/sys/roleForm',component:roleForm,name:'roleForm',meta: {menu:"roleList",keepAlive:true}},
            {path:'/sys/roleAuthorityForm',component:roleAuthorityForm,name:'roleAuthorityForm',meta: {menu:"roleList",keepAlive:true}},
        ]
    }

];

export default routes;
