<template>
  <div>
    <el-select v-model="innerId"  filterable :remote="remote" :multiple="multiple" :disabled="disabled"  placeholder="输入关键字" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.id" :label="item.fullName" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: {
      value: {
        required: true
      },
      multiple: {
        default: false
      },
      disabled: {
        default: false
      },
      remote: {
        default: true
      },
      officeRuleName: {
        required: false
      },
    },

    data() {
      return {
        innerId:null,
        itemList : [],
        remoteLoading:false,
      };
    } ,methods:{
      remoteSelect(query) {
        if(util.isBlank(query)) {
          return;
        }
        this.remoteLoading = true;
        this.doSearchByKey(query).then(()=>{
          this.remoteLoading = false;
        }).catch(()=>{
          this.remoteLoading = false;
        });
      }, handleChange(newVal) {
        if(newVal !== this.value) {
          this.$emit('input', newVal);
        }
      },setValue(val) {
        if(this.innerId===val){
          return;
        }
        if(this.multiple){
          this.innerId = (val ? val : []);
        }else{
          this.innerId = val;
        }
        this.$nextTick(()=>{
          this.$emit('afterInit');
        });
      },initItemList(val, create){
        //在setValue之前被调用，确保相应的id有对应的记录，可以正确显示label
        if(val){
            return;
        }
        if(this.remote){
          return this.doSearchByIds(val);
        }else if(create){
          return this.doSearchByKey(null);
        }else{
          return Promise.resolve();
        }
      },
      doSearchByKey(query){
        return axios.get('/api/basic/sys/office/search',{params:{officeRuleName:this.officeRuleName, name:query}}).then((response)=>{
          let newList = [];
          for(let item of this.itemList) {
            if(this.selected(item)) {
              newList.push(item);
            }
          }
          for(let item of response.data) {
            if(!this.selected(item)) {
              newList.push(item);
            }
          }
          this.itemList = newList;
        });
      },
      doSearchByIds(val){
        if(val){
          return axios.get('/api/basic/sys/office/findByIds', {params: {ids: val}}).then((response)=>{
            if(response.data){
              this.itemList=response.data;
            }else{
              this.itemList=[];
            }
          });
        }else {
          this.itemList=[];
          return Promise.resolve();
        }
      },
      selected(item){
        if(!this.innerId){
          return false;
        }
        if(this.multiple) {
          return this.innerId.indexOf(item.id) >=0;
        }
        return this.innerId === item.id;
      }
    },created () {
        if(util.isBlank(this.value)){
            return;
        }
      this.initItemList(this.value, true).then(()=>{
        this.setValue(this.value);
      });
    },watch: {
      value :function (newVal) {
          if(util.isBlank(this.value)){
              return;
          }
        this.initItemList(newVal, false).then(()=>{
          this.setValue(newVal);
        });
      }
    }
  };
</script>
