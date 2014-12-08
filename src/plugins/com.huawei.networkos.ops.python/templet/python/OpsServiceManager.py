from OpsRestCaller import RestCaller
from OpsServiceConfig import OPS_SERVICE_CONFIG

class OpsServiceManager:
    def _ini_(self):
        self = self
    
    def getDefaultOpsConfig(self):
        self.defaultconfig = OPS_SERVICE_CONFIG.get("default")
        return self.defaultconfig
    
    def getDefaultOpsRestCall(self):
        return RestCaller(self.getDefaultOpsConfig())
    
    def getOpsRestCallByName(self, name):
        config = OPS_SERVICE_CONFIG.get(name)
        return RestCaller(config)
            
OpsServiceManagerInstance = OpsServiceManager()