/**
 *  Irrigation Switch Driver
 *
 *  Copyright 2018 @Cobra
 *
 *  This driver was originally written by @mattw01 and I thank him for that!
 *  Heavily modified by myself: @Cobra with lots of help from @Scottma61 ( @Matthew )
 *  and with valuable input from the Hubitat community
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
 *  Last Update 11/05/2018
 *
 *
 *  V1.2.0 - cleanup of commented out code - put 'manual poll' in dropdown (removed autopoll switch) - Added WU_Update attribute & code - Fixed error in calculation formula
 *  V1.1.0 - Added ability to set time to calculate rainfall
 *  V1.0.0 - Original POC
 *
 */

metadata {
    definition (name: "Irrigation Switch Driver", namespace: "Cobra", author: "mattw01") {
        capability "Actuator"
        capability "Sensor"
        capability "Switch"

        

        command "ManualPoll"
//      command "PollCountReset"
//      command "createHistory"
//      command "calculateNow"
           
     	attribute "WU_Update", "string"
        attribute "Run_Today", "string"
        attribute "Polls_Since_Reset", "string"
        attribute "Run_Time", "string"
        attribute "Running_Today", "string"
        attribute "Todays_Calculation", "string"
        attribute "Display_Unit_Rain", "string"
        attribute "Display_Unit_Temp", "string"
        attribute "Driver_Version", "string"
        attribute "Driver_NameSpace", "string"
        attribute "Station_ID", "string"
        attribute "NumberOfPolls", "string"
        attribute "Expected_Rain_Tomorrow", "string"
        attribute "Expected_Rain_Day_After_Tomorrow", "string"
   	attribute "Sunrise", "string"
        attribute "Sunset", "string"
        attribute "Illuminance", "string"
        attribute "Rain_Chance", "string"
        attribute "Rain_Today", "number"
        attribute "Rain_Yesterday", "string"
        attribute "Rain_TheDayBeforeYesterday", "string"
        attribute "Temperature", "string"
 
     def pollIntervalLimit = 1
        
    }
    preferences() {
        section("Query Inputs"){
            input "apiKey", "text", required: true, title: "API Key"
            input "pollLocation", "text", required: true, title: "ZIP Code or Location" 
            input "goNogo", "number", required: true, title: " Switch on if calculation below" 
            input "rainFormat", "enum", required: true, title: "Display Unit - Rain: Inches or Millimetres",  options: ["Inches", "Millimetres"]
            input "tempFormat", "enum", required: true, title: "Display Unit - Temp: Fahrenheit or Celsius",  options: ["Fahrenheit", "Celsius"]
            input "pollInterval", "enum", title: "Poll Interval:", required: true, defaultValue: "5 Minutes", options: ["Manual Poll Only", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
            input "dbyWeight", "enum", required: true, title: "Weighting: The Day Before Yesterday",  options: ["1", "2", "3", "4", "5"]
            input "yWeight", "enum", required: true, title: "Weighting: Yesterday",  options: ["1", "2", "3", "4", "5"]
            input "tdWeight", "enum", required: true, title: "Weighting: Today",  options: ["1", "2", "3", "4", "5"]
            input "tmWeight", "enum", required: true, title: "Weighting: Tomorrow",  options: ["1", "2", "3", "4", "5"]
            input "datWeight", "enum", required: true, title: "Weighting: The Day After Tomorrow",  options: ["1", "2", "3", "4", "5"]
 	    input "cutOff", "time", title: "Day Reset Time", required: true
            input "checkTime", "time", title: "Check Criteria Time", required: true
            input "runTime", "time", title: "Time to switch ON ", required: true
            input "stopTime", "time", title: "Time to switch OFF ", required: true
            
            
        }
    }
}

def updated() {
    log.debug "updated called - $settings"
   state.version = "1.2.0"     // *******************************************************************************************************************************************
    unschedule()
    state.NumOfPolls = 0
    ForcePoll()
    def pollIntervalCmd = (settings?.pollInterval ?: "5 Minutes").replace(" ", "")
    
    if(pollInterval == "Manual Poll Only"){
        log.info "MANUAL POLLING ONLY"}
    else{
        "runEvery${pollIntervalCmd}"(pollSchedule)}
    
    def changeOver = cutOff
    schedule(changeOver, createHistory)
    def processTime = checkTime
    schedule(processTime, processNow)
    schedule(runTime, runNow)
    schedule(stopTime, offNow)
    
    
    state.rainTmp3 = 0  // default for rain the day before yesterday
    state.rainTmp2 = 0  // default for rain yesterday
    state.rainTmp1 = 0  // default for rain today
    state.rainTmp4 = 0  // default for rain tomorrow
    state.rainTmp5 = 0  // default for rain the day after tomorrow
    state.finalCalc = 0 // default for final calculation
    log.info "Changeover time = $changeOver"
    
}

def ManualPoll(evt){
    
 ForcePoll()   
    
}

def installed(){
  off() 
}

def offNow(evt){
 off()   
    
}

def calculateNow(evt){
    log.info "Manual 'Calculate Now called..."
    calculateFinal()
    
}



def  processNow(evt){
    log.info "Calling processNow"
     calculateFinal()
  
    
}


def runNow(evt){
   

    if(state.canRun == true){
    log.info " Not enough rain so turning on switch"
    on()    
		}
    else{
        log.info " No need to run at this time - There has already been enough rain."
		}
	}    
def on() {
 log.debug "****************** ON **********************"  
sendEvent(name: "switch", value: "on")
    
}

def off() {
 log.debug "******************  OFF *********************"   
sendEvent(name: "switch", value: "off")
    
}

def calculateFinal(){
    log.info " Running calculations for today... "
    
    /** 
	Day1 = The Day Before Yesterday
	Day2 = Yesterday
	Day3 = Today
	Day4 = Tomorrow
	Day5 = Day After Tomorrow
    
    */
    
 
    def dbyWeight1 = dbyWeight.toFloat()
    def yWeight1 = yWeight.toFloat()
    def tdWeight1 = tdWeight.toFloat()
    def tmWeight1 = tmWeight.toFloat()
    def datWeight1 = datWeight.toFloat()
    def goNogo1 = goNogo.toDouble()
    def day1Stat = state.rainTmp1.toDouble()
    def day2Stat = state.rainTmp2.toDouble()
    def day3Stat = state.rainTmp3.toDouble()
    def day4Stat = state.rainTmp4.toDouble()
    def day5Stat = state.rainTmp5.toDouble()
	
    def day1Calc = (day1Stat * dbyWeight1)
    def day2Calc = (day2Stat * yWeight1)
    def day3Calc = (day3Stat * tdWeight1)
    def day4Calc = (day4Stat * tmWeight1)
    def day5Calc = (day5Stat * datWeight1)
    
    log.info" day1Calc = $day1Calc -- day2Calc = $day2Calc -- day3Calc = $day3Calc -- day4Calc = $day4Calc -- day5Calc = $day5Calc "

    state.finalCalc = (day1Calc += day2Calc += day3Calc += day4Calc += day5Calc)
    log.info " Final Calculation = $state.finalCalc"
    
    if(state.finalCalc < goNogo1 || finalCalc == goNogo1 ){
         log.info " Final Calculation is less than: $goNogo1 - switch will turn on today"
       state.canRun = true
    }
    else{
        log.info " Final Calculation is greater than or equal to: $goNogo1 - switch will not turn on today"
       state.canRun = false
    }
    ForcePoll()
}

def createHistory(evt){
    ForcePoll()
    PollCountReset()
    log.info "Calling CreatHistory"
	state.rainTmp3 = state.rainTmp2
	state.rainTmp2 = state.rainTmp1
	state.rainTmp1 = state.rainToday.toDouble()

    ForcePoll() 
}





def PollCountReset(){
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
   
    log.debug "WU: ForcePoll called"
    def params1 = [
       uri: "http://api.wunderground.com/api/${apiKey}/alerts/astronomy/conditions/forecast/q/${pollLocation}.json"
    ]
    
    try {
        httpGet(params1) { resp1 ->
            resp1.headers.each {
//           log.debug "Response1: ${it.name} : ${it.value}"
        }

           
//           log.debug "params1: ${params1}"
//           log.debug "response contentType: ${resp1.contentType}"
// 		     log.debug "response data: ${resp1.data}"


  
            
            
            sendEvent(name: "WU_Update", value: resp1.data.current_observation.observation_time, isStateChange: true)
            sendEvent(name: "Run_Today", value: "$state.canRun", isStateChange: true)
     	    sendEvent(name: "Todays_Calculation", value: state.finalCalc, isStateChange: true)
            sendEvent(name: "Rain_TheDayBeforeYesterday", value: state.rainTmp3, isStateChange: true)
            sendEvent(name: "Rain_Yesterday", value: state.rainTmp2, isStateChange: true)
            sendEvent(name: "Polls_Since_Reset", value: state.NumOfPolls, isStateChange: true)
            sendEvent(name: "Driver_NameSpace", value: "Cobra", isStateChange: true)
            sendEvent(name: "Driver_Version", value: state.version, isStateChange: true)
            sendEvent(name: "Station_ID", value: resp1.data.current_observation.station_id, isStateChange: true)
            sendEvent(name: "Rain_Chance", value: resp1.data.forecast.simpleforecast.forecastday[0].pop + "%", isStateChange: true)
            sendEvent(name: "Sunrise", value: resp1.data.sun_phase.sunrise.hour + ":" + resp1.data.sun_phase.sunrise.minute, isStateChange: true)
            sendEvent(name: "Sunset", value: resp1.data.sun_phase.sunset.hour + ":" + resp1.data.sun_phase.sunset.minute, isStateChange: true)
   			
           if(tempFormat == "Celsius"){
            sendEvent(name: "Temperature", value: resp1.data.current_observation.temp_c, unit: "C", isStateChange: true)  
            sendEvent(name: "Display_Unit_Temp", value: "Celsius")
               
           }
           if(tempFormat == "Fahrenheit"){ 
           sendEvent(name: "Temperature", value: resp1.data.current_observation.temp_f, unit: "F", isStateChange: true)
           sendEvent(name: "Display_Unit_Temp", value: "Fahrenheit")
            
           }
            
            
            if(rainFormat == "Inches"){
                  log.info "rainFormat = Inches"
            sendEvent(name: "Rain_Today", value: resp1.data.current_observation.precip_today_in, unit: "IN", isStateChange: true)
            state.rainToday = (resp1.data.current_observation.precip_today_in)
            state.rainTmp4 = (resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.in)
            state.rainTmp5 = (resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.in)
            sendEvent(name: "Expected_Rain_Tomorrow", value: resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.in, unit: "IN", isStateChange: true)
            sendEvent(name: "Expected_Rain_Day_After_Tomorrow", value: resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.in, unit: "IN", isStateChange: true)
            sendEvent(name: "Display_Unit_Rain", value: "Inches", isStateChange: true)
            }
            if(rainFormat == "Millimetres"){  
                 log.info "rainFormat = Millimetres"
            sendEvent(name: "Rain_Today", value: resp1.data.current_observation.precip_today_metric, unit: "MM", isStateChange: true)
            state.rainToday = (resp1.data.current_observation.precip_today_metric)
            state.rainTmp4 = (resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.mm)
            state.rainTmp5 = (resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.mm)   
            sendEvent(name: "Expected_Rain_Tomorrow", value: resp1.data.forecast.simpleforecast.forecastday[1].qpf_allday.mm, unit: "MM", isStateChange: true)
            sendEvent(name: "Expected_Rain_Day_After_Tomorrow", value: resp1.data.forecast.simpleforecast.forecastday[2].qpf_allday.mm, unit: "MM", isStateChange: true)
            sendEvent(name: "Display_Unit_Rain", value: "Millimetres", isStateChange: true)
            }
      
   
          state.lastPoll = now()     

        } 
        
    } catch (e) {
        log.error "something went wrong: $e"
    }
    
}

