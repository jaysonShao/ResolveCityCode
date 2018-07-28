package com.example.jayson.resolvecitycode.utils;


import com.example.jayson.resolvecitycode.bean.ZBeanCity;

import java.util.Comparator;

public class CityPinyinComparator implements Comparator<ZBeanCity> {

	public int compare(ZBeanCity o1, ZBeanCity o2) {
		if (o1.getName().equals("@") || o2.getName().equals("#")) {
			return -1;
		} else if (o1.getName().equals("#") || o2.getName().equals("@")) {
			return 1;
		} else {
			return o1.getName().compareTo(o2.getName());
		}
	}

}
