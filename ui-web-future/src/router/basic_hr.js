const employeeList  = r => require.ensure([], () => r(require('pages/basic/hr/employeeList.vue')));
const employeeForm  = r => require.ensure([], () => r(require('pages/basic/hr/employeeForm.vue')));
const employeeEditForm  = r => require.ensure([], () => r(require('pages/basic/hr/employeeEditForm.vue')));
const accountList  = r => require.ensure([], () => r(require('pages/basic/hr/accountList.vue')));
const accountForm  = r => require.ensure([], () => r(require('pages/basic/hr/accountForm.vue')));
const accountChangeList  = r => require.ensure([], () => r(require('pages/basic/hr/accountChangeList.vue')));
const accountChangeForm  = r => require.ensure([], () => r(require('pages/basic/hr/accountChangeForm.vue')));
const positionList = r => require.ensure([],() => r(require('pages/basic/hr/positionList.vue')));
const positionForm = r => require.ensure([],() => r(require('pages/basic/hr/positionForm.vue')));
const dutySignList = r => require.ensure([],() => r(require('pages/basic/hr/dutySignList.vue')));
const dutyAnnualList = r => require.ensure([],() => r(require('pages/basic/hr/dutyAnnualList.vue')));
const dutyAnnualForm = r => require.ensure([],() => r(require('pages/basic/hr/dutyAnnualForm.vue')));
const dutyWorktimeList = r => require.ensure([],() => r(require('pages/basic/hr/dutyWorktimeList.vue')));
const dutyWorktimeForm = r => require.ensure([],() => r(require('pages/basic/hr/dutyWorktimeForm.vue')));
const dutyTaskList = r => require.ensure([],() => r(require('pages/basic/hr/dutyTaskList.vue')));
const dutyTaskForm = r => require.ensure([],() => r(require('pages/basic/hr/dutyTaskForm.vue')));
const employeePhoneList = r => require.ensure([],() => r(require('pages/basic/hr/employeePhoneList.vue')));
const employeePhoneForm = r => require.ensure([],() => r(require('pages/basic/hr/employeePhoneForm.vue')));
const employeePhoneDepositList = r => require.ensure([],() => r(require('pages/basic/hr/employeePhoneDepositList.vue')));
const employeePhoneDepositForm = r => require.ensure([],() => r(require('pages/basic/hr/employeePhoneDepositForm.vue')));

const recruitList = r => require.ensure([],() => r(require('pages/basic/hr/recruitList.vue')));
const recruitForm = r => require.ensure([],() => r(require('pages/basic/hr/recruitForm.vue')));
const recruitBatchForm = r => require.ensure([],() => r(require('pages/basic/hr/recruitBatchForm.vue')));
const dutyLeaveList= r => require.ensure([], () => r(require('pages/basic/hr/dutyLeaveList.vue')));
const dutyFreeList= r => require.ensure([], () => r(require('pages/basic/hr/dutyFreeList.vue')));
const dutyTripList= r => require.ensure([], () => r(require('pages/basic/hr/dutyTripList.vue')));
const dutyRestList= r => require.ensure([], () => r(require('pages/basic/hr/dutyRestList.vue')));
const dutyPublicFreeList= r => require.ensure([], () => r(require('pages/basic/hr/dutyPublicFreeList.vue')));
const dutyOvertimeList= r => require.ensure([], () => r(require('pages/basic/hr/dutyOvertimeList.vue')));
const auditFileList= r => require.ensure([], () => r(require('pages/basic/hr/auditFileList.vue')));
const auditFileForm= r => require.ensure([], () => r(require('pages/basic/hr/auditFileForm.vue')));
const auditFileDetail= r => require.ensure([], () => r(require('pages/basic/hr/auditFileDetail.vue')));
const accountTaskList= r => require.ensure([], () => r(require('pages/basic/hr/accountTaskList.vue')));
const accountAuthorityForm= r => require.ensure([], () => r(require('pages/basic/hr/accountAuthorityForm.vue')));


