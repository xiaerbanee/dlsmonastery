import Cookie from 'js-cookie'

const getTabs = function () {
  var tabs = new Map();
  let local = window.localStorage.getItem("tabs");
  if(local != null) {
    tabs = new Map(JSON.parse(local));
  }
  return tabs;
}

const getLang = function () {
  var local = window.localStorage.getItem("lang");
  if(!local) {
    local = "zh-cn";
  }
  return local;
}

const getToken = function () {
  var token = window.localStorage.getItem("token");
  if(token) {
    token = JSON.parse(token);
  } else {
    token = {};
  }
  return token;
}

const getMenus = function () {
  var local = window.localStorage.getItem("menus");
  if(local) {
    local = JSON.parse(local);
  } else {
    local = [];
  }
  return local;
}

const getAccount = function () {
  var local = window.localStorage.getItem("account");
  if(local) {
    local = JSON.parse(local);
  } else {
    local = {};
  }
  return local;
}

const getAuthorityList = function () {
  var local = window.localStorage.getItem("authorityList");
  if(local) {
    local = JSON.parse(local);
  } else {
    local = {};
  }
  return local;
}

export default {
  state: {
    tabs: getTabs(),
    lang: getLang(),
    menus:getMenus(),
    account:getAccount(),
    authorityList:getAuthorityList(),
    token: getToken(),
    isRefreshToken:false
  },getters: {
    getQuery: (state, routerName) => {
      var query = {};
      if (routerName != "home") {
        var tabs = state.tabs;
        query = tabs.get(routerName);
      }
      return query;
    }
  },
  mutations: {
    setTabs(state, tabs) {
      var tabArray = [...tabs];
      if(tabArray.length>=8) {
        tabArray = tabArray.splice(tabArray.length-8);
      }
      state.tabs = new Map(tabArray);
      localStorage.setItem('tabs', JSON.stringify(tabArray))
    },
    setLang(state,lang){
      state.lang = lang;
      localStorage.setItem('lang', lang)
    },
    setMenus(state,menus) {
      state.menus = menus;
      localStorage.setItem('menus', JSON.stringify(menus))
    },
    setAuthorityList(state,authorityList) {
      state.authorityList = authorityList;
      localStorage.setItem('authorityList', JSON.stringify(authorityList))
    },
    setAccount(state,account) {
      state.account = account;
      localStorage.setItem('account', JSON.stringify(account))
    },
    setToken(state,token) {
      token.exp = new Date().getTime();
      state.token = token;
      localStorage.setItem('token', JSON.stringify(token));
      if(util.isNotBlank(token.access_token)) {
        var expires = new Date(token.exp +  token.expires_in);
        Cookie.set('Authorization', token.access_token, { expires: expires,path:"" });
      } else {
        Cookie.remove('Authorization');
      }
    },
    setIsRefreshToken(state,isRefreshToken) {
      state.isRefreshToken = isRefreshToken;
    }
  },
  actions: {
    setAuthorityList({ commit, state }, authorityList) {
      commit('setAuthorityList', authorityList);
    },
    setTabs({ commit, state }, tabs) {
      commit('setTabs', tabs);
    },
    setQuery({ commit, state }, params) {
      if (params.routerName != "home") {
        var tabs = state.tabs;
        tabs.set(params.routerName, params.query);
        commit('setTabs', tabs);
      }
    },
    setLang({ commit, state }, lang) {
      commit('setLang', lang);
    },
    setMenus({ commit, state }, menus) {
      commit('setMenus', menus);
    },
    setAccount({ commit, state }, account) {
      commit('setAccount', account);
    },
    setToken({ commit, state }, token) {
      commit('setToken', token);
    },
    setIsRefreshToken({ commit, state }, isRefreshToken) {
      commit('setIsRefreshToken', isRefreshToken);
    },clearGlobal({ commit, state }) {
      commit('setTabs', new Map());
      commit('setMenus', []);
      commit('setAccount', {});
      commit('setAuthorityList', []);
      commit('setToken', {});
      commit('setIsRefreshToken', false);
    }
  }
}
