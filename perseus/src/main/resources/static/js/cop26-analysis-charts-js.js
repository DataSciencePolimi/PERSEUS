/*
	Funzione Base
*/

function drawDashboard() {
	drawTopics();
	drawDataVolumesPerDay();
	drawEUMentions();
	drawEUMentionsOverTime();
	drawClimatePolicySentiment();
	drawClimatePolicySentimentOverTime();
	drawClimateChangeSentiment();
	drawClimateChangeSentimentOverTime();
}

var topic_data = null;
function drawTopics() {

	$.ajax({
		type: "GET",
		url: "/getTopicsData",
		success: function(json){
			topic_data = JSON.parse(json);
			parseTopicsData(1, 1);
			parseTopicsDataOverall();
		}
	});

}

function parseTopicsData(topic, rep_type) {
	var data = new google.visualization.DataTable();
	
	data.addColumn('string', 'Date');
	
	var options = {
					height: 450,
					chartArea: {
				      	bottom: 120,
				      	top: 50,
				      	left: '7%',
				      	right: '2%',
				      	top: '8%',
				      	width: '100%',
				      	height: '100%'
				    },
					legend: {
						position: "bottom"
					},
					curveType: "function",
					vAxis: {
			            viewWindow: {
				        	min: 0
				        }
			        },
			        hAxis: {
			        	slantedText: true
			        }
	};
	
	if(topic == 1) {
		if(rep_type == 2) {
			data.addColumn('number', 'Climate Refugees');
			data.addColumn('number', 'Developing Countries');
			data.addColumn('number', 'Adaptation');
			data.addColumn('number', 'Financial Support');
			data.addColumn('number', 'Technical Support');
			data.addColumn('number', 'Loss and Damage');
			data.addColumn('number', 'Vulnerable Communities');
			data.addColumn('number', 'Local Communities');
			data.addColumn('number', 'Indigenous People');
			data.addColumn('number', 'Gender');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].climate_refugees, topic_data[x].developing_countries,
							 topic_data[x].adaptation, topic_data[x].financial_support,
							 topic_data[x].technical_support, topic_data[x].loss_and_damage,
							 topic_data[x].vulnerable_communities, topic_data[x].local_communities,
							 topic_data[x].indigenous_people, topic_data[x].gender]);
			}
		} else {
			data.addColumn('number', 'Volume');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].climate_refugees + topic_data[x].developing_countries
							 + topic_data[x].adaptation + topic_data[x].financial_support
							 + topic_data[x].technical_support + topic_data[x].loss_and_damage
							 + topic_data[x].vulnerable_communities + topic_data[x].local_communities
							 + topic_data[x].indigenous_people + topic_data[x].gender]);
			}
			
			options.legend.position = "none";
			options.chartArea.bottom = 70;
		}
	} else if(topic == 2) {
		if(rep_type == 2) {
			data.addColumn('number', 'LULUCF');
			data.addColumn('number', 'Carbon Pricing');
			data.addColumn('number', 'Climate Finance');
			data.addColumn('number', 'Sustainable Finance');
			data.addColumn('number', 'Green Recovery');
			data.addColumn('number', 'Corona Recovery');
			data.addColumn('number', 'Carbon Credits');
			data.addColumn('number', 'Nature Based Solutions');
			data.addColumn('number', 'Biodiversity Loss');
			data.addColumn('number', 'Phase Out');
			data.addColumn('number', 'Harmful Subsidies');
			data.addColumn('number', 'CCS');
			data.addColumn('number', 'Negative Emissions');
			data.addColumn('number', 'Geoengineering');
			data.addColumn('number', 'Border Tax');
			data.addColumn('number', 'Technology Transfer');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].LULUCF, topic_data[x].carbon_pricing,
							 topic_data[x].climate_finance, topic_data[x].sustainable_finance,
							 topic_data[x].green_recovery, topic_data[x].corona_recovery,
							 topic_data[x].carbon_credits, topic_data[x].nature_based_solutions,
							 topic_data[x].biodiversity_loss, topic_data[x].phase_out,
							 topic_data[x].harmful_subsidies, topic_data[x].CCS,
							 topic_data[x].negative_emissions, topic_data[x].geoengineering,
							 topic_data[x].border_tax, topic_data[x].technology_transfer]);
			}
		} else {
			data.addColumn('number', 'Volume');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].LULUCF + topic_data[x].carbon_pricing
							 + topic_data[x].climate_finance + topic_data[x].sustainable_finance
							 + topic_data[x].green_recovery + topic_data[x].corona_recovery
							 + topic_data[x].carbon_credits + topic_data[x].nature_based_solutions
							 + topic_data[x].biodiversity_loss + topic_data[x].phase_out
							 + topic_data[x].harmful_subsidies + topic_data[x].CCS
							 + topic_data[x].negative_emissions + topic_data[x].geoengineering
							 + topic_data[x].border_tax + topic_data[x].technology_transfer]);
			}
			
			options.legend.position = "none";
			options.chartArea.bottom = 70;
		}
	} else if(topic == 3) {
		if(rep_type == 2) {
			data.addColumn('number', 'NDC Comparability');
			data.addColumn('number', 'Global Stocktake');
			data.addColumn('number', 'Collective Progress');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].ndc_comparability, topic_data[x].global_stocktake,
							 topic_data[x].collective_progress]);
			}
		} else {
			data.addColumn('number', 'Volume');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].ndc_comparability + topic_data[x].global_stocktake
							 + topic_data[x].collective_progress]);
			}
			
			options.legend.position = "none";
			options.chartArea.bottom = 70;
		}
	} else if(topic == 4) {
		if(rep_type == 2) {
			data.addColumn('number', 'Food & Agriculture');
			data.addColumn('number', 'Just Energy Transition');
			data.addColumn('number', 'Coal');
			data.addColumn('number', 'International Aviation');
			data.addColumn('number', 'International Marine Transport');
			data.addColumn('number', 'Combustion Engine');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].food_and_agriculture, topic_data[x].just_energy_transition,
							 topic_data[x].coal, topic_data[x].international_aviation,
							 topic_data[x].international_marine_transport, topic_data[x].combustion_engine]);
			}
		} else {
			data.addColumn('number', 'Volume');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].food_and_agriculture + topic_data[x].just_energy_transition
							 + topic_data[x].coal + topic_data[x].international_aviation
							 + topic_data[x].international_marine_transport + topic_data[x].combustion_engine]);
			}
			
			options.legend.position = "none";
			options.chartArea.bottom = 70;
		}
	} else if(topic == 5) {
		if(rep_type == 2) {
			data.addColumn('number', '1.5 Degrees');
			data.addColumn('number', 'NDC Ambition');
			data.addColumn('number', 'Net Zero Target');
			data.addColumn('number', 'Decarbonization');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].degrees, topic_data[x].NDC_ambition,
							 topic_data[x].net_zero_target, topic_data[x].decarbonization]);
			}
		} else {
			data.addColumn('number', 'Volume');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].degrees + topic_data[x].NDC_ambition
							 + topic_data[x].net_zero_target + topic_data[x].decarbonization]);
			}
			
			options.legend.position = "none";
			options.chartArea.bottom = 70;
		}
	} else if(topic == 6) {
		if(rep_type == 2) {
			data.addColumn('number', 'NDC Fair');
			data.addColumn('number', 'CBDR');
			data.addColumn('number', 'CBDR-RC');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].NDC_fair, topic_data[x].CBDR,
							 topic_data[x].CBDR_RC]);
			}
		} else {
			data.addColumn('number', 'Volume');
			
			var objCount = topic_data.length;
			for (var x = 0; x < objCount ; x++) {
				data.addRow([topic_data[x].date, 
							 topic_data[x].NDC_fair + topic_data[x].CBDR
							 + topic_data[x].CBDR_RC]);
			}
			
			options.legend.position = "none";
			options.chartArea.bottom = 70;
		}
	}
	
    var chart = new google.visualization.LineChart(document.getElementById('topic_data'));
    chart.draw(data, options);
}

