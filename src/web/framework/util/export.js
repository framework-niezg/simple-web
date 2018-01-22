/**
 * Created by Administrator on 2016/8/19.
 * Util tool to format data
 */
import {message} from "antd"
message.config({
    top: 100,
    duration: 3
});
class Export{
    constructor(){
        this.init();
    }
}
Export.prototype={
    init(){
        this.explorer = this.getExplorer();
    },
    getExplorer(){
        let explorer = window.navigator.userAgent ;
        //ie
        if (explorer.indexOf("MSIE") >= 0){
            return 'ie';
        }
        //firefox
        else if (explorer.indexOf("Firefox") >= 0){
            return 'Firefox';
        }
        //Chrome
        else if(explorer.indexOf("Chrome") >= 0){
            return 'Chrome';
        }
        //Opera
        else if(explorer.indexOf("Opera") >= 0){
            return 'Opera';
        }
        //Safari
        else if(explorer.indexOf("Safari") >= 0){
            return 'Safari';
        }
    },
    exportExcel(id,fileName,key){
        if(this.explorer=='ie'){
            let curTbl = document.getElementById(id);
            let oXL = new ActiveXObject("Excel.Application");

            //创建AX对象excel
            let oWB = oXL.Workbooks.Add();
            //获取workbook对象
            let  xlsheet = oWB.Worksheets(1);
            //激活当前sheet
            let sel = document.body.createTextRange();
            sel.moveToElementText(curTbl);
            //把表格中的内容移到TextRange中
            sel.select;
            //全选TextRange中内容
            sel.execCommand("Copy");
            //复制TextRange中内容
            xlsheet.Paste();
            //粘贴到活动的EXCEL中
            oXL.Visible = true;
            //设置excel可见属性

            try {
                let fname = oXL.Application.GetSaveAsFilename("Excel.xls", "Excel Spreadsheets (*.xls), *.xls");
            } catch (e) {
                print("Nested catch caught " + e);
            } finally {
                oWB.SaveAs(fname);

                oWB.Close(savechanges = false);
                //xls.visible = false;
                oXL.Quit();
                oXL = null;
                //结束excel进程，退出完成
                //window.setInterval("Cleanup();",1);
                idTmr = window.setInterval(this.cleanup(), 1);

            }

        }else{
            this.tableToExcel(id,"统计报表",fileName,key);
        }
    },
    cleanup(){
        window.clearInterval(idTmr);
        CollectGarbage();
    },
    tableToExcel(table,name,fileName,key){
        var uri = 'data:application/vnd.ms-excel;base64,',
            template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--></head><body><table>{table}</table></body></html>',
            base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) },
            format = function(s, c) {
                return s.replace(/{(\w+)}/g,
                    function(m, p) { return c[p]; }) }
        if (!table.nodeType) table = document.getElementById(table)
        if(table&&table.innerHTML){
            var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
            document.getElementById("reportTable"+key).href = uri + base64(format(template, ctx));
            document.getElementById("reportTable"+key).download = fileName;
            document.getElementById("reportTable"+key).click()
        }else{
            message.warning("请先查询出有数据的报表");
        }

    }
}
export default Export

