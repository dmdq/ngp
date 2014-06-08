function reLoadChildren(id, context) {
	$('#' + id, context).empty();
	retrieveChildrenByAjax(id, context);
}

function retrieveChildrenByAjax(id, context) {
	if(context == null) context = document;
	
	$.ajax({
	    url:'<c:url value="/listCaseCategory.do"></c:url>',
	    type: 'GET',
	    dataType: 'html',
	    data: 'id=' + id,
	    error: function(html){
	        alert('Error loading XML document'+html);
	    },
	    success: function(html){
	    	
			if(id == null || id == '') {
				id = 'browser';
			}
		    
		    var target = "#" + id;
	    	var branches = $(target, context).append(html);
			$(target, context).treeview({
				add: branches
			});

			$(target + ">li", context).toggle(function(event) {
				if($(this).children('ul:first').html() == '') {
					var id = $(this).children('ul:first').attr("id");
					alert(id);
					retrieveChildrenByAjax(id, context);
					cancelEventBubble(event, context);
				}
				showInfo(id, context);
			},
			function(event) {
				var id = $(this).children('ul:first').attr("id");
				alert(id);
				cancelEventBubble(event);
				showInfo(id, context);
			});
	    }
	});
}

function cancelEventBubble(event) {
	if(document.all) 
		window.event.cancelBubble = true;
	else
		event.stopPropagation();
}

function showInfo(id, context) {
	if($('#id', context).attr('value') != id) {
		
		$('#treeForm', context).submit(function() {
			$('#id', context).attr('value', id);
			$(this).attr('target', 'info');
			$(this).attr('action', '<c:url value="/showCaseCategoryInfo.do"></c:url>');
		});

		$('#treeForm', context).submit();
	}
}