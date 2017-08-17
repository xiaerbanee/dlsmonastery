const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const carrierOrderList= r => require.ensure([], () => r(require('../pages/api/carrierOrderList.vue')));
const carrierOrderForm= r => require.ensure([], () => r(require('../pages/api/carrierOrderForm.vue')));
const carrierOrderShip= r => require.ensure([], () => r(require('../pages/api/carrierOrderShip.vue')));
const carrierShopList= r => require.ensure([], () => r(require('../pages/api/carrierShopList.vue')));
const carrierShopForm= r => require.ensure([], () => r(require('../pages/api/carrierShopForm.vue')));
const carrierProductList= r => require.ensure([], () => r(require('../pages/api/carrierProductList.vue')));
const carrierProductForm= r => require.ensure([], () => r(require('../pages/api/carrierProductForm.vue')));

let routes = [
    {
        path: '/',
        component: home,
        children: [
            {path:'/api/carrierOrderList',component:carrierOrderList,name:'carrierOrderList'},
            {path:'/api/carrierOrderForm',component:carrierOrderForm,name:'carrierOrderForm',meta: {menu:"carrierOrderList",keepAlive:true}},
            {path:'/api/carrierOrderShip',component:carrierOrderShip,name:'carrierOrderShip',meta: {menu:"carrierOrderList",keepAlive:true}},
            {path:'/api/carrierShopList',component:carrierShopList,name:'carrierShopList'},
            {path:'/api/carrierShopForm',component:carrierShopForm,name:'carrierShopForm',meta: {menu:"carrierShopList",keepAlive:true}},
            {path:'/api/carrierProductList',component:carrierProductList,name:'carrierProductList'},
            {path:'/api/carrierProductForm',component:carrierProductForm,name:'carrierProductForm',meta: {menu:"carrierProductList",keepAlive:true}}
        ]
    }
];

export default routes;
