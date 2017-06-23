import Vue from 'vue';
import VueI18n from 'vue-i18n';
import axios from 'axios'
import qs from 'qs'
import _ from 'lodash'
import store from './store'
import locale from './locate'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-default/index.css';
import VueQuillEditor from 'vue-quill-editor'
import VueProgressBar from 'vue-progressbar'

import pageable from './components/common/pageable';
import headTab from './components/common/head-tab';
import searchTag from './components/common/search-tag';
import datePicker from './components/common/date-picker.vue'
import dateRangePicker from './components/common/date-range-picker.vue';
import searchDialog from './components/common/search-dialog.vue';
import suAlert from './components/common/su-alert.vue';

import App from './app.vue';
import fullCalendar from 'vue-fullcalendar'
import router from './router'
import './style.scss';
import './filters'
import util from "./utils/util"
window.qs = qs;
window._=_;
window.util=util;
window.enumMap = null;

Vue.use(VueI18n);
Vue.use(ElementUI);
Vue.use(VueQuillEditor);

// register dashboard components
Vue.component('pageable', pageable);
Vue.component('full-calendar', fullCalendar);
Vue.component('head-tab', headTab);
Vue.component('search-tag', searchTag);
Vue.component('date-picker',datePicker);
Vue.component('date-range-picker', dateRangePicker);
Vue.component('search-dialog', searchDialog);
Vue.component('su-alert', suAlert);

// progressBar
const options = {
  color: 'rgb(143, 255, 199)',
  failedColor: 'red',
  thickness: '3px'
}

Vue.use(VueProgressBar, options)

// set locales
Vue.locale('zh-cn',locale.zhCn);
Vue.locale("id",locale.id);
Vue.config.lang = store.state.global.lang;

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

router.beforeEach((to, from, next) => {
  router.app.$Progress.start()

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
});

router.afterEach(route => {
  window.scrollTo(0, 0)
  router.app.$Progress.finish()
})


axios.interceptors.response.use((resp) => {
  return resp;
}, (error) => {
  if (error.response) {
    switch (error.response.status) {
      case 401:
        store.dispatch('clearGlobal');
        window.location.assign('/');
        break;
      case 500:
        ElementUI.Message.error({
          title: 'System Error',
          message:error.response.data
        });
    }
  }
  return Promise.reject(error)
})

window.axios = axios;

window.checkLogin = function () {
  var account = store.state.global.account;
  if(account==null || util.isBlank(account.id)) {
    return false;
  } else {
    return true;
  }
}


new Vue({
  render: h => h(App),
  store,
  router
}).$mount('#app');

