/*
 *  Aeon HEM V2 (Gen5)
 *
 *  
 *	
 *
 */

// metadata
metadata {
	definition (name: "Aeon HEM 2nd Edition (Gen5)", namespace: "Cobra", author: "A J Parker") {
		capability "Energy Meter"
		capability "Power Meter"
		capability "Configuration"
		capability "Refresh"
		capability "Sensor"
		command "manualReset"
        
        attribute "LastReset", "string"
        attribute "NextReset", "string"
        attribute "voltage", "string"
        attribute "current", "string"
        attribute "cost", "string" 
        attribute "power1", "string"
        attribute "current1", "string" 
        attribute "power2", "string"
        attribute "current2", "string"
        
		fingerprint deviceId: "0x3101", inClusters: "0x98"
		fingerprint inClusters: "0x5E,0x86,0x72,0x32,0x56,0x60,0x70,0x59,0x85,0x7A,0x73,0xEF,0x5A", outClusters: "0x82"
        fingerprint deviceId: "0xC1", mfr: "0x86", inClusters: "0x70,0x32,0x60,0x85,0x56,0x72,0x86"
	}





	preferences {
		
		input "kWhCost", "string",
			title: "Cost per kWh",
			description: "Your Electric Bill Cost Per kWh",
			defaultValue: "0.13" as String,
			required: false,
			displayDuringSetup: true
		input "monitorInterval1", "integer",
			title: "Volts & kWh Report (Seconds)",
			description: "Interval for Volts & kWh Report",
			defaultValue: 120,
			range: "1..4294967295?",
			required: false,
			displayDuringSetup: true
		input "monitorInterval2", "integer",
			title: "Amps Report (Seconds)",
			description: "Interval for Amps Report",
			defaultValue: 120,
			range: "1..4294967295?",
			required: false,
			displayDuringSetup: true
		input "monitorInterval3", "integer",
			title: "Watts Report (Seconds)",
			description: "Interval for Watts Report",
			defaultValue: 5,
			range: "1..4294967295?",
			required: false,
			displayDuringSetup: true
        
       
        input "currencyFormat", "enum", title: "Currency Format", required: true, options: ["£", "\$","€"]
        input "resetInterval", "enum", title: "Auto Reset Interval", required: false, submitOnChange: true, defaultValue: "None", options: ["None", "Every Day (00:01)", "Every Week (Monday 00:01)", "Every Month (First day 00:01)", "Monthly (Custom Date)", "Every Year (First Day 00:01)"]
        if(resetInterval == "Monthly (Custom Date)"){
            input "date1", "enum", title: "What Date Every Month", required: true, options: [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"]
        }
        input "refreshInterval", "enum", title: "Auto Refresh Interval", required: true, defaultValue: "5 Minutes", options: ["Manual Refresh Only", "5 Minutes", "10 Minutes", "15 Minutes", "30 Minutes", "1 Hour", "3 Hours"]
	}

}

def updated(){
    unschedule()
	if (state.sec && !isConfigured()) {
		response(configure())
	}
    def refreshIntervalCmd = (settings?.refreshInterval ?: "5 Minutes").replace(" ", "")
    if(refreshInterval == "Manual Refresh Only"){log.info "MANUAL REFRESH ONLY"}
    else{ "runEvery${refreshIntervalCmd}"(refresh)}
    setReset()
    state.Currency = currencyFormat
}



def setReset(){
   def reset1 = resetInterval 
if(reset1) 
    if(reset1 == "None"){
        log.info " NO reset interval configured"
    }
    else if (reset1 == "Every Day (00:01)"){
        log.info "Reset configured for every day"
        schedule("0 1 0 1/1 * ? *", reset) 
    }
    else if (reset1 == "Every Week (Monday 00:01)"){
        log.info "Reset configured for every week"
        schedule("0 1 0 ? * MON *", reset)  
        
    }
    else if (reset1 == "Every Month (First day 00:01)"){
        log.info "Reset configured for every month"
        schedule("0 1 0 1 1/1 ? *", reset) 
    }
    else if (reset1 == "Every Year (First Day 00:01)"){
        log.info "Reset configured for every year"
        schedule("0 1 0 1 1 ? *", reset) 
    }

    else if(reset1 ==  "Monthly (Custom Date)"){
state.ResetDate = date1
        state.CronSchedule = "0 1 0 ${state.ResetDate} 1/1 ? *"
            // "0 ${state.selectedMin} ${state.selectedHour} ${state.selectedDate} ${state.runMonth} ? *"
        						
        schedule(state.CronSchedule, reset)
    }
}


def parse(String description){
	def result = null
	if (description.startsWith("Err 106")) {
		state.sec = 0
		result = createEvent( name: "secureInclusion", value: "failed", isStateChange: true,
			descriptionText: "This sensor failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.")
	} else if (description != "updated") {
		def cmd = zwave.parse(description, [0x32: 3, 0x56: 1, 0x59: 1, 0x5A: 1, 0x60: 3, 0x70: 1, 0x72: 2, 0x73: 1, 0x82: 1, 0x85: 2, 0x86: 2, 0x8E: 2, 0xEF: 1])
		if (cmd) {
			result = zwaveEvent(cmd)
		}
	}
//log.debug "Parsed '${description}' to ${result.inspect()}"
	return result
}

def zwaveEvent(hubitat.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
	def encapsulatedCommand = cmd.encapsulatedCommand([0x32: 3, 0x56: 1, 0x59: 1, 0x5A: 1, 0x60: 3, 0x70: 1, 0x72: 2, 0x73: 1, 0x82: 1, 0x85: 2, 0x86: 2, 0x8E: 2, 0xEF: 1])
	state.sec = 1
//log.debug "encapsulated: ${encapsulatedCommand}"
	if (encapsulatedCommand) {
		zwaveEvent(encapsulatedCommand)
	} else {
		log.warn "Unable to extract encapsulated cmd from $cmd"
		createEvent(descriptionText: cmd.toString())
	}
}

def zwaveEvent(hubitat.zwave.commands.securityv1.SecurityCommandsSupportedReport cmd) {
	response(configure())
}

def zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd) {
    log.debug "---CONFIGURATION REPORT V1--- ${device.displayName} parameter ${cmd.parameterNumber} with a byte size of ${cmd.size} is set to ${cmd.configurationValue}"
}

