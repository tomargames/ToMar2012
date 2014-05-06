<?php

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
?>