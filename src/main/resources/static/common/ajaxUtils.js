/**
 * Created by zhang tong on 2017/7/24.
 */

/**
 * 执行ajax请求，如果有数据成功返回的话，执行callback回调函数
 * @param url
 * @param params ajax请求的参数
 * @param callback
 */
function execAjax(url, params, callback) {
    var _arguments = arguments;
    $.ajax({
        url: url,
        type: 'post',
        data: params,
        error: function (result) {
            return false;
        },
        success: function (result) {
            //console.log(JSON.stringify(result));
            if (result && (typeof(callback) == 'function')) {
                callback(result);
            }
        },
        beforeSend: _arguments.length > 3 ? _arguments[3] : function () {   /*请求前*/
        },
        complete: _arguments.length > 3 ? _arguments[4] : function () { /*请求结束后*/
        }
    });
}

function execAjaxJSON(url, params, callback) {
    var _arguments = arguments;
    $.ajax({
        url: url,
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        data: JSON.stringify(params),
        error: function (result) {
            return false;
        },
        success: function (result) {
            //console.log(JSON.stringify(result));
            if (result && (typeof(callback) == 'function')) {
                callback(result);
            }
        },
        beforeSend: _arguments.length > 3 ? _arguments[3] : function () {   /*请求前*/
        },
        complete: _arguments.length > 3 ? _arguments[4] : function () { /*请求结束后*/
        }
    });
}