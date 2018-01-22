<template>
  <el-transfer :value="checked"
               :titles="titles"
               :data="transferData"
               @change="handleTransChange"></el-transfer>
</template>

<script>
  import XHR from "framework/xhr/xhr"
  export default {
    props:["transferData","checked","titles"],
    methods:{
      handleTransChange(value){
        this.$store.dispatch("transferChange",{
          data:value
        })
      },
      resetTransferData(data){
        if(data.length>0){
          data.map((item)=>{
            item.key = parseInt(item.id);
            item.label = item.desc;
          })
        }
        this.data = data;
      }
    },
    computed:{

    },
    //生命周期函数
    beforeCreate(){

    },
    created(){//可以获取数据
      console.log(this.transferData)
    }
  };
</script>

<style type="text/css" lang="scss">
 @import "framework/scss/transfer"
</style>

