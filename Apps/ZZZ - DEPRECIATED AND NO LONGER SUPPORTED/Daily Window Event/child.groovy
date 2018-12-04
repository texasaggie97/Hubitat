/**
 *  Copyright 2018 Andrew Parker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Scheduled wind/vent opening/closing
 *  
 *  Changes
 *
 *
 *  
 *  V1.0.0 - POC
 *
 *  Last updated 29/10/2018
 *
 *  Sets a schedule to open a window/vent to a set level, then schedule it to close again 
 *
 *  Author: Andrew Parker
 */





definition(
    name: "Daily Window Event Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Schedule a window/vent to open to a set level then automatically close when you specify.",
    category: "Convenience",
    
   parent: "Cobra:Daily Window Event",
    
    iconUrl: "",
    iconX2Url: ""
)

preferences {
	 section("Which switch to Enable/Disable App") {
        input(name:"switch1", type: "capability.switch", required: false, multiple: true)
	}
	section("Open time...") {
		input (name: "switchOnTime", title: "At what time?", type: "time",  required: true)
	}
    section("Close time...") {
		input (name: "switchOffTime", title: "At what time?", type: "time",  required: true)
	}
    section("On Which Days") {
        input "days", "enum", title: "Select Days of the Week", required: true, multiple: true, options: ["Monday": "Monday", "Tuesday": "Tuesday", "Wednesday": "Wednesday", "Thursday": "Thursday", "Friday": "Friday", "Saturday": "Saturday", "Sunday": "Sunday"]
    }
    section("Which window/vent to control...") {
        input(name:"theSwitch", type: "capability.switchLevel", required: true, multiple: true)
	}
    section("What Level to open to") {
		input(name:"setLevel", type:"number", title: "%", required: true )// , options: [10,20,30,40,50,60,70,80,90,100])
	}		
    
}

def installed() {
    version()
	log.debug "Installed with settings: ${settings}"
	schedule(switchOnTime, switchOnNow)
    schedule(switchOffTime, switchOffNow)
     subscribe(switch1, "switch", switch1Handler)
    
         
}

def updated() {
    version()
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	schedule(switchOnTime, switchOnNow)
    schedule(switchOffTime, switchOffNow)
    subscribe(switch1, "switch", switch1Handler)
    
   
   
}

def switch1Handler(evt){
   state.currS1 = evt.value 
   log.trace " $switch1 is $state.currS1"
  }



def switchOnNow(evt) {
if (state.currS1 != "off" ) {
 def df = new java.text.SimpleDateFormat("EEEE")
    
    df.setTimeZone(location.timeZone)
    def day = df.format(new Date())
    def dayCheck = days.contains(day)
    if (dayCheck) {

def myLevel = setLevel.toInteger()


	log.debug "Opening $theSwitch..."
    theSwitch.setLevel(myLevel)
//    theSwitch.setPosition(myLevel) 
    log.debug "$theSwitch is set to $myLevel"
   
    
 } 
 else {
 log.debug "Not today"
 }
 }
 
 else {
 log.debug "$switch1 is off app is not active"
}
}

def switchOffNow(evt){
log.debug "Switching back off"
theSwitch.off()
log.debug "$theSwitch is now off"
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
	paragraph "<b>Update Info:</b> <BR>$state.UpdateInfo <BR>$state.updateURI"
     }
    }
	section(" ") {
      input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available", required: true, defaultValue: false, submitOnChange: true 
      if(updateNotification == true){ input "speaker", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
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
    
def pushOverUpdate(inMsg){
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
            pushOverUpdate(updateMsg)
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
		state.InternalName = "DailyWindowEventChild"
    	state.ExternalName = "Daily Window Event Child"
}

