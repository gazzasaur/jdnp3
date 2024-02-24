import inspect
import jdnp3.control

WAIT_TIMEOUT = 2.0
FLOATING_POINT_TOLERANCE = 1e-09

class Outstation:
    def __init__(self, url, site, device):
        self.site = site
        self.device = device
        self.control = jdnp3.control.Control(url)

    def get_internal_indicator(self, field):
        data = self.get();
        field = self.camel_case(field)
        return data['internalIndicators'][field]

    def wait_for_internal_indicator(self, field, value):
        def func():
            return is_equal(self.get_internal_indicator(field), value)
        if not jdnp3.control.wait_for(func, timeout=WAIT_TIMEOUT):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))

    def set_internal_indicator(self, field, value):
        field = self.camel_case(field)
        self.set({'type': 'internalIndicator', 'attribute': field, 'value': value})

    def get_analog_input(self, index, *args):
        data = self.get()['analogInputPoints'][int(index)]
        return self.get_attribute(data, *args);

    def wait_for_analog_input(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return is_equal(self.get_analog_input(int(index), *args), value)
        if not jdnp3.control.wait_for(func, timeout=WAIT_TIMEOUT):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))

    def set_analog_input(self, index, *args):
        data = self.get()['analogInputPoints'][int(index)]
        self.set_attribute(data, *args)
        self.set(data)

    def get_analog_output(self, index, *args):
        data = self.get()['analogOutputPoints'][int(index)]
        return self.get_attribute(data, *args);

    def wait_for_analog_output(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return is_equal(self.get_analog_output(int(index), *args), value)
        if not jdnp3.control.wait_for(func, timeout=WAIT_TIMEOUT):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))

    def set_analog_output(self, index, *args):
        data = self.get()['analogOutputPoints'][int(index)]
        self.set_attribute(data, *args)
        self.set(data)

    def get_binary_input(self, index, *args):
        data = self.get()['binaryInputPoints'][int(index)]
        return self.get_attribute(data, *args);
    
    def wait_for_binary_input(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return is_equal(self.get_binary_input(int(index), *args), value)
        if not jdnp3.control.wait_for(func, timeout=WAIT_TIMEOUT):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_binary_input(self, index, *args):
        data = self.get()['binaryInputPoints'][int(index)]
        self.set_attribute(data, *args)
        self.set(data)

    def get_double_bit_binary_input(self, index, *args):
        data = self.get()['doubleBitBinaryInputPoints'][int(index)]
        return self.get_attribute(data, *args);
    
    def wait_for_double_bit_binary_input(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return is_equal(self.get_double_bit_binary_input(int(index), *args), value)
        if not jdnp3.control.wait_for(func, timeout=WAIT_TIMEOUT):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_double_bit_binary_input(self, index, *args):
        data = self.get()['doubleBitBinaryInputPoints'][int(index)]
        self.set_attribute(data, *args)
        self.set(data)

    def get_binary_output(self, index, *args):
        data = self.get()['doubleBitBinaryOutputPoints'][int(index)]
        return self.get_attribute(data, *args);
    
    def wait_for_binary_output(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return is_equal(self.get_binary_output(int(index), *args), value)
        if not jdnp3.control.wait_for(func, timeout=WAIT_TIMEOUT):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_binary_output(self, index, *args):
        data = self.get()['binaryOutputPoints'][int(index)]
        self.set_attribute(data, *args)
        self.set(data)

    def get_counter(self, index, *args):
        data = self.get()['counterPoints'][int(index)]
        return self.get_attribute(data, *args);
    
    def wait_for_counter(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return is_equal(self.get_counter(int(index), *args), value)
        if not jdnp3.control.wait_for(func, timeout=WAIT_TIMEOUT):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_counter(self, index, *args):
        data = self.get()['counterPoints'][int(index)]
        self.set_attribute(data, *args)
        self.set(data)

    def trigger_event(self, index, event_type, *args):
        event = self.camel_case(event_type);
        message = {'site': self.site, 'device': self.device, 'type': event, 'index': index}
        if (len(args) > 0):
            message['timestamp'] = args[0]
        self.control.sendMessage(self.site, self.device, message)
        
    def get(self):
        return self.control.getOutstation(self.site, self.device)
        
    def set(self, data):
        data['site'] = self.site
        data['device'] = self.device
        self.control.sendMessage(self.site, self.device, data)
        
    def output(self):
        self.control.getOutstation(self.site, self.device, output=True)

    def get_attribute(self, data, *args):
        if (len(args) < 1):
            print("Must specify at least one attribute.")
            
        for i in range(0, len(args)):
            arg = self.camel_case(args[i])
            data = data[arg]
        
        return data

    def set_attribute(self, data, *args):
        if (len(args) < 2):
            print("Must specify at least one attribute and exactly one value.")
            
        value = args[len(args) - 1]
            
        for i in range(0, len(args) - 2):
            arg = self.camel_case(args[i])
            data = data[arg]
        
        arg = self.camel_case(args[len(args) - 2])
        data[arg] = value
        
    def camel_case(self, value):
        return ''.join(x for x in value.title() if x.isalpha())[0].lower() + ''.join(x for x in value.title() if x.isalpha())[1:]

class OutstationManager:
    def __init__(self, url):
        self.url = url

def create_method(method_name):
    def new_method(self, site, device, *args):
        outstation = Outstation(self.url, site, device)
        getattr(outstation, method_name)(*args)
    return new_method

outstation_methods = inspect.getmembers(Outstation, predicate=inspect.isfunction)
for outstation_method in outstation_methods:
    if (outstation_method[0] in [i[0] for i in inspect.getmembers(OutstationManager, predicate=inspect.isfunction)]):
        continue
    
    new_method = create_method(outstation_method[0])
    new_method.__name__ = outstation_method[0]
    setattr(OutstationManager, new_method.__name__, new_method)

def is_equal(primary, other):
    if (type(primary).__name__ == 'bool' and (type(other).__name__ == 'str' or type(other).__name__ == 'unicode')):
        other = str(other) == 'True'
    elif (type(primary).__name__ == 'float'):
        other = float(other)
        if (primary != primary and other != other):
            return True
        if (primary == other):
            return True
        return abs(primary - other) <= FLOATING_POINT_TOLERANCE * max(abs(primary), abs(other))

    try:
        return primary == type(primary)(other)
    except:
        return primary == other
