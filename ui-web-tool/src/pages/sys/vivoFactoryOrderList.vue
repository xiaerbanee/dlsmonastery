<template>
  <div>
    <head-tab active="vivoFactoryOrderList"></head-tab>
    <div>
      <el-form :inline="true" :model="inputForm" ref="inputForm"  :rules="rules"  class="form input-form">
        <el-form-item label="账户">
          <el-select v-model="inputForm.factoryCode" filterable clearable placeholder="请选择账户">
            <el-option v-for="(key,value) in inputForm.extra.factoryCodeMap" :key="value" :label="value+'_'+key" :value="value"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="inputForm.factoryPassword" type="password" placeholder="请选择账户"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="formSubmit">登陆</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
        <ul v-if="showList&&companyName=='JXVIVO'" style="color: #20A0FF;text-decoration: underline" >
          <li><a @click="itemAction('GCDD')" href="#">工厂订单</a></li>
          <li><a @click="itemAction('YDDHD')" href="#">一代调货单</a></li>
          <li><a @click="itemAction('GCFHD')" href="#">工厂发货单</a></li>
          <li><a @click="itemAction('GCFHDPJ')" href="#">工厂发货单(配件)</a></li>
          <li><a @click="itemAction('YDCGSQPJ')" href="#">一代采购申请(配件)</a></li>
        </ul>
        <ul v-else-if="showList&&companyName=='IDVIVO'" style="color: #20A0FF;text-decoration: underline" >
          <li><a @click="itemAction('IDGCDD')" href="#">工厂订单</a></li>
          <li><a @click="itemAction('IDYDDHD')" href="#">一代调货单</a></li>
          <li><a @click="itemAction('IDGCFHD')" href="#">工厂发货单</a></li>
          <li><a @click="itemAction('IDGCFHDPJ')" href="#">工厂发货单(配件)</a></li>
          <li><a @click="itemAction('IDJCJXX')" href="#">寄存机信息</a></li>
          <li><a @click="itemAction('IDYDCGSQPJ')" href="#">一代采购申请(配件)</a></li>
          <li><a @click="itemAction('IDYDCGDDPJ')" href="#">一代采购订单(配件)</a></li>
          <li><a @click="itemAction('IDGCYFHDPJ')" href="#">工厂月发货单(配件)</a></li>
        </ul>
    </div>
  </div>
</template>
<script>
  export default {
    data(){
      return this.getData()
    },methods: {
      getData() {
        return {
          companyName:JSON.parse(window.localStorage.getItem("account")).companyName,
          showList:false,
          inputForm:{
            extra:{}
          },
          rules: {},
        };
      },formSubmit(){
        var form = this.$refs["inputForm"];
        form.validate((valid) => {
          axios.post('api/global/tool/factory/vivo/factoryOrder?', qs.stringify(util.deleteExtra(this.inputForm))).then((response)=>{
            this.inputForm = response.data;
            this.showList = true;
          })
        })
      },itemAction:function(action){
        if(action == "GCDD"){
          window.open("http://esaleweb.vivo.cn:8888/PLANTESALEWEB/Product/Index?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "YDDHD"){
          window.open("http://esaleweb.vivo.cn:8888/PLANTESALEWEB/Transfer/Index?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "GCFHD"){
          window.open("http://esaleweb.vivo.cn:8888/PLANTESALEWEB/Product/Send?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "GCFHDPJ"){
          window.open("http://esaleweb.vivo.cn:8888/PLANTESALEWEB/Product/Send?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "YDCGSQPJ"){
          window.open("http://esaleweb.vivo.cn:8888/PLANTESALEWEB/Accessory/Index?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "IDGCDD"){
          window.open("http://id-transfer.vivo.xyz:666/WebUI/Product/Index?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "IDYDDHD"){
          window.open("http://id-transfer.vivo.xyz:666/WebUI/Transfer/Index?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "IDGCFHD"){
          window.open("http://id-transfer.vivo.xyz:666/WebUI/Product/Send?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "IDGCFHDPJ"){
          window.open("http://id-transfer.vivo.xyz:666/WebUI/Product/Send?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "IDJCJXX"){
          window.open("http://id-transfer.vivo.xyz:666/WebUI/DLSJCJ.aspx?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "IDYDCGSQPJ"){
          window.open("http://id-transfer.vivo.xyz:666/WebUI/Accessory/Index?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "IDYDCGDDPJ"){
          window.open("http://id-transfer.vivo.xyz:666/WebUI/StockRequMonthView.aspx?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else if(action == "IDGCYFHDPJ"){
          window.open("http://id-transfer.vivo.xyz:666/WebUI/FacMonthInvoice.aspx?NESAD="+this.inputForm.code+"&NESBU="+this.inputForm.password);
        }else{
          this.$message({message:"错误",type:'error'})
        }
      }
    },created(){
      axios.get('api/global/tool/factory/vivo/factoryOrder').then((response)=>{
        this.inputForm = response.data;
      })
    }
  }
</script>
