import json
import time
import random
import decimal
import urllib
import urllib2
import exceptions

def CREATE_DATA():
    return {
        'type': 'createDevice',
        'dataLink': '',
        'siteCode': '',
        'deviceCode': '',
        'deviceFactory': '',
        'primaryAddress': '',
        'extendedConfiguration': {
            'binaryInputPoints': [],
            'binaryOutputPoints': [],
            'analogInputPoints': [],
            'customTypes': [],
        },
    }

class Control:
    def __init__(self, url):
        self.url = url
    
    def createOutstation(self, deviceFactory, site, device, address, datalink, binaryInputPoints=[], binaryOutputPoints=[], analogInputPoints=[], customTypes=[]):
        data = CREATE_DATA()
        data['dataLink'] = datalink
        data['siteCode'] = site
        data['deviceCode'] = device
        data['primaryAddress'] = address
        data['deviceFactory'] = deviceFactory
        data['extendedConfiguration']['binaryInputPoints'] = binaryInputPoints
        data['extendedConfiguration']['binaryOutputPoints'] = binaryOutputPoints
        data['extendedConfiguration']['analogInputPoints'] = analogInputPoints
        data['extendedConfiguration']['customTypes'] = customTypes
        return self.postMessage(data)
        
    def getOutstation(self, site, device, output=False):
        data = {'type': 'getDevice'}
        return self.sendMessage(site, device, data, output=output)

        
    def sendMessage(self, site, device, data, output=False):
        query = [('siteCode', site), ('deviceCode', device)]
        return self.postMessage(data, query, output=output)
    
    def postMessage(self, data, query=None, output=False):
        url = self.url
        if query:
            url += '?' + urllib.urlencode(query)
        request = urllib2.Request(url, json.dumps(data), {'Content-Type': 'application/json'})
        response = urllib2.urlopen(request)
        code = response.getcode()
    
        if (code < 200 or code >= 300):
            raise exceptions.RuntimeError('HTTP request failed.  HTTP Status Code: ' + str(code))
    
        string_data = response.read()
        if output:
            print string_data
        data = json.loads(string_data, parse_float=decimal.Decimal)
        if (not 'type' in data or data['type'] == 'failure'):
            if ('reason' in data):
                raise exceptions.RuntimeError(data['reason'])
            else:
                raise exceptions.RuntimeError('Cannot decode data.')
        return data

def wait_for(func, sleep=0.100, timeout=1.0, *args):
    timeout = time.time() + timeout;
    
    while (time.time() < timeout):
        if (func(*args)):
            return True
        time.sleep(sleep)
        
    return False

class AutoControl:
    def __init__(self, control, site, device):
        self.control = control
        self.analogInputController = {}
        self.device = control.getOutstation(site, device)
        
    def randomiseAnalogInput(self, index, mean, standardDeviation):
        if (not self.analogInputController[str(index)]):
            pass
        
class AnalogInputController:
    def __init__(self, value, standardDeviation=0, rampValue=None, rampTime=None, rounding=2):
        self.value = value
        self.rounding = rounding
        self.standardDeviation = standardDeviation
        self.rampValue = rampValue if (rampValue) else value
        self.rampStart = 1000*time.time()
        self.rampFinish = (1000*time.time() + rampTime) if (rampTime) else self.rampStart
        
    def fetchValue(self):
        if (self.rampStart != self.rampFinish):
            currentTime = min(1000*time.time(), self.rampFinish)
            self.value = (self.rampValue - self.value)*(currentTime - self.rampStart)/(self.rampFinish - self.rampStart) + self.value
            self.rampStart = currentTime
            
        return int(pow(10.0, self.rounding)*random.gauss(self.value, self.standardDeviation))/pow(10.0, self.rounding)
        
    def isComplete(self):
        return self.rampStart == self.rampFinish
        