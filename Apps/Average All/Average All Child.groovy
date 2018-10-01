/**
 *  ****************  Average All (Illumination, Temperature, Humidity, Pressure & Motion)  ****************
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
 *  Last Update: 01/10/2018
 *
 *  Changes:
 *
 *  V1.5.2 - Revised auto update checking and added a manual update check button
 *  V1.5.1 - Debug timers (added 5 second delay before reset to prevent 'bounce')
 *  V1.5.0 - Recoded timers (how often To update virtual device)
 *  V1.4.1 - Debug Motion
 *  V1.4.0 - Added 'Motion'as a selectable 'average'
 *  V1.3.0 - Debug & Added separate 'last device' recording
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
    description: "This was designed to display/set an 'average' or mean illumination/Humidity/temparature/ambient pressure & motion from a group of devices",
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
   input "childTypeSelect", "enum", required: true, title: "What do You Want To Average", submitOnChange: true,  options: ["Illuminance", "Temperature", "Humidity", "Ambient Pressure", "Motion"] 
                                                                                                                          
 }  
        if(childTypeSelect){
        state.selection = childTypeSelect
 section("") {
    //    input "vDevice", "device.AverageAllDevice", title: "Virtual Device"
   		 input "vDevice", "capability.sensor", title: "Virtual Device"
     
     	if(state.selection == "Temperature"){ 
            input "tempSensors", "capability.temperatureMeasurement", title: "Physical Temperature Sensors", multiple: true
        	input "sendTempInterval", "number", title: "How Often To Update Virtual Temperature Device (Minutes - Set to '0' for instant)", required: true, defaultValue: "0"
        }
     	if(state.selection == "Illuminance"){
            input "illumSensors", "capability.illuminanceMeasurement", title: "Physical Illuminance Sensors", multiple: true
        	input "sendLuxInterval", "number", title: "How Often To Update Virtual Illuminance Device (Minutes - Set to '0' for instant)", required: true, defaultValue: "0"
        }
    	if(state.selection == "Humidity"){
            input "humiditySensors", "capability.relativeHumidityMeasurement", title: "Physical Humidity Sensors", multiple: true
        	input "sendHumInterval", "number", title: "How Often To Update Virtual Humidity Device (Minutes - Set to '0' for instant)", required: true, defaultValue: "0"
        } 
     	if(state.selection == "Ambient Pressure"){ 
            input "pressureSensors", "capability.sensor", title: "Weather Pressure Sensors", multiple: true
        	input "sendPressInterval", "number", title: "How Often To Update Virtual Pressure Device (Minutes - Set to '0' for instant)", required: true, defaultValue: "0"
        }
        if(state.selection == "Motion"){ 
            input "motionSensors", "capability.motionSensor", title: "Motion Sensors", multiple: true
        	input "delay1", "number", title: "Delay after motion stops to stop virtual motion", description: "Minutes", required: true
        }
     if(state.selection != "Motion"){
     		input "decimalUnit", "enum", title: "Max Decimal Places", required:true, defaultValue: "2", options: ["1", "2", "3", "4", "5"]
     }
     
     
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
    if(vDevice){
   state.devCapability = vDevice.capabilities.inspect()
    if(state.devCapability == "[IlluminanceMeasurement, TemperatureMeasurement, RelativeHumidityMeasurement, MotionSensor, Sensor]") {  
       LOGDEBUG( "You are using the correct Average All Virtual Device")
                state.correctDevice = true
    }
                else{ log.warn "You are not using the correct Average All Virtual Device - This may cause error messages, but will probably still work"
                 state.correctDevice = false     
    }
                }
      
version()
logCheck()
    state.DecimalPlaces = decimalUnit.toInteger() 
    
    if(state.selection == "Illuminance"){
        subscribe(illumSensors, "illuminance", illuminanceHandler)
        state.luxSendOK = true
    }
    
    if(state.selection == "Temperature"){
       subscribe(tempSensors, "temperature", tempSensorsHandler)
       state.tempSendOK = true
    }
     if(state.selection == "Humidity"){
       subscribe(humiditySensors, "humidity", humidityHandler)
       state.humSendOK = true
     }
    
    if(state.selection == "Ambient Pressure"){
        subscribe(pressureSensors, "pressure", pressureSensorsHandler)
        state.pressSendOK = true
    }

    if(state.selection == "Motion"){
        subscribe(motionSensors, "motion", motionSensorsHandler)
    }
    
}






def illuminanceHandler(evt) {
LOGDEBUG("Running illuminance handler")
    
     if(state.luxSendOK == true){
  def ave = evt.value
   def aveDev = evt.device
       
LOGDEBUG( "Received from: $aveDev - $ave")
    def sum = 0
    def count = 0
    state.mean = 0
    state.mean1 = 0
    state.mean2 = 0
LOGDEBUG("sum = $sum")
    for (sensor in settings.illumSensors) {
    count += 1 
LOGDEBUG( "Sensor data count = $count" )      
    sum += sensor.currentIlluminance }
LOGDEBUG( "Total Combined value =  $sum")

    state.mean1 = sum/count
   state.mean2 = state.mean1.toFloat()
    state.mean = state.mean2.round(state.DecimalPlaces)
   
LOGDEBUG("Average Illuminance = $state.mean")

   
        def timeCheck1 = (60 * sendLuxInterval)  
         if(timeCheck1 == 0){ timeCheck1 = 5}
LOGDEBUG("Sending $state.mean to $vDevice then waiting $timeCheck1 seconds before I can send again")
     settings.vDevice.setLux("${state.mean}")
     settings.vDevice.lastDeviceLux("${aveDev}")  
        
        state.luxSendOK = false
//         log.warn "timecheck1 = $timeCheck1"
       runIn(timeCheck1, resetLuxNow)  // , [overwrite: false])
     }
    else {
LOGDEBUG("Waiting for timer to expire")  
    }
}                           




def tempSensorsHandler(evt) {
LOGDEBUG("Running temperature handler")
    if(state.tempSendOK == true){
      def ave1 = evt.value
    def aveDev1 = evt.device
   
LOGDEBUG( "Received from: $aveDev1 - $ave1")
    def sumTemp = 0
    def countTemp = 0
    state.meanTemp = 0
	state.mean1Temp = 0
    state.mean2Temp = 0
    
    for (sensor in settings.tempSensors) {
    countTemp += 1 
LOGDEBUG( "Sensor data count = $countTemp" )
    sumTemp += sensor.currentTemperature }
LOGDEBUG( "Total Combined value =  $sumTemp")
	state.mean1Temp = sumTemp/countTemp
   state.mean2Temp = state.mean1Temp.toFloat()
    state.meanTemp = state.mean2Temp.round(state.DecimalPlaces)
    LOGDEBUG("Average Temperature = $state.meanTemp")

 
        def timeCheck2 = (60 * sendTempInterval)  
        if(timeCheck2 == 0){timeCheck2 = 5}
        LOGDEBUG("Sending $state.meanTemp to $vDevice then waiting $timeCheck2 seconds before I can send again")
    settings.vDevice.setTemperature("${state.meanTemp}")
    settings.vDevice.lastDeviceTemperature("${aveDev1}") 
 		state.tempSendOK = false
        runIn(timeCheck2, resetTempNow)  // , [overwrite: false])
 }
    else {
LOGDEBUG("Waiting for timer to expire")  
    }
}




def humidityHandler(evt) {
LOGDEBUG("Running humidity handler")
     if(state.humSendOK == true){
      def ave3 = evt.value
      def aveDev3 = evt.device
    
LOGDEBUG( "Received from: $aveDev3 - $ave3")
    def sumHum = 0
    def countHum = 0
    state.meanHum = 0
	state.mean1Hum = 0
    state.mean2Hum = 0
    
    for (sensor in settings.humiditySensors) {
    countHum += 1 
LOGDEBUG( "Sensor data count = $countHum" )      
    sumHum += sensor.currentHumidity }
LOGDEBUG( "Total Combined value =  $sumHum")

    state.mean1Hum = sumHum/countHum
    state.mean2Hum = state.mean1Hum.toFloat()
    state.meanHum = state.mean2Hum.round(state.DecimalPlaces)
LOGDEBUG("Average Humidity = $state.meanHum")

   
        def timeCheck3 = 60 * sendHumInterval  
         if(timeCheck3 == 0){timeCheck3 = 5}
LOGDEBUG("Sending $state.mean to $vDevice then waiting $timeCheck3 seconds before I can send again")
     settings.vDevice.setHumidity("${state.meanHum}")
     settings.vDevice.lastDeviceHumidity("${aveDev3}") 
        state.humSendOK = false
       runIn(timeCheck3, resetHumNow) // , [overwrite: false])
     }
    else {
LOGDEBUG("Waiting for timer to expire")  
    }
}                           


def pressureSensorsHandler(evt) {
LOGDEBUG("Running pressure handler")
    if(state.pressSendOK == true){
       def ave4 = evt.value.toFloat()
   	   def aveDev4 = evt.device
   
LOGDEBUG( "Received from: $aveDev4 - $ave4")
    def sumPress = 0
    def countPress = 0
    state.meanPress = 0
	state.mean1Press = 0
    state.mean2Press = 0
    
    for (sensor in settings.pressureSensors) {
    countPress += 1 
LOGDEBUG( "Sensor data count = $countPress" )
    sum1Press += sensor.currentValue("pressure") }
LOGDEBUG( "Sum1 =  $sum1Press")   
    sum2Press = sum1Press.minus('null')
LOGDEBUG( "Sum2 =  $sum2Press")        
    sumPress = sum2Press.toDouble()
LOGDEBUG( "Total Combined value =  $sumPress")        
	state.mean1Press = sumPress/countPress
   state.mean2Press = state.mean1Press.toDouble()
    state.meanPress = state.mean2Press.round(state.DecimalPlaces)
    LOGDEBUG("Average Pressure = $state.meanPress")

 
        def timeCheck4 = (60 * sendPressInterval)  
        if(timeCheck4 == 0){timeCheck4 = 5}
        LOGDEBUG("Sending $state.mean to $vDevice then waiting $timeCheck4 seconds before I can send again")
    settings.vDevice.setPressure("${state.meanPress}")
    settings.vDevice.lastDevicePressure("${aveDev4}") 
 		state.pressSendOK = false
        runIn(timeCheck4, resetPressNow)  // , [overwrite: false])
 }
    else {
LOGDEBUG("Waiting for timer to expire")  
    }
}

def resetLuxNow(){
 LOGDEBUG("Lux Timer reset")    
    state.luxSendOK = true
 }
def resetTempNow(){
 LOGDEBUG("Temperature Timer reset")    
    state.tempSendOK = true
 }
def resetHumNow(){
 LOGDEBUG("Humidity Timer reset")    
    state.humSendOK = true
 }
def resetPressNow(){
 LOGDEBUG("Pressure Timer reset")    
    state.pressSendOK = true
 }




def motionSensorsHandler(evt){
def ave5 = evt.value    
def aveDev5 = evt.device 
LOGDEBUG("Received from: $aveDev5 - $ave5")
	def activeNow = motionSensors.findAll { it?.latestValue("motion") == 'active' }
//   def activeNow = motionSensors.findAll { it?.currentValue("motion") == 'active' } 
		if (activeNow) { 
			state.go = "go"
LOGDEBUG("Active Sensors: ${activeNow.join(', ')}")
LOGDEBUG( "Active Now!")         
			settings.vDevice.setMotion("active")
            settings.vDevice.lastDeviceMotion("${aveDev5}")  
}
   def inActiveNow = motionSensors.findAll { it?.latestValue("motion") == "inactive"}
//    def inActiveNow = motionSensors.findAll { it?.currentValue("motion") == 'inactive' }
    
		if (!activeNow) { 
LOGDEBUG( "Inactive Sensors: ${inActiveNow}")
state.go = 'stop'
def myDelay = 60 * delay1
            if(myDelay == 0){myDelay = 5}
            
       LOGDEBUG(" Waiting $myDelay seconds before going inactive (If no further motion)")
runIn(myDelay, offNOW)
	}

}

def offNOW(){
 if (state.go == 'stop'){
    LOGDEBUG( "Inactive Now!")
	settings.vDevice.setMotion("inactive")
 }
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
    resetBtnName()
	unschedule()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
    checkButtons()
}

def display(){
  
	if(state.status){
	section{paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}
       
        }
    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){
     section(){ input "updateBtn", "button", title: "$state.btnName"}
    }
    
    if(state.status != "Current"){
	section{ 

	paragraph "<b>Update Info: $state.UpdateInfo ***</b>"
    }
         
    }         
}

def checkButtons(){
    log.info "Running checkButtons"
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
        log.info "Checking for updates now..."
        updateCheck()
        pause(3000)
  		state.btnName = state.newBtn
        runIn(2, resetBtnName)
    }
}   
def resetBtnName(){
    log.info "Resetting Button"
    if(state.status != "Current"){
	state.btnName = state.newBtn
    }
    else{
 state.btnName = "Check For Update" 
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
             state.newBtn = state.status
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
    if(state.status != "Current"){
		state.newBtn = state.status
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}


def setVersion(){
		state.version = "1.5.2"	 
		state.InternalName = "AverageAllchild"
}





