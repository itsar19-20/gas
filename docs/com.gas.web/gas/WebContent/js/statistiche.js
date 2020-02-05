$(() => {
	var dataPoints = [];

	var options = {
		animationEnabled: true,
		theme: "light1",
		title: {
			text: "Andamento prezzi",
			fontFamily: "tahoma"
		},
		axisX: {
			valueFormatString: "YYYY MM DD",
		},
		axisY: {
			title: "Prezzi",
			titleFontSize: 24,
			includeZero: false
		},
		data: [{
			type: "spline",
			yValueFormatString: "$#,###.##",
			dataPoints: dataPoints
		}]
	};

	$.ajax({
		url: '/gas/OttieniStat',
		method: 'get'
	})
		.done((stat) => {
			if (stat) {

				for (var i = 0; i < stat.length; i++) {
					dataPoints.push({
						x: new Date(stat[i].dataComunicazione),

						y: stat[i].prezzo
					});
				}


			}

			$("#chartContainer").CanvasJSChart(options);
			$("#chartContainer").render;
		})
})