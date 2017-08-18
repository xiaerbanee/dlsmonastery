const login  = r => require.ensure([], () => r(require('../pages/login.vue')));
const notFound  = r => require.ensure([], () => r(require('../pages/404.vue')));
const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const index  = r => require.ensure([], () => r(require('../pages/index.vue')));

const oppoPlantAgentProductSelList = r => require.ensure([], () => r(require('../pages/sys/oppoPlantAgentProductSelList.vue')));
const vivoFactoryOrderList = r => require.ensure([], () => r(require('../pages/sys/vivoFactoryOrderList.vue')));
const vivoPlantProductsList = r => require.ensure([], () => r(require('../pages/sys/vivoPlantProductsList.vue')));
const ImooPlantBasicProductList = r => require.ensure([], () => r(require('../pages/sys/ImooPlantBasicProductList.vue')));

let routes = [
    {path: '/login',component: login,name: 'login',meta: {requiresAuth: false}},
    {path: '/404',component: notFound,name: 'notFound'},
    {
        path: '/',
        component: home,
        children: [
            {path: '/index',component: index,name: 'index'},
            {path: '/sys/oppoPlantAgentProductSelList',component: oppoPlantAgentProductSelList,name: 'oppoPlantAgentProductSelList'},
            {path: '/sys/vivoFactoryOrderList',component: vivoFactoryOrderList,name: 'vivoFactoryOrderList'},
            {path: '/sys/vivoPlantProductsList',component: vivoPlantProductsList,name: 'vivoPlantProductsList'},
            {path: '/sys/ImooPlantBasicProductList',component: ImooPlantBasicProductList,name: 'ImooPlantBasicProductList'},
        ]
    }

];

export default routes;
