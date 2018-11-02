/**
 *  ****************  Check Open Contacts  ****************
 *
 *  Design Usage:
 *  This was designed to announce if any contacts are open when an event is triggered - It can also turn on other switches if any open or all closed
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
 *  Last Update: 31/10/2018
 *
 *  Changes:
 *
 *  V2.1.0 - Revised update checking
 *  V2.0.2 - Removed default value from 'no open contacts' setting
 *  V2.0.1 - Added optional pushover notification for update
 *  V2.0.0 - Added 'Thermostat' (heat, cool) trigger
 *  V1.9.0 - Added 'Nest' heating & cooling trigger
 *  V1.8.0 - Added 'Time' trigger
 *  V1.7.0 - Added 'Button' trigger
 *  V1.6.0 - Added 'Mode' trigger 
 *  V1.5.1 - Revised auto update checking and added a manual update check button
 *  V1.5.0 - Converted to Parent/Child app
 *  V1.4.0 - Added capability to turn on a switch after message.
 *  V1.3.0 - Added a second trigger option - Water Sensor.
 *  V1.2.0 - Added the option to use speech synthesis as well as music player
 *  V1.1.0 - Debug & Modified routine for between hours
 *  V1.0.0 - Basic port from my ST app
 *
 */
 
 
 
 
definition(
    name: "Check Open Contacts Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "This was designed to announce if any contacts are open when an event is triggered - It can also turn on other switches if any open or all closed",
    category: "",
    
parent: "Cobra:Check Open Contacts",
    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )


