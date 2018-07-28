package com.example.jayson.resolvecitycode.utils;

import android.text.TextUtils;


import com.example.jayson.resolvecitycode.bean.RealmCityBean;
import com.example.jayson.resolvecitycode.bean.SearchBean;
import com.example.jayson.resolvecitycode.bean.SearchChildBean;
import com.example.jayson.resolvecitycode.bean.ZBeanCity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jayson on 2018/3/4.
 */

public class CitySortManager {

    private Realm myRealm;
    private CityPinyinComparator pinyinComparator;
    private List<FirstClassifyList> sourceDateList;
    private CharacterParser characterParser;
    private List<SearchBean> beans = new ArrayList<>();
    private Pattern pLetter = Pattern.compile("[a-zA-Z]*");
    private Pattern pChinese = Pattern.compile("[\\u4e00-\\u9fa5]");
    private Matcher m;


    public CitySortManager() {

        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        // 根据拼音来排列ListView里面的数据类
       // pinyinComparator = new CityPinyinComparator();
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    public List<RealmCityBean> filterData(String filterStr) {
        List<RealmCityBean> filterDateList = new ArrayList<RealmCityBean>();
        myRealm = Realm.getDefaultInstance();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = null;//默认收索
        } else {
            filterDateList.clear();
            RealmResults<RealmCityBean> results = myRealm.where(RealmCityBean.class)
                   // .like("name", "?" + filterStr + "0")
                   // .contains("pinyin",filterStr)
                    .contains("name",filterStr)
                    .findAll();
            // Collections.sort(results, pinyinComparator);
            filterDateList.addAll(results);
        }
        // 根据a-z进行排序

        return filterDateList;
    }

