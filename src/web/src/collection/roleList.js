/**
 * Created by Administrator on 2017/4/7.
 */
import Role from "src/models/role";
class RoleList{
    constructor(data){
        this.default = data;
        this.models= [];
        this.reset(JSON.parse(JSON.stringify(this.default)));
    }
    reset(items){
        let _this = this;
        items.map(function(item){
           let simple = new Role(item);
            _this.models.push(simple.getModel());
       })
    }
    getModels(){
        return this.models;
    }
    getModel(i){
        return this.models[i]
    }
}
export default RoleList


