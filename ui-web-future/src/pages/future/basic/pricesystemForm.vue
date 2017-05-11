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
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('pricesystemForm.save')}}</el-button>
        </el-form-item>
        <el-input v-model="productName" @change="searchDetail" :placeholder="$t('pricesystemForm.input2Key')" style="width:200px;"></el-input>
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
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        productName:'',
        filterPricesystemDetailList:[],
        inputForm:{
          id:'',
          name:'',
          sort:'',
          remarks:'',
          pricesystemDetailList:[]
        },
        submitData:{
          id:'',
          name:'',
          sort:'',
          remarks:'',
          enabled:false,
          pricesystemDetailList:[]
        },
        pageLoading: false,
        rules: {
          name: [{ required: true, message: this.$t('pricesystemForm.prerequisiteMessage')}],
          sort: [{ required: true, message: this.$t('pricesystemForm.prerequisiteMessage')}, { type: 'number', message: this.$t('pricesystemForm.inputLegalValue')}]
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            var tempList=new Array();
            for(var index in this.filterPricesystemDetailList){
                var detail = this.filterPricesystemDetailList[index];
                if(util.isNotBlank(detail.price)){
                    tempList.push(detail);
                }
            }
            this.inputForm.pricesystemDetailList=tempList;
            util.copyValue(this.inputForm,this.submitData);
            this.submitData.enabled = true;
            axios.post('/api/ws/future/crm/pricesystem/save', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'pricesystemList',query:util.getQuery("pricesystemList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },searchDetail(){
        var val=this.productName;
        var tempList=new Array();
        for(var index in this.inputForm.pricesystemDetailList){
          var detail=this.inputForm.pricesystemDetailList[index];
          if(util.contains(detail.productName,val)){
            tempList.push(detail)
          }
        }
        this.filterPricesystemDetailList = tempList;
      }
    },created(){
        axios.get('/api/ws/future/crm/pricesystem/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
          this.inputForm.pricesystemDetailList=response.data.pricesystemDetailList;
          this.searchDetail();
        })
    }
  }
</script>
