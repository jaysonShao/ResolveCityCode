package com.example.jayson.resolvecitycode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.jayson.resolvecitycode.R;
import com.example.jayson.resolvecitycode.bean.SearchChildBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayson on 2018/3/5.
 */

public class SearchResultsSecondLvAdapter extends BaseAdapter {

    public static final int VIEW_FIRST = 0;//shi
    public static final int VIEW_SECOND = 1;//xian
    public static final int VIEW_THIRD = 2;//sheng
    private List<SearchChildBean> mList;
    private Context mContext;

    public SearchResultsSecondLvAdapter(Context mContext) {
        this.mContext = mContext;
        if (null == mList) {
            mList = new ArrayList<SearchChildBean>();
        }
    }

    public SearchResultsSecondLvAdapter(Context context, List<SearchChildBean> myList) {
        this.mContext = mContext;
        this.mList = myList;
        if (null == mList) {
            mList = new ArrayList<SearchChildBean>();
        }
    }

    public void updateListView(List<SearchChildBean> mList2) {
        this.mList = mList2;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchChildBean city = mList.get(position);
        int type =getItemViewType(position);
        ViewHolderFirst holderFirst = null;
        ViewHolderSecond holderSecond = null;
        ViewHolderThird  holderThird = null;
        if (convertView == null) {
            switch (type) {
                case VIEW_THIRD:
                    holderFirst = new ViewHolderFirst();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_first,
                            null);
                    holderFirst.titleFirst = convertView.findViewById(R.id.iv_firstclassify_title);
                    holderFirst.nameFirst = convertView.findViewById(R.id.tv_firstclassify_name);
                    holderFirst.nameSecoend = convertView.findViewById(R.id.tv_secondclassify_name);
                    holderFirst.titleFirst.setText("省 / 州");
                    holderFirst.nameFirst.setText(city.getTwoChildName());
                    holderFirst.nameSecoend.setText(" / " + city.getOneChildName());
                    city.setCityName(holderFirst.nameFirst.getText().toString());
                    convertView.setTag(holderFirst);
                    break;
                case VIEW_FIRST:
                    holderSecond = new ViewHolderSecond();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_second,
                            null);
                    holderSecond.titleSecond = convertView.findViewById(R.id.iv_secondclassify_title);
                    holderSecond.nameSecond = convertView.findViewById(R.id.tv_secondclassify_name);

                    if (city.getFourChildName().equals("0") && !city.getThreeChildName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getThreeChildName());

                    } else if (city.getThreeChildName().equals("0") && !city.getTwoChildName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getTwoChildName());
                    } else if (city.getTwoChildName().equals("0") && !city.getOneChildName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getOneChildName());
                    } else if (city.getFourChildName().equals("0") && city.getThreeChildName().equals("0")) {
//                        holderSecond.nameSecond.setText(city.getTwoChildName());
//                        holderSecond.nameSecond.setText("/" + city.getOneChildName());
                        holderSecond.nameSecond.setText(city.getOneChildName());
                    } else {
                        holderSecond.nameSecond.setText(city.getFourChildName());
                    }
                    city.setCityName(holderSecond.nameSecond.getText().toString());
                    convertView.setTag(holderSecond);
                    break;
                case VIEW_SECOND:
                    holderThird=new ViewHolderThird();
                    convertView=LayoutInflater.from(mContext).inflate(R.layout.item_city_third,null);
                    holderThird.nameThird=convertView.findViewById(R.id.tv_thirdclassify_name);
                    holderThird.titleThird=convertView.findViewById(R.id.iv_thirdclassify_title);
                    if (city.getFourChildName().equals("0") && !city.getThreeChildName().equals("0")) {
                        holderThird.nameThird.setText(city.getThreeChildName());
                    }else if (city.getThreeChildName().equals("0") && !city.getTwoChildName().equals("0")){
                        holderThird.nameThird.setText(city.getTwoChildName() );
                    }else if (city.getTwoChildName().equals("0") && !city.getOneChildName().equals("0")){
                        holderThird.nameThird.setText(city.getOneChildName());
                    }else if (city.getFourChildName().equals("0") && city.getThreeChildName().equals("0")) {
                        holderThird.nameThird.setText(city.getTwoChildName());
                    }else if (city.getThreeChildName().equals("0") && city.getTwoChildName().equals("0")) {
                        holderThird.nameThird.setText(city.getOneChildName());
                    }else {
                        holderThird.nameThird.setText(city.getFourChildName());
                    }
                    city.setCityName(holderThird.nameThird.getText().toString());
                    convertView.setTag(holderThird);
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case VIEW_THIRD:
                    holderFirst = (ViewHolderFirst) convertView.getTag();
                    holderFirst.nameFirst.setText(city.getTwoChildName());
                    holderFirst.titleFirst.setText("省 / 州");
                    city.setCityName(holderFirst.nameFirst.getText().toString());
                    break;
                case VIEW_FIRST:
                    holderSecond= (ViewHolderSecond) convertView.getTag();
                    if (city.getFourChildName().equals("0") && !city.getThreeChildName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getThreeChildName());

                    }else if (city.getThreeChildName().equals("0") && !city.getTwoChildName().equals("0")){
                        holderSecond.nameSecond.setText(city.getTwoChildName() );
                    }else if (city.getTwoChildName().equals("0") && !city.getOneChildName().equals("0")){
                        holderSecond.nameSecond.setText(city.getOneChildName());
                    }else if (city.getFourChildName().equals("0") && city.getThreeChildName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getTwoChildName());
                    }else if (city.getThreeChildName().equals("0") && city.getTwoChildName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getOneChildName());
                    }else {
                        holderSecond.nameSecond.setText(city.getFourChildName() );
                    }
                    city.setCityName(holderSecond.nameSecond.getText().toString());
                    break;
                case VIEW_SECOND:
                    holderThird=new ViewHolderThird();
                    convertView=LayoutInflater.from(mContext).inflate(R.layout.item_city_third,null);
                    holderThird.nameThird=convertView.findViewById(R.id.tv_thirdclassify_name);
                    holderThird.titleThird=convertView.findViewById(R.id.iv_thirdclassify_title);

                    if (city.getFourChildName().equals("0") && !city.getThreeChildName().equals("0")) {
                        holderThird.nameThird.setText(city.getThreeChildName());
                    }else if (city.getThreeChildName().equals("0") && !city.getTwoChildName().equals("0")){
                        holderThird.nameThird.setText(city.getTwoChildName() );
                    }else if (city.getTwoChildName().equals("0") && !city.getOneChildName().equals("0")){
                        holderThird.nameThird.setText(city.getOneChildName());
                    }else if (city.getFourChildName().equals("0") && city.getThreeChildName().equals("0")) {
                        holderThird.nameThird.setText(city.getTwoChildName());
                    }else if (city.getThreeChildName().equals("0") && city.getTwoChildName().equals("0")) {
                        holderThird.nameThird.setText(city.getOneChildName());
                    }else {
                        holderThird.nameThird.setText(city.getFourChildName() );
                    }
                    city.setCityName(holderThird.nameThird.getText().toString());
                    convertView.setTag(holderThird);
                    break;
                default:
                    break;
            }
        }
        return convertView;

    }



    @Override
    public int getItemViewType(int position) {
        SearchChildBean city = mList.get(position);
        int type = Integer.parseInt(city.getType());
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    private class ViewHolderFirst {
        private TextView titleFirst;
        private TextView nameFirst;
        private TextView nameSecoend;
    }

    private class ViewHolderSecond {
        private TextView titleSecond;
        private TextView nameSecond;
    }
    private class ViewHolderThird {
        private TextView titleThird;
        private TextView nameThird;
    }
}
