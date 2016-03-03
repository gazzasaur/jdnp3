import json
import time
import random
import urllib2
import exceptions

def CREATE_DATA():
    return {
        'type': 'createDevice',
        'dataLink': '',
        'site': '',
        'device': '',
        'deviceFactory': '',
        'primaryAddress': '',
        'extendedConfiguration': {
            'binaryInputPoints': [],
            'binaryOutputPoints': [],
            'analogInputPoints': [],
            'analogOutputPoints': [],
            'counterPoints': [],
            'customTypes': [],
        },
    }

class Control:
    def __init__(self, url):
        self.url = url
        
    def destroy_all(self):
        data = {'type': 'destroyAll'};
        return self.postMessage(data)
    
    def createOutstation(self, deviceFactory, site, device, address, binaryInputPoints=[], binaryOutputPoints=[], analogInputPoints=[], analogOutputPoints=[], counterPoints=[], customTypes=[]):
        data = CREATE_DATA()
        data['site'] = site
        data['device'] = device
        data['primaryAddress'] = address
        data['deviceFactory'] = deviceFactory
        data['extendedConfiguration']['binaryInputPoints'] = binaryInputPoints
        data['extendedConfiguration']['binaryOutputPoints'] = binaryOutputPoints
        data['extendedConfiguration']['analogInputPoints'] = analogInputPoints
        data['extendedConfiguration']['analogOutputPoints'] = analogOutputPoints
        data['extendedConfiguration']['counterPoints'] = counterPoints
        data['extendedConfiguration']['customTypes'] = customTypes
        return self.postMessage(data)
    
    def destroyOutstation(self, site, device):
        data = {'type': 'destroyDevice'};
        data['site'] = site
        data['device'] = device
        return self.postMessage(data)
    
    def bindOutstation(self, site, device, address, dataLinkName):
        data = {'type': 'bindDevice'};
        data['site'] = site
        data['device'] = device
        data['address'] = address
        data['dataLinkName'] = dataLinkName
        return self.postMessage(data)
    
    def unbindOutstation(self, site, device, address, dataLinkName):
        data = {'type': 'unbindDevice'};
        data['site'] = site
        data['device'] = device
        data['address'] = address
        data['dataLinkName'] = dataLinkName
        return self.postMessage(data)
        
    def getOutstation(self, site, device, output=False):
        data = {'type': 'getDevice', 'site': site, 'device': device}
        return self.sendMessage(site, device, data, output=output)
    
    def start_data_link(self, dataLinkName):
        data = {'type': 'startDataLink', 'dataLink': dataLinkName}
        self.postMessage(data)

    def stop_data_link(self, dataLinkName):
        data = {'type': 'stopDataLink', 'dataLink': dataLinkName}
        self.postMessage(data)

    def create_data_link(self, dataLinkFactory, dataLinkName, host, port):
        data = {'type': 'createDataLink', 'dataLinkFactory': dataLinkFactory, 'dataLink': dataLinkName, 'host': host, 'port': port}
        self.postMessage(data)

    def destroy_data_link(self, dataLinkName):
        data = {'type': 'destroyDataLink', 'dataLink': dataLinkName}
        self.postMessage(data)
        
    def sendMessage(self, site, device, data, output=False):
        return self.postMessage(data, output=output)
    
    def postMessage(self, data, output=False):
        url = self.url
        request = urllib2.Request(url, json.dumps(data), {'Content-Type': 'application/json'})
        response = urllib2.urlopen(request)
        code = response.getcode()
    
        if (code < 200 or code >= 300):
            raise exceptions.RuntimeError('HTTP request failed.  HTTP Status Code: ' + str(code))
    
        string_data = response.read()
        if output:
            print string_data
        data = json.loads(string_data)
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
        