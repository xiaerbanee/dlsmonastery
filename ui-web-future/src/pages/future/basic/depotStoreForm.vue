<template>
  <div>
    <head-tab active="depotStoreForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item label="名称" prop="name">
          <el-input v-model="inputForm.name" />
        </el-form-item>
        <el-form-item label="Code" prop="code">
          <el-input v-model="inputForm.code" />
        </el-form-item>
        <el-form-item label="机构" prop="officeId">
          <office-select v-model="inputForm.officeId"></office-select>
        </el-form-item>
        <el-form-item label="负责人" prop="contator">
          <el-input v-model="inputForm.contator" />
        </el-form-item>
        <el-form-item label="手机号" prop="mobilePhone">
          <el-input v-model="inputForm.mobilePhone" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <district-select v-model="inputForm.address" />
        </el-form-item>
        <el-form-item label="税务门店名称" prop="taxName">
          <district-select v-model="inputForm.taxName" />
        </el-form-item>
        <el-form-item label="寄售库" prop="delegateDepotId">
          <el-select v-model="inputForm.delegateDepotId" filterable>
            <el-option v-for="item in delegateDepotList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="仓库类型" prop="type">
          <el-select v-model="inputForm.type" filterable>
            <el-option v-for="item in typeList" :key="item" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="地区" prop="districtId">
          <district-select v-model="inputForm.districtId"></district-select>
        </el-form-item>
        <el-form-item label="分组" prop="group">
          <el-input v-model="inputForm.group"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('dictMapForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'
  import districtSelect from 'components/general/district-select'
  export default {
    components:{officeSelect,districtSelect},
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        inputForm:{},
        submitData:{
          id:'',
          name:"",
          code:"",
          officeId:"",
          contator:"",
          delegateDepotI:"",
          mobilePhone:"",
          address:"",
          taxName:"",
          delegateDepotId:"",
          type:"",
          districtId:"",
          group:"",
          remarks:"",
        },
        rules: {
          name: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          code: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          depotShopId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          name: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          officeId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          contator: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          mobilePhone: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          address: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          districtId: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          type: [{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          group:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
          taxName:[{ required: true, message: this.$t('dictMapForm.prerequisiteMessage')}],
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm,this.submitData);
            axios.post('/api/ws/future/basic/depotShop/saveDepot', qs.stringify(this.submitData)).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'depotShopList',query:util.getQuery("depotShopList")})
              }
            }).catch(function () {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }
    },created(){
      axios.get('/api/ws/future/basic/depotShop/findDepotForm',{params: {id:this.$route.query.id}}).then((response)=>{
        this.inputForm = response.data;
      })
    }
  }
</script>
