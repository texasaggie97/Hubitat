/**
 *  ****************  Contact Controlled Switch  ****************
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This SmartApp is free!
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm happy for you to use this without a donation (but, if you find it useful then it would be nice to get a 'shout out' on the forum!) -  @Cobra
 *
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
 *  If modifying this project, please keep the above header intact and add your comments/credits below -  @Cobra
 *
 *
 *
 *  Last Update: 27/10/2018
 *
 *  Changes:
 *
 * 
 *
 *
 *  
 *  V1.0.0 - POC
 *
 */
 
 
 
 
 
 
 
 
 
definition(
    name: "Contact Lights and Switches Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Contact Lights and Switches Child",
    category: "Convenience",
  parent: "Cobra:Contact Controlled Lights and Switches",  

    
    iconUrl: "",
	iconX2Url: "",
    iconX3Url: "",
    )

preferences {
	display() 

    section("") {
   input "childTypeSelect", "enum", required: true, title: "What to trigger", submitOnChange: true,  options: ["Light", "Switch"] 
     state.modeType = childTypeSelect                                                                                                                     
 }  
    if(state.modeType){  
        section(){
		input "contact1", "capability.contactSensor", title: "Door/Window Contact", required: true, multiple: true
	}
     
        
        
    if(state.modeType == 'Switch'){ 
        
	 section("Turn on this switch"){
		input "switch1",  "capability.switch", multiple: true, required: false
        input "contactMode1", "bool", title: "Switch off as well", required: true, defaultValue: false, submitOnChange: true 
        if(contactMode1 == true){ input (name: "delay1", type: "number", title: "Off Delay (Minutes)", required: true, defaultValue: '0') } 
       	}    
    
  
    } 
        
     if(state.modeType == "Light"){      
      section(){  
     input (name: "switch2", type: "capability.switchLevel", title: "Control these lights", multiple: true, required: true)
        input (name: "dimLevel1", type: "number", title: "Dim Level", required: true) 
         input "contactMode1", "bool", title: "Switch off as well", required: true, defaultValue: false, submitOnChange: true
          if(contactMode1 == true){ input (name: "delay1", type: "number", title: "Off Delay (Minutes)", required: true, defaultValue: '0') } 
     } 
         
         
     }
    }    
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {	 
 version()
    
      subscribe(contact1, "contact", contactHandler)
}


def contactHandler (evt) {
state.contact = evt.value
state.mydelay = 60 * delay1 
state.mylevel1 = dimLevel1
state.contactMode = contactMode1
    
   log.info "$contact1 = $state.contact"
   
 if(state.contact == 'open'){
 log.info "Switching on..."
     if(switch1){switch1.on()}
     if(switch2){switch2.setLevel (state.mylevel1)}
	}
   
    if(state.contact == 'closed' && state.contactMode == true){   
		log.info " switching off in $state.mydelay seconds"
		runIn(state.mydelay, offNow)
}
	else if(state.contact == 'closed' && state.contactMode == false){   
     log.info "No off configured"
    }
}
    
  
   
def offNow(){
log.info "Switching off... "
    if(switch1){switch1.off()}
    if(switch2){switch2.off()}   
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
	paragraph "<b>Update Information:</b> <BR>$state.UpdateInfo <BR>$state.updateURI"
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
    
def pushOver(inMsg){
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
		state.InternalName = "ContactLightSwitchesChild"  
}

















   