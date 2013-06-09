package org.nic.stockCharts.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import org.nic.stockCharts.StockCharts;
import org.nic.stockCharts.YQL_ConTest;
import org.nic.stockCharts.util.TimeUtil;

public class ChartPanelController 
{
	private StockCharts mainApp;
	private YQL_ConTest yqlCon;
	
	private volatile boolean isActive = true;
	
	
	public boolean getActiveStatus()	{ return isActive; }
	
	public void setMainApp(StockCharts mainApp)
	{
		this.mainApp = mainApp;
	}
	
	public synchronized void setActiveStatus()
	{
		isActive = !isActive;
		notify();
	}
	
	public void addStockChart(AnchorPane chartPane)
	{
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Time");
		yAxis.setLabel("Value");
		yAxis.setAutoRanging(true);
		yAxis.setForceZeroInRange(false);
		xAxis.setAnimated(true);
		yAxis.setAnimated(true);
		
		final LineChart<String,Number> lineChart = 
				new LineChart<String,Number>(xAxis,yAxis);
		
		lineChart.setTitle("Google");
		lineChart.setAnimated(true);
		
		lineChart.getData().add(mainApp.getSeries());
		
		chartPane.getChildren().add(lineChart);
	}

	public void createStockChartTask() 
	{
		yqlCon = new YQL_ConTest("GOOG");
		
		final Thread current = mainApp.getThread1();
		try
		{
			
			while(Thread.currentThread()==current)
			{
				while(!isActive)
				{
					wait();
				}
				
				while(Thread.currentThread()==current && isActive)
				{
					yqlCon.connectTo();	
					
					final double tempTradePrice = Double.valueOf(yqlCon.getLastTradePrice());
					System.out.println(tempTradePrice);
		
					System.out.println(TimeUtil.getFormattedTime() + ":" + tempTradePrice);
					
					
					Platform.runLater(new Runnable() 
					{
		                 @Override public void run() 
		                 {			        	 
		                	 if(mainApp.getSeries().getData().size() >= 18)
									mainApp.getSeries().getData().remove(0);
								
								mainApp.getSeries().getData().add(
										new XYChart.Data<String,Number>(
												TimeUtil.getFormattedTime(),
												tempTradePrice)); 
		                 }
		             });
					
					Thread.sleep(7000);
				}
			}
		}
		catch(InterruptedException e) {
			System.out.println("Thread interrupted");
		}
		System.out.println("Thread at end");
	}
}
