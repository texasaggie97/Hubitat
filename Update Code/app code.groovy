

// Code snippit version 1.0



Add to 'Preferences' wherever you want the version and update information to be displayed
---------------------------------------------------------------------------------------------

	display()

---------------------


Add to 'Initialise' or 'Update'
-----------------------------------

	version()

---------------------






Add to bottom of file and amend version and internal name as nesseccary
------------------------------------------------------------------------



def version(){
	unschedule()
	schedule("0 0 9 ? * FRI *", updateCheck) // Cron schedule - How often to perform the update check - (This example is 9am every Friday)
	updateCheck()  
}

def display(){
	if(state.Status){
	section{paragraph "Version: $state.version -  $state.Copyright"}
        
	if(state.Status != "Current"){
	section{ 
	paragraph "$state.Status"
	paragraph "$state.UpdateInfo"
    }
    }
}
}


def updateCheck(){
    setVersion()
	def paramsUD = [uri: "http://YourDomain.com/versions.json"]   // This is the URI & path to your hosted JSON file
       	try {
        httpGet(paramsUD) { respUD ->
 //  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.Status = "<b>** This app is no longer supported by $state.author  **</b> (But you may continue to use it)"       
            log.warn "** This app is no longer supported by $state.author **"      
      		}           
		else if(currentVer < newVer){
        	state.Status = "<b>New Version Available (Version: $newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn "** $state.UpdateInfo **"
       		} 
		else{ 
      		state.Status = "Current"
      		log.info "You are using the current version of this app"
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
   		
    
    	//	
}

def setVersion(){
		state.version = "1.0.0"	 // Version number of this app
		state.InternalName = "ExampleApp1"   // this is the name used in the JSON file for this app
}

