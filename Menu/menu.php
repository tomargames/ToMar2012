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
<table width=100% cellpadding="0" cellspacing="0">><tr>
<td width=20% class="outer"></td>
<td width=60%>
	<table width=100% cellpadding=0 cellspacing=0>
	<tr>
		<td class="fineprint" width="20%"><?php echo $d; ?></td>
		<td class="banner" width="60%">ToMarGames</td>
		<td class="fineprint" width="20%"><?php echo $nm ?></td>
	</tr>
	<tr>
		<td valign="top" width="20%">
			<table cellpadding="1" cellspacing="1">
				<tr><td class=menubutton><a class=menubutton href="javascript:menuFilter('J');">Just Games</a></td></tr>
				<tr><td class=menubutton><a class=menubutton href="javascript:menuFilter('W');">Word Games</a></td></tr>
				<tr><td class=menubutton><a class=menubutton href="javascript:menuFilter('L');">Logic/Math Games</a></td></tr>
				<tr><td class=menubutton><a class=menubutton href="javascript:menuFilter('F');">Fast Games</a></td></tr>
				<tr><td class=menubutton><a class=menubutton href="javascript:menuFilter('S');">Slow Games</a></td></tr>
				<tr><td class=menubutton><a class=menubutton href="javascript:menuFilter('P');">ToMar Pentathlon</a></td></tr>
				<tr><td class=menubutton><a class=menubutton href="javascript:menuFilter('A');">About ToMarGames</a></td></tr>
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
		<td class="fineprint" width="20%"></td>
		<td class="fineprint" width="60%">ToMarGames build brain cells!<br>Copyright &copy; 2013 ToMarGames<br>No rights reserved; feel free to use as you like.<br><?php echo $_SERVER['REMOTE_ADDR'] ?></td>
		<td class="fineprint" width="20%"></td>
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
	document.process.action = '<?php echo $site; ?>' + "Menu/menu.php";
	document.process.submit();
}			
</script> 
</body>
</html>