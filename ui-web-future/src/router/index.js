import Vue from 'vue'
import Router from 'vue-router'
import commonRoutes from './common'

Vue.use(Router)

const routes = [
  ...commonRoutes
]

export default new Router({
  routes,
  mode: 'hash',
  linkActiveClass: 'active'
})
