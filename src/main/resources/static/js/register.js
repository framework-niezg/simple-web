/**
 * Created by Administrator on 2017/2/10.
 */

var App = {
    getAreaById:function(id,dom){
        App.ajax({
            url:"/dict/getAreaList",
            method:"GET",
            dataType:'json',
            data:{
                parentid:id
            },
            success:function(data){
                App.renderArea(dom,data)
            },
            error:function(){

            }

        })
    },
    renderArea:function(dom,data){
        $(dom).html(App.getDom(data.data));
        App.renderChild(dom);
    },
    renderChild:function(dom){
        switch(dom){
            case"#provinceCode":
                App.getAreaById(parseInt( $(dom).val()),"#cityCode");
                break;
            case"#cityCode":
                App.getAreaById(parseInt( $(dom).val()),"#areaCode");
                break;
            default:
                break;
        }
    },
    getDom:function(data){
       /* var options = "<option value='-1'>请选择</option>";*/
        var options = "";
        $.each(data,function(i){
            var area = data[i];
            options+='<option value="'+ area.codeid +'">'+ area.name +'</option>';
        })
        return options;
    },
    ajax:function(options){
        $.ajax(options)
    },
    message(msg,time){
        if(msg=="请求成功。"){
            if(confirm("待系统管理员审核生效")){
                window.location.href = "/login";
            }
        }else{
            $("#messageRegisterContent").text(msg);
            $("#messageRegister").show();
            setTimeout(function(){
                $("#messageRegister").hide();
            },time)
        }
    },
    sendSubmit(){
        var formData = new FormData(document.forms.namedItem("registerForm"))
        $.ajax({
            url:"/userReg/regAdd",
            method:"POST",
            data:formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success:function(data){
                 App.message(data.msg,2000);
                if(data.success){
                    setTimeout(function(){
                        window.location.href="/login"
                    },4000)
                }
            }
        })
    },
    checkFile(){
        var val = $("#userName").val();
        if(val == ""){
            alert("账号不能为空")
        }else{
           App.checkAccunt(val)
        }
    },
    checkAccunt(val){
        $.ajax({
            url:"/userReg/checkAccount",
            method:"GET",
            contentType:"application/json",
            dataType:'json',
            data:{
                role_id:"1",
                account:val
            },
            success:function(data){
                if( data.data == 1 ){
                    alert("账号已存在")
                }else{
                    App.sendSubmit();
                }
            },
            error:function(data){

            }
        })
    }
}
var UploadFile={
    innerText:function(){
        return "+";
    },
    getFile:function(node){
        var file = null;
        if(node.files.length>1){
            alert("单种类型文件限传一个附件");
        }else{
            if(node.files && node.files[0] ){
                file = node.files[0];
            }else if(node.files && node.files.item(0)) {
                file = node.files.item(0);
            }
        }
        return file;
    },
    fileSize:function(file){
        var flag;
        var size = Math.floor(file.size/1024);
        if (size > 2000) {
            flag = false;
            alert("附件不能超过2M");
        }else{
            flag = true;
        };
        return flag;
    },
    domBySrcUrl:function(url){
        return '<div>'+
               "<img width='100' height='75' src='"+ url +"' class='upload-img'/>"+
                "<span class='cross'></span>"+
            '</div>'
    },
    renderImg:function(file,node,dom){
        var  url = window.URL.createObjectURL(file);
        var textHtml = UploadFile.domBySrcUrl(url,node);
        dom.siblings().html(textHtml)
    },
    clear:function(dom){
        //this.clearInputFile(document.forms.namedItem("fileForm").elements[0]);
        //document.getElementById(this.state.imgId).value = "";
        dom.siblings().val("");
    },
    clearInputFile:function(f){
        if(f.value){
            try{
                f.value = ''; //for IE11, latest Chrome/Firefox/Opera...
            }catch(err){
            }
            if(f.value){ //for IE5 ~ IE10
                var form = document.createElement('form'), ref = f.nextSibling;
                form.appendChild(f);
                form.reset();
                ref.parentNode.insertBefore(f,ref);
            }
        }
    },
    fileChange:function(e,dom){
        var node=e.srcElement?e.srcElement:e.target;
        var file = UploadFile.getFile(node);
        if(file){
            var rightSize = this.fileSize(file);
            if(rightSize){
                UploadFile.renderImg(file,node.id,dom);
            }else{
                UploadFile.clear();
            }
        }else{
            UploadFile.clear();
        }
    }
}
$(function(){
    App.getAreaById(0,"#provinceCode");
     $("#roleId").on("change",function(){
         $("#roleIdPanel").toggle();
     });
    $("#provinceCode").on("change",function(){
        App.getAreaById(parseInt($(this).val()),"#cityCode")
    });
    $("#cityCode").on("change",function(){
        App.getAreaById(parseInt($(this).val()),"#areaCode")
    });
    $(".back-btn").on("click",function(){
        window.location.href = "/login";
    });
    $(".submit-btn").on("click",function(){
       App.checkFile();
    });
    $(".file").on("change",function(e){
        var dom = $(this);
        UploadFile.fileChange(e,dom);
    });
    $("#uploadFileTop").delegate(".cross","click",function(e){
        var dom = $(this).parent().parent();
        dom .html("+");
        UploadFile.clear(dom);
    });
    $(".title").hover(function(){
        $(this).attr("title",$(this).val())
    })
});
