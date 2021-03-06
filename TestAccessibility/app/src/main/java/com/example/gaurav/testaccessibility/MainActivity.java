package com.example.gaurav.testaccessibility;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    TextView test_service_status;
    Button setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test_service_status = (TextView) findViewById(R.id.test_service_status);
        setting = (Button) findViewById(R.id.setting);
        setting.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        boolean isSet = AccessibilityServiceUtil.isAccessibilityServiceOn(
                getApplicationContext(),
                "com.example.gaurav.testaccessibility",
                "com.example.gaurav.testaccessibility.TestService");

        if (isSet) {
            test_service_status.setText(R.string.test_service_on);
        } else {
            test_service_status.setText(R.string.test_service_off);
        }

        super.onResume();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                Intent accessibilityServiceIntent
                        = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                //PackageManager pm = getPackageManager();
                //Intent intentWht=pm.getLaunchIntentForPackage(WhtNotificationService.PACKAGE_NAME);

                startActivity(accessibilityServiceIntent);
                break;
        }
    }
}
