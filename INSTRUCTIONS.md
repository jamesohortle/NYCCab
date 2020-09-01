Challenge Preface
=================

-   ● This is a private test. If you provide a repository with the code
    > no mentions to XXX can be seen (repo name or README.md)

    > ● the SQL file is a huge file. We suggest you import via command
    > line

    > **Please provide set up instructions, for example how to build
    > your code or external services to be configured**

Simple Cab Exercise
===================

Instruction to complete the java challenge

Your code will be evaluated in 2 parts:

1\. Server that provides REST endpoints

2\. Simple command line client that will consume the APIs provided by
the server

Please provide the instructions to execute

Cab Data Researcher is a company that provides insights on the open data
about NY cab trips

Cab trips in NY are public available as csv downloadable files. In order
to make it more useful we want to wrap the data in a public API.

Data format is as follow:

medallion, hack_license, vendor_id, rate_code, store_and_fwd_flag,
pickup_datetime, dropoff_datetime, passenger_count, trip_time_in_secs,
trip_distance

The medallion is cab identification.

The API should provide a way to query how many trips a particular cab
(medallion) has made given a particular pickup date ( using
pickup_datetime). Only consider the date and not the time.

The API must receive one or more medallions and return how many trips
each medallion has made.

Considering that the query creates a heavy load on the database, the
results must be cached.

The API must allow user to ask for fresh data, ignoring the cache.

There must be also be a method to clear the cache.

What do we provide:

-   SQL statements to populate database from the csv

Your code will be evaluated in 2 parts:

1\. Server that provides REST endpoints

2\. Simple command line client that will consume the APIs provided by
the server

Please provide the instructions to execute

Completed Exercise
==================

Please email your file back to us or provide a link to your file held on
a web-based hosting service (e.g. github, drop box, etc).

Accepted file formats for direct upload are listed below:

-   Accepted file types: pdf, jpg, jpeg, png, gif, bmp, doc, docx, ppt,
    > pptx, avi, wmv, mpg, mpeg, 3gp, flv, m4v, mp4, mp3, wav, mid, xls,
    > xlsx, htm, html, txt, mov, rtf, key, pages, numbers, psd

-   Max file size: 250 MB

Please note: .zip files are not accepted via direct upload to this
module. However, you can include a link to your .zip file using a file
sharing service (e.g. dropbox) in the comments box when you click
Upload.
