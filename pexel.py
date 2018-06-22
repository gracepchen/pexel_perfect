import sys
import random
import requests # import the requests library
from bs4 import BeautifulSoup # import the BeautifulSoup library

if len(sys.argv) != 2:
	print("Please enter a topic to search for images.")
	sys.exit(2)

# get data of whatever user wants to search for and save
webpage = "https://www.pexels.com/search/" + sys.argv[1] 
request = requests.get(webpage)

# Use BeautifulSoup to parse the DOM tree from the request's HTML text
soup = BeautifulSoup(request.text, 'html.parser')

# First remove non-ascii characters
# prettyString = soup.prettify().encode('utf-8').strip()

# find all images
images = soup.find_all('img', attrs={'class': 'photo-item__img'})

if len(images) is 0:
	print("Skipping")
	sys.exit(1)

# get list of image urls and select random url
image_urls = []

for url in images:
    image_urls.append(url['src'].strip())

# return output - random image url
rand_num = random.randint(0, len(image_urls))
print(image_urls[rand_num])
