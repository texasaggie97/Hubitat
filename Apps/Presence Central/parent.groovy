/**
 *  ****************  Presence Central  ****************
 *
 *  Design Usage:
 *  This is the 'Parent' app for presence automation
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
 *  Last Update: 21/06/2017
 *
 *  Changes:
 *
 * 
 *
 *  V2.1.0 - Added remote version checking
 *  V2.0.0 - Initial port to Hubitat
 *  V1.0.0 - POC
 *
 */

 
 definition(
    name: "Presence Central",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Parent App for Presence Automation.",
   category: "Fun & Social",
    iconUrl: "https://raw.githubusercontent.com/cobravmax/SmartThings/master/icons/presence.png",
    iconX2Url: "https://raw.githubusercontent.com/cobravmax/SmartThings/master/icons/presence.png",
    iconX3Url: "https://raw.githubusercontent.com/cobravmax/SmartThings/master/icons/presence.png",)

preferences {
    
    page(name: "mainPage", title: "Automations", install: true, uninstall: true,submitOnChange: true) {
    
    section() {
    
        paragraph image: "https://raw.githubusercontent.com/cobravmax/SmartThings/master/icons/presence.png",
                  title: "Presence Sensor Control",
                  required: false,
                  "This parent app is a container for all presence child apps"
    }
section{
            paragraph "Parent Status: $state.verCheck"
			paragraph "Parent Version: $state.appversion -  Copyright © 2018 Cobra"
			}
    
        section {
            app(name: "switchPresenceAutomation", appName: "Presence_Central_Child", namespace: "Cobra", title: "Create New Presence Automation", multiple: true)
           
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
    setAppVersion()
    cobra()
    schedule("0 0 14 ? * FRI *", cobra)
    log.debug "there are ${childApps.size()} child smartapps"
    childApps.each {child ->
        log.debug "child app: ${child.label}"
    }
}



def cobra(){
    setAppVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       try {
        httpGet(paramsUD) { respUD ->
  //   log.info "response data: ${respUD.data}"
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
    state.appversion = "2.1.0"
    state.InternalName = "PCparent"
}