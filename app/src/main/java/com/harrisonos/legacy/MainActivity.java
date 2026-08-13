package com.harrisonos.legacy;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Settings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHomeScreen();
    }

    private void showHomeScreen() {

        home = new LinearLayout(this);
        home.setOrientation(LinearLayout.VERTICAL);
        home.setPadding(16, 16, 16, 16);
        home.setBackgroundColor(Color.rgb(22, 25, 30));

        // TITLE
        TextView title = new TextView(this);
        title.setText("HTC Mobile OS");
        title.setTextColor(Color.WHITE);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 20, 0, 10);

        home.addView(title);

        // VERSION
        TextView version = new TextView(this);
        version.setText("Legacy Edition");
        version.setTextColor(Color.LTGRAY);
        version.setTextSize(14);
        version.setGravity(Gravity.CENTER);
        version.setPadding(0, 0, 0, 30);

        home.addView(version);

        // PHONE BUTTON
        Button phone = makeButton("Phone");
        phone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchPackage("com.android.htcdialer");
            }
        });
        home.addView(phone);

        // MESSAGES BUTTON
        Button messages = makeButton("Messages");
        messages.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchPackage("com.android.mms");
            }
        });
        home.addView(messages);

        // INTERNET BUTTON
        Button internet = makeButton("Internet");
        internet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchPackage("com.android.browser");
            }
        });
        home.addView(internet);

        // CAMERA BUTTON
        Button camera = makeButton("Camera");
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                launchPackage("com.android.camera");
            }
        });
        home.addView(camera);

        // APPS BUTTON
        Button apps = makeButton("Apps");
        apps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showApps();
            }
        });
        home.addView(apps);

        // SETTINGS BUTTON
        Button settingsButton = makeButton("Settings");
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent settingsIntent =
                            new Intent(Settings.ACTION_SETTINGS);
                    startActivity(settingsIntent);
                } catch (Exception e) {
                    showMessage("Settings could not be opened.");
                }
            }
        });
        home.addView(settingsButton);

        // ABOUT BUTTON
        Button about = makeButton("About HTC Mobile OS");
        about.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAbout();
            }
        });
        home.addView(about);

        TextView bottom = new TextView(this);
        bottom.setText("HTC Mobile OS • Legacy");
        bottom.setTextColor(Color.GRAY);
        bottom.setTextSize(12);
        bottom.setGravity(Gravity.CENTER);
        bottom.setPadding(0, 35, 0, 15);

        home.addView(bottom);

        ScrollView scroll = new ScrollView(this);
        scroll.addView(home);

        setContentView(scroll);
    }

    private Button makeButton(String name) {

        Button button = new Button(this);

        button.setText(name);
        button.setTextSize(18);
        button.setTextColor(Color.WHITE);
        button.setBackgroundDrawable(
                new ColorDrawable(Color.rgb(55, 60, 70))
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        70
                );

        params.setMargins(0, 6, 0, 6);

        button.setLayoutParams(params);

        return button;
    }

    private void launchPackage(String packageName) {

        try {

            PackageManager pm = getPackageManager();

            Intent launch =
                    pm.getLaunchIntentForPackage(packageName);

            if (launch != null) {
                startActivity(launch);
            } else {
                showMessage("This app could not be opened.");
            }

        } catch (Exception e) {

            showMessage("This app could not be opened.");

        }
    }

    private void showApps() {

        final Intent launcherIntent =
                new Intent(Intent.ACTION_MAIN, null);

        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getPackageManager();

        final List<ResolveInfo> apps =
                pm.queryIntentActivities(launcherIntent, 0);

        if (apps == null || apps.size() == 0) {
            showMessage("No apps found.");
            return;
        }

        final String[] appNames =
                new String[apps.size()];

        for (int i = 0; i < apps.size(); i++) {

            ResolveInfo info = apps.get(i);

            CharSequence label =
                    info.loadLabel(pm);

            if (label != null) {
                appNames[i] = label.toString();
            } else {
                appNames[i] = "App";
            }
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Apps");

        builder.setItems(
                appNames,
                new DialogInterface.OnClickListener() {

                    public void onClick(
                            DialogInterface dialog,
                            int which) {

                        try {

                            ResolveInfo selected =
                                    apps.get(which);

                            Intent launch =
                                    new Intent(
                                            Intent.ACTION_MAIN
                                    );

                            launch.addCategory(
                                    Intent.CATEGORY_LAUNCHER
                            );

                            launch.setClassName(
                                    selected.activityInfo.packageName,
                                    selected.activityInfo.name
                            );

                            startActivity(launch);

                        } catch (Exception e) {

                            showMessage(
                                    "Unable to open this app."
                            );

                        }
                    }
                }
        );

        builder.setNegativeButton(
                "Close",
                null
        );

        builder.show();
    }

    private void showAbout() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("HTC Mobile OS");

        builder.setMessage(
                "HTC Mobile OS Legacy Edition\n\n" +
                "Designed as a lightweight interface " +
                "for older HTC Android devices.\n\n" +
                "Version R1.1"
        );

        builder.setPositiveButton(
                "OK",
                null
        );

        builder.show();
    }

    private void showMessage(String message) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setMessage(message);

        builder.setPositiveButton(
                "OK",
                null
        );

        builder.show();
    }
                }
