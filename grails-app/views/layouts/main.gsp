<html>
	<head>
		<title><g:layoutTitle default="Collab Todo" />
		</title>
		<link rel="stylesheet"
			href="${createLinkTo(dir:'css',file:'main.css')}" />
		<link rel="shortcut icon"
			href="${createLinkTo(dir:'images',file:'favicon.ico')}"
			type="image/x-icon" />
		<g:layoutHead />
		<g:javascript library="application" />
        <feed:meta kind="atom" controller="rss" action="feed"/>
        
    </head>
	<body>
	<!-- 	 -->
		<div id="page">
			<div id="spinner" class="spinner" style="display: none;">
				<img src="${createLinkTo(dir:'images',file:'spinner.gif')}"
					alt="Spinner" />
			</div>

            <div id="topbar">
			    <g:render template="/common/topbar" />
			<div id="topbar">
			
			<div id="header">
               <h1>Collab-Todo</h1>
            </div>

			<div id="content">
				<g:layoutBody />
			</div>

			<div id="sidebar">
				<g:render template="/common/buddies" />
			</div>

			<div id="footer">
				<g:render template="/common/footer" />
			</div>
			<!-- 			 -->
		</div>
	</body>
</html>