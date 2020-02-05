$(() =>{
    var dataPoints=[];

var options =  {
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
		console.log('ciao');
		if(stat){

            for (var i = 0; i < stat.length; i++) {
            	console.log(stat);
            	console.log(stat[i].dataComunicazione);
            	console.log(stat[i].prezzo);

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