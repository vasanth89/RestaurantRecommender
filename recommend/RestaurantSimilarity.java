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

package com.yelp.recommend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import com.yelp.convert.RestaurantData;

/**
 * This class is used to implement the attribute aware collaborative filtering approach.
 * This class performs a tanimoto style similarity calculation considering attributes of a restaurant.
 * @author Vasanth
 *
 */
final class RestaurantSimilarity implements ItemSimilarity {

	//Map used to store the Restaurant Data along with the restaurant ID(long)
	private final FastByIDMap<RestaurantData> RestaurantData;

	/*Constructor that builds the map of restaurant data:
	 * Key -> Restaurant ID(long)
	 * Value ->  RestaurantData
	 */
	RestaurantSimilarity(String filename) throws IOException, TasteException {
		RestaurantData = new FastByIDMap<RestaurantData>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = br.readLine()) != null) {
			RestaurantData restaurantDatum = new RestaurantData(line);
			RestaurantData.put(restaurantDatum.getRestaurantID(), restaurantDatum);
		}
		br.close();
	}

	@Override
	public double itemSimilarity(long itemID1, long itemID2) {
		if (itemID1 == itemID2) {
			return 1.0;
		}
		RestaurantData data1 = RestaurantData.get(itemID1);
		RestaurantData data2 = RestaurantData.get(itemID2);
		if (data1 == null || data2 == null) {
			return 0.0;
		}

		// Tanimoto coefficient similarity based on attributes of restaurant
		
		FastIDSet attributes1 = data1.getAttributeSet();
		FastIDSet attributes2 = data2.getAttributeSet();
		if (attributes1 == null || attributes2 == null) {
			return 0.0;
		}
		int intersectionSize = attributes1.intersectionSize(attributes2);
		if (intersectionSize == 0) {
			return 0.0;
		}
		int unionSize = attributes1.size() + attributes2.size() - intersectionSize;
		return (double) intersectionSize / (double) unionSize;
	}

	@Override
	public double[] itemSimilarities(long itemID1, long[] itemID2s) {
		int length = itemID2s.length;
		double[] result = new double[length];
		for (int i = 0; i < length; i++) {
			result[i] = itemSimilarity(itemID1, itemID2s[i]);
		}
		return result;
	}

	@Override
	public long[] allSimilarItemIDs(long itemID) {
		FastIDSet allSimilarItemIDs = new FastIDSet();
		LongPrimitiveIterator allItemIDs = RestaurantData.keySetIterator();
		while (allItemIDs.hasNext()) {
			long possiblySimilarItemID = allItemIDs.nextLong();
			if (!Double.isNaN(itemSimilarity(itemID, possiblySimilarItemID))) {
				allSimilarItemIDs.add(possiblySimilarItemID);
			}
		}
		return allSimilarItemIDs.toArray();
	}

	@Override
	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		// do nothing
	}

}
