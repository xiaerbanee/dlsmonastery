<template>
  <div>
    <el-select v-model="innerDistrictId"  filterable remote :placeholder="$t('su_district.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in districts"  :key="item.id" :label="item.fullName" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value'],
    data() {
      return {
        innerDistrictId:this.value,
        districts : [],
        remoteLoading:false
      };
    } ,methods:{
      remoteSelect(query) {
        if(query == this.innerDistrictId) {
            return;
        }
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/general/sys/district/search',{params:{key:query}}).then((response)=>{
            this.districts=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.districts = [];
        }
      }, handleChange(newVal) {
        this.$emit('input', newVal);
        return true;
      },setValue(val) {
        this.innerDistrictId=val;
        if(val && val != "") {
          var idList = util.getIdList(this.districts);
          if(idList.indexOf(this.innerDistrictId) < 0) {
            this.remoteLoading = true;
            axios.get('/api/general/sys/district/findById?id=' + this.innerDistrictId).then((response)=>{
              this.districts=response.data;
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
