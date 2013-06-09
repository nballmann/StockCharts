package org.nic.stockCharts;

import java.io.IOException;

import org.nic.stockCharts.controller.ChartPanelController;
import org.nic.stockCharts.controller.RootLayoutController;
import org.nic.stockCharts.util.TimeUtil;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StockCharts extends Application {
	
	private Stage primaryStage;
	private BorderPane rootPane;
	private volatile AnchorPane chartPane;
	
	private RootLayoutController rootController;
	private ChartPanelController chartPanelController;
	
	private Thread t1;
	
	private volatile XYChart.Series<String, Number> series;
	
//	private StockInfo googleStock;
	
	public synchronized XYChart.Series<String, Number> getSeries()		{ return series; }
	public ChartPanelController getChartPanelController()	{ return chartPanelController; }
	public Thread getThread1()	{ return t1; }
	
	@Override
	public void start(Stage primaryStage) 
	{
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Stock Charts");

		showPrimaryStage(primaryStage);
		
		createChartPane();
		
		series = new XYChart.Series<String,Number>();

		series.setName("platzhalter");
		
		chartPanelController.addStockChart(chartPane);
		

		t1 = new Thread(new Task<Void>() {

			@Override
			protected Void call() throws Exception 
			{
				chartPanelController.createStockChartTask();
				
				return null;
			}
			
		});
		
		t1.setDaemon(true);
		t1.start();
		
	}
	
	
	private void showPrimaryStage(Stage primaryStage)
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(StockCharts.class.getResource("view/rootLayout.fxml"));
			rootPane = (BorderPane) loader.load();
			
			rootController = loader.getController();
			rootController.setMainApp(this);
			
			Scene scene = new Scene(rootPane);
			
			primaryStage.setScene(scene);
			primaryStage.show();
			
		}
		catch(IOException e) 
		{
			e.printStackTrace(); 
		}
	}
	
	private void createChartPane()
	{
		try
		{
			FXMLLoader loader = new FXMLLoader(StockCharts.class.getResource("view/chartPanelView.fxml"));
			chartPane = (AnchorPane) loader.load();
			
			chartPanelController = loader.getController();
			chartPanelController.setMainApp(this);
			
			rootPane.setCenter(chartPane);
		}
		catch(IOException e) {}
	}
	
//	private class StockChartService extends Service<Void>
//	{
//
//		@Override
//		protected Task<Void> createTask() 
//		{
//			chartPanelController.runStockChart();
//		
//			return null;
//		}
//		
//	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
