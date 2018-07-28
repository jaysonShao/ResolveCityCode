package com.example.jayson.resolvecitycode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.jayson.resolvecitycode.R;
import com.example.jayson.resolvecitycode.bean.FourCityBean;
import com.example.jayson.resolvecitycode.holder.ViewHolder;

import java.util.List;


public class CityAdapter extends CommonAdapter<FourCityBean> {
    public CityAdapter(Context context, int layoutId, List<FourCityBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, FourCityBean cityBean) {
        if (cityBean.getFour().equals("0") && !cityBean.getThree().equals("0")) {
            holder.setText(R.id.tvCity,cityBean.getThree());
            holder.setText(R.id.tvParent,  " / " + cityBean.getTwo() + " / " + cityBean.getOne());

        }else if (cityBean.getThree().equals("0") && !cityBean.getTwo().equals("0")){
            holder.setText(R.id.tvCity,cityBean.getTwo() );
            holder.setText(R.id.tvParent,  " / " + cityBean.getOne());
        }else if (cityBean.getTwo().equals("0") && !cityBean.getOne().equals("0")){
            holder.setText(R.id.tvCity,cityBean.getOne());
            // holder.setText(R.id.tvParent, cityBean.getOne());
        }else if (cityBean.getFour().equals("0") && cityBean.getThree().equals("0")) {
            holder.setText(R.id.tvCity,cityBean.getTwo());
            holder.setText(R.id.tvParent,  " / " + cityBean.getOne());
        }else if (cityBean.getThree().equals("0") && cityBean.getTwo().equals("0")) {
            holder.setText(R.id.tvCity, cityBean.getOne());
        }else {
            holder.setText(R.id.tvCity,cityBean.getFour() );
            holder.setText(R.id.tvParent, " / " + cityBean.getThree() + " / " + cityBean.getTwo() + " / " + cityBean.getOne());
        }
    }

}