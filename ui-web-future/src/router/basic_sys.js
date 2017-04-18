const dictEnumList  = r => require.ensure([], () => r(require('pages/sys/dictEnumList.vue')));
const dictEnumForm  = r => require.ensure([], () => r(require('pages/sys/dictEnumForm.vue')));
const dictMapList  = r => require.ensure([], () => r(require('pages/sys/dictMapList.vue')));
const dictMapForm  = r => require.ensure([], () => r(require('pages/sys/dictMapForm.vue')));
const companyConfigList  = r => require.ensure([], () => r(require('pages/sys/companyConfigList.vue')));
const companyConfigForm  = r => require.ensure([], () => r(require('pages/sys/companyConfigForm.vue')));
const menuCategoryList  = r => require.ensure([], () => r(require('pages/sys/menuCategoryList.vue')));
const menuCategoryForm  = r => require.ensure([], () => r(require('pages/sys/menuCategoryForm.vue')));
const menuList  = r => require.ensure([], () => r(require('pages/sys/menuList.vue')));
const menuForm  = r => require.ensure([], () => r(require('pages/sys/menuForm.vue')));
const folderList  = r => require.ensure([], () => r(require('pages/sys/folderList.vue')));
const folderForm  = r => require.ensure([], () => r(require('pages/sys/folderForm.vue')));
const folderFileList  = r => require.ensure([], () => r(require('pages/sys/folderFileList.vue')));
const processTypeList  = r => require.ensure([], () => r(require('pages/sys/processTypeList.vue')));
const processTypeForm  = r => require.ensure([], () => r(require('pages/sys/processTypeForm.vue')));
const permissionList  = r => require.ensure([], () => r(require('pages/sys/permissionList.vue')));
const permissionForm  = r => require.ensure([], () => r(require('pages/sys/permissionForm.vue')));
const processList= r => require.ensure([], () => r(require('pages/sys/processList.vue')));



let routes = [
  {path:'/sys/processList',component:processList,name:'processList'},
  {path:'/sys/dictEnumList',component:dictEnumList,name:'dictEnumList'},
  {path:'/sys/dictEnumForm',component:dictEnumForm,name:'dictEnumForm',meta: {menu:"dictEnumList"}},
  {path:'/sys/dictMapList',component:dictMapList,name:'dictMapList'},
  {path:'/sys/dictMapForm',component:dictMapForm,name:'dictMapForm',meta: {menu:"dictMapList"}},
  {path:'/sys/companyConfigList',component:companyConfigList,name:'companyConfigList'},
  {path:'/sys/companyConfigForm',component:companyConfigForm,name:'companyConfigForm',meta: {menu:"companyConfigList"}},
  {path:'/sys/menuCategoryList',component:menuCategoryList,name:'menuCategoryList'},
  {path:'/sys/menuCategoryForm',component:menuCategoryForm,name:'menuCategoryForm',meta: {menu:"menuCategoryList"}},
  {path:'/sys/menuList',component:menuList,name:'menuList'},
  {path:'/sys/menuForm',component:menuForm,name:'menuForm',meta: {menu:"menuList"}},
  {path:'/sys/folderList',component:folderList,name:'folderList'},
  {path:'/sys/folderForm',component:folderForm,name:'folderForm',meta: {menu:"folderList"}},
  {path:'/sys/folderFileList',component:folderFileList,name:'folderFileList'},
  {path:'/sys/processTypeList',component:processTypeList,name:'processTypeList'},
  {path:'/sys/processTypeForm',component:processTypeForm,name:'processTypeForm',meta: {menu:"processTypeList"}},
  {path:'/sys/permissionList',component:permissionList,name:'permissionList'},
  {path:'/sys/permissionForm',component:permissionForm,name:'permissionForm',meta: {menu:"permissionList"}},
];

export default routes;
