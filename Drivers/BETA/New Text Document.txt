/**
 *  Weewx Weather Driver
 *
 *  Copyright 2018 @Cobra
 *
 *  This driver was originally born from an idea by @mattw01 and @Jhoke and I thank them for that!
 *  
 *  This driver is specifically designed to be used with 'Weewx' and your own PWS
 *  It also has the capability to collect forecast data from Apixu.com (once you have an api key)
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Last Update 01/06/2018
 *
 *
 *  V1.0.0 - Original POC
 *
 */




metadata {
    definition (name: "Weewx Weather Driver With Apixu- Beta1", namespace: "Cobra", author: "Andrew Parker") {
        capability "Actuator"
        capability "Sensor"
        capability "Temperature Measurement"
        capability "Illuminance Measurement"
        capability "Relative Humidity Measurement"
        command "PollStationNow"
		command "PollApixuNow"
        
// Base Info        
        attribute "Author", "string"
        attribute "DriverVersion", "string"
        attribute "WeewxServerUptime", "string"
        attribute "WeewxServerLocation", "string"
        
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
        attribute "feelsLike", "string"
        attribute "LastUpdate-Weewx", "string"
        attribute "precip_1hr", "string"
        attribute "precip_today", "string"
        attribute "sunrise", "string"
        attribute "sunset", "string"
        attribute "moonPhase", "string"
        attribute "moonRise", "string"
        
        
        // Apixu Data (if used)
        attribute "LastUpdate-Apixu", "string"
        attribute "visibility", "string"
        attribute "forecastHigh", "string"
        attribute "forecastLow", "string"
        attribute "city", "string"
        attribute "state", "string"
        attribute "country", "string"
        attribute "weatherCurrent", "string"
        attribute "rainTomorrow", "string"
        attribute "rainDayAfterTomorrow", "string"
        attribute "weatherIcon", "string"
        attribute "weatherForecast", "string"
        
        
       
       
       
       attribute "percentPrecip", "string"
       attribute "wind_string", "string"
       
       
        
       
       attribute "forecastConditions", "string"
       
   
      
        
        
        attribute "weatherSummary", "string"
        attribute "weatherSummaryFormat", "string"
        attribute "chanceOfRain", "string"
        
        

 


     
        
    }
    preferences() {
        section("Query Inputs"){
            input "ipaddress", "text", required: true, title: "Weewx Server IP/URI"
            input "weewxPort", "text", required: true, title: "Connection Port", defaultValue: "80"
            input "weewxPath", "text", required: true, title: "path to file", defaultValue: "weewx/daily.json"
            input "unitSet", "bool", title: "Display Data Units", required: true, defaultValue: true
            input "logSet", "bool", title: "Log All Response Data", required: true, defaultValue: false
           
            input "pollInterval", "enum", title: "Weewx Station Poll Interval", required: true, defaultValue: "5 Minutes", options: ["Manual Poll Only", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "pressureUnit", "enum", title: "Pressure Unit", required:true, defaultValue: "INHg", options: ["INHg", "MBAR"]
            input "rainUnit", "enum", title: "Rain Unit", required:true, defaultValue: "IN", options: ["IN", "MM"]
            input "rainRateUnit", "enum", title: "Rain Rate Unit", required:true, defaultValue: "IN/HR", options: ["IN/HR", "MM/HR"]
            input "speedUnit", "enum", title: "Wind Speed Unit", required:true, defaultValue: "MPH", options: ["MPH", "KPH"]
            input "temperatureUnit", "enum", title: "Temperature Unit", required:true, defaultValue: "Fahrenheit (°F)", options: ["Fahrenheit (°F)", "Celsius (°C)"]
            input "decimalUnit", "enum", title: "Max Decimal Places", required:true, defaultValue: "2", options: ["1", "2", "3", "4", "5"]
            input "addData", "bool", title: "Collect Additional Forecast Data From Apixu.com", required: true, defaultValue: false
            if (addData == true){
                input "apiKey", "text", required: true, title: "Apixu API Key"
                input "pollLocation1", "text", required: true, title: "ZIP Code or Location"
                input "markType", "bool", title: "Mark Apixu Data", required: true, defaultValue: false
                input "iconType", "bool", title: "Apixu Icon: On = Current - Off = Forecast", required: true, defaultValue: false
                input "pollInterval1", "enum", title: "Apixu Poll Interval", required: true, defaultValue: "3 Hours", options: ["Manual Poll Only", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
                
                
                
                
            }
                
            
            
            
     //       input "tempFormat", "enum", required: true, title: "Display Unit - Temperature: Fahrenheit or Celsius",  options: ["Fahrenheit", "Celsius"]
   //         input "distanceFormat", "enum", required: true, title: "Display Unit - Distance/Speed: Miles or Kilometres",  options: ["Miles (mph)", "Kilometres (kph)"]
   //         input "pressureFormat", "enum", required: true, title: "Display Unit - Pressure: Inches or Millibar",  options: ["Inches", "Millibar"]
   //         input "rainFormat", "enum", required: true, title: "Display Unit - Precipitation: Inches or Millimetres",  options: ["Inches", "Millimetres"]
   //         input "pollIntervalLimit", "number", title: "Poll Interval Limit:", required: true, defaultValue: 1
      //    input "autoPoll", "bool", required: false, title: "Enable Auto Poll"
     //       input "pollInterval", "enum", title: "Auto Poll Interval:", required: false, defaultValue: "5 Minutes", options: ["5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
          
    //        input "cutOff", "time", title: "New Day Starts", required: true
    //        input "summaryType", "bool", title: "Full Weather Summary", required: true, defaultValue: false
    //        input "iconType", "bool", title: "Icon: On = Current - Off = Forecast", required: true, defaultValue: false
   //         input "weatherFormat", "enum", required: true, title: "How to format weather summary",  options: ["Celsius, Miles & MPH", "Fahrenheit, Miles & MPH", "Celsius, Kilometres & KPH"]
        }
    }
}

def updated() {
    log.debug "updated called"
    unschedule()
    units()
    PollStationNow()
    PollApixuNow()
    state.unitsOn = unitSet
    def pollIntervalCmd = (settings?.pollInterval ?: "3 Hours").replace(" ", "")
    
    if(pollInterval == "Manual Poll Only"){
        log.info "MANUAL POLLING ONLY"}
    else{
        "runEvery${pollIntervalCmd}"(pollSchedule)}
    
    def pollIntervalCmd1 = (settings?.pollInterval ?: "3 Hours").replace(" ", "")
    
    if(pollInterval == "Manual Poll Only"){
        log.info "MANUAL POLLING ONLY"}
    else{
        "runEvery${pollIntervalCmd1}"(pollSchedule1)}
    

}


def units(){
    state.unitsOn = unitSet
    
    if(state.unitsOn == false){
     state.PU = ""   
     state.RU = ""
     state.RRU = ""   
     state.SU = ""
     state.TU = ""   
     state.SRU = ""
     state.IU = ""
 	 state.HU = ""   
        
    }
    
    else{
    
    if(pressureUnit == "INHg"){state.PU = " inHg"}
    if(pressureUnit == "MBAR"){state.PU = " mbar"}
    if(rainUnit == "IN"){state.RU = " in"}
    if(rainUnit == "MM"){state.RU = " mm"}
    if(speedUnit == "MPH"){state.SU = " mph"}
    if(speedUnit == "KPH"){state.SU = " kph"}
    if(temperatureUnit == "Fahrenheit (°F)"){state.TU = " °F"}
    if(temperatureUnit == "Celsius (°C)"){state.TU = " °C"}
    if(rainRateUnit == "IN/HR"){state.RRU = " in per hour"}
    if(rainRateUnit == "MM/HR"){state.RRU = " mm per hour"}
     state.SRU = " watts"
    state.IU = " watts"
 	state.HU = " %"   
        
    }    
        
        
    
    state.decimalPlaces = decimalUnit.toInteger()
    state.unitsOn = unitSet
    
    
}





// Get APIXU data *******************************************************



def PollApixuNow(){
    units()
 log.debug "Apixu: Poll called"
    def params2 = [
          
          uri: "http://api.apixu.com/v1/forecast.json?key=${apiKey}&q=${pollLocation1}&days=3"
    ]
    

    try {
        httpGet(params2) { resp2 ->
            resp2.headers.each {
            log.debug "Response2: ${it.name} : ${it.value}"
        }
            if(logSet == true){  
           
            log.debug "params2: ${params2}"
            log.debug "response contentType: ${resp2.contentType}"
 		    log.debug "response data: ${resp2.data}"
            } 
            if(logSet == false){ 
            log.info "Further detailed Apixu data logging disabled"    
            }    
            
    
            
            // Apixu No Units ********************
            if(markType == true){
              sendEvent(name: "weatherCurrent", value: resp2.data.current.condition.text + " (A)", isStateChange: true)
              sendEvent(name: "weatherForecast", value: resp2.data.forecast.forecastday.day[1].condition.text + " (A)", isStateChange: true)
              sendEvent(name: "city", value: resp2.data.location.name + " (A)", isStateChange: true)
              sendEvent(name: "state", value: resp2.data.location.region + " (A)", isStateChange: true)
              sendEvent(name: "country", value: resp2.data.location.country + " (A)", isStateChange: true)
              sendEvent(name: "LastUpdate-Apixu", value: resp2.data.current.last_updated + " (A)", isStateChange: true)      
            }
                
             if(markType == false){
              sendEvent(name: "weatherCurrent", value: resp2.data.current.condition.text, isStateChange: true)
              sendEvent(name: "weatherForecast", value: resp2.data.forecast.forecastday.day[1].condition.text, isStateChange: true)
              sendEvent(name: "city", value: resp2.data.location.name, isStateChange: true)
              sendEvent(name: "state", value: resp2.data.location.region, isStateChange: true)
              sendEvent(name: "country", value: resp2.data.location.country, isStateChange: true)
              sendEvent(name: "LastUpdate-Apixu", value: resp2.data.current.last_updated, isStateChange: true)    
              
            }   
                
                
            
            // Select Apixu Icon
                if(iconType == false){   
                sendEvent(name: "weatherIcon", value: resp2.data.forecast.forecastday.day[1].condition.icon + " (A)", isStateChange: true)
                }
                if(iconType == true){ 
		        sendEvent(name: "weatherIcon", value: resp2.data.current.condition.icon + " (A)", isStateChange: true)
                }    
         
            
    // Apixu With Units ***************************************************************
            
          if(state.unitsOn == true){
                       
           if(rainUnit == "IN"){
                
               if(markType == true){   
          sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_in +state.RU + " (A)", isStateChange: true)
          sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_in +state.RU + " (A)", isStateChange: true)
            }
               
               if(markType == false){   
          sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_in +state.RU, isStateChange: true)
          sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_in +state.RU, isStateChange: true)
            }   
           }
               
           
          if(rainUnit == "MM"){ 
              if(markType == true){ 
          sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_mm +state.RU + " (A)", isStateChange: true)
          sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_mm +state.RU + " (A)", isStateChange: true)
              }
               if(markType == false){ 
          sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_mm +state.RU, isStateChange: true)
          sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_mm +state.RU, isStateChange: true)
              }
           }
            
          if(temperatureUnit == "Celsius (°C)"){
      		   if(markType == true){ 
            sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_c +state.TU + " (A)", isStateChange: true)
            sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_c +state.TU + " (A)", isStateChange: true)
      }
      		   if(markType == false){ 
            sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_c +state.TU, isStateChange: true)
            sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_c +state.TU, isStateChange: true)              
           }
          }
              
          if(temperatureUnit == "Fahrenheit (°F)"){ 
              if(markType == true){ 
         sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_f +state.TU + " (A)", isStateChange: true)
   	     sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_f +state.TU + " (A)", isStateChange: true)
              }
              if(markType == false){ 
         sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_f +state.TU, isStateChange: true)
   	     sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_f +state.TU, isStateChange: true)
              }      
           }  
            
   //       if(distanceFormat == "Miles (mph)"){  
    //      sendEvent(name: "visibility", value: resp2.data.current.vis_miles +state.SU, isStateChange: true)
     //      }  
            
   //       if(distanceFormat == "Kilometres (kph)"){
    //      sendEvent(name: "visibility", value: resp2.data.current.vis_km +state.SU, isStateChange: true)
    //       }
              
          }      
              
     // Apixu Without Units ***************************************************************
          if(state.unitsOn == false){
              
           
           if(rainUnit == "IN"){
          sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_in, unit: "in", isStateChange: true)
          sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_in, unit: "in", isStateChange: true)  
           }
           
          if(rainUnit == "MM"){   
          sendEvent(name: "rainTomorrow", value: resp2.data.forecast.forecastday.day[1].totalprecip_mm, unit: "mm", isStateChange: true)
          sendEvent(name: "rainDayAfterTomorrow", value: resp2.data.forecast.forecastday.day[2].totalprecip_mm, unit: "mm", isStateChange: true)  
           }
            
          if(temperatureUnit == "Celsius (°C)"){
                        
          sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_c, unit: "C", isStateChange: true)
          sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_c, unit: "C", isStateChange: true)
           }
              
          if(temperatureUnit == "Fahrenheit (°F)"){ 
          sendEvent(name: "forecastHigh", value: resp2.data.forecast.forecastday.day[0].maxtemp_f, unit: "F", isStateChange: true)
           sendEvent(name: "forecastLow", value: resp2.data.forecast.forecastday.day[0].mintemp_f, unit: "F", isStateChange: true)
           }  
            
          if(distanceFormat == "Miles (mph)"){  
          sendEvent(name: "visibility", value: resp2.data.current.vis_miles, unit: "mi", isStateChange: true)
           }  
            
          if(distanceFormat == "Kilometres (kph)"){
          sendEvent(name: "visibility", value: resp2.data.current.vis_km, unit: "km", isStateChange: true)
           }
              
          }
                  
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
              
         
     
