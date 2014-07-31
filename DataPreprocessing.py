import csv
import json

def sample:
    x="""[{"votes": {"funny": 0, "useful": 1, "cool": 0}, "user_id": "IpaY0MueAda612hHgDGsrg", "review_id": "7FTin6KBsjFe6k1Q8UAUrA", "stars": 5, "date": "2011-08-26", "text": "One of the best thai resturants in AZ.  I love thai food and will drive out of the way to eat a good one.  But luckily for me this one is in my naighborhood.  Three good dishes that they have are the drunken noodle, the lettuce wrap & the mussels (yuphia basil).  the mussels are unique and you should try it.  I've never had it anywhere else prepared this way.  give it a try.", "type": "review", "business_id": "90AXjqb4O-wrTHDKDoDUzg"}, {"votes": {"funny": 1, "useful": 1, "cool": 1}, "user_id": "xnaMND4R9UR7wDuFIUo2tw", "review_id": "FSs7c0NXPZMBnjqZZ3Xwzg", "stars": 1, "date": "2009-11-20", "text": "This place needs an update, bigtime.  I booked 5 rooms for co-workers and I in town on business.  Upon pulling up to the hotel, I was immediately thinking I may lose my job for making the decision to stay at this dump.  The hotel is comprised of white buildings that look like a run down apartment complex.  The lobby is nice..cool decor of butt shaped floor lamps and a random lamb.  The bars are nice too, but seem to be chock full of strung out coke heads.  One co-worker had to move rooms due to an ant infestation and the AC sounds like an airplane is about to land in your room.  I've also heard that the AC doesn't keep up in the summer months, but didn't experience that as our stay was during the month of October.  All in all, I would go back to the bars here, but wouldn't ever stay here again.", "type": "review", "business_id": "cpjNtM6Hzs3YjTBFEFcL-Q"}, {"votes": {"funny": 1, "useful": 2, "cool": 1}, "user_id": "0o0VMEJeQY0pAAZ9nxErBA", "review_id": "138203-t76d7bWwt33Desw", "stars": 5, "date": "2013-02-25", "text": "I LOVE this park and drive from Scottsdale just so my pup can play here. This park is huge, which is good considering I have a 130 lb ball of energy. I hardly ever see poo piles and there are buckets and water hoses everywhere. They even have a drinking fountain for the dogs and a kiddie pool to cool off after a rigorous run. 99% of the people that come here are great. You will run into the snobby one every now and then, but hey they calm down after a bit of slobber and dog hair clings to their fancy clothes.There is a separate section for small dogs, but I see alot of them in the main big dog area too. Over all great place!", "type": "review", "business_id": "RBmUv6UN0Fp1k8p0TlDEfg"}]"""
    x = json.loads(x)
    fi = open("test.csv", "wb+")
    f = csv.writer(fi)
    # Write CSV Header, If you dont need that, remove this line
    f.writerow(["user_id", "review_id", "stars", "business_id"])
    for item in x:
        f.writerow([item["user_id"], item["review_id"], item["stars"], item["business_id"]])
    fi.close();

def review:
    fin = open('yelp_phoenix_academic_dataset.json', 'rU')
    fout = open('yelp_phoenix_academic_dataset_new.json', 'wb+')
    for line in fin:
        if line.startswith('{"votes":'):
            fout.write(line)
        else:
            continue
    fin.close()
    fout.close()

