var max = 0;
var steps = 10;
var chartData = {};

function respondCanvas() {
    var c = $('#summary');
    var ctx = c.get(0).getContext("2d");
    var container = c.parent();

    var $container = $(container);

    c.attr('width', $container.width()); //max width

    c.attr('height', $container.height()); //max height                   

    //Call a function to redraw other content (texts, images etc)
    var myChart = new Chart(ctx).Line(chartData, {
        scaleOverride: true,
        scaleSteps: steps,
        scaleStepWidth: Math.ceil(max / steps),
        scaleStartValue: 0
    });
}

var GetChartData = function () {
    $.ajax({
        url: serviceUri,
        method: 'GET',
        dataType: 'json',
        success: function (d) {
           chartData = {
                labels: d.AxisLabels,
                datasets: [
                    {
                        fillColor: "rgba(220,220,220,0.5)",
                        strokeColor: "rgba(220,220,220,1)",
                        pointColor: "rgba(220,220,220,1)",
                        pointStrokeColor: "#fff",
                        data: d.DataSets[0]
                    }
                ]
            };

            max = Math.max.apply(Math, d.DataSets[0]);
            steps = 10;

            respondCanvas();
        }
    });
};

$(document).ready(function() {
    $(window).resize(respondCanvas);

    GetChartData();
});