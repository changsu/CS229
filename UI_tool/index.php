<?php
	require_once ('header.php');
?>

<div class = "search_wrapper">
	<form action="handle_search.php" enctype="multipart/form-data" method="POST">
		<h4>Entity 1</h4>
		<input type = "text" id = "e1" name = "e1" rows = "1" cols = "20"/>
		<h4>R (Verb) </h4>
		<input type = "text" id = "R" name = "R" rows = "1" cols = "20"/>
		<h4>Entity 2</h4>
		<input type = "text" id = "e2" name = "e2" rows = "1" cols = "20"/>
		</br></br>
		<input type="submit" name="submit" value="Search"/>
	</form>
</div>
<body>