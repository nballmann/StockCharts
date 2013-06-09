package org.nic.stockCharts.controller;

import org.nic.stockCharts.StockCharts;

import javafx.fxml.FXML;

public class RootLayoutController 
{
	private StockCharts mainApp;

	@FXML
	private void handleExit()
	{
		System.exit(0);
	}
	
	@FXML
	private void handleAbout()
	{
		mainApp.getChartPanelController().setActiveStatus();
		System.out.println(mainApp.getChartPanelController().getActiveStatus());
	}
	
	
	
	public void setMainApp(StockCharts mainApp)
	{
		this.mainApp = mainApp;
	}
	
}
