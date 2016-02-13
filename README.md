#Travel Mate

A must-have app for all the travellers who are visiting a city for the first time. The app provides them with everything **from Planning to their Journey** and also from **from Journey to the destination**. The platform basically uses a [mash-up technology](https://en.wikipedia.org/wiki/Mashup_(web_application_hybrid)). We have tried to include solutions for every possible problem the traveller might face during the course of his entire journey.


+ **[How Travel Mate Works](#how-travel-mate-works)**
+ **[Travel Phases](#phases)**
  + [Planning Phase](#1planning-phase)
  + [Travel Phase](#2travel-phase)
  + [Trip Phase](#3trip-phase)
+ **[Exclusive Feature : Estimote Beacons](#exclusive-feature)**
  + [Notify the user as soon as he enters proximity](#1notify-the-user-as-soon-as-he-enters-proximity)
  + [Display details of Monument](#2display-details-of-monument)
+ **[Technical Requirements](#technical-requirements)**
+ **[How does Estitmote Beacon work](#how-does-estitmote-beacon-work)**
+ **[Contact Us](#contact-us)**
+ **[Video URL](https://www.youtube.com/watch?v=D7fZXdUryeM)**
+ **[Presentation](https://docs.google.com/presentation/d/1gdx3OZjdIcqVqsgAj2jz-REFT7xI716d6iNE_1XBg4A/edit?usp=sharing)**





##How Travel Mate Works

While travelling from one place to another, there are a lot of factors to be considered in order to make the trip a memorable one. Our platform helps the traveller with anything and everything that he might need from the moment he plans the journey till the time he is back home happy and content.
The platform includes a wide variety of options - from selection of Mode of Transport, to finding out about the destination city to provision of best music, novels depending on the mood of the traveller. Travel Mate basically works in 3 phases. 

##Phases
###1.Planning Phase
Before you start your trip, we give you options across the country. You can “virtually” explore the city through our app. Choose your destination city and get to know about many important things in the city. Be it : Fun Facts, Climate, Culture, Restaurants, Hangout Places, Monuments (if any), Shopping Complexes; Travel Mate has all. The user interface of the platform is kept fun and colorful in order to keep the user engaged in the app. Obviously, people travel with their friends, so we have share option at each step, by which everyone can share the all the facts amongst themselves.

<img src="/screenshots/plan_journey.png" width="200px">
<img src="/screenshots/change_source_dest.png" width="200px">
<img src="/screenshots/city_info.png" width="200px">
<img src="/screenshots/city_info1.png" width="200px">
<img src="/screenshots/city_info2.png" width="200px">
<img src="/screenshots/fun_fact.png" width="200px">
<img src="/screenshots/share_fun_fact.png" width="200px">


Next comes the mode of transit. Compare the bus, trains and car travel prices from the platform itself. We have comprehensive list of various modes of transit sorted in the decreasing order of their user-rating. For car, we let the user know about the total toll taxes and the total cost of fuel he has to bear.

<img src="/screenshots/bus.png" width="200px">
<img src="/screenshots/car_directions.png" width="200px">
<img src="/screenshots/car_directions_share.png" width="200px">
<img src="/screenshots/trains.png" width="200px">

In pipeline, next, is shopping and searching for a suitable accomodation. Get the details of hotel from the app. Also, we need a checklist because remembering everything required for your perfect trip is quite cumbersome.

<img src="/screenshots/shop.png" width="200px">
<img src="/screenshots/hotel.png" width="200px">
<img src="/screenshots/checklist.png" width="200px">

###2.Travel Phase
Next phase is the phase in which you are travelling to your destination. Here we try to detect the mood of the user.

<img src="/screenshots/start_joruney.png" width="200px">
<img src="/screenshots/books.png" width="200px">

----

####Mood Detection
We try to find the current state of mind or the mood of our traveller on the basis of his activity and selections on the app and hence recommend him books and songs accordingly, **using Machine Learning**. The user interface of the app also changes according to traveller’s mood to make him comfortable.


<img src="/screenshots/music.png" width="200px">
<img src="/screenshots/music_sad.png" width="200px">

----

A very interesting feature of this app is - **Push notification** in between the journey. Often it happens that while travelling alone, a person tends to doze off. Push notification (alarm notification) feature saves the traveller from missing out the destination station.


<img src="/screenshots/notification.png" width="200px">


###3.Trip Phase
While travelling in the destination city, user usually doesn't know much about the nearby location. We provide the users with real time details about the nearby places, monuments, restaurants, hotels (so that, he can also take a detuor) and medical centres.

<img src="/screenshots/hotel_real.png" width="200px">
<img src="/screenshots/medical.png" width="200px">
<img src="/screenshots/monu.png" width="200px">
<img src="/screenshots/nearby_places.png" width="200px">
<img src="/screenshots/rest.png" width="200px">
<img src="/screenshots/emergency.png" width="200px">


## Exclusive Feature

###Estimote Beacons
Estimote Beacons are small wireless sensors that we can attach to any location or object. They broadcast tiny radio signals which our smartphone can receive and interpret, unlocking micro-location and contextual awareness.

<img src="http://www.mjdinteractive.com/wp-content/uploads/2013/12/estimote-beacons1.jpg" width="400px" height="200px" />



One Estimote beacon is placed in each monument and with that travellers can get numerous facilities. The data related to that monument, along with the Estimote beacon ID, are uploaded on the cloud MySQL database.

##Features

###1.Notify the user as soon as he enters proximity
As soon as the user with Travel Mate app installed comes in proximity (~70m) of some monument (detected by estimote beacon), he receives a push notification welcoming him to the store. This will help the travellers to get to know about the nearby monuments.


<img src="/screenshots/welcome.png" width="200px">
<img src="/screenshots/bye.png" width="200px">

###2.Display details of Monument
As soon as the estimote beacon finds a Travle Mate app it triggers and phone fetches all the the information about the monument from the cloud and are presented to the traveller. The key benefit of adding this is that user does not need to make extra efforts to know about the monument, he/she gets everything at one go.

<img src="/screenshots/after_estimote.png" width="200px">


## Technical Requirements

Technically, all Bluetooth Smart-enabled android devices could pick up Bluetooth Low Energy signals. The Bluetooth Special Interest Group maintains a [list of devices](https://www.bluetooth.com/what-is-bluetooth-technology/bluetooth-devices) that support Bluetooth Low Energy. These include Android devices like: Samsung Galaxy S devices, Google Nexus, Google Glass and many more.

## How does Estitmote Beacon work

![](https://raw.githubusercontent.com/Swati4star/Hackathon-airtel/master/screenies/Screenshot%20from%202016-01-17%2001%3A58%3A26.png)




## Contact Us

Feel free to contact us for any support, query, suggestion or even say hi.

**[Prabhakar Gupta](mailto:prabhakargupta267@gmail.com)**

**[Swati Garg](mailto:swati.garg.nsit@gmail.com)**
