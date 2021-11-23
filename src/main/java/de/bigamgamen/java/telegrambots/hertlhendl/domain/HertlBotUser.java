package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.util.ArrayList;
import java.util.List;

import one.microstream.persistence.types.Persister;

public class HertlBotUser {

	private Long chatId;
	private String userName;
	private List<HertlBotOrder> bestellungen = new ArrayList<>();

	public HertlBotUser(Long chatId, String userName) {
		this.chatId = chatId;
		this.userName = userName;
	}
	
	public synchronized void addBestellung(HertlBotOrder bestellung, final Persister persister)
	{
		bestellung.setUser(this);
		this.bestellungen.add(bestellung);
		bestellung.setIndex(this.bestellungen.indexOf(bestellung));
		persister.store(bestellungen);
	}
	
	public boolean isRightUser(Long chatId)
	{
		return this.chatId.equals(chatId);
	}
	
	

	public List<HertlBotOrder> getBestellungen() {
		return new ArrayList<>(bestellungen);
	}
	
	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

	public Long getChatId() {
		return chatId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

}
