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
	echo "<script> window.location = '".$site."SHG/login.php?login'; </script>";
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
	//Shanghai awards are different, but the above processing needs to be done, so now:
	if ($sc == 72)
	{
		$award = 3;
	}
	else 
	{
		if ($sc < 120)
		{ 
			$award = 2;
		}	
		else
		{
			$award = 1;
		}	
	}	
	global $blurb;
	$stats = "Your Score: ".$sc."<br>Award: ".$award."<br>".$stats;
	$temp = $player->processAward($award,$tsp);
	$message = $sc." points. ".$blurb[$award + 1].$temp;
	$players[trim($id)] = $player;
	writePlayerXML($players);
}		 
$dirArray = scandir("images");
array_splice($dirArray, 0, 2);
$puzzlePicks = 	randomSubset($dirArray, 36);
?>
<!doctype html>
<html>
<head>
	<title>Shanghai</title>
	<LINK REL="StyleSheet" HREF="../styles.css?<?php echo rand(); ?>"  TYPE="text/css" TITLE="ToMar Style" MEDIA="screen">
	
<script>
	function addme()
	{
		alert("The people in this puzzle are special to me. If you like to play this puzzle, then you too are special to me, and I'd love to include you! Send an email to tomargames@gmail.com and attach your picture, with a subject line: Add [your name] to Shanghai. It's that simple!");
	}	
</script>		
</head>
<body>
<table border=0 align="center"><tr>	
	<td width="10%"><img src="SHG.jpg" height="100" width="100"></td>
	<td  width="80%" class="biggest">Shanghai<br><span class="magenta10">by ToMarGames</span>&nbsp; &nbsp; &nbsp;<input type=button onclick="javascript:addme();" value="Add me!"><br></td>
	<td width="10%"><img src="SHG.jpg" height="100" width="100"></td></tr>
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
<td width="10%">&nbsp; </td>		
<td>		
<div id="app"> 
<applet name="applet" code="SHG.class" width="1200" height="650">
<?php 
	echo "<param name='id' value='".$id."'>";
	echo "<param name='nm' value='".$nm."'>";
	echo "<param name='site' value='".$site."'>";
	$iCnt = 0;
	foreach ($puzzlePicks as $p)
	{
		echo "<param value='".$p."' name='data".formatNumber(++$iCnt,2)."'>";
	}
	if ("NONE" != $message)
	{
		echo "<param name='message' value='".$message."'>";
	}	
?>
</applet>
</div>
</td></tr></table>
	<br>About the Game
	<ul class=green10>
		<li>There are 4 copies each of 36 different tiles.</li>
		<li>Remove matching pairs of tiles by clicking on tiles that are free.</li>
		<li>Tiles are free if they can slide to the right or the left.</li>
		<li>Light blue tiles are only one deep.</li>
		<li>Gold tiles are two deep.</li>
		<li>Dark green tiles are three deep.</li>
		<li>Red tiles are four deep.</li>
		<li>Most puzzles can be played down to zero tiles.</li>
		<li>Hints don't cost you anything, so you can use them to get started.</li>
		<li>Rather than a high score list, ToMarGames uses a ranking system based on all scores for that game.</li>
		<li>You move up through the ranks by earning stars.</li>
		<li>If you solve the puzzle, you earn a star.</li>
		<li>If you solve it in 72 moves, you earn two stars.</li>
	</ul>
</body>
</html>
