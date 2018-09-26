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
 *  Last Update: 30/08/2018
 *
 *  Changes:
 *
 *
 *
 *  V2.5.0 - Added additional triggers: 'Temperature', 'Max Temperature Today', 'Min Temperature Today', 'Max Inside Temp Today', 'Min Inside Temp Today', 'Chance of Rain'
 *  V2.4.4 - Debug & added uninstall logging
 *  V2.4.3 - Changed input of threshold to allow decimal inputs
 *  V2.4.2 - Fixed typo in precip_1hrHandler
 *  V2.4.1 - Debug
 *  V2.4.0 - Added 'rain_rate' as a trigger
 *  V2.3.0 - Added remote version checking
 *  V2.2.2 - Debug - state variable not clearing
 *  V2.2.1 - Debug - Typo in switch name
 *  V2.2.0 - Changed method so on/off command is only sent once (not repeatedly)
 *  V2.1.1 - Debug
 *  V2.1.0 - Dropped driver requirement checking & added remote version checking
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
    description: "Turns On/Off a switch based upon weather events",
    category: "",
    parent: "Cobra:Weather Switch",
    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")


preferences {
     page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
     page name: "restrictionsPage", title: "", install: true, uninstall: true
}
    
    
// main page *****************************************************************************************************************
  def mainPage() {
    dynamicPage(name: "mainPage") {  
        
       display()
        
        
	section() {
    input(name: "enableswitch1", type: "capability.switch", title: "Enable/Disable app with this switch", required: false, multiple: false)
	input(name: "sensor1", type: "capability.sensor", title: "Weather Device", required: true, multiple: false)
    input(name: "sensorswitch1", type: "capability.switch", title: "Switch to control", required: false, multiple: false)
       
	}
    section() {
  input "trigger", "enum", title: "Action to trigger switch", required: true, submitOnChange: true, 
      options: [
          
  
          "Chance of Rain",
          "Dewpoint",
          "Forecast Conditions",
          "Forecast High",
          "Forecast Low",
          "Humidity ",
          "Illuminance",
          "Rain Rate",
          "Rain in Last Hour",
          "Rain Today",
          "Rain Tomorrow",
          "Rain The Day After Tomorrow",
          "Pressure",
          "Solar Radiation",
          "Sunrise",
          "Sunset",
          "Temperature",
          "Max Temperature Today",
          "Min Temperature Today",
          "Temperature Feels Like",
          "Max Inside Temperature Today",
          "Min Inside Temperature Today",
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
         else if(state.selection == "Sunrise"){
     input(name: "actionMatch", type: "time", title: "Action when this time matches", required: true)   
     input(name: "action1", type: "bool", title: "Turn switch On or Off when condition matches", required: true, defaultValue: true)        
        }  
         else if(state.selection == "Sunset"){
     input(name: "actionMatch", type: "time", title: "Action when this time matches", required: true)   
     input(name: "action1", type: "bool", title: "Turn switch On or Off when condition matches", required: true, defaultValue: true)        
        }   
        
        else{
     input(name: "threshold1", type: "decimal", title: "Threshold", required: true, description: "Trigger above or below this number", defaultValue: '0')
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
                label title: "Enter a name for this automation", required: false
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

def uninstalled(){
   log.info "Child app uninstalled: ${app.label}" 
}

def initialize() {
	log.info "Initialised with settings: ${settings}"
    logCheck()
	version()
  	state.enablecurrS1 = 'on'
    state.already = 'off'   
        
	subscribe(enableswitch1, "switch", enableSwitch1Handler)
    subscribe(sensorswitch1, "switch", sensorSwitch1Handler)
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
    if(state.selection == "Humidity"){ subscribe(sensor1, "humidity", humidityHandler)}
    if(state.selection == "Solar Radiation"){subscribe(sensor1, "solarradiation", solarradiationHandler)}
    if(state.selection == "Temperature"){subscribe(sensor1, "temperature", temperatureHandler)}
    if(state.selection == "Max Temperature Today"){subscribe(sensor1, "tempMaxToday", maxTempTodayHandler)}
    if(state.selection == "Min Temperature Today"){subscribe(sensor1, "tempMinToday", minTempTodayHandler)}
    if(state.selection == "Max Inside Temperature Today"){subscribe(sensor1, "tempMaxInsideToday", maxTempInsideTodayHandler)}
    if(state.selection == "Min Inside Temperature Today"){subscribe(sensor1, "tempMinInsideToday", minTempInsideTodayHandler)}
    if(state.selection == "Temperature Feels Like"){subscribe(sensor1, "feelsLike", feelsLikeHandler)}
    if(state.selection == "Rain in Last Hour"){subscribe(sensor1, "precip_1hr", precip_1hrHandler)}
    if(state.selection == "Rain Rate"){subscribe(sensor1, "rain_rate", precip_RateHandler)}    
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
    if(state.selection == "Chance Of Rain"){subscribe(sensor1, "chanceOfRain", rainHandler)}
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




          
def sensorSwitch1Handler(evt){
state.currS1 = evt.value
LOGDEBUG("$sensorswitch1 is $state.currS1")
    
    if(state.currS1 == "on"){
        state.already = 'on'
    }
    
     if(state.currS1 == "off"){
        state.already = 'off'
    }  
    
    
    
    
    
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
    def evt4 = event4.toDouble()
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

def precip_RateHandler(evt){
    def event21 = evt.value
    def evt21 = event21.toDouble()
    def call21 = 'Precipitation Rate'
	LOGDEBUG("Precipitation Rate is $evt21")
    actionNow(call21, evt21)
}

def temperatureHandler(evt){
 def event22 = evt.value
    def evt22 = event22.toDouble()
    def call22 = 'Temperature'
	LOGDEBUG("Temperature is $evt22")
    actionNow(call22, evt22)
} 

def maxTempTodayHandler(evt){
 def event23 = evt.value
    def evt23 = event23.toDouble()
    def call23 = 'Max Temperature Today'
	LOGDEBUG("Max Temperature Today is $evt23")
    actionNow(call23, evt23)
}    

def minTempTodayHandler(evt){
 def event24 = evt.value
    def evt24 = event24.toDouble()
    def call24 = 'Min Temperature Today'
	LOGDEBUG("Min Temperature Today is $evt24")
    actionNow(call24, evt24)
}    

def maxTempInsideTodayHandler(evt){
 def event25 = evt.value
    def evt25 = event25.toDouble()
    def call25 = 'Max Temperature Inside Today'
	LOGDEBUG("Max Temperature InsideToday is $evt25")
    actionNow(call25, evt25)
}       

def minTempInsideTodayHandler(evt){
 def event26 = evt.value
    def evt26 = event26.toDouble()
    def call26 = 'Min Temperature Inside Today'
	LOGDEBUG("Min Temperature InsideToday is $evt26")
    actionNow(call26, evt26)
}       

def humidityHandler(evt){
 def event27 = evt.value
    def evt27 = event27.toDouble()
    def call27 = 'Humidity'
	LOGDEBUG("Humidity is $evt27")
    actionNow(call27, evt27)
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
    if(state.already == 'off'){
    if(state.enablecurrS1 == 'on'){ 
    	LOGDEBUG("Turning on switch...")   
    	sensorswitch1.on()  
        state.already = 'on'
    }
   
    if(state.enablecurrS1 == 'off'){ 
    	LOGDEBUG("Cannot switch on - App disabled...") 
    }
    }
    else{ LOGDEBUG("Cannot switch on - Already On")}
}


def off(){
    if(state.already == 'on'){
    LOGDEBUG("Turning off switch...")   
    	sensorswitch1.off()      
       state.already = 'off'
    }
     else{ LOGDEBUG("Cannot switch off - Already Off")}
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



def version(){
	unschedule()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
}

def display(){
	if(state.status){
	section{paragraph "Version: $state.version -  $state.Copyright"}
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
     state.version = "2.5.0"
     state.InternalName = "WSchild"
     
 

}

