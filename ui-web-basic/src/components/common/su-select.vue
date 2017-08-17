<template>
  <div>
    <el-select v-model="innerId"  filterable :remote="remote" :multiple="multiple" :disabled="disabled"  placeholder="输入关键字" :remote-method="remoteSelect" :loading="remoteLoading"  :clearable=true @change="handleChange">
      <el-option v-for="item in itemList"  :key="item.id" :label="item[labelProp]" :value="item.id"></el-option>
    </el-select>
  </div>
</template>
<script>
  export default {
    props: {
      value: {
        required: true
      },
      multiple: Boolean,
      disabled: Boolean,
      remote: Boolean,
      labelProp: {
        type: String,
        default: 'name'
      },
      searchByKeyMethod:{
        type:Function,
        required: true
      },
      searchByIdsMethod:{
        type:Function,
        required: true
      }
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
        this.doSearchByKey(this.innerId, query).then(()=>{
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
        if(this.remote){
          return this.doSearchByIds(val);
        }else if(create){
          return this.doSearchByKey(val, null);
        }else{
          return Promise.resolve();
        }
      },
      doSearchByKey(val, query){
        return this.searchByKeyMethod(query).then((response)=>{
          let newList = [];
          for(let item of this.itemList) {
            if(this.selected(val, item)) {
              newList.push(item);
            }
          }
          for(let item of response.data) {
            if(!this.selected(val, item)) {
              newList.push(item);
            }
          }
          this.itemList = newList;
        });
      },
      doSearchByIds(val){
        if(val){
          return this.searchByIdsMethod(val).then((response)=>{
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
      selected(val, item){
        if(!val){
          return false;
        }
        if(this.multiple) {
          return val.indexOf(item.id) >= 0;
        }
        return val === item.id;
      }
    },created () {
      this.initItemList(this.value, true).then(()=>{
        this.setValue(this.value);
      });
    },watch: {
      value :function (newVal) {
        if(this.innerId === newVal){
          return;
        }
        this.initItemList(newVal, false).then(()=>{
          this.setValue(newVal);
        });
      }
    }
  };
</script>
