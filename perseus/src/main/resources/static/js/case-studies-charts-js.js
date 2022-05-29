function drawDashboard() {
	setupDates();
	setUserNumber();
	drawQuizAnswers();
	drawFeelingsPieChart();
	drawVisionCountOverTime();
	drawMatchesPlayedOverTime();
	drawMatchesGraph();
	drawVisionFeelingsOverTime();
	drawQuickStats();
	drawEmpathyDistribution()
	drawMostUsedKeywords();
	drawMostUsedDescriptionWords();
	//drawTopVisions();
	drawSankeyFeelings();
}

function setupDates() {
	
	var dateOffset = (24 * 60 * 60 * 1000) * 7;
	
	var before_week = new Date();
	var after_week = new Date();

	before_week.setTime(before_week.getTime() - dateOffset);
	after_week.setTime(after_week.getTime() + dateOffset);

	var day_before = ("0" + before_week.getDate()).slice(-2);
	var month_before = ("0" + (before_week.getMonth() + 1)).slice(-2);
	var date_before = before_week.getFullYear() + "-" + (month_before) + "-" + (day_before);
	
	var day_after = ("0" + after_week.getDate()).slice(-2);
	var month_after = ("0" + (after_week.getMonth() + 1)).slice(-2);
	var date_after = after_week.getFullYear() + "-" + (month_after) + "-" + (day_after);
	
	$('#start_date_vision').val(date_before);
	$('#end_date_vision').val(date_after);
	$('#start_date_vision_feelings').val(date_before);
	$('#end_date_vision_feelings').val(date_after);
	$('#start_date_match').val(date_before);
	$('#end_date_match').val(date_after);
}

function setUserNumber() {
	// Create our data table.
	
	$.ajax({
		type: "GET",
		url: "/getUserNumber",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
		},
		success: function(active_users){
			$('#n-users').text(active_users + "");
		}
	});
}


$(document).on('change', '#age_filter, #gender_filter, #nationality_filter', function() {
	drawQuizAnswers();
})

function drawQuizAnswers() {
	
	$.ajax({
		type: "GET",
		url: "/getQuizAnswersData",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
			'genderFilter': $("#gender_filter option:selected").val(),
			'ageFilter': $("#age_filter option:selected").val(),
			'nationalityFilter': $("#nationality_filter option:selected").val(),
		},
		success: function(counts){
			
			var dashboard = new google.visualization.Dashboard(document.getElementById('quiz-answers'));
			var data = google.visualization.arrayToDataTable([['id_question', 'Answer', 'Total', '', {role: 'style', type: 'string'}]].concat(counts));
			var ctitle = 'Answers';

			
			var initState = { selectedValues: [] };
			// replace the 2 below with the index required
			initState.selectedValues.push(data.getColumnRange(3).min);
			
			// Create a range slider, passing some options
		  	var slider = new google.visualization.ControlWrapper({
	    		'controlType': 'CategoryFilter',
	    		'containerId': 'filter_div',
	    		'options': {
	      			'filterColumnIndex': '3',
					'ui': {
	            		'allowMultiple': false,
	                    'allowNone': false,
	                    'selectedValuesLayout': 'belowWrapped',
						'allowTyping': false
	                },
	    		},
				'state': initState,
		  	});
		
		  	// Create a pie chart, passing some options
		 	 var columnChart = new google.visualization.ChartWrapper({
		    	'chartType': 'ColumnChart',
		   		'containerId': 'chart_div',
		    	'options': {
		      		'title': ctitle,
					'legend': { position: 'none' }
		    	},
				'view': {'columns': [1, 2, 4]}
		  	});
		 	
		 	quizData = data;
		 	
			dashboard.bind(slider, columnChart);
			
			dashboard.draw(data);
			
			quizDashboard = dashboard;
			
			google.visualization.events.addListener(dashboard, 'ready', function () {
			    $(".goog-menu-button-outer-box").css({'background-color': '#FFFFFF'});
			    $(".goog-menu-button-caption").css({'padding': '.2rem .55rem'});
			    $(".goog-menu-button-dropdown").css({'margin-top': '0.5rem'});
			    $(".goog-menu-button-dropdown").css({'margin-left': '0.75rem'});
			});
		}
	});
}

function drawFeelingsPieChart() {
	
	$.ajax({
		type: "GET",
		url: "/getFeelings",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
		},
		success: function(feelings_count){
		
			$('#feelings-pie-chart').height(450);
			
			var data = new google.visualization.DataTable();
			var ctitle = "Feelings Distribution";
			
			data.addColumn('string', 'Feeling');
			data.addColumn('number', 'Count');
			
			data.addRows(feelings_count);
				
			var options = {
	          	title: ctitle,
	          	legend: { position: 'none' },
	          	chartArea: { width: 400, height: 400 },
	        };
			
			var chart = new google.visualization.PieChart(document.getElementById('feelings-pie-chart'));
			
        	chart.draw(data, options);
		}
	});
}

