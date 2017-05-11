<template>
  <div>
    <head-tab active="priceChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('priceChangeForm.name')" prop="name">
          <el-input v-model="inputForm.name" :disabled="!isCreate"></el-input>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.productTypeId')" prop="productTypeIdList">
          <el-select v-model="inputForm.productTypeIdList" multiple filterable clearable :placeholder="$t('priceChangeForm.inputWord')" :disabled="!isCreate">
            <el-option v-for="item in formProperty.productTypes" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item  :label="$t('priceChangeForm.priceChangeDate')" prop="priceChangeDate">
          <el-date-picker  v-model="inputForm.priceChangeDate" type="date" align="left" :placeholder="$t('priceChangeForm.selectDate')" format="yyyy-MM-dd" :disabled="!isCreate"></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.uploadEndDate')" prop="uploadEndDate">
          <el-date-picker  v-model="inputForm.uploadEndDate" type="date" align="left" :placeholder="$t('priceChangeForm.selectDate')" format="yyyy-MM-dd" :disabled="!isCreate"></el-date-picker>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" :disabled="!isCreate"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('priceChangeForm.save')}}</el-button>
        </el-form-item>
          <el-table :data="inputForm.priceChangeProductList" border stripe >
            <el-table-column :label="$t('priceChangeForm.productType')"  prop="productType.name"></el-table-column>
            <el-table-column :label="$t('priceChangeForm.price3')">
              <template scope="scope">
                <input type="text" v-model="scope.row.price3" class="el-input__inner"/>
              </template>
            </el-table-column>
            <el-table-column  :label="$t('priceChangeForm.baokaPrice')">
              <template scope="scope">
                <input type="text" v-model="scope.row.amount" class="el-input__inner"/>
              </template>
            </el-table-column>
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
            formProperty:{},
            inputForm:{
              id:this.$route.query.id,
              name:'',
              checkPercent:'',
              priceChangeDate:'',
              uploadEndDate:'',
              productTypeIdList:'',
              remarks:'',
              priceChangeProductList:[]
            },
            rules: {
              name: [{ required: true, message: this.$t('priceChangeForm.prerequisiteMessage')}],
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.priceChangeDate=util.formatLocalDate(this.inputForm.priceChangeDate);
              this.inputForm.uploadEndDate=util.formatLocalDate(this.inputForm.uploadEndDate);
              axios.post('/api/crm/priceChange/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'priceChangeList',query:util.getQuery("priceChangeList")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },
      },created(){
        axios.get('/api/crm/priceChange/getFormProperty').then((response)=>{
          this.formProperty=response.data;
        });
        if(!this.isCreate ){
          axios.get('/api/crm/priceChange/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
            this.inputForm.priceChangeProductList = response.data.priceChangeProductList;
            if(response.data.productTypeList!=null&&response.data.productTypeList.length>0){
              this.inputForm.productTypeIdList=util.getIdList(response.data.productTypeList);
            }
          })
        }
      }
    }
</script>
