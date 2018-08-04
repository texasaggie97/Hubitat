/**
 *  Virtual Temperature Device
 *
 *  Copyright (c) AJ Parker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain a
 *  copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License  for the specific language governing permissions and limitations
 *  under the License.
 *
 *  Changes: 05/08/2018
 *
 *  V1.2.0 - Added selectable decimal places
 *  V1.1.0 - Added remote version checking
 *  V1.0.0 - POC
 *
 */

metadata {
    definition (name:"Average Temperature Device", namespace:"Cobra", author:"AJ Parker") {
        capability "Temperature Measurement"
        capability "Sensor"
		attribute "trend", "string"
        attribute "DriverVersion", "string"
        attribute "DriverAuthor", "string"
        attribute "DriverStatus", "string"
        command "parse"     // (String "temperature:<value>")
        command "calculateTrendNow"
    }

 preferences() {
     
      section(){
        input "frequency", "number", required: true, title: "How often to check for trend (Minutes after temp change)", defaultValue: "30"  
        input "decimalUnit", "enum", title: "Max Decimal Places", required:true, defaultValue: "2", options: ["1", "2", "3", "4", "5"]
  }   
 }
}

def updated() {
    log.debug "Updated called"
    unschedule()
    version()
   state.DecimalPlaces = decimalUnit.toInteger()
}


def parse(message) {
    TRACE("parse(${message})")
def averageTemp = message.round(state.DecimalPlaces)
  state.current = averageTemp
  def checkFrequency = 60 * frequency
   runIn(checkFrequency, calculateTrendNow) 

    
    
    
    
    

    TRACE("event: (${averageTemp})")
    sendEvent(name:"temperature", value: "$averageTemp")
  
}

private def TRACE(message1) {
    log.debug message1
}


def calculateTrendNow(){
    cobra()
   state.previous = state.calc
    log.info "state.previous = $state.previous"
   state.calc = state.current
     log.info "state.current = $state.current"
    
    if(state.previous > state.current){ 
        state.trend = "Falling"
   		log.info "Temp Falling"
    }
   else if(state.previous < state.current){ 
       state.trend = "Rising"
   log.info "Temp Rising"
   } 
    else {
        state.trend = "Static"
        log.info "Temp Static"
         }
     sendEvent(name:"trend", value: state.trend)

    
}

// Check Version   *********************************************************************************
def version(){
    cobra()
    if (state.Type == "Application"){
    schedule("0 0 14 ? * FRI *", cobra)
    }
    if (state.Type == "Driver"){
    schedule("0 45 16 ? * MON *", cobra)
    }
}


def cobra(){
   
    setVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       try {
        httpGet(paramsUD) { respUD ->
//   log.info " Version Checking - Response Data: ${respUD.data}"
       def copyNow = (respUD.data.copyright)
       state.Copyright = copyNow
            def newver = (respUD.data.versions.(state.Type).(state.InternalName))
            def cobraVer = (respUD.data.versions.(state.Type).(state.InternalName).replace(".", ""))
       def cobraOld = state.version.replace(".", "")
       if(cobraOld < cobraVer){
		state.Status = "<b>** New Version Available (Version: $newver) **</b> " 
 		log.warn "** There is a newer version of this $state.Type available  (Version: $newver) ** "
       }    
       else{ 
      state.Status = "Current"
      log.info "$state.Type is the current version"
       }
       
       }
        } 
        catch (e) {
        log.error "Something went wrong: $e"
    }
}        



 
// App Version   *********************************************************************************
def setVersion(){
     state.version = "1.2.0"
     state.InternalName = "AverageTemp"
     state.Type = "Driver"
    
	sendEvent(name: "DriverAuthor", value: "Cobra", isStateChange: true)
    sendEvent(name: "DriverVersion", value: state.version, isStateChange: true)
    sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
}



