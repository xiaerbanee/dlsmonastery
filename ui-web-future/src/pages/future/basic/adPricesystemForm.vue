<template>
  <div>
    <head-tab active="adPricesystemForm"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px" class="form input-form">
            <el-form-item :label="$t('adPricesystemForm.name')" prop="name">
              <el-input v-model="inputForm.name"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adPricesystemForm.remarks')" prop="remarks">
              <el-input v-model="inputForm.remarks"></el-input>
            </el-form-item>
            <el-form-item :label="$t('adPricesystemForm.officeName')" prop="officeIdList">
              <office-select v-model="inputForm.officeIdList" multiple="multiple"></office-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adPricesystemForm.save')}}
              </el-button>
            </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
  import officeSelect from 'components/basic/office-select'
  export default{
    components:{
      officeSelect
    },
    data(){
      return this.getData()
    },
    methods:{
      getData() {
      return {
        isCreate: this.$route.query.id == null,
        submitDisabled: false,
        inputForm: {
          extra:{}
        },
        rules: {
          name: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
          officeIdList: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}]
        },
        remoteLoading: false,
        defaultProps: {
          label: 'label',
          children: 'children'
        }
      };
    },
      formSubmit(){
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            axios.post('/api/ws/future/basic/adPricesystem/save', qs.stringify(util.deleteExtra(this.inputForm), {allowDots:true})).then((response) => {
              if(response.data.success){
                this.$message(response.data.message);
                if (this.isCreate) {
                  Object.assign(this.$data,this.getData());
                  this.initPage();
                }else {
                  this.submitDisabled = false ;
                  this.$router.push({name: 'adPricesystemList', query: util.getQuery("adPricesystemList")})
                }
              }else {
                this.$message({
                  showClose: true,
                  message: response.data.message,
                  type: 'error'
                });
              }
            }).catch(()=> {
              this.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      },initPage(){
        axios.get('/api/ws/future/basic/adPricesystem/getForm').then((response)=> {
          this.inputForm = response.data;
          if(!this.isCreate){
            axios.get('/api/ws/future/basic/adPricesystem/findOne', {params: {id: this.$route.query.id}}).then((response) => {
              util.copyValue(response.data,this.inputForm);
            });
          }
        });
      }
    },created () {
      this.initPage();
    }
  }
</script>
