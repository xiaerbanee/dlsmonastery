const adPricesystemList= r => require.ensure([], () => r(require('pages/business/basic/adPricesystemList.vue')));
const adPricesystemForm= r => require.ensure([], () => r(require('pages/business/basic/adPricesystemForm.vue')));
const bankList= r  => require.ensure([], () => r(require('pages/business/basic/bankList.vue')));
const bankForm  = r => require.ensure([], () => r(require('pages/business/basic/bankForm.vue')));
const chainList  = r => require.ensure([], () => r(require('pages/business/basic/chainList.vue')));
const chainForm  = r => require.ensure([], () => r(require('pages/business/basic/chainForm.vue')));
const dealerList = r  => require.ensure([], () => r(require('pages/business/basic/dealerList.vue')));
const dealerForm= r  => require.ensure([], () => r(require('pages/business/basic/dealerForm.vue')));
const depotList = r  => require.ensure([], () => r(require('pages/business/basic/depotList.vue')));
const depotForm= r  => require.ensure([], () => r(require('pages/business/basic/depotForm.vue')));
const expressCompanyList= r  => require.ensure([], () => r(require('pages/business/basic/expressCompanyList.vue')));
const expressCompanyForm  = r => require.ensure([], () => r(require('pages/business/basic/expressCompanyForm.vue')));
const pricesystemList  = r => require.ensure([], () => r(require('pages/business/basic/pricesystemList.vue')));
const pricesystemForm = r => require.ensure([], () => r(require('pages/business/basic/pricesystemForm.vue')));
const productList = r  => require.ensure([], () => r(require('pages/business/basic/productList.vue')));
const productForm = r  => require.ensure([], () => r(require('pages/business/basic/productForm.vue')));
const productTypeList  = r => require.ensure([], () => r(require('pages/business/basic/productTypeList.vue')));
const productTypeForm = r => require.ensure([], () => r(require('pages/business/basic/productTypeForm.vue')));
const shopAdTypeList  = r => require.ensure([], () => r(require('pages/business/basic/shopAdTypeList.vue')));
const shopAdTypeForm  = r => require.ensure([], () => r(require('pages/business/basic/shopAdTypeForm.vue')));





let routes = [
  {path:'/business/basic/adPricesystemList',component:adPricesystemList,name:'adPricesystemList'},
  {path:'/business/basic/adPricesystemForm',component:adPricesystemForm,name:'adPricesystemForm',meta: {menu:"adPricesystemList"}},
  {path:'/business/basic/bankList',component:bankList,name:'bankList'},
  {path:'/business/basic/bankForm',component:bankForm,name:'bankForm',meta: {menu:"bankList"}},
  {path:'/business/basic/chainList',component:chainList,name:'chainList'},
  {path:'/business/basic/chainForm',component:chainForm,name:'chainForm',meta: {menu:"chainList"}},
  {path:'/business/basic/dealerList',component:dealerList,name:'dealerList'},
  {path:'/business/basic/dealerForm',component:dealerForm,name:'dealerForm',meta: {menu:"dealerList"}},
  {path:'/business/basic/depotList',component:depotList,name:'depotList'},
  {path:'/business/basic/depotForm',component:depotForm,name:'depotForm',meta: {menu:"depotList"}},
  {path:'/business/basic/expressCompanyList',component:expressCompanyList,name:'expressCompanyList'},
  {path:'/business/basic/expressCompanyForm',component:expressCompanyForm,name:'expressCompanyForm',meta: {menu:"expressCompanyList"}},
  {path:'/business/basic/pricesystemList',component:pricesystemList,name:'pricesystemList'},
  {path:'/business/basic/pricesystemForm',component:pricesystemForm,name:'pricesystemForm',meta: {menu:"pricesystemList"}},
  {path:'/business/basic/productList',component:productList,name:'productList'},
  {path:'/business/basic/productForm',component:productForm,name:'productForm',meta: {menu:"productList"}},
  {path:'/business/basic/productTypeList',component:productTypeList,name:'productTypeList'},
  {path:'/business/basic/productTypeForm',component:productTypeForm,name:'productTypeForm',meta: {menu:"productTypeList"}},
  {path:'/business/basic/shopAdTypeList',component:shopAdTypeList,name:'shopAdTypeList'},
  {path:'/business/basic/shopAdTypeForm',component:shopAdTypeForm,name:'shopAdTypeForm',meta: {menu:"shopAdTypeList"}},
];

export default routes;
