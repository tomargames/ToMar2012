<?php
require 'Player.php';
require 'Game.php';
require 'tmUtils.php';
$id = $_GET["id"];
$site = "http://www.tomargames.com/ToMar2012/";
$id = "test";
if ($id == null)
{
	echo "<script> window.location = '".$site."SDF/login.php?login'; </script>";
}
$nm = $_GET["nm"];
$sc = $_GET["score"];
$message = "NONE";
$player = "";
$stats = "----------------------";
$players = PlayerFromXML();
if (array_key_exists($id,$players))
{
	$player = $players[$id];
	$player->setName($nm);
}		
else
{
	$player = new Player();
	$player->setID($id);
	$player->setName($nm);
	$player->setGames(0);
	$player->setLevel(1);
	$player->setStars(4);
	$players[$id] = $player;
}
$sc=700;
if ($sc != null)
{
	$games = GameFromXML();
	$m = MeanScore($games);
	$std = StandardDeviationFromMean($games, $m);
	$award = CalculateAward($sc, $std, $m);
	$h = HighestScore($games);
	$blurb = Array("You lost a star.", "You broke even.", "You earned a star!", "Double stars!", "Triple stars!");
	$stats = "Your Score: ".$sc."<br>Mean: ".$m."<br>StdDev: ".$std."<br>Award: ".$award."<br>Highest: ".$h;
	$message = $sc." points. ".$blurb[$award + 1];
	if (array_key_exists($sc, $games))
	{
		$score = $games[$sc];
		$score->setCount($score->getCount() + 1);
	}
	else
	{
		$score = new Game();
		$score->setScore($sc);
		$score->setCount(1);
	}		
	$games[$sc] = $score;
//	writeGameXML($games);
	$player->setGames($player->getGames() + 1);
	$stars = $player->getStars() + $award;
	echo "Score is ".$sc;
	echo " Stars was ".$player->getStars();
	echo " Level was ".$player->getLevel();
	echo " Award is ".$award;
	echo " Stars is ".$stars;
	echo " Level is ".$player->getLevel();
	if ($stars > 4)
	{
		echo "ranking up.";
		$player->setLevel($player->getLevel() + 1);
//	$player->setStars($stars - 3);
		$player->setStars(2);
		$message = $message." Ranking up to ".$player->getLevel()."!";
	}
	elseif ($stars < 0)
	{
		if ($player->getLevel() < 2)
		{
			$player->setStars(0);
			$message = $message." Keep trying! ";
		}
		else
		{	
			$player->setLevel($player->getLevel() - 1);
			$player->setStars(2);
			$message = $message." Ranking down to ".$player->getLevel().".";
		}	
	}
	else
	{
		$player->setStars($stars);
	}
	$players[trim($id)] = $player;
//	writePlayerXML($players);
}
?>
<!doctype html>
<html>
<head>
	<title>Same Difference</title>
	<LINK REL="StyleSheet" HREF="../styles.css?<?php echo rand(); ?>"  TYPE="text/css" TITLE="ToMar Style" MEDIA="screen">
</head>
<body>
<table border=0 align="center"><tr>	
	<td width="10%"><img src="SDF.jpg" height="100" width="100"></td>
	<td  width="80%" class="biggest">Same Difference<br><span class="magenta10">by ToMarGames</span><br></td>
	<td width="10%"><img src="SDF.jpg" height="100" width="100"></td></tr>
</table>
<table border="0" align="center"><tr>
<td width="30%" align="left">
<table border=2>
<?php 
	$p = $players[$id];
	$pr = "";
	for ($i = 0; $i < $p->getStars(); $i++)
	{
		$pr = $pr."* ";
	}		
	echo "<tr><td class=green12>Player</td><td class=big>".$nm."</td></tr>";		
	echo "<tr><td class=green12>Rank</td><td class=big>".$p->getLevel()."</td></tr>";		
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
<iframe src="//www.facebook.com/plugins/like.php?href=http%3A%2F%2Fwww.tomargames.com%2FToMar2012%2FSDF%2F&amp;send=false&amp;layout=standard&amp;width=450&amp;show_faces=true&amp;action=like&amp;colorscheme=light&amp;font&amp;height=80&amp;appId=173037439390060" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:250px; height:80px;" allowTransparency="true"></iframe>
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
<td width="5%">&nbsp; </td>		
<td>		
<div id="app"> 
<applet name="applet" code="SDF.class" width="1200" height="500">
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
<ul valign="top">
	<li class="text10">Find sets of 3 cards where each characteristic is the same across all 3 cards,
		or different on each card.</li>
	<li class="text10">For example, if one of the cards is black, the other two must be black, or the
		other two must be red and blue.</li>
	<li class="text10">The same holds true for shape, shading, and number. If two of the selected
		cards match on a characteristic, then the third card must match as well, or
		all 3 must be different for that characteristic.</li>
	<li class="text10">Hints cost 10 points each.</li>
	<li class="text10">Sorts cost 5 points each.</li>
	<li class="text10">Do as much as you can in three minutes.</li>
	<li class="text10">If more or less than 12 cards are showing, a set will be worth more points.</li>
	<li class="text10">Sets also become worth more points if more than one characteristic is different.</li>
		<li>Rather than a high score list, ToMarGames uses a ranking system based on all scores for this game.</li>
		<li>You move up through the ranks by earning stars.</li>
		<li>Each game, your score will be measured against the mean score of all games played so far.</li>
		<li>To earn a star, you must score higher than the mean.</li>
		<li>If you score more than the mean plus standard deviation, you can earn multiple stars.</li>
		<li>If you score less than the mean minus standard deviation, you will lose a star.</li>
		<li>When you reach 5 stars, you will advance to the next rank, and come into the rank with two stars.</li>
		<li>If you have no stars, and you lose a star, you will descend to the rank below, and come in with two stars.</li>
</ul>
	<table width="100%" valign="top">
	<tr valign="top">
	<td class="darkred8" width="25%">
		<b>Colors</b><br>
			Red<br>
			Blue<br>
			Black
	</td>
	<td class="darkred8" width="25%">
		<b>Shapes</b><br>
			Circles<br>
			Squares<br>
			Triangles
	</td>
	<td class="darkred8" width="25%">
		<b>Shading</b><br>
			Solid<br>
			Empty<br>
			Dot in the middle
	</td>
	<td class="darkred8" width="25%">
		<b>Number of shapes</b><br>
			1<br>
			2<br>
			3
	</td>
	</tr>

</table>
</body>
</html>
