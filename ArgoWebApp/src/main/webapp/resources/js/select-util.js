var locationConfig = {
	selectpicker: false,
	countryControlId: null,
	provinceControlId: null,
	districtControlId: null,
	locationControlId: null,
	provinces: [],
	districts: [],
	locations: []
}

function setLocationControls(countryControlId, provinceControlId, districtControlId, locationControlId, selectpicker) {
	locationConfig.countryControlId = countryControlId;
	locationConfig.provinceControlId = provinceControlId;
	locationConfig.districtControlId = districtControlId;
	locationConfig.locationControlId = locationControlId;
	locationConfig.selectpicker = selectpicker;
}

function setLocationValues(provinces, districts, locations) {
	locationConfig.provinces = provinces;
	locationConfig.districts = districts;
	locationConfig.locations = locations;
}

function selectCountry(countryControlId, provinceControlId, 
		districtControlId, locationControlId, 
		allValue, allName, selectpicker, 
		selectedCountryId, selectedProvinceId, selectedDistrictId, selectedLocationId) {
	if (countryControlId == null || countryControlId == undefined) {
		countryControlId = locationConfig.countryControlId; 
	}
	
	if (provinceControlId == null || provinceControlId == undefined) {
		provinceControlId = locationConfig.provinceControlId; 
	}
	
	if (!countryControlId || !provinceControlId) {
		return;
	}
	
	var countryId = selectedCountryId;
	if (countryId == null || countryId == undefined) {
		countryId = $('#' + countryControlId).val();
		if (countryId == null || countryId == undefined) {
			return;
		}
	} else {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			$('#' + countryControlId).selectpicker('val', selectedCountryId);
		} else {
			$('#' + countryControlId).val(selectedCountryId);
		}
	}
	
	var provinceEl = $('#' + provinceControlId); 
	provinceEl.empty();
	
	if (allValue != null && allValue != undefined 
			&& allName != null && allName != undefined && allName != '') {
		provinceEl.append(
			$('<option>').attr('value', allValue).text(allName)
		);
	}
	
	if (!locationConfig.provinces) {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			provinceEl.selectpicker('refresh');
		}
		
		selectProvince(provinceControlId, districtControlId, locationControlId, 
				allValue, allName, selectpicker, 
				selectedProvinceId, selectedDistrictId, selectedLocationId);
		return;
	}
	
	var items = locationConfig.provinces[countryId];
	if (!items) {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			provinceEl.selectpicker('refresh');
		}
		
		selectProvince(provinceControlId, districtControlId, locationControlId, 
				allValue, allName, selectpicker, 
				selectedProvinceId, selectedDistrictId, selectedLocationId);
		return;
	}
		
	items.forEach(function(item) {
		provinceEl.append(
			$('<option>').attr('value', item.value).text(item.name)
		);
	})
	
	if (locationConfig.selectpicker == true || selectpicker == true) {
		provinceEl.selectpicker('refresh');
	}
	
	selectProvince(provinceControlId, districtControlId, locationControlId, 
			allValue, allName, selectpicker, 
			selectedProvinceId, selectedDistrictId, selectedLocationId);
}

