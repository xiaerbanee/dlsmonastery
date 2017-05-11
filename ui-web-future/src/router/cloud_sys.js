const kingdeeBookList  = r => require.ensure([], () => r(require('pages/cloud/sys/kingdeeBookList.vue')));
const kingdeeBookForm  = r => require.ensure([], () => r(require('pages/cloud/sys/kingdeeBookForm.vue')));
const accountKingdeeBookList  = r => require.ensure([], () => r(require('pages/cloud/sys/accountKingdeeBookList.vue')));

let routes = [
  {path: '/cloud/sys/kingdeeBookList',component: kingdeeBookList,name: 'kingdeeBookList'},
  {path: '/cloud/sys/kingdeeBookForm',component: kingdeeBookForm,name: 'kingdeeBookForm',meta:{menu:"kingdeeBookList"}},
  {path: '/cloud/sys/accountKingdeeBookList',component: accountKingdeeBookList,name: 'accountKingdeeBookList'},
];

export default routes;
