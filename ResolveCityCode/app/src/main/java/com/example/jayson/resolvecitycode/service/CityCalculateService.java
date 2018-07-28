package com.example.jayson.resolvecitycode.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.alibaba.fastjson.JSON;
import com.example.jayson.resolvecitycode.bean.AddressChineseBean;
import com.example.jayson.resolvecitycode.bean.AddressInternationBean;
import com.example.jayson.resolvecitycode.bean.FourCityBean;
import com.example.jayson.resolvecitycode.bean.JsonStringBean;
import com.example.jayson.resolvecitycode.bean.ZBeanCity;
import com.example.jayson.resolvecitycode.indexbar.IIndexBarDataHelper;
import com.example.jayson.resolvecitycode.indexbar.IndexBarDataHelperImpl;
import com.example.jayson.resolvecitycode.utils.DataUtil;
import com.example.jayson.resolvecitycode.utils.ReadFromAssets;
import com.example.jayson.resolvecitycode.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by jayson on 2018/5/21.
 */

public class CityCalculateService extends Service{
    private Realm myRealm;
    private List<FourCityBean> chinaName;
    private List<FourCityBean> interName;
    private List<ZBeanCity> oneType;
    private List<ZBeanCity> twoType;
    private List<ZBeanCity> threeType;
    private List<ZBeanCity> fourType;
    private List<ZBeanCity> chinaOnetype;
    private List<ZBeanCity> interOnetype;
    private List<ZBeanCity> datass ;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  super() name Used to name the worker thread, important only for debugging.
     */
    public CityCalculateService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        chinaName = new ArrayList<>();
        interName = new ArrayList<>();
        oneType = new ArrayList<>();
        twoType = new ArrayList<>();
        threeType = new ArrayList<>();
        fourType = new ArrayList<>();
        chinaOnetype = new ArrayList<>();
        interOnetype = new ArrayList<>();
        datass = new ArrayList<>();
        Observable.just(1)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        data();
                        myRealm.close();
                    }
                });
    }


    private void data() {
        if (myRealm == null) {
            myRealm = Realm.getDefaultInstance();
        }
        Boolean isChinese = Boolean.valueOf(SPUtils.get(this, "districtConfigs","true"));
        if (isChinese) {
//        if (true){
            createDatabase();
            initChinese();
            initInternation();
            myRealm.beginTransaction();
            JsonStringBean jsonStringBean = myRealm.createObject(JsonStringBean.class);
            jsonStringBean.setChinaName(JSON.toJSONString(chinaName));
            jsonStringBean.setInternationalName(JSON.toJSONString(interName));
            jsonStringBean.setOneType(JSON.toJSONString(oneType));
            jsonStringBean.setTwoType(JSON.toJSONString(twoType));
            jsonStringBean.setThreeType(JSON.toJSONString(threeType));
            jsonStringBean.setFourType(JSON.toJSONString(fourType));

            AddressChineseBean chineseBean = myRealm.createObject(AddressChineseBean.class);
            chineseBean.setChinese(JSON.toJSONString(chinaName));

            AddressInternationBean interBean = myRealm.createObject(AddressInternationBean.class);
            interBean.setInterNation(JSON.toJSONString(interName));

            myRealm.commitTransaction();
            SPUtils.put(this,"districtConfigs","false");
        }
    }

    private void initInternation() {
        for (int i = 0; i < interName.size(); i++) {
            if (interName.get(i).getFour().equals("0")){
                if (interName.get(i).getThree().equals("0")){
                    if (interName.get(i).getTwo().equals("0")){
                        interName.get(i).setCity(interName.get(i).getOne());
                    }else {
                        interName.get(i).setCity(interName.get(i).getTwo());
                    }
                }else {
                    interName.get(i).setCity(interName.get(i).getThree());
                }
            }else {
                interName.get(i).setCity(interName.get(i).getFour());
            }
        }
    }

    private void initChinese() {
        for (int i = 0; i < chinaName.size(); i++) {
            if (chinaName.get(i).getFour().equals("0")){
                if (chinaName.get(i).getThree().equals("0")){
                    if (chinaName.get(i).getTwo().equals("0")){
                        chinaName.get(i).setCity(chinaName.get(i).getOne());
                    }else {
                        chinaName.get(i).setCity(chinaName.get(i).getTwo());
                    }
                }else {
                    chinaName.get(i).setCity(chinaName.get(i).getThree());
                }
            }else {
                chinaName.get(i).setCity(chinaName.get(i).getFour());
            }
        }
    }

    private void createDatabase() {
        String cityDatas = ReadFromAssets.ReadJsonFile("district_select.json", getApplicationContext());
//        datass = gson.fromJson(cityDatas, new TypeToken<List<ZBeanCity>>(){}.getType());
        datass = JSON.parseArray(cityDatas,ZBeanCity.class);
        //排序
        IIndexBarDataHelper mSortDataHelper = new IndexBarDataHelperImpl();
        mSortDataHelper.sortSourceDatas(datass);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
