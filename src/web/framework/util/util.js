/**
 * Created by Administrator on 2016/8/19.
 * Util tool to format data
 */
import Immutable from "immutable";
export default class Util {
    constructor(options){
        this.options = options||{};
    }
    /*
     * @param none
     * @to test static method
     */
    static init(){
        return "init";
    }
    /*
     * @param name like "id"
     * @to get value of "id" from location url  like "http://localhost:8080/index.html?id='123'"
     * @result 123
     */
    static getUrlParam(name){
        var reg = new RegExp("^|&"+ name +"=([^&]*)()&|$");
        var r = window.location.search.substr(1).match(reg);
        return r!=null?unescape(r[2]):null;
    }
    /*
     * @param none
     * @to get value of hash from location url  like "http://localhost:8080/index.html#userInfo"
     * @result userInfo
     */
    static getCurrentHash(){
        return window.location.hash.replace(/^.*#\//g, "").replace(/\?.*$/g, "");
    };
    static array2Json(array){
        let json = {}
        array.map((item)=>{
            json[item]=item
        })
        return json;
    }
    /*
     * @param target,{b}
     * @to target extend {b}
     * @result target seams like {...a,b}
     */
    static extended(target,...param){
        for (var i = 1; i < arguments.length; i++){
            var source = arguments[i];
            for (var key in source){
                if (Object.prototype.hasOwnProperty.call(source, key)){
                    target[key] = source[key];
                }
            }
        }
        return target;
    };
    /*
     * @param object
     * @to test object is empty or not
     * @result true or false
     */
    static isEmptyObject(object) {
        for(var p in object) {
            if(Object.prototype.hasOwnProperty.call(object, p)) return false;
        }return true;
    };
    static setValue(data,model){
        for(let k in data){
            model[k] = data[k]
        }
        return model;
    }
    /*
     * @param number like 5
     * @to create  array  like [0,1,2,3,4]
     * @result []
     */
    static numberToArray(number){
        let a;
        try{
            var s;
            if((/^[0-9]*[1-9][0-9]*$/.test(number))){
                a = [];
                for(let i=1;i<=number;i++){
                    a.push(i);
                }
            }else{
                a = "请传递大于0的正整数"
            }
        }catch(e){
            console.log(e);
        }

        return a
    };
    /*
     * @param object like {name:"name",id:"id"}
     * @to get keys of object
     * @result like ["name","id"]
     */
    static ketSet(object){
        let a = [];
        for(var o in object){
            a.push(o);
        }
        return a;
    };
    static getKey(object){
        let a;
        for(var o in object){
            a=o;
        }
        return a;
    };
    /*
     * @param data like state {_size:1,_tail:{},_x:function(){}}
     * @to data like {jurisdiction:"1"name"name"password:"123456"}
     * @result object
     */
    static getObj(stateObj){
        var _this =this;
         stateObj.map(function(item){
             _this.result = item.data;
        });
        return _this.result;

    };
    /**
     * param props "/a"
     * return "a"
     */
    static getCurrentByRoute(props){
        let path = props.route.path;
        return path.substring(1,path.length);
    }
    static setItem(name,value){
        if(window.localStorage){
            window.localStorage.setItem(name,JSON.stringify(value))
        }else{
            window.local[name] = JSON.stringify(value);
        }
    };
    static removeItem(name){
        if(window.localStorage){
            window.localStorage.removeItem(name)
        }else{
            delete window.local[name]
        }
    };
    static getItem(name){
        if(window.localStorage){
            return JSON.parse(window.localStorage.getItem(name))
        }else{
            return JSON.parse(window.local[name])
        }
    };
    static immutable(data){
        return Immutable.fromJS(data);
    };
    static getObjectType(data){
        switch(Object.prototype.toString.call(data)){
            case"[object Object]":
                return "object";
            case"[object Array]":
                return "array"
            default:
                break;

        }
    };
    static assign(state,action){
        if(Object.assign){
               console.log(Object.assign)
            return Object.assign({},state,action);
        }else{
            console.log(state,action)
        }
    }
    static renderDom({type,data,id}){
        switch(type){
            case"select":
                Util.renderSelect(data,id,true)
                break;
            default:
                break
        }
    }
    static renderSelect(data,id,flag,current){
        let option = flag ? "<option value=''>---全部---</option>":"";
        data.map((item)=>{
            let name =  item.name || item.cname,
                code = item.value || item.code ||item.codeid || "";
            option += current==code ? '<option selected="selected" value="'+ code +'">'+ name +'</option>':'<option value="'+ code +'">'+ name +'</option>';
        });
        $("#"+id).html(option)
    }
    static getDom(type,data,id,flag=true,current=""){
        switch(type){
            case"select":
                Util.renderSelect(data,id,flag,current)
                break;
            case"singleSelect":
                Util.singleSelect(data,id,flag,current)
                break;
            default:

                break
        }
    }
    static singleSelect(data,id,flag){
        let option = flag ? "<option value=''>---全部---</option>":"";
        data.map((item)=>{
            option += '<option value="'+ item +'">'+ item +'</option>';
        });
        $("#"+id).html(option)
    }
    static resetParam(params){
      if(params.query && params.query.length>0){
        params.queryString=[];
        params.query.map((param)=>{//name,role
          if(params[param]&&params[param]!==""){
            params.queryString.push(param+"~like~%"+params[param]+"%")
          }
        })
      }
     return params
    }
    static empty(data){
      for(let k in data){
        data[k]="";
      }
    }
}
Util.prototype={

}
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
Math.formatFloat = function(f, digit) {
    console.log(f)
    var m = Math.pow(10, digit);
    return parseInt(f * m, 10) / m;
}
