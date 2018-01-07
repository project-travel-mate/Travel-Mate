# Travel Mate

[![Build Status](https://travis-ci.org/Swati4star/Travel-Mate.svg?branch=master)](https://travis-ci.org/Swati4star/Travel-Mate)

A must-have app for all the people who want to travel to a new city. The app provides them with everything from **choosing the correct destination** to **making all the bookings** and to **easily organizing the trip**. The platform basically uses a [mash-up technology](https://en.wikipedia.org/wiki/Mashup_(web_application_hybrid)). We have tried to include solutions for every possible problem the traveller might face during the course of his entire journey.


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

While travelling from one place to another, there are a lot of factors to be considered in order to make the trip a memorable one. Our platform helps the traveller with anything and everything that he might need from the moment he plans the journey till the time he is back home happy and content.
The platform includes a wide variety of options - from selection of Mode of Transport, to finding out about the destination city, to provision of best music, novels, depending on the mood of the traveller. Travel Mate basically works in 3 phases. 

## Select Destination
A comprehensive list of all the information one would need to know about a city, be it current weather of the city or a list of best hangout places there. Along with the information, the current trend of the city on twitter can also be seen. (It is extracted from Twitter using Twitter and Yahoo! APIs).
Based on this information and the recommendation from the app, user can opt for the best destination location for him.

<img src="/screenshots/all_cities.png" width="200px"> <img src="/screenshots/one_city.png" width="200px"> <img src="/screenshots/fact.png" width="200px">

<img src="/screenshots/city_here.png" width="200px"> <img src="/screenshots/trend.png" width="200px">


## Travel
### My trips
History of all the past trips of the user at one place so that he can categorically view all the photos from his trips and also see how much each person (who was on that trip along with the user) spent on different things for future reference.

<img src="/screenshots/travel.png" width="200px"> <img src="/screenshots/trips.png" width="200px"> <img src="/screenshots/mytrip_info.png" width="200px">


### Transport
Book and search for bus and railway tickets from this app itself. For car, we let the user know about the total toll taxes and the total cost of fuel he has to bear.

<img src="/screenshots/transport.png" width="200px"> <img src="/screenshots/trains.png" width="200px">


### Hotel Booking
<img src="/screenshots/hotel_book.png" width="200px">

### Online Shopping

<img src="/screenshots/shopping.png" width="200px">



### Real Time Locator
While travelling in the destination city, user usually doesn't know much about the nearby location. We provide the users with real time details about the nearby places, monuments, restaurants, hotels (so that, he can also take a detour) and medical centres.

<img src="/screenshots/here.png" width="200px"> <img src="/screenshots/here_filter.png" width="200px">

## Utilities




### Share contact
To save the job of typing and then saving the contact details of a fellow passenger, simply share the contacts using a unique QR code.

<img src="/screenshots/share_my_contact.png" width="200px">


### Checklist
We need a checklist because remembering everything required for your perfect trip is quite cumbersome.

<img src="/screenshots/checklist.png" width="200px">


## Push notification
Push notification in between the journey. Often it happens that while travelling alone, a person tends to doze off. Push notification (alarm notification) feature saves the traveller from missing out the destination station.


<img src="/screenshots/notification.png" width="200px">



## Estimote Beacons
Estimote Beacons are small wireless sensors that we can attach to any location or object. They broadcast tiny radio signals which our smartphone can receive and interpret, unlocking micro-location and contextual awareness.

<img src="http://www.mjdinteractive.com/wp-content/uploads/2013/12/estimote-beacons1.jpg" width="400px" height="200px" />



One Estimote beacon is placed in each monument and with that, travellers can get numerous facilities. The data related to that monument, along with the Estimote beacon ID, are uploaded on the cloud MySQL database.

## Features

### 1.Notify the user as soon as he enters proximity
As soon as the user with Travel Mate app installed comes in proximity (~70m) of some monument (detected by estimote beacon), he receives a push notification welcoming him to the store. This will help the travellers to get to know about the nearby monuments.

<img src="/screenshots/welcome.png" width="200px"> <img src="/screenshots/bye.png" width="200px">

### 2.Display details of Monument
As soon as the estimote beacon finds a Travel Mate app, it triggers and the phone fetches all the the information about the monument from the cloud and are presented to the traveller. The key benefit of adding this is that the user does not need to make extra efforts to know about the monument, he/she gets everything at one go.

<img src="/screenshots/after_estimote.png" width="200px">


## Technical Requirements

Technically, all Bluetooth Smart-enabled android devices could pick up Bluetooth Low Energy signals. The Bluetooth Special Interest Group maintains a [list of devices](https://www.bluetooth.com/what-is-bluetooth-technology/bluetooth-devices) that support Bluetooth Low Energy. These include Android devices like: Samsung Galaxy S devices, Google Nexus, Google Glass and many more.

## How does Estitmote Beacon work

![](https://raw.githubusercontent.com/Swati4star/Hackathon-airtel/master/screenies/Screenshot%20from%202016-01-17%2001%3A58%3A26.png)




## Contact Us

Feel free to [open an issue](https://github.com/Swati4star/Travel-Mate/issues) for any setup query
