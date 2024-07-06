package pointclub.shared.service;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import pointclub.shared.activity.RegisterActivity;
import pointclub.shared.enums.RegisterOption;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;

public class RegisterService {
    protected final ActivityLauncherService<Intent, ActivityResult> activityLauncher;
    private final AppCompatActivity context;

    public RegisterService(AppCompatActivity context) {
        this.context = context;
        activityLauncher = ActivityLauncherService.registerActivityForResult(context);
    }

    public void register(RegisterOption registerOption, Callable callback) {
        Intent intent = buildRegisterIntent(registerOption);
        launchRegisterActivity(intent, callback);
    }

    @NonNull
    private Intent buildRegisterIntent(RegisterOption registerOption) {
        String title = getRegisterTitle(registerOption);
        Intent intent = new Intent(context, RegisterActivity.class);
        intent = intent.putExtra("option", registerOption);
        intent = intent.putExtra("title", title);
        return intent;
    }

    private String getRegisterTitle(RegisterOption registerOption) {
        switch (registerOption) {
            case ROOM:
                LogService.info(LogTag.REGISTER, "Start Room Register");
                return "Please Enter Room Name";
            case USER:
                LogService.info(LogTag.REGISTER, "Start User Register");
                return "I see it's your first time in pointclub!\nPlease Register";
            default:
                return "Register";
        }
    }

    private void launchRegisterActivity(Intent intent, Callable callback) {
        activityLauncher.launch(intent, result -> {
            if (isActivityResultOK(result)) {
                Intent registerData = result.getData();
                if (registerData != null) {
                    callback.call((RegisterActivity.RegisterResult) registerData.getExtras().get("result"));
                } else {
                    LogService.error(LogTag.REGISTER, "Failed to Register");
                }
            }
        });
    }

    private boolean isActivityResultOK(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            return true;
        } else {
            LogService.error(LogTag.REGISTER, "Activity failed");
            return false;
        }
    }

    public interface Callable {
        void call(RegisterActivity.RegisterResult result);
    }
}
