<template>
  <div>
    <head-tab active="adPricesystemForm"></head-tab>
    <div >
      <el-form :model="inputForm" :inline="true" ref="inputForm" :rules="rules" class="form input-form">
        <el-form-item :label="$t('adPricesystemForm.name')" prop="name">
          <el-input v-model="inputForm.name"></el-input>
        </el-form-item>
        <el-form-item :label="$t('adPricesystemForm.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  :disabled="submitDisabled" @click="formSubmit()">{{$t('adPricesystemForm.save')}}</el-button>
          <el-button type="primary" @click="formVisible = true" icon="search">{{$t('adPricesystemForm.filter')}}</el-button>
          <search-tag  :formData="formData" :formLabel="formLabel"></search-tag>
        </el-form-item>
      </el-form>
      <el-dialog :title="$t('adPricesystemForm.filter')" v-model="formVisible" size="small" class="search-form">
        <el-form :model="formData">
          <el-row :gutter="4">
            <el-col :span="12">
              <el-form-item :label="formLabel.name.label" :label-width="formLabelWidth">
                <el-input v-model="formData.name" auto-complete="off" :placeholder="$t('adPricesystemForm.likeSearch')"></el-input>
              </el-form-item>
              <el-form-item :label="formLabel.type.label" :label-width="formLabelWidth">
                <el-select v-model="formData.type" filterable clearable :placeholder="$t('adPricesystemForm.inputKey')">
                  <el-option v-for="(value,key) in formProperty.types" :key="key" :label="value" :value="key"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.areaType.label" :label-width="formLabelWidth">
                <el-select v-model="formData.areaType" filterable clearable :placeholder="$t('adPricesystemForm.inputKey')">
                  <el-option v-for="areaType in formProperty.areaTypes" :key="areaType.name" :label="areaType.name" :value="areaType.name"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="formLabel.officeId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.officeId" filterable remote :placeholder="$t('adPricesystemForm.inputWord')" :remote-method="remoteOffice" :loading="remoteLoading" :clearable=true>
                  <el-option v-for="office in offices" :key="office.id" :label="office.name" :value="office.id"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item :label="formLabel.adPricesystemId.label" :label-width="formLabelWidth">
                <el-select v-model="formData.adPricesystemId" filterable clearable :placeholder="$t('adPricesystemForm.inputKey')">
                  <el-option v-for="adPricesystem in formProperty.adPricesystems" :key="adPricesystem.id" :label="adPricesystem.name" :value="adPricesystem.id"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            </el-row >
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="search()">{{$t('adPricesystemForm.sure')}}</el-button>
        </div>
      </el-dialog>
    </div>
    <el-table :data="content" :height="pageHeight" ref='table' style="margin-top:5px;" v-loading="pageLoading" reserve-selection="true" @selection-change="selectionChange"  :element-loading-text="$t('adPricesystemForm.loading')" @sort-change="sortChange" stripe border>
      <el-table-column type="selection" width="55" ></el-table-column>
      <el-table-column  prop="name" :label="$t('adPricesystemForm.name')" width="250"></el-table-column>
      <el-table-column prop="adPricesystemId" :label="$t('adPricesystemForm.isAdPricesystem')" >
        <template scope="scope">
          <el-tag :type="scope.row.adPricesystemId===inputForm.id ? 'primary' : 'danger'">{{scope.row.adPricesystemId===inputForm.id | bool2str}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column  prop="office.name" :label="$t('adPricesystemForm.officeName')"></el-table-column>
      <el-table-column prop="area.name" :label="$t('adPricesystemForm.areaName')"></el-table-column>
      <el-table-column prop="typeLabel" :label="$t('adPricesystemForm.typeLabel')"></el-table-column>
      <el-table-column prop="chain.name" :label="$t('adPricesystemForm.chainName')"></el-table-column>
      <el-table-column prop="areaType" :label="$t('adPricesystemForm.areaType')"></el-table-column>
    </el-table>
    <pageable :page="page" v-on:pageChange="pageChange"></pageable>
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
                if(v.adPricesystemId ===this.inputForm.id){
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
            formData:{
              page:0,
              size:25,
              order:'adPricesystemId:DESC',
              name:'',
              type:'',
              areaType:'',
              officeId:'',
              adPricesystemId:'',
            },formLabel:{
              name:{label:this.$t('adPricesystemForm.name')},
              type:{label:this.$t('adPricesystemForm.typeLabel'),value:""},
              areaType:{label:this.$t('adPricesystemForm.areaType')},
              officeId:{label:this.$t('adPricesystemForm.office'),value:""},
              adPricesystemId:{label:this.$t('adPricesystemForm.adPricesystem'),value:""},
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
              remarks:'',
              newDepotIds:"",
              pageIds:''
            },
            selects:[],
            rules: {
              name: [{ required: true, message: this.$t('adPricesystemForm.prerequisiteMessage')}]
            }
          }
      },
      methods:{
        formSubmit(){
          this.submitDisabled = true;
          var form = this.$refs["inputForm"];
          form.validate((valid) => {
            if (valid) {
              this.inputForm.newDepotIds = this.selects;
              axios.post('/api/ws/future/basic/adPricesystem/save',qs.stringify(this.inputForm)).then((response)=> {
                this.$message(response.data.message);
                if(this.isCreate){
                  form.resetFields();
                  this.submitDisabled = false;
                } else {
                  this.$router.push({name:'adPricesystemList',query:util.getQuery("adPricesystemList")})
                }
              }).catch(function () {
                this.submitDisabled = false;
              });
            }else{
              this.submitDisabled = false;
            }
          })
        },getDepots(){
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
        },selectionChange(selection){
          this.selects=new Array();
          for(var key in selection){
            this.selects.push(selection[key].id)
          }
          if(this.selectChange){
            var form = this.$refs["inputForm"];
            form.validate((valid) => {
              if (valid) {
                this.inputForm.newDepotIds = this.selects;
                axios.post('/api/ws/future/basic/adPricesystem/save',qs.stringify(this.inputForm, {allowDots:true})).then((response)=>{
                  this.$message(response.data.message);
                  this.selectChange = false;
                  this.getDepots();
                })
              }
            })
          }
        }
      },created(){
        this.pageHeight = window.outerHeight -350;

        axios.get('/api/ws/future/basic/depot/getQuery').then((response) =>{
          this.formProperty=response.data;
        this.getDepots();
        });
        if(!this.isCreate){
          axios.get('/api/ws/future/basic/adPricesystem/findOne',{params: {id:this.$route.query.id}}).then((response)=>{
            util.copyValue(response.data,this.inputForm);
          })
        }
      }
    }
</script>
