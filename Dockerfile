FROM eclipse-temurin:17-jammy
LABEL maintainer="AMDev"

RUN groupadd --system -g 1001 telegrambot && adduser --uid 1001 --gid 1001 --disabled-password --shell  /bin/false telegrambot

COPY --chown=telegrambot:telegrambot target/lib /opt/telegrambot/lib/
COPY --chown=telegrambot:telegrambot target/java.telegrambots.jar /opt/telegrambot/
RUN chown telegrambot:telegrambot /opt/telegrambot
RUN chmod +x /opt/telegrambot/java.telegrambots.jar
# Set the locale
RUN sed -i '/de_DE.UTF-8/s/^# //g' /etc/locale.gen && \
    locale-gen
ENV LANG de_DE.UTF-8  
ENV LANGUAGE de_DE:en  
ENV LC_ALL de_DE.UTF-8     

WORKDIR /opt/telegrambot/
 
USER telegrambot

#CMD ["echo $UID"]

ENTRYPOINT ["sh" , "-c", "java -cp java.telegrambots.jar:./lib/* de.bigamgamen.java.telegrambots.hertlhendl.HertlHendlBot ${BOT_TOKEN}:${BOT_KEY} ${BOT_NAME} ${CREATOR_ID} ${CREATOR_PAYPAL_EMAIL}"]
