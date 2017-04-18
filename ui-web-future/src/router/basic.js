

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


const employeeList  = r => require.ensure([], () => r(require('pages/hr/employeeList.vue')));
const employeeForm  = r => require.ensure([], () => r(require('pages/hr/employeeForm.vue')));
const employeeEditForm  = r => require.ensure([], () => r(require('pages/hr/employeeEditForm.vue')));
const accountList  = r => require.ensure([], () => r(require('pages/hr/accountList.vue')));
const accountForm  = r => require.ensure([], () => r(require('pages/hr/accountForm.vue')));
const accountChangeList  = r => require.ensure([], () => r(require('pages/hr/accountChangeList.vue')));
const accountChangeForm  = r => require.ensure([], () => r(require('pages/hr/accountChangeForm.vue')));
const positionList = r => require.ensure([],() => r(require('pages/hr/positionList.vue')));
const positionForm = r => require.ensure([],() => r(require('pages/hr/positionForm.vue')));
const jobList = r => require.ensure([],() => r(require('pages/hr/jobList.vue')));
const jobForm = r => require.ensure([],() => r(require('pages/hr/jobForm.vue')));
const dutySignList = r => require.ensure([],() => r(require('pages/hr/dutySignList.vue')));
const dutyAnnualList = r => require.ensure([],() => r(require('pages/hr/dutyAnnualList.vue')));
const dutyAnnualForm = r => require.ensure([],() => r(require('pages/hr/dutyAnnualForm.vue')));
const dutyWorktimeList = r => require.ensure([],() => r(require('pages/hr/dutyWorktimeList.vue')));
const dutyWorktimeForm = r => require.ensure([],() => r(require('pages/hr/dutyWorktimeForm.vue')));
const dutyTaskList = r => require.ensure([],() => r(require('pages/hr/dutyTaskList.vue')));
const dutyTaskForm = r => require.ensure([],() => r(require('pages/hr/dutyTaskForm.vue')));
const employeePhoneList = r => require.ensure([],() => r(require('pages/hr/employeePhoneList.vue')));
const employeePhoneForm = r => require.ensure([],() => r(require('pages/hr/employeePhoneForm.vue')));
const employeePhoneDepositList = r => require.ensure([],() => r(require('pages/hr/employeePhoneDepositList.vue')));
const employeePhoneDepositForm = r => require.ensure([],() => r(require('pages/hr/employeePhoneDepositForm.vue')));
const officeList = r => require.ensure([],() => r(require('pages/hr/officeList.vue')));
const officeForm = r => require.ensure([],() => r(require('pages/hr/officeForm.vue')));
const recruitList = r => require.ensure([],() => r(require('pages/hr/recruitList.vue')));
const recruitForm = r => require.ensure([],() => r(require('pages/hr/recruitForm.vue')));
const recruitBatchForm = r => require.ensure([],() => r(require('pages/hr/recruitBatchForm.vue')));

const dutyLeaveList= r => require.ensure([], () => r(require('pages/hr/dutyLeaveList.vue')));
const dutyFreeList= r => require.ensure([], () => r(require('pages/hr/dutyFreeList.vue')));
const dutyTripList= r => require.ensure([], () => r(require('pages/hr/dutyTripList.vue')));
const dutyRestList= r => require.ensure([], () => r(require('pages/hr/dutyRestList.vue')));
const dutyPublicFreeList= r => require.ensure([], () => r(require('pages/hr/dutyPublicFreeList.vue')));
const dutyOvertimeList= r => require.ensure([], () => r(require('pages/hr/dutyOvertimeList.vue')));
const auditFileList= r => require.ensure([], () => r(require('pages/hr/auditFileList.vue')));
const auditFileForm= r => require.ensure([], () => r(require('pages/hr/auditFileForm.vue')));
const auditFileDetail= r => require.ensure([], () => r(require('pages/hr/auditFileDetail.vue')));
const accountTaskList= r => require.ensure([], () => r(require('pages/hr/accountTaskList.vue')));



