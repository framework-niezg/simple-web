/**
 * Created by Administrator on 2016/8/19.
 * Util tool to format data
 */
import {message} from "antd"
message.config({
    top: 100,
    duration: 3
});
class Validate{
    constructor(){
        this.user_Boolean = false;//用户名、账号、
        this.password_Boolean = false;//密码
        this.confirm_Boolean = false;//确认密码
        this.emaile_Boolean = false;//邮箱
        this.mobile_Boolean = false;//手机
        this.id_Boolean = false;//身份证
        this.address_Boolean = false;//地址
        this.number_Boolean = false;//数字
        this.word_Boolean = false;//英文
        this.number_word_Boolean = false;//数字-英文
        this.length_Boolean = false;//长度
        //this.init(reg,value);
    }


}
Validate.prototype={
    init(reg,value,srcElement){
        this.dom = srcElement;
            if(value==""){
                $(this.dom).siblings().html("");
            }
            switch(reg){
                case"reg_email":
                    return value!=="" ? this.validateEmail(value) : {emaile_Boolean:true};
                case"reg_number":
                    return value!=="" ? this.validateNumber(value) : {number_Boolean:true};
                case"reg_mobile":
                    return value!=="" ? this.validateMobile(value) : {mobile_Boolean:true};
                case"reg_id":
                    return value!=="" ? this.validateId(value) : {id_Boolean:true};
                default:
                    this.validateUser();
                    this.validatePassword();
                    break;
            }
    },
    validateNumber(value){
        if ((/^[0-9]+$/).test(value)){
            //$(this.dom).siblings().html("✔").css("color","green");
            $(this.dom).siblings().html("");
            this.number_Boolean = true;
        }else {
            $(this.dom).siblings().html("只允许数字").css("color","red");
            this.number_Boolean = false;
        }
        return {"number_Boolean":this.number_Boolean};
    },
    validateId(value){
        if ((/^[0-9Xx]{18}$/).test(value)){
            //$(this.dom).siblings().html("✔").css("color","green");
            $(this.dom).siblings().html("");
            this.id_Boolean = true;
        }else {
            $(this.dom).siblings().html("身份证非法").css("color","red");
            this.id_Boolean = false;
        }
        return {"id_Boolean":this.id_Boolean};
    },
    validateUser(value){
        if ((/^[a-z0-9_-]{4,8}$/).test(value)){
            /* $('.user_hint').html("✔").css("color","green");*/
            $(this.dom).siblings().html("");
            this.user_Boolean = true;
        }else {
            /* $('.user_hint').html("×").css("color","red");*/
            this.user_Boolean = false;
            message.warning("账号非法")
        }
    },
    validatePassword(value){
        if ((/^[a-z0-9_-]{6,16}$/).test(value)){
            //$('.password_hint').html("✔").css("color","green");
            $(this.dom).siblings().html("");
            this.password_Boolean = true;
        }else {
            // $('.password_hint').html("×").css("color","red");
            this.password_Boolean = false;
            message.warning("6-16位小写字母请输入")
        }
    },
    validateEmail(value){
        if ((/^[a-z\d]+(\.[a-z\d]+)*@([\da-z](-[\da-z])?)+(\.{1,2}[a-z]+)+$/).test(value)){
            //$(this.dom).siblings().html("✔").css("color","green");
            $(this.dom).siblings().html("");
            this.emaile_Boolean = true;
        }else {
            $(this.dom).siblings().html("格式不正确").css("color","red");
            this.emaile_Boolean = false;
        }
        return {"emaile_Boolean":this.emaile_Boolean};
    },
    validateMobile(value){
        if ((/^1[34578]\d{9}$/).test(value)){
            //$(this.dom).siblings().html("✔").css("color","green");
            $(this.dom).siblings().html("");
            this.mobile_Boolean = true;
        }else {
            $(this.dom).siblings().html("格式不正确").css("color","red");
            this.mobile_Boolean = false;
        }
        return {"mobile_Boolean":this.mobile_Boolean};
    }
}
export default Validate

