/**
 *  Design Usage:
 *  This is the 'Child' app for weather switch
 *
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This SmartApp is free!
 *
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this app without a donation, but if you find it useful
 *  then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this app better?  - Please let me know :)
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
 *  Last Update: 24/04/2018
 *
 *  Changes:
 *
 * 
 *
 *
 *
 *
 *  V1.0.0 - POC
 *
 */


definition(
    name: "Weather Switch Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Turns On/Off a switch based upon WU reports",
    category: "",
    parent: "Cobra:Weather Switch",
    
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Input/Output") {
    input(name: "enableswitch1", type: "capability.switch", title: "Enable/Disable app with this switch", required: false, multiple: false)
	input(name: "sensor1", type: "capability.sensor", title: "WU Device", required: true, multiple: false)
    input(name: "sensorswitch1", type: "capability.switch", title: "Switch to control", required: false, multiple: false)
       
	}
    section() {
  input "trigger", "enum", title: "Action to trigger switch", required: true, submitOnChange: true, 
      options: [
          "Illuminance",
          "Solar Radiation",
          "Temperature Feels Like",
          "Precipitation in Last Hour",
          "Precipitation Today",
          "Wind Speed",
    //      "Wind Direction",
          "Pressure",
          "Dewpoint",
          "UV Radiation",
          "Visibility",
          "Forecast High",
          "Forecast Low"
      ] 
   input(name: "action1", type: "bool", title: "Turn switch On or Off when trigger active", required: true, defaultValue: true)      
   input(name: "threshold1", type: "number", title: "Threshold", required: true, description: "Trigger above or below this number", defaultValue: '0')
   input(name: "switchMode1", type: "bool", title: "On = Trigger Above Threshold - Off = Trigger Below Threshold", required: true, defaultValue: true ) 
      
 }

    section() {
            input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false
  	        }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	log.info "Initialised with settings: ${settings}"
	setAppVersion()
	logCheck()
	state.enablecurrS1 = 'on'
        
        
	subscribe(enableswitch1, "switch", enableSwitch1Handler)
    subscribe(sensorSwitch1, "switch", sensorSwitch1Handler)
    subscribe(sensor1, "DisplayUnit", displayUnitHandler)
    
    state.selection = trigger
    
    if(state.selection == "Illuminance"){ subscribe(sensor1, "illuminance", illuminanceHandler)}
    if(state.selection ==  "Solar Radiation"){subscribe(sensor1, "solarradiation", solarradiationHandler)}
    if(state.selection == "Temperature Feels Like"){subscribe(sensor1, "feelsLike", feelsLikeHandler)}
    if(state.selection == "Precipitation in Last Hour"){subscribe(sensor1, "precip_1hr_in", precip_1hr_inHandler)}
    if(state.selection == "Precipitation Today"){subscribe(sensor1, "precip_today_in", precip_today_inHandler)}
    if(state.selection == "Wind Speed"){subscribe(sensor1, "wind_mph", wind_mphHandler)}
 //   if(state.selection == "Wind Direction"){subscribe(sensor1, "wind_dir", wind_dirHandler)}
    if(state.selection == "Pressure"){subscribe(sensor1, "pressure_in", pressure_inHandler)}
    if(state.selection ==  "Dewpoint"){subscribe(sensor1, "dewpoint", dewpointHandler)}
    if(state.selection == "UV Radiation"){subscribe(sensor1, "UV", uvHandler)}
    if(state.selection == "Visibility"){subscribe(sensor1, "visibility_mi", visibility_miHandler)}
    if(state.selection == "Forecast High"){subscribe(sensor1, "forecastHigh", forecastHighHandler)}
    if(state.selection == "Forecast Low"){subscribe(sensor1, "forecastLow", forecastLowHandler)}

   
    subscribe(sensor1, "observation_time", observation_timeHandler)
    subscribe(sensor1, "weather", weatherHandler)
    subscribe(sensor1, "forecastConditions", forecastConditionsHandler)
    subscribe(sensor1, "wind_string", wind_stringHandler)
    
    
}

def enableSwitch1Handler(evt){
state.enablecurrS1 = evt.value
LOGDEBUG("$enableswitch1 is $state.enablecurrS1")
    if(state.enablecurrS1 == 'off'){
    LOGDEBUG("Cannot switch On - App disabled... Switching off")  
        off()
       }
     
}

def sensorSwitch1Handler(evt){
state.currS1 = evt.value
LOGDEBUG("$switch1 is $state.currS1")
}

def displayUnitHandler(evt){
state.unit1 = evt.value
LOGDEBUG("Display Unit is $state.unit1")
}


// Weather Report Handlers

def  illuminanceHandler(evt){
	def event1 = evt.value
    def evt1 = event1.toDouble()
    def call1 = 'Illuminance'
	LOGDEBUG("Illuminance is $evt1")
 	actionNow(Illuminance, evt1)
}
        
    


