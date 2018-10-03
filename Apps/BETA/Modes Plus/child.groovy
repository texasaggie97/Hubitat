/**
 *  **************** Modes Plus Child ****************
 *
 *  Design Usage:
 *  This was designed to use various triggers to control location modes
 *  Building on the ideas of HE 'Mode Manager' 
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
 *  Last Update: 03/10/2018
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
    name: "Modes Plus Child",
    namespace: "Cobra",
    author: "AJ Parker",
    description: "This was designed to use various triggers to control location modes",
    category: "My Apps",
    
parent: "Cobra:Modes Plus",
    
    iconUrl: "",
    iconX2Url: ""
)
preferences {

section("") {
        page name: "mainPage", title: "", install: true, uninstall: true
		}
   
    
}    
    

 def mainPage() {
    dynamicPage(name: "mainPage") {  
        
       display()
        
 section("") {
   input "newMode1", "mode", title: "Which Mode do you want to enter?",  required: true, multiple: false, submitOnChange:true
     if(newMode1){
    input "triggerMode", "enum", required: true, title: "Select Trigger ", submitOnChange: true,  options: ["Button", "Presence - Arrival", "Presence - Departure", "Sunrise", "Sunset", "Switch", "Time"]
         
          if(triggerMode == "Switch"){input "switch2", "capability.switch", title: "Select Switch", required: true, multiple: false}
          if(triggerMode == "Button"){
                input "button1", "capability.pushableButton", title: "Select Button Device", required: true, multiple: false, submitOnChange: true
              if(button1){
            	input "buttonNumber", "enum", title: "Enter Button Number", required: true, options: ["1", "2", "3", "4", "5"] 
              }
            }
         if(triggerMode == "Presence - Arrival" || triggerMode == "Presence - Departure"){
         input "presence1", "capability.presenceSensor", title: "Select Presence Sensor", required: true, multiple: false
         
         }
          if(triggerMode == "Time"){input (name: "runTime", title: "Time to run", type: "time",  required: true)}       
     }
    	 if(triggerMode == "Sunset"){	
       			input "sunsetOffsetValue", "number", title: "Optional Sunset Offset (Minutes)", required: false
				input "sunsetOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        }
     if(triggerMode == "Sunrise"){
    			input "sunriseOffsetValue", "number", title: "Optional Sunrise Offset (Minutes)", required: false
				input "sunriseOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
         
     }
         
 }  
        section() {
        label title: "Enter a name for this child app (optional)", required: false
            }    
       section(" ") {
            input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false
  	        }
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
   log.info "Initialised with settings: ${settings}"
  	schedule("0 0 10 1/1 * ? *", astroCheck)	// checks sunrise/sunset change at 10.00am every day 
version()
logCheck()
    if(triggerMode == "Sunrise" || triggerMode == "Sunset"){
    astroCheck()
    }
   if(triggerMode == "Sunrise"){
       state.sunRiseGo = true
       state.sunSetGo = false
   }
   if(triggerMode == "Sunset"){
       state.sunSetGo = true
   	   state.sunRiseGo = false
   } 
    
   
    if(triggerMode == "Switch"){subscribe(switch2, "switch.on", evtHandler)}
     if(triggerMode == "Button"){
        if(buttonNumber == '1'){subscribe(button1, "pushed.1", evtHandler)}
        if(buttonNumber == '2'){subscribe(button1, "pushed.2", evtHandler)}
        if(buttonNumber == '3'){subscribe(button1, "pushed.3", evtHandler)}
        if(buttonNumber == '4'){subscribe(button1, "pushed.4", evtHandler)}
        if(buttonNumber == '5'){subscribe(button1, "pushed.5", evtHandler)}
    } 
    if(triggerMode == "Time"){schedule(runTime, evtHandler)}
    if(triggerMode == "Presence - Arrival"){ subscribe(presence1, "presence", presenceHandler1)}
    if(triggerMode == "Presence - Departure"){ subscribe(presence1, "presence", presenceHandler1)}
}





def astroCheck() {
LOGDEBUG("Calling astroCheck...")


	def s = getSunriseAndSunset(sunriseOffset: sunriseOffset, sunsetOffset: sunsetOffset)
	def now = new Date()
	def riseTime = s.sunrise
	def setTime = s.sunset
	LOGDEBUG("riseTime: $riseTime")
	LOGDEBUG("setTime: $setTime")

	if (state.riseTime != riseTime.time) {
		unschedule("sunriseHandler")

		if(riseTime.before(now)) {
			riseTime = riseTime.next()
		}

		state.riseTime = riseTime.time

		LOGDEBUG("Scheduling sunrise handler for $riseTime")
		schedule(riseTime, sunriseHandler)
	}

	if (state.setTime != setTime.time) {
		unschedule("sunsetHandler")

	    if(setTime.before(now)) {
		    setTime = setTime.next()
	    }

		state.setTime = setTime.time

		LOGDEBUG("Scheduling sunset handler for $setTime")
	    schedule(setTime, sunsetHandler)
	
  }
   LOGDEBUG("AstroCheck Complete")
}


def sunsetHandler(evt) {
LOGDEBUG("Sun has set!")
    if(state.sunSetGo == true){
   eventHandler(Sunset)
    }
}


def sunriseHandler(evt) {
LOGDEBUG("Sun has risen!")
    if(state.sunRiseGo == true){
     eventHandler(Sunrise)
    }
}

private getSunriseOffset() {
	sunriseOffsetValue ? (sunriseOffsetDir == "Before" ? "-$sunriseOffsetValue" : sunriseOffsetValue) : null
}

private getSunsetOffset() {
	sunsetOffsetValue ? (sunsetOffsetDir == "Before" ? "-$sunsetOffsetValue" : sunsetOffsetValue) : null
}


def presenceHandler1(evt){
    state.presenceNow = evt.value

    if(triggerMode == "Presence - Arrival" && state.presenceNow == "present"){evtHandler(evt)}
    if(triggerMode == "Presence - Departure" && state.presenceNow == "not present"){evtHandler(evt)}    
        
}
def evtHandler(evt){
    if(triggerMode != "Time"){
    LOGDEBUG("Running eventHandler - Event received: $evt.value")
    }    
    state.requiredMode = newMode1

    LOGDEBUG("Changing to: $state.requiredMode ")
    setLocationMode(state.requiredMode) 
}



















// ******************************************************************************
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
    resetBtnName()
	unschedule()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
    checkButtons()
}

def display(){
  
	if(state.status){
	section{paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}
       
        }
    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){
     section(){ input "updateBtn", "button", title: "$state.btnName"}
    }
    
    if(state.status != "Current"){
	section{ 

	paragraph "<b>Update Info: $state.UpdateInfo ***</b>"
    }
         
    }         
}

def checkButtons(){
    log.info "Running checkButtons"
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
        log.info "Checking for updates now..."
        updateCheck()
        pause(3000)
  		state.btnName = state.newBtn
        runIn(2, resetBtnName)
    }
}   
def resetBtnName(){
    log.info "Resetting Button"
    if(state.status != "Current"){
	state.btnName = state.newBtn
    }
    else{
 state.btnName = "Check For Update" 
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
             state.newBtn = state.status
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
    if(state.status != "Current"){
		state.newBtn = state.status
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}

def setVersion(){
		state.version = "1.0.0"	 
		state.InternalName = "ModesPlusChild"
}





