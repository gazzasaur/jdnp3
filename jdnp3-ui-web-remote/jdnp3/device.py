import jdnp3.control

class Outstation:
    def __init__(self, url, site, device):
        self.site = site
        self.device = device
        self.control = jdnp3.control.Control(url)

    def set_internal_indicator(self, field, value):
        field = self.camel_case(field)
        self.set({'type': 'internalIndicator', 'attribute': field, 'value': value})

    def set_analog_input_attribute(self, index, *args):
        data = self.get()['analogInputPoints'][index]
        self.set_attribute(data, *args)
        self.set(data)
        
    def set_binary_input_attribute(self, index, *args):
        data = self.get()['binaryInputPoints'][index]
        self.set_attribute(data, *args)
        self.set(data)
        
    def set_binary_output_attribute(self, index, *args):
        data = self.get()['binaryOutputPoints'][index]
        self.set_attribute(data, *args)
        self.set(data)
        
    def get(self):
        return self.control.getOutstation(self.site, self.device)
        
    def set(self, data):
        self.control.sendMessage(self.site, self.device, data)
        
    def output(self):
        self.control.getOutstation(self.site, self.device, output=True)

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