import Vue from 'vue'
import Router from 'vue-router'
import commonRoutes from './common'
import basicRoutes from './basic'
import businessRoutes from './business'

Vue.use(Router)

const routes = [
  ...commonRoutes,
  ...basicRoutes,
  ...businessRoutes
]

export default new Router({
  routes,
  mode: 'hash',
  linkActiveClass: 'active'
})
