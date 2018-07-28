package com.example.jayson.resolvecitycode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.jayson.resolvecitycode.R;
import com.example.jayson.resolvecitycode.bean.SearchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayson on 2018/3/5.
 */

public class SearchResultsLvAdapter extends BaseAdapter {

    public static final int VIEW_FIRST = 0;
    public static final int VIEW_SECOND = 1;
    public static final int VIEW_THIRD = 2;
    private List<SearchBean> mList;
    private Context mContext;

    public SearchResultsLvAdapter(Context mContext) {
        this.mContext = mContext;
        if (null == mList) {
            mList = new ArrayList<SearchBean>();
        }
    }

    public SearchResultsLvAdapter(Context context, List<SearchBean> myList) {
        this.mContext = mContext;
        this.mList = myList;
        if (null == mList) {
            mList = new ArrayList<SearchBean>();
        }
    }

    public void updateListView(List<SearchBean> mList2) {
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
        SearchBean city = mList.get(position);
        int type = getItemViewType(position);
        ViewHolderFirst holderFirst = null;
        ViewHolderSecond holderSecond = null;
        ViewHolderThird  holderThird = null;
        if (convertView == null) {
            switch (type) {
                case VIEW_FIRST:
                    holderFirst = new ViewHolderFirst();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_first,
                            null);
                    holderFirst.titleFirst = convertView.findViewById(R.id.iv_firstclassify_title);
                    holderFirst.nameFirst = convertView.findViewById(R.id.tv_firstclassify_name);
                    holderFirst.nameSecond = convertView.findViewById(R.id.tv_secondclassify_name);

                    switch (city.getTarger()){
                        case "0":
                            holderFirst.titleFirst.setText("国家");
                            holderFirst.nameFirst.setText(city.getOneSearchName());
                            holderFirst.nameSecond.setText("");
                            break;
                        case "1":
                            holderFirst.titleFirst.setText("省 / 州");
                            holderFirst.nameFirst.setText(city.getTwoSearchName());
                            holderFirst.nameSecond.setText(" / "+city.getOneSearchName());
                            break;
                        case "2":
                            holderFirst.titleFirst.setText("市 / 县");
                            holderFirst.nameFirst.setText(city.getThreeSearchName());
                            holderFirst.nameSecond.setText(" / "+city.getTwoSearchName()+"/"+city.getOneSearchName());
                            break;
                        case "3":
                            holderFirst.titleFirst.setText("市 / 县");
                            holderFirst.nameFirst.setText(city.getFourSearchName());
                            holderFirst.nameSecond.setText(" / "+ city.getThreeSearchName() + " / "+city.getTwoSearchName()+" / "+city.getOneSearchName());
                            break;
                    }
                    city.setCityName(holderFirst.nameFirst.getText().toString());
                    convertView.setTag(holderFirst);
                    break;
                case VIEW_SECOND:
                    holderSecond = new ViewHolderSecond();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city_second,
                            null);
                    holderSecond.titleSecond = convertView.findViewById(R.id.iv_secondclassify_title);
                    holderSecond.nameSecond = convertView.findViewById(R.id.tv_secondclassify_name);

                    if (city.getFourSearchName().equals("0") && !city.getThreeSearchName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getThreeSearchName());

                    } else if (city.getThreeSearchName().equals("0") && !city.getTwoSearchName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getTwoSearchName());
                    } else if (city.getTwoSearchName().equals("0") && !city.getOneSearchName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getOneSearchName());
                    } else if (city.getFourSearchName().equals("0") && city.getThreeSearchName().equals("0")) {
//                        holderSecond.nameSecond.setText(city.getTwoSearchName());
//                        holderSecond.nameSecond.setText("/" + city.getOneSearchName());
                        holderSecond.nameSecond.setText(city.getOneSearchName());
                    } else {
                        holderSecond.nameSecond.setText(city.getFourSearchName());
                    }
                    city.setCityName(holderSecond.nameSecond.getText().toString());
                    convertView.setTag(holderSecond);
                    break;
                case VIEW_THIRD:
                    holderThird=new ViewHolderThird();
                    convertView=LayoutInflater.from(mContext).inflate(R.layout.item_city_third,null);
                    holderThird.nameThird=convertView.findViewById(R.id.tv_thirdclassify_name);
                    holderThird.titleThird=convertView.findViewById(R.id.iv_thirdclassify_title);

                    if (city.getFourSearchName().equals("0") && !city.getThreeSearchName().equals("0")) {
                        holderThird.nameThird.setText(city.getThreeSearchName());
                    }else if (city.getThreeSearchName().equals("0") && !city.getTwoSearchName().equals("0")){
                        holderThird.nameThird.setText(city.getTwoSearchName() );
                    }else if (city.getTwoSearchName().equals("0") && !city.getOneSearchName().equals("0")){
                        holderThird.nameThird.setText(city.getOneSearchName());
//                    }else if (city.getFourSearchName().equals("0") && city.getThreeSearchName().equals("0")) {
//                        holderThird.nameThird.setText(city.getTwoSearchName());
                    }else if (city.getThreeSearchName().equals("0") && city.getTwoSearchName().equals("0")) {
                        holderThird.nameThird.setText(city.getOneSearchName());
                    }else {
                        holderThird.nameThird.setText(city.getFourSearchName() );
                    }
                    city.setCityName(holderThird.nameThird.getText().toString());
                    convertView.setTag(holderThird);
                    break;
            }
        } else {
            switch (type) {
                case VIEW_FIRST:
                    holderFirst = (ViewHolderFirst) convertView.getTag();
                    holderFirst.nameFirst.setText(city.getOneSearchName());
                    switch (city.getTarger()){
                        case "0":
                            holderFirst.titleFirst.setText("国家");
                            holderFirst.nameFirst.setText(city.getOneSearchName());
                            holderFirst.nameSecond.setText("");
                            break;
                        case "1":
                            holderFirst.titleFirst.setText("省 / 州");
                            holderFirst.nameFirst.setText(city.getTwoSearchName());
                            holderFirst.nameSecond.setText(" / "+city.getOneSearchName());
                            break;
                        case "2":
                            holderFirst.titleFirst.setText("市 / 县");
                            holderFirst.nameFirst.setText(city.getThreeSearchName());
                            holderFirst.nameSecond.setText(" / "+city.getTwoSearchName()+"/"+city.getOneSearchName());
                            break;
                        case "3":
                            holderFirst.titleFirst.setText("市 / 县");
                            holderFirst.nameFirst.setText(city.getFourSearchName());
                            holderFirst.nameSecond.setText(" / " + city.getThreeSearchName() + " / " +city.getTwoSearchName()+" / "+city.getOneSearchName());
                            break;
                    }
                    city.setCityName(holderFirst.nameFirst.getText().toString());
                    break;
                case VIEW_SECOND:
                    holderSecond= (ViewHolderSecond) convertView.getTag();
                    if (city.getFourSearchName().equals("0") && !city.getThreeSearchName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getThreeSearchName());

                    }else if (city.getThreeSearchName().equals("0") && !city.getTwoSearchName().equals("0")){
                        holderSecond.nameSecond.setText(city.getTwoSearchName() );
                    }else if (city.getTwoSearchName().equals("0") && !city.getOneSearchName().equals("0")){
                        holderSecond.nameSecond.setText(city.getOneSearchName());
                    }else if (city.getFourSearchName().equals("0") && city.getThreeSearchName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getTwoSearchName());
                    }else if (city.getThreeSearchName().equals("0") && city.getTwoSearchName().equals("0")) {
                        holderSecond.nameSecond.setText(city.getOneSearchName());
                    }else {
                        holderSecond.nameSecond.setText(city.getFourSearchName() );
                    }
                    city.setCityName(holderSecond.nameSecond.getText().toString());
                    break;
                case VIEW_THIRD:
