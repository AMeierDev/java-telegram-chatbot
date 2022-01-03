/*
 * ChatBot Workshop
 * Copyright (C) 2020 Arne Meier
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.bigamgamen.java.telegrambots.hertlhendl;

import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.xml.sax.SAXException;

import com.google.common.annotations.VisibleForTesting;

import de.bigamgamen.java.helper.IOHelper;
import de.bigamgamen.java.telegrambots.hertlhendl.api.RightController;
import de.bigamgamen.java.telegrambots.hertlhendl.api.RoleController;
import de.bigamgamen.java.telegrambots.hertlhendl.builder.TelegramKeyBoardBuilder;
import de.bigamgamen.java.telegrambots.hertlhendl.controller.HertlRightController;
import de.bigamgamen.java.telegrambots.hertlhendl.controller.HertlRoleController;
import de.bigamgamen.java.telegrambots.hertlhendl.dal.HertlBotRootDao;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotArticle;
import de.bigamgamen.java.telegrambots.hertlhendl.domain.HertlBotOrder;
import de.bigamgamen.java.telegrambots.hertlhendl.helper.OrderHelper;
import de.bigamgamen.java.telegrambots.hertlhendl.helper.TelegramHelper;
import de.bigamgamen.java.telegrambots.hertlhendl.init.InitArticles;

public class HertlHendlBot extends AbilityBot
{

	public static final String ABILITY_NAME_HELP = "help";
	public static final String ABILITY_NAME_LOCATION_PHOTO = "standortefoto";
	public static final String ABILITY_NAME_PRICES_PHOTO = "preisefoto";
	public static final String ABILITY_NAME_ORDER = "bestellung";
	public static final String ABILITY_NAME_ITEM_LIST = "artikel";
	public static final String ABILITY_NAME_ADD_POSITION = "addposition";
	public static final String ABILITY_NAME_LIST_MY_ORDERS = "bestellungenauflistung";
	public static final String ABILITY_NAME_MY_ORDERS_AS_KEYBOARD = "bestellungenkeyboard";
	public static final String ABILITY_NAME_NEW_ORDER = "neuebestellung";
	public static final String ABILITY_NAME_MY_OPEN_ORDERS = "meineoffnenebestellungen";
	public static final String ABILITY_NAME_ADMIN_OPEN_ORDERS = "adminoffnenebestellungen";
	public static final String ABILITY_NAME_ADMIN_CLOSE_ORDERS = "admincloseorders";
	public static final String ABILITY_NAME_CLOSE_ORDER = "closeorder";
	public static final String ABILITY_NAME_COMMIT_ORDER = "commitorder";

	private static final String MESSAGE_CLOSE_SUCCESSFULL = "Die Bestellung wurde erfolgreich geschlossen.";
	private static final String ALL_MESSAGE_CLOSE_SUCCESSFULL = "Alle offenen Bestellungen wurde erfolgreich geschlossen.";
	private static final String MESSAGE_CLOSE_ALREADY_CLOSED = "Die Bestellung ist bereits geschlossen.";
	private static final String MESSAGE_ALREADY_COMMITED = "Die Bestellung wurde bereits bestätigt";
	private static final String MESSAGE_COMMIT_SUCCESSFULL = "Die Bestellung wurde erfolgreich bestätigt";

	private static final String HENDL_PREISE_JPG = "hendl_preise.jpg";
	private final static Logger LOG = LoggerFactory.getLogger(HertlHendlBot.class);
	private final static String BOT_TOKEN = "";
	private final static String BOT_USERNAME = "";
	private static Integer CREATOR_ID = 0;
	private static String HERTL_URL = "https://hertel-haehnchen.de/standplatzsuche?search=92637";
	private static HertlBotRootDao hertlBotDao;

	private final TelegramKeyBoardBuilder keyBoardBuilder;
	private final RightController rightController;
	private RoleController roleController;
	private final Long creatorId;

	public static void main(final String[] args)
			throws ParserConfigurationException, SAXException, IOException, URISyntaxException, TelegramApiException
	{
		LOG.info("HertlHendlBot starting");

		final String token = args[0] != null ? args[0] : BOT_TOKEN;
		final String username = args[1] != null ? args[1] : BOT_USERNAME;
		final Long creatorId = args[2] != null ? Long.valueOf(args[2]) : CREATOR_ID;
		final HertlHendlBot bot = new HertlHendlBot(token, username, creatorId);
		TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
		api.registerBot(bot);
		LOG.info("HertlHendlBot successfull started");
	}

	public HertlHendlBot(final String botToken, final String botUsername, Long creatorId)
			throws ParserConfigurationException, SAXException, IOException, URISyntaxException
	{
		super(botToken, botUsername);
		hertlBotDao = new HertlBotRootDao();
		InitArticles.initArtikels(hertlBotDao);
		this.keyBoardBuilder = new TelegramKeyBoardBuilder(hertlBotDao);
		this.rightController = new HertlRightController(creatorId);
		this.roleController = new HertlRoleController(rightController);
		this.creatorId = creatorId;
	}

	@Override
	public long creatorId()
	{
		return this.creatorId;
	}

	public Ability showHelp()
	{
		return Ability.builder().name(ABILITY_NAME_HELP).info("shows help").locality(ALL).privacy(PUBLIC)
				.action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_HELP))
					{
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						message.setText(keyBoardBuilder
								.createAbilityListForHelp(roleController.getAbilitiesForUser(context.user())));
						this.silent.execute(message);
					}
				}).build();
	}

	public Ability showOrder()
	{
		return Ability.builder().name(ABILITY_NAME_ORDER).info("zeigt eine bestimmte Bestellung").locality(ALL)
				.privacy(PUBLIC).input(1).action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_ORDER))
					{
						final int bestellId = Integer.parseInt(context.firstArg());
						final Long chatId = context.chatId();
						final HertlBotOrder bestellung = hertlBotDao.loadBestellung(chatId, TelegramHelper.getTotalUserName(context.user()), bestellId);
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						final String messageText = bestellung.toString() + System.lineSeparator()
								+ "Füge Positionen zu deiner Bestellung hinzu";

						message.setText(messageText);

						final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
						final List<KeyboardRow> keyboard = keyBoardBuilder
								.loadAndShowAllArticleForOrder(context.chatId(), bestellId);

						// activate the keyboard
						keyboardMarkup.setKeyboard(keyboard);
						message.setReplyMarkup(keyboardMarkup);

						this.silent.execute(message);
					}
				}).build();
	}

	public Ability showArticle()
	{
		return Ability.builder().name(ABILITY_NAME_ITEM_LIST).info("Listet alle Artikel auf").locality(ALL).privacy(PUBLIC)
				.action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_ITEM_LIST))
					{
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						message.setText(this.loadAndShowAllArticle());
						this.silent.execute(message);
					}
				}).build();
	}

	public Ability showMyOrders()
	{
		return Ability.builder().name(ABILITY_NAME_LIST_MY_ORDERS).info("Zeigt die eigenen Bestellungen").locality(ALL)
				.privacy(PUBLIC).action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_LIST_MY_ORDERS))
					{
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						message.setText(this.loadAndShowMyOrder(context.chatId(), TelegramHelper.getTotalUserName(context.user())));
						final ReplyKeyboardMarkup keyboardMarkup = keyBoardBuilder.buildOrderMarkup(context);
						message.setReplyMarkup(keyboardMarkup);
						this.silent.execute(message);
					}
				}).build();
	}
	
	public Ability showAdminOpenOrders()
	{
		return Ability.builder().name(ABILITY_NAME_ADMIN_OPEN_ORDERS).info("Zeigt die offenen Bestellungen").locality(ALL)
				.privacy(PUBLIC).action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_ADMIN_OPEN_ORDERS))
					{
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						message.setText(OrderHelper.getTotalOrder(hertlBotDao.loadOpenOrdersForToday()).toString());
						
						this.silent.execute(message);
					}
				}).build();
	}
	
	public Ability adminCloseOrders()
	{
		return Ability.builder().name(ABILITY_NAME_ADMIN_CLOSE_ORDERS).info("Schliest alle offnen Bestellungen für Heute").locality(ALL)
				.privacy(PUBLIC).action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_ADMIN_CLOSE_ORDERS))
					{
						 SendMessage messageOrder = new SendMessage();
						 messageOrder.setChatId(Long.toString(context.chatId()));
						hertlBotDao.loadOpenOrdersForToday().stream().forEach(order -> closeOrder(order, messageOrder));;

						
						SendMessage message = new SendMessage();
						message.setText(ALL_MESSAGE_CLOSE_SUCCESSFULL);
						message.setChatId(Long.toString(context.chatId()));
						this.silent.execute(message);
					}
				}).build();
	}

	public Ability showMyOrderKeyBoard()
	{

		return Ability.builder().name(ABILITY_NAME_MY_ORDERS_AS_KEYBOARD)
				.info("Zeigt die eigenen Bestellungen als keyboard").locality(ALL).privacy(PUBLIC).action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_MY_ORDERS_AS_KEYBOARD))
					{
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						message.setText("Öffne die Bestellungen über die Tastatur: ");

						final ReplyKeyboardMarkup keyboardMarkup = keyBoardBuilder.buildOrderMarkup(context);
						message.setReplyMarkup(keyboardMarkup);

						this.silent.execute(message);
					}
				}).build();
	}

	public Ability createNewOrder()
	{

		return Ability.builder().name(ABILITY_NAME_NEW_ORDER).info("Erstellt eine neue Bestellung").locality(ALL)
				.privacy(PUBLIC).action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_NEW_ORDER))
					{
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						message.setText(keyBoardBuilder.createAndShowNewOrder(context.chatId(), TelegramHelper.getTotalUserName(context.user())));
						final ReplyKeyboardMarkup keyboardMarkup = keyBoardBuilder.buildOrderMarkup(context);
						message.setReplyMarkup(keyboardMarkup);
						this.silent.execute(message);
					}
				}).build();
	}

	public Ability addPositionToOrder()
	{

		return Ability.builder().name(ABILITY_NAME_ADD_POSITION).info("Fügt eine Position zu einer Bestellung hinzu")
				.locality(ALL).privacy(PUBLIC).input(2).action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_ADD_POSITION))
					{
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						message.setText(this.createPositionForOrder(context.firstArg(), context.chatId(), TelegramHelper.getTotalUserName(context.user()),
								Integer.valueOf(context.secondArg())));
						this.silent.execute(message);
					}
				}).build();
	}

	public Ability sendKeyboard()
	{
		return Ability.builder().name("keyboard").info("send a custom keyboard").locality(ALL).privacy(PUBLIC)
				.action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_HELP))
					{
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));
						message.setText("Enjoy this wonderful keyboard!");

						final ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
						final List<KeyboardRow> keyboard = new ArrayList<>();

						// row 1
						final KeyboardRow row = new KeyboardRow();
						row.add(keyBoardBuilder.createKeyForAbility(ABILITY_NAME_PRICES_PHOTO));
						row.add(keyBoardBuilder.createKeyForAbility(ABILITY_NAME_LOCATION_PHOTO));
						keyboard.add(row);

						// activate the keyboard
						keyboardMarkup.setKeyboard(keyboard);
						message.setReplyMarkup(keyboardMarkup);

						this.silent.execute(message);
					}
				}).build();
	}

	public Ability closeOrderAbility()
	{

		return Ability.builder().name(ABILITY_NAME_CLOSE_ORDER).info("Schließt die Bestellung ab.").locality(ALL)
				.privacy(PUBLIC)
				.input(1)
				.action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_CLOSE_ORDER))
					{
						final int bestellId = Integer.parseInt(context.firstArg());
						final Long chatId = context.chatId();
						final HertlBotOrder bestellung = hertlBotDao.loadBestellung(chatId, TelegramHelper.getTotalUserName(context.user()), bestellId);
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));

						closeOrder(bestellung, message);

						this.silent.execute(message);
					}
				}).build();
	}
	
	private void closeOrder(final HertlBotOrder bestellung, final SendMessage message)
	{
			if (bestellung.isClosed())
							{
	
								message.setText(MESSAGE_CLOSE_ALREADY_CLOSED);
							} else
							{
								bestellung.setClosed(true);
								HertlBotRootDao.storageManager().store(bestellung);
								message.setText(MESSAGE_CLOSE_SUCCESSFULL);
							}
	}
	
	public Ability commitOrder()
	{

		return Ability.builder().name(ABILITY_NAME_COMMIT_ORDER).info("Bestätigt die Bestellung.").locality(ALL)
				.privacy(PUBLIC)
				.input(1)
				.action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_COMMIT_ORDER))
					{
						final int bestellId = Integer.parseInt(context.firstArg());
						final Long chatId = context.chatId();
						final HertlBotOrder bestellung = hertlBotDao.loadBestellung(chatId, TelegramHelper.getTotalUserName(context.user()), bestellId);
						final SendMessage message = new SendMessage();
						message.setChatId(Long.toString(context.chatId()));

						if (bestellung.isCommited())
						{

							message.setText(MESSAGE_ALREADY_COMMITED);
						} else
						{
							bestellung.setCommited(true);
							HertlBotRootDao.storageManager().store(bestellung);
							message.setText(MESSAGE_COMMIT_SUCCESSFULL);
						}

						this.silent.execute(message);
					}
				}).build();
	}

	public Ability showPricePhoto()
	{
		return Ability.builder().name(ABILITY_NAME_PRICES_PHOTO).info("send Preisfoto").locality(ALL).privacy(PUBLIC)
				.action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_PRICES_PHOTO))
					{
						this.sendPhotoFromUpload(HENDL_PREISE_JPG, context.chatId());
					}
				}).build();
	}

	public Ability showLocationPhoto()
	{
		return Ability.builder().name(ABILITY_NAME_LOCATION_PHOTO).info("standorteFoto Weiden").locality(ALL)
				.privacy(PUBLIC).action(context ->
				{
					if (roleController.canUseAbility(context.user(), ABILITY_NAME_LOCATION_PHOTO))
					{
						this.makeScreenshotSenditDeleteit(context.chatId());
					}
				}).build();
	}

	private void makeScreenshotSenditDeleteit(final Long chatId)
	{
		final String fileName = this.makingScreenshotOfHertlHomepage();
		this.sendPhotoFromUpload(fileName, chatId);
		final File fileToDelete = new File(fileName);
		fileToDelete.delete();
	}

	private void sendPhotoFromUpload(final String filePath, final Long chatId)
	{
		final SendPhoto sendPhotoRequest = new SendPhoto(); // 1
		sendPhotoRequest.setChatId(Long.toString(chatId)); // 2
		try
		{
			sendPhotoRequest.setPhoto(new InputFile(IOHelper.findResource(filePath), filePath));
		} catch (final Exception e1)
		{
			LOG.error("Fehler beim schicken des Photos:{}", e1);
		}
		
		try
		{
			this.execute(sendPhotoRequest); // 4
		}
		catch(final TelegramApiException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Make an Screenshot of the Hmepage with docker. Works only on Linux
	 * 
	 * @return Fullqualified Filename to load from Hdd.
	 */
	private String makingScreenshotOfHertlHomepage()
	{
		final ProcessBuilder processBuilder = new ProcessBuilder();

		final String hertlTimeStampFileName = "hertl_standorteFoto"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm-ss-SSS")) + ".png";

		try
		{

			// -- Linux --
			final String command = "sudo docker run --rm -v $PWD:/srv lifenz/docker-screenshot " + HERTL_URL + " "
					+ hertlTimeStampFileName + " 1500px 2000 1";
			System.out.println(command);
			// Run a shell command
			processBuilder.command("bash", "-c", command);

			final Process process = processBuilder.start();
			System.out.println("Docker gestartet");

			final StringBuilder output = new StringBuilder();

			final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null)
			{
				output.append(line + "\n");
			}

			final int exitVal = process.waitFor();
			System.out.println("exitvalue: " + exitVal);
			if (exitVal == 0)
			{
				System.out.println("Success!");
				System.out.println(output);
				return hertlTimeStampFileName;
			} else
			{
				return null;
			}

		} catch (final IOException e)
		{
			e.printStackTrace();
		} catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		return "";

	}

	@VisibleForTesting
	void setSender(final MessageSender sender)
	{
		this.sender = sender;
	}

	@VisibleForTesting
	void setSilent(final SilentSender silent)
	{
		this.silent = silent;
	}

	private String createPositionForOrder(final String artikelName, final Long chatId, String userName,
			final Integer bestellungId)
	{
		final HertlBotArticle artikel = hertlBotDao.root().artikels().ofName(artikelName);
		final HertlBotOrder bestellung = hertlBotDao.loadBestellung(chatId, userName, bestellungId);
		OrderHelper.addArticleToOrder(artikel,BigInteger.valueOf(1L), bestellung);

		return this.loadAndShowOrder(chatId,userName, bestellungId);

	}

	

	private String loadAndShowOrder(final Long chatId, String userName, final int bestellId)
	{
		final HertlBotOrder bestellung = hertlBotDao.loadBestellung(chatId, userName, bestellId);
		return bestellung.toString();
	}

	public String loadAndShowMyOrder(final Long chatId ,String userName)
	{
		final StringBuilder sb = new StringBuilder(
				"Ihre Bestellungen:" + System.lineSeparator() + System.lineSeparator());
		HertlHendlBot.hertlBotDao.loadUser(chatId, userName).getBestellungen()
				.forEach(bestellung -> sb.append(loadAndShowOrder(chatId, userName, bestellung.getIndex()))
						.append(System.lineSeparator() + System.lineSeparator()));
		return sb.toString();
	}

	private String loadAndShowAllArticle()
	{
		final StringBuilder sb = new StringBuilder();
		hertlBotDao.root().artikels().all()
				.forEach(artikel -> sb.append(artikel.toString()).append(System.lineSeparator()));
		return sb.toString();
	}

}
