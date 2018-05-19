/**
 *  Apixu Weather Driver
 *
 *  Copyright 2018 @Cobra
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
 *  Last Update 19/05/2018
 *
 *
 *  V1.0.0 - POC
 *
 */

metadata {
    definition (name: "Apixu Weather Driver", namespace: "Cobra", author: "Andrew Parker") {
        capability "Actuator"
        capability "Sensor"
        capability "Temperature Measurement"
        capability "Illuminance Measurement"
        capability "Relative Humidity Measurement"
        
       

        
        command "Poll"
        command "ForcePoll"
 //     command "ResetPollCount"
        
        attribute "solarradiation", "number"
        attribute "observation_time", "string"
        attribute "localSunrise", "string"
        attribute "localSunset", "string"
        attribute "weather", "string"
        attribute "feelsLike", "number"
   //     attribute "weatherIcon", "string"
		attribute "city", "string"
        attribute "state", "string"
        attribute "country", "string"
   //     attribute "percentPrecip", "string"
        attribute "wind_string", "string"
        attribute "pressure", "number"
     //   attribute "dewpoint", "number"
        attribute "visibility", "number"
        attribute "forecastHigh", "number"
        attribute "forecastLow", "number"
        attribute "forecastConditions", "string"
        attribute "wind_dir", "string"
        attribute "wind_gust", "string"
 //       attribute "precip_1hr", "number"
        attribute "precip_today", "number"
        attribute "wind", "number"
        attribute "UV", "number"
       
        attribute "pollsSinceReset", "number"
        attribute "temperatureUnit", "string"
        attribute "distanceUnit", "string"
        attribute "pressureUnit", "string"
        attribute "rainUnit", "string"
        attribute "summaryFormat", "string"
  //      attribute "alert", "string"
        attribute "driverVersion", "string"
        attribute "driverNameSpace", "string"
  //      attribute "stationID", "string"
  //      attribute "weatherSummary", "string"
  //      attribute "weatherSummaryFormat", "string"
   //     attribute "chanceOfRain", "string"
        attribute "rainTomorrow", "string"
        attribute "rainDayAfterTomorrow", "string"
        attribute "averageTemp", "string"
        attribute "moonrise", "string"
        attribute "moonset", "string"
 


     
        
    }
    preferences() {
        section("Query Inputs"){
            input "apiKey", "text", required: true, title: "API Key"
            input "pollLocation", "text", required: true, title: "ZIP Code or Location"
            input "tempFormat", "enum", required: true, title: "Display Unit - Temperature: Fahrenheit or Celsius",  options: ["Fahrenheit", "Celsius"]
            input "distanceFormat", "enum", required: true, title: "Display Unit - Distance/Speed: Miles or Kilometres",  options: ["Miles (mph)", "Kilometres (kph)"]
            input "pressureFormat", "enum", required: true, title: "Display Unit - Pressure: Inches or Millibar",  options: ["Inches", "Millibar"]
            input "rainFormat", "enum", required: true, title: "Display Unit - Precipitation: Inches or Millimetres",  options: ["Inches", "Millimetres"]
            input "pollIntervalLimit", "number", title: "Poll Interval Limit:", required: true, defaultValue: 1
            input "autoPoll", "bool", required: false, title: "Enable Auto Poll"
            input "pollInterval", "enum", title: "Auto Poll Interval:", required: false, defaultValue: "5 Minutes", options: ["5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "logSet", "bool", title: "Log All WU Response Data", required: true, defaultValue: false
            input "cutOff", "time", title: "New Day Starts", required: true
     //       input "summaryType", "bool", title: "Full Weather Summary", required: true, defaultValue: false
      //      input "iconType", "bool", title: "Icon: On = Current - Off = Forecast", required: true, defaultValue: false
      //      input "weatherFormat", "enum", required: true, title: "How to format weather summary",  options: ["Celsius, Miles & MPH", "Fahrenheit, Miles & MPH", "Celsius, Kilometres & KPH"]
        }
    }
}

def updated() {
    log.debug "Updated called"
    state.version = "1.0.0"    // ************************* Update as required *************************************
    unschedule()
    state.NumOfPolls = 0
    ForcePoll()
    def pollIntervalCmd = (settings?.pollInterval ?: "5 Minutes").replace(" ", "")
    if(autoPoll)
        "runEvery${pollIntervalCmd}"(pollSchedule)
    
     def changeOver = cutOff
    schedule(changeOver, ResetPollCount)
}

def ResetPollCount(){
state.NumOfPolls = -1
    log.info "Poll counter reset.."
ForcePoll()
}

def pollSchedule()
{
    ForcePoll()
}
              
def parse(String description) {
}

def Poll()
{
    if(now() - state.lastPoll > (pollIntervalLimit * 60000))
        ForcePoll()
    else
        log.debug "Poll called before interval threshold was reached"
}

def ForcePoll()
{
    
    state.NumOfPolls = (state.NumOfPolls) + 1
    log.info " state.NumOfPolls = $state.NumOfPolls" 
   
    log.debug "Apixu: ForcePoll called"
    def params1 = [
    //    uri: "http://api.apixu.com/v1/current.json?key=${apiKey}&q=${pollLocation}"
       uri: "http://api.apixu.com/v1/forecast.json?key=${apiKey}&q=${pollLocation}&days=7"
    ]
    
//    def params2 = [
//        uri: "http://api.apixu.com/v1/forecast.json?key=${apiKey}&q=${pollLocation}"
 //   ]
    
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
            log.info "Further detailed Apixu data logging disabled"    
            }    
            
    
           sendEvent(name: "pollsSinceReset", value: state.NumOfPolls, isStateChange: true)
           sendEvent(name: "driverNameSpace", value: "Cobra", isStateChange: true)
           sendEvent(name: "driverVersion", value: state.version, isStateChange: true)
      //       sendEvent(name: "stationID", value: resp1.data.current_observation.station_id, isStateChange: true)
      //      sendEvent(name: "chanceOfRain", value: resp1.data.forecast.forecastday.hour, isStateChange: true)
            
            
           sendEvent(name: "moonrise", value: resp1.data.forecast.forecastday.astro.moonrise[0] , isStateChange: true)
           sendEvent(name: "moonset", value: resp1.data.forecast.forecastday.astro.moonset[0] , isStateChange: true)
           sendEvent(name: "localSunrise", value: resp1.data.forecast.forecastday.astro.sunrise[0] , isStateChange: true)
           sendEvent(name: "localSunset", value: resp1.data.forecast.forecastday.astro.sunset[0] , isStateChange: true)
       //      sendEvent(name: "moonIllumination", value: resp1.data.moon_phase.percentIlluminated  + "%" , isStateChange: true)

           sendEvent(name: "weather", value: resp1.data.current.condition.text, isStateChange: true) 
           sendEvent(name: "humidity", value: resp1.data.current.humidity, isStateChange: true)
	  //  	sendEvent(name: "illuminance", value: resp1.data.current_observation.solarradiation, unit: "lux", isStateChange: true)
	  	sendEvent(name: "city", value: resp1.data.location.name, isStateChange: true)
          sendEvent(name: "state", value: resp1.data.location.region, isStateChange: true)
          sendEvent(name: "country", value: resp1.data.location.country, isStateChange: true)  
            
      //      sendEvent(name: "percentPrecip", value: resp1.data.forecast.forecastday.astro.moonrise[0] , isStateChange: true)
       //     sendEvent(name: "localSunrise", value: resp1.data.sun_phase.sunrise.hour + ":" + resp1.data.sun_phase.sunrise.minute, descriptionText: "Sunrise today is at $localSunrise", isStateChange: true)
       // 	sendEvent(name: "localSunset", value: resp1.data.sun_phase.sunset.hour + ":" + resp1.data.sun_phase.sunset.minute, descriptionText: "Sunset today at is $localSunset", isStateChange: true)
             
            
 // Select Icon
                if(iconType == false){   
      //             sendEvent(name: "weatherIcon", value: resp1.data.forecast.simpleforecast.forecastday[0].icon, isStateChange: true)
                }
                if(iconType == true){ 
		//	       sendEvent(name: "weatherIcon", value: resp1.data.current_observation.icon, isStateChange: true)
                }    
           
           
           def WeatherSummeryFormat = weatherFormat
            
            if(summaryType == true){
            
            if (WeatherSummeryFormat == "Celsius, Miles & MPH"){
              //  		 sendEvent(name: "weatherSummaryFormat", value: "Celsius, Miles & MPH", isStateChange: true)
           //              sendEvent(name: "weatherSummary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "   
           //            + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + " degrees, " + "and a low of " 
             //          + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
               //        + resp1.data.current_observation.temp_c + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_c + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
                 //      + " at " + resp1.data.current_observation.wind_mph + " mph" + ", with gusts up to " + resp1.data.current_observation.wind_gust_mph + " mph" + ". Visibility is around " 
                   //    + resp1.data.current_observation.visibility_mi + " miles" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
                  //    )  
            }
                
            if (WeatherSummeryFormat == "Fahrenheit, Miles & MPH"){
             //    		 sendEvent(name: "weatherSummaryFormat", value: "Fahrenheit, Miles & MPH", isStateChange: true)
             //            sendEvent(name: "weatherSummary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "  
              //         + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.fahrenheit + " degrees, " + "and a low of " 
             //          + resp1.data.forecast.simpleforecast.forecastday[0].low.fahrenheit  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
            //           + resp1.data.current_observation.temp_f + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_f + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
           //            + " at " + resp1.data.current_observation.wind_mph + " mph" + ", with gusts up to: " + resp1.data.current_observation.wind_gust_mph + " mph" + ". Visibility is around " 
           //            + resp1.data.current_observation.visibility_mi + " miles" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
           //           )  
            }   
                
             if (WeatherSummeryFormat == "Celsius, Kilometres & KPH"){
          //       		 sendEvent(name: "weatherSummaryFormat", value: "Celsius, Kilometres & KPH", isStateChange: true)
          //               sendEvent(name: "weatherSummary", value: "Weather summary for" + " " + resp1.data.current_observation.display_location.city + ", " + resp1.data.current_observation.observation_time+ ". "  
          //             + resp1.data.forecast.simpleforecast.forecastday[0].conditions + " with a high of " + resp1.data.forecast.simpleforecast.forecastday[0].high.celsius + " degrees, " + "and a low of " 
          //             + resp1.data.forecast.simpleforecast.forecastday[0].low.celsius  + " degrees. " + "Humidity is currently around " + resp1.data.current_observation.relative_humidity + " and temperature is " 
         //              + resp1.data.current_observation.temp_c + " degrees. " + " The temperature feels like it's " + resp1.data.current_observation.feelslike_c + " degrees. " + "Wind is from the " + resp1.data.current_observation.wind_dir
         //              + " at " + resp1.data.current_observation.wind_kph + " kph" + ", with gusts up to " + resp1.data.current_observation.wind_gust_kph + " kph" + ". Visibility is around " 
         //              + resp1.data.current_observation.visibility_km + " kilometres" + ". " + "There is a "+resp1.data.forecast.simpleforecast.forecastday[0].pop + "% chance of rain today." , isStateChange: true
        //              )  
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
            
            
    
            

                
      //      def illume = (resp1.data.current_observation.solarradiation)
      //      if(illume){
       //     	 sendEvent(name: "illuminance", value: resp1.data.current_observation.solarradiation, unit: "lux", isStateChange: true)
        //         sendEvent(name: "solarradiation", value: resp1.data.current_observation.solarradiation, unit: "W", isStateChange: true)
    //        }
      //      if(!illume){
        //         sendEvent(name: "illuminance", value: "This station does not send Illumination data", isStateChange: true)
          //  	 sendEvent(name: "solarradiation", value: "This station does not send Solar Radiation data", isStateChange: true)
      //      }   
            
            sendEvent(name: "observation_time", value: resp1.data.current.last_updated, isStateChange: true) 
      //      sendEvent(name: "weather", value: resp1.data.current_observation.weather, isStateChange: true)
  	//	    sendEvent(name: "wind_string", value: resp1.data.current_observation.wind_string)
     //     sendEvent(name: "UV", value: resp1.data.data.current.condition.UV, isStateChange: true)
    //        sendEvent(name: "forecastConditions", value: resp1.data.forecast.simpleforecast.forecastday[0].conditions, isStateChange: true)
         sendEvent(name: "wind_dir", value: resp1.data.current.wind_dir, isStateChange: true) 
            
            
            if(rainFormat == "Inches"){
   //         sendEvent(name: "precip_1hr", value: resp1.data.current_observation.precip_1hr_in, unit: "IN", isStateChange: true)
          sendEvent(name: "precip_today", value: resp1.data.forecast.forecastday.day[0].totalprecip_in, unit: "IN", isStateChange: true)
   //         sendEvent(name: "rainTomorrow", value: resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.in, unit: "IN", isStateChange: true)
   //         sendEvent(name: "rainDayAfterTomorrow", value: resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.in, unit: "IN", isStateChange: true)
          sendEvent(name: "rainUnit", value: "Inches", isStateChange: true)
            }
            if(rainFormat == "Millimetres"){   
          sendEvent(name: "precip_today", value: resp1.data.forecast.forecastday.day[0].totalprecip_mm, unit: "MM", isStateChange: true)
    //        sendEvent(name: "precip_1hr", value: resp1.data.current_observation.precip_1hr_metric, unit: "MM", isStateChange: true)
    //        sendEvent(name: "rainTomorrow", value: resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.mm, unit: "MM", isStateChange: true)
    //        sendEvent(name: "rainDayAfterTomorrow", value: resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.mm, unit: "MM", isStateChange: true)
          sendEvent(name: "rainUnit", value: "Millimetres", isStateChange: true)
            }
            
            if(tempFormat == "Celsius"){
           sendEvent(name: "averageTemp", value: resp1.data.forecast.forecastday.day[0].avgtemp_c, unit: "C", isStateChange: true)
            sendEvent(name: "forecastHigh", value: resp1.data.forecast.forecastday.day[0].maxtemp_c, unit: "C", isStateChange: true)
            sendEvent(name: "forecastLow", value: resp1.data.forecast.forecastday.day[0].mintemp_c, unit: "C", isStateChange: true)
           sendEvent(name: "temperatureUnit", value: "Celsius", isStateChange: true)
         sendEvent(name: "feelsLike", value: resp1.data.current.feelslike_c, unit: "C", isStateChange: true)   
          sendEvent(name: "temperature", value: resp1.data.current.temp_c, unit: "C", isStateChange: true)
         
            	
        }
           if(tempFormat == "Fahrenheit"){ 
           sendEvent(name: "temperature", value: resp1.data.current.temp_f, unit: "F", isStateChange: true)
           sendEvent(name: "feelsLike", value: resp1.data.current.feelslike_f, unit: "F", isStateChange: true) 
            sendEvent(name: "averageTemp", value: resp1.data.forecast.forecastday.day[0].avgtemp_f, unit: "F", isStateChange: true)
           sendEvent(name: "forecastHigh", value: resp1.data.forecast.forecastday.day[0].maxtemp_f, unit: "F", isStateChange: true)
           sendEvent(name: "forecastLow", value: resp1.data.forecast.forecastday.day[0].mintemp_f, unit: "F", isStateChange: true)
           sendEvent(name: "temperatureUnit", value: "Fahrenheit", isStateChange: true)
           sendEvent(name: "feelsLike", value: resp1.data.current_observation.feelslike_f, unit: "F", isStateChange: true)    
           sendEvent(name: "temperature",value: resp1.data.current.temp_f, unit: "F", isStateChange: true)
    	
           }  
            
          if(distanceFormat == "Miles (mph)"){  
          sendEvent(name: "visibility", value: resp1.data.current.vis_miles, unit: "mi", isStateChange: true)
          sendEvent(name: "wind", value: resp1.data.current.wind_mph, unit: "MPH", isStateChange: true)
           sendEvent(name: "wind_gust", value: resp1.data.forecast.forecastday.day[0].maxwind_mph, unit: "MPH", isStateChange: true) 
          sendEvent(name: "distanceUnit", value: "Miles (mph)", isStateChange: true)
          }  
            
          if(distanceFormat == "Kilometres (kph)"){
           sendEvent(name: "visibility", value: resp1.data.current.vis_km, unit: "km", isStateChange: true)
           sendEvent(name: "wind", value: resp1.data.current.wind_kph, unit: "KPH", isStateChange: true)  
           sendEvent(name: "wind_gust", value: resp1.data.forecast.forecastday.day[0].maxwind_kph, unit: "KPH", isStateChange: true) 
           sendEvent(name: "distanceUnit", value: "Kilometres (kph)", isStateChange: true)  
          }
                      
            if(pressureFormat == "Inches"){
                
            sendEvent(name: "pressure", value: resp1.data.current.pressure_in, unit: "in", isStateChange: true)
            sendEvent(name: "pressureUnit", value: "Inches")  
            }
            
            if(pressureFormat == "Millibar"){
            sendEvent(name: "pressure", value: resp1.data.current.pressure_mb, unit: "mb", isStateChange: true)
            sendEvent(name: "pressureUnit", value: "Millibar", isStateChange: true) 
            }
            
   
      //       def possAlert = (resp1.data.alerts.description)
       //        if (possAlert){
        //       sendEvent(name: "alert", value: resp1.data.alerts.description, isStateChange: true)  
       //        }
       //         if (!possAlert){
       //        sendEvent(name: "alert", value: " No current weather alerts for this area")
        //        }
       //        
          state.lastPoll = now()     

        } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}



def Report(){
  def obvTime = Observation_Time.value
    
  log.info "$obvTime"  
    
}