<template>
  <div>
    <head-tab active="chainForm"></head-tab>
    <!--<div>
      <el-form :model="inputForm" :inline="true" ref="inputForm" :rules="rules"  class="form input-form">
        <el-form-item :label="$t('chainForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        &lt;!&ndash;<el-form-item :label="$t('chainForm.account')" prop="accountIdList">
          <el-select v-model="inputForm.accountIdList" multiple filterable remote :placeholder="$t('chainForm.inputWord')" :remote-method="remoteAccount" :loading="remoteLoading" :clearable=true >
            <el-option v-for="account in accounts" :key="account.id" :label="account.fullName" :value="account.id"></el-option>
          </el-select>
        </el-form-item>&ndash;&gt;
        <el-form-item :label="$t('chainForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('chainForm.save')}}</el-button>
          <el-button type="primary" @click="formVisible = true" icon="search">{{$t('chainForm.filter')}}</el-button>
          <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
        </el-form-item>
      </el-form>
      <el-dialog :title="$t('chainForm.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('chainForm.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('chainForm.inputKey')">
                  <el-option v-for="(value,key) in formProperty.types" :key="key" :label="value" :value="key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaType" filterable clearable :placeholder="$t('chainForm.inputKey')">
                  <el-option v-for="areaType in formProperty.areaTypes" :key="areaType.name" :label="areaType.name" :value="areaType.name"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable remote :placeholder="$t('chainForm.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
                  <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.adPricesystemId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.adPricesystemId" filterable clearable :placeholder="$t('chainForm.inputKey')">
                  <el-option v-for="adPricesystem in formProperty.adPricesystems"  :key="adPricesystem.id" :label="adPricesystem.name" :value="adPricesystem.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row >
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('chainForm.sure')}}</el-button>
        </div>
      </el-dialog>
    </div>-->

    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
        <el-form-item :label="$t('chainForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <!--<el-form-item :label="$t('chainForm.account')" prop="accountIdList">
          <el-select v-model="inputForm.accountIdList" filterable remote multiple :placeholder="$t('bankForm.inputWord')" :remote-method="remoteAccount" :loading="remoteLoading" >
            <el-option v-for="item in accounts" :key="item.id" :label="item.fullName" :value="item.id"></el-option>
          </el-select>
        </el-form-item>-->
        <el-form-item :label="$t('chainForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('chainForm.save')}}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--<el-table :data="content" :height="pageHeight" ref='table' style="margin-top:5px;" v-loading="pageLoading"  @selection-change="selectionChange"  :element-loading-text="$t('chainForm.loading')" @sort-change="sortChange" stripe border>
      <el-table-column type="selection" width="55" ></el-table-column>
      <el-table-column  prop="name" :label="$t('chainForm.name')" width="250"></el-table-column>
      <el-table-column prop="adPricesystemId" :label="$t('chainForm.isAdPricesystem')" >
        <template scope="scope">
          <el-tag :type="scope.row.chainId===inputForm.id ? 'primary' : 'danger'">{{scope.row.chainId===inputForm.id | bool2str}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column  prop="office.name" :label="$t('chainForm.officeName')" ></el-table-column>
      <el-table-column prop="area.name" :label="$t('chainForm.areaName')" ></el-table-column>
      <el-table-column prop="typeLabel" :label="$t('chainForm.shopType')"  ></el-table-column>
      <el-table-column prop="chain.name" :label="$t('chainForm.chainName')"></el-table-column>
      <el-table-column prop="areaType" :label="$t('chainForm.areaType')"></el-table-column>
    </el-table>-->
    <!--<pageable :page="page" v-on:pageChange="pageChange"></pageable>-->
  </div>
</template>
<script>
  export default{
    updated(){
      if(this.pageUpdated) {
        this.pageUpdated = false;
        if(this.content != null && this.content.length>0) {
          this.$refs['table'].clearSelection();
          this.content.map((v,index)=>{
            if(v.chainId ===this.inputForm.id){
              this.$refs['table'].toggleRowSelection(this.content[index],true);
            }
          })
          this.selectChange = true;
        }
      }
    },
    data(){
      return{
        isCreate:this.$route.query.id==null,
        submitDisabled:false,
        formProperty:{},
        page:{},
        content:[],
        offices:[],
        accounts:[],
        formData:{
          page:0,
          size:25,
          order:'chainId:DESC',
          name:'',
          type:'',
          areaType:'',
          officeId:'',
          adPricesystemId:'',
        },formLabel:{
          name:{label:this.$t('chainForm.name')},
          type:{label:this.$t('chainForm.shopType'),value:''},
          areaType:{label:this.$t('chainForm.areaType')},
          officeId:{label:this.$t('chainForm.officeName'),value:''},
          adPricesystemId:{label:this.$t('chainForm.adPricesystemName'),value:''},
        },
        pageLoading: false,
        pageUpdated:false,
        remoteLoading:false,
        formVisible: false,
        selectChange:false,
        formLabelWidth: '120px',
        inputForm:{
          id:this.$route.query.id,
          name:'',
          accountIdList:'',
          remarks:'',
          newDepotIdList:"",
          pageIds:''
        },
        selects:[],
        rules: {
          name: [{ required: true, message: this.$t('chainForm.prerequisiteMessage')}]
        }
      }
    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            this.inputForm.newDepotIdList = this.selects;
            axios.post('/api/ws/future/basic/chain/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(this.isCreate){
                form.resetFields();
                this.submitDisabled = false;
              } else {
                this.$router.push({name:'chainList',query:util.getQuery("chainList")})
              }
            });
          }else{
            this.submitDisabled = false;
          }
        })
      }/*,getDepots(){
        this.pageLoading=true;
        this.pageUpdated = false;
        this.formLabel.type.value = this.formProperty.types[this.formData.type];
        this.formLabel.officeId.value = util.getLabel(this.offices, this.formData.officeId);
        this.formLabel.adPricesystemId.value = util.getLabel(this.formProperty.adPricesystems, this.formData.adPricesystemId);
        axios.get('/api/ws/future/basic/depot',{params:this.formData}).then((response) => {
          this.page = response.data;
          this.content = response.data.content;
          this.inputForm.pageIds=util.getIdList(this.content);
          this.pageLoading = false;
          this.pageUpdated = true;
        })
      },remoteAccount(query) {
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/account/search',{params:{key:query}}).then((response)=>{
            this.accounts=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.leaders = [];
        }
      },pageChange(pageNumber,pageSize) {
        this.formData.page = pageNumber;
        this.formData.size = pageSize;
        this.getDepots();
        this.selectChange = false;
      },sortChange(column) {
        this.formData.order=util.getOrder(column);
        this.formData.page=0;
        this.getDepots();
        this.selectChange = false;
      },remoteOffice(query){
        if (query !== '') {
          this.remoteLoading = true;
          axios.get('/api/hr/office/search',{params:{name:query}}).then((response)=>{
            this.offices=response.data;
            this.remoteLoading = false;
          })
        } else {
          this.offices = [];
        }
      },search() {
        this.formVisible = false;
        this.getDepots();
      }*/,selectionChange(selection){
        this.selects=new Array();
        for(var key in selection){
          this.selects.push(selection[key].id)
        }
        if(this.selectChange){
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.newDepotIdList = this.selects;
              axios.post('/api/ws/future/basic/chain/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=>{
                this.$message(response.data.message);
                this.selectChange = false;
                this.getDepots();
              })
            }
          })
        }
      }
    }/*,created(){
      this.pageHeight = window.outerHeight -350;
      axios.get('/api/ws/future/basic/depot/getQuery').then((response) =>{
        this.formProperty=response.data;
      this.getDepots();
    });
      if(!this.isCreate){
        axios.get('/api/ws/future/basic/chain/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
          util.copyValue(response.data,this.inputForm);
        if(response.data.accountList!=null&&response.data.accountList.length>0){
          this.accounts=response.data.accountList;
          this.inputForm.accountIdList=util.getIdList(this.accounts);
        }
        })
      }
    }*/
  }
</script>
