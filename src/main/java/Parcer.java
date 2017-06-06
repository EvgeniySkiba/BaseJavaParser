
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Skiba on 06.06.2017.
 */
public class Parcer {
    private static Document getPage() throws IOException {

        String url = "http://www.pogoda.spb.ru";
        Document page = Jsoup.parse(new URL(url), 3000);

        return page;
    }

    // регулярка(шаблон)
    // \d - символьный знак
    // \\ экранирование \
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    /**
     * @param stringDate 06.06 Вторник погода сегодня
     * @return 06.06
     */
    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't extract from string");
    }

    private static int printPairValues(Elements values, int index) {

        int iterationCount = 4;
        if (index == 0) {
            Element valueline = values.get(3);

            boolean isMorning = valueline.text().contains("Утро");
            boolean isDay = valueline.text().contains("День");
            boolean isEvening = valueline.text().contains("Вечер");

            if (isMorning)
                iterationCount = 3;

            if(isDay)
                iterationCount =2 ;

            if(isEvening)
                iterationCount = 1;
        }

        for (int i = 0; i < iterationCount; i++) {
            Element valueline = values.get(index+i);
            for (Element td : valueline.select("td")) {
                System.out.print(td.text() + "    ");
            }

            System.out.println();
        }
        return iterationCount;

    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        // css query language
        Element tableWheater = page.select("table[class=wt]").first();

        Elements names = tableWheater.select("tr[class=wth]");
        Elements values = tableWheater.select("tr[valign=top]");

        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date+ "     Явление Температура Давление Влажность  Ветер");
            int  iterationCount = printPairValues(values, index);
            index = index + iterationCount;
        }
    }
}
