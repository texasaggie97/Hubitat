/**
 *  Average Virtual Illuminance/Temperature/Humidity/Pressure/Motion Device
 *
 *  Copyright 2018 Andrew Parker
 *
 *  
 *  
 *  
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
 *  Last Update 11/02/2019
 *
 *
 *   
 *  V1.5.0 - Added 'LastEventTime' & 'LastEventDate' and date format option to show when a device reports
 *  V1.4.1 - Debug issue with driver not working correctly after reboot
 *  V1.4.0 - New update json
 *  V1.3.3 - Debug - Typo in lastDeviceHumidity
 *  V1.3.2 - Debug UI
 *  V1.3.1 - Debug 'LastDevice'
 *  V1.3.0 - Added switchable logging
 *  V1.2.0 - Added 'Motion' average
 *  V1.1.0 - Debug and added 'last device' separation - one for each attribute
 *  V1.0.0 - POC
 */



metadata {
	definition (name: "Average All Device", namespace: "Cobra", author: "Cobra") {
		capability "Illuminance Measurement"
		capability "Relative Humidity Measurement"
        capability "Temperature Measurement"
        capability "Motion Sensor"
		capability "Sensor"
        
        command "setTemperature", ["decimal"]
        command "setHumidity", ["decimal"]
        command "setLux", ["decimal"]
        command "setPressure", ["decimal"]
        
        command "lastDeviceLux"
        command "lastDeviceTemperature"
        command "lastDeviceHumidity"
        command "lastDevicePressure"
        command "lastDeviceMotion"
        command "setMotion", ["string"]
		
//      command "active"
//		command "inactive"
        
        attribute " ", "string"
        attribute "LastDeviceLux", "string"
        attribute "LastDeviceTemperature", "string"
        attribute "LastDevicePressure", "string"
        attribute "LastDeviceHumidity", "string"
        attribute "LastDeviceMotion", "string"
		attribute "LastEventDate", "string"
		attribute "LastEventTime", "string"
        
//        attribute "DriverAuthor", "string"
        attribute "DriverVersion", "string"
        attribute "DriverStatus", "string"
        attribute "DriverUpdate", "string" 
        attribute "pressure", "string"
	}
    
    
  preferences() {
    
     section("") {
   		input "unitSelect", "enum",  title: "Temperature Units (If using temperature)", required: true, options: ["C", "F"] 
   		input "pressureUnit", "enum", title: "Pressure Unit (If using pressure)", required:true, options: ["inhg", "mbar"]
        input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false 
		input "dateFormatNow", "enum", title: "Last Event Date Format", required:true, options: ["dd MMM yyyy", "MMM dd yyyy"]
 		}  
 }   
}

def installed(){
    initialize()
}

def updated(){
    initialize()
}



def initialize() {
    logCheck()
    version()
    if(state.TemperatureUnit == null){ state.TemperatureUnit = "F"}
    else{state.TemperatureUnit = unitSelect}
    if(pressureUnit == null){state.PressureUnit = "inhg"}
    else{state.PressureUnit = pressureUnit}
    
    
}

def lastDeviceLux(dev1){  
    state.LastDeviceLux = dev1
	sendLastEvent()
	sendEvent(name: "LastDeviceLux", value: state.LastDeviceLux)
	
}
def lastDeviceHumidity(dev2){  
	state.LastDeviceHumid = dev2
	sendLastEvent()
    sendEvent(name: "LastDeviceHumidity", value: state.LastDeviceHumid)
}
def lastDevicePressure(dev3){ 
	state.LastDevicePressure = dev3
	sendLastEvent()
    sendEvent(name: "LastDevicePressure", value: state.LastDevicePressure)
}
def lastDeviceTemperature(dev4){ 
	state.LastDeviceTemperature = dev4
	sendLastEvent()
    sendEvent(name: "LastDeviceTemperature", value: state.LastDeviceTemperature)
}

def lastDeviceMotion(dev5){ 
	state.LastDeviceMotion = dev5
	sendLastEvent()
    sendEvent(name: "LastDeviceMotion", value: state.LastDeviceMotion)
}


