Public-Transit
==============
Create a trip planner using public transit in Washington, DC.  You have four input files that are provided, which you will use to create the graph of transit system.  
Your program should take as input two stop ids, and a departure time, and output an optimal route for the passenger.

Input
-----

The input format will be as follows:

[Start-Stop_id] [End-Stop_id] [Departure time Hour] [Departure time Min] 

where:

[Start-Stop_id] is the stop id for the start of the trip

[End-Stop_id] is the stop id for the end of the trip

[Departure time Hour] is an integer with the 24-hour departure time hour (i.e. 17 for a 5:30pm departure)

[Departure time Min] is an integer with the departure time minute (i.e. 30 for a 5:30pm departure)

You should assume that the departure time second is 00.

Output
------

Your output should describe the fastest way to get from the starting stop to the ending stop for the provided start time.  The output include the desired departure time, the boarding time for each leg of the trip, the short name of each route, a description of each transfer, and the arrival time at the destination.  The exact format for the output is shown in the sample below.

Example Input
-------------

7333 6963 7 0

Example Output
--------------

Your output should be in the following format

- Travelling from FEDERAL CENTER METRO STATION to TAKOMA METRO STATION at 7:00:00
- Board Orange at FEDERAL CENTER METRO STATION at 7:02:00
- Transfer to Green at L'ENFANT PLAZA METRO STATION at 7:06:00
- Transfer to Red at FORT TOTTEN METRO STATION at 7:22:00
- Arrive at TAKOMA METRO STATION at 7:25:00

We are making the following assumptions as we create our route guidance system:

Users care only about earliest arrival time (not about cost of trip, comfort on the trip, transportation mode, number of transfers, etc.)
Our routes always run on time.
It takes no time to make a transfer.  In other words, if a person arrives at a station at 5:17:15pm and there is a bus leaving at 5:17:15pm, they can always make this transfer.
Some normal transfers will currently not be allowed because some nearby stops are not connected, e.g. the bus bays and their associated metro stations are treated as distinct stops in the input data.

Notes:
------
You are allowed to preprocess the input files in the following ways:  changing the format to a different delimited format and removing columns.  You are not allowed to reorder the input.  You are not allowed to use a database to store or query data.  You are not allowed to do any other preprocessing of the data.  Any processing of the data must be done after the input is read.  For example, you are not allowed to precalculate routes, transfer points, etc.
