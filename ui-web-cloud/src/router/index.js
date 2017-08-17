import Vue from 'vue'
import Router from 'vue-router'
import cloudSys from './cloud_sys'
import cloudReport from './cloud_report'
import cloudInput from './cloud_input'

Vue.use(Router);


const routes = [
    ...cloudSys,
    ...cloudReport,
    ...cloudInput
];

export default new Router({
    routes,
    base: "/cloud/",
    mode: 'hash',
    linkActiveClass: 'active'
})
