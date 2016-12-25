package com.gerli.gerli.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gerli.gerli.R;
import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthPlanFragment extends Fragment implements RobotoCalendarView.RobotoCalendarListener {
    private static final int SET_MPinput=1;//跳轉回傳到頁面，intent
    private RobotoCalendarView robotoCalendarView;
    int month=0,year=0,date=0;
    private ListView listView;
    private ArrayAdapter adapter;
    private View myView;
    private RobotoCalendarView.RobotoCalendarListener robotoCalendarListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView= inflater.inflate(R.layout.fragment_month_plan, container, false);

        // Gets the calendar from the view
        robotoCalendarView = (RobotoCalendarView) myView.findViewById(R.id.robotoCalendarPicker);

        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);
        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.updateView();
        return myView;
    }
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }


    public void onDayClick(Calendar daySelectedCalendar) {
        year=daySelectedCalendar.get(Calendar.YEAR);
        month=daySelectedCalendar.get(Calendar.MONTH)+1;//月處理的方式
        date=daySelectedCalendar.get(Calendar.MONTH);
        robotoCalendarView.markCircleImage1(daySelectedCalendar);//產生點點的函式

        //listview
        listView = (ListView) myView.findViewById(R.id.listView1);

        // 清單陣列
        adapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1);

//        for(int i=0;i<description.length;i++){
//            adapter.add(description[i]+id[i]);
//        }

        adapter.add("紅豆");//liatview 資料扔進去的方法
        adapter.add("黑豆");
        adapter.add("綠豆");
        adapter.add("花豆");
        adapter.add("毛豆");
        adapter.add("土豆");
        adapter.add("芋頭");
        adapter.add("地瓜");

        listView.setAdapter(adapter);
        //longclick
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean  onItemLongClick(AdapterView arg0, View arg1, int arg2,
                                            long arg3) { //長按觸發
                // TODO Auto-generated method stub
                ListView listView = (ListView) arg0;// ID arg3  文字 arg2
                Toast.makeText(
                       getActivity(),
                        "ID：" + arg3 +
                                "   選單文字："+ listView.getItemAtPosition(arg2).toString()+"刪除",
                        Toast.LENGTH_LONG).show();

                return true;
            }
        });
        Toast.makeText(getActivity(), "onDayClick: " + daySelectedCalendar.getTime(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDayLongClick(Calendar daySelectedCalendar) {
        //Toast.makeText(this, "onDayLongClick: " + daySelectedCalendar.getTime(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightButtonClick() {
        // Toast.makeText(this, "onRightButtonClick!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftButtonClick() {
        //Toast.makeText(this, "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
    }

    
}
