"""
config of ops service, Control by OPS devices
@author:  opsdev
"""
import httplib
import json

from OpsServiceConfig import OPS_SERVICE_CONFIG

class OpsDeviceManager():

    def __init__(self,config):
        self.server = config["server"]
        self.port = config["port"]
        self.addAction = "POST"
        self.getAction = "GET"
        self.modifyAction = "PUT"
        self.deleteAction = "DELETE" 
    
    def addDevice(self,data):
        return self.restcall("/devices", data, self.addAction)
    
    def delDevice(self,deviceid):
        path = "/devices/%s" % (deviceid)
        return self.restcall(path, {}, self.deleteAction)
    
    def modifyDevice(self,data):
        return self.restcall("/devices", data, self.modifyAction)
    
    def getDevice(self,deviceid):
        path = "/devices/%s" % (deviceid)
        return self.restcall(path, "", self.getAction)
        
    def getDevices(self):
        return self.restcall("/devices", "", self.getAction)
    
    def restcall(self, opspath, data, action):
        conn = httplib.HTTPConnection(self.server, self.port)
        body = json.dumps(data)
        if action == self.addAction:
            conn.request(action, opspath, body)
        elif action == self.getAction:
            conn.request(action, opspath)
        elif action == self.modifyAction:
            conn.request(action, opspath, body)
        elif action == self.deleteAction:
            conn.request(action, opspath, body)    
        response = conn.getresponse()
        ret = (response.status, response.reason, response.read())
        conn.close()
        return ret
    
        
       
    