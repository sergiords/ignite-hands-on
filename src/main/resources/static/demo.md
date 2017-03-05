# DEMO: Train Best Price Calendar

=========
## Trains

For each combination of **origin**, **destination** and **date** there are many **trains** starting at a different **time**.
```sh
                       |                     TRAINS                    |
                       -------------------------------------------------
                       |ORIGIN     |DESTINATION|DATE       |TIME       |
                       -------------------------------------------------
                       |FRPAR      |GBLON      |3001-01-01 |10:00      |
                       |FRPAR      |GBLON      |3001-01-01 |12:00      |
                       |FRPAR      |GBLON      |3001-01-02 |10:00      |
                       |FRPAR      |GBCAM      |3001-01-02 |12:00      |
                       |FRLIL      |GBCAM      |3001-01-01 |10:00      |
                       |...        |...        |...        |...        |
```

>All dates are between 3001/01/01 and 3001/12/31.

>There are 48 180 trains in total.

=========
## Prices

For each **train** there are many different **prices** with a given number of **available seats**.

```sh
             ____/____________________   ______________________   ______________________ 
            /_]  [___][___][___][___] | | [___][___][___][___] | | [___][___][___][___] |
           /__________________________| |______________________| |______________________|
           `-o-o-o--------------o-o-o-'-'-o=o--------------o=o-'-'-o=o--------------o=o-'
                      price 1                  price 2                  price 3          
                      30€                      15€                      5€               
                      50 seats                 25 seats                 5 seats          
```

>For all trains there are 3 468 960 prices.

=========
## Problem

Customers request your application to find **Best Price** for a given **origin**, **destination**, **period** and number of **seats**.

Customers want to know the **Best Price** **for each day** in the requested **period**.

=========
## Data Sources

Possible stations can be found using **TrainServer#stations** method.

Possible trains for an origin, destination and date can be found using **TrainServer#trains** method.

Possible prices for a train can be found using **TrainServer#prices** method.

=========
## Tip #1: Compute parallelism

**1** Best Price request for **n days (a period)**

is equivalent to

**n** Best Price requests for **1 day**

=========
## Tip #2: Data distribution

Data can be partitioned by **origin-destination-date**.

>Mind cache affinity: all cache accesses should be local

=========
## Tip #3: Service 

Node services should be used to:

- load cache on initialization **using affinity** (data is loaded in respective primary nodes)

- find the BestPrice for a given origin, destination, date and seats **using cache**

=========
## Code It !

Application is a super-classic SpringBoot application with code to complete in **BestPriceApi** and **BestPriceFinderService** classes.

=========
## Run It !

Start **BestPriceApp** main class using your IDE or using this command:
```bash
./gradlew runDemo
```

>IDE launch is a bit faster than gradle command

=========
## Check Your API

Wait for cache to be fully loaded on all server nodes (check logs).

You can use cURL or your browser to request **BestPriceApi**.

E.g.: [/FRPAR/GBLON/3001-01-01/3001-01-07?seats=2](http://localhost:8081/FRPAR/GBLON/3001-01-01/3001-01-07?seats=2)

Make sure everything in [Demo App](http://localhost:8081/demo.html) is **green**.
