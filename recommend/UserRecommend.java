package com.yelp.recommend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.yelp.convert.RestaurantDataConvert;
import com.yelp.convert.UserDataConvert;

/**
 * Class which is going to perform recommendations for users.
 * This class follows user-based collaborative filtering technique.
 * Longer time to compute but surprisingly better results.
 * @author Vasanth
 *
 */
public class UserRecommend {

	public static void main(String[] args) {
		try {
			
			//Newly written FileDataModel class that helps us with Alphanumeric item and user IDs
			AlphaItemFileDataModel afdm = new AlphaItemFileDataModel(new File(
					"data/dataset_vote_filter.csv"));
			DataModel dm = afdm;
			
			//File used to fetch the name of the user/restaurant - makes more sense to show the name of the user/restaurant than some random ID
			String restaurant_filename = "data/business_id_list2.txt";
			String user_filename = "data/user_id_list2.txt";
			RestaurantDataConvert rdc = new RestaurantDataConvert(restaurant_filename);
			UserDataConvert udc = new UserDataConvert(user_filename);
			
			//Using this to write the results to a file to see what comes up as recommendations
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					"results/user/Cityblock_kNN_5users_2items.txt"));

			/*
			 * Well we have loads of similarity metrics. Feel free to choose from them. Keep the best results.
			 * Not all metrics are guaranteed to provide best results but whats the harm in trying.
			 * The perfect similarity metric depends on many factors such as dataset, what we are mining and so on.
			 */
			// UserSimilarity sim = new LogLikelihoodSimilarity(dm);
			// UserSimilarity sim = new TanimotoCoefficientSimilarity(dm);
			 UserSimilarity sim = new CityBlockSimilarity(dm);
			// UserSimilarity sim = new EuclideanDistanceSimilarity(dm);
			// UserSimilarity sim = new UncenteredCosineSimilarity(dm);
			// UserSimilarity sim = new PearsonCorrelationSimilarity(dm,org.apache.mahout.cf.taste.common.Weighting.WEIGHTED);
			// UserSimilarity sim = new SpearmanCorrelationSimilarity(dm);
			
			/*
			 * We can use two types of metrics for neighborhood calculation:
			 * 1. NearestNUserNeighborhood - kNN approach - still based on similarity metric used to define nearest neighbors
			 * 2. ThresholdUserNeighborhood - Filters users based on a threshold value of similarity
			 * */
			NearestNUserNeighborhood neighborhood = new NearestNUserNeighborhood(5, sim, dm);
			// ThresholdUserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.7, sim, dm);
			
			/*
			 * There are two types of UserBasedRecommender classes:
			 * 1. GenericUserBasedRecommender - Makes most sense
			 * 2. GenericBooleanPrefUserBasedRecommender - Does not consider preference values. Only takes a yes or no. Not very useful in our case.
			 * */
			GenericUserBasedRecommender recommender = new GenericUserBasedRecommender(dm, neighborhood, sim);

			/*
			 * Iterator that iterates through all the users and provides the recommender s output
			 * This can be modified to providing a specific user id and retrieving the recommended items.
			 * */
			for (LongPrimitiveIterator items = dm.getUserIDs(); items.hasNext();) {
				long userId = items.nextLong();
				//Get the list of recommended items for the user - mention count of items needed
				List<RecommendedItem> recommendations = recommender.recommend(userId, 2);
				//Output the recommended items on the console as well as write it to a file
				for (RecommendedItem recommendation : recommendations) {
					System.out.println(udc.getUserName(afdm.getUserIDAsString(userId)) + ","
							+ rdc.getRestaurantName(afdm.getItemIDAsString(recommendation.getItemID())) + ","
							+ recommendation.getValue());
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

