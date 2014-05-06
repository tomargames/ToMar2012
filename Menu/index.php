<?php
require 'Player.php';
require 'Game.php';
global $id;
global $site;
global $em;
global $nm;
global $ctg;

date_default_timezone_set("America/New_York");
if (strpos("".getcwd(),"\\") > -1)
{
		$id = 'AItOawmTT-FoSMaRkIwnMJRp101plSb0SGCUfiM';
		$site = "http://localhost/ToMar2012/";
		$em = 'tomargames@gmail.com';
		$nm = 'marie baldys';
}
else
{		
		$id = $_GET["id"];
		$site = "http://www.tomargames.com/ToMar2012/";
		$nm = $_GET["nm"];
		$em = $_GET["em"];
}
if ($id == null)
{
	echo "<script> window.location = 'login.php?login'; </script>";
}
if(isset($_GET["ctg"]))
{
	$ctg = $_GET["ctg"];
}
else
{
	 $ctg = "J";
}	 
$d = substr(date("r"), 0, strpos(date("r"),"-")); 
if ($nm < "A" || $nm == "null")
{
	$end = strpos($em,"@"); 
	$nm = substr($em,0,$end); 
}	
$games = GameFromXML();
?>
<!DOCTYPE HTML>
<html lang="en">   
<meta charset="utf-8">   
<head>	
	<title>ToMarGames</title>
	<LINK REL="StyleSheet" HREF="menu.css?<?php echo rand(); ?>"  TYPE="text/css" TITLE="ToMar Style" MEDIA="screen">
</head>	
<body>
<table width=100% cellpadding="0" cellspacing="0"><tr>
<td width=20% class="outer"></td>
<td width=60%>
	<table width=100% cellpadding=0 cellspacing=0>
	<tr>
		<td class="fineprint10" width="20%"><?php echo $d; ?></td>
		<td class="banner" width="60%">ToMarGames</td>
		<td class="fineprint10" width="20%"><?php echo $nm ?></td>
	</tr>
	<tr>
		<td valign="top" width="20%">
			<table cellpadding="1" cellspacing="1">
<?php
		$codes = array("J", "W", "L", "F", "S");
		$descs = array("Just Games", "Word Games", "Logic/Math Games", "Fast Games", "Slow Games");
		for ($i = 0; $i < 5; $i++)
		{
			if ($codes[$i] == $ctg)
			{
				$class = "menuselected";
				$rest = ">".$descs[$i];
			}
			else
			{
				$class = "menubutton";
				$rest = "><a class=".$class." href=javascript:menuFilter('".$codes[$i]."');>".$descs[$i]."</a>";
			}		
			echo "<tr><td class=".$class.$rest."</td></tr>";
		}
?>			
				<tr><td class=menubutton><a class=menubutton href="../PNT">ToMar Pentathlon</a></td></tr>
				<tr><td class=menubutton><a class=menubutton href="about.html");">About ToMarGames</a></td></tr>
				</tr>
			</table>	
		</td>
		<td span=2>
			<table>
<?php
	foreach ($games as $g)
	{
			if (strpos($g->getType(), $ctg) > -1)
			{
				$lk = "javascript:playGame('".$g->getID()."');";
				$dd = "<a href=javascript:playGame('".$g->getID()."');>".$g->getDesc()."</a>";
				echo "<tr><td><a href=".$lk."><img height=100 width=100 src=".$site.$g->getID()."/".$g->getID().".jpg></a>";
				echo "</td><td class=blue10><a href=".$lk." class=green12L>".$g->getName()."</a><br>".$dd."</td></tr><tr><td colspan=3><hr></td></tr>";
			}		
	}		
?>
		</table>
		</td>
	</tr>

	<tr>
		<td class="fineprint8" width="20%"></td>
		<td class="fineprint8" width="60%">ToMarGames build brain cells!<br>Copyright &copy; 2013 ToMarGames<br>No rights reserved; feel free to use as you like.<br><?php echo $_SERVER['REMOTE_ADDR'] ?></td>
		<td class="fineprint8" width="20%"><a class="coinbase-button" data-code="00732638e22fdc666879c182753a7910" data-button-style="donation_small" href="https://coinbase.com/checkouts/00732638e22fdc666879c182753a7910">Donate Bitcoins</a><script src="https://coinbase.com/assets/button.js" type="text/javascript"></script></td>
	</tr>
	</table>
	<form method="get" name="process">
	<input type="hidden" name="id" value='<?php echo $id ?>'>	
	<input type="hidden" name="nm" value='<?php echo $nm ?>'>
	<input type="hidden" name="score" value="0">
	<input type="hidden" name="ctg" value='<?php echo $ctg ?>'>	
	</form>	
	
</td>				
<td width=20% class="outer"></td></tr></table>			
<script>  
function playGame(game)
{
	document.process.action = '<?php echo $site; ?>' + game + '/';
	document.process.submit();
}
function menuFilter(c)
{
	document.process.ctg.value = c;
	document.process.action = '<?php echo $site; ?>' + "Menu/";
	document.process.submit();
}			
</script> 
</body>
</html>