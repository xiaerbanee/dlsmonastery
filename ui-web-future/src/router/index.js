import Vue from 'vue'
import Router from 'vue-router'
import commonRoutes from './common'
import basicHr from './basic_hr'
import basicSys from './basic_sys'
import businessBasic from './business_basic'
import businessCrm from './business_crm'
import businessLayout from './business_layout'

Vue.use(Router)

const routes = [
  ...commonRoutes,
  ...basicHr,
  ...basicSys,
  ...businessBasic,
  ...businessCrm,
  ...businessLayout
]

export default new Router({
  routes,
  mode: 'hash',
  linkActiveClass: 'active'
})
