/**
 * Created by Administrator on 2017/2/10.
 */

var login = {
    init:function(){
        this.login();
        this.isLogin();
    },
    login:function(){
        $(".login-input").on("focus",function(){
            $("#error").html("")
        });
        this.initInput();
    },
    initInput:function(){
        var user = this.isRemember();
        if(user){
            $("#userName").val(user);
            $("#remember").attr("checked",true);
        }
    },
    isLogin:function(){
        var user = login.getItem("userName");
        $("#userName").val(user ? user.userName : "");
    },
    isRemember:function(){
        var user = "";
        if(window.localStorage){
            user = window.localStorage.getItem("userName");
        }else{
            user =window.user;
        }
        return user;
    },
    setItem:function(k,v){
        if(window.localStorage){
            window.localStorage.setItem(k,v);
        }else{
            window[k] =v;
        }
    },
    getItem:function(k){
        if(window.localStorage){
            return JSON.parse(window.localStorage.getItem(k));
        }else{
           return JSON.parse(window[k]);
        }
    },
    rememberMe:function(userName){
        if(window.localStorage){
            window.localStorage.setItem("userName",userName);
        }else{
            window.user =userName;
        }
    },
    leaveMe:function(){
        if(window.localStorage){
            window.localStorage.removeItem("userName");
        }else{
            delete window.user
        }
    }
}
$(function(){
    login.init();
    $(".reset-btn").click(function(){
        $("#userName").val("");
        $("#userPassword").val("")
    })
    $(".select-label").click(function(){
        $(".select-list").show();
    })
})
function checkForm(){
    var userName = $("#userName").val(),userPassword=$("#userPassword").val(),flag=null;
    if(userName.trim()==""||userPassword.trim()==""){
        $("#error").html("账号或密码不能为空");
        flag = false;
    }else{
       /* if($("#remember").is(":checked")){
            login.rememberMe(userName);
        }else{
            login.leaveMe();
        }*/
        $(".login-btn").text("登录中....");
        login.setItem("userName",JSON.stringify({userName:userName}));
        flag = true;
    }
    return flag;
}