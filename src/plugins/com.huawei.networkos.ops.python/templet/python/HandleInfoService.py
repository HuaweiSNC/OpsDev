"""
Generate service call, Control by OPS devices
@author:  opsdev
"""
from OpsRestCaller import RestCaller
from OpsServiceConfig import OPS_SERVICE_CONFIG

class HandleInfoService:
    def __ifmdefaultHandle__(self,):
        url = "/ifm/interfaces/interface/json"
        restcaller = RestCaller(OPS_SERVICE_CONFIG.get("default"))
        return restcaller.get(url)

    def __default_defaultHandle__(self,):
        url = "/ifm/interfaces/interface/json"
        restcaller = RestCaller(OPS_SERVICE_CONFIG.get("default"))
        return restcaller.get(url)