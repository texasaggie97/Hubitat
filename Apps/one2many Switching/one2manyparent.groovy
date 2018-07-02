/**
 *  Design Usage:
 *  This is the 'Parent' app for One to many switching
 *
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This SmartApp is free!
 *
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this app without a donation, but if you find it useful
 *  then it would be nice to get a 'shout out' on the forum! -  @Cobra
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
 *  Last Update: 02/07/2018
 *
 *  Changes:
 *
 * 
 *
 *
 *  V1.0.0 - POC
 *
 */



definition(
    name:"One To Many",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "This is the 'Parent' app for One To Many Switching",
    category: "Convenience",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
    )







preferences {
	
     page name: "mainPage", title: "", install: true, uninstall: true // ,submitOnChange: true 
     
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
    schedule("0 0 14 ? * FRI *", cobra)
    cobra()
    log.info "There are ${childApps.size()} child smartapps"
    childApps.each {child ->
    log.info "Child app: ${child.label}"
    }
}
 
 
 
def mainPage() {
    dynamicPage(name: "mainPage") {
      
		section {    
			paragraph image: "https://raw.githubusercontent.com/cobravmax/SmartThings/master/icons/cobra.png",
				title: "One to Many",
				required: false,
				"This parent app is a container for all One To Many child apps"
			}
            
		section{
            paragraph "Parent Status: $state.verCheck"
			paragraph "Parent Version: $state.appversion -  $state.copyrightNow"
			}
    
		
  
     
     
// New Child Apps 
      
      
      
        
      section ("Add An Event"){
		app(name: "newApp", appName: "One To Many Child", namespace: "Cobra", title: "Add a new event automation child", multiple: true)
		
            
            }
                  
   
           
           
// End: New Child Apps
  
  
              section("App name") {
                label title: "Enter a name for parent app (optional)", required: false
            }
  
  
  
  
  
 } // DynamicPage 
  
  } // Mainpage




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
    state.appversion = "1.0.0"
     state.InternalName = "OneToManyparent"
}