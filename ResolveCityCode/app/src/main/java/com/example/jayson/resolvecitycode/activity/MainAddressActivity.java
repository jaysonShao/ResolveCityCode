package com.example.jayson.resolvecitycode.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.jayson.resolvecitycode.R;
import com.example.jayson.resolvecitycode.adapter.CityFragmentAdapter;
import com.example.jayson.resolvecitycode.adapter.SearchResultsLvAdapter;
import com.example.jayson.resolvecitycode.adapter.SearchResultsSecondLvAdapter;
import com.example.jayson.resolvecitycode.bean.FourCityBean;
import com.example.jayson.resolvecitycode.bean.JsonStringBean;
import com.example.jayson.resolvecitycode.bean.SearchBean;
import com.example.jayson.resolvecitycode.bean.SearchChildBean;
import com.example.jayson.resolvecitycode.bean.ZBeanCity;
import com.example.jayson.resolvecitycode.fragment.ChinessCityFragment;
import com.example.jayson.resolvecitycode.fragment.InternationalCityFragment;
import com.example.jayson.resolvecitycode.indexbar.IIndexBarDataHelper;
import com.example.jayson.resolvecitycode.indexbar.IndexBarDataHelperImpl;
import com.example.jayson.resolvecitycode.utils.AndroidScheduler;
import com.example.jayson.resolvecitycode.utils.CitySortManager;
import com.example.jayson.resolvecitycode.utils.DataUtil;
import com.example.jayson.resolvecitycode.utils.DensityUtil;
import com.example.jayson.resolvecitycode.utils.ReadFromAssets;
import com.example.jayson.resolvecitycode.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jayson on 2018/5/14.
 */