//                    holderThird=new ViewHolderThird();
                    holderThird= (ViewHolderThird) convertView.getTag();
                    convertView=LayoutInflater.from(mContext).inflate(R.layout.item_city_third,null);
                    holderThird.nameThird=convertView.findViewById(R.id.tv_thirdclassify_name);
                    holderThird.titleThird=convertView.findViewById(R.id.iv_thirdclassify_title);

                    if (city.getFourSearchName().equals("0") && !city.getThreeSearchName().equals("0")) {
                        holderThird.nameThird.setText(city.getThreeSearchName());
                    }else if (city.getThreeSearchName().equals("0") && !city.getTwoSearchName().equals("0")){
                        holderThird.nameThird.setText(city.getTwoSearchName() );
                    }else if (city.getTwoSearchName().equals("0") && !city.getOneSearchName().equals("0")){
                        holderThird.nameThird.setText(city.getOneSearchName());
                    }else if (city.getFourSearchName().equals("0") && city.getThreeSearchName().equals("0")) {
                        holderThird.nameThird.setText(city.getTwoSearchName());
                    }else if (city.getThreeSearchName().equals("0") && city.getTwoSearchName().equals("0")) {
                        holderThird.nameThird.setText(city.getOneSearchName());
                    }else {
                        holderThird.nameThird.setText(city.getFourSearchName() );
                    }
                    city.setCityName(holderThird.nameThird.getText().toString());
                    convertView.setTag(holderThird);
                    break;
            }
        }





        return convertView;

    }



    @Override
    public int getItemViewType(int position) {
        SearchBean city = mList.get(position);
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
        private TextView nameSecond;
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
