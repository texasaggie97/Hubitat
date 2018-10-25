/**
 *  Design Usage:
 *  This is the 'Parent' app for all 'Cobra' apps
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
 *  Last Update: 18/10/2018
 *
 *  Changes:
 *
 * 
 *
 *  V1.0.0 - POC
 *
 */



definition(
    name:"Cobra Apps",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Parent container for all Cobra Apps ",
    category: "Convenience",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
    )







preferences {
	
     page name: "mainPage", title: "", install: true, uninstall: true
     
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
    installCheck()
    log.info "There are ${childApps.size()} child apps"
    childApps.each {child ->
    log.info "Child app: ${child.label}"
    }
    
}


def mainPage() {
    dynamicPage(name: "mainPage") {
      
       
if(state.appInstalled == 'COMPLETE'){
			display()
    if(state.appName1){
    state.para1 = "<b>Installed Apps:</b>"
        
        if(state.appName1.contains("Average All")) {state.para1 = state.para1 + "<BR>Average All"}
        if(state.appName1.contains("Message Central")){state.para1 = state.para1 + "<BR>Message Central"}
    	if(state.appName1.contains("Modes Plus")) {state.para1 = state.para1 + "<BR>Modes Plus"}
        if(state.appName1.contains( "One To Many")) {state.para1 = state.para1 + "<BR>One To Many"} 
    	if(state.appName1.contains("Presence Central")) {state.para1 = state.para1 + "<BR>Presence Central"}  
        if(state.appName1.contains("Scheduled Switch")) {state.para1 = state.para1 + "<BR>Scheduled Switch"}  
        if(state.appName1.contains("Temperature Controlled Switch")) {state.para1 = state.para1 + "<BR>Temperature Controlled Switch"}  
    	if(state.appName1.contains("Weather Switch")) {state.para1 = state.para1 + "<BR>Weather Switch"}  
    	
   
    
    section(){paragraph state.para1}}
                                
   
                               
 
   // section(" "){""}
    
    section(){
        paragraph "If you remove the Cobra Apps container, you will remove ALL configured apps & child apps from your system"
        paragraph  "You can only install apps here once you have already installed the code for that app (Both parent and child)"
             }
			childAppList()
    
    
    

  
	}
  }
}



def installCheck(){         
   state.appInstalled = app.getInstallationState() 
  if(state.appInstalled != 'COMPLETE'){
section{paragraph "Please hit 'Done' to install '${app.label}' parent app "}
  }
    else{
 //       log.info "Parent Installed OK"
    }
	}



def childAppList(){
    state.appName1 = ""
    childApps.each {child -> child.label
        state.appName1 = state.appName1 + " " + child.label
      
       }
   

     log.info "Installed Apps = $state.appName1"
    
     if(!state.appName1.contains("Average All")){
       section (""){
		app(name: "averageParent", appName: "Average All", namespace: "Cobra", title: "<b>Install Average All</b>", multiple: true)
            }
     }    

    if(!state.appName1.contains("Message Central")){
        section (""){
        app(name: "mcParent", appName: "Message Central", namespace: "Cobra", title: "<b>Install Message Central</b>", multiple: true)
        } 
    }

    if(!state.appName1.contains("Modes Plus")){
        section (""){
		app(name: "modesPlusParent", appName: "Modes Plus", namespace: "Cobra", title: "<b>Install Modes Plus</b>", multiple: true)
            }
     }
     if(!state.appName1.contains("One To Many")){
        section (""){
		app(name: "oneToManyParent", appName: "One To Many", namespace: "Cobra", title: "<b>Install One To Many</b>", multiple: true)
            }
     }
    if(!state.appName1.contains("Presence Central")){
        section (""){
		app(name: "presenceCentralsParent", appName: "Presence Central", namespace: "Cobra", title: "<b>Install Presence Central</b>", multiple: true)
            }
     }
     if(!state.appName1.contains("Scheduled Switch")){
        section (""){
		app(name: "scheduledSwitchParent", appName: "Scheduled Switch", namespace: "Cobra", title: "<b>Install Scheduled Switch</b>", multiple: true)
            }
     }
    if(!state.appName1.contains("Temperature Controlled Switch")){
        section (""){
		app(name: "tempControlledSwitchParent", appName: "Temperature Controlled Switch", namespace: "Cobra", title: "<b>Install Temperature Controlled Switch</b>", multiple: true)
            }
     }
    if(!state.appName1.contains("Weather Switch")){
        section (""){
		app(name: "weatherSwitchParent", appName: "Weather Switch", namespace: "Cobra", title: "<b>Install Weather Switch</b>", multiple: true)
            }
     }    
    
    
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
		state.InternalName = "CobraParent" 
    	state.ExternalName = "Cobra Apps Container"
}





