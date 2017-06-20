const kingdeeBookList  = r => require.ensure([], () => r(require('pages/cloud/sys/kingdeeBookList.vue')));
const kingdeeBookForm  = r => require.ensure([], () => r(require('pages/cloud/sys/kingdeeBookForm.vue')));
const accountKingdeeBookList  = r => require.ensure([], () => r(require('pages/cloud/sys/accountKingdeeBookList.vue')));
const accountKingdeeBookForm  = r => require.ensure([], () => r(require('pages/cloud/sys/accountKingdeeBookForm.vue')));
const productManager  = r => require.ensure([], () => r(require('pages/cloud/sys/productManager.vue')));
const voucherList  = r => require.ensure([], () => r(require('pages/cloud/sys/voucherList.vue')));
const voucherForm  = r => require.ensure([], () => r(require('pages/cloud/sys/voucherForm.vue')));

let routes = [
  {path: '/cloud/sys/kingdeeBookList',component: kingdeeBookList,name: 'kingdeeBookList'},
  {path: '/cloud/sys/kingdeeBookForm',component: kingdeeBookForm,name: 'kingdeeBookForm',meta:{menu:"kingdeeBookList",keepAlive:true}},
  {path: '/cloud/sys/accountKingdeeBookList',component: accountKingdeeBookList,name: 'accountKingdeeBookList'},
  {path: '/cloud/sys/accountKingdeeBookForm',component: accountKingdeeBookForm,name: 'accountKingdeeBookForm',meta:{menu:"accountKingdeeBookList"}},
  {path: '/cloud/sys/productManager',component: productManager,name: 'productManager'},
  {path: '/cloud/sys/voucherList',component: voucherList,name: 'voucherList'},
  {path: '/cloud/sys/voucherForm',component: voucherForm,name: 'voucherForm',meta:{menu:"voucherList"}},
];

export default routes;
