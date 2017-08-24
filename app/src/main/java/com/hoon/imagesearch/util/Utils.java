package com.hoon.imagesearch.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    public static void showKeyboard(@NonNull Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(getCurrentFocus(activity), 0);
        }
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus(activity).getWindowToken(), 0);
        }
    }

    private static View getCurrentFocus(@NonNull Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = activity.getWindow().getDecorView();
        }
        return view;
    }
}
