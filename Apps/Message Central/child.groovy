/**
 *  ****************  Message_Central_Child  ****************
 *
 *  Design Usage:
 *  This is the 'Child' app for message automation...
 *
 *
 *  Copyright 2018 Andrew Parker
 *
 *  A big 'Thank You!' to @matthew for his creative work on the number of random phrases
 *  Without him, you would not have this feature!
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
 *
 *  Last Update: 20/11/2018
 *
 *  Changes:
 *
 *  V13.2.3 - Debug - Fixed bug where %time% variable would not work with 24Hr switched on.
 *  V13.2.2 - Changed contact input to multiple so ANY selected contact will trigger open/close message
 *  V13.2.1 - Debug - Update pushover message had same speaker name as others - changed now.
 *  V13.2.0 - Changed the way message is converted to English - Now converted to lowercase before processing.
 *  V13.1.1 - Debug 'poll' before weather summary
 *	V13.1.0 - Reworked all the restrictions to cleanup the code and reduce duplication of methods
 *  V13.0.1 - Debug and added preconfigured defaults settings
 *  V13.0.0 - Added configurable actions on 'restriction' switch 
 *  V12.9.0 - Added multiple speaker slots for 'quiet time'
 *  V12.8.0 - Added multiple speaker volume slots for 'normal time'
 *  V12.7.1 - Edited variables help page to show new variables available
 *  V12.7.0 - Added app pause switch
 *  V12.6.0 - Added a bunch of new variables so you can report on light & switch state
 *  V12.5.1 - Change the way %rain% is spoken - If 0% then: 'Rain is not expected today" - If n% then 'There is a n% chance of rain today
 *  V12.5.0 - Added an optional, configurable 'prefix' input for weather alerts - "Message to play before weather alert (optional)"
 *  V12.4.0 - Added new trigger: 'Weather Alerts' will alert if weather device reports a change in 'Alert' attribute
 *  V12.3.5 - Fixed issue with '0 pm' being spoken not "o'clock" as it should be on the hour (12 hr setting)
 *  V12.3.4 - Debug 'Join' message on Lock/Unlock
 *  V12.3.3 - Debug lock trigger
 *  V12.3.2 - Debug mp3 playback - Now fixed!
 *  V12.3.1 - Added optional pushover message for updates
 *  V12.3.0 - Added %alert% as a variable for use with weather devices
 *	V12.2.1 - Revised auto update checking and added manual check for update button
 *  V12.2.0 - Added 'Lock' as a trigger
 *  V12.1.1 - Code consolidation & Debug mp3 playing
 *  V12.1.0 - Added three new variables for use within messages: %opencount%, %closedcount% & %mode%
 *  V12.0.0 - Added additional %group% random phrases and made the number of random phrases selectable for each group - Thanks to @matthew for his creative work on this
 *  V11.1.0 - Fixed 'Presence' (with speechSynth device) - Fixed 'Time Restrictions' - Fixed 'Quiet Time' - Made all other variables work inside 'Group#' random variable
 *  V11.0.0 - Major recode to cover options for 'Speech Synthesis'
 *  V10.6.0 - Added weather variables (use with weather driver or individual sensors)
 *  V10.5.0 - Debug disable switch & power input (allow decimal input)
 *  V10.4.0 - Sorted out %date% %year% & %day% variables (to correct errors in different format for Hubitat)
 *  V10.3.0 - Added Mode restriction in Hubitat format (previous format was not working correctly) and debug/reformat of version checking
 *  V10.2.1 - Debug 'colon' was being spoken - now replaced with a space in message conversion
 *  V10.2.0 - Added version checking
 *  V10.1.0 - Added 'Join' messaging & debug
 *  V10.0.0 - Hubitat port
 *
 *
 *  V3.3.0 - Made random messages configurable from GUI
 *  V3.2.0 - added random 'pre' & 'post' message variables - also 'wakeup' variable messages
 *  V3.1.2 - added variable: %greeting% - to say 'good morning' , 'good afternoon' ... etc...
 *  V3.1.1 - UI slight revamp
 *  V3.1.0 - Added a second presence restriction - Useful if you want something to happen when one person is home but NOT another
 *  V3.0.1 - Added additional variables: %device% & %event%
 *  V3.0.0 - Added a new trigger setup 'Appliance Power Monitor' - This uses a second power threshold which must be exceeded before monitoring starts
 *  V2.9.0 - Added Missed message config to 'Time' trigger
 *  V2.8.0 - Added %opencontact% variable to check any open windows/door
 *  V2.7.0 - Added 'Button' as a trigger - either pushed or held
 *  V2.6.1 - Added delay between messages to SMS/Push
 *  V2.6.0 - Added 'Temperature' trigger ability for above or below configured temperature
 *  V2.5.0 - Added 'Motion' trigger ability for motion 'active' or 'inactive'
 *  V2.4.1 - Debug issue with presence restrictions not working correctly
 *  V2.4.0 - Revamped Weather Report - converted it to variable %weather%
 *  V2.3.2 - Changed %day% variable to correct English
 *  V2.3.1 - Added option to use 24hr format
 *  V2.3.0 - Added %time%, %day%, %date%, %year% as variables used in messages
 *  V2.2.0 - Removed requirement for allowed time & days - Now optional
 *  V2.1.0 - GUI revamp - Moved restrictions to their own page
 *  V2.0.1 - Debug
 *  V2.0.0 - Added 'Weather Report' - Trigger with Switch, Water, Contact, & Time
 *  V1.9.0 - Added 'Open Too Long' to speak when a contact (door?) has been open for more than the configured number of minutes
 *  V1.8.0 - Added ability to speak/send message if contact is open at a certain time (Used to check I closed the shed door)
 *  V1.7.0 - Added ability to SMS/Push instead of speaking
 *  V1.6.0 - Added Routines & Mode Change as triggers
 *  V1.5.1 - Debug - Disable switch not always working
 *  V1.5.0 - Added 'Presence' restriction so will only speak if someone is present/not present
 *  V1.4.0 - Added 'Power' trigger and ability to use 'and stays that way' to use with Washer or Dryer applicance
 *  V1.3.2 - Debug
 *  V1.3.1 - Code cleanup & new icon path
 *  V1.3.0 - Added 'quiet' time to allow different volume levels at certain times
 *  V1.2.2 - New Icons
 *  V1.2.1 - Debug - Time did not have day restriction
 *  V1.2.0 - Added switchable logging
 *	V1.1.0 - Added delay between messages
 *  V1.0.2 - Debug
 *  V1.0.1 - Header & Debug
 *  V1.0.0 - POC
 *
 *  If modifying this project, please keep the above header intact and add your comments/credits below - Thank you! -  @Cobra
 *
 *-------------------------------------------------------------------------------------------------------------------
 */

definition(
    name: "Message_Central_Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Child App for Message Automation",
     category: "Fun & Social",

 parent: "Cobra:Message Central",

    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")

preferences {
    page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "setUpPage"
	  page name: "pageHelp"
  	  page name: "pageHelpVariables"
	  page name: "prePostPage1"
	  page name: "prePostPage2"
	  page name: "wakeUpPage"
	  page name: "prePostPage4"
    
    page name: "setUpPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage" // "variablesPage"
 //  page name: "variablesPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
    page name: "restrictionsPage", title: "", install: true, uninstall: true
    
}

def installed() {
    
  
    initialize()
}

def updated() {
    unschedule()
    initialize()
}

def initialize() {
	logCheck()
    setDefaults()
    version()
    log.info "Initialised with settings: ${settings}"
      
      
      
     
      switchRunCheck()
      state.timer1 = true
      state.timer2 = true
      state.timeDelay = 0
	  state.duration = 30
      state.mp3Timer = true
  	  state.mp3Switch = mp3Action
    
      if (!restrictPresenceSensor){
      state.presenceRestriction = true
      }
      if (!restrictPresenceSensor1){
      state.presenceRestriction1 = true
      }
      state.contact1SW = 'closed' 
     if(state.msgType == "Voice Message (MusicPlayer)"){ 
     checkVolume()
     }
    if(msgDelay == null){state.msgDelay = 0}
    else{state.msgDelay = msgDelay}
      
// Subscriptions    

    if(enableSwitch){subscribe(enableSwitch, "switch", switchEnable)}
subscribe(location, "mode", modeHandler)

if(trigger == 'Time'){
   LOGDEBUG("Trigger is $trigger")
   schedule(runTime,timeTalkNow)
  if (missedPresenceSensor1){subscribe(missedPresenceSensor1, "presence", missedPresenceCheckNow)}
    }
    
if(trigger == 'Time if Contact Open'){
   LOGDEBUG("Trigger is $trigger")
   schedule(runTime,timeTalkNow1)
   subscribe(contact1, "contact", contact1Handler)
    }   
    
else if(trigger == 'Button'){
     LOGDEBUG( "Trigger is $trigger")
subscribe(button1, "button", buttonEvent)
    }
    
else if(trigger == 'Switch'){
     LOGDEBUG( "Trigger is $trigger")
subscribe(switch1, "switch", switchTalkNow)
    }
else if(trigger == 'Water'){
    LOGDEBUG( "trigger is $trigger")
subscribe(water1, "water.wet", waterTalkNow) 
subscribe(water1, "water.dry", waterTalkNow) 
	}
else if(trigger == 'Contact'){
    LOGDEBUG( "trigger is $trigger")
subscribe(contactSensor, "contact", contactTalkNow) 
	}
else if(trigger == 'Presence'){
    LOGDEBUG("trigger is $trigger")
subscribe(presenceSensor1, "presence", presenceTalkNow) 
     
	}
else if(trigger == 'Power'){
    LOGDEBUG("trigger is $trigger")
subscribe(powerSensor, "power", powerTalkNow) 
     
	}
    
else if(trigger == 'Appliance Power Monitor'){
    LOGDEBUG("trigger is $trigger")
subscribe(powerSensor, "power", powerApplianceNow) 
     
	}    
    
else if(trigger == 'Motion'){
    LOGDEBUG("trigger is $trigger")
subscribe(motionSensor, "motion" , motionTalkNow) 
     
	}    
    
else if(trigger == 'Temperature'){
    LOGDEBUG("trigger is $trigger")
subscribe(tempSensor, "temperature" , tempTalkNow) 
     
	}    

else if(trigger == 'Mode Change'){
    LOGDEBUG("trigger is $trigger")
 subscribe(location, "mode", modeChangeHandler)

	}
    
else if(trigger == 'Contact - Open Too Long'){
    LOGDEBUG("trigger is $trigger")
subscribe(openSensor, "contact", tooLongOpen)
state.timeDelay = 0
	}
    
else if(trigger == 'Lock/Unlock'){
    LOGDEBUG("trigger is $trigger")
subscribe(lock1, "lock" , lockTalkNow) 
     
	}        

    
if (restrictPresenceSensor){
subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)
}    

if (restrictPresenceSensor1){
subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)
}    

    if(weather1){
        subscribe(weather1, "alert", weatherAlert)
    	subscribe(weather1, "weatherSummary", weatherSummaryHandler)
		subscribe(weather1, "weather", weatherNow) 
		subscribe(weather1, "forecastHigh", weatherForecastHigh) 
        subscribe(weather1, "forecastLow", weatherForecastLow) 
        subscribe(weather1, "humidity", weatherHumidity) 
        subscribe(weather1, "temperature", weatherTemperature) 
        subscribe(weather1, "feelsLike", weatherFeelsLike) 
        subscribe(weather1, "wind_dir", weatherWindDir) 
        subscribe(weather1, "wind", weatherWindSpeed) 
        subscribe(weather1, "wind_gust", weatherWindGust) 
        subscribe(weather1, "visibility", weatherVisibility) 
 		subscribe(weather1, "chanceOfRain", weatherChanceOfRain)
    }
    if(weather2){ subscribe(weather2, "alert", weatherAlert)}
    if(lights){subscribe(lights, "switch",lightsOnOff)}
    if(switches){subscribe(switches, "switch",switchesOnOff)}
    if(enableSwitchMode == null){enableSwitchMode = true}
}


// main page *************************************************************************
def mainPage() {
    dynamicPage(name: "mainPage") {
		
		
	    section {
        paragraph title: "Message Central Child",
                  required: false,
                  "This child app allows you use different triggers to create different messages"
                  }
    display()
   
       section{
              href "pageHelp", title:"Message Variables", description:"Tap here for a list of 'variables' you can configure for use in your messages (and what they do)"
        }

    
      section() {
        	speakerInputs()
            triggerInput()
            actionInputs()
        }
         }
}


def variablesPage() {
       dynamicPage(name: "variablesPage") {
            
      }  
    }
    
def prePostPage1() {
	dynamicPage(name: "prePostPage1") {
    state.preCount = settings.preCount.toInteger()
        section() { 
            if (state.preCount != null && state.preCount > 0) {
                for(int i = 0; i<state.preCount; i++) {
					g1Item = getGroup1(i)
                    g1ItemPadded = (i<9 ?'0' : '') + (i+1)
					if(state.preCount>i){
                        input "preMsg${g1ItemPadded}", "text", title: " %group1% -  message ${g1ItemPadded}",  required: false, defaultValue: "${g1Item}"
					}
                }
            }
        }  
    }
}   

       
def prePostPage2() {
	dynamicPage(name: "prePostPage2") {
    state.postCount = settings.postCount.toInteger()
		section() {
            if (state.postCount != null && state.postCount > 0) {
                for(int i = 0; i<state.postCount; i++) {
					g2Item = getGroup2(i)
                    g2ItemPadded = (i<9 ?'0' : '') + (i+1)
					if(state.postCount>i){
                        input "postMsg${g2ItemPadded}", "text", title: " %group2% -  message ${g2ItemPadded}",  required: false, defaultValue: "${g2Item}"
					}
                }
            }
		}  
	}
}

def prePostPage4() {
	dynamicPage(name: "prePostPage4") {
    state.postCount1 = settings.postCount1.toInteger()
		section() {
            if (state.postCount1 != null && state.postCount1 > 0) {
                for(int i = 0; i<state.postCount1; i++) {
					g4Item = getGroup4(i)
                    g4ItemPadded = (i<9 ?'0' : '') + (i+1)
					if(state.postCount1>i){
                        input "postMsg${g4ItemPadded}", "text", title: " %group4% -  message ${g4ItemPadded}",  required: false, defaultValue: "${g4Item}"
					}
                }
            }
		}  
	}
}

def wakeUpPage() {
	dynamicPage(name: "wakeUpPage") {
	state.wakeCount = settings.wakeCount.toInteger()        
		section() { 
            if (state.wakeCount != null && state.wakeCount > 0) {
                for(int i = 0; i<state.wakeCount; i++) {
					g3Item = getGroup3(i)
                    g3ItemPadded = (i<9 ?'0' : '') + (i+1)
					if(state.wakeCount>i){
                        input "wakeMsg${g3ItemPadded}", "text", title: " %group3% -  message ${g3ItemPadded}",  required: false, defaultValue: "${g3Item}"
					}
                }
            }
		}  
	}
} 
   
def pageHelpVariables(){
    dynamicPage(name: "pageHelpVariables", title: "Message Variables", install: false, uninstall:false){
  section("The following variables can be used in your event messages and will be replaced with the details listed below"){

	def AvailableVariables = ""

	AvailableVariables += " %time% 			-		Replaced with current time in 12 or 24 hour format (Switchable)\n\n"
	AvailableVariables += " %day% 			- 		Replaced with current day of the week\n\n"
	AvailableVariables += " %date% 			- 		Replaced with current day number & month\n\n"
	AvailableVariables += " %year% 			- 		Replaced with the current year\n\n"
    AvailableVariables += " %greeting% 		- 		Replaced with 'Good Morning', 'Good Afternoon' or 'Good Evening' (evening starts at 6pm)\n\n"
 

    AvailableVariables += " %group1%		- 		Replaced with the a random message from Group1\n\n"
    AvailableVariables += " %group2%		- 		Replaced with the a random message from Group2\n\n"
    AvailableVariables += " %group3%		- 		Replaced with the a random message from Group3\n\n"
      
	
	AvailableVariables += " %opencontact% 	- 		Replaced with a <b>list</b> of configured contacts if they are open\n\n"
    AvailableVariables += " %opencount% 	- 		Replaced with the <b>number</b> of configured contacts that are open\n\n"
    AvailableVariables += " %closedcontact% - 		Replaced with a <b>list</b> of configured contacts if they are closed\n\n"
    AvailableVariables += " %closedcount% 	-   		Replaced with the <b>number</b> of configured contacts that are closed\n\n"
      
    AvailableVariables += " %lightsOn% 	-   			Replaced with the <b>list</b> of configured lights that are on\n\n"  
    AvailableVariables += " %lightsOncount% - 		Replaced with the <b>number</b> of configured lights that are on\n\n" 
    AvailableVariables += " %lightsOff% 	- 			Replaced with the <b>list</b> of configured lights that are off\n\n"  
    AvailableVariables += " %lightsOffcount% - 		Replaced with the <b>number</b> of configured lights that are off\n\n"  
    AvailableVariables += " %switchesOn% 	- 		Replaced with the <b>list</b> of configured switches that are on\n\n"  
    AvailableVariables += " %switchesOncount% - 		Replaced with the <b>number</b> of configured switches that are on\n\n"   
    AvailableVariables += " %switchesOff% 	- 		Replaced with the <b>list</b> of configured switches that are off\n\n"  
    AvailableVariables += " %switchesOffcount% - 		Replaced with the <b>number</b> of configured switches that are off\n\n"    
	AvailableVariables += " %device% 		- 		Replaced with the name of the triggering device\n\n"
	AvailableVariables +=  " %event% 		- 		Replaced with what triggered the action (e.g. On/Off, Wet/Dry) \n\n" 
    AvailableVariables +=  " %mode% 		- 		Replaced with the current hub location mode \n\n" 
      
    AvailableVariables += " Weather Variables (Available if your weather device supports them as attributes)\n\n" 
    AvailableVariables +=  " %alert% 		- 		Replaced with weather alert  \n\n" 
    AvailableVariables +=  " %wsum% 		- 		Replaced with weather summary  \n\n" 
    AvailableVariables +=  " %high%   		- 		Replaced with 'Forecast High' \n\n" 
    AvailableVariables +=  " %low%   		- 		Replaced with 'Forecast Low \n\n" 
    AvailableVariables +=  " %hum%  		- 		Replaced with 'Humidity' \n\n" 
    AvailableVariables +=  " %wnow% 		- 		Replaced with 'Weather Now' \n\n" 
    AvailableVariables +=  " %rain%   		- 		Replaced with 'Chance of Rain' \n\n" 
    AvailableVariables +=  " %vis%			- 		Replaced with 'Visibility' \n\n" 
    AvailableVariables +=  " %wgust% 		- 		Replaced with 'Wind Gust'  \n\n" 
    AvailableVariables +=  " %wspeed% 		- 		Replaced with 'Wind Speed'  \n\n"
    AvailableVariables +=  " %wdir%   		- 		Replaced with 'Wind Direction'  \n\n"
    AvailableVariables +=  " %feel%   		- 		Replaced with 'FeelsLike'  \n\n"
    AvailableVariables +=  " %temp% 		- 		Replaced with 'Temperature' "
    
      

	paragraph(AvailableVariables)
	}
  }
}    
   
   
def pageHelp(){
	 dynamicPage(name: "pageHelp", title: "Message Variables", install: false, uninstall:false){
section(){
	href "pageHelpVariables", title:"Message Variables Help", description:"Tap here to view the available variables"
    input "preCount", "number", title: "Number of 'Group1' Random Phrases?", submitOnChange: true,required: true, defaultValue: 5;            
    if(settings.preCount>0){href "prePostPage1", title:"Group1 Random Messages", description:"Tap here to configure 'Group1' random messages"}
    input "postCount", "number", title: "Number of 'Group2' Random Phrases?", submitOnChange: true, required: true, defaultValue: 5;                
    if(settings.postCount>0){href "prePostPage2", title:"Group2 Random Messages", description:"Tap here to configure 'Group2 random messages"}
    input "wakeCount", "number", title: "Number of 'Group3' Random Phrases?", submitOnChange: true, required: true, defaultValue: 5;                
    if(settings.wakeCount>0){href "wakeUpPage", title:"Group3 Random Messages", description:"Tap here to configure 'Group3' random messages"}
    input "postCount1", "number", title: "Number of 'Group4' Random Phrases?", submitOnChange: true,required: true, defaultValue: 5;            
    if(settings.postCount1>0){href "prePostPage4", title:"Group4 Random Messages", description:"Tap here to configure 'Group4' random messages"}
    }
  }
}    
    

