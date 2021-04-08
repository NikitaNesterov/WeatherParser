import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class Main {

    public static void getMaxMorningForecastTemp(JSONArray array){

      Double maxTemp = 0.00;
      String dateWithMaxtemp = "";

      for (int i = 0; i < array.size(); i++) {
        JSONObject dateTimeWeather = new JSONObject((Map) array.get(i));
        String timeCheck = (String) dateTimeWeather.get("dt_txt");
        String[] arrayToCheck = timeCheck.split("\\s");
        if(arrayToCheck[1].equals("06:00:00")){
            JSONObject tempToCheck = (JSONObject) dateTimeWeather.get("main");
            Double temp = (Double) tempToCheck.get("temp");
            if (temp > maxTemp){
                maxTemp = temp;
                dateWithMaxtemp = arrayToCheck[0];
            }
        }
    }
       System.out.println("Макисмальная прогнозная утренняя температура будет "
+ dateWithMaxtemp + " и составит в градусах С: " +  (maxTemp - 273.15));
    }

    public static String getControlDate(JSONArray array){
        JSONObject dateTimeWeather = new JSONObject((Map) array.get(0));
        String timeCheck = (String) dateTimeWeather.get("dt_txt");
        String[] arrayToCheck = timeCheck.split("\\s");
        String dateToCheck = arrayToCheck[0];
        return dateToCheck;
    }

    public static void getAvgDaylyForecastTemp(JSONArray array){
        String dateToCheck = getControlDate(array);

        Double sumTemp = 0.00;
        Double avgTemp = 0.00;
        int count = 0;

        for (int i = 0; i < array.size(); i++) {
            JSONObject dateTimeWeather = new JSONObject((Map) array.get(i));
            String timeCheck = (String) dateTimeWeather.get("dt_txt");
            String[] arrayToCheck = timeCheck.split("\\s");

            if(arrayToCheck[0].equals(dateToCheck)){
                JSONObject tempToCheck = (JSONObject) dateTimeWeather.get("main");
                Double temp = (Double) tempToCheck.get("temp");
                count++;
                sumTemp = sumTemp + (temp - 273.15);
                avgTemp = sumTemp / count;
            }  else {
                System.out.println("Средняя прогнозная дневная температура " +
 dateToCheck + " в градусах С составит " + avgTemp);
                dateToCheck = arrayToCheck[0];
                count = 1;
                avgTemp = 0.00;
                sumTemp = 0.00;
            }
            if (i == array.size() -1){
                System.out.println("Средняя прогнозная дневная температура " + dateToCheck + " в градусах С составит " + avgTemp);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?lat=48.707103&lon=44.516939&appid=ec19b5394f790f80cc543e9dc1cfec7f");

        try{

            StringBuilder result = new StringBuilder();
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = rd.readLine()) != null){
                result.append(line);
                System.out.println(line);
            }
            String res = result.toString();

            JSONObject weatherJsonObject = (JSONObject) JSONValue.parseWithException(res);
            System.out.println(weatherJsonObject.get("list"));

            JSONArray days = (JSONArray) weatherJsonObject.get("list");

            getMaxMorningForecastTemp(days);

            getAvgDaylyForecastTemp(days);

    } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }
}


