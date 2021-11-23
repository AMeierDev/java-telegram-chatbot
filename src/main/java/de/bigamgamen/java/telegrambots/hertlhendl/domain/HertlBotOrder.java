package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.bigamgamen.java.helper.Pricehelper;
import one.microstream.persistence.types.Persister;

public class HertlBotOrder {
	
	private static final String ORDER_COMMITED = "BESTÄTIGT";
	private static final String ORDER_CLOSED = "GESCHLOSSEN";
	private static final String ORDER_TITLE = "Ihre Bestellung:";
	private static final String DD_MM_YYYY = "dd-MM-yyyy";
	private int index;
	private LocalDate bestellDatum;
	private HertlBotUser user;
	private List<HertlBotPosition> positionen = new ArrayList<>();
	private Boolean commited = false;
	private Boolean closed = false;
	
	public HertlBotOrder() {
		
	}
	
	public HertlBotOrder(final HertlBotUser user, final List<HertlBotPosition> positionen) {
		this.bestellDatum = LocalDate.now();
		this.user = user;
		this.positionen = positionen;
	}
	
	public synchronized void addPosition(HertlBotPosition position, final Persister persister)
	{
		positionen.add(position);	
		persister.store(position);
		persister.store(positionen);
	}
	

	public String getBestellDatumFormated() {
		return this.bestellDatum.format(DateTimeFormatter.ofPattern(DD_MM_YYYY));
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(ORDER_TITLE + " " +this.index);
		sb.append(" Für: " + this.getUser().getUserName())
			.append(" Vom: " + this.getBestellDatumFormated() + "." 
		+ (isCommited() ? ORDER_COMMITED + " und ":"")
		+ (isClosed() ? ORDER_CLOSED:"") 
		+ System.lineSeparator());
		this.positionen.forEach(pos -> sb.append(pos.toString()+System.lineSeparator()));
		sb.append("Summe: "+ this.getSumme());
		return sb.toString();
	}
	
	private String getSumme() {
		 BigInteger summe = new BigInteger("0");
		for(HertlBotPosition pos : positionen)
		{
			summe = summe.add(pos.getPositionPrice());
		}
		return Pricehelper.getPriceAsEuroString(summe);
	}

	public HertlBotUser getUser() {
		return this.user;
	}
	public void setUser(final HertlBotUser user) {
		this.user = user;
	}
	public List<HertlBotPosition> getPositionen() {
		return this.positionen;
	}
	public void setPositionen(final List<HertlBotPosition> positionen) {
		this.positionen = positionen;
	}

	public LocalDate getBestellDatum() {
		return this.bestellDatum;
	}

	public void setBestellDatum(final LocalDate bestellDatum) {
		this.bestellDatum = bestellDatum;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public Boolean isClosed()
	{
		return closed;
	}

	public void setClosed(Boolean closed)
	{
		this.closed = closed;
	}
	
	public Boolean isCommited()
	{
		return this.commited;
	}
	
	public void setCommited(Boolean commited)
	{
		this.commited = commited;
	}
	
	
	
}
