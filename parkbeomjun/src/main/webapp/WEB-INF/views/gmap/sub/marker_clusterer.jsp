<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>GMaps.js &mdash; Marker Clusterer</title>
  <script type="text/javascript" src="http://google-maps-utility-library-v3.googlecode.com/svn/tags/markerclusterer/1.0/src/markerclusterer.js"></script>
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js"></script>
  <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
  <script type="text/javascript" src="${rootPath}/resources/js/gmaps/gmaps.js"></script>
  <link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.3.0/bootstrap.min.css" />
  <link rel="stylesheet" type="text/css" href="${rootPath}/resources/css/gmaps/examples.css" />
  <script type="text/javascript">
    var map;
    $(document).ready(function(){
      map = new GMaps({
        div: '#map',
        lat: 37.5321140,
        lng: 126.8465070,
        markerClusterer: function(map) {
          return new MarkerClusterer(map);
        }
      });

      var lat_span = 37.538884015462803 - 37.532552468585555;
      var lng_span = 126.84634203093454 - 126.84634143461212;

      for(var i = 0; i < 100; i++) {
        var latitude = Math.random()*(lat_span) + 37.53211465388489;
        var longitude = Math.random()*(lng_span) + 126.84623578578643;

        map.addMarker({
          lat: latitude,
          lng: longitude,
          title: 'Marker #' + i
        });
      }
    });
  </script>
</head>
<body>
  <h1>GMaps.js &mdash; Marker Clusterer</h1>
  <div class="row">
    <div class="span11">
      <div id="map" style="width:100%;height:100%"></div>
    </div>
    <div class="span6">
      <p>With GMaps.js you can use a marker clusterer to group large amount of markers:</p>
      <pre>map = new GMaps({
  div: '#map',
  lat: -12.043333,
  lng: -77.028333,
  markerClusterer: function(map) {
    return new MarkerClusterer(map);
  }
});</pre>
      <p>You can use MarkerClusterer or MarkerClustererPlus. If you want to use a custom marker clustering library, you have to define a <code>addMarker</code> method.</p>
      <p><span class="label notice">Note:</span> Read more about MarkerClusterer and MarkerClustererPlus <a href="http://code.google.com/p/google-maps-utility-library-v3/wiki/Libraries">here</a>.</p>
    </div>
  </div>
</body>
</html>
