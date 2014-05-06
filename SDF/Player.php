<?php 
class Player { 
 private $ID = null;
 public function setID($x) { $this->ID = $x; }
 public function getID() { return $this->ID; }
 private $Name = null;
 public function setName($x) { $this->Name = $x; }
 public function getName() { return $this->Name; }
 private $Level = null;
 public function setLevel($x) { $this->Level = $x; }
 public function getLevel() { return $this->Level; }
 private $Stars = null;
 public function setStars($x) { $this->Stars = $x; }
 public function getStars() { return $this->Stars; }
 private $Games = null;
 public function setGames($x) { $this->Games = $x; }
 public function getGames() { return $this->Games; }
} 
function PlayerFromXML() 	{ 
 $returnArray = null; 
 $table = simplexml_load_file('Player.xml');
	foreach($table->children() as $record)	{ 
 $x = new Player();
 foreach($record->children() as $attr) { 
  if ($attr->getName() == 'ID') { 
 	$x->setID(trim($attr)); }  
  if ($attr->getName() == 'Name') { 
 	$x->setName(trim($attr)); }  
  if ($attr->getName() == 'Level') { 
 	$x->setLevel(trim($attr)); }  
  if ($attr->getName() == 'Stars') { 
 	$x->setStars(trim($attr)); }  
  if ($attr->getName() == 'Games') { 
 	$x->setGames(trim($attr)); }  
 } $returnArray[$x->getID()] = $x; } 
 $newArray = null; 
 usort($returnArray, 'sortArray'); 
 foreach($returnArray as $r)  {  	$newArray[$r->getID()] = $r;  } return $newArray; } 
 function writePlayerXML($arr)	{
$xmlString = "<?xml version='1.0' encoding='ISO-8859-1'?><root>";
 foreach ($arr as $a) { $t1 = '<Player>';
 $t1 = $t1.'<ID>'.$a->getID().'</ID>'; 
 $t1 = $t1.'<Name>'.$a->getName().'</Name>'; 
 $t1 = $t1.'<Level>'.$a->getLevel().'</Level>'; 
 $t1 = $t1.'<Stars>'.$a->getStars().'</Stars>'; 
 $t1 = $t1.'<Games>'.$a->getGames().'</Games>'; 
 $t1 = $t1.'</Player>'; $xmlString = $xmlString.$t1;} 
	$xmlString = $xmlString.'</root>'; 
	$xml2 = str_ireplace('\\','',$xmlString); 
	$file=fopen('Player.xml','w'); 
	fwrite($file,$xml2); 
	fclose($file); 	} 
function sortArray($f1, $f2) {
 $a = trim($f1->getLevel());
 $b = trim($f2->getLevel());
 return $a < $b ? 1 : -1; }
?> 
