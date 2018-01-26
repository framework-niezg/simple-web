import * as types from '../mutation-types'
import Util from 'framework/util/util'
import XHR from 'framework/xhr/xhr'
import UserList from "src/collection/userList"

const state = {
  name:"userManager",
  key:"USER_",
  param: {
    name: "",
    query: ["name"],
    queryString: [],
    pageIndex: "1",
    limit: "10",
    orderBy: ""
  },
  options:{
    transferTitles:["角色列表","已选角色"],
    search:[
      {text:"用户名称",id:"userName",name:"name",type:"input"},
      {text:"查询",id:"userSearch",name:"search",type:"button",event:"submit"},
    ],
    table: {
      type: "selection",
      isPageNation: true,
      highlight: true,
      caption:"caption",
      th:[
        {property:"name",label:"用户名称"},
        {property:"account",label:"用户账号"}
      ],
      deals:{
        max:3
      },
      tops:[
        {text:"新增",id:"userAdd",name:"userAdd",type:"primary",event:"add"},
        {text:"批量删除",id:"userDeletes",name:"userDeletes",type:"info",event:"deletes"}
      ]
    },
  },
  tableData: [],
  pageData:{},
  currentDialog:{},
  currentUser: {},
  url: {
    del: "deleteUser",
    change: "changeUser",
    search: "users",
    transfer: "roles",
    has: "roleHasMenu",
    add:"addUser",
    reset:"resetUserPass"
  },
  actions:{
    param:"PARAM_CHANGE",
    add:"USER_ADD",
    change:"USER_CHANGE",
    addReset:"RESET_ADD",
    changeReset:"RESET_CHANGE",
    items:"USERS_SUCCESS",
    roles:"ROLES_SUCCESS",
    close:"DIALOG_CLOSE",
    id:"USER_ID",
    current:"USER_CURRENT",
    has:"USER_ROLES",
    transfer:"TRANSFER_CHANGE",
    tableSelected:"TABLE_SELECTED",
    resetPass:"RESETPASS"
  },
  dialog:{
    _default:{
      title:"",
      visible:false,
      template:""
    },
    add:{
      title:"新增用户信息",
      visible:false,
      width:"654px",
      template:"addUser"
    },
    change:{
      title:"用户信息修改",
      visible:false,
      width:"654px",
      template:"changeUser"
    }
  },
  template:{
    addUser:"addUser",
    changeUser:"changeUser"
  },
  sendInfo:{
    account:"",
    address: "",
    date: "",
    id: "",
    idCard:"",
    name: "",
    password: "",
    phone: "",
    roles: [],
    sex: 1,
    status: "0",
    statusText: "",
    type: ""
  },
  _default:{
    account:"",
    address: "",
    date: "",
    id: "",
    idCard:"",
    name: "",
    password: "",
    phone: "",
    roles: [],
    sex: 1,
    status: "0",
    statusText: "",
    type: ""
  },
  result: {},
  flag: {},
  transferData: [],
  transferChecked:[],
  tableSelected:[]

}

// getters 对数据进行格式化
const getters = {
  transferTitles:(state)=>{
    return state.options.transferTitles
  },
  currentDialog:(state)=>{
    return state.currentDialog;
  },
  currentUser:(state)=>{
    return state.currentUser;
  },
  sendInfo: state => {
    return state.sendInfo
  },
  transferData:state=>{
    return state.transferData;
  },
  transferChecked:state=>{
    return state.sendInfo.roles;
  },
  tableSelected:state=>{
    return state.tableSelected;
  }
}

