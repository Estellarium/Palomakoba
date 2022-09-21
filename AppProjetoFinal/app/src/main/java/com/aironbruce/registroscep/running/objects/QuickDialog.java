package com.aironbruce.registroscep.running.objects;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.aironbruce.registroscep.R;

public class QuickDialog {

    private final AlertDialog.Builder builder;
    private final Context context;
    private static int key;

    public QuickDialog(Context context, int dialogKey) {
        builder = new AlertDialog.Builder(context, R.style.Dialog);
        builder.setNeutralButton(R.string.ok, null);
        this.context = context;
        key = dialogKey;
    }

    public QuickDialog(Context context, int dialogKey, boolean autoButton) {
        builder = new AlertDialog.Builder(context, R.style.Dialog);
        if (autoButton) builder.setNeutralButton(R.string.ok, null);
        this.context = context;
        key = dialogKey;
    }

    @SuppressLint("NonConstantResourceId")
    public AlertDialog.Builder build() {
        //Acabei não usando todos os itens.
        switch (key) {

            case R.integer.HELP:
                builder.setTitle(R.string.info_title);
                builder.setMessage(R.string.info_text);
                break;

            case R.integer.NO_CONNECTION:
                builder.setTitle(R.string.connection_title);
                builder.setMessage(R.string.fail_connection);
                break;

            case R.integer.GALLERY_PERMISSION_DENIED:
                builder.setTitle(R.string.permission_title);
                builder.setMessage(R.string.fail_gallery);
                break;

            case R.integer.LOCATION_PERMISSION_DENIED:
                builder.setTitle(R.string.permission_title);
                builder.setMessage(R.string.fail_location);
                break;

            case R.integer.TOO_MANY_ITEMS:
                builder.setTitle(R.string.full_title);
                builder.setMessage(R.string.list_full);
                break;

            case R.integer.BATTERY_LOW:
                builder.setTitle(R.string.alert);
                builder.setMessage(R.string.battery_critical);
                break;

            case R.integer.CONFIRM_DELETE_LOCATION:
                builder.setTitle(R.string.delete_title);
                break;

            case R.integer.CONFIRM_DELETE_DATABASE:
                builder.setTitle(R.string.delete_title);
                builder.setMessage(R.string.warn_deletion);
                break;
        }
        return builder;
    }
}
