package com.yelp.convert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that is going to be used to map the restaurant name to the restaurant ID. 
 * Will be used while displaying the results.
 * @author Vasanth
 *
 */
public class RestaurantDataConvert {

	//Hashmap used to map restaurant name with its ID
	private final Map<String,String> restaurantMap;

	/*Constructor which processes the file and forms the map with:
	 * Key -> Restaurant ID
	 * Value -> Restaurant Name
	 */
	public RestaurantDataConvert(String filename) throws IOException {
		this.restaurantMap = new HashMap<String,String>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while((line = br.readLine()) != null){
			String[] values = line.split(":",-2);
			restaurantMap.put(values[0], values[1]);
		}
		br.close();
	}
	
	//Method which is used to fetch the restaurant name from the map with the ID
	public String getRestaurantName(String restaurantId) throws IOException {
		return restaurantMap.get(restaurantId);
	}

}
