<template>
  <div>
    <el-select v-model="innerId"  filterable remote :placeholder="$t('su_district.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.id" :label="item.fullName" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value'],
    data() {
      return {
        innerId:this.value,
        itemList : [],
        remoteLoading:false
      };
    } ,methods:{
      remoteSelect(query) {
        if(query == this.innerId || query == util.getLabel(this.itemList,this.innerId,"fullName")) {
            return;
        }
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/general/sys/district/search',{params:{key:query}}).then((response)=>{
            this.itemList=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.itemList = [];
        }
      }, handleChange(newVal) {
        this.$emit('input', newVal);
        return true;
      },setValue(val) {
        this.innerId=val;
        if(val && val != "") {
          var idList = util.getIdList(this.itemList);
          if(idList.indexOf(this.innerId) < 0) {
            this.remoteLoading = true;
            axios.get('/api/general/sys/district/findById?id=' + this.innerId).then((response)=>{
              this.itemList=response.data;
              this.remoteLoading = false;
            })
          }
        }
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