def setUpPage() {
       dynamicPage(name: "setUpPage") {
       section() { 
       input "hour24", "bool", title: "If using the %time% variable, On = 24hr format - Off = 12Hr format", required: true, defaultValue: false, submitOnChange: true
       }
       section("Check Contacts") { 
       input "sensors", "capability.contactSensor", title: "If using the %opencontact% or %closedcontact% variable, choose window/door contact", required: false, multiple: true, submitOnChange: true       
       } 
        section("Check Lights") { 
       input "lights", "capability.switch", title: "If using the %lightsOn%,  %lightsOff%, %lightsOnCount% or %lightsOffCount% variable, choose lights", required: false, multiple: true, submitOnChange: true      
       }  
       section("Check Switches") { 
       input "switches", "capability.switch", title: "If using the %switchesOn%, %switchesOff%, %switchesOnCount% or %switchesOffCount% variable, choose switches", required: false, multiple: true, submitOnChange: true       
       }         
        section("Weather Device") { 
       input "weather1", "capability.sensor", title: "If using any of the weather variables (Except 'Alert' trigger) choose weather device", required: false, multiple: false, submitOnChange: true
       if (weather1){input "pollorNot", "bool", title: "Poll weather device before speaking", required: true, defaultValue: true}
       } 
           
           
      }  
    }


       
   def restrictionsPage() {
    dynamicPage(name: "restrictionsPage") {
        section(){paragraph "<br><font size='+1'><i><b>App Restrictions</b></i></font> <br>These restrictions are optional <br>Any restriction you don't want to use, you can just leave blank"}  // 
        section(){
        input "enableSwitch", "capability.switch", required: false, title: "Select a switch to remotely enable or disable this app", multiple: false, submitOnChange: true 
     	if(enableSwitch){ input "enableSwitchMode", "bool", title: "Allow app to run only when this switch is..", required: true, defaultValue: false, submitOnChange: true}
      }
  section(){
            input(name:"modes", type: "mode", title: "Allow actions when current mode is:", multiple: true, required: false)
       	
    	input "fromTime", "time", title: "Allow actions from", required: false
    	input "toTime", "time", title: "Allow actions until", required: false
    	input "days", "enum", title: "Allow actions only on these days of the week", required: false, multiple: true, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
    	input "restrictPresenceSensor", "capability.presenceSensor", title: "Select Presence Sensor (1) to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor){input "restrictPresenceAction", "bool", title: "On = Allow action only when Presence Sensor is 'Present'  <br>Off = Allow action only when Presence Sensor is 'NOT Present'  ", required: true, defaultValue: false}
     	input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select Presence Sensor (2) to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor1){input "restrictPresenceAction1", "bool", title: "On = Allow action only when Presence Sensor is 'Present'  <br>Off = Allow action only when Presence Sensor is 'NOT Present'  ", required: true, defaultValue: false}
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
        section(" ") {label title: "Enter a name for this automation", required: false} // 
        section() {input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}

    }
}
           
           




// defaults
def speakerInputs(){	
	   
    input "messageAction", "enum", title: "Select Message Type", required: false, submitOnChange: true,  options: [ "Voice Message (MusicPlayer)", "Voice Message (SpeechSynth)",  "SMS Message", "PushOver Message", "Join Message", "Play an Mp3 (No variables can be used)"]

 if (messageAction){
     input "msgDelay", "number", title: "Delay between messages (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true
     
 state.msgType = messageAction
    if(state.msgType == "Voice Message (MusicPlayer)"){
         input "speaker", "capability.musicPlayer", title: "All Music Player Device(s)", required: true, multiple: true
        input "multiVolume", "bool", title: "Multiple Volume Slots?", required: false, defaultValue: false, submitOnChange: true
        state.multiVolumeSlots = multiVolume
        
        if(state.multiVolumeSlots == true){
        
        
        input "speakerNo", "enum", title: "How Many Volume Slots Do You Need", submitOnChange: true, defaultValue: "0", options: [ "2","3","4","5"]
        if(speakerNo != null){
            state.speakerNumber = speakerNo
        }
         if(state.speakerNumber == null){
         paragraph "You need to make a selection here!"
         }
        
         if(state.speakerNumber == "2"){
         input "speakerN1", "capability.musicPlayer", title: "Music Player Device Slot 1", required: true, multiple: true
         input "volumeA", "number", title: "Normal Volume (Slot 1)", description: "0-100%", defaultValue: "70",  required: true
         input "speakerN2", "capability.musicPlayer", title: "Music Player Device Slot 2", required: true, multiple: true
         input "volumeB", "number", title: "Normal Volume (Slot 2)", description: "0-100%", defaultValue: "70",  required: true    
          state.voiceVolumeA = volumeA
          state.voiceVolumeB = volumeB   
             
             
             
         }   
        
         if(state.speakerNumber == "3"){
        input "speakerN1", "capability.musicPlayer", title: "Music Player Device Slot 1", required: true, multiple: true 
        input "volumeA", "number", title: "Normal Volume (Slot 1)", description: "0-100%", defaultValue: "70",  required: true     
        input "speakerN2", "capability.musicPlayer", title: "Music Player Device Slot 2", required: true, multiple: true
        input "volumeB", "number", title: "Normal Volume (Slot 2)", description: "0-100%", defaultValue: "70",  required: true     
        input "speakerN3", "capability.musicPlayer", title: "Music Player Device Slot 3", required: true, multiple: true 
        input "volumeC", "number", title: "Normal Volume (Slot 3)", description: "0-100%", defaultValue: "70",  required: true
        state.voiceVolumeA = volumeA
        state.voiceVolumeB = volumeB   
        state.voiceVolumeC = volumeC
             
         }     
             
         if(state.speakerNumber == "4"){
        input "speakerN1", "capability.musicPlayer", title: "Music Player Device Slot 1", required: true, multiple: true 
        input "volumeA", "number", title: "Normal Volume (Slot 1)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN2", "capability.musicPlayer", title: "Music Player Device Slot 2", required: true, multiple: true
        input "volumeB", "number", title: "Normal Volume (Slot 2)", description: "0-100%", defaultValue: "70",  required: true     
        input "speakerN3", "capability.musicPlayer", title: "Music Player Device Slot 3", required: true, multiple: true
        input "volumeC", "number", title: "Normal Volume (Slot 3)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN4", "capability.musicPlayer", title: "Music Player Device Slot 4", required: true, multiple: true
        input "volumeD", "number", title: "Normal Volume (Slot 4)", description: "0-100%", defaultValue: "70",  required: true      
        state.voiceVolumeA = volumeA
        state.voiceVolumeB = volumeB   
        state.voiceVolumeC = volumeC
        state.voiceVolumeD = volumeD     
             
         }     
             
         if(state.speakerNumber == "5"){    
        input "speakerN1", "capability.musicPlayer", title: "Music Player Device Slot 1", required: true, multiple: true
        input "volumeA", "number", title: "Normal Volume (Slot 1)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN2", "capability.musicPlayer", title: "Music Player Device Slot 2", required: true, multiple: true
        input "volumeB", "number", title: "Normal Volume (Slot 2)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN3", "capability.musicPlayer", title: "Music Player Device Slot 3", required: true, multiple: true
        input "volumeC", "number", title: "Normal Volume (Slot 3)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN4", "capability.musicPlayer", title: "Music Player Device Slot 4", required: true, multiple: true
        input "volumeD", "number", title: "Normal Volume (Slot 4)", description: "0-100%", defaultValue: "70",  required: true     
        input "speakerN5", "capability.musicPlayer", title: "Music Player Device Slot 5", required: true, multiple: true
        input "volumeE", "number", title: "Normal Volume (Slot 5)", description: "0-100%", defaultValue: "70",  required: true
        state.voiceVolumeA = volumeA
        state.voiceVolumeB = volumeB   
        state.voiceVolumeC = volumeC
        state.voiceVolumeD = volumeD    
        state.voiceVolumeE = volumeE     
         }
            
        }
        else {
            input "volume1", "number", title: "Normal Speaker volume", description: "0-100%", defaultValue: "70",  required: true
            state.volumeAll = volume1
        }    
            
    }    
    else if(state.msgType == "PushOver Message"){ 
		input "speaker", "capability.speechSynthesis", title: "PushOver Device", required: false, multiple: true
  
	}
    else if(state.msgType == "Voice Message (SpeechSynth)"){ 
		input "speaker", "capability.speechSynthesis", title: "Speech Synthesis Device(s)", required: false, multiple: true
  
	}
    else if(state.msgType == "Missed Message (MusicPlayer)"){ 
		input "missedPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor", required: true, multiple: false
        input "missedMsgDelay", "number", title: "Delay after arriving home before reminder message (minutes)", defaultValue: '0', description: "Minutes", required: true
        input "speaker", "capability.musicPlayer", title: "Choose speaker(s)", required: true, multiple: true
		input "volume1", "number", title: "Speaker volume", description: "0-100%", defaultValue: "70",  required: true  
        input "preMsg", "text", title: "Message to speak before list of missed messages", required: false, defaultValue: "Welcome home! ,,, I know that you've just arrived ,,, but while you were away ,,, you missed the following messages ,,,"
		 input "runTime1", "time", title: "Time message is due", required: false, submitOnChange: true
			if(runTime1){input "missedMessage1", "text", title: "Message to play when presence sensor arrives (if this event missed)",  required: true}  
	}  
     else if(state.msgType == "Missed Message (SpeechSynth)"){ 
		input "speaker", "capability.speechSynthesis", title: "Speech Synthesis Device(s)", required: false, multiple: true
  		input "missedMsgDelay", "number", title: "Delay after arriving home before reminder message (minutes)", defaultValue: '0', description: "Minutes", required: true
        input "speaker", "capability.musicPlayer", title: "Choose speaker(s)", required: true, multiple: true
		input "preMsg", "text", title: "Message to speak before list of missed messages", required: false, defaultValue: "Welcome home! ,,, I know that you've just arrived ,,, but while you were away ,,, you missed the following messages ,,,"
	}   
     
     else if(state.msgType == "Join Message"){ 
		input "speaker", "capability.speechSynthesis", title: "Join API Device", required: false, multiple: true
  
	}
     
	else if(state.msgType == "SMS Message"){
       	input(name: "sms1", type: "phone", title: "Input 1st Phone Number ", required: false)
        input(name: "sms2", type: "phone", title: "Input 2nd Phone Number ", required: false)
        input(name: "sms3", type: "phone", title: "Input 3rd Phone Number ", required: false)
        input(name: "sms4", type: "phone", title: "Input 4th Phone Number ", required: false)
        input(name: "sms5", type: "phone", title: "Input 5th Phone Number ", required: false)
      
     }
	if(state.msgType == "Play an Mp3 (No variables can be used)"){
   		 input "speaker", "capability.musicPlayer", title: "Speaker ", required: true, multiple: true
         input "volume1", "number", title: "volume", description: "0-100%", required: true
         input "duration", "number", title: "Duration of mp3 (seconds)",  required: true
         input "mp3Delay", "number", title: "Seconds after event before playing mp3", description: "Seconds", defaultValue: '0', required: true
        
	     input "pathURI", "text", title: "Enter URI to mp3 files (e.g. mydomain.com/files or localwebserver/files or IPaddress/files)",  required: true 
    	 input "sound", "text", title: "Enter mp3 name here (e.g. alert.mp3)",  required: true 
	}
 }
}




// inputs ***********************************************************************************
def triggerInput() {
   input "trigger", "enum", title: "How to trigger message?",required: false, submitOnChange: true, options: ["Appliance Power Monitor", "Contact", "Contact - Open Too Long", "Lock/Unlock", "Mode Change", "Motion", "Power", "Presence", "Switch", "Time", "Time if Contact Open", "Water", "Weather Alert"]
  /// "Temperature",  "Button",
}

def restrictionInputs(){


        

		section() {
        
    input "restrictions1", "bool", title: "Configure Time or Day or Mode Restriction", required: true, defaultValue: false, submitOnChange: true
    input "restrictions2", "bool", title: "Configure 'Volume by Time' Restriction", required: true, defaultValue: false, submitOnChange: true
    input "restrictions3", "bool", title: "Configure Presence Sensor Restriction", required: true, defaultValue: false, submitOnChange: true
     }

		
    
     if(restrictions1){    
         
           section() {input(name:"modes", type: "mode", title: "Set for specific mode(s)", multiple: true, required: false)}
              
         
     	section("Time/Day") {
            
            
    input "fromTime", "time", title: "Allow messages from", required: false
    input "toTime", "time", title: "Allow messages until", required: false
    input "days", "enum", title: "Select Days of the Week", required: false, multiple: true, options: ["Monday": "Monday", "Tuesday": "Tuesday", "Wednesday": "Wednesday", "Thursday": "Thursday", "Friday": "Friday", "Saturday": "Saturday", "Sunday": "Sunday"]
    		}
    }
    if(restrictions2){
		section("'Quiet Time' - This is to reduce volume during a specified time") {
          input "multiVolumeQuiet", "bool", title: "Multiple Volume Slots?", required: false, defaultValue: false, submitOnChange: true   
        state.multiVolumeSlotsq = multiVolumeQuiet
        
        if(state.multiVolumeSlotsq == true){
        input "fromTime2", "time", title: "Quiet Time Start", required: false
   		input "toTime2", "time", title: "Quiet Time End", required: false  
        
        input "speakerNoq", "enum", title: "How Many Volume Slots Do You Need", submitOnChange: true, defaultValue: "0", options: [ "2","3","4","5"]
        if(speakerNoq != null){
            state.speakerNumberq = speakerNoq
        }
         if(state.speakerNumberq == null){
         paragraph "You need to make a selection here!"
         }
		
         if(state.speakerNumberq == "2"){
         input "speakerN1q", "capability.musicPlayer", title: "Music Player Device Slot 1", required: true, multiple: true
         input "volumeAq", "number", title: "Quiet Volume (Slot 1)", description: "0-100%", defaultValue: "70",  required: true
         input "speakerN2q", "capability.musicPlayer", title: "Music Player Device Slot 2", required: true, multiple: true
         input "volumeBq", "number", title: "Quiet Volume (Slot 2)", description: "0-100%", defaultValue: "70",  required: true    
          state.voiceVolumeAq = volumeAq
          state.voiceVolumeBq = volumeBq   
             
             
             
         }   
        
         if(state.speakerNumberq == "3"){
        input "speakerN1q", "capability.musicPlayer", title: "Music Player Device Slot 1", required: true, multiple: true 
        input "volumeAq", "number", title: "Quiet Volume (Slot 1)", description: "0-100%", defaultValue: "70",  required: true     
        input "speakerN2q", "capability.musicPlayer", title: "Music Player Device Slot 2", required: true, multiple: true
        input "volumeBq", "number", title: "Quiet Volume (Slot 2)", description: "0-100%", defaultValue: "70",  required: true     
        input "speakerN3q", "capability.musicPlayer", title: "Music Player Device Slot 3", required: true, multiple: true 
        input "volumeCq", "number", title: "Quiet Volume (Slot 3)", description: "0-100%", defaultValue: "70",  required: true
        state.voiceVolumeAq = volumeAq
        state.voiceVolumeBq = volumeBq   
        state.voiceVolumeCq = volumeCq
             
         }     
             
         if(state.speakerNumberq == "4"){
        input "speakerN1q", "capability.musicPlayer", title: "Music Player Device Slot 1", required: true, multiple: true 
        input "volumeAq", "number", title: "Quiet Volume (Slot 1)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN2q", "capability.musicPlayer", title: "Music Player Device Slot 2", required: true, multiple: true
        input "volumeBq", "number", title: "Quiet Volume (Slot 2)", description: "0-100%", defaultValue: "70",  required: true     
        input "speakerN3q", "capability.musicPlayer", title: "Music Player Device Slot 3", required: true, multiple: true
        input "volumeCq", "number", title: "Quiet Volume (Slot 3)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN4q", "capability.musicPlayer", title: "Music Player Device Slot 4", required: true, multiple: true
        input "volumeDq", "number", title: "Quiet Volume (Slot 4)", description: "0-100%", defaultValue: "70",  required: true      
        state.voiceVolumeAq = volumeAq
        state.voiceVolumeBq = volumeBq   
        state.voiceVolumeCq = volumeCq
        state.voiceVolumeDq = volumeDq     
             
         }     
             
         if(state.speakerNumberq == "5"){    
        input "speakerN1q", "capability.musicPlayer", title: "Music Player Device Slot 1", required: true, multiple: true
        input "volumeAq", "number", title: "Quiet Volume (Slot 1)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN2q", "capability.musicPlayer", title: "Music Player Device Slot 2", required: true, multiple: true
        input "volumeBq", "number", title: "Quiet Volume (Slot 2)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN3q", "capability.musicPlayer", title: "Music Player Device Slot 3", required: true, multiple: true
        input "volumeCq", "number", title: "Quiet Volume (Slot 3)", description: "0-100%", defaultValue: "70",  required: true
        input "speakerN4q", "capability.musicPlayer", title: "Music Player Device Slot 4", required: true, multiple: true
        input "volumeDq", "number", title: "Quiet Volume (Slot 4)", description: "0-100%", defaultValue: "70",  required: true     
        input "speakerN5q", "capability.musicPlayer", title: "Music Player Device Slot 5", required: true, multiple: true
        input "volumeEq", "number", title: "Quiet Volume (Slot 5)", description: "0-100%", defaultValue: "70",  required: true
        state.voiceVolumeAq = volumeAq
        state.voiceVolumeBq = volumeBq   
        state.voiceVolumeCq = volumeCq
        state.voiceVolumeDq = volumeDq    
        state.voiceVolumeEq = volumeEq     
         }
            
        }
        else {
        input "fromTime2", "time", title: "Quiet Time Start", required: false
    	input "toTime2", "time", title: "Quiet Time End", required: false     
    	input "volume2", "number", title: "Quiet Time All Speaker volume", description: "0-100%", required: false, defaultValue: "50",submitOnChange: true
   		state.volumeAllq = volume2

    	}
    }
}    
    
    
    
    
    
    if(restrictions3){
    section("This is to restrict on 1 or 2 presence sensor(s)") {
    input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    if(restrictPresenceSensor){
   	input "restrictPresenceAction", "bool", title: "   On = Action only when someone is 'Present'  <br>Off = Action only when someone is 'NOT Present'  ", required: true, defaultValue: false    
	}
     input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    if(restrictPresenceSensor1){
   	input "restrictPresenceAction1", "bool", title: "   On = Action only when someone is 'Present'  <br>Off = Action only when someone is 'NOT Present'  ", required: true, defaultValue: false    
	}
    
    }
            }
		
  
}