    public List<SearchBean> searchArithmetic(String text, List<ZBeanCity> oneType, List<ZBeanCity> twoType, List<ZBeanCity> threeType, List<ZBeanCity> fourType){
        beans.clear();
        //输入的国家 省  市  县  挨个搜索 有就  添加 没有就不添加
        if (isChinese(text)){
            //是中文
            ChineseSearch(text, oneType, twoType, threeType, fourType);
        }else if (isiLetter(text)){
            //是英文
            LetterSeach(text.toUpperCase(), oneType, twoType, threeType, fourType);
            EnglishSeach(text.toUpperCase(), oneType, twoType, threeType, fourType);
        }



        return beans;
    }
    private void EnglishSeach(String text, List<ZBeanCity> oneType, List<ZBeanCity> twoType, List<ZBeanCity> threeType, List<ZBeanCity> fourType) {
        for (int i = 0; i < oneType.size(); i++) {
            if (oneType.get(i).getName_en() == null)
                break;
            if (oneType.get(i).getName_en().toUpperCase().startsWith(text)) {
                //输入匹配国家
                //只会有两级  type = 0 和 type = 1
                //但要有三级和四级的数据 一个list<Bean>

                //添加国家
                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("0");
                bean.setOneSearchName(oneType.get(i).getName());
                bean.setOneSearchCode(oneType.get(i).getCode());
                bean.setChildList(null);
                bean.setTwoSearchName("0");
                bean.setTwoSearchCode("0");
                bean.setThreeSearchName("0");
                bean.setThreeSearchCode("0");
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                beans.add(bean);

                for (int j = 0; j < twoType.size(); j++) {
                    if (oneType.get(i).getCode().equals(twoType.get(j).getParent())) {
                        //添加省
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("1");
                        bean1.setOneSearchName(oneType.get(i).getName());
                        bean1.setOneSearchCode(oneType.get(i).getCode());
                        bean1.setTwoSearchName(twoType.get(j).getName());
                        bean1.setTwoSearchCode(twoType.get(j).getCode());
                        bean1.setThreeSearchName("0");
                        bean1.setThreeSearchCode("0");
                        bean1.setFourSearchName("0");
                        bean1.setFourSearchCode("0");
                        List<SearchChildBean> childBeans = new ArrayList<>();
                        SearchChildBean bean4 = new SearchChildBean();
                        bean4.setType("2");
                        bean4.setOneChildName(oneType.get(i).getName());
                        bean4.setOneChildCode(oneType.get(i).getCode());
                        bean4.setTwoChildName(twoType.get(j).getName());
                        bean4.setTwoChildCode(twoType.get(j).getCode());
                        bean4.setThreeChildName("0");
                        bean4.setThreeChildCode("0");
                        bean4.setFourChildName("0");
                        bean4.setFourChildCode("0");
                        childBeans.add(bean4);
                        for (int k = 0; k < threeType.size(); k++) {
                            if (twoType.get(j).getCode().equals(threeType.get(k).getParent())) {
                                //添加市
                                SearchChildBean bean2 = new SearchChildBean();
                                bean2.setType("0");
                                bean2.setOneChildName(oneType.get(i).getName());
                                bean2.setOneChildCode(oneType.get(i).getCode());
                                bean2.setTwoChildName(twoType.get(j).getName());
                                bean2.setTwoChildCode(twoType.get(j).getCode());
                                bean2.setThreeChildName(threeType.get(k).getName());
                                bean2.setThreeChildCode(threeType.get(k).getCode());
                                bean2.setFourChildName("0");
                                bean2.setFourChildCode("0");
                                childBeans.add(bean2);
                                for (int l = 0; l < fourType.size(); l++) {
                                    if (threeType.get(k).getCode().equals(fourType.get(l).getParent())) {
                                        //添加县
                                        SearchChildBean bean3 = new SearchChildBean();
                                        bean3.setType("1");
                                        bean3.setOneChildName(oneType.get(i).getName());
                                        bean3.setOneChildCode(oneType.get(i).getCode());
                                        bean3.setTwoChildName(twoType.get(j).getName());
                                        bean3.setTwoChildCode(twoType.get(j).getCode());
                                        bean3.setThreeChildName(threeType.get(k).getName());
                                        bean3.setThreeChildCode(threeType.get(k).getCode());
                                        bean3.setFourChildName(fourType.get(l).getName());
                                        bean3.setFourChildCode(fourType.get(l).getCode());
                                        childBeans.add(bean3);
                                    }
                                }
                            }
                        }
                        bean1.setChildList(childBeans);
                        beans.add(bean1);
                    }
                }

            }
        }


        for (int i = 0; i < twoType.size(); i++) {
            if (twoType.get(i).getName_en() == null)
                break;
            if (twoType.get(i).getName_en().toUpperCase().startsWith(text)){
                //输入匹配到省
                //要查找国家是谁  后面全部固定填写国家
                //有三级  0,1,2,
                String oneName = "";
                String oneCode = "";
                for (int j = 0; j < oneType.size(); j++) {
                    //找他的国家是谁
                    if (oneType.get(j).getCode().equals(twoType.get(i).getParent())){
                        oneName = oneType.get(j).getName();
                        oneCode = oneType.get(j).getCode();
                    }
                }
                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("1");
                bean.setOneSearchName(oneName);
                bean.setOneSearchCode(oneCode);
                bean.setTwoSearchName(twoType.get(i).getName());
                bean.setTwoSearchCode(twoType.get(i).getCode());
                bean.setThreeSearchName("0");
                bean.setThreeSearchCode("0");
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                bean.setChildList(null);
                beans.add(bean);

                for (int j = 0; j < threeType.size(); j++) {
                    //找市
                    if (twoType.get(i).getCode().equals(threeType.get(j).getParent())){
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("2");
                        bean1.setOneSearchName(oneName);
                        bean1.setOneSearchCode(oneCode);
                        bean1.setTwoSearchName(twoType.get(i).getName());
                        bean1.setTwoSearchCode(twoType.get(i).getCode());
                        bean1.setThreeSearchName(threeType.get(j).getName());
                        bean1.setThreeSearchCode(threeType.get(j).getCode());
                        bean1.setFourSearchName("0");
                        bean1.setFourSearchCode("0");
                        bean1.setChildList(null);
                        beans.add(bean1);
                        for (int k = 0; k < fourType.size(); k++) {
                            //找县
                            if (threeType.get(j).getCode().equals(fourType.get(k).getParent())){
                                SearchBean bean2 = new SearchBean();
                                bean2.setType("2");
                                bean2.setTarger("3");
                                bean2.setOneSearchName(oneName);
                                bean2.setOneSearchCode(oneCode);
                                bean2.setTwoSearchName(twoType.get(i).getName());
                                bean2.setTwoSearchCode(twoType.get(i).getCode());
                                bean2.setThreeSearchName(threeType.get(j).getName());
                                bean2.setThreeSearchCode(threeType.get(j).getCode());
                                bean2.setFourSearchName(fourType.get(k).getName());
                                bean2.setFourSearchCode(fourType.get(k).getCode());
                                bean2.setChildList(null);
                                beans.add(bean2);
                            }
                        }
                    }
                }
            }
        }


        for (int i = 0; i < threeType.size(); i++) {
            if (threeType.get(i).getName_en() == null)
                return;
            if (threeType.get(i).getName_en().toUpperCase().startsWith(text)){
                //输入匹配到市
                //要查找上面的国家和省是谁 后面固定填写
                //单独显示
                String oneName = "";
                String oneCode = "";
                String twoName = "";
                String twoCode = "";
                for (int j = 0; j < twoType.size(); j++) {
                    //找省
                    if (threeType.get(i).getParent().equals(twoType.get(j).getCode())){
                        twoName = twoType.get(j).getName();
                        twoCode = twoType.get(j).getCode();
                        for (int k = 0; k < oneType.size(); k++) {
                            //找国
                            if (twoType.get(j).getParent().equals(oneType.get(k).getCode())){
                                oneName = oneType.get(k).getName();
                                oneCode = oneType.get(k).getCode();
                            }
                        }
                    }
                }

                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("2");
                bean.setOneSearchName(oneName);
                bean.setOneSearchCode(oneCode);
                bean.setTwoSearchName(twoName);
                bean.setTwoSearchCode(twoCode);
                bean.setThreeSearchName(threeType.get(i).getName());
                bean.setThreeSearchCode(threeType.get(i).getCode());
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                bean.setChildList(null);
                beans.add(bean);

                //想下找
                for (int j = 0; j < fourType.size(); j++) {
                    //找县
                    if (threeType.get(i).getCode().equals(fourType.get(j).getParent())){
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("3");
                        bean1.setOneSearchName(oneName);
                        bean1.setOneSearchCode(oneCode);
                        bean1.setTwoSearchName(twoName);
                        bean1.setTwoSearchCode(twoCode);
                        bean1.setThreeSearchName(threeType.get(i).getName());
                        bean1.setThreeSearchCode(threeType.get(i).getCode());
                        bean1.setFourSearchName(fourType.get(j).getName());
                        bean1.setFourSearchCode(fourType.get(j).getCode());
                        bean1.setChildList(null);
                        beans.add(bean1);
                    }
                }
            }
        }

        for (int i = 0; i < fourType.size(); i++) {
            if (fourType.get(i).getName_en() == null)
                return;
            if (fourType.get(i).getName_en().toUpperCase().startsWith(text)){
                //输入匹配县
                //type = 0
                //找到他的所有上层

                for (int j = 0; j < threeType.size(); j++) {
                    //找市
                    if (threeType.get(j).getCode().equals(fourType.get(i).getParent())){
                        SearchBean bean = new SearchBean();
                        bean.setType("0");
                        bean.setTarger("3");
                        bean.setFourSearchName(fourType.get(i).getName());
                        bean.setFourSearchCode(fourType.get(i).getCode());
                        bean.setThreeSearchName(threeType.get(j).getName());
                        bean.setThreeSearchCode(threeType.get(j).getCode());
                        for (int k = 0; k < twoType.size(); k++) {
                            //找省
                            if (twoType.get(k).getCode().equals(threeType.get(j).getParent())){
                                bean.setTwoSearchName(twoType.get(k).getName());
                                bean.setTwoSearchCode(twoType.get(k).getCode());
                                for (int l = 0; l < oneType.size(); l++) {
                                    //找国
                                    if (oneType.get(l).getCode().equals(twoType.get(k).getParent())){
                                        bean.setOneSearchName(oneType.get(l).getName());
                                        bean.setOneSearchCode(oneType.get(l).getCode());
                                    }
                                }
                            }
                        }
                        bean.setChildList(null);
                        beans.add(bean);
                    }
                }

            }
        }
    }


