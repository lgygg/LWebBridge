var bridge = {
    default:this,
    call: function (method, args, callback) {
        var ret = '';
        if (typeof args == 'function') {
            callback = args;
            args = {};
        }
        // 对应Request的args，callbackName
        var arg={args:args===undefined?null:args}
        if (typeof callback == 'function') {
            //回调函数的名字
            var cbName = 'LGYCallback' + window.callbackIndex++;
            window[cbName] = callback;
            arg['callbackName'] = cbName;
        }
        arg = JSON.stringify(arg)

        //window.LBridge对应WebView里的BRIDGE_NAME
        if(window.LBridge){
           ret= LBridge.call(method, arg)
        }else if(navigator.userAgent.indexOf("LBridge")!=-1){
           ret = prompt("LBridge=" + method, arg);
        }

       return JSON.parse(ret||'{}').data
    }
};

!function () {
    if (window._syncFunc) return;
    var ob = {
        //保存JS同步方法
        _syncFunc: {
            _obs: {}
        },
        //保存JS异步方法
        _asyncFunc: {
            _obs: {}
        },
        //标记回调函数，每当增加一个回调函数，就+1，这样就可以区别是哪个函数的回调
        callbackIndex: 0,
        LgyBridge: bridge
    }
    for (var attr in ob) {
        window[attr] = ob[attr]
    }
}();

module.exports = bridge;