<template>
  <div>
    <head-tab active="storeAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
            <el-form-item :label="$t('storeAllotForm.allotType')" prop="allotType" >
              <el-select v-model="inputForm.allotType"  clearable @change="getStoreAllot">
                <el-option v-for="item in formProperty.storeAllotTypes" :key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('storeAllotForm.fromStore')" prop="fromStore">
              <el-select v-model="inputForm.fromStoreId"  clearable>
                <el-option v-for="fromStore in formProperty.fromStores" :key="fromStore.id" :label="fromStore.name" :value="fromStore.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('storeAllotForm.toStore')" prop="toStore">
              <el-select v-model="inputForm.toStoreId"  clearable >
                <el-option v-for="toStore in formProperty.toStores" :key="toStore.id" :label="toStore.name" :value="toStore.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('storeAllotForm.shipType')" prop="shipType">
              <el-select v-model="inputForm.shipType"  clearable >
                <el-option v-for="item in formProperty.shipTypes":key="item" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('storeAllotForm.expressCompany')" prop="expressCompany">
              <el-select v-model="inputForm.expressCompanyId"  clearable >
                <el-option v-for="item in formProperty.expressCompanys" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('storeAllotForm.syn')" prop="syn">
              <el-radio-group v-model="inputForm.syn">
                <el-radio v-for="(value,key) in formProperty.bools" :key="key" :label="value">{{key | bool2str}}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="$t('storeAllotForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('storeAllotForm.save')}}</el-button>
            </el-form-item>
            <template>
              <el-table :data="inputForm.storeAllotDetailList" border stripe>
                <el-table-column :label="$t('storeAllotForm.productName')">
                  <template scope="scope">
                    <el-select v-model="scope.row.productId" remote filterable :placeholder="$t('storeAllotForm.inputKeyOf100Item')" :loading="loading" :remote-method="remoteProduct" @change="getCloudQty(scope.row)">
                      <el-option v-for="item in products" :key="item.id" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column  :label="$t('storeAllotForm.cloudQty')" prop="cloudQty"></el-table-column>
                <el-table-column :label="$t('storeAllotForm.billQty')">
                  <template scope="scope">
                    <input type="text" v-model="scope.row.billQty" class="el-input__inner"/>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('storeAllotForm.operation')" :render-header="renderAction"  >
                  <template scope="scope">
                    <el-button size="small" type="danger" @click.prevent="removeDomain(scope.row)">{{$t('storeAllotForm.delete')}}</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </template>
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
        loading: false,
        remoteLoading:false,
        formProperty:{},
        products:[],
        selectedProducts:new Map(),
        inputForm:{
          id:'',
          allotType:'',
          fromStoreId:'',
          toStoreId:'',
          shipType:'',
          expressCompanyId:'',
          syn:'',
          remarks:'',
          storeAllotDetailList:[]
        },
        rules: {
          allotType: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          fromStoreId: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          toStoreId: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          shipType: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          expressCompanyId: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
          syn: [{ required: true, message: this.$t('storeAllotForm.prerequisiteMessage')}],
        },
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/crm/storeAllot/save', qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'shopPrintList',query:util.getQuery("shopPrintList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },getStoreAllot(value){
        if(value=="快速调拨"){
          axios.get('/api/crm/storeAllot/getStoreAllotData',{params: {id:this.$route.query.id,allotType:"快速调拨"}}).then((response)=>{
              util.copyValue(response.data,this.inputForm);
              console.log(response.data)
              if(response.data.storeAllotDetailList!=null&&response.data.storeAllotDetailList.size()>0){
                  this.inputForm.storeAllotDetailList=response.data.storeAllotDetailList
              }
          })
        }
      },removeDomain(item) {
          var index = this.inputForm.storeAllotDetailList.indexOf(item)
          if (index !== -1) {
            this.inputForm.storeAllotDetailList.splice(index, 1)
          }
        },renderAction(createElement) {
          return createElement(
            'a',{
               attrs: {
                class: 'el-button el-button--primary el-button--small'
              }, domProps: {
                innerHTML: '增加'
              },on: {
                click: this.addDomain
              }
            }
          );
        },addDomain(){
        this.inputForm.storeAllotDetailList.push({productId:"",cloudQty:"",qty:""});
          return false;
        },remoteProduct(query){
          if (query !== '') {
            this.remoteLoading = true;
            axios.get('/api/crm/product/searchAll',{params:{name:query}}).then((response)=>{
              var dataMap = new Map();
              response.data.map((v,index)=>{
                dataMap.set(response.data[index].id,response.data[index]);
                this.selectedProducts.set(response.data[index].id,response.data[index]);
              });
             this.inputForm.storeAllotDetailList.map((v,index)=>{
                var productId = this.inputForm.storeAllotDetailList[index].productId;
                 if(!dataMap.has(productId) && this.selectedProducts.has(productId) ) {
                  dataMap.set(productId,this.selectedProducts.get(productId));
                 }
              });
              this.products = new Array();
              for(let value of dataMap.values()){
                this.products.push(value);
              }
              this.remoteLoading = false;
            })
          }
      },getCloudQty(row){
        axios.get('/api/crm/storeAllot/getCloudQty?productId='+row.productId+"&fromStoreId="+this.inputForm.fromStoreId).then((res)=>{
            row.cloudQty=res.data
        });
      }
      },created(){
        axios.get('/api/crm/storeAllot/getFormProperty').then((res)=>{
            this.formProperty=res.data;
            this.formProperty.fromStores=res.data.stores;
            this.formProperty.toStores=res.data.stores;
        });
        if(this.isCreate){
          for(var i = 0;i<3;i++) {
             this.inputForm.storeAllotDetailList.push({productId:"",cloudQty:"",qty:""});
          }
        } else {
          axios.get('/api/crm/storeAllot/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        }
      }
  }
</script>
