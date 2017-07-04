const oppoPlantAgentProductSelList = r => require.ensure([], () => r(require('pages/tool/sys/oppoPlantAgentProductSelList.vue')));
const vivoFactoryOrderList = r => require.ensure([], () => r(require('pages/tool/sys/vivoFactoryOrderList.vue')));

let routes = [
  {path: '/tool/sys/oppoPlantAgentProductSelList',component: oppoPlantAgentProductSelList,name: 'oppoPlantAgentProductSelList'},
  {path: '/tool/sys/vivoFactoryOrderList',component: oppoPlantAgentProductSelList,name: 'vivoFactoryOrderList'},
];

export default routes;
