package com.winit.airarabia.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.winit.airarabia.dataaccess.AirportDA;
import com.winit.airarabia.objects.AirportNamesDO;
import com.winit.airarabia.objects.AirportsDO;
import com.winit.airarabia.objects.AirportsDestDO;
import com.winit.airarabia.objects.AllAirportNamesDO;
import com.winit.airarabia.objects.AllAirportsDO;
import com.winit.airarabia.objects.AllAirportsMainDO;
import com.winit.airarabia.objects.CurrencyDo;
import com.winit.airarabia.objects.OriginsDO;
import com.winit.airarabia.utils.LogUtils;

import android.content.Context;
import android.widget.Toast;

public class AirportsDataParser extends BaseHandler
{
	private Vector<AirportsDO> vecAirport ;
	private AirportsDO airdo;
	private AllAirportsMainDO allAirportsMainDO;
	private  Context context;
	
	public AirportsDataParser(Context context) {
		this.context = context;
	}

	private static JSONObject getJsonObjectFromInputStream(InputStream is) {
		JSONObject jsonObject = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) 
			{
				sb.append(line);
//				Toast.makeText(context, "Iam Adding", Toast.LENGTH_LONG).show();
				LogUtils.errorLog("My test ganesh", "iam adding");
			}
		} catch (IOException eJson1) {
			eJson1.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			jsonObject = new JSONObject(sb.toString());
		} catch (JSONException eJson) {
			eJson.printStackTrace();
		}
		return jsonObject;

	}

	public void getAirportsData(InputStream isResponse)
	{
		allAirportsMainDO = new AllAirportsMainDO();
		AirportDA airportDA=new AirportDA(context);
		allAirportsMainDO.airportParserDO = new AllAirportsDO();
		allAirportsMainDO.allAirportNamesDO = new AllAirportNamesDO();
		
		JSONObject jsonObject = null;
		jsonObject = getJsonObjectFromInputStream(isResponse);
		
		//All Airports data
		vecAirport = new Vector<AirportsDO>();
		
		try {
			JSONArray airportsArray = jsonObject.getJSONArray("airports");
			AirportNamesDO airportNamesDO;
			int j=0;
			if(airportsArray != null && airportsArray.length() > 0)
			for (int i = 0; i < airportsArray.length(); i++) 
			{
				JSONObject localjsonobj = airportsArray.getJSONObject(i);

				airdo = new AirportsDO();
				airdo.code = localjsonobj.getString("code");
				airdo.en = localjsonobj.getString("en");
				airdo.ar = localjsonobj.getString("ar");
				airdo.fr = localjsonobj.getString("fr");
				
				airdo.countryname = localjsonobj.getString("countryname");
				airdo.countryid = localjsonobj.getString("countryid");
				airdo.language = localjsonobj.getString("language");
				airdo.currency = localjsonobj.getString("currency");
				
				airportNamesDO 	= new AirportNamesDO();
				airportNamesDO.code 			= airdo.code;
				airportNamesDO.ar 				= airdo.ar;
				airportNamesDO.en 				= airdo.en;
				airportNamesDO.fr 				= airdo.fr;
				
				allAirportsMainDO.allAirportNamesDO.vecAirport.add(airportNamesDO);
				
				vecAirport.add(airdo);
				LogUtils.errorLog("my count ", "iam printing" + j+localjsonobj.getString("en"));
				j++;
			}
			allAirportsMainDO.airportParserDO.vecAirport = vecAirport;
			
			/*Vector<AirportsDO> allAirport=airportDA.getAllAirport();
			Vector<AirportNamesDO> allAirportName=airportDA.getAllAirportName();
			if(allAirport.size()<1 && allAirportName.size()<1){
				airportDA.insertAllAirport(allAirportsMainDO.airportParserDO.vecAirport);
				airportDA.insertAllAirportName(allAirportsMainDO.allAirportNamesDO.vecAirport);	
			}else{
				airportDA.deleteAllAirport();
				airportDA.insertAllAirport(allAirportsMainDO.airportParserDO.vecAirport);
				airportDA.deleteAllAirportName();
				airportDA.insertAllAirportName(allAirportsMainDO.allAirportNamesDO.vecAirport);	

			}
			*/
			
		} catch (JSONException eparser1) {
			eparser1.printStackTrace();
		}
		
		//All Destination Airports data
		try {
			JSONArray originsArray = jsonObject.getJSONArray("origins");
			if(originsArray != null && originsArray.length() > 0)
			for (int i = 0; i < originsArray.length(); i++) 
			{
				JSONArray localJsonArray = (JSONArray) originsArray.get(i);
				ArrayList<OriginsDO> arrayListOriginsDOs = new ArrayList<OriginsDO>();
				
				if(localJsonArray != null && localJsonArray.length() > 0)
					for(int j=0; j<localJsonArray.length();j++)
					{
						JSONArray innerJsonArray = (JSONArray) localJsonArray.get(j);
						OriginsDO originsDO = new OriginsDO();

						if(innerJsonArray != null && !innerJsonArray.isNull(0))
						{
							originsDO.airport_num 	= innerJsonArray.getInt(0);
							originsDO.flight_type1 	= innerJsonArray.getString(1);
							originsDO.currency_num 	= innerJsonArray.getInt(2);
							originsDO.flight_type2 	= innerJsonArray.getString(3);
							originsDO.noOfStops 	= innerJsonArray.getInt(4);;

							//assigning to Airports item
							for (int k = 0; k < allAirportsMainDO.airportParserDO.vecAirport.size(); k++) {
								if(k == originsDO.airport_num)
								{
									allAirportsMainDO.airportParserDO.vecAirport.get(k).service_type1 = originsDO.flight_type1;
									AirportsDestDO airportsDestDO = new AirportsDestDO();
									airportsDestDO.ar = allAirportsMainDO.airportParserDO.vecAirport.get(k).ar;
									airportsDestDO.en = allAirportsMainDO.airportParserDO.vecAirport.get(k).en;
									airportsDestDO.fr = allAirportsMainDO.airportParserDO.vecAirport.get(k).fr;
									airportsDestDO.code = allAirportsMainDO.airportParserDO.vecAirport.get(k).code;
									airportsDestDO.service_type1 = allAirportsMainDO.airportParserDO.vecAirport.get(k).service_type1;
									airportsDestDO.name = allAirportsMainDO.airportParserDO.vecAirport.get(k).name;
									
									airportsDestDO.countryname = allAirportsMainDO.airportParserDO.vecAirport.get(k).countryname;
									airportsDestDO.countryid = allAirportsMainDO.airportParserDO.vecAirport.get(k).countryid;
									airportsDestDO.language = allAirportsMainDO.airportParserDO.vecAirport.get(k).language;
									airportsDestDO.currency = allAirportsMainDO.airportParserDO.vecAirport.get(k).currency;
									
									vecAirport.get(i).destList.add(airportsDestDO);
								}
							}
							arrayListOriginsDOs.add(originsDO);
							
							
						}
						else
						{
							LogUtils.errorLog("innerJsonArray", innerJsonArray.toString()+"---is null");
						}
				}
						
			}
			Collections.sort(vecAirport,new ItemNameComparator());
		} catch (JSONException eparser2) {
			eparser2.printStackTrace();
		}
		
		//All Currencies data
		try {
			JSONArray currenciesArray = jsonObject.getJSONArray("currencies");
			allAirportsMainDO.arlCurrencies = new ArrayList<CurrencyDo>();
			CurrencyDo currencyDo;
			if(currenciesArray != null && currenciesArray.length() > 0)
			for (int i = 0; i < currenciesArray.length(); i++) 
			{
				JSONObject localjsonobj = currenciesArray.getJSONObject(i);
				 currencyDo = new CurrencyDo();
				/*currencyDo.code = localjsonobj.getString("code");
				currencyDo.en = localjsonobj.getString("en");
				currencyDo.ar = localjsonobj.getString("ar");
				currencyDo.ru = localjsonobj.getString("ru");
				currencyDo.es = localjsonobj.getString("es");
				currencyDo.it = localjsonobj.getString("it");
				currencyDo.tr = localjsonobj.getString("tr");
				currencyDo.zh = localjsonobj.getString("zh");
				currencyDo.fr = localjsonobj.getString("fr");*/
				if (!localjsonobj.getString("code").isEmpty())
					currencyDo.code = localjsonobj.getString("code");
				else if (!localjsonobj.getString("en").isEmpty())
					currencyDo.en = localjsonobj.getString("en");
				else if (!localjsonobj.getString("ar").isEmpty())
					currencyDo.ar = localjsonobj.getString("ar");
				else if (!localjsonobj.getString("ru").isEmpty())
					currencyDo.ru = localjsonobj.getString("ru");
				else if (!localjsonobj.getString("es").isEmpty())
					currencyDo.es = localjsonobj.getString("es");
				else if (!localjsonobj.getString("it").isEmpty())
					currencyDo.it = localjsonobj.getString("it");
				else if (!localjsonobj.getString("tr").isEmpty())
					currencyDo.tr = localjsonobj.getString("tr");
				else if (!localjsonobj.getString("zh").isEmpty())
					currencyDo.zh = localjsonobj.getString("zh");
				else if (!localjsonobj.getString("fr").isEmpty())
					currencyDo.fr = localjsonobj.getString("fr");
				
				allAirportsMainDO.arlCurrencies.add(currencyDo);
			}
			/*ArrayList<CurrencyDo> currency=airportDA.getCurrency();
			if(currency.size()<1){
				airportDA.insertCurrency(allAirportsMainDO.arlCurrencies);
			}else{
				airportDA.deleteCurrency();
				airportDA.insertCurrency(allAirportsMainDO.arlCurrencies);
				
			}*/
			
		} catch (JSONException eparser1) {
			eparser1.printStackTrace();
		}
	//*******************************insert into database*******************************	
		/*new QuickSortCurrencyDo().sortAirPorts(allAirportsMainDO.airportParserDO.vecAirport);
		new QuickSortCurrencyDo().sortAirPortNames(allAirportsMainDO.allAirportNamesDO.vecAirport);
		new QuickSortCurrencyDo().sort(allAirportsMainDO.arlCurrencies);
		Vector<AirportsDO> allAirport=airportDA.getAllAirport();
		Vector<AirportNamesDO> allAirportName=airportDA.getAllAirportName();
		ArrayList<CurrencyDo> currency=airportDA.getCurrency();
		if(allAirport.size()<1 && allAirportName.size()<1 && currency.size()<1){
			airportDA.insertAllAirport(allAirportsMainDO.airportParserDO.vecAirport);
			airportDA.insertAllAirportName(allAirportsMainDO.allAirportNamesDO.vecAirport);
			airportDA.insertCurrency(allAirportsMainDO.arlCurrencies);
		}else{
			airportDA.deleteAllAirport();
			airportDA.insertAllAirport(allAirportsMainDO.airportParserDO.vecAirport);
			airportDA.deleteAllAirportName();
			airportDA.insertAllAirportName(allAirportsMainDO.allAirportNamesDO.vecAirport);	
			airportDA.deleteCurrency();
			airportDA.insertCurrency(allAirportsMainDO.arlCurrencies);
		}
			*/
		
	} 
	private class ItemNameComparator implements Comparator<AirportsDO> {
		public int compare(AirportsDO o1, AirportsDO o2) {
			return o1.en.compareTo(o2.en);
		}
	}

	@Override
	public Object getData() 
	{
		if(allAirportsMainDO != null)
		{
			if(allAirportsMainDO.airportParserDO.vecAirport != null 
					&& allAirportsMainDO.airportParserDO.vecAirport.size() > 0)
				for (int j = 0; j < allAirportsMainDO.airportParserDO.vecAirport.size(); j++) {
					if(allAirportsMainDO.airportParserDO.vecAirport.get(j).destList != null && allAirportsMainDO.airportParserDO.vecAirport.get(j).destList.isEmpty())
					{
						LogUtils.errorLog("checking empty dest", allAirportsMainDO.airportParserDO.vecAirport.get(j).en+"--"+allAirportsMainDO.airportParserDO.vecAirport.get(j).destList.size()+"---is null");
						allAirportsMainDO.airportParserDO.vecAirport.remove(j);
						allAirportsMainDO.allAirportNamesDO.vecAirport.remove(j);
					}
				}
			return allAirportsMainDO;
		}
		return null;
	}

	@Override
	public Object getErrorData() {
		return null;
	}
}