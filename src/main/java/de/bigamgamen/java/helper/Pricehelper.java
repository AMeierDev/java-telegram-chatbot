package de.bigamgamen.java.helper;

import java.math.BigInteger;
import java.text.NumberFormat;

public class Pricehelper {
	private Pricehelper() {

	}

	public static String getPriceAsEuroString(BigInteger priceInCent) {
		return NumberFormat.getCurrencyInstance().format(priceInCent.doubleValue() / 100);
	}
}
