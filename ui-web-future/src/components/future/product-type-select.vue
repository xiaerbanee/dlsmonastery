<template>
  <div>
    <el-select v-model="innerId"  filterable remote :multiple="multiple" :disabled="disabled" :placeholder="$t('su_district.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value','multiple','disabled'],
    data() {
      return {
        innerId:this.value,
        itemList : [],
        remoteLoading:false,
      };
    } ,methods:{
      remoteSelect(query) {
        if(query=="" || query == this.innerId || query == util.getLabel(this.itemList,this.innerId,"name")) {
          return;
        }
        this.remoteLoading = true;
        axios.get('/api/ws/future/basic/productType/search',{params:{name:query}}).then((response)=>{
          this.itemList=response.data;
          this.remoteLoading = false;
        })
      }, handleChange(newVal) {
        this.$emit('input', newVal);
      },setValue(val) {
        if(this.innerId == val || val=="") {
          return;
        }
        this.innerId=val;
        this.remoteLoading = true;
        axios.get('/api/ws/future/basic/productType/searchById?id=' + this.innerId).then((response)=>{
          this.itemList=response.data;
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
