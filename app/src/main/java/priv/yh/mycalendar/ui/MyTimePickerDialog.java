package priv.yh.mycalendar.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import priv.yh.mycalendar.R;

/**
 * on-duty and off-duty time picker
 *
 * @author moonleo
 * @date 2019/02/22
 */
public class MyTimePickerDialog extends Dialog implements View.OnClickListener{

    private TimePicker onDutyTimePicker;
    private TimePicker offDutyTimePicker;
    private Button okBtn;
    private Button cancelBtn;
    private OnTimeSetListener mListener;

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
            default :
                break;
        }
    }

    /** We have two time pickers, so we need create a new interface */
    public interface OnTimeSetListener {
        /**
         * the method is called after user have set their on-duty and off-duty time, and the parameters
         * are detailed time
         *
         * @param tp1 first time picker object
         * @param hourOfDay1 hour of first time picker that user is set
         * @param minute1 minute of first time picker that user is set
         * @param tp2 second time picker object
         * @param hourOfDay2 hour of second time picker that user is set
         * @param minute2 minute of time picker that user is set
         */
        void onTimeSet(TimePicker tp1, int hourOfDay1, int minute1, TimePicker tp2, int hourOfDay2,
                       int minute2);
    }
}
