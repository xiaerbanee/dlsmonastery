const getTabs = function () {
  var tabs = new Map();
  let local = window.localStorage.getItem("businessManager_tabs");
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
    authorityList:getAuthorityList()
  },
  mutations: {
    setTabs(state, tabs) {
      var tabArray = [...tabs];
      if(tabArray.length>=8) {
        tabArray = tabArray.splice(tabArray.length-8);
      }
      localStorage.setItem('businessManager_tabs', JSON.stringify(tabArray))
      state.tabs = new Map(tabArray);
    },
    setMenus(state,menus) {
      localStorage.setItem('menus', JSON.stringify(menus))
      state.menus = menus;
    },
    setAccount(state,account) {
      localStorage.setItem('account', JSON.stringify(account))
      state.account = account;
    },
    setAuthorityList(state,authorityList) {
        localStorage.setItem('authorityList', JSON.stringify(authorityList))
        state.authorityList = authorityList;
    }
  },
  actions: {
    setQuery({ commit, state }, params) {
        if (params.routerName != "index") {
            var tabs = state.tabs;
            tabs.set(params.routerName, params.query);
            commit('setTabs', tabs);
        }
    },
      setLang(state,lang){
          localStorage.setItem('lang', lang)
          state.lang = lang;
      },
      closeTab({ commit, state }, tabName) {
          var tabs = state.tabs;
          tabs.delete(tabName);
          commit('setTabs', tabs);
      },
    setTabs({ commit, state }, tabs) {
      commit('setTabs', tabs);
    },
    setMenus({ commit, state }, menus) {
      commit('setMenus', menus);
    },
    setAccount({ commit, state }, account) {
      commit('setAccount', account);
    },setAuthorityList({ commit, state }, authorityList) {
          commit('setAuthorityList', authorityList);
    },clearGlobal({ commit, state }) {
      commit('setTabs', new Map());
      commit('setMenus', []);
      commit('setAccount', {});
      commit('setAuthorityList', []);
    },
      closeTab({ commit, state }, tabName) {
          var tabs = state.tabs;
          tabs.delete(tabName);
          commit('setTabs', tabs);
      },
      setLang({ commit, state }, lang) {
          commit('setLang', lang);
      },
  }
}
