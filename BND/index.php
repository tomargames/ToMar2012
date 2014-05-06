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
		echo "<script> window.location = '".$site."BND/login.php?login'; </script>";
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
?>
<!doctype html>
<html>
<head>
	<title>BoonDoggle!</title>
	<LINK REL="StyleSheet" HREF="../styles.css?<?php echo rand(); ?>"  TYPE="text/css" TITLE="ToMar Style" MEDIA="screen">
	<script>
		addWord = window.open('','dummy','width=100,height=100');
		addWord.blur();
	</script>	
</head>
<body>
<table border=0 align="center"><tr>	
	<td width="10%"><img src="BND.jpg" height="100" width="100"></td>
	<td  width="80%" class="biggest">BoonDoggle<br><span class="magenta10">by ToMarGames</span><br></td>
	<td width="10%"><img src="BND.jpg" height="100" width="100"></td></tr>
</table>
<table border="0" align="center"><tr>
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
<td width="1">&nbsp; </td>		
<td>		
<div id="app"> 
<?php 
	$w = 800;
	$h = 600;
	echo "<applet name='applet' code='BND.class' width='".$w."' height='".$h."'>";
	echo "<param name='id' value='".$id."'>";
	echo "<param name='nm' value='".$nm."'>";
	echo "<param name='site' value='".$site."'>";
	echo "<param name='WIDTH value='".$w."'>";
	echo "<param name='HEIGHT value='".$h."'>";
	echo "<param name='SQUARE' value='11'>";
	echo "<param name='MARGIN' value='0'>";
	echo "<param name='SIDE' value='6'>";
	echo "<param name='TIME' value='2000'>";
	if ("NONE" != $message)
	{
		echo "<param name='message' value='".$message."'>";
	}	
?>
</applet>
</div>
</td></tr></table>
About the Game
	<ul class=green10>
		<li>BoonDoggle plays best using the <a href='https://www.google.com/chrome'>Chrome</a> browser.</li>
		<li>Click on letters in order to form words.</li>
		<li>Words are scored by the number of letters in them; longer words get more points.</li>
		<li>Purple letters each multiply the score by 3, so 3 purple letters multiplies it by 27.</li>
		<li>When the grid fills up completely, the game is over.</li>
		<li>At the beginning, there is a 2-second delay between letters, that gradually speeds up.</li>
		<li>If you submit a word that isn't in our dictionary, it generates a log entry that we review &dash; that's how we add words.</li>
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

