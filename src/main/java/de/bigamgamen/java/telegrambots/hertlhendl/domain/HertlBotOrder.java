package de.bigamgamen.java.telegrambots.hertlhendl.domain;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.bigamgamen.java.helper.Pricehelper;
import org.eclipse.serializer.persistence.types.Persister;


public class HertlBotOrder {

	private static final String ORDER_UNCOMMITED = "UNBESTÄTIGT";
	private static final String ORDER_COMMITED = "BESTÄTIGT";
	private static final String ORDER_CLOSED = "GESCHLOSSEN";
	private static final String ORDER_OPEN = "OFFEN";
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
		this.positionen.add(position);
		persister.store(position);
		persister.store(this.positionen);
	}

	
	public String getBestellDatumFormated() {
		return this.bestellDatum.format(DateTimeFormatter.ofPattern(DD_MM_YYYY));
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(ORDER_TITLE + " " + this.index);
		sb.append(" Für: " + this.getUser().getUserName())
		.append(" Vom: " + this.getBestellDatumFormated() + ". "
			+ (this.isCommited() ? ORDER_COMMITED : ORDER_UNCOMMITED)
			+ "\n und "
			+ (this.isClosed() ? ORDER_CLOSED: ORDER_OPEN)
			+ System.lineSeparator());
		this.positionen.forEach(pos -> sb.append(pos.toString()+System.lineSeparator()));
		sb.append("Summe: "+ this.getSumme());
		return sb.toString();
	}


	public BigInteger getBigIntegerSumme() {
		BigInteger summe = new BigInteger("0");
		for(final HertlBotPosition pos : this.positionen)
		{
			summe = summe.add(pos.getPositionPrice());
		}
		return summe;
	}

	private String getSumme() {
		return Pricehelper.getPriceAsEuroString(this.getBigIntegerSumme());
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
		return this.closed;
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
	
	public String getPayPalDescription()
	{
		final StringBuilder sb = new StringBuilder();
		this.positionen.forEach(pos -> sb.append(pos.toString()+","));

		return URLEncoder.encode(sb.toString(), StandardCharsets.UTF_8);
	}



}
