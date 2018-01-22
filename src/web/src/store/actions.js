import * as types from './mutation-types'

//全局
export const reSetSate = ({ commit },data) => {
  commit(types.STATE,data)
};
//树事件
export const treeChange = ({ commit },data) => {
  commit(types.TREE_CHANGE,data)
}
//穿梭框
export const transferChange = ({dispatch ,state},data) => {
  dispatch(state.current+"/transferChange",data)
}
//弹出框事件
export const dialogEvent = ({ dispatch,commit,state },data) => {
  switch(data.type){
    case types.SURE:
      dispatch(state.current+"/dialogSure",data);
      break;
    default:
      dispatch(state.current+"/dialogClose");
      break;

  }
  //commit(types.DIALOG_CHANGE,data)
}



