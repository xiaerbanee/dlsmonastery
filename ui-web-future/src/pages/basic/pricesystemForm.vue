<template>
  <div>
    <head-tab active="pricesystemForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('pricesystemForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('pricesystemForm.sort')" prop="sort">
          <el-input v-model.number="inputForm.sort"></el-input>
        </el-form-item>
        <el-form-item :label="$t('pricesystemForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('pricesystemForm.save')}}</el-button>
        </el-form-item>
        <el-input v-model="productName" @change="searchDetail" :placeholder="$t('pricesystemForm.inputKey')" style="width:200px;"></el-input>
          <el-table :data="filterPricesystemDetailList"  style="margin-top:5px;"   stripe border >
            <el-table-column prop="productName" :label="$t('pricesystemForm.productName')"></el-table-column>
            <el-table-column prop="price":label="$t('pricesystemForm.price')" v-if="isCreate">
              <template scope="scope">
                <input type="text" v-model="scope.row.price" class="el-input__inner"/>
              </template>
            </el-table-column>
            <el-table-column prop="price" :label="$t('pricesystemForm.price')" v-if="!isCreate"></el-table-column>
            <el-table-column prop="sort" :label="$t('pricesystemForm.sort')" v-if="!isCreate"></el-table-column>
          </el-table>
      </el-form>
    </div>

  </div>
</template>
<script>
  export default{
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        isCreate:this.$route.query.id == null,
        submitDisabled:false,
        productName:'',
        pricesystemDetailList:[],
        filterPricesystemDetailList:[],
        inputForm:{
          extra:{}
        },
        pageLoading: false,
        rules: {
          name: [{ required: true, message: this.$t('pricesystemForm.prerequisiteMessage')}],
          sort: [{ required: true, message: this.$t('pricesystemForm.prerequisiteMessage')}, { type: 'number', message: this.$t('pricesystemForm.inputLegalValue')}]
        }
      }
    },
      formSubmit(){
        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let tempList=new Array();
            for(let index of this.pricesystemDetailList){
                if(util.isNotBlank(index.price)){
                    tempList.push(index);
                }
            }
            this.inputForm.pricesystemDetailList=tempList;
            this.inputForm.enabled = true;
            axios.post('/api/ws/future/basic/pricesystem/save', qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                Object.assign(this.$data,this.getData());
                this.initPage();
              }else {
                util.closeAndBackToPage(this.$router,'pricesystemList')
              }
            }).catch( ()=> {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },searchDetail(){
        let val=this.productName;
        console.log(this.productName)
        console.log(this.filterPricesystemDetailList,"filter");
        console.log(this.pricesystemDetailList)
        if(!val){
            this.filterPricesystemDetailList = this.pricesystemDetailList;
            return;
        }
        let tempList=new Array();
//        for(let index of this.pricesystemDetailList){
//          if(util.isNotBlank(index.price)){
//            tempList.push(index)
//          }
//        }
        for(let index of this.pricesystemDetailList){
          if(util.contains(index.productName,val)&&util.isNotBlank(index.price)){
            tempList.push(index)
          }
        }
        this.filterPricesystemDetailList = tempList;
      },initPage(){
        axios.get('/api/ws/future/basic/pricesystem/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
          this.pricesystemDetailList = response.data.pricesystemDetailList;
          this.searchDetail();
          if(!this.isCreate) {
            axios.get('/api/ws/future/basic/pricesystem/findOne', {params: {id: this.$route.query.id}}).then((response) => {
              util.copyValue(response.data, this.inputForm);
            });
          }
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
