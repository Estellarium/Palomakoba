package com.aironbruce.registroscep.running.objects;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import com.aironbruce.registroscep.R;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private static boolean under50, under15, under5, charging; //fazem com que os toasts apareçam
    //só uma vez por condição
    @Override
    public void onReceive(Context context, Intent intent) {

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;

        int pct = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float porcentagem = pct * 100 / (float) scale;

        if (isCharging) {
            if (!charging) {
                charging = true;
                Toast.makeText(context, R.string.loading, Toast.LENGTH_SHORT).show();
            }
        } else charging = false;

        if (porcentagem <= 50) {
            if (!under50) {
                under50 = true;
                Toast.makeText(context, context.getString(R.string.percentage,(int) porcentagem),
                        Toast.LENGTH_SHORT).show();
            }
        } else under50 = false;

        if (porcentagem <= 15) {
            if (!under15) {
                under15 = true;
                Toast.makeText(context, R.string.battery_low,
                        Toast.LENGTH_LONG).show();
            }
        } else under15 = false;

        if (porcentagem <= 5) {
            if (!under5) {
                under5 = true;
                Toast.makeText(context, R.string.battery_critical,
                        Toast.LENGTH_LONG).show();
            }
        } else under5 = false;
    }
}
