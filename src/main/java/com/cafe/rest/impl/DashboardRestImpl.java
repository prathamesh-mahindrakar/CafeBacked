package com.cafe.rest.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.cafe.contants.CafeContants;
import com.cafe.rest.DashboardRest;
import com.cafe.service.DashboardService;
import com.cafe.utils.CafeUtils;

@RestController
@CrossOrigin(origins = "*")
public class DashboardRestImpl implements DashboardRest{

	private DashboardService dashboardService;
	
	@Autowired
	public void setDashboardService(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}




	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		try {
			return dashboardService.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}


