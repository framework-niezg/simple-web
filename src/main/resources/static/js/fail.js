/**
 * Created by Administrator on 2017/2/10.
 */

var Fail = {
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
    domBySrcUrl:function(url,dom,id){
        var textHtml = "<img width='80'height='80' src='"+url+"'class='upload-img'/>";
        textHtml += "<span id='"+dom+"Delete' class='cross' name='"+ id +"'></span>";
        return textHtml;
    },
    fileSize:function(file,id){
        var flag;
        var size = Math.floor(file.size/1024);
        if (size > 2000) {
            var text = Fail.innerText(id);
            flag = false;
            alert(text+"不能超过2M");
        }else{
           flag = true;
        };
        return flag;
    },
    renderImg:function(file,node){
        var  url = window.URL.createObjectURL(file);
        var textHtml = Fail.domBySrcUrl(url,node);
        $("#"+node+"Html").html(textHtml);
        $("#"+node+"Delete").click(function(){
            $("#"+node+"Html").html(Fail.innerText(node));
            $("#"+node).val("");
        });
    },
    clear:function(id){
        $("#"+id).val("");
    },
    getAreaById:function(id,dom,current){
        Fail.ajax({
            url:"/dict/getAreaList",
            method:"GET",
            dataType:'json',
            data:{
                parentid:id
            },
            success:function(data){
                Fail.renderArea(dom,data,current)
            },
            error:function(){

            }

        })
    },
    renderArea:function(dom,data,current){
        $(dom).html(Fail.getDom(data.data,current,dom));
        Fail.renderChild(dom,current);
    },
    renderChild:function(dom,current){
        switch(dom){
            case"#province_code":
                Fail.getAreaById(parseInt( $(dom).val()),"#city_code",current);
                break;
            case"#city_code":
                Fail.getAreaById(parseInt( $(dom).val()),"#area_code",current);
                break;
            default:
                break;
        }
    },
    getDom:function(data,current,dom){
        console.log(data,current,dom)
       /* var options = "<option value='-1'>请选择</option>";*/
        var options = "",cu="-2";
        if(current){
             cu = current[dom.substring(1)]
            $.each(data,function(i){
                var area = data[i];
                if(area.codeid == cu){
                    options+='<option value="'+ area.codeid +'" selected="selected">'+ area.name +'</option>';
                }else{
                    options+='<option value="'+ area.codeid +'">'+ area.name +'</option>';
                }

            })
        }else{
            $.each(data,function(i){
                var area = data[i];
                options+='<option value="'+ area.codeid +'">'+ area.name +'</option>';
            })
        }

        return options;
    },
    getItem(name){
        if(window.localStorage){
            return JSON.parse(window.localStorage.getItem(name))
        }else{
            return JSON.parse(window.local[name])
        }
    },
    renderRegister(data){
        var role_id = data.role_type;
        if(role_id == 2){
            $("#roleIdPanel").hide();
        }
        Fail.getAreaById(0,"#province_code",data);
        $.each(data,function(k,v){
            var $dom = $("#"+k);
            switch(k){
                case"role_type":
                    $("#role_id").val(v)
                    break;
               case"ent_type":
                   $("#ent_type").val(v)
                    break;
                case"files":
                    Fail.renderFile(v)
                    break;
                case"province_code":
                    $dom.val( data["province_code"])
                    break;
                case"city_code":
                    $dom.val( data["city_code"])
                    break;
                case"area_code":
                    $dom.val(data["county_code"])
                    break;
                default:
                    $dom.val(v);
                    break;
            }
        })
    },
    renderFile(data){
        let _this =this;
        $.each(data,function(i){
            var k = i+1,id=data[i].id;
            _this.renderImgById("img"+k,id)
        });
    },
    renderImgById(dom,id){//"img1",59
        var html = Fail.domBySrcUrl("/userReg/getRegFile?id="+id,dom,id);
        $("#"+dom+"Html").html(html);
        $("#"+dom+"Delete").click(function(){
            var id = $(this).attr("name");
            var removeId = $("#removeFileIds").val();
            var str = removeId == ""? id : removeId+","+id;
            $("#removeFileIds").val(str);
            $("#"+dom+"Html").html(Fail.innerText());
            $("#"+dom).val("");
        });
    },
    getUserRegister(){
        Fail.ajax({
            url:"/userReg/getReg",
            method:"GET",
            dataType:'json',
            success:function(data){
                Fail.renderRegister(data.data);
            },
            error:function(){

            }

        })
    },
    ajax:function(options){
        $.ajax(options)
    },
    initUser:function(){
        Fail.setItem("user",{
            id:$("#userId").attr("name"),
            userName :$("#userName").attr("name"),
            role_id :$("#role_Id").attr("name"),
            role_type :$("#role_type").attr("name"),
            account :$("#account").attr("name"),
            refid:$("#refid").attr("name")
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
}

$(function(){
    $("#removeFileIds").val("");
   // Fail.initUser();
    Fail.getUserRegister();
     $("#role_id").on("change",function(){
         $("#roleIdPanel").toggle();
     });
    $("#province_code").on("change",function(){
        Fail.getAreaById(parseInt($(this).val()),"#city_code")
    });
    $("#city_code").on("change",function(){
        Fail.getAreaById(parseInt($(this).val()),"#area_code")
    });
});
function fileChange(node){
    var file = Fail.getFile(node);
    if(file){
        var rightSize = Fail.fileSize(file,node.id);
        if(rightSize){
            Fail.renderImg(file,node.id);
        }else{
            Fail.clear(node.id);
        }
    }else{
        Fail.clear(node.id);
    }
}
function checkFile(){
    return true;
}