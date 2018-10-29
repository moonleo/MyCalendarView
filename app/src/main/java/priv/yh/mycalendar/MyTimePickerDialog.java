package priv.yh.mycalendar;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.sql.Time;

public class MyTimePickerDialog extends AlertDialog implements View.OnClickListener{

    private TimePicker onDutyTimePicker;
    private TimePicker offDutyTimePicker;
    private Button okBtn;
    private Button cancelBtn;
    private OnTimeSetListener mListener;

    /*protected MyTimePickerDialog(Context context) {
        super(context);
    }

    protected MyTimePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected MyTimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }*/

    protected MyTimePickerDialog(Context context, MyTimePickerDialog.OnTimeSetListener listener,
                                 boolean is24HourFormat) {
        super(context, 0);
        //throw new RuntimeException("Stub!");
        setOnTimeSetListener(listener);
        if(onDutyTimePicker != null) {
            onDutyTimePicker.setIs24HourView(is24HourFormat);
        }
        if(offDutyTimePicker != null) {
            offDutyTimePicker.setIs24HourView(is24HourFormat);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_pick_view);
        initView();
        bindEvent();
    }

    private void initView() {
        onDutyTimePicker = findViewById(R.id.timepicker_on_duty);
        offDutyTimePicker = findViewById(R.id.timepicker_off_duty);
        okBtn = findViewById(R.id.time_picker_ok_btn);
        cancelBtn = findViewById(R.id.time_picker_cancel_btn);
    }

    private void bindEvent() {
        if(okBtn != null) {
            okBtn.setOnClickListener(this);
        }
        if(cancelBtn != null) {
            cancelBtn.setOnClickListener(this);
        }
    }

    public void setOnTimeSetListener(OnTimeSetListener listener) {
        if(listener != null) {
            mListener = listener;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_picker_ok_btn:
                mListener.onTimeSet(onDutyTimePicker, onDutyTimePicker.getHour(),
                        onDutyTimePicker.getMinute(), offDutyTimePicker, offDutyTimePicker.getHour(),
                        offDutyTimePicker.getMinute());
                this.dismiss();
                break;
            case R.id.time_picker_cancel_btn:
                this.dismiss();
                break;
        }
    }

    //We have two time pickers, so we need create a new interface
    public interface OnTimeSetListener {
        void onTimeSet(TimePicker tp1, int hourOfDay1, int minute1, TimePicker tp2, int hourOfDay2, int minute2);
    }
}
