<?php
require 'Player.php';
require 'Game.php';
require 'tmUtils.php';
// Step1: verify login
	$id = $_GET["id"];
	$site = "http://www.tomargames.com/ToMar2012/";
	if ($id == null)
	{
		echo "<script> window.location = '".$site."Scoring/login.php?login'; </script>";
	}
// Step2: read player file and identify player
	$nm = $_GET["nm"];
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
	echo "Score: ".$sc."<br>";
	if (!(is_finite($sc)))
	{
?>
		<html>
			<form action="http://www.tomargames.com/ToMar2012/Scoring/">
				Score:
				<input type="text" name="score">
				<input type="hidden" name="nm" value=<?php echo $nm; ?> >
				<input type="hidden" name="id" value=<?php echo $id; ?> >
				<input type="submit" value="Submit">
			</form>	
	</html>
<?php 		
	}	
	else	
	{
		echo $sc." is a number.<br>";
		$award = CalculateAward($sc, $games);
		global $blurb;
		$stats = "Your Score: ".$sc."<br>Award: ".$award."<br>".$stats;
		debug("Before");
	  debug("Games: ".$player->getGames());
	  debug("Level: ".$player->getLevel());
	  debug("Stars: ".$player->getStars());
		$message = $sc." points. ".$blurb[$award + 1].$player->processAward($award);
		$players[trim($id)] = $player;
		debug("After");
		debug($message);
	  debug("Games: ".$player->getGames());
	  debug("Level: ".$player->getLevel());
	  debug("Stars: ".$player->getStars());
		writePlayerXML($players);
	}
	debug($stats);		 
?>	
