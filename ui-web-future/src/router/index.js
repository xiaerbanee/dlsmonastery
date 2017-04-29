import Vue from 'vue'
import Router from 'vue-router'
import common from './common'
import basicHr from './basic_hr'
import basicSys from './basic_sys'
import businessBasic from './future_basic'
import businessCrm from './future_crm'
import businessLayout from './future_layout'
import cloudSys from './cloud_sys'
import cloudReport from './cloud_report'
import cloudInput from './cloud_input'

Vue.use(Router)

const redirect = {
    path: '*',
    redirect: {path: '/common/404'}
}

const routes = [
  ...common,
  ...basicHr,
  ...basicSys,
  ...businessBasic,
  ...businessCrm,
  ...businessLayout,
  ...cloudSys,
  ...cloudReport,
  ...cloudInput,
  redirect
]

export default new Router({
  routes,
  mode: 'hash',
  linkActiveClass: 'active'
})