const actions = {
  resetParam({commit,state},data){
    commit(types[state.actions.param],data);
  },
  getItems({commit, state}) {
    let param = Util.resetParam(Object.assign({},state.param));
    XHR.ajaxGetForArray({
      url: state.url.search,
      data: param
    }, function (data) {
      let parse = {
        type: state.url.search,
        data: data
      };
      data.success ? commit(types[state.actions.items], parse) : commit(types.ERROR, parse);
    })
  },
  topHandler({commit, state},type){
    switch(type){
      case"add":
        commit(types[state.actions.add]);
        break;
      default:
        commit(types.INFO,"获取操作类型错误");
        break;
    }
  },
  tableSelected({commit, state},data){
    commit(types[state.actions.tableSelected],data)
  },
  pageChange({dispatch,commit,state},pageIndex){
    commit(types[state.actions.param],{pageIndex:pageIndex});
    dispatch("getItems");
  },
  transferData({commit, state}) {
    XHR.get({
      url: state.url.transfer
    }, function (data) {
      data.success ? commit(types[state.actions.roles], data.data) : ""
    })
  },
  transferChange({commit ,state},data){
    commit(types[state.actions.transfer],data)
  },
  //用户输入引起新增信息变化
  setAddInfo({commit, state},data){
    commit(types[state.actions.addReset],data);
  },
  //新增
  addItem({commit, state}){
    let roles = [],pre = state.sendInfo.roles;
    if(pre.length>0){
      pre.map((item)=>{
        roles.push({id:item})
      });
    };
    let flag = actions.checkInfo("add");
    if(flag){
      XHR.post({
        url:state.url.add,
        data:Object.assign({},state.sendInfo,{roles:roles}),
      },function(data){
        data.success ? commit(types.SUCCESS,{type:"add",data:data}):commit(types.ERROR,{type:"add",data:data})
      })
    }else{
      commit(types.ERROR,{type:"add",data:{
        msg:"请输入必填项"
      }})
    }
  },
  checkInfo(type){
    switch(type){
      case"add":
      case"change":
        let {account,name,password} = state.sendInfo;
        return (account==""||name==""||password=="") ? false : true;
      default:
        return false;
    }
  },
  setChangeInfo({commit, state},data){
    commit(types[state.actions.changeReset],data);
  },
  changeItem({commit, state}){
    let a = [];
    state.sendInfo.roles.map((item) => {
      a.push({id: item})
    });
    let flag = actions.checkInfo("change");
    if(flag){
      XHR.restfulMiddle({
        url: state.url.change,
        method: "PUT",
        think: {id: state.currentUser.id},
        data:Object.assign({},state.sendInfo,{roles:a}),
      }, function (data) {
        data.success ? commit(types.SUCCESS, {type:"change",data:data}) : commit(types.ERROR, {type:"change",data:data});
      })
    }else{
      commit(types.ERROR,{type:"add",data:{
        msg:"请输入必填项"
      }})
    }
  },
  deleteItem({commit, state}, data) {
    XHR.restfulMiddle({
      url: state.url.del,
      method: "DELETE",
      think: {id: data[0]},
    }, function (data) {
      let parse = {
        type: "delete",
        data: data
      };
      data.success ? commit(types.SUCCESS, parse) : commit(types.ERROR, parse);
    })
  },
  //修改密码
  resetPass({commit, state}, data){
    console.log(data.id);
    XHR.restfulMiddle({
      url: state.url.reset,
      method: "PUT",
      think: {id: data.id},
    }, function (data) {
      data.success ? commit(types.SUCCESS, {type:"reset",data:data}) : commit(types.ERROR, {type:"reset",data:data});
    })
  },
  //弹出窗口
  dialogSure({dispatch, state},data){
    let {dialog} = data;
    switch(dialog.template){
      case state.template.addUser:
        dispatch("addItem")
        break;
      case state.template.changeUser:
        dispatch("changeItem")
        break;
      default:
        break;
    }
  },
  dialogClose({commit}){
    commit(types[state.actions.close]);
  },
  //获取当前对象的数据
  getCurrentData({commit, state}, data) {
    commit(types[state.actions.current], data);
    commit(types[state.actions.change]);
    commit(types[state.actions.has],data.roles)
  }
}
const mutations = {
  //查
  [types[state.actions.items]](state, data) {
    let result = data.data.data;
    let List = new UserList(result.content);
   // console.log(models);
    state.tableData = List.getModels();
    if(result.pageIndex){
      state.pageData = {
        limit:result.limit,
        pageIndex: result.pageIndex,
        total: result.total
      }
    }else{
      state.pageData =  null;
    }

  },
  //改
  [types[state.actions.change]](state) {
    state.currentDialog= Object.assign({},state.dialog.change,{visible:true});
  },
  //增
  [types[state.actions.add]](state){
    state.currentDialog= Object.assign({},state.dialog.add,{visible:true});
  },
  //修改参数
  [types[state.actions.param]](state,param){
    state.param = Object.assign({},state.param,param);
  },
  [types[state.actions.roles]](state, data){
    state.transferData = data;
  },
  //重置密码
  [types[state.actions.resetPass]](state, data){
    state.transferData = data;
  },
  //穿梭狂数据改变监听
  [types[state.actions.transfer]](state, data){
    state.sendInfo = Object.assign({},state.sendInfo,{roles:data.data});
  },
  [types[state.actions.addReset]](state,data){
    state.sendInfo = Object.assign({},state.sendInfo,data);
  },
  [types[state.actions.changeReset]](state,data){
    state.sendInfo = Object.assign({},state.sendInfo,data);
  },
  [types[state.actions.close]](state){
    state.currentDialog = Object.assign({},state.currentDialog,state.dialog._default);
    state.sendInfo = Object.assign({},state.sendInfo,state._default);
  },
  [types[state.actions.id]](state,id){
    state.userId = id;
  },
  [types[state.actions.current]](state,data){
    state.sendInfo = Object.assign({},state.sendInfo,data)
    state.currentUser = data;
  },
  [types[state.actions.has]](state,data){
    let a =  data.map((item)=>{
      return item.id
    });
   state.sendInfo = Object.assign({},state.sendInfo,{roles:a})
  },
  [types[state.actions.tableSelected]](state,data){
    console.log(data)
    state.tableSelected = data;
  },
  //回调
  [types.SUCCESS](state, data) {
    state.result = data;
  },
  [types.ERROR](state, data) {
    state.result = data;
  }
}

export default {
  namespaced:true,
  state,
  getters,
  actions,
  mutations
}