def sendLastEvent(){
	if(dateFormatNow == null){log.warn "Date format not set"}
	if(dateFormatNow == "dd MMM yyyy"){
	def date = new Date()
	state.LastTime = date.format('HH:mm:ss', location.timeZone)
	state.LastDate = date.format('dd MMM yyyy', location.timeZone)	
	}
	
	if(dateFormatNow == "MMM dd yyyy"){
	def date = new Date()
	state.LastTime = date.format('HH:mm:ss', location.timeZone)
	state.LastDate = date.format('MMM dd yyyy', location.timeZone)	
	}	
	
	sendEvent(name: "LastEventTime", value: state.LastTime)
	sendEvent(name: "LastEventDate", value: state.LastDate)

}

def active(motion1) {
//	state.ReceivedMotion = motion1
    LOGDEBUG( "Setting motion for ${device.displayName} from external input ($state.LastDeviceMotion), Motion = ${motion1}.")
	sendEvent(name: "motion", value: 'active')
}

def inactive(motion1) {
//	state.ReceivedMotion = motion1
    LOGDEBUG( "Setting motion for ${device.displayName} from external input ($state.LastDeviceMotion), Motion = ${motion1}.")
	sendEvent(name: "motion", value: 'inactive')
}

def setMotion(motion1){
 state.ReceivedMotion = motion1
    LOGDEBUG( "Setting motion for ${device.displayName} from external input ($state.LastDeviceMotion), Motion = ${state.ReceivedMotion}.")
   sendEvent(name: "motion", value: state.ReceivedMotion) 
}

def setHumidity(hum1) {
	state.ReceivedHumidity = hum1
    LOGDEBUG( "Setting humidity for ${device.displayName} from external input ($state.LastDeviceHumid), Humidity = ${state.ReceivedHumidity}.")
	sendEvent(name: "humidity", value: state.ReceivedHumidity, unit: "%")
}


def setLux(ilum1) {
    state.ReceivedIlluminance = ilum1
    LOGDEBUG("Setting illuminance for ${device.displayName} from external input ($state.LastDeviceLux), Illuminance = ${state.ReceivedIlluminance}.")
	sendEvent(name: "illuminance", value: state.ReceivedIlluminance, unit: "lux", isStateChange: true)
}


def setTemperature(temp1){ 
 state.ReceivedTemp = temp1
	LOGDEBUG( "Setting temperature for ${device.displayName} from external input ($state.LastDeviceTemperature), Temperature = ${state.ReceivedTemp}.")
    sendEvent(name:"temperature", value: state.ReceivedTemp , unit: state.TemperatureUnit, isStateChange: true)
   
}

def setPressure(pres1){ 
 state.ReceivedPressure = pres1
	LOGDEBUG("Setting pressure for ${device.displayName} from external input ($state.LastDevicePressure), Pressure = ${state.ReceivedPressure}.")
    sendEvent(name:"pressure", value: state.ReceivedPressure , unit: state.PressureUnit, isStateChange: true)
    
}
def logCheck(){
state.checkLog = debugMode
if(state.checkLog == true){
log.info "All Logging Enabled"
}
else if(state.checkLog == false){
log.info "Debug Logging Disabled"
}

}
def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("Device Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}


def version(){
    updateCheck()
   schedule("0 0 9 ? * FRI *", updateCheck)
}
    

def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"] 
       	try {
        httpGet(paramsUD) { respUD ->
//  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code **********************
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Driver.(state.InternalName))
	//		log.warn "$state.InternalName = $newVerRaw"
  			def newVer = newVerRaw.replace(".", "")
//			log.warn "$state.InternalName = $newVer"
			state.newUpdateDate = (respUD.data.Comment)
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = "Updated: "+state.newUpdateDate + " - "+(respUD.data.versions.UpdateInfo.Driver.(state.InternalName))
            state.author = (respUD.data.author)
			state.icon = (respUD.data.icon)
           
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
 			sendEvent(name: " ", value: state.icon +"<br>" +state.Copyright, isStateChange: true)
    		sendEvent(name: "DriverVersion", value: state.version, isStateChange: true)
    
    
    	//	
}

def setVersion(){
    state.version = "1.5.0"
    state.InternalName = "AverageAllDriver"
   	state.CobraAppCheck = "averagealldriver.json"
    sendEvent(name: "DriverAuthor", value: "Cobra", isStateChange: true)
    sendEvent(name: "DriverVersion", value: state.version, isStateChange: true)
    
}



