function parseTopicsDataOverall() {
	var data = new google.visualization.DataTable();
	
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Climate Change Affected Groups');
	data.addColumn('number', 'Climate Policy Instruments');
	data.addColumn('number', 'Monitoring Progress');
	data.addColumn('number', 'Sectors');
	data.addColumn('number', 'Ambition');
	data.addColumn('number', 'Contribution Distribution');
	
	var objCount = topic_data.length;
	for (var x = 0; x < objCount ; x++) {
		data.addRow([topic_data[x].date, 
					 Math.round((topic_data[x].climate_refugees + topic_data[x].developing_countries
					 + topic_data[x].adaptation + topic_data[x].financial_support
					 + topic_data[x].technical_support + topic_data[x].loss_and_damage
					 + topic_data[x].vulnerable_communities + topic_data[x].local_communities
					 + topic_data[x].indigenous_people + topic_data[x].gender) / 10),
					 Math.round((topic_data[x].LULUCF + topic_data[x].carbon_pricing
					 + topic_data[x].climate_finance + topic_data[x].sustainable_finance
					 + topic_data[x].green_recovery + topic_data[x].corona_recovery
					 + topic_data[x].carbon_credits + topic_data[x].nature_based_solutions
					 + topic_data[x].biodiversity_loss + topic_data[x].phase_out
					 + topic_data[x].harmful_subsidies + topic_data[x].CCS
					 + topic_data[x].negative_emissions + topic_data[x].geoengineering
					 + topic_data[x].border_tax + topic_data[x].technology_transfer) / 16),
					 Math.round((topic_data[x].ndc_comparability + topic_data[x].global_stocktake
					 + topic_data[x].collective_progress) / 3),
					 Math.round((topic_data[x].food_and_agriculture + topic_data[x].just_energy_transition
					 + topic_data[x].coal + topic_data[x].international_aviation
					 + topic_data[x].international_marine_transport + topic_data[x].combustion_engine) / 6),
					 Math.round((topic_data[x].degrees + topic_data[x].NDC_ambition
					 + topic_data[x].net_zero_target + topic_data[x].decarbonization) / 4),
					 Math.round((topic_data[x].NDC_fair + topic_data[x].CBDR
					 + topic_data[x].CBDR_RC) / 3)]);
	}
	
	var options = {
					height: 450,
					chartArea: {
				      	bottom: 120,
				      	top: 50,
				      	left: '7%',
				      	right: '2%',
				      	top: '8%',
				      	width: '100%',
				      	height: '100%'
				    },
					legend: {
						position: "bottom"
					},
					curveType: "function",
					vAxis: {
			            viewWindow: {
				        	min: 0
				        }
			        },
			        hAxis: {
			        	slantedText: true
			        }
	};

    var chart = new google.visualization.LineChart(document.getElementById('topic_data_summary'));
    chart.draw(data, options);
}

