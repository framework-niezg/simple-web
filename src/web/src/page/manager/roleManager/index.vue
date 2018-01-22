<template>
  <div class="container">
    <br/>
    <TableVue v-bind:currentTable="currentTable">
      <template slot="caption">
        <div class="clearfix">
          <span>角色列表</span>
          <el-button v-for="deal in options.tops"
                     :type="deal.type"
                     size="mini"
                     style="float: right"
                     @click.native="onTopClickHandler(deal.event)"
                     :name="deal.event">
            {{deal.text}}
          </el-button>
        </div>
      </template>
    </TableVue>
    <br/>
    <DialogVue>
      <template slot="addRole">
        <AddVue></AddVue>
      </template>
      <template slot="changeRole">
        <ChangeVue></ChangeVue>
      </template>
    </DialogVue>
  </div>
</template>
<script>
  import "src/basic"
  import { mapGetters, mapActions } from 'vuex'
  import XHR from "framework/xhr/xhr"
  import Util from "framework/util/util"
  import TableVue from 'framework/components/TableVue'
  import DialogVue from 'framework/components/dialogVue'
  import AddVue from './children/add'
  import ChangeVue from './children/change'

  export default {
    name: 'roleManager',
    data(){
      return{
        currentTable:"roleManager",
      }
    },
    components: {
      TableVue,
      DialogVue,
      AddVue,
      ChangeVue
    },
    computed: {
      ...mapGetters({
        setState: "setState",
        options:"options",
        selectItems:"selectItems"
      }),
    },
    methods:{
      onTopClickHandler(type){
        switch(type){
          case"add":
            this.handleAdd();
            break;
          case"deletes":
            this.handleDeletes();
            break;
          default:
            break;
        }
      },
      handleAdd(){
        this.$store.dispatch(this.currentTable+'/topHandler',"add");
      },
      //批量删除
      handleDeletes(){
        if(this.selectItems.length==0){
          this.$message({
            showClose: false,
            message: '请您务必选择一条记录',
            type: 'warning'
          });
        }else{
          this.handleConfirm(
            '此操作将永久删除该数据, 是否继续?',
            "提示",
            {confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'},
            this.onDelete);
        }
      },
    },
    beforeCreate(){
      this.$store.dispatch("reSetSate","roleManager");
      Util.setItem("currentVue",{vue:"roleManager"});
    },
    create(){
      console.log(2)
    }
  }
</script>

<style lang="sass" type="text/css">
  @import "static/scss/container";
</style>
