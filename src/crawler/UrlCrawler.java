package crawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;


public class UrlCrawler {

    // Connect to a sample database
    public static DatabaseHandler databaseHandler = new DatabaseHandler();

    /**
     * Business logic to parse the links and deep links
     * @param URL The URL to crawl
     * @param word The word to be searched for
     * @throws SQLException
     * @throws IOException
     */
    public static void crawl(String URL, String word) throws SQLException, IOException {
        //check if the given URL is already in database
        String sql = "select * from visited where URL = '"+URL+"'";
        ResultSet resultSet = databaseHandler.runSelectQuery(sql);

        // If it doesn't exist in the Database then keep crawling
        if(!resultSet.next()){
            //store the URL to database to avoid parsing again
            sql = "INSERT INTO visited (URL) VALUES (?)";
            PreparedStatement preparedStatement = databaseHandler.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, URL);
            preparedStatement.execute();

            //get useful information
            Document document = Jsoup.connect("http://www.mit.edu/").get();

            if(document.text().contains(word)){
                System.out.println(URL);
            }

            //get all links and recursively call the crawl method
            Elements questions = document.select("a[href]");
            for(Element link: questions){
                if(link.attr("href").contains("mit.edu"))
                    crawl(link.attr("abs:href"), word);
            }
        }
    }

    /**
     * Main method to call the business logic
     */
    public static void main(String[] args) throws IOException, SQLException {
        //databaseHandler.truncateTableQuery("delete from visited;");
        crawl("http://www.mit.edu", "research");
    }
}