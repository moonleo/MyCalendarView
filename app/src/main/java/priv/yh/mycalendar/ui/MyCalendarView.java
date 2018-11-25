package priv.yh.mycalendar.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import priv.yh.mycalendar.R;
import priv.yh.mycalendar.model.DayEvent;
import priv.yh.mycalendar.utils.Constants;
import priv.yh.mycalendar.utils.MyDBManager;
import priv.yh.mycalendar.utils.Utils;

/**
 * Created by yzh on 2018/8/19.
 */

public class MyCalendarView extends LinearLayout
        implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private ImageView preMonthBtn;
    private ImageView nextMonthBtn;
    private TextView dateText;

    private GridView gridView;

    private Calendar mCalendar = Calendar.getInstance();

    private MyDBManager mDBManager;

    public MyCalendarView(Context context) {
        super(context);
        initView(context);
        refreshView();
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context);
        refreshView();
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData(context);
        refreshView();
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.calendar_view, this);

        preMonthBtn = findViewById(R.id.btnPreMonth);
        nextMonthBtn = findViewById(R.id.btnNextMonth);
        dateText = findViewById(R.id.txtDate);

        gridView = findViewById(R.id.calendar_grid);

        bindEvent();
    }

    private void initData(Context context) {
        mDBManager = MyDBManager.getInstance(context);
    }

    private void bindEvent() {
        if (preMonthBtn != null) {
            preMonthBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCalendar.add(Calendar.MONTH, -1);
                    refreshView();
                }
            });
        }

        if (nextMonthBtn != null) {
            nextMonthBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCalendar.add(Calendar.MONTH, +1);
                    refreshView();
                }
            });
        }
    }

    private void refreshView() {
        //set current month and year
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_STRING);
        dateText.setText(sdf.format(mCalendar.getTime()));

        //set every day of current month
        ArrayList<Date> dates = new ArrayList<>();

        Calendar curDate = (Calendar) mCalendar.clone();
        curDate.set(Calendar.DAY_OF_MONTH, 1);
        int preDays = curDate.get(Calendar.DAY_OF_WEEK) - 1;
        curDate.add(Calendar.DAY_OF_MONTH, -preDays);

        for (int i = 0; i < Constants.MAX_DATE_CELL_NUM; i++) {
            dates.add(curDate.getTime());
            curDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridView.setAdapter(new MyCalendarAdapter(getContext(), R.layout.day_view, dates));
        //bind long click event
        gridView.setOnItemLongClickListener(this);
        //bind click event
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String workedStr = null;
        TextView clickedView = view.findViewById(R.id.day_cur_textview);
        int clickedDate = Integer.parseInt(clickedView.getText().toString());
        List<DayEvent> list = mDBManager.queryDayEvents(mCalendar.getTime().getYear(),
                mCalendar.getTime().getMonth(), clickedDate);
        if (list != null && list.size() > 0) {
            workedStr = String.valueOf(list.get(0).getManHour());
        }
        String toastStr = TextUtils.isEmpty(workedStr) ?
                getContext().getString(R.string.worked_no_data_string) :
                String.format(getContext().getString(R.string.worked_time_string), workedStr);
        Toast.makeText(getContext(), toastStr, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        final boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        final ImageView imageView = view.findViewById(R.id.day_cur_event_imageview);
        final TextView clickedView = view.findViewById(R.id.day_cur_textview);
        final int clickedDay = Integer.parseInt(clickedView.getText().toString());
        new MyTimePickerDialog(getContext(), new MyTimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker tp1, int hourOfDay1, int minute1,
                                  TimePicker tp2, int hourOfDay2, int minute2) {
                Log.e("moonleo", "hourOfDay1=" + hourOfDay1 + ", hourOfDay2=" + hourOfDay2
                        + ", minute1=" + minute1 + ", minute2=" + minute2);
                if (hourOfDay1 > hourOfDay2) {
                    //imageView.setImageResource(R.drawable.black);
                    return;
                }
                if (hourOfDay1 == hourOfDay2) {
                    if (minute1 >= minute2) {
                        //imageView.setImageResource(R.drawable.black);
                        return;
                    }
                }
                int min = minute2 - minute1;
                int hour1 = hourOfDay1;
                int hour2 = hourOfDay2;
                if (min < 0) {
                    hour2 -= 1;
                }
                double result = Utils.formatDouble(hour2 - hour1 + min / 60);
                Date clickedViewDate = mCalendar.getTime();
                Log.e("moonleo", "year:" + clickedViewDate.getYear() +
                        "month:" + clickedViewDate.getMonth() +
                        "day:" + +clickedDay+
                        "worked :" + result);
                mDBManager.insertManHour(new DayEvent(clickedViewDate.getYear(),
                        clickedViewDate.getMonth(), clickedDay, result));
                imageView.setVisibility(View.VISIBLE);
                if (Utils.isEnoughManHour(result)) {
                    imageView.setImageResource(R.drawable.green);
                } else {
                    imageView.setImageResource(R.drawable.red);
                }
            }
        }, is24HourFormat).show();
        return true;
    }

    private class MyCalendarAdapter extends ArrayAdapter<Date> {
        private int mResourceId;
        private LayoutInflater inflater;
        private ViewHolder holder;

        public MyCalendarAdapter(@NonNull Context context, int resource, ArrayList<Date> dates) {
            super(context, resource, dates);
            inflater = LayoutInflater.from(context);
            this.mResourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date curDayCell = getItem(position);
            Date today = new Date();
            boolean isCurMonth = (mCalendar.get(Calendar.MONTH) == curDayCell.getMonth());
            if (convertView == null) {
                convertView = inflater.inflate(mResourceId, null);
                holder = new ViewHolder();
                holder.backgroundImageView = convertView.findViewById(R.id.day_bg_imageview);
                holder.dayTextView = convertView.findViewById(R.id.day_cur_textview);
                holder.dayEventImageView = convertView.findViewById(R.id.day_cur_event_imageview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.dayTextView.setText(String.valueOf(curDayCell.getDate()));
            holder.dayTextView.setTextColor(isCurMonth ?
                    getResources().getColor(R.color.color_cur_month_day) :
                    getResources().getColor(R.color.color_not_cur_month_day));
            if (today.getDate() == curDayCell.getDate()
                    && today.getMonth() == curDayCell.getMonth()
                    && today.getYear() == curDayCell.getYear()
                    /*for other month not set today-flag*/
                    && mCalendar.get(Calendar.MONTH) == today.getMonth()) {
                holder.backgroundImageView.setVisibility(VISIBLE);
                holder.dayTextView.setTextColor(getResources().getColor(R.color.color_cur_day_text));
            }
            Log.e("moonleo","view " + curDayCell.getYear()+"/"+curDayCell.getMonth()+"/"+curDayCell.getDate());
            List<DayEvent> dayEvents = mDBManager.queryDayEvents(curDayCell.getYear(), curDayCell.getMonth(),
                    curDayCell.getDate());
            Log.e("moonleo", "dayEvents.size = " + dayEvents.size());
            if(dayEvents != null && dayEvents.size() > 0) {
                DayEvent curDayEvent = dayEvents.get(0);
                if (curDayEvent.getManHour() != 0) {
                    holder.dayEventImageView.setVisibility(View.VISIBLE);
                    if(Utils.isEnoughManHour(curDayEvent.getManHour())) {
                        holder.dayEventImageView.setImageResource(R.drawable.green);
                    } else {
                        holder.dayEventImageView.setImageResource(R.drawable.red);
                    }
                } else {
                    holder.dayEventImageView.setVisibility(View.INVISIBLE);
                }
            }
            return convertView;
        }

        class ViewHolder {
            ImageView backgroundImageView;
            TextView dayTextView;
            ImageView dayEventImageView;
        }
    }

}
