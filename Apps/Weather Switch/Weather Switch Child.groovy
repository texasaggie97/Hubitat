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
 *  Last Update: 25/04/2018
 *
 *  Changes:
 *
 * 
 *
 *
 *  V1.2.1 - Debug & added driver version checking
 *  V1.1.0 - additional data logging
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
     page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
     page name: "restrictionsPage", title: "", install: true, uninstall: true
}
    
    
// main page *****************************************************************************************************************
  def mainPage() {
    dynamicPage(name: "mainPage") {  
	section("Input/Output") {
    input(name: "enableswitch1", type: "capability.switch", title: "Enable/Disable app with this switch", required: false, multiple: false)
	input(name: "sensor1", type: "capability.sensor", title: "WU Device", required: true, multiple: false)
    input(name: "sensorswitch1", type: "capability.switch", title: "Switch to control", required: false, multiple: false)
       
	}
    section() {
  input "trigger", "enum", title: "Action to trigger switch", required: true, submitOnChange: true, 
      options: [
          "Dewpoint",
          "Forecast High",
          "Forecast Low",
          "Illuminance",
          "Precipitation in Last Hour",
          "Precipitation Today",
          "Pressure",
          "Solar Radiation",
          "Temperature Feels Like",
          "UV Radiation",
          "Visibility",
  //      "Wind Direction",
          "Wind Gust",
          "Wind Speed"
          ]       
          
          
          
          
      
   input(name: "action1", type: "bool", title: "Turn switch On or Off when trigger active", required: true, defaultValue: true)      
   input(name: "threshold1", type: "number", title: "Threshold", required: true, description: "Trigger above or below this number", defaultValue: '0')
   input(name: "switchMode1", type: "bool", title: "On = Trigger Above Threshold - Off = Trigger Below Threshold", required: true, defaultValue: true ) 
     
     }
    } 
  }

// Restrictions Page *************************************************************************************************************

