/**
 *  ****************  Average All (Illumination, Temperature, Humidity, Pressure)  ****************
 *
 *  Design Usage:
 *  This was designed to display/set an 'average' or mean illumination from a group of illumination devices
 *
 *  This app was originally born from an idea by @bobgodbold and I thank him for that!
 *  @bobgodbold ported some of the original driver code from SmartThings before I adapted it to it's present form for use with this app
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This SmartApp is free!
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this app without a donation, but if you find it useful then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this app better?  - Please let me know :)
 *
 *  Website: http://hubitat.uk
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
 *  Last Update: 17/09/2018
 *
 *  Changes:
 *
 *
 *  V1.3.0 - Addes separate 'last device' recording
 *  V1.2.0 - Added 'Ambient Pressure' average (for use with weather devices)
 *  V1.1.0 - Debug and code cleanup/consolidation
 *  V1.0.2 - Debug fixed issue with delay timer
 *  V1.0.1 - Debug calculations (reset variable was incorrect)
 *  V1.0.0 - POC
 *
 */



definition(
    name: "Average All Child",
    namespace: "Cobra",
    author: "AJ Parker",
    description: "This was designed to display/set an 'average' or mean illumination/Humidity/temparature or ambient pressure from a group of devices",
    category: "My Apps",
    
parent: "Cobra:Average All",
    
    iconUrl: "",
    iconX2Url: ""
)
preferences {

section("") {
        page name: "mainPage", title: "", install: true, uninstall: true
		}
   
    
}    
    

 def mainPage() {
    dynamicPage(name: "mainPage") {  
        
       display()
        
 section("") {
   input "childTypeSelect", "enum", required: true, title: "What do You Want To Average", submitOnChange: true,  options: ["Illuminance", "Temperature", "Humidity", "Ambient Pressure"] 
                                                                                                                          
 }  
        if(childTypeSelect){
        state.selection = childTypeSelect
 section("") {
        input "vDevice", "capability.sensor", title: "Virtual Device"
     	if(state.selection == "Temperature"){ input "tempSensors", "capability.temperatureMeasurement", title: "Physical Temperature Sensors", multiple: true}
     	if(state.selection == "Illuminance"){ input "illumSensors", "capability.illuminanceMeasurement", title: "Physical Illuminance Sensors", multiple: true}
    	if(state.selection == "Humidity"){input "humiditySensors", "capability.relativeHumidityMeasurement", title: "Physical Humidity Sensors", multiple: true} 
     	if(state.selection == "Ambient Pressure"){ input "pressureSensors", "capability.sensor", title: "Weather Pressure Sensors", multiple: true}
        input "sendInterval", "number", title: "How Often To Update Virtual Device (Minutes - Set to '0' for instant)", required: true, defaultValue: "0"
        input "decimalUnit", "enum", title: "Max Decimal Places", required:true, defaultValue: "2", options: ["1", "2", "3", "4", "5"]
        label title: "Enter a name for child app (optional)", required: false 
        input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false    
    }                  
        }                     
    }                   

}    
        

 


def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    initialize()
}

def initialize() {
 
  state.sendOK = true     
version()
logCheck()
    state.DecimalPlaces = decimalUnit.toInteger() 
    
    if(state.selection == "Illuminance"){
        subscribe(illumSensors, "illuminance", illuminanceHandler)
       
    }
    
    if(state.selection == "Temperature"){
       subscribe(tempSensors, "temperature", tempSensorsHandler)
      
    }
     if(state.selection == "Humidity"){
       subscribe(humiditySensors, "humidity", humidityHandler)
     }
    
    if(state.selection == "Ambient Pressure"){
        subscribe(pressureSensors, "pressure", pressureSensorsHandler)
    }

}




def illuminanceHandler(evt) {
LOGDEBUG("Running illuminance handler")
    
     if(state.sendOK == true){
  def ave = evt.value
   def  aveDev = evt.device
LOGDEBUG( "Received from: $aveDev - $ave")
    def sum = 0
    def count = 0
    state.mean = 0
    state.mean1 = 0
    state.mean2 = 0

    for (sensor in settings.illumSensors) {
    count += 1 
LOGDEBUG( "Sensor data count = $count" )      
    sum += sensor.currentIlluminance }
LOGDEBUG( "Total Combined value =  $sum")

    state.mean1 = sum/count
   state.mean2 = state.mean1.toFloat()
    state.mean = state.mean2.round(state.DecimalPlaces)
   
LOGDEBUG("Average Illuminance = $state.mean")

   
        def timeCheck = 60 * sendInterval  
LOGDEBUG("Sending $state.mean to $vDevice then waiting $timeCheck seconds before I can send again")
     settings.vDevice.setLux("${state.mean}")
     settings.vDevice.lastDeviceLux("${aveDev}")  
        
        state.sendOK = false
       runIn(timeCheck, resetNow)  // , [overwrite: false])
     }
    else {
LOGDEBUG("Waiting for timer to expire")  
    }
}                           




