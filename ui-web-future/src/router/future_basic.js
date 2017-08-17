const login  = r => require.ensure([], () => r(require('../pages/login.vue')));
const notFound  = r => require.ensure([], () => r(require('../pages/404.vue')));
const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const index  = r => require.ensure([], () => r(require('../pages/index.vue')));


const adPricesystemList= r => require.ensure([], () => r(require('../pages/basic/adPricesystemList.vue')));
const adPricesystemForm= r => require.ensure([], () => r(require('../pages/basic/adPricesystemForm.vue')));
const bankList= r  => require.ensure([], () => r(require('../pages/basic/bankList.vue')));
const bankForm  = r => require.ensure([], () => r(require('../pages/basic/bankForm.vue')));
const chainList  = r => require.ensure([], () => r(require('../pages/basic/chainList.vue')));
const chainForm  = r => require.ensure([], () => r(require('../pages/basic/chainForm.vue')));
const clientList = r  => require.ensure([], () => r(require('../pages/basic/clientList.vue')));
const clientForm= r  => require.ensure([], () => r(require('../pages/basic/clientForm.vue')));
const expressCompanyList= r  => require.ensure([], () => r(require('../pages/basic/expressCompanyList.vue')));
const expressCompanyForm  = r => require.ensure([], () => r(require('../pages/basic/expressCompanyForm.vue')));
const pricesystemList  = r => require.ensure([], () => r(require('../pages/basic/pricesystemList.vue')));
const pricesystemForm = r => require.ensure([], () => r(require('../pages/basic/pricesystemForm.vue')));
const pricesystemView = r => require.ensure([], () => r(require('../pages/basic/pricesystemView.vue')));
const productAdEdit = r  => require.ensure([], () => r(require('../pages/basic/productAdEdit.vue')));
const productList = r  => require.ensure([], () => r(require('../pages/basic/productList.vue')));
const productForm = r  => require.ensure([], () => r(require('../pages/basic/productForm.vue')));
const productTypeList  = r => require.ensure([], () => r(require('../pages/basic/productTypeList.vue')));
const productTypeForm = r => require.ensure([], () => r(require('../pages/basic/productTypeForm.vue')));
const shopAdTypeList  = r => require.ensure([], () => r(require('../pages/basic/shopAdTypeList.vue')));
const shopAdTypeForm  = r => require.ensure([], () => r(require('../pages/basic/shopAdTypeForm.vue')));
const shopAttributeList  = r => require.ensure([], () => r(require('../pages/basic/shopAttributeList.vue')));
const shopAttributeForm  = r => require.ensure([], () => r(require('../pages/basic/shopAttributeForm.vue')));

const depotShopAccountForm  = r => require.ensure([], () => r(require('../pages/basic/depotShopAccountForm.vue')));
const depotShopMergeForm  = r => require.ensure([], () => r(require('../pages/basic/depotShopMergeForm.vue')));
const depotShopForm  = r => require.ensure([], () => r(require('../pages/basic/depotShopForm.vue')));
const depotShopList  = r => require.ensure([], () => r(require('../pages/basic/depotShopList.vue')));
const shopForm  = r => require.ensure([], () => r(require('../pages/basic/shopForm.vue')));
const depotStoreForm  = r => require.ensure([], () => r(require('../pages/basic/depotStoreForm.vue')));
const depotStoreAddForm  = r => require.ensure([], () => r(require('../pages/basic/depotStoreAddForm.vue')));
const depotStoreList  = r => require.ensure([], () => r(require('../pages/basic/depotStoreList.vue')));

const depotChangeForm  = r => require.ensure([], () => r(require('../pages/basic/depotChangeForm.vue')));
const depotChangeList  = r => require.ensure([], () => r(require('../pages/basic/depotChangeList.vue')));
const depotChangeDetail  = r => require.ensure([], () => r(require('../pages/basic/depotChangeDetail.vue')));

const employeePhoneList = r => require.ensure([],() => r(require('../pages/basic/employeePhoneList.vue')));
const employeePhoneForm = r => require.ensure([],() => r(require('../pages/basic/employeePhoneForm.vue')));
const employeePhoneDepositList = r => require.ensure([],() => r(require('../pages/basic/employeePhoneDepositList.vue')));
const employeePhoneDepositForm = r => require.ensure([],() => r(require('../pages/basic/employeePhoneDepositForm.vue')));
const employeePhoneDepositBatchForm  = r => require.ensure([], () => r(require('../pages/basic/employeePhoneDepositBatchForm.vue')));