let routes = [
  {path:'/basic/hr/dutyLeaveList',component:dutyLeaveList,name:'dutyLeaveList'},
  {path:'/basic/hr/dutyTripList',component:dutyTripList,name:'dutyTripList'},
  {path:'/basic/hr/dutyOvertimeList',component:dutyOvertimeList,name:'dutyOvertimeList'},
  {path:'/basic/hr/dutyRestList',component:dutyRestList,name:'dutyRestList'},
  {path:'/basic/hr/dutyFreeList',component:dutyFreeList,name:'dutyFreeList'},
  {path:'/basic/hr/dutyPublicFreeList',component:dutyPublicFreeList,name:'dutyPublicFreeList'},
  {path:'/basic/hr/accountList',component:accountList,name:'accountList'},
  {path:'/basic/hr/accountForm',component:accountForm,name:'accountForm',meta:{menu:"accountList",keepAlive:true}},
  {path:'/basic/hr/accountChangeList',component:accountChangeList,name:'accountChangeList'},
  {path:'/basic/hr/accountChangeForm',component:accountChangeForm,name:'accountChangeForm',meta:{menu:"accountChangeList",keepAlive:false}},
  {path:'/basic/hr/employeeList',component:employeeList,name:'employeeList'},
  {path:'/basic/hr/employeeForm',component:employeeForm,name:'employeeForm',meta: {menu:"employeeList",keepAlive:true}},
  {path:'/basic/hr/employeeEditForm',component:employeeEditForm,name:'employeeEditForm',meta: {menu:"employeeList",keepAlive:true}},
  {path:'/basic/hr/employeePhoneList',component:employeePhoneList,name:'employeePhoneList'},
  {path:'/basic/hr/employeePhoneForm',component:employeePhoneForm,name:'employeePhoneForm',meta: {menu:"employeePhoneList",keepAlive:true}},
  {path:'/basic/hr/employeePhoneDepositList',component:employeePhoneDepositList,name:'employeePhoneDepositList'},
  {path:'/basic/hr/employeePhoneDepositForm',component:employeePhoneDepositForm,name:'employeePhoneDepositForm',meta: {menu:"employeePhoneDepositList",keepAlive:true}},
  {path:'/basic/hr/positionList',component:positionList,name:'positionList'},
  {path:'/basic/hr/positionForm',component:positionForm,name:'positionForm',meta: {menu:"positionList",keepAlive:true}},
  {path:'/basic/hr/dutySignList',component:dutySignList,name:'dutySignList'},
  {path:'/basic/hr/dutyAnnualList',component:dutyAnnualList,name:'dutyAnnualList'},
  {path:'/basic/hr/dutyAnnualForm',component:dutyAnnualForm,name:'dutyAnnualForm',meta:{menu:"dutyAnnualList",keepAlive:true}},
  {path:'/basic/hr/dutyWorktimeList',component:dutyWorktimeList,name:'dutyWorktimeList'},
  {path:'/basic/hr/dutyWorktimeForm',component:dutyWorktimeForm,name:'dutyWorktimeForm',meta: {menu:"dutyWorktimeList",keepAlive:true}},
  {path:'/basic/hr/dutyTaskList',component:dutyTaskList,name:'dutyTaskList'},
  {path:'/basic/hr/dutyTaskForm',component:dutyTaskForm,name:'dutyTaskForm',meta: {menu:"dutyTaskList",keepAlive:true}},
  {path:'/basic/hr/auditFileList',component:auditFileList,name:'auditFileList'},
  {path:'/basic/hr/auditFileForm',component:auditFileForm,name:'auditFileForm',meta: {menu:"auditFileList",keepAlive:true}},
  {path:'/basic/hr/auditFileDetail',component:auditFileDetail,name:'auditFileDetail',meta: {menu:"auditFileList"}},

  {path:'/basic/hr/accountTaskList',component:accountTaskList,name:'accountTaskList'},
  {path:'/basic/hr/recruitList',component:recruitList,name:'recruitList'},
  {path:'/basic/hr/recruitForm',component:recruitForm,name:'recruitForm',meta: {menu:"recruitList",keepAlive:true}},
  {path:'/basic/hr/recruitBatchForm',component:recruitBatchForm,name:'recruitBatchForm',meta: {menu:"recruitList",keepAlive:true}},
  {path:'/basic/hr/accountAuthorityForm',component:accountAuthorityForm,name:'accountAuthorityForm',meta: {menu:"accountList",keepAlive:true}},
];

export default routes;
