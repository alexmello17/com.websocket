package com.websockets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/wsuser")
public class WebSocketUser {
	
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());
	private static Map<Session, String> users = Collections.synchronizedMap(new HashMap<Session, String>());

	@OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
		
		System.out.println("Received User: " + message);
		users.put(session, message);
		String usersS ="";
		synchronized(users){
	    	  for(String s : users.values()) {
	    		  usersS += s+"<br/>";
	    	  }
   	  	}
		synchronized(clients){
	      for(Session client : clients){
	    	  client.getBasicRemote().sendText(usersS);
	      }
	    }	
    }
	
	@OnOpen
    public void onOpen (Session session) {
        System.out.println("Client connected iD: "+session.getId());
        clients.add(session);
    }

    @OnClose
    public void onClose (Session session) {
    	System.out.println("Connection closed iD: "+session.getId());
    	clients.remove(session);
    	for(Session s : users.keySet()) {
    		if(session.equals(s)) {
    			users.remove(s);
    		}
    	}
    }
}
