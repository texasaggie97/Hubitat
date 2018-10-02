/**
* Candle Flicker
*
* Copyright 2015 Kristopher Kubicki
*
*  Ported and adapted by @Cobra
*
*
*  V1.0.0 - POC
*
*
*/
definition(
	name: "Random Dim & Flicker",
	namespace: "Cobra",
	author: "kristopher@acm.org",
	description: "Randomly dim or 'flicker' lights",
	category: "My Apps",
	iconUrl: "",
	iconX2Url: "")

	preferences {
		section("Select Dimmable Lights...") {
		input "dimmers", "capability.switchLevel", title: "Lights", required: true, multiple: true
	}

	section("Activate the flicker when this switch is on...") {
		input "switches", "capability.switch", title: "Switch", required: true, multiple: false
	}
}


def installed() {
	initialize()
}

def updated() {	
	initialize()
}

def initialize() {
	unsubscribe()
	unschedule() 
	subscribe(switches, "switch.on", eventHandler)
}


def eventHandler(evt) {
	if(switches.currentValue("switch") == "on") {
		for (dimmer in dimmers) {      
               	def lowLevel= Math.abs(new Random().nextInt() % 3) + 59
                	def upLevel= Math.abs(new Random().nextInt() % 10) + 90
                	def upDelay = Math.abs(new Random().nextInt() % 10000)
                	def lowDelay = upDelay + Math.abs(new Random().nextInt() % 5000)
            		
           
   //     def upLevel = 90
  //      def lowLevel = 10    
        log.warn "low: $lowLevel $lowDelay high: $upLevel $upDelay"    
			dimmer.setLevel(upLevel,[delay: upDelay])
            state.sleepTime = Math.abs(new Random().nextInt() % 10000)
        	pause(state.sleepTime)
            dimmer.setLevel(lowLevel,[delay: lowDelay])
            
        	}
        	state.sleepTime = Math.abs(new Random().nextInt() % 10000)
        	pause(state.sleepTime)
        	runIn(5,"eventHandler")
	}
}