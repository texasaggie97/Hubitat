/**
 *  ****************  Temperature Controlled Switch  ****************
 *
 *
 *  Design Usage:
 *  This was designed to control a heater or cooler - Switching on/off around desired temperature
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
 *  Website: http://securendpoint.com/smartthings
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
 *  Created: 03/08/2017
 *  Last Update:25/10/2018
 *
 *  Changes:
 *
 *  V1.0.0 - POC - Initial port from ST
 *
 *  Author: Cobra
 */



definition(
    name: "Temperature Controlled Switch Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "This App was designed to control a heater - turning on/off with  varying temperatures. \r\nIt has an optional 'override' switch and configurable restrictions on when it can run",
    category: "",
    
   parent: "Cobra:Temperature Controlled Switch",
    
    iconUrl: "",
	iconX2Url: "",
    iconX3Url: "",
    )


preferences {
	page name: "inputPage", title: "", install: false, uninstall: true, nextPage: "introPage"
	page name: "introPage", title: "", install: false, uninstall: true, nextPage: "settingsPage"
    page name: "settingsPage", title: "", install: false, uninstall: true, nextPage: "namePage"
    page name: "namePage", title: "", install: true, uninstall: true

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
	version()
	logCheck()
    state.enable = "on"
    log.info "state.enable = $state.enable"
    getpresenceOk()
    
// Subscriptions ********************************************************************
	subscribe(temperatureSensor1, "temperature", temperatureHandler)
    if(switch1){ subscribe(switch1, "switch", switchEnableNow) } // Default - Enable/Disable switch
    if(ending){	schedule(ending, offNow) }
    if(contact1){ subscribe(contact1, "contact", contactHandler)}
    if(restrictPresenceSensor) {subscribe (restrictPresenceSensor, "presence", presenceHandler)}
    if(temperatureControl1) {subscribe (temperatureControl1, "temperature", customTempHandler1)}
    
   }


// main page *************************************************************************
def introPage() {
    dynamicPage(name: "introPage") {
   
    
    
       	section() {
        
		input "switch1", "capability.switch", title: "Select switch to enable/disable app (Optional)", required: false, multiple: false 
        }
    
	
        section("Only allow action if this sensor is 'Present'") {
        input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor (Optional)", required: false, multiple: false
    }  
}

}

// Settings Page ***************************************************
def settingsPage(){
	 dynamicPage(name: "settingsPage") {
     
     
 // BASIC SETTINGSS

		
    section("RunTime Settings") {
    input "starting", "time", title: "Start Time (Optional)", required: false
    input "ending", "time", title: "End Time (Optional)", required: false
    input "days", "enum", title: "Select Days of the Week (Optional)", required: false, multiple: true, options: ["Monday": "Monday", "Tuesday": "Tuesday", "Wednesday": "Wednesday", "Thursday": "Thursday", "Friday": "Friday", "Saturday": "Saturday", "Sunday": "Sunday"]
	
     
	}     
       
 }    
}

// Input Page  *********************************************************************
def inputPage(){
	 dynamicPage(name: "inputPage") {
         display()
          section() {       
        paragraph title: "Temperature Controlled Switch",
                  "This App was designed to control a heater or cooler - turning on/off with  varying temperatures. \r\nIt has optional, configurable restrictions on when it can run. \r\nIt also has an option for a 'Temperature Control/Thermostat' to control temperature setting."
                  }
         
         
     section() {
		input "temperatureSensor1", "capability.temperatureMeasurement" , title: "Select Temperature Sensor", required: true
        input "appmode", "bool", title: " Select mode of operation\r\n Off = Heating - On = Cooling", required: true, submitOnChange: true, defaultValue: false    
	}
    
    
    
    
	section() {
    
    input "tempMode", "bool", title: " Select mode of operation\r\n Off = Fixed Temperature - On = Variable Temperature (thermostat)", required: true, submitOnChange: true, defaultValue: false  
    if(tempMode == true){
    input "temperatureControl1", "capability.temperatureMeasurement" , title: "Select Controller/Thermostat", required: true
    
    }
   
    else{
		input "temperature1", "number", title: "Fixed Temperature?", required: true
	}
    
    }
   	section("Control this Switch/Heater/Cooler...") {
		input "switch2", "capability.switch", required: true, multiple: true
	}
    section("Switch off if this contact is open (Optional)") {
		input "contact1", "capability.contactSensor", required: false, multiple: true
	}
     
 }
}

