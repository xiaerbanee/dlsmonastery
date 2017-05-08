const adPricesystemList= r => require.ensure([], () => r(require('pages/future/basic/adPricesystemList.vue')));
const adPricesystemForm= r => require.ensure([], () => r(require('pages/future/basic/adPricesystemForm.vue')));
const bankList= r  => require.ensure([], () => r(require('pages/future/basic/bankList.vue')));
const bankForm  = r => require.ensure([], () => r(require('pages/future/basic/bankForm.vue')));
const chainList  = r => require.ensure([], () => r(require('pages/future/basic/chainList.vue')));
const chainForm  = r => require.ensure([], () => r(require('pages/future/basic/chainForm.vue')));
const dealerList = r  => require.ensure([], () => r(require('pages/future/basic/dealerList.vue')));
const dealerForm= r  => require.ensure([], () => r(require('pages/future/basic/dealerForm.vue')));
const depotList = r  => require.ensure([], () => r(require('pages/future/basic/depotList.vue')));
const depotForm= r  => require.ensure([], () => r(require('pages/future/basic/depotForm.vue')));
const expressCompanyList= r  => require.ensure([], () => r(require('pages/future/basic/expressCompanyList.vue')));
const expressCompanyForm  = r => require.ensure([], () => r(require('pages/future/basic/expressCompanyForm.vue')));
const pricesystemList  = r => require.ensure([], () => r(require('pages/future/basic/pricesystemList.vue')));
const pricesystemForm = r => require.ensure([], () => r(require('pages/future/basic/pricesystemForm.vue')));
const pricesystemView = r => require.ensure([], () => r(require('pages/future/basic/pricesystemView.vue')));
const productList = r  => require.ensure([], () => r(require('pages/future/basic/productList.vue')));
const productForm = r  => require.ensure([], () => r(require('pages/future/basic/productForm.vue')));
const productTypeList  = r => require.ensure([], () => r(require('pages/future/basic/productTypeList.vue')));
const productTypeForm = r => require.ensure([], () => r(require('pages/future/basic/productTypeForm.vue')));
const shopAdTypeList  = r => require.ensure([], () => r(require('pages/future/basic/shopAdTypeList.vue')));
const shopAdTypeForm  = r => require.ensure([], () => r(require('pages/future/basic/shopAdTypeForm.vue')));

let routes = [
  {path:'/future/basic/adPricesystemList',component:adPricesystemList,name:'adPricesystemList'},
  {path:'/future/basic/adPricesystemForm',component:adPricesystemForm,name:'adPricesystemForm',meta: {menu:"adPricesystemList"}},
  {path:'/future/basic/bankList',component:bankList,name:'bankList'},
  {path:'/future/basic/bankForm',component:bankForm,name:'bankForm',meta: {menu:"bankList"}},
  {path:'/future/basic/chainList',component:chainList,name:'chainList'},
  {path:'/future/basic/chainForm',component:chainForm,name:'chainForm',meta: {menu:"chainList"}},
  {path:'/future/basic/dealerList',component:dealerList,name:'dealerList'},
  {path:'/future/basic/dealerForm',component:dealerForm,name:'dealerForm',meta: {menu:"dealerList"}},
  {path:'/future/basic/depotList',component:depotList,name:'depotList'},
  {path:'/future/basic/depotForm',component:depotForm,name:'depotForm',meta: {menu:"depotList"}},
  {path:'/future/basic/expressCompanyList',component:expressCompanyList,name:'expressCompanyList'},
  {path:'/future/basic/expressCompanyForm',component:expressCompanyForm,name:'expressCompanyForm',meta: {menu:"expressCompanyList"}},
  {path:'/future/basic/pricesystemList',component:pricesystemList,name:'pricesystemList'},
  {path:'/future/basic/pricesystemForm',component:pricesystemForm,name:'pricesystemForm',meta: {menu:"pricesystemList"}},
  {path:'/future/basic/pricesystemView',component:pricesystemView,name:'pricesystemView',meta: {menu:"pricesystemList"}},
  {path:'/future/basic/productList',component:productList,name:'productList'},
  {path:'/future/basic/productForm',component:productForm,name:'productForm',meta: {menu:"productList"}},
  {path:'/future/basic/productTypeList',component:productTypeList,name:'productTypeList'},
  {path:'/future/basic/productTypeForm',component:productTypeForm,name:'productTypeForm',meta: {menu:"productTypeList"}},
  {path:'/future/basic/shopAdTypeList',component:shopAdTypeList,name:'shopAdTypeList'},
  {path:'/future/basic/shopAdTypeForm',component:shopAdTypeForm,name:'shopAdTypeForm',meta: {menu:"shopAdTypeList"}},
];

export default routes;
