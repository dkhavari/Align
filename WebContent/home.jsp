<%@ include file="templates/header.jsp"%>

<%@ include file="templates/navbar.jsp" %>

<div class="container">

	<div class="large-content" style="height: 100%">
		<div class="row">
			<div class="col-lg-5">
				<div class="text-left feature">
					<a href="goals.jsp"></a>
					<h1 class="padding header emerald">Goals</h1>
					<div class="text-left white content-box" style="height: 400px"></div>
				</div>
			</div>
			<div class="col-lg-5 col-lg-offset-2">
				<div class="text-left feature" onclick="document.getElementById('dailybtn').click(); return false;">
					<a href="#"></a>
					<h1 class="padding header alazarin">Habits</h1>
					<div class="text-left white content-box" style="height: 400px"></div>
				</div>
			</div>
		</div>
		<div class="row spacious">
			<div class="col-lg-5">
				<div class="text-left feature">
					<a href="sleep.jsp"></a>
					<h1 class="padding header clouds">Sleep</h1>
					<div class="text-left white content-box" style="height: 400px"></div>
				</div>
			</div>
			<div class="col-lg-5 col-lg-offset-2">
				<div class="text-left feature">
					<a href="logs.jsp"></a>
					<h1 class="padding header river">Logs</h1>
					<div class="text-left white content-box" style="height: 400px"></div>
				</div>
			</div>
		</div>
	</div>

</div>


<!-- Forms for submission when needed -->
<div style="display: none">
	<form action="RefreshDaily" method="get">
		<button id="dailybtn" type="submit"></button>
	</form>
</div>

</html>