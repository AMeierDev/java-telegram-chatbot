package de.bigamgamen.java.telegrambots.hertlhendl.init;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotArticle;

public class InitArticles
{

	private InitArticles()
	{

	}

	public synchronized static void initArtikels(final HertlBotRootDao dao)
	{
		final List<HertlBotArticle> artikelList = new ArrayList<>();
        // Die Namen Dürfen keine Leerzeichen enthalten !!!!
		artikelList.add(new HertlBotArticle(1, "1/2-Hähnchen", BigInteger.valueOf(490L)));
		artikelList.add(new HertlBotArticle(2, "Hähnchen-Schenkel", BigInteger.valueOf(290L)));
		artikelList.add(new HertlBotArticle(3, "Krautsalat", BigInteger.valueOf(200L)));        
		artikelList.add(new HertlBotArticle(4, "Brezel-klein", BigInteger.valueOf(100L)));
		artikelList.add(new HertlBotArticle(5, "Brezel-groß", BigInteger.valueOf(170L)));
        artikelList.add(new HertlBotArticle(6, "Kartoffelsalat-Essig-Öl", BigInteger.valueOf(200L)));
        artikelList.add(new HertlBotArticle(7, "Kartoffelsalat-Majo", BigInteger.valueOf(200L)));

        dao.root().artikels().clearAll();
        
		dao.root().artikels().addAll(artikelList);
	}

}
