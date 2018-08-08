/**
 *  Weewx Weather Driver - With External Forecasting
 *
 *  Copyright 2018 Andrew Parker
 *
 *  This driver was originally born from an idea by @mattw01 and @Jhoke and I thank them for that!
 *  
 *  This driver is specifically designed to be used with 'Weewx' and your own PWS
 *  It also has the capability to collect forecast data from an external source (once you have an api key)
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
 *  Last Update 08/08/2018
 *
 *  V2.0.0 - Debug & Revised version checking
 *  v1.9.0 - Made external source selectable (for those who have the relevant api key)
 *  V1.8.0 - Added remote version checking & cleaned up code
 *  V1.7.2 - Debug illumination & solarradiation calc errors
 *  V1.7.1 - Added additional logging to help debug
 *  V1.7.0 - Added 'Poll Inside' - This sends internal temperature and humidity as 'standard' external data (for use with Rule Machine)
 *  V1.6.0 - Added more error checking for PWS that don't send all data
 *  V1.5.0 - Code cleanup & removed commented out test code
 *  V1.4.0 - Disabled OWM data (not much useful data)
 *  V1.3.0 - Added Open Weather Map as an external source (limited details)
 *  V1.2.0 - Renamed some attributes for 'Dashboard' compliance - Started work on additional external source
 *  V1.1.1 - Debug 'UV Harm' indication
 *  V1.1.0 - Made ALL logging switchable
 *  V1.0.0 - Original POC
 *
 */




