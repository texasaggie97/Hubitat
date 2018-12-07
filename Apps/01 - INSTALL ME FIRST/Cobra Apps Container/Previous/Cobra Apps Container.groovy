/**
 *  Design Usage:
 *  This is the 'Parent' app for all 'Cobra' apps
 *
 *
 *  Copyright 2018 Andrew Parker
 *  
 *  This app is free!
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
 *  Last Update: 30/11/2018
 *
 *  Changes:
 *
 *  V2.4.0 - Added 'Disable All Cobra Apps' switch
 *  V2.3.0 - Added 'Irrigation Switch'
 *  V2.2.2 - Revised UI
 *  V2.2.1 - Revised update checking - separate json file
 *  V2.2.0 - Updated 'Motion Controlled Switch' to 'Motion Controlled Switches & Lights'
 *  V2.1.0 - Added Motion Controlled Switches & Lights
 *  V2.0.0 - Added 'Sunset Switch'
 *  V1.9.0 - Added 'Temperature Controlled Window-Vent-Blind'
 *  V1.8.0 - Added 'Temperature Controlled Single Speed Fan'
 *  V1.7.0 - Added 'Switch Chengeover'
 *  V1.6.0 - Added 'Motion Controlled Switch' (depreciated feature)
 *  V1.5.0 - Added 'Flasher'
 *  V1.4.0 - Added 'Mode Switch'
 *  V1.3.0 - Added 'Daily Window or Blind Event'
 *  V1.2.0 - Added 'Daily Switch Event'
 *  V1.1.0 - Added "Contact Controlled Lights and Switches" 
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







preferences {page name: "mainPage", title: "", install: true, uninstall: true}
def installed() {initialize()}
def updated() {initialize()}
def initialize() {
    unsubscribe()
	version()
    
    log.info "Initialised with settings: ${settings}"
    log.info "There are ${childApps.size()} installed child apps"
    childApps.each {child ->
    log.info "Child app: ${child.label}"
    }    
}


def mainPage() {
    dynamicPage(name: "mainPage") {   
	installCheck()
	if(state.appInstalled == 'COMPLETE'){
	display()
  section{
	paragraph "Cobra Apps is a collection of apps to aid in the everyday automation of your home <br>It is used as a 'container' to hold all your Cobra Apps together in one place."
}
        
 	chooseApps()
    if(state.appName1){
    state.para1 = "<b>Installed Apps:</b>"
        
        if(state.appName1.contains("Average All")) {state.para1 = state.para1 + "<BR>Average All"}
        if(state.appName1.contains("Check Open Contacts")) {state.para1 = state.para1 + "<BR>Check Open Contacts"}
        if(state.appName1.contains("Contact Controlled Lights and Switches")) {state.para1 = state.para1 + "<BR>Contact Controlled Lights and Switches"}
        if(state.appName1.contains("Daily Switch Event")){state.para1 = state.para1 + "<BR>Daily Switch Event"}
        if(state.appName1.contains("Daily Window or Blind Event")){state.para1 = state.para1 + "<BR>Daily Window or Blind Event"}
        if(state.appName1.contains("Flasher")){state.para1 = state.para1 + "<BR>Flasher"}
		if(state.appName1.contains("Irrigation Switch")){state.para1 = state.para1 + "<BR>Irrigation Switch"}
        if(state.appName1.contains("Message Central")){state.para1 = state.para1 + "<BR>Message Central"}
        if(state.appName1.contains("Mode Switch")) {state.para1 = state.para1 + "<BR>Mode Switch"}
        if(state.appName1.contains("Modes Plus")) {state.para1 = state.para1 + "<BR>Modes Plus"}
        if(state.appName1.contains("Motion Controlled Switches & Lights")) {state.para1 = state.para1 + "<BR>Motion Controlled Switches & Lights"}
        if(state.appName1.contains("One To Many")) {state.para1 = state.para1 + "<BR>One To Many"} 
    	if(state.appName1.contains("Presence Central")) {state.para1 = state.para1 + "<BR>Presence Central"}  
        if(state.appName1.contains("Scheduled Switch")) {state.para1 = state.para1 + "<BR>Scheduled Switch"}
if(state.appName1.contains("Sunset Switch")) {state.para1 = state.para1 + "<BR>Sunset Switch"}
        if(state.appName1.contains("Super Smart Fan")) {state.para1 = state.para1 + "<BR>Super Smart Fan"}
		if(state.appName1.contains("Switch Changeover")) {state.para1 = state.para1 + "<BR>Switch Changeover"}
        if(state.appName1.contains("Temperature Controlled Single-Speed Fan")) {state.para1 = state.para1 + "<BR>Temperature Controlled Single-Speed Fan"} 
        if(state.appName1.contains("Temperature Controlled Switch")) {state.para1 = state.para1 + "<BR>Temperature Controlled Switch"} 
        if(state.appName1.contains("Temperature Controlled Window-Vent-Blind")) {state.para1 = state.para1 + "<BR>Temperature Controlled Window-Vent-Blind"} 
    	if(state.appName1.contains("Weather Switch")) {state.para1 = state.para1 + "<BR>Weather Switch"}  
    	
   
    
    section(){paragraph state.para1}}
    section(){
        paragraph "<b>If you remove the Cobra Apps container, you will remove ALL configured apps & child apps from your system</b>"
        paragraph  "You can only load apps here once you have already installed the latest code for that app (both parent and child) <br>Parent & Child apps code can be found on GitHub <a href='https://github.com/CobraVmax/Hubitat/tree/master/Apps' target='_blank'>here</a> "
             }
    	childAppList()
		displayDisable()
	}
  }
}




// 

def childAppList(){
    state.appName1 = ""
    childApps.each {child -> child.label
        state.appName1 = state.appName1 + " " + child.label
}
    if(state.appList){
     if(!state.appName1.contains("Average All") && state.appList.contains("Average All")){
       section (""){
		app(name: "averageParent", appName: "Average All", namespace: "Cobra", title: "<b>Load 'Average All' into this container</b>", multiple: true)
            }
     }    
     if(!state.appName1.contains("Check Open Contacts") && state.appList.contains("Check Open Contacts")){
       section (""){
		app(name: "checkContactsParent", appName: "Check Open Contacts", namespace: "Cobra", title: "<b>Load 'Check Open Contacts' into this container</b>", multiple: true)
            }
     }
     if(!state.appName1.contains("Contact Controlled Lights and Switches") && state.appList.contains("Contact Controlled Lights and Switches")){
       section (""){
		app(name: "contactLightSwitchesParent", appName: "Contact Controlled Lights and Switches", namespace: "Cobra", title: "<b>Load 'Contact Controlled Lights and Switches' into this container</b>", multiple: true)
            }
     }        
        
    if(!state.appName1.contains("Daily Switch Event") && state.appList.contains("Daily Switch Event")){
        section (""){
        app(name: "dailySwitchEvent", appName: "Daily Switch Event", namespace: "Cobra", title: "<b>Load 'Daily Switch Event' into this container</b>", multiple: true)
        } 
    }

    if(!state.appName1.contains("Daily Window or Blind Event") && state.appList.contains("Daily Window or Blind Event")){
        section (""){
        app(name: "dailyWindowEvent", appName: "Daily Window or Blind Event", namespace: "Cobra", title: "<b>Load 'Daily Window or Blind Event' into this container</b>", multiple: true)
        } 
    }
     if(!state.appName1.contains("Flasher") && state.appList.contains("Flasher")){
        section (""){
        app(name: "flasherParent", appName: "Flasher", namespace: "Cobra", title: "<b>Load 'Flasher' into this container</b>", multiple: true)
        } 
    }   
		
	 if(!state.appName1.contains("Irrigation Switch") && state.appList.contains("Irrigation Switch")){
        section (""){
        app(name: "irrigationSwitchParent", appName: "Irrigation Switch", namespace: "Cobra", title: "<b>Load 'Irrigation Switch' into this container</b>", multiple: true)
        } 
    }   	
     if(!state.appName1.contains("Message Central") && state.appList.contains("Message Central")){
        section (""){
        app(name: "mcParent", appName: "Message Central", namespace: "Cobra", title: "<b>Load 'Message Central' into this container</b>", multiple: true)
        } 
    }
    if(!state.appName1.contains("Modes Plus") && state.appList.contains("Modes Plus")){
        section (""){
		app(name: "modesPlusParent", appName: "Modes Plus", namespace: "Cobra", title: "<b>Load 'Modes Plus' into this container</b>", multiple: true)
            }
     }
     if(!state.appName1.contains("Mode Switch") && state.appList.contains("Mode Switch")){
        section (""){
		app(name: "modeSwitchParent", appName: "Mode Switch", namespace: "Cobra", title: "<b>Load 'Mode Switch' into this container</b>", multiple: true)
            }
     }
     if(!state.appName1.contains("Motion Controlled Switches & Lights") && state.appList.contains("Motion Controlled Switches & Lights")){
        section (""){
		app(name: "motionSwitchParent", appName: "Motion Controlled Switches & Lights", namespace: "Cobra", title: "<b>Load 'Motion Controlled Switches & Lights' into this container</b>", multiple: true)
            }
     }      
     if(!state.appName1.contains("One To Many") && state.appList.contains("One To Many")){
        section (""){
		app(name: "oneToManyParent", appName: "One To Many", namespace: "Cobra", title: "<b>Loead 'One To Many' into this container</b>", multiple: true)
            }
     }
    if(!state.appName1.contains("Presence Central") && state.appList.contains("Presence Central")){
        section (""){
		app(name: "presenceCentralsParent", appName: "Presence Central", namespace: "Cobra", title: "<b>Load 'Presence Central' into this container</b>", multiple: true)
            }
     }
     if(!state.appName1.contains("Scheduled Switch") && state.appList.contains("Scheduled Switch")){
        section (""){
		app(name: "scheduledSwitchParent", appName: "Scheduled Switch", namespace: "Cobra", title: "<b>Load 'Scheduled Switch' into this container</b>", multiple: true)
            }
     }
     if(!state.appName1.contains("Sunset Switch") && state.appList.contains("Sunset Switch")){
        section (""){
		app(name: "sunsetSwitchParent", appName: "Sunset Switch", namespace: "Cobra", title: "<b>Load 'Sunset Switch' into this container</b>", multiple: true)
            }
     }
	 if(!state.appName1.contains("Super Smart Fan") && state.appList.contains("Super Smart Fan")){
        section (""){
		app(name: "ssfanParent", appName: "Super Smart Fan", namespace: "Cobra", title: "<b>Load 'Super Smart Fan' into this container</b>", multiple: true)
            }
     }   
		
     if(!state.appName1.contains("Switch Changeover") && state.appList.contains("Switch Changeover")){
        section (""){
		app(name: "switchChangeoverParent", appName: "Switch Changeover", namespace: "Cobra", title: "<b>Load 'Switch Changeover' into this container</b>", multiple: true)
            }
     }   
    
    if(!state.appName1.contains("Temperature Controlled Single-Speed Fan") && state.appList.contains("Temperature Controlled Single-Speed Fan")){
        section (""){
		app(name: "tempControlledFanParent", appName: "Temperature Controlled Single-Speed Fan", namespace: "Cobra", title: "<b>Load 'Temperature Controlled Single-Speed Fan' into this container</b>", multiple: true)
            }
     }    
    if(!state.appName1.contains("Temperature Controlled Switch") && state.appList.contains("Temperature Controlled Switch")){
        section (""){
		app(name: "tempControlledSwitchParent", appName: "Temperature Controlled Switch", namespace: "Cobra", title: "<b>Load 'Temperature Controlled Switch' into this container</b>", multiple: true)
            }
     }
    if(!state.appName1.contains("Temperature Controlled Window-Vent-Blind") && state.appList.contains("Temperature Controlled Window-Vent-Blind")){
        section (""){
		app(name: "tempControlledWindowVentParent", appName: "Temperature Controlled Window-Vent-Blind", namespace: "Cobra", title: "<b>Load 'Temperature Controlled Window-Vent-Blind' into this container</b>", multiple: true)
            }
     }    
    if(!state.appName1.contains("Weather Switch") && state.appList.contains("Weather Switch")){
        section (""){
		app(name: "weatherSwitchParent", appName: "Weather Switch", namespace: "Cobra", title: "<b>Load 'Weather Switch' into this container</b>", multiple: true)
            }
     }    
    }    
    
}




def chooseApps(){
    section(){input "IncludedApps", "enum", title: "Select Apps To Include", required: false, multiple: true, submitOnChange: true, options: checkInput()}
    state.appList = IncludedApps 
    if(!IncludedApps){section(){paragraph "You must choose at least one app from the list above"}  
     }
}


def checkInput(){
    listInput = [
        "Average All",
        "Check Open Contacts",
        "Contact Controlled Lights and Switches",
        "Daily Switch Event",
        "Daily Window or Blind Event",
        "Flasher",
		"Irrigation Switch",
        "Message Central",
        "Mode Switch",
        "Modes Plus",
        "Motion Controlled Switches & Lights",
        "One To Many",
        "Presence Central",
        "Scheduled Switch",
  //      "Sunset Switch",
		"Super Smart Fan",
        "Switch Changeover",
        "Temperature Controlled Single-Speed Fan",
        "Temperature Controlled Switch",
        "Temperature Controlled Window-Vent-Blind",
        "Weather Switch"
]  
    
    return listInput
}
   

    



def version(){
    resetBtnName()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
	updateCheck()  
    checkButtons()
   
}


def installCheck(){         
   state.appInstalled = app.getInstallationState() 
  if(state.appInstalled != 'COMPLETE'){
section{
	paragraph "Cobra Apps is a collection of apps to aid in the everyday automation of your home <br>It is used as a 'container' to hold all your Cobra apps together in one place.<br><br>Please hit 'Done' to install this app "
}
  }
   

   
	}

def display(){
	if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}}
	if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){section(){input "updateBtn", "button", title: "$state.btnName"}}
	if(state.status != "Current"){
		section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>Update URL: </b><font color = 'red'> $state.updateURI</font><hr>"}
		}
		section(){
		input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available for Cobra Apps", required: true, defaultValue: false, submitOnChange: true 
		if(updateNotification == true){ input "speakerUpdate", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
		}	
	
}


def displayDisable(){
	if(app.label){
	section("<hr>"){
		input "disableAll", "bool", title: "Disable <b><i>ALL</i></b> ${app.label} within this container", required: true, defaultValue: false, submitOnChange: true
		state.parentDisable = disableAll
		stopAll()
	}
	section("<hr>"){}
	}
	else{
	section("<hr>"){
		input "disableAll", "bool", title: "Disable <b><i>ALL</i></b> apps within this container", required: true, defaultValue: false, submitOnChange: true
		state.parentDisable = disableAll
		stopAll()
	}
	section("<hr>"){}
	}
	
}

def stopAll(){
	
	if(state.parentDisable == true){ 
		state.msg = "Disabled by Cobra Apps"
		childApps.each { child ->
		child.stopAllParent(state.parentDisable, state.msg)
		log.warn "Disabling: $child.label"
		}
	}	
	if(state.parentDisable == false){
		state.msg = "Enabled by Cobra Apps"
		childApps.each { child ->
		child.stopAllParent(state.parentDisable, state.msg)
		log.trace "Re-enabling: $child.label "
		}
	}
}


def checkButtons(){
//    log.debug "Running checkButtons"
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
//    log.info "Resetting Button"
    if(state.status != "Current"){
	state.btnName = state.newBtn
    }
    else{
 	state.btnName = "Check For Update" 
    }
}    
    


def updateCheck(){
    setVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"]
    
       	try {
        httpGet(paramsUD) { respUD ->
//  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def commentRead = (respUD.data.Comment)
       		state.Comment = commentRead

            def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
            state.updateURI = updateUri   
            
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            state.newver = newVerRaw
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
            state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.status = "<b>** This app is no longer supported by $state.author  **</b>"  
             log.warn "** This app is no longer supported by $state.author **" 
            
      		}           
		else if(currentVer < newVer){
        	state.status = "<b>New Version Available ($newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn " Update: $state.UpdateInfo "
             state.newBtn = state.status
            def updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
            
       		} 
		else{ 
      		state.status = "Current"
       		log.info("You are using the current version of this app")
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
		state.version = "2.4.0"	 
		state.InternalName = "CobraParent" 
    	state.ExternalName = "Cobra Apps Container"
    	state.CobraAppCheck = "cobraapps.json"
}