$(document).on('change', '#start_date_vision, #end_date_vision', function() {
	drawVisionCountOverTime();
})

function drawVisionCountOverTime() {
	
	$.ajax({
		type: "GET",
		url: "/getVisionCountOverTime",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
			'startDate': $("#start_date_vision").val(),
			'endDate': $("#end_date_vision").val(),
		},
		success: function(visions_count){
			var data = new google.visualization.DataTable();
	
			data.addColumn('string', 'Date');
			data.addColumn('number', 'Visions Count');
			
			data.addRows(visions_count);
			
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
			
		    var chart = new google.visualization.LineChart(document.getElementById('vision-count-over-time'));
		    chart.draw(data, options);
		}
	});
}

$(document).on('change', '#start_date_vision_feelings, #end_date_vision_feelings', function() {
	drawVisionFeelingsOverTime();
})

function drawVisionFeelingsOverTime() {

	$.ajax({
		type: "GET",
		url: "/getVisionFeelingsOverTime",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
			'startDate': $("#start_date_vision_feelings").val(),
			'endDate': $("#end_date_vision_feelings").val(),
		},
		success: function(feelings_count){
			var data = new google.visualization.DataTable();
	
			data.addColumn('string', 'Date');
			data.addColumn('number', 'Joy');
			data.addColumn('number', 'Trust');
			data.addColumn('number', 'Fear');
			data.addColumn('number', 'Surprise');
			data.addColumn('number', 'Sadness');
			data.addColumn('number', 'Disgust');
			data.addColumn('number', 'Anger');
			data.addColumn('number', 'Anticipation');
			
			data.addRows(feelings_count);
			
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
			
		    var chart = new google.visualization.LineChart(document.getElementById('vision-feelings-over-time'));
		    chart.draw(data, options);
			
		}
	});

}

$(document).on('change', '#start_date_match, #end_date_match', function() {
	drawMatchesPlayedOverTime();
})

function drawMatchesPlayedOverTime() {
	
	$.ajax({
		type: "GET",
		url: "/getMatchesPlayedOverTime",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
			'startDate': $("#start_date_match").val(),
			'endDate': $("#end_date_match").val(),
		},
		success: function(matches_count){
			var data = new google.visualization.DataTable();
	
			data.addColumn('string', 'Date');
			data.addColumn('number', 'Match Played Count');
			
			data.addRows(matches_count);
			
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
			
		    var chart = new google.visualization.LineChart(document.getElementById('matches-played-over-time'));
		    chart.draw(data, options);
		}
	});
}