// END: Get APIXU data *******************************************************           
            
            
           

        
    
       } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}




def pollSchedule1()
{
    PollApixuNow()
}


def pollSchedule()
{
    PollStationNow()
}
              
def parse(String description) {
}


def PollStationNow()
{
    units()
    state.driverversion = "1.0.0"   
    // ************************* Update as required *************************************
    
 
    log.debug "Weewx: ForcePoll called"
    def params1 = [
        uri: "http://${ipaddress}:${weewxPort}/${weewxPath}"
         ]
    
    try {
        httpGet(params1) { resp1 ->
            resp1.headers.each {
            log.debug "Response1: ${it.name} : ${it.value}"
        }
            if(logSet == true){  
           
            log.debug "params1: ${params1}"
            log.debug "response contentType: ${resp1.contentType}"
 		    log.debug "response data: ${resp1.data}"
            } 
            
            
            if(logSet == false){ 
            log.info "Further Weewx detailed data logging disabled"    
            }    
   
           
            
  // Conversions
            
            // inHg to mbar - multiply inches by 33.8638815 to get mbar
            // Example: (50°F - 32) x .5556 = 10°C   *********** def Celcius1=(Faran =32) *0.5556
            // 1 mi = 1.609344 km -   ************  def newKM=miles * 1.609344
            //   = (68°F - 32) × 5/9 = 20 °C
            
            
            // def convertToC = (Faran1 - 32) *0.5556
            // def convertToKM = miles * 1.609344
            // def convertToMbar = inHg1 * 33.8638815
            
// Collect Data
            // Sometimes, after saving, Hubitat changes some characters that we don't want changing!
            // & # 1 7 6 ; F = degrees F (remove spaces before replacing on lines: 258, 288, 312 & 324)
            //  W/m & # 1 7 8 ; = W/m² (remove spaces before replacing on lines: 246 & 252)

            
              def illuminanceRaw1 = (resp1.data.stats.current.solarRadiation.replaceFirst("W/m²", ""))
               	if(illuminanceRaw1 == "   N/A" | illuminanceRaw1 == null){
                	state.illuminanceRaw = "0"
                	state.illuminenceNow = 'No Data'}
            	else{
                    state.illuminenceNow = 'OK'
                	state.illuminanceRaw = illuminanceRaw1
                }
           
            
              def solarradiationRaw1 = (resp1.data.stats.current.solarRadiation.replaceFirst("W/m²", ""))
            	if(solarradiationRaw1 == "   N/A" | solarradiationRaw1 == null){
                    state.solarradiationRaw = "0"
                	state.solarradiationNow = 'No Data'}
            	else{
                    state.solarradiationNow = 'OK'
                	state.solarradiationRaw = solarradiationRaw1
                }
            
              def dewpointRaw1 = (resp1.data.stats.current.dewpoint.replaceFirst("°F", ""))
            	if(dewpointRaw1 == "   N/A" | dewpointRaw1 == null){
                    state.dewpointRaw = "0"
                	state.dewpointNow = 'No Data'}
            	else{
                    state.dewpointNow = 'OK'
                    state.dewpointRaw = dewpointRaw1
                }
            
              def humidityRaw1 = (resp1.data.stats.current.humidity.replaceFirst("%", ""))
            	if(humidityRaw1 == "   N/A" | humidityRaw1 == null){
                    state.humidityRaw = "0"
                	state.humidityNow = 'No Data'}
            	else{
                    state.humidityNow = 'OK'
                	state.humidityRaw = humidityRaw1
                }
            
              def pressureRaw1 = (resp1.data.stats.current.barometer.replaceFirst("inHg", ""))
            	if(pressureRaw1 == "   N/A" | pressureRaw1 == null){
                    state.pressureRaw = "0"
                	state.pressureNow = 'No Data'}
            	else{
                    state.pressureNow = 'OK'
                	state.pressureRaw = pressureRaw1
                }
            
    		  def windSpeedRaw1 = (resp1.data.stats.current.windSpeed.replaceFirst("mph", "")) 
            	if(windSpeedRaw1 == "   N/A" | windSpeedRaw1 == null){
                    state.windSpeedRaw = "0" 
                    state.windNow = 'No Data'}
            	else{
                    state.windNow = 'OK'
                	state.windSpeedRaw = windSpeedRaw1
                }
                                                                
              def windGustRaw1 = (resp1.data.stats.current.windGust.replaceFirst("mph", ""))  
            	if(windGustRaw1 == "   N/A" | windGustRaw1 == null){
                    state.windGustRaw = "0"
                	state.windgustNow = 'No Data'}
           		else{
                    state.windgustNow = 'OK'
                	state.windGustRaw = windGustRaw1
                }
                                                    
              def insideTemperatureRaw1 = (resp1.data.stats.current.insideTemp.replaceFirst("°F", "")) 
            	if(insideTemperatureRaw1 == "   N/A" | insideTemperatureRaw1 == null){
                    state.insideTemperatureRaw = "0"
                	state.insideTempNow = 'No Data'}
            	else{
                    state.insideTempNow = 'OK'
                	state.insideTemperatureRaw = insideTemperatureRaw1
                }
            
              def rainRateRaw1 = (resp1.data.stats.current.rainRate.replaceFirst("in/hr", "")) 
            	if(rainRateRaw1 == "   N/A" | rainRateRaw1 == null){
                    state.rainRateRaw = "0"
                	state.rainRateNow = 'No Data'}
            	else{
                    state.rainRateNow = 'OK'
                	state.rainRateRaw = rainRateRaw1
                }
            
              def rainTodayRaw1 = (resp1.data.stats.sinceMidnight.rainSum.replaceFirst("in", ""))
            	if(rainTodayRaw1 == "   N/A" | rainTodayRaw1 == null){
                    state.rainTodayRaw = "0"
                	state.rainTodayNow = 'No Data'}
            	else{
                    state.rainTodayNow = 'OK'
                	state.rainTodayRaw = rainTodayRaw1
                }
            
              def inHumidRaw1 = (resp1.data.stats.current.insideHumidity.replaceFirst("%", "")) 
            	if(inHumidRaw1 == "   N/A" | inHumidRaw1 ==null){
                    state.inHumidRaw = "0"
                	state.inHumidNow = 'No Data'}
            	else{
                    state.inHumidNow = 'OK'
                	state.inHumidRaw = inHumidRaw1
                }
            
              def temperatureRaw1 = (resp1.data.stats.current.outTemp.replaceFirst("°F", "")) 
            	if(temperatureRaw1 == "   N/A" | temperatureRaw1 ==null){
                    state.temperatureRaw = "0"
                	state.tempNow = 'No Data'}
            	else{
                    state.tempNow = 'OK'
                	state.temperatureRaw = temperatureRaw1
                }
            
              def UVRaw1 = (resp1.data.stats.current.UV)
            	if(UVRaw1 == "   N/A" | UVRaw1 ==null){
                    state.UVRaw = "0"
                	state.uvNow = 'No Data'}
            	else{
                    state.uvNow = 'OK'
                	state.UVRaw = UVRaw1
                }
               
              def windChillRaw1 = (resp1.data.stats.current.windchill.replaceFirst("°F", ""))
            	if(windChillRaw1 == "   N/A" | windChillRaw1 ==null){
                    state.windChillRaw = "0"
                	state.windChillNow = 'No Data'}
           		else{
                    state.windChillNow = 'OK'
                	state.windChillRaw = windChillRaw1
                }
            
             def obsTime1 = (resp1.data.time)
                if(obsTime1 == "   N/A" | obsTime1 ==null){
                     state.obsTimeNow = 'No Data'}
            	else{
                    state.obsTimeNow = 'OK'
                	state.obsTime = obsTime1
                }
                      
             //  any more?
                 
             		
// Calculations ************************************************************************************************************              
  
    if(pressureUnit == "INHg"){
    def pressureFinalTemp = state.pressureRaw.toFloat()    
    state.pressureFinal = pressureFinalTemp.round(state.decimalPlaces)
           }
    
    if(pressureUnit == "MBAR"){
    def pressureFinalTemp = state.pressureRaw.toFloat()
    def pressureFinalAlmost = (pressureFinalTemp * 33.8638815) 
    state.pressureFinal = pressureFinalAlmost.round(state.decimalPlaces)
       	   }
         
    if(rainUnit == "IN"){
    def rainTodayAlmostFinal = state.rainTodayRaw.toFloat()     
    state.rainTodayFinal = rainTodayAlmostFinal.round(state.decimalPlaces)
        
    def rainRateAlmostFinal =  state.rainRateRaw.toFloat()   
    state.rainRateFinal = rainRateAlmostFinal.round(state.decimalPlaces)  
           }
            
    if(rainUnit == "MM"){
    def rainTodayFinalTemp = state.rainTodayRaw.toFloat()
    def rainTodayAlmostFinal = (rainTodayFinalTemp * 25.4)    
    state.rainTodayFinal = rainTodayAlmostFinal.round(state.decimalPlaces)
        
    def rainRateFinalTemp = state.rainRateRaw.toFloat() 
    def rainRateAlmostFinal =  (rainRateFinalTemp * 25.4)   
    state.rainRateFinal = rainRateAlmostFinal.round(state.decimalPlaces)    
    	  }   
            
   
    if(speedUnit == "MPH"){
     
    def windSpeedAlmostFinal = state.windSpeedRaw.toFloat()    
    state.windSpeedFinal = windSpeedAlmostFinal.round(state.decimalPlaces) 
        
    def windGustAlmostFinal = state.windGustRaw.toFloat()    
    state.windGustFinal = windGustAlmostFinal.round(state.decimalPlaces) 
    }
            
    if(speedUnit == "KPH"){
    def windSpeedFinalTemp = state.windSpeedRaw.toFloat() 
    def windSpeedAlmostFinal = (windSpeedFinalTemp * 1.609344)
    state.windSpeedFinal =  windSpeedAlmostFinal.round(state.decimalPlaces) 
        
    def windGustFinalTemp = state.windGustRaw.toFloat()
    def windGustAlmostFinal = (windGustFinalTemp * 1.609344)   
    state.windGustFinal =  windGustAlmostFinal.round(state.decimalPlaces)
    }
            
    if(temperatureUnit == "Fahrenheit (°F)"){
    def insideTemperatureAlmostFinal = state.insideTemperatureRaw.toFloat()    
    state.insideTemperatureFinal = insideTemperatureAlmostFinal.round(state.decimalPlaces)
        
    def temperatureAlmostFinal = state.temperatureRaw.toFloat()    
    state.temperatureFinal = temperatureAlmostFinal.round(state.decimalPlaces)
        
    def windChillAlmostFinal = state.windChillRaw.toFloat()
    state.windChillFinal = windChillAlmostFinal.round(state.decimalPlaces) 
        
    def dewPointAlmostFinal = state.dewpointRaw.toFloat()
    state.dewPointFinal = dewPointAlmostFinal.round(state.decimalPlaces)     
        
    }
            
    if(temperatureUnit == "Celsius (°C)"){
    def insideTemperatureAlmostFinal =  state.insideTemperatureRaw.toFloat()  
    def insideTemperatureTemp = ((insideTemperatureAlmostFinal - 32) *0.5556)        
    state.insideTemperatureFinal = insideTemperatureTemp.round(state.decimalPlaces)
        
    def temperatureAlmostFinal =  state.temperatureRaw.toFloat()  
    def temperatureTemp = ((temperatureAlmostFinal - 32) *0.5556)
    state.temperatureFinal = temperatureTemp.round(state.decimalPlaces)
        
    def windChillAlmostFinal = state.windChillRaw.toFloat()
    def windChillTemp =  ((windChillAlmostFinal - 32) *0.5556)  
    state.windChillFinal = windChillTemp.round(state.decimalPlaces)  
        
    def dewPointAlmostFinal = state.dewpointRaw.toFloat()
    def dewpointTemp =  ((dewPointAlmostFinal - 32) *0.5556)     
    state.dewPointFinal = dewpointTemp.round(state.decimalPlaces)         
        
        
    }        
        
    
            
 // Basics - No units ************************************************************************************************
            
             sendEvent(name: "Author", value: "Cobra", isStateChange: true)
             sendEvent(name: "DriverVersion", value: state.driverversion, isStateChange: true)
             sendEvent(name: "WeewxServerUptime", value: resp1.data.serverUptime, isStateChange: true)
             sendEvent(name: "WeewxServerLocation", value: resp1.data.location, isStateChange: true)
             sendEvent(name: "sunrise", value: resp1.data.almanac.sun.sunrise, isStateChange: true)
             sendEvent(name: "sunset", value: resp1.data.almanac.sun.sunset, isStateChange: true)
             sendEvent(name: "moonPhase", value: resp1.data.almanac.moon.phase, isStateChange: true)
             sendEvent(name: "moonRise", value: resp1.data.almanac.moon.rise, isStateChange: true)
            
            
            
            
            if(state.obsTimeNow == 'No Data'){sendEvent(name: "Last_Weewx_Update", value:"No Station Data", isStateChange: true)}
            else{sendEvent(name: "LastUpdate-Weewx", value: resp1.data.time, isStateChange: true)}
            
            if(state.uvNow == 'No Data'){sendEvent(name: "uv", value:"No Station Data", isStateChange: true)}
            else{sendEvent(name: "uv", value: state.UVRaw, isStateChange: true)}
            
            
            def windDirRaw = (resp1.data.stats.current.windDirText)
            	if(windDirRaw){
                    if(windDirRaw == 'N/A'){sendEvent(name: "wind_dir", value:"No Station Data", isStateChange: true)}
                    else {sendEvent(name: "wind_dir", value: windDirRaw, isStateChange: true)} 
                
                }
         
             def pressureTrend = (resp1.data.stats.current.barometerTrendData) 
                  if(pressureTrend){
                      if(pressureTrend == 'N/A'){sendEvent(name: "pressure_trend", value:"No Station Data", isStateChange: true)}
                      else if(pressureTrend.contains("-")){sendEvent(name: "pressure_trend", value:"Falling", isStateChange: true)} 
                      else if(pressureTrend.contains("+")){sendEvent(name: "pressure_trend", value:"Rising", isStateChange: true)} 
                      else {sendEvent(name: "pressure_trend", value:"Static", isStateChange: true)} 
                  }
            
            
            
            
// // Send Events  - WITH UNITS ********************************************************************************************            
              if(state.unitsOn == true){  
                  if(state.illuminenceNow == 'No Data'){sendEvent(name: "illuminance", value:"No Station Data", isStateChange: true)}              
                  else{sendEvent(name: "illuminance", value: state.illuminanceRaw + state.IU, isStateChange: true)}
                  
                  if(state.solarradiationNow == 'No Data'){sendEvent(name: "solarradiation", value:"No Station Data", isStateChange: true)}  
                  else{sendEvent(name: "solarradiation", value: state.solarradiationRaw + state.SRU, isStateChange: true)}
                  
                  if(state.dewpointNow == 'No Data'){sendEvent(name: "dewpoint", value:"No Station Data", isStateChange: true)}  
                  else{sendEvent(name: "dewpoint", value: state.dewPointFinal + state.TU, isStateChange: true)}
                  
                  if(state.humidityNow == 'No Data'){sendEvent(name: "humidity", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "humidity", value: state.humidityRaw + state.HU, isStateChange: true)}
                  
                  if(state.pressureNow == 'No Data'){sendEvent(name: "pressure", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "pressure", value: state.pressureFinal + state.PU, isStateChange: true)} 
                  
                  if(state.windNow == 'No Data'){sendEvent(name: "wind", value:"No Station Data", isStateChange: true)}     
                  else{ sendEvent(name: "wind", value: state.windSpeedFinal + state.SU, isStateChange: true)}
                  
                  if(state.windgustNow == 'No Data'){sendEvent(name: "wind_gust", value:"No Station Data", isStateChange: true)}     
                  else{sendEvent(name: "wind_gust", value: state.windGustFinal + state.SU, isStateChange: true)}
                  
                  if(state.insideTempNow == 'No Data'){sendEvent(name: "inside_temperature", value:"No Station Data", isStateChange: true)} 
                  else{sendEvent(name: "inside_temperature", value: state.insideTemperatureFinal + state.TU, isStateChange: true)}
                  
                  if(state.inHumidNow == 'No Data'){sendEvent(name: "inside_humidity", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "inside_humidity", value: state.inHumidRaw + state.HU, isStateChange: true)}     
                  
                  if(state.tempNow == 'No Data'){sendEvent(name: "temperature", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "temperature", value: state.temperatureFinal + state.TU, isStateChange: true)}
                  
                  if(state.rainRateNow == 'No Data'){sendEvent(name: "rain_rate", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "rain_rate", value: state.rainRateFinal + state.RRU, isStateChange: true)}  
           
                  if(state.rainTodayNow == 'No Data'){sendEvent(name: "precip_today", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "precip_today", value: state.rainTodayFinal + state.RU, isStateChange: true)}  
                  
                  if(state.rainRateNow == 'No Data'){sendEvent(name: "precip_1hr", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "precip_1hr", value: state.rainRateFinal + state.RU, isStateChange: true)}    
                 
                  if(state.windChillNow == 'No Data'){sendEvent(name: "feelsLike", value:"No Station Data", isStateChange: true)}      
                  else{sendEvent(name: "feelsLike", value: state.windChillFinal + state.TU, isStateChange: true)}      
            
           
                 
                 
                 
                  
                  
              }
            
// // Send Events  - WITHOUT UNITS *****************************************************************************************               
             if(state.unitsOn == false){  
             if(state.illuminenceNow == 'No Data'){sendEvent(name: "illuminance", value:"No Station Data", isStateChange: true)}              
                  else{sendEvent(name: "illuminance", value: state.illuminanceRaw, isStateChange: true)}
                  
                  if(state.solarradiationNow == 'No Data'){sendEvent(name: "solarradiation", value:"No Station Data", isStateChange: true)}  
                  else{sendEvent(name: "solarradiation", value: state.solarradiationRaw, isStateChange: true)}
                  
                  if(state.dewpointNow == 'No Data'){sendEvent(name: "dewpoint", value:"No Station Data", isStateChange: true)}  
                  else{sendEvent(name: "dewpoint", value: state.dewPointFinal, isStateChange: true)}
                  
                  if(state.humidityNow == 'No Data'){sendEvent(name: "humidity", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "humidity", value: state.humidityRaw, isStateChange: true)}
                  
                  if(state.pressureNow == 'No Data'){sendEvent(name: "pressure", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "pressure", value: state.pressureFinal, isStateChange: true)} 
                  
                  if(state.windNow == 'No Data'){sendEvent(name: "wind", value:"No Station Data", isStateChange: true)}     
                  else{ sendEvent(name: "wind", value: state.windSpeedFinal , isStateChange: true)}
                  
                  if(state.windgustNow == 'No Data'){sendEvent(name: "wind_gust", value:"No Station Data", isStateChange: true)}     
                  else{sendEvent(name: "wind_gust", value: state.windGustFinal, isStateChange: true)}
                  
                  if(state.insideTempNow == 'No Data'){sendEvent(name: "inside_temperature", value:"No Station Data", isStateChange: true)} 
                  else{sendEvent(name: "inside_temperature", value: state.insideTemperatureFinal, isStateChange: true)}
                  
                  if(state.inHumidNow == 'No Data'){sendEvent(name: "inside_humidity", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "inside_humidity", value: state.inHumidRaw, isStateChange: true)}     
                  
                  if(state.tempNow == 'No Data'){sendEvent(name: "temperature", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "temperature", value: state.temperatureFinal, isStateChange: true)}
                  
                  if(state.rainRateNow == 'No Data'){sendEvent(name: "rain_rate", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "rain_rate", value: state.rainRateFinal, isStateChange: true)}  
           
                  if(state.rainTodayNow == 'No Data'){sendEvent(name: "precip_today", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "precip_today", value: state.rainTodayFinal, isStateChange: true)}  
                  
                  if(state.rainRateNow == 'No Data'){sendEvent(name: "precip_1hr", value:"No Station Data", isStateChange: true)}
                  else{sendEvent(name: "precip_1hr", value: state.rainRateFinal, isStateChange: true)}    
                 
                  if(state.windChillNow == 'No Data'){sendEvent(name: "feelsLike", value:"No Station Data", isStateChange: true)}      
                  else{sendEvent(name: "feelsLike", value: state.windChillFinal, isStateChange: true)}      
            
              
                 
                 
                 
                 
                 
                 
                 
        }
            
// **********************************************************************************************            
  
             
            
            
         //    sendEvent(name: "chanceOfRain", value: resp1.data.forecast.simpleforecast.forecastday[0].pop + "%", isStateChange: true)
        //     sendEvent(name: "moonPhase", value: resp1.data.moon_phase.phaseofMoon , isStateChange: true)
        //     sendEvent(name: "moonIllumination", value: resp1.data.moon_phase.percentIlluminated  + "%" , isStateChange: true)

         //   sendEvent(name: "weather", value: resp1.data.current_observation.weather, isStateChange: true)
     
	   //		sendEvent(name: "city", value: resp1.data.current_observation.display_location.city, isStateChange: true)
      //      sendEvent(name: "state", value: resp1.data.current_observation.display_location.state, isStateChange: true)
     //       sendEvent(name: "percentPrecip", value: resp1.data.forecast.simpleforecast.forecastday[0].pop , isStateChange: true)
       //     sendEvent(name: "localSunrise", value: resp1.data.sun_phase.sunrise.hour + ":" + resp1.data.sun_phase.sunrise.minute, descriptionText: "Sunrise today is at $localSunrise", isStateChange: true)
      //  	sendEvent(name: "localSunset", value: resp1.data.sun_phase.sunset.hour + ":" + resp1.data.sun_phase.sunset.minute, descriptionText: "Sunset today at is $localSunset", isStateChange: true)
             
            
 // Select Icon
                if(iconType == false){   
           //        sendEvent(name: "weatherIcon", value: resp1.data.forecast.simpleforecast.forecastday[0].icon, isStateChange: true)
                }
                if(iconType == true){ 
			//       sendEvent(name: "weatherIcon", value: resp1.data.current_observation.icon, isStateChange: true)
                }    
           
           
           def WeatherSummeryFormat = weatherFormat
            
            if(summaryType == true){
            
            if (WeatherSummeryFormat == "Celsius, Miles & MPH"){
                		 sendEvent(name: "weatherSummaryFormat", value: "Celsius, Miles & MPH", isStateChange: true)
                         sendEvent(name: "weatherSummary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "   
                       + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + " degrees, " + "and a low of " 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
                       + resp1.data.current_observation.temp_c + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_c + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
                       + " at " + resp1.data.current_observation.wind_mph + " mph" + ", with gusts up to " + resp1.data.current_observation.wind_gust_mph + " mph" + ". Visibility is around " 
                       + resp1.data.current_observation.visibility_mi + " miles" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
                      )  
            }
                
            if (WeatherSummeryFormat == "Fahrenheit, Miles & MPH"){
                 		 sendEvent(name: "weatherSummaryFormat", value: "Fahrenheit, Miles & MPH", isStateChange: true)
                         sendEvent(name: "weatherSummary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "  
                       + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.fahrenheit + " degrees, " + "and a low of " 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.fahrenheit  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
                       + resp1.data.current_observation.temp_f + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_f + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
                       + " at " + resp1.data.current_observation.wind_mph + " mph" + ", with gusts up to: " + resp1.data.current_observation.wind_gust_mph + " mph" + ". Visibility is around " 
                       + resp1.data.current_observation.visibility_mi + " miles" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
                      )  
            }   
                
             if (WeatherSummeryFormat == "Celsius, Kilometres & KPH"){
                 		 sendEvent(name: "weatherSummaryFormat", value: "Celsius, Kilometres & KPH", isStateChange: true)
                         sendEvent(name: "weatherSummary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "  
                       + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + " degrees, " + "and a low of " 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
                       + resp1.data.current_observation.temp_c + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_c + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
                       + " at " + resp1.data.current_observation.wind_kph + " kph" + ", with gusts up to " + resp1.data.current_observation.wind_gust_kph + " kph" + ". Visibility is around " 
                       + resp1.data.current_observation.visibility_km + " kilometres" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
                      )  
            }
                
                
        }    
            
            
            
            
            
            
            
            if(summaryType == false){
                
             if (WeatherSummeryFormat == "Celsius, Miles & MPH"){
                		 sendEvent(name: "weatherSummaryFormat", value: "Celsius, Miles & MPH", isStateChange: true)
                         sendEvent(name: "weatherSummary", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions + ". " + " Forecast High:" + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + ", Forecast Low:" 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  +  ". Humidity: " + resp1.data.current_observation.relative_humidity + " Temperature: " 
                       + resp1.data.current_observation.temp_c  + ". Wind Direction: " + resp1.data.current_observation.wind_dir + ". Wind Speed: " + resp1.data.current_observation.wind_mph + " mph" 
                       + ", Gust: " + resp1.data.current_observation.wind_gust_mph + " mph. Rain: "  +resp1.data.forecast.simpleforecast.forecastday[0].pop + "%" , isStateChange: true
                      )  
            }
            
            if (WeatherSummeryFormat == "Fahrenheit, Miles & MPH"){
                		 sendEvent(name: "weatherSummaryFormat", value: "Fahrenheit, Miles & MPH", isStateChange: true)
                         sendEvent(name: "weatherSummary", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions + ". " + " Forecast High:" + resp1.data.forecast.simpleforecast.forecastday[0].high.fahrenheit + ", Forecast Low:" 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.fahrenheit  +  ". Humidity: " + resp1.data.current_observation.relative_humidity + " Temperature: " 
                       + resp1.data.current_observation.temp_f  + ". Wind Direction: " + resp1.data.current_observation.wind_dir + ". Wind Speed: " + resp1.data.current_observation.wind_mph + " mph" 
                       + ", Gust: " + resp1.data.current_observation.wind_gust_mph + " mph. Rain:"  +resp1.data.forecast.simpleforecast.forecastday[0].pop + "%", isStateChange: true
                      )  
            }
            
             if (WeatherSummeryFormat ==  "Celsius, Kilometres & KPH"){
                		 sendEvent(name: "weatherSummaryFormat", value:  "Celsius, Kilometres & KPH", isStateChange: true)
                         sendEvent(name: "weatherSummary", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions + ". " + " Forecast High:" + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + ", Forecast Low:" 
                       + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  +  ". Humidity: " + resp1.data.current_observation.relative_humidity + " Temperature: " 
                       + resp1.data.current_observation.temp_c  + ". Wind Direction: " + resp1.data.current_observation.wind_dir + ". Wind Speed: " + resp1.data.current_observation.wind_kph + " kph" 
                       + ", Gust: " + resp1.data.current_observation.wind_gust_kph + " kph. Rain:"  +resp1.data.forecast.simpleforecast.forecastday[0].pop + "%", isStateChange: true
                      )  
            }
            
            }    
            
            
    
            

                
    
       //     sendEvent(name: "observation_time", value: resp1.data.current_observation.observation_time, isStateChange: true)
      //      sendEvent(name: "weather", value: resp1.data.current_observation.weather, isStateChange: true)
  		//    sendEvent(name: "wind_string", value: resp1.data.current_observation.wind_string)
       //     sendEvent(name: "forecastConditions", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions, isStateChange: true)
      
            
            
     //       if(rainFormat == "Inches"){
      //      sendEvent(name: "precip_1hr", value: resp1.data.current_observation.precip_1hr_in, unit: "IN", isStateChange: true)
       //     sendEvent(name: "precip_today", value: resp1.data.current_observation.precip_today_in, unit: "IN", isStateChange: true)
      //      sendEvent(name: "rainTomorrow", value: resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.in, unit: "IN", isStateChange: true)
       //     sendEvent(name: "rainDayAfterTomorrow", value: resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.in, unit: "IN", isStateChange: true)
      //      sendEvent(name: "rainUnit", value: "Inches", isStateChange: true)
    //        }
      //      if(rainFormat == "Millimetres"){   
      //      sendEvent(name: "precip_today", value: resp1.data.current_observation.precip_today_metric, unit: "MM", isStateChange: true)
      //      sendEvent(name: "precip_1hr", value: resp1.data.current_observation.precip_1hr_metric, unit: "MM", isStateChange: true)
     //       sendEvent(name: "rainTomorrow", value: resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.mm, unit: "MM", isStateChange: true)
     //       sendEvent(name: "rainDayAfterTomorrow", value: resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.mm, unit: "MM", isStateChange: true)
     //       sendEvent(name: "rainUnit", value: "Millimetres", isStateChange: true)
    //        }
            
      //      if(tempFormat == "Celsius"){
      //      sendEvent(name: "dewpoint", value: resp1.data.current_observation.dewpoint_c, unit: "C", isStateChange: true)
      //      sendEvent(name: "forecastHigh", value: resp1.data.forecast.simpleforecast.forecastday[0].high.celsius, unit: "C", isStateChange: true)
      //      sendEvent(name: "forecastLow", value: resp1.data.forecast.simpleforecast.forecastday[0].low.celsius, unit: "C", isStateChange: true)
      //      sendEvent(name: "temperatureUnit", value: "Celsius", isStateChange: true)
      //      sendEvent(name: "feelsLike", value: resp1.data.current_observation.feelslike_c, unit: "C", isStateChange: true)   
     //       sendEvent(name: "temperature", value: resp1.data.current_observation.temp_c, unit: "C", isStateChange: true)
         
            	
     //   }
      //     if(tempFormat == "Fahrenheit"){ 
     //      sendEvent(name: "temperature", value: resp1.data.current_observation.temp_f, unit: "F", isStateChange: true)
     //      sendEvent(name: "feelsLike", value: resp1.data.current_observation.feelslike_f, unit: "F", isStateChange: true)
     //      sendEvent(name: "dewpoint", value: resp1.data.current_observation.dewpoint_f, unit: "F", isStateChange: true)
     //      sendEvent(name: "forecastHigh", value: resp1.data.forecast.simpleforecast.forecastday[0].high.fahrenheit, unit: "F", isStateChange: true)
     //      sendEvent(name: "forecastLow", value: resp1.data.forecast.simpleforecast.forecastday[0].low.fahrenheit, unit: "F", isStateChange: true)
     //      sendEvent(name: "temperatureUnit", value: "Fahrenheit", isStateChange: true)
     //      sendEvent(name: "feelsLike", value: resp1.data.current_observation.feelslike_f, unit: "F", isStateChange: true)    
     //      sendEvent(name: "temperature", value: resp1.data.current_observation.temp_f, unit: "F", isStateChange: true)	
    	
   //        }  
            
    //      if(distanceFormat == "Miles (mph)"){  
      //      sendEvent(name: "visibility", value: resp1.data.current_observation.visibility_mi, unit: "mi", isStateChange: true)
     //       sendEvent(name: "wind", value: resp1.data.current_observation.wind_mph, unit: "MPH", isStateChange: true)
       //     sendEvent(name: "wind_gust", value: resp1.data.current_observation.wind_gust_mph, isStateChange: true) 
     //       sendEvent(name: "distanceUnit", value: "Miles (mph)", isStateChange: true)
     //     }  
            
     //     if(distanceFormat == "Kilometres (kph)"){
     //      sendEvent(name: "visibility", value: resp1.data.current_observation.visibility_km, unit: "km", isStateChange: true)
     //      sendEvent(name: "wind", value: resp1.data.current_observation.wind_kph, unit: "KPH", isStateChange: true)  
     //      sendEvent(name: "wind_gust", value: resp1.data.current_observation.wind_gust_kph, isStateChange: true) 
     //      sendEvent(name: "distanceUnit", value: "Kilometres (kph)", isStateChange: true)  
     //     }
                      
     //       if(pressureFormat == "Inches"){
                
     //       sendEvent(name: "pressure", value: resp1.data.current_observation.pressure_in, unit: "mi", isStateChange: true)
      //      sendEvent(name: "pressureUnit", value: "Inches")  
      //      }
            
      //      if(pressureFormat == "Millibar"){
      //      sendEvent(name: "pressure", value: resp1.data.current_observation.pressure_mb, unit: "mb", isStateChange: true)
      //      sendEvent(name: "pressureUnit", value: "Millibar", isStateChange: true) 
      //      }
            
   

               
      //    state.lastPoll = now()     
   } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}


