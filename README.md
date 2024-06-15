# Data Engineering coding test

### Welcome to the Data Engineering code test.

This purpose of this coding test is to see how you work with code to make it production ready and maintainable.

## Tasks
1. Make the code easier to understand and more maintainable.
2. Come up with good tests that prove the code generates correct results.
3. Come up with good abstractions on the input data that make the solution easy to understand.
4. Follow branching and PR procedures as you would in a team of developers - the DE team will review your PR as part of the test. 

## The problem.
We receive a daily data file, in csv format, containing details of journeys made by drivers throughout the day.

This daily file is called 2021-10-05_journeys.csv

Each line holds details of one journey.

A very small example journey file is located in the test/resources folder and is structured as follows :<br>
`journeyId,driverId,startTime,endTime,startLat,startLon,endLat,endLon,startOdometer,endOdometer`

Each journey has an id, a driverId, a start and end epoch time in milliseconds, a start position (latitude and longitude), an end position and the mileage at the start of the journey (startOdometer) and the end of the journey (endOdometer).

**A junior developer was asked to write a program that processes the 2021-10-05_journeys.csv file
to**
1. Find journeys that are 90 minutes or more.
2. Find the average speed per journey in kph.
3. Find the total mileage by driver for the whole day.
4. Find the most active driver - the driver who has driven the most kilometers.
5. Output results to STDOUT as follows :
<pre>
Journeys of 90 minutes or more.
journeyId: 000006 driver_c distance 111.0 durationMS  7200000 avgSpeed in kph was 55.50

Average speeds in Kph
journeyId: 000001 driver_a distance   3.0 durationMS   600000 avgSpeed in kph was 18.00
journeyId: 000002 driver_a distance  41.0 durationMS  3600000 avgSpeed in kph was 41.00
journeyId: 000003 driver_b distance   3.0 durationMS   600000 avgSpeed in kph was 18.00
journeyId: 000004 driver_b distance   1.0 durationMS    60000 avgSpeed in kph was 60.00
journeyId: 000005 driver_b distance   1.0 durationMS    60000 avgSpeed in kph was 60.00
journeyId: 000006 driver_c distance 111.0 durationMS  7200000 avgSpeed in kph was 55.50

Mileage By Driver
driver_a drove   44 kilometers
driver_b drove    5 kilometers
driver_c drove  111 kilometers

Most active driver is driver_c
</pre>

6. <span style="color:green">You should identify and filter out any incorrect data</span>.

#### As a senior developer, your task is to take over their code and complete the tasks detailed above. 

#### You are expected to complete this exercise within 1 week.

## You can build this project either in an ide or on the command line as follows :
`mvn clean install`
