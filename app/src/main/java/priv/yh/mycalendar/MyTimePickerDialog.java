package priv.yh.mycalendar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TimePicker;

public class MyTimePickerDialog extends AlertDialog {

    protected MyTimePickerDialog(Context context) {
        super(context);
    }

    protected MyTimePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected MyTimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyTimePickerDialog(Context context, MyTimePickerDialog.OnTimeSetListener listener,
                                 int hourOfDay1, int minute1, int hourOfDay2, int minute2, boolean is24HourView) {
        super(context, 0);
        //throw new RuntimeException("Stub!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_pick_view);
    }

    //We have two time pickers, so we need create a new interface
    public interface OnTimeSetListener {
        void onTimeSet(TimePicker tp1, int hourOfDay1, int minute1, TimePicker tp2, int hourOfDay2, int minute2);
    }
}
