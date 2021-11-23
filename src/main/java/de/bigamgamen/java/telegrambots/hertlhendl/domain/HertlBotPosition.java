package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.math.BigInteger;

public class HertlBotPosition {
	private BigInteger menge = BigInteger.valueOf(1L);
	private HertlBotArticle artikel;
	private HertlBotOrder bestelllung;
	
	public HertlBotPosition() {

	}

	public HertlBotPosition(BigInteger menge, HertlBotArticle artikel) {
		this.menge = menge;
		this.artikel = artikel;
	}
	
	@Override
	public String toString() {
		String positionString = menge + "-mal "+ artikel.getName();
		return positionString;
	}

	public BigInteger getPositionPrice() {
		return artikel.getPriceInCent().multiply(menge);
	}

	public BigInteger getMenge() {
		return this.menge;
	}

	public void setMenge(final BigInteger menge) {
		this.menge = menge;
	}

	public HertlBotArticle getArtikel() {
		return this.artikel;
	}

	public void setArtikel(final HertlBotArticle artikel) {
		this.artikel = artikel;
	}

	public HertlBotOrder getBestelllung()
	{
		return this.bestelllung;
	}

	public void setBestelllung(final HertlBotOrder bestelllung)
	{
		this.bestelllung = bestelllung;
	}

	
	
}
