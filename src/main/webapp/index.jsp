<html>
<body>
    <body bgcolor="#82CAFA"/>
    <p>SiteURL/webapi/*</p>
    <p>GET::/weather  (return all weather data)</p>
    <p>GET::/weather?lat={latitude}&lon={longitude}.   (return filtered weather data)</p>
    <p>/weather/temperature?start={startDate}&end={endDate} (Returning the lowest and highest temperature for all the cities in the given date range)</p>
    <p>POST:: /weather (Adding new weather data, consuming JSON weather data)</p>
    <p>DELETE:: /erase (Erasing all the weather data) </p>
    <p>DELETE:: /erase?start={startDate}&end={endDate}&lat= {latitude}&lon={longitude} (Erasing all the weather data by the date range inclusive and the location coordinates)</p>
</body>
</html>
