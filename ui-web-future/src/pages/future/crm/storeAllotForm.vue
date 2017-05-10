<template>
  <div>
    <head-tab active="storeAllotForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('storeAllotForm.allotType')" prop="allotType" >
          <el-select v-model="inputForm.allotType"  clearable @change="allotTypeChanged">
            <el-option v-for="item in inputForm.allotTypeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.fromStore')" prop="fromStore">
          <su-depot :disabled="inputForm.allotType=='快速调拨'" type="store" v-model="inputForm.fromStoreId" @input="fromStoreChanged"></su-depot>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.toStore')" prop="toStore">
          <su-depot :disabled="inputForm.allotType=='快速调拨'" type="store" v-model="inputForm.toStoreId"  ></su-depot>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.shipType')" prop="shipType">
          <el-select v-model="inputForm.shipType"  clearable >
            <el-option v-for="item in inputForm.shipTypeList":key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.expressCompany')" prop="expressCompany">
          <su-express-company v-model="inputForm.expressCompanyId"></su-express-company>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.syn')" prop="syn">
          <el-radio-group v-model="inputForm.syn">
            <el-radio :label="true">{{$t('storeAllotForm.yes')}}</el-radio>
            <el-radio :label="false">{{$t('storeAllotForm.no')}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('storeAllotForm.remarks')" prop="remarks">
          <el-input type="textarea" v-model="inputForm.remarks"></el-input>
        </el-form-item>


        <template>
          <el-form-item>
            <el-button type="primary" :disabled="submitDisabled"  @click="formSubmit()">{{$t('storeAllotForm.save')}}</el-button>
          </el-form-item>
          <el-table :data="inputForm.storeAllotDetailFormList" border stripe>
            <el-table-column  :label="$t('storeAllotForm.productName')">
              <template scope="scope">
                <su-product v-model ="scope.row.productId" @input="productSelected(scope.row)"></su-product>
              </template>
            </el-table-column>
            <el-table-column  :label="$t('storeAllotForm.cloudQty')" prop="cloudQty"></el-table-column>
            <el-table-column :label="$t('storeAllotForm.billQty')">
              <template scope="scope">
                <input type="text" v-model="scope.row.billQty" class="el-input__inner"/>
              </template>
            </el-table-column>
            <el-table-column :render-header="renderAction"  >
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
        productSelectedList:[],
        selectedProducts:new Map(),
        inputForm:{},
        submitData:{
          id:'',
          allotType:'',
          fromStoreId:'',
          toStoreId:'',
          shipType:'',
          expressCompanyId:'',
          syn:true,
          remarks:'',
          storeAllotDetailFormList:[],
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
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/ws/future/crm/storeAllot/save', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
              if(response.data.message){
                this.$message(response.data.message);
              }
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'storeAllotList',query:util.getQuery("storeAllotList")});
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },allotTypeChanged() {
        console.log("refreshAllotDetailFormList");
        axios.get('/api/ws/future/crm/storeAllot/findForm', {params: {id: this.$route.query.id, allotType: this.inputForm.allotType}}).then((response) => {
          this.inputForm = response.data;
          this.inputForm.storeAllotDetailFormList.push({productId: "", cloudQty: "", qty: ""});
        });
      },fromStoreChanged() {
        if(this.inputForm.allotType !='快速调拨'){

          axios.get('/api/ws/future/crm/storeAllot/findForm', {params: {id: this.$route.query.id, allotType: this.inputForm.allotType}}).then((response) => {
            this.inputForm = response.data;
            this.inputForm.storeAllotDetailFormList.push({productId: "", cloudQty: "", qty: ""});
          });
        }

      },removeDomain(item) {
        var index = this.inputForm.storeAllotDetailFormList.indexOf(item)
        if (index !== -1) {
          this.inputForm.storeAllotDetailFormList.splice(index, 1)
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
        this.inputForm.storeAllotDetailFormList.push({productId:"",cloudQty:"",qty:""});
        return false;
      },productSelected(row){
        axios.get('/api/ws/future/crm/storeAllot/getCloudQty?productId='+row.productId+"&fromStoreId="+this.inputForm.fromStoreId).then((res)=>{
          row.cloudQty=res.data
        });
      }
    },created(){
      axios.get('/api/ws/future/crm/storeAllot/findForm', {params: {id: this.$route.query.id}}).then((response) => {
        this.inputForm = response.data;
        this.inputForm.storeAllotDetailFormList.push({productId: "", cloudQty: "", qty: ""});
      });
    }
  }
</script>