// NamePage ***************************************************
def namePage() {
       dynamicPage(name: "namePage") {
       
            section("App name") {
                label title: "Enter a name for this app (Optional)", required: false
            }
            section() {
           		 input(name:"modes", type: "mode", title: "Set for specific mode(s) (Optional)", multiple: true, required: false)
            }    
             section("Logging") {
            input "debugMode", "bool", title: "Debug Logging (Optional)", required: true, defaultValue: false
  	        }
      }  
    }



// Handlers & Actions *****************************

def customTempHandler1(evt){
    log.warn "tempset - $evt.value"
state.reqTemp1 = evt.value
state.reqTemp = state.reqTemp1.toDouble()
LOGDEBUG("Required Temp set to: $state.reqTemp degrees by controller/thermostat: $temperatureControl1 ")
         runTemp() 
    }





def presenceHandler(evt){
state.presence1Now = evt.value
if (state.presence1Now == 'not present'){
	LOGDEBUG("Presence is $state.presence1Now - Switching off now...")
switch2.off()
	LOGDEBUG("$switch2 is OFF - Heating/Cooling Disabled")
	}
 else if (state.presence1Now == 'present'){
LOGDEBUG("Presence is $state.presence1Now - Heating/Cooling Allowed")
switch2.on()
	}
}



def contactHandler(evt){
	state.contact1Now = evt.value

if (state.contact1Now == 'open'){
	LOGDEBUG("Contact is $state.contact1Now - Switching off now...")
switch2.off()
	LOGDEBUG("$switch2 is OFF - Heating/Cooling Disabled")
	}
 else{
LOGDEBUG("Contact is $state.contact1Now - Heating/Cooling Allowed")
	}
}

def offNow(){
LOGDEBUG("Time expired.. Switching off now...")
switch2.off()
}



def temperatureHandler(evt) {
    state.newtemp1 = evt.value
	state.newTemp = state.newtemp1.toDouble()
// LOGDEBUG("requested temperature = $state.newTemp degrees")
	runTemp()
}









def runTemp(){
	def currTemp = state.newTemp
LOGDEBUG("Reported temperature is now: $currTemp degrees.")

	if(allOk){    
LOGDEBUG("All ok so can continue...")

	if(appmode == false){
	LOGDEBUG("Configured for heating mode")
    
	if(tempMode == true){ 
     state.confTemp = state.reqTemp
     LOGDEBUG("Configured to use variable temp controller - state.confTemp = $state.confTemp")
	}  
    else if(tempMode == false){
    state.confTemp = temperature1
     LOGDEBUG("Configured to use fixed temp - state.confTemp = $state.confTemp")
    }
   
    
	// Is reported temp below required setting?	
    if (currTemp < state.confTemp) {
	LOGDEBUG( "Reported temperature is below $state.confTemp so activating $switch2")
			switch2.on()
	}
    else if (currTemp >= state.confTemp) {
    LOGDEBUG( "Reported temperature is equal to, or above, $state.confTemp so deactivating $switch2")
			switch2.off()
	}
}

else if(appmode == true){
LOGDEBUG("Configured for cooling mode")

    if(tempMode == true){   
	state.confTemp = state.reqTemp
     LOGDEBUG("Configured to use variable temp controller - state.confTemp = $state.confTemp")
    }
    else if(tempMode == false){
    state.confTemp = temperature1
    LOGDEBUG("Configured to use fixed temp - state.confTemp = $state.confTemp")
    }

	// Is reported temp above setting?	
	if (currTemp > state.confTemp) {	
	LOGDEBUG( "Reported temperature is above $state.confTemp so activating $switch2")
			switch2.on()
	}
    else if (currTemp <= state.confTemp) {
    LOGDEBUG( "Reported temperature is equal to, or below, $state.confTemp so deactivating $switch2")
			switch2.off()
	}
  }
}
	else if(!allOk){
LOGDEBUG(" Not ok - one or more conditions are not met")
LOGDEBUG("modeOk = $modeOk - daysOk = $daysOk - timeOk = $timeOk - enableOk = $enableOk - presenceOK = $presenceOk")
	}
}


