<?php
	function readxml() {
		$r = new DOMDocument();
		$r->load('php://input');
		return $r;
	}
	
	function getChildrenByTag($e, $t) {
		$r = [];
		foreach ($e->childNodes as $c) {
			if (!is_a($c, 'DOMElement')) continue;
			if ($c->tagName != $t) continue;
			
			$r[] = $c;
		}
		return $r;
	}
