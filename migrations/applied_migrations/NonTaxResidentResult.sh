#!/bin/bash

echo ""
echo "Applying migration NonTaxResidentResult"

echo "Adding routes to conf/app.routes"
echo "" >> ../conf/app.routes
echo "GET        /nonTaxResidentResult                       controllers.NonTaxResidentResultController.onPageLoad()" >> ../conf/app.routes

echo "Adding messages to conf.messages"
echo "" >> ../conf/messages.en
echo "nonTaxResidentResult.title = nonTaxResidentResult" >> ../conf/messages.en
echo "nonTaxResidentResult.heading = nonTaxResidentResult" >> ../conf/messages.en

echo "Migration NonTaxResidentResult completed"