metadata {
    definition (name: "Weewx Weather Driver - With External Forecasting", namespace: "Cobra", author: "Andrew Parker") {
        capability "Actuator"
        capability "Sensor"
        capability "Temperature Measurement"
        capability "Illuminance Measurement"
        capability "Relative Humidity Measurement"
        command "PollStation"
		command "PollExternal"
        command "PollInside"
        

		
        
// Base Info        
        attribute "DriverAuthor", "string"
        attribute "DriverVersion", "string"
        attribute "DriverStatus", "string"
        attribute "DriverUpdate", "string" 
        attribute "WeewxUptime", "string"
        attribute "WeewxLocation", "string"
        attribute "Refresh-Weewx", "string"
        
// Units
        attribute "distanceUnit", "string"
        attribute "pressureUnit", "string"
        attribute "rainUnit", "string"
        attribute "summaryFormat", "string"
        
// Collected Local Station Data       
        attribute "solarradiation", "string"
        attribute "dewpoint", "string"
        attribute "inside_humidity", "string"
        attribute "inside_temperature", "string"
        attribute "pressure", "string"
        attribute "pressure_trend", "string"
        attribute "wind", "string"
        attribute "wind_gust", "string"
        attribute "wind_dir", "string"
        attribute "rain_rate", "string"
        attribute "uv", "string"
        attribute "uvHarm", "string"
        attribute "feelsLike", "string"
        attribute "LastUpdate-Weewx", "string"
        attribute "precip_1hr", "string"
        attribute "precip_today", "string"
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "moonPhase", "string"
        attribute "moonRise", "string"
        
        
// External Data (if used)
        attribute "LastUpdate-External", "string"
        attribute "Refresh-External", "string"
        attribute "visibility", "string"
        attribute "forecastHigh", "string"
        attribute "forecastLow", "string"
        attribute "city", "string"
        attribute "state", "string"
        attribute "country", "string"
        attribute "weather", "string"
        attribute "rainTomorrow", "string"
        attribute "rainDayAfterTomorrow", "string"
        attribute "weatherIcon", "string"
        attribute "weatherForecast", "string"
        attribute "visibility", "string"
       attribute "chanceOfRain", "string"
        
     
     
    
 


     
        
    }
    preferences() {
    
        section("Query Inputs"){
            input "ipaddress", "text", required: true, title: "Weewx Server IP/URI", defaultValue: "0.0.0.0"
            input "weewxPort", "text", required: true, title: "Connection Port", defaultValue: "800"
            input "weewxPath", "text", required: true, title: "path to file", defaultValue: "weewx/daily.json"
            input "unitSet", "bool", title: "Display Data Units", required: true, defaultValue: false
            input "logSet", "bool", title: "Log All Data", required: true, defaultValue: false
            input "pollInterval", "enum", title: "Weewx Station Poll Interval", required: true, defaultValue: "5 Minutes", options: ["Manual Poll Only", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "pressureUnit", "enum", title: "Pressure Unit", required:true, defaultValue: "INHg", options: ["INHg", "MBAR"]
            input "rainUnit", "enum", title: "Rain Unit", required:true, defaultValue: "IN", options: ["IN", "MM"]
            input "speedUnit", "enum", title: "Distance & Speed Unit", required:true, defaultValue: "Miles (MPH)", options: ["Miles (MPH)", "Kilometers (KPH)"]
            input "temperatureUnit", "enum", title: "Temperature Unit", required:true, defaultValue: "Fahrenheit (°F)", options: ["Fahrenheit (°F)", "Celsius (°C)"]
            input "decimalUnit", "enum", title: "Max Decimal Places", required:true, defaultValue: "2", options: ["1", "2", "3", "4", "5"]
            input "extSource", "enum", title: "Select External Source", required:true, defaultValue: "None", options: ["None", "Apixu", "Weather Underground" ]// , "Open Weather Map"
                if(extSource != "None"){
                input "apiKey", "text", required: true, title: "API Key"
                input "pollLocation1", "text", required: true, title: "ZIP Code or Location"
                input "pollInterval1", "enum", title: "External Source Poll Interval", required: true, defaultValue: "3 Hours", options: ["Manual Poll Only", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
                }
                
              
        }
    }
}

def updated() {
    log.debug "Updated called"
    unschedule()
    logCheck()
    version()
    units()
    
    PollStation()
    if(extSource == "Apixu"){ PollApixuNow()}
    if(extSource == "Weather Underground"){ PollWUNow()}                        
    def pollIntervalCmd = (settings?.pollInterval ?: "3 Hours").replace(" ", "")
    
    
    if(pollInterval == "Manual Poll Only"){LOGINFO( "MANUAL POLLING ONLY")}
    else{ "runEvery${pollIntervalCmd}"(pollSchedule)}
    
    def pollIntervalCmd1 = (settings?.pollInterval1 ?: "3 Hours").replace(" ", "")
    
    if(pollInterval1 == "Manual Poll Only"){LOGINFO( "MANUAL POLLING ONLY")}
    else{"runEvery${pollIntervalCmd1}"(pollSchedule1)}
    

}


def units(){
    state.SRU = " watts"
    state.IU = " watts"
 	state.HU = " %" 
    
    state.DecimalPlaces = decimalUnit.toInteger()
    state.DisplayUnits = unitSet
}

def PollExternal(){
 if(extSource == "Apixu"){ PollApixuNow()}                                
 if(extSource == "Weather Underground"){ PollWUNow()}                               
 }



// Get WU data (If selected) *******************************************************
                            


def PollWUNow(){
    units()
 LOGDEBUG("Weather Underground: Poll called")
    def params2 = [
        uri: "http://api.wunderground.com/api/${apiKey}/alerts/astronomy/conditions/forecast/q/${pollLocation1}.json"
    ]
    

    try {
        httpGet(params2) { resp2 ->
            resp2.headers.each {
            LOGINFO("Response2: ${it.name} : ${it.value}")
        }
            if(logSet == true){  
           
            LOGINFO( "params2: ${params2}")
            LOGINFO( "response contentType: ${resp2.contentType}")
 		    LOGINFO( "response data: ${resp2.data}")
            } 
            if(logSet == false){ 
            log.info "Further detailed 'External Source' data logging disabled"    
            }    
            
            
             // WU No Units ********************
            

            
           
			sendEvent(name: "weather", value: resp2.data.current_observation.weather, isStateChange: true)
            sendEvent(name: "city", value: resp2.data.current_observation.display_location.city, isStateChange: true)            
			sendEvent(name: "state", value: resp2.data.current_observation.display_location.state, isStateChange: true) 
			sendEvent(name: "chanceOfRain", value: resp2.data.forecast.simpleforecast.forecastday[0].pop + "%", isStateChange: true)           
			sendEvent(name: "weather", value: resp2.data.current_observation.weather, isStateChange: true)    
			sendEvent(name: "LastUpdate-External", value: resp2.data.current_observation.observation_time, isStateChange: true) 
            sendEvent(name: "weatherForecast", value: resp2.data.forecast.simpleforecast.forecastday[0].conditions, isStateChange: true)            
			sendEvent(name: "Refresh-External", value: pollInterval1, isStateChange: true)             
			sendEvent(name: "country", value: resp2.data.current_observation.display_location.country, isStateChange: true) // resp2.data.city.country, isStateChange: true)
            sendEvent(name: "weatherIcon", value: resp2.data.current_observation.icon, isStateChange: true)  // Current Conditions Icon
            // sendEvent(name: "weatherIcon", value: resp2.data.forecast.simpleforecast.forecastday[0].icon, isStateChange: true) // Forecast Icon
            
            
            
            
            
              
  			  
           	 

     
        
            
                 
            
            
            
            
            
            
            
    // WU With Units ***************************************************************
            
          if(state.DisplayUnits == true){
            if(rainUnit == "IN"){    
           sendEvent(name: "rainTomorrow", value: resp2.data.forecast.simpleforecast.forecastday[1].qpf_allday.in +" " +state.RU, isStateChange: true)
 		   sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.simpleforecast.forecastday[2].qpf_allday.in +" " +state.RU, isStateChange: true)
           }   
            if(rainUnit == "MM"){   
 			sendEvent(name: "rainTomorrow", value: resp2.data.forecast.simpleforecast.forecastday[1].qpf_allday.mm +" " +state.RU, isStateChange: true)
            sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.simpleforecast.forecastday[2].qpf_allday.mm +" " +state.RU, isStateChange: true)
            }
            if(temperatureUnit == "Fahrenheit (°F)"){  
            sendEvent(name: "forecastHigh", value: resp2.data.forecast.simpleforecast.forecastday[0].high.fahrenheit +" " +state.TU, isStateChange: true)
            sendEvent(name: "forecastLow", value: resp2.data.forecast.simpleforecast.forecastday[0].low.fahrenheit +" " +state.TU, isStateChange: true)
            }  
            if(temperatureUnit == "Celsius (°C)"){  
 			sendEvent(name: "forecastHigh", value: resp2.data.forecast.simpleforecast.forecastday[0].high.celsius +" " +state.TU, isStateChange: true)
            sendEvent(name: "forecastLow", value: resp2.data.forecast.simpleforecast.forecastday[0].low.celsius +" " +state.TU, isStateChange: true)
            } 
             if(speedUnit == "Miles (MPH)"){  
            sendEvent(name: "visibility", value: resp2.data.current_observation.visibility_mi + " mi", isStateChange: true)
            }
            if(speedUnit == "Kilometers (KPH)"){
            sendEvent(name: "visibility", value: resp2.data.current_observation.visibility_km + " km", isStateChange: true)
            }    
              
            

 }      
              
     // WU Without Units ***************************************************************
          if(state.DisplayUnits == false){
         
                       
           if(rainUnit == "IN"){    
           sendEvent(name: "rainTomorrow", value: resp2.data.forecast.simpleforecast.forecastday[1].qpf_allday.in, unit: "IN", isStateChange: true)
 		   sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.simpleforecast.forecastday[2].qpf_allday.in, unit: "IN", isStateChange: true)
           }   
            if(rainUnit == "MM"){   
 			sendEvent(name: "rainTomorrow", value: resp2.data.forecast.simpleforecast.forecastday[1].qpf_allday.mm, unit: "MM", isStateChange: true)
            sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.simpleforecast.forecastday[2].qpf_allday.mm, unit: "MM", isStateChange: true)
            }
            if(temperatureUnit == "Fahrenheit (°F)"){  
            sendEvent(name: "forecastHigh", value: resp2.data.forecast.simpleforecast.forecastday[0].high.fahrenheit, unit: "F", isStateChange: true)
            sendEvent(name: "forecastLow", value: resp2.data.forecast.simpleforecast.forecastday[0].low.fahrenheit, unit: "F", isStateChange: true)
            }
            if(temperatureUnit == "Celsius (°C)"){  
 			sendEvent(name: "forecastHigh", value: resp2.data.forecast.simpleforecast.forecastday[0].high.celsius, unit: "C", isStateChange: true)
            sendEvent(name: "forecastLow", value: resp2.data.forecast.simpleforecast.forecastday[0].low.celsius, unit: "C", isStateChange: true)
            } 
            if(speedUnit == "Miles (MPH)"){  
            sendEvent(name: "visibility", value: resp2.data.current_observation.visibility_mi, unit: "mi", isStateChange: true)  
            }
            if(speedUnit == "Kilometers (KPH)"){
            sendEvent(name: "visibility", value: resp2.data.current_observation.visibility_km, unit: "km", isStateChange: true)  
            }              
              
              
}

       } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}
