//Release History
//		1.0 May 20, 2016
//			Initial Release

// Ported to Hubitat 10/03/2018 - Cobra





metadata {
        definition (name: "Virtual Presence Plus", namespace: "Cobra", author: "Austin Pritchett") {
        capability "Switch"
 //       capability "Refresh"
        capability "Presence Sensor"
		capability "Sensor"
        
		command "arrived"
		command "departed"
    }
}

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

// handle commands
def arrived() {
	on()
}


def departed() {
    off()
}

def on() {
	sendEvent(name: "switch", value: "on")
    sendEvent(name: "presence", value: "present")

}

def off() {
	sendEvent(name: "switch", value: "off")
    sendEvent(name: "presence", value: "not present")

}