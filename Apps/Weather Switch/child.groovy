/**
 *  Design Usage:
 *  This is the 'Child' app for weather switch
 *
 *
 *  Copyright 2019 Andrew Parker
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
 *  Last Update: 14/01/2019
 *
 *  Changes:
 *
 *  V3.2.0 - Added additonal (2nd) switch for restriction & fixed other restriction bugs
 *  V3.1.1 - Debug presence restriction
 *  V3.1.0 - Added 'Cloud Cover' (cloudiness) trigger
 *  V3.0.0 - Added disable apps code
 *  V2.9.0 - Streamlined restrictions page to action faster if specific restrictions not used.
 *  V2.8.0 - Moved update notification to child
 *  V2.7.0 - added restrictions page
 *  V2.6.2 - Slight UI changes
 *  V2.6.1 - Revised update checks
 *  V2.6.0 - Added trigger: Alert 
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
	section() {
	page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
	page name: "restrictionsPage", title: "", install: true, uninstall: true
	}
}

def mainPage() {
	dynamicPage(name: "mainPage") {
	preCheck()
	section() {
    input(name: "sensor1", type: "capability.sensor", title: "Weather Device", required: true, multiple: false)
    input(name: "sensorswitch1", type: "capability.switch", title: "Switch to control", required: false, multiple: false)
       
	}
    section() {
  input "trigger", "enum", title: "Action to trigger switch", required: true, submitOnChange: true, 
      options: [
          
  		  "Alert",
          "Chance of Rain",
		  "Cloud Cover",
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
        else if(state.selection == "Alert"){
     input(name: "actionMatch", type: "test", title: "EXACT phrase or word to match (case insensitive)", required: true, description: "e.g. severe wind, No current alerts") 
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

def restrictionsPage() {
    dynamicPage(name: "restrictionsPage") {
        section(){paragraph "<font size='+1'>App Restrictions</font> <br>These restrictions are optional <br>Any restriction you don't want to use, you can just leave blank or disabled"}
        section(){
		input "enableSwitchYes", "bool", title: "Enable restriction by external on/off switch(es)", required: true, defaultValue: false, submitOnChange: true
			if(enableSwitchYes){
			input "enableSwitch1", "capability.switch", title: "Select the first switch to Enable/Disable this app", required: false, multiple: false, submitOnChange: true 
			if(enableSwitch1){ input "enableSwitchMode1", "bool", title: "Allow app to run only when this switch is On or Off", required: true, defaultValue: false, submitOnChange: true}
			input "enableSwitch2", "capability.switch", title: "Select a second switch to Enable/Disable this app", required: false, multiple: false, submitOnChange: true 
			if(enableSwitch2){ input "enableSwitchMode2", "bool", title: "Allow app to run only when this switch is On or Off", required: true, defaultValue: false, submitOnChange: true}
			}
		}
        section(){
		input "modesYes", "bool", title: "Enable restriction by current mode(s)", required: true, defaultValue: false, submitOnChange: true	
			if(modesYes){	
			input(name:"modes", type: "mode", title: "Allow actions when current mode is:", multiple: true, required: false)
			}
		}	
       	section(){
		input "timeYes", "bool", title: "Enable restriction by time", required: true, defaultValue: false, submitOnChange: true	
			if(timeYes){	
    	input "fromTime", "time", title: "Allow actions from", required: false
    	input "toTime", "time", title: "Allow actions until", required: false
        	}
		}
		section(){
		input "dayYes", "bool", title: "Enable restriction by day(s)", required: true, defaultValue: false, submitOnChange: true	
			if(dayYes){	
    	input "days", "enum", title: "Allow actions only on these days of the week", required: false, multiple: true, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        	}
		}
		section(){
		input "presenceYes", "bool", title: "Enable restriction by presence sensor(s)", required: true, defaultValue: false, submitOnChange: true	
			if(presenceYes){	
    	input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor){input "restrictPresenceAction", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
     	input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor1){input "restrictPresenceAction1", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
   			}
		}	
		section(){
		input "sunrisesetYes", "bool", title: "Enable restriction by sunrise or sunset", required: true, defaultValue: false, submitOnChange: true	
			if(sunrisesetYes){
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
		}	
       
        section() {input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}
		 section() {label title: "Enter a name for this automation", required: false}
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
	if(enableSwitch1){subscribe(enableSwitch1, "switch", switchEnable1)}
	if(enableSwitch2){subscribe(enableSwitch2, "switch", switchEnable2)}
	if(enableSwitchMode == null){enableSwitchMode = true} // ????
	if(restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if(restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
	if(sunriseSunset){astroCheck()}
	if(sunriseSunset){schedule("0 1 0 1/1 * ? *", astroCheck)} // checks sunrise/sunset change at 00.01am every day
    
  // App Specific subscriptions & settings below here   
 


    
 
	
	subscribe(sensorswitch1, "switch", sensorSwitch1Handler)
    subscribe(sensor1, "LastUpdate-Weewx", observation_timeHandler)
    subscribe(sensor1, "LastUpdate-External", observation_timeHandler1) 
    subscribe(sensor1, "city", cityHandler)
    subscribe(sensor1, "state", stateHandler)
    subscribe(sensor1, "country", countryHandler)
    subscribe(sensor1, "WeewxServerLocation", stationHandler)
    subscribe(sensor1, "WeewxServerUptime", stationHandler1)
    state.already = 'off'
    state.selection = trigger
    if(state.selection == "Alert"){ subscribe(sensor1, "alert", alertHandler)}
	if(state.selection == "Cloud Cover"){ subscribe(sensor1, "cloudiness", cloudHandler)}
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
    if(state.selection == "Sunrise"){subscribe(sensor1, "localSunrise", sunriseHandler1)}
    if(state.selection == "Sunset"){subscribe(sensor1, "localSunset", sunsetHandler1)}
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

def sunriseHandler1(evt){
LOGDEBUG("Sunrise =  $evt.value")
    def event19 = evt.value
    def evt19 = event19.toString()
    def call19 = 'Sunrise'
    LOGDEBUG("Sunrise =  $evt19")
    altSwitch(call19, evt19)
}

def sunsetHandler1(evt){
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

def cloudHandler(evt){
	
	def event28 = evt.value
    def evt28 = event28.toDouble()
    def call28 = 'Cloudiness'
	LOGDEBUG("Cloudiness is $evt28")
    actionNow(call28, evt28)
}        




def  alertHandler(evt){ 
   
  LOGDEBUG("Calling alertHandler")  
   checkAllow()
	if(state.allAllow == true){
 
    
state.evtNow = evt.value.toString()
   LOGDEBUG(" state.evtNow = $state.evtNow")
state.match = actionMatch.toString()
    LOGDEBUG(" state.match = $state.match")
state.action = action1

 def msgComp = ""
    msgComp = state.evtNow.toUpperCase()
    msgMatch = state.match.toUpperCase()
    LOGDEBUG("msgComp = $msgComp -  msgMatch = $msgMatch")
     if (msgComp.toUpperCase().contains(msgMatch)) {
      state.yesMatch = true  
        LOGDEBUG("state.yesMatch = $state.yesMatch") 
     }   
     else{
     state.yesMatch = false  
       LOGDEBUG("state.yesMatch = $state.yesMatch") 
     }
    
    
     if(state.action == true){   
        LOGDEBUG("state.action = $state.action")
         
    if(state.yesMatch == true){ 
       LOGDEBUG("Content Match - Sent on command..")  
        on()}
          if(state.yesMatch == false){
        LOGDEBUG("Content Not Matched - Sent off command..")       
        off()}
                                                       
	
    }
      if(state.action == false){   
        LOGDEBUG("state.action = $state.action")
         
    if(state.yesMatch == false){ 
       LOGDEBUG("Content Match - Sent on command..")  
        on()}
          if(state.yesMatch == true){
        LOGDEBUG("Content Not Matched - Sent off command..")       
        off()}                                                 
	
    }
 }      
}








// Switch Actions







def altSwitch(call, evt){
 LOGDEBUG("Calling altSwitch") 
checkAllow()
	if(state.allAllow == true){
   
state.evtNow = evt.value.toString()
   LOGDEBUG(" state.evtNow = $state.evtNow")
state.match = actionMatch.toString()
    LOGDEBUG(" state.match = $state.match")
state.action = action1

   LOGDEBUG("Calling event: $call") 

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
checkAllow()
	if(state.allAllow == true){

state.evtNow = evt.value.toDouble()
state.thresh = threshold1.toDouble()
state.action = action1
state.mode = switchMode1
state.callNow = call.value
   LOGDEBUG("Calling event: $state.callNow - state.thresh = $state.thresh - state.evtNow = $state.evtNow") 
        
     if(state.action == true){   
        LOGDEBUG("state.action = $state.action")
         
    if(state.evtNow > state.thresh && state.mode == true){
		LOGDEBUG("state.evtNow > state.thresh && state.mode == true")
		on()}
	if(state.evtNow > state.thresh && state.mode == false){
		LOGDEBUG("state.evtNow > state.thresh && state.mode == false")
		off()}
	if(state.evtNow < state.thresh && state.mode == true){ 
		LOGDEBUG("state.evtNow < state.thresh && state.mode == true")
				 off()}
    if(state.evtNow < state.thresh && state.mode == false){
		LOGDEBUG("state.evtNow < state.thresh && state.mode == false")				 
				 on()}
    }
    
    if(state.action == false){ 
        LOGDEBUG("state.action = $state.action")
        
    if(state.evtNow > state.thresh && state.mode == true){ off()}
	if(state.evtNow > state.thresh && state.mode == false){ on()}
	if(state.evtNow < state.thresh && state.mode == true){ on()}
    if(state.evtNow < state.thresh && state.mode == false){ off()}
    }
     
    
    
 }
  
}


def on(){
	LOGDEBUG("Turning on switch...(Unless already on) ")
	if(state.already == 'off'){
    	LOGDEBUG("Turning on switch...")   
    	sensorswitch1.on()  
        state.already = 'on'
    }
   

    else{ LOGDEBUG("Cannot switch on - Already On")}
}



def off(){
	LOGDEBUG("Turning off switch... (Unless already off)")  
   if(state.already == 'on'){
    LOGDEBUG("Turning off switch...")   
    	sensorswitch1.off()      
       state.already = 'off'
   }
     else{ LOGDEBUG("Cannot switch off - Already Off")}
}
  
def checkAllow(){
    state.allAllow = false
    LOGDEBUG("Checking for any restrictions...")
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){
        LOGDEBUG("Continue - App NOT paused")
        state.noPause = true
		state.modeCheck = true
		state.presenceRestriction = true
		state.presenceRestriction1 = true
		state.dayCheck = true
		state.sunGoNow = true
		state.timeOK = true
		state.modes = modes
		state.fromTime = fromTime
		state.days = days
		state.sunriseSunset = sunriseSunset
		state.restrictPresenceSensor = restrictPresenceSensor
		state.restrictPresenceSensor1 = restrictPresenceSensor1
		state.timeYes = timeYes
		state.enableSwitchYes = enableSwitchYes
		state.modesYes = modesYes
		state.dayYes = dayYes
		state.sunrisesetYes = sunrisesetYes
		state.presenceYes = presenceYes
		
		if(state.enableSwitchYes == false){
		state.appgo1 = true
		state.appgo2 = true
		}
		if(state.modes != null && state.modesYes == true){modeCheck()}	
		if(state.fromTime !=null && state.timeYes == true){checkTime()}
		if(state.days!=null && state.dayYes == true){checkDay()}
		if(state.sunriseSunset !=null && state.sunrisesetYes == true){checkSun()}
		if(state.restrictPresenceSensor != null && state.presenceYes == true){checkPresence()}
        if(state.restrictPresenceSensor1 != null && state.presenceYes == true){checkPresence1()}
 
	if(state.modeCheck == false){
	LOGDEBUG("Not in correct 'mode' to continue")
	    }    
	if(state.presenceRestriction ==  false || state.presenceRestriction1 ==  false){
	LOGDEBUG( "Cannot continue - Presence failed")
	}
	if(state.appgo1 == false){
	LOGDEBUG("$enableSwitch1 is not in the correct position so cannot continue")
	}
	if(state.appgo2 == false){
	LOGDEBUG("$enableSwitch2 is not in the correct position so cannot continue")
	}
	if(state.appgo1 == true && state.appgo2 == true && state.dayCheck == true && state.presenceRestriction == true && state.presenceRestriction1 == true && state.modeCheck == true && state.timeOK == true && state.noPause == true && state.sunGoNow == true){
	state.allAllow = true 
 	  }
	else{
 	state.allAllow = false
	LOGWARN( "One or more restrictions apply - Unable to continue")
 	LOGDEBUG("state.appgo1 = $state.appgo1, state.appgo2 = $state.appgo2, state.dayCheck = $state.dayCheck, state.presenceRestriction = $state.presenceRestriction, state.presenceRestriction1 = $state.presenceRestriction1, state.modeCheck = $state.modeCheck, state.timeOK = $state.timeOK, state.noPause = $state.noPause, state.sunGoNow = $state.sunGoNow")
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

def switchEnable1(evt){
	state.enableInput1 = evt.value
	LOGDEBUG("Switch changed to: $state.enableInput")  
    if(enableSwitchMode1 == true && state.enableInput1 == 'off'){
	state.appgo1 = false
	LOGDEBUG("Cannot continue - App disabled by switch1")  
    }
	if(enableSwitchMode1 == true && state.enableInput1 == 'on'){
	state.appgo1 = true
	LOGDEBUG("Switch1 restriction is OK.. Continue...") 
    }    
	if(enableSwitchMode1 == false && state.enableInput1 == 'off'){
	state.appgo1 = true
	LOGDEBUG("Switch1 restriction is OK.. Continue...")  
    }
	if(enableSwitchMode1 == false && state.enableInput1 == 'on'){
	state.appgo1 = false
	LOGDEBUG("Cannot continue - App disabled by switch1")  
    }    
	LOGDEBUG("Allow by switch1 is $state.appgo1")
}

def switchEnable2(evt){
	state.enableInput2 = evt.value
	LOGDEBUG("Switch changed to: $state.enableInput")  
    if(enableSwitchMode2 == true && state.enableInput2 == 'off'){
	state.appgo2 = false
	LOGDEBUG("Cannot continue - App disabled by switch2")  
    }
	if(enableSwitchMode2 == true && state.enableInput2 == 'on'){
	state.appgo2 = true
	LOGDEBUG("Switch2 restriction is OK.. Continue...") 
    }    
	if(enableSwitchMode2 == false && state.enableInput2 == 'off'){
	state.appgo2 = true
	LOGDEBUG("Switch2 restriction is OK.. Continue...")  
    }
	if(enableSwitchMode2 == false && state.enableInput2 == 'on'){
	state.appgo2 = false
	LOGDEBUG("Cannot continue - App disabled by switch2")  
    }    
	LOGDEBUG("Allow by switch2 is $state.appgo2")
}





def version(){
	setDefaults()
	pauseOrNot()
	logCheck()
	resetBtnName()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
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
    if(state.status != "Current"){section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>Update URL: </b><font color = 'red'> $state.updateURI</font><hr>"}}
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


def stopAllChildren(disableChild, msg){
	state.disableornot = disableChild
	state.message1 = msg
	LOGDEBUG(" $state.message1 - Disable app = $state.disableornot")
	state.appgo = state.disableornot
	state.restrictRun = state.disableornot
	if(state.disableornot == true){
	unsubscribe()
//	unschedule()
	}
	if(state.disableornot == false){
	subscribeNow()}

	
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
            state.updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
            
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
		inform()
        
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}


def inform(){
	log.warn "An update is available - Telling the parent!"
	parent.childUpdate(true,state.updateMsg) 
}



def preCheck(){
	setVersion()
    state.appInstalled = app.getInstallationState()  
    if(state.appInstalled != 'COMPLETE'){
    section(){ paragraph "$state.preCheckMessage"}
    }
    if(state.appInstalled == 'COMPLETE'){
    display()   
 	}
}

def cobra(){
	log.warn "Previous schedule for old 'Cobra Update' found... Removing......"
	unschedule(cobra)
	log.info "Cleanup Complete!"
}


def setDefaults(){
    LOGDEBUG("Initialising defaults...")
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}
    if(enableSwitch1 == null){
    LOGDEBUG("Enable switch1 is NOT used.. Continue..")
    state.appgo1 = true
	}
	if(enableSwitch2 == null){
    LOGDEBUG("Enable switch2 is NOT used.. Continue..")
    state.appgo2 = true	
    }
	state.restrictRun = false
}







def setVersion(){
     state.version = "3.2.0"
     state.InternalName = "WeatherSwitchChild"
     state.ExternalName = "Weather Switch Child"
	 state.preCheckMessage = "This app is designed to turn On/Off a switch based upon weather events." 
     state.CobraAppCheck = "weatherswitch.json"
 

}

