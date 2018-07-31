/**
 * ****************  One_To_Many_Switching.  ****************
 *
 *  Design Usage:
 *	This was designed to force a number of switches to follow a single switch 
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
 *  Last Update: 31/07/2018
 *
 *  Changes:
 *
 * 
 *
 * 
 *  V1.0.0 - POC 
 */
 
 
 
 
definition(
    name: "Switch Central - One To Many",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "If you turn on/off a switch - All others follow",
    category: "Convenience",
        
      parent: "Cobra:Switch Central",
    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )


preferences {

section ("") {

  paragraph title: "One To Many Switching", "If you turn on/off a switch - All others follow"
 
 }
display()
    
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
	subscribe(switch1, "switch", switchHandler1)
    schedule("0 0 14 ? * FRI *", cobra)
    cobra()
   
}



def switchHandler1 (evt) {

if (reversemode == false){
	if (state.currS1 == "on") { 
	log.info "Turning on $switch2"
	switch2.on()
	}

	else if (state.currS1 == "off") { 
	log.info "Turning off $switch2"
	switch2.off()
	}
 }
if (reversemode == true){
	if (state.currS1 == "on") { 
	log.info "Turning off $switch2"
	switch2.off()
	}

	else if (state.currS1 == "off") { 
	log.info "Turning on $switch2"
	switch2.on()
	}
 } 
}

def version(){
    updatecheck()
    if (state.Type == "Application"){schedule("0 0 9 ? * FRI *", updatecheck)}
    if (state.Type == "Driver"){schedule("0 0 8 ? * FRI *", updatecheck)}
}

def display(){
    if(state.Status){
    section{paragraph "Version: $state.version -  $state.Copyright"}
	if(state.Status != "Current"){
       section{ 
       paragraph "$state.Status"
       paragraph "$state.updateInfo"
    }
    }
}
}

def updatecheck(){
    setAppVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       try {
        httpGet(paramsUD) { respUD ->
//  log.info " Version Checking - Response Data: ${respUD.data}"   // Debug Code 
       def copyNow = (respUD.data.copyright)
       state.Copyright = copyNow
            def newver = (respUD.data.versions.(state.Type).(state.InternalName))
            def cobraVer = (respUD.data.versions.(state.Type).(state.InternalName).replace(".", ""))
       def cobraOld = state.version.replace(".", "")
       state.updateInfo = (respUD.data.versions.UpdateInfo.(state.Type).(state.InternalName)) 
            if(cobraVer == "NLS"){
            state.Status = "<b>** This $state.Type is no longer supported by Cobra  **</b>"       
            log.warn "** This $state.Type is no longer supported by Cobra **"      
      }           
      		else if(cobraOld < cobraVer){
        	state.Status = "<b>New Version Available (Version: $newver)</b>"
        	log.warn "** There is a newer version of this $state.Type available  (Version: $newver) **"
        	log.warn "** $state.updateInfo **"
       } 
            else{ 
      		state.Status = "Current"
      		log.info "$state.Type is the current version"
       }
       
       }
        } 
        catch (e) {
        log.error "Something went wrong: $e"
    }
}        



 
// App Version   *********************************************************************************
def setAppVersion(){
    state.appversion = "1.0.0"
     state.InternalName = "SC-OneToManychild"
}