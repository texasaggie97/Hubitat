/**
 * ****************  Schedule Switch ****************
 *
 *  Design Usage:
 *	This was designed to schedule a switch on/off some months ahead
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
 *  Last Update: 08/10/2018
 *
 *  Changes:
 *
 *
 *  V1.1.1 - set default switch states.
 *  V1.1.0 - Added 'return' option
 *  V1.0.1 - Code cleanup & revised version checking - Debug - March was not working correctly when selected
 *  V1.0.0 - POC 
 */
 
 
 
 
definition(
    name: "Schedule Switch Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Schedule a switch on a certain date & time",
    category: "Convenience",
        
    parent: "Cobra:Scheduled Switch",
    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )


preferences {

section ("") {

  paragraph "Schedule a switch on a certain date & time"
 
 }
    
 display()
	 section(){
		input "switch1",  "capability.switch",  title: "Switch to Schedule", multiple: false, required: true
		input "month1", "enum", title: "Select Month", required: true, options: [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		input "date1", "enum", title: "Select Date", required: true, options: [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"]
		input "hour1", "enum", title: "Select Hour", required: true,  options: [ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]
		input "min1", "enum", title: "Select Minute", required: true, options: [ "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"]
	    input "mode1", "bool", title: "Turn Switch On or Off", required: true, submitOnChange: true, defaultValue: false    
	    }
     section(){
    	input "return1", "bool", title: "Schedule a 'return' event", required: true, submitOnChange: true, defaultValue: false  
         if(return1){
        input "month2", "enum", title: "Select Month", required: true, options: [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		input "date2", "enum", title: "Select Date", required: true, options: [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"]
		input "hour2", "enum", title: "Select Hour", required: true,  options: [ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]
		input "min2", "enum", title: "Select Minute", required: true, options: [ "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"]
	    input "mode2", "bool", title: "Turn Switch On or Off", required: true, submitOnChange: true, defaultValue: false       
             
         }
     }
    
    
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unschedule()
	unsubscribe()
	initialize()
}

def initialize() {
    version()
    logCheck()
	subscribe(switch1, "switch", switchHandler1)
    calculateCron1()
    state.return = return1
    if(state.return == true){calculateCron2()}    
    state.switchMode = false
    state.switchMode2 = false
   	state.appGo = true
    state.btnName = "Pause"
    state.pause = false
}




def calculateCron1(){

state.selectedMonth = month1    
    if(state.selectedMonth == "Jan"){state.runMonth = "1"}
    if(state.selectedMonth == "Feb"){state.runMonth = "2"}
    if(state.selectedMonth == "Mar"){state.runMonth = "3"}
    if(state.selectedMonth == "Apr"){state.runMonth = "4"}
    if(state.selectedMonth == "May"){state.runMonth = "5"}
    if(state.selectedMonth == "Jun"){state.runMonth = "6"}
    if(state.selectedMonth == "Jul"){state.runMonth = "7"}
    if(state.selectedMonth == "Aug"){state.runMonth = "8"}
    if(state.selectedMonth == "Sep"){state.runMonth = "9"}
    if(state.selectedMonth == "Oct"){state.runMonth = "10"}
    if(state.selectedMonth == "Nov"){state.runMonth = "11"}
    if(state.selectedMonth == "Dec"){state.runMonth = "12"}
 
state.selectedDate = date1
state.selectedHour = hour1
state.selectedMin = min1
state.schedule1 = "0 ${state.selectedMin} ${state.selectedHour} ${state.selectedDate} ${state.runMonth} ? *"
    
    log.info "state.schedule1 = $state.schedule1"
    schedule(state.schedule1, switchNow1) 


    
}


def calculateCron2(){

state.selectedMonth2 = month2    
    if(state.selectedMonth2 == "Jan"){state.runMonth2 = "1"}
    if(state.selectedMonth2 == "Feb"){state.runMonth2 = "2"}
    if(state.selectedMonth2 == "Mar"){state.runMonth2 = "3"}
    if(state.selectedMonth2 == "Apr"){state.runMonth2 = "4"}
    if(state.selectedMonth2 == "May"){state.runMonth2 = "5"}
    if(state.selectedMonth2 == "Jun"){state.runMonth2 = "6"}
    if(state.selectedMonth2 == "Jul"){state.runMonth2 = "7"}
    if(state.selectedMonth2 == "Aug"){state.runMonth2 = "8"}
    if(state.selectedMonth2 == "Sep"){state.runMonth2 = "9"}
    if(state.selectedMonth2 == "Oct"){state.runMonth2 = "10"}
    if(state.selectedMonth2 == "Nov"){state.runMonth2 = "11"}
    if(state.selectedMonth2 == "Dec"){state.runMonth2 = "12"}
 
state.selectedDate2 = date2
state.selectedHour2 = hour2
state.selectedMin2 = min2
state.schedule2 = "0 ${state.selectedMin2} ${state.selectedHour2} ${state.selectedDate2} ${state.runMonth2} ? *"
    
    log.info "state.schedule2 = $state.schedule2"
    schedule(state.schedule2, switchNow2) 


    
}



def switchNow1(){
state.switchMode = mode1
    
    if(state.switchMode == true){
        log.info "It's $state.selectedHour:$state.selectedMin on $state.selectedMonth $state.selectedDate so switching on: $switch1"
        switch1.on()
    } 
        if(state.switchMode == false){
        log.info "It's $state.selectedHour:$state.selectedMin on $state.selectedMonth $state.selectedDate so switching off: $switch1"
        switch1.off()
    } 
    
}
def switchNow2(){
state.switchMode2 = mode2
    
    if(state.switchMode2 == true){
        log.info "It's $state.selectedHour2:$state.selectedMin2 on $state.selectedMonth2 $state.selectedDate2 so switching on: $switch1"
        switch1.on()
    } 
        if(state.switchMode == false){
        log.info "It's $state.selectedHour2:$state.selectedMin2 on $state.selectedMonth2 $state.selectedDate2 so switching off: $switch1"
        switch1.off()
    } 
    
}





def switchHandler1 (evt) {
def switching = evt.value
    if(switching == "on"){
        log.info "Switch is turned on"
    }
        
    if(switching == "off"){
        log.info "Switch is turned off"
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
		state.version = "1.1.1"	 
		state.InternalName = "SchedSwitchchild"
    	state.ExternalName = "Scheduled Switch Child"
}




 
