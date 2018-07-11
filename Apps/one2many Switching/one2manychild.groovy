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
 *  Last Update: 11/07/2018
 *
 *  Changes:
 *
 * 
 *
 *  V1.0.1 - Changed the way default settings work to prevent having to toggle switches first time
 *  V1.0.0 - POC 
 */
 
 
 
 

definition(
    name: "One To Many Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "If you turn on/off a switch - All others follow",
    category: "Convenience",
        
      parent: "Cobra:One To Many",
    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )



preferences {

section ("") {
 
  paragraph image:  "",
       	title: "Switch Follow",
        required: false, 
    	 "If you turn on/off a switch - All others follow"
 
 }
    section{
            paragraph "Child Status: $state.verCheck"
			paragraph "Child Version: $state.appversion -  $state.copyrightNow"
			}

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
    reversemode = false

    
   
}



def switchHandler1 (evt) {
state.reverse = reversemode
state.currS1 = evt.value
    log.info "Control switch is $state.currS1"
    
if (state.reverse == false || state.reverse == null){
	if (state.currS1 == "on") { 
	log.info "Turning on $switch2"
	switch2.on()
	}

	else if (state.currS1 == "off") { 
	log.info "Turning off $switch2"
	switch2.off()
	}
 }
if (state.reverse == true){
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




def cobra(){
    setAppVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       try {
        httpGet(paramsUD) { respUD ->
  //   log.info "response data: ${respUD.data}"
       def copyNow = (respUD.data.versions.copyright)
       state.copyrightNow = copyNow
       def cobraVer = (respUD.data.versions.(state.InternalName))
       def cobraOld = state.appversion.replace(".", "")
       if(cobraOld < cobraVer){
		state.verCheck = "** New Version Available **"
           log.warn "There is a newer version of this app available"
       }    
       else{ 
      state.verCheck = "Current"
       }
       
       }
        } 
        catch (e) {
        log.error "Something went wrong: $e"
    }
}        


 
// App Version   *********************************************************************************
def setAppVersion(){
    state.appversion = "1.0.1"
     state.InternalName = "OneToManychild"
}