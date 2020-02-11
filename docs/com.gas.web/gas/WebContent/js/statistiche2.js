$(() => {
	
	$('#carburanti').change(() => {
		
        $.ajax({
            url: '/gas/OttieniStat',
            method: 'get',
            data: {
                carburanti :$('#carburanti').val()
                  }

        })
	.done((stat) => {
		var wow= [];
		var wowow=[];
		var ahahah=[];
		var ctx = $('#myChart');
		var myChart = new Chart(ctx, {
			type: 'line',
			data: {
				labels: wowow,
				datasets: [{
					label: 'Prezzo',
					data: wow,
					backgroundColor: "rgba(4,23,1,0.2)",
					borderColor: "rgba(4,23,1,1)",
					borderWidth: 3
				}]
			},
			options: {
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero: true
						}
					}]
				}
			}
		})
			console.log(stat);		
		    myChart.data.labels.pop();
		    for (var i = 0; i < myChart.data.length; i++) {
		        dataset.data.pop();
		    }
			console.log(stat);
			
			for (var i = 0; i < stat.length; i++) {
				var stat1 = (stat[i]);
				wow[i]=((stat1.prezzo).toFixed(3));
				var dt=new Date(parseInt(stat1.dataComunicazione));				
				var giorno = dt.getDate();
				console.log(giorno);
                var month = dt.getMonth() + 1;
				var dataCompleta=
					(giorno	< 10 ? "0" + giorno : giorno )+ "/" + (month.toString().length > 1 ? month : "0" + month) + "/" +
                dt.getFullYear();
				console.log(dataCompleta);
				wowow[i]=dataCompleta;					
				ahahah.push({labels:wowow[i],data:wowow[i]});
			}

			myChart.update();		
		}).fail(() => {alert("Non ci siamo")})
	})
	
});