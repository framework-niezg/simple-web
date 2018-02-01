<template>
  <el-card class="box-card scrollbar">
    <template slot="header">
      <slot :name="options.caption"></slot>
    </template>
    <el-table  ref="multipleTable" :data="tableData"
                 :highlight-current-row="options.highlight"
                 @selection-change="selectionChange"
                 @cell-click="tableCellClick"
                 @select-all="selectAll"
                 @current-change="handleRowChange"
      >
        <el-table-column :type="options.type" width="50"></el-table-column>
        <el-table-column v-for="item in options.th"
                         :property="item.property"
                         :label="item.label">
        </el-table-column>
        <el-table-column v-if="options.deals && options.deals.max > 0"
                         :label="options.deals.label || '操作'"
                         align="left"
                         :width="options.deals.max*60">
          <template scope="scope" >
            <template v-for="deal in scope.row.deals" >
              <label v-if="deal.type == 'label'" class="deal-label">
                {{deal.text}}
              </label>
              <el-button v-else
                         :type="deal.type"
                         size="mini"
                         @click.native="onClickHandler(scope.$index, scope.row,deal.event)"
                         :name="deal.event">
                {{deal.text}}
              </el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    <el-row v-if="pageData" type="flex" align="middle" :gutter="20" class="page-nation">
      <el-col  :span="24" style="text-align: right">
        <el-pagination
          @current-change="handleCurrentPage"
          :current-page="pageData.pageIndex"
          layout="prev, pager, next"
          :page-size="pageData.limit"
          :total="pageData.total">
        </el-pagination>
      </el-col>
    </el-row>
    </el-card>
</template>

<style>
  .el-table td, .el-table th {
    padding: 4px 0;
  }
  .page-nation{
    padding:10px 10px 0 10px;
  }
  .el-pager li {
    border: 1px solid #ccc;
    border-radius: 5px;
    margin: 0 3px;
    min-width: 25px;
    color: #5a5e66;
  }
  .el-pager li.active+li {
    border: 1px solid #ccc;
  }
  .el-pagination .btn-prev,.el-pagination .btn-next {
    border: 1px solid #ccc;
    border-radius: 5px;
    min-width: 25px;
  }
  .deal-label{
    font-size: 12px;
    color: #5a5e66;
  }
</style>

<script>
  import { mapGetters, mapActions } from 'vuex'
  import Util from "framework/util/util"
  export default {
    data() {
      return {
        currentRow: 0
      }
    },
    computed: {
      ...mapGetters({
        tableData: "tableData",
        pageData: "pageData",
        options:"options",
        result:"result",
      }),
    },
    methods: {
      //表格事件
      tableCellClick: function (row,column,cell,event){
        if (column.label == '操作') {
          //点击操作栏内的功能按钮
        } else if(column.type == 'selection'){
          row.$info = !row.$selected;//行选中转换
        }else{
          //单击表格切换选中状态
          row.$selected = !row.$selected;
          row.$info = row.$selected;
          //更新数据模型
          //this.toggleSelection(row);
          this.setSelectionData(row)
        }
      },
      toggleSelection(row){
        if (row) {
          this.$refs.multipleTable.clearSelection();
          this.$refs.multipleTable.toggleRowSelection(row);
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
      //用户单击行
      setSelectionData(row){
       /* //this.setCurrent(row);
        this.$store.dispatch('tableHandler',"detail");
        this.$store.dispatch('trHandler',{data:row});
        this.$store.dispatch('getCurrentData',row);
        //this.$emit('detail',row,0)*/
      },
      //用户点击单个checkbox
      selectionChange(val){
        var arr = [];
        val.forEach((item)=>{
          arr.push(item.id);
        });
        this.$store.dispatch(this.currentTable+'/tableSelected',arr);
      },
      //用户点击全选
      selectAll(selection){
        this.selectionChange(selection)
      },
      //用户点击操作栏
      onClickHandler(index,row,type){
        switch(type){
          case"change":
            this.handleChange(row);
            break;
          case"reset":
            this.handleReset(row);
            break;
          case"delete":
            this.handleDelete(row);
            break;
          default:
            break;
        }
      },

      handleChange(row){
        //this.toggleSelection(row);
        this.$store.dispatch(this.currentTable+'/getCurrentData',row);
      },
      //行点击
      trHandler(row){
        this.$store.dispatch(this.currentTable+'/trHandler',row);
      },
      //用户点击重置
      handleReset(row){
        //this.message('info',"功能维护中");
        this.$store.dispatch(this.currentTable+'/resetPass',row);
      },
      handleRowChange(val,old){
        console.log(val,old);
      },
      //单体删除
      handleDelete(row){
        this.selectItems=[];
        this.selectItems.push(row.id);
        this.handleConfirm(
          '此操作将永久删除该数据, 是否继续?',
          "提示",
          {confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'},
          this.onDelete);
      },
      handleRemove(){
        var tableData = this.tableData;
        this.selectItems.forEach(function (id) {
          tableData.forEach(function (data) {
            if(id == data.id){
              tableData.splice(tableData.indexOf(data),1)
            }
          })
        });
        this.selectItems = [];
      },
      handleConfirm(message="删除",title="提示",button={confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'},callBack=this.onDelete){
        this.$confirm(message,title,button,callBack).then(() => {
          callBack();
        }).catch(() => {
          this.message('info',"已取消删除");
        });
      },
      handleCurrentPage(pageIndex){
        console.log(pageIndex)
        this.$store.dispatch(this.currentTable + '/pageChange',pageIndex);
      },
      onDelete(){
        this.$store.dispatch(this.currentTable + '/deleteItem',this.selectItems);
      },
      success(type,data){
        switch(type){
          case"delete":
          case"add":
          case"change":
            this.message('success',data.msg);
            break;
          case"reset":
            this.message('success',"密码重置成功，新密码为" + data.data);
            break;
          default:
            break;
        }
        this.$store.dispatch(this.currentTable + '/getItems');
        this.$store.dispatch(this.currentTable + '/dialogClose');

      },
      error(data){
        this.message('warning',data.msg);
      },
      message(type,msg){
        this.$message({
          type: type,
          message:msg,
          duration:"5000"
        });
      }
    },
    watch:{
      result:{
        handler:function({type,data}){
          data.success ? this.success(type,data) : this.error(data);
        },
        deep:true//对象内部的属性监听，也叫深度监听
      }
    },
    beforeCreate(){
      console.log()
    },
    created () {
      this.currentTable = Util.getItem("currentVue").vue;
      this.$store.dispatch(this.currentTable + '/getItems');
    }
  }
</script>
<!--table 组件 api-->

