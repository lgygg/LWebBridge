package com.lgy.web.bridge;

import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.lgy.web.bridge.util.NameSpaceUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2022/9/1
 *
 * 原生提供给js的基础接口
 */
public class BaseJavaScriptInterface implements IBaseJSInterface{

    IManage manager;
    HandleParameterStrategy parameterStrategy;

    public BaseJavaScriptInterface(IManage manager){
        this.manager = manager;
    }

    public void setParameterStrategy(HandleParameterStrategy parameterStrategy) {
        this.parameterStrategy = parameterStrategy;
    }

    /*
    * @methodName: 方法名，由模块名.方法名组成
    * @argStr: 传递的参数，必须是json格式
    * */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Keep
    @JavascriptInterface
    public String call(String methodName, String argStr) {
        Gson gson = new Gson();
        String[] nameStr = NameSpaceUtil.parseNameSpace(methodName.trim());
        methodName = nameStr[1];
        // 根据命名空间查找接口对象
        Object jsNativeObj = this.manager.getJavascriptObject(nameStr[0]);
        Response ret = new Response();
        // 无命名空间则返回结果
        if (jsNativeObj == null) {
            ret.setCode(Constant.CODE_NOT_NAMESPACE);
            ret.setMsg(Constant.MSG_CANNOT_FIND_JS_INTERFACE);
            ret.setData(null);
            this.manager.printDebugInfo(Constant.MSG_CANNOT_FIND_JS_INTERFACE);
            return gson.toJson(ret);
        }
        String arg = null;
        Method method = null;
        String callback = null;
        Request request = gson.fromJson(argStr,Request.class);
        callback = request.getCallbackName();
        arg = request.getArgs();

        Class<?> cls = jsNativeObj.getClass();
        boolean isAsync = false;
        Class[] argsClass = this.parameterStrategy.getParameterTypes(arg);
        try {
            Class[] mergedArr = Arrays.copyOf(argsClass, argsClass.length + 1);
            System.arraycopy(new Class[]{IResult.class}, 0, mergedArr, argsClass.length, 1);
            method = cls.getMethod(methodName, mergedArr);
            isAsync = true;
        } catch (Exception e) {
            try {

                method = cls.getMethod(methodName, argsClass);
            } catch (Exception ex) {

            }
        }

        // 找不到该函数
        if (method == null) {
            String error = String.format(Constant.MSG_METHOD_NOT_FIND,methodName);
            this.manager.printDebugInfo(error);
            ret.setCode(Constant.CODE_ERROR);
            ret.setMsg(error);
            ret.setData(null);
            return gson.toJson(ret);
        }

        //该函数没有JavascriptInterface标志，不允许访问
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            JavascriptInterface annotation = method.getAnnotation(JavascriptInterface.class);
            if (annotation == null) {
                String error = String.format(Constant.MSG_METHOD_NOT_PERMISSION,methodName);
                this.manager.printDebugInfo(error);
                ret.setCode(Constant.CODE_ERROR);
                ret.setMsg(error);
                ret.setData(null);
                return gson.toJson(ret);
            }
        }

        Object retData;
        method.setAccessible(true);
        try {
            Object[] afterArg = this.parameterStrategy.getParameter(arg);
            if (isAsync) {
                final String cb = callback;
                Object[] arr2 = new Object[]{new IResult() {

                    @Override
                    public void success(Object retValue) {
                        complete(retValue, true);
                    }

                    @Override
                    public void success() {
                        complete(null, true);
                    }

                    @Override
                    public void failure(Object exception) {

                    }

                    @Override
                    public void setProgressData(Object value) {
                        complete(value, false);
                    }

                    private void complete(Object retValue, boolean complete) {
                        try {

                            JSONObject ret = new JSONObject();
                            ret.put("code", 0);
                            ret.put("data", retValue);
                            //retValue = URLEncoder.encode(ret.toString(), "UTF-8").replaceAll("\\+", "%20");
                            if (cb != null) {
                                //String script = String.format("%s(JSON.parse(decodeURIComponent(\"%s\")).data);", cb, retValue);
                                String script = String.format("%s(%s.data);", cb, ret.toString());
                                if (complete) {
                                    script += "delete window." + cb;
                                }
//                                Log.d(LOG_TAG, "complete " + script);
                                BaseJavaScriptInterface.this.manager.evaluateJavascriptCode(script);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }};
                Object[] mergedArr = Arrays.copyOf(afterArg, argsClass.length + 1);
                System.arraycopy(arr2, 0, mergedArr, argsClass.length, 1);
                method.invoke(jsNativeObj, mergedArr);
            } else {
                retData = method.invoke(jsNativeObj, afterArg);
                ret.setCode(Constant.CODE_SUCCESS);
                ret.setMsg(Constant.MSG_SUCCESS);
                Map<String,Object> map = new HashMap();
                map.put("result",retData);
                ret.setData(map);
                return gson.toJson(ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String error = String.format("Call failed：The parameter of \"%s\" in Java is invalid.", methodName);
            this.manager.printDebugInfo(error);
            ret.setCode(Constant.CODE_ERROR);
            ret.setMsg(error);
            ret.setData(null);
            return gson.toJson(ret);
        }
        return ret.toString();
    }


    @Keep
    @JavascriptInterface
    public boolean existMethod(Object args) {
        JSONObject jsonObject = (JSONObject) args;
        // name表示方法名
        String methodName = null;
        // type表示函数类型：异步或同步
        String type = null;
        try {
            methodName = jsonObject.getString("name").trim();
            type = jsonObject.getString("type").trim();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] nameStr = NameSpaceUtil.parseNameSpace(methodName);
        Object jsb = this.manager.getJavascriptObject(nameStr[0]);
        if (jsb != null) {
            Class<?> cls = jsb.getClass();
            boolean asyn = false;
            Method method = null;
            try {
                method = cls.getMethod(nameStr[1],
                        new Class[]{Object.class, IResult.class});
                asyn = true;
            } catch (Exception e) {
                try {
                    method = cls.getMethod(nameStr[1], new Class[]{Object.class});
                } catch (Exception ex) {

                }
            }
            if (method != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    JavascriptInterface annotation = method.getAnnotation(JavascriptInterface.class);
                    if (annotation == null) {
                        return false;
                    }
                }
                if ((asyn && "asyn".equals(type) || (!asyn && "syn".equals(type)))) {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public String getNameSpace() {
        return "base";
    }
}
