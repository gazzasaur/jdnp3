import jdnp3.control

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
            return self.get_internal_indicator(field) == value
        if not jdnp3.control.wait_for(func):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))

    def set_internal_indicator(self, field, value):
        field = self.camel_case(field)
        self.set({'type': 'internalIndicator', 'attribute': field, 'value': value})

    def get_analog_input(self, index, *args):
        data = self.get()['analogInputPoints'][index]
        return self.get_attribute(data, *args);

    def wait_for_analog_input(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return self.get_analog_input(index, *args) == value
        if not jdnp3.control.wait_for(func):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))

    def set_analog_input(self, index, *args):
        data = self.get()['analogInputPoints'][index]
        self.set_attribute(data, *args)
        self.set(data)

    def get_analog_output(self, index, *args):
        data = self.get()['analogOutputPoints'][index]
        return self.get_attribute(data, *args);

    def wait_for_analog_output(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return self.get_analog_output(index, *args) == value
        if not jdnp3.control.wait_for(func):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))

    def set_analog_output(self, index, *args):
        data = self.get()['analogOutputPoints'][index]
        self.set_attribute(data, *args)
        self.set(data)

    def get_binary_input(self, index, *args):
        data = self.get()['binaryInputPoints'][index]
        return self.get_attribute(data, *args);
    
    def wait_for_binary_input(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return self.get_binary_input(index, *args) == value
        if not jdnp3.control.wait_for(func):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_binary_input(self, index, *args):
        data = self.get()['binaryInputPoints'][index]
        self.set_attribute(data, *args)
        self.set(data)

    def get_binary_output(self, index, *args):
        data = self.get()['binaryOutputPoints'][index]
        return self.get_attribute(data, *args);
    
    def wait_for_binary_output(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return self.get_binary_output(index, *args) == value
        if not jdnp3.control.wait_for(func):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_binary_output(self, index, *args):
        data = self.get()['binaryOutputPoints'][index]
        self.set_attribute(data, *args)
        self.set(data)

    def get_counter(self, index, *args):
        data = self.get()['counterPoints'][index]
        return self.get_attribute(data, *args);
    
    def wait_for_counter(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return self.get_counter(index, *args) == value
        if not jdnp3.control.wait_for(func):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_counter(self, index, *args):
        data = self.get()['counterPoints'][index]
        self.set_attribute(data, *args)
        self.set(data)
        
    def get(self):
        return self.control.getOutstation(self.site, self.device)
        
    def set(self, data):
        self.control.sendMessage(self.site, self.device, data)
        
    def output(self):
        self.control.getOutstation(self.site, self.device, output=True)

    def start_data_link(self, dataLinkName):
        data = {'type': 'startDataLink', 'dataLink': dataLinkName}
        self.control.sendMessage(self.site, self.device, data)

    def stop_data_link(self, dataLinkName):
        data = {'type': 'stopDataLink', 'dataLink': dataLinkName}
        self.control.sendMessage(self.site, self.device, data)

    def get_attribute(self, data, *args):
        if (len(args) < 1):
            print "Must specify at least one attribute."
            
        for i in range(0, len(args)):
            arg = self.camel_case(args[i])
            data = data[arg]
        
        return data

    def set_attribute(self, data, *args):
        if (len(args) < 2):
            print "Must specify at least one attribute and exactly one value."
            
        value = args[len(args) - 1]
            
        for i in range(0, len(args) - 2):
            arg = self.camel_case(args[i])
            data = data[arg]
        
        arg = self.camel_case(args[len(args) - 2])
        data[arg] = value
        
    def camel_case(self, value):
        return ''.join(x for x in value.title() if x.isalpha())[0].lower() + ''.join(x for x in value.title() if x.isalpha())[1:]