<template>
  <div>
    <el-select v-model="status"  filterable  :multiple="multiple" :disabled="disabled" :placeholder="$t('su_district.inputKey')"    :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.name" :label="item.name" :value="item.name"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value','type','multiple','disabled'],
    data() {
      return {
        status : this.value,
        itemList : [],
      };
    } ,methods:{
     handleChange(newVal) {
       if(newVal !== this.value) {
         this.$emit('input', newVal);
       }
     }
    },created () {
      axios.get('/api/general/sys/processFlow/findByProcessTypeName',{params:{processTypeName:this.type}}).then((response)=>{
        this.itemList=response.data;
      });
    },watch: {
      value :function (newVal) {
          this.status = newVal;
      }
    }
  };
</script>
