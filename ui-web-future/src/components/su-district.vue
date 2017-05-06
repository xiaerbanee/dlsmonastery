<template>
  <div>
    <el-select v-model="innerDistrictId"  filterable remote :placeholder="$t('su_district.inputKey')" :remote-method="remoteDistrict" :loading="remoteLoading"  :clearable=true @change="valueChanged">

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
        remoteLoading:false,
        searchLock:false,
      };
    } ,methods:{
      remoteDistrict(query) {
        if(this.searchLock) return;
        if ( query !== '') {
          this.remoteLoading = true;
          axios.get('/api/general/sys/district/search',{params:{key:query}}).then((response)=>{
            this.districts=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.districts = [];
        }

      }, valueChanged(newVal) {
        this.$emit('input', newVal);
        return true;
      }
    }, watch:{
      value:function(){
        if(this.value==this.innerDistrictId) return;

        this.innerDistrictId=this.value;
        this.searchLock = true;
        this.remoteLoading = true;
        axios.get('/api/general/sys/district/searchById',{params:{id:this.innerDistrictId}}).then((response)=>{
          this.districts=response.data;
          this.remoteLoading = false;
          this.searchLock = false;
        })

      }
    }
  };
</script>
