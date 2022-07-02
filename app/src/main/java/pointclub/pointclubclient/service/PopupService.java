package pointclub.pointclubclient.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pointclub.pointclubclient.R;
import pointclub.pointclubclient.activity.MainActivity;

public class PopupService {

    public static void showPopup(AppCompatActivity activity) {

    }

    public static void showPopup(MainActivity activity, Callable callback, String message) {
        View customView = createPopupView(activity, message);
        PopupWindow popupWindow = buildPopupWindow(customView);
        View rootView = activity.findViewById(android.R.id.content).getRootView();
        rootView.post(() -> {
            popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            setCallback(callback, customView, popupWindow);
        });
    }

    private static void setCallback(Callable function, View customView, PopupWindow popupWindow) {
        customView.findViewById(R.id.closePopupBtn).setOnClickListener(v -> {
            popupWindow.dismiss();
            function.call();
        });
    }

    @NonNull
    private static PopupWindow buildPopupWindow(View customView) {
        PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(androidx.appcompat.R.style.Animation_AppCompat_Dialog);
        return popupWindow;
    }

    private static View createPopupView(MainActivity activity, String message) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customView = layoutInflater.inflate(R.layout.popup, null);
        ((TextView)customView.findViewById(R.id.popupMessage)).setText(message);
        return customView;
    }

    public interface Callable{
        void call();
    }
}
