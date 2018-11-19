import logging 


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
    
    path = request.path
    airport = path.split('/')[-1]
    
    countByAirport = 0
    
    logging.info("Count for airport '%s' = %d", airport, count)
    return airport
    