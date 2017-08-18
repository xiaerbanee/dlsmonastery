import Vue from 'vue'
import Router from 'vue-router'
import toolSys from './tool_sys'

Vue.use(Router)


const routes = [
    ...toolSys
]

export default new Router({
    routes,
    base: "/tool/",
    mode: 'hash',
    linkActiveClass: 'active'
})
