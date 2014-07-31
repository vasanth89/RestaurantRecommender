package com.yelp.convert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class that is going to be used to map the user name to the user ID. 
 * Will be used while displaying the results.
 * @author Vasanth
 *
 */
public class UserDataConvert {

	//Hashmap used to map user name with his/her ID
	private final Map<String,String>userMap;
	private static final Pattern SEPARATOR = Pattern.compile("\\:");

	/*Constructor which processes the file and forms the map with:
	 * Key -> User ID
	 * Value -> User Name
	 */
	public UserDataConvert(String filename) throws IOException {
		this.userMap = new HashMap<String,String>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while((line = br.readLine()) != null){
			String[] values = SEPARATOR.split(line);
			userMap.put(values[0], values[1]);
		}
		br.close();
	}
	
	//Method which is used to fetch the user name from the map with the ID
	public String getUserName(String userId) throws IOException {
		return userMap.get(userId);
	}

}
