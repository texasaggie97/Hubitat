/**
 *  ****************  Super Smart Fan.  ****************
 *
 *  Design Usage:
 *  This was designed to control a bathroom fan - switching with humidity, motion etc
 *
 *  
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This SmartApp is free!
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this app without a donation, but if you find it useful then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this app better?  - Please let me know :)
 *
 *  Website: http://hubitat.uk
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
 *  Last Update: 29/08/2018
 *
 *  Changes:
 *
 *
 *
 *  
 *  V1.0.0 - POC
 *
 */



definition(
    name: "Super Smart Fan",
    namespace: "Cobra",
    author: "AJ Parker",
    description: "This was designed to control a bathroom fan - switching with humidity, motion etc",
    category: "My Apps",

    iconUrl: "",
    iconX2Url: ""
)

preferences {
     page name: "mainPage", title: "", install: true, uninstall: true
}




// main page *****************************************************************************************************************
  def mainPage() {
    dynamicPage(name: "mainPage") {  
        

        

section("") {
         
		display()
}
 
        
    section("<b>Humidity Sensors</b>"){
        input "baselineSensors", "capability.sensor", title: "Select Baseline Sensor", required: true, multiple: false
        input "bathroomSensor", "capability.sensor", title: "Select Bathroom Sensor", required: true, multiple: false
    }
   section("<b>Bathroom Motion Sensor (Optional)</b>"){     
   input "motion1", "capability.motionSensor", title: "Select Motion Sensor", required: false, multiple: false, submitOnChange: true     
   if(motion1){input "thresholdMotion", "number", title: "If activated by motion - How long to keep fan on after motion stops (minutes)", required: true, defaultValue: "2" }     

        }
        
            section("<b>Bathroom Fan</b>"){
     input(name: "fanSwitch1", type: "capability.switch", title: "Fan to control", required: true, multiple: false, submitOnChange: true)
     if(fanSwitch1){input "thresholdSwitch", "number", title: "How long to keep fan on after manual switch on (minutes)", required: true, defaultValue: "2"}

       }
     
        
      
   //   section(" "){}
        
     section("<b>Thresholds</b>"){
     input "thresholdHigh", "number", title: "High Threshold (% humidity above baseline to switch on)", required: true, defaultValue: "25"
     input "thresholdLow", "number", title: "Low Threshold (% humidity above baseline to switch off)", required: true, defaultValue: "5"
     }
        
        
    section("") {       
    mode title: "Run only when in specific mode(s) ", required: false
    input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false
  	        }
}
      
  }      
      
      
      
      

def installed() {
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
     
	version()
	logCheck()
    subscribe(baselineSensors, "humidity", baseLineHandler)
    subscribe(bathroomSensor, "humidity", bathroomSensorHandler)
    subscribe(fanSwitch1, "switch", fanSwitch1Handler)
    if("motion1"){subscribe(motion1, "motion", motion1Handler)}
    
    state.humidityNow = false
    state.motionNow = "inactive"
    state.baseline = 0
    state.highThresh = 0
    state.lowThresh = 0
}






def baseLineHandler(evt) {
    LOGDEBUG("Running humidity baseLineHandler")
    state.baseline = evt.value.toFloat()
    LOGDEBUG("state.baseline = $state.baseline")
} 
    
def bathroomSensorHandler(evt) {

LOGDEBUG("Running humidity bathroomSensorHandler")
LOGDEBUG("bathroom sensor = $evt.value")
	def bathSensor1 = evt.value.toFloat()
    state.bathSensor = bathSensor1
   
LOGDEBUG("Bathroom Humidity Sensor = $state.bathSensor")
    switchHumidity()
}


def switchHumidity(){
    LOGDEBUG("Running switchHumidity")
    state.highThresh = thresholdHigh.toFloat()
    state.lowThresh = thresholdLow.toFloat()
        
    state.highNow = (state.baseline + state.highThresh) 
    LOGDEBUG("state.highNow = $state.highNow")
    state.lowNow = (state.baseline + state.lowThresh) 
    LOGDEBUG("state.lowNow = $state.lowNow")
    
    if(state.bathSensor >= state.highNow){
        state.humidityNow = true
        fanOn()
    }
    if(state.bathSensor <= state.lowNow){
        state.humidityNow = false
        fanOff()
    }
}



def fanSwitch1Handler(evt){
    LOGDEBUG("Running fanSwitch1Handler")
    state.fanSwitchNow = evt.value
    if(state.fanSwitchNow == 'on'){
     fanOn()
       
    if(state.humidityNow == false && state.motionNow == 'inactive'){
    def thresholdSwitch1 = thresholdSwitch
    def resetTime = 60 * thresholdSwitch1
LOGDEBUG("Humidity is low - Turning off fan in $resetTime seconds")
       runIn(resetTime,resetNow) 
    } 
    }
   if(state.fanSwitchNow == 'off'){ 
LOGDEBUG("Fan is now off")        
   }   
    
}


def motion1Handler(evt){
    LOGDEBUG("Running motion1Handler")
    state.motionNow = evt.value
    LOGDEBUG("state.motionNow = $state.motionNow")
    
    thresholdMotion1 = thresholdMotion
    if(state.motionNow == "active"){
     fanOn()   
    }
    if(state.motionNow == "inactive" && state.humidityNow == false){
    def thresholdMotion1 = thresholdMotion    
    def resetTime = 60 * thresholdMotion1  
    LOGDEBUG("Humidity is low and motion has stopped - Turning off fan in $resetTime seconds")    
    runIn(resetTime, resetNow)    
    }
    
}





def fanOn(){
LOGDEBUG("Switching fan ON")       
   fanSwitch1.on()     
    }

def fanOff(){
LOGDEBUG("Switching fan OFF")         
    fanSwitch1.off()    
    }





def resetNow(){
 LOGDEBUG("Timer reset")    
    fanSwitch1.off()
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
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
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
		state.version = "1.0.0"	 
		state.InternalName = "SuperSmartFan"
}





