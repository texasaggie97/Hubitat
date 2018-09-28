/**
 *  ****************  Check Open Contacts  ****************
 *
 *  Design Usage:
 *  This was designed to announce if any contacts are open when a switch turns on or a water sensor reports wet - It can also turn on other switches if any open or all closed
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
 *  Last Update: 28/09/2018
 *
 *  Changes:
 *
 *
 *  V1.4.0 - Added capability to turn on a switch after message.
 *  V1.3.0 - Added a second trigger option - Water Sensor.
 *  V1.2.0 - Added the option to use speech synthesis as well as music player
 *  V1.1.0 - Debug & Modified routine for between hours
 *  V1.0.0 - Basic port from my ST app
 *
 */
 
 
 
 
definition(
    name: "Check Open Contacts",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "This was designed to announce if any contacts are open when a switch turns on or a water sensor reports wet - It can also turn on other switches if any open or all closed",
    category: "",
    

    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )


preferences {

display()

	section() {
    
        paragraph "This was designed to announce if any contacts are open when a switch turns on or a water sensor reports wet"
    }

	
     section() {
    		input "switch1", "capability.switch", title: "Select Enable/Disable Switch (Optional)", required: false, multiple: false 
    }  
    
		section() {
            input "triggerMode", "enum", required: true, title: "Select Trigger Type", submitOnChange: true,  options: ["Switch", "Water Sensor"] 
            if(triggerMode == "Switch"){input "switch2", "capability.switch", title: "Select Trigger Device", required: true, multiple: false}
            if(triggerMode == "Water Sensor"){input "water1", "capability.waterSensor", title: "Select Trigger Device", required: true, multiple: false}
            
            
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
         input "message2", "text", title: "Message to speak if there are NO open devices",  defaultValue: "There are no open windows or doors", required: true 
         input "msgDelay", "number", title: "Number of minutes between messages - enter 0 for no delay", description: "Minutes", required: true
          }
    }
     section() {
 	 input "switchMode1", "bool", title: "Also Control A Switch", required: true, defaultValue: false, submitOnChange:true
        
         if(switchMode1 == true){
          input "switch3", "capability.switch", title: "Select Switch(s) to control", required: false, multiple: true 
          input "switchMode2", "bool", title: "Switch Mode: On = Turn switch ON when one or more contacts are open - Off = Turn switch On when there are NO open contacts", required: true, defaultValue: false, submitOnChange:true   
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
	  log.info "Initialised with settings: ${settings}"
       version()
      logCheck()
state.timer = 'yes'
state.currS1 = "on"	
    
    if(volume2 == null){volume2 = 0}
    
    subscribe(switch1, "switch", switchHandler)
    if(triggerMode == "Switch"){subscribe(switch2, "switch.on", evtHandler)}
    if(triggerMode == "Water Sensor"){subscribe(water1, "water.wet", evtHandler)}
                                
    subscribe(sensors, "contact", contactHandler)
}


def switchHandler(evt) {
   state.currS1 = evt.value  // Note: Optional if switch is used to control action
  LOGDEBUG("$switch1 = $evt.value")
  					   }


def evtHandler (evt){
    LOGDEBUG("Running evtHandler...") 
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




def talkNow1() {
LOGDEBUG(" Timer = $state.timer")
if (state.timer != 'no'){

def newmsg = message1
// def newmsg = "It's raining ,,, and the following windows or doors, are open:"  //test

LOGDEBUG(" Checking open contacts now...")
	
    
LOGDEBUG("Speaker(s) in use: $speaker1")     
def open = sensors.findAll { it?.latestValue("contact") == 'open' }
		if (open) { 
LOGDEBUG("Open windows or doors: ${open.join(',,, ')}")
                state.fullMsg1 = "$newmsg ,,,  ${open.join(',,, ')}"
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
//  LOGDEBUG(" Timer = $state.timer")
if (state.timer != 'no'){
state.fullMsg1 = message2
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
	unschedule()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
}

def display(){
	if(state.status){
	section{paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}
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
		state.version = "1.4.0"	 
		state.InternalName = "CheckContacts"
}