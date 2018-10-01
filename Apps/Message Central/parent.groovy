/**
 *  ****************  Message Central..  ****************
 *
 *  Design Usage:
 *  This is the 'Parent' app for message automation..
 *
 *
 *  Copyright 2017 Andrew Parker.
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
 *  Last Update: 01/10/2018
 *
 *  Changes:
 *
 *  V2.1.1 - Revised auto update and added manual check for update button
 *  V2.1.0 - Code cleanup and remote version checking
 *  V2.0.0 - Port to Hubitat (disabled missed messages child)
 *  V1.1.0 - Added second child to remind of missed alerts
 *  V1.0.2 - Added icons
 *  V1.0.1 - Header & Debug
 *  V1.0.0 - POC
 *
 */

 
 definition(
    name: "Message Central",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Message Automation.",
   category: "Fun & Social",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")

preferences {
    
    page name: "mainPage", title: "", install: true, uninstall: true
    
   
}

def installed() {
    log.debug "Installed with settings: ${settings}"
    initialize()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
    
    initialize()
}

def initialize() {
    version()
    
    log.debug "there are ${childApps.size()} child smartapps"
    childApps.each {child ->
        log.debug "child app: ${child.label}"
    }
    
   
    
}

def mainPage() {
    dynamicPage(name: "mainPage") {
installCheck()
if(state.appInstalled == 'COMPLETE'){
			display()
    
     section (""){
            app(name: "switchMessageAutomation", appName: "Message_Central_Child", namespace: "Cobra", title: "Create New Triggered Message", multiple: true)
            }  
     section(" ") {
        label title: "Enter a name for parent app (optional)", required: false
            }      
		}
    }
}
    
    
def installCheck(){         
   state.appInstalled = app.getInstallationState() 
  if(state.appInstalled != 'COMPLETE'){
section{paragraph "Please hit 'Done' to install Message Central"}
  }
    else{
 //       log.info "Parent Installed OK"
    }
	}

def version(){
    resetBtnName()
	unschedule()
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

	paragraph "<b>Update Info: $state.UpdateInfo ***</b>"
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
             state.newBtn = state.status
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
		state.version = "2.1.1"	 
		state.InternalName = "MCparent"  
}

