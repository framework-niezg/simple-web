/**
 * Created by Administrator on 2017/2/10.
 */

var App = {
    query:function(code){
        App.ajax({
            url:"/queryCode",
            method:"GET",
            data:code,
            success:function(){

            },
            error:function(){

            }
        })
    },
    ajax:function(option){
        $.ajax(option)
    }
};
$(function(){
    $(".btn-query").on("click",function(){
        var code = $("#checkCode").val();
        if(code.trim()==""){
            alert("查询代码不能为空");
        }else{
            App.query(code);
        }
    })
});
