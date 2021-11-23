package de.bigamgamen.java.telegrambots.hertlhendl.helper;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotArticle;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotOrder;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotPosition;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotUser;

public class OrderHelper
{
	private OrderHelper ()
	{
		
	}
	
	public static HertlBotOrder getTotalOrder(List<HertlBotOrder> orderList)
	{
		HertlBotOrder totalOrder = new HertlBotOrder();
		HertlBotUser totalUser = new HertlBotUser(1L,"Gesamtbestellung");
		
		totalOrder.setUser(totalUser);
		totalOrder.setBestellDatum(LocalDate.now());
		orderList.forEach(order -> order.getPositionen().forEach(position -> addArticleToOrder(position.getArtikel(), position.getMenge(), totalOrder)));
		
		return totalOrder;
	}
	
	public static void addArticleToOrder(final HertlBotArticle artikel, BigInteger amount, final HertlBotOrder bestellung)
	{
		final Predicate<HertlBotPosition> positionAlreadyExist = position -> position.getArtikel().getName()
				.equals(artikel.getName());

		HertlBotPosition position;

		final Optional<HertlBotPosition> positionOpt = bestellung.getPositionen().stream().filter(positionAlreadyExist)
				.findFirst();
		if (positionOpt.isPresent())
		{
			position = positionOpt.get();
			position.setMenge(position.getMenge().add(amount));
			HertlBotRootDao.storageManager().store(position);
		} else
		{
			position = new HertlBotPosition();
			position.setArtikel(artikel);
			position.setMenge(amount);
			bestellung.addPosition(position, HertlBotRootDao.storageManager());
		}
	}
}
