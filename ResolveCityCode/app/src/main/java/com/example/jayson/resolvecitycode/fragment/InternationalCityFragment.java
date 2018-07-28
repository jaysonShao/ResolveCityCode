package com.example.jayson.resolvecitycode.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.jayson.resolvecitycode.R;
import com.example.jayson.resolvecitycode.activity.MainAddressActivity;
import com.example.jayson.resolvecitycode.adapter.CityAdapter;
import com.example.jayson.resolvecitycode.adapter.CommonAdapter;
import com.example.jayson.resolvecitycode.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.example.jayson.resolvecitycode.bean.AddressInternationBean;
import com.example.jayson.resolvecitycode.bean.CityHeaderBean;
import com.example.jayson.resolvecitycode.bean.FourCityBean;
import com.example.jayson.resolvecitycode.holder.ViewHolder;
import com.example.jayson.resolvecitycode.indexbar.BaseIndexPinyinBean;
import com.example.jayson.resolvecitycode.indexbar.IndexBar;
import com.example.jayson.resolvecitycode.indexbar.SuspensionDecoration;
import com.example.jayson.resolvecitycode.listener.OnItemClickListener;
import com.example.jayson.resolvecitycode.utils.AndroidScheduler;
import com.example.jayson.resolvecitycode.utils.DataUtil;
import com.example.jayson.resolvecitycode.utils.DividerItemDecoration;
import com.example.jayson.resolvecitycode.utils.SPUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;
import static com.example.jayson.resolvecitycode.activity.MainAddressActivity.KEY_PICKED_CITY;


public class InternationalCityFragment extends android.support.v4.app.Fragment {

    private IndexBar mInIndexBar;
    private RecyclerView mInRv;
    private TextView mInSideBarHint;
    private LinearLayoutManager mInManager;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private CityAdapter mInAdapter;
    //主体部分数据源（城市数据）
    private List<FourCityBean> mBodyDatas;
    //头部数据源
    private List<CityHeaderBean> mHeaderDatas= new ArrayList<>();
    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas = new ArrayList<>();
    private SuspensionDecoration mDecoration;
    private FrameLayout mCover;
    private LinkedList<FourCityBean> mHistoryCity= new LinkedList<>();;
    public Realm myRealm;

    public InternationalCityFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myRealm != null){
            myRealm = null;
        }
        mBodyDatas = null;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_international_city, container, false);
        init();
        mInSideBarHint = (TextView) view.findViewById(R.id.inSideBarHint);
        mInRv = (RecyclerView) view.findViewById(R.id.in_rv);
        mInRv.setLayoutManager(mInManager = new LinearLayoutManager(getActivity()));
        mInAdapter = new CityAdapter(getActivity(), R.layout.item_city_select_city, mBodyDatas);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mInAdapter) {

            @Override
            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
                if (layoutId == R.layout.item_city_header) {
                    final CityHeaderBean meituanHeaderBean = (CityHeaderBean) o;
                    //网格
                    RecyclerView recyclerView = holder.getView(R.id.rvCity);
                    recyclerView.setAdapter(
                            new CommonAdapter<FourCityBean>(getActivity(), R.layout.item_city_item_header, meituanHeaderBean.getCityList()) {
                                @Override
                                public void convert(ViewHolder holder, final FourCityBean cityName) {
                                    holder.setText(R.id.tvName, cityName.getCity());
                                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            backWithData(cityName);
                                        }
                                    });
                                }
                            });
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

                }
            }
        };
        mHeaderAdapter.setHeaderView(0, R.layout.item_city_header, mHeaderDatas.get(0));
        mInRv.setAdapter(mHeaderAdapter);
        mInAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                backWithData(mBodyDatas.get(position));
                for (int i = 0; i < mHistoryCity.size(); i++) {
                    if (mHistoryCity.get(i).getFour().equals(mBodyDatas.get(position).getFour()) && mHistoryCity.get(i).getThree().equals(mBodyDatas.get(position).getThree()) && mHistoryCity.get(i).getTwo().equals(mBodyDatas.get(position).getTwo())){
                        mHistoryCity.remove(i);
                    }
                }
                mHistoryCity.add(0, mBodyDatas.get(position));
                for (int i = 4; i < mHistoryCity.size(); i++) {
                    mHistoryCity.remove(i);
                }
                DataUtil.updateHistoryCityName(getActivity(), mHistoryCity, "inter_history_nums", "item_inter");
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mInRv.addItemDecoration(mDecoration = new SuspensionDecoration(getActivity(), mSourceDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics()))
                .setColorTitleBg(0xffefefef)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()))
                .setColorTitleFont(getActivity().getResources().getColor(android.R.color.black))
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size()));
        mInRv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mInIndexBar = view.findViewById(R.id.inIndexBar);
        mInIndexBar.setmPressedShowTextView(mInSideBarHint)
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mInManager)//设置RecyclerView的LayoutManager
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size());
        Boolean isChinese = Boolean.valueOf(SPUtils.get(getActivity(), "districtConfigs","true"));
        if (!isChinese) {
            getDatas();
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myRealm = Realm.getDefaultInstance();
    }

    private void backWithData(FourCityBean city) {
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, city);
        getActivity().setResult(RESULT_OK, data);
        getActivity().finish();
    }

    public void init() {
        LinkedList<FourCityBean> citys = DataUtil.getHistoryCityName(getActivity(), "inter_history_nums", "item_inter");
        mHistoryCity.clear();
        if (citys.size() > 4) {
            for (int i = 0; i < 4; i++) {
                mHistoryCity.add(citys.get(i));
            }
        } else {
            mHistoryCity.addAll(citys);
        }
        mHeaderDatas.add(new CityHeaderBean(mHistoryCity, "历史", "历史"));
        mSourceDatas.addAll(mHeaderDatas);

    }

    public void getDatas() {
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        myRealm = Realm.getDefaultInstance();
                        RealmResults<AddressInternationBean> mList = myRealm.where(AddressInternationBean.class).findAll();
                        initDatas(mList);
                    }
                });
    }

    private void initDatas(final List<AddressInternationBean> data) {


//        mBodyDatas = gson.fromJson(data.get(0).getInterNation(), new TypeToken<List<FourCityBean>>() {
//        }.getType());
        mBodyDatas = JSON.parseArray(data.get(0).getInterNation(),FourCityBean.class);

        //先排序
        if ( getActivity() == null || getActivity().isFinishing()){
            return;
        }
        mInIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);

        mInAdapter.setDatas(mBodyDatas);
        mHeaderAdapter.notifyDataSetChanged();
        mSourceDatas.addAll(mBodyDatas);

        mInIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                .invalidate();
        mDecoration.setmDatas(mSourceDatas);
//        if (getActivity() != null) {//异步加载数据源，防止Activity提前销毁
//            ((MainAddressActivity) getActivity()).hideLoading();
//        }
    }


}
