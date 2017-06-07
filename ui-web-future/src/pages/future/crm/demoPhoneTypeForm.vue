<template>
  <div>
    <head-tab active="demoPhoneTypeForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('demoPhoneTypeForm.name')" prop="name">
          <el-input  v-model.number="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.productType')" prop="productTypeIdList">
          <product-type-select  v-model="inputForm.productTypeIdList" :multiple=true></product-type-select>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.limitQty')" prop="limitQty">
          <el-input  v-model="inputForm.limitQty" @blur="showQty"></el-input>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.applyEndDate')" prop="applyEndDate">
          <date-picker v-model="inputForm.applyEndDate" :placeholder="$t('demoPhoneTypeForm.selectDate')"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.renewEndDate')" prop="renewEndDate">
          <date-picker v-model="inputForm.renewEndDate" :placeholder="$t('demoPhoneTypeForm.selectDate')"></date-picker>
        </el-form-item>
        <el-form-item :label="$t('demoPhoneTypeForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('demoPhoneTypeForm.save')}}</el-button>
        </el-form-item>
        <el-table :data="demoPhoneTypeOfficeDtos"  style="margin-top:5px;"   stripe border>
          <el-table-column prop="officeName" :label="$t('demoPhoneTypeForm.officeName')"></el-table-column>
          <el-table-column prop="officeTaskPointString" :label="$t('demoPhoneTypeForm.taskPoint')"></el-table-column>
          <el-table-column prop="qty" :label="$t('demoPhoneTypeForm.qty')">
            <template scope="scope">
              <el-input  v-model="scope.row.qty" @input="qtySum"></el-input>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
    </div>
  </div>
</template>
<script>
  import productTypeSelect from 'components/future/product-type-select'
  export default{
    components:{
      productTypeSelect
    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return{
        isInit:false,
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        formProperty:{},
        inputForm:{},
        demoPhoneTypeOfficeDtos:[],
        submitData:{
          id:'',
          name:'',
          limitQty:'',
          applyEndDate:'',
          renewEndDate:'',
          remarks:'',
          demoPhoneTypeOfficeDtos:[],
          productTypeIdList:[]
        },
        remoteLoading:false,
        productTypes:[],
        rules: {
          name: [{ required: true, message: this.$t('demoPhoneTypeForm.prerequisiteMessage')}],
          productType: [{ required: true, message: this.$t('demoPhoneTypeForm.prerequisiteMessage')}],
          applyEndDate: [{ required: true, message: this.$t('demoPhoneTypeForm.prerequisiteMessage')}]
        },
        pickerDateOption:util.pickerDateOption
      }
    },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            let demoPhoneTypeOfficeList = new Array();
            for(let key in this.demoPhoneTypeOfficeDtos){
                if(this.demoPhoneTypeOfficeDtos[key].qty!=0&&this.demoPhoneTypeOfficeDtos[key].qty!=null){
                    demoPhoneTypeOfficeList.push(this.demoPhoneTypeOfficeDtos[key]);
                }
            }
            this.submitData.demoPhoneTypeOfficeDtos = demoPhoneTypeOfficeList;
            axios.post('/api/ws/future/crm/demoPhoneType/save', qs.stringify(this.submitData, {allowDots:true})).then((response)=> {
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                if(!this.isCreate){
                  this.$router.push({name:'demoPhoneTypeList',query:util.getQuery("demoPhoneTypeList")})
                }
              }).catch(function () {
                that.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
          }
        })
      },remoteProduct(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/ws/future/crm/demoPhoneType/search',{params:{name:query}}).then((response)=>{
            this.productTypes = response.data;
            this.remoteLoading = false;
          });
        }
      },showQty(){
          let sum = this.inputForm.limitQty;
          let realSum = 0;
          for(let key in this.demoPhoneTypeOfficeDtos){
              this.demoPhoneTypeOfficeDtos[key].qty = parseInt(sum*this.demoPhoneTypeOfficeDtos[key].officeTaskPoint/100);
          }

          for(let key in this.demoPhoneTypeOfficeDtos){
            realSum += parseInt(this.demoPhoneTypeOfficeDtos[key].qty);
          }
        this.inputForm.limitQty  = realSum;
      },qtySum(){
        let realSum = 0;
        for(let key in this.demoPhoneTypeOfficeDtos){
          realSum += parseInt(this.demoPhoneTypeOfficeDtos[key].qty);
        }
        this.inputForm.limitQty  = realSum;
      }
    },activated () {
      if(!this.$route.query.headClick || !this.isInit) {
        axios.get('/api/ws/future/crm/demoPhoneType/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          this.inputForm = response.data;
        });
        axios.get('/api/ws/future/crm/demoPhoneType/getForm',{params: {id:this.$route.query.id}}).then((response)=>{
          this.demoPhoneTypeOfficeDtos = response.data.demoPhoneTypeOfficeDtos;
        });
      }
      this.isInit = true;
    }
  }
</script>

