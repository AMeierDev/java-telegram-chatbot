package de.bigamgamen.java.telegrambots.hertlhendl.domain;

public class HertlBotRoot {
	private HertlBotUsers users = new HertlBotUsers();
	private HertlBotArticles artikels = new HertlBotArticles();
	
	public HertlBotRoot()
	{
		
	}

	public HertlBotUsers users() {
		return users;
	}

	public HertlBotArticles artikels() {
		return artikels;
	}

}