def solarradiationHandler(evt){
    def event2 = evt.value
    def evt2 = event2.toDouble()
    def call2 = 'Solar Radiation'
	LOGDEBUG("Solar Radiation is $evt2")
    actionNow(call2, evt2)
}

def observation_timeHandler(evt){
state.observe1 = evt.value
LOGDEBUG("Observation Time is $state.observe1")
}

def weatherHandler(evt){
state.weather1 = evt.value
LOGDEBUG("Weather is $state.weather1")
}

def feelsLikeHandler(evt){
    def event3 = evt.value
    def evt3 = event3.toDouble()
    def call3 = 'Temperature feels Like'
	LOGDEBUG("Temperature feels Like: $evt3")
    actionNow(call3, evt3)
}



def precip_1hr_inHandler(evt){
    def event4 = evt.value
    def evt4 = event4.toDouble()
    def call4 = 'Precipitation in last hour'
	LOGDEBUG("Precipitation in last hour is $evt4")
    actionNow(call4, evt4)

}

def precip_today_inHandler(evt){
    def event5 = evt.value
    def evt5 = event5.toDouble()
    def call5 = 'Precipitation Today'
	LOGDEBUG("Precipitation Today is $evt5")
    actionNow(call5, evt5)


}

def wind_mphHandler(evt){ 
def event6 = evt.value
    def evt6 = event6.toDouble()
    def call6 = 'Wind Speed'
    LOGDEBUG("Wind =  $evt6.value")
    actionNow(call6, evt6.value)

}

def wind_stringHandler(evt){
LOGDEBUG("Wind String = $evt.value")
}

def pressure_inHandler(evt){
    def event7 = evt.value
    def evt7 = event7.toDouble()
    def call7 = 'Pressure'
    LOGDEBUG("Pressure =  $evt7")
    actionNow(call7, evt7)

}

def dewpointHandler(evt){
    def event8 = evt.value
    def evt8 = event8.toDouble()
    def call8 = 'Dewpoint'
    LOGDEBUG("Dewpoint =  $evt8")
    actionNow(call8, evt8)

}

def uvHandler(evt){
    def event9 = evt.value
    def evt9 = event9.toDouble()
    def call9 = 'UV'
    LOGDEBUG("UV =  $evt9")
    actionNow(call8, evt9)

}

def visibility_miHandler(evt){
    def event10 = evt.value
    def evt10 = event10.toDouble()
    def call10 = 'Visibility'
    LOGDEBUG("Visibility =  $evt10")
    actionNow(call10, evt10)

}


def forecastHighHandler(evt){
    def event11 = evt.value
    def evt11 = event11.toDouble()
    def call11 = 'Forecast High'
    LOGDEBUG("Forecast High =  $evt11")
    actionNow(call11, evt11)

}


def forecastLowHandler(evt){
    def event12 = evt.value
    def evt12 = event12.toDouble()
    def call12 = 'Forecast Low'
    LOGDEBUG("Forecast Low =  $evt12")
   actionNow(call12, evt12)
}

def wind_dirHandler(evt){
  def event13 = evt.value
    def evt13 = event13.toDouble()
    def call13 = 'Wind Directiopn'
    LOGDEBUG("Wind Direction =  $evt13")
    actionNow(call13, evt13)

}



def forecastConditionsHandler(evt){
state.forecastCond1 = evt.value
LOGDEBUG("Forecast Conditions = $state.forecastCond1")
}



// Switch Actions

    
def actionNow(call, evt){
state.evtNow = evt.value as int
state.thresh = threshold1
state.action = action1
state.mode = switchMode1
   LOGDEBUG("Calling event = $call") 
    LOGDEBUG("state.evtNow = $state.evtNow -- state.thresh = $state.thresh -- state.action = $state.action -- state.mode = $state.mode")
    
    if(state.action == true){   
        LOGDEBUG("state.action = $state.action")
    if(state.evtNow > state.thresh && state.mode == true){ on()}
	if(state.evtNow > state.thresh && state.mode == false){ off()}
	if(state.evtNow < state.thresh && state.mode == true){ off()}
    if(state.evtNow < state.thresh && state.mode == false){ on()}
    }
    
    if(state.action == false){ 
        LOGDEBUG("state.action = $state.action")
    if(state.evtNow > state.thresh && state.mode == true){ off()}
	if(state.evtNow > state.thresh && state.mode == false){ on()}
	if(state.evtNow < state.thresh && state.mode == true){ on()}
    if(state.evtNow < state.thresh && state.mode == false){ off()}
    }    
     
}


def on(){
    if(state.enablecurrS1 == 'on'){ 
    	LOGDEBUG("Turning on switch...")   
    	sensorswitch1.on()      
    }
    if(state.enablecurrS1 == 'off'){ 
    	LOGDEBUG("Cannot switch on - App disabled...") 
    }
}


def off(){
      	LOGDEBUG("Turning off switch...")   
    	sensorswitch1.off()      
       
}




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
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (Version: ${state.appversion}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}


// App Version   *********************************************************************************
def setAppVersion(){
    state.appversion = "1.0.0"
}