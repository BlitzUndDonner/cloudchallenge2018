import logging 
from google.cloud import bigquery

DATASET_ID = 'cloud-hackathon-team-athena:flight_messages'

def counters(request):
    """HTTP Cloud Function.
    Args:
        request (flask.Request): The request object.
        <http://flask.pocoo.org/docs/0.12/api/#flask.Request>
    Returns:
        The response text, or any set of values that can be turned into a
        Response object using `make_response`
        <http://flask.pocoo.org/docs/0.12/api/#flask.Flask.make_response>.
    """
    
    #path = request.path
    #airport = path.split('/')[-1]
    
    airport = 'ZRH'
    countByAirport = query_bigquery(airport)  
    
    logging.info("Count for airport '%s' = %d", airport, countByAirport)
    return airport
    
    

def query_bigquery(airport_code):
    # Instantiates a client
    bigquery_client = bigquery.Client()
    query_job = client.query("SELECT COUNT(*) FROM flight_messages.raw_flight_messages WHERE airport = " + airport_code)
    results = query_job.result() 
    return results[0]
