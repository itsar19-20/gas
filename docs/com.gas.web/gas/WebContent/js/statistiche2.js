
$(() => {
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
});

$.ajax({
	url: '/gas/OttieniStat',
	method: 'get',
	dataType :'json'	
})
	.done((stat) => {
		console.log(stat);
		
		    myChart.data.labels.pop();
		    for (var i = 0; i < myChart.data.length; i++) {
		        dataset.data.pop();
		    }
		    // myChart.update();
			

			console.log(stat);
			
			for (var i = 0; i < stat.length; i++) {
				var stat1 = (stat[i]);
				wow[i]=(stat1.prezzo);
				var dt=new Date(parseInt(stat1.dataComunicazione));
				
				var giorno = dt.getDate();
				console.log(giorno);
// var mese=dtc.getMonth()+1;
// var anno=dtc.getFullYear();
				
                var month = dt.getMonth() + 1;
				var dataCompleta=
					(giorno	< 10 ? "0" + giorno : giorno )+ "/" + (month.toString().length > 1 ? month : "0" + month) + "/" +
                dt.getFullYear();
				console.log(dataCompleta);
				wowow[i]=dataCompleta;					
				ahahah.push({labels:wowow[i],data:wowow[i]});
			}

			myChart.update();		
	})
})