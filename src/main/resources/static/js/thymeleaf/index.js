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
    resetPass:function(old,newPass){
        $.ajax({
            url:"/user/userInfo/changePassword",
            method:"post",
            type:"json",
            contentType:"application/json",
            data:JSON.stringify({
                id:App.getItem("user").id,
                oldPasswd:old,
                passwd:newPass
            }),
            success:function(data){
                if(data.data !== "原密码错误"){
                    App.layerOut();
                }
                App.message(data.data,2000)

            },
            error:function(data){
                console.log(data)
            }
        })
    },
    message:function(msg,time){
        $("#messageContent").text(msg);
        $("#message").show();
        setTimeout(function(){
            $("#message").hide();
        },time)
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
        console.log(window.location);
        App.getItem("contextPath").contextPath;
        window.location.href = App.getItem("contextPath").contextPath+ "/logout";
    },
    layerOpen:function(){
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
    /*$(".slide").on("click",function(){
        if(menuOut){
            App.asideIn();
        }else{
            App.asideOut();
        }
    })*/
    $("#btnLogOut").on("click",function(){
        App.logOut();
    })
    $("#userResetPass").on("click",function(){
       var old =$("#oldPasswd").val();
       var newPass =$("#passwd").val();
        var newPassSure =$("#passwdSure").val();
        if(old==""){
            alert("旧密码不能为空")
        }else if(newPass==""){
            alert("新密码不能为空");
        }else if(newPassSure==""){
            alert("请确认新密码");
        }else if(newPassSure!==newPass){
            alert("两次新密码输入不一致");
        }else{
            App.resetPass(old,newPass);
        }
    })
    $("#userCancelPass").on("click",function(){
        App.layerOut();
    })
    $("#btnResetPass").on("click",function(){
          $("#oldPasswd").val("");
          $("#passwd").val("");
          $("#passwdSure").val("");
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