let routes = [
  {path:'/sys/processList',component:processList,name:'processList'},
  {path:'/hr/dutyLeaveList',component:dutyLeaveList,name:'dutyLeaveList'},
  {path:'/hr/dutyTripList',component:dutyTripList,name:'dutyTripList'},
  {path:'/hr/dutyOvertimeList',component:dutyOvertimeList,name:'dutyOvertimeList'},
  {path:'/hr/dutyRestList',component:dutyRestList,name:'dutyRestList'},
  {path:'/hr/dutyFreeList',component:dutyFreeList,name:'dutyFreeList'},
  {path:'/hr/dutyPublicFreeList',component:dutyPublicFreeList,name:'dutyPublicFreeList'},
  {path:'/sys/dictEnumList',component:dictEnumList,name:'dictEnumList'},
  {path:'/sys/dictEnumForm',component:dictEnumForm,name:'dictEnumForm',meta: {menu:"dictEnumList"}},
  {path:'/sys/dictMapList',component:dictMapList,name:'dictMapList'},
  {path:'/sys/dictMapForm',component:dictMapForm,name:'dictMapForm',meta: {menu:"dictMapList"}},
  {path:'/sys/companyConfigList',component:companyConfigList,name:'companyConfigList'},
  {path:'/sys/companyConfigForm',component:companyConfigForm,name:'companyConfigForm',meta: {menu:"companyConfigList"}},
  {path:'/hr/accountList',component:accountList,name:'accountList'},
  {path:'/hr/accountForm',component:accountForm,name:'accountForm',meta:{menu:"accountList"}},
  {path:'/hr/accountChangeList',component:accountChangeList,name:'accountChangeList'},
  {path:'/hr/accountChangeForm',component:accountChangeForm,name:'accountChangeForm',meta:{menu:"accountChangeList"}},
  {path:'/hr/employeeList',component:employeeList,name:'employeeList'},
  {path:'/hr/employeeForm',component:employeeForm,name:'employeeForm',meta: {menu:"employeeList"}},
  {path:'/hr/employeeEditForm',component:employeeEditForm,name:'employeeEditForm',meta: {menu:"employeeList"}},
  {path:'/hr/employeePhoneList',component:employeePhoneList,name:'employeePhoneList'},
  {path:'/hr/employeePhoneForm',component:employeePhoneForm,name:'employeePhoneForm',meta: {menu:"employeePhoneList"}},
  {path:'/hr/employeePhoneDepositList',component:employeePhoneDepositList,name:'employeePhoneDepositList'},
  {path:'/hr/employeePhoneDepositForm',component:employeePhoneDepositForm,name:'employeePhoneDepositForm',meta: {menu:"employeePhoneDepositList"}},
  {path:'/hr/positionList',component:positionList,name:'positionList'},
  {path:'/hr/positionForm',component:positionForm,name:'positionForm',meta: {menu:"positionList"}},
  {path:'/hr/jobList',component:jobList,name:'jobList'},
  {path:'/hr/jobForm',component:jobForm,name:'jobForm',meta:{menu:"jobList"}},
  {path:'/hr/dutySignList',component:dutySignList,name:'dutySignList'},
  {path:'/hr/dutyAnnualList',component:dutyAnnualList,name:'dutyAnnualList'},
  {path:'/hr/dutyAnnualForm',component:dutyAnnualForm,name:'dutyAnnualForm',meta:{menu:"dutyAnnualList"}},
  {path:'/hr/dutyWorktimeList',component:dutyWorktimeList,name:'dutyWorktimeList'},
  {path:'/hr/dutyWorktimeForm',component:dutyWorktimeForm,name:'dutyWorktimeForm',meta: {menu:"dutyWorktimeList"}},
  {path:'/hr/dutyTaskList',component:dutyTaskList,name:'dutyTaskList'},
  {path:'/hr/dutyTaskForm',component:dutyTaskForm,name:'dutyTaskForm',meta: {menu:"dutyTaskList"}},
  {path:'/hr/auditFileList',component:auditFileList,name:'auditFileList'},
  {path:'/hr/auditFileForm',component:auditFileForm,name:'auditFileForm',meta: {menu:"auditFileList"}},
  {path:'/hr/auditFileDetail',component:auditFileDetail,name:'auditFileDetail',meta: {menu:"auditFileList"}},
  {path:'/hr/officeList',component:officeList,name:'officeList'},
  {path:'/hr/officeForm',component:officeForm,name:'officeForm',meta: {menu:"officeList"}},
  {path:'/hr/accountTaskList',component:accountTaskList,name:'accountTaskList'},
  {path:'/hr/recruitList',component:recruitList,name:'recruitList'},
  {path:'/hr/recruitForm',component:recruitForm,name:'recruitForm',meta: {menu:"recruitList"}},
  {path:'/hr/recruitBatchForm',component:recruitBatchForm,name:'recruitBatchForm',meta: {menu:"recruitList"}},

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
