<template>
  <div>
    <el-select v-model="innerId"  filterable remote :multiple="multiple" :disabled="disabled" :placeholder="$t('su_district.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value','category','multiple','disabled'],
    data() {
      return {
        innerId:this.value,
        itemList : [],
        remoteLoading:false
      };
    }, computed: {
      searchUrl: function() {
          if(this.category =='directShop'){
              return '/api/ws/future/basic/depot/directShop';
          }else if(this.category =='delegateShop'){
            return '/api/ws/future/basic/depot/delegateShop';
          }else if(this.category =='shop'){
            return '/api/ws/future/basic/depot/shop';
          }else if(this.category =='directStore'){
            return '/api/ws/future/basic/depot/directStore';
          }else if(this.category =='delegateStore'){
            return '/api/ws/future/basic/depot/delegateStore';
          }else if(this.category =='store'){
            return '/api/ws/future/basic/depot/store';
          }else if(this.category =='adShop'){
            return '/api/ws/future/basic/depot/adShop';
          }else if(this.category =='popShop'){
            return '/api/ws/future/basic/depot/popShop';
          }
          return '';
      },
      searchByIdUrl:function(){
          return '/api/ws/future/basic/depot/findById';
      }
    },methods:{
      remoteSelect(query) {
        if(query=="" || query == this.innerId || query == util.getLabel(this.itemList,this.innerId,"name")) {
          return;
        }
        this.remoteLoading = true;
        axios.get(this.searchUrl,{params:{name:query}}).then((response)=>{
          this.itemList=response.data;

          this.remoteLoading = false;
        })
      }, handleChange(newVal) {
        this.$emit('input', newVal);
        return true;
      },setValue(val) {
        if(this.innerId != val ) {
          this.innerId=val;
        }

        if(!this.innerId){
            return;
        }

        this.remoteLoading = true;
        axios.get(this.searchByIdUrl+'?id=' + this.innerId).then((response)=>{
          this.itemList=[];
          this.itemList.push(response.data);
          this.remoteLoading = false;
        })
      }
    },created () {
      this.setValue(this.value);
    },watch: {
      value :function (newVal) {

          this.setValue(newVal);

      }
    }
  };
</script>