def actionInputs() {
    if (trigger) {
    state.selection = trigger
    
if(state.selection == 'Button'){
   input "button1", "capability.button", title: "Button", multiple: false, required: false

    
    if(state.msgType == "Voice Message (MusicPlayer)"){
	input "message1", "text", title: "Message to play when button pressed",  required: false, submitOnChange: true
	input "message2", "text", title: "Message to play when button held",  required: false, submitOnChange: true
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
    
    
    }
    if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send when button pressed",  required: false, submitOnChange: true
	 input "message2", "text", title: "Message to send when button held",  required: false, submitOnChange: true
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	 
 
    
    }
   if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send when button pressed",  required: false
    input "priority1", "enum", title: "Message Priority for button pressed",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	input "message2", "text", title: "Message to send when button held",  required: false
    input "priority2", "enum", title: "Message Priority for button held",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
   
     }
    
    if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send when button pressed",  required: false
   	input "message2", "text", title: "Message to send when button held",  required: false
    
     }
  }
	
    

    
    
    
if(state.selection == 'Switch'){
    input "switch1", "capability.switch", title: "Select switch to trigger message/report", required: false, multiple: false 

    
    if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
	input "message1", "text", title: "Message to play when switched on",  required: false
	input "message2", "text", title: "Message to play when switched off",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
    
    
    }

    if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send when switched On",  required: false
	 input "message2", "text", title: "Message to send when switched Off",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	 

    
    }
	if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send when switched on",  required: false
    input "priority1", "enum", title: "Message Priority for switch on",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	input "message2", "text", title: "Message to send when switched off",  required: false
    input "priority2", "enum", title: "Message Priority for switch off",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
    
     }
    
    if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send when switched on",  required: false
 	input "message2", "text", title: "Message to send when switched off",  required: false
    
     }
    
    if(state.msgType == "Play an Mp3 (No variables can be used)"){
    input "mp3Action", "bool", title: "Play mp3 when switch on (ON) or off (OFF)?  ", required: true, defaultValue: true  
	} 

}    
 

else if(state.selection == 'Water'){
	 input "water1", "capability.waterSensor", title: "Select water sensor to trigger message", required: false, multiple: false 
     
    if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
   	input "message1", "text", title: "Message to play when WET",  required: false
	input "message2", "text", title: "Message to play when DRY",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay)", description: "Seconds", required: true
   
	}

    if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send when Wet",  required: false
	 input "message2", "text", title: "Message to send when Dry",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	
   
    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send when wet",  required: false
    input "priority1", "enum", title: "Message Priority for wet", required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	input "message2", "text", title: "Message to send when dry",  required: false
    input "priority2", "enum", title: "Message Priority for dry",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
   
     }
    
    if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send when wet",  required: false
 	input "message2", "text", title: "Message to send when dry",  required: false
   
     }

	if(state.msgType == "Play an Mp3 (No variables can be used)"){
	 input "mp3Action", "bool", title: "Play mp3 when wet (ON) or dry (OFF)? ", required: true, defaultValue: true  
	}
}   
        
        

else if(state.selection == 'Presence'){
	input "presenceSensor1", "capability.presenceSensor", title: "Select presence sensor to trigger message", required: false, multiple: false 
   
    if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
	input "message1", "text", title: "Message to play when sensor arrives",  required: false
	input "message2", "text", title: "Message to play when sensor leaves",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay)", defaultValue: "0", description: "Seconds", required: true
    
	}

     if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send when sensor arrives",  required: false
	 input "message2", "text", title: "Message to send when sensor leaves",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	
     
    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send when sensor arrives",  required: false
    input "priority1", "enum", title: "Message Priority for arrival",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	input "message2", "text", title: "Message to send when sensor leaves",  required: false
    input "priority2", "enum", title: "Message Priority for departure",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
    
     }
    
    if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send when sensor arrives",  required: false
 	input "message2", "text", title: "Message to send when sensor leaves",  required: false
    
     }
    
    if(state.msgType == "Play an Mp3 (No variables can be used)"){
     input "mp3Action", "bool", title: "Play mp3 when arrived (ON) or departed (OFF)? ", required: true, defaultValue: true  
	} 
    
} 

else if(state.selection == 'Contact'){
	input "contactSensor", "capability.contactSensor", title: "Select contact sensor to trigger message", required: false, multiple: true 
   
     
    if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
	input "message1", "text", title: "Message to play when sensor opens",  required: false
	input "message2", "text", title: "Message to play when sensor closes",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay)", description: "Seconds", required: true
    
	    }
    
     if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send when sensor opens",  required: false
	 input "message2", "text", title: "Message to send when sensor closes",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	
    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send when sensor open",  required: false
    input "priority1", "enum", title: "Message Priority for open",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	input "message2", "text", title: "Message to send when sensor closed",  required: false
    input "priority2", "enum", title: "Message Priority for closed",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
    
     }   
    
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send when sensor open",  required: false
 	input "message2", "text", title: "Message to send when sensor closed",  required: false
   
     }
    
    if(state.msgType == "Play an Mp3 (No variables can be used)"){
    input "mp3Action", "bool", title: "Play mp3 when open (ON) or closed (OFF)?  ", required: true, defaultValue: true  
	}   
   
} 

else if(state.selection == 'Power'){
	input "powerSensor", "capability.powerMeter", title: "Select power sensor to trigger message", required: false, multiple: false 
    input(name: "belowThreshold", type: "decimal", title: "Power Threshold (Watts)", required: true, description: "this number of watts")
    input "actionType1", "bool", title: "Select Power Sensor action type: \r\n \r\n On = Alert when power goes ABOVE configured threshold  \r\n Off = Alert when power goes BELOW configured threshold", required: true, defaultValue: false
	input(name: "delay1", type: "number", title: "Only if it stays that way for this number of minutes...", required: true, description: "this number of minutes", defaultValue: '0')
    
    
  if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
    input "message1", "text", title: "Message to play ...",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay - Seconds)", description: "Seconds", required: true, defaultValue: '0'
   
    }
 
  if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send...",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	 
  
    	}
     if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send...",  required: false
    input "priority1", "enum", title: "Message Priority...", required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	
     }
    
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send...",  required: false
 	
     }
 
   
} 


else if(state.selection == 'Appliance Power Monitor'){
	input "powerSensor", "capability.powerMeter", title: "Select power sensor to trigger message", required: true, multiple: false 
    input(name: "belowThreshold", type: "decimal", title: "Below Power Threshold (Watts)", required: true, description: "Trigger below this number of watts", defaultValue: '0')
    input(name: "delay2", type: "number", title: "Only if it stays that way for this number of minutes...", required: true, description: "this number of minutes", defaultValue: '0')
    input(name: "aboveThreshold", type: "number", title: "Activate Power Threshold (Watts)", required: true, description: "Start monitoring above this number of watts", defaultValue: '0')
	
    
    
  if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
    input "message1", "text", title: "Message to play ...",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay - Seconds)", description: "Seconds", required: true, defaultValue: '0'
   
    }

  if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send...",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	
 
    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send...",  required: false
    input "priority1", "enum", title: "Message Priority...", required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	
    
     }
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send...",  required: false
 	
     }
   
} 

else if(state.selection == 'Motion'){
	input "motionSensor",  "capability.motionSensor", title: "Select Motion Sensor", required: false, multiple: false 
    input "motionActionType", "bool", title: "Select Motion Sensor action type: \r\n \r\n On = Alert when motion 'Active'  \r\n Off = Alert when motion 'Inactive'", required: true, defaultValue: false
	
    
  if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
    input "message1", "text", title: "Message to play ...",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay - Seconds)", description: "Seconds", required: true, defaultValue: '0'
   
    }
 
  if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send...",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	

   
    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send...",  required: false
    input "priority1", "enum", title: "Message Priority...",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	
     }   
    
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send...",  required: false
 	input "msgDelay", "number", title: "Delay between messages (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true
     }
}
else if(state.selection == 'Temperature'){
	input "tempSensor",  "capability.temperatureMeasurement" , title: "Select Temperature Sensor", required: false, multiple: false 
    input "temperature1", "number", title: "Set Temperature", required: true
    input "tempActionType", "bool", title: "Select Temperature Sensor action type: \r\n \r\n On = Alert when above set temperature  \r\n Off = Alert when below set temperature", required: true, defaultValue: false
	
    
  if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
    input "message1", "text", title: "Message to play ...",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay - Seconds)", description: "Seconds", required: true, defaultValue: '0'
   
    }
 
  if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send...",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	 

    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send...",  required: false
    input "priority1", "enum", title: "Message Priority...",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	
     }
    
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send...",  required: false
 	
     }
        
}

else if(state.selection == 'Time'){
	input (name: "runTime", title: "Time to run", type: "time",  required: true) 
   
    
   if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
	input "messageTime", "text", title: "Message to play",  required: true
//    input "missedMessageAction", "bool", title: "Let me know if I miss this message while away ", required: true, defaultValue: false, submitOnChange: true 
  
       
   if(missedMessageAction == true){
    input "missedPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor", required: true, multiple: false
    input "missedMessage", "text", title: "Message reminder to play when presence sensor arrives if original message missed",  required: true
    input "missedMsgDelay", "number", title: "Delay after arriving before reminder message", defaultValue: '0', description: "Seconds", required: true
   }
     
        
   		}
 
  if(state.msgType == "SMS Message"){
     input "messageTime", "text", title: "Message to send...",  required: false

    	}
    if(state.msgType == "PushOver Message"){
    input "messageTime", "text", title: "Message to send...",  required: false
    input "priority1", "enum", title: "Message Priority...",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	
     }  
    
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send...",  required: false
 	
     }
   
    
     
}   

else if(state.selection == 'Time if Contact Open'){
	input (name: "runTime", title: "Time to run", type: "time",  required: true) 
    input "contact1", "capability.contactSensor", title: "Select contact sensor to check", required: false, multiple: false 
   
  if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
	input "messageTime", "text", title: "Message to play if contact open",  required: true
    
   		}
  
  if(state.msgType == "SMS Message"){
     input "messageTime", "text", title: "Message to send if contact open",  required: false
     

      
    	}
    if(state.msgType == "PushOver Message"){
    input "messageTime", "text", title: "Message to send if contact open",  required: false
    input "priority1", "enum", title: "Message Priority for switch on",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	
     }  
    
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send...",  required: false
 	
     }
      
}   

else if(state.selection == 'Mode Change'){
	input "newMode1", "mode", title: "Action when changing to this mode",  required: false
    
     
  if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
	input "message1", "text", title: "Message to play",  required: true
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay - Seconds)", description: "Seconds", required: true, defaultValue: '0'
   
       }
  
    if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send...",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	

    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send...",  required: false
    input "priority1", "enum", title: "Message Priority...",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	
     }
    
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send...",  required: false
 	
     }

	} 
    
 
	else if(state.selection == 'Contact - Open Too Long'){
	input "openSensor", "capability.contactSensor", title: "Select contact sensor to trigger message", required: false, multiple: false 
   	input(name: "opendelay1", type: "number", title: "Only if it stays open for this number of minutes...", required: true, description: "this number of minutes", defaultValue: '0')
   
    
  if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
    input "message1", "text", title: "Message to play ...",  required: false
    
  	
    }
  
  if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send...",  required: false
     

      
    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send...",  required: false
    input "priority1", "enum", title: "Message Priority...",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	
     }
    
     if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send...",  required: false
 	
     }
}

        
     else if(state.selection == 'Lock/Unlock'){
	 input "lock1", "capability.lock", title: "Select lock to trigger message", required: false, multiple: false 
     
    if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
   	input "message1", "text", title: "Message to play when LOCKED",  required: false
	input "message2", "text", title: "Message to play when UNLOCKED",  required: false
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay)", description: "Seconds", required: true
   
	}

    if(state.msgType == "SMS Message"){
     input "message1", "text", title: "Message to send when LOCKED",  required: false
	 input "message2", "text", title: "Message to send when UNLOCKED",  required: false
     input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	
   
    	}
    if(state.msgType == "PushOver Message"){
    input "message1", "text", title: "Message to send when LOCKED",  required: false
    input "priority1", "enum", title: "Message Priority for LOCKED", required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	input "message2", "text", title: "Message to send when UnLOCKED",  required: false
    input "priority2", "enum", title: "Message Priority for UULOCKED",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
   
     }
    
    if(state.msgType == "Join Message"){
    input "message1", "text", title: "Message to send when LOCKED",  required: false
 	input "message2", "text", title: "Message to send when UNLOCKED",  required: false
   
     }

	if(state.msgType == "Play an Mp3 (No variables can be used)"){
	 input "mp3Action", "bool", title: "Play mp3 when LOCKED (ON) or UNLOCKED (OFF)? ", required: true, defaultValue: true  
	}
}   

               
     else if(state.selection == 'Weather Alert'){
	 input "weather2", "capability.sensor", title: "Select Weather Device", required: false, multiple: false 
     input "preAlert", "text", title: "Message to play before weather alert (optional)", required: false, multiple: false 
         
    if(state.msgType == "Voice Message (MusicPlayer)" || state.msgType == "Voice Message (SpeechSynth)"){
    input "triggerDelay", "number", title: "Delay after trigger before speaking (Enter 0 for no delay)", description: "Seconds", required: true
   
	}

    if(state.msgType == "SMS Message"){
    input "triggerDelay", "number", title: "Delay after trigger before sending (Enter 0 for no delay)", defaultValue: '0', description: "Seconds", required: true
	
   
    	}
    if(state.msgType == "PushOver Message"){
    input "priority1", "enum", title: "Message Priority", required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
     }
    
    if(state.msgType == "Join Message"){
   
   
     }

	if(state.msgType == "Play an Mp3 (No variables can be used)"){
	 input "mp3Action", "bool", title: "Play mp3 when LOCKED (ON) or UNLOCKED (OFF)? ", required: true, defaultValue: true  
	}
}   
        
}
}





// Handlers



def lightsOnoff(evt){
   LOGDEBUG(" Lights on/off - $evt.device : $evt.value") 
    
}


def switchesOnoff(evt){
    LOGDEBUG(" Switches on/off - $evt.device : $evt.value")  
    
    
    
}


def weatherSummaryHandler(evt){

 state.weatherSummary1 = evt.value
    if(state.weatherSummary1 == null){
        LOGDEBUG("No weather summary receieved from device")
     state.weatherSummary1 = " No weather summary receieved from device"  
    }
    
 LOGDEBUG("Running weatherSummaryHandler.. ")
  LOGDEBUG("state.weatherSummary1 = $state.weatherSummary1")  
}

def weatherNow(evt){
 state.weatherNow = evt.value.toString()
 LOGDEBUG("Running weatherNow.. ")
  LOGDEBUG("state.weatherNow = $state.weatherNow")  
}

