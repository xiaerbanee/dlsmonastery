const adPricesystemList= r => require.ensure([], () => r(require('pages/future/basic/adPricesystemList.vue')));
const adPricesystemForm= r => require.ensure([], () => r(require('pages/future/basic/adPricesystemForm.vue')));
const bankList= r  => require.ensure([], () => r(require('pages/future/basic/bankList.vue')));
const bankForm  = r => require.ensure([], () => r(require('pages/future/basic/bankForm.vue')));
const chainList  = r => require.ensure([], () => r(require('pages/future/basic/chainList.vue')));
const chainForm  = r => require.ensure([], () => r(require('pages/future/basic/chainForm.vue')));
const clientList = r  => require.ensure([], () => r(require('pages/future/basic/clientList.vue')));
const clientForm= r  => require.ensure([], () => r(require('pages/future/basic/clientForm.vue')));
const depotList = r  => require.ensure([], () => r(require('pages/future/basic/depotList.vue')));
const depotForm= r  => require.ensure([], () => r(require('pages/future/basic/depotForm.vue')));
const expressCompanyList= r  => require.ensure([], () => r(require('pages/future/basic/expressCompanyList.vue')));
const expressCompanyForm  = r => require.ensure([], () => r(require('pages/future/basic/expressCompanyForm.vue')));
const pricesystemList  = r => require.ensure([], () => r(require('pages/future/basic/pricesystemList.vue')));
const pricesystemForm = r => require.ensure([], () => r(require('pages/future/basic/pricesystemForm.vue')));
const pricesystemView = r => require.ensure([], () => r(require('pages/future/basic/pricesystemView.vue')));
const productAdEdit = r  => require.ensure([], () => r(require('pages/future/basic/productAdEdit.vue')));
const productList = r  => require.ensure([], () => r(require('pages/future/basic/productList.vue')));
const productForm = r  => require.ensure([], () => r(require('pages/future/basic/productForm.vue')));
const productTypeList  = r => require.ensure([], () => r(require('pages/future/basic/productTypeList.vue')));
const productTypeForm = r => require.ensure([], () => r(require('pages/future/basic/productTypeForm.vue')));
const shopAdTypeList  = r => require.ensure([], () => r(require('pages/future/basic/shopAdTypeList.vue')));
const shopAdTypeForm  = r => require.ensure([], () => r(require('pages/future/basic/shopAdTypeForm.vue')));
const shopAttributeList  = r => require.ensure([], () => r(require('pages/future/basic/shopAttributeList.vue')));
const shopAttributeForm  = r => require.ensure([], () => r(require('pages/future/basic/shopAttributeForm.vue')));

const depotShopForm  = r => require.ensure([], () => r(require('pages/future/basic/depotShopForm.vue')));
const depotShopList  = r => require.ensure([], () => r(require('pages/future/basic/depotShopList.vue')));
const shopForm  = r => require.ensure([], () => r(require('pages/future/basic/shopForm.vue')));
const depotStoreForm  = r => require.ensure([], () => r(require('pages/future/basic/depotStoreForm.vue')));
const depotStoreList  = r => require.ensure([], () => r(require('pages/future/basic/depotStoreList.vue')));

const depotChangeForm  = r => require.ensure([], () => r(require('pages/future/basic/depotChangeForm.vue')));
const depotChangeList  = r => require.ensure([], () => r(require('pages/future/basic/depotChangeList.vue')));
const depotChangeDetail  = r => require.ensure([], () => r(require('pages/future/basic/depotChangeDetail.vue')));

const employeePhoneList = r => require.ensure([],() => r(require('pages/future/basic/employeePhoneList.vue')));
const employeePhoneForm = r => require.ensure([],() => r(require('pages/future/basic/employeePhoneForm.vue')));
const employeePhoneDepositList = r => require.ensure([],() => r(require('pages/future/basic/employeePhoneDepositList.vue')));
const employeePhoneDepositForm = r => require.ensure([],() => r(require('pages/future/basic/employeePhoneDepositForm.vue')));

