const carrierOrderList= r => require.ensure([], () => r(require('pages/future/api/carrierOrderList.vue')));
const carrierOrderForm= r => require.ensure([], () => r(require('pages/future/api/carrierOrderForm.vue')));
const carrierOrderShip= r => require.ensure([], () => r(require('pages/future/api/carrierOrderShip.vue')));
const carrierShopList= r => require.ensure([], () => r(require('pages/future/api/carrierShopList.vue')));
const carrierShopForm= r => require.ensure([], () => r(require('pages/future/api/carrierShopForm.vue')));
const carrierProductList= r => require.ensure([], () => r(require('pages/future/api/carrierProductList.vue')));
const carrierProductForm= r => require.ensure([], () => r(require('pages/future/api/carrierProductForm.vue')));

let routes = [
  {path:'/future/api/carrierOrderList',component:carrierOrderList,name:'carrierOrderList'},
  {path:'/future/api/carrierOrderForm',component:carrierOrderForm,name:'carrierOrderForm',meta: {menu:"carrierOrderList",keepAlive:true}},
  {path:'/future/api/carrierOrderShip',component:carrierOrderShip,name:'carrierOrderShip',meta: {menu:"carrierOrderList",keepAlive:true}},
  {path:'/future/api/carrierShopList',component:carrierShopList,name:'carrierShopList'},
  {path:'/future/api/carrierShopForm',component:carrierShopForm,name:'carrierShopForm',meta: {menu:"carrierShopList",keepAlive:true}},
  {path:'/future/api/carrierProductList',component:carrierProductList,name:'carrierProductList'},
  {path:'/future/api/carrierProductForm',component:carrierProductForm,name:'carrierProductForm',meta: {menu:"carrierProductList",keepAlive:true}},
]

export default routes;