    private void LetterSeach(String text, List<ZBeanCity> oneType, List<ZBeanCity> twoType, List<ZBeanCity> threeType, List<ZBeanCity> fourType) {
        for (int i = 0; i < oneType.size(); i++) {
            if (oneType.get(i).getBaseIndexPinyin().startsWith(text)) {
                //输入匹配国家
                //只会有两级  type = 0 和 type = 1
                //但要有三级和四级的数据 一个list<Bean>

                //添加国家
                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("0");
                bean.setOneSearchName(oneType.get(i).getName());
                bean.setOneSearchCode(oneType.get(i).getCode());
                bean.setChildList(null);
                bean.setTwoSearchName("0");
                bean.setTwoSearchCode("0");
                bean.setThreeSearchName("0");
                bean.setThreeSearchCode("0");
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                beans.add(bean);

                for (int j = 0; j < twoType.size(); j++) {
                    if (oneType.get(i).getCode().equals(twoType.get(j).getParent())) {
                        //添加省
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("1");
                        bean1.setOneSearchName(oneType.get(i).getName());
                        bean1.setOneSearchCode(oneType.get(i).getCode());
                        bean1.setTwoSearchName(twoType.get(j).getName());
                        bean1.setTwoSearchCode(twoType.get(j).getCode());
                        bean1.setThreeSearchName("0");
                        bean1.setThreeSearchCode("0");
                        bean1.setFourSearchName("0");
                        bean1.setFourSearchCode("0");
                        List<SearchChildBean> childBeans = new ArrayList<>();
                        SearchChildBean bean4 = new SearchChildBean();
                        bean4.setType("2");
                        bean4.setOneChildName(oneType.get(i).getName());
                        bean4.setOneChildCode(oneType.get(i).getCode());
                        bean4.setTwoChildName(twoType.get(j).getName());
                        bean4.setTwoChildCode(twoType.get(j).getCode());
                        bean4.setThreeChildName("0");
                        bean4.setThreeChildCode("0");
                        bean4.setFourChildName("0");
                        bean4.setFourChildCode("0");
                        childBeans.add(bean4);
                        for (int k = 0; k < threeType.size(); k++) {
                            if (twoType.get(j).getCode().equals(threeType.get(k).getParent())) {
                                //添加市
                                SearchChildBean bean2 = new SearchChildBean();
                                bean2.setType("0");
                                bean2.setOneChildName(oneType.get(i).getName());
                                bean2.setOneChildCode(oneType.get(i).getCode());
                                bean2.setTwoChildName(twoType.get(j).getName());
                                bean2.setTwoChildCode(twoType.get(j).getCode());
                                bean2.setThreeChildName(threeType.get(k).getName());
                                bean2.setThreeChildCode(threeType.get(k).getCode());
                                bean2.setFourChildName("0");
                                bean2.setFourChildCode("0");
                                childBeans.add(bean2);
                                for (int l = 0; l < fourType.size(); l++) {
                                    if (threeType.get(k).getCode().equals(fourType.get(l).getParent())) {
                                        //添加县
                                        SearchChildBean bean3 = new SearchChildBean();
                                        bean3.setType("1");
                                        bean3.setOneChildName(oneType.get(i).getName());
                                        bean3.setOneChildCode(oneType.get(i).getCode());
                                        bean3.setTwoChildName(twoType.get(j).getName());
                                        bean3.setTwoChildCode(twoType.get(j).getCode());
                                        bean3.setThreeChildName(threeType.get(k).getName());
                                        bean3.setThreeChildCode(threeType.get(k).getCode());
                                        bean3.setFourChildName(fourType.get(l).getName());
                                        bean3.setFourChildCode(fourType.get(l).getCode());
                                        childBeans.add(bean3);
                                    }
                                }
                            }
                        }
                        bean1.setChildList(childBeans);
                        beans.add(bean1);
                    }
                }

            }
        }


        for (int i = 0; i < twoType.size(); i++) {
            if (twoType.get(i).getBaseIndexPinyin().startsWith(text)){
                //输入匹配到省
                //要查找国家是谁  后面全部固定填写国家
                //有三级  0,1,2,
                String oneName = "";
                String oneCode = "";
                for (int j = 0; j < oneType.size(); j++) {
                    //找他的国家是谁
                    if (oneType.get(j).getCode().equals(twoType.get(i).getParent())){
                        oneName = oneType.get(j).getName();
                        oneCode = oneType.get(j).getCode();
                    }
                }
                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("1");
                bean.setOneSearchName(oneName);
                bean.setOneSearchCode(oneCode);
                bean.setTwoSearchName(twoType.get(i).getName());
                bean.setTwoSearchCode(twoType.get(i).getCode());
                bean.setThreeSearchName("0");
                bean.setThreeSearchCode("0");
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                bean.setChildList(null);
                beans.add(bean);

                for (int j = 0; j < threeType.size(); j++) {
                    //找市
                    if (twoType.get(i).getCode().equals(threeType.get(j).getParent())){
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("2");
                        bean1.setOneSearchName(oneName);
                        bean1.setOneSearchCode(oneCode);
                        bean1.setTwoSearchName(twoType.get(i).getName());
                        bean1.setTwoSearchCode(twoType.get(i).getCode());
                        bean1.setThreeSearchName(threeType.get(j).getName());
                        bean1.setThreeSearchCode(threeType.get(j).getCode());
                        bean1.setFourSearchName("0");
                        bean1.setFourSearchCode("0");
                        bean1.setChildList(null);
                        beans.add(bean1);
                        for (int k = 0; k < fourType.size(); k++) {
                            //找县
                            if (threeType.get(j).getCode().equals(fourType.get(k).getParent())){
                                SearchBean bean2 = new SearchBean();
                                bean2.setType("2");
                                bean2.setTarger("3");
                                bean2.setOneSearchName(oneName);
                                bean2.setOneSearchCode(oneCode);
                                bean2.setTwoSearchName(twoType.get(i).getName());
                                bean2.setTwoSearchCode(twoType.get(i).getCode());
                                bean2.setThreeSearchName(threeType.get(j).getName());
                                bean2.setThreeSearchCode(threeType.get(j).getCode());
                                bean2.setFourSearchName(fourType.get(k).getName());
                                bean2.setFourSearchCode(fourType.get(k).getCode());
                                bean2.setChildList(null);
                                beans.add(bean2);
                            }
                        }
                    }
                }
            }
        }


        for (int i = 0; i < threeType.size(); i++) {
            if (threeType.get(i).getBaseIndexPinyin().startsWith(text)){
                //输入匹配到市
                //要查找上面的国家和省是谁 后面固定填写
                //单独显示
                String oneName = "";
                String oneCode = "";
                String twoName = "";
                String twoCode = "";
                for (int j = 0; j < twoType.size(); j++) {
                    //找省
                    if (threeType.get(i).getParent().equals(twoType.get(j).getCode())){
                        twoName = twoType.get(j).getName();
                        twoCode = twoType.get(j).getCode();
                        for (int k = 0; k < oneType.size(); k++) {
                            //找国
                            if (twoType.get(j).getParent().equals(oneType.get(k).getCode())){
                                oneName = oneType.get(k).getName();
                                oneCode = oneType.get(k).getCode();
                            }
                        }
                    }
                }

                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("2");
                bean.setOneSearchName(oneName);
                bean.setOneSearchCode(oneCode);
                bean.setTwoSearchName(twoName);
                bean.setTwoSearchCode(twoCode);
                bean.setThreeSearchName(threeType.get(i).getName());
                bean.setThreeSearchCode(threeType.get(i).getCode());
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                bean.setChildList(null);
                beans.add(bean);

                //想下找
                for (int j = 0; j < fourType.size(); j++) {
                    //找县
                    if (threeType.get(i).getCode().equals(fourType.get(j).getParent())){
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("3");
                        bean1.setOneSearchName(oneName);
                        bean1.setOneSearchCode(oneCode);
                        bean1.setTwoSearchName(twoName);
                        bean1.setTwoSearchCode(twoCode);
                        bean1.setThreeSearchName(threeType.get(i).getName());
                        bean1.setThreeSearchCode(threeType.get(i).getCode());
                        bean1.setFourSearchName(fourType.get(j).getName());
                        bean1.setFourSearchCode(fourType.get(j).getCode());
                        bean1.setChildList(null);
                        beans.add(bean1);
                    }
                }
            }
        }

        for (int i = 0; i < fourType.size(); i++) {
            if (fourType.get(i).getBaseIndexPinyin().startsWith(text)){
                //输入匹配县
                //type = 0
                //找到他的所有上层

                for (int j = 0; j < threeType.size(); j++) {
                    //找市
                    if (threeType.get(j).getCode().equals(fourType.get(i).getParent())){
                        SearchBean bean = new SearchBean();
                        bean.setType("0");
                        bean.setTarger("3");
                        bean.setFourSearchName(fourType.get(i).getName());
                        bean.setFourSearchCode(fourType.get(i).getCode());
                        bean.setThreeSearchName(threeType.get(j).getName());
                        bean.setThreeSearchCode(threeType.get(j).getCode());
                        for (int k = 0; k < twoType.size(); k++) {
                            //找省
                            if (twoType.get(k).getCode().equals(threeType.get(j).getParent())){
                                bean.setTwoSearchName(twoType.get(k).getName());
                                bean.setTwoSearchCode(twoType.get(k).getCode());
                                for (int l = 0; l < oneType.size(); l++) {
                                    //找国
                                    if (oneType.get(l).getCode().equals(twoType.get(k).getParent())){
                                        bean.setOneSearchName(oneType.get(l).getName());
                                        bean.setOneSearchCode(oneType.get(l).getCode());
                                    }
                                }
                            }
                        }
                        bean.setChildList(null);
                        beans.add(bean);
                    }
                }

            }
        }
    }

