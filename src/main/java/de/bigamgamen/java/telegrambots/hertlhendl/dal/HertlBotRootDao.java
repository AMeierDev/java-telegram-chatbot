package de.bigamgamen.java.telegrambots.hertlhendl.dal;

import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotOrder;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotRoot;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotUser;
import one.microstream.persistence.binary.jdk17.types.BinaryHandlersJDK17;
import one.microstream.storage.embedded.configuration.types.EmbeddedStorageConfiguration;
import one.microstream.storage.embedded.types.EmbeddedStorageFoundation;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HertlBotRootDao
{

	private final static EmbeddedStorageManager storageManager = createStorageManager();

	private Predicate<HertlBotOrder> openOrderUntilToday = order ->
		{
			LocalDate now = LocalDate.now();
			LocalDate orderDate = order.getBestellDatum();
			return (orderDate.isBefore(now) || orderDate.equals(now)) && !order.isClosed() && order.isCommited();
		};
	

	public HertlBotRootDao()
	{

	}

	private static EmbeddedStorageManager createStorageManager()
	{
		final EmbeddedStorageFoundation<?> foundation = EmbeddedStorageConfiguration.Builder()
				.setStorageDirectory(Paths.get("data", "microstream", "storage").toString())
				.setChannelCount(Math.max(
						1, // minimum one channel, if only 1 core is available
						Integer.highestOneBit(Runtime.getRuntime().availableProcessors() - 1)
				))
				.createEmbeddedStorageFoundation();

		foundation.onConnectionFoundation(BinaryHandlersJDK17::registerJDK17TypeHandlers);
		final EmbeddedStorageManager storageManager = foundation.createEmbeddedStorageManager().start();

		if (storageManager.root() == null)
		{
			final HertlBotRoot root = new HertlBotRoot();
			storageManager.setRoot(root);
			storageManager.storeRoot();
		}

		return storageManager;
	}

	public static EmbeddedStorageManager storageManager()
	{
		return storageManager;
	}

	public HertlBotRoot root()
	{
		return (HertlBotRoot) storageManager().root();
	}

	public HertlBotOrder loadBestellung(Long chatId, String userName, int bestellId)
	{
		HertlBotUser user = loadUser(chatId,userName);

		return user.getBestellungen().stream().filter(bestellung -> bestellung.getIndex() == bestellId).findFirst()
				.get();
	}

	public HertlBotUser loadUser(Long chatId, String userName)
	{

		Optional<HertlBotUser> userOpt = root().users().ofId(chatId);

		if (userOpt.isPresent())
		{
			return userOpt.get();
		}

		HertlBotUser user = new HertlBotUser(chatId,userName);
		root().users().add(user, storageManager());
		return user;
	}

	public HertlBotOrder createNewBestellungForUser(Long chatId, String userName)
	{
		HertlBotUser user = this.loadUser(chatId,userName);
		HertlBotOrder bestellung = new HertlBotOrder(user, new ArrayList<>());
		user.addBestellung(bestellung, storageManager);

		return bestellung;
	}

	public List<HertlBotOrder> loadOpenOrdersForToday()
	{
		List<HertlBotOrder> orders = new ArrayList<>();
		root().users().all().forEach(user -> orders
				.addAll(user.getBestellungen().stream().filter(openOrderUntilToday).collect(Collectors.toList())));
		return orders;
	}

	public void shutDown()
	{

		storageManager.shutdown();
	}

}
