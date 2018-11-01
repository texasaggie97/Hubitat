/**
 *  Average Virtual Illuminance Device
 *
 *  Copyright 2018 Andrew Parker
 *
 *  This driver was originally born from an idea by @bobgodbold and I thank him for that!
 *  @bobgodbold ported some of the original code from SmartThings before I adapted it to it's present form
 *  
 *  This driver is specifically designed to be used with 'Weewx' and your own PWS
 *  It also has the capability to collect forecast data from an external source (once you have an api key)
 *
 *  
 *  This driver is free!
 *
 *  Donations to support development efforts are welcomed via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this driver without a donation, but if you find it useful
 *  then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this driver better?  - Please let me know :)
 *
 *  
 *
 *-------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  If modifying this project, please keep the above header intact and add your comments/credits below - Thank you! -  @Cobra
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  Last Update 25/08/2018
 *
 *
 *  V1.2.0 - Added 'Units' to events & Cleaned out old ST code
 *  V1.1.0 - Urgent Update - Stripped out variable logging as it was causing the hub problems
 *  V1.0.0 - POC
 */



metadata {
	definition (name: "Average Virtual Illuminance Device", namespace: "Cobra", author: "Cobra") {
		capability "Illuminance Measurement"
		capability "Sensor"
        command "setLux", ["decimal"]
        attribute "DriverAuthor", "string"
        attribute "DriverVersion", "string"
        attribute "DriverStatus", "string"
        attribute "DriverUpdate", "string" 
	}
    


}


def setLux(val) {
    log.debug "Setting illuminance for ${device.displayName} from external input, illuminance = ${val}."
	sendEvent(name: "illuminance", value: val, unit: "lux")
 //   log.warn "set lux unit = $unit" 
}




def updated() {version()}

def version(){
    unschedule()
    schedule("0 0 8 ? * FRI *", updateCheck)  
    updateCheck()
}

def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/cobra.json" ]  
       	try {
        httpGet(paramsUD) { respUD ->
 //  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code **********************
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Driver.(state.InternalName))
            def newVer = (respUD.data.versions.Driver.(state.InternalName).replace(".", ""))
       		def currentVer = state.Version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Driver.(state.InternalName))
            state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.Status = "<b>** This driver is no longer supported by $state.author  **</b>"       
            log.warn "** This driver is no longer supported by $state.author **"      
      		}           
		else if(currentVer < newVer){
        	state.Status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this driver available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
       		} 
		else{ 
      		state.Status = "Current"
      		log.info "You are using the current version of this driver"
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
   		if(state.Status == "Current"){
			state.UpdateInfo = "N/A"
		    sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	 	    sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
			}
    	else{
	    	sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	     	sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
	    }   
 			sendEvent(name: "DriverAuthor", value: state.author, isStateChange: true)
    		sendEvent(name: "DriverVersion", value: state.Version, isStateChange: true)
    
    
    	//	
}

def setVersion(){
		state.Version = "1.2.0"	
		state.InternalName = "AverageIllum"   
}




















