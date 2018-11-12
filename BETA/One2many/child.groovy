/**
 * ****************  One_To_Many_Switching.  ****************
 *
 *  Design Usage:
 *	This was designed to force one or more switches to follow a single switch 
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
 *  
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
 *  Last Update: 12/11/2018
 *
 *  Changes:
 *
 *  V2.0.0 - Changed to be able to control switches AND dimmers AND Lights
 *  V1.1.0 - Added restrictions page
 *  V1.0.2 - Revised Update Checking
 *  V1.0.1 - Changed the way default settings work to prevent having to toggle switches first time
 *  V1.0.0 - POC 
 */
 
 
 
 

definition(
    name: "One To Many Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "If you turn on/off a switch or adjust a dimmer/light - All others follow",
    category: "Convenience",
        
      parent: "Cobra:One To Many",
    
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

def mainPage() {
	dynamicPage(name: "mainPage") {
	preCheck()
        
        section() {
	input "childTypeSelect", "enum", required: true, title: "What to control", submitOnChange: true,  options: ["Dimmer or Light", "Switch"] 
	state.modeType = childTypeSelect                                                                                                                     
 }  
  	  if(state.modeType == 'Dimmer or Light'){           
	section("Control Dimmer or Light"){
		input "dimmer1",  "capability.switchLevel", multiple: false, required: true
	    }  
    section("Follow Dimmer(s) or Light(s)"){
		input "dimmer2",  "capability.switchLevel", multiple: true, required: false
		}
   	}   
   	         
        
      if(state.modeType == 'Switch'){           
	section("Control Switch"){
		input "switch1",  "capability.switch", multiple: false, required: true
	    }  
    section("Follow Switch(es)"){
		input "switch2",  "capability.switch", multiple: true, required: true
		}
    section("Select Mode"){
     input "reversemode", "bool", title: " Reverse operation? (Follow switch(es) off when Control Switch on)", required: true, submitOnChange: true, defaultValue: false    
	    }
	}
    }
}    
def restrictionsPage() {
    dynamicPage(name: "restrictionsPage") {
        section(""){paragraph "<font size='+1'>App Restrictions</font> <br>These restrictions are optional <br>Any restriction you don't want to use, you can just leave blank"}
        section(){
        input "enableSwitch", "capability.switch", title: "Select a switch Enable/Disable this app", required: false, multiple: false, submitOnChange: true 
     	if(enableSwitch){ input "enableSwitchMode", "bool", title: "Allow app to run only when this switch is..", required: true, defaultValue: false, submitOnChange: true}
        }
        section(){input(name:"modes", type: "mode", title: "Select Mode(s)", multiple: true, required: false)}
       	section(){
    	input "fromTime", "time", title: "Allow actions from", required: false
    	input "toTime", "time", title: "Allow actions until", required: false
    	input "days", "enum", title: "Allow actions only on these days of the week", required: false, multiple: true, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        	}
        section(){
    	input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor){
   		input "restrictPresenceAction", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false    
			}
     	input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor1){
   		input "restrictPresenceAction1", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false    
			}
   		}   
        section() {label title: "Enter a name for this automation", required: false}
        section() {input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}
    }
}  