def business_id_data:
    fin = open('yelp_phoenix_academic_dataset.json', 'rU')
    fout = open('dataset_business_id.json', 'wb+')
    for line in fin:
        if line.startswith('{"business_id":'):
            x = line.find('"Restaurants"')
            if x != -1:
                fout.write(line)
        else:
            continue
    fin.close()
    fout.close()
    fin = open('dataset_business_id.json', 'rU')
    fout = open('business_id_list.txt', 'wb+')
    for line in fin:
        x = json.loads(line)
        fout.write(x["business_id"])
        fout.write("\n")
    fin.close()
    fout.close()
    fin = open('business_id_list.txt', 'rU')
    business_id_list=[]
    for line in fin:
        business_id_list.append(line[:22])
    fin.close()
    fin = open('dataset_business_id.json', 'rU')
    fout = open('business_id_list2.txt', 'wb+')
    for line in fin:
        x = json.loads(line)
        abc = x["business_id"] + ":" + x["name"] + ":"
        for cat in x["categories"]:
            abc = abc + cat + ","
        abc = abc[:-13]
        abc = abc + "\n"
        fout.write(abc.encode('utf-8'))
    fin.close()
    fout.close()
    fin = open('dataset_user_id.json', 'rU')
    fout = open('business_id_list3.txt', 'wb+')
    for line in fin:
	x = json.loads(line)
	abc = x["business_id"] + ":" + x["name"] + ":"
	for cat in x["categories"]:
		if (cat != 'Restaurants'):
			abc = abc + cat + ","
	abc = abc[:-1]
	attribdict = x["attributes"]
	if(True == attribdict.get('Good for Kids') or 'true' == attribdict.get('Good for Kids')):
		abc = abc + "," + 'Good for Kids'
	if(attribdict.get('Alcohol') == 'full_bar' or attribdict.get('Alcohol') == 'beer_and_wine'):
		abc = abc + "," + 'Alcohol'
	if(True == attribdict.get('Waiter Service') or 'true' == attribdict.get('Waiter Service')):
		abc = abc + "," + 'Waiter Service'
	if(True == attribdict.get('Accepts Credit Cards') or 'true' == attribdict.get('Accepts Credit Cards')):
		abc = abc + "," + 'Accepts Credit Cards'
	if(True == attribdict.get('Wheelchair Accessible') or 'true' == attribdict.get('Wheelchair Accessible')):
		abc = abc + "," + 'Wheelchair Accessible'
	if(attribdict.get('Wi-Fi') == 'free' or attribdict.get('Wi-Fi') == 'paid'):
		abc = abc + "," + 'WiFi'
	if(True == attribdict.get('Outdoor Seating') or 'true' == attribdict.get('Outdoor Seating')):
		abc = abc + "," + 'Outdoor Seating'
	if(True == attribdict.get('Has TV') or 'true' == attribdict.get('Has TV')):
		abc = abc + "," + 'Has TV'
	if(True == attribdict.get('Takes Reservations') or 'true' == attribdict.get('Takes Reservations')):
		abc = abc + "," + 'Takes Reservations'
	if(True == attribdict.get('Delivery') or 'true' == attribdict.get('Delivery')):
		abc = abc + "," + 'Delivery'
	if(True == attribdict.get('Take-out') or 'true' == attribdict.get('Take-out')):
		abc = abc + "," + 'Take-out'
	if(True == attribdict.get('Good For Groups') or 'true' == attribdict.get('Good For Groups')):
		abc = abc + "," + 'Good For Groups'
	abc = abc + "\n"
	fout.write(abc.encode('utf-8'))
    fin.close()
    fout.close()

def votes_data:
    fin = open('dataset_vote.json', 'rU')
    fout = open('dataset_vote_filter.csv', 'wb+')
    writer = csv.writer(fout)
    for line in fin:
        x = json.loads(line)
        business_id = x["business_id"]
        if business_id in business_id_list:
            writer.writerow([x["user_id"], x["business_id"], x["stars"]])
    fin.close()
    fout.close()

def user_id_data:
    fin = open('yelp_phoenix_academic_dataset.json', 'rU')
    fout = open('dataset_user_id.json', 'wb+')
    for line in fin:
        if line.startswith('{"yelping_since":'):
                fout.write(line)
    fin.close()
    fout.close()
    fin = open('dataset_user_id.json', 'rU')
    fout = open('user_id_list.txt', 'wb+')
    for line in fin:
        x = json.loads(line)
        fout.write(x["user_id"])
        fout.write("\n")
    fin.close()
    fout.close()
    fin = open('dataset_user_id.json', 'rU')
    fout = open('user_id_list2.txt', 'wb+')
    for line in fin:
        x = json.loads(line)
        abc = x["user_id"] + ":" + x["name"] + "\n"
        fout.write(abc.encode('utf-8'))
    fin.close()
    fout.close()
    
def makecsv:
    fin = open('yelp_phoenix_academic_dataset_new.json', 'rU')
    fout = open('review.csv', 'wb+')
    writer = csv.writer(fout)
    writer.writerow(["user_id", "review_id", "stars", "business_id"])
    for line in fin:
        x = json.loads(line)
        writer.writerow([x["user_id"], x["review_id"], x["stars"], x["business_id"]])
    fin.close()
    fout.close()
    
