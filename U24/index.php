<?php
require '../Scoring/Player.php';
require '../Scoring/Game.php';
require '../Scoring/tmUtils.php';
global $id;
global $site;
global $em;
global $nm;

tmMode();
if ($id == null)
{
	echo "<script> window.location = '".$site."U24/login.php?login'; </script>";
}
// Step2: read player file and identify player
$players = PlayerFromXML();
$player = thisPlayer($id, $nm, $players);
// Step3: read game file and build stats coming in
global $mean;
global $highest;
global $std;
$games = GameFromXML();
$stats = "Highest: ".$highest."<br>Average: ".$mean."<br>StdDev: ".$std."<br>";
// Step4: if there's a score coming in, process it
$sc = $_GET["score"];
$message = "NONE";
if (is_finite($sc) && $sc > 0)
{
	$tsp = $_GET["tsp"];
	$award = CalculateAward($sc, $tsp.$id, $games);
	global $blurb;
	$stats = "Your Score: ".$sc."<br>Award: ".$award."<br>".$stats;
	$temp = $player->processAward($award,$tsp);
	$message = $sc." points. ".$blurb[$award + 1].$temp;
	$players[trim($id)] = $player;
	writePlayerXML($players);
}		 
$puzzleUniverse = loadPuzzles();
$puzzleSet = createPuzzleSet(40, $puzzleUniverse);
?>
<!doctype html>
<html>
<head>
	<title>Ultimate 24</title>
	<LINK REL="StyleSheet" HREF="../styles.css?<?php echo rand(); ?>"  TYPE="text/css" TITLE="ToMar Style" MEDIA="screen">
</head>
<body>
<table border=0 align="center"><tr>	
	<td width="10%"><img src="U24.jpg" height="100" width="100"></td>
	<td  width="80%" class="biggest">Ultimate 24<br><span class="magenta10">by ToMarGames</span><br></td>
	<td width="10%"><img src="U24.jpg" height="100" width="100"></td></tr>
</table>
<table border="0" width=100% align="left"><tr>
<td width="30%" align="left">
<table border=2>
<?php 
	$pr = "";
	for ($i = 0; $i < $player->getStars(); $i++)
	{
		$pr = $pr."* ";
	}		
	echo "<tr><td class=green12>Player</td><td class=big>".$nm."</td></tr>";		
	echo "<tr><td class=green12>Rank</td><td class=big>".$player->getLevel()."</td></tr>";		
	echo "<tr><td class=green12>Progress</td><td class=big>".$pr."</td></tr>";
	echo "<tr><td class=magenta8 colspan=2>".$stats."</td></tr>";
?>
</table>
<br><br>
<form action='<?php echo $site; ?>' method='get'>			
<input type="hidden" name='id' value='<?php echo $id; ?>'>
<input type="hidden" name='nm' value='<?php echo $nm; ?>'>
<input type="submit" value="ToMarGames Menu"><br><br>	
</form>
<table border=1>
	<tr><td colspan="3" class="magentah">Players</td></tr>
	<tr><td class="greenh">Name</td><td class="greenh">Rank</td><td class="greenh">Games</td></tr>
<?php
	foreach ($players as $p)
	{
		echo "<tr><td class='green10'>".$p->getName()."</td><td class='green10num'>".$p->getLevel()."</td><td class='green10num'>".$p->getGames()."</td></tr>";
	}	
?>	
</table>	
</td>
<td>		
<div id="app"> 
<applet name="applet" code="U24.class" width="500" height="500">
<?php 
	echo "<param name='id' value='".$id."'>";
	echo "<param name='nm' value='".$nm."'>";
	echo "<param name='site' value='".$site."'>";
	echo $puzzleSet; 
	if ("NONE" != $message)
	{
		echo "<param name='message' value='".$message."'>";
	}	
?>
</applet>
</div>
</td></tr></table>
<br><br><br>About the Game
	<ul class=green10>
		<li>Solve puzzles by using the buttons and/or entering text to create an equation that equals 24.</li>
		<li>For example: 4 * 3 * 2 * 1.</li>
		<li>For the card in the banner at the top, one solution is: (4 + 1 + 1) * 4.</li>
		<li>You must use all 4 numbers in the equation, and it must equal 24.</li>
		<li>You have 3 minutes to solve as many puzzles as you can.</li>
		<li>Scoring for each puzzle you solve will be based on how many seconds remain on the clock.</li>
		<li>The number of seconds will be multiplied according to the multiplier, and added to your score.</li>
		<li>The multiplier will increase every time you solve a puzzle.</li>
		<li>If you pass a puzzle, the multiplier will be set back to 1.</li>
		<li>Rather than a high score list, ToMarGames uses a ranking system based on all scores for this game.</li>
		<li>You move up through the ranks by earning stars.</li>
		<li>Each game, your score will be measured against the mean score of all games played so far.</li>
		<li>To earn a star, you must score higher than the mean.</li>
		<li>If you score more than the mean plus standard deviation, you can earn multiple stars.</li>
		<li>If you score less than the mean minus standard deviation, you will lose a star.</li>
		<li>When you reach 5 stars, you will advance to the next rank, and come into the rank with two stars.</li>
		<li>If you have no stars, and you lose a star, you will descend to the rank below, and come in with two stars.</li>
	</ul>

</body>
</html>
<?php 
function loadPuzzles()
{
	$puzzleTable = null;
	$counter = 0;
	$file=fopen("u24.txt","r") or exit("Unable to open file!");
	while(!feof($file))
  	{
  		$puzzleTable[formatNumber($counter++,5)] = fgets($file);
  	}
	fclose($file);
	return $puzzleTable;
}
function createPuzzleSet($numberOfPuzzles, $univ)
{
	$puzzleNumbers = 	randomPicks(count($univ), $numberOfPuzzles);
	$puzzleString = "";
	$counter = 0;
	foreach($puzzleNumbers as $pn)
	{
		$p = explode(",", $univ[$pn]);
		$ps = "";
		foreach ($p as $a)
		{
			$ps = $ps.formatNumber($a, 2);
		}	
		$puzzleString = $puzzleString."<param name='P".$counter++."' VALUE='".$ps."'>";
	}
	return $puzzleString;	
}	
?>