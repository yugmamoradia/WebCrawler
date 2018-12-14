# Java Implementation for a web crawler

This project contains library and a sample database (`crawler.db`) to connect to and store the records.
Clone the project and in the UrlCrawler.java, edit the main method to include the URL
you wish to crawl and the word to be searched for.

You can have a dependency manager (like Maven etc) for managing the project, but this is a simple basic implementation 
for demonstration purposes.

## Requirements:
1: SQLite3

### How to run the project:
1: Make sure SQLite3 is installed on your computer. Create the table `visited` by opening typing the following
on the sql cli:
```
sqlite3 /Users/WebCrawler/src/crawler/crawler.db
sqlite3> CREATE TABLE IF NOT EXISTS visited (
           RecordId INTEGER PRIMARY KEY NOT NULL,
           URL text NOT NULL
         );
```
This will create the empty schema for you.

2: Edit the `main()` method in `UrlCrawler.java` and pass in Url you want to crawl, along with the word to search.
Also provide the baseUrl of the URL to be searched

3: Hit Run and see the System console for crawled links. You can also see those in the table you created
by a simple select query in the Database.