// END: Get WU data *******************************************************       





// Get APIXU data (If selected) *******************************************************
                            


def PollApixuNow(){
    units()
 LOGDEBUG("Apixu: Poll called")
    def params2 = [
          
          uri: "http://api.apixu.com/v1/forecast.json?key=${apiKey}&q=${pollLocation1}&days=3"
    ]
    

    try {
        httpGet(params2) { resp2 ->
            resp2.headers.each {
            LOGINFO("Response2: ${it.name} : ${it.value}")
        }
            if(logSet == true){  
           
            LOGINFO( "params2: ${params2}")
            LOGINFO( "response contentType: ${resp2.contentType}")
 		    LOGINFO( "response data: ${resp2.data}")
            } 
            if(logSet == false){ 
        //    log.info "Further detailed 'External Source' data logging disabled"    
            }    
            
    
            
            // Apixu No Units ********************

              sendEvent(name: "weather", value: resp2.data.current.condition.text, isStateChange: true)
              sendEvent(name: "weatherForecast", value: resp2.data.forecast.forecastday.day[1].condition.text, isStateChange: true)
              sendEvent(name: "city", value: resp2.data.location.name, isStateChange: true)
              sendEvent(name: "state", value: resp2.data.location.region, isStateChange: true)
              sendEvent(name: "country", value: resp2.data.location.country, isStateChange: true)
              sendEvent(name: "LastUpdate-External", value: resp2.data.current.last_updated, isStateChange: true)  
              sendEvent(name: "Refresh-External", value: pollInterval1, isStateChange: true) 
              sendEvent(name: "weatherIcon", value: resp2.data.current.condition.icon, isStateChange: true)  // Current Conditions Icon
      //      sendEvent(name: "weatherIcon", value: resp2.data.forecast.forecastday.day[1].condition.icon, isStateChange: true)  // Forecast Icon
                    
    // Apixu With Units ***************************************************************
            
          if(state.DisplayUnits == true){
                       
           if(rainUnit == "IN"){
           sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_in +" " +state.RU, isStateChange: true)
           sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_in +state.RU, isStateChange: true)
           }
          
           if(rainUnit == "MM"){ 
           sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_mm +" " +state.RU, isStateChange: true)
           sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_mm +" "  +state.RU, isStateChange: true)
           }
            
           if(temperatureUnit == "Celsius (°C)"){
           sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_c +" " +state.TU, isStateChange: true)
           sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_c +" " +state.TU, isStateChange: true)              
          }

          if(temperatureUnit == "Fahrenheit (°F)"){ 
          sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_f +" " +state.TU, isStateChange: true)
   	      sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_f +" " +state.TU, isStateChange: true)
           } 
              
         if(speedUnit == "Miles (MPH)"){  
          sendEvent(name: "visibility", value: resp2.data.current.vis_miles + " mi", isStateChange: true)
          }  
            
          if(speedUnit == "Kilometers (KPH)"){
          sendEvent(name: "visibility", value: resp2.data.current.vis_km + " km", isStateChange: true)
          }    

 }      
              
     // Apixu Without Units ***************************************************************
          if(state.DisplayUnits == false){
              
           if(rainUnit == "IN"){
           sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_in, unit:"in", isStateChange: true)
           sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_in, unit:"in", isStateChange: true) 
           }    
               
                  
          if(rainUnit == "MM"){  
          sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_mm, unit:"mm", isStateChange: true)
          sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_mm, unit:"mm", isStateChange: true)
           }    

            
          if(temperatureUnit == "Celsius (°C)"){
          sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_c, unit:"C", isStateChange: true)
          sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_c, unit:"C", isStateChange: true)
          }
              
          if(temperatureUnit == "Fahrenheit (°F)"){ 
          sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_f, unit:"F", isStateChange: true)
          sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_f, unit:"F", isStateChange: true)
          }   
           
          if(speedUnit == "Miles (MPH)"){  
          sendEvent(name: "visibility", value: resp2.data.current.vis_miles, unit: "mi", isStateChange: true)
          }  
            
          if(speedUnit == "Kilometers (KPH)"){
          sendEvent(name: "visibility", value: resp2.data.current.vis_km, unit: "km", isStateChange: true)
          }
}

       } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}
// END: Get APIXU data *******************************************************       





def pollSchedule1()
{
    if(extSource == "Apixu"){ PollApixuNow()}
    if(extSource == "Weather Underground"){ PollWUNow()}
}


def pollSchedule()
{
    PollStation()
}
              
def parse(String description) {
}

       

