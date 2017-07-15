package com.example.gaurav.testaccessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by gaurav on 7/15/17.
 */
public class TestService extends AccessibilityEventCaptureService {
    public TestService() {
        super();
        this.setTag(TestService.class.getSimpleName());
    }

    @Override
    protected boolean isDebugMode() {
        return true;
    }
}
