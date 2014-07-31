package com.yelp.recommend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.yelp.convert.RestaurantDataConvert;
import com.yelp.convert.UserDataConvert;

/**
 * Class which is going to perform recommendations for users.
 * This class follows item-based collaborative filtering technique.
 * Faster than user-based because computations can be done offline.
 * @author Vasanth
 *
 */
public class ItemRecommend {

	public static void main(String[] args) {
		try {
			
			//Newly written FileDataModel class that helps us with Alphanumeric item and user IDs
			AlphaItemFileDataModel afdm = new AlphaItemFileDataModel(new File(
					"data/all/vote_all.csv"));
			DataModel dm = afdm;
			
			//File used to fetch the name of the user/restaurant - makes more sense to show the name of the user/restaurant than some random ID
			String restaurant_filename = "data/all/business_list_all.txt";
			String user_filename = "data/all/user_id_all.txt";
			RestaurantDataConvert rdc = new RestaurantDataConvert(restaurant_filename);
			UserDataConvert udc = new UserDataConvert(user_filename);
			
			//Using this to write the results to a file to see what comes up as recommendations
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					"results/final/all_users.txt"));

			/*
			 * Well we have loads of similarity metrics. Feel free to choose from them. Keep the best results.
			 * Not all metrics are guaranteed to provide best results but whats the harm in trying.
			 * The perfect similarity metric depends on many factors such as dataset, what we are mining and so on.
			 */
			// ItemSimilarity sim = new LogLikelihoodSimilarity(dm);
			// ItemSimilarity sim = new TanimotoCoefficientSimilarity(dm);
			// ItemSimilarity sim = new CityBlockSimilarity(dm);
			// ItemSimilarity sim = new EuclideanDistanceSimilarity(dm);
			// ItemSimilarity sim = new UncenteredCosineSimilarity(dm);
			// ItemSimilarity sim = new PearsonCorrelationSimilarity(dm,org.apache.mahout.cf.taste.common.Weighting.WEIGHTED);

			/*
			 * There are two types of ItemBasedRecommender classes:
			 * 1. GenericItemBasedRecommender - Makes most sense
			 * 2. GenericBooleanPrefItemBasedRecommender - Does not consider preference values. Only takes a yes or no. Not very useful in our case.
			 * 3. RestaurantRecommender - My precious! Custom written class that is going to include the attribute aware collaborative filtering approach.
			 * 	                          This mostly does what a recommender class does but only the similarity metric part is changed.
			 * */
			// GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dm, sim);
			 RestaurantRecommender recommender = new RestaurantRecommender(dm,restaurant_filename);

			/*
			 * Iterator that iterates through all the users and provides the recommender s output
			 * This can be modified to providing a specific user id and retrieving the recommended items.
			 * */
			for (LongPrimitiveIterator users = dm.getUserIDs(); users.hasNext();) {
				long userId = users.nextLong();
				//Get the list of recommended items for the user - mention count of items needed
				List<RecommendedItem> recommendations = recommender.recommend(userId, 2);
				//Output the recommended items on the console as well as write it to a file
				for (RecommendedItem recommendation : recommendations) {
					bw.write("User Id : "
							+ udc.getUserName(afdm.getUserIDAsString(userId))
							+ "\tRecommendation Item Id : "
							+ rdc.getRestaurantName(afdm.getItemIDAsString(recommendation.getItemID()))
							+ "\tRecommendation Value : "
							+ recommendation.getValue() + "\n");
				}
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("There was an error!!");
			e.printStackTrace();
		} catch (TasteException e) {
			System.out.println("There was a Tase Exception!!");
			e.printStackTrace();
		}

	}

}
