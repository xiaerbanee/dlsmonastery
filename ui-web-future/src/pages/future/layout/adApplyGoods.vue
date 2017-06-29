<template>
  <div>
    <head-tab active="adApplyGoods"></head-tab>
    <div>
      <el-form :model="inputForm" ref="inputForm" :rules="rules" label-width="120px"  class="form input-form">
        <el-form-item :label="$t('adApplyGoods.productName')" prop="productId">
          <product-select v-model="inputForm.productId" outGroupName="POP"></product-select>
        </el-form-item>
        <el-form-item :label="$t('adApplyGoods.remarks')" prop="remarks">
          <el-input v-model="inputForm.remarks" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :disabled="submitDisabled" @click="formSubmit()">{{$t('adApplyGoods.save')}}</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" style="margin-bottom:20px;float:right">
        <span>{{$t('adApplyGoods.search')}}</span>
        <el-input v-model="depotName" @change="searchDetail" :placeholder="$t('adApplyGoods.inputKey')" style="width:200px;margin-right:10px"></el-input>
      </el-row>
      <el-table :data="filterShop"  stripe border>
        <el-table-column prop="name" sortable :label="$t('adApplyGoods.shopName')"></el-table-column>
        <el-table-column prop="applyQty" :label="$t('adApplyGoods.applyQty')+'('+totalApplyQty+')'">
          <template scope="scope">
            <el-input v-model.number="scope.row.applyQty" @input = "getTotalApplyQty()"></el-input>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
  import productSelect from 'components/future/product-select';
  export default{
    components:{
      productSelect
    },
    data(){
      return this.getData();
    },
    methods:{
        getData(){
          return{
            submitDisabled:false,
            depotName:'',
            filterShop:[],
            products:{},
            inputForm:{
                extra:{}
            },
            rules: {
              productId: [{ required: true, message: this.$t('adApplyGoods.prerequisiteMessage')}]
            },
            remoteLoading:false,
            totalApplyQty:0,
          }
        },
      formSubmit(){

        let validateMsg = this.customValidate();
        if(util.isNotBlank(validateMsg)){
          this.$alert(validateMsg);
          return;
        }

        this.submitDisabled = true;
        let form = this.$refs["inputForm"];
        form.validate((valid) => {
          if (valid) {
            let tempList = new Array();
            for(let index of this.filterShop){
              if(util.isNotBlank(index.applyQty)){
                tempList.push(index)
               }
            }
           let submitData = util.deleteExtra(this.inputForm);
            submitData.depotAdApplyForms = tempList;
            axios.post('/api/ws/future/layout/adApply/goodsSave',qs.stringify(submitData,{allowDots:true})).then((response)=> {
              this.$message(response.data.message);
              if(response.data.success){
                Object.assign(this.$data, this.getData());
                this.initPage();
              }
            }).catch(()=> {
              this.submitDisabled = false;
            });
          }else{
            this.submitDisabled = false;
          }
        })
      },customValidate(){
        let totalQty = 0;
        for(let index of this.filterShop){
          if(util.isBlank(index.applyQty)){
            continue;
          }

          if(!Number.isInteger(index.applyQty) || index.applyQty < 0){
            return '门店：'+index.name+'的订货数不是一个大于等于0的整数';
          }

          totalQty += index.applyQty;
        }
        if(totalQty<=0){
          return "总订货数要大于0";
        }
        return null;
      },searchDetail(){
          let val = this.depotName;
          if(!val){
              this.filterShop = this.inputForm.depotAdApplyForms;
              return;
          }
          let tempList=[];
          for(let index of this.inputForm.depotAdApplyForms){
            if(util.isNotBlank(index.applyQty)){
              tempList.push(index);
            }
          }
          for(let index of this.inputForm.depotAdApplyForms){
            if(util.contains(index.name,val)&&util.isBlank(index.applyQty)){
              tempList.push(index);
            }
          }
          this.filterShop = tempList;
      },getTotalApplyQty(){
          let tempTotalApplyQty = 0;
          for(let index of this.filterShop){
              if(util.isNotBlank(index.applyQty)&&Number.isInteger(index.applyQty)){
                  tempTotalApplyQty += index.applyQty;
              }
          }
          this.totalApplyQty = tempTotalApplyQty;
      },initPage(){
        this.pageHeight = window.outerHeight -320;
        axios.get('api/ws/future/layout/adApply/getAdApplyGoodsList').then((response) =>{
          this.inputForm = response.data;
          this.searchDetail();
        });
      }
    },created () {
        this.initPage();
    }
  }
</script>
