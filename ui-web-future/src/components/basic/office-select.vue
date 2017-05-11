<template>
  <div>
    <el-select v-model="innerId"  filterable remote :multiple="isMultiple" :disabled="isDisabled"  :placeholder="$t('su_district.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.id" :label="item.fullName" :value="item.id"></el-option>
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
        isMultiple:false,
        isDisabled:false
      };
    } ,methods:{
      remoteSelect(query) {
        if(query=="" || query == this.innerId || query == util.getLabel(this.itemList,this.innerId,"fullName")) {
          return;
        }
        this.remoteLoading = true;
        axios.get('/api/basic/sys/office/search',{params:{name:query}}).then((response)=>{
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
        axios.get('/api/basic/sys/office/findById?id=' + this.innerId).then((response)=>{
          this.itemList=new Array(response.data);
          this.remoteLoading = false;
        })
      },
      setMultiple(mul){
        if(mul==='true'){
          this.isMultiple=true;
        }
      },
      setDisabled(dis){
        if(dis){
          this.isDisabled=true;
        }
      }
    },created () {
      this.setValue(this.value);
      this.setMultiple(this.multiple);
      this.setDisabled(this.disabled)
    },watch: {
      value :function (newVal) {
        this.setValue(newVal);
      }
    }
  };
</script>
