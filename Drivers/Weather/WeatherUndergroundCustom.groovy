/**
 *  WeatherUndergroundCustom
 *
 *  Copyright 2018 mattw01
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
 *  - Last Update 25/04/2018
 * 
 *  - Added ability to choose 'Pressure', 'Distance/Speed' & 'Precipitation' units & switchable logging- @Cobra 25/04/2018
 *  - Added wind gust - @Cobra 24/04/2018
 *  - Added wind direction - @Cobra 23/04/2018
 *  - Added ability to choose between "Fahrenheit" and "Celsius" - @Cobra 23/03/2018
 *
 */

metadata {
    definition (name: "WeatherUndergroundCustom", namespace: "Cobra", author: "mattw01") {
        capability "Actuator"
        capability "Sensor"
        command "poll"
        command "forcePoll"
        attribute "Solar_Radiation", "number"
        attribute "Observation_Time", "string"
        attribute "Weather", "string"
        attribute "Feels_Like", "number"
        attribute "Precip_Last_Hour", "number"
        attribute "Precip_Today", "number"
        attribute "Wind_Speed", "number"
        attribute "Wind_String", "string"
        attribute "Pressure", "number"
        attribute "Dewpoint", "number"
        attribute "UV", "number"
        attribute "Visibility", "number"
        attribute "Forecast_High", "number"
        attribute "Forecast_Low", "number"
        attribute "Forecast_Conditions", "string"
        attribute "Display_Unit_Temperature", "string"
        attribute "Display_Unit_Distance", "string"
        attribute "Display_Unit_Pressure", "string"
        attribute "Wind_Direction", "string"
        attribute "Wind_Gust", "string"
        attribute "Temperature", "string"
        attribute "Illuminance", "string"
        attribute "Humidity", "string"
        
       
       
        
         
    }
    preferences() {
        section("Query Inputs"){
            input "apiKey", "text", required: true, title: "API Key"
            input "pollLocation", "text", required: true, title: "ZIP Code or Location"
            input "tempFormat", "enum", required: true, title: "Display Unit - Temperature: Fahrenheit or Celsius",  options: ["Fahrenheit", "Celsius"]
            input "distanceFormat", "enum", required: true, title: "Display Unit - Distance/Speed: Miles or Kilometres",  options: ["Miles (mph)", "Kilometres (kph)"]
            input "pressureFormat", "enum", required: true, title: "Display Unit - Pressure: Inches or Millibar",  options: ["Inches", "Millibar"]
            input "rainFormat", "enum", required: true, title: "Display Unit - Precipitation: Inches or Millimetres",  options: ["Inches", "Millimetres"]
            input "pollIntervalLimit", "number", title: "Poll Interval Limit:", required: true
            input "autoPoll", "bool", required: false, title: "Enable Auto Poll"
            input "pollInterval", "enum", title: "Auto Poll Interval:", required: false, defaultValue: "5 Minutes",
                   options: ["5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "logSet", "bool", title: "Log All WU Response Data", required: true, defaultValue: false
			
        }
    }
}

def updated() {
    log.debug "updated called"
    unschedule()
    forcePoll()
    def pollIntervalCmd = (settings?.pollInterval ?: "5 Minutes").replace(" ", "")
    if(autoPoll)
        "runEvery${pollIntervalCmd}"(pollSchedule)
}
def pollSchedule()
{
    forcePoll()
}
              
def parse(String description) {
}

def poll()
{
    if(now() - state.lastPoll > (pollIntervalLimit * 60000))
        forcePoll()
    else
        log.debug "poll called before interval threshold was reached"
}

def forcePoll()
{
    log.debug "WU: forcePoll called"
    def params = [
        uri: "http://api.wunderground.com/api/${apiKey}/conditions/forecast/q/${pollLocation}.json"
    ]
    log.debug "params: ${params}"
    try {
        httpGet(params) { resp ->
            resp.headers.each {
            log.debug "Response: ${it.name} : ${it.value}"
        }
            if(logSet == true){  
            log.debug "params: ${params}"
            
 		    log.debug "response contentType: ${resp.contentType}"
 		    log.debug "response data: ${resp.data}"
            }        
            sendEvent(name: "Illuminance", value: resp.data.current_observation.solarradiation, unit: "lux")  
            sendEvent(name: "Observation_Time", value: resp.data.current_observation.observation_time)
            sendEvent(name: "Weather", value: resp.data.current_observation.weather)
            sendEvent(name: "Wind_String", value: resp.data.current_observation.wind_string)
            sendEvent(name: "Solar_Radiation", value: resp.data.current_observation.solarradiation, unit: "W")
            sendEvent(name: "Humidity", value: resp.data.current_observation.relative_humidity, unit: "%")
            sendEvent(name: "UV", value: resp.data.current_observation.UV)
            sendEvent(name: "Forecast_Conditions", value: resp.data.forecast.simpleforecast.forecastday[0].conditions)
            sendEvent(name: "Wind_Direction", value: resp.data.current_observation.wind_dir)
            
            
            if(rainFormat == "Inches"){
            sendEvent(name: "Precip_Last_Hour", value: resp.data.current_observation.precip_1hr_in, unit: "IN")
            sendEvent(name: "Precip_Today", value: resp.data.current_observation.precip_today_in, unit: "IN")
            }
            if(rainFormat == "Millimetres"){   
            sendEvent(name: "Precip_Today", value: resp.data.current_observation.precip_today_metric, unit: "MM")
            sendEvent(name: "Precip_Last_Hour", value: resp.data.current_observation.precip_1hr_metric, unit: "MM")
            }
            
            if(tempFormat == "Celsius"){
            sendEvent(name: "Temperature", value: resp.data.current_observation.temp_c, unit: "C")
            sendEvent(name: "Feels_Like", value: resp.data.current_observation.feelslike_c, unit: "C")
            sendEvent(name: "Dewpoint", value: resp.data.current_observation.dewpoint_c, unit: "C")
            sendEvent(name: "Forecast_High", value: resp.data.forecast.simpleforecast.forecastday[0].high.celsius, unit: "C")
            sendEvent(name: "Forecast_Low", value: resp.data.forecast.simpleforecast.forecastday[0].low.celsius, unit: "C")
            sendEvent(name: "Display_Unit_Temperature", value: "Celsius")
            	
        }
           if(tempFormat == "Fahrenheit"){ 
           sendEvent(name: "Temperature", value: resp.data.current_observation.temp_f, unit: "F")
           sendEvent(name: "Feels_Like", value: resp.data.current_observation.feelslike_f, unit: "F")
           sendEvent(name: "Dewpoint", value: resp.data.current_observation.dewpoint_f, unit: "F")
           sendEvent(name: "Forecast_High", value: resp.data.forecast.simpleforecast.forecastday[0].high.fahrenheit, unit: "F")
           sendEvent(name: "Forecast_Low", value: resp.data.forecast.simpleforecast.forecastday[0].low.fahrenheit, unit: "F")
            sendEvent(name: "Display_Unit_Temperature", value: "Fahrenheit")
            	
           }  
            
          if(distanceFormat == "Miles (mph)"){  
            sendEvent(name: "Visibility", value: resp.data.current_observation.visibility_mi, unit: "mi")
            sendEvent(name: "Wind_Speed", value: resp.data.current_observation.wind_mph, unit: "MPH")
            sendEvent(name: "Wind_Gust", value: resp.data.current_observation.wind_gust_mph)  
            sendEvent(name: "Display_Unit_Distance", value: "Miles (mph)")  
          }  
            
          if(distanceFormat == "Kilometres (kph)"){
           sendEvent(name: "Visibility", value: resp.data.current_observation.visibility_km, unit: "km")
           sendEvent(name: "Wind_Speed", value: resp.data.current_observation.wind_kph, unit: "KPH")  
           sendEvent(name: "Wind_Gust", value: resp.data.current_observation.wind_gust_kph)  
           sendEvent(name: "Display_Unit_Distance", value: "Kilometres (kph)")  
          }
                      
            if(pressureFormat == "Inches"){
            sendEvent(name: "Pressure", value: resp.data.current_observation.pressure_in, unit: "mi")
            sendEvent(name: "Display_Unit_Pressure", value: "Inches")  
            }
            
            if(pressureFormat == "Millibar"){
            sendEvent(name: "Pressure", value: resp.data.current_observation.pressure_mb, unit: "mb")
            sendEvent(name: "Display_Unit_Pressure", value: "Millibar")  
            }
            
         state.lastPoll = now()   
            
            
        }           
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}