var selected_topic_descr = 1;
$(document).on('change', '#topic-select', function(e) {
	$('#topic-type').val(1);
	changeKeywords($('#topic-select').val());
	parseTopicsData($('#topic-select').val(), $('#topic-type').val());
	
});

function changeKeywords(topic_number) {
	$("#k-" + selected_topic_descr).hide();
	selected_topic_descr = topic_number
	$("#k-" + topic_number).show();
}

$(document).on('change', '#topic-type', function(e) {
	parseTopicsData($('#topic-select').val(), $('#topic-type').val());
});

var data_volumes = null;
function drawDataVolumesPerDay() {
	
	$.ajax({
		type: "GET",
		url: "/getDataVolumes",
		success: function(json){
			data_volumes = JSON.parse(json);
			parseDataVolumesPerDay();
		}
	});
	
}

function parseDataVolumesPerDay() {
	var data = new google.visualization.DataTable();
	
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Volume');
	
	var objCount = data_volumes.length;
	for (var x = 0; x < objCount ; x++) {
		data.addRow([data_volumes[x].date, data_volumes[x].volume]);
	}
	
	var options = {
					height: 300,
					chartArea: {
				      	bottom: 70,
				      	left: '7%',
				      	right: '2%',
				      	top: '12%',
				      	width: '100%',
				      	height: '100%'
				    },
					legend: {
						position: "none"
					},
					curveType: "function",
					vAxis: {
			            viewWindow: {
				        	min: 0
				        }
			        },
			        hAxis: {
			        	slantedText: true
			        }
	};

    var chart = new google.visualization.LineChart(document.getElementById('data_volumes'));
    chart.draw(data, options);
}

