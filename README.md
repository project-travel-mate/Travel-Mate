# Travel Mate

[![Build Status](https://travis-ci.org/Swati4star/Travel-Mate.svg?branch=master)](https://travis-ci.org/Swati4star/Travel-Mate)

A must-have app for those interested in travel. The app provides users with various features from **choosing the correct destination** to **making all the bookings** and to **easily organizing the trip**. The platform basically uses a [mash-up technology](https://en.wikipedia.org/wiki/Mashup_(web_application_hybrid)). The app provides solutions for every possible problem a traveller might face during the course of his or her entire journey.


+ **[How Travel Mate Works](#how-travel-mate-works)**
+ **[Select Destination](#select-destination)**
+ **[Travel](#travel)**
  + [My trips](#my-trips)
  + [Transport](#transport)
  + [Hotel Booking](#hotel-booking)
  + [Online Shopping](#online-shopping)
  + [Real Time Locator](#real-time-locator)
+ **[Utilities](#utilities)**
  + [Share contact](#share-contact)
  + [Checklist](#checklist)
+ **[Push notification](#push-notification)**
+ **[Estimote Beacons](#estimote-beacons)**
+ **[Features](#features)**
  + [Notify the user as soon as he enters proximity](#1notify-the-user-as-soon-as-he-enters-proximity)
  + [Display details of Monument](#2display-details-of-monument)
+ **[Technical Requirements](#technical-requirements)**
+ **[How does Estitmote Beacon work](#how-does-estitmote-beacon-work)**
+ **[Contact Us](#contact-us)**




## How Travel Mate Works

While travelling from one place to another, there are a lot of factors to be considered to make the trip a memorable one. Our platform helps the traveller with anything and everything that he or she might need, from the moment he or she plans the journey till the time he or she is back home happy and content.
The platform includes a wide variety of options - from selection of Mode of Transport, to finding out about the destination city, to provision of best music, novels, depending on the mood of the traveller. Travel Mate basically works in 3 phases. 

## Select Destination
A comprehensive list of all the information one would need to know about a city, be it current weather of the city or a list of best hangout places there. Along with the information, the current trend of the city on twitter can also be seen. (It is extracted from Twitter using Twitter and Yahoo! APIs).
Based on this information and the recommendation from the app, user can opt for the preferred destination location.

<img src="./screenshots/all_cities.png" width="200px"> <img src="./screenshots/one_city.png" width="200px"> <img src="./screenshots/fact.png" width="200px">

<img src="./screenshots/city_here.png" width="200px"> <img src="./screenshots/trend.png" width="200px">


## Travel
### My trips
This option allows users to view their travel history, including pictures during each trip, names of accompanying induviduals, and details of expenses for future reference.

<img src="./screenshots/travel.png" width="200px"> <img src="./screenshots/trips.png" width="200px"> <img src="./screenshots/mytrip_info.png" width="200px">


### Transport
This option allows users to book bus and railway tickets, using the Travel Mate app. Users opting to travel by car are informed of the total toll and fuel charges they are likely to incur. 

<img src="./screenshots/transport.png" width="200px"> <img src="./screenshots/trains.png" width="200px">


### Hotel Booking
<img src="./screenshots/hotel_book.png" width="200px">

### Online Shopping
<img src="./screenshots/shopping.png" width="200px">



### Real Time Locator
For users travelling in unfamiliar cities, the app also provides real-time information about places around them as they travel through the city, including monuments, restaurants, hotels, medical centres, etc.


<img src="./screenshots/here.png" width="200px"> <img src="./screenshots/here_filter.png" width="200px">

## Utilities




### Share contact
Users can share their contact details with their co-passengers, by simply sharing the QR code made available on the Travel Mate app.  

<img src="./screenshots/share_my_contact.png" width="200px">


### Checklist
The app also includes an interactive checklist of items users need to carry with them on their trips. 

<img src="./screenshots/checklist.png" width="200px">


## Push notification
To prevent users from missing their destination station while travelling by train or bus, the app includes the push notification feature, that sounds an alarm when the users nears their respective destination stations. 
P

<img src="./screenshots/notification.png" width="200px">



## Estimote Beacons
Estimote Beacons are small wireless sensors that can be attached to any location or object. They broadcast tiny radio signals which smartphone can receive and interpret, unlocking micro-location and contextual awareness.

<img src="http://www.mjdinteractive.com/wp-content/uploads/2013/12/estimote-beacons1.jpg" width="400px" height="200px" />



One Estimote beacon is placed in each monument, emitting signals that can be detcted by users' smartphones. The data related to that monument, along with the Estimote beacon ID, are uploaded on the cloud MySQL database.

## Features

### 1.Notify the user as soon as he enters proximity
As soon as the user with Travel Mate app installed comes in proximity (~70m) of a monument (detected by estimote beacon), he  or she will receive a push notification welcoming him. 

<img src="./screenshots/welcome.png" width="200px"> <img src="./screenshots/bye.png" width="200px">

### 2.Display details of Monument
As soon as the estimote beacon finds a Travel Mate app, it triggers and the phone fetches all the the information about the monument from the cloud and are presented to the traveller. This saves the user the trouble of gathering information about places of interest beforehand.  
<img src="./screenshots/after_estimote.png" width="200px">


## Technical Requirements

Technically, all Bluetooth Smart-enabled android devices could pick up Bluetooth Low Energy signals. The Bluetooth Special Interest Group maintains a [list of devices](https://www.bluetooth.com/what-is-bluetooth-technology/bluetooth-devices) that support Bluetooth Low Energy. These include Android devices like: Samsung Galaxy S devices, Google Nexus, Google Glass and many more.

## How does Estitmote Beacon work

![](https://raw.githubusercontent.com/Swati4star/Hackathon-airtel/master/screenies/Screenshot%20from%202016-01-17%2001%3A58%3A26.png)




## Contact Us

Feel free to [open an issue](https://github.com/Swati4star/Travel-Mate/issues) for any setup query


## Stargazers over time

[![Stargazers over time](https://starcharts.herokuapp.com/Swati4star/Travel-Mate.svg)](https://starcharts.herokuapp.com/Swati4star/Travel-Mate)
      
