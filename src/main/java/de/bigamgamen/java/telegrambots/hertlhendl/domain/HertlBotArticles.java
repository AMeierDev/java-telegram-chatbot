package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import one.microstream.persistence.types.Persister;

public class HertlBotArticles {

	private final Map<String, HertlBotArticle> artikels = new HashMap<>();

	public HertlBotArticles() {
		super();
	}
	
	
	
	public void add(
		final HertlBotArticle artikel,
		final Persister persister
	)
	{
		this.artikels.put(artikel.getName(), artikel);
		persister.store(this.artikels);
	}

	public void addAll(final Collection<? extends HertlBotArticle> HertlBotArtikels)
	{
		this.addAll(HertlBotArtikels, HertlBotRootDao.storageManager());
	}
	
	public void addAll(
		final Collection<? extends HertlBotArticle> artikels,
		final Persister persister
	)
	{
		this.artikels.putAll(
				artikels.stream().collect(
				Collectors.toMap(HertlBotArticle::getName, Function.identity())
			)
		);
		persister.store(this.artikels);
	}
	
	public int HertlBotArtikelCount()
	{
		return this.artikels.size();
	}

	public List<HertlBotArticle> all()
	{
		return new ArrayList<>(this.artikels.values());
	}

	public HertlBotArticle ofName(final String artikelName)
	{
		return this.artikels.get(artikelName);
	}
    
    public void clearAll()
	{
		this.clearAll(HertlBotRootDao.storageManager());
	}
    
    public void clearAll(
		final Persister persister
	)
	{
		this.artikels.clear();
		persister.store(this.artikels);
	}
	
	

}