def weatherAlert(evt){
    state.weatherAlertRaw = evt.value
    if(state.weatherAlertRaw == null || state.weatherAlertRaw == " "){
     LOGDEBUG("Weather Alert is 'null'")   
    }
    else{
        if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 

    if(state.selection == 'Weather Alert'){
    if(preAlert == null){state.alertPre = " "}
    else {state.alertPre = preAlert}
        
 
 LOGDEBUG("Running weatherAlert.. Previous Alert = $state.oldAlert - Current Alert = $state.weatherAlertRaw")
        
    if(state.oldAlert == state.weatherAlertRaw){
     LOGDEBUG("No new weather alert.. ") 
     state.weatherAlert = state.weatherAlertRaw
    }
    if(state.oldAlert != state.weatherAlertRaw){
        LOGDEBUG("New weather alert received.. ") 
    state.oldAlert = state.weatherAlertRaw
        state.weatherAlert = state.weatherAlertRaw
 
        
        
       if(state.msgType == "Voice Message (MusicPlayer)"){
           state.msg1 = state.alertPre + " " + state.weatherAlert
			state.msgNow = 'oneNow'
    talkSwitch()
   }          

    if(state.msgType == "Voice Message (SpeechSynth)"){
    def msg = state.alertPre + " " + state.weatherAlert    
    LOGDEBUG("weatherNow - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(msg)  
        } 
      

    if(state.msgType == "SMS Message"){
	def msg = state.alertPre + " " + state.weatherAlert
LOGDEBUG("weatherNow - SMS Message - Sending Message: $msg")
  sendMessage(msg)
	}
    
    
if(state.msgType == "PushOver Message"){
	def msg = state.alertPre + " " + state.weatherAlert
LOGDEBUG("weatherNow - PushOver Message - Sending Message: $msg")
 pushOver(1, msg)
}
    
 if(state.msgType == "Join Message"){
	def msg = state.alertPre + " " + state.weatherAlert
LOGDEBUG("weatherNow - Join Message - Sending Message: $msg")
 joinMsg(msg)
	}  
    
 if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")}
}       
        
    }
    
  LOGDEBUG("Current Alert = $state.alertPre $state.weatherAlert")  
}
}
}
}
def weatherForecastHigh(evt){
   state.weatherForecastHigh = evt.value
 LOGDEBUG("Running weatherForecastHigh.. ")
  LOGDEBUG("state.weatherForecastHigh = $state.weatherForecastHigh")  
}
def weatherForecastLow(evt){
    state.weatherForecastLow = evt.value

 LOGDEBUG("Running weatherForecastLow.. ")
  LOGDEBUG("state.weatherForecastLow = $state.weatherForecastLow")  
}
def weatherHumidity(evt){
        state.weatherHumidity = (evt.value.minus('%')) 
  
 LOGDEBUG("Running weatherHumidity.. ")
  LOGDEBUG("state.weatherHumidity = $state.weatherHumidity")  
}
def weatherTemperature(evt){
 
        state.weatherTemperature = evt.value    
      
    
 
 LOGDEBUG("Running weatherTemperature.. ")
  LOGDEBUG("state.weatherTemperature = $state.weatherTemperature")  
}
def weatherFeelsLike(evt){
        state.weatherFeelsLike = evt.value    
   
 LOGDEBUG("Running weatherFeelsLike.. ")
  LOGDEBUG("state.weatherFeelsLike = $state.weatherFeelsLike")  
}
def weatherWindDir(evt){
 state.weatherWindDir = evt.value
 LOGDEBUG("Running weatherWindDir.. ")
  LOGDEBUG("state.weatherWindDir = $state.weatherWindDir")  
}
def weatherWindSpeed(evt){
       state.weatherWindSpeed = evt.value    
  
 LOGDEBUG("Running weatherWindSpeed.. ")
  LOGDEBUG("state.weatherWindSpeed = $state.weatherWindSpeed")  
}
def weatherWindGust(evt){
        state.weatherWindGust = evt.value   
   
 LOGDEBUG("Running weatherWindGust.. ")
  LOGDEBUG("state.weatherWindGust = $state.weatherWindGust")  
}
def weatherVisibility(evt){
    LOGDEBUG("Running weatherVisibility.. ")
        state.weatherVisibility = evt.value   
  
  LOGDEBUG("state.weatherVisibility = $state.weatherVisibility")  
}

def weatherChanceOfRain(evt){
 state.weatherChanceOfRain = (evt.value.minus('%')) 
 LOGDEBUG("Running weatherChanceOfRain.. ")
  LOGDEBUG("state.weatherChanceOfRain = $state.weatherChanceOfRain") 
    if(state.weatherChanceOfRain == "0"){
    state.rainFall = "Rain is not expected today"    
    }
    else{
    state.rainFall = ("There is a " + state.weatherChanceOfRain + " percent chance of rain today")
    } 
    
}





def mp3EventHandler(){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
	checkAllow()
	if(state.allAllow == true && state.mp3Timer == true){
	LOGDEBUG( " Continue... Check delay...")
	def delayBefore = mp3Delay
	runIn(delayBefore, mp3Now)
		}
    }
}



def mp3Now(){

def soundURI = pathURI + "/" + sound 
LOGDEBUG("soundURI = $soundURI " )
state.soundToPlay = soundURI
LOGDEBUG("Playing: $state.soundToPlay " )    
    
checkVolume()
speaker.playTrack(state.soundToPlay) 
 
    
state.mp3Timer = false
    
// log.debug "Message allow: set to $state.timer as I have just played a message"
state.timeDelay = 60 * state.msgDelay
LOGDEBUG("Waiting for $state.timeDelay seconds before resetting timer to allow further messages")
runIn(state.timeDelay, resetMp3Timer)
  
}

def resetMp3Timer() {
state.mp3Timer = true
LOGDEBUG("Timer reset - Messages allowed")

}


// Appliance Power Monitor
def powerApplianceNow(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
state.meterValue = evt.value as double
state.activateThreshold = aboveThreshold
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value as double
LOGDEBUG( "Power reported $state.meterValue watts")
if(state.meterValue > state.activateThreshold){
state.activate = true
LOGDEBUG( "Activate threshold reached or exceeded setting state.activate to: $state.activate")

}



 if(state.appgo == true && state.activate == true){
LOGDEBUG( "powerApplianceNow -  Power is: $state.meterValue")
    state.belowValue = belowThreshold as double
    if (state.meterValue < state.belowValue) {
   def mydelay = 60 * delay2 
   LOGDEBUG( "Checking again after delay: $delay2 minutes... Power is: $state.meterValue")
       runIn(mydelay, checkApplianceAgain1, [overwrite: false])     
       
      }
}

	 if(state.activate == false){
     LOGDEBUG( "Not reached threshold yet to activate monitoring")
     
     }
     
     
 if(state.appgo == false){
    LOGDEBUG("App disabled by $enableswitch being off")

}
}
}


def checkApplianceAgain1() {
   
     if (state.meterValue < state.belowValue) {
      LOGDEBUG( " checkApplianceAgain1 - Checking again now... Power is: $state.meterValue")
    
      speakNow()
      state.activate = false  
			}
     else  if (state.meterValue > state.belowValue) {
     LOGDEBUG( "checkApplianceAgain1 -  Power is: $state.meterValue so cannot run yet...")
	}	
}	






// Missed message presence handler
def missedPresenceCheckNow(evt){
	state.missedPresencestatus1 = evt.value
LOGDEBUG("state.missedPresencestatus1 = $evt.value")

	def	myMissedDelay = missedMsgDelay

	if(state.missedPresencestatus1 == "present" && state.missedEvent == true){
   
LOGDEBUG("Telling you about missed events in $missedMsgDelay seconds (If there are any, and I haven't already told you about them)")
    
    runIn(myMissedDelay, speakMissedNow, [overwrite: false])
    
    }
if(state.missedPresencestatus1 == "present" && state.missedEvent == false){
LOGDEBUG("No missed messages yet")
	}
}


// check if any timed messages have been missed - Disabled for a while :)
def checkTimeMissedNow(){
LOGDEBUG("Checking missed events now...")
	if(state.missedPresencestatus1 == 'not present'){
    state.missedEvent = true
    state.alreadyDone = false
LOGDEBUG("Missed a time event")
	}

	if(state.missedPresencestatus1 == 'present'){
	state.missedEvent = false
LOGDEBUG("No missed timed events")
	}
}

// speak any missed message
def speakMissedNow(){

LOGDEBUG("SpeakMissedNow called...")
	state.myMsg = missedMessage
	    
  if (state.alreadyDone == false){  
LOGDEBUG("Message = $state.myMsg")
      speaker.playTextAndRestore(state.myMsg)

	state.alreadyDone = true
	}

  if (state.alreadyDone == true){ 
LOGDEBUG("Already told you, so won't tell you again!")
	}
}







// Button
def buttonEvent(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
state.buttonStatus1 = evt.value
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
LOGDEBUG("Button is $state.buttonStatus1 state.presenceRestriction = $state.presenceRestriction")
state.msg1 = message1
state.msg2 = message2
def mydelay = triggerDelay



if(state.msgType == "Voice Message (MusicPlayer)"){
LOGDEBUG("Button - Voice Message")

	if(state.buttonStatus1 == 'pushed'){
state.msgNow = 'oneNow'

    }

	else if (state.buttonStatus1 == 'held'){
state.msgNow = 'twoNow'
	}

LOGDEBUG( "$button1 is $state.buttonStatus1")

checkVolume()
LOGDEBUG("Speaker(s) in use: - waiting $mydelay seconds before continuing..."  )

runIn(mydelay, talkSwitch)
}

if(state.msgType == "SMS Message"){
LOGDEBUG("Button - SMS Message")

	if(state.buttonStatus1 == 'pushed' && state.msg1 != null){
def msg = message1
LOGDEBUG("Button - SMS/Push Message - Sending Message: $msg")
  sendMessage(msg)
    }
    
    
    if(state.buttonStatus1 == 'held' && state.msg2 != null){
def msg = message2
LOGDEBUG("Button - SMS Message - Sending Message: $msg")
  sendMessage(msg)
    }

}
    
if(state.msgType == "PushOver Message"){
LOGDEBUG("Button - PushOver Message")

	if(state.buttonStatus1 == 'pushed' && state.msg1 != null){
def msg = message1
LOGDEBUG("Button - PushOver Message - Sending Message: $msg")
pushOver(1, msg)
    }
    
    
    if(state.buttonStatus1 == 'held' && state.msg2 != null){
def msg = message2
LOGDEBUG("Button - PushOver Message - Sending Message: $msg")
pushOver(2, msg)
    }

}    
                               }
}


// Temperature
def tempTalkNow(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
state.tempStatus2 = evt.value
state.tempStatus1 = state.tempStatus2.toDouble()
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
    LOGDEBUG("$state.nameOfDevice - is reporting temperature as: $state.tempStatus1")
    
state.msg1 = message1
state.msgNow = 'oneNow'
def myTemp = temperature1.toDouble()
LOGDEBUG("myTemp = $myTemp")
    
 if(state.tempStatus1 > myTemp){   
 LOGDEBUG("Event: state.tempStatus1 > myTemp")
 }
  if(state.tempStatus1 < myTemp){   
 LOGDEBUG("Event: state.tempStatus1 < myTemp")
 }   
    
if(tempActionType == true && state.tempStatus1 > myTemp){
LOGDEBUG("Action = true - state.tempStatus1 > myTemp")
 if(state.msgType == "Voice Message (MusicPlayer)"){
    talkSwitch()
   }          
 else if(state.msgType == "Voice Message (SpeechSynth)"){
    def msg = message1    
    LOGDEBUG("TempTalkNow - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(msg)  
        } 
      
  else if(state.msgType == "SMS Message"){
	def msg = message1
LOGDEBUG("TempTalkNow - SMS Message - Sending Message: $msg")
  sendMessage(msg)
	}
    
  else if(state.msgType == "PushOver Message"){
	def msg = message1
LOGDEBUG("TempTalkNow - PushOver Message - Sending Message: $msg")
 pushOver(1, msg)
	}
    
      else if(state.msgType == "Join Message"){
	def msg = message1
LOGDEBUG("TempTalkNow - Join Message - Sending Message: $msg")
 joinMsg(msg)
	}
    
 if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")}
} 
    
}    
    
if(tempActionType == false && state.tempStatus1 < myTemp){
    LOGDEBUG("Action = false - state.tempStatus1 < myTemp")
 if(state.msgType == "Voice Message (MusicPlayer)"){
    talkSwitch()
   }          
  
   if(state.msgType == "Voice Message (SpeechSynth)"){
    def msg = message1    
    LOGDEBUG("TempTalkNow - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(msg)  
        } 
    
  if(state.msgType == "SMS Message"){
	def msg = message1
      
LOGDEBUG("TempTalkNow - SMS Message - Sending Message: $msg")
  sendMessage(msg)
	}
    
 if(state.msgType == "PushOver Message"){
	def msg = message1
LOGDEBUG("TempTalkNow - PushOver Message - Sending Message: $msg")
 pushOver(1, msg)
	}
    
    
   if(state.msgType == "Join Message"){
	def msg = message1
LOGDEBUG("TempTalkNow - Join Message - Sending Message: $msg")
 joinMsg(msg)
	}
    
 if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")}
} 
    
    
    
  }
 }
}



def stopRepeat(){
    if(tempActionType == true) {}  
 state.repeatStop = true   
    
    
}


// Motion

def motionTalkNow(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
state.motionStatus1 = evt.value
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
state.msg1 = message1
state.msgNow = 'oneNow'

if(motionActionType == true && state.motionStatus1 == 'active'){
 LOGDEBUG( "MotionTalkNow... Sensor Active - Configured to alert on active motion sensor")
    
    if(state.msgType == "Voice Message (MusicPlayer)"){
    talkSwitch()
   }          

    if(state.msgType == "Voice Message (SpeechSynth)"){
    def msg = message1    
    LOGDEBUG("MotionTalkNow - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(msg)  
        } 
      

    if(state.msgType == "SMS Message"){
	def msg = message1
LOGDEBUG("MotionTalkNow - SMS Message - Sending Message: $msg")
  sendMessage(msg)
	}
    
    
if(state.msgType == "PushOver Message"){
	def msg = message1
LOGDEBUG("MotionTalkNow - PushOver Message - Sending Message: $msg")
 pushOver(1, msg)
}
    
 if(state.msgType == "Join Message"){
	def msg = message1
LOGDEBUG("MotionTalkNow - Join Message - Sending Message: $msg")
 joinMsg(msg)
	}  
    
 if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")}
}   
}
    
if(motionActionType == false && state.motionStatus1 == 'inactive'){
 LOGDEBUG( "MotionTalkNow... Sensor Inactive - Configured to alert on inactive motion sensor")
    
    if(state.msgType == "Voice Message (MusicPlayer)"){
    talkSwitch()
   }          
 if(state.msgType == "Voice Message (SpeechSynth)"){
    def msg = message1    
    LOGDEBUG("MotionTalkNow - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(msg)  
        }      
 if(state.msgType == "SMS Message"){
	def msg = message1
LOGDEBUG("MotionTalkNow - SMS Message - Sending Message: $msg")
  sendMessage(msg)
	}
    
if(state.msgType == "PushOver Message"){
	def msg = message1
LOGDEBUG("MotionTalkNow - PushOver Message - Sending Message: $msg")
 pushOver(1, msg)
}

if(state.msgType == "Join Message"){
	def msg = message1
LOGDEBUG("MotionTalkNow - Join Message - Sending Message: $msg")
 joinMsg(msg)
	}
    
 if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")}
}    
}
}
}


// Open Too Long
def tooLongOpen(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
LOGDEBUG("tooLongOpen - Contact is $evt.value")
state.openContact = evt.value
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
    
    LOGDEBUG(" state.openContact = $state.openContact")
    
if (state.openContact == 'open' && state.appgo == true && state.presenceRestriction == true && state.presenceRestriction1 == true){
LOGDEBUG("tooLongOpen - Contact is open")
openContactTimer1()
}

else if (state.openContact == 'closed'){
LOGDEBUG("tooLongOpen - Contact is closed")
}
 else if(state.appgo == false){
    LOGDEBUG("App disabled by $enableswitch being off")
}

}
}

def openContactTimer1(){

LOGDEBUG( "tooLongOpen - openContactTimer1 -  Contact is: $state.openContact")
   def mydelayOpen = 60 * opendelay1
   LOGDEBUG( "openContactTimer1 - Checking again after delay: $opendelay1 minute(s)... ")
       runIn(mydelayOpen, openContactSpeak)     
      }
      
      
def openContactSpeak(){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
LOGDEBUG( "openContactSpeak -  Contact is: $state.openContact")
state.msg1 = message1
state.msgNow = 'oneNow'


if (state.openContact == 'open'){
     LOGDEBUG( "openContactSpeak -  Still open...")
    
    if(state.msgType == "Voice Message (MusicPlayer)"){
    talkSwitch()
   }          
 if(state.msgType == "Voice Message (SpeechSynth)"){
    def msg = message1    
    LOGDEBUG("OpenContact Speak - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(msg)  
        }
      
if(state.msgType == "SMS Message"){
	def msg = message1
LOGDEBUG("tooLongOpen - SMS Message - Sending Message: $msg")
  sendMessage(msg)
	}
   
if(state.msgType == "PushOver Message"){
	def msg = message1
LOGDEBUG("tooLongOpen - PushOver Message - Sending Message: $msg")
 pushOver(1, msg)
}
    
if(state.msgType == "Join Message"){
	def msg = message1
LOGDEBUG("tooLongOpen - Join Message - Sending Message: $msg")
 joinMsg(msg)
	}
    
 if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")}
}      
  }
 }
}
// Mode Change

def modeHandler(evt){
 state.modeNow = evt.value
state.actionEvent = evt.value
//    LOGDEBUG("state.actionEvent = $evt.value")   
    
    
}


def modeChangeHandler(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused"     
state.modeNow = evt.value
state.actionEvent = evt.value
    LOGDEBUG("state.actionEvent = $evt.value")
LOGDEBUG("state.modeNow = $state.modeNow")
 state.msg1 = message1
 LOGDEBUG("state.msg1 = $state.msg1")
	
 
 state.msgNow = 'oneNow'
 
	if (evt.isStateChange){
     LOGDEBUG("State Change")   

def modeRequired = newMode1
        
      
        LOGDEBUG("modeRequired = ${modeRequired} - current mode = ${state.modeNow}")  
      if(state.modeNow != modeRequired){  
        LOGDEBUG("not an exact match")
          
      }
	 if(state.modeNow == modeRequired){
    
   	LOGDEBUG("Mode is now $modeRequired")
    
 	if(state.msgType == "Voice Message (MusicPlayer)"){    
def mydelay = triggerDelay
checkVolume()
LOGDEBUG("Speaker(s) in use: $speaker - waiting $mydelay seconds before continuing..."  )
runIn(mydelay, talkSwitch)
}
 if(state.msgType == "Voice Message (SpeechSynth)"){
    def msg = message1    
    LOGDEBUG("Mode - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(msg)  
        }	

if(state.msgType == "SMS Message"){
def msg = message1
LOGDEBUG("Mode Change - SMS Message - Sending Message: $msg")
  sendMessage(msg)
	} 

 if(state.msgType == "PushOver Message"){
	def msg = message1
LOGDEBUG("Mode Change - PushOver Message - Sending Message: $msg")
 pushOver(1, msg)

   }
        
if(state.msgType == "Join Message"){
	def msg = message1
LOGDEBUG("Mode Change - Join Message - Sending Message: $msg")
 joinMsg(msg)
	}
        
 if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")}
}        
}
}
 }
}


// Define restrictPresenceSensor actions
def restrictPresenceSensorHandler(evt){
state.presencestatus1 = evt.value
LOGDEBUG("state.presencestatus1 = $evt.value")
checkPresence()
checkPresence1()

}


