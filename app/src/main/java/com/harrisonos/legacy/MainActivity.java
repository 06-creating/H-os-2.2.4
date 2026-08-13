package com.harrisonos.legacy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    private LinearLayout root;
    private TextView clockText;
    private TextView dateText;

    private final Handler clockHandler = new Handler();

    private final Runnable clockUpdater = new Runnable() {
        public void run() {
            updateClock();
            clockHandler.postDelayed(this, 30000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHome();
        clockHandler.post(clockUpdater);
    }

    @Override
    protected void onDestroy() {
        clockHandler.removeCallbacks(clockUpdater);
        super.onDestroy();
    }

    private void showHome() {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(12, 8, 12, 8);

        GradientDrawable background = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {
                        Color.rgb(20, 35, 48),
                        Color.rgb(9, 12, 18)
                }
        );

        root.setBackgroundDrawable(background);

        createStatusBar();
        createClockArea();
        createMainApps();
        createDock();

        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);

        setContentView(scroll);
    }

    private void createStatusBar() {

        LinearLayout bar = new LinearLayout(this);
        bar.setOrientation(LinearLayout.HORIZONTAL);
        bar.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(this);
        title.setText("HTC Mobile OS");
        title.setTextColor(Color.WHITE);
        title.setTextSize(14);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );

        bar.addView(title, titleParams);

        Button quick = new Button(this);
        quick.setText("Quick");
        quick.setTextSize(11);

        quick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showQuickSettings();
            }
        });

        bar.addView(quick);

        root.addView(bar);
    }

    private void createClockArea() {

        clockText = new TextView(this);
        clockText.setTextColor(Color.WHITE);
        clockText.setTextSize(42);
        clockText.setGravity(Gravity.CENTER);
        clockText.setPadding(0, 25, 0, 0);

        root.addView(clockText);

        dateText = new TextView(this);
        dateText.setTextColor(Color.LTGRAY);
        dateText.setTextSize(15);
        dateText.setGravity(Gravity.CENTER);
        dateText.setPadding(0, 0, 0, 20);

        root.addView(dateText);

        updateClock();
    }

    private void updateClock() {

        Date now = new Date();

        if (clockText != null) {
            clockText.setText(DateFormat.format("h:mm", now));
        }

        if (dateText != null) {
            dateText.setText(DateFormat.format("EEEE, d MMMM", now));
        }
    }

    private void createMainApps() {

        TextView heading = new TextView(this);
        heading.setText("HOME");
        heading.setTextColor(Color.LTGRAY);
        heading.setTextSize(12);
        heading.setPadding(4, 8, 0, 8);

        root.addView(heading);

        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);

        row1.addView(
                createTile("PHONE", "Call", new View.OnClickListener() {
                    public void onClick(View v) {
                        openPhone();
                    }
                }),
                tileParams()
        );

        row1.addView(
                createTile("MSG", "Messages", new View.OnClickListener() {
                    public void onClick(View v) {
                        openMessages();
                    }
                }),
                tileParams()
        );

        root.addView(row1);

        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);

        row2.addView(
                createTile("WEB", "Internet", new View.OnClickListener() {
                    public void onClick(View v) {
                        openInternet();
                    }
                }),
                tileParams()
        );

        row2.addView(
                createTile("CAM", "Camera", new View.OnClickListener() {
                    public void onClick(View v) {
                        openCamera();
                    }
                }),
                tileParams()
        );

        root.addView(row2);

        LinearLayout row3 = new LinearLayout(this);
        row3.setOrientation(LinearLayout.HORIZONTAL);

        row3.addView(
                createTile("APPS", "All Apps", new View.OnClickListener() {
                    public void onClick(View v) {
                        showApps();
                    }
                }),
                tileParams()
        );

        row3.addView(
                createTile("SET", "Settings", new View.OnClickListener() {
                    public void onClick(View v) {
                        openSettings();
                    }
                }),
                tileParams()
        );

        root.addView(row3);
    }

    private LinearLayout.LayoutParams tileParams() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        115,
                        1
                );

        params.setMargins(5, 5, 5, 5);

        return params;
    }

    private View createTile(
            String iconText,
            String label,
            View.OnClickListener listener) {

        LinearLayout tile = new LinearLayout(this);
        tile.setOrientation(LinearLayout.VERTICAL);
        tile.setGravity(Gravity.CENTER);
        tile.setPadding(5, 5, 5, 5);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(42, 54, 66));
        bg.setCornerRadius(10);

        tile.setBackgroundDrawable(bg);

        TextView icon = new TextView(this);
        icon.setText(iconText);
        icon.setTextColor(Color.WHITE);
        icon.setTextSize(20);
        icon.setGravity(Gravity.CENTER);

        tile.addView(icon);

        TextView text = new TextView(this);
        text.setText(label);
        text.setTextColor(Color.WHITE);
        text.setTextSize(14);
        text.setGravity(Gravity.CENTER);
        text.setPadding(0, 6, 0, 0);

        tile.addView(text);

        tile.setOnClickListener(listener);

        return tile;
    }

    private void createDock() {

        TextView divider = new TextView(this);
        divider.setText("");
        divider.setHeight(20);
        root.addView(divider);

        LinearLayout dock = new LinearLayout(this);
        dock.setOrientation(LinearLayout.HORIZONTAL);

        dock.addView(
                dockButton("Phone", new View.OnClickListener() {
                    public void onClick(View v) {
                        openPhone();
                    }
                }),
                dockParams()
        );

        dock.addView(
                dockButton("Messages", new View.OnClickListener() {
                    public void onClick(View v) {
                        openMessages();
                    }
                }),
                dockParams()
        );

        dock.addView(
                dockButton("Apps", new View.OnClickListener() {
                    public void onClick(View v) {
                        showApps();
                    }
                }),
                dockParams()
        );

        root.addView(dock);

        TextView version = new TextView(this);
        version.setText("HTC Mobile OS  •  Legacy Edition R2");
        version.setTextColor(Color.GRAY);
        version.setTextSize(11);
        version.setGravity(Gravity.CENTER);
        version.setPadding(0, 18, 0, 10);

        root.addView(version);
    }

    private Button dockButton(
            String text,
            View.OnClickListener listener) {

        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(12);
        button.setOnClickListener(listener);

        return button;
    }

    private LinearLayout.LayoutParams dockParams() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        65,
                        1
                );

        params.setMargins(3, 0, 3, 0);

        return params;
    }

    private void openPhone() {

        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            startActivity(intent);
        } catch (Exception e) {
            showMessage("Phone could not be opened.");
        }
    }

    private void openMessages() {

        try {
            PackageManager pm = getPackageManager();

            Intent intent =
                    pm.getLaunchIntentForPackage("com.android.mms");

            if (intent != null) {
                startActivity(intent);
            } else {
                showMessage("Messages could not be opened.");
            }

        } catch (Exception e) {
            showMessage("Messages could not be opened.");
        }
    }

    private void openInternet() {

        try {
            PackageManager pm = getPackageManager();

            Intent intent =
                    pm.getLaunchIntentForPackage("com.android.browser");

            if (intent != null) {
                startActivity(intent);
            } else {
                Intent web = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.google.com")
                );

                startActivity(web);
            }

        } catch (Exception e) {
            showMessage("Internet could not be opened.");
        }
    }

    private void openCamera() {

        try {
            Intent camera =
                    new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivity(camera);

        } catch (Exception e) {
            showMessage("Camera could not be opened.");
        }
    }

    private void openSettings() {

        try {
            Intent settings =
                    new Intent(Settings.ACTION_SETTINGS);

            startActivity(settings);

        } catch (Exception e) {
            showMessage("Settings could not be opened.");
        }
    }

    private void showQuickSettings() {

        final String[] items = {
                "Wi-Fi settings",
                "Bluetooth settings",
                "Display settings",
                "Main settings",
                "About HTC Mobile OS"
        };

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Quick Settings");

        builder.setItems(
                items,
                new DialogInterface.OnClickListener() {

                    public void onClick(
                            DialogInterface dialog,
                            int which) {

                        if (which == 0) {

                            launchSettings(
                                    Settings.ACTION_WIFI_SETTINGS
                            );

                        } else if (which == 1) {

                            launchSettings(
                                    Settings.ACTION_BLUETOOTH_SETTINGS
                            );

                        } else if (which == 2) {

                            launchSettings(
                                    Settings.ACTION_DISPLAY_SETTINGS
                            );

                        } else if (which == 3) {

                            openSettings();

                       
