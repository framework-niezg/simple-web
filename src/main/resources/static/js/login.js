/**
 * Created by Administrator on 2017/2/10.
 */

var login = {
    init:function(){
        this.login();
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
    isRemember:function(){
        var user = "";
        if(window.localStorage){
            user = window.localStorage.getItem("userName");
        }else{
            user =window.user;
        }
        return user;
    },
    setItem:function(user){
        if(window.localStorage){
            window.localStorage.setItem("user",user);
        }else{
            window.user =userName;
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
    var userRole = $("#userRole").val();
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
        $.ajax({
            url:"/userExist",
            async: false,
            method:"post",
            type:"json",
            contentType:"application/json",
            data:JSON.stringify({userName:userName,password:userPassword}),
            success:function(data){
                data.success ? login.setItem(JSON.stringify(data.data)):""
            },
            error:function(){

            }
        });
        flag = true;
    }
    return flag;
}