def checkPresence(){
LOGDEBUG("running checkPresence - restrictPresenceSensor = $restrictPresenceSensor")

if(restrictPresenceSensor){
LOGDEBUG("Presence = $state.presencestatus1")
def actionPresenceRestrict = restrictPresenceAction


if (state.presencestatus1 == "present" && actionPresenceRestrict == true){
LOGDEBUG("Presence ok")
state.presenceRestriction = true
}
else if (state.presencestatus1 == "not present" && actionPresenceRestrict == true){
LOGDEBUG("Presence not ok")
state.presenceRestriction = false
}

if (state.presencestatus1 == "not present" && actionPresenceRestrict == false){
LOGDEBUG("Presence ok")
state.presenceRestriction = true
}
else if (state.presencestatus1 == "present" && actionPresenceRestrict == false){
LOGDEBUG("Presence not ok")
state.presenceRestriction = false
}
}
else if(!restrictPresenceSensor){
state.presenceRestriction = true
LOGDEBUG("Presence sensor restriction not used")
}
}


def restrictPresence1SensorHandler(evt){
state.presencestatus2 = evt.value
LOGDEBUG("state.presencestatus2 = $evt.value")
checkPresence1()


}


def checkPresence1(){
LOGDEBUG("running checkPresence1 - restrictPresenceSensor1 = $restrictPresenceSensor1")

if(restrictPresenceSensor1){
LOGDEBUG("Presence = $state.presencestatus1")
def actionPresenceRestrict1 = restrictPresenceAction1


if (state.presencestatus2 == "present" && actionPresenceRestrict1 == true){
LOGDEBUG("Presence 2 ok")
state.presenceRestriction1 = true
}
else if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == true){
LOGDEBUG("Presence 2 not ok")
state.presenceRestriction1 = false
}

if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == false){
LOGDEBUG("Presence 2 ok")
state.presenceRestriction1 = true
}
else if (state.presencestatus2 == "present" && actionPresenceRestrict1 == false){
LOGDEBUG("Presence 2 not ok")
state.presenceRestriction1 = false
}
}
else if(!restrictPresenceSensor1){
state.presenceRestriction1 = true
LOGDEBUG("Presence sensor 2 restriction not used")
}
}





// define debug action
def logCheck(){
state.checkLog = debugMode
if(state.checkLog == true){
log.info "All Logging Enabled"
}
else if(state.checkLog == false){
log.info "Further Logging Disabled"
}

}
def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (Childapp Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}



// Enable Switch

def switchRunCheck(){
if(enableSwitch){
def switchstate = enableSwitch.currentValue('switch')
LOGDEBUG("Enable switch is used. Switch is: $enableSwitch ")

if(switchstate == 'on'){
state.appgo = true
LOGDEBUG("$enableSwitch - Switch State = $switchstate - Appgo = $state.appgo")
}
if(switchstate == 'off'){
state.appgo = false
LOGDEBUG("$enableSwitch - Switch State = $switchstate - Appgo = $state.appgo")
}
if(switchstate == null){
state.appgo = true
LOGDEBUG("$enableSwitch - Switch State = $switchstate - Appgo = $state.appgo")
}  
}


if(!enableSwitch){
LOGDEBUG("Enable switch is NOT used. Switch is: $enableSwitch ")
state.appgo = true
LOGDEBUG("AppGo = $state.appgo")
}
}

def switchEnable(evt){
state.enableInput = evt.value
    
    if(enableSwitchMode == true && state.enableInput == 'off'){
state.appgo = false
        LOGDEBUG("Cannot change mode - App disabled by switch")  
    }
     if(enableSwitchMode == true && state.enableInput == 'on'){
state.appgo = true
        LOGDEBUG("Switch restriction is OK.. Continue...") 
    }    
     if(enableSwitchMode == false && state.enableInput == 'off'){
state.appgo = true
        LOGDEBUG("Switch restriction is OK.. Continue...")  
    }
     if(enableSwitchMode == false && state.enableInput == 'on'){
state.appgo = false
        LOGDEBUG("Cannot change mode - App disabled by switch")  
    }    
      
    
LOGDEBUG("Allow by switch is $state.appgo")
        
    
state.sEnable = evt.value
LOGDEBUG("$enableSwitch = $state.sEnable")
if(state.sEnable == 'on'){
state.appgo = true
LOGDEBUG("AppGo = $state.appgo")
}
else if(state.sEnable == 'off'){
state.appgo = false
LOGDEBUG("AppGo = $state.appgo")
}
}


// Time
def timeTalkNow(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused"    
checkAllow()

LOGDEBUG("state.appgo = $state.appgo - state.dayCheck = $state.dayCheck - state.volume = $state.volume - runTime = $runTime")
if(state.allAllow == true ){
LOGDEBUG("Time trigger -  Activating now! ")
    
    if(state.msgType == "Play an Mp3 (No variables can be used)"){
    LOGDEBUG("All OK! - Playing Mp3")
    mp3EventHandler()
    }
    else{
        
LOGDEBUG("Calling.. CompileMsg")
def msg = messageTime   
if(state.msgType != "Play an Mp3 (No variables can be used)"){compileMsg(msg)}
    
if(state.msgType == "Voice Message (MusicPlayer)"){ 

checkVolume()
LOGDEBUG( "Speaker(s) in use: $speaker  - Message to play: $msg"  )
LOGDEBUG("All OK! - Playing message: '$state.fullPhrase'")

    speaker.playTextAndRestore(state.fullPhrase)  

}
 if(state.msgType == "Voice Message (SpeechSynth)"){
      
    LOGDEBUG("Time - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(state.fullPhrase)  
        }
if(state.msgType == "SMS Message"){

LOGDEBUG("Time - SMS Message - Sending Message: $msg")
  sendMessage(state.fullPhrase)
	} 

   else if(state.msgType == "PushOver Message"){

LOGDEBUG("Time - PushOver - PushOver Message - Sending Message: $msg")
 pushOver(1, state.fullPhrase)    
   }

      else if(state.msgType == "Join Message"){

LOGDEBUG("Time - Join Message - Sending Message: $msg")
 joinMsg(state.fullPhrase)
	}
        
 if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")}
}         

    }
}
else if(state.appgo == false){
LOGDEBUG( "$enableSwitch is off so cannot continue")
}
else if(state.dayCheck == false){
LOGDEBUG( "Cannot continue - Daycheck failed")
}
else if(state.presenceRestriction ==  false){
LOGDEBUG( "Cannot continue - Presence failed")
}
}
}


// Time if Contact Open
def contact1Handler (evt) {
state.contact1SW = evt.value 
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
LOGDEBUG( "$contact1 = $evt.value")
						 }



def timeTalkNow1(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused"     
checkAllow()

LOGDEBUG("state.appgo = $state.appgo - state.dayCheck = $state.dayCheck - state.volume = $state.volume - runTime = $runTime")
if(state.allAllow == true  && state.contact1SW == 'open' ){
LOGDEBUG("Time trigger -  Activating now! ")
LOGDEBUG("Calling.. CompileMsg")
  def msg = messageTime
  if(state.msgType != "Play an Mp3 (No variables can be used)"){compileMsg(msg)}
if(state.msgType == "Voice Message (MusicPlayer)"){ 

checkVolume()
LOGDEBUG( "Speaker(s) in use: $speaker - Message to play: $msg"  )

LOGDEBUG("All OK! - Playing message: '$state.fullPhrase'")
   
    speaker.playTextAndRestore(state.fullPhrase)


}
 if(state.msgType == "Voice Message (SpeechSynth)"){
     LOGDEBUG("Time - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(state.fullPhrase)  
        }
if(state.msgType == "SMS Message"){

LOGDEBUG("Time - SMS Message - Sending Message: $msg")
  sendMessage(state.fullPhrase)
	} 
    
else if(state.msgType == "PushOver Message"){
	
LOGDEBUG("Time - PushOver - PushOver Message - Sending Message: $msg")
 pushOver(1, state.fullPhrase)      

}
    
else if(state.msgType == "Join Message"){
LOGDEBUG("Time - Join Message - Sending Message: $msg")
 joinMsg(state.fullPhrase)
	}
    
   else if(state.msgType == "Play an Mp3 (No variables can be used)"){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
       else{ LOGDEBUG("Time - Mp3 - Playing: $state.soundToPlay")  }
	}        
}   


else if(state.contact1SW != 'open'){
LOGDEBUG( "Cannot continue - $contact1 is Closed")
}
}
}





// Switch
def switchTalkNow(evt){
    log.warn "talkswitch called"
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused"     
state.talkswitch1 = evt.value
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
state.msg1 = message1
state.msg2 = message2
    
def mydelay = triggerDelay

if(state.msgType == "Voice Message (MusicPlayer)"){
LOGDEBUG("Switch - Voice Message - $state.nameOfDevice")

	if(state.talkswitch1 == 'on'){
state.msgNow = 'oneNow'
    }

	else if (state.talkswitch1 == 'off'){
state.msgNow = 'twoNow'
	}

LOGDEBUG( "$switch1 is $state.talkswitch1")

checkVolume()
LOGDEBUG("Speaker(s) in use: $speaker - waiting $mydelay seconds before continuing..."  )

runIn(mydelay, talkSwitch)
}
 if(state.msgType == "Voice Message (SpeechSynth)"){
     if(state.talkswitch1 == 'on' && state.msg1 != null){
   def msg = message1     
   compileMsg(msg)    
    LOGDEBUG("Switch - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(state.fullPhrase)  
        }
     if(state.talkswitch1 == 'off' && state.msg2 != null){
     def msg = message2     
   compileMsg(msg)    
    LOGDEBUG("Switch - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(state.fullPhrase)  
         
         
     }  
 }   
if(state.msgType == "SMS Message"){
LOGDEBUG("Switch - SMS Message")
	if(state.talkswitch1 == 'on' && state.msg1 != null){
   def msg = message1     
   compileMsg(msg)
LOGDEBUG("Switch - SMS/Push Message - Sending Message: $state.fullPhrase - $state.nameOfDevice")
  sendMessage(state.fullPhrase)
    }
    
    
    if(state.talkswitch1 == 'off' && state.msg2 != null){
def msg = message2
    compileMsg(msg)
LOGDEBUG("Switch - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)
    }

}

         
    
if(state.msgType == "PushOver Message"){
LOGDEBUG("Switch - PushOver Message")
	if(state.talkswitch1 == 'on' && state.msg1 != null){
        def msg = message1
        compileMsg(msg)
LOGDEBUG("Switch - PushOver Message - Sending Message: $state.fullPhrase - $state.nameOfDevice")
 pushOver(1, state.fullPhrase)
    }
    
    
    if(state.talkswitch1 == 'off' && state.msg2 != null){
		def msg = message2
        compileMsg(msg)
LOGDEBUG("Switch - PushOver Message - Sending Message: $state.fullPhrase")
 pushOver(2, state.fullPhrase)
    }
}   
    
if(state.msgType == "Join Message"){
LOGDEBUG("Switch - Join Message")
	if(state.talkswitch1 == 'on' && state.msg1 != null){
		def msg = message1
        compileMsg(msg)
LOGDEBUG("Switch - Join Message - Sending Message: $msg - $state.nameOfDevice")
 joinMsg(state.fullPhrase)
    }
    
    
    if(state.talkswitch1 == 'off' && state.msg2 != null){
		def msg = message2
        compileMsg(msg)
LOGDEBUG("Switch - Join Message - Sending Message: $msg - $state.nameOfDevice")
 joinMsg(state.fullPhrase)
    }    

}
    
     if(state.msgType == "Play an Mp3 (No variables can be used)"){
         if(state.talkswitch1 == 'on' && state.mp3Switch == true){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         } 
         
        if(state.talkswitch1 == 'off' && state.mp3Switch == false){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         }   
}        
}   
}




// Contact
def contactTalkNow(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
state.talkcontact = evt.value
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
state.msg1 = message1
state.msg2 = message2

if(state.msgType == "Voice Message (MusicPlayer)"){
	if(state.talkcontact == 'open'){
state.msgNow = 'oneNow'
}
	else if (state.talkcontact == 'closed'){
state.msgNow = 'twoNow'
}

LOGDEBUG("$contactSensor is $state.talkcontact")
def mydelay = triggerDelay
checkVolume()
LOGDEBUG( "Speaker(s) in use: $speaker - waiting $mydelay seconds before continuing..."  )

runIn(mydelay, talkSwitch)
}

if(state.msgType == "SMS Message"){
	if(state.talkcontact == 'open' && state.msg1 != null){
def msg = message1
compileMsg(msg)        
LOGDEBUG("Contact - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)

}

	else if (state.talkcontact == 'closed' && state.msg2 != null){
def msg = message2
compileMsg(msg)

LOGDEBUG("Contact - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)

}


	}
 if(state.msgType == "Voice Message (SpeechSynth)"){
     if(state.talkcontact == 'open' && state.msg1 != null){
   def msg = message1     
   compileMsg(msg)    
    LOGDEBUG("Contact - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(state.fullPhrase)  
        }    
    else if (state.talkcontact == 'closed' && state.msg2 != null){
     def msg = message2     
   compileMsg(msg)    
    LOGDEBUG("Contact - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(state.fullPhrase)  
        }
     
    
 }     
         
    
if(state.msgType == "PushOver Message"){
	if(state.talkcontact == 'open' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Contact - PushOver Message - Sending Message: $state.fullPhrase")
  pushOver(1, state.fullPhrase)

}

	else if (state.talkcontact == 'closed' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Contact - Pushover Message - Sending Message: $state.fullPhrase")
  pushOver(2, state.fullPhrase)

}

	}    
if(state.msgType == "Join Message"){
	if(state.talkcontact == 'open' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Contact - Join Message - Sending Message: $state.fullPhrase")
 joinMsg(state.fullPhrase)

}

	else if (state.talkcontact == 'closed' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Contact - Join Message - Sending Message: $state.fullPhrase")
  joinMsg(state.fullPhrase)

}

	} 
    
          if(state.msgType == "Play an Mp3 (No variables can be used)"){
         if(state.talkcontact == 'open' && state.mp3Switch == true){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         } 
         
        if(state.talkswitch1 == 'off' && state.mp3Switch == false){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         }   
}     
}    
}



// Lock/Unlock
def lockTalkNow(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
state.talklock = evt.value
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
state.msg1 = message1
state.msg2 = message2


if(state.msgType == "Voice Message (MusicPlayer)"){
        
	if(state.talklock == 'locked'){
state.msgNow = 'oneNow'
	}
	else if (state.talklock == 'unlocked'){
state.msgNow = 'twoNow'
	}

LOGDEBUG( "$lock1 is $state.talklock")
def mydelay = triggerDelay
checkVolume()
LOGDEBUG( "Speaker(s) in use: $speaker - waiting $mydelay seconds before continuing..."  )
runIn(mydelay, talkSwitch)
	}
  if(state.msgType == "Voice Message (SpeechSynth)"){
     if(state.talklock == 'locked' && state.msg1 != null){
   def msg = message1     
   compileMsg(msg)    
   LOGDEBUG("Lock - Speech Synth Message - Sending Message: $msg") 
   speechSynthNow(state.fullPhrase) 
        }
     if(state.talklock == 'unlocked' && state.msg2 != null){
     def msg = message2     
   compileMsg(msg)    
    LOGDEBUG("Lock - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(state.fullPhrase)  
         
         
     }  
 }      
    
    
    
    
if(state.msgType == "SMS Message"){
	if(state.talklock == 'locked' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Lock - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)

}

	else if(state.talklock == 'unlocked' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Lock - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)

}    
}    
if(state.msgType == "PushOver Message"){
	if(state.talklock == 'locked' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Lock - PushOver Message - Sending Message: $state.fullPhrase")
  pushOver(1, state.fullPhrase)

}

	else if(state.talklock == 'unlocked' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Lock - PushOver Message - Sending Message: $state.fullPhrase")
 pushOver(2, state.fullPhrase)

}    
}    
     if(state.msgType == "Play an Mp3 (No variables can be used)"){
         if(state.talklock == 'locked' && state.mp3Switch == true){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         } 
         
        if(state.talklock == 'unlocked' && state.mp3Switch == false){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         }   
} 
    
    
    if(state.msgType == "Join Message"){
	if(state.talklock == 'locked' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Locked - Join Message - Sending Message: $state.fullPhrase")
 joinMsg(state.fullPhrase)

}

	else if (state.talklock == 'unlocked' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Unlocked - Join Message - Sending Message: $state.fullPhrase")
  joinMsg(state.fullPhrase)

}

	} 
}
}



// Water
def waterTalkNow(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
state.talkwater = evt.value
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
state.msg1 = message1
state.msg2 = message2


if(state.msgType == "Voice Message (MusicPlayer)"){
        
	if(state.talkwater == 'wet'){
state.msgNow = 'oneNow'
	}
	else if (state.talkwater == 'dry'){
state.msgNow = 'twoNow'
	}

LOGDEBUG( "$water1 is $state.talkwater")
def mydelay = triggerDelay
checkVolume()
LOGDEBUG( "Speaker(s) in use: $speaker - waiting $mydelay seconds before continuing..."  )
runIn(mydelay, talkSwitch)
	}
  if(state.msgType == "Voice Message (SpeechSynth)"){
     if(state.talkwater == 'wet' && state.msg1 != null){
   def msg = message1     
   compileMsg(msg)    
   LOGDEBUG("Water - Speech Synth Message - Sending Message: $msg") 
   speechSynthNow(state.fullPhrase) 
        }
     if(state.talkwater == 'dry' && state.msg2 != null){
     def msg = message2     
   compileMsg(msg)    
    LOGDEBUG("Water - Speech Synth Message - Sending Message: $msg")   
    speechSynthNow(state.fullPhrase)  
         
         
     }  
 }      
    
    
    
    
if(state.msgType == "SMS Message"){
	if(state.talkwater == 'wet' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Water - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)

}

	else if(state.talkwater == 'dry' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Water - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)

}    
}    
if(state.msgType == "PushOver Message"){
	if(state.talkwater == 'wet' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Water - PushOver Message - Sending Message: $state.fullPhrase")
  pushOver(1, state.fullPhrase)

}

	else if(state.talkwater == 'dry' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Water - SMS Message - Sending Message: $state.fullPhrase")
 pushOver(2, state.fullPhrase)

}    
}
    
     if(state.msgType == "Play an Mp3 (No variables can be used)"){
         if(state.talkwater == 'wet' && state.mp3Switch == true){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         } 
         
        if(state.talkwater == 'dry' && state.mp3Switch == false){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         }   
}
}
}

