<template>
  <div>
    <el-select v-model="innerId"  filterable remote :multiple="isMultiple" :disabled="this.disabled" :placeholder="$t('su_district.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
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
        remoteLoading:false,
        isMultiple:false,
      };
    } ,methods:{
      remoteSelect(query) {
        if(query=="" || query == this.innerId || query == util.getLabel(this.itemList,this.innerId,"name")) {
          return;
        }
        this.remoteLoading = true;
        axios.get('/api/ws/future/basic/depot/search',{params:{name:query,category:this.category}}).then((response)=>{
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
        axios.get('/api/ws/future/basic/depot/searchById?id=' + this.innerId).then((response)=>{
          this.itemList=response.data;
          this.remoteLoading = false;
        })
      },
      setMultiple(mul){
        if(mul){
          this.isMultiple=true;
        }
      }
    },created () {
      this.setValue(this.value);
      this.setMultiple(this.multiple);
    },watch: {
      value :function (newVal) {
        this.setValue(newVal);
      }
    }
  };
</script>
