/**
 * ****************  Roy's Music App  ****************
 *
 *  Design Usage:
 *	This was designed to force one or more switches/lights/dimmers to follow a single switch/light/dimmer 
 *
 *
 *  Copyright 2019 Andrew Parker
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
 *  Last Update: 12/02/2019
 *
 *  Changes:
 *
 *  V1.0.0 - POC 
 */
 
 
 
 

definition(
    name: "Roy's Music App Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: " ",
   
 parent: "Cobra:Roy's Music App",
    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )

preferences {
	section() {
	page name: "mainPage", title: "", install: true, uninstall: true
	
	}
}

def installed(){initialize()}
def updated(){initialize()}
def initialize(){
	
	subscribeNow()
	log.info "Initialised with settings: ${settings}"
//	logCheck()	
}
def subscribeNow() {
	unsubscribe()
	if(switch1){subscribe(switch1, "switch.on", switchHandler1)}
	if(switch2){subscribe(switch2, "switch.on", switchHandler2)}
	if(switch3){subscribe(switch3, "switch.on", switchHandler3)}
	if(switch4){subscribe(switch4, "switch.on", switchHandler4)}
	if(switch5){subscribe(switch5, "switch.on", switchHandler5)}
	if(switch6){subscribe(switch6, "switch.on", switchHandler6)}
	if(switch7){subscribe(switch7, "switch.on", switchHandler7)}
	if(switch8){subscribe(switch8, "switch.on", switchHandler8)}
	if(stopSwitch){subscribe(stopSwitch, "switch.on", switchHandlerStop)}

}




def mainPage() {
	dynamicPage(name: "mainPage") {

    section(){
		input "musicDevice", "capability.pushableButton", title: "Samsung Device", required: true, multiple: false
	}
	section(){
		
		input "stopSwitch",  "capability.switch", title: "Stop Switch", multiple: false
		
	}
	section(){	
		input "switch1",  "capability.switch", title: "Switch 1", multiple: false
		input "switch1n", "number", title: "Button Number To Send"
		
		input "switch2",  "capability.switch", title: "Switch 2", multiple: false
		input "switch2n", "number", title: "Button Number To Send"
		
		input "switch3",  "capability.switch", title: "Switch 3", multiple: false
		input "switch3n", "number", title: "Button Number To Send"
		
		input "switch4",  "capability.switch", title: "Switch 4", multiple: false
		input "switch4n", "number", title: "Button Number To Send"
		
		input "switch5",  "capability.switch", title: "Switch 5", multiple: false
		input "switch5n", "number", title: "Button Number To Send"
		
		input "switch6",  "capability.switch", title: "Switch 6", multiple: false
		input "switch6n", "number", title: "Button Number To Send"
		
		input "switch7",  "capability.switch", title: "Switch 7", multiple: false
		input "switch7n", "number", title: "Button Number To Send"
		
		input "switch8",  "capability.switch", title: "Switch 8", multiple: false
		input "switch8n", "number", title: "Button Number To Send"
		

		
		
	    } 
		
		section() {label title: "Enter a name for this automation", required: false}
 
	}
   
}    


def switchHandlerStop(evt){
	log.info "Stop Called"
	if(switch1){switch1.off()}
	if(switch2){switch2.off()}
	if(switch3){switch3.off()}
	if(switch4){switch4.off()}
	if(switch5){switch5.off()}
	if(switch6){switch6.off()}
	if(switch7){switch7.off()}
	if(switch8){switch8.off()}
	musicDevice.stop()
}

def switchHandler1(evt){
	log.info "Switch 1 Called"
	if(switch2){switch2.off()}
	if(switch3){switch3.off()}
	if(switch4){switch4.off()}
	if(switch5){switch5.off()}
	if(switch6){switch6.off()}
	if(switch7){switch7.off()}
	if(switch8){switch8.off()}
	if(stopSwitch){stopSwitch.off()}
	musicDevice.push(1)
}
def switchHandler2(evt){
	log.info "Switch 2 Called"
	if(switch1){switch1.off()}
	if(switch3){switch3.off()}
	if(switch4){switch4.off()}
	if(switch5){switch5.off()}
	if(switch6){switch6.off()}
	if(switch7){switch7.off()}
	if(switch8){switch8.off()}
	if(stopSwitch){stopSwitch.off()}
	musicDevice.push(2)
}

def switchHandler3(evt){
	log.info "Switch 3 Called"
	if(switch1){switch1.off()}
	if(switch2){switch2.off()}
	if(switch4){switch4.off()}
	if(switch5){switch5.off()}
	if(switch6){switch6.off()}
	if(switch7){switch7.off()}
	if(switch8){switch8.off()}
	if(stopSwitch){stopSwitch.off()}
	musicDevice.push(3)
}

def switchHandler4(evt){
	log.info "Switch 4 Called"
	if(switch1){switch1.off()}
	if(switch2){switch2.off()}
	if(switch3){switch3.off()}
	if(switch5){switch5.off()}
	if(switch6){switch6.off()}
	if(switch7){switch7.off()}
	if(switch8){switch8.off()}
	if(stopSwitch){stopSwitch.off()}
	musicDevice.push(4)
}

def switchHandler5(evt){
	log.info "Switch 5 Called"
	if(switch1){switch1.off()}
	if(switch2){switch2.off()}
	if(switch3){switch3.off()}
	if(switch4){switch4.off()}
	if(switch6){switch6.off()}
	if(switch7){switch7.off()}
	if(switch8){switch8.off()}
	if(stopSwitch){stopSwitch.off()}
	musicDevice.push(5)
}

def switchHandler6(evt){
	log.info "Switch 6 Called"
	if(switch1){switch1.off()}
	if(switch2){switch2.off()}
	if(switch3){switch3.off()}
	if(switch4){switch4.off()}
	if(switch5){switch5.off()}
	if(switch7){switch7.off()}
	if(switch8){switch8.off()}
	if(stopSwitch){stopSwitch.off()}
	musicDevice.push(6)
}

def switchHandler7(evt){
	log.info "Switch 7 Called"
	if(switch1){switch1.off()}
	if(switch2){switch2.off()}
	if(switch3){switch3.off()}
	if(switch4){switch4.off()}
	if(switch5){switch5.off()}
	if(switch6){switch6.off()}	
	if(switch8){switch8.off()}
	if(stopSwitch){stopSwitch.off()}
	musicDevice.push(7)
}

def switchHandler8(evt){
	log.info "Switch 8 Called"
	if(switch1){switch1.off()}
	if(switch2){switch2.off()}
	if(switch3){switch3.off()}
	if(switch4){switch4.off()}
	if(switch5){switch5.off()}
	if(switch6){switch6.off()}
	if(switch7){switch7.off()}
	if(stopSwitch){stopSwitch.off()}
	musicDevice.push(8)
}
















