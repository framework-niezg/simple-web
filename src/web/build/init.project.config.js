/**
 * @param a means b
 * Created by why on 2017/01/05.
 * lastUser: why
 * Date: 2017/01/05
 * Time: 16:20
 */
var fs = require("fs"),
    path = require("path"),
    package = require('./package.json');

var project = package.name,//项目名称
    folder = package.folder,//项目内文件夹
    files = package.files;//项目内files

/*创建项目文件夹名字*/
mkdirs(package.name,"0777",function(){
    console.log("文件夹创建成功")
})

function mkdirs(dirname, mode, callback){
    fs.exists(dirname, function (exists){
        if(exists){
            callback();
        }else{
            mkdirs(path.dirname(dirname), mode, function (){
                fs.mkdir(dirname, mode, callback);
            });
        }
    });
}

