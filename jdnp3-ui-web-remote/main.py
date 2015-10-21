import time
import random
import jdnp3.control

HOST_URL = 'http://127.0.0.1:8080/servlet/jsonapi'

control = jdnp3.control.Control(HOST_URL)

#for stationIndex in range(0, 100):
#    for deviceIndex in range(0, 100):
#        control.createOutstation('pumpStationFactory', 'Pump Station ' + str(stationIndex).zfill(5), 'Pump ' + str(deviceIndex).zfill(5), stationIndex, "20000")

data = {
    'type': 'getDevice'
}
device = control.sendMessage('Pump Station 1', 'Pump 1', data)

aic = jdnp3.control.AnalogInputController(10000, 100, 15000, 50000, 2)

while True:
    data = device['analogInputPoints'][0]
    data['value'] = aic.fetchValue()
    control.sendMessage('Pump Station 1', 'Pump 1', data)
    
    data = device['analogInputPoints'][1]
    data['value'] = 1000 + int(100*100*(random.random() - 0.5))/100.0
    control.sendMessage('Pump Station 1', 'Pump 1', data)
    
    time.sleep(0.1)
