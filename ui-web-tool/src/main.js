import babelpolyfill from 'babel-polyfill'
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
//import './assets/theme/theme-green/index.css'
import VueRouter from 'vue-router'
import store from './store'
import locale from './locate'
import Vuex from 'vuex'
//import NProgress from 'nprogress'
//import 'nprogress/nprogress.css'
import router from './router'
import 'font-awesome/css/font-awesome.min.css'

import VueI18n from 'vue-i18n';

import pageable from './components/common/pageable';
import headTab from './components/common/head-tab';
import searchTag from './components/common/search-tag';
import datePicker from './components/common/date-picker.vue'
import dateRangePicker from './components/common/date-range-picker.vue';
import searchDialog from './components/common/search-dialog.vue';
import imgPreviewer from './components/common/img-previewer.vue'
import suAlert from './components/common/su-alert.vue';
import suSelect from './components/common/su-select.vue';

import './filters'
import './styles/style.scss'
import axios from 'axios'
import qs from 'qs'
import util from "./utils/util"
import _ from 'lodash'



Vue.use(VueI18n);
Vue.use(ElementUI)
Vue.use(VueRouter)
Vue.use(Vuex)


Vue.component('pageable', pageable);
Vue.component('head-tab', headTab);
Vue.component('search-tag', searchTag);
Vue.component('date-picker',datePicker);
Vue.component('date-range-picker', dateRangePicker);
Vue.component('search-dialog', searchDialog);
Vue.component('su-alert', suAlert);
Vue.component('su-select', suSelect);
Vue.component('img-previewer',imgPreviewer);

// set locales
Vue.locale('zh-cn',locale.zhCn);
Vue.locale("id",locale.id);
Vue.config.lang = store.state.global.lang;

//NProgress.configure({ showSpinner: false });

router.beforeEach((to, from, next) => {
    if(to.params._closeFrom){
        store.dispatch('closeTab', from.name);
    }

    //检查是否已经登陆
    if (to.matched.some(record => record.meta.requiresAuth==false)) {
        next();
    } else {
        if(checkLogin()) {
            //增加tab
            util.setQuery(to.name, to.query);
            next();
        } else {
            store.dispatch('clearGlobal');
            router.push({ name: 'login' });
        }
    }
})


window.qs = qs;
window.axios = axios;
window.util=util;
window._=_;

window.checkLogin = function () {
    var account = store.state.global.account;
    if(account==null || account.id== null) {
        return false;
    } else {
        return true;
    }
}
Vue.directive('permit', function (el, binding) {
    var  hasPermit=false;
    if(binding.value){
        hasPermit= util.isPermit(binding.value);
    }
    if(!hasPermit){
        el.style.display="none"
    }else {
        el.style.display=""
    }
})
Vue.directive('title', {
    inserted: function (el, binding) {
        document.title = binding.value
    }
})
//router.afterEach(transition => {
//NProgress.done();
//});

new Vue({
  //el: '#app',
  //template: '<App/>',
  router,
  store,
  //components: { App }
  render: h => h(App)
}).$mount('#app')