    private void ChineseSearch(String text, List<ZBeanCity> oneType, List<ZBeanCity> twoType, List<ZBeanCity> threeType, List<ZBeanCity> fourType) {
        for (int i = 0; i < oneType.size(); i++) {
            if (oneType.get(i).getName().startsWith(text)) {
                //输入匹配国家
                //只会有两级  type = 0 和 type = 1
                //但要有三级和四级的数据 一个list<Bean>

                //添加国家
                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("0");
                bean.setOneSearchName(oneType.get(i).getName());
                bean.setOneSearchCode(oneType.get(i).getCode());
                bean.setChildList(null);
                bean.setTwoSearchName("0");
                bean.setTwoSearchCode("0");
                bean.setThreeSearchName("0");
                bean.setThreeSearchCode("0");
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                beans.add(bean);

                for (int j = 0; j < twoType.size(); j++) {
                    if (oneType.get(i).getCode().equals(twoType.get(j).getParent())) {
                        //添加省
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("1");
                        bean1.setOneSearchName(oneType.get(i).getName());
                        bean1.setOneSearchCode(oneType.get(i).getCode());
                        bean1.setTwoSearchName(twoType.get(j).getName());
                        bean1.setTwoSearchCode(twoType.get(j).getCode());
                        bean1.setThreeSearchName("0");
                        bean1.setThreeSearchCode("0");
                        bean1.setFourSearchName("0");
                        bean1.setFourSearchCode("0");
                        List<SearchChildBean> childBeans = new ArrayList<>();
                        SearchChildBean bean4 = new SearchChildBean();
                        bean4.setType("2");
                        bean4.setOneChildName(oneType.get(i).getName());
                        bean4.setOneChildCode(oneType.get(i).getCode());
                        bean4.setTwoChildName(twoType.get(j).getName());
                        bean4.setTwoChildCode(twoType.get(j).getCode());
                        bean4.setThreeChildName("0");
                        bean4.setThreeChildCode("0");
                        bean4.setFourChildName("0");
                        bean4.setFourChildCode("0");
                        childBeans.add(bean4);
                        for (int k = 0; k < threeType.size(); k++) {
                            if (twoType.get(j).getCode().equals(threeType.get(k).getParent())) {
                                //添加市
                                SearchChildBean bean2 = new SearchChildBean();
                                bean2.setType("0");
                                bean2.setOneChildName(oneType.get(i).getName());
                                bean2.setOneChildCode(oneType.get(i).getCode());
                                bean2.setTwoChildName(twoType.get(j).getName());
                                bean2.setTwoChildCode(twoType.get(j).getCode());
                                bean2.setThreeChildName(threeType.get(k).getName());
                                bean2.setThreeChildCode(threeType.get(k).getCode());
                                bean2.setFourChildName("0");
                                bean2.setFourChildCode("0");
                                childBeans.add(bean2);
                                for (int l = 0; l < fourType.size(); l++) {
                                    if (threeType.get(k).getCode().equals(fourType.get(l).getParent())) {
                                        //添加县
                                        SearchChildBean bean3 = new SearchChildBean();
                                        bean3.setType("1");
                                        bean3.setOneChildName(oneType.get(i).getName());
                                        bean3.setOneChildCode(oneType.get(i).getCode());
                                        bean3.setTwoChildName(twoType.get(j).getName());
                                        bean3.setTwoChildCode(twoType.get(j).getCode());
                                        bean3.setThreeChildName(threeType.get(k).getName());
                                        bean3.setThreeChildCode(threeType.get(k).getCode());
                                        bean3.setFourChildName(fourType.get(l).getName());
                                        bean3.setFourChildCode(fourType.get(l).getCode());
                                        childBeans.add(bean3);
                                    }
                                }
                            }
                        }
                        bean1.setChildList(childBeans);
                        beans.add(bean1);
                    }
                }

            }
        }


        for (int i = 0; i < twoType.size(); i++) {
            if (twoType.get(i).getName().startsWith(text)){
                //输入匹配到省
                //要查找国家是谁  后面全部固定填写国家
                //有三级  0,1,2,
                String oneName = "";
                String oneCode = "";
                for (int j = 0; j < oneType.size(); j++) {
                    //找他的国家是谁
                    if (oneType.get(j).getCode().equals(twoType.get(i).getParent())){
                        oneName = oneType.get(j).getName();
                        oneCode = oneType.get(j).getCode();
                    }
                }
                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("1");
                bean.setOneSearchName(oneName);
                bean.setOneSearchCode(oneCode);
                bean.setTwoSearchName(twoType.get(i).getName());
                bean.setTwoSearchCode(twoType.get(i).getCode());
                bean.setThreeSearchName("0");
                bean.setThreeSearchCode("0");
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                bean.setChildList(null);
                beans.add(bean);

                for (int j = 0; j < threeType.size(); j++) {
                    //找市
                    if (twoType.get(i).getCode().equals(threeType.get(j).getParent())){
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("2");
                        bean1.setOneSearchName(oneName);
                        bean1.setOneSearchCode(oneCode);
                        bean1.setTwoSearchName(twoType.get(i).getName());
                        bean1.setTwoSearchCode(twoType.get(i).getCode());
                        bean1.setThreeSearchName(threeType.get(j).getName());
                        bean1.setThreeSearchCode(threeType.get(j).getCode());
                        bean1.setFourSearchName("0");
                        bean1.setFourSearchCode("0");
                        bean1.setChildList(null);
                        beans.add(bean1);
                        for (int k = 0; k < fourType.size(); k++) {
                            //找县
                            if (threeType.get(j).getCode().equals(fourType.get(k).getParent())){
                                SearchBean bean2 = new SearchBean();
                                bean2.setType("2");
                                bean2.setTarger("3");
                                bean2.setOneSearchName(oneName);
                                bean2.setOneSearchCode(oneCode);
                                bean2.setTwoSearchName(twoType.get(i).getName());
                                bean2.setTwoSearchCode(twoType.get(i).getCode());
                                bean2.setThreeSearchName(threeType.get(j).getName());
                                bean2.setThreeSearchCode(threeType.get(j).getCode());
                                bean2.setFourSearchName(fourType.get(k).getName());
                                bean2.setFourSearchCode(fourType.get(k).getCode());
                                bean2.setChildList(null);
                                beans.add(bean2);
                            }
                        }
                    }
                }
            }
        }


        for (int i = 0; i < threeType.size(); i++) {
            if (threeType.get(i).getName().startsWith(text)){
                //输入匹配到市
                //要查找上面的国家和省是谁 后面固定填写
                //单独显示
                String oneName = "";
                String oneCode = "";
                String twoName = "";
                String twoCode = "";
                for (int j = 0; j < twoType.size(); j++) {
                    //找省
                    if (threeType.get(i).getParent().equals(twoType.get(j).getCode())){
                        twoName = twoType.get(j).getName();
                        twoCode = twoType.get(j).getCode();
                        for (int k = 0; k < oneType.size(); k++) {
                            //找国
                            if (twoType.get(j).getParent().equals(oneType.get(k).getCode())){
                                oneName = oneType.get(k).getName();
                                oneCode = oneType.get(k).getCode();
                            }
                        }
                    }
                }

                SearchBean bean = new SearchBean();
                bean.setType("0");
                bean.setTarger("2");
                bean.setOneSearchName(oneName);
                bean.setOneSearchCode(oneCode);
                bean.setTwoSearchName(twoName);
                bean.setTwoSearchCode(twoCode);
                bean.setThreeSearchName(threeType.get(i).getName());
                bean.setThreeSearchCode(threeType.get(i).getCode());
                bean.setFourSearchName("0");
                bean.setFourSearchCode("0");
                bean.setChildList(null);
                beans.add(bean);

                //想下找
                for (int j = 0; j < fourType.size(); j++) {
                    //找县
                    if (threeType.get(i).getCode().equals(fourType.get(j).getParent())){
                        SearchBean bean1 = new SearchBean();
                        bean1.setType("1");
                        bean1.setTarger("3");
                        bean1.setOneSearchName(oneName);
                        bean1.setOneSearchCode(oneCode);
                        bean1.setTwoSearchName(twoName);
                        bean1.setTwoSearchCode(twoCode);
                        bean1.setThreeSearchName(threeType.get(i).getName());
                        bean1.setThreeSearchCode(threeType.get(i).getCode());
                        bean1.setFourSearchName(fourType.get(j).getName());
                        bean1.setFourSearchCode(fourType.get(j).getCode());
                        bean1.setChildList(null);
                        beans.add(bean1);
                    }
                }
            }
        }

        for (int i = 0; i < fourType.size(); i++) {
            if (fourType.get(i).getName().startsWith(text)){
                //输入匹配县
                //type = 0
                //找到他的所有上层

                for (int j = 0; j < threeType.size(); j++) {
                    //找市
                    if (threeType.get(j).getCode().equals(fourType.get(i).getParent())){
                        SearchBean bean = new SearchBean();
                        bean.setType("0");
                        bean.setTarger("3");
                        bean.setFourSearchName(fourType.get(i).getName());
                        bean.setFourSearchCode(fourType.get(i).getCode());
                        bean.setThreeSearchName(threeType.get(j).getName());
                        bean.setThreeSearchCode(threeType.get(j).getCode());
                        for (int k = 0; k < twoType.size(); k++) {
                            //找省
                            if (twoType.get(k).getCode().equals(threeType.get(j).getParent())){
                                bean.setTwoSearchName(twoType.get(k).getName());
                                bean.setTwoSearchCode(twoType.get(k).getCode());
                                for (int l = 0; l < oneType.size(); l++) {
                                    //找国
                                    if (oneType.get(l).getCode().equals(twoType.get(k).getParent())){
                                        bean.setOneSearchName(oneType.get(l).getName());
                                        bean.setOneSearchCode(oneType.get(l).getCode());
                                    }
                                }
                            }
                        }
                        bean.setChildList(null);
                        beans.add(bean);
                    }
                }

            }
        }



    }

    private boolean isChinese(String input){
        char[] chars = input.toCharArray();
        for (int i = 0; i < input.length(); i++) {
            m = pChinese.matcher(chars[i]+"");
            if (m.matches()){
                if (i == input.length() -1)
                    return true;
            }else {
                return false;
            }
        }
        return false;
    }

    private boolean isiLetter(String input){
        m = pLetter.matcher(input);
        if (m.matches()){
            return true;
        }else {
            return false;
        }

    }

}
