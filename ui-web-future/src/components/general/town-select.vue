<template>
  <div>
    <el-select v-model="innerId"  filterable remote  :multiple="multiple" :disabled="disabled" :placeholder="$t('su_district.inputKey')" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.townId" :label="item.fullName" :value="item.townId"></el-option>
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
        if(!query) {
            return;
        }
        this.remoteLoading = true;
        axios.get('/api/general/sys/town/search',{params:{name:query}}).then((response)=>{
          this.itemList=response.data;
          this.remoteLoading = false;
        })
      }, handleChange(newVal) {
        this.$emit('input', newVal);
      },setValue(val) {
        if(val) {
          this.innerId = val;
          let idStr = this.innerId;
          if (this.multiple && this.innerId) {
            idStr = this.innerId.join();
          }
          if (util.isBlank(idStr)) {
            return;
          }
          this.remoteLoading = true;
          axios.get('/api/general/sys/town/findOne?id=' + this.innerId).then((response)=>{
            this.itemList=response.data;
            this.remoteLoading = false;
            this.$nextTick(()=>{
              this.$emit('afterInit');
            });
          })
        }else{
          this.innerId=[];
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