def PollStation()
{
    units()
  
    
 
    LOGDEBUG("Weewx: ForcePoll called")
    def params1 = [
        uri: "http://${ipaddress}:${weewxPort}/${weewxPath}"
         ]
    
    try {
        httpGet(params1) { resp1 ->
            resp1.headers.each {
            LOGINFO( "Response1: ${it.name} : ${it.value}")
        }
            if(logSet == true){  
           
            LOGINFO( "params1: ${params1}")
            LOGINFO( "response contentType: ${resp1.contentType}")
 		    LOGINFO( "response data: ${resp1.data}")
            } 
            
            
            if(logSet == false){ 
      //      log.info "Further Weewx detailed data logging disabled"    
            }    

            
// Collect Data
           
 // ************************ ILLUMINANCE **************************************************************************************           
           LOGINFO("Checking illuminance")    
            def illuminanceRaw = (resp1.data.stats.current.solarRadiation)  
                if(illuminanceRaw == null || illuminanceRaw.contains("N/A")){
                	state.Illuminance = 'No Station Data'
                }   
            	else{
                	def illuminanceRaw1 = (resp1.data.stats.current.solarRadiation.replaceFirst(wmcode, ""))
                	state.Illuminance = illuminanceRaw1.toFloat()
                }
            
// ************************* SOLAR RADIATION*****************************************************************************************           
            	LOGINFO("Checking SolarRadiation")
              def solarradiationRaw = (resp1.data.stats.current.solarRadiation)
            	if(solarradiationRaw == null || solarradiationRaw.contains("N/A")){
                  	state.SolarRadiation = 'No Station Data'
                }
            	else{
                     def solarradiationRaw1 = (resp1.data.stats.current.solarRadiation.replaceFirst(wmcode, ""))
                     state.SolarRadiation = solarradiationRaw1.toFloat()
                }
            
// ************************** HUMIDITY ***************************************************************************************   
         LOGINFO("Checking Humidity")
              def humidityRaw = (resp1.data.stats.current.humidity)
            	if(humidityRaw == null || humidityRaw.contains("N/A")){
                state.Humidity = 'No Station Data'
                }
            	else{
                   def humidityRaw1 = (resp1.data.stats.current.humidity.replaceFirst("%", ""))
                   state.Humidity = humidityRaw1
                }

// ************************** INSIDE HUMIDITY ************************************************************************************
            LOGINFO("Checking Inside Humidity")
              def inHumidRaw1 = (resp1.data.stats.current.insideHumidity.replaceFirst("%", "")) 
            	if(inHumidRaw1 ==null || inHumidRaw1.contains("N/A")){
                   
                	state.InsideHumidity = 'No Station Data'}
            	else{
                    
                	state.InsideHumidity = inHumidRaw1
                }
                        
            
// ************************* DEWPOINT *****************************************************************************************
            LOGINFO("Checking Dewpoint")
                def dewpointRaw1 = (resp1.data.stats.current.dewpoint)
                 	if(dewpointRaw1 == null || dewpointRaw1.contains("N/A")){
                    state.Dewpoint = 'No Station Data'}
            
            	if (dewpointRaw1.contains("F")) {
                dewpointRaw1 = dewpointRaw1.replace(fcode, "")
                    
                if(temperatureUnit == "Fahrenheit (°F)"){
            	state.TU = '°F'
                state.Dewpoint = dewpointRaw1
                LOGINFO("Dewpoint Input = F - Output = F -- No conversion required")
                }    
                if(temperatureUnit == "Celsius (°C)"){
                state.TU = '°C'
                def dewpoint1 = convertFtoC(dewpointRaw1) 
                state.Dewpoint = dewpoint1 
                   
                }    

            } 
            
           		if (dewpointRaw1.contains("C")) {
                dewpointRaw1 = dewpointRaw1.replace(ccode, "")
                    
                 if(temperatureUnit == "Fahrenheit (°F)"){
            	state.TU = '°F'
                def dewpoint1 = convertCtoF(dewpointRaw1)    
                state.Dewpoint = dewpoint1 
                }    
                 if(temperatureUnit == "Celsius (°C)"){
                state.TU = '°C'
                state.Dewpoint = dewpointRaw1
                 LOGINFO("Dewpoint Input = C - Output = C -- No conversion required" ) 
                }        

            } 
            
            
            
            
            

// ************************** PRESSURE ****************************************************************************************            
           LOGINFO("Checking Pressure")
              def pressureRaw1 = (resp1.data.stats.current.barometer)
                    if (insideTemperatureRaw1 == null || pressureRaw1.contains("N/A")){
                    state.Pressure = 'No Station Data'}
            
            if (pressureRaw1.contains("inHg")) {
                pressureRaw1 = pressureRaw1.replace("inHg", "")
                
                if(pressureUnit == "INHg"){
            	state.PU = 'inhg'
                state.Pressure = pressureRaw1
                LOGINFO("Pressure Input = INHg - Output = INHg -- No conversion required")
                }
                
                if(pressureUnit == "MBAR"){
                state.PU = 'mbar'
                def pressureTemp1 = convertINtoMB(pressureRaw1) 
                state.Pressure = pressureTemp1 
                
                }
                
            } 
            
            if (pressureRaw1.contains("mbar")) {
                 pressureRaw1 = pressureRaw1.replace("mbar", "")
                
            	if(pressureUnit == "INHg"){
            	state.PU = 'inhg'
                def pressureTemp1 = convertMBtoIN(pressureRaw1)
                state.Pressure = pressureTemp1
                }
                 if(pressureUnit == "MBAR"){
                 state.PU = 'mbar'
                 state.Pressure = pressureRaw1 
                 LOGINFO( "Pressure Input = MBAR - Output = MBAR --No conversion required")
                }
                
            } 
            
            
         
            
// ************************** WIND SPEED ****************************************************************************************
            LOGINFO("Checking Wind speed")
    		  def windSpeedRaw1 = (resp1.data.stats.current.windSpeed) 
            if(windSpeedRaw1 == null || windSpeedRaw1.contains("N/A")){
                    state.WindSpeed = 'No Station Data'}
            
            if (windSpeedRaw1.contains("mph")) {
                windSpeedRaw1 = windSpeedRaw1.replace("mph", "")
                
                if(speedUnit == "Miles (MPH)"){
            	state.SU = 'mph'
                state.WindSpeed = windSpeedRaw1
                LOGINFO("Wind Speed Input = MPH - Output = MPH -- No conversion required")
                }
                
                if(speedUnit == "Kilometers (KPH)"){
                state.SU = 'kph'
                def speedTemp1 = convertMPHtoKPH(windSpeedRaw1) 
                state.WindSpeed = speedTemp1 
            
                }
                
            } 
            
            if (windSpeedRaw1.contains("kph")) {
                 windSpeedRaw1 = windSpeedRaw1.replace("kph", "")
                
            	if(speedUnit == "Miles (MPH)"){
            	state.SU = 'mph'
                def speedTemp1 = convertKPHtoMPH(pressureRaw1)
                state.WindSpeed = speedTemp1
                }
                 if(speedUnit == "Kilometers (KPH)"){
                 state.SU = 'kph'
                 state.WindSpeed = windSpeedRaw1 
                 LOGINFO("WindSpeed Input = KPH - Output = KPH --No conversion required")
                }
                
            } 
            
                   
// ************************** WIND GUST ****************************************************************************************
            LOGINFO("Checking Wind Gust")
              def windGustRaw1 = (resp1.data.stats.current.windGust)  
            	 if(windGustRaw1 == null || windGustRaw1.contains("N/A")){
                    state.WindGust = 'No Station Data'}
            
            if (windGustRaw1.contains("mph")) {
                windGustRaw1 = windGustRaw1.replace("mph", "")
                
                if(speedUnit == "Miles (MPH)"){
            	state.SU = 'mph'
                state.WindGust = windGustRaw1
                LOGINFO( "Wind Gust Speed Input = MPH - Output = MPH -- No conversion required")
                }
                
                if(speedUnit == "Kilometers (KPH)"){
                state.SU = 'kph'
                def speedTemp2 = convertMPHtoKPH(windGustRaw1) 
                state.WindGust = speedTemp2 
            
                }
                
            } 
            
            if (windGustRaw1.contains("kph")) {
                 windGustRaw1 = windGustRaw1.replace("kph", "")
                
            	if(speedUnit == "Miles (MPH)"){
            	state.SU = 'mph'
                def speedTemp2 = convertKPHtoMPH(windGustRaw1)
                state.WindGust = speedTemp2
                }
                 if(speedUnit == "Kilometers (KPH)"){
                 state.SU = 'kph'
                 state.WindGust = windGustRaw1 
                LOGINFO( "Wind Gust Speed Input = KPH - Output = KPH --No conversion required")
                }
                
            } 
            
// ************************** INSIDE TEMP **************************************************************************************** 
          LOGINFO("Checking Inside Temperature")
              def insideTemperatureRaw1 = (resp1.data.stats.current.insideTemp)
                    if (insideTemperatureRaw1 == null || insideTemperatureRaw1.contains("N/A")){
                    state.InsideTemp = 'No Station Data'}
            
            if (insideTemperatureRaw1.contains("F")) {
                insideTemperatureRaw1 = insideTemperatureRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (°F)"){
            	state.TU = '°F'
                state.InsideTemp = insideTemperatureRaw1
                LOGINFO("InsideTemperature Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (°C)"){
                state.TU = '°C'
                def insideTemp1 = convertFtoC(insideTemperatureRaw1) 
                state.InsideTemp = insideTemp1 
                
                }
                
            } 
            
            if (insideTemperatureRaw1.contains("C")) {
                insideTemperatureRaw1 = insideTemperatureRaw1.replace(ccode, "")
                
            	if(temperatureUnit == "Fahrenheit (°F)"){
            	state.TU = '°F'
                def insideTemp1 = convertCtoF(insideTemperatureRaw1)
                state.InsideTemp = insideTemp1
                }
                if(temperatureUnit == "Celsius (°C)"){
                state.TU = '°C'
                state.InsideTemp = insideTemperatureRaw1  
                LOGINFO( "InsideTemperature Input = C - Output = C --No conversion required")
                }
                
            } 
  
// ************************** RAIN RATE ****************************************************************************************    
            LOGINFO("Checking Rain Rate")
            
            def rainRateRaw1 = (resp1.data.stats.current.rainRate) 
            	if(rainRateRaw1 == null || rainRateRaw1.contains("N/A")){
                   state.Rainrate = 'No Station Data'}
            	
            if(rainRateRaw1.contains("in/hr")){
                rainRateRaw1 = rainRateRaw1.replace("in/hr", "")
                
                if(rainUnit == "IN"){
                    state.RRU = " in/hr"
                 	state.Rainrate = rainRateRaw1  
                    LOGINFO( "Rainrate Input = in/hr - Output = in/hr --No conversion required")
                }
            
            	if(rainUnit == "MM"){
            		state.RRU = "mm/hr"
                    rrTemp = convertINtoMM(rainRateRaw1)
                    state.Rainrate = rrTemp
            }
            }
            
             if(rainRateRaw1.contains("mm/hr")){
                rainRateRaw1 = rainRateRaw1.replace("mm/hr", "")
                0.621371
                if(rainUnit == "IN"){
                    state.RRU = "in/hr"
                    rrTemp = convertMMtoIN(rainRateRaw1)
                 	state.Rainrate = rrTemp 
                }
            
            	if(rainUnit == "MM"){
            		state.RRU = " mm/hr"
                   state.Rainrate = rainRateRaw1 
                   LOGINFO( "Rainrate Input = mm/hr - Output = mm/hr --No conversion required")
            }
            }
            

// ************************** RAIN TODAY ****************************************************************************************    
            LOGINFO("Checking Rain Today")
              def rainTodayRaw1 = (resp1.data.stats.sinceMidnight.rainSum)
               	if(rainTodayRaw1 == null || rainTodayRaw1.contains("N/A")){
                   state.RainToday = 'No Station Data'}
            	
            if(rainTodayRaw1.contains("in")){
                rainTodayRaw1 = rainTodayRaw1.replace("in", "")
                
                if(rainUnit == "IN"){
                    state.RU = "in"
                 	state.RainToday = rainTodayRaw1 
                    LOGINFO( "RainToday Input = in - Output = in --No conversion required")
                }
            
            	if(rainUnit == "MM"){
            		state.RU = "mm"
                    rtTemp = convertINtoMM(rainTodayRaw1)
                    state.RainToday = rtTemp
            }
            }
            
             if(rainTodayRaw1.contains("mm")){
                rainTodayRaw1 = rainTodayRaw1.replace("mm", "")
                
                if(rainUnit == "IN"){
                    state.RU = "in"
                    rtTemp = convertMMtoIN(rainTodayRaw1)
                 	state.RainToday = rtTemp 
                }
            
            	if(rainUnit == "MM"){
            		state.RU = "mm"
                   state.RainToday = rainTodayRaw1 
                  LOGINFO("RainToday Input = mm - Output = mm --No conversion required")
            }
            }


// ************************** TEMPERATURE ****************************************************************************************
            LOGINFO("Checking Temperature")
              def temperatureRaw1 = (resp1.data.stats.current.outTemp) 
            	if(temperatureRaw1 ==null || temperatureRaw1.contains("N/A")){
                state.Temperature = 'No Station Data'}
            
            if (temperatureRaw1.contains("F")) {
                temperatureRaw1 = temperatureRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (°F)"){
            	state.TU = '°F'
                state.Temperature = temperatureRaw1
                LOGINFO("Temperature Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (°C)"){
                state.TU = '°C'
                def temp1 = convertFtoC(temperatureRaw1) 
                state.Temperature = temp1 
                
                }
                
            } 
            
            if (temperatureRaw1.contains("C")) {
                temperatureRaw1 = temperatureRaw1.replace(ccode, "")
                
            	if(temperatureUnit == "Fahrenheit (°F)"){
            	state.TU = '°F'
                    def temp1 = convertCtoF(temperatureRaw1)
                state.Temperature = temp1
                }
                if(temperatureUnit == "Celsius (°C)"){
                state.TU = '°C'
                state.Temperature = temperatureRaw1  
                 LOGINFO("Temperature Input = C - Output = C --No conversion required")
                }
                
            } 
           
                    
                    
                    
                    
                    
                    
                    
  
            
// ************************** UV ************************************************************************************************            
            LOGINFO("Checking UV")
              def UVRaw1 = (resp1.data.stats.current.UV)
            	if(UVRaw1 ==null || UVRaw1.contains("N/A")){
                   
                	state.UV = 'No Station Data'}
            	else{
                    state.UV = UVRaw1
   
                    
 // Calculate UV likelyhood of causing harm to someone *****************************************************     
                    
                    
                    LOGINFO ( "state.UV -- $state.UV")
                    if(state.UV <= '0.1'){
                        state.UVHarm = 'Zero'
                        LOGINFO  ("UV Zero -- $state.UV")
                    }
                    if(state.UV >= '0.2' && state.UV <= '2.9'){
                        state.UVHarm = 'Low'
                         LOGINFO  ( "UV Low -- $state.UV")
                    }
                    if(state.UV >= '3.0' && state.UV <= '5.9'){
                        state.UVHarm = 'Moderate'
                        LOGINFO  ( "UV Moderate -- $state.UV")
                    }
            		if(state.UV >= '6.0' && state.UV <= '7.9'){
                        state.UVHarm = 'High'
                         LOGINFO  ( "UV High -- $state.UV")
                    }
 					if(state.UV >= '8.0' && state.UV <= '9.8'){
                        state.UVHarm = 'VeryHigh'
                        LOGINFO  ( "UV VeryHigh -- $state.UV")
                    }
					if(state.UV >= "9.99"){
                        state.UVHarm = 'Extreme'
                         LOGINFO  ( "UV Extreme -- $state.UV")
                    }



                } 
            
            
            
            
            

// ************************** WINDCHILL ****************************************************************************************            
            LOGINFO("Checking WindChill")
              def windChillRaw1 = (resp1.data.stats.current.windchill)
            	if(windChillRaw1 ==null || windChillRaw1.contains("N/A")){
                   state.FeelsLike = 'No Station Data'}
                	
            
 				if (windChillRaw1.contains("F")) {
                windChillRaw1 = windChillRaw1.replace(fcode, "")
                
                if(temperatureUnit == "Fahrenheit (°F)"){
            	state.TU = '°F'
                state.FeelsLike = windChillRaw1
                LOGINFO( "FeelsLike Input = F - Output = F -- No conversion required")
                }
                
                if(temperatureUnit == "Celsius (°C)"){
                state.TU = '°C'
                def feelslike1 = convertFtoC(windChillRaw1) 
                state.FeelsLike = feelslike1
                
                }
                
            } 
            
            if (windChillRaw1.contains("C")) {
                windChillRaw1 = windChillRaw1.replace(ccode, "")
                             
            	if(temperatureUnit == "Fahrenheit (°F)"){
            	state.TU = '°F'
                def feelslike1 = convertCtoF(windChillRaw1)
                state.FeelsLike = feelslike1
                }
                if(temperatureUnit == "Celsius (°C)"){
                state.TU = '°C'
                state.FeelsLike = windChillRaw1 
                 LOGINFO( "FeelsLike Input = C - Output = C --No conversion required")
                }
                
            } 
           
// ************************** WIND DIR ****************************************************************************************                     
            def windDirRaw = (resp1.data.stats.current.windDirText)
            	if(windDirRaw != null){
                    if(windDirRaw.contains("N/A")){sendEvent(name: "wind_dir", value:"No Station Data", isStateChange: true)}
                    else {sendEvent(name: "wind_dir", value: windDirRaw, isStateChange: true)} 
                
                }                    
                    
            

// ************************** PRESSURE TREND ************************************************************************************             
             def pressureTrend = (resp1.data.stats.current.barometerTrendData) 
                  if(pressureTrend != null){
                      if(pressureTrend.contains("N/A")){sendEvent(name: "pressure_trend", value:"No Station Data", isStateChange: true)}
                      else if(pressureTrend.contains("-")){sendEvent(name: "pressure_trend", value:"Falling", isStateChange: true)} 
                      else if(pressureTrend.contains("+")){sendEvent(name: "pressure_trend", value:"Rising", isStateChange: true)} 
                      else {sendEvent(name: "pressure_trend", value:"Static", isStateChange: true)} 
                  }
            
            
             //  any more?
                 
             		
            
      
        
    
            
 // Basics - No units ************************************************************************************************
            
             
             sendEvent(name: "WeewxUptime", value: resp1.data.serverUptime, isStateChange: true)
             sendEvent(name: "WeewxLocation", value: resp1.data.location, isStateChange: true)
             sendEvent(name: "Refresh-Weewx", value: pollInterval, isStateChange: true)
             sendEvent(name: "localSunrise", value: resp1.data.almanac.sun.sunrise, isStateChange: true)
             sendEvent(name: "localSunset", value: resp1.data.almanac.sun.sunset, isStateChange: true)
             sendEvent(name: "moonPhase", value: resp1.data.almanac.moon.phase, isStateChange: true)
             sendEvent(name: "moonRise", value: resp1.data.almanac.moon.rise, isStateChange: true)
             sendEvent(name: "uv", value: state.UV, isStateChange: true)
             sendEvent(name: "uvHarm", value: state.UVHarm, isStateChange: true)
             sendEvent(name: "LastUpdate-Weewx", value: resp1.data.time, isStateChange: true)
            
            
            
            
            
                      
            

         

            
            
            
            
// // Send Events  - WITH UNITS ********************************************************************************************            
              if(state.DisplayUnits == true){  
                         
                  sendEvent(name: "illuminance", value: state.Illuminance +" " +state.IU, isStateChange: true)
                  sendEvent(name: "solarradiation", value: state.SolarRadiation +" " +state.SRU, isStateChange: true)
                  sendEvent(name: "dewpoint", value: state.Dewpoint +" " +state.TU, isStateChange: true)
                  sendEvent(name: "humidity", value: state.Humidity +" " +state.HU, isStateChange: true)
                  sendEvent(name: "pressure", value: state.Pressure +" " +state.PU, isStateChange: true) 
                  sendEvent(name: "wind", value: state.WindSpeed +" " +state.SU, isStateChange: true)
                  sendEvent(name: "wind_gust", value: state.WindGust +" " +state.SU, isStateChange: true)
                  sendEvent(name: "inside_temperature", value: state.InsideTemp +" " +state.TU, isStateChange: true)
                  sendEvent(name: "inside_humidity", value: state.InsideHumidity +" " +state.HU, isStateChange: true)  
                  sendEvent(name: "temperature", value: state.Temperature +" " +state.TU, isStateChange: true)
                  sendEvent(name: "rain_rate", value: state.Rainrate +" " +state.RRU, isStateChange: true)
                  sendEvent(name: "precip_today", value: state.RainToday +" " +state.RU, isStateChange: true) 
                  sendEvent(name: "precip_1hr", value: state.Rainrate +" " +state.RU, isStateChange: true)
                  sendEvent(name: "feelsLike", value: state.FeelsLike +" " +state.TU, isStateChange: true)
                  
                  
    
                  
              }
            
// // Send Events  - WITHOUT UNITS ****************************************************************************************
            
            if(state.DisplayUnits == false){
                
                  sendEvent(name: "illuminance", value: state.Illuminance, unit: "lux", isStateChange: true)    
                  sendEvent(name: "solarradiation", value: state.SolarRadiation, unit: "lux", isStateChange: true)
                  sendEvent(name: "dewpoint", value: state.Dewpoint, isStateChange: true)
                  sendEvent(name: "humidity", value: state.Humidity, isStateChange: true)
                  sendEvent(name: "pressure", value: state.Pressure, isStateChange: true)
                  sendEvent(name: "wind", value: state.WindSpeed , isStateChange: true)
                  sendEvent(name: "wind_gust", value: state.WindGust, isStateChange: true)
                  sendEvent(name: "inside_temperature", value: state.InsideTemp, isStateChange: true)
                  sendEvent(name: "inside_humidity", value: state.InsideHumidity, isStateChange: true)   
                  sendEvent(name: "temperature", value: state.Temperature, isStateChange: true)
                  sendEvent(name: "rain_rate", value: state.Rainrate, isStateChange: true)  
                  sendEvent(name: "precip_today", value: state.RainToday, isStateChange: true)  
                  sendEvent(name: "precip_1hr", value: state.Rainrate, isStateChange: true)  
                  sendEvent(name: "feelsLike", value: state.FeelsLike, isStateChange: true) 
            

        }
            
// **********************************************************************************************            
  
   
   } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}


def PollInside(){
    LOGINFO( "Polling internal temperature and humidity and sending it as 'standard' temperature & humidity data" )   
state.Temperature = state.InsideTemp
state.Humidity = state.InsideHumidity
    
    if(state.DisplayUnits == true){ 
		sendEvent(name: "humidity", value: state.Humidity +" " +state.HU, isStateChange: true)
		sendEvent(name: "temperature", value: state.Temperature +" " +state.TU, isStateChange: true)
    }
   
    if(state.DisplayUnits == false){ 
		sendEvent(name: "humidity", value: state.Humidity, isStateChange: true)
		sendEvent(name: "temperature", value: state.Temperature, isStateChange: true)
    }
    
}


def getFcode(){
     def charF1 ="&-#-1-7-6-;-F"
     def charF = charF1.replace("-", "")
return charF
}

def getCcode(){
    def charC1 ="&-#-1-7-6-;-C"
    def charC = charC1.replace("-", "")
return charC
}

def getWmcode(){
     def wm1 ="W-/-m-&-#-1-7-8;"
    def wm = wm1.replace("-", "")
return wm
}

def convertFtoC(temperatureIn){
    LOGDEBUG( "Converting F to C")
    def tempIn = temperatureIn.toFloat()
    LOGDEBUG("tempIn = $tempIn")
    def tempCalc = ((tempIn - 32) *0.5556)  
    def tempOut1 = tempCalc.round(state.DecimalPlaces)
    def tempOut = tempOut1
    LOGDEBUG( "tempOut =  $tempOut")
	return tempOut
            }
            
            
def convertCtoF(temperatureIn){
    LOGDEBUG( "Converting C to F")
    def tempIn = temperatureIn.toFloat()
    LOGDEBUG( "tempIn = $tempIn")
    def tempCalc = ((tempIn * 1.8) + 32)  
    def tempOut1 = tempCalc.round(state.DecimalPlaces)
    def tempOut = tempOut1
    LOGDEBUG( "tempOut =  $tempOut")
	return tempOut
            }   

 
def convertINtoMM(unitIn){
      LOGDEBUG( "Converting IN to MM" )           
      def tempIn1 = unitIn.toFloat()           
     LOGDEBUG( "tempIn1 = $tempIn1" )
    def tempCalc1 = (tempIn1 * 25.4)
    def tempOut2 = tempCalc1.round(state.DecimalPlaces)
    def tempOut1 = tempOut2
    LOGDEBUG( "tempOut1 =  $tempOut1")
	return tempOut1              
               }

def convertMMtoIN(unitIn){
      LOGDEBUG( "Converting IN to MM" )            
      def tempIn1 = unitIn.toFloat()           
      LOGDEBUG( "tempIn1 = $tempIn1")
    def tempCalc1 = (tempIn1/25.4)
    def tempOut2 = tempCalc1.round(state.DecimalPlaces)
    def tempOut1 = tempOut2
    LOGDEBUG( "tempOut1 =  $tempOut1")
	return tempOut1              
               }               
               
               
               
def convertMBtoIN(pressureIn){
      LOGDEBUG( "Converting MBAR to INHg")           
     def pressIn1 = pressureIn.toFloat()           
      LOGDEBUG("Pressure In = $pressIn1") 
    def pressCalc1 = (pressIn1 * 0.02953)
    def pressOut2 = pressCalc1.round(state.DecimalPlaces)
    def pressOut1 = pressOut2
    LOGDEBUG( "Pressure Out =  $pressOut1")
	return pressOut1              
               }                              
               
def convertINtoMB(pressureIn){
      LOGDEBUG( "Converting INHg to MBAR" )            
      def pressIn1 = pressureIn.toFloat()           
      LOGDEBUG( "Pressure In = $pressIn1" )
    def pressCalc1 = (pressIn1 * 33.8638815)
    def pressOut2 = pressCalc1.round(state.DecimalPlaces)
    def pressOut1 = pressOut2
   LOGDEBUG( "Pressure Out =  $pressOut1")
	return pressOut1              
               }                                    
               
def convertMPHtoKPH(speed1In) {
  LOGDEBUG( "Converting MPH to KPH")            
      def speed1 = speed1In.toFloat()           
     LOGDEBUG( "Speed In = $speed1In")
    def speedCalc1 = (speed1In * 1.60934)
    def speedOut2 = speedCalc1.round(state.DecimalPlaces)
    def speedOut1 = speedOut2
    LOGDEBUG("Speed Out =  $pressOut1")
	return speedOut1              
               }                                    
               
   

def convertKPHtoMPH(speed1In) {
  LOGDEBUG("Converting KPH to MPH" )            
      def speed1 = speed1In.toFloat()           
  LOGDEBUG( "Speed In = $speed1In") 
    def speedCalc1 = (speed1In * 0.621371)
    def speedOut2 = speedCalc1.round(state.DecimalPlaces)
    def speedOut1 = speedOut2
  LOGDEBUG( "Speed Out =  $pressOut1")
	return speedOut1              
               }    


def convertKelvinToF(tempIn){
     LOGDEBUG("Converting Kelvin to F" )  
 def tempK = tempIn.toFloat()
     LOGDEBUG("Kelvin in = $tempK")
    def tempFahrenheitCalc = ((tempK * 9/5) - 459.67)
    def tempFahrenheit = tempFahrenheitCalc.round(state.DecimalPlaces)
     LOGDEBUG( "F out =  $tempFahrenheit")
	return tempFahrenheit
 }

def convertKelvinToC(tempIn){
       LOGDEBUG("Converting Kelvin to C" )  
 def tempK = tempIn.toFloat()
     LOGDEBUG("Kelvin in = $tempK")
    def tempCelsiusCalc = (tempK - 273.15)
    def tempCelsius = tempCelsiusCalc.round(state.DecimalPlaces)
     LOGDEBUG( "C out =  $tempCelsius")
	return tempCelsius 
    
}





// define debug action ***********************************
def logCheck(){
state.checkLog = logSet
if(state.checkLog == true){
log.info "All Logging Enabled"
}
else if(state.checkLog == false){
log.info "Further Logging Disabled"
}

}
def LOGDEBUG(txt){
    try {
    	if(state.checkLog == true){ log.debug("Weewx Driver - DEBUG:  ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}

def LOGINFO(txt){
    try {
    	if(state.checkLog == true){log.info("Weewx Driver - INFO:  ${txt}") }
    } catch(ex) {
    	log.error("LOGINFO unable to output requested data!")
    }
}



// Check Version   *********************************************************************************
def version(){
    updatecheck()
    if (state.Type == "Application"){schedule("0 0 9 ? * FRI *", updatecheck)}
    if (state.Type == "Driver"){schedule("0 0 8 ? * FRI *", updatecheck)}
}
    

    

def updatecheck(){
    
    setVersion()
  
    def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       try {
        httpGet(paramsUD) { respUD ->
//  log.info " Version Checking - Response Data: ${respUD.data}"   // Debug Code 
       def copyNow = (respUD.data.copyright)
       state.Copyright = copyNow
            def newver = (respUD.data.versions.(state.Type).(state.InternalName))
            def cobraVer = (respUD.data.versions.(state.Type).(state.InternalName).replace(".", ""))
       def cobraOld = state.version.replace(".", "")
      state.UpdateInfo = (respUD.data.versions.UpdateInfo.(state.Type).(state.InternalName)) 
           
      		if(cobraOld < cobraVer){
        	state.Status = "<b>New Version Available (Version: $newver)</b>"
        	log.warn "** There is a newer version of this $state.Type available  (Version: $newver) **"
        	log.warn "** $state.UpdateInfo **"
       } 
            if(cobraOld == cobraVer){ 
      		state.Status = "Current"
      		log.info "$state.Type is the current version"
       }
       
       }
        } 
        catch (e) {
        log.error "Something went wrong: $e"
    }
    
    checkInfo()
        
}        

def checkInfo(){
  if(state.Status == "Current"){
    state.UpdateInfo = "None"
    sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
    sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
    }
    else{
     sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
     sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
    }   
    
    
    
    
}

 

def setVersion(){
     state.version = "2.0.0"
     state.InternalName = "WeewxExternal"
     state.Type = "Driver"
   
    sendEvent(name: "DriverAuthor", value: "Cobra", isStateChange: true)
    sendEvent(name: "DriverVersion", value: state.version, isStateChange: true)
    sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
 
   
}














               
               
               
               
               
