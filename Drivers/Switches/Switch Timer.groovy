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

    definition (name: "Switch Timer", namespace: "Cobra", author: "AJ Parker") {
		capability "Switch"


	}


preferences {
		
		input(name: "delayNum", type: "number", title:"Delay before on/off", required: true, defaultValue: 0)	
    	input(name: "delayType", type: "bool", title: "Default State ON or OFF ", required: true, defaultValue: false)
		input(name: "minSec", type: "bool", title: "Off = Seconds - On = Minutes", required: true, defaultValue: false)
	}
}


def on() {
checkDelay()
    if(delayType == false){
    log.debug "$version - ON"
	sendEvent(name: "switch", value: "on")
    log.debug "Turning off in $state.delay1 seconds"
    runIn(state.delay1, off,[overwrite: false])
    }
    
    if(delayType == true){
    log.debug "$version - ON"
	sendEvent(name: "switch", value: "on")
    
    }
}

def off() {
     if(delayType == false){
	log.debug "$version - OFF"
	sendEvent(name: "switch", value: "off")
     }
    
    if(delayType == true){
	log.debug "$version - OFF"
	sendEvent(name: "switch", value: "off")
    runIn(state.delay1, on,[overwrite: false])
     }
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
	"Switch Timer Version 1.0"
}

