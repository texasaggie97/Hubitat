/**
 * 
 *  Driver for Chinese 'Zigbee ONOff Switch'
 *  made by 'SZ' this is actually sold as a 'Lamp_01' model 
 *  but is, in effect, a zigbee relay switch
 *
 *  Hacked together and cleaned up from a driver example by Robin Winbourne
 *
 *  Cobra
 *
 *
 *	
 */


metadata {
    definition (name: "Zigbee OnOff Switch", namespace: "Cobra", author: "Andrew Parker") {

        capability "Configuration"
        capability "Switch"
		command "toggle"

	fingerprint profileId: "0104", inClusters: "0000, 0003, 0004, 0005, 0006", outClusters: "0000", manufacturer: "SZ", model: "Lamp_01", deviceJoinName: "Zigbee OnOff Switch"
    }
}


preferences {
	
		input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
        input name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true
	
}



def initialize(){
	configure()
	
}

def logsOff(){
    log.warn "debug logging disabled..."
    device.updateSetting("logEnable",[value:"false",type:"bool"])
}

def updated(){
log.info "updated..."
    log.warn "debug logging is: ${logEnable == true}"
    log.warn "description logging is: ${txtEnable == true}"
    if (logEnable) runIn(1800,logsOff)
}



def parse(String description) {
if (logEnable) log.debug "description is $description"
    def event = zigbee.getEvent(description)
    if (event) {
        sendEvent(event)
		 if (txtEnable) log.info "Switching $event.value"
		state.onoffNow = event.value
if (logEnable) log.debug " evt = $state.onoffNow" // debug testing
    }
    else {
         if (txtEnable) log.info "Unhandled data from device : $description (Nothing to worry about here)"
      def mydata = (zigbee.parseDescriptionAsMap(description))
if (logEnable) log.debug "$mydata"

    }
}


def toggle(){
	 if (txtEnable) log.info "Toggle called..."
	if (state.onoffNow == 'on'){
		 if (logEnable) log.debug "Switch is currently on so I'm turning it off"
		off()	
	}
	else{
		 if (logEnable) log.debug "Switch is currently off so I'm turning it on"
		on()
		
	}
}



def off() {
    zigbee.off()
}

def on() {
    zigbee.on()
}


def configure() {
     if (logEnable) log.debug "Configure"
	zigbee.onOffConfig() + zigbee.onOffRefresh()
}