def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationReport cmd) {
    log.debug "---ASSOCIATION REPORT V2--- ${device.displayName} groupingIdentifier: ${cmd.groupingIdentifier}, maxNodesSupported: ${cmd.maxNodesSupported}, nodeId: ${cmd.nodeId}, reportsToFollow: ${cmd.reportsToFollow}"
}

def zwaveEvent(hubitat.zwave.commands.meterv3.MeterReport cmd) {
    def meterTypes = ["Unknown", "Electric", "Gas", "Water"]
    def electricNames = ["energy", "energy", "power", "count",  "voltage", "current", "powerFactor",  "unknown"]
    def electricUnits = ["kWh",    "kVAh",   "W",     "pulses", "V",       "A",       "Power Factor", ""]

    //NOTE ScaledPreviousMeterValue does not always contain a value
    def previousValue = cmd.scaledPreviousMeterValue ?: 0

    //Here is where all HEM polled values are defined. Scale(0-7) is in reference to the Aeon Labs HEM Gen5 data for kWh, kVAh, W, V, A, and M.S.T. respectively.
    //If scale 7 (M.S.T.) is polled, you would receive Scale2(0-1) which is kVar, and kVarh respectively. We are ignoring the Scale2 ranges in this device handler.
    def map = [ name: electricNames[cmd.scale], unit: electricUnits[cmd.scale], displayed: state.display]
    switch(cmd.scale) {
        case 0: //kWh
						previousValue = device.currentValue("energy") ?: cmd.scaledPreviousMeterValue ?: 0
						BigDecimal costDecimal = cmd.scaledMeterValue * (kWhCost as BigDecimal)
						def costDisplay = String.format("%5.2f",costDecimal)
        sendEvent(name: "cost", value: costDisplay, unit: "${state.Currency}") //, descriptionText: "Display Cost: ${costDisp}")
						map.value = cmd.scaledMeterValue
            break;
        case 1: //kVAh (not used in the U.S.)
            map.value = cmd.scaledMeterValue
            break;
        case 2: //Watts
            previousValue = device.currentValue("power") ?: cmd.scaledPreviousMeterValue ?: 0
            map.value = Math.round(cmd.scaledMeterValue)
            break;
        case 3: //pulses
						map.value = Math.round(cmd.scaledMeterValue)
            break;
        case 4: //Volts
            previousValue = device.currentValue("voltage") ?: cmd.scaledPreviousMeterValue ?: 0
            map.value = cmd.scaledMeterValue
            break;
        case 5: //Amps
            previousValue = device.currentValue("current") ?: cmd.scaledPreviousMeterValue ?: 0
            map.value = cmd.scaledMeterValue
            break;
        case 6: //Power Factor
        case 7: //Scale2 values (not currently implimented or needed)
            map.value = cmd.scaledMeterValue
            break;
        default:
            break;
    }
createEvent(map)
}

