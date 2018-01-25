/**
 * Created by Administrator on 2016/8/19.
 */
import Final from "src/config"
import Qs from "qs"
import $ from "jquery"

export default class XHR {
    constructor(options){

        this.defaultParam=$.extend({
            method:"GET",
            dataType:"json",
            contentType:"application/json",
            success:function(data){
                options.callback(data);
            },
            error:function(data){
                options.callback(data);
            }
        },options.param);

        this.ajax(this.defaultParam);
    }
    static TYPE () {
       return{
           "json":"application/json",
           "text":"text/html"
       }
    }
    static decodeURL(url){
      let uri = process.env.NODE_ENV == "development" ? "/develop/" : "../../";//当前node环境
      return uri+Final.URL[url];
    }
    static downLoad(id){
        window.location.href = Final.URL.DOWNLOAD+"?id="+id;
        return true;
    }
    static isGet(obj){
        return obj.param&&obj.param.method==="GET" ? true : false;
    }
    static complete(xhr,callBack){
      let status = xhr.status.toString().substring(0,1);
      switch(status){
        case"3":
          callBack(false,3);
          break;
        case"4":
          break;
        case"5":
          break;
        case"6":
          break;
        default:
          callBack(xhr.responseJSON,true);
          break;
      }
    }
    static ajax(obj,callBack){
          let  type = obj.param&&obj.param.method?obj.param.method:"GET",
               data = obj.param&&obj.param.body?obj.param.body:"{}";
         let result = type=="POST"? data : JSON.parse(data);
        $.ajax({
            type:type,
            dataType:"json",
            contentType:obj.param.contentType||"application/json;charset=UTF-8",
            url:XHR.decodeURL(obj.url),
            data:result,
            success:function(data){
                callBack(data);
            },
            error:function(data){
                callBack(data);
            }});
    }
    static get(obj,callBack){
        $.ajax({
          type: "json",
          method: "GET",
          dataType: "json",
          contentType: obj.contentType || "application/json;charset=UTF-8",
          url:XHR.decodeURL(obj.url),
          data: obj.data || {},
          complete: function (xhr) {
            XHR.complete(xhr, callBack);
          }
        })
    }
    static post(obj,callBack){
        $.ajax({
            type:"json",
            method:"POST",
            dataType:"json",
            contentType:"application/json",
            url:XHR.decodeURL(obj.url),
            data:JSON.stringify(obj.data||{}),
            complete: function (xhr) {
              XHR.complete(xhr, callBack);
            }
        });
    }
    static form(obj,callBack){
        $.ajax({
            url:XHR.decodeURL(url),
            method:obj.method,
            data:obj.data,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            complete: function (xhr) {
              XHR.complete(xhr, callBack);
            }
        })
    }
    static getAsync(obj,callBack){
        $.ajax({
            type:"json",
            method:"GET",
            dataType:"json",
            async: false,
            contentType:obj.contentType||"application/json",
            url:XHR.decodeURL(obj.url),
            data:obj.data||{},
            complete: function (xhr) {
              XHR.complete(xhr, callBack);
            }
        });
    }
    static restful(param,callBack){
        let url =XHR.decodeURL(param.url),think=param.think,body=param.data;
        for(let k in think){
            url+="/"+think[k];
        }
        $.ajax({
            url:url,
            method:param.method,
            contentType:param.contentType||"application/json",
            data:JSON.stringify(body||{}),
            complete: function (xhr) {
              XHR.complete(xhr, callBack);
            }
        })
    }
    static restfulMiddle(param,callBack){
    let url = XHR.decodeURL(param.url),think=param.think;
        let regUrl = url.replace("{id}",think.id);
        if(param.method === "GET"||param.method === "DELETE"){
          $.ajax({
            url:regUrl,
            method:param.method,
            contentType:param.contentType||"application/json",
            complete: function (xhr) {
              XHR.complete(xhr, callBack);
            }
          })
        }else{
          $.ajax({
            url:regUrl,
            method:param.method,
            data:JSON.stringify(param.data),
            contentType:param.contentType||"application/json",
            complete: function (xhr) {
              XHR.complete(xhr, callBack);
            }
          })
        }

  }
    static Axios(url,param,callBack){
      let $http = axios.create({
        transformRequest: [function (data) {
          if( data instanceof FormData ){
            return data;
          }
          return Qs.stringify(data);
        }],
        paramsSerializer: function(params){
          return Qs.stringify(params,{arrayFormat:'brackets'})
        },
        headers : {
         /* 'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'*/
          'Content-Type' : 'application/json;charset=utf-8'
        }

      })
      $http({
        url : url,
        params :param,
        method : 'GET'
      }).then((data)=>{
        let res = data.data;
        console.log({res});
      });
    }
    static ajaxGetForArray(obj,callBack){
      $.ajax({
          type:"json",
          method:"GET",
          dataType:"json",
          contentType:"application/json;charset=UTF-8",
          url:XHR.decodeURL(obj.url),
          data:obj.data||{},
          traditional: true,
          complete: function (xhr) {
            XHR.complete(xhr, callBack);
          }
      });
    }
}
