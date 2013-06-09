package org.nic.stockCharts;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.nic.stockCharts.util.StockInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class YQL_ConTest {

	public static final String YAHOO_URL_FIRST = "http://query.yahooapis.com/v1/" +
		"public/yql?q=select%20*%20from%20yahoo.finance." + 
			"quote%20where%20symbol%20in%20(%22";
	
	public static final String YAHOO_URL_SECOND = "%22)&env=store%3A%2F%2Fdatatables." +
			"org%2Falltableswithkeys"; 
	
	private String stockSymbol;
	
	// XML Data to Retrieve
	String name ;
	String yearLow;
	String yearHigh;
	String daysLow;
	String daysHigh;
	private String lastTradePriceOnly;
	String change;
	String daysRange;
		 

	public String getLastTradePrice()	{ return lastTradePriceOnly; }
	
	public YQL_ConTest(String stockSymbol)
	{
		 name = "";
		 yearLow = "";
		 yearHigh = "";
		 daysLow = "";
		 daysHigh = "";
		 lastTradePriceOnly = "";
		 change = "";
		 daysRange = "";
		 
		 this.stockSymbol= stockSymbol; 
	}
	
	public void connectTo()
	{
		try
		{
			String yqlURL = YAHOO_URL_FIRST + stockSymbol + YAHOO_URL_SECOND;
			
			URL url = new URL(yqlURL);
			
			URLConnection connection;
			connection = url.openConnection();
			System.out.println("after open connection");
			
			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			
			
			int responseCode = httpConnection.getResponseCode();
			
			System.out.println(responseCode);
			
		     // Tests if responseCode == 200 Good Connection
		     if (responseCode == HttpURLConnection.HTTP_OK) {
		    	 
		    	 // Reads data from the connection
		        InputStream in = httpConnection.getInputStream();

		        // Provides a way to parse DOM object trees from XML documents
		        
		        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		        
		        // Provides a DOM Document from an xml page
		        DocumentBuilder db = dbf.newDocumentBuilder();

		        // Parse the Yahoo Financial YQL Stock XML File
		        Document dom = db.parse(in);
		        
		        // The root element is query
		        Element docEle = dom.getDocumentElement();

		        // Get a list of quote nodes
		        NodeList nl = docEle.getElementsByTagName("quote");
		        
		        
		        // Checks to make sure we found a quote tag
		        if (nl != null && nl.getLength() > 0) {
		        	
		        // Cycles through if we find multiple quote tags
		        // Mainly used for demonstration purposes
		          for (int i = 0 ; i < nl.getLength(); i++) {
		            
		            // Passes the root element of the XML page, so 
		        	// that the function below can search for the 
		        	  // information needed
		            StockInfo theStock = getStockInformation(docEle);
		            
		            // Gets the values stored in the StockInfo object
		            daysLow = theStock.getDaysLow();
		            daysHigh = theStock.getDaysHigh();
		            yearLow = theStock.getYearLow();
		            yearHigh = theStock.getYearHigh();
		            name = theStock.getName();
		            lastTradePriceOnly = theStock.getLastTradePriceOnly();
		            change = theStock.getChange();
		            daysRange = theStock.getDaysRange();
		            
		            System.out.println(theStock.toString());
		          }
		        }
		      }

		}
		catch(IOException e){}
		catch(ParserConfigurationException e) {}
		catch(SAXException e) {}
	}
	
	// Sends the root xml tag and the tag name we are searching for to
		// getTextValue for processing. Then uses that information to create
		// a new StockInfo object
		private static StockInfo getStockInformation(Element entry){
			
			String stockName = getTextValue(entry, "Name");
			String stockYearLow = getTextValue(entry, "YearLow");
			String stockYearHigh = getTextValue(entry, "YearHigh");
			String stockDaysLow;
			String stockDaysHigh;
			
			try
			{
				stockDaysLow = getTextValue(entry, "DaysLow");
				stockDaysHigh = getTextValue(entry, "DaysHigh");
			} 
			catch(Exception e) 
			{
				stockDaysLow = "0.0";
				stockDaysHigh = "0.0";
			}
			String stocklastTradePriceOnlyTextView = getTextValue(entry, "LastTradePriceOnly");
			String stockChange = getTextValue(entry, "Change");
			String stockDaysRange = getTextValue(entry, "DaysRange");
			
			StockInfo theStock = new StockInfo(stockDaysLow, stockDaysHigh, stockYearLow,
				stockYearHigh, stockName, stocklastTradePriceOnlyTextView,
				stockChange, stockDaysRange);
			
			return theStock;
			
		}
		
		// Searches through the XML document for a tag that matches 
		// the tagName passed in. Then it gets the value from that
		// tag and returns it
		
		private static String getTextValue(Element entry, String tagName){
			
			String tagValueToReturn = null;
			
			NodeList nl = entry.getElementsByTagName(tagName);
			
			if(nl != null && nl.getLength() > 0){
				
				Element element = (Element) nl.item(0);
					
				tagValueToReturn = element.getFirstChild().getNodeValue();
				
			}
			
			return tagValueToReturn;
			
		}

}
