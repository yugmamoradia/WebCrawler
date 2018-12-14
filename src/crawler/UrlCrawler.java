package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;


public class UrlCrawler {

    // Connect to a sample database
    public static DatabaseHandler databaseHandler = new DatabaseHandler();

    /**
     * Helper method to get the domain name from a complete URL
     * Example: https://www.yahoo.com ---> yahoo.com
     */
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    /**
     * Business logic to parse the links and deep links
     * @param baseUrl The start URL or the base URL where we start parsing. For making this method more customized.
     * @param Url The URL to crawl
     * @param word The word to be searched for
     * @throws SQLException
     * @throws IOException
     */
    public static void crawl(String baseUrl, String Url, String word) throws SQLException, IOException, URISyntaxException {
        if(databaseHandler.connection == null){
            System.out.println("Cannot connect to the database. Will not proceed further");
            throw new SQLException();
        }

        // Null check on the String params.
        if(baseUrl != null && Url != null && word != null){
            //check if the given URL is already in database
            String sql = "select * from visited where URL = '"+Url+"'";
            ResultSet resultSet = databaseHandler.runSelectQuery(sql);

            // If it doesn't exist in the Database then keep crawling
            if(!resultSet.next()){
                //store the URL to database to avoid parsing again
                sql = "INSERT INTO visited (URL) VALUES (?)";
                PreparedStatement preparedStatement = databaseHandler.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, Url);
                preparedStatement.execute();

                // Parse and extract information from the base URL
                Document document = Jsoup.connect(baseUrl).get();

                if(document.text().contains(word)){
                    System.out.println(Url);
                }

                String domainName = getDomainName(baseUrl); // Example: "yahoo.com"

                // get all links and recursively call the crawl method
                Elements questions = document.select("a[href]");
                for(Element link: questions){
                    if(link.attr("href").contains(domainName))
                        crawl(baseUrl, link.attr("abs:href"), word);
                }
            }
        }
    }

    /**
     * Main method to call the business logic
     */
    public static void main(String[] args) throws IOException, SQLException, URISyntaxException {
        //databaseHandler.truncateTableQuery("delete from visited;");
        crawl("http://www.berkeley.edu","http://www.berkeley.edu", "research");
    }
}