const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const employeeList  = r => require.ensure([], () => r(require('../pages/hr/employeeList.vue')));
const employeeForm  = r => require.ensure([], () => r(require('../pages/hr/employeeForm.vue')));
const employeeEditForm  = r => require.ensure([], () => r(require('../pages/hr/employeeEditForm.vue')));
const accountList  = r => require.ensure([], () => r(require('../pages/hr/accountList.vue')));
const accountForm  = r => require.ensure([], () => r(require('../pages/hr/accountForm.vue')))
const accountPositionForm  = r => require.ensure([], () => r(require('../pages/hr/accountPositionForm.vue')));

const accountChangeList  = r => require.ensure([], () => r(require('../pages/hr/accountChangeList.vue')));
const accountChangeForm  = r => require.ensure([], () => r(require('../pages/hr/accountChangeForm.vue')));
const accountChangeBatchForm  = r => require.ensure([], () => r(require('../pages/hr/accountChangeBatchForm.vue')));
const positionList = r => require.ensure([],() => r(require('../pages/hr/positionList.vue')));
const positionForm = r => require.ensure([],() => r(require('../pages/hr/positionForm.vue')));
const dutySignList = r => require.ensure([],() => r(require('../pages/hr/dutySignList.vue')));
const dutyAnnualList = r => require.ensure([],() => r(require('../pages/hr/dutyAnnualList.vue')));
const dutyAnnualForm = r => require.ensure([],() => r(require('../pages/hr/dutyAnnualForm.vue')));
const dutyWorktimeList = r => require.ensure([],() => r(require('../pages/hr/dutyWorktimeList.vue')));
const dutyWorktimeForm = r => require.ensure([],() => r(require('../pages/hr/dutyWorktimeForm.vue')));
const dutyTaskList = r => require.ensure([],() => r(require('../pages/hr/dutyTaskList.vue')));
const dutyTaskForm = r => require.ensure([],() => r(require('../pages/hr/dutyTaskForm.vue')));


const recruitList = r => require.ensure([],() => r(require('../pages/hr/recruitList.vue')));
const recruitForm = r => require.ensure([],() => r(require('../pages/hr/recruitForm.vue')));
const recruitEnumList = r => require.ensure([],() => r(require('../pages/hr/recruitEnumList.vue')));
const recruitEnumForm = r => require.ensure([],() => r(require('../pages/hr/recruitEnumForm.vue')));
const recruitBatchForm = r => require.ensure([],() => r(require('../pages/hr/recruitBatchForm.vue')));
const dutyLeaveList= r => require.ensure([], () => r(require('../pages/hr/dutyLeaveList.vue')));
const dutyFreeList= r => require.ensure([], () => r(require('../pages/hr/dutyFreeList.vue')));
const dutyTripList= r => require.ensure([], () => r(require('../pages/hr/dutyTripList.vue')));
const dutyRestList= r => require.ensure([], () => r(require('../pages/hr/dutyRestList.vue')));
const dutyPublicFreeList= r => require.ensure([], () => r(require('../pages/hr/dutyPublicFreeList.vue')));
const dutyOvertimeList= r => require.ensure([], () => r(require('../pages/hr/dutyOvertimeList.vue')));
const auditFileList= r => require.ensure([], () => r(require('../pages/hr/auditFileList.vue')));
const auditFileForm= r => require.ensure([], () => r(require('../pages/hr/auditFileForm.vue')));
const auditFileDetail= r => require.ensure([], () => r(require('../pages/hr/auditFileDetail.vue')));
const auditFilePrint= r => require.ensure([], () => r(require('../pages/hr/auditFilePrint.vue')));
const accountFavoriteList = r => require.ensure([],() => r(require('../pages/hr/accountFavoriteList.vue')));
const accountFavoriteForm = r => require.ensure([],() => r(require('../pages/hr/accountFavoriteForm.vue')));
const accountTaskList= r => require.ensure([], () => r(require('../pages/hr/accountTaskList.vue')));
const accountAuthorityForm= r => require.ensure([], () => r(require('../pages/hr/accountAuthorityForm.vue')));

const unitsOfficeForm= r => require.ensure([], () => r(require('../pages/hr/unitsOfficeForm.vue')));
const batchUnitsForm= r => require.ensure([], () => r(require('../pages/hr/batchUnitsForm.vue')));
const accountWeixinList= r => require.ensure([], () => r(require('../pages/hr/accountWeixinList.vue')));


