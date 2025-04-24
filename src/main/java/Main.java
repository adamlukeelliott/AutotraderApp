import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

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

        System.out.println(searchUrl);

        // Wait for required element to appear
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120 Safari/537.36");

        WebDriver webDriver = new ChromeDriver(chromeOptions);

        try {
            webDriver.get(searchUrl);

            Wait<WebDriver> wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(carElementClass)));

            //old stuff
            Document webpage = Jsoup.parse(webDriver.getPageSource());
            Elements carElements = webpage.select(carElementClass);

            for (Element element : carElements) {
                System.out.println(rootUrl + element.attr("href"));
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            webDriver.quit();
        }
    }
}
