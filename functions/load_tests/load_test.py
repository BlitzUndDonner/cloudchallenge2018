import requests
import random

URL = 'https://europe-west1-cloud-hackathon-team-athena.cloudfunctions.net/counters/'

airport_codes = ['GVA', 'ZRH', 'BSL', 'JFK', 'CDG']

counter = 0

while True:
    counter += 1
    airport = random.choice(airport_codes)
    response = requests.get(URL + airport)
    if (counter % 10000):
        print(counter)