function selectProvince(provinceControlId, districtControlId, 
		locationControlId, allValue, allName, selectpicker,
		selectedProvinceId, selectedDistrictId, selectedLocationId) {
	if (provinceControlId == null || provinceControlId == undefined) {
		provinceControlId = locationConfig.provinceControlId; 
	}
	
	if (districtControlId == null || districtControlId == undefined) {
		districtControlId = locationConfig.districtControlId; 
	}
	
	if (!provinceControlId || !districtControlId) {
		return;
	}
	
	var provinceId = selectedProvinceId;
	if (provinceId == null || provinceId == undefined) {
		provinceId = $('#' + provinceControlId).val();
		if (provinceId == null || provinceId == undefined) {
			return;
		}
	} else {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			$('#' + provinceControlId).selectpicker('val', selectedProvinceId);
		} else {
			$('#' + provinceControlId).val(selectedProvinceId);
		}
	}
	
	var districtEl = $('#' + districtControlId); 
	districtEl.empty();
	
	if (allValue != null && allValue != undefined 
			&& allName != null && allName != undefined && allName != '') {
		districtEl.append(
			$('<option>').attr('value', allValue).text(allName)
		);
	}
	
	if (!locationConfig.districts) {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			districtEl.selectpicker('refresh');
		}
		
		selectDistrict(districtControlId, locationControlId, 
				allValue, allName, selectpicker, 
				selectedDistrictId, selectedLocationId);
		return;
	}
	
	var items = locationConfig.districts[provinceId];
	if (!items) {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			districtEl.selectpicker('refresh');
		}
		
		selectDistrict(districtControlId, locationControlId, 
				allValue, allName, selectpicker, 
				selectedDistrictId, selectedLocationId);
		return;
	}
		
	items.forEach(function(item) {
		districtEl.append(
			$('<option>').attr('value', item.value).text(item.name)
		);
	})
	
	if (locationConfig.selectpicker == true || selectpicker == true) {
		districtEl.selectpicker('refresh');
	}
	
	selectDistrict(districtControlId, locationControlId, 
			allValue, allName, selectpicker, 
			selectedDistrictId, selectedLocationId);
}

function selectDistrict(districtControlId, locationControlId, 
		allValue, allName, selectpicker, 
		selectedDistrictId, selectedLocationId) {
	if (districtControlId == null || districtControlId == undefined) {
		districtControlId = locationConfig.districtControlId; 
	}
	
	if (locationControlId == null || locationControlId == undefined) {
		locationControlId = locationConfig.locationControlId; 
	}
	
	if (!districtControlId || !locationControlId) {
		return;
	}
	
	var districtId = selectedDistrictId;
	if (districtId == null || districtId == undefined) {
		districtId = $('#' + districtControlId).val();
		if (districtId == null || districtId == undefined) {
			return;
		}
	} else {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			$('#' + districtControlId).selectpicker('val', selectedDistrictId);
		} else {
			$('#' + districtControlId).val(selectedDistrictId);
		}
	}
	
	var locationEl = $('#' + locationControlId); 
	locationEl.empty();
	
	if (allValue != null && allValue != undefined 
			&& allName != null && allName != undefined && allName != '') {
		locationEl.append(
			$('<option>').attr('value', allValue).text(allName)
		);
	}
	
	if (!locationConfig.locations) {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			locationEl.selectpicker('refresh');
		}
		
		selectLocation(locationControlId, selectpicker, selectedLocationId);
		return;
	}
	
	var items = locationConfig.locations[districtId];
	if (!items) {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			locationEl.selectpicker('refresh');
		}
		
		selectLocation(locationControlId, selectpicker, selectedLocationId);
		return;
	}
		
	items.forEach(function(item) {
		locationEl.append(
			$('<option>').attr('value', item.value).text(item.name)
		);
	})
	
	if (locationConfig.selectpicker == true || selectpicker == true) {
		locationEl.selectpicker('refresh');
	}
	
	selectLocation(locationControlId, selectpicker, selectedLocationId);
}

function selectLocation(locationControlId, selectpicker, selectedLocationId) {
	if (locationControlId == null || locationControlId == undefined) {
		locationControlId = locationConfig.locationControlId; 
	}
	
	if (!locationControlId) {
		return;
	}
	
	if (selectedLocationId != null && selectedLocationId != undefined) {
		if (locationConfig.selectpicker == true || selectpicker == true) {
			$('#' + locationControlId).selectpicker('val', selectedLocationId);
		} else {
			$('#' + locationControlId).val(selectedLocationId);
		}
	}
}

function selectWithAllItem(el, allValue, selectpicker) {
	var values = $(el).val();
	
	if ($.isArray(values) && values.length > 0) {
		for (var i = 0; i < values.length; i++) {
			if (values[i] == allValue) {
				$(el).val([allValue]);
				
				if (selectpicker) {
					$(el).selectpicker('refresh');
				}
				
				break;
			}
		}
	}
}