import Vue from 'vue'
import Router from 'vue-router'
import futureBasic from './future_basic'
import futureCrm from './future_crm'
import futureLayout from './future_layout'
import futureApi from './future_api'

Vue.use(Router)

const routes = [
    ...futureBasic,
    ...futureCrm,
    ...futureLayout,
    ...futureApi
]

export default new Router({
    routes,
    base: "/future/",
    mode: 'hash',
    linkActiveClass: 'active'
})