def installed(){initialise()}
def updated(){
	unsubscribe()
	initialise()
}
def initialise(){
	version()
	log.info "Initialised with settings: ${settings}"
	subscribeNow()
}
def subscribeNow() {
    unsubscribe()
	if(enableSwitch){subscribe(enableSwitch, "switch", switchEnable)}
	if(enableSwitchMode == null){enableSwitchMode = true} // ????
	if(restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if(restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
	if(modes){subscribe(location, "mode", modeHandler)}
    subscribe(switch1, "switch", switchHandler)
    subscribe(dimmer1, "level", dimmerHandler1)
    subscribe(dimmer1, "switch", dimmerHandler3)
    subscribe(dimmer2, "level", dimmerHandler2)
    subscribe(dimmer2, "switch", dimmerHandler2)
}

def dimmerHandler2(evt){
LOGDEBUG("$dimmer2 set to: $evt.value")
}

def dimmerHandler1(evt){
     if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
	if(state.pauseApp == false){checkAllow()}
	if(state.allAllow == true){
    state.mylevel1 = evt.value
        
    if(dimmer2){
    LOGDEBUG("Control dimmer is $state.mylevel1 - Setting $dimmer2 to the same...")
 
    state.mylevel1 = evt.value.toDouble()
   	dimmer2.setLevel(state.mylevel1)
    }   
  }    
}
def dimmerHandler3(evt){
     if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
	if(state.pauseApp == false){checkAllow()}
	if(state.allAllow == true){
    state.mylevel1 = evt.value
        
    if(dimmer2){
    LOGDEBUG("Control dimmer is $state.mylevel1 - Setting $dimmer2 to the same...")
    
    if(state.mylevel1 == 'on'){dimmer2.on()}
    if(state.mylevel1 == 'off'){dimmer2.off()}
     }  
  }    
}


def switchHandler1 (evt) {
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
	if(state.pauseApp == false){checkAllow()}
	if(state.allAllow == true){
	state.reverse = reversemode
if(reversemode == null){state.reverse = false}
	state.currS1 = evt.value
    LOGDEBUG("Control switch is $state.currS1")
    
if (state.reverse == false || state.reverse == null){
	if (state.currS1 == "on") { 
	LOGDEBUG("Turning on $switch2")
	switch2.on()
	}

	else if (state.currS1 == "off") { 
	LOGDEBUG("Turning off $switch2")
	switch2.off()
	}
 }
if (state.reverse == true){
	if (state.currS1 == "on") { 
	LOGDEBUG("Turning off $switch2")
	switch2.off()
	}

	else if (state.currS1 == "off") { 
	LOGDEBUG("Turning on $switch2")
	switch2.on()
	}
 } 
}
}
def checkAllow(){
    LOGDEBUG("Checking for any restrictions...")
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){
        LOGDEBUG("Continue - App NOT paused")
        state.noPause = true
    	modeCheck()
    	checkTime()
        checkDay()
        checkPresence()
        checkPresence1()
 
	if(state.modeCheck == false){
	LOGDEBUG("Not in correct 'mode' to continue")
	    }    
	if(state.presenceRestriction ==  false || state.presenceRestriction1 ==  false){
	LOGDEBUG( "Cannot continue - Presence failed")
	}
	if(state.appgo == false){
	LOGDEBUG("$enableSwitch is not in the correct position so cannot continue")
	}
	if(state.appgo == true && state.dayCheck == true && state.presenceRestriction == true && state.presenceRestriction1 == true && state.modeCheck == true && state.timeOK == true && state.noPause == true){
	state.allAllow = true 
 	  }
	else{
 	state.allAllow = false
	log.warn "One or more restrictions apply - Unable to continue"
 	LOGDEBUG("state.appgo = $state.appgo, state.dayCheck = $state.dayCheck, state.presenceRestriction = $state.presenceRestriction, state.presenceRestriction1 = $state.presenceRestriction1, state.modeCheck = $state.modeCheck, state.timeOK = $state.timeOK, state.noPause = $state.noPause")
      }
   }
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
	updateCheck()  
    checkButtons()
   
}


def preCheck(){
	state.appInstalled = app.getInstallationState()  
    
    if(state.appInstalled != 'COMPLETE'){
    section(){ paragraph "If you turn on/off a switch or adjust a dimmer/light - All others follow"}
    }
	if(state.appInstalled == 'COMPLETE'){
	display()   
 	}
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


def display(){
    setDefaults()

	if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}}
    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){section(){input "updateBtn", "button", title: "$state.btnName"}}
    
	if(state.status != "Current"){section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>$state.updateURI</b><hr>"}}
	section() {
      input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available", required: true, defaultValue: false, submitOnChange: true 
      if(updateNotification == true){ input "speakerUpdate", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
    }
    
   section(){input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false }
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
            pushOverNow(updateMsg)
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



def setDefaults(){
	log.info "Initialising defaults..." 
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}
	if(enableSwitch == null){
	LOGDEBUG("Enable switch is NOT used. Switch is: $enableSwitch - Continue..")
	state.appgo = true
    }
}
def setVersion(){
    state.version = "2.0.0"
     state.InternalName = "OneToManyChild"
     state.ExternalName = "One To Many Child"
     state.CobraAppCheck = "one2many.json"
}