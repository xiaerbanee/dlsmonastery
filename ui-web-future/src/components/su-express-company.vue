<template>
  <div>
    <el-select v-model="innerId"  filterable remote :placeholder="$t('su_district.inputKey')" :remote-method="remoteSearch" :loading="remoteLoading"  :clearable=true @change="valueChanged">
      <el-option v-for="item in options"  :key="item.id" :label="item.name" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: {
      value: {
        required: true
      }
    },
    data() {
      return {
        innerId:this.value,
        options : [],
        remoteLoading:false,
        searchLock:false,
      };
    } ,
    computed:{
      url: function() {
              return {
                search:'/api/ws/future/basic/expressCompany/search',
                searchById:'/api/ws/future/basic/expressCompany/searchById',
              }
      }
    },
    methods:{
      remoteSearch(query) {
        if(this.searchLock) return;
        if ( query !== '') {
          this.remoteLoading = true;
          axios.get(this.url.search, {params:{key:query}}).then((response)=>{
            this.options=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.options = [];
        }

      }, valueChanged(newVal) {
        this.$emit('input', newVal);
        return true;
      }
    }, watch:{
      value:function(){
        if(this.value==this.innerId) return;
        this.searchLock = true;
        this.remoteLoading = true;
        this.innerId=this.value;

        axios.get(this.url.searchById,{params:{id:this.innerId}}).then((response)=>{
          this.options=response.data;
          this.remoteLoading = false;
          this.searchLock = false;
        })

      }
    }
  };
</script>
