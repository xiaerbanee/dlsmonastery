<template>
  <div>
    <head-tab active="priceChangeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('priceChangeForm.name')" prop="name">
          <el-input v-model="inputForm.name" :disabled="!isCreate"></el-input>
        </el-form-item>
        <el-form-item :label="$t('priceChangeForm.productTypeId')" prop="productTypeIdList">
          <product-type  v-model="inputForm.productTypeIdList"></product-type>
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
          <el-table :data="inputForm.productTypeDtos" border stripe >
            <el-table-column :label="$t('priceChangeForm.productType')"  prop="name"></el-table-column>
            <el-table-column :label="$t('priceChangeForm.price3')">
              <template scope="scope">
                <input type="text" v-model="scope.row.price3" class="el-input__inner"/>
              </template>
            </el-table-column>
            <el-table-column  :label="$t('priceChangeForm.baokaPrice')">
              <template scope="scope">
                <input type="text" v-model="scope.row.baokaPrice" class="el-input__inner"/>
              </template>
            </el-table-column>
          </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  import productType from 'components/future/product-type-select'
    export default{
      components:{productType},
      data(){
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            inputForm:{
            },
            submitData:{
              id:this.$route.query.id,
              name:'',
              checkPercent:'',
              priceChangeDate:'',
              uploadEndDate:'',
              productTypeIdList:'',
              remarks:'',
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
          axios.get('/api/ws/future/crm/priceChange/findForm',{params: {id:this.$route.query.id}}).then((response)=>{
            this.inputForm = response.data;
            console.log(this.inputForm);
          })
        }
    }
</script>
