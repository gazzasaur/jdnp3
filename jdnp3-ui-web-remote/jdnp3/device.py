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
        data = self.get()['analogInputPoints'][int(index)]
        return self.get_attribute(data, *args);

    def wait_for_analog_input(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return self.get_analog_input(int(index), *args) == value
        if not jdnp3.control.wait_for(func):
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
            return self.get_analog_output(int(index), *args) == value
        if not jdnp3.control.wait_for(func):
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
            return self.get_binary_input(int(index), *args) == value
        if not jdnp3.control.wait_for(func):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_binary_input(self, index, *args):
        data = self.get()['binaryInputPoints'][int(index)]
        self.set_attribute(data, *args)
        self.set(data)

    def get_binary_output(self, index, *args):
        data = self.get()['binaryOutputPoints'][int(index)]
        return self.get_attribute(data, *args);
    
    def wait_for_binary_output(self, index, *args):
        value = args[-1]
        args_list = list(args)
        del args_list[-1]
        args = tuple(args_list)
        
        def func():
            return self.get_binary_output(int(index), *args) == value
        if not jdnp3.control.wait_for(func):
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
            return self.get_counter(int(index), *args) == value
        if not jdnp3.control.wait_for(func):
            raise RuntimeError('Timeout while waiting for value %s.' % (value))
        
    def set_counter(self, index, *args):
        data = self.get()['counterPoints'][int(index)]
        self.set_attribute(data, *args)
        self.set(data)
        
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