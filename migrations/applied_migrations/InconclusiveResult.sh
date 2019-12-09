#!/bin/bash

echo ""
echo "Applying migration InconclusiveResult"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /inconclusiveResult                       controllers.InconclusiveResultController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "inconclusiveResult.title = inconclusiveResult" >> ../conf/messages.en
echo "inconclusiveResult.heading = inconclusiveResult" >> ../conf/messages.en

echo "Migration InconclusiveResult completed"
