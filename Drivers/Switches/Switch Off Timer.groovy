/**
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
 *
 */


metadata {

    definition (name: "Switch Off Timer", namespace: "Cobra", author: "AJ Parker") {
		capability "Switch"
        capability "Momentary"
        
 //       capability "Relay Switch"
//		  capability "Sensor"
//		  capability "Actuator"


	}


preferences {
		
		input(name: "delayNum", type: "number", title:"Delay before 'Off'", required: true, defaultValue: 0)	
		input(name: "minSec", type: "bool", title: "Off = Seconds - On = Minutes", required: true, defaultValue: false)
	}
}


def on() {
checkDelay()
	log.debug "$version - ON"
	sendEvent(name: "switch", value: "on")
    log.debug "Turning off in $state.delay1 seconds"
    runIn(state.delay1, off,[overwrite: false])
}

def off() {
	log.debug "$version - OFF"
	sendEvent(name: "switch", value: "off")
}

def checkDelay(){
    if (minSec == true) {
    state.delay1 = 60 * delayNum as int    
        
    }
    
    else{
      state.delay1 = delayNum as int   
        
    }
    
    
}



private getVersion() {
	"Switch Off Timer Version 1.0"
}

