import * as types from '../mutation-types'
import Util from 'framework/util/util'
import XHR from 'framework/xhr/xhr'
import RoleList from "src/collection/roleList"

const state = {
  name:"roleManager",
  param: {
    name: "",
    query: ["name"],
    queryString: [],
    pageIndex: "1",
    limit: "5",
    orderBy: ""
  },
  options:{
    table: {
      type: "index",
      isPageNation: true,
      highlight: true,
      caption:"caption",
      th: [
        {property: "name", label: "角色账号"},
        {property: "desc", label: "角色名称"}
      ],
      deals: {
        max:2
      },
      tops: [
        {text: "新增", id: "btnAdd", name: "add", type: "primary", event: "add"}
      ]
    }
  },
  tableData: [],
  currentDialog:{},
  treeData: [],//menus
  currentHas: [],
  checksMenu: [],
  treeCheckData:[],
  currentRole: {},
  url: {
    del: "deleteRole",
    change: "changeRole",
    search: "roles",
    menus: "menus",
    has: "roleHasMenu",
    infoChange: "roleMenuChange",
    add:"addRole",
  },
  actions:{
    add:"ROLE_ADD",
    change:"ROLE_CHANGE",
    addReset:"RESET_ADD",
    changeReset:"RESET_CHANGE",
    items:"ROLES_SUCCESS",
    tree:"MENUS_SUCCESS",
    close:"DIALOG_CLOSE",
    id:"ROLE_ID",
    current:"ROLE_CURRENT",
    has:"ROLE_MENUS"
  },
  dialog:{
    _default:{
      title:"",
      visible:false,
      template:""
    },
    add:{
      title:"新增角色信息",
      visible:false,
      width:"654px",
      template:"addRole"
    },
    change:{
      title:"角色信息修改",
      visible:false,
      width:"654px",
      template:"changeRole"
    }
  },
  template:{
    addRole:"addRole",
    changeRole:"changeRole"
  },
  sendInfo:{
    name: "",
    desc: "",
    menus: []
  },
  _default:{
    name: "",
    desc: "",
    menus: []
  },
  result: {},
  flag: {}
}

// getters 对数据进行格式化
const getters = {
  tableData:(state) => {
    return state.tableData
  },
  id: state => {
    return state.roleId
  },
  currentDialog:(state)=>{
    return state.currentDialog;
  },
  currentRole:(state)=>{
    return state.currentRole;
  },
  treeData:state => state.treeData,
  allMenuDisabled:state=>{
    let reset = JSON.parse(JSON.stringify(state.menus));
    reset.map((menu)=>{
      menu.disabled = true;
      if(menu.children.length>0){
        menu.children.map((m)=>{
          m.disabled = true;
        })
      }
    });
    console.log(reset);
    return reset;
  },
  treeCheckData: state => {
    return state.treeCheckData;
   /* return state.currentHas.filter(({id})=>{
      console.log(id);
    })*/
  },
  roleMenusId: state => {
    let a = [];
    if (state.roleMenus.length > 0) {
      state.roleMenus.map((roleMenu) => {
        a.push(roleMenu.id)
      });
    }
    return a;
  },
  sendInfo: state => {
    return state.sendInfo
  },
  result: state => {
    return state.result;
  }
}

const actions = {
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
  //获取所有菜单
  getTreeData({commit, state}) {
    XHR.get({
      url: state.url.menus
    }, function (data) {
      data.success ? commit(types[state.actions.tree], data.data) : ""
    })
  },
  //用户输入引起新增信息变化
  setAddInfo({commit, state},data){
    commit(types[state.actions.addReset],data);
  },
  //新增
  addItem({commit, state}){
    let menus = [];
    if(state.sendInfo.menus.length>0){
      state.sendInfo.menus.map((item)=>{
        menus.push({id:item})
      });
    };
    XHR.post({
      url:state.url.add,
      data:Object.assign({},state.sendInfo,{menus:menus}),
    },function(data){
      data.success ? commit(types.SUCCESS,{type:"add",data:data}):commit(types.ERROR,{type:"add",data:data})
    })
  },
  setChangeInfo({commit, state},data){
    commit(types[state.actions.changeReset],data);
  },
  changeItem({commit, state}){
    let menus = [];
    state.sendInfo.menus.map((item) => {
      menus.push({id: item})
    });
    console.log(state.sendInfo);
    XHR.restfulMiddle({
      url: state.url.infoChange,
      method: "PUT",
      think: {id: state.currentRole.id},
      data:Object.assign({},state.sendInfo,{menus:menus}),
    }, function (data) {
      data.success ? commit(types.SUCCESS, {type:"change",data:data}) : commit(types.ERROR, {type:"change",data:data});
    })
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
  //弹出窗口
  dialogSure({dispatch, state},data){
    let {dialog} = data;
    switch(dialog.template){
      case state.template.addRole:
        dispatch("addItem")
        break;
      case state.template.changeRole:
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
    XHR.restfulMiddle({
      url: state.url.has,
      method: "GET",
      think: {id: state.currentRole.id},
    }, function (data) {
        data.success ? commit(types[state.actions.has], data.data) : ""
    })
  },
}
const mutations = {
  //查
  [types[state.actions.items]](state, data) {
    let result = data.data.data;
    let List = new RoleList(result);
    state.tableData = List.getModels();
  },
  //改
  [types[state.actions.change]](state) {
    state.currentDialog= Object.assign({},state.dialog.change,{visible:true});
  },
  //增
  [types[state.actions.add]](state) {
    state.currentDialog= Object.assign({},state.dialog.add,{visible:true});
  },

  [types[state.actions.tree]](state, data) {
    state.treeData = data;
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
    state.roleId = id;
  },
  [types[state.actions.current]](state,data){
    state.sendInfo = Object.assign({},state.sendInfo,data)
    state.currentRole = data;
  },
  [types[state.actions.has]](state,data){
    state.treeCheckData = data.map((item)=>{
      return item.id
    });
   state.sendInfo = Object.assign({},state.sendInfo,{menus:state.treeCheckData})
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
