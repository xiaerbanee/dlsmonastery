const notFound  = r => require.ensure([], () => r(require('pages/common/404.vue')));
// list with filters page
const login  = r => require.ensure([], () => r(require('pages/sys/login.vue')));
const home  = r => require.ensure([], () => r(require('pages/sys/home.vue')));
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


const chainList  = r => require.ensure([], () => r(require('pages/crm/chainList.vue')));
const chainForm  = r => require.ensure([], () => r(require('pages/crm/chainForm.vue')));
const expressCompanyList= r  => require.ensure([], () => r(require('pages/crm/expressCompanyList.vue')));
const expressCompanyForm  = r => require.ensure([], () => r(require('pages/crm/expressCompanyForm.vue')));
const bankList= r  => require.ensure([], () => r(require('pages/crm/bankList.vue')));
const bankForm  = r => require.ensure([], () => r(require('pages/crm/bankForm.vue')));
const goodsOrderList = r => require.ensure([], () => r(require('pages/crm/goodsOrderList.vue')));
const goodsOrderForm = r => require.ensure([], () => r(require('pages/crm/goodsOrderForm.vue')));
const goodsOrderDetail = r => require.ensure([], () => r(require('pages/crm/goodsOrderDetail.vue')));
const goodsOrderBill= r => require.ensure([], () => r(require('pages/crm/goodsOrderBill.vue')));
const goodsOrderShip= r => require.ensure([], () => r(require('pages/crm/goodsOrderShip.vue')));
const goodsOrderPrint= r => require.ensure([], () => r(require('pages/crm/goodsOrderPrint.vue')));
const goodsOrderShipPrint= r => require.ensure([], () => r(require('pages/crm/goodsOrderShipPrint.vue')));
const shopAdTypeList  = r => require.ensure([], () => r(require('pages/crm/shopAdTypeList.vue')));
const shopAdTypeForm  = r => require.ensure([], () => r(require('pages/crm/shopAdTypeForm.vue')));
const depotList = r  => require.ensure([], () => r(require('pages/crm/depotList.vue')));
const depotForm= r  => require.ensure([], () => r(require('pages/crm/depotForm.vue')));
const depotAccountList = r  => require.ensure([], () => r(require('pages/crm/depotAccountList.vue')));
const depotAccountDetail = r  => require.ensure([], () => r(require('pages/crm/depotAccountDetail.vue')));
const productList = r  => require.ensure([], () => r(require('pages/crm/productList.vue')));
const productForm = r  => require.ensure([], () => r(require('pages/crm/productForm.vue')));
const productAdEdit = r  => require.ensure([], () => r(require('pages/crm/productAdEdit.vue')));
const dealerList = r  => require.ensure([], () => r(require('pages/crm/dealerList.vue')));
const dealerForm= r  => require.ensure([], () => r(require('pages/crm/dealerForm.vue')));
const depotDetailList  = r => require.ensure([], () => r(require('pages/crm/depotDetailList.vue')));
const pricesystemList  = r => require.ensure([], () => r(require('pages/crm/pricesystemList.vue')));
const pricesystemForm = r => require.ensure([], () => r(require('pages/crm/pricesystemForm.vue')));
const productTypeList  = r => require.ensure([], () => r(require('pages/crm/productTypeList.vue')));
const productTypeForm = r => require.ensure([], () => r(require('pages/crm/productTypeForm.vue')));
const depotChangeList  = r => require.ensure([], () => r(require('pages/crm/depotChangeList.vue')));
const depotChangeForm= r => require.ensure([], () => r(require('pages/crm/depotChangeForm.vue')));
const depotChangeDetail= r => require.ensure([], () => r(require('pages/crm/depotChangeDetail.vue')));
const demoPhoneList= r => require.ensure([], () => r(require('pages/crm/demoPhoneList.vue')));
const demoPhoneForm= r => require.ensure([], () => r(require('pages/crm/demoPhoneForm.vue')));
const demoPhoneTypeList= r => require.ensure([], () => r(require('pages/crm/demoPhoneTypeList.vue')));
const demoPhoneTypeForm= r => require.ensure([], () => r(require('pages/crm/demoPhoneTypeForm.vue')));
const shopPrintList= r => require.ensure([], () => r(require('pages/crm/shopPrintList.vue')));
const shopPrintForm= r => require.ensure([], () => r(require('pages/crm/shopPrintForm.vue')));
const shopPrintDetail = r => require.ensure([], () => r(require('pages/crm/shopPrintDetail.vue')));
const shopImageList= r => require.ensure([], () => r(require('pages/crm/shopImageList.vue')));
const shopImageForm= r => require.ensure([], () => r(require('pages/crm/shopImageForm.vue')));
const adPricesystemList= r => require.ensure([], () => r(require('pages/crm/adPricesystemList.vue')));
const adPricesystemForm= r => require.ensure([], () => r(require('pages/crm/adPricesystemForm.vue')));
const imeAllotList= r => require.ensure([], () => r(require('pages/crm/imeAllotList.vue')));
const imeAllotForm= r => require.ensure([], () => r(require('pages/crm/imeAllotForm.vue')));
const pricesystemChangeList= r => require.ensure([], () => r(require('pages/crm/pricesystemChangeList.vue')));
const pricesystemChangeForm= r => require.ensure([], () => r(require('pages/crm/pricesystemChangeForm.vue')));
const productImeSaleList= r => require.ensure([], () => r(require('pages/crm/productImeSaleList.vue')));
const productImeSaleForm= r => require.ensure([], () => r(require('pages/crm/productImeSaleForm.vue')));
const shopPromotionList= r => require.ensure([], () => r(require('pages/crm/shopPromotionList.vue')));
const shopPromotionForm= r => require.ensure([], () => r(require('pages/crm/shopPromotionForm.vue')));
const shopDepositList= r => require.ensure([], () => r(require('pages/crm/shopDepositList.vue')));
const shopDepositForm= r => require.ensure([], () => r(require('pages/crm/shopDepositForm.vue')));
const shopAdList= r => require.ensure([], () => r(require('pages/crm/shopAdList.vue')));
const shopAdForm= r => require.ensure([], () => r(require('pages/crm/shopAdForm.vue')));
const shopAdDetail= r => require.ensure([], () => r(require('pages/crm/shopAdDetail.vue')));
const adApplyList = r => require.ensure([], () => r(require('pages/crm/adApplyList.vue')));
const adApplyForm = r => require.ensure([], () => r(require('pages/crm/adApplyForm.vue')));
const adApplyGoods = r => require.ensure([], () => r(require('pages/crm/adApplyGoods.vue')));
const adApplyBillForm = r => require.ensure([], () => r(require('pages/crm/adApplyBillForm.vue')));
const shopBuildList  = r => require.ensure([], () => r(require('pages/crm/shopBuildList.vue')));
const shopBuildForm  = r => require.ensure([], () => r(require('pages/crm/shopBuildForm.vue')));
const shopBuildDetail  = r => require.ensure([], () => r(require('pages/crm/shopBuildDetail.vue')));
const priceChangeList  = r => require.ensure([], () => r(require('pages/crm/priceChangeList.vue')));
const priceChangeForm  = r => require.ensure([], () => r(require('pages/crm/priceChangeForm.vue')));
const priceChangeDetail  = r => require.ensure([], () => r(require('pages/crm/priceChangeDetail.vue')));
const priceChangeImeList  = r => require.ensure([], () => r(require('pages/crm/priceChangeImeList.vue')));
const priceChangeImeForm  = r => require.ensure([], () => r(require('pages/crm/priceChangeImeForm.vue')));
const priceChangeImeDetail  = r => require.ensure([], () => r(require('pages/crm/priceChangeImeDetail.vue')));
const adPricesystemChangeList  = r => require.ensure([], () => r(require('pages/crm/adPricesystemChangeList.vue')));
const adPricesystemChangeForm = r => require.ensure([], () => r(require('pages/crm/adPricesystemChangeForm.vue')));
const productImeUploadList  = r => require.ensure([], () => r(require('pages/crm/productImeUploadList.vue')));
const productImeUploadForm = r => require.ensure([], () => r(require('pages/crm/productImeUploadForm.vue')));
const expressList  = r => require.ensure([], () => r(require('pages/crm/expressList.vue')));
const expressForm = r => require.ensure([], () => r(require('pages/crm/expressForm.vue')));
const expressOrderList  = r => require.ensure([], () => r(require('pages/crm/expressOrderList.vue')));
const expressOrderForm = r => require.ensure([], () => r(require('pages/crm/expressOrderForm.vue')));
const afterSaleList = r => require.ensure([], () => r(require('pages/crm/afterSaleList.vue')));
const afterSaleForm= r => require.ensure([], () => r(require('pages/crm/afterSaleForm.vue')));
const afterSaleEditForm= r => require.ensure([], () => r(require('pages/crm/afterSaleEditForm.vue')));

