const notFound  = r => require.ensure([], () => r(require('pages/common/404.vue')));
const login  = r => require.ensure([], () => r(require('pages/sys/login.vue')));
const home  = r => require.ensure([], () => r(require('pages/sys/home.vue')));
const routes = [
  {path: '/login',component: login,name: 'login',meta: {hidden: true,requiresAuth: false}},
  {path: '/',component: home,name: 'home'},
  {path: '/404',component: notFound,name: '404'},
  {path: '*',redirect: {path: '/404'}}
]
export default routes