def zwaveEvent(hubitat.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
	//This is where the HEM clamp1 and clamp2 (subdevice) report values are defined. Scale(2,5) is in reference to the Aeon Labs HEM Gen5 (subdevice) data for W, and A respectively.
	//Z-Wave Command Class 0x60 (multichannelv3) is necessary to interpret the subdevice data from the HEM clamps.
	//In addition, "cmd.commandClass == 50" and "encapsulatedCommand([0x30: 1, 0x31: 1])" below is necessary to properly receive and inturpret the encasulated subdevice data sent to the SmartThings hub by the HEM.
	//The numbered "command class" references: 50, 0x30v1, and 0x31v1 do not seem to be true Z-Wave Command Classes and any correlation is seemingly coincidental.
	//It should also be noted that without the above, the data received will not be processed here under the 0x60 (multichannelv3) command class and you will see unhandled messages from the HEM along with references to command class 50 as well as Meter Types 33, and 161.
	//sourceEndPoint 1, and 2 are the Clamps 1, and 2.
	def dispValue
	def newValue
	def formattedValue
	def MAX_AMPS = 220
	def MAX_WATTS = 24000
	if (cmd.commandClass == 50) { //50 is likely a manufacturer specific code, Z-Wave specifies this as a "Basic Window Covering" so it's not a true Z-Wave Command Class.   
		def encapsulatedCommand = cmd.encapsulatedCommand([0x30: 1, 0x31: 1]) // The documentation on working with Z-Wave subdevices and the technical specs from Aeon Labs do not explain this adequately, but it's necessary.
	//log.debug ("Command from endpoint ${cmd.sourceEndPoint}: ${encapsulatedCommand}")
		if (encapsulatedCommand) {
			if (cmd.sourceEndPoint == 1) {
				if (encapsulatedCommand.scale == 2 ) {
						newValue = Math.round(encapsulatedCommand.scaledMeterValue)
	                    if (newValue > MAX_WATTS) { return }
						formattedValue = newValue
						dispValue = "${formattedValue}"
						sendEvent(name: "power1", value: dispValue as String, unit: "", descriptionText: "L1 Power: ${formattedValue} Watts")
				}
				if (encapsulatedCommand.scale == 5 ) {
						newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
	                    if (newValue > MAX_AMPS) { return }
						formattedValue = String.format("%5.2f", newValue)
						dispValue = "${formattedValue}"
						sendEvent(name: "current1", value: dispValue as String, unit: "", descriptionText: "L1 Current: ${formattedValue} Amps")
				}
			}
			else if (cmd.sourceEndPoint == 2) {
				if (encapsulatedCommand.scale == 2 ) {
						newValue = Math.round(encapsulatedCommand.scaledMeterValue)
	                    if (newValue > MAX_WATTS) { return }
						formattedValue = newValue
						dispValue = "${formattedValue}"
						sendEvent(name: "power2", value: dispValue as String, unit: "", descriptionText: "L2 Power: ${formattedValue} Watts")
				}
				if (encapsulatedCommand.scale == 5 ) {
						newValue = Math.round(encapsulatedCommand.scaledMeterValue * 100) / 100
	                    if (newValue > MAX_AMPS) { return }
						formattedValue = String.format("%5.2f", newValue)
						dispValue = "${formattedValue}"
						sendEvent(name: "current2", value: dispValue as String, unit: "", descriptionText: "L2 Current: ${formattedValue} Amps")
				}
			}
		}
	}
}

def zwaveEvent(hubitat.zwave.Command cmd) {
	//This will log any unhandled command output to the debug window.
	log.debug "Unhandled: $cmd"
    createEvent(descriptionText: cmd.toString(), isStateChange: false)
}

def refresh() {
	def request = [
	//This is where the action "refresh" is defined. Refresh is very basic. It simply gets and displays the latest values from the HEM exclusive of the clamp subdevices.
		zwave.meterV3.meterGet(scale: 0),	//kWh
		zwave.meterV3.meterGet(scale: 2),	//Wattage
		zwave.meterV3.meterGet(scale: 4),	//Volts
		zwave.meterV3.meterGet(scale: 5),	//Amps
	]
	commands(request)
}

def manualReset(){reset()}


