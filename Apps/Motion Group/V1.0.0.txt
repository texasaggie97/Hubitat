/**
 *  Motion Group 
 *
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
 *
 *
 *
 *  V1.0.0 - POC
 *
 */



definition(
    name: "Motion Group",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Takes input of a number of motion sensors and controls a virtual sensor",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Title") {
				input "motion1", "capability.motionSensor", title: "Select Motion Sensor", required: true, multiple: true 
                input "simMotion1", "capability.motionSensor", title: "Select Simulated Motion Sensor", required: true, multiple: false 
                input "delay1", "number", title: "Delay after movement stops to stop virtual motion", description: "Minutes", required: true
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
	subscribe(motion1, "motion", motionHandler1)
}



def motionHandler1 (evt){
def activity = evt.value
def activeNow = motion1.findAll { it?.latestValue("motion") == 'active' }
		if (activeNow) { 
       state.go = 'go'
log.info "Active Sensors: ${activeNow.join(' ')}"
  log.info "Active now"          
simMotion1.active()
}



    def inActiveNow = motion1.findAll { it?.latestValue("motion") == "inactive"}

		if (inActiveNow) { 
log.debug "All Inactive: ${inActiveNow}"
state.go = 'stop'
def myDelay = 60 * delay1
            log.info " Waiting $myDelay seconds before going inactive (If no further motion)"
runIn(myDelay, off)
	}

}

def off(){
if (state.go == 'stop'){
    log.info "Inactive now"
simMotion1.inactive()
	}
}
            