const productImeList = r => require.ensure([], () => r(require('pages/crm/productImeList.vue')));
const afterSaleImeAllotList = r => require.ensure([], () => r(require('pages/crm/afterSaleImeAllotList.vue')));
const afterSaleProductAllotList = r => require.ensure([], () => r(require('pages/crm/afterSaleProductAllotList.vue')));
const afterSaleStoreAllotList = r => require.ensure([], () => r(require('pages/crm/afterSaleStoreAllotList.vue')));
const afterSaleFromCompany = r => require.ensure([], () => r(require('pages/crm/afterSaleFromCompany.vue')));
const afterSaleAreaEdit = r => require.ensure([], () => r(require('pages/crm/afterSaleAreaEdit.vue')));
const afterSaleToCompany = r => require.ensure([], () => r(require('pages/crm/afterSaleToCompany.vue')));

const adGoodsOrderList = r => require.ensure([], () => r(require('pages/crm/adGoodsOrderList.vue')));
const adGoodsOrderForm= r => require.ensure([], () => r(require('pages/crm/adGoodsOrderForm.vue')));
const adGoodsOrderDetail= r => require.ensure([], () => r(require('pages/crm/adGoodsOrderDetail.vue')));
const goodsOrderSign = r => require.ensure([], () => r(require('pages/crm/goodsOrderSign.vue')));
const goodsOrderSreturn = r => require.ensure([], () => r(require('pages/crm/goodsOrderSreturn.vue')));
const adGoodsOrderBill= r => require.ensure([], () => r(require('pages/crm/adGoodsOrderBill.vue')));
const adGoodsOrderShip= r => require.ensure([], () => r(require('pages/crm/adGoodsOrderShip.vue')));
const adGoodsOrderSign= r => require.ensure([], () => r(require('pages/crm/adGoodsOrderSign.vue')));

