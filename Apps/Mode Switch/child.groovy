/**
 *  **************** Mode Switch Child ****************
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
 *  Last Update: 01/11/2018
 *
 *  Changes:
 *
 *  V1.0.0 - POC
 *
 */

definition(
    name: "Mode Switch Child",
    namespace: "Cobra",
    author: "AJ Parker",
    description: "This was designed to use location modes to control switches/lights etc",
    category: "My Apps",
    
parent: "Cobra:Mode Switch",
    
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
        input "newMode1", "mode", title: "Which Mode(s) do you want to react to?", required: true, multiple: true 
        
     }   
   
       
        
	 section(){
	 input "switch1",  "capability.switch", multiple: true, title: "Turn On these switches", required: false
     input "switch2",  "capability.switch", multiple: true, title: "Turn Off these switches", required: false
     input (name: "light1", type: "capability.switchLevel", title: "Set these lights", multiple: true, required: false, submitOnChange: true)
         if(light1){
     input (name: "dimLevel1", type: "number", title: "To this level (set to 0 for off)", required: true) 
         } 
     input (name: "light2", type: "capability.switchLevel", title: "And set these lights", multiple: true, required: false, submitOnChange: true)
         if(light2){
     input (name: "dimLevel2", type: "number", title: "To this level (set to 0 for off)", required: true)      
         }
        
         
         
     }
          section("Automation name") {
                label title: "Enter a name for this automation", required: false
            }
        section(){
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
   	logCheck()
	version()
	pauseOrNot()
    subscribe(location, "mode", modeHandler)
    setDefaults()
}

        
  


def modeHandler(evt){
     LOGDEBUG("Mode handler running")
state.modeNow = evt.value    
state.modeRequired = newMode1
 
   LOGDEBUG("modeRequired = $state.modeRequired - current mode = $state.modeNow")  
	  if(state.modeRequired.contains(location.mode)){ 
      LOGDEBUG("Mode is now $state.modeRequired")   
      runNow()
    
}
     else{  
        LOGDEBUG("Mode not matched")
      }
}

def modeCheck() {
    LOGDEBUG("Checking mode...")
	def result = state.modeRequired.contains(location.mode)
    
    LOGDEBUG("Mode = $result")
    state.modeCheck = result
    return state.modeCheck
 }



def runNow(){
    state.dim1 = dimLevel1.toInteger()
    state.dim2 = dimLevel2.toInteger()
     LOGDEBUG("Switching/Setting level now....")
    if(state.dim1 == 0){light1.off()}
    if(state.dim2 == 0){light2.off()}
    
    if(switch1){switch1.on()}
    if(switch2){switch2.off()} 
    if(light1){
        LOGDEBUG("Setting level1 to $state.dim1")
        light1.setLevel(state.dim1)
    }
    if(light2){
          LOGDEBUG("Setting level2 to $state.dim2")
        light2.setLevel(state.dim2)
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
def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}

        
        def version(){
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
 
    
   
    
}

def display(){
 //   setDefaults()
  	
    
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
		state.InternalName = "ModeSwitchchild" 
    	state.ExternalName = "Mode Switch Child"
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










