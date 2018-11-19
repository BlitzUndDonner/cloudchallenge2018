from google.cloud import bigquery
import logging

DATASET_ID = 'cloud-hackathon-team-athena:flight_messages'

def airport_counter(request):    
    return query_bigquery('ZRH')

def query_bigquery(airport_code):
    # Instantiates a client
    bigquery_client = bigquery.Client()
    query_job = client.query("SELECT COUNT(*) FROM flight_messages.raw_flight_messages WHERE airport = " + airport_code)
    results = query_job.result() 
    return results[0]