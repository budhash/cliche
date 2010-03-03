<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="1.css" />
		<title>Cliche Command Line Shell</title>
	</head>
	<body>
    <table class="TopPanel" cellpadding="0" cellspacing="0" border="0">
        <tr>
            <td class="TopLeftCorner">&nbsp;</td>
            <td class="TopCenter"> Cliche Command-Line Shell </td>
            <td class="TopRightCorner">&nbsp;</td>
        </tr>
    </table>
    <div class="MainPanel">
	
<?php
    // this is VERY-VERY simple Markdown-To-Html script.
    require "markdown.php";
    if (!$_GET['f']) {
        $action='index';
    } else {
        $action=$_GET['f'];
    }
    $myFile = "down/$action.txt";
    $fh = fopen($myFile, 'r');
    if ($fh) {
        $theText = fread($fh, filesize($myFile));
        fclose($fh);
        echo Markdown($theText);
    } else {
        echo "Error: action <code>$action</code> undefined.";
    }
?>

		<p class="BottomLine"><a href="/">cliche home</a> | <a href="http://sourceforge.net/projects/cliche/">project page</a></p>
	</div>
	<a href="http://sourceforge.net"><img src="http://sflogo.sourceforge.net/sflogo.php?group_id=249929&amp;type=5" class="Banner" width="210" height="62" border="0" alt="SourceForge.net Logo" /></a>
	</body>
</html>