const storeAllotList = r => require.ensure([], () => r(require('pages/crm/storeAllotList.vue')));
const storeAllotForm = r => require.ensure([], () => r(require('pages/crm/storeAllotForm.vue')));
const storeAllotShip = r => require.ensure([], () => r(require('pages/crm/storeAllotShip.vue')));
const storeAllotDetail = r => require.ensure([], () => r(require('pages/crm/storeAllotDetail.vue')));

const shopGoodsDepositList = r => require.ensure([], () => r(require('pages/crm/shopGoodsDepositList.vue')));
const shopGoodsDepositForm = r => require.ensure([], () => r(require('pages/crm/shopGoodsDepositForm.vue')));

const shopAllotList = r => require.ensure([], () => r(require('pages/crm/shopAllotList.vue')));
const shopAllotForm = r => require.ensure([], () => r(require('pages/crm/shopAllotForm.vue')));
const shopAllotDetail = r => require.ensure([], () => r(require('pages/crm/shopAllotDetail.vue')));

const bankInList = r => require.ensure([], () => r(require('pages/crm/bankInList.vue')));
const bankInForm = r => require.ensure([], () => r(require('pages/crm/bankInForm.vue')));
const bankInDetail = r => require.ensure([], () => r(require('pages/crm/bankInDetail.vue')));