// Check if ok to run *** (Time, Mode, Day & Enable Switch) ************************************

// disable/enable switch
def switchEnableNow(evt){
	state.enable = evt.value
	LOGDEBUG( "Enable/Disable switch: $switch1 is $state.enable")
	if (state.enable == "off" ) {
		switch2.off()
   }
}


private getAllOk() {
	modeOk && daysOk && timeOk && enableOk && contactOk && presenceOk
}


private getpresenceOk() {
	def result = true
		if (state.presence1Now == 'not present' ) {
	result = false
	}
    else  {
	result = true
    }
    LOGDEBUG("presenceOk = $result")
	result
}




private getcontactOk() {
	def result = true
		if (state.contact1Now != 'open' ) {
	result = true
	}
    else if (state.contact1Now == 'open' ) {
	result = false
    }
    LOGDEBUG("contactOk = $result")
	result
}


private getModeOk() {
	def result = !modes || modes.contains(location.mode)
	LOGDEBUG("modeOk = $result")
	result
}



private getDaysOk() {
	def result = true
	if (days) {
		def df = new java.text.SimpleDateFormat("EEEE")
		if (location.timeZone) {
			df.setTimeZone(location.timeZone)
		}
		else {
			df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
		}
		def day = df.format(new Date())
		result = days.contains(day)
	}
	LOGDEBUG("daysOk = $result")
	result
}

private getTimeOk() {
	def result = true
	if (starting && ending) {
		def currTime = now()
		def start = timeToday(starting).time
		def stop = timeToday(ending).time
		result = start < stop ? currTime >= start && currTime <= stop : currTime <= stop || currTime >= start
	}
	LOGDEBUG("timeOk = $result")
	result
}

private hhmm(time, fmt = "h:mm a"){
	def t = timeToday(time, location.timeZone)
	def f = new java.text.SimpleDateFormat(fmt)
	f.setTimeZone(location.timeZone ?: timeZone(time))
	f.format(t)
}

private getTimeIntervalLabel(){
	(starting && ending) ? hhmm(starting) + "-" + hhmm(ending, "h:mm a z") : ""
}


private getenableOk(){
	def result = true
	if(state.enable == 'on'){result = true }
	else if(state.enable == 'off'){result = false }
	LOGDEBUG("enableOk = $result")
	result
}



private hideOptionsSection() {
	(starting || ending || days || modes) ? false : true
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
    	if (settings.debugMode) { log.debug("${app.label.replace(" ",".").toUpperCase()}  (AppVersion: ${state.appversion})  ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}


def version(){
    resetBtnName()
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
	paragraph "<b>Update Info:</b> <BR>$state.UpdateInfo <BR>$state.updateURI"
     }
    }
	section(" ") {
      input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available", required: true, defaultValue: false, submitOnChange: true 
      if(updateNotification == true){ input "speaker", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
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
    if(state.btnCall == "updateBtn1"){
    state.btnName1 = "Click Here" 
    httpGet("https://github.com/CobraVmax/Hubitat/tree/master/Apps' target='_blank")
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
    
def pushOverUpdate(inMsg){
    if(updateNotification == true){  
     newMessage = inMsg
  LOGDEBUG(" Message = $newMessage ")  
     state.msg1 = '[L]' + newMessage
	speaker.speak(state.msg1)
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
            def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
            state.updateURI = updateUri   
            
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
            def updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
            pushOverUpdate(updateMsg)
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
		state.InternalName = "TempControlledSwitchChild"
    	state.ExternalName = "Temperature Controlled Switch Child"
}

