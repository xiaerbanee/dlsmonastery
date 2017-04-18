const notFound  = r => require.ensure([], () => r(require('pages/common/404.vue')));
const login  = r => require.ensure([], () => r(require('pages/basic/sys/login.vue')));
const home  = r => require.ensure([], () => r(require('pages/basic/sys/home.vue')));
const routes = [
  {path: '/basic/sys/login',component: login,name: 'login',meta: {hidden: true,requiresAuth: false}},
  {path: '/',component: home,name: 'home'},
  {path: '/common/404',component: notFound,name: '404'},
  {path: '*',redirect: {path: '/404'}}
]
export default routes
