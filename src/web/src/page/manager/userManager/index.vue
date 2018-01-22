<template>
  <div class="container">
    <SearchVue v-bind:current="currentTable"></SearchVue>
    <TableVue v-bind:currentTable="currentTable">
      <template slot="caption">
        <div class="clearfix">
          <span>用户列表</span>
          <el-button v-for="deal in options.tops"
                     :type="deal.type"
                     size="mini"
                     style="float: right;margin-left:5px"
                     @click.native="onTopClickHandler(deal.event)"
                     :name="deal.event">
            {{deal.text}}
          </el-button>
        </div>
      </template>
    </TableVue>
    <br/>
    <DialogVue>
      <template slot="addUser">
        <AddVue></AddVue>
      </template>
      <template slot="changeUser">
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
  import SearchVue from 'framework/components/searchVue'
  import AddVue from './children/add'
  import ChangeVue from './children/change'

  export default {
    name: 'userManager',
    data(){
      return{
        currentTable:"userManager"
      }
    },
    components: {
      TableVue,
      DialogVue,
      SearchVue,
      AddVue,
      ChangeVue
    },
    computed: {
      ...mapGetters({
        setState: "setState",
        options:"options"
      }),
      ...mapGetters("userManager",{
        tableSelected:"tableSelected"
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
        if(!this.tableSelected||this.tableSelected.length==0){
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
      handleConfirm(message="删除",title="提示",button={confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'},callBack=this.onDelete){
        this.$confirm(message,title,button,callBack).then(() => {
          callBack();
        }).catch(() => {
          this.message('info',"已取消删除");
        });
      },
      onDelete(){
        let msg = this.tableSelected.join(",");
        this.$notify({
          title: '提示',
          message: '您要删除的列是id='+ msg + "我们抱歉的通知您，当前不支持此操作",
          duration: 0
        });
        //this.$store.dispatch(this.currentTable + "/deletes")
      }
    },
    beforeCreate(){
      this.$store.dispatch("reSetSate","userManager");
      Util.setItem("currentVue",{vue:"userManager"});
    },
    create(){
      console.log(2)
    }
  }
</script>

<style lang="sass" type="text/css">
  @import "static/scss/container";
</style>
