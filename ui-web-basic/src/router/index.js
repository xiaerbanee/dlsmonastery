import Vue from 'vue'
import Router from 'vue-router'
import basicHr from './basic_hr'
import basicSys from './basic_sys'
import basicSalary from './basic_salary'

Vue.use(Router)


const routes = [
    ...basicHr,
    ...basicSys,
    ...basicSalary
]

export default new Router({
    routes,
    base: "/basic/",
    mode: 'hash',
    linkActiveClass: 'active'
})
