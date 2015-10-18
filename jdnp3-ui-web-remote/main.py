import jdnp3.control

HOST_URL = 'http://127.0.0.1:8080/servlet/jsonapi'

control = jdnp3.control.Control(HOST_URL)

for stationIndex in range(0, 100):
    for deviceIndex in range(0, 100):
        control.createOutstation('pumpStationFactory', 'Pump Station ' + str(stationIndex).zfill(5), 'Pump ' + str(deviceIndex).zfill(5), stationIndex, "20000")