public class MainAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private ChinessCityFragment mChinessCityFragment;
    private InternationalCityFragment mInternationalCityFragment;
    private ArrayList<Fragment> mFragments;
    private ArrayList<String> mTitles;
    private CityFragmentAdapter mAdapter;
    private ViewPager mViewPager;
    private TableLayout mTab;
    private FrameLayout mCover;
    private EditText mEtSearch;
    private TextView mCancel;
    private TextView mCancelRight;
    public static final String KEY_PICKED_CITY = "picked_city";
    private RelativeLayout mResultPage;
    private CitySortManager mSortManager;
    private ListView mSearch_lv;
    private SearchResultsLvAdapter mSearchResultsAdapter;

    private List<SearchBean> mFilterData;
    private List<SearchChildBean> mSecondFilterData;
    private RelativeLayout mResultSecondPage;
    private ListView mSearchSecondLv;
    private SearchResultsSecondLvAdapter mSearchResultsSecondLvAdapter;
    private SearchBean mSearchBean;

    private List<ZBeanCity> datass;
    private List<FourCityBean> chinaName;
    private List<FourCityBean> interName;
    private List<ZBeanCity> oneType = new ArrayList<>();
    private List<ZBeanCity> twoType = new ArrayList<>();
    private List<ZBeanCity> threeType = new ArrayList<>();
    private List<ZBeanCity> fourType = new ArrayList<>();
    private List<ZBeanCity> chinaOnetype = new ArrayList<>();
    private List<ZBeanCity> interOnetype = new ArrayList<>();
    private List<ZBeanCity> sortedDatas = new ArrayList<>();
    private Realm myRealm;
    private int isHide = 0;

    private PublishSubject<String> mPublishSubject;
    private DisposableObserver<String> mDisposableObserver;
    private CompositeDisposable mCompositeDisposable;



    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        mCompositeDisposable = null;
        myRealm = null;
        chinaName = null;
        interName = null;
        oneType = null;
        twoType = null;
        threeType = null;
        fourType = null;
        chinaOnetype = null;
        interOnetype = null;
        datass = null;
        super.onDestroy();
    }

    /**
     * 正向解析成每个县
     */
    private void createDatabase() {
        String cityDatas = ReadFromAssets.ReadJsonFile("district_select.json", MainAddressActivity.this);
        datass = JSON.parseArray(cityDatas,ZBeanCity.class);
        //排序
        IIndexBarDataHelper mSortDataHelper = new IndexBarDataHelperImpl();
        mSortDataHelper.sortSourceDatas(datass);
        sortedDatas = datass;
        chinaName = new ArrayList<>();
        interName = new ArrayList<>();

        for (int i = 0; i < datass.size(); i++) {
            switch (datass.get(i).getType()) {
                case "0":
                    //国家
                    datass.get(i).setPinyin(DataUtil.getPinyin(datass.get(i).getName()));
                    oneType.add(datass.get(i));
                    if (datass.get(i).getName().equals("中国")) {
                        chinaOnetype.add(datass.get(i));
                    } else {
                        interOnetype.add(datass.get(i));
                    }
                    break;
                case "1":
                    //省
                    datass.get(i).setPinyin(DataUtil.getPinyin(datass.get(i).getName()));
                    twoType.add(datass.get(i));
                    break;
                case "2":
                    //市
                    datass.get(i).setPinyin(DataUtil.getPinyin(datass.get(i).getName()));
                    threeType.add(datass.get(i));
                    break;
                case "3":
                    //县
                    datass.get(i).setPinyin(DataUtil.getPinyin(datass.get(i).getName()));
                    fourType.add(datass.get(i));
                    break;
            }
        }

//        initaddress2();


//        if (true)
//            return;



        //国内
        for (int i = 0; i < twoType.size(); i++) {
            if (chinaOnetype.get(0).getCode().equals(twoType.get(i).getParent())) {
                for (int j = 0; j < threeType.size(); j++) {
                    if (twoType.get(i).getCode().equals(threeType.get(j).getParent())) {
                        for (int k = 0; k < fourType.size(); k++) {
                            if (threeType.get(j).getCode().equals(fourType.get(k).getParent())) {

                                FourCityBean bean = new FourCityBean();
                                bean.setParent(threeType.get(j).getCode());
                                bean.setOne(chinaOnetype.get(0).getName());
                                bean.setOneCode(chinaOnetype.get(0).getCode());
                                bean.setTwo(twoType.get(i).getName());
                                bean.setTwoCode(twoType.get(i).getCode());
                                bean.setThree(threeType.get(j).getName());
                                bean.setThreeCode(threeType.get(j).getCode());
                                bean.setFour(fourType.get(k).getName());
                                bean.setFourCode(fourType.get(k).getCode());
                                chinaName.add(bean);
                                k = fourType.size();
                            } else {
                                if (threeType.size() == k) {
                                    FourCityBean bean = new FourCityBean();
                                    bean.setParent(threeType.get(j).getCode());
                                    bean.setOne(chinaOnetype.get(0).getName());
                                    bean.setOneCode(chinaOnetype.get(0).getCode());
                                    bean.setTwo(twoType.get(i).getName());
                                    bean.setTwoCode(twoType.get(i).getCode());
                                    bean.setThree(threeType.get(j).getName());
                                    bean.setThreeCode(threeType.get(j).getCode());
                                    bean.setFour("0");
                                    bean.setFourCode("0");
                                    chinaName.add(bean);
                                }
                            }
                        }
                    } else {
                        if (threeType.size() == j) {
                            FourCityBean bean = new FourCityBean();
                            bean.setParent(twoType.get(i).getCode());
                            bean.setOne(chinaOnetype.get(0).getName());
                            bean.setOneCode(chinaOnetype.get(0).getCode());
                            bean.setTwo(twoType.get(i).getName());
                            bean.setTwoCode(twoType.get(i).getCode());
                            bean.setThree("0");
                            bean.setThreeCode("0");
                            bean.setFour("0");
                            bean.setFour("0");
                            chinaName.add(bean);
                        }
                    }
                }
            } else {
                if (twoType.size() == i) {
                    FourCityBean bean = new FourCityBean();
                    bean.setParent(chinaOnetype.get(0).getCode());
                    bean.setOne(chinaOnetype.get(0).getName());
                    bean.setOneCode(chinaOnetype.get(0).getCode());
                    bean.setTwo("0");
                    bean.setTwoCode("0");
                    bean.setThree("0");
                    bean.setThreeCode("0");
                    bean.setFour("0");
                    bean.setFourCode("0");
                    chinaName.add(bean);
                }
            }
        }

        //国际
        for (int m = 0; m < interOnetype.size(); m++) {
            for (int i = 0; i < twoType.size(); i++) {
                if (interOnetype.get(m).getCode().equals(twoType.get(i).getParent())) {
                    for (int j = 0; j < threeType.size(); j++) {
                        if (twoType.get(i).getCode().equals(threeType.get(j).getParent())) {
                            for (int k = 0; k < fourType.size(); k++) {
                                if (threeType.get(j).getCode().equals(fourType.get(k).getParent())) {
                                    FourCityBean bean = new FourCityBean();
                                    bean.setParent(threeType.get(j).getCode());
                                    bean.setOne(interOnetype.get(m).getName());
                                    bean.setOneCode(interOnetype.get(m).getCode());
                                    bean.setTwo(twoType.get(i).getName());
                                    bean.setTwoCode(twoType.get(i).getCode());
                                    bean.setThree(threeType.get(j).getName());
                                    bean.setThreeCode(threeType.get(j).getCode());
                                    bean.setFour(fourType.get(k).getName());
                                    bean.setFourCode(fourType.get(k).getCode());
                                    interName.add(bean);
                                    k = fourType.size();
                                } else {
                                    if (threeType.size() - 1 == k) {
                                        FourCityBean bean = new FourCityBean();
                                        bean.setParent(threeType.get(j).getCode());
                                        bean.setOne(interOnetype.get(m).getName());
                                        bean.setOneCode(interOnetype.get(m).getCode());
                                        bean.setTwo(twoType.get(i).getName());
                                        bean.setTwoCode(twoType.get(i).getCode());
                                        bean.setThree(threeType.get(j).getName());
                                        bean.setThreeCode(threeType.get(j).getCode());
                                        bean.setFour("0");
                                        bean.setFourCode("0");
                                        interName.add(bean);
                                    }
                                }
                            }
                        } else {
                            if (threeType.size() - 1 == j) {
                                FourCityBean bean = new FourCityBean();
                                bean.setParent(twoType.get(i).getCode());
                                bean.setOne(interOnetype.get(m).getName());
                                bean.setOneCode(interOnetype.get(m).getCode());
                                bean.setTwo(twoType.get(i).getName());
                                bean.setTwoCode(twoType.get(i).getCode());
                                bean.setThree("0");
                                bean.setThreeCode("0");
                                bean.setFour("0");
                                bean.setFour("0");
                                interName.add(bean);
                            }
                        }
                    }
                } else {
                    if (twoType.size() - 1 == i) {
                        FourCityBean bean = new FourCityBean();
                        bean.setParent(interOnetype.get(m).getCode());
                        bean.setOne(interOnetype.get(m).getName());
                        bean.setOneCode(interOnetype.get(m).getCode());
                        bean.setTwo("0");
                        bean.setTwoCode("0");
                        bean.setThree("0");
                        bean.setThreeCode("0");
                        bean.setFour("0");
                        bean.setFourCode("0");
                        interName.add(bean);
                    }
                }
            }
        }
    }

    /**
     * 反正解析成每个县
     * @return
     */
    private boolean createDatabase2() {
        //国内优化四级
        for (int i = 0; i < fourType.size(); i++) {
            //先找结尾是县的
            for (int j = 0; j < threeType.size(); j++) {
                if (threeType.get(j).getCode().equals(fourType.get(i).getParent())){
                    //找到市
                    for (int k = 0; k < twoType.size(); k++) {
                        if (twoType.get(k).getCode().equals(threeType.get(j).getParent())){
                            //找到省
                            //先找结尾是县的
                            if (chinaOnetype.get(0).getCode().equals(twoType.get(k).getParent())) {
                                //国内
                                if (threeType.get(j).getHavs() == 0)
                                    threeType.get(j).setHavs(1);
                                FourCityBean bean = new FourCityBean();
                                bean.setCity(fourType.get(i).getName());
                                bean.setFour(fourType.get(i).getName());
                                bean.setFourCode(fourType.get(i).getCode());
                                bean.setThree(threeType.get(j).getName());
                                bean.setThreeCode(threeType.get(j).getCode());
                                bean.setTwo(twoType.get(k).getName());
                                bean.setTwoCode(twoType.get(k).getCode());
                                bean.setOne(chinaOnetype.get(0).getName());
                                bean.setOneCode(chinaOnetype.get(0).getCode());
                                chinaName.add(bean);
                            }
                        }
                    }
                }
            }
        }


        //国内优化 三级
        //先找结尾是县的
        for (int j = 0; j < threeType.size(); j++) {
            //找到市
            for (int k = 0; k < twoType.size(); k++) {
                if (twoType.get(k).getCode().equals(threeType.get(j).getParent())) {
                    //找到省
                    //先找结尾是县的
                    if (chinaOnetype.get(0).getCode().equals(twoType.get(k).getParent())) {
                        //国内
                        if (threeType.get(j).getHavs() == 1)
                            return true;
                        if (twoType.get(k).getHavs() == 0)
                            twoType.get(k).setHavs(1);
                        FourCityBean bean = new FourCityBean();
                        bean.setCity(threeType.get(j).getName());
                        bean.setFour("0");
                        bean.setFourCode("0");
                        bean.setThree(threeType.get(j).getName());
                        bean.setThreeCode(threeType.get(j).getCode());
                        bean.setTwo(twoType.get(k).getName());
                        bean.setTwoCode(twoType.get(k).getCode());
                        bean.setOne(chinaOnetype.get(0).getName());
                        bean.setOneCode(chinaOnetype.get(0).getCode());
                        chinaName.add(bean);
                    }
                }
            }
        }


        //国内优化 二级
        //找到市
        for (int k = 0; k < twoType.size(); k++) {
                //找到省
                //先找结尾是县的
                if (chinaOnetype.get(0).getCode().equals(twoType.get(k).getParent())) {
                    //国内
                    if (twoType.get(k).getHavs() == 1)
                        return true;
                    if (chinaOnetype.get(0).getHavs() == 0)
                        chinaOnetype.get(0).setHavs(1);
                    FourCityBean bean = new FourCityBean();
                    bean.setCity(twoType.get(k).getName());
                    bean.setFour("0");
                    bean.setFourCode("0");
                    bean.setThree("0");
                    bean.setThreeCode("0");
                    bean.setTwo(twoType.get(k).getName());
                    bean.setTwoCode(twoType.get(k).getCode());
                    bean.setOne(chinaOnetype.get(0).getName());
                    bean.setOneCode(chinaOnetype.get(0).getCode());
                    chinaName.add(bean);
                }
    }


        //国外优化 四级
        for (int i = 0; i < fourType.size(); i++) {
            //先找结尾是县的
            for (int j = 0; j < threeType.size(); j++) {
                if (threeType.get(j).getCode().equals(fourType.get(i).getParent())){
                    //找到市
                    for (int k = 0; k < twoType.size(); k++) {
                        if (twoType.get(k).getCode().equals(threeType.get(j).getParent())){
                            //找到省
                            //先找结尾是县的
                            for (int l = 0; l < interOnetype.size(); l++) {
                                if (interOnetype.get(l).getCode().equals(twoType.get(k).getParent())) {
                                    //国内
                                    if (threeType.get(j).getHavs() == 0)
                                        threeType.get(j).setHavs(1);
                                    FourCityBean bean = new FourCityBean();
                                    bean.setCity(fourType.get(i).getName());
                                    bean.setFour(fourType.get(i).getName());
                                    bean.setFourCode(fourType.get(i).getCode());
                                    bean.setThree(threeType.get(j).getName());
                                    bean.setThreeCode(threeType.get(j).getCode());
                                    bean.setTwo(twoType.get(k).getName());
                                    bean.setTwoCode(twoType.get(k).getCode());
                                    bean.setOne(interOnetype.get(l).getName());
                                    bean.setOneCode(interOnetype.get(l).getCode());
                                    interName.add(bean);
                                }
                            }
                        }
                    }
                }
            }
        }


        //国外优化 三级
        //先找结尾是县的
        for (int j = 0; j < threeType.size(); j++) {
            //找到市
            for (int k = 0; k < twoType.size(); k++) {
                if (twoType.get(k).getCode().equals(threeType.get(j).getParent())) {
                    //找到省
                    //先找结尾是县的
                    for (int i = 0; i < interOnetype.size(); i++) {

                        if (interOnetype.get(i).getCode().equals(twoType.get(k).getParent())) {
                            //国内
                            if (threeType.get(j).getHavs() == 1)
                                return true;
                            if (twoType.get(k).getHavs() == 0)
                                twoType.get(k).setHavs(1);
                            FourCityBean bean = new FourCityBean();
                            bean.setCity(threeType.get(j).getName());
                            bean.setFour("0");
                            bean.setFourCode("0");
                            bean.setThree(threeType.get(j).getName());
                            bean.setThreeCode(threeType.get(j).getCode());
                            bean.setTwo(twoType.get(k).getName());
                            bean.setTwoCode(twoType.get(k).getCode());
                            bean.setOne(interOnetype.get(i).getName());
                            bean.setOneCode(interOnetype.get(i).getCode());
                            interName.add(bean);
                        }
                    }
                }
            }
        }


        //国外优化 二级
        //找到市
        for (int k = 0; k < twoType.size(); k++) {
            //找到省
            //先找结尾是县的
            for (int i = 0; i < interOnetype.size(); i++) {
                if (interOnetype.get(i).getCode().equals(twoType.get(k).getParent())) {
                    //国内
                    if (twoType.get(k).getHavs() == 1)
                        return true;
                    if (interOnetype.get(i).getHavs() == 0)
                        interOnetype.get(i).setHavs(1);
                    FourCityBean bean = new FourCityBean();
                    bean.setCity(twoType.get(k).getName());
                    bean.setFour("0");
                    bean.setFourCode("0");
                    bean.setThree("0");
                    bean.setThreeCode("0");
                    bean.setTwo(twoType.get(k).getName());
                    bean.setTwoCode(twoType.get(k).getCode());
                    bean.setOne(interOnetype.get(i).getName());
                    bean.setOneCode(interOnetype.get(i).getCode());
                    interName.add(bean);
                }
            }
        }


        //国外优化 一级
        for (int i = 0; i < interOnetype.size(); i++) {
            //国内
            if (interOnetype.get(i).getHavs() == 1)
                return true;
            FourCityBean bean = new FourCityBean();
            bean.setCity(interOnetype.get(i).getName());
            bean.setFour("0");
            bean.setFourCode("0");
            bean.setThree("0");
            bean.setThreeCode("0");
            bean.setTwo("0");
            bean.setTwoCode("0");
            bean.setOne(interOnetype.get(i).getName());
            bean.setOneCode(interOnetype.get(i).getCode());
            interName.add(bean);
        }
        return false;
    }



    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_main);

        mCancel = (TextView) findViewById(R.id.tv_search_cancel);
        mCancelRight = (TextView) findViewById(R.id.tv_search_cancel_right);
        mTab = (TableLayout) findViewById(R.id.city_search_tab);
        LinearLayout linearLayout = (LinearLayout) mTab.getChildAt(0);
