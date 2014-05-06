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
	echo "<script> window.location = '".$site."WDM/login.php?login'; </script>";
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
	<title>WordMania!</title>
	<LINK REL="StyleSheet" HREF="../styles.css?<?php echo rand(); ?>"  TYPE="text/css" TITLE="ToMar Style" MEDIA="screen">
	<script>
		addWord = window.open('','dummy','width=100,height=100');
		addWord.blur();
	</script>	
</head>
<body>
<table border=0 align="center"><tr>	
	<td width="10%"><img src="WDM.jpg" height="100" width="100"></td>
	<td  width="80%" class="biggest">WordMania!<br><span class="magenta10">by ToMarGames</span><br></td>
	<td width="10%"><img src="WDM.jpg" height="100" width="100"></td></tr>
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
<iframe src="//www.facebook.com/plugins/like.php?href=http%3A%2F%2Fwww.tomargames.com%2FToMar2012%2FWDM%2F&amp;send=false&amp;layout=standard&amp;width=250&amp;show_faces=true&amp;action=like&amp;colorscheme=light&amp;font&amp;height=80&amp;appId=4648147979" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:200px; height:80px;" allowTransparency="true"></iframe>
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
<applet name="applet" code="WDM.class" width="800" height="600">
<?php 
	echo "<param name='id' value='".$id."'>";
	echo "<param name='nm' value='".$nm."'>";
	echo "<param name='site' value='".$site."'>";
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
		<li>WordMania plays best using the <a href='https://www.google.com/chrome'>Chrome</a> browser.</li>
		<li>You have 3 minutes to find all the words you can.</li>
		<li>You can use the mouse, but the keyboard is much faster.</li>
		<li>The answer words are sorted by length, then alphabetically.</li>
		<li>All the 3-letter words appear first, beside a black *.</li>
		<li>Then the 4-letter words, beside a green *.</li>
		<li>Then the 5-letter words, beside a blue *.</li>
		<li>Then the 6-letter words, beside a red *.</li>
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

