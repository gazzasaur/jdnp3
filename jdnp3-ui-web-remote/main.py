import time
import jdnp3.device
import jdnp3.control

HOST_URL = 'http://127.0.0.1:8080/servlet/jsonapi'

control = jdnp3.control.Control(HOST_URL)

customType = {
    'expectedData': '46011B012907',
    'responseData': '1b0129070000',
    'functionCode': 'READ',
}

binaryInputDataPoints = [
    'Running',
    'Low Fuel',
    'Non-Urgent Fail',
    'Urgent Fail',
]

binaryOutputDataPoints = [
    'Running',
]

analogInputDataPoints = [
    'Speed',
    'Volume',
]

counterDataPoints = [
    'Other',
]

# An object to rule them all.
outstation_manager = jdnp3.device.OutstationManager(HOST_URL)

# Or you could address a single object at a time.
outstation = jdnp3.device.Outstation(HOST_URL, "Pump Station 1", "Pump 1")

# For those who like to really clean up.
control.destroy_all()
control.create_data_link("dataLinkFactory", "20000", "0.0.0.0", 20000)
control.createOutstation('pumpStationFactory', 'Pump Station 1', 'Pump 1', 3, counterPoints=counterDataPoints)
outstation.set_internal_indicator('device restart', True)
control.bindOutstation('Pump Station 1', 'Pump 1', 3, "20000")
control.start_data_link("20000")
 
config = outstation.get()
outstation.output()

sitesInfo = control.list_outstations()
for site in sitesInfo['siteDeviceLists']:
    print('Site: %s' % (site['site']))
    for device in site['devices']:
        print('\tDevice: %s' % (device))

outstation.set_internal_indicator('device restart', True)
outstation.set_binary_input(1, 'local forced', True)
outstation.set_binary_input(0, 'event type', 'variation', 0)

# String 'NaN' or float nan.
# On wait for it must be aparseable python value or exact value.
outstation.set_analog_output(0, 'value', 'NaN');
outstation.wait_for_analog_output(0, 'value', 'NaN');

# On the setter, you may either use the full string Infinity and -Infinity or use the float inf and -inf
# On wait for it must be aparseable python value or exact value.
outstation.set_analog_input(0, 'value', 0.1);
outstation.set_analog_input(0, 'static type', 'variation', '5');
outstation.set_analog_input(0, 'value', 'Infinity');
outstation.set_analog_input(0, 'value', float('inf'));
outstation.wait_for_analog_input(0, 'value', 'inf');
outstation.set_analog_input(0, 'value', '1000.0');
outstation.wait_for_analog_input(0, 'value', '1000');
outstation.wait_for_analog_input(0, 'value', '1000.0');

# Simulate soft restart.
control.unbindOutstation('Pump Station 1', 'Pump 1', 3, "20000")
outstation.set_internal_indicator('device restart', True)
control.bindOutstation('Pump Station 1', 'Pump 1', 3, "20000")
try:
    outstation.wait_for_internal_indicator('device restart', False)
except:
    print("WARNING: You may not have a master polling this.")

outstation.set_binary_input(0, 'event type', 'variation', 1)
outstation.set_binary_input(0, 'event type', 'variation', 2)
outstation.set_binary_input(0, 'event type', 'variation', 3)
outstation.set_binary_input(0, 'event type', 'variation', 0)
outstation.set_binary_output(0, 'active', True)
outstation.wait_for_binary_input(0, 'event type', 'variation', 0);
outstation.set_binary_output(0, 'remote forced', True)
outstation.set_analog_input(1, 'value', 'Infinity')
outstation.set_internal_indicator('device restart', False)
outstation.trigger_event(0, 'binary output event')
 
outstation.set_analog_output(1, 'value', -20.3)
outstation.set_analog_output(1, 'value', 'Infinity')

outstation.set_double_bit_binary_input(0, 'value', 2)

outstation.set_counter(1, 'rollover', False)
for i in range(1,1000):
    outstation_manager.set_counter('Pump Station 1', 'Pump 1', 1, 'value', str(i))
    outstation.wait_for_counter(1, 'value', str(i))
outstation.set_counter(1, 'rollover', True)
outstation.set_counter(1, 'value', 0)

outstation.set_counter(0, 'value', 123);

outstation.set_double_bit_binary_input(0, 'value', 3)

time.sleep(10)
outstation.trigger_event(0, 'counter event', 1500);
outstation.trigger_event(0, 'binary input event', 2600);

# Simulate device cold restart.
control.stop_data_link("20000")
outstation.set_internal_indicator("device restart", True)
outstation.wait_for_internal_indicator("device restart", True)
control.start_data_link("20000")

# Create another RTU on the same datalink
control.createOutstation('muffinFactory', 'Muffin Factory', 'Factory Floor', 4)
control.bindOutstation('Muffin Factory', 'Factory Floor', 4, "20000")

outstation = jdnp3.device.Outstation(HOST_URL, "Muffin Factory", "Factory Floor")
outstation.set_binary_input(10, 'active', True)
outstation.wait_for_binary_input(10, 'active', True)
outstation.set_binary_input(10, 'active', False)
outstation.wait_for_binary_input(10, 'active', False)

###

# A global or device specific websocket may also be created for realtime updates.
#
# from websockets.sync.client import connect
#
# with websockets.sync.client.connect('ws://localhost:8080/ws/globalDevice') as ws:
#     data = ws.recv() # This is a JSON data point
#
#     # Device specific websocket
#     with connect("ws://localhost:8080/ws/device?stationCode=Muffin%20Factory&deviceCode=Factory%20Floor") as websocket:
#         # Do stuff here using send and recv
#         pass
#
#     # Everything
#     with connect("ws://localhost:8080/ws/globalDevice") as websocket:
#         # Do stuff here using send and recv
#         pass

###

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