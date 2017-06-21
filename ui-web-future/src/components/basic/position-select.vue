<template>
  <div>
    <el-select v-model="innerId"  filterable remote :multiple="multiple" :disabled="disabled" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.id" :label="item.name" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: ['value','multiple','disabled'],
    data() {
      return {
        innerId:null,
        itemList : [],
        remoteLoading:false,
      };
    } ,methods:{
      remoteSelect(query) {
        if(query=="" || query == this.innerId || query == util.getLabel(this.itemList,this.innerId,"name")) {
          return;
        }
        this.remoteLoading = true;
        axios.get('/api/basic/hr/position/search',{params:{name:query}}).then((response)=>{
          this.itemList=response.data;
          this.remoteLoading = false;
        })
      }, handleChange(newVal) {
        this.$emit('input', newVal);
      },setValue(val) {
        if(this.innerId===val){
          return;
        }
        if(val) {
          this.innerId=val;
          this.remoteLoading = true;
          let idStr=this.innerId;
          if(this.innerId instanceof Array){
            idStr=this.innerId.join();
          }
          console.log("idStr"+idStr)
          axios.get('/api/basic/hr/position/findByIds?idStr=' + idStr).then((response)=>{
            this.itemList=response.data;
            this.remoteLoading = false;
            this.$nextTick(()=>{
              this.$emit('afterInit');
            });
          })
        }else{
          this.innerId=[];
          this.$nextTick(()=>{
            this.$emit('afterInit');
          });
        }
      },
    },created () {
      console.log(this.innerId)
      this.setValue(this.value);
    },watch: {
      value :function (newVal) {
          this.setValue(newVal);
      }
    }
  };
</script>
