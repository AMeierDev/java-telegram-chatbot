#Production
docker run -d --restart unless-stopped --name chatbot -v chatbotvol:/opt/telegrambot/data --env-file ./env.list amgamen/chatbot

#Develop
docker run -it -v chatbotvol:/opt/telegrambot/data  --env-file ./env.list amgamen/chatbot
docker run -it -v chatbotvol:/opt/telegrambot/data  -e "BOT_TOKEN=" -e "BOT_KEY=" -e "BOT_NAME=" -e "CREATOR_ID=" amgamen/chatbot
