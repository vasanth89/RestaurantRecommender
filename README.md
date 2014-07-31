RestaurantRecommender
=====================

This project aims at building an intelligent restaurant recommender system. General recommendation systems only consider the rating of restaurants given by users. This system aims to consider the various factors involved with each restaurant and use them to provide better recommendations. In short, the attempt is to design and attribute-aware collaborative filtering approach to restaurant recommendations.

Folder structure
================

convert -> Files in the folder are used to parse the data in JSON format for processing. Since the restaurant ID, user ID are identified using alphanumeric characters these files are used to map the names with the respective IDs.

check -> Just used to check if the mapping is working fine.

recommend -> Major folder that contains all the necessary files used in recommender system. This project used the Mahout library for building the recommender system and Yelp dataset.
