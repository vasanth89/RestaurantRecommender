/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yelp.convert;

import java.util.regex.Pattern;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;

import com.yelp.recommend.ItemUserMemIDMigrator;

/**
 * This class is used to enable the attribute-aware approach to collaborative
 * filtering. This class serves as the data structure which is going to hold the
 * restaurant information.
 * 
 * @author Vasanth
 * 
 */
public final class RestaurantData {

	// initialize required variables
	private ItemUserMemIDMigrator restaurantIdMigtr;
	private ItemUserMemIDMigrator attributeMigtr;
	private static final Pattern SEPARATOR = Pattern.compile("\\:");
	private static final Pattern COMMA = Pattern.compile("\\,");
	private static final FastIDSet NO_ATTRIB = new FastIDSet();

	private long restaurantID;
	private String restaurantAlphaID;
	private final String restaurantName;
	private final FastIDSet attributeSet;

	public RestaurantData(String line) throws TasteException {
		String[] tokens = SEPARATOR.split(line);

		// store the restaurant alpha numeric ID
		restaurantAlphaID = tokens[0];

		restaurantID = 0;
		if (restaurantIdMigtr == null)
			restaurantIdMigtr = new ItemUserMemIDMigrator();
		try {
			// store the restaurant ID which has been converted to long value
			restaurantID = restaurantIdMigtr.singleInit(tokens[0]);
		} catch (TasteException e) {
			e.printStackTrace();
		}

		// store the restaurant name
		restaurantName = tokens[1];

		/*
		 * Loop through the attributes which include parameters such as:
		 * Cuisine, Good for Kids, Alcohol, Waiter Service, Accepts Credit
		 * Cards, Wheelchair Accessible, Wi-Fi, Outdoor Seating, Has TV, Takes
		 * Reservations, Delivery, Take-out, Good For Groups
		 */
		if (tokens.length > 2) {
			String[] attributes = COMMA.split(tokens[2]);
			attributeSet = new FastIDSet(attributes.length);
			for (String attrib : attributes) {
				if (attributeMigtr == null)
					attributeMigtr = new ItemUserMemIDMigrator();
				try {
					attributeSet.add(attributeMigtr.singleInit(attrib));
				} catch (TasteException e) {
					e.printStackTrace();
				}
			}
		} else {
			attributeSet = NO_ATTRIB;
		}
	}

	// method for reverse mapping of restaurant ID from long to alphanumeric
	// value
	public String getRestaurantIDAsString() throws TasteException {
		return restaurantIdMigtr.toStringID(restaurantID);
	}

	// getter method for restaurant ID(long)
	public long getRestaurantID() {
		return restaurantID;
	}

	// getter method for restaurant ID(actual)
	public String getRestaurantAlphaID() {
		return restaurantAlphaID;
	}

	// getter method for restaurant name
	public String getRestaurantName() {
		return restaurantName;
	}

	// getter method for attributes
	public FastIDSet getAttributeSet() {
		return attributeSet;
	}

	// method to do reverse mapping from long to string
	public String[] getAttributesAsString() throws TasteException {
		String[] attributes = new String[attributeSet.size()];
		int i = 0;
		LongPrimitiveIterator iterator = attributeSet.iterator();
		while (iterator.hasNext()) {
			attributes[i] = attributeMigtr.toStringID(iterator.next());
			i++;
		}
		return attributes;
	}

}
