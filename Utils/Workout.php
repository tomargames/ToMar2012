<?php
date_default_timezone_set("America/New_York");
//Array ( [tm_sec] => 34 [tm_min] => 37 [tm_hour] => 23 [tm_mday] => 1 [tm_mon] => 11 [tm_year] => 113 [tm_wday] => 0 [tm_yday] => 334 [tm_isdst] => 0 )
print_r(localtime());
echo "<br><br>";
print_r(localtime(time(),true));
?>