import groovyx.net.http.RESTClient
import groovy.json.*
import static groovyx.net.http.ContentType.*

RESTClient paasRestClient = new RESTClient("http://paas1.attplatform.com/");

/*
 * Object name: ATTAssetIsFixed
 *
 * Script is responsible for propagating information about fixed device to Long Jump.
 *
 * Parameters:
 *
 * serial - Device's serial number.
 */

// Credentials for AT&T platform
String username = "1347644721";
String password = "e21fe58b26048f43bb3b7ebdbf4cc918";

try {
  //Login to AT&T platform request
  String loginXmlRequest = "<platform><login><userName>"+username+"</userName><password>"+password+"</password></login></platform>"

  def loginResponse = paasRestClient.post(path: "/networking/rest/login",
      requestContentType: XML,
      contentType: JSON,
      body: loginXmlRequest);
  
  // Identifier of current session
  def sessionId = loginResponse.getData().platform.login.sessionId
  
  // Parameters of broken Asset
  def serial = parameters.serial
  
  // Find active alert for Asset
  def filter = "device_id contains '" + serial + "' AND status != 'DONE'";
      
  def searchResponse = paasRestClient.get(path: "/networking/rest/record/Alerts",
    requestContentType: JSON,
    contentType: JSON,
    params : [fieldList : "id,device_id,status", filter : filter],
    headers: [Cookie : "JSESSIONID=" + sessionId]);
  
  def data = searchResponse.getData()
  
  def update = { recordId ->
      updateReq = "<platform><record><status>DONE</status></record></platform>"

      paasRestClient.put(path: "/networking/rest/record/Alerts/" + recordId,
        requestContentType: XML,
        contentType: JSON,
        body: updateReq,
        headers: [Cookie : "JSESSIONID=" + sessionId]);
  }
  
  if (data.platform.record != null) {
    if (data.platform.record[0] == null) {
        update(data.platform.record.id)
    } else {
      data.platform.record.id.each{
        update(it.value)
      }
    }
  }
  
  // Logout request
  paasRestClient.get(path: "/networking/rest/logout",
      requestContentType: URLENC,
      contentType: XML,
      headers: [Cookie : "JSESSIONID=" + sessionId]);
  
} catch(Exception e){
  logger.error(e.getMessage());
}
