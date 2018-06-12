/**
 *  Design Usage:
 *  This is the 'Child' app for weather switch
 *
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This SmartApp is free!
 *
 *  Donations to support development efforts are welcomed via: 
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
 *  Last Update: 11/06/2018
 *
 *  Changes:
 *
 *
 *  V2.0.1.172 - Driver requirement updated
 *  V2.0.1.150 - Driver requirement updated
 *  V2.0.1.140 - debug
 *  V2.0.0.130 - Recode for new supported weewx/apixu driver
 *  V1.6.0.250 - New driver and changed attributes to lowercase to match driver
 *  V1.5.0.241 - Driver requirement updated
 *  V1.5.0.240 - Added Triggers: 'Sunrise', 'Sunset', 'Wind Direction' & 'Forecast Conditions' for use with driver 2.4.0
 *  V1.4.0.211 - Added rain for tomorrow & the day after as triggers for use with driver v2.1.1
 *  V1.3.0.190 - Added 'Chance Of Rain' as a trigger for use with a new driver (v1.9.0)
 *  V1.3.0.180 - New versioning to incorporate required driver version
 *  V1.2.1 - Debug & added driver version checking
 *  V1.1.0 - additional data logging
 *  V1.0.0 - POC
 *
 */


definition(
    name: "Weather Switch Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Turns On/Off a switch based upon weather reports",
    category: "",
    parent: "Cobra:Weather Switch",
    
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
     page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
     page name: "restrictionsPage", title: "Final Page", install: true, uninstall: true
}
    
    
// main page *****************************************************************************************************************
  def mainPage() {
    dynamicPage(name: "mainPage") {  
	section("Input/Output") {
    input(name: "enableswitch1", type: "capability.switch", title: "Enable/Disable app with this switch", required: false, multiple: false)
	input(name: "sensor1", type: "capability.sensor", title: "Weather Device", required: true, multiple: false)
    input(name: "sensorswitch1", type: "capability.switch", title: "Switch to control", required: false, multiple: false)
       
	}
    section() {
  input "trigger", "enum", title: "Action to trigger switch", required: true, submitOnChange: true, 
      options: [
          
         
          "Dewpoint",
          "Forecast Conditions",
          "Forecast High",
          "Forecast Low",
          "Illuminance",
          "Rain in Last Hour",
          "Rain Today",
          "Rain Tomorrow",
          "Rain The Day After Tomorrow",
          "Pressure",
          "Solar Radiation",
          "Sunrise",
          "Sunset",
          "Temperature Feels Like",
          "UV Radiation",
          "UV Harm Index",
          "Wind Direction",
          "Wind Gust",
          "Wind Speed"
          
          ]       
          
          
          
      state.selection = trigger    
              
        if(state.selection == "Wind Direction"){
     input(name: "actionMatch", type: "test", title: "Wind direction to match", required: true, description: "MUST match case & direction e.g W or NW") 
     input(name: "action1", type: "bool", title: "Turn switch On or Off when direction matches", required: true, defaultValue: true)   
        }
        
        else if(state.selection == "Forecast Conditions"){
     input(name: "actionMatch", type: "test", title: "Condition to match", required: true, description: "MUST match case & output e.g RAIN, CLOUDY") 
     input(name: "action1", type: "bool", title: "Turn switch On or Off when condition matches", required: true, defaultValue: true)   
        }
        
         else if(state.selection == "UV Harm Index"){
     input(name: "actionMatch", type: "enum", title: "Action when this condition matches", required: true, options: ["Low", "Moderate", "High", "VeryHigh", "Extreme"])   
     input(name: "action1", type: "bool", title: "Turn switch On or Off when condition matches", required: true, defaultValue: true)        
        }   
        
        else{
     input(name: "threshold1", type: "number", title: "Threshold", required: true, description: "Trigger above or below this number", defaultValue: '0')
   	 input(name: "switchMode1", type: "bool", title: "On = Trigger Above Threshold - Off = Trigger Below Threshold", required: true, defaultValue: true )      
     input(name: "action1", type: "bool", title: "Turn switch On or Off when trigger active", required: true, defaultValue: true)         
        }
     
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
    signOff()
	state.enablecurrS1 = 'on'
        
        
	subscribe(enableswitch1, "switch", enableSwitch1Handler)
    subscribe(sensorSwitch1, "switch", sensorSwitch1Handler)
    subscribe(sensor1, "DriverAuthor", checkNameHandler)
    subscribe(sensor1, "DriverVersion", checkDriverVerHandler)
    subscribe(sensor1, "LastUpdate-Weewx", observation_timeHandler)
    subscribe(sensor1, "LastUpdate-External", observation_timeHandler1) 
    subscribe(sensor1, "city", cityHandler)
    subscribe(sensor1, "state", stateHandler)
    subscribe(sensor1, "country", countryHandler)
    subscribe(sensor1, "WeewxServerLocation", stationHandler)
    subscribe(sensor1, "WeewxServerUptime", stationHandler1)
    
    

   
    if (restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if (restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
        
    state.selection = trigger
    
    if(state.selection == "Illuminance"){ subscribe(sensor1, "illuminance", illuminanceHandler)}
    if(state.selection ==  "Solar Radiation"){subscribe(sensor1, "solarradiation", solarradiationHandler)}
    if(state.selection == "Temperature Feels Like"){subscribe(sensor1, "feelsLike", feelsLikeHandler)}
    if(state.selection == "Rain in Last Hour"){subscribe(sensor1, "precip_1hr", precip_1hrHandler)}
    if(state.selection == "Rain Today"){subscribe(sensor1, "precip_today", precip_todayHandler)}
    if(state.selection == "Wind Speed"){subscribe(sensor1, "wind", windHandler)}
    if(state.selection == "Wind Gust"){subscribe(sensor1, "wind_gust", wind_gustHandler)}
    if(state.selection == "Wind Direction"){subscribe(sensor1, "wind_dir", wind_dirHandler)}
    if(state.selection == "Pressure"){subscribe(sensor1, "pressure", pressureHandler)}
    if(state.selection ==  "Dewpoint"){subscribe(sensor1, "dewpoint", dewpointHandler)}
    if(state.selection == "UV Radiation"){subscribe(sensor1, "uv", uvHandler)}
    if(state.selection == "UV Harm Index"){subscribe(sensor1, "uvHarm", uvHarmHandler)}
    if(state.selection == "Visibility"){subscribe(sensor1, "visibility", visibilityHandler)}
    if(state.selection == "Forecast High"){subscribe(sensor1, "forecastHigh", forecastHighHandler)}
    if(state.selection == "Forecast Low"){subscribe(sensor1, "forecastLow", forecastLowHandler)}
// 	if(state.selection == "Chance Of Rain"){subscribe(sensor1, "chanceOfRain", rainHandler)}
    if(state.selection == "Rain Tomorrow"){subscribe(sensor1, "rainTomorrow", rainTomorrowHandler)}
  	if(state.selection == "Rain The Day After Tomorrow"){subscribe(sensor1, "rainDayAfterTomorrow", rainDayAfterTomorrowHandler)}
    if(state.selection == "Forecast Conditions"){subscribe(sensor1, "weatherForecast", forecastConditionsHandler)}
    if(state.selection == "Sunrise"){subscribe(sensor1, "localSunrise", sunriseHandler)}
    if(state.selection == "Sunset"){subscribe(sensor1, "localSunset", sunsetHandler)}
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
    if(state.reqNameSpace == drivername){LOGDEBUG("You are using a Cobra weather driver ")} 
    else {log.warn "*** Warning! *** You are not using Cobra's driver - Results may vary from expected  "} 
}

def checkDriverVerHandler(evt){
    def driverversion = evt.value
  
    if(state.reqdriverversion == driverversion){LOGDEBUG("Driver version number:OK")}
    else{log.warn "*** Warning! *** - Driver version number does not match requirement for this app - Results may vary from expected  "} 
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
 	actionNow(call1, evt1)
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
LOGDEBUG("Weewx Observation Time is $state.observe1")
}

def observation_timeHandler1(evt){
state.observe2 = evt.value
LOGDEBUG("External Observation Time is $state.observe2")
}

def weatherHandler(evt){
state.weather1 = evt.value
LOGDEBUG("Weather is $state.weather1")
}

def  alertHandler(evt){
state.alert1 = evt.value
LOGDEBUG("Weather Alert: $state.alert1")    
}

def  cityHandler(evt){
state.city1 = evt.value
LOGDEBUG("City is $state.city1")    
}

def  stateHandler(evt){
state.state1 = evt.value
LOGDEBUG("State is $state.state1")    
}

def  countryHandler(evt){
state.country1 = evt.value
LOGDEBUG("Country is $state.country1")    
}

def  stationHandler(evt){
state.station1 = evt.value
LOGDEBUG("Weewx Server Location is $state.station1")    
}

def  stationHandler1(evt){
state.station2 = evt.value
LOGDEBUG("Weewx Server Uptime is $state.station2")    
}

def checkPollHandler(evt){
state.pollCount1 = evt.value
LOGDEBUG("Poll Count from driver is $state.pollCount1")    
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
    def evt4 = event4.
        
    def call4 = 'Precipitation in last hour'
	LOGDEBUG("Precipitation in last hour is $evt4")
    actionNow(call4, evt4)
}

def precip_todayHandler(evt){
    def event5 = evt.value
    def evt5 = event5.toDouble()
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
    actionNow(call9, evt9)
}

def uvHarmHandler(evt){
    def evt21 = evt.value
    def call21 = 'UV Harm Index'
    LOGDEBUG("UVHarm =  $evt21")
    altSwitch(call21, evt21)
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
    def evt13 = event13
    def call13 = 'Wind Direction'
    LOGDEBUG("Wind Direction =  $evt13")
    altSwitch(call13, evt13)
}

def wind_gustHandler(evt){
  	def event14 = evt.value
    def evt14 = event14.toDouble()
    def call14 = 'Wind Gust'
    LOGDEBUG("Wind Direction =  $evt14")
    actionNow(call14, evt14)   
}   
    
def rainHandler(evt){
    def removePercent = (evt.value.minus('%')) 
    def event15 = removePercent
    def evt15 = event15.toDouble()
    def call15 = 'Chance Of Rain'
    LOGDEBUG("Chance Of Rain =  $evt15 %")
    actionNow(call15, evt15)
}

def rainDayAfterTomorrowHandler(evt){
LOGDEBUG(" Rain day after tomorrow is $evt.value")
    def event16 = evt.value
    def evt16 = event16.toDouble()
    def call16 = 'Rain The Day After Tomorrow'
    LOGDEBUG("Rain The Day After Tomorrow =  $evt16 %")
    actionNow(call16, evt16)     
}

def rainTomorrowHandler(evt){
LOGDEBUG("Rain tomorrow is $evt.value")
      def event17 = evt.value
    def evt17 = event17.toDouble()
    def call17 = 'Rain Tomorrow'
    LOGDEBUG("Rain Tomorrow =  $evt17 %")
    actionNow(call17, evt17)     
}


def forecastConditionsHandler(evt){
LOGDEBUG("Forecast Conditions =  $evt.value")
    def event18 = evt.value
    def evt18 = event18
    def call18 = 'Forecast Conditions'
    LOGDEBUG("Forecast Conditions =  $evt18")
    altSwitch(call18, evt18)
}

def sunriseHandler(evt){
LOGDEBUG("Sunrise =  $evt.value")
    def event19 = evt.value
    def evt19 = event19.toString()
    def call19 = 'Sunrise'
    LOGDEBUG("Sunrise =  $evt19")
    altSwitch(call19, evt19)
}

def sunsetHandler(evt){
LOGDEBUG("Sunset =  $evt.value")
    def event20 = evt.value
    def evt20 = event20.toString()
    def call20 = 'Sunset'
    LOGDEBUG("Sunset =  $evt20")
    altSwitch(call20, evt20)
}         










def signOff(){
LOGDEBUG("Observation Time: $state.observe1 - City: $state.city1 - Station: $state.station1")   
    
    
}

// Switch Actions

def altSwitch(call, evt){
 LOGDEBUG("Calling altSwitch")   
state.evtNow = evt.value.toString()
   LOGDEBUG(" state.evtNow = $state.evtNow")
state.match = actionMatch.toString()
    LOGDEBUG(" state.match = $state.match")
state.action = action1

   LOGDEBUG("Calling event: $call") 
LOGDEBUG("Calling.. CheckTime")
checkTime()
LOGDEBUG("Calling.. CheckDay")
checkDay()
LOGDEBUG("Calling.. CheckPresence")
checkPresence()
LOGDEBUG("Calling.. CheckPresence1")
checkPresence1()   
    
    
    LOGDEBUG("state.evtNow = $state.evtNow -- state.match = $state.match -- state.action = $state.action -- state.presenceRestriction = $state.presenceRestriction -- state.presenceRestriction1 = $state.presenceRestriction1")
 if(state.timeOK == true && state.dayCheck == true && state.presenceRestriction == true && state.presenceRestriction1 == true){

      if(state.action == true){   
        LOGDEBUG("state.action = $state.action")
         
    if(state.evtNow == state.match){ 
       LOGDEBUG("Content Match - Sent on command..")  
        on()}
          if(state.evtNow != state.match){
        LOGDEBUG("Content Not Matched - Sent off command..")       
        off()}
                                                       
	
    }
    
    if(state.action == false){ 
        LOGDEBUG("state.action = $state.action")
        
	if(state.evtNow != state.match){
        LOGDEBUG("Content Match - Sent off command..")  
        off()}
	if(state.evtNow == state.match){ 
        LOGDEBUG("Content Not Matched - Sent on command..")  
        on()}
	
    }
     

 }  
    
}    
    
def actionNow(call, evt){
    LOGDEBUG("Calling actionNow")
state.evtNow = evt.value as int
state.thresh = threshold1
state.action = action1
state.mode = switchMode1
state.callNow = call.value
   LOGDEBUG("Calling event: $state.callNow") 
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
    state.appversion = "2.0.1.172"
    state.reqdriverversion = "1.7.2" // required driver version for this app
    state.reqNameSpace = "Cobra"   // check to confirm Cobra's driver is being used
   
}