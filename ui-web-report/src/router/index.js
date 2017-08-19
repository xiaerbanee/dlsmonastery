import Vue from 'vue'
import Router from 'vue-router'
import reportCrm from './report_crm'
import reportBasic from './report_basic'

Vue.use(Router)

const routes = [
    ...reportCrm,
    ...reportBasic
]

export default new Router({
    routes,
    base: "/future/",
    mode: 'hash',
    linkActiveClass: 'active'
})