function drawMatchesGraph() {

    d3.json("getMatchesNetwork?idScenario=" + $("#currentScenario option:selected").val()).then(function (graph) {
    
    	if(graph.nodes.length == 0) {
	    	$('#matches-graph').replaceWith('<h2 class="text-center" id="matches-graph">No content Available</h2>')
	    } else {
	    	$('#matches-graph').replaceWith('<svg id="matches-graph" class="my-3" style="background-color:white;"></svg>')
	    }
    
    	var width = $('.container').width();
		var height = $(window).innerHeight()*0.7;
		var color = d3.scaleOrdinal(d3.schemeCategory10);
		
    	var label = {
		    'nodes': [],
		    'links': []
		};
		
		graph.nodes.forEach(function(d, i) {
		    label.nodes.push({node: d});
		    label.nodes.push({node: d});
		    label.links.push({
		        source: i * 2,
		        target: i * 2 + 1
		    });
		});
		
		var labelLayout = d3.forceSimulation(label.nodes)
						    .force("charge", d3.forceManyBody().strength(-50))
						    .force("link", d3.forceLink(label.links).distance(0).strength(2));
	    
	    var graphLayout = d3.forceSimulation(graph.nodes)
						    .force("charge", d3.forceManyBody().strength(-3000))
						    .force("center", d3.forceCenter(width / 2, height / 2))
						    .force("x", d3.forceX(width / 2).strength(1))
						    .force("y", d3.forceY(height / 2).strength(1))
						    .force("link", d3.forceLink(graph.links).id(function(d) {return d.id; }).distance(50).strength(1))
						    .on("tick", ticked);

		var adjlist = [];

		graph.links.forEach(function(d) {
		    adjlist[d.source.index + "-" + d.target.index] = true;
		    adjlist[d.target.index + "-" + d.source.index] = true;
		});
		
		function neigh(a, b) {
		    return a == b || adjlist[a + "-" + b];
		}
		
		var svg = d3.select("#matches-graph").attr("width", width).attr("height", height);
		var container = svg.append("g");
		
		svg.call(
		    d3.zoom()
		        .scaleExtent([.1, 4])
		        .on("zoom", function() { container.attr("transform", d3.event.transform); })
		);
		
		var link = container.append("g").attr("class", "links")
		    .selectAll("line")
		    .data(graph.links)
		    .enter()
		    .append("line")
		    .attr("stroke", "#aaa")
		    .attr("stroke-width", "1px");
		
		var node = container.append("g").attr("class", "nodes")
		    .selectAll("g")
		    .data(graph.nodes)
		    .enter()
		    .append("circle")
		    .attr("r", 5);
		
		node.on("mouseover", focus).on("mouseout", unfocus);
		
		node.call(
		    d3.drag()
		        .on("start", dragstarted)
		        .on("drag", dragged)
		        .on("end", dragended)
		);
		
		var labelNode = container.append("g").attr("class", "labelNodes")
		    .selectAll("text")
		    .data(label.nodes)
		    .enter()
		    .append("text")
		    .text(function(d, i) { return i % 2 == 0 ? "" : ""; }) //d.node.id; })
		    .style("fill", "#555")
		    .style("font-family", "Arial")
		    .style("font-size", 12)
		    .style("pointer-events", "none"); // to prevent mouseover/drag capture
		
		node.on("mouseover", focus).on("mouseout", unfocus);
		
		function ticked() {
		
		    node.call(updateNode);
		    link.call(updateLink);
		
		    labelLayout.alphaTarget(0.3).restart();
		    labelNode.each(function(d, i) {
		        if(i % 2 == 0) {
		            d.x = d.node.x;
		            d.y = d.node.y;
		        } else {
		            var b = this.getBoundingClientRect();
		
		            var diffX = d.x - d.node.x;
		            var diffY = d.y - d.node.y;
		
		            var dist = Math.sqrt(diffX * diffX + diffY * diffY);
		
		            var shiftX = b.width * (diffX - dist) / (dist * 2);
		            shiftX = Math.max(-b.width, Math.min(0, shiftX));
		            var shiftY = 16;
		            this.setAttribute("transform", "translate(" + shiftX + "," + shiftY + ")");
		        }
		    });
		    labelNode.call(updateNode);
		
		}
		
		function fixna(x) {
		    if (isFinite(x)) return x;
		    return 0;
		}
		
		function focus(d) {
		    var index = d3.select(d3.event.target).datum().index;
		    node.style("opacity", function(o) {
		        return neigh(index, o.index) ? 1 : 0.1;
		    });
		    labelNode.attr("display", function(o) {
		      return neigh(index, o.node.index) ? "block": "none";
		    });
		    link.style("opacity", function(o) {
		        return o.source.index == index || o.target.index == index ? 1 : 0.1;
		    });
		}
		
		function unfocus() {
		   labelNode.attr("display", "block");
		   node.style("opacity", 1);
		   link.style("opacity", 1);
		}
		
		function updateLink(link) {
		    link.attr("x1", function(d) { return fixna(d.source.x); })
		        .attr("y1", function(d) { return fixna(d.source.y); })
		        .attr("x2", function(d) { return fixna(d.target.x); })
		        .attr("y2", function(d) { return fixna(d.target.y); });
		}
		
		function updateNode(node) {
		    node.attr("transform", function(d) {
		        return "translate(" + fixna(d.x) + "," + fixna(d.y) + ")";
		    });
		}
		
		function dragstarted(d) {
		    d3.event.sourceEvent.stopPropagation();
		    if (!d3.event.active) graphLayout.alphaTarget(0.3).restart();
		    d.fx = d.x;
		    d.fy = d.y;
		}
		
		function dragged(d) {
		    d.fx = d3.event.x;
		    d.fy = d3.event.y;
		}
		
		function dragended(d) {
		    if (!d3.event.active) graphLayout.alphaTarget(0);
		    d.fx = null;
		    d.fy = null;
		}
	    
    });
    
}

function drawQuickStats() {
	
	$.ajax({
		type: "GET",
		url: "/getQuickStats",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
		},
		success: function(result){
			
			$('#n-visions').text(result[0] + "");
			$('#n-matches').text(result[1] + "");
			$('#emp-perc').text(result[2] + " %");
			
		}
	});
}

