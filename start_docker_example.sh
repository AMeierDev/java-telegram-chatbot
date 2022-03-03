#Production
docker run -d --restart unless-stopped --name chatbot -v chatbotvol:/opt/telegrambot/data --env-file ./env.list amgamen/chatbot

#Develop
docker run -it -v chatbotvol:/opt/telegrambot/data  --env-file ./env.list amgamen/chatbot
docker run -v chatbotvol:/opt/telegrambot/data --restart unless-stopped --name bot-develop -d -e "BOT_TOKEN=2103518689" -e "BOT_KEY=" -e "BOT_NAME=" -e "CREATOR_ID=" -e "CREATOR_PAYPAL_EMAIL=" amgamen/chatbot:develop
