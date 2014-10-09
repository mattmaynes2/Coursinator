<?php
	function readxml() {
		$xml = file_get_contents('php://input');
		return DOMDocument::loadXML($xml);
	}
