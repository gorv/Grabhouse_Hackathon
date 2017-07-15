package com.example.gaurav.testaccessibility;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by gaurav on 7/15/17.
 */
public class AccessibilityServiceUtil {
    public static ArrayList<String> getAllAccessibilityServices(Context context) {
        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        ArrayList<String> allAccessibilityServices = new ArrayList<String>();

        String settingValue = Settings.Secure.getString(
                context.getApplicationContext().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        if (settingValue != null) {
            colonSplitter.setString(settingValue);
            while (colonSplitter.hasNext()) {
                String accessabilityService = colonSplitter.next();
                allAccessibilityServices.add(accessabilityService);
            }
        }
        return allAccessibilityServices;
    }

    public static boolean isAccessibilityServiceOn(Context context, String packageName, String className) {
        ArrayList<String> allAccessibilityServices = getAllAccessibilityServices(context);
        StringBuffer concat = new StringBuffer();
        concat.append(packageName);
        concat.append('/');
        concat.append(className);

        return allAccessibilityServices.contains(concat.toString());
    }
}
