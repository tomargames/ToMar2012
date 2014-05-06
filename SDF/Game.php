<?php 
class Game { 
 private $Score = null;
 public function setScore($x) { $this->Score = $x; }
 public function getScore() { return $this->Score; }
 private $Count = null;
 public function setCount($x) { $this->Count = $x; }
 public function getCount() { return $this->Count; }
} 
function GameFromXML() 	{ 
 $returnArray = null; 
 $table = simplexml_load_file('Game.xml');
	foreach($table->children() as $record)	{ 
 $x = new Game();
 foreach($record->children() as $attr) { 
  if ($attr->getName() == 'Score') { 
 	$x->setScore(trim($attr)); }  
  if ($attr->getName() == 'Count') { 
 	$x->setCount(trim($attr)); }  
 } $returnArray[$x->getScore()] = $x; } 
 return $returnArray; } 
function writeGameXML($arr)	{
$xmlString = "<?xml version='1.0' encoding='ISO-8859-1'?><root>";
 foreach ($arr as $a) { $t1 = '<Game>';
 $t1 = $t1.'<Score>'.$a->getScore().'</Score>'; 
 $t1 = $t1.'<Count>'.$a->getCount().'</Count>'; 
 $t1 = $t1.'</Game>'; $xmlString = $xmlString.$t1;	} 
	$xmlString = $xmlString.'</root>'; 
	$xml2 = str_ireplace('\\','',$xmlString); 
	$file=fopen('Game.xml','w'); 
	fwrite($file,$xml2); 
	fclose($file); 	}
function MeanScore($s)
{
	$total = 0;
	$count = 0;
	foreach ($s as $item)
	{
		for ($i = 0; $i < $item->getCount(); $i++)
		{
//			echo $item->getScore()."\n";
			$total += $item->getScore();
			$count += 1;
		}	
	}
//	echo "total is ".$total.", count is ".$count."\n";
	return round($total / $count);		
}
function HighestScore($s)
{
	$h = 0;	
	foreach ($s as $item)
	{
		$h = ($item->getScore() > $h) ? $item->getScore() : $h;
	}
	return $h;
}			
function StandardDeviationFromMean($s, $m)
{
	$total = 0;
	$count = 0;
	foreach ($s as $item)
	{
		for ($i = 0; $i < $item->getCount(); $i++)
		{
			$total += pow($item->getScore() - $m, 2);
			$count += 1;
		}	
	}
//	echo "total is ".$total.", count is ".$count."\n";
	return sqrt($total / $count);		
}
function CalculateAward($sc, $sd, $m)
{
	$min = $m - $sd;
	$m1 = $m + $sd;
	$m2 = $m1 + $sd;
	if ($sc < $min)
	{
		return -1;
	}	
	if ($sc < $m)
	{
		return 0;
	}	
	if ($sc < $m1)
	{
		return 1;
	}	
	if ($sc < $m2)
	{
		return 2;
	}
	return 3;
}
//$scores = GameFromXML();
//$m = MeanScore($scores);
//$std = StandardDeviationFromMean($scores, $m);		
?> 
