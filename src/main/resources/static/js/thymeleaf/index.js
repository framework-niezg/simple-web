/**
 * Created by Administrator on 2017/3/30.
 */
/**
 * Created by Administrator on 2017/2/10.
 */
var menuOut = false;
var App = {
    duration:500,
    $aside : $(".aside"),
    $main : $(".main"),
    $menu2Li : $(".menu-2 li"),
    asideOut:function(){
        App.$aside.animate({
            width:"2.5%",
            minWidth:52
        },{
            easing: 'linear',
            duration: App.duration,
            complete: function(){
                menuOut = true;
            }
        });
        App.$main.animate({
            marginLeft:"2.5%"
        },{
            easing: 'linear',
            duration: App.duration
        });
    },
    asideIn:function(){
        App.$aside.animate({
            width:"10%",
            minWidth:200
        },{
            easing: 'linear',
            duration: App.duration,
            complete: function(){
                menuOut = false;
            }
        });
        App.$main.animate({
            marginLeft:200

        },{
            easing: 'linear',
            duration: App.duration
        });
    },
    decodeUrl:function(){
        var href = window.location.href,port=window.location.port;
        var index = href.indexOf(port);
        var hash = href.substring(index+port.length);
        var a = hash.split("/");
        var length = a.length-1;
        var uri ="";
        for(var i=0;i<length;i++){
            uri+="../";
        }
        console.log(uri);
        return uri
    },
    resetPass:function(old,newPass){
        var uri = App.decodeUrl();
        $.ajax({
            url:uri+"users/current/change/password",
            method:"PUT",
            type:"json",
            contentType:"application/json",
            data:JSON.stringify({
                oldPassword:old,
                newPassword:newPass
            }),
            success:function(data){
                App.message(data,2000);
            },
            error:function(data){
                console.log(data)
            }
        })
    },
    message:function(data,time){
        if(data.success){
            $("#userChangePass").hide(function(){
                $("#messageContent").text(data.msg);
                $("#message").show();
                setTimeout(function(){
                    $("#message").hide();
                },time)
            });
        }else{
            var index = data.msg.indexOf(":");
            var text = data.msg.substring(index+1);
         //   var message =
           $("#userChangePassError").text(text);
        }
    },
    resetMenu:function(){
        if(parseInt($("#menuLength").text())>6){
           /* $('.slider').bxSlider({
                slideWidth: 90,
                maxSlides: 6,
                moveSlides: 1,
                pager:false,
            });*/
        }
    },
    logOut:function(){
        App.getItem("contextPath").contextPath;
        window.location.href = App.getItem("contextPath").contextPath+ "/logout";
    },
    layerOpen:function(){
        App.resetAllInfo();
        $("#userChangePass").show();
    },
    layerOut:function(){
        $("#userChangePass").hide();
    },
    initUser:function(){
        App.setItem("contextPath",{
            contextPath:$("#contextPath").text()
        })

    },
    eyes:function(name,flag){
        if(flag == "down" ){
            $("#"+name).hide();
            $("#"+name+"Text").show();
        }else{
            $("#"+name+"Text").hide();
            $("#"+name).show();
        }
    },
    resetAllInfo(){
        $("#oldPasswd").val("");
        $("#passwd").val("");
        $("#passwdSure").val("");
        $("#oldPasswdText").val("");
        $("#passwdText").val("");
        $("#passwdSureText").val("");
        $("#userChangePassError").val("");
    },
    showMessage:function(text){
       /* $("#messageContent").text(text);
        $("#message").show(function(){
            setTimeout(function(){
                $("#message").hide();
                $("#messageContent").text("");
            },3000)
        });*/
        $("#userChangePassError").text(text);
    },
     setItem:function(name,value){
        if(window.localStorage){
            window.localStorage.setItem(name,JSON.stringify(value))
        }else{
            window.local[name] = JSON.stringify(value);
        }
    },
     removeItem:function(name){
        if(window.localStorage){
            window.localStorage.removeItem(name)
        }else{
            delete window.local[name]
        }
    },
     getItem:function(name){
        if(window.localStorage){
            return JSON.parse(window.localStorage.getItem(name))
        }else{
            return JSON.parse(window.local[name])
        }
    }
};
$(function(){
    App.initUser();
    App.resetMenu();
    $("#btnLogOut").on("click",function(){
        App.logOut();
    })
    $(".input-next").on("change",function(){
        var id = $(this).attr("id"),val=$(this).val();
        $("#"+id+"Text").val(val);
    });
    $(".input-next").on("focus",function(){
          $("#userChangePassError").text("");
    });
    $(".iconfont-pass").mousedown(function(){
        var name = $(this).attr("name");
        App.eyes(name,"down")
    })
    $(".iconfont-pass").mouseup(function(){
        var name = $(this).attr("name");
        App.eyes(name,"up")
    })
    $("#userResetPass").on("click",function(){
       var old =$("#oldPasswd").val();
       var newPass =$("#passwd").val();
       var newPassSure =$("#passwdSure").val();
        if(old==""){
            App.showMessage("旧密码不能为空")
        }else if(newPass==""){
            App.showMessage("新密码不能为空")
        }else if(newPassSure==""){
            App.showMessage("请确认新密码")
        }else if(newPassSure!==newPass){
            App.showMessage("两次新密码输入不一致")
        }else{
            App.resetPass(old,newPass);
        }
    })
    $("#userCancelPass").on("click",function(){
        App.layerOut();
    })
    $("#btnResetPass").on("click",function(){
          App.layerOpen();
    })
    $(".menu-0").on("click",function(event){
        $(this).addClass("temp-active").siblings().removeClass("temp-active");
        $(".user-zone").removeClass("temp-active");
        event.stopPropagation();
        $(".user-sub-menu").hide();
        $(this).siblings().find("ul").hide();
        $(this).find("ul").toggle();
    })
    $(".user-zone").on("click",function(event){
        $(this).addClass("temp-active");
        event.stopPropagation();
        $(".menu-0").find("ul").hide();
        $(".menu-0").removeClass("temp-active");
        $(this).find("ul").toggle();
    })
    /*$(".menu").hover(function(event){
        event.stopPropagation();
        var lis = $(".menu").find(">li")
       if(lis.length>6){
           $(".menu-before").show();
           $(".menu-after").show();
       }
    },function(event){
        event.stopPropagation();
        var srcElement = event.srcElement || event.target;
        console.log($(srcElement).attr("class"))
       /!* $(".menu-before").hide();
        $(".menu-after").hide();*!/
    })
    $(".menu-before").hover(function(){
        "use strict";
        event.stopPropagation();

    })
    $(".menu::before").on("click",function(){
        "use strict";
        console.log(123)
    });*/
    $(document).click(function(e){
        var srcElement = e.srcElement || e.target;
        if($(srcElement).attr("class")!=="link"){
            $(".sub-menu").hide();
            $(".menu-0").removeClass("temp-active");
            $(".user-zone").removeClass("temp-active");
        }
    })
});