// Presence
def presenceTalkNow(evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused"     
state.talkpresence = evt.value
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value
state.msg1 = message1
state.msg2 = message2

if(state.msgType == "Voice Message (MusicPlayer)"){

	if(state.talkpresence == 'present'){
state.msgNow = 'oneNow'
	}

	else if (state.talkpresence == 'not present'){
state.msgNow = 'twoNow'
	}

LOGDEBUG( "$presenceSensor1 is $state.talkpresence")
def mydelay = triggerDelay
checkVolume()
LOGDEBUG("Speaker(s) in use: $speaker - waiting $mydelay seconds before continuing..."  )
runIn(mydelay, talkSwitch)
}

  if(state.msgType == "Voice Message (SpeechSynth)"){
    if(state.talkpresence == 'present' && state.msg1 != null){
		def msg = message1     
		compileMsg(msg)    
		LOGDEBUG("Presence - Speech Synth Message - Sending Message: $msg")  
        speechSynthNow(state.fullPhrase)   
    }
	if(state.talkpresence == 'not present' && state.msg2 != null){
		def msg = message2     
		compileMsg(msg)    
		LOGDEBUG("Presence - Speech Synth Message - Sending Message: $msg")   
		speechSynthNow(state.fullPhrase) 
     }  
 }      
    
    
if(state.msgType == "SMS Message"){
	if(state.talkpresence == 'present' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Presence - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)

}

	else if (state.talkpresence == 'not present' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Presence - SMS Message - Sending Message: $state.fullPhrase")
  sendMessage(state.fullPhrase)

}    
} 

if(state.msgType == "PushOver Message"){
	if(state.talkpresence == 'present' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Presence - PushOver Message - Sending Message: $state.fullPhrase")
  pushOver(1, state.fullPhrase)

}

	else if(state.talkpresence == 'not present' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Presence - PushOver Message - Sending Message: $state.fullPhrase")
  pushOver(2, state.fullPhrase)

}    
} 
if(state.msgType == "Join Message"){
	if(state.talkpresence == 'present' && state.msg1 != null){
def msg = message1
        compileMsg(msg)
LOGDEBUG("Presence - Join Message - Sending Message: $state.fullPhrase")
  joinMsg(state.fullPhrase)

}

	else if(state.talkpresence == 'not present' && state.msg2 != null){
def msg = message2
        compileMsg(msg)
LOGDEBUG("Presence - Join Message - Sending Message: $state.fullPhrase")
  joinMsg(state.fullPhrase)

}    
} 

     if(state.msgType == "Play an Mp3 (No variables can be used)"){
         if(state.talkpresence == 'present' && state.mp3Switch == true){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         } 
         
        if(state.talkpresence == 'not present' && state.mp3Switch == false){
	mp3EventHandler()
    if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
      
         }   
}

}
}

// Power 
def powerTalkNow (evt){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused" 
state.meterValue = evt.value as double
state.nameOfDevice = evt.displayName
state.actionEvent = evt.value as double
	LOGDEBUG("$powerSensor shows $state.meterValue Watts")
    if(state.appgo == true){
	checkNow1()  
	}
    else if(state.appgo == false){
    LOGDEBUG("App disabled by $enableswitch being off")

}
                              
}                               
}
        
    
def checkNow1(){
if( actionType1 == false){
LOGDEBUG( "checkNow1 -  Power is: $state.meterValue")
    state.belowValue = belowThreshold as double
    if (state.meterValue < state.belowValue) {
   def mydelay = 60 * delay1 
   LOGDEBUG( "Checking again after delay: $delay1 minutes... Power is: $state.meterValue")
       runIn(mydelay, checkAgain1, [overwrite: false])     
      }
      }
      
else if( actionType1 == true){
LOGDEBUG( "checkNow1 -  Power is: $state.meterValue")
    state.belowValue = belowThreshold as double
    if (state.meterValue > state.belowValue) {
   def mydelay = 60 * delay1
   LOGDEBUG( "Checking again after delay: $delay1 minutes... Power is: $state.meterValue")
       runIn(mydelay, checkAgain2, [overwrite: false])     
      }
      }
  }

 

def checkAgain1() {
   
     if (state.meterValue < state.belowValue) {
      LOGDEBUG( " checkAgain1 - Checking again now... Power is: $state.meterValue")
    
      speakNow()
        
			}
     else  if (state.meterValue > state.belowValue) {
     LOGDEBUG( "checkAgain1 -  Power is: $state.meterValue so cannot run yet...")
	}	
}		

def checkAgain2() {
   
     if (state.meterValue > state.belowValue) {
      LOGDEBUG( "checkAgain2 - Checking again now... Power is: $state.meterValue")
    
      speakNow()
        
			}
     else  if (state.meterValue < state.belowValue) {
     LOGDEBUG( "checkAgain2 -  Power is: $state.meterValue so cannot run yet...")
	}	
}		



def speakNow(){
	checkAllow()
    if(state.allAllow == true){  
		LOGDEBUG("Power - speakNow...")
		state.msg1 = message1
	    if ( state.timer1 == true){
            if(state.msgType != "Play an Mp3 (No variables can be used)"){compileMsg(state.msg1)}
            
		    if(state.msgType == "Voice Message (MusicPlayer)"){
	    		checkVolume()
				LOGDEBUG("All OK! - Playing message: '$state.fullPhrase'")
			    speaker.playTextAndRestore(state.fullPhrase)     
				startTimerPower()  
    		}
    		if(state.msgType == "Voice Message (SpeechSynth)"){
			    LOGDEBUG("Power - Playing Message - Sending Message: $state.fullPhrase")   
			    speechSynthNow(state.fullPhrase) 
                startTimerPower()
        	}         
			if(state.msgType == "SMS Message" && state.msg1 != null){
				LOGDEBUG("Power - SMS Message - Sending Message: $state.fullPhrase")
				sendMessage(state.fullPhrase)
				startTimerPower()
			} 
    		if(state.msgType == "PushOver Message" && state.msg1 != null){
				LOGDEBUG("Power - PushOver Message - Sending Message: $state.fullPhrase")
				pushOver(1, state.fullPhrase)
				startTimerPower()
			} 
      		if(state.msgType == "Join Message"){
				LOGDEBUG("Power - Join Message - Sending Message: $state.fullPhrase")
				joinMsg(state.fullPhrase)
				startTimerPower()
			}     
            
            if(state.msgType == "Play an Mp3 (No variables can be used)"){
			mp3EventHandler()
    		if(state.soundToPlay == null){ LOGDEBUG(" Mp3 ERROR - cannot find $state.soundToPlay")} 
       		startTimerPower()    
	}
		}    
  		if(state.presenceRestriction ==  false || state.presenceRestriction1 ==  false){
			LOGDEBUG( "Cannot continue - Presence failed")
		}
	}
}

def startTimerPower(){
	state.timer1 = false
	state.timeDelay = (60 * state.msgDelay)
	LOGDEBUG("Waiting for $state.timeDelay seconds before resetting timer to allow further messages")
	runIn(state.timeDelay, resetTimerPower)
}

def resetTimerPower() {
	state.timer1 = true
	LOGDEBUG( "Timer reset - Messages allowed")
}






// PushOver Message Actions =============================
def pushOver(msgType, inMsg){
    checkAllow()
    if(state.allAllow == true){  
    if(state.timer1 == true){
        if(state.selection == "Weather Alert"){state.fullPhrase = inMsg}
        if(state.selection != "Weather Alert"){
            compileMsg(inMsg)
             LOGDEBUG("Compiled Message = $state.fullPhrase ")
                                              }
 
    newMessage = state.fullPhrase
   
 if(msgType == 1){
  def newPriority = priority1 
  LOGDEBUG("Message priority = $newPriority - Action = $msgType" ) 
     
    if(newPriority  == 'None'){
       LOGDEBUG("Priority = $newPriority")
     state.msg1 = newMessage
	speaker.speak(state.msg1)
    }
     
    else if(newPriority  == 'Low'){
        LOGDEBUG("Priority = $newPriority")
     state.msg1 = '[L]' + newMessage
	speaker.speak(state.msg1)
    }
    else if(newPriority  == 'Normal'){
        LOGDEBUG("Priority = $newPriority")
     state.msg1 = '[N]' + newMessage
	speaker.speak(state.msg1)
    }
    else if(newPriority  == 'High'){
        LOGDEBUG("Priority = $newPriority")
     state.msg1 = '[H]' + newMessage
	speaker.speak(state.msg1)
    }
  } 
 if(msgType == 2){
  def newPriority = priority2 
  LOGDEBUG("Message priority = $newPriority - Action = $msgType" ) 
     
    if(newPriority  == 'None'){
        LOGDEBUG("Priority = $newPriority")
     state.msg1 = newMessage
	speaker.speak(state.msg1)
    }
     
    else if(newPriority  == 'Low'){
        LOGDEBUG("Priority = $newPriority")
     state.msg1 = '[L]' + newMessage
	speaker.speak(state.msg1)
    }
    else if(newPriority  == 'Normal'){
        LOGDEBUG("Priority = $newPriority")
     state.msg1 = '[N]' + newMessage
	speaker.speak(state.msg1)
    }
    else if(newPriority  == 'High'){
        LOGDEBUG("Priority = $newPriority")
     state.msg1 = '[H]' + newMessage
	speaker.speak(state.msg1)
    }
  }
     state.timer1 = false
            startTimer1()
  }
 }
}


// Speech Synth *****************

def speechSynthNow(inMsg){
    LOGDEBUG("Calling.. speechSynthNow")
    checkAllow()
    if(state.allAllow == true){ 
      
			if(state.timer1 == true){
			def newMessage = inMsg
			if(state.selection == "Weather Alert"){state.msg1 = inMsg}
			if(state.selection != "Weather Alert"){
            compileMsg(inMsg)
			LOGDEBUG("Compiled Message = $state.fullPhrase ")
                   }
            def soundType=inMsg.toUpperCase()
				if (soundType == '%CHIME%')
					speaker.chime()
				else if (soundType == '%DOORBELL%')
 					speaker.doorbell()
				else{   
	            state.msg1 = state.fullPhrase
            	speaker.speak(state.msg1)
				state.timer1 = false
				startTimer1()
 			}
		}
    }    
}



def joinMsg(inMsg){
     checkAllow()
    if(state.allAllow == true){  
    if(state.timer1 == true){
    newMessage = state.fullPhrase
  LOGDEBUG(" Join message = $newMessage ")  
     state.msg1 = newMessage
	speaker.speak(state.msg1)
    }
 }
}    
    


// Talk now....

def talkSwitch(){
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){log.info "Continue - App NOT paused"     
 	checkAllow()

if(state.allAllow == true){
LOGDEBUG( " Continue... Check delay...")

if(state.msgNow == 'oneNow' && state.timer1 == true && state.msg1 != null){
LOGDEBUG("Calling.. CompileMsg")
    if(state.selection == 'Weather Alert'){state.fullPhrase = state.msg1}
    if(state.selection != 'Weather Alert'){compileMsg(state.msg1)}    

LOGDEBUG("All OK! - Playing message 1: '$state.fullPhrase'")
     speaker.playTextAndRestore(state.fullPhrase)
		startTimer1()
}
    
if(state.msgNow == 'twoNow'  && state.msg2 != null && state.timer2 == true){
LOGDEBUG("Calling.. CompileMsg")
compileMsg(state.msg2)
LOGDEBUG("All OK! - Playing message 2: '$state.fullPhrase'")
     speaker.playTextAndRestore(state.fullPhrase)
		startTimer2()
			}
		}
	}
}


def checkVolume(){
    LOGDEBUG("Calling.. CheckVolume")
    setDefaults()
    LOGDEBUG("state.multiVolumeSlots = $state.multiVolumeSlots")
def timecheck = fromTime2
if (timecheck != null){
def between2 = timeOfDayIsBetween(toDateTime(fromTime2), toDateTime(toTime2), new Date(), location.timeZone)
    if (between2) {
   state.volume = state.volumeAllq 
 
 if(state.multiVolumeSlotsq == false){
      speaker.setLevel(state.volume)
    LOGDEBUG("Multi Volume Not used - setting volume of all speakers to $state.volume")

  }
    
   else if(state.multiVolumeSlotsq == true){
     LOGDEBUG("Multi Volume used - setting volume of each speaker")  
		if(state.speakerNumberq == "2"){ 
        speakerN1q.setLevel(state.voiceVolumeAq)
        LOGDEBUG("Speaker 1: setting volume to $state.voiceVolumeAq")     
    	speakerN2q.setLevel(state.voiceVolumeBq)
        LOGDEBUG("Speaker 2: setting volume to $state.voiceVolumeBq")      
		}
       
		if(state.speakerNumberq == "3"){ 
        speakerN1q.setLevel(state.voiceVolumeAq)
        LOGDEBUG("Speaker 1: setting volume to $state.voiceVolumeAq")  
    	speakerN2q.setLevel(state.voiceVolumeBq)
        LOGDEBUG("Speaker 2: setting volume to $state.voiceVolumeBq")      
        speakerN3q.setLevel(state.voiceVolumeCq)  
        LOGDEBUG("Speaker 3: setting volume to $state.voiceVolumeCq")      
		}
    
        if(state.speakerNumberq == "4"){ 
        speakerN1q.setLevel(state.voiceVolumeAq)
            LOGDEBUG("Speaker 1: setting volume to $state.voiceVolumeAq")  
    	speakerN2q.setLevel(state.voiceVolumeBq)
            LOGDEBUG("Speaker 2: setting volume to $state.voiceVolumeBq")  
        speakerN3q.setLevel(state.voiceVolumeCq) 
            LOGDEBUG("Speaker 3: setting volume to $state.voiceVolumeCq")  
        speakerN4q.setLevel(state.voiceVolumeDq) 
            LOGDEBUG("Speaker 4: setting volume to $state.voiceVolumeDq")  
		}
        
        if(state.speakerNumberq == "5"){ 
        speakerN1q.setLevel(state.voiceVolumeAq)
            LOGDEBUG("Speaker 1: setting volume to $state.voiceVolumeAq")  
    	speakerN2q.setLevel(state.voiceVolumeBq)
            LOGDEBUG("Speaker 2: setting volume to $state.voiceVolumeBq")  
        speakerN3q.setLevel(state.voiceVolumeCq) 
            LOGDEBUG("Speaker 3: setting volume to $state.voiceVolumeCq")  
        speakerN4q.setLevel(state.voiceVolumeDq) 
            LOGDEBUG("Speaker 4: setting volume to $state.voiceVolumeDq")  
        speakerN5q.setLevel(state.voiceVolumeEq)   
            LOGDEBUG("Speaker 5: setting volume to $state.voiceVolumeEq")  
		} 
       
    } 
    
 
        
        
    
   LOGDEBUG("Quiet Time = Yes - Setting Quiet time volume")
    
}
else if (!between2) {
state.volume = state.volumeAll
LOGDEBUG("Quiet Time = No - Setting Normal time volume ")
    
  if(state.multiVolumeSlots == false){
      speaker.setLevel(state.volume)
     LOGDEBUG("Multi Volume Not used - setting volume of all speakers to $state.volume")
  }
    
   else if(state.multiVolumeSlots == true){
		if(state.speakerNumber == "2"){ 
        speakerN1.setLevel(state.voiceVolumeA)
            LOGDEBUG("Speaker 1: setting volume to $state.voiceVolumeA")  
    	speakerN2.setLevel(state.voiceVolumeB)
            LOGDEBUG("Speaker 2: setting volume to $state.voiceVolumeB") 
		}
       
		if(state.speakerNumber == "3"){ 
        speakerN1.setLevel(state.voiceVolumeA)
            LOGDEBUG("Speaker 1: setting volume to $state.voiceVolumeA") 
    	speakerN2.setLevel(state.voiceVolumeB)
            LOGDEBUG("Speaker 2: setting volume to $state.voiceVolumeB") 
        speakerN3.setLevel(state.voiceVolumeC)    
            LOGDEBUG("Speaker 3: setting volume to $state.voiceVolumeC") 
		}
    
        if(state.speakerNumber == "4"){ 
        speakerN1.setLevel(state.voiceVolumeA)
            LOGDEBUG("Speaker 1: setting volume to $state.voiceVolumeA") 
    	speakerN2.setLevel(state.voiceVolumeB)
            LOGDEBUG("Speaker 2: setting volume to $state.voiceVolumeB") 
        speakerN3.setLevel(state.voiceVolumeC) 
            LOGDEBUG("Speaker 3: setting volume to $state.voiceVolumeC") 
        speakerN4.setLevel(state.voiceVolumeD)   
            LOGDEBUG("Speaker 4: setting volume to $state.voiceVolumeD") 
		}
        
        if(state.speakerNumber == "5"){ 
        speakerN1.setLevel(state.voiceVolumeA)
            LOGDEBUG("Speaker 1: setting volume to $state.voiceVolumeA") 
    	speakerN2.setLevel(state.voiceVolumeB)
            LOGDEBUG("Speaker 2: setting volume to $state.voiceVolumeB") 
        speakerN3.setLevel(state.voiceVolumeC) 
            LOGDEBUG("Speaker 3: setting volume to $state.voiceVolumeC") 
        speakerN4.setLevel(state.voiceVolumeD) 
            LOGDEBUG("Speaker 4: setting volume to $state.voiceVolumeD") 
        speakerN5.setLevel(state.voiceVolumeE)     
            LOGDEBUG("Speaker 5: setting volume to $state.voiceVolumeE") 
		}
       
    } 
    

	}
}
else if (timecheck == null){
   if(state.multiVolumeSlots == false){
       state.volume = state.volumeAll
      speaker.setLevel(state.volume)
    LOGDEBUG("No 'quiet time' settings...Multi Volume Not used - setting volume of all speakers to $state.volume - state.volume = $state.volumeAll")
     speaker.setLevel(state.volume)  
       

      
  }
   else if(state.multiVolumeSlots == true){
		if(state.speakerNumber == "2"){ 
        speakerN1.setLevel(state.voiceVolumeA)
    	speakerN2.setLevel(state.voiceVolumeB)
		}
       
		if(state.speakerNumber == "3"){ 
        speakerN1.setLevel(state.voiceVolumeA)
    	speakerN2.setLevel(state.voiceVolumeB)
        speakerN3.setLevel(state.voiceVolumeC)    
		}
    
        if(state.speakerNumber == "4"){ 
        speakerN1.setLevel(state.voiceVolumeA)
    	speakerN2.setLevel(state.voiceVolumeB)
        speakerN3.setLevel(state.voiceVolumeC) 
        speakerN4.setLevel(state.voiceVolumeD)   
		}
        
        if(state.speakerNumber == "5"){ 
        speakerN1.setLevel(state.voiceVolumeA)
    	speakerN2.setLevel(state.voiceVolumeB)
        speakerN3.setLevel(state.voiceVolumeC) 
        speakerN4.setLevel(state.voiceVolumeD) 
        speakerN5.setLevel(state.voiceVolumeE)     
		}
       
    } 
    

	}
 
}
// Message Actions ==================================


