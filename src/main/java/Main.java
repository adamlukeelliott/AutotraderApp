import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
    static String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    static String configPath = rootPath + "config.properties";
    static Properties properties = new Properties();

    public static void main(String[] args) throws IOException {
        properties.load(new FileInputStream(configPath));
        String url = properties.getProperty("autotrader.url");
        String rootUrl = properties.getProperty("autotrader.rootUrl");
        String carElementClass = properties.getProperty("autotrader.html.carElementClass");

        String postcode = URLEncoder.encode("BT22 2HU", StandardCharsets.UTF_8);
        String carMake = URLEncoder.encode("BMW", StandardCharsets.UTF_8);
        Integer pageNum = 1;

        String searchUrl = String.format(url, postcode, carMake, pageNum);

        HeadlessWebBrowser webBrowser = new HeadlessWebBrowser();

        try {

            String pageHtml = webBrowser.getPageSourceWaitForClassElement(searchUrl, carElementClass);

            Document webpage = Jsoup.parse(pageHtml);
            Elements carElements = webpage.select(carElementClass);

            for (Element element : carElements) {
                System.out.println(rootUrl + element.attr("href"));
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        webBrowser.getWebDriver().quit();
    }
}
