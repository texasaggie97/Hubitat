/**
 *  ****************  Test Device  ****************
 *
 *	
 *  Design Usage:
 *  This was designed to be used with 'Temperature Coltrolled Switch' smartapp
 *  Creating a virtual device with this driver enables the app to set the required temperature 'on the fly'
 *
 *  Copyright 2017 Andrew Parker
 *  
 *  This DTH is free!
 *  Donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  I'm very happy for you to use this DTH without a donation, but if you find it useful then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this DTH better?  - Please let me know :)
 *
 *  Website: http://securendpoint.com/smartthings
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
 *  Created: 14/10/2018
 *  Changes:
 *  V1.0.0 - POC
 *
 */


metadata {
	
	definition (name: "Test Device", namespace: "Cobra", author: "AJ Parker") {
		
		capability "Sensor"
        command "sendAttribute", ["attribute", "value"]
       
	}

}



def sendAttribute(attribute, value) {
    state.Attribute = attribute
    state.Value = value
	sendEvent(name:state.Attribute, value: state.Value)
}



