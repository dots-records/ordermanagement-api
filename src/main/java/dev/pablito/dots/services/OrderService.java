package dev.pablito.dots.services;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.pablito.dots.aop.Timed;
import dev.pablito.dots.api.discogs.DiscogsClient;
import dev.pablito.dots.api.discogs.DiscogsOrder;
import dev.pablito.dots.entity.DatabaseOrder;
import dev.pablito.dots.entity.Message;
import dev.pablito.dots.entity.OrderRequest;
import dev.pablito.dots.entity.OrdersInfo;
import dev.pablito.dots.mapper.OrderMapper;
import dev.pablito.dots.repository.ListingRepository;
import dev.pablito.dots.repository.OrderRepository;
import dev.pablito.dots.repository.OrdersInfoRepository;
import dev.pablito.dots.repository.ProviderRepository;
import dev.pablito.dots.repository.ReleaseRepository;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrdersInfoRepository ordersInfoRepository;
	@Autowired
	private ReleaseRepository releaseRepository;
	@Autowired
	private ProviderRepository providerRepository;
	@Autowired
	private ListingRepository listingRepository;
	@Autowired
	private DiscogsClient discogsClient;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private ListingService listingService;

	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	// Function: Get orders from Database

	@Timed
	public DatabaseOrder getOrder(String id) throws IOException, InterruptedException {
		return orderRepository.findOrderById(id).get();
	}

	@Timed
	public Page<DatabaseOrder> getOrders(int page, int size) throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		Page<DatabaseOrder> orderPage = orderRepository.findAll(pageable);
		return orderPage;
	}

	@Timed
	public Page<DatabaseOrder> getOrdersByArchived(int page, int size, boolean archived)
			throws IOException, InterruptedException {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		Page<DatabaseOrder> orderPage = orderRepository.findByArchived(archived, pageable);
		return orderPage;
	}

	@Timed
	public Page<DatabaseOrder> searchOrders(String palabra, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		return orderRepository.findBySearchTerm(palabra, pageable);
	}

	@Timed
	public Page<DatabaseOrder> searchOrdersByArchived(String palabra, int page, int size, boolean archived) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdComplete"));
		return orderRepository.findByArchivedAndSearchTerm(archived, palabra, pageable);
	}

	@Timed
	public Optional<DatabaseOrder> updateOrderStatusInDatabase(String id, String new_status) {
		return orderRepository.findById(id).map(order -> {
			// Actualizar el estado del pedido
			order.setStatus(new_status);
			// Guardar el documento actualizado
			return orderRepository.save(order);
		});
	}

	@Timed
	public void createOrder(OrderRequest request) throws IOException, InterruptedException {
		DatabaseOrder order = new DatabaseOrder();
		String platform = request.getPlatform();
		order.setPlatform(platform);
		// Id
		if (platform.equals("Vinted")) {

		} else if (platform.equals("Wallapop")) {

		} else if (platform.equals("Other")) {

		} else {
			// throw new Exception();
		}
		String actualDate = getActualDate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH);
		order.setCreated(ZonedDateTime.parse(actualDate).format(formatter));
		order.setCreatedComplete(actualDate);
		order.setArchived(false);
		order.setJustAdded(true);
		order.setStatus("Payment Received");
		order.setItems(request.getItems());
		orderRepository.save(order);

	}

	@Timed
	public DiscogsOrder updateOrderStatusInDiscogs(String id, String new_status)
			throws IOException, InterruptedException {
		return discogsClient.updateOrderStatus(id, new_status);
	}

	@Timed
	public Optional<DatabaseOrder> updateOrderJustAdded(String id, Boolean justAdded) {
		return orderRepository.findById(id).map(order -> {
			// Actualizar el estado del pedido
			order.setJustAdded(justAdded);
			return orderRepository.save(order);
		});
	}

	@Timed
	public void updateUpdatableDiscogsOrders() throws Exception {
		List<DatabaseOrder> orders = new ArrayList<>();
		orders.addAll(orderRepository.findByStatusAndArchivedAndPlatform("Other", false, "Discogs"));
		orders.addAll(orderRepository.findByStatusAndArchivedAndPlatform("Invoice Sent", false, "Discogs"));
		orders.addAll(orderRepository.findByStatusAndArchivedAndPlatform("Payment Pending", false, "Discogs"));
		List<DiscogsOrder> updateOrdersDiscogs = new ArrayList<>();
		for (DatabaseOrder orderDB : orders) {
			System.out.println(orderDB.getDiscogsId());
			DiscogsOrder orderDS = discogsClient.getDiscogsOrder(orderDB.getDiscogsId());
			String statusDS = orderDS.getStatus();
			if (!(statusDS.equals("Payment Pending") || statusDS.equals("Invoice Sent")
					|| statusDS.equals("Payment Received") || statusDS.equals("In Progress")
					|| statusDS.equals("Shipped") || statusDS.equals("Cancelled (Non-Paying Buyer)")
					|| statusDS.equals("Cancelled (Item Unavailable)")
					|| statusDS.equals("Cancelled (Per Buyer's Request)"))) {
				statusDS = "Other";
			}

			if (!statusDS.equals(orderDB.getStatus())) {
				updateOrdersDiscogs.add(orderDS);
			}
		}
		List<DatabaseOrder> updateOrdersDB = updateOrdersDiscogs.stream().map(order -> {
			try {
				return orderMapper.mapToDatabaseOrder(order);

			} catch (IOException | InterruptedException e) {
				throw new RuntimeException("Error al convertir DiscogsOrder", e);
			}
		}).toList();
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
		List<DatabaseOrder> dbOrders = orders_discogs.stream().map(order -> {
			try {
				return orderMapper.mapToDatabaseOrder(order);
			} catch (IOException | InterruptedException e) {
				throw new RuntimeException("Error al convertir DiscogsOrder", e);
			}
		}).toList();
		saveOrdersAndUpdateInfo(dbOrders, info);
	}

	@Transactional(rollbackFor = Exception.class)
	protected void saveOrdersAndUpdateInfo(List<DatabaseOrder> dbOrders, OrdersInfo oldInfo) {
		orderRepository.saveAll(dbOrders);
		ordersInfoRepository.delete(oldInfo); // elimina el OrdersInfo antiguo

		OrdersInfo new_info = new OrdersInfo();
		new_info.setCreatedAfter(getActualDate());
		ordersInfoRepository.insert(new_info); // inserta el OrdersInfo nuevo
	}

	@Timed
	public String getOrdersInformation() throws Exception {
		OrdersInfo info = ordersInfoRepository.findAll().getFirst();
		return info.getCreatedAfter();
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

}
