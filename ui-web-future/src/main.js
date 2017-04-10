import Vue from 'vue';
import VueI18n from 'vue-i18n';
import VueRouter from 'vue-router';
import axios from 'axios'
import qs from 'qs'
import _ from 'lodash'
import store from './store/'
import jwtDecode from 'jwt-decode';

import ElementUI from 'element-ui';
import zhElement from 'element-ui/lib/locale/lang/zh-CN'
import idElement from 'element-ui/lib/locale/lang/id'
import 'element-ui/lib/theme-default/index.css';
import VueQuillEditor from 'vue-quill-editor'
import VueProgressBar from 'vue-progressbar'

import pageable from './components/pageable';
import headTab from './components/head-tab';
import searchTag from './components/search-tag';

import App from './app.vue';
import fullCalendar from 'vue-fullcalendar'
import routes from './routes';
import './style.scss';
import './filters'
import util from "./utils/util"
import zhLocale from "./utils/locales/zh-CN"
import idLocale from "./utils/locales/id"

axios.defaults.headers.common['Authorization'] = store.state.global.token;

window.axios = axios;
window.qs = qs;
window._=_;
window.util=util;
window.enumMap = null;

Vue.use(VueI18n);
Vue.use(VueRouter);
Vue.use(ElementUI);
Vue.use(VueQuillEditor);

// register dashboard components
Vue.component('pageable', pageable);
Vue.component('full-calendar', fullCalendar);
Vue.component('head-tab', headTab);
Vue.component('search-tag', searchTag);

// progressBar
const options = {
  color: 'rgb(143, 255, 199)',
  failedColor: 'red',
  thickness: '3px'
}

Vue.use(VueProgressBar, options)

// set locales
Vue.locale('zh-cn', util.mergeJsonObject(zhElement,zhLocale));
Vue.locale("id",util.mergeJsonObject(idElement,idLocale));

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
Vue.config.errorHandler = function (err, vm) {
  vm.$notify.error({
    title: '系统异常',
    message: err+""
  });
  console.error(err);
}

export const router = new VueRouter({
  routes,
  mode: 'hash',
  linkActiveClass: 'active'
});

router.beforeEach((to, from, next) => {
  router.app.$Progress.start()
  //检查是否已经登陆
  if (to.matched.some(record => record.meta.requiresAuth==false)) {
    next();
  } else {
    var token = store.state.global.token;
    //如果没有token，去登陆页面
    if(util.isBlank(token)) {
      store.dispatch('clearGlobal');
      next({path: '/login', query: {redirect: to.fullPath}});
    } else {
      //如果包含token，解析token
      var decoded = jwtDecode(token);
      var exp = decoded.exp;
      console.log(exp);
      util.setQuery(to.name, to.query);
      next();
    }
  }
});

router.afterEach(route => {
  window.scrollTo(0, 0)
  router.app.$Progress.finish()
})

new Vue({
  render: h => h(App),
  store,
  router
}).$mount('#app');