var eu_mentions_data = null;
function drawEUMentions() {
	
	$.ajax({
		type: "GET",
		url: "/getEUMentions",
		success: function(json){
			eu_mentions_data = JSON.parse(json);
			parseEuMentions();
		}
	});
}

function parseEuMentions() {

	var data = new google.visualization.DataTable();
	
	data.addColumn('string', 'Language');
	data.addColumn('number', 'Mentions');
	
	var objCount = eu_mentions_data.length;
	for (var x = 0; x < objCount ; x++) {
		if(eu_mentions_data[x].lang == "en") {
			data.addRow(['English', eu_mentions_data[x].entity_mentions]);
		} else if(eu_mentions_data[x].lang == "de") {
			data.addRow(['German', eu_mentions_data[x].entity_mentions]);
		} else if(eu_mentions_data[x].lang == "fr") {
			data.addRow(['French', eu_mentions_data[x].entity_mentions]);
		} else if(eu_mentions_data[x].lang == "hu") {
			data.addRow(['Hungarian', eu_mentions_data[x].entity_mentions]);
		} else if(eu_mentions_data[x].lang == "pl") {
			data.addRow(['Polish', eu_mentions_data[x].entity_mentions]);
		} else if(eu_mentions_data[x].lang == "pt") {
			data.addRow(['Portoguese', eu_mentions_data[x].entity_mentions]);
		} else if(eu_mentions_data[x].lang == "sv") {
			data.addRow(['Swedish', eu_mentions_data[x].entity_mentions]);
		}
	}
	
	data.sort({column: 0, desc: false});
      
    var view = new google.visualization.DataView(data);
    view.setColumns([0, 1, {
						    calc: "stringify",
						    sourceColumn: 1,
						    type: "string",
						    role: "annotation"
						    }]);

    var options = {
      				legend: {
      					position: "none"
      				},
      				annotations: {
      					alwaysOutside: true
      				},
      			};

    var chart = new google.visualization.ColumnChart(document.getElementById("eu_mentions"));
    chart.draw(view, options);

}

var eu_mentions_data_over_time = null;
function drawEUMentionsOverTime() {
	
	$.ajax({
		type: "GET",
		url: "/getEuMentionsOverTime",
		success: function(json){
			eu_mentions_data_over_time = JSON.parse(json);
			parseEuMentionsOverTime();
		}
	});
}

function parseEuMentionsOverTime() {
	var data = new google.visualization.DataTable();
	
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Volume');
	
	var objCount = eu_mentions_data_over_time.length;
	for (var x = 0; x < objCount ; x++) {
		data.addRow([eu_mentions_data_over_time[x].date, eu_mentions_data_over_time[x].volume]);
	}
	
	var options = {
					height: 300,
					chartArea: {
				      	bottom: 70,
				      	left: '7%',
				      	right: '2%',
				      	top: '12%',
				      	width: '100%',
				      	height: '100%'
				    },
					legend: {
						position: "none"
					},
					curveType: "function",
					vAxis: {
			            viewWindow: {
				        	min: 0
				        }
			        },
			        hAxis: {
			        	slantedText: true
			        }
	};

    var chart = new google.visualization.LineChart(document.getElementById('eu_mentions_over_time'));
    chart.draw(data, options);
}

var policy_parsed_data = null;
function drawClimatePolicySentiment() {
	
	$.ajax({
		type: "GET",
		url: "/getClimatePolicySentiment",
		success: function(json){
			policy_parsed_data = JSON.parse(json);
			parseClimatePolicy();
		}
	});
}

function parseClimatePolicy() {

	var data = google.visualization.arrayToDataTable([
        ["Sentiment", "Count", {role: "style"}],
        ['Positive', policy_parsed_data[0].positive, 'green'],
        ['Neutral', policy_parsed_data[0].neutral, '#dbd40d'],
        ['Negative', policy_parsed_data[0].negative, 'red']
    ]);
      
    var view = new google.visualization.DataView(data);
    view.setColumns([0, 1, {
						    calc: "stringify",
						    sourceColumn: 1,
						    type: "string",
						    role: "annotation"
						    }, 2]);

    var options = {
      				legend: {
      					position: "none"
      				},
      				annotations: {
      					alwaysOutside: true
      				}
      			};

    var chart = new google.visualization.BarChart(document.getElementById('climate_policy_sentiment'));
    chart.draw(view, options);
}

