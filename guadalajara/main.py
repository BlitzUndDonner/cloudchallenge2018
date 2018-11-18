import mysql_sample

def writeToMySQL(data, context):
    """Background Cloud Function to be triggered by Pub/Sub.
    Args:
         data (dict): The dictionary with data specific to this type of event.
         context (google.cloud.functions.Context): The Cloud Functions event
         metadata.
    """
    import base64

    if 'data' in data:
        name = base64.b64decode(data['data']).decode('utf-8')
    else:
        name = 'World'
    print('Hello, {}!'.format(name))
    sqlresponse = mysql_sample.mysql_demo(name)
    print('SQL Response was {}'.format(sqlresponse))



