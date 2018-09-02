package priv.yh.mycalendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import priv.yh.mycalendar.utils.Constants;

/**
 * Created by yzh on 2018/8/19.
 */

public class MyCalendarView extends LinearLayout {

    private ImageView preMonthBtn;
    private ImageView nextMonthBtn;
    private TextView dateText;

    private MyGridView gridView;

    private Calendar calendar = Calendar.getInstance();

    public MyCalendarView(Context context) {
        super(context);
        initView(context);
        refreshView();
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        refreshView();
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        refreshView();
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.calendar_view, this);

        preMonthBtn = findViewById(R.id.btnPreMonth);
        nextMonthBtn = findViewById(R.id.btnNextMonth);
        dateText = findViewById(R.id.txtDate);

        gridView = findViewById(R.id.calendar_grid);

        initEvent();
    }

    private void initEvent() {
        if(preMonthBtn != null) {
            preMonthBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar.add(Calendar.MONTH, -1);
                    refreshView();
                }
            });
        }

        if(nextMonthBtn != null) {
            nextMonthBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar.add(Calendar.MONTH, +1);
                    refreshView();
                }
            });
        }
    }

    private void refreshView() {
        //set current month and year
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        dateText.setText(sdf.format(calendar.getTime()));

        //set current month details
        ArrayList<Date> dates = new ArrayList<>();

        Calendar curDate = (Calendar) calendar.clone();
        curDate.set(Calendar.DAY_OF_MONTH, 1);
        int preDays = curDate.get(Calendar.DAY_OF_WEEK) - 1;
        curDate.add(Calendar.DAY_OF_MONTH, -preDays);

        for(int i = 0; i < Constants.MAX_DATE_CELL_NUM; i ++) {
            dates.add(curDate.getTime());
            curDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridView.setAdapter(new MyCalendarAdapter(getContext(), R.layout.day_view, dates));
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
            boolean isCurMonth = (today.getMonth() == curDayCell.getMonth());
            if(convertView == null) {
                convertView = inflater.inflate(mResourceId,null);
                holder = new ViewHolder();
                holder.dayView = convertView.findViewById(R.id.dayTextView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.dayView.setText(String.valueOf(curDayCell.getDate()));
            holder.dayView.setTextColor(isCurMonth?
                    getResources().getColor(R.color.color_cur_month_day):
                    getResources().getColor(R.color.color_not_cur_month_day));
            if(today.getDate() == curDayCell.getDate()
               && today.getMonth() == curDayCell.getMonth()
               && today.getYear() == curDayCell.getYear()) {
                holder.dayView.setToday(true);
                holder.dayView.setTextColor(getResources().getColor(R.color.color_cur_day_circle));
            }

            return convertView;
        }

        class ViewHolder {
            MyDayView dayView;
        }
    }

}