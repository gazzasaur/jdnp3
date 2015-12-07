
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

# For those who like to really clean up.
outstation = jdnp3.device.Outstation(HOST_URL, "Pump Station 1", "Pump 1")

control.destroy_all()
control.create_data_link("dataLinkFactory", "20000", "0.0.0.0", 20000)
control.createOutstation('pumpStationFactory', 'Pump Station 1', 'Pump 1', 3, counterPoints=counterDataPoints)
outstation.set_internal_indicator('device restart', True)
control.bindOutstation('Pump Station 1', 'Pump 1', 3, "20000")
control.start_data_link("20000")
 
outstation = jdnp3.device.Outstation(HOST_URL, "Pump Station 1", "Pump 1")
config = outstation.get()
outstation.output()
 
outstation.set_internal_indicator('device restart', True)
outstation.set_binary_input(1, 'local forced', True)
outstation.set_binary_input(0, 'event type', 'variation', 0)
 
outstation.set_analog_input(0, 'value', 'Infinity');
outstation.wait_for_analog_input(0, 'value', 'Infinity');
outstation.set_analog_input(0, 'value', '1000.0');
outstation.wait_for_analog_input(0, 'value', '1000.0');
 
outstation.set_binary_input(0, 'event type', 'variation', 1)
outstation.set_binary_input(0, 'event type', 'variation', 2)
outstation.set_binary_input(0, 'event type', 'variation', 3)
outstation.set_binary_input(0, 'event type', 'variation', 0)
outstation.set_binary_output(0, 'active', True)
outstation.wait_for_binary_input(0, 'event type', 'variation', 0);
outstation.set_binary_output(0, 'remote forced', True)
outstation.set_analog_input(1, 'value', 'Infinity')
outstation.set_internal_indicator('device restart', False)
 
outstation.set_analog_output(1, 'value', -20.3)
outstation.set_analog_output(1, 'value', 'Infinity')
 
outstation.set_counter(1, 'rollover', False)
for i in range(1,1000):
    outstation.set_counter(1, 'value', i)
    outstation.wait_for_counter(1, 'value', i)
outstation.set_counter(1, 'rollover', True)
outstation.set_counter(1, 'value', 0)
 
# Simulate device cold restart.
control.stop_data_link("20000")
outstation.set_internal_indicator("device restart", True)
outstation.wait_for_internal_indicator("device restart", True)
control.start_data_link("20000")

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