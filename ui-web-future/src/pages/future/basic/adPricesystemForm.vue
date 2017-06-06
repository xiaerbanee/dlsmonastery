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
            <el-form-item :label="$t('adPricesystemForm.officeName')" prop="officeName">
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
        isInit:false,
        isCreate: this.$route.query.id == null,
        submitDisabled: false,
        inputForm: {},
        submitData: {
          id: this.$route.query.id,
          name: '',
          remarks: '',
          officeIdList:[],
        },
        rules: {
          name: [{required: true, message: this.$t('officeForm.prerequisiteMessage')}],
        },
        remoteLoading: false,
        defaultProps: {
          label: 'label',
          children: 'children'
        }
      };
    },
      formSubmit(){
        var that = this;
        this.submitDisabled = true;
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            util.copyValue(this.inputForm, this.submitData);
            axios.post('/api/ws/future/basic/adPricesystem/save', qs.stringify(this.submitData, {allowDots:true})).then((response) => {
              if(response.data.success){
                this.$message(response.data.message);
                Object.assign(this.$data, this.getData());
                if (!this.isCreate) {
                  this.$router.push({name: 'adPricesystemList', query: util.getQuery("adPricesystemList")})
                }
              }else {
                this.$message({
                  showClose: true,
                  message: response.data.message,
                  type: 'error'
                });
              }
            }).catch(function () {
              that.submitDisabled = false;
            });
          } else {
            this.submitDisabled = false;
          }
        })
      }
    },activated () {
        if(!this.$route.query.headClick || !this.isInit) {
        axios.get('/api/ws/future/basic/adPricesystem/findOne', {params: {id: this.$route.query.id}}).then((response) => {
          this.inputForm = response.data;
      });
      }
      this.isInit = true;
    }
  }
</script>
