import Vue from 'vue'
import Vuex from 'vuex'
import * as types from './mutation-types'
import * as actions from './actions'
import * as getters from './getters'

import userManager from './modules/userManager'
import roleManager from './modules/roleManager'

Vue.use(Vuex)

const state = {
  current:""
}
const mutations = {
  [types.STATE](state, id) {
    state.current = id;
  },
  [types.TREE_CHANGE](state, data) {
    switch(data.type){
      case types.ADD:
      case types.CHANGE:
        state[state.current].sendInfo.menus = data.data;
        break;
      default:
        break;
    }
  }
}
export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production',
  state,
  actions,
  getters,
  mutations,
  modules:{
    userManager,
    roleManager
  }
})
