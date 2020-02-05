mapboxgl.accessToken = 'pk.eyJ1IjoiZGllZ29jaXBhIiwiYSI6ImNrNXozOGRrOTA1eGczZ3FqazR2amthb2UifQ.Kyzu-TDi7MYZPhBe4s_WqA';
var map = new mapboxgl.Map({
    // container id
    container: 'map',
    // stylesheet location
    style: 'mapbox://styles/diegocipa/ck67nibv60jrl1iquqggrb3gu',
    center: [10.818282, 44.500979], // starting position [lng, lat]
    zoom: 6 // starting zoom
});
map.addControl(new mapboxgl.NavigationControl());

map.on('load', function () {
    $.ajax({
        url: '/gas/cercaDistributori',
        method: 'get'
    }).done((risposta) => {
        //metto i distributori dentro un array
        let distributori = [];
        risposta.forEach(d => {
            distributori.push(JSON.parse('{"type": "Feature", "properties": {"Bandiera": "Bandiera - '+d.bandiera+'", "Provincia": "<br>Provincia - '+d.provincia+'", "Comune": "<br>Comune - '+d.comune+'", "icon": "rocket"}, "geometry": { "type": "Point", "coordinates": [ '+d.longitudine+','+d.latitudine+' ]}}'));
        });
        map.addSource('places', {
            'type': 'geojson',
            'data': {
                'type': 'FeatureCollection',
                'features': distributori
            }

        });

        //add layer showing places
        map.addLayer({
            'id': 'places',
            'type': 'symbol',
            'source': 'places',
            'layout': {
                'icon-image': '{icon}-15',
                'icon-allow-overlap': true
            }
        });
        //when click event occurs, open popup at location
        map.on('click', 'places', function (e) {
            var coordinates = e.features[0].geometry.coordinates.slice();
            var description = e.features[0].properties.Bandiera + e.features[0].properties.Comune + e.features[0].properties.Provincia;
            while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
                coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
            }

            new mapboxgl.Popup()
                .setLngLat(coordinates)
                .setHTML(description)
                .addTo(map);
        });
        // Change the cursor to a pointer when the mouse is over the places layer.
        map.on('mouseenter', 'places', function () {
            map.getCanvas().style.cursor = 'pointer';
        });

        // Change it back to a pointer when it leaves.
        map.on('mouseleave', 'places', function () {
            map.getCanvas().style.cursor = '';
        });
    }).fail(() => {
        alert('OOPS, Distributori non caricati!');

    })
})

