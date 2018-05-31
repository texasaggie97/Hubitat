/**
 *  Weewx Weather Driver
 *
 *  Copyright 2018 @Cobra
 *
 *  This driver was originally born from an idea by @mattw01 and @Jhoke and I thank them for that!
 *  
 *  This driver is specifically designed to be used with 'Weewx' and your own PWS
 *
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
 *  Last Update 31/05/2018
 *
 *
 *  V1.1.2 - Changed single speechmarks for double & some code cleanup
 *  V1.1.1 - debug
 *  V1.1.0 - Added error checking for 'N/A' and 'No Station Data' when N/A is returned
 *  V1.0.0 - Original POC
 *
 */




metadata {
    definition (name: "Weewx Weather Driver", namespace: "Cobra", author: "Andrew Parker") {
        capability "Actuator"
        capability "Sensor"
        capability "Temperature Measurement"
        capability "Illuminance Measurement"
        capability "Relative Humidity Measurement"
        command "PollNow"

        
// Base Info        
        attribute "DriverCreatedBy", "string"
        attribute "DriverVersion", "string"
        attribute "ServerUptime", "string"
        attribute "ServerLocation", "string"
        
// Units
        attribute "distanceUnit", "string"
        attribute "pressureUnit", "string"
        attribute "rainUnit", "string"
        attribute "summaryFormat", "string"
        
// Collected Data       
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
        attribute "UV", "string"
        attribute "feelsLike", "string"
        attribute "observation_time", "string"
        attribute "precip_1hr", "string"
        attribute "precip_today", "string"
        
        
        
        
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "weather", "string"
       
        attribute "weatherIcon", "string"
		attribute "city", "string"
        attribute "state", "string"
        attribute "percentPrecip", "string"
        attribute "wind_string", "string"
       
       
        
        attribute "visibility", "number"
        attribute "forecastHigh", "number"
        attribute "forecastLow", "number"
        attribute "forecastConditions", "string"
       
     //   attribute "wind_gust_dir", "string"
      
        
        
        attribute "weatherSummary", "string"
        attribute "weatherSummaryFormat", "string"
        attribute "chanceOfRain", "string"
        attribute "rainTomorrow", "string"
        attribute "rainDayAfterTomorrow", "string"
        attribute "moonPhase", "string"
        attribute "moonIllumination", "string"
 


     
        
    }
    preferences() {
        section("Query Inputs"){
            input "ipaddress", "text", required: true, title: "Weewx Server IP/URI"
            input "weewxPort", "text", required: true, title: "Connection Port", defaultValue: "80"
            input "weewxPath", "text", required: true, title: "path to file", defaultValue: "weewx/daily.json"
            input "unitSet", "bool", title: "Display Data Units", required: true, defaultValue: true
            input "logSet", "bool", title: "Log All Response Data", required: true, defaultValue: false
            input "pollInterval", "enum", title: "Poll Interval", required: true, defaultValue: "5 Minutes", options: ["Manual Poll Only", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "pressureUnit", "enum", title: "Pressure Unit", required:true, defaultValue: "INHg", options: ["INHg", "MBAR"]
            input "rainUnit", "enum", title: "Rain Unit", required:true, defaultValue: "IN", options: ["IN", "MM"]
            input "rainRateUnit", "enum", title: "Rain Rate Unit", required:true, defaultValue: "IN/HR", options: ["IN/HR", "MM/HR"]
            input "speedUnit", "enum", title: "Wind Speed Unit", required:true, defaultValue: "MPH", options: ["MPH", "KPH"]
            input "temperatureUnit", "enum", title: "Temperature Unit", required:true, defaultValue: "Fahrenheit (°F)", options: ["Fahrenheit (°F)", "Celsius (°C)"]
            input "decimalUnit", "enum", title: "Max Decimal Places", required:true, defaultValue: "2", options: ["1", "2", "3", "4", "5"]
            
 
  
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
    setVer()
    units()
    PollNow()
    def pollIntervalCmd = (settings?.pollInterval ?: "5 Minutes").replace(" ", "")
    
    if(pollInterval == "Manual Poll Only"){
        log.info "MANUAL POLLING ONLY"}
    else{
        "runEvery${pollIntervalCmd}"(pollSchedule)}
    

}


def units(){
    
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
    state.decimalPlaces = decimalUnit.toInteger()
}


















def pollSchedule()
{
    PollNow()
}
              
def parse(String description) {
}


def PollNow()
{
    units()
	setVer()
    
 
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

            
              def illuminanceRaw1 = (resp1.data.stats.current.solarRadiation.replaceFirst("W/m&#178;", ""))
               	if(illuminanceRaw1 == "N/A" | illuminanceRaw1 == null){
                	state.illuminanceRaw = "0"
                	state.illuminenceNow = 'No Data'}
            	else{
                    state.illuminenceNow = 'OK'
                	state.illuminanceRaw = illuminanceRaw1
                }
           
            
              def solarradiationRaw1 = (resp1.data.stats.current.solarRadiation.replaceFirst("W/m&#178;", ""))
            	if(solarradiationRaw1 == "N/A" | solarradiationRaw1 == null){
                    state.solarradiationRaw = "0"
                	state.solarradiationNow = 'No Data'}
            	else{
                    state.solarradiationNow = 'OK'
                	state.solarradiationRaw = solarradiationRaw1
                }
            
              def dewpointRaw1 = (resp1.data.stats.current.dewpoint.replaceFirst("&#176;F", ""))
            	if(dewpointRaw1 == "N/A" | dewpointRaw1 == null){
                    state.dewpointRaw = "0"
                	state.dewpointNow = 'No Data'}
            	else{
                    state.dewpointNow = 'OK'
                    state.dewpointRaw = dewpointRaw1
                }
            
              def humidityRaw1 = (resp1.data.stats.current.humidity.replaceFirst("%", ""))
            	if(humidityRaw1 == "N/A" | humidityRaw1 == null){
                    state.humidityRaw = "0"
                	state.humidityNow = 'No Data'}
            	else{
                    state.humidityNow = 'OK'
                	state.humidityRaw = humidityRaw1
                }
            
              def pressureRaw1 = (resp1.data.stats.current.barometer.replaceFirst("inHg", ""))
            	if(pressureRaw1 == "N/A" | pressureRaw1 == null){
                    state.pressureRaw = "0"
                	state.pressureNow = 'No Data'}
            	else{
                    state.pressureNow = 'OK'
                	state.pressureRaw = pressureRaw1
                }
            
    		  def windSpeedRaw1 = (resp1.data.stats.current.windSpeed.replaceFirst("mph", "")) 
            	if(windSpeedRaw1 == "N/A" | windSpeedRaw1 == null){
                    state.windSpeedRaw = "0" 
                    state.windNow = 'No Data'}
            	else{
                    state.windNow = 'OK'
                	state.windSpeedRaw = windSpeedRaw1
                }
                                                                
              def windGustRaw1 = (resp1.data.stats.current.windGust.replaceFirst("mph", ""))  
            	if(windGustRaw1 == "N/A" | windGustRaw1 == null){
                    state.windGustRaw = "0"
                	state.windgustNow = 'No Data'}
           		else{
                    state.windgustNow = 'OK'
                	state.windGustRaw = windGustRaw1
                }
                                                    
              def insideTemperatureRaw1 = (resp1.data.stats.current.insideTemp.replaceFirst("&#176;F", "")) 
            	if(insideTemperatureRaw1 == "N/A" | insideTemperatureRaw1 == null){
                    state.insideTemperatureRaw = "0"
                	state.insideTempNow = 'No Data'}
            	else{
                    state.insideTempNow = 'OK'
                	state.insideTemperatureRaw = insideTemperatureRaw1
                }
            
              def rainRateRaw1 = (resp1.data.stats.current.rainRate.replaceFirst("in/hr", "")) 
            	if(rainRateRaw1 == "N/A" | rainRateRaw1 == null){
                    state.rainRateRaw = "0"
                	state.rainRateNow = 'No Data'}
            	else{
                    state.rainRateNow = 'OK'
                	state.rainRateRaw = rainRateRaw1
                }
            
              def rainTodayRaw1 = (resp1.data.stats.sinceMidnight.rainSum.replaceFirst("in", ""))
            	if(rainTodayRaw1 == "N/A" | rainTodayRaw1 == null){
                    state.rainTodayRaw = "0"
                	state.rainTodayNow = 'No Data'}
            	else{
                    state.rainTodayNow = 'OK'
                	state.rainTodayRaw = rainTodayRaw1
                }
            
              def inHumidRaw1 = (resp1.data.stats.current.insideHumidity.replaceFirst("%", "")) 
            	if(inHumidRaw1 == "N/A" | inHumidRaw1 ==null){
                    state.inHumidRaw = "0"
                	state.inHumidNow = 'No Data'}
            	else{
                    state.inHumidNow = 'OK'
                	state.inHumidRaw = inHumidRaw1
                }
            
              def temperatureRaw1 = (resp1.data.stats.current.outTemp.replaceFirst("&#176;F", "")) 
            	if(temperatureRaw1 == "N/A" | temperatureRaw1 ==null){
                    state.temperatureRaw = "0"
                	state.tempNow = 'No Data'}
            	else{
                    state.tempNow = 'OK'
                	state.temperatureRaw = temperatureRaw1
                }
            
              def UVRaw1 = (resp1.data.stats.current.UV)
            	if(UVRaw1 == "N/A" | UVRaw1 ==null){
                    state.UVRaw = "0"
                	state.uvNow = 'No Data'}
            	else{
                    state.uvNow = 'OK'
                	state.UVRaw = UVRaw1
                }
               
              def windChillRaw1 = (resp1.data.stats.current.windchill.replaceFirst("&#176;F", ""))
            	if(windChillRaw1 == "N/A" | windChillRaw1 ==null){
                    state.windChillRaw = "0"
                	state.windChillNow = 'No Data'}
           		else{
                    state.windChillNow = 'OK'
                	state.windChillRaw = windChillRaw1
                }
            
             def obsTime1 = (resp1.data.time)
                if(obsTime1 == "N/A" | obsTime1 ==null){
                     state.obsTimeNow = 'No Data'}
            	else{
                    state.obsTimeNow = 'OK'
                	state.obsTime = obsTime1
                }
                      
             //  any more?
                 
             		
// Calculations ************************************************************************************************************            
  
    if(pressureUnit == "INHg"){
    def pressureFinalTemp = state.pressureRaw.toFloat()    
    state.pressureFinal = pressureFinalTemp.round(2)
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
            
             sendEvent(name: "DriverCreatedBy", value: "Cobra", isStateChange: true)
             sendEvent(name: "DriverVersion", value: state.driverversion, isStateChange: true)
             sendEvent(name: "ServerUptime", value: resp1.data.serverUptime, isStateChange: true)
      //       sendEvent(name: "ServerLocation", value: resp1.data.location, isStateChange: true)
            
            if(state.obsTimeNow == 'No Data'){sendEvent(name: "observation_time", value:"No Station Data", isStateChange: true)}
            else{sendEvent(name: "observation_time", value: state.obsTime, isStateChange: true)}
            
            if(state.uvNow == 'No Data'){sendEvent(name: "UV", value:"No Station Data", isStateChange: true)}
            else{sendEvent(name: "UV", value: state.UVRaw, isStateChange: true)}
            
            
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
              if(unitSet == true){  
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
             if(unitSet == false){  
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

def setVer(){
        state.driverversion = "1.1.2"   // ************************* Update as required *************************************
}
