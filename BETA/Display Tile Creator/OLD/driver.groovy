/**
 *  ****************  Dashboard_Display  ****************
 *
 *  Copyright 2019 Andrew Parker
 *  
 *  This driver is free!
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this driver without a donation, but if you find it useful then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this driver better?  - Please let me know :)
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
 *  Last Update: 07/03/2019
 *
 *  Changes:
 *
 * 
 *
 *
 *  
 *  V1.0.0 - POC
 *
 */


metadata {
	
	definition (name: "Dashboard_Display", namespace: "Cobra", author: "AJ Parker") {
	
		capability "Sensor"
        command "line1a", ["string"]
		command "line1b", ["string"]
		command "line1c", ["string"]
		command "line2a", ["string"]
		command "line2b", ["string"]
		command "line2c", ["string"]
		command "line3a", ["string"]
		command "line3b", ["string"]
		command "line3c", ["string"]
		command "line4a", ["string"]
		command "line4b", ["string"]
		command "line4c", ["string"]
		command "line5a", ["string"]
		command "line5b", ["string"]
		command "line5c", ["string"]
		command "line6a", ["string"]
		command "line6b", ["string"]
		command "line6c", ["string"]
		command "line7a", ["string"]
		command "line7b", ["string"]
		command "line7c", ["string"]
		command "line8a", ["string"]
		command "line8b", ["string"]
		command "line8c", ["string"]
		command "LastDevice", ["string"]
		command "LastEvent", ["string"]
		command "refresh"
		command "clear"

		attribute "CustomDisplay", "string"
       
	}
   preferences() {
	   		input "fweight", "enum",  title: "Font Weight", submitOnChange: true, defaultValue: "Normal", options: ["Normal", "Italic", "Bold"]
			input "fcolour", "text",  title: "Font Colour (Hex Value)", defaultValue:"FFFFFF", submitOnChange: true
	   
   }	
	   
}


def refresh(){compile()}
def clear(){
	state.dashFormat = " "
	state.in1a = " "
	state.in2a = " "
	state.in3a = " "
	state.in4a = " "
	state.in5a = " "
	state.in6a = " "
	state.in7a = " "
	state.in8a = " "
	state.in1b = " "
	state.in2b = " "
	state.in3b = " "
	state.in4b = " "
	state.in5b = " "
	state.in6b = " "
	state.in7b = " "
	state.in8b = " "
	state.in1c = " "
	state.in2c = " "
	state.in3c = " "
	state.in4c = " "
	state.in5c = " "
	state.in6c = " "
	state.in7c = " "
	state.in8c = " "
	state.dashFormat = " "
	
	sendEvent(name: "CustomDisplay", value: state.dashFormat, isStateChange: true)
}

def compile() {
//	log.warn "compile"
	if(state.in1a == null){state.in1a = " "}
	if(state.in1b == null){state.in1b = " "}
	if(state.in1c == null){state.in1c = " "}
	if(state.in2a == null){state.in2a = " "}
	if(state.in2b == null){state.in2b = " "}
	if(state.in2c == null){state.in2c = " "}
	if(state.in3a == null){state.in3a = " "}
	if(state.in3b == null){state.in3b = " "}
	if(state.in3c == null){state.in3c = " "}
	if(state.in4a == null){state.in4a = " "}
	if(state.in4b == null){state.in4b = " "}
	if(state.in4c == null){state.in4c = " "}
	if(state.in5a == null){state.in5a = " "}
	if(state.in5b == null){state.in5b = " "}
	if(state.in5c == null){state.in5c = " "}
	if(state.in6a == null){state.in6a = " "}
	if(state.in6b == null){state.in6b = " "}
	if(state.in6c == null){state.in6c = " "}
	if(state.in7a == null){state.in7a = " "}
	if(state.in7b == null){state.in7b = " "}
	if(state.in7c == null){state.in7c = " "}
	if(state.in8a == null){state.in8a = " "}
	if(state.in8b == null){state.in8b = " "}
	if(state.in8c == null){state.in8c = " "}
 
	standardDash()
}




def standardDash(){
	setFont()
	state.dashFormat = ""
	state.dashFormat +="<div><br><font color = $state.fc>$state.fw1 ${state.in1a} ${state.in1b} ${state.in1c} $state.fw2</font><br></div>"
	state.dashFormat +="<div><font color = $state.fc>$state.fw1 ${state.in2a} ${state.in2b} ${state.in2c} $state.fw2</font><br></div>"
	state.dashFormat +="<div><font color = $state.fc>$state.fw1 ${state.in3a} ${state.in3b} ${state.in3c} $state.fw2</font><br></div>"
	state.dashFormat +="<div><font color = $state.fc>$state.fw1 ${state.in4a} ${state.in4b} ${state.in4c} $state.fw2</font><br></div>"
	state.dashFormat +="<div><font color = $state.fc>$state.fw1 ${state.in5a} ${state.in5b} ${state.in5c} $state.fw2</font><br></div>"
	state.dashFormat +="<div><font color = $state.fc>$state.fw1 ${state.in6a} ${state.in6b} ${state.in6c} $state.fw2</font><br></div>"
	state.dashFormat +="<div><font color = $state.fc>$state.fw1 ${state.in7a} ${state.in7b} ${state.in7c} $state.fw2</font><br></div>"
	state.dashFormat +="<div><font color = $state.fc>$state.fw1 ${state.in8a} ${state.in8b} ${state.in8c} $state.fw2</font></div>"
	sendEvent(name: "CustomDisplay", value: state.dashFormat, isStateChange: true)

	
	
}

def line1a(in1a) {state.in1a = in1a}
def line1b(in1b) {state.in1b = in1b}
def line1c(in1c) {state.in1c = in1c}
def line2a(in2a) {state.in2a = in2a}
def line2b(in2b) {state.in2b = in2b}
def line2c(in2c) {state.in2c = in2c}
def line3a(in3a) {state.in3a = in3a}
def line3b(in3b) {state.in3b = in3b}
def line3c(in3c) {state.in3c = in3c}
def line4a(in4a) {state.in4a = in4a}
def line4b(in4b) {state.in4b = in4b}
def line4c(in4c) {state.in4c = in4c}
def line5a(in5a) {state.in5a = in5a}
def line5b(in5b) {state.in5b = in5b}
def line5c(in5c) {state.in5c = in5c}
def line6a(in6a) {state.in6a = in6a}
def line6b(in6b) {state.in6b = in6b}
def line6c(in6c) {state.in6c = in6c}
def line7a(in7a) {state.in7a = in7a}
def line7b(in7b) {state.in7b = in7b}
def line7c(in7c) {state.in7c = in7c}
def line8a(in8a) {state.in8a = in8a}
def line8b(in8b) {state.in8b = in8b}
def line8c(in8c) {state.in8c = in8c}




	
def setFont(){
	dFontW()
	dFontC()

}
def dFontC(){state.fc = "${fcolour}"}
def dFontW(){
	if(fweight == "Normal"){
	state.fw1 = ""
	state.fw2 = ""}
	if(fweight == "Bold"){
	state.fw1 = "<b><strong>"
	state.fw2 = "</b></strong>"}
	if(fweight == "Italic"){
	state.fw1 = "<i>"
	state.fw2 = "</i>"}
}            