let routes = [
  {path:'/future/basic/adPricesystemList',component:adPricesystemList,name:'adPricesystemList'},
  {path:'/future/basic/adPricesystemForm',component:adPricesystemForm,name:'adPricesystemForm',meta: {menu:"adPricesystemList",keepAlive:true}},
  {path:'/future/basic/bankList',component:bankList,name:'bankList'},
  {path:'/future/basic/bankForm',component:bankForm,name:'bankForm',meta: {menu:"bankList",keepAlive:true}},
  {path:'/future/basic/chainList',component:chainList,name:'chainList'},
  {path:'/future/basic/chainForm',component:chainForm,name:'chainForm',meta: {menu:"chainList",keepAlive:true}},
  {path:'/future/basic/clientList',component:clientList,name:'clientList'},
  {path:'/future/basic/clientForm',component:clientForm,name:'clientForm',meta: {menu:"clientList",keepAlive:true}},
  {path:'/future/basic/depotList',component:depotList,name:'depotList'},
  {path:'/future/basic/depotForm',component:depotForm,name:'depotForm',meta: {menu:"depotList",keepAlive:true}},
  {path:'/future/basic/expressCompanyList',component:expressCompanyList,name:'expressCompanyList'},
  {path:'/future/basic/expressCompanyForm',component:expressCompanyForm,name:'expressCompanyForm',meta: {menu:"expressCompanyList",keepAlive:true}},
  {path:'/future/basic/pricesystemList',component:pricesystemList,name:'pricesystemList'},
  {path:'/future/basic/pricesystemForm',component:pricesystemForm,name:'pricesystemForm',meta: {menu:"pricesystemList",keepAlive:true}},
  {path:'/future/basic/pricesystemView',component:pricesystemView,name:'pricesystemView',meta: {menu:"pricesystemList"}},
  {path:'/future/basic/productAdEdit',component:productAdEdit,name:'productAdEdit',meta: {menu:"productList",keepAlive:true}},
  {path:'/future/basic/productList',component:productList,name:'productList'},
  {path:'/future/basic/productForm',component:productForm,name:'productForm',meta: {menu:"productList",keepAlive:true}},
  {path:'/future/basic/productTypeList',component:productTypeList,name:'productTypeList'},
  {path:'/future/basic/productTypeForm',component:productTypeForm,name:'productTypeForm',meta: {menu:"productTypeList",keepAlive:true}},
  {path:'/future/basic/shopAdTypeList',component:shopAdTypeList,name:'shopAdTypeList'},
  {path:'/future/basic/shopAdTypeForm',component:shopAdTypeForm,name:'shopAdTypeForm',meta: {menu:"shopAdTypeList",keepAlive:true}},
  {path:'/future/basic/shopAttributeList',component:shopAttributeList,name:'shopAttributeList'},
  {path:'/future/basic/shopAttributeForm',component:shopAttributeForm,name:'shopAttributeForm',meta: {menu:"shopAttributeList",keepAlive:true}},
  {path:'/future/basic/employeePhoneList',component:employeePhoneList,name:'employeePhoneList'},
  {path:'/future/basic/employeePhoneForm',component:employeePhoneForm,name:'employeePhoneForm',meta: {menu:"employeePhoneList",keepAlive:true}},
  {path:'/future/basic/employeePhoneDepositList',component:employeePhoneDepositList,name:'employeePhoneDepositList'},
  {path:'/future/basic/employeePhoneDepositForm',component:employeePhoneDepositForm,name:'employeePhoneDepositForm',meta: {menu:"employeePhoneDepositList",keepAlive:true}},
  {path:'/future/basic/depotShopList',component:depotShopList,name:'depotShopList'},
  {path:'/future/basic/depotShopForm',component:depotShopForm,name:'depotShopForm',meta: {menu:"depotShopList",keepAlive:true}},
  {path:'/future/basic/shopForm',component:shopForm,name:'shopForm',meta: {menu:"depotShopList",keepAlive:true}},
  {path:'/future/basic/depotStoreList',component:depotStoreList,name:'depotStoreList'},
  {path:'/future/basic/depotStoreForm',component:depotStoreForm,name:'depotStoreForm',meta: {menu:"depotStoreList",keepAlive:true}},

  {path:'/future/basic/depotChangeList',component:depotChangeList,name:'depotChangeList'},
  {path:'/future/basic/depotChangeForm',component:depotChangeForm,name:'depotChangeForm',meta: {menu:"depotChangeList",keepAlive:true}},
  {path:'/future/basic/depotChangeDetail',component:depotChangeDetail,name:'depotChangeDetail',meta: {menu:"depotChangeList",keepAlive:true}},
];

export default routes;
