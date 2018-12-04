/**
 *  ****************  Average All (Illumination, Temperature, Humidity, Pressure & Motion)  ****************
 *
 *  Design Usage:
 *  This app was designed to display/set an 'average' Illumination, Temperature, Humidity or Pressure from a group of devices. It can also be used to 'group' a number of Motion sensors together to act as one
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
 *  Last Update: 12/11/2018
 *
 *  Changes:
 *
 *  V1.8.0 - Added 'restrictions' page & code cleanup 
 *  V1.7.0 - Revised update checking and added 'pause' button
 *  V1.6.0 - Added to Cobra Apps
 *  V1.5.3 - Added pushover for update notification
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
	section() {
	page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
	page name: "restrictionsPage", title: "", install: true, uninstall: true
	}
}
    

 def mainPage() {
	dynamicPage(name: "mainPage") {  
	preCheck()
 
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
     
    }                  
        }                     
    }                   

}    
        
def restrictionsPage() {
    dynamicPage(name: "restrictionsPage") {
        section(""){paragraph "<font size='+1'>App Restrictions</font> <br>These restrictions are optional <br>Any restriction you don't want to use, you can just leave blank"}
        section(){
        input "enableSwitch", "capability.switch", title: "Select a switch Enable/Disable this app", required: false, multiple: false, submitOnChange: true 
     	if(enableSwitch){ input "enableSwitchMode", "bool", title: "Allow app to run only when this switch is..", required: true, defaultValue: false, submitOnChange: true}
        }
        section(){input(name:"modes", type: "mode", title: "Allow actions when current mode is:", multiple: true, required: false)}
       	section(){
    	input "fromTime", "time", title: "Allow actions from", required: false
    	input "toTime", "time", title: "Allow actions until", required: false
    	input "days", "enum", title: "Allow actions only on these days of the week", required: false, multiple: true, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        	}
        section(){
    	input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor){input "restrictPresenceAction", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
     	input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor1){input "restrictPresenceAction1", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
   			}
		section(){
       	input "sunriseSunset", "enum", title: "Sunrise/Sunset Restriction", required: false, submitOnChange: true, options: ["Sunrise","Sunset"] 
		if(sunriseSunset == "Sunset"){	
       	input "sunsetOffsetValue", "number", title: "Optional Sunset Offset (Minutes)", required: false
		input "sunsetOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        	}
		if(sunriseSunset == "Sunrise"){
    	input "sunriseOffsetValue", "number", title: "Optional Sunrise Offset (Minutes)", required: false
		input "sunriseOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        	}
     	}
        section() {label title: "Enter a name for this automation", required: false}
        section() {input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}

    }
}

def installed(){initialise()}
def updated(){initialise()}
def initialise(){
	version()
	subscribeNow()
	log.info "Initialised with settings: ${settings}"
	logCheck()	
}
def subscribeNow() {
	unsubscribe()
	if(enableSwitch){subscribe(enableSwitch, "switch", switchEnable)}
	if(enableSwitchMode == null){enableSwitchMode = true} // ????
	if(restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if(restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
	if(sunriseSunset){astroCheck()}
	if(sunriseSunset){schedule("0 1 0 1/1 * ? *", astroCheck)} // checks sunrise/sunset change at 00.01am every day
    
  // App Specific subscriptions & settings below here   
    
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
    if(state.selection == "Motion"){ subscribe(motionSensors, "motion", motionSensorsHandler)}    
    if(state.selection != "Motion"){ state.DecimalPlaces = decimalUnit.toInteger() }
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
}






def illuminanceHandler(evt) {
LOGDEBUG("Running illuminance handler")
    checkAllow()
	if(state.allAllow == true){

   
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
}



def tempSensorsHandler(evt) {
LOGDEBUG("Running temperature handler")
    checkAllow()
	if(state.allAllow == true){


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
}



def humidityHandler(evt) {
LOGDEBUG("Running humidity handler")
    checkAllow()
	if(state.allAllow == true){

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
}

def pressureSensorsHandler(evt) {
LOGDEBUG("Running pressure handler")
   checkAllow()
	if(state.allAllow == true){

    
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
   checkAllow()
	if(state.allAllow == true){

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
}    

def offNOW(){
 if (state.go == 'stop'){
    LOGDEBUG( "Inactive Now!")
	settings.vDevice.setMotion("inactive")
 }
}



def checkAllow(){
    state.allAllow = false
    LOGDEBUG("Checking for any restrictions...")
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){
        LOGDEBUG("Continue - App NOT paused")
        state.noPause = true
    	modeCheck()
    	checkTime()
        checkDay()
        checkSun()
        checkPresence()
        checkPresence1()
	
 
	if(state.modeCheck == false){
	LOGDEBUG("Not in correct 'mode' to continue")
	    }    
	if(state.presenceRestriction ==  false || state.presenceRestriction1 ==  false){
	LOGDEBUG( "Cannot continue - Presence failed")
	}
	if(state.appgo == false){
	LOGDEBUG("$enableSwitch is not in the correct position so cannot continue")
	}
	if(state.appgo == true && state.dayCheck == true && state.presenceRestriction == true && state.presenceRestriction1 == true && state.modeCheck == true && state.timeOK == true && state.noPause == true && state.sunGoNow == true){
	state.allAllow = true 
 	  }
	else{
 	state.allAllow = false
	LOGWARN( "One or more restrictions apply - Unable to continue")
 	LOGDEBUG("state.appgo = $state.appgo, state.dayCheck = $state.dayCheck, state.presenceRestriction = $state.presenceRestriction, state.presenceRestriction1 = $state.presenceRestriction1, state.modeCheck = $state.modeCheck, state.timeOK = $state.timeOK, state.noPause = $state.noPause, state.sunGoNow = $state.sunGoNow")
      }
   }
}

def checkSun(){
	LOGDEBUG("Checking Sunrise/Sunset restrictions...")
	if(!sunriseSunset){
        state.sunGoNow = true
        LOGDEBUG("No Sunrise/Sunset restrictions in place")	
	}
        if(sunriseSunset){
        if(sunriseSunset == "Sunset"){	
        if(state.astro == "Set"){
        state.sunGoNow = true
        LOGDEBUG("Sunset OK")
            } 
    	if(state.astro == "Rise"){
        state.sunGoNow = false
        LOGDEBUG("Sunset NOT OK")
            } 
        }
	if(sunriseSunset == "Sunrise"){	
        if(state.astro == "Rise"){
        state.sunGoNow = true
        LOGDEBUG("Sunrise OK")
            } 
    	if(state.astro == "Set"){
        state.sunGoNow = false
        LOGDEBUG("Sunrise NOT OK")
            } 
        }  
    } 
		return state.sunGoNow
}    

def astroCheck() {
    state.sunsetOffsetValue1 = sunsetOffsetValue
    state.sunriseOffsetValue1 = sunriseOffsetValue
    if(sunsetOffsetDir == "Before"){state.sunsetOffset1 = -state.sunsetOffsetValue1}
    if(sunsetOffsetDir == "after"){state.sunsetOffset1 = state.sunsetOffsetValue1}
    if(sunriseOffsetDir == "Before"){state.sunriseOffset1 = -state.sunriseOffsetValue1}
    if(sunriseOffsetDir == "after"){state.sunriseOffset1 = state.sunriseOffsetValue1}
	def both = getSunriseAndSunset(sunriseOffset: state.sunriseOffset1, sunsetOffset: state.sunsetOffset1)
	def now = new Date()
	def riseTime = both.sunrise
	def setTime = both.sunset
	LOGDEBUG("riseTime: $riseTime")
	LOGDEBUG("setTime: $setTime")
	unschedule("sunriseHandler")
	unschedule("sunsetHandler")
	if (riseTime.after(now)) {
	LOGDEBUG("scheduling sunrise handler for $riseTime")
	runOnce(riseTime, sunriseHandler)
		}
	if(setTime.after(now)) {
	LOGDEBUG("scheduling sunset handler for $setTime")
	runOnce(setTime, sunsetHandler)
		}
	LOGDEBUG("AstroCheck Complete")
}

def sunsetHandler(evt) {
	LOGDEBUG("Sun has set!")
	state.astro = "Set" 
}
def sunriseHandler(evt) {
	LOGDEBUG("Sun has risen!")
	state.astro = "Rise"
}

def modeCheck() {
    LOGDEBUG("Checking for any 'mode' restrictions...")
	def result = !modes || modes.contains(location.mode)
    LOGDEBUG("Mode = $result")
    state.modeCheck = result
    return state.modeCheck
 }



def checkTime(){
    LOGDEBUG("Checking for any time restrictions")
	def timecheckNow = fromTime
	if (timecheckNow != null){
    
def between = timeOfDayIsBetween(toDateTime(fromTime), toDateTime(toTime), new Date(), location.timeZone)
    if (between) {
    state.timeOK = true
   LOGDEBUG("Time is ok so can continue...")
    
}
	else if (!between) {
	state.timeOK = false
	LOGDEBUG("Time is NOT ok so cannot continue...")
	}
  }
	else if (timecheckNow == null){  
	state.timeOK = true
  	LOGDEBUG("Time restrictions have not been configured -  Continue...")
  }
}



def checkDay(){
    LOGDEBUG("Checking for any 'Day' restrictions")
	def daycheckNow = days
	if (daycheckNow != null){
 	def df = new java.text.SimpleDateFormat("EEEE")
    df.setTimeZone(location.timeZone)
    def day = df.format(new Date())
    def dayCheck1 = days.contains(day)
    if (dayCheck1) {
	state.dayCheck = true
	LOGDEBUG( "Day ok so can continue...")
 }       
 	else {
	LOGDEBUG( "Cannot run today!")
 	state.dayCheck = false
 	}
 }
if (daycheckNow == null){ 
	LOGDEBUG("Day restrictions have not been configured -  Continue...")
	state.dayCheck = true 
	} 
}

def restrictPresenceSensorHandler(evt){
	state.presencestatus1 = evt.value
	LOGDEBUG("state.presencestatus1 = $evt.value")
	checkPresence()
}



def checkPresence(){
	LOGDEBUG("Running checkPresence - restrictPresenceSensor = $restrictPresenceSensor")
	if(restrictPresenceSensor){
	LOGDEBUG("Presence = $state.presencestatus1")
	def actionPresenceRestrict = restrictPresenceAction
	if (state.presencestatus1 == "present" && actionPresenceRestrict == true){
	LOGDEBUG("Presence ok")
	state.presenceRestriction = true
	}
	if (state.presencestatus1 == "not present" && actionPresenceRestrict == true){
	LOGDEBUG("Presence not ok")
	state.presenceRestriction = false
	}

	if (state.presencestatus1 == "not present" && actionPresenceRestrict == false){
	LOGDEBUG("Presence ok")
	state.presenceRestriction = true
	}
	if (state.presencestatus1 == "present" && actionPresenceRestrict == false){
	LOGDEBUG("Presence not ok")
	state.presenceRestriction = false
	}
}
	else if(restrictPresenceSensor == null){
	state.presenceRestriction = true
	LOGDEBUG("Presence sensor restriction not used")
	}
}


def restrictPresence1SensorHandler(evt){
	state.presencestatus2 = evt.value
	LOGDEBUG("state.presencestatus2 = $evt.value")
	checkPresence1()
}


def checkPresence1(){
	LOGDEBUG("running checkPresence1 - restrictPresenceSensor1 = $restrictPresenceSensor1")
	if(restrictPresenceSensor1){
	LOGDEBUG("Presence = $state.presencestatus1")
	def actionPresenceRestrict1 = restrictPresenceAction1
	if (state.presencestatus2 == "present" && actionPresenceRestrict1 == true){
	LOGDEBUG("Presence 2 ok - Continue..")
	state.presenceRestriction1 = true
	}
	if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == true){
	LOGDEBUG("Presence 2 not ok")
	state.presenceRestriction1 = false
	}
	if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == false){
	LOGDEBUG("Presence 2 ok - Continue..")
	state.presenceRestriction1 = true
	}
	if (state.presencestatus2 == "present" && actionPresenceRestrict1 == false){
	LOGDEBUG("Presence 2 not ok")
	state.presenceRestriction1 = false
	}
  }
	if(restrictPresenceSensor1 == null){
	state.presenceRestriction1 = true
	LOGDEBUG("Presence sensor 2 restriction not used - Continue..")
	}
}

def switchEnable(evt){
	state.enableInput = evt.value
	LOGDEBUG("Switch changed to: $state.enableInput")  
    if(enableSwitchMode == true && state.enableInput == 'off'){
	state.appgo = false
	LOGDEBUG("Cannot continue - App disabled by switch")  
    }
	if(enableSwitchMode == true && state.enableInput == 'on'){
	state.appgo = true
	LOGDEBUG("Switch restriction is OK.. Continue...") 
    }    
	if(enableSwitchMode == false && state.enableInput == 'off'){
	state.appgo = true
	LOGDEBUG("Switch restriction is OK.. Continue...")  
    }
	if(enableSwitchMode == false && state.enableInput == 'on'){
	state.appgo = false
	LOGDEBUG("Cannot continue - App disabled by switch")  
    }    
	LOGDEBUG("Allow by switch is $state.appgo")
}

def version(){
	setDefaults()
    pauseOrNot()
    logCheck()
    resetBtnName()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
    checkButtons()
   
}



def logCheck(){
    state.checkLog = debugMode
    if(state.checkLog == true){log.info "All Logging Enabled"}
    if(state.checkLog == false){log.info "Further Logging Disabled"}
}

def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}
def LOGWARN(txt){
    try {
    	if (settings.debugMode) { log.warn("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGWARN unable to output requested data!")
    }
}



def display(){
    setDefaults()
    if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}}
    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){section(){input "updateBtn", "button", title: "$state.btnName"}}
    if(state.status != "Current"){section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>$state.updateURI</b><hr>"}}
    section() {
    input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available", required: true, defaultValue: false, submitOnChange: true 
    if(updateNotification == true){ input "speakerUpdate", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
    }
    section(){input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false }
}



def checkButtons(){
    LOGDEBUG("Running checkButtons")
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
    LOGDEBUG("Checking for updates now...")
    updateCheck()
    pause(3000)
    state.btnName = state.newBtn
    runIn(2, resetBtnName)
    }
    if(state.btnCall == "updateBtn1"){
    state.btnName1 = "Click Here" 
    httpGet("https://github.com/CobraVmax/Hubitat/tree/master/Apps' target='_blank")
    }
    
}   
def resetBtnName(){
    LOGDEBUG("Resetting Button")
    if(state.status != "Current"){
    state.btnName = state.newBtn
    }
    else{
    state.btnName = "Check For Update" 
    }
}    
    

def pushOverUpdate(inMsg){
    if(updateNotification == true){  
    newMessage = inMsg
    LOGDEBUG(" Message = $newMessage ")  
    state.msg1 = '[L]' + newMessage
    speakerUpdate.speak(state.msg1)
    }
}

def pauseOrNot(){
LOGDEBUG(" Calling 'pauseOrNot'...")
    state.pauseNow = pause1
    if(state.pauseNow == true){
    state.pauseApp = true
    if(app.label){
    if(app.label.contains('red')){
    log.warn "Paused"}
    else{app.updateLabel(app.label + ("<font color = 'red'> (Paused) </font>" ))
    log.warn "App Paused - state.pauseApp = $state.pauseApp "   
    }
   }
  }
    if(state.pauseNow == false){
    state.pauseApp = false
    if(app.label){
    if(app.label.contains('red')){ app.updateLabel(app.label.minus("<font color = 'red'> (Paused) </font>" ))
    LOGDEBUG("App Released - state.pauseApp = $state.pauseApp ")                          
    }
   }
  }    
}

def updateCheck(){
    setVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"]
    try {
    httpGet(paramsUD) { respUD ->
//  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def commentRead = (respUD.data.Comment)
       		state.Comment = commentRead

            def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
            state.updateURI = updateUri   
            
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            state.newver = newVerRaw
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.status = "<b>** This app is no longer supported by $state.author  **</b>"  
             log.warn "** This app is no longer supported by $state.author **" 
            
      		}           
		else if(currentVer < newVer){
        	state.status = "<b>New Version Available ($newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn " Update: $state.UpdateInfo "
             state.newBtn = state.status
            def updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
            pushOverUpdate(updateMsg)
       		} 
		else{ 
      		state.status = "Current"
       		LOGDEBUG("You are using the current version of this app")
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


def preCheck(){
	state.appInstalled = app.getInstallationState()  
    
    if(state.appInstalled != 'COMPLETE'){
    section(){ paragraph "This app was designed to display/set an 'average' Illumination, Temperature, Humidity or Pressure from a group of devices. <br>It can also be used to 'group' a number of Motion sensors together to act as one"}
    }
	if(state.appInstalled == 'COMPLETE'){
	display()   
 	}
}

def setDefaults(){
    LOGDEBUG("Initialising defaults...")
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}
    if(enableSwitch == null){
    LOGDEBUG("Enable switch is NOT used. Switch is: $enableSwitch - Continue..")
    state.appgo = true
    }
}

    
def setVersion(){
		state.version = "1.8.0"	 
		state.InternalName = "AverageAllChild"
    	state.ExternalName = "Average All Child"
    	state.CobraAppCheck = "averageall.json"
}