def reset() {
	
	//Tapping reset will send the meter reset command to HEM and zero out the kWh data so you can start fresh.
	//This will also clear the cost data and reset the last reset timestamp. Finally it will poll for latest values from the HEM.
	//This has no impact on Pole1 or Pole2 (clamp1 and clamp2 subdevice) tile data as that is sent via reports from the HEM.
  
    
 //   Define the date format (Default is d/M/YY - UK)
    
 //    def dateString = new Date().format("M/d/YY", location.timeZone)
  
    def dateString = new Date().format("d/M/YY", location.timeZone)

	def timeString = new Date().format("h:mm a", location.timeZone)    
	state.LastReset = dateString+" @ "+timeString
	sendEvent(name: "LastReset", value: state.LastReset)	
	def request = [
		zwave.meterV3.meterReset(),
		zwave.meterV3.meterGet(scale: 0),	//kWh
		zwave.meterV3.meterGet(scale: 2),	//Wattage
		zwave.meterV3.meterGet(scale: 4),	//Volts
		zwave.meterV3.meterGet(scale: 5),	//Amps
	]
	commands(request)
}

def configure() {
	//This is where the tile action "configure" is defined. Configure resends the configuration commands below (using the variables set by the preferences section above) to the HEM Gen5 device.
	//If you're watching the debug log when you tap configure, you should see the full configuration report come back slowly over about a minute.
	//If you don't see the full configuration report (seven messages) followed by the association report, tap configure again.
	def monitorInt1 = 60
		if (monitorInterval1) {
			monitorInt1=monitorInterval1.toInteger()
		}
	def monitorInt2 = 30
		if (monitorInterval2) {
			monitorInt2=monitorInterval2.toInteger()
		}
	def monitorInt3 = 6
		if (monitorInterval3) {
			monitorInt3=monitorInterval3.toInteger()
	}
	log.debug "Sending configure commands - kWhCost '${kWhCost}', monitorInterval1 '${monitorInt1}', monitorInterval2 '${monitorInt2}', monitorInterval3 '${monitorInt3}'"
	def request = [
		// Reset switch configuration to defaults.
	//zwave.configurationV1.configurationSet(parameterNumber: 255, size: 1, scaledConfigurationValue: 1),
		// Disable selective reporting, so always update based on schedule below <set to 1 to reduce network traffic>.
		zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, scaledConfigurationValue: 1),
		// (DISABLED by first option) Don't send unless watts have changed by 50 <default>.
		zwave.configurationV1.configurationSet(parameterNumber: 4, size: 2, scaledConfigurationValue: 10),
		// (DISABLED by first option) Or by 10% <default>.
		zwave.configurationV1.configurationSet(parameterNumber: 8, size: 1, scaledConfigurationValue: 5),

		// Which reports need to send in Report group 1.
		zwave.configurationV1.configurationSet(parameterNumber: 101, size: 4, scaledConfigurationValue: 6149),
		// Which reports need to send in Report group 2.
		zwave.configurationV1.configurationSet(parameterNumber: 102, size: 4, scaledConfigurationValue: 1572872),
		// Which reports need to send in Report group 3.
		zwave.configurationV1.configurationSet(parameterNumber: 103, size: 4, scaledConfigurationValue: 770),
		// Interval to send Report group 1.
		zwave.configurationV1.configurationSet(parameterNumber: 111, size: 4, scaledConfigurationValue: monitorInt1),
		// Interval to send Report group 2.
		zwave.configurationV1.configurationSet(parameterNumber: 112, size: 4, scaledConfigurationValue: monitorInt2),
		// Interval to send Report group 3.
		zwave.configurationV1.configurationSet(parameterNumber: 113, size: 4, scaledConfigurationValue: monitorInt3),

		// Report which configuration commands were sent to and received by the HEM Gen5 successfully.
		zwave.configurationV1.configurationGet(parameterNumber: 3),
		zwave.configurationV1.configurationGet(parameterNumber: 4),
		zwave.configurationV1.configurationGet(parameterNumber: 8),
		zwave.configurationV1.configurationGet(parameterNumber: 101),
		zwave.configurationV1.configurationGet(parameterNumber: 102),
		zwave.configurationV1.configurationGet(parameterNumber: 103),
		zwave.configurationV1.configurationGet(parameterNumber: 111),
		zwave.configurationV1.configurationGet(parameterNumber: 112),
		zwave.configurationV1.configurationGet(parameterNumber: 113),
		zwave.associationV2.associationGet(groupingIdentifier: 1)
	]
	commands(request)
}

private setConfigured() {
	updateDataValue("configured", "true")
}

private isConfigured() {
	getDataValue("configured") == "true"
}

private command(hubitat.zwave.Command cmd) {
	if (state.sec) {
		zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
	} else {
		cmd.format()
	}
}

private commands(commands, delay=500) {
	delayBetween(commands.collect{ command(it) }, delay)
}