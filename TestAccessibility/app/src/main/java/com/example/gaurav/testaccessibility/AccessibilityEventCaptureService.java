package com.example.gaurav.testaccessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.InputStream;
import java.util.List;

/**
 * Created by gaurav on 7/15/17.
 */
public abstract class AccessibilityEventCaptureService extends AccessibilityService {

    public static final String ACTION_CAPTURE_NOTIFICATION = "action_capture_notification";
    public static final String EXTRA_NOTIFICATION_TYPE = "extra_notification_type";
    public static final String EXTRA_PACKAGE_NAME = "extra_package_name";
    public static final String EXTRA_MESSAGE = "extra_message";

    public static final int EXTRA_TYPE_NOTIFICATION = 0x19;
    public static final int EXTRA_TYPE_OTHERS = EXTRA_TYPE_NOTIFICATION + 1;

    public static String TAG = AccessibilityEventCaptureService.class.getSimpleName();


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        Log.i("Event", event.toString());
        Log.i("Source", source.toString());
        Log.i("Child Count", Integer.toString(source.getChildCount()));
        if (source == null) {
            return;
        }
    }


    protected abstract boolean isDebugMode();

    protected void setTag(String tag) {
        AccessibilityEventCaptureService.TAG = tag;
    }

    public AccessibilityEventCaptureService() {
    }

    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info=new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        info.packageNames = new String[] {"com.whatsapp","com.facebook","com.zomato"};
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS; info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY; info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        info.notificationTimeout = 0;
        this.setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
    }

    private boolean hasMessage(AccessibilityEvent event) {
        return event != null && (event.getText().size() > 0);
    }
}
