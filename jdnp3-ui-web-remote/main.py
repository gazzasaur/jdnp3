import time
import jdnp3.device

HOST_URL = 'http://127.0.0.1:8080/servlet/jsonapi'

outstation = jdnp3.device.Outstation(HOST_URL, "Pump Station 1", "Pump 1")
outstation.output()

outstation.set_internal_indicator('device restart', True)
outstation.set_binary_input_attribute(0, 'event type', 'variation', 0)
time.sleep(2)
outstation.set_binary_input_attribute(0, 'event type', 'variation', 1)
time.sleep(2)
outstation.set_binary_input_attribute(0, 'event type', 'variation', 2)
time.sleep(2)
outstation.set_binary_input_attribute(0, 'event type', 'variation', 3)
time.sleep(2)
outstation.set_binary_input_attribute(0, 'event type', 'variation', 0)
outstation.set_binary_output_attribute(0, 'remote forced', True)
time.sleep(2)
outstation.set_analog_input_attribute(1, 'value', 'Infinity')
outstation.set_internal_indicator('device restart', False)

# control = jdnp3.control.Control(HOST_URL)
#
# customType = {
#     'expectedData': '46011B012907',
#     'responseData': '1b0129070000',
#     'functionCode': 'READ',
# }
# 
# for stationIndex in range(10, 11):
#     for deviceIndex in range(0, 1):
#         control.createOutstation('pumpStationFactory', 'Pump Station ' + str(stationIndex).zfill(5), 'Pump ' + str(deviceIndex).zfill(5), stationIndex, "20000", binaryOutputPoints=['asdf', '1234'], customTypes=[customType])
# 
# data = {
#     'type': 'getDevice'
# }
# device = control.sendMessage('Pump Station 1', 'Pump 1', data)
# 
# aic = jdnp3.control.AnalogInputController(10000, 100, 15000, 50000, 2)
# 
# while True:
#     data = device['analogInputPoints'][0]
#     data['value'] = aic.fetchValue()
#     control.sendMessage('Pump Station 1', 'Pump 1', data)
#     
#     data = device['analogInputPoints'][1]
#     data['value'] = 1000 + int(100*100*(random.random() - 0.5))/100.0
#     control.sendMessage('Pump Station 1', 'Pump 1', data)
#     
#     time.sleep(0.1)
# 