//        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.layout_divider_vertical));
//        linearLayout.setDividerPadding(DensityUtil.dip2px(this, 10));
        mViewPager = (ViewPager) findViewById(R.id.city_search_vp);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mCover = (FrameLayout) findViewById(R.id.cover);
        mResultPage = (RelativeLayout) findViewById(R.id.search_expand);
        mSearch_lv = (ListView) findViewById(R.id.search_lv);
        mResultSecondPage = (RelativeLayout) findViewById(R.id.search_second_expand);
        mSearchSecondLv = (ListView) findViewById(R.id.search_second_lv);
        mCover.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mCancelRight.setOnClickListener(this);
        initSearch();
        initFragment();

        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        myRealm = Realm.getDefaultInstance();
                        createData();
                    }
                });
    }



    private void createData() {
//        Boolean isChinese = DataUtil.getDatabaseConfig(this, "districtConfig");
        Boolean isChinese = Boolean.valueOf(SPUtils.get(this, "districtConfigs","true"));
        if (isChinese) {
//        if (true){
//            ((ChinessCityFragment)mFragments.get(0)).getDatas();
//            ((InternationalCityFragment)mFragments.get(1)).getDatas();
        }else {

            RealmResults<JsonStringBean> mList = myRealm.where(JsonStringBean.class).findAll();

//            oneType = gson.fromJson(mList.get(0).getOneType(), new TypeToken<List<ZBeanCity>>() {
//            }.getType());
//            twoType = gson.fromJson(mList.get(0).getTwoType(), new TypeToken<List<ZBeanCity>>() {
//            }.getType());
//            threeType = gson.fromJson(mList.get(0).getThreeType(), new TypeToken<List<ZBeanCity>>() {
//            }.getType());
//            fourType = gson.fromJson(mList.get(0).getFourType(), new TypeToken<List<ZBeanCity>>() {
//            }.getType());

            oneType = JSON.parseArray(mList.get(0).getOneType(),ZBeanCity.class);
            twoType = JSON.parseArray(mList.get(0).getTwoType(),ZBeanCity.class);
            threeType = JSON.parseArray(mList.get(0).getThreeType(),ZBeanCity.class);
            fourType = JSON.parseArray(mList.get(0).getFourType(),ZBeanCity.class);
//        SPUtils.put(this, "districtConfigs","true");
        }

        myRealm = null;
//        hideLoading();
    }

    private void backWithData(FourCityBean city) {
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, city);
        this.setResult(RESULT_OK, data);
    }

    private void initSearch() {
        mSortManager = new CitySortManager();
        mSearchResultsSecondLvAdapter = new SearchResultsSecondLvAdapter(this);
        mSearchResultsAdapter = new SearchResultsLvAdapter(this);
        mSearch_lv.setAdapter(mSearchResultsAdapter);
        mSearchSecondLv.setAdapter(mSearchResultsSecondLvAdapter);
        mSearchSecondLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    SearchChildBean searchChildBean = mSearchBean.getChildList().get(position);
                    FourCityBean fourCityBean = new FourCityBean();
                    fourCityBean.setOne(searchChildBean.getOneChildName());
                    fourCityBean.setTwo(searchChildBean.getTwoChildName());
                    fourCityBean.setThree(searchChildBean.getThreeChildName());
                    fourCityBean.setFour(searchChildBean.getFourChildName());
                    fourCityBean.setOneCode(searchChildBean.getOneChildCode());
                    fourCityBean.setTwoCode(searchChildBean.getTwoChildCode());
                    fourCityBean.setThreeCode(searchChildBean.getThreeChildCode());
                    fourCityBean.setFourCode(searchChildBean.getFourChildCode());
                    fourCityBean.setCity(searchChildBean.getCityName());
                    backWithData(fourCityBean);
                }
            }
        });
        mSearch_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchBean = mFilterData.get(position);
                if (mSearchBean.getChildList() == null){
                    FourCityBean fourCityBean = new FourCityBean();
                    fourCityBean.setOne(mSearchBean.getOneSearchName());
                    fourCityBean.setTwo(mSearchBean.getTwoSearchName());
                    fourCityBean.setThree(mSearchBean.getThreeSearchName());
                    fourCityBean.setFour(mSearchBean.getFourSearchName());
                    fourCityBean.setOneCode(mSearchBean.getOneSearchCode());
                    fourCityBean.setTwoCode(mSearchBean.getTwoSearchCode());
                    fourCityBean.setThreeCode(mSearchBean.getThreeSearchCode());
                    fourCityBean.setFourCode(mSearchBean.getFourSearchCode());
                    fourCityBean.setCity(mSearchBean.getCityName());
                    backWithData(fourCityBean);
                }else if (mSearchBean.getChildList() != null && mSearchBean.getChildList().size() != 0 && mSearchBean.getChildList().size() != 1) {
                    mResultPage.setVisibility(View.GONE);
                    mResultSecondPage.setVisibility(View.VISIBLE);
                    mSearchResultsSecondLvAdapter.updateListView(mSearchBean.getChildList());
                } else if (mSearchBean.getTarger().equals("0") || mSearchBean.getTarger().equals("1") && mSearchBean.getChildList().size() != 1) {

                } else {
                    FourCityBean fourCityBean = new FourCityBean();
                    fourCityBean.setOne(mSearchBean.getOneSearchName());
                    fourCityBean.setTwo(mSearchBean.getTwoSearchName());
                    fourCityBean.setThree(mSearchBean.getThreeSearchName());
                    fourCityBean.setFour(mSearchBean.getFourSearchName());
                    fourCityBean.setOneCode(mSearchBean.getOneSearchCode());
                    fourCityBean.setTwoCode(mSearchBean.getTwoSearchCode());
                    fourCityBean.setThreeCode(mSearchBean.getThreeSearchCode());
                    fourCityBean.setFourCode(mSearchBean.getFourSearchCode());
                    fourCityBean.setCity(mSearchBean.getCityName());
                    backWithData(fourCityBean);
                }
                // lisenter.OnChoiced(fourCityBean);
            }
        });


        mEtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mCancel.setVisibility(View.GONE);
                    mCancelRight.setVisibility(View.VISIBLE);
                    mCover.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) MainAddressActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    mCancel.setVisibility(View.VISIBLE);
                    mCancelRight.setVisibility(View.GONE);
                    mCover.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) MainAddressActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                }
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCover.setVisibility(View.GONE);
                mResultPage.setVisibility(View.VISIBLE);
                mResultSecondPage.setVisibility(View.GONE);
                if (TextUtils.isEmpty(s)) {
                    mResultPage.setVisibility(View.GONE);
                    mCover.setVisibility(View.VISIBLE);
                    return;
                }

