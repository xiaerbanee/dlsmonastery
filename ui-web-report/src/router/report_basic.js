const login  = r => require.ensure([], () => r(require('../pages/login.vue')));
const notFound  = r => require.ensure([], () => r(require('../pages/404.vue')));
const home  = r => require.ensure([], () => r(require('../pages/home.vue')));
const index  = r => require.ensure([], () => r(require('../pages/index.vue')));

let routes = [
    {path: '/login',component: login,name: 'login',meta: {requiresAuth: false}},
    {path: '/404',component: notFound,name: 'notFound'},
    {
        path: '/',
        component: home,
        children: [
            {path: '/index',component: index,name: 'index'},
        ]
    }

];

export default routes;