def sendMessage(msg) {
    LOGDEBUG("Calling.. sendMessage")
    compileMsg(msg)
    if(state.allAllow == true){
        def mydelay = triggerDelay
        LOGDEBUG("Waiting $mydelay seconds before sending")
        runIn(mydelay, pushNow)
	}
}


// end message actions ===============================




def pushNow(){

if(state.allAllow == true && state.timer1 == true){

log.trace "SendMessage - $state.fullPhrase"
        if (sms1) {
          LOGDEBUG("Sending message: '$state.fullPhrase' to $sms1")
         sendSms(sms1, state.fullPhrase)
           
     }
     if (sms2) {
          LOGDEBUG("Sending message: '$state.fullPhrase' to $sms2")
         sendSms(sms2, state.fullPhrase)
         
     }
     if (sms3) { 
         LOGDEBUG("Sending message: '$state.fullPhrase' to $sms3")
                sendSms(sms3, state.fullPhrase)
               }
     if (sms4) {
          LOGDEBUG("Sending message: '$state.fullPhrase' to $sms4")
         sendSms(sms4, state.fullPhrase)
     }
     if (sms5) {
         LOGDEBUG("Sending message: '$state.fullPhrase' to $sms5")
         sendSms(sms5, state.fullPhrase)
     }
     state.timer1 = false
            startTimer1()
}
}



// Check Mode
def modeCheck() {
    LOGDEBUG("Checking mode...")
	def result = !modes || modes.contains(location.mode)
    
    LOGDEBUG("Mode = $result")
    state.modeCheck = result
    return state.modeCheck
 }


// Check time allowed to run...



def checkTime(){
def timecheckNow = fromTime
if (timecheckNow != null){
    
def between = timeOfDayIsBetween(toDateTime(fromTime), toDateTime(toTime), new Date(), location.timeZone)
    if (between) {
    state.timeOK = true
   LOGDEBUG("Time is ok so can continue...")
    
}
else if (!between) {
state.timeOK = false
LOGDEBUG("Time is NOT ok so cannot continue...")
	}
  }
else if (timecheckNow == null){  
state.timeOK = true
  LOGDEBUG("Time restrictions have not been configured -  Continue...")
  }
}


// check days allowed to run
def checkDay(){
def daycheckNow = days
if (daycheckNow != null){
 def df = new java.text.SimpleDateFormat("EEEE")
    
    df.setTimeZone(location.timeZone)
    def day = df.format(new Date())
    def dayCheck1 = days.contains(day)
    if (dayCheck1) {

  state.dayCheck = true
LOGDEBUG( " Day ok so can continue...")
 }       
 else {
LOGDEBUG( " Not today!")
 state.dayCheck = false
 }
 }
if (daycheckNow == null){ 
 LOGDEBUG("Day restrictions have not been configured -  Continue...")
 state.dayCheck = true 
} 
}


 // Delay between messages...

def startTimer1(){
 
    LOGDEBUG("state.msgDelay = $state.msgDelay")
state.timer1 = false
state.timeDelay = 60 * state.msgDelay
LOGDEBUG("Waiting for $state.timeDelay seconds before resetting timer1 to allow further messages")
runIn(state.timeDelay, resetTimer1)
}

def startTimer2(){
state.timer2 = false
state.timeDelay = 60 * state.msgDelay
LOGDEBUG( "Waiting for $state.timeDelay seconds before resetting timer2 to allow further messages")
runIn(state.timeDelay, resetTimer2)
}

def resetTimer1() {
state.timer1 = true
LOGDEBUG( "Timer 1 reset - Messages allowed")
}
def resetTimer2() {
state.timer2 = true
LOGDEBUG("Timer 2 reset - Messages allowed")
}




private compileMsg(msg) {
	LOGDEBUG("compileMsg - msg = ${msg}")
    if(pollorNot){
        weather1.poll()
        pause(5000)
		LOGDEBUG("Waiting a short while for the weather device to catch up")  
    }
    def msgComp = ""
    msgComp = msg.toLowerCase()
    LOGDEBUG("msgComp = $msgComp")
    if (msgComp.toLowerCase().contains("%group1%")) {msgComp = msgComp.toLowerCase().replace('%group1%', getPre() )}
    if (msgComp.toLowerCase().contains("%group2%")) {msgComp = msgComp.toLowerCase().replace('%group2%', getPost() )}
    if (msgComp.toLowerCase().contains("%group3%")) {msgComp = msgComp.toLowerCase().replace('%group3%', getWakeUp() )}
    if (msgComp.toLowerCase().contains("%group4%")) {msgComp = msgComp.toLowerCase().replace('%group4%', getGroup4() )}
 	if (msgComp.toLowerCase().contains("%alert%")) {msgComp = msgComp.toLowerCase().replace('%alert%', state.weatherAlert )}
    if (msgComp.toLowerCase().contains("%wnow%")) {msgComp = msgComp.toLowerCase().replace('%wnow%', state.weatherNow)}
    if (msgComp.toLowerCase().contains("%rain%")) {msgComp = msgComp.toLowerCase().replace('%rain%', state.rainFall )}
    if (msgComp.toLowerCase().contains("%vis%")) {msgComp = msgComp.toLowerCase().replace('%vis%', state.weatherVisibility )}
    if (msgComp.toLowerCase().contains("%wgust%")) {msgComp = msgComp.toLowerCase().replace('%wgust%', state.weatherWindGust )}
    if (msgComp.toLowerCase().contains("%wspeed%")) {msgComp = msgComp.toLowerCase().replace('%wspeed%', state.weatherWindSpeed )}
    if (msgComp.toLowerCase().contains("%wdir%")) {msgComp = msgComp.toLowerCase().replace('%wdir%', state.weatherWindDir )}
    if (msgComp.toLowerCase().contains("%feel%")) {msgComp = msgComp.toLowerCase().replace('%feel%', state.weatherFeelsLike )}
    if (msgComp.toLowerCase().contains("%temp%")) {msgComp = msgComp.toLowerCase().replace('%temp%', state.weatherTemperature )}
    if (msgComp.toLowerCase().contains("%hum%")) {msgComp = msgComp.toLowerCase().replace('%hum%', state.weatherHumidity )}
    if (msgComp.toLowerCase().contains("%low%")) {msgComp = msgComp.toLowerCase().replace('%low%', state.weatherForecastLow )}
    if (msgComp.toLowerCase().contains("%high%")) {msgComp = msgComp.toLowerCase().replace('%high%', state.weatherForecastHigh )} 
    if (msgComp.toLowerCase().contains("%wsum%")) {msgComp = msgComp.toLowerCase().replace('%wsum%', state.weatherSummary1 )} 
    if (msgComp.toLowerCase().contains("%time%")) {msgComp = msgComp.toLowerCase().replace('%time%', getTime(false,true))}  
    if (msgComp.toLowerCase().contains("%day%")) {msgComp = msgComp.toLowerCase().replace('%day%', getDay() )}  
	if (msgComp.toLowerCase().contains("%date%")) {msgComp = msgComp.toLowerCase().replace('%date%', getdate() )}  
    if (msgComp.toLowerCase().contains("%year%")) {msgComp = msgComp.toLowerCase().replace('%year%', getyear() )}  
 	if (msgComp.toLowerCase().contains("%opencontact%")) {msgComp = msgComp.toLowerCase().replace('%opencontact%', getContactReportOpen() )}  
    if (msgComp.toLowerCase().contains("%closedcontact%")) {msgComp = msgComp.toLowerCase().replace('%closedcontact%', getContactReportClosed() )} 
    if (msgComp.toLowerCase().contains("%mode%")) {msgComp = msgComp.toLowerCase().replace('%mode%', state.modeNow )}
	if (msgComp.toLowerCase().contains("%opencount%")) {msgComp = msgComp.toLowerCase().replace('%opencount%', getContactOpenCount() )}  
    if (msgComp.toLowerCase().contains("%closedcount%")) {msgComp = msgComp.toLowerCase().replace('%closedcount%', getContactClosedCount() )} 
    if (msgComp.toLowerCase().contains("%lightsoncount%")) {msgComp = msgComp.toLowerCase().replace('%lightsoncount%', getLightsOnCount() )} 
    if (msgComp.toLowerCase().contains("%lightsoffcountT%")) {msgComp = msgComp.toLowerCase().replace('%lightsoffcount%', getLightsOffCount() )} 
    if (msgComp.toLowerCase().contains("%lightson%")) {msgComp = msgComp.toLowerCase().replace('%lightson%', getLightsOnReport() )} 
    if (msgComp.toLowerCase().contains("%lightsoff%")) {msgComp = msgComp.toLowerCase().replace('%lightsoff%', getLightsOffReport() )}
    if (msgComp.toLowerCase().contains("%switchesoncount%")) {msgComp = msgComp.toLowerCase().replace('%switchesoncount%', getSwitchesOnCount() )} 
    if (msgComp.toLowerCase().contains("%switchesoffcount%")) {msgComp = msgComp.toLowerCase().replace('%switchesoffcount%', getSwitchesOffCount() )} 
    if (msgComp.toLowerCase().contains("%switcheson%")) {msgComp = msgComp.toLowerCase().replace('%switcheson%', getSwitchesOnReport() )} 
    if (msgComp.toLowerCase().contains("%switchesoff%")) {msgComp = msgComp.toLowerCase().replace('%switchesoff%', getSwitchesOffReport() )}
 	if (msgComp.toLowerCase().contains("%device%")) {msgComp = msgComp.toLowerCase().replace('%device%', getNameofDevice() )}  
	if (msgComp.toLowerCase().contains("%event%")) {msgComp = msgComp.toLowerCase().replace('%event%', getWhatHappened() )}  
    if (msgComp.toLowerCase().contains("%greeting%")) {msgComp = msgComp.toLowerCase().replace('%greeting%', getGreeting() )}      
    if (msgComp.toLowerCase().contains("n/a")) {msgComp = msgComp.toLowerCase().replace('n/a', ' ' )}
	if (msgComp.toLowerCase().contains("no station data")) {msgComp = msgComp.toLowerCase().replace('no station data', ' ' )}
    if (msgComp.toLowerCase().contains(":")) {msgComp = msgComp.toLowerCase().replace(':', ' ')}
 //   if (msgComp.toLowerCase().contains("!")) {msgComp = msgComp.toLowerCase().replace('!', ' ')}
    
    LOGDEBUG("1st Stage Compile (Pre weather processing) = $msgComp")
    convertWeatherMessage(msgComp)
  	LOGDEBUG("2nd Stage Compile (Post weather processing) = $state.fullPhrase")
}


// Message variables ***************************************************





// Random message processing ************************************

// 'Pre' random message processing

private getPre(){
    def msgPre = getGroup1(null)
    LOGDEBUG("msgPre = $msgPre")

    return msgPre
}

// 'Post' random message processing
private getPost(){
    def msgPost = getGroup2(null)
    LOGDEBUG("msgPost = $msgPost")

    return msgPost
}

// 'WakeUp' random message processing
private getWakeUp(){
    def msgWake = getGroup3(null)
    LOGDEBUG("msgWake = $msgWake")

    return msgWake
}

// End random message processing ************************************


// 'Greeting' message processing
private getGreeting(){
    def calendar = Calendar.getInstance()
	calendar.setTimeZone(location.timeZone)
	def timeHH = calendar.get(Calendar.HOUR) toInteger() // changed from toString() to toInteger()  ***********************************
    def timeampm = calendar.get(Calendar.AM_PM) ? "pm" : "am" 
    
LOGDEBUG("timeHH = $timeHH")
if(timeampm == 'am'){
state.greeting = "GOOD MORNING"
}

else if(timeampm == 'pm' && timeHH < 6){
state.greeting = "GOOD AFTERNOON"
LOGDEBUG("timeampm = $timeampm - timehh = $timeHH")
}

else if(timeampm == 'pm' && timeHH >= 6){
LOGDEBUG("timehh = $timeHH - timeampm = $timeampm")

state.greeting = "GOOD EVENING"
} 

LOGDEBUG("Greeting = $state.greeting")
return state.greeting
}





private getWhatHappened(){
LOGDEBUG("Event = $state.actionEvent")
return state.actionEvent

}

private getNameofDevice(){
LOGDEBUG("Device = $state.nameOfDevice")
return state.nameOfDevice

}

private getContactReportOpen(){
LOGDEBUG("Calling getContactReportOpen")

def open = sensors.findAll { it?.latestValue("contact") == 'open' }
		if (open) { 
LOGDEBUG("Open windows or doors: ${open.join(',,, ')}")
           state.anyOpen = "${open.join(',,, ')}"
	}
    else
    {
     state.anyOpen = " "   
       
    }
  return state.anyOpen      
}
private getContactOpenCount(){
LOGDEBUG("Calling getContactOpenCount")
def countOpen = 0
    def open = sensors.findAll { it?.latestValue("contact") == 'open' }
    for (sensor in open) {
    countOpen += 1
     }
 LOGDEBUG("Open count = $countOpen")         
return countOpen.toString()
	
}
private getContactClosedCount(){
LOGDEBUG("Calling getContactClosedCount")
def countClosed = 0
    def closed = sensors.findAll { it?.latestValue("contact") == 'closed' }
    for (sensor in closed) {
    countClosed += 1
     }
 LOGDEBUG("Closed count = $countClosed")         
return countClosed.toString()
	
}

private getContactReportClosed(){
LOGDEBUG("Calling getContactReportClosed")

def closed = sensors.findAll { it?.latestValue("contact") == 'closed' }
		if (closed) { 
LOGDEBUG("Closed windows or doors: ${closed.join(',,, ')}")
           def anyClosed = "${closed.join(',,, ')}"
            
return anyClosed
	}
     else { return " "}
}

private getLightsOnCount(){
LOGDEBUG("Calling getLightsOnCount")
def countlightsOn = 0
    def lightsOn = lights.findAll { it?.latestValue("switch") == 'on' }
    
    
    for (lights in lightsOn) {
    countlightsOn += 1
     }
 LOGDEBUG("lights ON count = $countlightsOn")         
return countlightsOn.toString()
	
}

private getLightsOnReport(){
LOGDEBUG("Calling getlightsOnReport")

def lightsOn1 = lights.findAll { it?.latestValue("switch") == 'on' }
		if (lightsOn1) { 
LOGDEBUG("Lights on: ${lightsOn1.join(', ')}")
           def anylightsOn1 = "${lightsOn1.join(', ')}"
            
return anylightsOn1
	}
    else { return " "}
}

private getLightsOffCount(){
LOGDEBUG("Calling getLightsOffCount")
def countlightsOff = 0
    def lightsOff = lights.findAll { it?.latestValue("switch") == 'off' }
    for (lights in lightsOff) {
    countlightsOff += 1
     }
 LOGDEBUG("lights OFF count = $countlightsOff")         
return countlightsOff.toString()
	
}

private getLightsOffReport(){
LOGDEBUG("Calling getlightsOffReport")

def lightsOff = lights.findAll { it?.latestValue("switch") == 'off' }
		if (lightsOff) { 
LOGDEBUG("Lights off: ${lightsOff.join(', ')}")
           def anylightsOff1 = "${lightsOff.join(', ')}"
          
                
                
return anylightsOff1
	}
     else { return " "}
}


private getSwitchesOnCount(){
LOGDEBUG("Calling getswitchesOnCount")
state.countswitchesOn = 0
    def switchesOn = switches.findAll { it?.latestValue("switch") == 'on' }
    for (switches in switchesOn) {
    state.countswitchesOn += 1
     }
 LOGDEBUG("switches ON count = $state.countswitchesOn")         
return state.countswitchesOn.toString()
	
}

private getSwitchesOnReport(){
LOGDEBUG("Calling getSwitchesOnReport")
  def switchesOn1 = switches.findAll { it?.latestValue("switch") == 'on' }
		if (switchesOn1) { 
LOGDEBUG("switches on: ${switchesOn1.join(', ')}")
           def anyswitchesOn1 = "${switchesOn1.join(', ')}"
            
return anyswitchesOn1
	}

         else { return " "}
}

private getSwitchesOffCount(){
LOGDEBUG("Calling getswitchesOffCount")
state.countswitchesOff = 0
    def switchesOff = switches.findAll { it?.latestValue("switch") == 'off' }
    for (switches in switchesOff) {
    state.countswitchesOff += 1
     }
 LOGDEBUG("switches OFF count = $countswitchesOff")         
return state.countswitchesOff.toString()
	
}

private getSwitchesOffReport(){
LOGDEBUG("Calling getswitchesOffReport")

def switchesOff1 = switches.findAll { it?.latestValue("switch") == 'off' }
		if (switchesOff1) { 
LOGDEBUG("switches off: ${switchesOff1.join(', ')}")
           def anyswitchesOff1 = "${switchesOff1.join(', ')}"
            
return anyswitchesOff1
	}
         else { return " "}
}




















private convertWeatherMessage(msgIn){

LOGDEBUG("Running convertWeatherMessage... Converting weather message to English (If weather requested)...")

    def msgOut = ""
    msgOut = msgIn.toLowerCase()
    
// Weather Variables    

    
    msgOut = msgOut.replace(" n ", " north ")
    msgOut = msgOut.replace(" s ", " south ")
    msgOut = msgOut.replace(" e ", " east ")
    msgOut = msgOut.replace(" w ", " west ")
    msgOut = msgOut.replace(" ne ", " northeast ")
    msgOut = msgOut.replace(" nw ", " northwest ")
    msgOut = msgOut.replace(" se ", " southeast ")
    msgOut = msgOut.replace(" sw ", " southwest ")
    msgOut = msgOut.replace(" nne ", " north northeast ")
    msgOut = msgOut.replace(" nnw ", " north northwest ")
    msgOut = msgOut.replace(" sse ", " south southeast ")
    msgOut = msgOut.replace(" ssw ", " south southwest ")
    msgOut = msgOut.replace(" ene ", " east northeast ")
    msgOut = msgOut.replace(" ese ", " east southeast ")
    msgOut = msgOut.replace(" wnw ", " west northwest ")
    msgOut = msgOut.replace(" wsw ", " west southwest ")
    msgOut = msgOut.replace(" mph ", "milesper hour")
    msgOut = msgOut.replace(" precip ", " precipitation")
    
   state.fullPhrase = msgOut

  return state.fullPhrase
  
  
}




