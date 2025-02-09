package dev.pablito.dots.services;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

	// Function: Get orders from Database

	public List<DatabaseOrder> getUnarchivedNewOrders() throws IOException, InterruptedException {
		// Crear una lista que contenga los pedidos con "archived = false" y el estado
		// deseado
		List<DatabaseOrder> orders = new ArrayList<>();
		orders.addAll(orderRepository.findByStatusAndArchived("Payment Received", false));
		orders.addAll(orderRepository.findByStatusAndArchived("Invoice Sent", false));
		orders.addAll(orderRepository.findByStatusAndArchived("Payment Pending", false));
		// Ordenar los pedidos por el atributo "number" de mayor a menor
		orders.sort(Comparator.comparing(DatabaseOrder::getNumber).reversed());

		return orders;
	}

	public Page<DatabaseOrder> getUnarchivedOrders(int page, int size) throws IOException, InterruptedException {
		List<DatabaseOrder> orders = new ArrayList<>();
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "number"));
		Page<DatabaseOrder> orderPage = orderRepository.findByArchived(false, pageable);
		return orderPage;
	}

	public Page<DatabaseOrder> getArchivedOrders(int page, int size) throws IOException, InterruptedException {
		List<DatabaseOrder> orders = new ArrayList<>();
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "number"));
		Page<DatabaseOrder> orderPage = orderRepository.findByArchived(true, pageable);
		return orderPage;
	}

	public Page<DatabaseOrder> getAllOrders(int page, int size) throws IOException, InterruptedException {
		List<DatabaseOrder> orders = new ArrayList<>();
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "number"));
		Page<DatabaseOrder> orderPage = orderRepository.findAll(pageable);
		return orderPage;
	}

	public DatabaseOrder getOrder(String id) throws IOException, InterruptedException {
		return orderRepository.findOrderById(id).get();
	}

	// Checks if a new order has been made in Discogs and updates the database
	// "Orders"
	public void checkOrdersInDiscogs() throws Exception {
		// If there is not an OrdersInformation item, creates one
		if (ordersInfoRepository.findAll().isEmpty()) {
			OrdersInfo new_info = new OrdersInfo();
			// Defined to last year to get all orders created after
			new_info.setCreatedAfter("2025-01-01T08:43:03.9248907-08:00");
			ordersInfoRepository.insert(new_info);
		}
		List<DatabaseOrder> added_orders_database = new ArrayList<DatabaseOrder>();
		OrdersInfo info = ordersInfoRepository.findAll().getFirst();
		ordersInfoRepository.delete(info);
		String createdAfter = info.getCreatedAfter();
		List<DiscogsOrder> orders_discogs = discogsClient.getDiscogsOrdersCreatedAfter(createdAfter);
		for (DiscogsOrder order : orders_discogs) {
			DatabaseOrder orderDb = createDatabaseOrder(convertDiscogsOrder(order));
			added_orders_database.addLast(orderDb);
			// notificationRepository.insert(new DatabaseNotification(orderDb.getNumber(),
			// "New Order"));
		}
		OrdersInfo new_info = new OrdersInfo();
		new_info.setCreatedAfter(getActualDate());
		ordersInfoRepository.insert(new_info);
	}

	public Page<DatabaseOrder> searchOrdersByArchived(String palabra, int page, int size, boolean archived) {
		// Crear el Pageable con la página y el tamaño, puedes agregar un orden si lo
		// necesitas
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "number"));
		return orderRepository.findByArchivedAndSearchTerm(archived, palabra, pageable);
	}

	public Page<DatabaseOrder> searchOrders(String palabra, int page, int size) {
		// Crear el Pageable con la página y el tamaño, puedes agregar un orden si lo
		// necesitas
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "number"));
		return orderRepository.findBySearchTerm(palabra, pageable);
	}

	// From Discogs
	public DiscogsOrder getOrderFromDiscogs(String id) throws IOException, InterruptedException {
		DiscogsOrder order = discogsClient.getDiscogsOrder(id);
		if (order != null) {
			// Add extra information of the order here
		}
		return order;
	}

	public List<Message> getDiscogsMessages(String id) throws IOException, InterruptedException {
		return discogsClient.getDiscogsMessages(id).getMessages();
	}

	// From Database

	public String createListings() throws IOException, InterruptedException {
		ArrayList<Long> listings = new ArrayList<>();

		discogsClient.createMarketplaceListing(32366235, 55.49, "Brand new. Never played. Ships on February");

		/*
		 * // Birds of a Feather client.createMarketplaceListing(32278635, 29,
		 * "Brand new. Never played. Ships around December 13th."); //11
		 * client.createMarketplaceListing(26830313, 52.49,
		 * "Brand new. Never played. 2024"); //Daft punk - Discovery (Japan)
		 * 
		 * client.createMarketplaceListing(148392, 65.49 ,
		 * "Official 2024 Edition , releasing on December 13, 2024. Limited edition Japanese black vinyl. Available for pre-order. Refunds offered."
		 * );
		 * 
		 * //Partynextdoor client.createMarketplaceListing(5293889, 62.50,
		 * "Brand new. Never played. 2024"); //5
		 * client.createMarketplaceListing(14134460, 52.49,
		 * "Brand new. Never played. 2024"); client.createMarketplaceListing(14025860,
		 * 52.49, "Brand new. Never played. 2024");
		 * client.createMarketplaceListing(16542747, 52.49,
		 * "Brand new. Never played. 2024");
		 * 
		 * // Alligator Bites Never Heal Hazel Vinyl
		 * client.createMarketplaceListing(32366235, 55.49,
		 * "Brand new. Never played. Ships on February");
		 * 
		 * // This Wasn't Meant For You Anyway client.createMarketplaceListing(31093745,
		 * 55.49, "Brand new. Never played. Ships on February");
		 * client.createMarketplaceListing(31019518, 55.49,
		 * "Brand new. Never played. Ships on February");
		 * 
		 * //Rose In The Dark (CD) client.createMarketplaceListing(16027839, 30.49,
		 * "Brand new. Never played."); //Mother (CD)
		 * client.createMarketplaceListing(20922673, 30.49, "Brand new. Never played.");
		 * //Heaven (CD) client.createMarketplaceListing(29894128, 30.49,
		 * "Brand new. Never played."); //Gold (CD)
		 * client.createMarketplaceListing(29894311, 30.49, "Brand new. Never played.");
		 * 
		 * //Rose In The Dark client.createMarketplaceListing(15668236, 62.49,
		 * "Brand new. Never played. 2024"); //Gold
		 * //client.createMarketplaceListing(29560756, 62.49,
		 * "Brand new. Never played. 2024"); //Heaven
		 * //client.createMarketplaceListing(29544340, 62.49,
		 * "Brand new. Never played. 2024"); //Fruitcake (Green)
		 * client.createMarketplaceListing(29187700, 42.49,
		 * "Brand new. Never played. Green Vinyl. 2024"); //Fruitcake (Fruit Punch)
		 * client.createMarketplaceListing(29187700, 42.49,
		 * "Brand new. Never played. Fruit Punch. 2024"); // Nonsense / A Nonsense
		 * Christmas client.createMarketplaceListing(28955275, 28.49,
		 * "Brand new. Never played. 2024");
		 */

		return "Todo ok";
	}

	// Other

	public String getActualDate() {
		ZoneOffset zonaHoraria = ZoneOffset.of("-08:00"); // UTC-8
		OffsetDateTime horaActual = OffsetDateTime.now(zonaHoraria);
		return horaActual.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}

	// CONVERT

	public DatabaseOrder convertDiscogsOrder(DiscogsOrder order) throws IOException, InterruptedException {
		return orderMapper.mapToDatabaseOrder(order);
	}

	// CREATE

	public DatabaseOrder createDatabaseOrder(DatabaseOrder order) {
		return orderRepository.insert(order);
	}

	public DatabaseOrder createDatabaseOrderFromDiscogs(String id) throws IOException, InterruptedException {
		return createDatabaseOrder(convertDiscogsOrder(getOrderFromDiscogs(id)));
	}

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

	public DiscogsOrder updateOrderStatusInDiscogs(String id, String new_status)
			throws IOException, InterruptedException {
		return discogsClient.updateOrderStatus(id, new_status);
	}

}
