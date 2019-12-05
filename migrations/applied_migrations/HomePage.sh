#!/bin/bash

echo ""
echo "Applying migration HomePage"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /homePage                       controllers.HomePageController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "homePage.title = homePage" >> ../conf/messages.en
echo "homePage.heading = homePage" >> ../conf/messages.en

echo "Migration HomePage completed"
