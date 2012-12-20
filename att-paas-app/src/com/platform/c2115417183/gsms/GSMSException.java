/* Licensed by AT&T under 'Software Development Kit Tools Agreement.' 2012
 * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION: 
 * http://developer.att.com/apsl-1.0
 * Copyright 2012 AT&T Intellectual Property. All rights reserved. http://developer.att.com
 * For more information refer to http://pte.att.com/Engage.aspx
 */
package com.platform.c2115417183.gsms;

@SuppressWarnings("serial")
public class GSMSException extends Exception {

  public GSMSException(String message) {
    super(message);
  }
  
  public GSMSException(String message, Throwable cause) {
    super(message, cause);
  }
  
}