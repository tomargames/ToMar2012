<?php
function tmMode()
{
	// this function will populate globals id, em, nm, and site
	global $id;
	global $site;
	global $em;
	global $nm;
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
}	
function formatNumber($number, $digits)
{
	$temp = 	"000000000" . trim($number);
	return substr($temp, strlen($temp) - $digits);
}

function randomPicks($univ, $numPicks)
{
	$selectors = "";
	$strLen = 5;
	for ($i = 0; $i < $univ; $i++)
	{
		$selectors = $selectors.formatNumber($i, $strLen);		// will do universes up to 99999
	}	
	for ($i = 0; $i < $numPicks; $i++)
	{
		$rPick = mt_rand(0, $univ);
		$rArray[$i] = substr($selectors,  $rPick * $strLen, $strLen);
		$selectors = substr($selectors, 0, $rPick * $strLen).substr($selectors, ($rPick + 1) * $strLen );
		$univ = $univ - 1;
	}
	return $rArray;
}
function debug($str)
{
	echo $str."<br>";
}		
function randomSubset($arr, $numPicks)
{
	$selectors = "";
	$rArray = null;
	$univ = count($arr);
	$strLen = ($numPicks < 100) ? 2 : (($numPicks < 1000) ? 3 : (($numPicks < 10000) ? 4 : 5));
	for ($i = 0; $i < $univ; $i++)
	{
		$selectors = $selectors.formatNumber($i, $strLen);		// will do universes up to 99999
	}
	for ($i = 0; $i < $numPicks; $i++)
	{
		$rPick = mt_rand(0, $univ-1);
		$idx = abs(substr($selectors,  $rPick * $strLen, $strLen));
		$rArray[$i] = $arr[$idx];
		$selectors = substr($selectors, 0, $rPick * $strLen).substr($selectors, ($rPick + 1) * $strLen );
		$univ = $univ - 1;
	}
	return $rArray;
}

?>