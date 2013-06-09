package org.nic.stockCharts.model;

import javafx.scene.chart.XYChart;

public class StockData 
{
	
	private XYChart.Series<String, Number> series;
	
	private String stockSymbol;
	
	
	public StockData(String stockSymbol)
	{
		series = new XYChart.Series<String,Number>();
		this.stockSymbol = stockSymbol;
	}
	
	public XYChart.Series<String, Number> getSeries()	{ return series; }
	public String getStockSymbol()	{ return stockSymbol; }
	
	
	public void addData(XYChart.Data<String, Number> newData)
	{
		series.getData().add(newData);
	}
}
