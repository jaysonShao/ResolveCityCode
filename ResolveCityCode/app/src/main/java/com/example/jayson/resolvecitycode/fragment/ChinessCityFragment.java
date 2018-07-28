package com.example.jayson.resolvecitycode.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.jayson.resolvecitycode.R;
import com.example.jayson.resolvecitycode.adapter.CityAdapter;
import com.example.jayson.resolvecitycode.adapter.CommonAdapter;
import com.example.jayson.resolvecitycode.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.example.jayson.resolvecitycode.bean.AddressChineseBean;
import com.example.jayson.resolvecitycode.bean.CityHeaderBean;
import com.example.jayson.resolvecitycode.bean.FourCityBean;
import com.example.jayson.resolvecitycode.bean.RealmCityBean;
import com.example.jayson.resolvecitycode.holder.ViewHolder;
import com.example.jayson.resolvecitycode.indexbar.BaseIndexPinyinBean;
import com.example.jayson.resolvecitycode.indexbar.IndexBar;
import com.example.jayson.resolvecitycode.indexbar.SuspensionDecoration;
import com.example.jayson.resolvecitycode.listener.OnItemClickListener;
import com.example.jayson.resolvecitycode.utils.AndroidScheduler;
import com.example.jayson.resolvecitycode.utils.DataUtil;
import com.example.jayson.resolvecitycode.utils.SPUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;
import static com.example.jayson.resolvecitycode.activity.MainAddressActivity.KEY_PICKED_CITY;

/**
 * Created by shaoheng on 2018/5/14.
 */
public class ChinessCityFragment extends android.support.v4.app.Fragment {


    private IndexBar mIndexBar;
    private RecyclerView mRv;
    private TextView mTvSideBarHint;
    private LinearLayoutManager mManager;
    private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
    private CityAdapter mAdapter;
    //主体部分数据源（城市数据）
    private List<FourCityBean> mBodyDatas;
    //头部数据源
    private List<CityHeaderBean> mHeaderDatas = new ArrayList<>();
    //设置给InexBar、ItemDecoration的完整数据集
    private List<BaseIndexPinyinBean> mSourceDatas = new ArrayList<>();
    ;
    private SuspensionDecoration mDecoration;
    private LinkedList<FourCityBean> mHistoryCity = new LinkedList<>();
    ;
    private static final int LOAD_SUCCESS = 0x0002;
    public Realm myRealm;
    private RealmResults<RealmCityBean> mResultList;

    private List<FourCityBean> china = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public ChinessCityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initDatass() {
        LinkedList<FourCityBean> citys = DataUtil.getHistoryCityName(getActivity(), "china_history_nums", "china_item");

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
        Flowable.just(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        myRealm = Realm.getDefaultInstance();
                        RealmResults<AddressChineseBean> mList = myRealm.where(AddressChineseBean.class).findAll();
                        initDatas(mList);
                    }
                });
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chiness_city, container, false);
        initDatass();
        mTvSideBarHint = (TextView) view.findViewById(R.id.tvSideBarHint);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mRv.setLayoutManager(mManager = new LinearLayoutManager(getActivity()));
        mAdapter = new CityAdapter(getActivity(), R.layout.item_city_select_city, mBodyDatas);
        mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
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
        mRv.setAdapter(mHeaderAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
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
                DataUtil.updateHistoryCityName(getActivity(), mHistoryCity, "china_history_nums", "china_item");
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRv.addItemDecoration(mDecoration = new SuspensionDecoration(getActivity(), mSourceDatas)
                .setmTitleHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics()))
                .setColorTitleBg(0xffefefef)
                .setTitleFontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()))
                .setColorTitleFont(getActivity().getResources().getColor(android.R.color.black))
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size()));
        mRv.addItemDecoration(new DividerItemDecoration(getActivity(),  LinearLayout.VERTICAL));

        mIndexBar = view.findViewById(R.id.indexBar);
        mIndexBar.setmPressedShowTextView(mTvSideBarHint)
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setHeaderViewCount(mHeaderAdapter.getHeaderViewCount() - mHeaderDatas.size());
        Boolean isChinese = Boolean.valueOf(SPUtils.get(getActivity(), "districtConfigs","true"));
        if (!isChinese) {
            getDatas();
        }

        return view;
    }

    private void backWithData(FourCityBean city) {
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, city);
        getActivity().setResult(RESULT_OK, data);
        getActivity().finish();
    }

    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(final List<AddressChineseBean> data) {
        mBodyDatas = JSON.parseArray(data.get(0).getChinese(),FourCityBean.class);
        //先排序
        if ( getActivity() == null || getActivity().isFinishing()){
            return;
        }
        mIndexBar.getDataHelper().sortSourceDatas(mBodyDatas);

        //只能使用List的子类
        mAdapter.setDatas(mBodyDatas);
        mHeaderAdapter.notifyDataSetChanged();

        mSourceDatas.addAll(mBodyDatas);

        mIndexBar.setmSourceDatas(mSourceDatas)//设置数据
                .invalidate();
        mDecoration.setmDatas(mSourceDatas);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myRealm != null) {
            myRealm.close();
        }
        mBodyDatas = null;
    }
}
