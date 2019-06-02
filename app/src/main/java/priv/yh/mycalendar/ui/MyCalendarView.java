package priv.yh.mycalendar.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import priv.yh.mycalendar.R;
import priv.yh.mycalendar.model.DayEvent;
import priv.yh.mycalendar.model.DayType;
import priv.yh.mycalendar.model.HolidayMode;
import priv.yh.mycalendar.utils.Constants;
import priv.yh.mycalendar.utils.DbManagerImpl;
import priv.yh.mycalendar.utils.HttpUtils;
import priv.yh.mycalendar.utils.JsonUtils;
import priv.yh.mycalendar.utils.Utils;

/**
 * My Calendar View
 *
 * @author moonleo
 * @date 2018/08/19
 */
public class MyCalendarView extends LinearLayout
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "MyCalendarView";

    private ImageView mPreMonthBtn;
    private ImageView mNextMonthBtn;
    private TextView mTitleDateText;

    private GridView mGridView;

    private LocalDate mCurDate = LocalDate.now();

    private DbManagerImpl mDBManager;

    private MyCalendarAdapter myCalendarAdapter;

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

        mPreMonthBtn = findViewById(R.id.btnPreMonth);
        mNextMonthBtn = findViewById(R.id.btnNextMonth);
        mTitleDateText = findViewById(R.id.txtDate);

        mGridView = findViewById(R.id.calendar_grid);

        bindEvent();
    }

    private void initData(Context context) {
        mDBManager = DbManagerImpl.getInstance(context);
    }

    private void bindEvent() {
        if (mPreMonthBtn != null) {
            mPreMonthBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCurDate = mCurDate.minusMonths(1);
                    refreshView();
                }
            });
        }

        if (mNextMonthBtn != null) {
            mNextMonthBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCurDate = mCurDate.plusMonths(1);
                    refreshView();
                }
            });
        }
    }

    private void refreshView() {
        //set current month and year at the title
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_STRING);
        mTitleDateText.setText(mCurDate.format(formatter));

        //record every day of current month
        ArrayList<DayEvent> dates = new ArrayList<>();

        //first day of cur month
        LocalDate dayCursor = mCurDate.with(TemporalAdjusters.firstDayOfMonth());
        //cal what day of the week it is because the calendar start at Sunday
        int dayOfWeek = dayCursor.getDayOfWeek().getValue();
        //the firstDay must minus this days, because we have to show this days on last month in our
        // calendar
        dayCursor = dayCursor.minusDays(dayOfWeek);

        DayEvent day;
        for (int i = 0; i < Constants.MAX_DATE_CELL_NUM; i++) {
            day = new DayEvent();
            day.setYear(dayCursor.getYear());
            day.setMonth(dayCursor.getMonthValue());
            day.setDay(dayCursor.getDayOfMonth());
            dates.add(day);
            dayCursor = dayCursor.plusDays(1);
        }
        myCalendarAdapter = new MyCalendarAdapter(getContext(), R.layout.day_view, dates);
        mGridView.setAdapter(myCalendarAdapter);
        //bind long click event
        mGridView.setOnItemLongClickListener(this);
        //bind click event
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String workedStr = null;
        TextView clickedView = view.findViewById(R.id.day_cur_textview);
        int clickedDate = Integer.parseInt(clickedView.getText().toString());
        List<DayEvent> list = mDBManager.queryDayEvents(mCurDate.getYear(), mCurDate.getMonthValue(), clickedDate);
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
                Log.d(TAG, "hourOfDay1=" + hourOfDay1 + ", hourOfDay2=" + hourOfDay2
                        + ", minute1=" + minute1 + ", minute2=" + minute2);
                if (hourOfDay1 > hourOfDay2) {
                    Log.e(TAG, "off work hour < on duty hour");
                    return;
                }
                if (hourOfDay1 == hourOfDay2) {
                    if (minute1 >= minute2) {
                        Log.e(TAG, "off work minute < on duty minute");
                        return;
                    }
                }
                int min = minute2 - minute1;
                int hour1 = hourOfDay1;
                int hour2 = hourOfDay2;
                if (min < 0) {
                    hour2 -= 1;
                }
                double result = Utils.formatDouble(Constants.DOUBLE_SCALE,
                        hour2 - hour1 + min / 60);
                DayEvent clickedViewDate = new DayEvent(mCurDate.getYear(), mCurDate.getMonthValue(), clickedDay);
                Log.d(TAG, "year:" + clickedViewDate.getYear() +
                        "month:" + clickedViewDate.getMonth() +
                        "day:" + +clickedDay +
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

    private void requestDayType(DayEvent curDayCell) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case HttpUtils.HTTP_RESPONSE_SUCCESS:
                        HolidayMode holidayMode = JsonUtils.parseHolidayMode(msg.obj.toString());
                        Log.e(TAG,
                                "enter handleMessage http success!, curDay="+curDayCell+"holiday " +
                                        "type =" +
                                        " " + holidayMode.getData());
                        curDayCell.setDayType(holidayMode.getData());
                        mDBManager.updateDayType(curDayCell);
                        if (myCalendarAdapter != null) {
                            myCalendarAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        Log.w(TAG, "http request error...");
                        break;
                }
            }
        };
        String formattedDate =
                curDayCell.getYear() +
                        "" + Utils.formatDayOrMonthToTwoCharString(curDayCell.getMonth()) +
                        "" + Utils.formatDayOrMonthToTwoCharString(curDayCell.getDay());
        String concatedUrl = Constants.CHECK_HOLIDAY_URL + "?date=" + formattedDate;
        Log.d(TAG, "url = " + concatedUrl);
        curDayCell.setTag(formattedDate);
        HttpUtils.getRequest(concatedUrl, handler);
    }

    private void showDayType(DayEvent curDayCell, MyCalendarAdapter.ViewHolder holder) {
        DayType type = DayType.get(curDayCell.getDayType());
        switch(type) {
            case DAY_TYPE_HOLIDAY:
                holder.dayHolidayTextView.setVisibility(View.VISIBLE);
                break;
            default:
                holder.dayHolidayTextView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private class MyCalendarAdapter extends ArrayAdapter<DayEvent> {
        private Context context;

        private int mResourceId;
        private LayoutInflater inflater;
        private ViewHolder holder;

        public MyCalendarAdapter(@NonNull Context context, int resource, ArrayList<DayEvent> dates) {
            super(context, resource, dates);
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.mResourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            final DayEvent curDayCell = getItem(position);
            LocalDate todayDate = LocalDate.now();
            if (convertView == null) {
                convertView = inflater.inflate(mResourceId, null);
                holder = new ViewHolder();
                holder.backgroundImageView = convertView.findViewById(R.id.day_bg_imageview);
                holder.dayTextView = convertView.findViewById(R.id.day_cur_textview);
                holder.dayEventImageView = convertView.findViewById(R.id.day_cur_event_imageview);
                holder.dayHolidayTextView =
                        convertView.findViewById(R.id.day_cur_holiday_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.dayTextView.setText(String.valueOf(curDayCell.getDay()));
            boolean isCurMonth = (mCurDate.getMonthValue() == curDayCell.getMonth());
            holder.dayTextView.setTextColor(isCurMonth ?
                    context.getColor(R.color.color_cur_month_day) :
                    context.getColor(R.color.color_not_cur_month_day));
            if (todayDate.getDayOfMonth() == curDayCell.getDay()
                    && todayDate.getMonthValue() == curDayCell.getMonth()
                    && todayDate.getYear() == curDayCell.getYear()
                    /* for other month not set today-flag */
                    && mCurDate.getMonthValue() == todayDate.getMonthValue()) {
                holder.backgroundImageView.setVisibility(VISIBLE);
                holder.dayTextView.setTextColor(ContextCompat.getColor(context, R.color.color_cur_day_text));
            }
            List<DayEvent> dayEvents = mDBManager.queryDayEvents(curDayCell.getYear(), curDayCell.getMonth(),
                    curDayCell.getDay());
            if (dayEvents != null && dayEvents.size() > 0) {
                DayEvent curDayEvent = dayEvents.get(0);
                // set man hour view
                if (curDayEvent.getManHour() != 0) {
                    holder.dayEventImageView.setVisibility(View.VISIBLE);
                    if (Utils.isEnoughManHour(curDayEvent.getManHour())) {
                        holder.dayEventImageView.setImageResource(R.drawable.green);
                    } else {
                        holder.dayEventImageView.setImageResource(R.drawable.red);
                    }
                } else {
                    holder.dayEventImageView.setVisibility(View.INVISIBLE);
                }
                // set day type view
                if(curDayEvent.getDayType() == DayType.DAY_TYPE_NULL.getValue() && curDayCell.getTag() == null) {
                    //TODO optimize http request
                    requestDayType(curDayCell);
                } else {
                    showDayType(curDayCell, holder);
                }
            } else if(curDayCell.getDayType() == DayType.DAY_TYPE_NULL.getValue() && curDayCell.getTag() == null) {
                //TODO optimize http request
                requestDayType(curDayCell);
            } else {
                showDayType(curDayCell, holder);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView backgroundImageView;
            TextView dayTextView;
            ImageView dayEventImageView;
            TextView dayHolidayTextView;
        }
    }

}
