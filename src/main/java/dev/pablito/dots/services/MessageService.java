package dev.pablito.dots.services;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.entity.DatabaseNotification;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.Message;
import dev.pablito.dots.entity.MessageManager;
import dev.pablito.dots.repository.NotificationRepository;
import dev.pablito.dots.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
	
	@Autowired 
	private OrderRepository orderRepository;
	@Autowired 
	private NotificationRepository notificationRepository;
	@Autowired 
	private DiscogsClient discogsClient;
	@Value("${discogs.seller.id}")
	private String sellerId ;

	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

	@Timed
	public void updateUnarchivedMessages() throws Exception {
		List<DatabaseOrder> orders = new ArrayList<>();
		orders.addAll(orderRepository.findByArchived(false));
		for(int i = 0; i< orders.size();i++ ) {
			DatabaseOrder order = orders.get(i);
			updateMessages(order);
			Thread.sleep(1000);
		}
		
	}

	@Timed
	public void updateArchivedMessages() throws Exception {
		List<DatabaseOrder> orders = new ArrayList<>();
		orders.addAll(orderRepository.findByArchived(false));
		for(int i = 0; i< orders.size();i++ ) {
			DatabaseOrder order = orders.get(i);
			updateMessages(order);
			Thread.sleep(1000);
		}
		
	}
	
	public boolean afterThan(String actualMessage, String lastMessage) {
        try {
            if(lastMessage == null) {
            	return true;
            }
            OffsetDateTime dateTime1 = OffsetDateTime.parse(actualMessage);
            OffsetDateTime dateTime2 = OffsetDateTime.parse(lastMessage);
            return dateTime1.isAfter(dateTime2);
        } catch (DateTimeParseException e) {
            // Manejar error si el formato de fecha no es válido
            e.printStackTrace();
            return false;
        }
    }
	
	public void updateMessages(DatabaseOrder order) throws Exception {
		
		MessageManager mmDs = discogsClient.getDiscogsMessages(order.getId());
		List<Message> messagesDs = mmDs.getMessages();
		Integer newMessagesCustomerBackup = order.getNewMessagesCustomer();
		Integer newMessagesSellerBackup = order.getNewMessagesSeller();
		Integer newMessagesDiscogsBackup = order.getNewMessagesDiscogs();
		if(!messagesDs.isEmpty()) {
			MessageManager mmDb = order.getMessageManager();
			List<Message> messagesDb = mmDb.getMessages();
			String lastMessage = mmDb.getLastMessage();
			String newLastMessage = null;
			Integer newMessagesCustomer = 0;
			Integer newMessagesSeller = 0;
			Integer newMessagesDiscogs = 0;
			boolean added_messages = false;
				
			if(lastMessage == null || messagesDb == null) {
				messagesDb = new ArrayList<Message>();
				for(int i = 0; i < messagesDs.size();i++) {
					Message addedMessage = messagesDs.get(i);
					messagesDb.addLast(addedMessage);
					String type = addedMessage.getType();
					if(type.equals("message")) {
						if(messagesDs.get(i).getFrom().getId().equals(sellerId)) {
							newMessagesSeller++;
						} else {
							newMessagesCustomer++;
							notificationRepository.insert(new DatabaseNotification(order.getId(), "Message", addedMessage.getTimestamp()));
						}
					} else {
						newMessagesDiscogs++;
					}
					if(i == 0) {
						newLastMessage = messagesDs.get(i).getTimestamp();
						added_messages = true;
					}
				}
					
			} else {
				for(int i = 0; i < messagesDs.size() && afterThan(messagesDs.get(i).getTimestamp(), lastMessage); i++) {
					Message addedMessage = messagesDs.get(i);
					messagesDb.addLast(addedMessage);
					String type = addedMessage.getType();
					if(type.equals("message")) {
						if(messagesDs.get(i).getFrom().getId().equals(sellerId)) {
							newMessagesSeller++;
						} else {
							newMessagesCustomer++;
							notificationRepository.insert(new DatabaseNotification(order.getId(), "Message", addedMessage.getTimestamp()));
						}
					} else  {
						newMessagesDiscogs++;
					}
					if(i == 0) {
						newLastMessage = messagesDs.get(i).getTimestamp();
						added_messages = true;
					}
				}
			}
				
			if(newLastMessage != null) {
				mmDb.setLastMessage(newLastMessage);
			}
				
			mmDb.setMessages(messagesDb);
			if(added_messages) {
				order.setNewMessagesCustomer(newMessagesCustomer + newMessagesCustomerBackup);
				order.setNewMessagesSeller(newMessagesSeller + newMessagesSellerBackup);
				order.setNewMessagesDiscogs(newMessagesDiscogs + newMessagesDiscogsBackup);
			}
					
			orderRepository.save(order);
		} 	
	}
	
	public void sendMessage(String id, String message) throws Exception {
		discogsClient.sendMessage(id, message);
	}
	
	public void resetNewMessages(String id) throws Exception {
		orderRepository.findById(id)
                .map(order -> {
                    // Actualizar el estado del pedido
                	order.setNewMessagesCustomer(0); 
                    order.setNewMessagesSeller(0);
                    order.setNewMessagesDiscogs(0);
                    // Guardar el documento actualizado
                    return orderRepository.save(order);
                });
	}
	
	

}
