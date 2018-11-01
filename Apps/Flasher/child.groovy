/**
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Last Update: 01/11/2018
 *
 *
 *
 *
 *  V1.0.0 - POC
 */
definition(
    name: "Flasher Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Flashes a set of lights at a certain time",
    category: "Convenience",
    
  parent: "Cobra:Flasher",
     
    iconUrl: "",
    iconX2Url: ""
)

preferences {
    display()
	section("Time") {
		input (name: "switchOnTime", title: "At this time", type: "time",  required: true)
	}
    section("Then flash..."){
		input "switches", "capability.switch", title: "These lights", multiple: true
	    input "numFlashes", "enum", title: "This number of times", submitOnChange: true, defaultValue: "0", options: [ "1","2","3","4","5"]
        input "delay1", "decimal", title: "Delay between flashes (seconds)", required: true
        input "startfinish", "bool", title: "Start & Finish On or Off", submitOnChange: true, defaultValue: true
        
	}
  
  

    section("Logging") {
              input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false
  	        }
	
}

def installed() {
	initialise()
	subscribe()
   
    
    log.debug "Installed with settings: ${settings}"
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	subscribe()
    initialise()

}

def initialise(){
    
    logCheck()
    setDefaults()
    version()
    log.info "Initialised with settings: ${settings}"

}



def subscribe() {
	if (switchOnTime) {
		schedule(switchOnTime, switchOnNow)
	}
   logCheck() 
   setAppVersion()
}









def switchOnNow(evt) {
	LOGDEBUG( "Time - Flashing Now!")
    state.flashNow = numFlashes
		flashLights()
   
}



def flashLights() {
    
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 

    def delay2 = delay1.toFloat()
    state.myDelay = (delay2 * 1000).toInteger()
    
     if(startfinish == null){
        state.startfinishNow = true
    }
    else{
	    state.startfinishNow = startfinish
    }
    LOGDEBUG("state.startfinishNow = $state.startfinishNow ")
    if(state.flashNow == '5'){
        
    if(state.startfinishNow == true){
    off()
    pause(state.myDelay)
	on()
    pause(state.myDelay)
    off()
    pause(state.myDelay)
    on()
    pause(state.myDelay)
    off()
    pause(state.myDelay)
    on()
    pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()        
        }
        
    if(state.startfinishNow == false){
   	on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on() 
     pause(state.myDelay)
     off()
       }    
} 
    if(state.flashNow == '4'){
        
    if(state.startfinishNow == true){
    off()
     pause(state.myDelay)
	on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
       
        }
        
    if(state.startfinishNow == false){
   	on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
    }    
}     
    
    if(state.flashNow == '3'){
        
    if(state.startfinishNow == true){
    off()
     pause(state.myDelay)
	on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()

       
        }
        
    if(state.startfinishNow == false){
   	on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()
    }    
}     
    
    if(state.flashNow == '2'){
        
    if(state.startfinishNow == true){
    off()
     pause(state.myDelay)
	on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
       
        }
        
    if(state.startfinishNow == false){
   	on()
     pause(state.myDelay)
    off()
     pause(state.myDelay)
    on()
     pause(state.myDelay)
    off()

    }    
} 
    
     if(state.flashNow == '1'){
        
    if(state.startfinishNow == true){
    off()
     pause(state.myDelay)
	on()
        }
        
    if(state.startfinishNow == false){
   	on()
     pause(state.myDelay)
    off()
 

    }    
}    
}  
}


def on(){
   LOGDEBUG("turning on...") 
    switches.on()
    
}


def off(){
     LOGDEBUG("turning off...") 
   switches.off() 
    
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
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.appversion}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}

def version(){
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
 
    
   
    
}

def display(){
    setDefaults()
  	
    
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
		state.version = "1.0.0"	 
		state.InternalName = "Flasherchild" 
    	state.ExternalName = "Flasher Child"
}

def setDefaults(){
    log.info "Initialising defaults..." 
    checkButtons()
    resetBtnName()
    pauseOrNot()
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}  


 // add any further default settings below here               
 


}

