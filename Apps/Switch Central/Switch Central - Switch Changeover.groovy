/**
 * ****************  Switch Changeover  ****************
 *
 *  Design Usage:
 *	This was originally designed to switch 'Virtual' season switches (Spring, Summer, Autumn, Winter) but got expanded to cover 6 switches
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
 *  Last Update:31/07/2018
 *
 *  Changes:
 *
 * 
 *  V1.0.0 - POC 
 */
 
 
 
 
definition(
    name: "Switch Central - Switch Changeover",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "If you turn on a switch - All others turn off. \r\nTwo switches cannot be on at the same time.",
    category: "Convenience",
     parent: "Cobra:Switch Central",    
      iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )


preferences {

section ("") {
 
  paragraph title: "Switch Changeover", "If you turn on a switch - All others turn off. \r\nTwo switches cannot be on at the same time."
 
 }
    display()

	 section("Switches"){
		input "switch1",  "capability.switch", multiple: false, required: false
		input "switch2",  "capability.switch", multiple: false, required: false
        input "switch3",  "capability.switch", multiple: false, required: false
        input "switch4",  "capability.switch", multiple: false, required: false
        input "switch5",  "capability.switch", multiple: false, required: false
        input "switch6",  "capability.switch", multiple: false, required: false
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
	subscribe(switch1, "switch", switchHandler1)
    subscribe(switch2, "switch", switchHandler2)
    subscribe(switch3, "switch", switchHandler3)
    subscribe(switch4, "switch", switchHandler4)
    subscribe(switch5, "switch", switchHandler5)
    subscribe(switch6, "switch", switchHandler6)
}



def switchHandler1 (evt) {
state.currS1 = evt.value 
if (state.currS1 == "on") { 
log.info "Turning on $switch1 - Turning off others"

if (switch2 != null){ switch2.off()}
if (switch3 != null){ switch3.off()}
if (switch4 != null){ switch4.off()}
if (switch5 != null){ switch5.off()}
if (switch6 != null){ switch6.off()}
}
}

def switchHandler2 (evt) {
state.currS2 = evt.value 
if (state.currS2 == "on") { 
log.info "Turning on $switch2 - Turning off others"
if (switch1 != null){ switch1.off()}
if (switch3 != null){ switch3.off()}
if (switch4 != null){ switch4.off()}
if (switch5 != null){ switch5.off()}
if (switch6 != null){ switch6.off()}
}
}

def switchHandler3 (evt) {
state.currS3 = evt.value 
if (state.currS3 == "on") { 
log.info "Turning on $switch3 - Turning off others"
if (switch2 != null){ switch2.off()}
if (switch1 != null){ switch1.off()}
if (switch4 != null){ switch4.off()}
if (switch5 != null){ switch5.off()}
if (switch6 != null){ switch6.off()}
}
}
def switchHandler4 (evt) {
state.currS4 = evt.value 
if (state.currS4 == "on") { 
log.info "Turning on $switch4 - Turning off others"
if (switch2 != null){ switch2.off()}
if (switch3 != null){ switch3.off()}
if (switch1 != null){ switch1.off()}
if (switch5 != null){ switch5.off()}
if (switch6 != null){ switch6.off()}
}
}
def switchHandler5 (evt) {
state.currS5 = evt.value 
if (state.currS5 == "on") { 
log.info "Turning on $switch5 - Turning off others"
if (switch1 != null){ switch1.off()}
if (switch2 != null){ switch2.off()}
if (switch3 != null){ switch3.off()}
if (switch4 != null){ switch4.off()}
if (switch6 != null){ switch6.off()}
}
}
def switchHandler6 (evt) {
state.currS6 = evt.value 
if (state.currS6 == "on") { 
log.info "Turning on $switch6 - Turning off others"
if (switch1 != null){ switch1.off()}
if (switch2 != null){ switch2.off()}
if (switch3 != null){ switch3.off()}
if (switch4 != null){ switch4.off()}
if (switch5 != null){ switch5.off()}

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

def setAppVersion(){
     state.version = "1.0.0"
     state.InternalName = "SC-SwitchChangeOver"
     state.Type = "Application"
 

}
 
