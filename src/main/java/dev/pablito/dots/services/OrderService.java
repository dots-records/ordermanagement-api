package dev.pablito.dots.services;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import dev.pablito.dots.aop.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.api.discogs.DiscogsOrder;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.Message;
import dev.pablito.dots.entity.OrdersInfo;
import dev.pablito.dots.mapper.OrderMapper;
import dev.pablito.dots.repository.OrderRepository;
import dev.pablito.dots.repository.OrdersInfoRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrdersInfoRepository ordersInfoRepository;
	@Autowired
	private DiscogsClient discogsClient;
	@Autowired
	private OrderMapper orderMapper;

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	// Function: Get orders from Database

	@Timed
	public List<DatabaseOrder> getUnarchivedNewOrders() throws IOException, InterruptedException {
		// Crear una lista que contenga los pedidos con "archived = false" y el estado
		// deseado
		List<DatabaseOrder> orders = new ArrayList<>();
		orders.addAll(orderRepository.findByStatusAndArchived("Payment Received", false));
		orders.addAll(orderRepository.findByStatusAndArchived("Invoice Sent", false));
		orders.addAll(orderRepository.findByStatusAndArchived("Payment Pending", false));
		
		return orders;
	}

	@Timed
	public Page<DatabaseOrder> getUnarchivedOrders(int page, int size) throws IOException, InterruptedException {
		List<DatabaseOrder> orders = new ArrayList<>();
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		Page<DatabaseOrder> orderPage = orderRepository.findByArchived(false, pageable);
		return orderPage;
	}

	@Timed
	public Page<DatabaseOrder> getArchivedOrders(int page, int size) throws IOException, InterruptedException {
		List<DatabaseOrder> orders = new ArrayList<>();
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		Page<DatabaseOrder> orderPage = orderRepository.findByArchived(true, pageable);
		return orderPage;
	}

	@Timed
	public Page<DatabaseOrder> getAllOrders(int page, int size) throws IOException, InterruptedException {
		List<DatabaseOrder> orders = new ArrayList<>();
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		Page<DatabaseOrder> orderPage = orderRepository.findAll(pageable);
		return orderPage;
	}

	@Timed
	public DatabaseOrder getOrder(String id) throws IOException, InterruptedException {
		return orderRepository.findOrderById(id).get();
	}

	@Timed
	public void updateUpdatableDiscogsOrders() throws Exception {
		List<DatabaseOrder> orders = new ArrayList<>();
		orders.addAll(orderRepository.findByStatusAndArchivedAndType("Other", false, "Discogs"));
		orders.addAll(orderRepository.findByStatusAndArchivedAndType("Invoice Sent", false, "Discogs"));
		orders.addAll(orderRepository.findByStatusAndArchivedAndType("Payment Pending", false, "Discogs"));
		List<DiscogsOrder> updateOrdersDiscogs = new ArrayList<>();
		for(DatabaseOrder orderDB: orders) {
			System.out.println(orderDB.getDiscogsId());
		    DiscogsOrder orderDS = discogsClient.getDiscogsOrder(orderDB.getDiscogsId());
		    String statusDS = orderDS.getStatus();
		    if(!(statusDS.equals("Payment Pending") || statusDS.equals("Invoice Sent") 
		     		|| statusDS.equals("Payment Received") || statusDS.equals("In Progress")
		    		|| statusDS.equals("Shipped") || statusDS.equals("Cancelled (Non-Paying Buyer)")
		       		|| statusDS.equals("Cancelled (Item Unavailable)") 
		       		|| statusDS.equals("Cancelled (Per Buyer's Request)"))) {
		    	statusDS = "Other";
			} 
		    if(!statusDS.equals(orderDB.getStatus())) {
		    	updateOrdersDiscogs.add(orderDS);
		    }
		}
	    List<DatabaseOrder> updateOrdersDB = updateOrdersDiscogs.stream()
	        .map(order -> {
	            try {
	                return orderMapper.mapToDatabaseOrder(order);
	            } catch (IOException | InterruptedException e) {
	                throw new RuntimeException("Error al convertir DiscogsOrder", e);
	            }
	        })
	        .toList();
	    saveOrders(updateOrdersDB);
	}
	
	@Transactional(rollbackFor = Exception.class)
	protected void saveOrders(List<DatabaseOrder> dbOrders) {
		orderRepository.saveAll(dbOrders);
		
	}
	// Checks if a new order has been made in Discogs and updates the database
	@Timed
	public void checkOrdersInDiscogs() throws Exception {
	    if (ordersInfoRepository.findAll().isEmpty()) {
	        OrdersInfo new_info = new OrdersInfo();
	        new_info.setCreatedAfter("1900-01-01T08:43:03.9248907-07:00");
	        ordersInfoRepository.insert(new_info);
	    }
	    OrdersInfo info = ordersInfoRepository.findAll().getFirst();
	    String createdAfter = info.getCreatedAfter();
	    List<DiscogsOrder> orders_discogs = discogsClient.getDiscogsOrdersCreatedAfter(createdAfter);
	    List<DatabaseOrder> dbOrders = orders_discogs.stream()
	        .map(order -> {
	            try {
	                return orderMapper.mapToDatabaseOrder(order);
	            } catch (IOException | InterruptedException e) {
	                throw new RuntimeException("Error al convertir DiscogsOrder", e);
	            }
	        })
	        .toList();
	    saveOrdersAndUpdateInfo(dbOrders, info);
	}

	@Transactional(rollbackFor = Exception.class)
	protected void saveOrdersAndUpdateInfo(List<DatabaseOrder> dbOrders, OrdersInfo oldInfo) {
		orderRepository.saveAll(dbOrders);
	    ordersInfoRepository.delete(oldInfo);    // elimina el OrdersInfo antiguo

	    OrdersInfo new_info = new OrdersInfo();
	    new_info.setCreatedAfter(getActualDate());
	    ordersInfoRepository.insert(new_info);   // inserta el OrdersInfo nuevo
	}



	@Timed
	public String getOrdersInformation() throws Exception {
			OrdersInfo info = ordersInfoRepository.findAll().getFirst();
			return info.getCreatedAfter();
	}
	@Timed
	public Page<DatabaseOrder> searchOrdersByArchived(String palabra, int page, int size, boolean archived) {
		// Crear el Pageable con la página y el tamaño, puedes agregar un orden si lo
		// necesitas
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		return orderRepository.findByArchivedAndSearchTerm(archived, palabra, pageable);
	}

	@Timed
	public Page<DatabaseOrder> searchOrders(String palabra, int page, int size) {
		// Crear el Pageable con la página y el tamaño, puedes agregar un orden si lo
		// necesitas
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		return orderRepository.findBySearchTerm(palabra, pageable);
	}

	// From Discogs
	@Timed
	public DiscogsOrder getOrderFromDiscogs(String id) throws IOException, InterruptedException {
		DiscogsOrder order = discogsClient.getDiscogsOrder(id);
		if (order != null) {
			// Add extra information of the order here
		}
		return order;
	}

	@Timed
	public List<Message> getDiscogsMessages(String id) throws IOException, InterruptedException {
		return discogsClient.getDiscogsMessages(id).getMessages();
	}

	// From Database

	@Timed
	public String createListings() throws IOException, InterruptedException {
		ArrayList<Long> listings = new ArrayList<>();

		discogsClient.createMarketplaceListing(32366235, 55.49, "Brand new. Never played. Ships on February");

		return "Todo ok";
	}

	// Other

	private String getActualDate() {
		ZoneOffset zonaHoraria = ZoneOffset.of("-07:00"); // UTC-7
		OffsetDateTime horaActual = OffsetDateTime.now(zonaHoraria);
		return horaActual.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}


	// CREATE



	public void checkUpdateOrder(DatabaseOrder orderDb) throws IOException, InterruptedException {
		DiscogsOrder orderDisc = getOrderFromDiscogs(orderDb.getId());
		if (!orderDisc.getStatus().equals(orderDb.getStatus())) {
			orderRepository.delete(orderDb);
			orderDb.setStatus(orderDisc.getStatus());
			orderRepository.insert(orderDb);
		}
	}

	public void checkUpdatesPendingOrders() throws IOException, InterruptedException {
		List<DatabaseOrder> orders = orderRepository.findByStatus("Payment Pending");
		for (DatabaseOrder order : orders) {
			checkUpdateOrder(order);
		}
	}

	// UPDATE

	@Timed
	public Optional<DatabaseOrder> updateOrderStatusInDatabase(String id, String new_status) {
		return orderRepository.findById(id).map(order -> {
			// Actualizar el estado del pedido
			order.setStatus(new_status);
			// Guardar el documento actualizado
			return orderRepository.save(order);
		});
	}

	// TODO: Revisar que funcione bien
	// TODO: Los mensajes no se estan añadiendo en order, hacer que los mensajes se
	// ordenen por fecha despues a la hora de hacer
	// el get de MongoDb

	@Timed
	public DiscogsOrder updateOrderStatusInDiscogs(String id, String new_status)
			throws IOException, InterruptedException {
		return discogsClient.updateOrderStatus(id, new_status);
	}

}
