
$(() => {
	function formatDate(value)
	{
	   return value.getDate()+1 + "/" + value.getMonth() + "/" + value.getYear();
	}
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
           
            borderWidth: 1
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
		    //myChart.update();
			

			console.log(stat);
			
			for (var i = 0; i < stat.length; i++) {
				var stat1 = (stat[i]);
				wow[i]=(stat1.prezzo);
				wowow[i]=new Date(parseInt(stat1.dataComunicazione));
				formatDate(wowow[i]);
				ahahah.push({labels:wowow[i],data:wowow[i]});
			}

			myChart.update();		
	})
})