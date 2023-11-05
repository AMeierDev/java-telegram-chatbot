# How to

## Developer how to use:

Create your Ownbot :
<https://github.com/chatbot-workshop/java-telegram-workshop/blob/master/WORKSHOP.adoc>

Start the Bot like this.

    java -cp java.telegrambots-0.9-FINAL.jar:./lib/* de.bigamgamen.java.telegrambots.hertlhendl.HertlHendlBot TOKEN bot_name CREATOR_ID

Or test it with this bot: @herthaehnchen_bot

## User how to use:

type /help and choose a command.

Usecase: Make an order:

    /neuebestellung
    /bestellungenkeyboard

choose a Button

    /bestellung 0

add article to order with Buttons

    /addposition 1/2-Hähnchen 0
    /addposition Krautsalat 0

Commit your order

    /commitorder 0

## Admin how to use:

    /adminoffnenebestellungen

# Copyright and Licensing

Copyright © 2023 Arne Meier

Licensed under the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License. You may obtain
a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

See the [LICENSE](#LICENSE.md#) file for details.
