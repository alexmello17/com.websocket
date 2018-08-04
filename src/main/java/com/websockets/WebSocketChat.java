package com.websockets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/wschat")
public class WebSocketChat {
	
	private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

	@OnMessage
    public void onMessage(String message, Session session) throws IOException, InterruptedException {
		
		// Print the client message for testing purposes
		System.out.println("Received Chat: " + message);
				
		synchronized(clients){
	      // Iterate over the connected sessions
	      // and broadcast the received message
	      for(Session client : clients){
//			otimizacao: utilizar msg no js e adicionar a text Area    	  
//	        if (!client.equals(session)){
//	        }
	    	  client.getBasicRemote().sendText(message);
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
    }
}
