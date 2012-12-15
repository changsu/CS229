<?php
	require_once ('header.php');
	require_once ('find_relations.php');
	
	// get post parameters
	$e1 = $_POST['e1'];
	$R = $_POST['R'];
	$e2 = $_POST['e2'];
	
?>

<div class = "input">
	<p>You have entered <br>
		<b>E1</b> <?php echo $e1?> <br>
		<b>R</b> <?php echo $R?> <br>
		<b>E2</b> <?php echo $e2?>
	</p>
</div>

<div class = "result">
	<div class = "direct result">	
		<h2>Direct Result</h2>	
		<table border="1">
		<?php
			// issue query based on e1 get set of relaion records
			$records = find_relations($e1, $R, $e2, $conn);
			
			if (count($records) == 0) {
				echo "<h3>Sorry, no results found!</h3>";
				return;
			}
			
			foreach ($records as $value) {
				// get sentence
				$sql = "SELECT * from 229sentence where id = ". $value['sentenceid'];
				$result = mysql_query($sql, $conn);
				$records = convert_array($result);
				foreach ($records as $sentence_tuple) {
					$sentence = $sentence_tuple['sentence'];
					$urlid = $sentence_tuple['urlid'];
				}

				// get url
				$sql = "SELECT url from 229url where id = ". $urlid;
				$result = mysql_query($sql, $conn);
				$records = convert_array($result);
				foreach ($records as $url_tuple) {
					$url = $url_tuple['url'];
				}
				echo "<tr><td><a href=" . $url. " target = '_blank'>". 
					htmlentities($sentence, ENT_QUOTES | ENT_IGNORE, "UTF-8") ."</a></td></tr>";
			}
						
		?>
		</table>
		<!-- Highlight e1 | e2 | R -->
		<script>
			highlightWord(document.body,"<?php echo $e1 ?>", 'e1');
			highlightWord(document.body,"<?php echo $R ?>", 'R');
			highlightWord(document.body,"<?php echo $e2 ?>", 'e2');
			<?php
				$e1 = ucwords($e1); $e2 = ucwords($e2);
			?>
			highlightWord(document.body,"<?php echo $e1 ?>", 'e1');
			highlightWord(document.body,"<?php echo $e2 ?>", 'e2');
		</script>
	</div>
	<div class = "indirect result">
		<h2>Indirect Result(Under Construction)</h2>
	</div>
</div>

</body>


