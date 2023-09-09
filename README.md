# 流程图
![pic1](.\img\pic1.png)
# window的用法
由于window对象同时扮演着ES中的Global对象的角色，因此所有在全局作用域中声明的变量，函数都会变成window对象的属性和方法
```
var age = 29
function sayAge(){
    console.log(this.age)
}
console.log(window.age) //29
sayAge() //29
window.sayAge() //29
```

# JS方括号的作用
1、中括号语法可以用变量作为属性名或者访问，而点语法不可以;
```
    // 例1
    var obj = {};
    obj.name = '小王';
    var MyName = 'name';
    console.log(obj.name); //小王
    console.log(obj["name"]); //小王
    console.log(obj.MyName); //undefined
    console.log(obj[MyName]); //小王
    var MyName = 'name2';
    console.log(obj[MyName]); //undefined
    // 例2
    var person = {
      name: '哆啦'
    };
    console.log(person.name); //哆啦
    console.log(person["name"]); //哆啦
```

# JS如何删除变量

使用delete 关键字
如
删除全局变量 delete str; 或 delete window.str;
删除对象属性 detete str.string(外部操作)，detete this.string(内部操作)；

# !function是什么意思
!function跟(function(){... })();函数意义相同，叫做立即运行的匿名函数(也叫立即调用函数)。

# 使用

Android端

```
package com.lgy.testdsbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.lgy.web.BridgeManager;
import com.lgy.web.bridge.BridgeImpl;
import com.lgy.web.bridge.IBridge;
import com.lgy.web.bridge.IWebView;
import com.lgy.web.business.PhoneOperator;
import com.lgy.web.business.TestOperator;

public class TestLWebViewActivity extends AppCompatActivity {

    BridgeManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_lweb_view);

        IBridge bridge = new BridgeImpl();
        bridge.addJavascriptObject(new PhoneOperator(this));
        bridge.addJavascriptObject(new TestOperator());
        IWebView webView = findViewById(R.id.l_webview);
        manager = new BridgeManager(bridge,webView);
        manager.setDebug(true);

        ((WebView)webView).loadUrl("file:///android_asset/js-call-native-test.html");
    }
}
```

布局文件

```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".TestLWebViewActivity">

    <com.lgy.web.LWebView
        android:id="@+id/l_webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

HTML

```
<!DOCTYPE html>
<html>
<head lang="zh-cmn-Hans">
    <meta charset="UTF-8">
    <title>DSBridge Test</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="viewport" content="width=device-width,initial-scale=0.5,user-scalable=no"/>
    <script src="./LgyBridge.js"> </script>
</head>
<style>
    .btn {
        text-align: center;
        background: #d8d8d8;
        color: #222;
        padding: 20px;
        margin: 30px;
        font-size: 24px;
        border-radius: 4px;
        box-shadow: 4px 2px 10px #999;
    }

    .btn:active {
        opacity: .7;
        box-shadow: 4px 2px 10px #555;
    }

</style>
<body>
<div class="btn" onclick="callSyn()">Synchronous call</div>
<div class="btn" onclick="callAsyn()">Asynchronous call</div>

<script>
    function callSyn2() {

        alert('hello world')
    }
    function callSyn() {
        var arg = {arg0:'10000'}
        var jsonStr = LgyBridge.call("test.test1", JSON.stringify(arg));
        alert(JSON.stringify(jsonStr))
    }

    function callAsyn() {
    var arg = {arg0:'10000'}
        LgyBridge.call("test.test2",JSON.stringify(arg), function (v) {
            alert(v)
        })
    }



</script>
</body>
</html>

```

