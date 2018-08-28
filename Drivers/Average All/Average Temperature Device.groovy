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
 *  Changes: 28/08/2018
 *
 *
 *  V1.5.0 - Added 'Trend'
 *  V1.4.0 - Code cleanup and revised remote version checking for use with 'Average All' app also added 'unit' selection for dashboards
 *  V1.3.0 - Added 'setTemperature' and removed 'parse' button - This must be used with V1.3.0 of the app
 *  V1.2.0 - Debug
 *  V1.1.0 - Added remote version checking
 *  V1.0.0 - POC
 *
 */

metadata {
    definition (name:"Average Temperature Device Driver", namespace:"Cobra", author:"AJ Parker") {
        capability "Temperature Measurement"
        capability "Sensor"
        
		attribute "trend", "string"
        attribute "DriverVersion", "string"
        attribute "DriverAuthor", "string"
        attribute "DriverStatus", "string"
        command "calculateTrendNow"
        command "setTemperature", ["number"]
    }

 preferences() {
     
      section(){
        input "frequency", "number", required: true, title: "How often to check for trend (Minutes after temp change)", defaultValue: "30"  
       
  }   
     section("") {
   input "unitSelect", "enum", required: true, title: "Temperature Units", submitOnChange: true,  options: ["C", "F"] 
 }  
 }
}

def updated() {
    log.debug "Updated called"
    version()
   
}

def setTemperature(message){
    log.info "setTemperature(${message})"
    
def averageTemp = message
  state.current = averageTemp
  def checkFrequency = 60 * frequency
   runIn(checkFrequency, calculateTrendNow) 

    log.info "event: (${averageTemp})"
    
    if(unitSelect == "C"){ 
    sendEvent(name:"temperature", value: "$averageTemp" , unit: "C")
    }
   if(unitSelect == "F"){ 
    sendEvent(name:"temperature", value: "$averageTemp" , unit: "F")
    } 
    
}


def calculateTrendNow(){
    
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

def version(){
    unschedule()
    schedule("0 0 8 ? * FRI *", updateCheck)  
    updateCheck()
}

def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://update.hubitat.uk/cobra.json" ]  
       	try {
        httpGet(paramsUD) { respUD ->
 //  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code **********************
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Driver.(state.InternalName))
            def newVer = (respUD.data.versions.Driver.(state.InternalName).replace(".", ""))
       		def currentVer = state.Version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Driver.(state.InternalName))
            state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.Status = "<b>** This driver is no longer supported by $state.author  **</b>"       
            log.warn "** This driver is no longer supported by $state.author **"      
      		}           
		else if(currentVer < newVer){
        	state.Status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this driver available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
       		} 
		else{ 
      		state.Status = "Current"
      		log.info "You are using the current version of this driver"
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
   		if(state.Status == "Current"){
			state.UpdateInfo = "N/A"
		    sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	 	    sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
			}
    	else{
	    	sendEvent(name: "DriverUpdate", value: state.UpdateInfo, isStateChange: true)
	     	sendEvent(name: "DriverStatus", value: state.Status, isStateChange: true)
	    }   
 			sendEvent(name: "DriverAuthor", value: state.author, isStateChange: true)
    		sendEvent(name: "DriverVersion", value: state.Version, isStateChange: true)
    
    
    	//	
}

def setVersion(){
		state.Version = "1.5.0"	
		state.InternalName = "AverageTemp"  
}


 