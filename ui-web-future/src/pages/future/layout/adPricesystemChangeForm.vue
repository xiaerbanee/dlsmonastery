<template>
  <div>
    <head-tab active="adPricesystemChangeForm"></head-tab>
    <el-row>
      <el-button type="primary" @click="formSubmit()" icon="check">{{$t('adPricesystemChangeForm.save')}}</el-button>
      <el-button type="primary" @click="formVisible = true" icon="search">{{$t('adPricesystemChangeForm.filter')}}</el-button>
      <search-tag  :submitData="submitData" :formLabel="formLabel"></search-tag>
    </el-row>
    <el-dialog :title="$t('adPricesystemChangeForm.filter')"  v-model="formVisible"  size="tiny" class="search-form">
      <el-form :model="formData">
        <el-form-item :label="formLabel.productName.label">
          <product-select v-model="formData.productId"></product-select>
        </el-form-item>
        <!--<el-form-item :label="formLabel.productCode.label" :label-width="formLabelWidth">
          <el-input v-model="formData.productCode" auto-complete="off" :placeholder="$t('adPricesystemChangeForm..likeSearch')"></el-input>
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="search()">{{$t('adPricesystemChangeForm.sure')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import productSelect from 'components/future/product-select';

  export default{
    components:{productSelect},
    data(){
      return{
        formData:{},
        submitData:{
            productId:'',
        },
        formLabel:{
          productName:{label:this.$t('adPricesystemChangeForm.productName')},
          productCode:{label:this.$t('adPricesystemChangeForm.productCode')}
        },
        inputForm:{
            data:''
        },
        rules:{},
        productTypes:[],
        formVisible: false,
        submitDisabled:false,
      }

    },
    methods:{
      formSubmit(){
        this.submitDisabled = true;
        this.inputForm.data = new Array();
        let list = this.table.getData();
        for(var item in list){
          if(!this.table.isEmptyRow(item)){
            this.inputForm.data.push(list[item]);
           }
        }
       this.inputForm.data = JSON.stringify(this.inputForm.data);
       axios.post('/api/ws/future/layout/adPricesystemChange/save',qs.stringify({data:this.inputForm.data},{allowDots:true})).then((response)=> {
          this.$message(response.data.message);
          this.submitDisabled = false;
        }).catch(function () {
         this.submitDisabled = false;
       });
      },search() {
        this.formVisible = false;
        this.getData();
      },getData(){
          util.copyValue(this.formData,this.submitData);
        axios.get('/api/ws/future/layout/adPricesystemChange/findFilter',{params:this.submitData}).then((response)=>{
          this.settings.data = response.data;
          this.table.loadData(this.settings.data);
        })
      }
    }, created(){
      axios.get('/api/ws/future/layout/adPricesystemChange/getQuery').then((response)=>{
        this.formData=response.data;
        util.copyValue(this.$route.query,this.formData);
        this.getData();
      })
    }
  }
</script>


