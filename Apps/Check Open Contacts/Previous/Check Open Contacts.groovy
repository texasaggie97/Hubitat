/**
 *  ****************  Check Open Contacts  ****************
 *
 *  Design Usage:
 *  This was designed to announce if any windows/doors are open when clicking a switch
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
 *  Last Update: 27/09/2018
 *
 *  Changes:
 *
 *
 *  V1.1.0 - Debug & Modified routine for 'between' hours
 *  V1.0.0 - Basic port from ST
 *
 */
 
 
 
 
definition(
    name: "Check Open Contacts",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Announce any open windows & doors (Contact Sensors)",
    category: "",
    

    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )


preferences {

display()

	section() {
    
        paragraph "This was designed to announce if any windows are open when clicking a switch"
    }

	
     section() {
    		input "switch1", "capability.switch", title: "Select Enable/Disable Switch (Optional)", required: false, multiple: false 
    }  
    
		section() {
    		input "switch2", "capability.switch", title: "Select Trigger Switch", required: false, multiple: false 
    }  
      section("Speaker Settings") { 
        input "speaker1", "capability.musicPlayer", title: "Choose a speaker", required: false, multiple: true, submitOnChange:true
         input "volume1", "number", title: "Speaker volume", description: "0-100%", required: false
         input "delay1", "number", title: "Delay before speaking (Seconds - enter 0 for no delay)", description: "Seconds", required: true
         input "message1", "text", title: "Message to speak before list of open devices",  defaultValue: "The following windows or doors are open:", required: true
         input "message2", "text", title: "Message to speak if there are NO open devices",  defaultValue: "There are no open windows or doors", required: true 
         input "msgDelay", "number", title: "Number of minutes between messages - enter 0 for no delay", description: "Minutes", required: true
          }
 	section("Allow messages between what times?") {
        input "fromTime", "time", title: "From", required: false
        input "toTime", "time", title: "To", required: false
} 
	section("Set different volume on messages between these times?") {
	input "volume2", "number", title: "Quiet Time Speaker volume", description: "0-100%",  required: false // defaultValue: "0",
    input "fromTime2", "time", title: "Quiet Time Start", required: false
    input "toTime2", "time", title: "Quiet Time End", required: false
    }
    
   
    section("'Contact Sensors' to check..."){
		input "sensors", "capability.contactSensor", multiple: true
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
	  log.info "Initialised with settings: ${settings}"
       version()
      logCheck()
state.timer = 'yes'
state.currS1 = "on"	
    
    if(volume2 == null){volume2 = 0}
    
    subscribe(switch1, "switch", switchHandler)
     subscribe(switch2, "switch.on", switch2Handler)
    subscribe(sensors, "contact", contactHandler)
}


def switchHandler(evt) {
   state.currS1 = evt.value  // Note: Optional if switch is used to control action
  LOGDEBUG("$switch1 = $evt.value")
  					   }


def switch2Handler (evt){
    checkTime()
    if(state.timeOK == true){
    
LOGDEBUG(" Switch activated! - Waiting $delay1 seconds before checking to see if I can play message")

def myDelay1 = delay1
LOGDEBUG("myDelay1 = $myDelay1") 
 if (state.currS1 != 'nul' && state.currS1 == "on") {
LOGDEBUG("Running switch2Handler...") 

LOGDEBUG("Running soon...") 
		runIn(myDelay1,talkNow1)


	}
else  if (state.currS1 != 'nul' && state.currS1 == "off") {  
LOGDEBUG( " Switch activated but '$switch1' is set to 'Off' so I'm doing as I'm told and keeping quiet!")
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

LOGDEBUG(" Checking open windows & doors now...")
	setVolume()
    
LOGDEBUG("Speaker(s) in use: $speaker1")     
def open = sensors.findAll { it?.latestValue("contact") == 'open' }
		if (open) { 
LOGDEBUG("Open windows or doors: ${open.join(',,, ')}")
                state.fullMsg1 = "$newmsg  ${open.join(',,, ')}"
                
   state.duration = '60'
  state.sound = textToSpeech(state.fullMsg1)
LOGDEBUG("state.sound = $state.sound")  
            
            speaker1.playTextAndRestore(state.fullMsg1)
// speaker1.playTrackAndRestore(state.sound.uri, state.duration, state.volume)          
            

	//	speaker1.speak(state.fullMsg1)
        
	state.timer = 'no'
    
// log.debug "Message allow: set to $state.timer as I have just played a message"

            if(msgDelay == null){state.timeDelay = 0}
            else{state.timeDelay = 60 * msgDelay}
            
LOGDEBUG("Waiting for $state.timeDelay seconds before resetting timer to allow further messages")
runIn(state.timeDelay, resetTimer)
}
if (!open) {
LOGDEBUG(" Timer = $state.timer")
if (state.timer != 'no'){
state.fullMsg1 = message2
LOGDEBUG("Speaking now...")
// speaker1.speak(state.fullMsg1)

speaker1.playTextAndRestore(state.fullMsg1)

state.timer = 'no'
LOGDEBUG("There are no open windows or doors ")
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


def resetTimer() {
state.timer = 'yes'
LOGDEBUG("Timer reset - Messages allowed")

}


def setVolume(){
def timecheck = fromTime2
if (timecheck != null){
def between2 = timeOfDayIsBetween(toDateTime(fromTime2), toDateTime(toTime2), new Date(), location.timeZone)
    if (between2) {
    
    state.volume = volume2
   speaker1.setLevel(state.volume)
    
   LOGDEBUG("Quiet Time = Yes - Setting Quiet time volume")
    
}
else if (!between2) {
state.volume = volume1
LOGDEBUG("Quiet Time = No - Setting Normal time volume")

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
		state.version = "1.1.0"	 
		state.InternalName = "CheckContacts"
}