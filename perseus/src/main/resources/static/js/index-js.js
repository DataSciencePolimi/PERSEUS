var url = "/getPerseusHome";

function checkMobile() {
	var check = false;
	(function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
	return check;
}

$(document).ready(function () {
    
	$(document).on('click', '#sidebarCollapse', function () {
        $('#sidebar').toggleClass('active');
        $(this).toggleClass('active');
    });
});

var global_data = null;
var global_narrative = null;

$(document).on('click', '#deep_dives, #foresight_technology, #agg_eurlex, #agg_repo, #scenarios, #perseus_home, #document_search, #document_upload, #back_document_query, #do_login, #cop_26', function(e){
	e.preventDefault();
	window.history.pushState('', null, '/');
	
	var id = e.target.id;
	switch(id) {
		case "foresight_technology":
			url = "/getTechnologyFragment";
			break;
		case "deep_dives":
			url = "/getDeepDivesFragment";
			break;
		case "agg_repo":
			url = "/getRepositoryFragment";
			break;
		case "agg_eurlex":
			url = "/getEurLexDataVizFragment";
			break;
		case "scenarios":
			url = "/getCaseStudiesFragment";
			break;
		case "back_document_query":
			url = "/getDocumentSearchFragment";
			break;
		case "document_search":
			url = "/getDocumentSearchFragment";
			break;
		case "document_upload":
			url = "/getDocumentUploadFragment";
			break;
		case "do_login":
			url = "/login";
			break;
		case "cop_26":
			url = "/getCop26Fragment";
			break;
		default:
			url = "/getPerseusHome";
	}
	
	if(url == "/login") {
		$.ajax({
			type: "POST",
			url: url,
			data: {
				'username': $('#username').val(),
				'password': $('#password').val(),
			},
			success: function(fragment){
				$('#visible-content').replaceWith(fragment);
			}
		});
	} else {
		
		if(url == "/getPerseusHome" && role != null) {
		
			if(role == 'PM') {
			
				$.ajax({
					type: "GET",
					url: "/getPolicymakerButtonFragment",
					success: function(fragment){
						$('#visible-content').replaceWith(fragment);
					}
				});
			
			} else if(role == 'RS') {
			
				$.ajax({
					type: "GET",
					url: "/getResearcherButtonFragment",
					success: function(fragment){
						$('#visible-content').replaceWith(fragment);
					}
				});
			
			} else if(role == 'CT') {
			
				$.ajax({
					type: "GET",
					url: "/getCitizenButtonFragment",
					success: function(fragment){
						$('#visible-content').replaceWith(fragment);
					}
				});
			
			}
		
		} else {
		
			$.ajax({
				type: "GET",
				url: url,
				success: function(fragment){
					$('#visible-content').replaceWith(fragment);
				}
			});
			
		}
	}
	
	if(checkMobile()){
		$('#sidebar').toggleClass('active');
		$('#sidebarCollapse').toggleClass('active');
	}
});

$(document).on('click', '.explore-btn', function(e){
	e.preventDefault();
	var narrative = $(this).data('narrative');
	
	global_narrative = narrative;
	
	$.ajax({
		type: "GET",
		url: "/getCaseViz",
		data: {
			'narrative': narrative
		},
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});
});

$(document).on('click', '.explore-btn', function() {
	if($('#sidebar').hasClass('active')) {
		$('#sidebar').toggleClass('active');
		$('#sidebarCollapse').toggleClass('active');
	}
})

$(document).on('submit', '#document_query_form', function(e){
	e.preventDefault();
	
	url = "/documentSearchQuery";
	
	global_data = $('#document_query_form').serialize();
	
	$.ajax({
		type: "POST",
		url: url,
		data: global_data,
		success: function(fragment) {
			$('#visible-content').replaceWith(fragment);
		}
	});
});

$(document).on('submit', '#document_simple_query_form', function(e){
	e.preventDefault();
	
	url = "/simpleSearchQuery";
	
	global_data = $('#document_simple_query_form').serialize();
	
	$.ajax({
		type: "POST",
		url: url,
		data: global_data,
		success: function(fragment) {
			$('#visible-content').replaceWith(fragment);
		}
	});
});

$(document).on('submit', '#document_upload_form', function(e) {
	e.preventDefault();
	
	$("#documentUploadButton").prop('disabled', true);
	
	var form = $('#document_upload_form')[0];
	var data = new FormData(form);
	
	url = "/documentUpload";
	
	$.ajax({
		type: "POST",
		url: url,
		enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
		data: data,
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});
})

$(document).on('click', '#documentResource', function() {
	if($('#documentResource').val() == "web page") {
		$('#documentFormFragment').hide();
		$('#websiteFormFragment').show();
		$('#downloadLink').val('')
		$('#documentFile').val(null);
		$('#websiteLink').val('')
	} else {
		$('#documentFormFragment').show();
		$('#websiteFormFragment').hide();
		$('#downloadLink').val('');
		$('#documentFile').val(null);
		$('#websiteLink').val('');
	}
})

$(document).on('change', '#confirmUpload', function() {
	if($('#confirmUpload').is(":checked")) {
		$('#documentUploadButton').prop('disabled', false)
	} else {
		$('#documentUploadButton').prop('disabled', true)
	}
})

$(document).on('click', '#publicationDate', function() {
	
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth() + 1;
	var yyyy = today.getFullYear();
	
	if(dd < 10) {
	  dd = '0' + dd
	}
	 
	if(mm < 10){
	  mm = '0' + mm
	}
	
	max_date = yyyy + "-" + mm + "-" + dd;
	
	$('#publicationDate').attr("max", max_date);
})

const min_shown = 4;
var from = 0;
var to = 4;
var page = 1
var results_per_page = 4;

$(document).on('click', '#prev_page, #next_page', function(event) {
	var ev = event.target.id;
	var max_loaded = $('#btn-div').data("total");
	
	for(var i = from; i < to; i++) {
		$('#result-' + i).hide();
	}
	
	//console.log("from = " + from + " to = " + to); 
	
	if(ev == "next_page") {
		if(to < max_loaded) {
			from = to;
			to += results_per_page;
			
			if(to > max_loaded) {
				to = max_loaded;
			}
			
			page += 1;
		}
	} else if(ev == "prev_page") {
		if(from >= min_shown) {
			to = from;
			from -= results_per_page;
			page -= 1;
		}
	}
	
	//console.log("from = " + from + " to = " + to); 
	
	for(var i = from; i < to; i++) {
		$('#result-' + i).show();
	}
	
	if(from == 0) {
		$("#prev_page").prop('disabled', true);
	} else {
		$("#prev_page").prop('disabled', false);
	}
	
	if(to == max_loaded) {
		$("#next_page").prop('disabled', true);
	} else {
		$("#next_page").prop('disabled', false);
	}
	
	$('#curr_page').text(page);
})

// Control tab behaviour for AGGREGATOR datasets
$('#eu_datasets a').on('click', function (e) {
	e.preventDefault();
	$(this).tab('show');
});

var role = null;
$(document).on('click', '#policymaker-role-btn', function() {
	role = 'PM';

	$.ajax({
		type: "GET",
		url: "/getPolicymakerButtonFragment",
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});
});

$(document).on('click', '#researcher-role-btn', function() {
	role = 'RS';

	$.ajax({
		type: "GET",
		url: "/getResearcherButtonFragment",
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});
});

$(document).on('click', '#citizen-role-btn', function() {
	role = 'CT';

	$.ajax({
		type: "GET",
		url: "/getCitizenButtonFragment",
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});
});

$(document).on('click', '#foresight-btn', function() {
	
	$.ajax({
		type: "GET",
		url: "/getCaseStudiesFragment",
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});
	
})

$(document).on('click', '#aggregator-btn', function() {
	
	$.ajax({
		type: "GET",
		url: "/getRepositoryFragment",
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});
	
})

$(document).on('click', '#search-engine-btn', function() {
	
	$.ajax({
		type: "GET",
		url: "/getDocumentSearchFragment",
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});
	
})

$(document).on('click', '.deep-dives-btn', function() {

	$.ajax({
		type: "GET",
		url: "/getDeepDivesDocs",
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});

})

$(document).on('click', '.tech-doc-btn', function() {

	$.ajax({
		type: "GET",
		url: "/getTechnologyDocs",
		success: function(fragment){
			$('#visible-content').replaceWith(fragment);
		}
	});

})