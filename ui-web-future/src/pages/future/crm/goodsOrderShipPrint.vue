<template>
  <div style="width:794px;height:400px;margin:auto;">
    <div style="margin-top: 215px;">
      <span style="margin-left:  110px;">{{goodsOrder.contator}}</span>
      <span style="margin-left:  118px;">{{goodsOrder.mobilePhone}}</span>
    </div>
    <div style="margin-top: 3px;">
      <span style="margin-left:  110px;width:350px;">{{goodsOrder.name}}</span>
    </div>
    <div style="margin-top: 5px;">
      <span style="margin-left:  110px;width:350px;">{{goodsOrder.address}}</span>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        goodsOrder:{},
        rules: {
          pass: [{ required: true, message: this.$t('expressOrderList.prerequisiteMessage')}],
        },
        activityEntity:{},
      }
    },
    mounted() {
      setTimeout("window.print()",500);
    },
    methods:{
      findOne(){
        axios.get('/api/ws/future/crm/goodsOrderShip/shipPrint',{params: {goodsOrderId:this.$route.query.id}}).then((response)=>{
          this.goodsOrder=response.data;
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

