mkdir -p /home/bigmama/chatbot/data/chatbotvol-develop
chown 1001:1001 /home/bigmama/chatbot/data/chatbotvol-develop
docker volume create --opt type=none --opt o=bind --opt device=/home/bigmama/chatbot/data/chatbotvol-develop --name chatbotvol-develop
mkdir -p /home/bigmama/chatbot/data/chatbotvol-staging
chown 1001:1001 /home/bigmama/chatbot/data/chatbotvol-staging
docker volume create --opt type=none --opt o=bind --opt device=/home/bigmama/chatbot/data/chatbotvol-staging --name chatbotvol-staging
mkdir -p /home/bigmama/chatbot/data/chatbotvol-main
chown 1001:1001 /home/bigmama/chatbot/data/chatbotvol-main
docker volume create --opt type=none --opt o=bind --opt device=/home/bigmama/chatbot/data/chatbotvol-main --name chatbotvol-main