const productMonthPriceList = r => require.ensure([], () => r(require('pages/crm/productMonthPriceList.vue')));
const productMonthPriceForm = r => require.ensure([], () => r(require('pages/crm/productMonthPriceForm.vue')));
const reportScoreList = r => require.ensure([], () => r(require('pages/crm/reportScoreList.vue')));
const reportScoreForm = r => require.ensure([], () => r(require('pages/crm/reportScoreForm.vue')));
const reportScoreOfficeList = r => require.ensure([], () => r(require('pages/crm/reportScoreOfficeList.vue')));
const reportScoreAreaList = r => require.ensure([], () => r(require('pages/crm/reportScoreAreaList.vue')));

const dataReportList = r => require.ensure([], () => r(require('pages/crm/dataReportList.vue')));

const k3cloudSynList = r => require.ensure([], () => r(require('pages/api/k3cloudSynList.vue')));
const k3cloudSynForm = r => require.ensure([], () => r(require('pages/api/k3cloudSynForm.vue')));


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
  {path: '/login',component: login,name: 'login',meta: {hidden: true,requiresAuth: false}},
  {path: '/',component: home,name: 'home'},
  {path: '/404',component: notFound,name: '404'},
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

  {path: '/crm/goodsOrderList',component: goodsOrderList,name:"goodsOrderList"},
  {path:'/crm/goodsOrderForm',component:goodsOrderForm,name:'goodsOrderForm',meta: {menu:"goodsOrderList"}},
  {path:'/crm/goodsOrderDetail',component:goodsOrderDetail,name:'goodsOrderDetail',meta: {menu:"goodsOrderList"}},
  {path:'/crm/goodsOrderBill',component:goodsOrderBill,name:'goodsOrderBill',meta: {menu:"goodsOrderList"}},
  {path:'/crm/goodsOrderShip',component:goodsOrderShip,name:'goodsOrderShip',meta: {menu:"goodsOrderList"}},
  {path:'/crm/goodsOrderPrint',component:goodsOrderPrint,name:'goodsOrderPrint',meta: {hidden: true}},
  {path:'/crm/goodsOrderShipPrint',component:goodsOrderShipPrint,name:'goodsOrderShipPrint',meta: {hidden: true}},
  {path:'/crm/goodsOrderSign',component:goodsOrderSign,name:'goodsOrderSign',meta: {menu:"goodsOrderList"}},
  {path:'/crm/goodsOrderSreturn',component:goodsOrderSreturn,name:'goodsOrderSreturn',meta: {menu:"goodsOrderList"}},

  {path:'/crm/shopAdTypeList',component:shopAdTypeList,name:'shopAdTypeList'},
  {path:'/crm/shopAdTypeForm',component:shopAdTypeForm,name:'shopAdTypeForm',meta: {menu:"shopAdTypeList"}},
  {path:'/crm/depotDetailList',component:depotDetailList,name:'depotDetailList'},
  {path:'/crm/depotList',component:depotList,name:'depotList'},
  {path:'/crm/depotForm',component:depotForm,name:'depotForm',meta: {menu:"depotList"}},
  {path:'/crm/depotAccountList',component:depotAccountList,name:'depotAccountList'},
  {path:'/crm/depotAccountDetail',component:depotAccountDetail,name:'depotAccountDetail',meta: {menu:"depotAccountList"}},
  {path:'/crm/productList',component:productList,name:'productList'},
  {path:'/crm/productForm',component:productForm,name:'productForm',meta: {menu:"productList"}},
  {path:'/crm/productAdEdit',component:productAdEdit,name:'productAdEdit',meta: {menu:"productList"}},
  {path:'/crm/dealerList',component:dealerList,name:'dealerList'},

  {path:'/crm/dealerForm',component:dealerForm,name:'dealerForm',meta: {menu:"dealerList"}},
  {path:'/crm/chainList',component:chainList,name:'chainList'},
  {path:'/crm/chainForm',component:chainForm,name:'chainForm',meta: {menu:"chainList"}},
  {path:'/crm/expressCompanyList',component:expressCompanyList,name:'expressCompanyList'},
  {path:'/crm/expressCompanyForm',component:expressCompanyForm,name:'expressCompanyForm',meta: {menu:"expressCompanyList"}},
  {path:'/crm/bankList',component:bankList,name:'bankList'},
  {path:'/crm/bankForm',component:bankForm,name:'bankForm',meta: {menu:"bankList"}},
  {path:'/crm/pricesystemList',component:pricesystemList,name:'pricesystemList'},
  {path:'/crm/pricesystemForm',component:pricesystemForm,name:'pricesystemForm',meta: {menu:"pricesystemList"}},
  {path:'/crm/productTypeList',component:productTypeList,name:'productTypeList'},
  {path:'/crm/productTypeForm',component:productTypeForm,name:'productTypeForm',meta: {menu:"productTypeList"}},
  {path:'/crm/depotChangeList',component:depotChangeList,name:'depotChangeList'},
  {path:'/crm/depotChangeForm',component:depotChangeForm,name:'depotChangeForm',meta: {menu:"depotChangeList"}},
  {path:'/crm/depotChangeDetail',component:depotChangeDetail,name:'depotChangeDetail',meta: {menu:"depotChangeList"}},
  {path:'/crm/demoPhoneList',component:demoPhoneList,name:'demoPhoneList'},
  {path:'/crm/demoPhoneForm',component:demoPhoneForm,name:'demoPhoneForm',meta: {menu:"demoPhoneList"}},
  {path:'/crm/demoPhoneTypeList',component:demoPhoneTypeList,name:'demoPhoneTypeList'},
  {path:'/crm/demoPhoneTypeForm',component:demoPhoneTypeForm,name:'demoPhoneTypeForm',meta: {menu:"demoPhoneTypeList"}},
  {path:'/crm/shopPrintList',component:shopPrintList,name:'shopPrintList'},
  {path:'/crm/shopPrintForm',component:shopPrintForm,name:'shopPrintForm',meta: {menu:"shopPrintList"}},
  {path:'/crm/shopPrintDetail',component:shopPrintDetail,name:'shopPrintDetail',meta: {menu:"shopPrintList"}},
  {path:'/crm/shopImageList',component:shopImageList,name:'shopImageList'},
  {path:'/crm/shopImageForm',component:shopImageForm,name:'shopImageForm',meta: {menu:"shopImageList"}},
  {path:'/crm/adPricesystemList',component:adPricesystemList,name:'adPricesystemList'},
  {path:'/crm/adPricesystemForm',component:adPricesystemForm,name:'adPricesystemForm',meta: {menu:"adPricesystemList"}},
  {path:'/crm/imeAllotList',component:imeAllotList,name:'imeAllotList'},
  {path:'/crm/imeAllotForm',component:imeAllotForm,name:'imeAllotForm',meta: {menu:"imeAllotList"}},
  {path:'/crm/pricesystemChangeList',component:pricesystemChangeList,name:'pricesystemChangeList'},
  {path:'/crm/pricesystemChangeForm',component:pricesystemChangeForm,name:'pricesystemChangeForm',meta: {menu:"pricesystemChangeList"}},
  {path:'/crm/productImeSaleList',component:productImeSaleList,name:'productImeSaleList'},
  {path:'/crm/productImeSaleForm',component:productImeSaleForm,name:'productImeSaleForm',meta: {menu:"productImeSaleList"}},
  {path:'/crm/shopPromotionList',component:shopPromotionList,name:'shopPromotionList'},
  {path:'/crm/shopPromotionForm',component:shopPromotionForm,name:'shopPromotionForm',meta: {menu:"shopPromotionList"}},
  {path:'/crm/shopBuildList',component:shopBuildList,name:'shopBuildList'},
  {path:'/crm/shopBuildForm',component:shopBuildForm,name:'shopBuildForm',meta: {menu:"shopBuildList"}},
  {path:'/crm/shopBuildDetail',component:shopBuildDetail,name:'shopBuildDetail',meta: {menu:"shopBuildList"}},
  {path:'/crm/shopDepositList',component:shopDepositList,name:'shopDepositList'},
  {path:'/crm/shopDepositForm',component:shopDepositForm,name:'shopDepositForm',meta: {menu:"shopDepositList"}},
  {path:'/crm/priceChangeList',component:priceChangeList,name:'priceChangeList'},
  {path:'/crm/priceChangeForm',component:priceChangeForm,name:'priceChangeForm',meta: {menu:"priceChangeList"}},
  {path:'/crm/priceChangeDetail',component:priceChangeDetail,name:'priceChangeDetail',meta: {menu:"priceChangeList"}},
  {path:'/crm/shopAdList',component:shopAdList,name:'shopAdList'},
  {path:'/crm/shopAdForm',component:shopAdForm,name:'shopAdForm',meta: {menu:"shopAdList"}},
  {path:'/crm/shopAdDetail',component:shopAdDetail,name:'shopAdDetail',meta: {menu:"shopAdList"}},
  {path:'/crm/adApplyList',component:adApplyList,name:'adApplyList'},
  {path:'/crm/adApplyForm',component:adApplyForm,name:'adApplyForm',meta: {menu:"adApplyList"}},
  {path:'/crm/adApplyGoods',component:adApplyGoods,name:'adApplyGoods',meta: {menu:"adApplyList"}},
  {path:'/crm/adApplyBillForm',component:adApplyBillForm,name:'adApplyBillForm',meta: {menu:"adApplyList"}},
  {path:'/crm/adPricesystemChangeList',component:adPricesystemChangeList,name:'adPricesystemChangeList'},
  {path:'/crm/adPricesystemChangeForm',component:adPricesystemChangeForm,name:'v',meta: {menu:"adPricesystemChangeList"}},
  {path:'/crm/priceChangeImeList',component:priceChangeImeList,name:'priceChangeImeList'},
  {path:'/crm/priceChangeImeForm',component:priceChangeImeForm,name:'priceChangeImeForm',meta: {menu:"priceChangeImeList"}},
  {path:'/crm/priceChangeImeDetail',component:priceChangeImeDetail,name:'priceChangeImeDetail',meta: {menu:"priceChangeImeList"}},
  {path:'/crm/productImeUploadList',component:productImeUploadList,name:'productImeUploadList'},
  {path:'/crm/productImeUploadForm',component:productImeUploadForm,name:'productImeUploadForm',meta: {menu:"productImeUploadList"}},
  {path:'/crm/expressList',component:expressList,name:'expressList'},
  {path:'/crm/expressForm',component:expressForm,name:'expressForm',meta: {menu:"expressList"}},
  {path:'/crm/expressOrderList',component:expressOrderList,name:'expressOrderList'},
  {path:'/crm/expressOrderForm',component:expressOrderForm,name:'expressOrderForm',meta: {menu:"expressOrderList"}},
  {path:'/crm/afterSaleList',component:afterSaleList,name:'afterSaleList'},
  {path:'/crm/afterSaleForm',component:afterSaleForm,name:'afterSaleForm',meta: {menu:"afterSaleList"}},
  {path:'/crm/afterSaleEditForm',component:afterSaleEditForm,name:'afterSaleEditForm',meta: {menu:"afterSaleList"}},

  {path:'/crm/afterSaleImeAllotList',component:afterSaleImeAllotList,name:'afterSaleImeAllotList'},
  {path:'/crm/afterSaleProductAllotList',component:afterSaleProductAllotList,name:'afterSaleProductAllotList'},
  {path:'/crm/afterSaleStoreAllotList',component:afterSaleStoreAllotList,name:'afterSaleStoreAllotList'},
  {path:'/crm/afterSaleFromCompany',component:afterSaleFromCompany,name:'afterSaleFromCompany'},
  {path:'/crm/afterSaleAreaEdit',component:afterSaleAreaEdit,name:'afterSaleAreaEdit'},
  {path:'/crm/afterSaleToCompany',component:afterSaleToCompany,name:'afterSaleToCompany'},

  {path:'/crm/adGoodsOrderList',component:adGoodsOrderList,name:'adGoodsOrderList'},
  {path:'/crm/adGoodsOrderForm',component:adGoodsOrderForm,name:'adGoodsOrderForm',meta: {menu:"adGoodsOrderList"}},
  {path:'/crm/adGoodsOrderDetail',component:adGoodsOrderDetail,name:'adGoodsOrderDetail',meta: {menu:"adGoodsOrderList"}},
  {path:'/crm/adGoodsOrderBill',component:adGoodsOrderBill,name:'adGoodsOrderBill',meta: {menu:"adGoodsOrderList"}},
  {path:'/crm/adGoodsOrderShip',component:adGoodsOrderShip,name:'adGoodsOrderShip',meta: {menu:"adGoodsOrderList"}},
  {path:'/crm/adGoodsOrderSign',component:adGoodsOrderSign,name:'adGoodsOrderSign',meta: {menu:"adGoodsOrderList"}},

  {path:'/crm/productImeList',component:productImeList,name:'productImeList'},
  {path:'/crm/storeAllotList',component:storeAllotList,name:'storeAllotList'},
  {path:'/crm/storeAllotForm',component:storeAllotForm,name:'storeAllotForm',meta: {menu:"storeAllotList"}},
  {path:'/crm/storeAllotShip',component:storeAllotShip,name:'storeAllotShip',meta: {menu:"storeAllotList"}},
  {path:'/crm/storeAllotDetail',component:storeAllotDetail,name:'storeAllotDetail',meta: {menu:"storeAllotList"}},

  {path:'/crm/shopAllotList',component:shopAllotList,name:'shopAllotList'},
  {path:'/crm/shopAllotForm',component:shopAllotForm,name:'shopAllotForm',meta: {menu:"shopAllotList"}},
  {path:'/crm/shopAllotDetail',component:shopAllotDetail,name:'shopAllotDetail',meta: {menu:"shopAllotList"}},

  {path:'/crm/bankInList',component:bankInList,name:'bankInList'},
  {path:'/crm/bankInForm',component:bankInForm,name:'bankInForm',meta: {menu:"bankInList"}},
  {path:'/crm/bankInDetail',component:bankInDetail,name:'bankInDetail',meta: {menu:"bankInList"}},
  {path:'/crm/shopGoodsDepositList',component:shopGoodsDepositList,name:'shopGoodsDepositList'},
  {path:'/crm/shopGoodsDepositForm',component:shopGoodsDepositForm,name:'shopGoodsDepositForm',meta: {menu:"shopGoodsDepositList"}},

  {path:'/crm/productMonthPriceList',component:productMonthPriceList,name:'productMonthPriceList' },
  {path:'/crm/productMonthPriceForm',component:productMonthPriceForm,name:'productMonthPriceForm',meta: {menu:"productMonthPriceList"}},
  {path:'/crm/reportScoreList',component:reportScoreList,name:'reportScoreList' },
  {path:'/crm/reportScoreForm',component:reportScoreForm,name:'reportScoreForm',meta: {menu:"reportScoreList"}},
  {path:'/crm/reportScoreOfficeList',component:reportScoreOfficeList,name:'reportScoreOfficeList' },
  {path:'/crm/reportScoreAreaList',component:reportScoreAreaList,name:'reportScoreAreaList',meta: {menu:"reportScoreOfficeList"}},

  {path:'/crm/dataReportList',component:dataReportList,name:'dataReportList'},

  {path:'/api/k3cloudSynList',component:k3cloudSynList,name:'k3cloudSynList' },
  {path:'/api/k3cloudSynForm',component:k3cloudSynForm,name:'k3cloudSynForm',meta: {menu:"k3cloudSynList"}},

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
  {path: '*',redirect: {path: '/404'}}
];

export default routes;
