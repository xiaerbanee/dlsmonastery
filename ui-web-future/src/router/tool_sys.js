const oppoPlantAgentProductSelList = r => require.ensure([], () => r(require('pages/tool/sys/oppoPlantAgentProductSelList.vue')));
const vivoFactoryOrderList = r => require.ensure([], () => r(require('pages/tool/sys/vivoFactoryOrderList.vue')));
const vivoPlantProductsList = r => require.ensure([], () => r(require('pages/tool/sys/vivoPlantProductsList.vue')));

let routes = [
  {path: '/tool/sys/oppoPlantAgentProductSelList',component: oppoPlantAgentProductSelList,name: 'oppoPlantAgentProductSelList'},
  {path: '/tool/sys/vivoFactoryOrderList',component: vivoFactoryOrderList,name: 'vivoFactoryOrderList'},
  {path: '/tool/sys/vivoPlantProductsList',component: vivoPlantProductsList,name: 'vivoPlantProductsList'},
];

export default routes;
