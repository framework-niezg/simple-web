/**
 * @param a means b
 * Created by Administrator on 2016/11/14.
 * lastUser: Administrator
 * Date: 2016/11/14
 * Time: 16:02
 */
var fs = require( 'fs' );
var path = require( 'path' );

stat = fs.stat;
//复制文件夹、文件等
var copy = function( src, dst ){
    fs.readdir( src, function( err, paths ){
        if( err ){
            throw err;
        }
        paths.forEach(function( path ){
            var _src = src + '/' + path,
            _dst = dst + '/' + path,
            readable, writable;
            stat( _src, function( err, st ){
                if( err ){
                    throw err;
                }
                if( st.isFile() ){
                    readable = fs.createReadStream( _src );
                    writable = fs.createWriteStream( _dst );
                    readable.pipe( writable );
                }
                else if( st.isDirectory() ){
                    exists( _src, _dst, copy );
                }
            });
        });
    });
};
var exists = function( src, dst, callback ){
    mkdirs( dst,"0777",function(e){
        callback(src,dst)
    })
};
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
exists( './node_modules_zjcds', './node_modules', copy );