let routes = [
    {path: '/login',component: login,name: 'login',meta: {requiresAuth: false}},
    {path: '/404',component: notFound,name: 'notFound'},
    {
        path: '/',
        component: home,
        children: [
            {path: '/index',component: index,name: 'index'},
            {path:'/basic/adPricesystemList',component:adPricesystemList,name:'adPricesystemList'},
            {path:'/basic/adPricesystemForm',component:adPricesystemForm,name:'adPricesystemForm',meta: {menu:"adPricesystemList",keepAlive:true}},
            {path:'/basic/bankList',component:bankList,name:'bankList'},
            {path:'/basic/bankForm',component:bankForm,name:'bankForm',meta: {menu:"bankList",keepAlive:true}},
            {path:'/basic/chainList',component:chainList,name:'chainList'},
            {path:'/basic/chainForm',component:chainForm,name:'chainForm',meta: {menu:"chainList",keepAlive:true}},
            {path:'/basic/clientList',component:clientList,name:'clientList'},
            {path:'/basic/clientForm',component:clientForm,name:'clientForm',meta: {menu:"clientList",keepAlive:true}},
            {path:'/basic/expressCompanyList',component:expressCompanyList,name:'expressCompanyList'},
            {path:'/basic/expressCompanyForm',component:expressCompanyForm,name:'expressCompanyForm',meta: {menu:"expressCompanyList",keepAlive:true}},
            {path:'/basic/pricesystemList',component:pricesystemList,name:'pricesystemList'},
            {path:'/basic/pricesystemForm',component:pricesystemForm,name:'pricesystemForm',meta: {menu:"pricesystemList",keepAlive:true}},
            {path:'/basic/pricesystemView',component:pricesystemView,name:'pricesystemView',meta: {menu:"pricesystemList"}},
            {path:'/basic/productAdEdit',component:productAdEdit,name:'productAdEdit',meta: {menu:"productList",keepAlive:true}},
            {path:'/basic/productList',component:productList,name:'productList'},
            {path:'/basic/productForm',component:productForm,name:'productForm',meta: {menu:"productList",keepAlive:true}},
            {path:'/basic/productTypeList',component:productTypeList,name:'productTypeList'},
            {path:'/basic/productTypeForm',component:productTypeForm,name:'productTypeForm',meta: {menu:"productTypeList",keepAlive:true}},
            {path:'/basic/shopAdTypeList',component:shopAdTypeList,name:'shopAdTypeList'},
            {path:'/basic/shopAdTypeForm',component:shopAdTypeForm,name:'shopAdTypeForm',meta: {menu:"shopAdTypeList",keepAlive:true}},
            {path:'/basic/shopAttributeList',component:shopAttributeList,name:'shopAttributeList'},
            {path:'/basic/shopAttributeForm',component:shopAttributeForm,name:'shopAttributeForm',meta: {menu:"shopAttributeList",keepAlive:true}},
            {path:'/basic/employeePhoneList',component:employeePhoneList,name:'employeePhoneList'},
            {path:'/basic/employeePhoneForm',component:employeePhoneForm,name:'employeePhoneForm',meta: {menu:"employeePhoneList",keepAlive:true}},
            {path:'/basic/employeePhoneDepositList',component:employeePhoneDepositList,name:'employeePhoneDepositList'},
            {path:'/basic/employeePhoneDepositForm',component:employeePhoneDepositForm,name:'employeePhoneDepositForm',meta: {menu:"employeePhoneDepositList",keepAlive:true}},
            {path:'/basic/employeePhoneDepositBatchForm',component:employeePhoneDepositBatchForm,name:'employeePhoneDepositBatchForm',meta: {menu:"employeePhoneList",keepAlive:true}},

            {path:'/basic/depotShopList',component:depotShopList,name:'depotShopList'},
            {path:'/basic/depotShopForm',component:depotShopForm,name:'depotShopForm',meta: {menu:"depotShopList",keepAlive:true}},
            {path:'/basic/depotShopAccountForm',component:depotShopAccountForm,name:'depotShopAccountForm',meta: {menu:"depotShopList",keepAlive:true}},
            {path:'/basic/depotShopMergeForm',component:depotShopMergeForm,name:'depotShopMergeForm',meta: {menu:"depotShopList",keepAlive:true}},
            {path:'/basic/shopForm',component:shopForm,name:'shopForm',meta: {menu:"depotShopList",keepAlive:true}},
            {path:'/basic/depotStoreList',component:depotStoreList,name:'depotStoreList'},
            {path:'/basic/depotStoreForm',component:depotStoreForm,name:'depotStoreForm',meta: {menu:"depotStoreList",keepAlive:true}},
            {path:'/basic/depotStoreAddForm',component:depotStoreAddForm,name:'depotStoreAddForm',meta: {menu:"depotStoreList",keepAlive:true}},

            {path:'/basic/depotChangeList',component:depotChangeList,name:'depotChangeList'},
            {path:'/basic/depotChangeForm',component:depotChangeForm,name:'depotChangeForm',meta: {menu:"depotChangeList",keepAlive:true}},
            {path:'/basic/depotChangeDetail',component:depotChangeDetail,name:'depotChangeDetail',meta: {menu:"depotChangeList",keepAlive:true}}
        ]
    }

];

export default routes;