var policy_parsed_data_over_time = null;
function drawClimatePolicySentimentOverTime() {
	
	$.ajax({
		type: "GET",
		url: "/getClimatePolicySentimentOverTime",
		success: function(json){
			policy_parsed_data_over_time = JSON.parse(json);
			parseClimatePolicyOverTime();
		}
	});
}

function parseClimatePolicyOverTime() {
	var data = new google.visualization.DataTable();
	
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Positive');
	data.addColumn('number', 'Neutral');
	data.addColumn('number', 'Negative');
	
	var objCount = policy_parsed_data_over_time.length;
	for (var x = 0; x < objCount ; x++) {
		data.addRow([policy_parsed_data_over_time[x].date, policy_parsed_data_over_time[x].positive,
					 policy_parsed_data_over_time[x].neutral, policy_parsed_data_over_time[x].negative]);
	}
	
	var options = {
					height: 300,
					chartArea: {
				      	bottom: 120,
				      	left: '7%',
				      	right: '2%',
				      	top: '12%',
				      	width: '100%',
				      	height: '100%'
				    },
					legend: {
						position: "bottom"
					},
					curveType: "function",
					vAxis: {
			            viewWindow: {
				        	min: 0
				        }
			        },
			        hAxis: {
			        	slantedText: true
			        },
			        colors: ['green', '#dbd40d', 'red']
	};

    var chart = new google.visualization.LineChart(document.getElementById('climate_policy_sentiment_over_time'));
    chart.draw(data, options);
}

var climate_parsed_data = null;
function drawClimateChangeSentiment() {
	
	$.ajax({
		type: "GET",
		url: "/getClimateChangeSentiment",
		success: function(json){
			climate_parsed_data = JSON.parse(json)
			parseClimateChange();
		}
	});
}

function parseClimateChange() {

	var data = google.visualization.arrayToDataTable([
        ["Sentiment", "Count", {role: "style"}],
        ['Positive', climate_parsed_data[0].positive, 'green'],
        ['Neutral', climate_parsed_data[0].neutral, '#dbd40d'],
        ['Negative', climate_parsed_data[0].negative, 'red']
    ]);
      
    var view = new google.visualization.DataView(data);
    view.setColumns([0, 1, {
						    calc: "stringify",
						    sourceColumn: 1,
						    type: "string",
						    role: "annotation"
						    }, 2]);

    var options = {
      				legend: { position: "none" },
      				annotations: { alwaysOutside: true }
      			};

    var chart = new google.visualization.BarChart(document.getElementById('climate_change_sentiment'));
    chart.draw(view, options);
}

var climate_parsed_data_over_time = null;
function drawClimateChangeSentimentOverTime() {
	
	$.ajax({
		type: "GET",
		url: "/getClimateChangeSentimentOverTime",
		success: function(json){
			climate_parsed_data_over_time = JSON.parse(json)
			parseClimateChangeOverTime();
		}
	});
}

function parseClimateChangeOverTime() {
	var data = new google.visualization.DataTable();
	
	data.addColumn('string', 'Date');
	data.addColumn('number', 'Positive');
	data.addColumn('number', 'Neutral');
	data.addColumn('number', 'Negative');
	
	var objCount = climate_parsed_data_over_time.length;
	for (var x = 0; x < objCount ; x++) {
		data.addRow([climate_parsed_data_over_time[x].date, climate_parsed_data_over_time[x].positive,
					 climate_parsed_data_over_time[x].neutral, climate_parsed_data_over_time[x].negative]);
	}
	
	var options = {
					height: 300,
					chartArea: {
				      	bottom: 120,
				      	left: '7%',
				      	right: '2%',
				      	top: '12%',
				      	width: '100%',
				      	height: '100%'
				    },
					legend: {
						position: "bottom"
					},
					curveType: "function",
					vAxis: {
			            viewWindow: {
				        	min: 0
				        }
			        },
			        hAxis: {
			        	slantedText: true
			        },
			        colors: ['green', '#dbd40d', 'red']
	};

    var chart = new google.visualization.LineChart(document.getElementById('climate_change_sentiment_over_time'));
    chart.draw(data, options);
}