let routes = [
    {
        path: '/',
        component: home,
        children: [
            {path:'/hr/dutyLeaveList',component:dutyLeaveList,name:'dutyLeaveList'},
            {path:'/hr/dutyTripList',component:dutyTripList,name:'dutyTripList'},
            {path:'/hr/dutyOvertimeList',component:dutyOvertimeList,name:'dutyOvertimeList'},
            {path:'/hr/dutyRestList',component:dutyRestList,name:'dutyRestList'},
            {path:'/hr/dutyFreeList',component:dutyFreeList,name:'dutyFreeList'},
            {path:'/hr/dutyPublicFreeList',component:dutyPublicFreeList,name:'dutyPublicFreeList'},
            {path:'/hr/accountList',component:accountList,name:'accountList'},

            {path:'/hr/accountPositionForm',component:accountPositionForm,name:'accountPositionForm',meta:{menu:"accountList",keepAlive:true}},
            {path:'/hr/accountForm',component:accountForm,name:'accountForm',meta:{menu:"accountList",keepAlive:true}},
            {path:'/hr/accountChangeList',component:accountChangeList,name:'accountChangeList'},
            {path:'/hr/accountChangeForm',component:accountChangeForm,name:'accountChangeForm',meta:{menu:"accountChangeList",keepAlive:false}},
            {path:'/hr/accountChangeBatchForm',component:accountChangeBatchForm,name:'accountChangeBatchForm',meta:{menu:"accountChangeList",keepAlive:false}},
            {path:'/hr/employeeList',component:employeeList,name:'employeeList'},
            {path:'/hr/employeeForm',component:employeeForm,name:'employeeForm',meta: {menu:"employeeList",keepAlive:true}},
            {path:'/hr/employeeEditForm',component:employeeEditForm,name:'employeeEditForm',meta: {menu:"employeeList",keepAlive:true}},
            {path:'/hr/positionList',component:positionList,name:'positionList'},
            {path:'/hr/positionForm',component:positionForm,name:'positionForm',meta: {menu:"positionList",keepAlive:true}},
            {path:'/hr/dutySignList',component:dutySignList,name:'dutySignList'},
            {path:'/hr/dutyAnnualList',component:dutyAnnualList,name:'dutyAnnualList'},
            {path:'/hr/dutyAnnualForm',component:dutyAnnualForm,name:'dutyAnnualForm',meta:{menu:"dutyAnnualList",keepAlive:true}},
            {path:'/hr/dutyWorktimeList',component:dutyWorktimeList,name:'dutyWorktimeList'},
            {path:'/hr/dutyWorktimeForm',component:dutyWorktimeForm,name:'dutyWorktimeForm',meta: {menu:"dutyWorktimeList",keepAlive:true}},
            {path:'/hr/dutyTaskList',component:dutyTaskList,name:'dutyTaskList'},
            {path:'/hr/dutyTaskForm',component:dutyTaskForm,name:'dutyTaskForm',meta: {menu:"dutyTaskList",keepAlive:true}},
            {path:'/hr/auditFileList',component:auditFileList,name:'auditFileList'},
            {path:'/hr/auditFileForm',component:auditFileForm,name:'auditFileForm',meta: {menu:"auditFileList",keepAlive:true}},
            {path:'/hr/auditFileDetail',component:auditFileDetail,name:'auditFileDetail',meta: {menu:"auditFileList"}},
            {path:'/hr/auditFilePrint',component:auditFilePrint,name:'auditFilePrint',meta:{hidden:true}},
            {path:'/hr/accountFavoriteList',component:accountFavoriteList,name:'accountFavoriteList'},
            {path:'/hr/accountFavoriteForm',component:accountFavoriteForm,name:'accountFavoriteForm',meta: {menu:"accountFavoriteList",keepAlive:true}},

            {path:'/hr/accountTaskList',component:accountTaskList,name:'accountTaskList'},
            {path:'/hr/recruitList',component:recruitList,name:'recruitList'},
            {path:'/hr/recruitForm',component:recruitForm,name:'recruitForm',meta: {menu:"recruitList",keepAlive:true}},
            {path:'/hr/recruitEnumList',component:recruitEnumList,name:'recruitEnumList'},
            {path:'/hr/recruitEnumForm',component:recruitEnumForm,name:'recruitEnumForm',meta: {menu:"recruitEnumList",keepAlive:true}},
            {path:'/hr/recruitBatchForm',component:recruitBatchForm,name:'recruitBatchForm',meta: {menu:"recruitList",keepAlive:true}},
            {path:'/hr/accountAuthorityForm',component:accountAuthorityForm,name:'accountAuthorityForm',meta: {menu:"accountList",keepAlive:true}},

            {path:'/hr/unitsOfficeForm',component:unitsOfficeForm,name:'unitsOfficeForm'},
            {path:'/hr/batchUnitsForm',component:batchUnitsForm,name:'batchUnitsForm'},
            {path:'/hr/accountWeixinList',component:accountWeixinList,name:'accountWeixinList'},
        ]
    }

];

export default routes;
