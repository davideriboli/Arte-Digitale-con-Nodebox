import json

def load_file(a):

    json_data = open(a).read()
    data = json.loads(json_data)
    return json.dumps(data)