def tempSensorsHandler(evt) {
LOGDEBUG("Running temperature handler")
    if(state.sendOK == true){
      def ave1 = evt.value
    def aveDev1 = evt.device
LOGDEBUG( "Received from: $aveDev1 - $ave1")
    def sum = 0
    def count = 0
    state.mean = 0
	state.mean1 = 0
    state.mean2 = 0
    
    for (sensor in settings.tempSensors) {
    count += 1 
LOGDEBUG( "Sensor data count = $count" )
    sum += sensor.currentTemperature }
LOGDEBUG( "Total Combined value =  $sum")
	state.mean1 = sum/count
   state.mean2 = state.mean1.toFloat()
    state.mean = state.mean2.round(state.DecimalPlaces)
    LOGDEBUG("Average Temperature = $state.mean")

 
        def timeCheck = 60 * sendInterval  
        LOGDEBUG("Sending $state.mean to $vDevice then waiting $timeCheck seconds before I can send again")
    settings.vDevice.setTemperature("${state.mean}")
    settings.vDevice.lastDeviceTemperature("${aveDev1}") 
 		state.sendOK = false
        runIn(timeCheck, resetNow)  // , [overwrite: false])
 }
    else {
LOGDEBUG("Waiting for timer to expire")  
    }
}




def humidityHandler(evt) {
LOGDEBUG("Running humidity handler")
     if(state.sendOK == true){
      def ave3 = evt.value
      def aveDev3 = evt.device
LOGDEBUG( "Received from: $aveDev - $ave")
    def sum = 0
    def count = 0
    state.mean = 0
	state.mean1 = 0
    state.mean2 = 0
    
    for (sensor in settings.humiditySensors) {
    count += 1 
LOGDEBUG( "Sensor data count = $count" )      
    sum += sensor.currentHumidity }
LOGDEBUG( "Total Combined value =  $sum")

    state.mean1 = sum/count
    state.mean2 = state.mean1.toFloat()
    state.mean = state.mean2.round(state.DecimalPlaces)
LOGDEBUG("Average Humidity = $state.mean")

   
        def timeCheck = 60 * sendInterval  
LOGDEBUG("Sending $state.mean to $vDevice then waiting $timeCheck seconds before I can send again")
     settings.vDevice.setHumidity("${state.mean}")
     settings.vDevice.lastDeviceHumidity("${aveDev3}") 
        state.sendOK = false
       runIn(timeCheck, resetNow) // , [overwrite: false])
     }
    else {
LOGDEBUG("Waiting for timer to expire")  
    }
}                           


def pressureSensorsHandler(evt) {
LOGDEBUG("Running pressure handler")
    if(state.sendOK == true){
       def ave4 = evt.value.toFloat()
   	   def aveDev4 = evt.device
LOGDEBUG( "Received from: $aveDev - $ave")
    def sum = 0
    def count = 0
    state.mean = 0
	state.mean1 = 0
    state.mean2 = 0
    
    for (sensor in settings.pressureSensors) {
    count += 1 
LOGDEBUG( "Sensor data count = $count" )
    sum1 += sensor.currentValue("pressure") }
LOGDEBUG( "Sum1 =  $sum1")   
    sum2 = sum1.minus('null')
LOGDEBUG( "Sum2 =  $sum2")        
    sum = sum2.toDouble()
LOGDEBUG( "Total Combined value =  $sum")        
	state.mean1 = sum/count
   state.mean2 = state.mean1.toDouble()
    state.mean = state.mean2.round(state.DecimalPlaces)
    LOGDEBUG("Average Pressure = $state.mean")

 
        def timeCheck = 60 * sendInterval  
        LOGDEBUG("Sending $state.mean to $vDevice then waiting $timeCheck seconds before I can send again")
    settings.vDevice.setPressure("${state.mean}")
    settings.vDevice.lastDevicePressure("${aveDev4}") 
 		state.sendOK = false
        runIn(timeCheck, resetNow)  // , [overwrite: false])
 }
    else {
LOGDEBUG("Waiting for timer to expire")  
    }
}

def resetNow(){
 LOGDEBUG("Timer reset")    
    state.sendOK = true
 }




// ******************************************************************************
// define debug action
def logCheck(){
state.checkLog = debugMode
if(state.checkLog == true){
log.info "All Logging Enabled"
}
else if(state.checkLog == false){
log.info "Further Logging Disabled"
}

}
def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}


def version(){
	unschedule()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
}

def display(){
	if(state.status){
	section{paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}
	if(state.status != "Current"){
	section{ 
	paragraph "$state.status"
	paragraph "$state.UpdateInfo"
    }
    }
}
}


def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       	try {
        httpGet(paramsUD) { respUD ->
 //  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.status = "<b>** This app is no longer supported by $state.author  **</b>"       
            log.warn "** This app is no longer supported by $state.author **"      
      		}           
		else if(currentVer < newVer){
        	state.status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
       		} 
		else{ 
      		state.status = "Current"
      		log.info "You are using the current version of this app"
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
 	
}

def setVersion(){
		state.version = "1.3.0"	 
		state.InternalName = "AverageAllchild"
}