preferences {

display()

	section() {
    
        paragraph "This was designed to announce if any contacts are open when an event is triggered"
    }

	
     section() {
    		input "switch1", "capability.switch", title: "Select Enable/Disable Switch (Optional)", required: false, multiple: false 
    }  
    
		section() {
            input "triggerMode", "enum", required: true, title: "Select Trigger Type", submitOnChange: true,  options: ["Button", "Mode Change", "Nest Thermostat - Heating", "Nest Thermostat - Cooling", "Standard Thermostat - Heating", "Standard Thermostat - Cooling", "Switch", "Time", "Water Sensor"] 
            if(triggerMode == "Switch"){input "switch2", "capability.switch", title: "Select Trigger Device", required: true, multiple: false}
            if(triggerMode == "Water Sensor"){input "water1", "capability.waterSensor", title: "Select Trigger Device", required: true, multiple: false}
            if(triggerMode == "Mode Change"){input "newMode1", "mode", title: "Action when changing to this mode",  required: true, multiple: false}
            if(triggerMode == "Button"){
                input "button1", "capability.pushableButton", title: "Select Button Device", required: true, multiple: false
            	input "buttonNumber", "enum", title: "Enter Button Number", required: true, options: ["1", "2", "3", "4", "5"] 
            }
            if(triggerMode == "Time"){input (name: "runTime", title: "Time to run", type: "time",  required: true)}            
             if(triggerMode == "Nest Thermostat - Heating" ){input "nestDevice", "capability.thermostat", title: "Select Trigger Device",  required: true}  
             if(triggerMode == "Nest Thermostat - Cooling"){input "nestDevice", "capability.thermostat", title: "Select Trigger Device",  required: true} 
            if(triggerMode == "Standard Thermostat - Heating"){input "statDevice", "capability.thermostat", title: "Select Trigger Device",  required: true} 
            if(triggerMode == "Standard Thermostat - Cooling"){input "statDevice", "capability.thermostat", title: "Select Trigger Device",  required: true} 
    }  
    
        section(){
		input "sensors", "capability.contactSensor", title: "Contact Sensors to check", multiple: true
}
    
      section() { 
    
           input "speechMode", "enum", required: true, title: "Select Speaker Type", submitOnChange: true,  options: ["Music Player", "Speech Synth"] 
    
          if (speechMode == "Music Player"){ 
              input "speaker1", "capability.musicPlayer", title: "Choose speaker(s)", required: false, multiple: true, submitOnChange:true
              input "volume1", "number", title: "Speaker volume", description: "0-100%", required: false
              input "volume2", "number", title: "Quiet Time Speaker volume", description: "0-100%",  required: false // defaultValue: "0",
    		  input "fromTime2", "time", title: "Quiet Time Start", required: false
    		  input "toTime2", "time", title: "Quiet Time End", required: false
    
          }  
               
        if (speechMode == "Speech Synth"){ 
         input "speaker1", "capability.speechSynthesis", title: "Choose speaker(s)", required: false, multiple: true
          }
      }    
    if(speechMode){ 
        section("Allow messages between what times? (Optional)") {
        input "fromTime", "time", title: "From", required: false
        input "toTime", "time", title: "To", required: false
         input "delay1", "number", title: "Delay before message (Seconds - enter 0 for no delay)", description: "Seconds", required: true
         input "message1", "text", title: "Message to speak before list of open devices",  defaultValue: "The following windows or doors are open:", required: true
         input "message2", "text", title: "Message to speak if there are NO open devices", required: false
         input "msgDelay", "number", title: "Number of minutes between messages - enter 0 for no delay", description: "Minutes", required: true
          }
    }
     section() {
 	 input "switchMode1", "bool", title: "Also Control A Switch", required: true, defaultValue: false, submitOnChange:true
        
         if(switchMode1 == true){
          input "switch3", "capability.switch", title: "Select Switch(s) to control", required: false, multiple: true 
          input "switchMode2", "bool", title: "Switch Mode: <br> On = Turn switch ON when one or more contacts are open <br> Off = Turn switch On when there are NO open contacts", required: true, defaultValue: false, submitOnChange:true   
         }
  }
	
    
    section(" ") {}


	section() {
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
	logCheck()
    setDefaults()
    version()
   
	  log.info "Initialised with settings: ${settings}"
      

    
   
    
    subscribe(switch1, "switch", switchHandler)
    if(triggerMode == "Switch"){subscribe(switch2, "switch.on", evtHandler)}
    if(triggerMode == "Water Sensor"){subscribe(water1, "water.wet", evtHandler)}
    if(triggerMode == "Mode Change"){ subscribeLocation(location, "mode", modeChangeHandler )}  
    if(triggerMode == "Button"){
        if(buttonNumber == '1'){subscribe(button1, "pushed.1", evtHandler)}
        if(buttonNumber == '2'){subscribe(button1, "pushed.2", evtHandler)}
        if(buttonNumber == '3'){subscribe(button1, "pushed.3", evtHandler)}
        if(buttonNumber == '4'){subscribe(button1, "pushed.4", evtHandler)}
        if(buttonNumber == '5'){subscribe(button1, "pushed.5", evtHandler)}
    } 
    if(triggerMode == "Time"){schedule(runTime, evtHandler)}
    if(triggerMode == "Nest Thermostat - Heating"){subscribe(nestDevice, "thermostatOperatingState.heating", evtHandler)}
    if(triggerMode == "Nest Thermostat - Cooling"){subscribe(nestDevice, "thermostatOperatingState.cooling", evtHandler)}
	if(triggerMode == "Standard Thermostat - Heating"){subscribe(statDevice, "thermostatMode.heat", evtHandler)}
    if(triggerMode == "Standard Thermostat - Cooling"){subscribe(statDevice, "thermostatMode.cool", evtHandler)}
    
    
    subscribe(sensors, "contact", contactHandler)
    
   
}




def switchHandler(evt) {
   state.currS1 = evt.value  // Note: Optional if switch is used to control action
  LOGDEBUG("$switch1 = $evt.value")
  					   }


def evtHandler (evt){
    LOGDEBUG("Running evtHandler... Event received: $evt.value") 
    checkTime()
    if(state.timeOK == true){
    
LOGDEBUG(" Device activated! - Waiting $delay1 seconds before checking to see if I can play message")

def myDelay1 = delay1
LOGDEBUG("myDelay1 = $myDelay1") 
 if (state.currS1 != 'nul' && state.currS1 == "on") {


LOGDEBUG("Running soon...") 
		runIn(myDelay1,talkNow1)
     
	}
else  if (state.currS1 != 'nul' && state.currS1 == "off") {  
LOGDEBUG( " Trigger activated but '$switch1' is set to 'Off' so I'm doing as I'm told and keeping quiet!")
}		
}
}

def contactHandler(evt){
  LOGDEBUG("Contact = $evt.value")  
  
}
def modeChangeHandler(evt){
	state.modeNow = evt.value
	state.modeRequired = newMode1
 
    
   
 LOGDEBUG("modeRequired = $state.modeRequired - current mode = $state.modeNow")  

	if (evt.isStateChange){
     LOGDEBUG("State Change Occured!")   

      if(state.modeRequired.contains(state.modeNow)){  
        LOGDEBUG("Mode - YES a match")
        evtHandler(evt)  
      }
        else { 
    
   	LOGDEBUG("Mode is now $state.modeNow")
LOGDEBUG("Mode - NOT a match")
        	
    
    
    }
    }
}
    
    
    
def talkNow1() {
LOGDEBUG(" Timer = $state.timer")
if (state.timer != 'no'){

def newmsg = message1
// def newmsg = "It's raining ,,, and the following windows or doors, are open:"  //test

LOGDEBUG(" Checking open contacts now...")
	
    
LOGDEBUG("Speaker(s) in use: $speaker1")     
def open = sensors.findAll { it?.latestValue("contact") == 'open' }
		if (open) { 
LOGDEBUG("Open windows or doors: ${open.join(',')}")
                state.fullMsg1 = "$newmsg ,  ${open.join(',')}"
         if(switchMode1 == true){
    	 if(switchMode2 == true){switchOn()}
     	 if(switchMode2 == false){switchOff()}
      }        
 
  if (speechMode == "Music Player"){ 
      LOGDEBUG("Music Player...")
      setVolume()
    speaker1.playTextAndRestore(state.fullMsg1)
  }
            
if (speechMode == "Speech Synth"){ 
    LOGDEBUG("Speech Synth...")
	speaker1.speak(state.fullMsg1)
}
	state.timer = 'no'
    
// log.debug "Message allow: set to $state.timer as I have just played a message"

            if(msgDelay == null){state.timeDelay = 0}
            else{state.timeDelay = 60 * msgDelay}
            
LOGDEBUG("Waiting for $state.timeDelay seconds before resetting timer to allow further messages")
runIn(state.timeDelay, resetTimer)
}
if (!open) {

    
if (state.timer != 'no'){
   
state.fullMsg1 = message2
    if(state.fullMsg1 != null){
    
     if(switchMode1 == true){
    	if(switchMode2 == false){switchOn()}
     	if(switchMode2 == true){switchOff()}
      }
LOGDEBUG("Speaking now...")
  if (speechMode == "Music Player"){ 
      setVolume()
    speaker1.playTextAndRestore(state.fullMsg1)
  }
    
 if (speechMode == "Speech Synth"){ 
	speaker1.speak(state.fullMsg1)
}   
    

    
state.timer = 'no'
LOGDEBUG("There are no open contacts ")
state.timeDelay = 60 * msgDelay
LOGDEBUG("Waiting for $state.timeDelay seconds before resetting timer to allow further messages")
runIn(state.timeDelay, resetTimer)
	}
    else{LOGDEBUG("There is nothing configured to say if no open contacts")}
}
}
}
	else if (state.timer == 'no'){
	state.timeDelay = 60 * msgDelay
LOGDEBUG( "Can't speak message yet - too close to last message")
LOGDEBUG( "Waiting for $state.timeDelay seconds before resetting timer")
	runIn(state.timeDelay, resetTimer)
}

}


def switchOn(){
    LOGDEBUG( "Turning on $switch3...")
    switch3.on()
    
}

def switchOff(){
    LOGDEBUG( "Turning off $switch3...")
    switch3.off()
    
    
}


def resetTimer() {
state.timer = 'yes'
LOGDEBUG("Timer reset - Further messages are now allowed")

}


def setVolume(){
def timecheck = fromTime2
if (timecheck != null){
def between2 = timeOfDayIsBetween(toDateTime(fromTime2), toDateTime(toTime2), new Date(), location.timeZone)
    if (between2) {
    
    state.volume = volume2
   speaker1.setLevel(state.volume)
    
   LOGDEBUG("Quiet Time = Yes - Setting Quiet time volume")
   LOGDEBUG("between2 = $between2 - state.volume = $state.volume - Speaker = $speaker1") 
}
	if (!between2) {
	state.volume = volume1
	LOGDEBUG("Quiet Time = No - Setting Normal time volume")
	LOGDEBUG("between2 = $between2 - state.volume = $state.volume - Speaker = $speaker1")
	speaker1.setLevel(state.volume)
 
	}
}
else if (timecheck == null){

state.volume = volume1
speaker1.setLevel(state.volume)

	}
}


def checkTime(){
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


// logging...
def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}


def version(){
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
//	updateCheck()  
    
   
    
}

def display(){
//    setDefaults()
  	
    
	if(state.status){
	section{paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}
       
        }
   

    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){
     section(){ input "updateBtn", "button", title: "$state.btnName"}
    }
    
    section(){
   //     log.info "app.label = $app.label"
    input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false  
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
    LOGDEBUG("Running checkButtons")
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
//    log.info "Resetting Update Button Name"
    if(state.status != "Current"){
	state.btnName = state.newBtn
    }
    else{
 state.btnName = "Check For Update" 
    }
}    
    
def pushOverNow(inMsg){
    if(updateNotification == true){  
     newMessage = inMsg
  log.info "Message = $newMessage " 
     state.msg1 = '[L]' + newMessage
	speaker.speak(state.msg1)
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
              LOGDEBUG("App Paused - state.pauseApp = $state.pauseApp ")   
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



def setVersion(){
		state.version = "2.1.0"	 
		state.InternalName = "CheckContactsChild"
    	state.ExternalName = "Check Contacts Child"
}


def setDefaults(){
  log.info "Initialising defaults..." 
    checkButtons()
    resetBtnName()
    pauseOrNot()
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}
    if(volume2 == null){volume2 = 0}
    state.timer = 'yes'
    state.currS1 = "on"	
    
}








