import httplib
import json
import base64

class RestCaller():
    
    def __init__(self,config):
        self.server = config["server"]
        self.port = config["port"]
        self.protocal = config["protocal"]
        self.username = config["username"]
        self.devicename = config["devicename"]
        self.passwd = config["passwd"]
        self.device_id = config["id"]
        self.version = config["version"]
        self.productType = config["productType"]
        self.getAction = "GET"
        self.putAction = "PUT"
        self.postAction = "POST"
        self.deleteAction = "DELETE"
    
    def get(self,path):
        return self.restcall(path,"",self.getAction)
    
    def post(self,path,data):
        return self.restcall(path,data,self.postAction)
    
    def put(self,path,data):

        return self.restcall(path,data,self.putAction)
    
    def delete(self,path):
        return self.restcall(path,"",self.deleteAction)
        
    def touni(self,s, enc='utf8', err='strict'):
        return s.decode(enc, err) if isinstance(s, bytes) else unicode(s)
    
    def restcall(self, path, data, action):
        urlpath = "/devices/%s%s" % ( self.device_id, path)
        tmpserverip = self.server
        tmpserverport = self.port
        tmpprotocal = self.protocal
        header = {}

        if "https" == tmpprotocal:
            conn = httplib.HTTPSConnection(tmpserverip, tmpserverport)
        else:
            conn = httplib.HTTPConnection(tmpserverip, tmpserverport)
            
        header = None
        if None != self.username and "" != self.username:
            kskafskdfal = self.username + ":" + self.passwd
            HTTP_AUTHORIZATION = "Basic " + self.touni(base64.b64encode(kskafskdfal))
            header = {'Authorization': HTTP_AUTHORIZATION }

        body = json.dumps(data)
        if action == self.getAction:
            conn.request(action, urlpath, None, header)
        elif action == self.postAction:
            conn.request(action, urlpath, body, header)
        elif action == self.putAction:
            conn.request(action, urlpath, body, header)
        elif action == self.deleteAction:
            conn.request(action, urlpath, body, header)    
        response = conn.getresponse()
        ret = (response.status, response.reason, response.read())
        conn.close()
        return ret