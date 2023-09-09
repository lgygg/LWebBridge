package com.lgy.web.business;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.lgy.web.bridge.NameSpace;
import java.lang.ref.SoftReference;

/**
 * @author: Administrator
 * @date: 2023/9/8
 */
public class PhoneOperator implements NameSpace {
    @Override
    public String getNameSpace() {
        return "phone";
    }

    private SoftReference<Context> contextSoftReference;

    public PhoneOperator(Context context) {
        contextSoftReference = new SoftReference<>(context);
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    @SuppressLint("MissingPermission")
    @JavascriptInterface
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        contextSoftReference.get().startActivity(intent);
    }

    /**
     * 针对即使获取了拨打电话的权限依然报错问题的解决方案
     */
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @JavascriptInterface
    public void callPermission(String number) {
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(contextSoftReference.get(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) contextSoftReference.get(), Manifest.permission.CALL_PHONE)) {
                // 返回值：
                //如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                //如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                //如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                // 弹窗需要解释为何需要该权限，再次请求授权
                Toast.makeText(contextSoftReference.get(), "请授权！", Toast.LENGTH_SHORT).show();
                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", contextSoftReference.get().getPackageName(), null);
                intent.setData(uri);
                contextSoftReference.get().startActivity(intent);
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions((Activity) contextSoftReference.get(), new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        } else {
            // 已经获得授权，可以打电话
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            //url:统一资源定位符
            intent.setData(Uri.parse("tel:" + number));
            //开启系统拨号器
            contextSoftReference.get().startActivity(intent);
        }
    }

}
