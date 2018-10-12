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
 *  V1.2.0 - Added 'pause' switch
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

  paragraph title: "Schedule Switch Child", "Schedule a switch on a certain date & time"
 
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
    state.switchMode == false
    state.switchMode2 == false
	subscribe(switch1, "switch", switchHandler1)
    calculateCron1()
    state.return = return1
    if(state.return == true){
    calculateCron2()    
    }
   state.pauseApp = false
    
    
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
    if(state.pauseApp == false){
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
}
def switchNow2(){
    if(state.pauseApp == false){
state.switchMode2 = mode2
    
    if(state.switchMode2 == true){
        log.info "It's $state.selectedHour2:$state.selectedMin2 on $state.selectedMonth2 $state.selectedDate2 so switching on: $switch1"
        switch1.on()
    } 
        if(state.switchMode2 == false){
        log.info "It's $state.selectedHour2:$state.selectedMin2 on $state.selectedMonth2 $state.selectedDate2 so switching off: $switch1"
        switch1.off()
    } 
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
    updatecheck()
    if (state.Type == "Application"){schedule("0 0 9 ? * FRI *", updatecheck)}
    if (state.Type == "Driver"){schedule("0 0 8 ? * FRI *", updatecheck)}
}

def display(){
    section{paragraph "Version: $state.version -  $state.Copyright"}
	if(state.Status != "Current"){
       section{ 
       paragraph "$state.Status"
       paragraph "$state.updateInfo"
    }
    }
    
     section(){
    input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false  
        if(pause1 == true){
            if(app.label.contains('red')){log.warn "Already Paused"}
               else{app.updateLabel(app.label + (" <font color = 'red'>(Paused) </font>" ))}
            state.pauseApp = true
        log.warn "App Paused"
    }                 
     if(pause1 == false){
          if(app.label.contains('red')){
        app.updateLabel(app.label.minus("<font color = 'red'>(Paused) </font>"  ))
              state.pauseApp = false
       log.info "App Released"
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
     state.version = "1.2.0"
     state.InternalName = "SchedSwitchchild"
     state.Type = "Application"


}
 