function drawEmpathyDistribution() {
	
	$.ajax({
		type: "GET",
		url: "/getEmpathyDistribution",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
		},
		success: function(empathy_distribution){
			
			var data = new google.visualization.DataTable();
	
			data.addColumn('string', 'Empathy Percentage');
			data.addColumn('number', 'Frequency');
			
			data.addRows(empathy_distribution);
			
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
			
		    var chart = new google.visualization.ColumnChart(document.getElementById('empathy-distribution'));
		    chart.draw(data, options);
			
		}
	});
}

function drawMostUsedKeywords() {

	$.ajax({
		type: "GET",
		url: "/getMostUsedKeywords",
		data: {
			'idScenario': $("#currentScenario option:selected").val() 
		},
		success: function(data){
			words = JSON.parse(data);
			
			var layout_width = $('.container').width()*0.95;
			var layout_height = $(window).innerHeight()*0.3;
			
			d3.layout.cloud()
				.size([layout_width, layout_height])
				.rotate(function() { return ~~(Math.random() * 45); })
				.words(words)
				.text(function(d){return d.text;})
				.padding(8)
				.fontSize(function(d) {
					return d.size*15;
				})
				.on("end", drawKeywords)
				.start();
		}
	});
}

function drawMostUsedDescriptionWords() {
	
	$.ajax({
		type: "GET",
		url: "/getMostUsedDescriptionWords",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
		},
		success: function(data){
		
			words = JSON.parse(data);
			
			var layout_width = $('.container').width()*0.95;
			var layout_height = $(window).innerHeight()*0.3;
			
			d3.layout.cloud()
				.size([layout_width, layout_height])
				.rotate(function() { return ~~(Math.random() * 45); })
				.words(words)
				.text(function(d){return d.text;})
				.padding(8)
				.fontSize(function(d) {
					return d.size*15;
				})
				.on("end", drawDescriptionWords)
				.start();
		}
	});
}

function drawKeywords(words) {
	var layout_width = $('.container').width()*0.95;
	var layout_height = $(window).innerHeight()*0.3;
	
	if($("#most-used-keywords").children("svg").length == 0){
		d3.select("#most-used-keywords").append("svg")
		    	.attr("width", layout_width)
		    	.attr("height", layout_height)
		    	.attr("class", "wordcloud")
		    	.style("margin-top", "5px")
		    	.style("background-color", "white")
		    	.append("g")
		    	.attr("transform", "translate(" + layout_width / 2 + "," + layout_height / 2 + ")")
		    .selectAll("text")
		    	.data(words)
		    .enter().append("text")
		    	.style("font-size", function(d) { return d.size + "px"; })
		    	.attr("text-anchor", "middle")
			    .attr("transform", function(d) {
			    	return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
			    })
			    .text(function(d) { return d.text; });
	}
}

function drawDescriptionWords(words) {
	var layout_width = $('.container').width()*0.95;
	var layout_height = $(window).innerHeight()*0.3;
	
	if($("#most-used-description-words").children("svg").length == 0){
		d3.select("#most-used-description-words").append("svg")
		    	.attr("width", layout_width)
		    	.attr("height", layout_height)
		    	.attr("class", "wordcloud")
		    	.style("margin-top", "5px")
		    	.style("background-color", "white")
		    	.append("g")
		    	.attr("transform", "translate(" + layout_width / 2 + "," + layout_height / 2 + ")")
		    .selectAll("text")
		    	.data(words)
		    .enter().append("text")
		    	.style("font-size", function(d) { return d.size + "px"; })
		    	.attr("text-anchor", "middle")
			    .attr("transform", function(d) {
			    	return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
			    })
			    .text(function(d) { return d.text; });
	}
}

function drawTopVisions() {
	
	$.ajax({
		type: "GET",
		url: "/getTopVisions",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
		},
		success: function(result){
			
			
		}
	});
}

function drawSankeyFeelings() {
	
	$.ajax({
		type: "GET",
		url: "/getSankeyFeelings",
		data: {
			'idScenario': $("#currentScenario option:selected").val(),
		},
		success: function(counts){
		
			var data = new google.visualization.DataTable();
			var ctitle = 'Feelings Shift';
			data.addColumn('string', 'From');
			data.addColumn('string', 'To');
			data.addColumn('number', 'Weight');

			data.addRows(counts);
			
			var options = {
	          	title: ctitle,
	          	legend: { position: 'none' },
	        };
	        
			var chart = new google.visualization.Sankey(document.getElementById('sankey-feelings'));
			
        	chart.draw(data, options);
        	
        	if(counts.length == 0) {
				$('#sankey-feelings').html('<h2 class="text-center">No content Available</h2>')
			}
		}
	});
}