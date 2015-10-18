import json
import urllib2
import exceptions

def CREATE_DATA():
    return {
        'type': 'createDevice',
        'dataLink': '20000',
        'siteCode': 'asdf',
        'deviceCode': 'fdsa',
        'deviceFactory': 'pumpStationFactory',
        'primaryAddress': '5',
        'binaryInputPoints': ['ASDF', 'FDSA'],
    }

class Control:
    def __init__(self, url):
        self.url = url
    
    def createOutstation(self, deviceFactory, site, device, address, datalink, binaryInputPoints=[], binaryOutputPoints=[], analogInputPoints=[]):
        data = CREATE_DATA()
        data['dataLink'] = datalink
        data['siteCode'] = site
        data['deviceCode'] = device
        data['primaryAddress'] = address
        data['deviceFactory'] = deviceFactory
        data['binaryInputPoints'] = binaryInputPoints
        data['binaryOutputPoints'] = binaryOutputPoints
        data['analogInputPoints'] = analogInputPoints
        self.postMessage(data)
    
    def postMessage(self, data):
        request = urllib2.Request(self.url, json.dumps(data), {'Content-Type': 'application/json'})
        response = urllib2.urlopen(request)
        code = response.getcode()
    
        if (code < 200 or code >= 300):
            raise exceptions.RuntimeError('HTTP request failed.  HTTP Status Code: ' + str(code))
    
        data = json.loads(response.read())
        if (not 'type' in data or data['type'] == 'failure'):
            if ('reason' in data):
                raise exceptions.RuntimeError(data['reason'])
            else:
                raise exceptions.RuntimeError('Cannot decode data.')
    
