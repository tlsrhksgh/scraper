package com.single.project.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ScrapperApplication {

    public static void main(String[] args) {
//        SpringApplication.run(ScrapperApplication.class, args);

        try {
            Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history?period1=1643891423&period2=1675427423&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
            Document document = connection.get();

            Elements elems = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element elem = elems.get(0);

            Element tbody = elem.children().get(1);
            for (Element e : tbody.children()) {
                String txt = e.text();
                if(!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                String month = splits[0];
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                System.out.println(year + "/"+ month + "/" + day + "->" + dividend);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