private getTime(includeSeconds, includeAmPm){
    def calendar = Calendar.getInstance()
	calendar.setTimeZone(location.timeZone)
	def timeHH1 = calendar.get(Calendar.HOUR) 
    def timeHH = timeHH1.toString()
    def timemm1 = calendar.get(Calendar.MINUTE)
    def timemm = timemm1.toString()
    def timess1 = calendar.get(Calendar.SECOND)
    def timess = timess1.toString()
    def timeampm = calendar.get(Calendar.AM_PM) ? "pm" : "am" 
    
LOGDEBUG("timeHH = $timeHH")
LOGDEBUG("timemm = $timemm")
 
 if (timeHH == "0") {timeHH = timeHH.replace("0", "12")}   //  Changes hours so it doesn't say 0 for 12 midday/midnight
//     if(state.msgType == "Voice Message"){
// if (timeHH == "10") {timeHH = timeHH.replace("10", "TEN")}   //  Changes 10 to TEN as there seems to be an issue with it saying 100 for 10 o'clock
//     } 
    
 if (hour24 == true){ // Convert to 24hr clock if selected
LOGDEBUG("hour24 = $hour24 -  So converting hours to 24hr format")
     
 if (timeHH == "1" && timeampm.contains ("pm")){timeHH = timeHH.replace("1", "13")}
 if (timeHH == "2" && timeampm.contains ("pm")){timeHH = timeHH.replace("2", "14")}
 if (timeHH == "3" && timeampm.contains ("pm")){timeHH = timeHH.replace("3", "15")}
 if (timeHH == "4" && timeampm.contains ("pm")){timeHH = timeHH.replace("4", "16")}
 if (timeHH == "5" && timeampm.contains ("pm")){timeHH = timeHH.replace("5", "17")}
 if (timeHH == "6" && timeampm.contains ("pm")){timeHH = timeHH.replace("6", "18")}
 if (timeHH == "7" && timeampm.contains ("pm")){timeHH = timeHH.replace("7", "19")}
 if (timeHH == "8" && timeampm.contains ("pm")){timeHH = timeHH.replace("8", "20")}
 if (timeHH == "9" && timeampm.contains ("pm")){timeHH = timeHH.replace("9", "21")}
 if (timeHH == "10" && timeampm.contains ("pm")){timeHH = timeHH.replace("10", "22")}
 if (timeHH == "11" && timeampm.contains ("pm")){timeHH = timeHH.replace("11", "23")}
	 timeampm = timeampm.replace("pm", " ")
 log.warn " Added 12 hrs to numbers"    
  if (timemm == "0") {
     LOGDEBUG("timemm = 0  - So changing to 'hundred hours")
     timemm = timemm.replace("0", " hundred hours")
    	  if(timeampm.contains ("pm")){timeampm = timeampm.replace("pm", " ")}
     	  if(timeampm.contains ("am")){timeampm = timeampm.replace("am", " ")}
      }
 }
    if(hour24 != true){
     if (timemm == "0"){
     LOGDEBUG("timemm = $timemm  - So changing to o'clock")
     timemm = timemm.replace("0", "o'clock")
     if(timeampm.contains ("pm")){timeampm = timeampm.replace("pm", " ")}
     if(timeampm.contains ("am")){timeampm = timeampm.replace("am", " ")}
      }

    
	if (timemm == "1") {timemm = timemm.replace("1", "01")}
	if (timemm == "2") {timemm = timemm.replace("2", "02")}
	if (timemm == "3") {timemm = timemm.replace("3", "03")}
	if (timemm == "4") {timemm = timemm.replace("4", "04")}  
	if (timemm == "5") {timemm = timemm.replace("5", "05")}  
	if (timemm == "6") {timemm = timemm.replace("6", "06")}  
	if (timemm == "7") {timemm = timemm.replace("7", "07")}  
	if (timemm == "8") {timemm = timemm.replace("8", "08")}  
	if (timemm == "9") {timemm = timemm.replace("9", "09")}  
    }

 LOGDEBUG("timeHH Now = $timeHH")
 LOGDEBUG("timemm Now = $timemm")   
    def timestring = "${timeHH}:${timemm}"
    if (includeSeconds) { timestring += ":${timess}" }
    if (includeAmPm) { timestring += " ${timeampm}" }
   LOGDEBUG("timestring = $timestring")
    
    return timestring

}

private getDay(){
	def df = new java.text.SimpleDateFormat("EEEE")
	if (location.timeZone) {
		df.setTimeZone(location.timeZone)
	}
	else {
		df.setTimeZone(TimeZone.getTimeZone("America/New_York"))
	}
	def day = df.format(new Date())
    
    return day
}

private parseDate(date, epoch, type){
    def parseDate = ""
    if (epoch){
        LOGDEBUG("epoc")
    	long longDate = Long.valueOf(epoch).longValue()
        LOGDEBUG("longDate done")
        parseDate = new Date(longDate).format("yyyy-MM-dd HH:mm", location.timeZone)
         LOGDEBUG("1st parseDate done")
    }
    else {
    	parseDate = date
    }
    new Date().parse("yyyy-MM-dd HH:mm", parseDate).format("${type}") //, timeZone(parseDate))
 
}
private getdate() {
    def month = parseDate("", now(), "MMMM")
    def dayNum = parseDate("", now(), "dd")
  LOGDEBUG("Date:  $dayNum $month")
    
    LOGDEBUG("dayNum = $dayNum - Converting into 'proper' English")
    if(dayNum == "01"){dayNum = dayNum.replace("01","THE FIRST OF")}
	if(dayNum == "02"){dayNum = dayNum.replace("02","THE SECOND OF")}
    if(dayNum == "03"){dayNum = dayNum.replace("03","THE THIRD OF")}
    if(dayNum == "04"){dayNum = dayNum.replace("04","THE FOURTH OF")}
    if(dayNum == "05"){dayNum = dayNum.replace("05","THE FIFTH OF")}
    if(dayNum == "06"){dayNum = dayNum.replace("06","THE SIXTH OF")}
    if(dayNum == "07"){dayNum = dayNum.replace("07","THE SEVENTH OF")}
    if(dayNum == "08"){dayNum = dayNum.replace("08","THE EIGHTH OF")}
    if(dayNum == "09"){dayNum = dayNum.replace("09","THE NINTH OF")}
    if(dayNum == "10"){dayNum = dayNum.replace("10","THE TENTH OF")}
    if(dayNum == "11"){dayNum = dayNum.replace("11","THE ELEVENTH OF")}
    if(dayNum == "12"){dayNum = dayNum.replace("12","THE TWELTH OF")}
    if(dayNum == "13"){dayNum = dayNum.replace("13","THE THIRTEENTH OF")}
    if(dayNum == "14"){dayNum = dayNum.replace("14","THE FOURTEENTH OF")}
    if(dayNum == "15"){dayNum = dayNum.replace("15","THE FIFTEENTH OF")}
    if(dayNum == "16"){dayNum = dayNum.replace("16","THE SIXTEENTH OF")}
    if(dayNum == "17"){dayNum = dayNum.replace("17","THE SEVENTEENTH OF")}
    if(dayNum == "18"){dayNum = dayNum.replace("18","THE EIGHTEENTH OF")}
    if(dayNum == "19"){dayNum = dayNum.replace("19","THE NINETEENTH OF")}
    if(dayNum == "20"){dayNum = dayNum.replace("20","THE TWENTIETH OF")}
    if(dayNum == "21"){dayNum = dayNum.replace("21","THE TWENTY FIRST OF")}
    if(dayNum == "22"){dayNum = dayNum.replace("22","THE TWENTY SECOND OF")} 
    if(dayNum == "23"){dayNum = dayNum.replace("23","THE TWENTY THIRD OF")}
    if(dayNum == "24"){dayNum = dayNum.replace("24","THE TWENTY FOURTH OF")}
    if(dayNum == "25"){dayNum = dayNum.replace("21","THE TWENTY FIFTH OF")}
    if(dayNum == "26"){dayNum = dayNum.replace("26","THE TWENTY SIXTH OF")}
    if(dayNum == "27"){dayNum = dayNum.replace("27","THE TWENTY SEVENTH OF")}
    if(dayNum == "28"){dayNum = dayNum.replace("28","THE TWENTY EIGHTH OF")}
    if(dayNum == "29"){dayNum = dayNum.replace("29","THE TWENTY NINTH OF")}
    if(dayNum == "30"){dayNum = dayNum.replace("30","THE THIRTIETH OF")}
    if(dayNum == "31"){dayNum = dayNum.replace("31","THE THIRTY FIRST OF")}
     LOGDEBUG("Day number has been converted to: '$dayNum'")  
    
    return dayNum + " " + month + " "
}
private getyear() {
    def year = parseDate("", now(), "yyyy")
	
   LOGDEBUG("Year =  $year")
         
    return year
}

private getGroup1(msgPreitem) {
    def preList = [
        "Welcome home %device%.", 
        "How you doing %device%?",
        "What's up %device%?",
        "Long time no see %device%.",
        "Hey %device%.",
        "Nice you see you %device%.",
        "Look who's home, it's %device%.",
        "Nice to have you back %device%.",
        "Howdy do %device%.",
        "What's going on %device%?",
        "How is everything %device%?",
        "It's been ages since I have seen you %device%.",
        "Where have you been hiding %device%?",
        "How's ette hanging %device%?",
        "What's cookin %device%?",
        "What's shakin %device%?",
        "Greetings and salutations %device%.",
        "Aloha %device%.",
        "Shalom %device%.",
        "How goes ette %device%?",
        "What's happening %device%",
        "How goes ette %device%?",
        "Roll out the red carpet for %device%!",
        "%greeting% %device%.",
        "OHlah %device%."
    ]
    if(state.preCount>25){
        for(int i = 25;i<state.preCount;i++) {
            def preDisplay = i.toInteger() + 1
            preList.add("Group 1 - Message ${preDisplay}")
        }
    }
    if(msgPreitem == null) {
        MaxRandom = (preList.size() >= state.preCount ? preList.size() : state.preCount)
        LOGDEBUG("MaxRandom = $MaxRandom") 
        def randomKey1 = new Random().nextInt(MaxRandom)
        LOGDEBUG("randomKey1 = $randomKey1") 
		msgPre = preList[randomKey1]
    } else {
        msgPre = preList[msgPreitem]
    }

	return msgPre
}

private getGroup2(msgPostitem) {
    def postList = [            
        "Goodbye %device%.  Hope you have a %greeting%." ,  
        "See you later %device%.",
        "Live long and prosper %device%.",
        "Farewell %device%",
        "Hope you have a great time %device%.",
        "Take care %device%.",
        "Smell you later %device%.",
        "I thought %device% would never leave!",
        "AhhDEEOhs %device%",
        "Chow %device%.",
        "Oh revwahr %device%.",
        "Sighenara %device%.",
        "Tah Tah for now %device%.",
        "Catch you later %device%.",
        "To-da-loo %device%.",
        "See you in the funny papers %device%.",
        "Toodles %device%.",
        "Godspeed %device%.",
        "So long %device%.",
        "Ouf vee duhr zane %device%.",
        "Aloha %device%",
        "Shalom %device%",
        "Cheerio %device%",
        "AhrreevehDEHRtchee %device%",
        "Bye %device%.  Hope you have a %greeting%."
    ]  
    if(state.postCount>25){
        for(int i = 25;i<state.postCount;i++) {
            def postDisplay = i.toInteger() + 1
            postList.add("Group 2 - Message ${postDisplay}")            
        }
    }
    if(msgPostitem == null) {
        MaxRandom2 = (postList.size() >= state.postCount ? postList.size() : state.postCount)
        LOGDEBUG("MaxRandom2 = $MaxRandom2") 
        def randomKey2 = new Random().nextInt(MaxRandom2)
        LOGDEBUG("randomKey2 = $randomKey2") 
        msgPost = postList[randomKey2]
    } else {
        msgPost = postList[msgPostitem]
    }

	return msgPost
}

private getGroup3(msgWakeitem) {
        def wakeList = [                        
        "It's time to wake up!",
        "Please Wake Up!",
        "Come on!,,, It's time to wake up!",
        "Get out of bed!,NOW!",
        "Come on! It's time to get up!",
        "It is %time%.  Time to wake up!",
        "I am not asking again.  Wake Up!",
        "Rise and shine! It's time to get up!",
        "Don't make me come over there. Get out of bed!,NOW!",
        "I am going to start counting! ,,, 5 ,,, 4 ,,, 3,,, 2 ,,, 1"    
    ]
    if(state.wakeCount>10){
        for(int i = 10;i<state.wakeCount;i++) {
            def wakeDisplay = i.toInteger() + 1
            wakeList.add("Group 3 - Message ${wakeDisplay}")
        }
    }

	if(msgWakeitem == null) {
        MaxRandom3 = (wakeList.size() >= state.wakeCount ? wakeList.size() : state.wakeCount)
        LOGDEBUG("MaxRandom3 = $MaxRandom3") 
        def randomKey3 = new Random().nextInt(MaxRandom3)
        LOGDEBUG("randomKey3 = $randomKey3") 
        msgWake = wakeList[randomKey3]
    } else {
        msgWake = wakeList[msgWakeitem]
    }
    
    return msgWake
}


private getGroup4(msgPostitem) {
    def postList = [            
        "Hey! ,,," ,  
        "Information!,",
        "I thought you might like to know ,",
        "I'm sorry to disturb you but,",
        "Hey! ,, I thought you might like to know," ,
        "Take care %device%.",
        "Smell you later %device%.",
        "I thought %device% would never leave!",
        "AhhDEEOhs %device%",
        "Chow %device%.",
        "Oh revwahr %device%.",
        "Sighenara %device%.",
        "Tah Tah for now %device%.",
        "Catch you later %device%.",
        "To-da-loo %device%.",
        "See you in the funny papers %device%.",
        "Toodles %device%.",
        "Godspeed %device%.",
        "So long %device%.",
        "Ouf vee duhr zane %device%.",
        "Aloha %device%",
        "Shalom %device%",
        "Cheerio %device%",
        "AhrreevehDEHRtchee %device%",
        "Bye %device%.  Hope you have a %greeting%."
    ]  
    if(state.postCount1>25){
        for(int i = 25;i<state.postCount1;i++) {
            def postDisplay = i.toInteger() + 1
            postList.add("Group 4 - Message ${postDisplay}")            
        }
    }
    if(msgPostitem == null) {
        MaxRandom4 = (postList.size() >= state.postCount1 ? postList.size() : state.postCount1)
        LOGDEBUG("MaxRandom4 = $MaxRandom4") 
        def randomKey4 = new Random().nextInt(MaxRandom4)
        LOGDEBUG("randomKey4 = $randomKey4") 
        msgPost = postList[randomKey4]
    } else {
        msgPos1t = postList[msgPostitem]
    }

	return msgPost1
}

def checkAllow(){
    LOGDEBUG("Running Check Allow...")
    	LOGDEBUG("Calling.. ModeCheck")
    	modeCheck()
    	LOGDEBUG("Calling.. CheckTime")
		checkTime()
        LOGDEBUG("Calling.. CheckDay")
        checkDay()
        LOGDEBUG("Calling.. CheckPresence")
        checkPresence()
        LOGDEBUG("Calling.. CheckPresence1")
        checkPresence1()
    	LOGDEBUG("Calling.. SwitchRunCheck")
    	switchRunCheck()
 
if(state.modeCheck == false){
LOGDEBUG("Not in correct 'mode' to continue")
    }    
if(state.timeOK == false){
LOGDEBUG("Not enabled for this time so cannot continue")
}
if(state.dayCheck == false){    
LOGDEBUG("Not the correct day to continue")
}    
if(state.presenceRestriction ==  false || state.presenceRestriction1 ==  false){
LOGDEBUG( "Cannot continue - Presence failed")
}
if(state.appgo == false){
LOGDEBUG("$enableSwitch is off so cannot continue")
}

	if(state.appgo == true && state.dayCheck == true && state.presenceRestriction == true && state.presenceRestriction1 == true && state.modeCheck == true && state.timeOK == true){
		 state.allAllow = true 
   }
		else{
 		state.allAllow = false
		log.info "One or more restrictions apply - Unable to continue"
 log.debug "state.appgo = $state.appgo, state.dayCheck = $state.dayCheck, state.presenceRestriction = $state.presenceRestriction, state.presenceRestriction1 = $state.presenceRestriction1, state.modeCheck = $state.modeCheck, state.timeOK = $state.timeOK"
   }
}



def version(){
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
  
    
   
    
}

def display(){
    setDefaults()

	if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}}
    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){section(){input "updateBtn", "button", title: "$state.btnName"}}
    
	if(state.status != "Current"){section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>$state.updateURI</b><hr>"}}
	section(){input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false }
}


def checkButtons(){
    LOGDEBUG("Running checkButtons")
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
//    log.info "Resetting Update Button Name"
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
  log.info "Message = $newMessage " 
     state.msg1 = '[L]' + newMessage
	speakerUpdate.speak(state.msg1)
    }
}

def pauseOrNot(){
LOGDEBUG(" Calling 'pauseOrNot'...")
    state.pauseNow = pause1
        if(state.pauseNow == true){
            state.pauseApp = true
            if(app.label){
            if(app.label.contains('red')){
                log.warn "Paused"}
            else{app.updateLabel(app.label + ("<font color = 'red'> (Paused) </font>" ))
              LOGDEBUG("App Paused - state.pauseApp = $state.pauseApp ")   
                }
    
            }
        }
    
     if(state.pauseNow == false){
         state.pauseApp = false
         if(app.label){
     if(app.label.contains('red')){ app.updateLabel(app.label.minus("<font color = 'red'> (Paused) </font>" ))
     LOGDEBUG("App Released - state.pauseApp = $state.pauseApp ")                          
                                  }
         }
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
            state.updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
    
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
        inform()
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}


def setDefaults(){
  log.info "Initialising defaults..." 
  	checkButtons()
    resetBtnName()
    pauseOrNot()
    
    
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}                 
    if(state.multiVolumeSlots == null){state.multiVolumeSlots = false}    
    if(state.multiVolumeSlotsq == null){state.multiVolumeSlotsq = false}

}

def inform(){
	log.warn "An update is available - Telling the parent to send a pushover message to user..."
      parent.childUpdate(true,state.updateMsg) 
}


def setVersion(){
		state.version = "13.2.3"	 
		state.InternalName = "MessageCentralChild" 
    	state.ExternalName = "Message Central Child"
    	state.CobraAppCheck = "messagecentral.json"
}







