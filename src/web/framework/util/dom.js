/**
 * Created by Administrator on 2016/8/19.
 * Util tool to format data
 */
import React ,{PropTypes}from "react"
import ReactDOM from "react-dom"
import Immutable from "immutable";
export default class Dom {
    constructor(options){
        this.options = options||{};
    }
    //获取某事件回调句柄dom的属性名的值，name="userName"   (name) => userName
   static getAttribute(e,attr){
       let srcElement=e.srcElement||e.target;
       return srcElement.getAttribute(attr);
   }
    //获取某事件回调句柄dom的值，  (e) => 3
    static getValue(e){
        let srcElement=e.srcElement||e.target;
        return srcElement.value;
    }
    //获取某事件回调句柄dom的json对象，name="id"   (name) => {id:3}
    static getAttributeJSON(e,attr){
        let srcElement=e.srcElement||e.target,attrValue=srcElement.getAttribute(attr);
        return {
            [attrValue]:srcElement.value
        };
    }
    //渲染节点dom--react
    static renderDOM(dom,id){
        ReactDOM.render(
            dom,id
        )
    }
}
Dom.prototype={
   
}

