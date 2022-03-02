package de.bigamgamen.java.helper;

import java.net.MalformedURLException;
import java.net.URL;

import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotOrder;

/**
 * generates a link like https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=greatsave@web.de&currency_code=EUR&amount=1&item_name=Knowledgebase%20Spende
 * @author Bigmama
 *
 */
public class PaypalLinkGenerator
{
	public static String DEFAULT_CURRENCY = "EUR";
	public static String CURRENCY_CODE = "&currency_code=";
	
	public static String BUSINESS = "&business="; // (Zahlungsempfänger) = PayPal E-Mail des Zahlungsempfängers
	public static String AMOUNT = "&amount="; // (Betrag) = 123.12 (Achtung, mit Punkt als Dezimaltrennzeichen)
	public static String ITEM_NAME = "&item_name="; // (Artikelname)
	public static String URL_START = "https://www.paypal.com/cgi-bin/webscr?cmd=_xclick";
	
	public PaypalLinkGenerator()
	{

	}
	
	public URL generatePayPalLinkForOrder(final HertlBotOrder order, String adminEmail) throws MalformedURLException
	{
		final StringBuilder sb = new StringBuilder(URL_START);
		sb.append(BUSINESS).append(adminEmail);
		sb.append(AMOUNT).append(Pricehelper.getPriceAsDotString(order.getBigIntegerSumme()));
		sb.append(ITEM_NAME).append(order.getPayPalDescription());
		sb.append(CURRENCY_CODE).append(DEFAULT_CURRENCY);

		final URL paylpalLink = new URL(sb.toString());
		return paylpalLink;
	}
	
}
