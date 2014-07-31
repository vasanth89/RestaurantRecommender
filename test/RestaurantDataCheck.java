package com.yelp.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;

import com.yelp.convert.RestaurantData;

/**
 * Class to check if the mapping of restaurant ID to restaurant data worked fine
 * 
 * @author Vasanth
 * 
 */
public class RestaurantDataCheck {

	public static void main(String[] args) throws IOException, TasteException {
		BufferedReader br = new BufferedReader(new FileReader(
				"data/business_id_list2.txt"));
		FastByIDMap<RestaurantData> RestaurantList = new FastByIDMap<RestaurantData>();
		Map<String, Long> restaurantMap = new HashMap<String, Long>();
		String line;
		long value;
		while ((line = br.readLine()) != null) {
			RestaurantData restaurantDatum = new RestaurantData(line);
			RestaurantList.put(restaurantDatum.getRestaurantID(),
					restaurantDatum);
			restaurantMap.put(restaurantDatum.getRestaurantIDAsString(),
					restaurantDatum.getRestaurantID());
		}
		br.close();
		br = new BufferedReader(new FileReader("data/business_id_list.txt"));

		while ((line = br.readLine()) != null) {
			value = restaurantMap.get(line);
			RestaurantData restaurantDatum = RestaurantList.get(value);
			System.out.println("Restaurant Name : "
					+ restaurantDatum.getRestaurantName() + "Restaurant ID : "
					+ restaurantDatum.getRestaurantID()
					+ "Restaurant Alpha Id : "
					+ restaurantDatum.getRestaurantAlphaID());
			String[] attributes = restaurantDatum.getAttributesAsString();
			for (String attrib : attributes) {
				System.out.print("Attribute Name : " + attrib);
			}
			System.out.println();
		}
	}

}
