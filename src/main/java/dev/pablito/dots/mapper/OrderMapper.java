package dev.pablito.dots.mapper;

import java.util.List;
import java.util.Locale;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.api.discogs.DiscogsOrder;
import dev.pablito.dots.api.discogs.DiscogsRelease;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.MessageManager;
import dev.pablito.dots.entity.DatabaseOrder.DatabaseItem;
import dev.pablito.dots.entity.DatabaseOrder.Payment;
import dev.pablito.dots.entity.DatabaseOrder.Provider;
import dev.pablito.dots.entity.DatabaseRelease;
import dev.pablito.dots.services.ReleaseService;

@Component
public class OrderMapper {
	
	@Value("${discogs.seller.id}")
	private String sellerId ;
	
	@Autowired
	private ReleaseService releaseService;
	
    public DatabaseOrder mapToDatabaseOrder(DiscogsOrder order) throws IOException, InterruptedException {
        if (order == null) {
            return null;
        }
        System.out.println(order.getId());

        DatabaseOrder dbOrder = new DatabaseOrder();
        dbOrder.setId(order.getId());
        dbOrder.setType("Discogs");
        String replace = "^" + sellerId +"-";
        dbOrder.setNumber(order.getId().replaceFirst(replace, ""));
        dbOrder.setStatus(order.getStatus());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH);
        dbOrder.setCreated(ZonedDateTime.parse(order.getCreated()).format(formatter));
        dbOrder.setArchived(order.isArchived());
        dbOrder.setRevenue(order.getRevenue());
        
        
        // Map Payment
        DatabaseOrder.Payment payment = new DatabaseOrder.Payment();
        payment.setShipping(order.getShipping() != null ? order.getShipping().getValue() : null);
        payment.setItems(order.getTotal() != null ? order.getTotal().getValue() : null);
        dbOrder.setPayment(payment);

        // Map Provider
        DatabaseOrder.Provider provider = new DatabaseOrder.Provider();
        provider.setName("");  // Placeholder, set appropriately if needed
        provider.setInformation("");  // Placeholder, set appropriately if needed
        provider.setPrice(0);
        dbOrder.setProvider(provider);
        
        
        // Map Items
        
        List<DatabaseOrder.DatabaseItem> dbItems = order.getItems().stream()
                .map(item -> {
                    DatabaseOrder.DatabaseItem dbItem = new DatabaseOrder.DatabaseItem();
                    dbItem.setId(item.getId());
                    
                    DatabaseRelease release;
                    
                    try {
                    	if(!releaseService.contains(item.getRelease().getId())) {
                        	releaseService.putReleaseFromDiscogs(item.getRelease().getId());
                    	}
                    	
                    	release = releaseService.getRelease(item.getRelease().getId());
                    	
					} catch (IOException | InterruptedException e) {
						release = null;
						e.printStackTrace();
					}
                    dbItem.setName(release != null ? release.getTitle() : null);
                    dbItem.setArtists(release != null ? release.getArtists() : null);
                    dbItem.setThumb(release != null ? release.getThumb() : null);
                    return dbItem;
                })
                .collect(Collectors.toList());
        dbOrder.setItems(dbItems);
                
        /*
        List<DatabaseOrder.DatabaseItem> dbItems = order.getItems().stream()
                .map(item -> {
                    DatabaseOrder.DatabaseItem dbItem = new DatabaseOrder.DatabaseItem();
                    dbItem.setId(item.getId());
                    Release release;
                    dbItem.setName("Hola");
                    return dbItem;
                })
                .collect(Collectors.toList());
        dbOrder.setItems(dbItems);
        */
        
        

        // Set delivery_date if applicable
        dbOrder.setDelivery_date("");  // Placeholder, set appropriately if needed
        dbOrder.setNotified(false);
        dbOrder.setNewMessagesCustomer(0); 
        dbOrder.setNewMessagesSeller(0);
        dbOrder.setNewMessagesDiscogs(0);
        MessageManager messageManager = new MessageManager();
        dbOrder.setMessageManager(messageManager);

        return dbOrder;
    }
}