/**
 *  Irrigation Switch
 *  This was created to use either as a switch to water the garden or as a switch to 'announce' that the garden needs water.
 *
 *  Copyright 2018 Andrew Parker
 *  Concept, testing, and help debugging, was provided by @CAL.Hub when we initially used a driver to perform this task.
 *  With Weather Underground about to stop access to their API I created an app to do the job instead so people can use whichever driver they wish
 *  (Providing the correct atrtributes are sent by the driver)
 *  
 *  This SmartApp is free!
 *
 *  Donations to support development efforts are welcomed via: 
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
 *
 *  V1.1.0 - added additional logging and remote version checking
 *  V1.0.0 - POC - Initial concept proof
 *
 */

definition(
    name: "Irrigation Switch",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Turn on a switch when there is insufficient rain so that the garden requires watering",
    category: "",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")


preferences {
     page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
     page name: "restrictionsPage", title: "Final Page", install: true, uninstall: true
}

// main page *****************************************************************************************************************
  def mainPage() {
    dynamicPage(name: "mainPage") {  
        
     display()
        
        
	section("Input/Output") {
    input(name: "enableswitch1", type: "capability.switch", title: "Enable/Disable app with this switch", required: false, multiple: false)
	input(name: "sensor1", type: "capability.sensor", title: "Weather Device", required: true, multiple: false)
    input(name: "sensorswitch1", type: "capability.switch", title: "Switch to control", required: false, multiple: false)
    input "goNogo", "decimal", required: true, title: " Switch on if calculation below" 
    input "dbyWeight", "enum", required: true, title: "Weighting: The Day Before Yesterday",  options: ["1", "2", "3", "4", "5"]
    input "yWeight", "enum", required: true, title: "Weighting: Yesterday",  options: ["1", "2", "3", "4", "5"]
    input "tdWeight", "enum", required: true, title: "Weighting: Today",  options: ["1", "2", "3", "4", "5"]
    input "tmWeight", "enum", required: true, title: "Weighting: Tomorrow",  options: ["1", "2", "3", "4", "5"]
    input "datWeight", "enum", required: true, title: "Weighting: The Day After Tomorrow",  options: ["1", "2", "3", "4", "5"]
 	input "cutOff", "time", title: "What Time To Change Day", required: true
    input "checkTime", "time", title: "Check Criteria Time", required: true
    input "runTime", "time", title: "Time to switch ON ", required: true
    input "stopTime", "time", title: "Time to switch OFF ", required: true    
        
        
	}
        
    }
  }

 
// Restrictions Page *********************************************************************************************************
 def restrictionsPage() {
       dynamicPage(name: "restrictionsPage") {
           
           section() {
           		mode title: "Run only when in specific mode(s) ", required: false
            }
        
       
     	section() {
      input "days", "enum", title: "Select Days of the Week", required: false, multiple: true, options: ["Monday": "Monday", "Tuesday": "Tuesday", "Wednesday": "Wednesday", "Thursday": "Thursday", "Friday": "Friday", "Saturday": "Saturday", "Sunday": "Sunday"]
    		}      
     
           
       
       section() {
                label title: "Enter a name for this automation ", required: false
            }
      section() {
            input "debugMode", "bool", title: "Enable logging", required: true, defaultValue: false
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
    version()
    logCheck()
   
     state.enablecurrS1 = 'on'
     def changeOver = cutOff
     schedule(changeOver, createHistory)
     def processTime = checkTime
     schedule(processTime, processNow)
     schedule(runTime, runNow)
     schedule(stopTime, offNow)    
     subscribe(sensor1, "precip_today", irrigationCollect1)
     subscribe(sensor1, "rainTomorrow", irrigationCollect2)
     subscribe(sensor1, "rainDayAfterTomorrow",  irrigationCollect3)
     state.rainTmp3 = 0  // default for rain the day before yesterday
	 state.rainTmp2 = 0  // default for rain yesterday
	 state.rainTmp1 = 0  // default for rain today
     state.rainTmp4 = 0  // default for rain tomorrow
     state.rainTmp5 = 0  // default for rain the day after tomorrow
     state.finalCalc = 0 // default for final calculation
    LOGDEBUG("Changeover time = $changeOver")
    
    
    
}




def irrigationCollect1(evt){
 state.rainTmp1 = evt.value    
  LOGDEBUG("Day before yesterday's rain = $state.rainTmp3")
  LOGDEBUG("Yesterday's rain = $state.rainTmp2")  
  LOGDEBUG("Today's rain = $state.rainTmp1")  
  
  
    
    
}
def irrigationCollect2(evt){
 state.rainTmp4 = evt.value   
LOGDEBUG("Tomorrow's rain = $state.rainTmp4" )    
    
}
def irrigationCollect3(evt){
state.rainTmp5 = evt.value      
 LOGDEBUG("The Day After Tomorrow's rain = $state.rainTmp5" )    
    
}


def  processNow(evt){
    LOGDEBUG("Calling processNow")
    LOGDEBUG("Calling.. CheckDay")
	checkDay()
    if(state.dayCheck == true){
     calculateFinal()
        
    }    
}

def calculateFinal(){
    log.info " Running calculations for today... "
    
    /** 
Day1 = Today
Day2 = Yesterday
Day3 = The Day Before Yesterday
Day4 = Tomorrow
Day5 = Day After Tomorrow
    
    */
    
 
    def dbyWeight1 = dbyWeight.toFloat()
    def yWeight1 = yWeight.toFloat()
    def tdWeight1 = tdWeight.toFloat()
    def tmWeight1 = tmWeight.toFloat()
    def datWeight1 = datWeight.toFloat()
    def goNogo1 = goNogo.toDouble()
    def day1Stat = state.rainTmp1.toDouble()
    def day2Stat = state.rainTmp2.toDouble()
    def day3Stat = state.rainTmp3.toDouble()
    def day4Stat = state.rainTmp4.toDouble()
    def day5Stat = state.rainTmp5.toDouble()
    
    
	
    def day1Calc = (day1Stat * tdWeight1)
    def day2Calc = (day2Stat * yWeight1)
    def day3Calc = (day3Stat * dbyWeight1)
    def day4Calc = (day4Stat * tmWeight1)
    def day5Calc = (day5Stat * datWeight1)
    
    LOGDEBUG(" day1Calc = $day1Calc -- day2Calc = $day2Calc -- day3Calc = $day3Calc -- day4Calc = $day4Calc -- day5Calc = $day5Calc ")

    state.finalCalc = (day1Calc += day2Calc += day3Calc += day4Calc += day5Calc)
   LOGDEBUG(" Final Calculation = $state.finalCalc")
    
    if(state.finalCalc < goNogo1 || finalCalc == goNogo1 ){
         LOGDEBUG(" Final Calculation is less than: $goNogo1 - switch will turn on today")
       state.canRun = true
    }
    else{
       LOGDEBUG(" Final Calculation is greater than or equal to: $goNogo1 - switch will not turn on today")
       state.canRun = false
    }

}




def createHistory(evt){
    log.info "Calling CreatHistory"
state.rainTmp3 = state.rainTmp2
state.rainTmp2 = state.rainTmp1
    
    LOGDEBUG("Day Before Yesterday's rain = $state.rainTmp3" )    
    LOGDEBUG("Yesterday's rain = $state.rainTmp2" )   
    LOGDEBUG("Today's rain = $state.rainTmp1" )   
    LOGDEBUG("Tomorrow's rain = $state.rainTmp4" )   
    LOGDEBUG("Day After Tomorrow's rain = $state.rainTmp5" )   


}

def runNow(evt){
   

    if(state.canRun == true){
    log.info " Not enough rain so turning on switch"
    IRon()    
		}
    else{
        log.info " No need to run at this time - There has already been enough rain."
		}
	}    

def IRon() {
 log.debug "****************** ON **********************"  
sensorswitch1.on()
    
}

def IRoff() {
 log.debug "******************  OFF *********************"   
sensorswitch1.off()
    
}
def offNow() {
 log.debug "******************  OFF *********************"   
sensorswitch1.off()
    
}


def enableSwitch1Handler(evt){
state.enablecurrS1 = evt.value
LOGDEBUG("$enableswitch1 is $state.enablecurrS1")
    if(state.enablecurrS1 == 'off'){
    LOGDEBUG("App disabled... Switching off")  
        sensorswitch1.off()
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
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (Version: ${state.appversion}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}




// Check Version   *********************************************************************************
def version(){
    cobra()
    if (state.Type == "Application"){
    schedule("0 0 14 ? * FRI *", cobra)
    }
    if (state.Type == "Driver"){
    schedule("0 45 16 ? * MON *", cobra)
    }
}

def display(){
    
    section{
            paragraph "Version Status: $state.Status"
			paragraph "Current Version: $state.version -  $state.Copyright"
			}

}


def cobra(){
    
    setVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/cobra.json"]
       try {
        httpGet(paramsUD) { respUD ->
 //  log.info " Version Checking - Response Data: ${respUD.data}"
       def copyNow = (respUD.data.copyright)
       state.Copyright = copyNow
            def newver = (respUD.data.versions.(state.Type).(state.InternalName))
            def cobraVer = (respUD.data.versions.(state.Type).(state.InternalName).replace(".", ""))
       def cobraOld = state.version.replace(".", "")
       if(cobraOld < cobraVer){
		state.Status = "<b>** New Version Available (Version: $newver) **</b>"
           log.warn "** There is a newer version of this $state.Type available  (Version: $newver) **"
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



 
// App Version   *********************************************************************************
def setVersion(){
     state.version = "1.1.0"
     state.InternalName = "Iswitch"
     state.Type = "Application"


}

