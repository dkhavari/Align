<%@ include file="templates/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- Hidden form to refresh the page when necessary -->
<form id="refresh" action="RefreshDaily" method="get"
	style="display: none"></form>

<!-- Refresh data as necessary -->
<c:if test="${refresh != null}">
	<script type="text/javascript">
		(function() {
			var getInfo = document.getElementById("refresh");
			getInfo.submit();
		})();
	</script>
</c:if>

<%@ include file="templates/navbar.jsp"%>

<div class="container">

	<div class="large-content">
		<div class="row">
			<div class="col-lg-10">
				<div class="text-left">
					<h1 class="padding header alazarin">Habits</h1>
				</div>
			</div>
		</div>

		<!-- The desired habit. -->
		<div class="row">
			<div class="col-lg-10">
				<form id="create" action="DailyHabits" method="get">
					<input style="margin: 0px"
						class="form-control input-lg styled-input margin-bottom"
						type="text" name="habit" id="habit"
						placeholder="This is the habit I want to form.">
				</form>
			</div>
		</div>

		<!-- The button to confirm that habit. -->
		<div class="row">
			<div class="col-lg-10">
				<div class="text-center feature">
					<a href="#" onclick="document.getElementById('create').submit()"></a>
					<h3 style="margin: 0px" class="padding emerald">Add to Habits</h3>
				</div>
			</div>
		</div>

		<!-- Habit header -->
		<div class="row">
			<div class="col-lg-10">
				<div class="text-left">
					<h1 style="margin-bottom: 0px" class="padding river">Today's
						Habituation Progress</h1>
				</div>
			</div>
		</div>

		<!-- The display of habits -->
		<c:if test="${habits != null}">
			<div class="row">
				<div class="col-lg-10">
					<form action="DailyHabits" method="post" id="record">
						<c:forEach items="${habits}" varStatus="loop">

							<h2 class="padding header paper"
								style="margin: 0px; color: black">${habits[loop.index]}</h2>
							<div class="padding white">

								<p>Current Streak: ${currentStreaks[loop.index]}</p>
								<div class="radius progress alert">
									<span class="meter"
										style="width:  calc(3% * ${currentStreaks[loop.index]})"></span>
								</div>
								<p>Best Streak: ${bestStreaks[loop.index]}</p>
								<div class="radius progress success">
									<span class="meter"
										style="width:  calc(3% * ${bestStreaks[loop.index]})"></span>
								</div>

								<p>
									<input type="checkbox" name="habit"
										value="${habits[loop.index]}" id="${loop.index}" /> I
									completed this habit today.
								</p>
							</div>
						</c:forEach>
					</form>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-10">
					<div class="text-center feature">
						<a href="#" onclick="document.getElementById('record').submit()"></a>
						<h3 style="margin: 0px" class="padding emerald">Submit daily
							habit log</h3>
					</div>
				</div>
			</div>
		</c:if>
	</div>

</div>

</html>