def restrictionsPage() {
       dynamicPage(name: "restrictionsPage") {
           
           section() {
           		mode title: "Run only when in specific mode(s) ", required: false
            }
        

		section() {
        
    input "restrictions1", "bool", title: "Restrict by Time & Day", required: true, defaultValue: false, submitOnChange: true
    input "restrictions2", "bool", title: "Restrict by Presence Sensor", required: true, defaultValue: false, submitOnChange: true
     }

      if(restrictions1 == true){    
     	section("Time/Day") {
    input "fromTime", "time", title: "Allow actions from", required: false
    input "toTime", "time", title: "Allow actions until", required: false
    input "days", "enum", title: "Select Days of the Week", required: false, multiple: true, options: ["Monday": "Monday", "Tuesday": "Tuesday", "Wednesday": "Wednesday", "Thursday": "Thursday", "Friday": "Friday", "Saturday": "Saturday", "Sunday": "Sunday"]
    		}      
      }       
      if(restrictions2 == true){
    section("This is to restrict on 1 or 2 presence sensor(s)") {
    input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    if(restrictPresenceSensor){
   	input "restrictPresenceAction", "bool", title: "   On = Action only when someone is 'Present'  \r\n   Off = Action only when someone is 'NOT Present'  ", required: true, defaultValue: false    
	}
     input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    if(restrictPresenceSensor1){
   	input "restrictPresenceAction1", "bool", title: "   On = Action only when someone is 'Present'  \r\n   Off = Action only when someone is 'NOT Present'  ", required: true, defaultValue: false    
	}
    
    }
            }         
           
       
       section() {
                label title: "Enter a name for this automation child", required: false
            }
      section() {
            input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false
  	        }

           
           
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
    
    subscribe(sensor1, "Driver_NameSpace", checkNameHandler)
    subscribe(sensor1, "Driver_Version", checkDriverVerHandler)
    
    subscribe(sensor1, "Display_Unit_Temperature", displayTempUnitHandler)
    subscribe(sensor1,  "Display_Unit_Pressure", displayPressureUnitHandler)
    subscribe(sensor1, "Display_Unit_Distance", displayDistanceUnitHandler)
   
    if (restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if (restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
        
    state.selection = trigger
    
    if(state.selection == "Illuminance"){ subscribe(sensor1, "Illuminance", illuminanceHandler)}
    if(state.selection ==  "Solar Radiation"){subscribe(sensor1, "Solar_Radiation", solarradiationHandler)}
    if(state.selection == "Temperature Feels Like"){subscribe(sensor1, "Feels_Like", feelsLikeHandler)}
    if(state.selection == "Precipitation in Last Hour"){subscribe(sensor1, "Precip_Last_Hour", precip_1hrHandler)}
    if(state.selection == "Precipitation Today"){subscribe(sensor1, "Precip_Today", precip_todayHandler)}
    if(state.selection == "Wind Speed"){subscribe(sensor1, "Wind_Speed", windHandler)}
    if(state.selection == "Wind Gust"){subscribe(sensor1, "Wind_Gust", wind_gustHandler)}
    if(state.selection == "Pressure"){subscribe(sensor1, "Pressure", pressureHandler)}
    if(state.selection ==  "Dewpoint"){subscribe(sensor1, "Dewpoint", dewpointHandler)}
    if(state.selection == "UV Radiation"){subscribe(sensor1, "UV", uvHandler)}
    if(state.selection == "Visibility"){subscribe(sensor1, "Visibility", visibilityHandler)}
    if(state.selection == "Forecast High"){subscribe(sensor1, "Forecast_High", forecastHighHandler)}
    if(state.selection == "Forecast Low"){subscribe(sensor1, "Forecast_Low", forecastLowHandler)}

  	subscribe(sensor1, "Wind_Direction", wind_dirHandler)
    subscribe(sensor1, "Observation_Time", observation_timeHandler)
    subscribe(sensor1, "Weather", weatherHandler)
    subscribe(sensor1, "Forecast_Conditions", forecastConditionsHandler)
    subscribe(sensor1, "Wind_String", wind_stringHandler)
    subscribe(sensor1, "Alert", alertHandler)
    
}

def enableSwitch1Handler(evt){
state.enablecurrS1 = evt.value
LOGDEBUG("$enableswitch1 is $state.enablecurrS1")
    if(state.enablecurrS1 == 'off'){
    LOGDEBUG("Cannot switch On - App disabled... Switching off")  
        off()
       }
     
}



def checkNameHandler(evt){
  def drivername = evt.value 
    if(state.reqNameSpace == drivername){LOGDEBUG("You ARE using Cobra's version of the driver ")} 
    else {log.warn "*** You are not using Cobra's version of the driver ***  "} 
}

def checkDriverVerHandler(evt){
    def driverversion = evt.value
    if(state.reqdriverversion == driverversion){LOGDEBUG("Driver version number:OK")}
    else{log.warn "*** Driver version number does not match requirement for this app ***"}
}
          
def sensorSwitch1Handler(evt){
state.currS1 = evt.value
LOGDEBUG("$switch1 is $state.currS1")
}

def displayTempUnitHandler(evt){
state.unit1 = evt.value
LOGDEBUG("Temp Display Unit is $state.unit1")
}

def displayPressureUnitHandler(evt){
state.unit2 = evt.value
LOGDEBUG("Pressure Display Unit is $state.unit2")
}

def displayDistanceUnitHandler(evt){
state.unit3 = evt.value
LOGDEBUG("Distance Display Unit is $state.unit3")
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

def  alertHandler(evt){
state.alert1 = evt.value
LOGDEBUG("Weather Alert is $state.alert1")    
   
}


def feelsLikeHandler(evt){
    def event3 = evt.value
    def evt3 = event3.toDouble() 
    def call3 = 'Temperature feels Like'
	LOGDEBUG("Temperature feels Like: $evt3")
    actionNow(call3, evt3)
}



def precip_1hrHandler(evt){
    def event4 = evt.value
    def evt4 = event4.toInteger() 
    def call4 = 'Precipitation in last hour'
	LOGDEBUG("Precipitation in last hour is $evt4")
    actionNow(call4, evt4)

}

def precip_todayHandler(evt){
    def event5 = evt.value
    def evt5 = event5.toInteger() 
    def call5 = 'Precipitation Today'
	LOGDEBUG("Precipitation Today is $evt5")
    actionNow(call5, evt5)


}

def windHandler(evt){ 
def event6 = evt.value
    def evt6 = event6.toDouble() 
    def call6 = 'Wind Speed'
    LOGDEBUG("Wind =  $evt6.value")
    actionNow(call6, evt6.value)

}

def wind_stringHandler(evt){
LOGDEBUG("Wind String = $evt.value")
}

def pressureHandler(evt){
    def event7 = evt.value
    def evt7 = event7.toDouble() 
    def call7 = 'Pressure'
    LOGDEBUG("Pressure =  $evt7")
    actionNow(call7, evt7)

}

def dewpointHandler(evt){
    def event8 = evt.value
    def evt8 = event8.toInteger() 
    def call8 = 'Dewpoint'
    LOGDEBUG("Dewpoint =  $evt8")
    actionNow(call8, evt8)

}

def uvHandler(evt){
    def event9 = evt.value
    def evt9 = event9.toInteger() 
    def call9 = 'UV'
    LOGDEBUG("UV =  $evt9")
    actionNow(call8, evt9)

}

def visibilityHandler(evt){
    def event10 = evt.value
    def evt10 = event10.toDouble()
    def call10 = 'Visibility'
    LOGDEBUG("Visibility =  $evt10")
    actionNow(call10, evt10)

}


def forecastHighHandler(evt){
    def event11 = evt.value
    def evt11 = event11.toInteger()  //.toDouble()
    def call11 = 'Forecast High'
    LOGDEBUG("Forecast High =  $evt11")
    actionNow(call11, evt11)

}


def forecastLowHandler(evt){
    def event12 = evt.value
    def evt12 = event12.toInteger() 
    def call12 = 'Forecast Low'
    LOGDEBUG("Forecast Low =  $evt12")
   actionNow(call12, evt12)
}

def wind_dirHandler(evt){
  def event13 = evt.value
   
    def evt13 = event13
    def call13 = 'Wind Direction'
    LOGDEBUG("Wind Direction =  $evt13")
//    actionNow(call13, evt13)

}

def wind_gustHandler(evt){
  def event14 = evt.value
    def evt14 = event14.toDouble()
    def call14 = 'Wind Gust'
    LOGDEBUG("Wind Direction =  $evt13")
    actionNow(call14, evt14)   
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
LOGDEBUG("Calling.. CheckTime")
checkTime()
LOGDEBUG("Calling.. CheckDay")
checkDay()
LOGDEBUG("Calling.. CheckPresence")
checkPresence()
LOGDEBUG("Calling.. CheckPresence1")
checkPresence1()   
    
    
    LOGDEBUG("state.evtNow = $state.evtNow -- state.thresh = $state.thresh -- state.action = $state.action -- state.mode = $state.mode - state.presenceRestriction = $state.presenceRestriction - state.presenceRestriction1 = $state.presenceRestriction1")
 if(state.timeOK == true && state.dayCheck == true && state.presenceRestriction == true && state.presenceRestriction1 == true){
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
    else{
     LOGDEBUG("One or more restrictions are active so cannot continue")
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
  
// Check time allowed to run...

def checkTime(){
def timecheckNow = fromTime
if (timecheckNow != null){
def between = timeOfDayIsBetween(fromTime, toTime, new Date(), location.timeZone)
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


// check days allowed to run
def checkDay(){
def daycheckNow = days
if (daycheckNow != null){
 def df = new java.text.SimpleDateFormat("EEEE")
    
    df.setTimeZone(location.timeZone)
    def day = df.format(new Date())
    def dayCheck1 = days.contains(day)
    if (dayCheck1) {

  state.dayCheck = true
LOGDEBUG( " Day ok so can continue...")
 }       
 else {
LOGDEBUG( " Not today!")
 state.dayCheck = false
 }
 }
if (daycheckNow == null){ 
 LOGDEBUG("Day restrictions have not been configured -  Continue...")
 state.dayCheck = true 
} 
}

    
    
    
    
    
// Define restrictPresenceSensor actions
def restrictPresenceSensorHandler(evt){
state.presencestatus1 = evt.value
LOGDEBUG("state.presencestatus1 = $evt.value")
checkPresence()
checkPresence1()

}


def checkPresence(){
LOGDEBUG("running checkPresence - restrictPresenceSensor = $restrictPresenceSensor")

if(restrictPresenceSensor){
LOGDEBUG("Presence = $state.presencestatus1")
def actionPresenceRestrict = restrictPresenceAction


if (state.presencestatus1 == "present" && actionPresenceRestrict == true){
LOGDEBUG("Presence ok")
state.presenceRestriction = true
}
else if (state.presencestatus1 == "not present" && actionPresenceRestrict == true){
LOGDEBUG("Presence not ok")
state.presenceRestriction = false
}

if (state.presencestatus1 == "not present" && actionPresenceRestrict == false){
LOGDEBUG("Presence ok")
state.presenceRestriction = true
}
else if (state.presencestatus1 == "present" && actionPresenceRestrict == false){
LOGDEBUG("Presence not ok")
state.presenceRestriction = false
}
}
else if(!restrictPresenceSensor){
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
LOGDEBUG("Presence 2 ok")
state.presenceRestriction1 = true
}
else if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == true){
LOGDEBUG("Presence 2 not ok")
state.presenceRestriction1 = false
}

if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == false){
LOGDEBUG("Presence 2 ok")
state.presenceRestriction1 = true
}
else if (state.presencestatus2 == "present" && actionPresenceRestrict1 == false){
LOGDEBUG("Presence 2 not ok")
state.presenceRestriction1 = false
}
}
else if(!restrictPresenceSensor1){
state.presenceRestriction1 = true
LOGDEBUG("Presence sensor 2 restriction not used")
}
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


// App & Driver Version   *********************************************************************************
def setAppVersion(){
    state.appversion = "1.2.1"
    state.reqdriverversion = "1.7.2"  // required driver version for this app
    state.reqNameSpace = "Cobra"   // check to confirm Cobra's driver is being used
}