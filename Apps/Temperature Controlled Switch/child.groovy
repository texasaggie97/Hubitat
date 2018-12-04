/**
 *  ****************  Temperature Controlled Switch  ****************
 *
 *
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
 *  
 *  Last Update: 04/12/2018
 *  Changes:
 *
 *  V1.5.0 - Added disable apps code
 *  V1.4.0 - Streamlined restrictions page to action faster if specific restrictions not used.
 *  V1.3.0 - Moved update notification to parent
 *  V1.2.0 - Code Cleanup
 *  V1.1.0 - Added restrictions page
 *  V1.0.0 - POC - Initial port from ST
 *
 *  Author: Cobra
 */



definition(
    name: "Temperature Controlled Switch Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "This App was designed to control a heater (switch) - turning on/off with  varying temperatures. <br>It has an optional, configurable restrictions on when it can run",
    category: "",
    
   parent: "Cobra:Temperature Controlled Switch",
    
    iconUrl: "",
	iconX2Url: "",
    iconX3Url: "",
    )

preferences {
	section() {
	page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
	page name: "restrictionsPage", title: "", install: true, uninstall: true
	}
}
        
          
def installed(){initialise()}
def updated(){initialise()}
def initialise(){
	version()
	subscribeNow()
	log.info "Initialised with settings: ${settings}"
	logCheck()	
}
def subscribeNow() {
	unsubscribe()
	if(enableSwitch){subscribe(enableSwitch, "switch", switchEnable)}
	if(enableSwitchMode == null){enableSwitchMode = true} // ????
	if(restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if(restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
	if(sunriseSunset){astroCheck()}
	if(sunriseSunset){schedule("0 1 0 1/1 * ? *", astroCheck)} // checks sunrise/sunset change at 00.01am every day
    
  // App Specific subscriptions & settings below here

    
	subscribe(temperatureSensor1, "temperature", temperatureHandler)
    if(ending){	schedule(ending, offNow) }
    if(contact1){ subscribe(contact1, "contact", contactHandler)}
     
   }

def mainPage() {
	dynamicPage(name: "mainPage") {
	preCheck()
       
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
def restrictionsPage() {
    dynamicPage(name: "restrictionsPage") {
        section(){paragraph "<font size='+1'>App Restrictions</font> <br>These restrictions are optional <br>Any restriction you don't want to use, you can just leave blank or disabled"}
        section(){
		input "enableSwitchYes", "bool", title: "Enable restriction by external on/off switch", required: true, defaultValue: false, submitOnChange: true
			if(enableSwitchYes){
			input "enableSwitch", "capability.switch", title: "Select a switch Enable/Disable this app", required: false, multiple: false, submitOnChange: true 
			if(enableSwitch){ input "enableSwitchMode", "bool", title: "Allow app to run only when this switch is On or Off", required: true, defaultValue: false, submitOnChange: true}
			}
		}
        section(){
		input "modesYes", "bool", title: "Enable restriction by current mode(s)", required: true, defaultValue: false, submitOnChange: true	
			if(modesYes){	
			input(name:"modes", type: "mode", title: "Allow actions when current mode is:", multiple: true, required: false)
			}
		}	
       	section(){
		input "timeYes", "bool", title: "Enable restriction by time", required: true, defaultValue: false, submitOnChange: true	
			if(timeYes){	
    	input "fromTime", "time", title: "Allow actions from", required: false
    	input "toTime", "time", title: "Allow actions until", required: false
        	}
		}
		section(){
		input "dayYes", "bool", title: "Enable restriction by day(s)", required: true, defaultValue: false, submitOnChange: true	
			if(dayYes){	
    	input "days", "enum", title: "Allow actions only on these days of the week", required: false, multiple: true, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        	}
		}
		section(){
		input "presenceYes", "bool", title: "Enable restriction by presence sensor(s)", required: true, defaultValue: false, submitOnChange: true	
			if(presenceYes){	
    	input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor){input "restrictPresenceAction", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
     	input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor1){input "restrictPresenceAction1", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
   			}
		}	
		section(){
		input "sunrisesetYes", "bool", title: "Enable restriction by sunrise or sunset", required: true, defaultValue: false, submitOnChange: true	
			if(sunrisesetYes){
       	input "sunriseSunset", "enum", title: "Sunrise/Sunset Restriction", required: false, submitOnChange: true, options: ["Sunrise","Sunset"] 
		if(sunriseSunset == "Sunset"){	
       	input "sunsetOffsetValue", "number", title: "Optional Sunset Offset (Minutes)", required: false
		input "sunsetOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        	}
		if(sunriseSunset == "Sunrise"){
    	input "sunriseOffsetValue", "number", title: "Optional Sunrise Offset (Minutes)", required: false
		input "sunriseOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        	}
     	}
		}	
       
        section() {input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}
		 section() {label title: "Enter a name for this automation", required: false}
    }
}



// Handlers & Actions *****************************

def customTempHandler1(evt){
	checkAllow()
	if(state.allAllow == true){

//    log.warn "tempset - $evt.value"
state.reqTemp1 = evt.value
state.reqTemp = state.reqTemp1.toDouble()
LOGDEBUG("Required Temp set to: $state.reqTemp degrees by controller/thermostat: $temperatureControl1 ")
         runTemp() 
    }
}

def contactHandler(evt){
	state.contact1Now = evt.value
	checkAllow()
	if(state.allAllow == true){


if (state.contact1Now == 'open'){
	LOGDEBUG("Contact is $state.contact1Now - Switching off now...")
switch2.off()
	LOGDEBUG("$switch2 is OFF - Heating/Cooling Disabled")
	}
 else{
LOGDEBUG("Contact is $state.contact1Now - Heating/Cooling Allowed")
  }	
 }
}

def offNow(){
LOGDEBUG("Time expired.. Switching off now...")
switch2.off()
}



def temperatureHandler(evt) {
	checkAllow()
	if(state.allAllow == true){

    state.newtemp1 = evt.value
	state.newTemp = state.newtemp1.toDouble()
// LOGDEBUG("requested temperature = $state.newTemp degrees")
	runTemp()
	}
}








def runTemp(){
	checkAllow()
	if(state.allAllow == true){

	def currTemp = state.newTemp
LOGDEBUG("Reported temperature is now: $currTemp degrees.")
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
}
def checkAllow(){
    state.allAllow = false
    LOGDEBUG("Checking for any restrictions...")
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){
        LOGDEBUG("Continue - App NOT paused")
        state.noPause = true
		state.modeCheck = true
		state.presenceRestriction = true
		state.presenceRestriction1 = true
		state.dayCheck = true
		state.sunGoNow = true
		state.timeOK = true
		state.modes = modes
		state.fromTime = fromTime
		state.days = days
		state.sunriseSunset = sunriseSunset
		state.restrictPresenceSensor = restrictPresenceSensor
		state.restrictPresenceSensor1 = restrictPresenceSensor1
		state.timeYes = timeYes
		state.enableSwitchYes = enableSwitchYes
		state.modesYes = modesYes
		state.dayYes = dayYes
		state.sunrisesetYes = sunrisesetYes
		
		if(state.enableSwitchYes == false){state.appgo = true}
		if(state.modes != null && state.modesYes == true){modeCheck()}	
		if(state.fromTime !=null && state.timeYes == true){checkTime()}
		if(state.days!=null && state.dayYes == true){checkDay()}
		if(state.sunriseSunset !=null && state.sunrisesetYes == true){checkSun()}
		if(state.restrictPresenceSensor != null && state.presenceYes == true){checkPresence()}
        if(state.restrictPresenceSensor1 != null && state.presenceYes == true){checkPresence1()}
 
	if(state.modeCheck == false){
	LOGDEBUG("Not in correct 'mode' to continue")
	    }    
	if(state.presenceRestriction ==  false || state.presenceRestriction1 ==  false){
	LOGDEBUG( "Cannot continue - Presence failed")
	}
	if(state.appgo == false){
	LOGDEBUG("$enableSwitch is not in the correct position so cannot continue")
	}
	if(state.appgo == true && state.dayCheck == true && state.presenceRestriction == true && state.presenceRestriction1 == true && state.modeCheck == true && state.timeOK == true && state.noPause == true && state.sunGoNow == true){
	state.allAllow = true 
 	  }
	else{
 	state.allAllow = false
	LOGWARN( "One or more restrictions apply - Unable to continue")
 	LOGDEBUG("state.appgo = $state.appgo, state.dayCheck = $state.dayCheck, state.presenceRestriction = $state.presenceRestriction, state.presenceRestriction1 = $state.presenceRestriction1, state.modeCheck = $state.modeCheck, state.timeOK = $state.timeOK, state.noPause = $state.noPause, state.sunGoNow = $state.sunGoNow")
      }
   }

}

def checkSun(){
	LOGDEBUG("Checking Sunrise/Sunset restrictions...")
	if(!sunriseSunset){
        state.sunGoNow = true
        LOGDEBUG("No Sunrise/Sunset restrictions in place")	
	}
        if(sunriseSunset){
        if(sunriseSunset == "Sunset"){	
        if(state.astro == "Set"){
        state.sunGoNow = true
        LOGDEBUG("Sunset OK")
            } 
    	if(state.astro == "Rise"){
        state.sunGoNow = false
        LOGDEBUG("Sunset NOT OK")
            } 
        }
	if(sunriseSunset == "Sunrise"){	
        if(state.astro == "Rise"){
        state.sunGoNow = true
        LOGDEBUG("Sunrise OK")
            } 
    	if(state.astro == "Set"){
        state.sunGoNow = false
        LOGDEBUG("Sunrise NOT OK")
            } 
        }  
    } 
		return state.sunGoNow
}    

def astroCheck() {
    state.sunsetOffsetValue1 = sunsetOffsetValue
    state.sunriseOffsetValue1 = sunriseOffsetValue
    if(sunsetOffsetDir == "Before"){state.sunsetOffset1 = -state.sunsetOffsetValue1}
    if(sunsetOffsetDir == "after"){state.sunsetOffset1 = state.sunsetOffsetValue1}
    if(sunriseOffsetDir == "Before"){state.sunriseOffset1 = -state.sunriseOffsetValue1}
    if(sunriseOffsetDir == "after"){state.sunriseOffset1 = state.sunriseOffsetValue1}
	def both = getSunriseAndSunset(sunriseOffset: state.sunriseOffset1, sunsetOffset: state.sunsetOffset1)
	def now = new Date()
	def riseTime = both.sunrise
	def setTime = both.sunset
	LOGDEBUG("riseTime: $riseTime")
	LOGDEBUG("setTime: $setTime")
	unschedule("sunriseHandler")
	unschedule("sunsetHandler")
	if (riseTime.after(now)) {
	LOGDEBUG("scheduling sunrise handler for $riseTime")
	runOnce(riseTime, sunriseHandler)
		}
	if(setTime.after(now)) {
	LOGDEBUG("scheduling sunset handler for $setTime")
	runOnce(setTime, sunsetHandler)
		}
	LOGDEBUG("AstroCheck Complete")
}

def sunsetHandler(evt) {
	LOGDEBUG("Sun has set!")
	state.astro = "Set" 
}
def sunriseHandler(evt) {
	LOGDEBUG("Sun has risen!")
	state.astro = "Rise"
}

def modeCheck() {
    LOGDEBUG("Checking for any 'mode' restrictions...")
	def result = !modes || modes.contains(location.mode)
    LOGDEBUG("Mode = $result")
    state.modeCheck = result
    return state.modeCheck
 }



def checkTime(){
    LOGDEBUG("Checking for any time restrictions")
	def timecheckNow = fromTime
	if (timecheckNow != null){
    
def between = timeOfDayIsBetween(toDateTime(fromTime), toDateTime(toTime), new Date(), location.timeZone)
    if (between) {
    state.timeOK = true
   LOGDEBUG("Time is ok so can continue...")
    
}
	else if (!between) {
	state.timeOK = false
	LOGDEBUG("Time is NOT ok so cannot continue...")
	}
  }
	else if (timecheckNow == null){  
	state.timeOK = true
  	LOGDEBUG("Time restrictions have not been configured -  Continue...")
  }
}



def checkDay(){
    LOGDEBUG("Checking for any 'Day' restrictions")
	def daycheckNow = days
	if (daycheckNow != null){
 	def df = new java.text.SimpleDateFormat("EEEE")
    df.setTimeZone(location.timeZone)
    def day = df.format(new Date())
    def dayCheck1 = days.contains(day)
    if (dayCheck1) {
	state.dayCheck = true
	LOGDEBUG( "Day ok so can continue...")
 }       
 	else {
	LOGDEBUG( "Cannot run today!")
 	state.dayCheck = false
 	}
 }
if (daycheckNow == null){ 
	LOGDEBUG("Day restrictions have not been configured -  Continue...")
	state.dayCheck = true 
	} 
}

def restrictPresenceSensorHandler(evt){
	state.presencestatus1 = evt.value
	LOGDEBUG("state.presencestatus1 = $evt.value")
	checkPresence()
}



def checkPresence(){
	LOGDEBUG("Running checkPresence - restrictPresenceSensor = $restrictPresenceSensor")
	if(restrictPresenceSensor){
	LOGDEBUG("Presence = $state.presencestatus1")
	def actionPresenceRestrict = restrictPresenceAction
	if (state.presencestatus1 == "present" && actionPresenceRestrict == true){
	LOGDEBUG("Presence ok")
	state.presenceRestriction = true
	}
	if (state.presencestatus1 == "not present" && actionPresenceRestrict == true){
	LOGDEBUG("Presence not ok")
	state.presenceRestriction = false
	}

	if (state.presencestatus1 == "not present" && actionPresenceRestrict == false){
	LOGDEBUG("Presence ok")
	state.presenceRestriction = true
	}
	if (state.presencestatus1 == "present" && actionPresenceRestrict == false){
	LOGDEBUG("Presence not ok")
	state.presenceRestriction = false
	}
}
	else if(restrictPresenceSensor == null){
	state.presenceRestriction = true
	LOGDEBUG("Presence sensor restriction not used")
	}
}


def restrictPresence1SensorHandler(evt){
	state.presencestatus2 = evt.value
	LOGDEBUG("state.presencestatus2 = $evt.value")
	checkPresence1()
}


def checkPresence1(){
	LOGDEBUG("running checkPresence1 - restrictPresenceSensor1 = $restrictPresenceSensor1")
	if(restrictPresenceSensor1){
	LOGDEBUG("Presence = $state.presencestatus1")
	def actionPresenceRestrict1 = restrictPresenceAction1
	if (state.presencestatus2 == "present" && actionPresenceRestrict1 == true){
	LOGDEBUG("Presence 2 ok - Continue..")
	state.presenceRestriction1 = true
	}
	if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == true){
	LOGDEBUG("Presence 2 not ok")
	state.presenceRestriction1 = false
	}
	if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == false){
	LOGDEBUG("Presence 2 ok - Continue..")
	state.presenceRestriction1 = true
	}
	if (state.presencestatus2 == "present" && actionPresenceRestrict1 == false){
	LOGDEBUG("Presence 2 not ok")
	state.presenceRestriction1 = false
	}
  }
	if(restrictPresenceSensor1 == null){
	state.presenceRestriction1 = true
	LOGDEBUG("Presence sensor 2 restriction not used - Continue..")
	}
}

def switchEnable(evt){
	state.enableInput = evt.value
	LOGDEBUG("Switch changed to: $state.enableInput")  
    if(enableSwitchMode == true && state.enableInput == 'off'){
	state.appgo = false
	LOGDEBUG("Cannot continue - App disabled by switch")  
    }
	if(enableSwitchMode == true && state.enableInput == 'on'){
	state.appgo = true
	LOGDEBUG("Switch restriction is OK.. Continue...") 
    }    
	if(enableSwitchMode == false && state.enableInput == 'off'){
	state.appgo = true
	LOGDEBUG("Switch restriction is OK.. Continue...")  
    }
	if(enableSwitchMode == false && state.enableInput == 'on'){
	state.appgo = false
	LOGDEBUG("Cannot continue - App disabled by switch")  
    }    
	LOGDEBUG("Allow by switch is $state.appgo")
}

def version(){
	setDefaults()
	pauseOrNot()
	logCheck()
	resetBtnName()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	checkButtons()
   
}






def logCheck(){
    state.checkLog = debugMode
    if(state.checkLog == true){log.info "All Logging Enabled"}
    if(state.checkLog == false){log.info "Further Logging Disabled"}
}

def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}
def LOGWARN(txt){
    try {
    	if (settings.debugMode) { log.warn("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGWARN unable to output requested data!")
    }
}



def display(){
    setDefaults()
    if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}}
    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){section(){input "updateBtn", "button", title: "$state.btnName"}}
    if(state.status != "Current"){section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>Update URL: </b><font color = 'red'> $state.updateURI</font><hr>"}}
    section(){input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false }
}



def checkButtons(){
    LOGDEBUG("Running checkButtons")
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
    LOGDEBUG("Checking for updates now...")
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
    LOGDEBUG("Resetting Button")
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
    speakerUpdate.speak(state.msg1)
    }
}

def pauseOrNot(){
LOGDEBUG(" Calling 'pauseOrNot'...")
    state.pauseNow = pause1
    if(state.pauseNow == true){
    state.pauseApp = true
    if(app.label){
    if(app.label.contains('red')){
    log.warn "Paused"}
    else{app.updateLabel(app.label + ("<font color = 'red'> (Paused) </font>" ))
    log.warn "App Paused - state.pauseApp = $state.pauseApp "   
    }
   }
  }
    if(state.pauseNow == false){
    state.pauseApp = false
    if(app.label){
    if(app.label.contains('red')){ app.updateLabel(app.label.minus("<font color = 'red'> (Paused) </font>" ))
    LOGDEBUG("App Released - state.pauseApp = $state.pauseApp ")                          
    }
   }
  }    
}


def stopAllChildren(disableChild, msg){
	state.disableornot = disableChild
	state.message1 = msg
	log.trace " $state.message1 - Disable app = $state.disableornot"
	state.appgo = state.disableornot
	state.restrictRun = state.disableornot
	if(state.disableornot == true){
	unsubscribe()
//	unschedule()
	}
	if(state.disableornot == false){
	subscribeNow()}

	
}

def updateCheck(){
    setVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"]
    try {
    httpGet(paramsUD) { respUD ->
//  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def commentRead = (respUD.data.Comment)
       		state.Comment = commentRead

            def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
            state.updateURI = updateUri   
            
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            state.newver = newVerRaw
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.status = "<b>** This app is no longer supported by $state.author  **</b>"  
             log.warn "** This app is no longer supported by $state.author **" 
            
      		}           
		else if(currentVer < newVer){
        	state.status = "<b>New Version Available ($newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn " Update: $state.UpdateInfo "
             state.newBtn = state.status
            def updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
            
       		} 
		else{ 
      		state.status = "Current"
       		LOGDEBUG("You are using the current version of this app")
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
    if(state.status != "Current"){
		state.newBtn = state.status
		inform()
        
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}


def inform(){
	log.warn "An update is available - Telling the parent!"
	parent.childUpdate(true,state.updateMsg) 
}



def preCheck(){
	setVersion()
    state.appInstalled = app.getInstallationState()  
    if(state.appInstalled != 'COMPLETE'){
    section(){ paragraph "$state.preCheckMessage"}
    }
    if(state.appInstalled == 'COMPLETE'){
    display()   
 	}
}

def setDefaults(){
    LOGDEBUG("Initialising defaults...")
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}
    if(enableSwitch == null){
    LOGDEBUG("Enable switch is NOT used. Switch is: $enableSwitch - Continue..")
    state.appgo = true	
    }
	state.restrictRun = false
}



def setVersion(){
		state.version = "1.5.0"	 
		state.InternalName = "TempControlledSwitchChild"
    	state.ExternalName = "Temperature Controlled Switch Child"
		state.preCheckMessage = "This App was designed to control a heater (switch) - turning on/off with  varying temperatures. <br>It has an optional, configurable restrictions on when it can run"
		state.CobraAppCheck = "tempcontrolledswitch.json"
}

