<template>
  <el-transfer v-model="checked"
               :titles="titles"
               :data="data"
               @change="handleTransChange"></el-transfer>
</template>

<script>
  import XHR from "framework/xhr/xhr"
  export default {
    props:["transferType","checks"],
    computed:{
      checked:{
        get:function(){
          return this.resetChecked();
        },
        set:function(v){
          this.checks = v;
        }
      }
    },
    methods:{
      getDatas(){
        let _this =this;
        XHR.get({
          url:this.transferType
        },function(data){
          console.log(data);
          data.success ? _this.resetTransferData(data.data):""
        })
      },
      handleTransChange(value){
        this.$emit('transfer',value);
      },
      resetChecked(){
        let a = [];
        if(this.checks.length>0){
          this.checks.map((item)=>{
            a.push(parseInt(item.id))
          })
        }
       return a;
      },
      resetTransferData(data){
        if(data.length>0){
          data.map((item)=>{
            item.key = parseInt(item.id);
            item.label = item.desc;
            item.disabled = false;
          })
        }
        this.data = data;
      }
    },
    data() {
      return {
        titles:["角色列表","已选择角色"],
        data:[]
      };
    },
    //生命周期函数
    beforeCreate(){

    },
    created(){//可以获取数据
       this.getDatas()
    },
    beforeMount(){

    },
    mounted(){

    },
    beforeUpdate(){

    },
    updated() {

    },
    beforeDestroy() {

    },
    destroyed() {

    }
  };
</script>

<style>
  .transfer{

  }
  .el-transfer-panel__body{
    height:195px;
    max-height:195px;
    overflow: auto;
  }
  .el-transfer-panel__list{
    height:192px;
  }
</style>

