/**
 *  ****************  Kennel Heater Control  ****************
 *
 *
 *  Design Usage:
 *  This App was designed to control a kennel heater - Switching on/off around a desired temperature with a motion sensor to increase the set temperature when motion 'active'
 *
 *
 *  Copyright 2018 Andrew Parker
 *  
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
 *  
 *  Last Update: 19/11/2018
 *
 *  Changes:
 *
 *  V1.0.1 - Debug motion forcing heater on.
 *  V1.0.0 - POC 
 *
 *  Author: Cobra
 */



definition(
    name: "Motion and Temperature Controlled Switch",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "This App was designed to control a kennel heater - Switching on/off around a set temperature with a motion sensor to increase the set temperature",
    category: "",
    
    iconUrl: "",
	iconX2Url: "",
    iconX3Url: "",
    )


preferences {
	section(){input "switch1", "bool", title: "<b>Enable/Disable this app</b>", required: true, defaultValue: false}
   section("<b><u>Devices</i></u></b>") {
	
	input "temperatureSensor1", "capability.temperatureMeasurement" , title: "Select Temperature Sensor ", required: true
	input "motion1", "capability.motionSensor", title: "Select Motion Sensor ", required: true, multiple: false
	input "switch2", "capability.switch", title: "Switch(es) ", required: true, multiple: true   
   } 
	section(){}
	section("<b><u>Baseline Settings</u></b>") {   
	input "temperatureBase", "number", title: "Baseline Temperature ", defaultValue: 65, required: true	
	input "delay1", "number", title: "Delay after motion stops before returning to baseline temperature (Minutes) ", defaultValue: 30, required: true	
	}
	section(){}
	section("<b><u>Temperature Settings</b></u> <br>(If Motion Sensor <i>'Active'</i> or within delay period)") { 
  	input "temperature1", "number", title: "If temperature goes below this setting the switch will be ON ", defaultValue: 75, required: true
    input "temperature2", "number", title: "If temperature goes above this setting the switch will be OFF ", defaultValue: 85, required: true   
	}
   section() {input "debugMode", "bool", title: "Enable debug logging", defaultValue: false}  // required: true, 
	section(){} 
	section("<hr>"){}  
 }	
	

def installed(){initialise()}
def updated(){initialise()}
def initialise(){
	subscribeNow()
	log.info "Initialised with settings: ${settings}"	
}
def subscribeNow() {
	unsubscribe()
	setVersion()
	subscribe(motion1, "motion", motionHandler)
	subscribe(switch2, "switch", switch2Handler)
	subscribe(temperatureSensor1, "temperature", temperatureHandler)
	logCheck()
	state.enable = switch1
	state.confTempOff = temperature2
    state.confTempOn = temperature1
	state.baseTemp = temperatureBase
   }



def motionHandler(evt){
	LOGDEBUG("Motion Handler Called")
	if(state.enable == true){
	state.motion = evt.value
	if(delay1){state.delay = 60 * delay1}
    else {state.delay = 0}           

if(state.motion == 'active' ){
LOGDEBUG("$motion1 is 'active' so switching $switch2 on and setting to higher temperature range ($state.confTempOn&#176- $state.confTempOff&#176) but only if current temperature is below $state.confTempOff&#176")
		motionTemp()
		state.motionNow = true
}
if(state.motion == 'inactive'){
LOGDEBUG("Motion has stopped so waiting for $delay1 minutes before setting to baseline temperature to $state.baseTemp&#176 unless $motion1 goes 'active' again")
		runIn(state.delay, timeReset)
	}
  }
	else {LOGDEBUG("Motion has changed but app is disabled by switch")}
}

def timeReset(){
	LOGDEBUG("Motion has timed out so setting back to baseline temperature and switching off")
	state.motionNow = false
	switch2.off()
}

def temperatureHandler(evt) {
	LOGDEBUG("Temperature Handler Called")
	if(state.enable == true){
    def newtemp1 = evt.value
	state.newTemp = newtemp1.toDouble()
	LOGDEBUG("Reported temperature is now: $state.newTemp&#176")	
	state.baseTemp = temperatureBase
	if(state.motionNow == true){
	LOGDEBUG("Reported Motion Active.")		
	motionTemp()
	}
	if(state.motionNow == false){
	if(state.newTemp < state.baseTemp){
	LOGDEBUG("Temperature is below $state.baseTemp&#176 so turning on $switch2")	
	switch2.on()
	}
	if(state.newTemp > state.baseTemp){
	LOGDEBUG("Temperature is above $state.baseTemp&#176 so turning off $switch2")
	switch2.off()
	}
	}
  }
	else {LOGDEBUG("Temperature changed but app is disabled by switch")}
}




def motionTemp(){
	LOGDEBUG("MotionTemp Handler Called - Checking if I turn on heat or not")
	if(state.newTemp){
    if (state.newTemp < state.confTempOff) {
	LOGDEBUG( "Reported temperature is below $state.confTempOff&#176 so activating $switch2")
	switch2.on()
	}
    if (state.newTemp > state.confTempOff) {
    LOGDEBUG( "Reported temperature is above $state.confTempOff&#176 so deactivating $switch2")
	switch2.off()
	}
  }
	else{LOGDEBUG("No temperature recorded yet - Nothing to work with...")}
}

def switch2Handler(evt){
	state.switch1Now = evt.value
	LOGDEBUG("$switch2 is now: $state.switch1Now")
}

def logCheck(){
    state.checkLog = debugMode
    if(state.checkLog == true){log.info "All Logging Enabled"}
    if(state.checkLog == false){log.info "Further Logging Disabled"}
}


def LOGDEBUG(txt){
    try {
    if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    log.error("LOGDEBUG unable to output requested data!")
    }
}

def setVersion(){state.version = "1.0.1"}
		



