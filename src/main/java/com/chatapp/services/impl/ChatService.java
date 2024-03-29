package com.chatapp.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;

import javax.websocket.EncodeException;

import com.chatapp.models.dtos.FileDTO;
import com.chatapp.models.dtos.MessageDTO;
import com.chatapp.services.ChatServiceAbstract;
import com.chatapp.services.FileServiceAbstract;
import com.chatapp.websockets.ChatWebsocket;

public class ChatService extends ChatServiceAbstract {

	private static ChatService chatService = null;

	private ChatService() {
	}

	public synchronized static ChatService getInstance() {
		if (chatService == null) {
			chatService = new ChatService();
		}
		return chatService;
	}

	@Override
	public boolean register(ChatWebsocket chatWebsocket) {
		return chatWebsockets.add(chatWebsocket);
	}

	@Override
	public boolean close(ChatWebsocket chatWebsocket) {
		return chatWebsockets.remove(chatWebsocket);
	}

	@Override
	public boolean isUserOnline(String username) {
		for (ChatWebsocket chatWebsocket : chatWebsockets) {
			if (chatWebsocket.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void sendMessageToAllUsers(MessageDTO message) {
		message.setOnlineList(getUsernames());
		chatWebsockets.stream().forEach(chatWebsocket -> {
			try {
				chatWebsocket.getSession().getBasicRemote().sendObject(message);
			} catch (IOException | EncodeException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void sendMessageToOneUser(MessageDTO message, Queue<FileDTO> fileDTOs) {
		if (!message.getType().equals("text")) {
			String fileName = message.getMessage();
			fileName = fileName.replaceAll("\\s+", "");
			String destFile = FileServiceAbstract.rootLocation.toString() + "/" + message.getUsername() + "/"
					+ fileName;
			File uploadedFile = new File(destFile);
			String sender = message.getUsername();
			String receiver = message.getReceiver();
			String url = FileServiceAbstract.rootURL + sender + "/" + fileName;
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(uploadedFile, false);
				FileDTO newFileDTO = new FileDTO(fileName, message.getType(), fileOutputStream, sender, receiver, url);
				fileDTOs.add(newFileDTO);
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			}
		} else {
			chatWebsockets.stream().filter(chatWebsocket -> chatWebsocket.getUsername().equals(message.getReceiver()))
					.forEach(chatWebsocket -> {
						try {
							chatWebsocket.getSession().getBasicRemote().sendObject(message);
						} catch (IOException | EncodeException e) {
							e.printStackTrace();
						}
					});
		}
	}

	@Override
	public void handleFileUpload(ByteBuffer byteBuffer, boolean last, Queue<FileDTO> fileDTOs) {
		try {
			if (!last) {
				while (byteBuffer.hasRemaining()) {
					fileDTOs.peek().getFileOutputStream().write(byteBuffer.get());
				}
			} else {
				fileDTOs.peek().getFileOutputStream().flush();
				fileDTOs.peek().getFileOutputStream().close();
				System.out
						.println("Done: " + fileDTOs.peek().getFilename() + " of user: " + fileDTOs.peek().getSender());
				String message = "<img src=\"" + fileDTOs.peek().getUrl() + "\" alt=\"\">";
				String typeFile = fileDTOs.peek().getTypeFile();
				if (typeFile.startsWith("audio")) {
					message = "<audio controls>\r\n" + "  <source src=\"" + fileDTOs.peek().getUrl() + "\" type=\""
							+ typeFile + "\">\r\n" + "</audio>";
				} else if (typeFile.startsWith("video")) {
					message = "<video width=\"400\" controls>\r\n" + "  <source src=\"" + fileDTOs.peek().getUrl()
							+ "\" type=\"" + typeFile + "\">\r\n" + "</video>";
				} else if (typeFile.startsWith("image")) {
					message = "<img src=\"" + fileDTOs.peek().getUrl() + "\" alt=\"\">";
				} else {
					message = "<a href=" + fileDTOs.peek().getUrl() + ">" + fileDTOs.peek().getFilename() + "</a>";
				}
				String type = "text";
				String username = fileDTOs.peek().getSender();
				String receiver = fileDTOs.peek().getReceiver();
				MessageDTO messageResponse = new MessageDTO(username, message, type, receiver);
				fileDTOs.remove();
				sendMessageToOneUser(messageResponse, fileDTOs);
			}

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
