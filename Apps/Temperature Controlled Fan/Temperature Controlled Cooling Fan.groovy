/**
 *  ****************  Temperature Controlled Cooling Fan  ****************
 *
 *
 *  Design Usage:
 *  This was designed to control a cooling fan - Switching on/off around desired temperatures
 *
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This App is free!
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
 *  Created: 11/08/2017
 *  Last Update:
 *
 *  Changes:
 *
 *  V1.0.0 - POC - Initial port from ST
 *
 *  Author: Cobra
 */



definition(
    name: "Temperature Controlled Cooling Fan",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "This App was designed to control a fan - turning on/off with  varying temperatures.",
    category: "",
    
   
    
    iconUrl: "",
	iconX2Url: "",
    iconX3Url: "",
    )


preferences {
	page name: "inputPage", title: "", install: false, uninstall: true, nextPage: "restrictionPage"
	page name: "restrictionPage", title: "", install: false, uninstall: true, nextPage: "settingsPage"
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
	logCheck()
    state.enable = "on"
    log.info "state.enable = $state.enable"
    getpresenceOk()
    version()
    
// Subscriptions ********************************************************************
	subscribe(temperatureSensor1, "temperature", temperatureHandler)
    if(switch1){ subscribe(switch1, "switch", switchEnableNow) } // Default - Enable/Disable switch
    if(ending){	schedule(ending, offNow) }
    if(restrictPresenceSensor) {subscribe (restrictPresenceSensor, "presence", presenceHandler)}
   
    
   }



def restrictionPage() {
    dynamicPage(name: "restrictionPage") {
   
    
    
       	section() {
        
		input "switch1", "capability.switch", title: "Select switch to enable/disable app (Optional)", required: false, multiple: false 
        }
    
	
        section("Only allow action if this sensor is 'Present'") {
        input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor (Optional)", required: false, multiple: false
    }  
}

}


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


def inputPage(){
	 dynamicPage(name: "inputPage") {
         
          section() {       
        paragraph title: "Temperature Controlled Fan",
                  "This App was designed to control a fan turning on/off with  varying temperatures. \r\nIt has optional, configurable restrictions on when it can run."
                  }
        display() 
         
     section() {
		input "temperatureSensor1", "capability.temperatureMeasurement" , title: "Select Temperature Sensor", required: true
       
	}
    
    
    
    
	section("Desired Temperature") {
    
  	input "temperature1", "number", title: "Temperature1 - Above this temp fan will be ON", required: true
    input "temperature2", "number", title: "Temperature2 - Below this temp fan will be OFF", required: true   
	}
   
   	section("Control this fan") {
		input "switch2", "capability.switch", required: true, multiple: true
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



def presenceHandler(evt){
state.presence1Now = evt.value
if (state.presence1Now == 'not present'){
	LOGDEBUG("Presence is $state.presence1Now - Switching off now...")
switch2.off()
	LOGDEBUG("$switch2 is OFF - Fan Disabled")
	}
 else if (state.presence1Now == 'present'){
LOGDEBUG("Presence is $state.presence1Now - Fan Allowed")
switch2.on()
	}
}




def offNow(){
LOGDEBUG("Time expired.. Switching off now...")
switch2.off()
}



def temperatureHandler(evt) {
    def newtemp1 = evt.value
	def newTemp = newtemp1.toDouble()
	runTemp(newTemp)
}









def runTemp(currTemp){
	
LOGDEBUG("Reported temperature is now: $currTemp degrees.")

	if(allOk){    
LOGDEBUG("All ok so can continue...")

	def confTempOff = temperature2
    def confTempOn = temperature1
   

    if (currTemp > confTempOn) {
	LOGDEBUG( "Reported temperature is above $confTempOn so activating $switch2")
			switch2.on()
	}
    if (currTemp < confTempOff) {
    LOGDEBUG( "Reported temperature is below $confTempOff so deactivating $switch2")
			switch2.off()
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
	unschedule()
	schedule("0 0 9 ? * FRI *", updateCheck) // Cron schedule - How often to perform the update check - (This example is 9am every Friday)
	updateCheck()  
}

def display(){
	if(state.Status){
	section{paragraph "Version: $state.version -  $state.Copyright"}
        
	if(state.Status != "Current"){
	section{ 
	paragraph "$state.Status"
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
 // log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.Status = "<b>** This app is no longer supported by $state.author  **</b> (But you may continue to use it)"       
            log.warn "** This app is no longer supported by $state.author **"      
      		}           
		else if(currentVer < newVer){
        	state.Status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
       		} 
		else{ 
      		state.Status = "Current"
      		log.info "You are using the current version of this app"
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
   		
    
    	//	
}




def setVersion(){
		state.version = "1.0.0"	 
		state.InternalName = "TCCFan"   
}



