mapboxgl.accessToken = 'pk.eyJ1IjoiZGllZ29jaXBhIiwiYSI6ImNrNXozaW5jZjFiNXAza3J2Y2s0anBzcDQifQ.mQENEGGn8KgFefzm76BuAQ';
var map = new mapboxgl.Map({
// container id
container: 'map', 
// stylesheet location
style: 'mapbox://styles/mapbox/streets-v11', 
center: [10.818282, 44.500979], // starting position [lng, lat]
zoom: 6 // starting zoom
});