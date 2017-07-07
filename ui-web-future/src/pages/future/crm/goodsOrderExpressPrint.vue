<template>
  <div style="width:794px;height:400px;margin:auto;">
    <div style="margin-top: 215px;">
      <span style="margin-left:  110px;">{{goodsOrderStore.contator}}</span>
      <span style="margin-left:  118px;">{{goodsOrderStore.mobilePhone}}</span>
    </div>
    <div style="margin-top: 3px;">
      <span style="margin-left:  110px;width:350px;">{{goodsOrderStore.shopName}}</span>
    </div>
    <div style="margin-top: 5px;">
      <span style="margin-left:  110px;width:350px;">{{goodsOrderStore.address}}</span>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        goodsOrderStore:{},
        rules: {
          pass: [{ required: true, message: this.$t('expressOrderList.prerequisiteMessage')}],
        }
      }
    },
    methods:{
      findOne(){
        axios.get('/api/ws/future/crm/goodsOrderShip/shipPrint',{params: {goodsOrderId:this.$route.query.id}}).then((response)=>{
          this.goodsOrderStore=response.data;
          this.$nextTick(()=>{
            window.print();
            this.$router.push({ name: 'adGoodsOrderList'});
          });
        })
      }
    },created(){
      this.findOne();
    }
  }
</script>
<style type="text/css">
  span {
    font-size: 19px;
  }
  body {
    display: block;
    margin: 8px;
  }
</style>

