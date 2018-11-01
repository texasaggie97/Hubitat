/**
 *  ****************  Average Illumination.  ****************
 *
 *  Design Usage:
 *  This was designed to display/set an 'average' or mean illumination from a group of illumination devices
 *
 *  This app was originally born from an idea by @bobgodbold and I thank him for that!
 *  @bobgodbold ported some of the original driver code from SmartThings before I adapted it to it's present form for use with this app
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
 *  Last Update: 23/08/2018
 *
 *  Changes:
 *
 *
 *
 *  V1.1.0 - Added ability to set how often data is sent to the virtual device
 *  V1.0.0 - POC
 *
 */



definition(
    name: "Average Illumination",
    namespace: "Cobra",
    author: "AJ Parker",
    description: "This was designed to display/set an 'average' or mean illumination from a group of illumination devices",
    category: "My Apps",

    iconUrl: "",
    iconX2Url: ""
)
preferences {

section("") {
         
		display()
}
    section(""){}
    section("Choose Physical Illuminance Sensors"){
        input "illumSensors", "capability.sensor", title: "Physical Sensors", multiple: true
    }
     section("Set Virtual Illuminance Sensor "){
        input "vDevice", "capability.sensor", title: "Virtual Device"
    }
     section(){
     input "sendInterval", "number", title: "How Often To Update Virtual Device (Minutes)", required: true, defaultValue: "0"
     }
    section("Logging") {
            input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false
  	        }
}

def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    initialize()
}

def initialize() {
  state.sendOK = true     
version()
logCheck()
    subscribe(illumSensors, "illuminance", illuminanceHandler)
}

def illuminanceHandler(evt) {
    LOGDEBUG("running illuminance handler")
        ave = evt.value
    LOGDEBUG( "received $ave")
    def sum = 0
    def count = 0
    def mean = 0

    for (sensor in settings.illumSensors) {
    count += 1 
LOGDEBUG( "Sensor data count = $count" )      
    sum += sensor.currentIlluminance }
LOGDEBUG( "Total Combined value =  $sum")

    state.mean = sum/count
    LOGDEBUG("Average Illuminance = $state.mean")

    if(state.sendOK == true){
        def timeCheck = 60 * sendInterval  
        LOGDEBUG("Sending info to $vDevice then waiting $timeCheck seconds before I can send again")
     settings.vDevice.setLux("${state.mean}")
        state.sendOK = false
       runIn(timeCheck, resetNow) 
     }
    else {
     LOGDEBUG("Waiting for timer to expire")  
    }
}                           


def resetNow(){
 LOGDEBUG("Timer reset")    
    state.sendOK = true
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
		state.version = "1.1.0"	 
		state.InternalName = "AverageIllum"
}





