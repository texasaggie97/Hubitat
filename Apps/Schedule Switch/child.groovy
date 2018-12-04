/**
 * ****************  Scheduled Switch ****************
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
 *  Last Update: 04/12/2018
 *
 *  Changes:
 *
 *  V1.6.0 - Added disable apps code
 *  V1.5.0 - Streamlined restrictions page to action faster if specific restrictions not used.
 *  V1.4.0 - Move update notification to parent
 *  V1.3.0 - Debug & code cleanup
 *  V1.2.0 - added restrictions page
 *  V1.1.1 - set default switch states.
 *  V1.1.0 - Added 'return' option
 *  V1.0.1 - Code cleanup & revised version checking - Debug - March was not working correctly when selected
 *  V1.0.0 - POC 
 */
 
 
 
 
definition(
    name: "Scheduled Switch Child",
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
	section() {
	page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
	page name: "restrictionsPage", title: "", install: true, uninstall: true
	}
}

def mainPage() {
	dynamicPage(name: "mainPage") {  
	preCheck()

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
}
def restrictionsPage() {
    dynamicPage(name: "restrictionsPage") {
        section(){paragraph "<font size='+1'>App Restrictions</font> <br>These restrictions are optional <br>Any restriction you don't want to use, you can just leave blank or disabled"}
        section(){
		input "enableSwitchYes", "bool", title: "Enable restriction by external on/off switch", required: true, defaultValue: false, submitOnChange: true
			if(enableSwitchYes){
			input "enableSwitch", "capability.switch", title: "Select a switch Enable/Disable this app", required: false, multiple: false, submitOnChange: true 
			if(enableSwitch){ input "enableSwitchMode", "bool", title: "Allow app to run only when this switch is On or Off", required: true, defaultValue: false, submitOnChange: true}
			}
		}
        section(){
		input "modesYes", "bool", title: "Enable restriction by current mode(s)", required: true, defaultValue: false, submitOnChange: true	
			if(modesYes){	
			input(name:"modes", type: "mode", title: "Allow actions when current mode is:", multiple: true, required: false)
			}
		}	
       	section(){
		input "timeYes", "bool", title: "Enable restriction by time", required: true, defaultValue: false, submitOnChange: true	
			if(timeYes){	
    	input "fromTime", "time", title: "Allow actions from", required: false
    	input "toTime", "time", title: "Allow actions until", required: false
        	}
		}
		section(){
		input "dayYes", "bool", title: "Enable restriction by day(s)", required: true, defaultValue: false, submitOnChange: true	
			if(dayYes){	
    	input "days", "enum", title: "Allow actions only on these days of the week", required: false, multiple: true, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        	}
		}
		section(){
		input "presenceYes", "bool", title: "Enable restriction by presence sensor(s)", required: true, defaultValue: false, submitOnChange: true	
			if(presenceYes){	
    	input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor){input "restrictPresenceAction", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
     	input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor1){input "restrictPresenceAction1", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
   			}
		}	
		section(){
		input "sunrisesetYes", "bool", title: "Enable restriction by sunrise or sunset", required: true, defaultValue: false, submitOnChange: true	
			if(sunrisesetYes){
       	input "sunriseSunset", "enum", title: "Sunrise/Sunset Restriction", required: false, submitOnChange: true, options: ["Sunrise","Sunset"] 
		if(sunriseSunset == "Sunset"){	
       	input "sunsetOffsetValue", "number", title: "Optional Sunset Offset (Minutes)", required: false
		input "sunsetOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        	}
		if(sunriseSunset == "Sunrise"){
    	input "sunriseOffsetValue", "number", title: "Optional Sunrise Offset (Minutes)", required: false
		input "sunriseOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        	}
     	}
		}	
       
        section() {input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}
		 section() {label title: "Enter a name for this automation", required: false}
    }
}



  


def installed(){initialise()}
def updated(){initialise()}
def initialise(){
	version()
	subscribeNow()
	log.info "Initialised with settings: ${settings}"
	logCheck()	
}
def subscribeNow() {
	unsubscribe()
	if(enableSwitch){subscribe(enableSwitch, "switch", switchEnable)}
	if(enableSwitchMode == null){enableSwitchMode = true} // ????
	if(restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if(restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
	if(sunriseSunset){astroCheck()}
	if(sunriseSunset){schedule("0 1 0 1/1 * ? *", astroCheck)} // checks sunrise/sunset change at 00.01am every day
    
  // App Specific subscriptions & settings below here

	
	subscribe(switch1, "switch", switchHandler1)
    calculateCron1()
    state.return = return1
    if(state.return == true){calculateCron2()}    
    state.switchMode = false
    state.switchMode2 = false
   	
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
    checkAllow()
	if(state.allAllow == true){

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
    checkAllow()
	if(state.allAllow == true){

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


state.timer1 = true
	state.timerDoor = true
    state.timerlock = true


def setVersion(){
		state.version = "1.6.0"	 
		state.InternalName = "SchedSwitchChild"
    	state.ExternalName = "Scheduled Switch Child"
		state.preCheckMessage = "This app is designed to schedule a switch on/off some hours/days/weeks/months ahead..."
		state.CobraAppCheck = "scheduledswitch.json"
}




 
