const dictEnumList  = r => require.ensure([], () => r(require('pages/basic/sys/dictEnumList.vue')));
const dictEnumForm  = r => require.ensure([], () => r(require('pages/basic/sys/dictEnumForm.vue')));
const dictMapList  = r => require.ensure([], () => r(require('pages/basic/sys/dictMapList.vue')));
const dictMapForm  = r => require.ensure([], () => r(require('pages/basic/sys/dictMapForm.vue')));
const companyConfigList  = r => require.ensure([], () => r(require('pages/basic/sys/companyConfigList.vue')));
const companyConfigForm  = r => require.ensure([], () => r(require('pages/basic/sys/companyConfigForm.vue')));
const menuCategoryList  = r => require.ensure([], () => r(require('pages/basic/sys/menuCategoryList.vue')));
const menuCategoryForm  = r => require.ensure([], () => r(require('pages/basic/sys/menuCategoryForm.vue')));
const menuList  = r => require.ensure([], () => r(require('pages/basic/sys/menuList.vue')));
const menuForm  = r => require.ensure([], () => r(require('pages/basic/sys/menuForm.vue')));
const folderList  = r => require.ensure([], () => r(require('pages/basic/sys/folderList.vue')));
const folderForm  = r => require.ensure([], () => r(require('pages/basic/sys/folderForm.vue')));
const folderFileList  = r => require.ensure([], () => r(require('pages/basic/sys/folderFileList.vue')));
const processTypeList  = r => require.ensure([], () => r(require('pages/basic/sys/processTypeList.vue')));
const processTypeForm  = r => require.ensure([], () => r(require('pages/basic/sys/processTypeForm.vue')));
const permissionList  = r => require.ensure([], () => r(require('pages/basic/sys/permissionList.vue')));
const permissionForm  = r => require.ensure([], () => r(require('pages/basic/sys/permissionForm.vue')));
const processList= r => require.ensure([], () => r(require('pages/basic/sys/processList.vue')));



let routes = [
  {path:'/basic/sys/processList',component:processList,name:'processList'},
  {path:'/basic/sys/dictEnumList',component:dictEnumList,name:'dictEnumList',meta: {menu:"dictEnumList"}},
  {path:'/basic/sys/dictEnumForm',component:dictEnumForm,name:'dictEnumForm',meta: {menu:"dictEnumForm"}},
  {path:'/basic/sys/dictMapList',component:dictMapList,name:'dictMapList'},
  {path:'/basic/sys/dictMapForm',component:dictMapForm,name:'dictMapForm',meta: {menu:"dictMapList"}},
  {path:'/basic/sys/companyConfigList',component:companyConfigList,name:'companyConfigList'},
  {path:'/basic/sys/companyConfigForm',component:companyConfigForm,name:'companyConfigForm',meta: {menu:"companyConfigList"}},
  {path:'/basic/sys/menuCategoryList',component:menuCategoryList,name:'menuCategoryList'},
  {path:'/basic/sys/menuCategoryForm',component:menuCategoryForm,name:'menuCategoryForm',meta: {menu:"menuCategoryList"}},
  {path:'/basic/sys/menuList',component:menuList,name:'menuList'},
  {path:'/basic/sys/menuForm',component:menuForm,name:'menuForm',meta: {menu:"menuList"}},
  {path:'/basic/sys/folderList',component:folderList,name:'folderList'},
  {path:'/basic/sys/folderForm',component:folderForm,name:'folderForm',meta: {menu:"folderList"}},
  {path:'/basic/sys/folderFileList',component:folderFileList,name:'folderFileList'},
  {path:'/basic/sys/processTypeList',component:processTypeList,name:'processTypeList'},
  {path:'/basic/sys/processTypeForm',component:processTypeForm,name:'processTypeForm',meta: {menu:"processTypeList"}},
  {path:'/basic/sys/permissionList',component:permissionList,name:'permissionList'},
  {path:'/basic/sys/permissionForm',component:permissionForm,name:'permissionForm',meta: {menu:"permissionList"}},
];

export default routes;
