<template>

  <div style="width:794px;height:562px;margin:auto;">
    <br/>

    <div style="width:794px;height:400px;margin:auto;">
      <div style="margin-top: 215px;">
        <span style="margin-left:  110px;">{{storeAllot.toStoreContator }}</span>
        <span style="margin-left:  118px;">{{storeAllot.toStoreMobilePhone }}</span>
      </div>
      <div style="margin-top: 3px;">
        <span style="margin-left:  110px;width:350px;">{{storeAllot.toStoreName}}</span>
      </div>
      <div style="margin-top: 5px;">
        <span style="margin-left:  110px;width:350px;">{{storeAllot.toStoreAddress}}</span>
      </div>
    </div>
  </div>
</template>
<script>
  export default{
    data(){
      return{
        storeAllot : {},
      }
    },
    created(){
      axios.get('/api/ws/future/crm/storeAllot/shipPrint', {params: {id: this.$route.query.id}}).then((response)=>{
        this.storeAllot = response.data;
        this.$nextTick(()=>{
          window.print();
          this.$router.push({ name: 'storeAllotList'});
        });
      });
    }
  }
</script>
<style type="text/css">
body{
  font-size: 18px;
  font-weight:500;
  line-height: 26px;
}

</style>
