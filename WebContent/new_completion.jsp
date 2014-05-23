<%@ include file="templates/header.jsp"%>

<%@ include file="templates/navbar.jsp"%>


<div class="container">

	<div class="large-content" style="height: 100%">
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h3 style="color: black" class="padding header white">
					New <span style="color: #2ECC71">Completion</span> Habit
				</h3>
			</div>
		</div>
		
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<h4 style="margin-top: 0; color: black" class="padding header white">Simple and rewarding. Studies suggest that humans
					habituate a task after doing it for <span style="color:#E74C3C">30 days</span> straight, and that the
					hardest part is always the first day. However, you are free to
					adjust the max number of days according to the needs of your
					particular habit.</h4>
			</div>
		</div>

		<div class="row" style="margin-bottom: 0">
			<div class="col-lg-6 col-lg-offset-3">
				<h4 style="margin-top: 0; color: black" class="padding header white">
					I, <span style="color: #3498DB">${realname},</span> commit to
					forming this habit.
				</h4>
				<div class="clearfix padding white">
					<form id="login" action="DailyCompletion" method="get">
						<input required
							class="form-control input-lg styled-input margin-bottom"
							type="text" name="habit"
							placeholder="This is the habit I will form."> <input
							required class="form-control input-lg styled-input margin-bottom"
							type="number" min="1" name="days"
							placeholder="Number of days to form.">
						<input type="submit" style="position: absolute; left: -9999px" />
					</form>
				</div>

			</div>
		</div>
		<div class="row">
			<div class="col-lg-6 col-lg-offset-3">
				<div class="text-center feature">
					<a href="#" onclick="document.getElementById('login').submit()"></a>
					<h3 class="padding emerald" style="margin-top: 0">Add to Habits</h3>
				</div>
			</div>
		</div>







	</div>

</div>

</body>

</html>