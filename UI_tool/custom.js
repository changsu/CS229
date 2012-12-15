function highlightWord(root,word, colorType){
  if (word == null || word == undefined || word == "")
	return;
	
  alert(colorType);
  var highlightClass = 'highlighted_e1'; // by default
	if (colorType == 'e1') {
		highlightClass = 'highlighted_e1';
	} else if (colorType == 'e2') {
		highlightClass = 'highlighted_e2';
	} else if (colorTye == 'R') {
	    highlightClass = 'highlighted_R';
    }
  textNodesUnder(root).forEach(highlightWords);
  function textNodesUnder(root){
    var walk=document.createTreeWalker(root,NodeFilter.SHOW_TEXT,null,false),
        text=[], node;
    while(node=walk.nextNode()) text.push(node);
    return text;
  }

  function highlightWords(n){
    for (var i; (i=n.nodeValue.indexOf(word,i)) > -1; n=after){
      var after = n.splitText(i+word.length);
      var highlighted = n.splitText(i);
      var span = document.createElement('span');
      span.className = highlightClass;
      span.appendChild(highlighted);
      after.parentNode.insertBefore(span,after);
	  alert("come to this");
    }
  }
}