//                mPublishSubject.onNext(mEtSearch.getText().toString());
                Observable.just(1)
                        .debounce(200,TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidScheduler.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
//                                showLoadding();
                                mFilterData = mSortManager.searchArithmetic(mEtSearch.getText().toString(), oneType, twoType, threeType, fourType);
                                if (mFilterData != null) {
                                    mSearchResultsAdapter.updateListView(mFilterData);
//                                    hideLoading();
                                }
                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {
                mEtSearch.setFocusable(true);
                mEtSearch.setFocusableInTouchMode(true);
                mEtSearch.requestFocus();
            }
        });

        mPublishSubject = PublishSubject.create();
        mDisposableObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                mFilterData = mSortManager.searchArithmetic(mEtSearch.getText().toString(), oneType, twoType, threeType, fourType);
                if (mFilterData != null) {
                    mSearchResultsAdapter.updateListView(mFilterData);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        mPublishSubject
                .debounce(400,TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.length() > 0;
                    }
                }).switchMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String s) throws Exception {
                return getSearchObservable(s);
            }
        })
                .subscribe(mDisposableObserver);
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(mDisposableObserver);
    }

    private Observable<String> getSearchObservable(final String query) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> observableEmitter) throws Exception {
                try {
                    Thread.sleep(100 + (long) (Math.random() * 500));
                } catch (InterruptedException e) {
                    if (!observableEmitter.isDisposed()) {
                        observableEmitter.onError(e);
                    }
                }
                observableEmitter.onNext(query);
                observableEmitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        mChinessCityFragment = new ChinessCityFragment();
        mInternationalCityFragment = new InternationalCityFragment();
        mFragments.add(mChinessCityFragment);
        mFragments.add(mInternationalCityFragment);
        mTitles = new ArrayList<>();
        mTitles.add("国内");
        mTitles.add("国际/港澳台");
        mAdapter = new CityFragmentAdapter(getSupportFragmentManager(), mFragments, mTitles);
        mViewPager.setAdapter(mAdapter);
//        mTab.setTabTextColors(Color.rgb(0,0,0), Color.rgb(0,0,0));
//        mTab.setupWithViewPager(mViewPager);
//        mTab.setSelectedTabIndicatorColor(Color.rgb(0,0,0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cover:
                mCover.setVisibility(View.GONE);
                mEtSearch.clearFocus();
                InputMethodManager imm = (InputMethodManager) MainAddressActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                break;
            case R.id.tv_search_cancel:
                finish();
                break;
            case R.id.tv_search_cancel_right:
                mEtSearch.setText("");
                mResultPage.setVisibility(View.GONE);
                mEtSearch.clearFocus();
                break;
        }
    }

}
