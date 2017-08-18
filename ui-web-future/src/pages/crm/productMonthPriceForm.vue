<template>
  <div>
    <head-tab active="productMonthPriceForm"></head-tab>
    <su-alert  :text="errMsg"  type="danger"></su-alert>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('productMonthPriceForm.month')" prop="month">
          <month-picker v-model="inputForm.month"  @input="onMonthChange"></month-picker>
        </el-form-item>

          <el-form-item :label="$t('productMonthPriceForm.remarks')" prop="remarks">
            <el-input type="textarea" v-model="inputForm.remarks"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('productMonthPriceForm.save')}}</el-button>
          </el-form-item>

        <template>
          <el-input v-model="productTypeName" @input="filterProducts" :placeholder="$t('productMonthPriceForm.searchProductTypeName')" style="width:200px;"></el-input>
          <el-table :data="filterProductMonthPriceDetailList" border >
            <el-table-column :label="$t('productMonthPriceForm.totalName')" prop="productTypeName"></el-table-column>
            <el-table-column :label="$t('productMonthPriceForm.baokaPrice')">
              <template scope="scope">
                <el-input v-model="scope.row.baokaPrice"></el-input>
              </template>
            </el-table-column>
            <el-table-column :label="$t('productMonthPriceForm.price3')">
              <template scope="scope">
                <el-input v-model="scope.row.price3"></el-input>
              </template>
            </el-table-column>
              <el-table-column :label="$t('productMonthPriceForm.price2')">
                <template scope="scope">
                  <el-input v-model="scope.row.price2"></el-input>
                </template>
            </el-table-column>
          </el-table>
        </template>

      </el-form>
    </div>
  </div>
</template>
<script>
  import monthPicker from 'components/common/month-picker'

    export default{
      components:{
        monthPicker,
      },

      data(){
        return this.getData()
      },
      methods:{
        getData() {
          return{
            isCreate:this.$route.query.id==null,
            submitDisabled:false,
            filterProductMonthPriceDetailList:[],
            productTypeName:"",
            inputForm:{
              extra:{},
            },
            rules: {
              month: [{ required: true, message: this.$t('productMonthPriceForm.prerequisiteMessage')}]
            },
            errMsg:''
          }
      },
        formSubmit(){
          if (this.errMsg) {
            this.$alert( this.$t('productMonthPriceForm.formInvalid'), this.$t('productMonthPriceForm.notify'));
            return;
          }
          this.submitDisabled = true;
          let form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              axios.post('/api/ws/future/crm/productMonthPrice/save',qs.stringify(util.deleteExtra(this.inputForm),{allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                if(response.data.success) {
                  if (this.isCreate) {
                    Object.assign(this.$data, this.getData());
                    this.initPage();
                  }else{
                    this.$router.push({name: 'productMonthPriceList', query: util.getQuery("productMonthPriceList"), params:{_closeFrom:true}})
                  }
                }
              }).catch(() => {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },onMonthChange(){

          if(!this.inputForm.month){
            this.errMsg="";
            return;
          }

          axios.get('/api/ws/future/crm/productMonthPrice/checkMonth',{params: {id:this.inputForm.id, month:this.inputForm.month}}).then((response)=>{
            this.errMsg=response.data;
          });

        },setProductMonthPriceDetailList(list){
          this.inputForm.productMonthPriceDetailList = list;
          this.filterProducts();

        },filterProducts(){

          if(!this.inputForm.productMonthPriceDetailList){
            this.filterProductMonthPriceDetailList = [];
            return;
          }
          if(!this.productTypeName){
            this.filterProductMonthPriceDetailList = this.inputForm.productMonthPriceDetailList;
            return;
          }
          let val=this.productTypeName;
          let tempList=[];

          for(let productMonthPriceDetail of this.inputForm.productMonthPriceDetailList){

            if(util.contains(productMonthPriceDetail.productTypeName, val)){
              tempList.push(productMonthPriceDetail);
            }
          }
          this.filterProductMonthPriceDetailList = tempList;
        },initPage(){

          axios.get('/api/ws/future/crm/productMonthPrice/getForm').then((response)=>{
            this.inputForm = response.data;
            if(this.isCreate){
              axios.get('/api/ws/future/crm/productMonthPrice/findDetailListForNew').then((response)=>{
                this.setProductMonthPriceDetailList(response.data);
              });
            }else{
              axios.get('/api/ws/future/crm/productMonthPrice/findDetailListForEdit',{params: {productMonthPriceId:this.$route.query.id}}).then((response)=>{
                this.setProductMonthPriceDetailList(response.data);
              });
              axios.get('/api/ws/future/crm/productMonthPrice/findDto',{params: {id:this.$route.query.id}}).then((response)=>{
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
